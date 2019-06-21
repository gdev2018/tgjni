package com.github.gdev2018.master.tgnet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONObject;
import com.github.gdev2018.master.AndroidUtilities;
import com.github.gdev2018.master.BuildConfig;
import com.github.gdev2018.master.BaseBuildVars;
import com.github.gdev2018.master.ContactsController;
import com.github.gdev2018.master.di.BaseApplication;
import com.github.gdev2018.master.FileLog;
import com.github.gdev2018.master.KeepAliveJob;
import com.github.gdev2018.master.LocaleController;
import com.github.gdev2018.master.MessagesController;
import com.github.gdev2018.master.NotificationCenter;
import com.github.gdev2018.master.StatsController;
import com.github.gdev2018.master.UserConfig;
import com.github.gdev2018.master.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsManager {

    public final static int ConnectionTypeGeneric = 1;
    public final static int ConnectionTypeDownload = 2;
    public final static int ConnectionTypeUpload = 4;
    public final static int ConnectionTypePush = 8;
    public final static int ConnectionTypeDownload2 = ConnectionTypeDownload | (1 << 16);

    public final static int FileTypePhoto = 0x01000000;
    public final static int FileTypeVideo = 0x02000000;
    public final static int FileTypeAudio = 0x03000000;
    public final static int FileTypeFile = 0x04000000;

    public final static int RequestFlagEnableUnauthorized = 1;
    public final static int RequestFlagFailOnServerErrors = 2;
    public final static int RequestFlagCanCompress = 4;
    public final static int RequestFlagWithoutLogin = 8;
    public final static int RequestFlagTryDifferentDc = 16;
    public final static int RequestFlagForceDownload = 32;
    public final static int RequestFlagInvokeAfter = 64;
    public final static int RequestFlagNeedQuickAck = 128;

    public final static int ConnectionStateConnecting = 1;
    public final static int ConnectionStateWaitingForNetwork = 2;
    public final static int ConnectionStateConnected = 3;
    public final static int ConnectionStateConnectingToProxy = 4;
    public final static int ConnectionStateUpdating = 5;

    private static long lastDnsRequestTime;

    public final static int DEFAULT_DATACENTER_ID = Integer.MAX_VALUE;

    private long lastPauseTime = System.currentTimeMillis();
    private boolean appPaused = true;
    private boolean isUpdating;
    private int connectionState;
    private AtomicInteger lastRequestToken = new AtomicInteger(1);
    private int appResumeCount;

    private static AsyncTask currentTask;

    private static class ResolvedDomain {

        public ArrayList<String> addresses;
        long ttl;

        public ResolvedDomain(ArrayList<String> a, long t) {
            addresses = a;
            ttl = t;
        }

        public String getAddress() {
            return addresses.get(Utilities.random.nextInt(addresses.size()));
        }
    }

    private static ThreadLocal<HashMap<String, ResolvedDomain>> dnsCache = new ThreadLocal<HashMap<String, ResolvedDomain>>() {
        @Override
        protected HashMap<String, ResolvedDomain> initialValue() {
            return new HashMap<>();
        }
    };

    private static int lastClassGuid = 1;

    private int currentAccount;
    private static volatile ConnectionsManager[] Instance = new ConnectionsManager[UserConfig.MAX_ACCOUNT_COUNT];
    public static ConnectionsManager getInstance(int num) {
        ConnectionsManager localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (ConnectionsManager.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    Instance[num] = localInstance = new ConnectionsManager(num);
                }
            }
        }
        return localInstance;
    }

    public ConnectionsManager(int instance) {
        currentAccount = instance;
        connectionState = native_getConnectionState(currentAccount);
        String deviceModel;
        String systemLangCode;
        String langCode;
        String appVersion;
        String systemVersion;
        File config = BaseApplication.getFilesDirFixed();
        if (instance != 0) {
            config = new File(config, "account" + instance);
            config.mkdirs();
        }
        String configPath = config.toString();
        SharedPreferences preferences = MessagesController.getGlobalNotificationsSettings();
        boolean enablePushConnection = preferences.getBoolean("pushConnection", true);
        try {
            systemLangCode = LocaleController.getSystemLocaleStringIso639().toLowerCase();
            langCode = LocaleController.getLocaleStringIso639().toLowerCase();
            deviceModel = Build.MANUFACTURER + Build.MODEL;
            PackageInfo pInfo = BaseApplication.mApplicationContext.getPackageManager().getPackageInfo(BaseApplication.mApplicationContext.getPackageName(), 0);
            appVersion = pInfo.versionName + " (" + pInfo.versionCode + ")";
            systemVersion = "SDK " + Build.VERSION.SDK_INT;
        } catch (Exception e) {
            systemLangCode = "en";
            langCode = "";
            deviceModel = "Android unknown";
            appVersion = "App version unknown";
            systemVersion = "SDK " + Build.VERSION.SDK_INT;
        }
        if (systemLangCode.trim().length() == 0) {
            systemLangCode = "en";
        }
        if (deviceModel.trim().length() == 0) {
            deviceModel = "Android unknown";
        }
        if (appVersion.trim().length() == 0) {
            appVersion = "App version unknown";
        }
        if (systemVersion.trim().length() == 0) {
            systemVersion = "SDK Unknown";
        }
        UserConfig.getInstance(currentAccount).loadConfig();
        init(BaseBuildVars.BUILD_VERSION, TLRPC.LAYER, BaseBuildVars.APP_ID, deviceModel, systemVersion, appVersion, langCode, systemLangCode, configPath, FileLog.getNetworkLogPath(), UserConfig.getInstance(currentAccount).getClientUserId(), enablePushConnection);
    }

    public long getCurrentTimeMillis() {
        return native_getCurrentTimeMillis(currentAccount);
    }

    public int getCurrentTime() {
        return native_getCurrentTime(currentAccount);
    }

    public int getTimeDifference() {
        return native_getTimeDifference(currentAccount);
    }

    public int sendRequest(TLObject object, RequestDelegate completionBlock) {
        return sendRequest(object, completionBlock, null, 0);
    }

    public int sendRequest(TLObject object, RequestDelegate completionBlock, int flags) {
        return sendRequest(object, completionBlock, null, null, flags, DEFAULT_DATACENTER_ID, ConnectionTypeGeneric, true);
    }

    public int sendRequest(TLObject object, RequestDelegate completionBlock, int flags, int connetionType) {
        return sendRequest(object, completionBlock, null, null, flags, DEFAULT_DATACENTER_ID, connetionType, true);
    }

    public int sendRequest(TLObject object, RequestDelegate completionBlock, QuickAckDelegate quickAckBlock, int flags) {
        return sendRequest(object, completionBlock, quickAckBlock, null, flags, DEFAULT_DATACENTER_ID, ConnectionTypeGeneric, true);
    }

    public int sendRequest(final TLObject object, final RequestDelegate onComplete, final QuickAckDelegate onQuickAck, final WriteToSocketDelegate onWriteToSocket, final int flags, final int datacenterId, final int connetionType, final boolean immediate) {
        final int requestToken = lastRequestToken.getAndIncrement();
        Utilities.stageQueue.postRunnable(() -> {
            if (BaseBuildVars.LOGS_ENABLED) {
                FileLog.d("send request " + object + " with token = " + requestToken);
            }
            try {
                NativeByteBuffer buffer = new NativeByteBuffer(object.getObjectSize());
                object.serializeToStream(buffer);
                object.freeResources();

                native_sendRequest(currentAccount, buffer.address, (response, errorCode, errorText, networkType) -> {
                    try {
                        TLObject resp = null;
                        TLRPC.TL_error error = null;
                        if (response != 0) {
                            NativeByteBuffer buff = NativeByteBuffer.wrap(response);
                            buff.reused = true;
                            resp = object.deserializeResponse(buff, buff.readInt32(true), true);
                        } else if (errorText != null) {
                            error = new TLRPC.TL_error();
                            error.code = errorCode;
                            error.text = errorText;
                            if (BaseBuildVars.LOGS_ENABLED) {
                                FileLog.e(object + " got error " + error.code + " " + error.text);
                            }
                        }
                        if (resp != null) {
                            resp.networkType = networkType;
                        }
                        if (BaseBuildVars.LOGS_ENABLED) {
                            FileLog.d("java received " + resp + " error = " + error);
                        }
                        final TLObject finalResponse = resp;
                        final TLRPC.TL_error finalError = error;
                        Utilities.stageQueue.postRunnable(() -> {
                            onComplete.run(finalResponse, finalError);
                            if (finalResponse != null) {
                                finalResponse.freeResources();
                            }
                        });
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }, onQuickAck, onWriteToSocket, flags, datacenterId, connetionType, immediate, requestToken);
            } catch (Exception e) {
                FileLog.e(e);
            }
        });
        return requestToken;
    }

    public void cancelRequest(int token, boolean notifyServer) {
        native_cancelRequest(currentAccount, token, notifyServer);
    }

    public void cleanup(boolean resetKeys) {
        native_cleanUp(currentAccount, resetKeys);
    }

    public void cancelRequestsForGuid(int guid) {
        native_cancelRequestsForGuid(currentAccount, guid);
    }

    public void bindRequestToGuid(int requestToken, int guid) {
        native_bindRequestToGuid(currentAccount, requestToken, guid);
    }

    public void applyDatacenterAddress(int datacenterId, String ipAddress, int port) {
        native_applyDatacenterAddress(currentAccount, datacenterId, ipAddress, port);
    }

    public int getConnectionState() {
        if (connectionState == ConnectionStateConnected && isUpdating) {
            return ConnectionStateUpdating;
        }
        return connectionState;
    }

    public void setUserId(int id) {
        native_setUserId(currentAccount, id);
    }

    public void checkConnection() {
        native_setUseIpv6(currentAccount, useIpv6Address());
        native_setNetworkAvailable(currentAccount, BaseApplication.isNetworkOnline(), BaseApplication.getCurrentNetworkType(), BaseApplication.isConnectionSlow());
    }

    public void setPushConnectionEnabled(boolean value) {
        native_setPushConnectionEnabled(currentAccount, value);
    }

    public void init(int version, int layer, int apiId, String deviceModel, String systemVersion, String appVersion, String langCode, String systemLangCode, String configPath, String logPath, int userId, boolean enablePushConnection) {
        SharedPreferences preferences = BaseApplication.mApplicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
        String proxyAddress = preferences.getString("proxy_ip", "");
        String proxyUsername = preferences.getString("proxy_user", "");
        String proxyPassword = preferences.getString("proxy_pass", "");
        String proxySecret = preferences.getString("proxy_secret", "");
        int proxyPort = preferences.getInt("proxy_port", 1080);
        if (preferences.getBoolean("proxy_enabled", false) && !TextUtils.isEmpty(proxyAddress)) {
            native_setProxySettings(currentAccount, proxyAddress, proxyPort, proxyUsername, proxyPassword, proxySecret);
        }

        native_init(currentAccount, version, layer, apiId, deviceModel, systemVersion, appVersion, langCode, systemLangCode, configPath, logPath, userId, enablePushConnection, BaseApplication.isNetworkOnline(), BaseApplication.getCurrentNetworkType());
        checkConnection();
    }

    public static void setLangCode(String langCode) {
        langCode = langCode.replace('_', '-').toLowerCase();
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            native_setLangCode(a, langCode);
        }
    }

    public static void setSystemLangCode(String langCode) {
        langCode = langCode.replace('_', '-').toLowerCase();
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            native_setSystemLangCode(a, langCode);
        }
    }

    public void switchBackend() {
        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
        preferences.edit().remove("language_showed2").commit();
        native_switchBackend(currentAccount);
    }

    public void resumeNetworkMaybe() {
        native_resumeNetwork(currentAccount, true);
    }

    public void updateDcSettings() {
        native_updateDcSettings(currentAccount);
    }

    public long getPauseTime() {
        return lastPauseTime;
    }

    public long checkProxy(String address, int port, String username, String password, String secret, RequestTimeDelegate requestTimeDelegate) {
        if (TextUtils.isEmpty(address)) {
            return 0;
        }
        if (address == null) {
            address = "";
        }
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        if (secret == null) {
            secret = "";
        }
        return native_checkProxy(currentAccount, address, port, username, password, secret, requestTimeDelegate);
    }

    public void setAppPaused(final boolean value, final boolean byScreenState) {
        if (!byScreenState) {
            appPaused = value;
            if (BaseBuildVars.LOGS_ENABLED) {
                FileLog.d("app paused = " + value);
            }
            if (value) {
                appResumeCount--;
            } else {
                appResumeCount++;
            }
            if (BaseBuildVars.LOGS_ENABLED) {
                FileLog.d("app resume count " + appResumeCount);
            }
            if (appResumeCount < 0) {
                appResumeCount = 0;
            }
        }
        if (appResumeCount == 0) {
            if (lastPauseTime == 0) {
                lastPauseTime = System.currentTimeMillis();
            }
            native_pauseNetwork(currentAccount);
        } else {
            if (appPaused) {
                return;
            }
            if (BaseBuildVars.LOGS_ENABLED) {
                FileLog.d("reset app pause time");
            }
            if (lastPauseTime != 0 && System.currentTimeMillis() - lastPauseTime > 5000) {
                ContactsController.getInstance(currentAccount).checkContacts();
            }
            lastPauseTime = 0;
            native_resumeNetwork(currentAccount, false);
        }
    }

    public static void onUnparsedMessageReceived(long address, final int currentAccount) {
        try {
            NativeByteBuffer buff = NativeByteBuffer.wrap(address);
            buff.reused = true;
            int constructor = buff.readInt32(true);
            final TLObject message = TLClassStore.Instance().TLdeserialize(buff, constructor, true);
            if (message instanceof TLRPC.Updates) {
                if (BaseBuildVars.LOGS_ENABLED) {
                    FileLog.d("java received " + message);
                }
                KeepAliveJob.finishJob();
                Utilities.stageQueue.postRunnable(() -> MessagesController.getInstance(currentAccount).processUpdates((TLRPC.Updates) message, false));
            } else {
                if (BaseBuildVars.LOGS_ENABLED) {
                    FileLog.d(String.format("java received unknown constructor 0x%x", constructor));
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void onUpdate(final int currentAccount) {
        Utilities.stageQueue.postRunnable(() -> MessagesController.getInstance(currentAccount).updateTimerProc());
    }

    public static void onSessionCreated(final int currentAccount) {
        Utilities.stageQueue.postRunnable(() -> MessagesController.getInstance(currentAccount).getDifference());
    }

    public static void onConnectionStateChanged(final int state, final int currentAccount) {
        AndroidUtilities.runOnUIThread(() -> {
            getInstance(currentAccount).connectionState = state;
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.didUpdateConnectionState);
        });
    }

    public static void onLogout(final int currentAccount) {
        AndroidUtilities.runOnUIThread(() -> {
            if (UserConfig.getInstance(currentAccount).getClientUserId() != 0) {
                UserConfig.getInstance(currentAccount).clearConfig();
                MessagesController.getInstance(currentAccount).performLogout(0);
            }
        });
    }

    public static int getInitFlags() {
        return 0;
    }

    public static void onBytesSent(int amount, int networkType, final int currentAccount) {
        try {
            StatsController.getInstance(currentAccount).incrementSentBytesCount(networkType, StatsController.TYPE_TOTAL, amount);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void onRequestNewServerIpAndPort(final int second, final int currentAccount) {
        Utilities.stageQueue.postRunnable(() -> {
            if (currentTask != null || second == 0 && Math.abs(lastDnsRequestTime - System.currentTimeMillis()) < 10000 || !BaseApplication.isNetworkOnline()) {
                if (BaseBuildVars.LOGS_ENABLED) {
                    FileLog.d("don't start task, current task = " + currentTask + " next task = " + second + " time diff = " + Math.abs(lastDnsRequestTime - System.currentTimeMillis()) + " network = " + BaseApplication.isNetworkOnline());
                }
                return;
            }
            lastDnsRequestTime = System.currentTimeMillis();
            if (second == 2) {
                if (BaseBuildVars.LOGS_ENABLED) {
                    FileLog.d("start azure dns task");
                }
                AzureLoadTask task = new AzureLoadTask(currentAccount);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                currentTask = task;
            } else if (second == 1) {
                if (BaseBuildVars.LOGS_ENABLED) {
                    FileLog.d("start dns txt task");
                }
                DnsTxtLoadTask task = new DnsTxtLoadTask(currentAccount);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                currentTask = task;
            } else {
                if (BaseBuildVars.LOGS_ENABLED) {
                    FileLog.d("start firebase task");
                }
                FirebaseTask task = new FirebaseTask(currentAccount);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                currentTask = task;
            }
        });
    }

    public static void onProxyError() {
        AndroidUtilities.runOnUIThread(() -> NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needShowAlert, 3));
    }

    public static String getHostByName(String domain, final int currentAccount) {
        HashMap<String, ResolvedDomain> cache = dnsCache.get();
        ResolvedDomain resolvedDomain = cache.get(domain);
        if (resolvedDomain != null && SystemClock.elapsedRealtime() - resolvedDomain.ttl < 5 * 60 * 1000) {
            return resolvedDomain.getAddress();
        }

        ByteArrayOutputStream outbuf = null;
        InputStream httpConnectionStream = null;
        try {
            URL downloadUrl = new URL("https://www.google.com/resolve?name=" + domain + "&type=A");
            URLConnection httpConnection = downloadUrl.openConnection();
            httpConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
            httpConnection.addRequestProperty("Host", "dns.google.com");
            httpConnection.setConnectTimeout(1000);
            httpConnection.setReadTimeout(2000);
            httpConnection.connect();
            httpConnectionStream = httpConnection.getInputStream();

            outbuf = new ByteArrayOutputStream();

            byte[] data = new byte[1024 * 32];
            while (true) {
                int read = httpConnectionStream.read(data);
                if (read > 0) {
                    outbuf.write(data, 0, read);
                } else if (read == -1) {
                    break;
                } else {
                    break;
                }
            }

            JSONObject jsonObject = new JSONObject(new String(outbuf.toByteArray()));
            JSONArray array = jsonObject.getJSONArray("Answer");
            int len = array.length();
            if (len > 0) {
                ArrayList<String> addresses = new ArrayList<>(len);
                for (int a = 0; a < len; a++) {
                    addresses.add(array.getJSONObject(a).getString("data"));
                }
                ResolvedDomain newResolvedDomain = new ResolvedDomain(addresses, SystemClock.elapsedRealtime());
                cache.put(domain, newResolvedDomain);
                return newResolvedDomain.getAddress();
            }
        } catch (Throwable e) {
            FileLog.e(e);
        } finally {
            try {
                if (httpConnectionStream != null) {
                    httpConnectionStream.close();
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            try {
                if (outbuf != null) {
                    outbuf.close();
                }
            } catch (Exception ignore) {

            }
        }
        return "";
    }

    public static void onBytesReceived(int amount, int networkType, final int currentAccount) {
        try {
            StatsController.getInstance(currentAccount).incrementReceivedBytesCount(networkType, StatsController.TYPE_TOTAL, amount);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void onUpdateConfig(long address, final int currentAccount) {
        try {
            NativeByteBuffer buff = NativeByteBuffer.wrap(address);
            buff.reused = true;
            final TLRPC.TL_config message = TLRPC.TL_config.TLdeserialize(buff, buff.readInt32(true), true);
            if (message != null) {
                Utilities.stageQueue.postRunnable(() -> MessagesController.getInstance(currentAccount).updateConfig(message));
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void onInternalPushReceived(final int currentAccount) {
        KeepAliveJob.startJob();
    }

    public static void setProxySettings(boolean enabled, String address, int port, String username, String password, String secret) {
        if (address == null) {
            address = "";
        }
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        if (secret == null) {
            secret = "";
        }
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            if (enabled && !TextUtils.isEmpty(address)) {
                native_setProxySettings(a, address, port, username, password, secret);
            } else {
                native_setProxySettings(a, "", 1080, "", "", "");
            }
            if (UserConfig.getInstance(a).isClientActivated()) {
                MessagesController.getInstance(a).checkProxyInfo(true);
            }
        }
    }

    public static native void native_switchBackend(int currentAccount);
    public static native int native_isTestBackend(int currentAccount);
    public static native void native_pauseNetwork(int currentAccount);
    public static native void native_setUseIpv6(int currentAccount, boolean value);
    public static native void native_updateDcSettings(int currentAccount);
    public static native void native_setNetworkAvailable(int currentAccount, boolean value, int networkType, boolean slow);
    public static native void native_resumeNetwork(int currentAccount, boolean partial);
    public static native long native_getCurrentTimeMillis(int currentAccount);
    public static native int native_getCurrentTime(int currentAccount);
    public static native int native_getTimeDifference(int currentAccount);
    public static native void native_sendRequest(int currentAccount, long object, RequestDelegateInternal onComplete, QuickAckDelegate onQuickAck, WriteToSocketDelegate onWriteToSocket, int flags, int datacenterId, int connetionType, boolean immediate, int requestToken);
    public static native void native_cancelRequest(int currentAccount, int token, boolean notifyServer);
    public static native void native_cleanUp(int currentAccount, boolean resetKeys);
    public static native void native_cancelRequestsForGuid(int currentAccount, int guid);
    public static native void native_bindRequestToGuid(int currentAccount, int requestToken, int guid);
    public static native void native_applyDatacenterAddress(int currentAccount, int datacenterId, String ipAddress, int port);
    public static native int native_getConnectionState(int currentAccount);
    public static native void native_setUserId(int currentAccount, int id);
    public static native void native_init(int currentAccount, int version, int layer, int apiId, String deviceModel, String systemVersion, String appVersion, String langCode, String systemLangCode, String configPath, String logPath, int userId, boolean enablePushConnection, boolean hasNetwork, int networkType);
    public static native void native_setProxySettings(int currentAccount, String address, int port, String username, String password, String secret);
    public static native void native_setLangCode(int currentAccount, String langCode);
    public static native void native_setSystemLangCode(int currentAccount, String langCode);
    public static native void native_seSystemLangCode(int currentAccount, String langCode);
    public static native void native_setJava(boolean useJavaByteBuffers);
    public static native void native_setPushConnectionEnabled(int currentAccount, boolean value);
    public static native void native_applyDnsConfig(int currentAccount, long address, String phone);
    public static native long native_checkProxy(int currentAccount, String address, int port, String username, String password, String secret, RequestTimeDelegate requestTimeDelegate);

    public static int generateClassGuid() {
        return lastClassGuid++;
    }

    public void setIsUpdating(final boolean value) {
        AndroidUtilities.runOnUIThread(() -> {
            if (isUpdating == value) {
                return;
            }
            isUpdating = value;
            if (connectionState == ConnectionStateConnected) {
                NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.didUpdateConnectionState);
            }
        });
    }

    @SuppressLint("NewApi")
    protected static boolean useIpv6Address() {
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        if (BaseBuildVars.LOGS_ENABLED) {
            try {
                NetworkInterface networkInterface;
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    networkInterface = networkInterfaces.nextElement();
                    if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.getInterfaceAddresses().isEmpty()) {
                        continue;
                    }
                    if (BaseBuildVars.LOGS_ENABLED) {
                        FileLog.d("valid interface: " + networkInterface);
                    }
                    List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
                    for (int a = 0; a < interfaceAddresses.size(); a++) {
                        InterfaceAddress address = interfaceAddresses.get(a);
                        InetAddress inetAddress = address.getAddress();
                        if (BaseBuildVars.LOGS_ENABLED) {
                            FileLog.d("address: " + inetAddress.getHostAddress());
                        }
                        if (inetAddress.isLinkLocalAddress() || inetAddress.isLoopbackAddress() || inetAddress.isMulticastAddress()) {
                            continue;
                        }
                        if (BaseBuildVars.LOGS_ENABLED) {
                            FileLog.d("address is good");
                        }
                    }
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        try {
            NetworkInterface networkInterface;
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            boolean hasIpv4 = false;
            boolean hasIpv6 = false;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback()) {
                    continue;
                }
                List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
                for (int a = 0; a < interfaceAddresses.size(); a++) {
                    InterfaceAddress address = interfaceAddresses.get(a);
                    InetAddress inetAddress = address.getAddress();
                    if (inetAddress.isLinkLocalAddress() || inetAddress.isLoopbackAddress() || inetAddress.isMulticastAddress()) {
                        continue;
                    }
                    if (inetAddress instanceof Inet6Address) {
                        hasIpv6 = true;
                    } else if (inetAddress instanceof Inet4Address) {
                        String addrr = inetAddress.getHostAddress();
                        if (!addrr.startsWith("192.0.0.")) {
                            hasIpv4 = true;
                        }
                    }
                }
            }
            if (!hasIpv4 && hasIpv6) {
                return true;
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }

        return false;
    }

    private static class DnsTxtLoadTask extends AsyncTask<Void, Void, NativeByteBuffer> {

        private int currentAccount;

        public DnsTxtLoadTask(int instance) {
            super();
            currentAccount = instance;
        }

        protected NativeByteBuffer doInBackground(Void... voids) {
            ByteArrayOutputStream outbuf = null;
            InputStream httpConnectionStream = null;
            for (int i = 0; i < 3; i++) {
                try {
                    String googleDomain;
                    if (i == 0) {
                        googleDomain = "www.google.com";
                    } else if (i == 1) {
                        googleDomain = "www.google.ru";
                    } else {
                        googleDomain = "google.com";
                    }
                    String domain = native_isTestBackend(currentAccount) != 0 ? "tapv2.stel.com" : MessagesController.getInstance(currentAccount).dcDomainName;
                    int len = Utilities.random.nextInt(116) + 13;
                    final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

                    StringBuilder padding = new StringBuilder(len);
                    for (int a = 0; a < len; a++) {
                        padding.append(characters.charAt(Utilities.random.nextInt(characters.length())));
                    }
                    URL downloadUrl = new URL("https://" + googleDomain + "/resolve?name=" + domain + "&type=ANY&random_padding=" + padding);
                    URLConnection httpConnection = downloadUrl.openConnection();
                    httpConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                    httpConnection.addRequestProperty("Host", "dns.google.com");
                    httpConnection.setConnectTimeout(5000);
                    httpConnection.setReadTimeout(5000);
                    httpConnection.connect();
                    httpConnectionStream = httpConnection.getInputStream();

                    outbuf = new ByteArrayOutputStream();

                    byte[] data = new byte[1024 * 32];
                    while (true) {
                        if (isCancelled()) {
                            break;
                        }
                        int read = httpConnectionStream.read(data);
                        if (read > 0) {
                            outbuf.write(data, 0, read);
                        } else if (read == -1) {
                            break;
                        } else {
                            break;
                        }
                    }

                    JSONObject jsonObject = new JSONObject(new String(outbuf.toByteArray(), "UTF-8"));
                    JSONArray array = jsonObject.getJSONArray("Answer");
                    len = array.length();
                    ArrayList<String> arrayList = new ArrayList<>(len);
                    for (int a = 0; a < len; a++) {
                        JSONObject object = array.getJSONObject(a);
                        int type = object.getInt("type");
                        if (type != 16) {
                            continue;
                        }
                        arrayList.add(object.getString("data"));
                    }
                    Collections.sort(arrayList, (o1, o2) -> {
                        int l1 = o1.length();
                        int l2 = o2.length();
                        if (l1 > l2) {
                            return -1;
                        } else if (l1 < l2) {
                            return 1;
                        }
                        return 0;
                    });
                    StringBuilder builder = new StringBuilder();
                    for (int a = 0; a < arrayList.size(); a++) {
                        builder.append(arrayList.get(a).replace("\"", ""));
                    }
                    byte[] bytes = Base64.decode(builder.toString(), Base64.DEFAULT);
                    NativeByteBuffer buffer = new NativeByteBuffer(bytes.length);
                    buffer.writeBytes(bytes);
                    return buffer;
                } catch (Throwable e) {
                    FileLog.e(e);
                } finally {
                    try {
                        if (httpConnectionStream != null) {
                            httpConnectionStream.close();
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                    try {
                        if (outbuf != null) {
                            outbuf.close();
                        }
                    } catch (Exception ignore) {

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final NativeByteBuffer result) {
            Utilities.stageQueue.postRunnable(() -> {
                if (result != null) {
                    currentTask = null;
                    native_applyDnsConfig(currentAccount, result.address, UserConfig.getInstance(currentAccount).getClientPhone());
                } else {
                    if (BaseBuildVars.LOGS_ENABLED) {
                        FileLog.d("failed to get dns txt result");
                        FileLog.d("start azure task");
                    }
                    AzureLoadTask task = new AzureLoadTask(currentAccount);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                    currentTask = task;
                }
            });
        }
    }

    private static class FirebaseTask extends AsyncTask<Void, Void, NativeByteBuffer> {

        private int currentAccount;
        private FirebaseRemoteConfig firebaseRemoteConfig;

        public FirebaseTask(int instance) {
            super();
            currentAccount = instance;
        }

        protected NativeByteBuffer doInBackground(Void... voids) {
            try {
                if (native_isTestBackend(currentAccount) != 0) {
                    throw new Exception("test backend");
                }
                firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
                firebaseRemoteConfig.setConfigSettings(configSettings);
                String currentValue = firebaseRemoteConfig.getString("ipconfigv2");
                if (BaseBuildVars.LOGS_ENABLED) {
                    FileLog.d("current firebase value = " + currentValue);
                }

                firebaseRemoteConfig.fetch(0).addOnCompleteListener(finishedTask -> {
                    final boolean success = finishedTask.isSuccessful();
                    Utilities.stageQueue.postRunnable(() -> {
                        currentTask = null;
                        String config = null;
                        if (success) {
                            firebaseRemoteConfig.activateFetched();
                            config = firebaseRemoteConfig.getString("ipconfigv2");
                        }
                        if (!TextUtils.isEmpty(config)) {
                            byte[] bytes = Base64.decode(config, Base64.DEFAULT);
                            try {
                                NativeByteBuffer buffer = new NativeByteBuffer(bytes.length);
                                buffer.writeBytes(bytes);
                                native_applyDnsConfig(currentAccount, buffer.address, UserConfig.getInstance(currentAccount).getClientPhone());
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        } else {
                            if (BaseBuildVars.LOGS_ENABLED) {
                                FileLog.d("failed to get firebase result");
                                FileLog.d("start dns txt task");
                            }
                            DnsTxtLoadTask task = new DnsTxtLoadTask(currentAccount);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                            currentTask = task;
                        }
                    });
                });
            } catch (Throwable e) {
                Utilities.stageQueue.postRunnable(() -> {
                    if (BaseBuildVars.LOGS_ENABLED) {
                        FileLog.d("failed to get firebase result");
                        FileLog.d("start dns txt task");
                    }
                    DnsTxtLoadTask task = new DnsTxtLoadTask(currentAccount);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                    currentTask = task;
                });
                FileLog.e(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(NativeByteBuffer result) {

        }
    }

    private static class AzureLoadTask extends AsyncTask<Void, Void, NativeByteBuffer> {

        private int currentAccount;

        public AzureLoadTask(int instance) {
            super();
            currentAccount = instance;
        }

        protected NativeByteBuffer doInBackground(Void... voids) {
            ByteArrayOutputStream outbuf = null;
            InputStream httpConnectionStream = null;
            try {
                URL downloadUrl;
                if (native_isTestBackend(currentAccount) != 0) {
                    downloadUrl = new URL("https://software-download.microsoft.com/testv2/config.txt");
                } else {
                    downloadUrl = new URL("https://software-download.microsoft.com/prodv2/config.txt");
                }
                URLConnection httpConnection = downloadUrl.openConnection();
                httpConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                httpConnection.addRequestProperty("Host", "tcdnb.azureedge.net");
                httpConnection.setConnectTimeout(5000);
                httpConnection.setReadTimeout(5000);
                httpConnection.connect();
                httpConnectionStream = httpConnection.getInputStream();

                outbuf = new ByteArrayOutputStream();

                byte[] data = new byte[1024 * 32];
                while (true) {
                    if (isCancelled()) {
                        break;
                    }
                    int read = httpConnectionStream.read(data);
                    if (read > 0) {
                        outbuf.write(data, 0, read);
                    } else if (read == -1) {
                        break;
                    } else {
                        break;
                    }
                }
                byte[] bytes = Base64.decode(outbuf.toByteArray(), Base64.DEFAULT);
                NativeByteBuffer buffer = new NativeByteBuffer(bytes.length);
                buffer.writeBytes(bytes);
                return buffer;
            } catch (Throwable e) {
                FileLog.e(e);
            } finally {
                try {
                    if (httpConnectionStream != null) {
                        httpConnectionStream.close();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                try {
                    if (outbuf != null) {
                        outbuf.close();
                    }
                } catch (Exception ignore) {

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final NativeByteBuffer result) {
            Utilities.stageQueue.postRunnable(() -> {
                if (result != null) {
                    native_applyDnsConfig(currentAccount, result.address, UserConfig.getInstance(currentAccount).getClientPhone());
                } else {
                    if (BaseBuildVars.LOGS_ENABLED) {
                        FileLog.d("failed to get azure result");
                    }
                }
                currentTask = null;
            });
        }
    }
}
