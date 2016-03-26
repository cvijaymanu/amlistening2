package com.amlistening2;

import com.amlistening2.commons.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Vijay Chiliveri (cvijaymanu@gmail.com)
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {
	private static final String TAG = "BootCompleteReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		TopSongBroadcastReceiver topSongBroadcastReceiver = new TopSongBroadcastReceiver();
		Logger.d(TAG, "Invoked after boot complete");
		boolean isAlarmSchd = topSongBroadcastReceiver.isAlarmSet(context);
		Logger.d(TAG, "is alarm scheduled: "+isAlarmSchd);
		if(!isAlarmSchd){
			new TopSongBroadcastReceiver().setAlarm(context);
			Logger.d(TAG, "Alarm manager scheduled");
		}
	}

}
