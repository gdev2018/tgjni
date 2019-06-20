package com.github.gdev2018.master.voip;

import org.json.JSONException;
import org.json.JSONObject;
import com.github.gdev2018.master.BaseBuildVars;
import com.github.gdev2018.master.FileLog;

/**
 * Created by grishka on 01.03.17.
 */

public class VoIPServerConfig{

	private static JSONObject config=new JSONObject();

	public static void setConfig(String json){
		try{
			config=new JSONObject(json);
			nativeSetConfig(json);
		}catch(JSONException x){
			if (BaseBuildVars.LOGS_ENABLED) {
				FileLog.e("Error parsing VoIP config", x);
			}
		}
	}

	public static int getInt(String key, int fallback){
		return config.optInt(key, fallback);
	}

	public static double getDouble(String key, double fallback){
		return config.optDouble(key, fallback);
	}

	public static String getString(String key, String fallback){
		return config.optString(key, fallback);
	}

	public static boolean getBoolean(String key, boolean fallback){
		return config.optBoolean(key, fallback);
	}

	private static native void nativeSetConfig(String json);
}
