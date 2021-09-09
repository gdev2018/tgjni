package com.github.gdev2018.master;

import android.content.SharedPreferences;

import com.github.gdev2018.master.tgnet.ConnectionsManager;

public class BaseAccountInstance {

    private int currentAccount;
    private static volatile BaseAccountInstance[] Instance = new BaseAccountInstance[BaseUserConfig.MAX_ACCOUNT_COUNT];
    public static BaseAccountInstance getInstance(int num) {
        BaseAccountInstance localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (BaseAccountInstance.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    Instance[num] = localInstance = new BaseAccountInstance(num);
                }
            }
        }
        return localInstance;
    }

    public BaseAccountInstance(int instance) {
        currentAccount = instance;
    }

    public MessagesController getMessagesController() {
        return MessagesController.getInstance(currentAccount);
    }

    public MessagesStorage getMessagesStorage() {
        return MessagesStorage.getInstance(currentAccount);
    }

    public ContactsController getContactsController() {
        return ContactsController.getInstance(currentAccount);
    }

///*    public MediaDataController getMediaDataController() {
//        return MediaDataController.getInstance(currentAccount);
//    }*/

    public ConnectionsManager getConnectionsManager() {
        return ConnectionsManager.getInstance(currentAccount);
    }

    public NotificationsController getNotificationsController() {
        return NotificationsController.getInstance(currentAccount);
    }

    public NotificationCenter getNotificationCenter() {
        return NotificationCenter.getInstance(currentAccount);
    }

    public LocationController getLocationController() {
        return LocationController.getInstance(currentAccount);
    }

    public BaseUserConfig getUserConfig() {
        return BaseUserConfig.getInstance(currentAccount);
    }

    public DownloadController getDownloadController() {
        return DownloadController.getInstance(currentAccount);
    }

    public SendMessagesHelper getSendMessagesHelper() {
        return SendMessagesHelper.getInstance(currentAccount);
    }

    public SecretChatHelper getSecretChatHelper() {
        return SecretChatHelper.getInstance(currentAccount);
    }

    public StatsController getStatsController() {
        return StatsController.getInstance(currentAccount);
    }

    public FileLoader getFileLoader() {
        return FileLoader.getInstance(currentAccount);
    }

    public FileRefController getFileRefController() {
        return FileRefController.getInstance(currentAccount);
    }

    public SharedPreferences getNotificationsSettings() {
        return MessagesController.getNotificationsSettings(currentAccount);
    }

    public int getCurrentAccount() {
        return currentAccount;
    }
}
