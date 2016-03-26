package com.amlistening2.commons;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * @author Vijay Chiliveri (cvijaymanu@gmail.com)
 * 
 */
public class Logger {
	/**
	 * Method to check if debug logging 
	 * is enabled or not and log the debug logs.
	 * 
	 * @param TAG
	 * @param message
	 */
	public static void d(String TAG, String message) {
		/*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			int debugMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED , 0); 
		}else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int adb = Settings.Secure.getInt(ContentResolver., Settings.Secure.ADB_ENABLED, 0);
		}
		int adb = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ADB_ENABLED, 0);*/
		if (AmListen2Util.DEBUG) {
			Log.d(TAG, message);
		}
	}
}
