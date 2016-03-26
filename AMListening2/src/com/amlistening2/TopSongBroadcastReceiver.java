package com.amlistening2;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.amlistening2.commons.AmListen2Util;
import com.amlistening2.commons.Logger;
import com.amlistening2.service.Amlistening2Service;


/**
 * @author Vijay Chiliveri (cvijaymanu@gmail.com)
 * 
 */
public class TopSongBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "TopSongBroadcastReceiver";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.d(
				TAG,
				"Invoked broadcast receiver @ "
						+ AmListen2Util.getTodayFormattedDate());
		new Amlistening2Service().getTopSong(context,
				AmListen2Util.TOP_SONG_TODAY);
		/*
		 * if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 17 &&
		 * !getSharedPref(context).getBoolean(
		 * AmListen2Util.IS_USER_NOTIFIED_TOP_SONG, false)) { // Ensures // the
		 * activity is triggered only after 6 PM in the evening service = new
		 * Amlistening2Service(); service.getTopSong(context,
		 * AmListen2Util.TOP_SONG_TODAY);
		 * 
		 * 
		 * boolean postStatus = postSocial(context, infoTO); if (postStatus) {
		 * String currentTrack = AmListen2Util.getPostSocialMessage( infoTO,
		 * false); if (!AmListen2Util.isEmpty(currentTrack)) { if
		 * (AmListen2Util.DEBUG) { Toast.makeText(context, currentTrack,
		 * Toast.LENGTH_LONG).show(); } }
		 * 
		 * intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * intent2.putExtra("currentTrack", currentTrack);
		 * context.startActivity(intent2); }
		 * 
		 * }else if(userNotifiedDate!=null &&
		 * userNotifiedDate.get(Calendar.DAY_OF_YEAR) <
		 * AmListen2Util.getTodayFormattedDateInstance
		 * ().get(Calendar.DAY_OF_YEAR)) {
		 * AmListen2Util.updateUserNotifiedStatus(context, false);
		 * AmListen2Util.updateUserNotifiedDate(context,
		 * AmListen2Util.getTodayFormattedDate());
		 * 
		 * }
		 */

	}

	public void setAlarm(Context context) {
		Logger.d(TAG, "Invoked set alarm manager");
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, TopSongBroadcastReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		// alarmManager.cancel(pendingIntent);
		Calendar cal = Calendar.getInstance();
		Calendar cur_cal = Calendar.getInstance();
		cur_cal.setTimeInMillis(System.currentTimeMillis());
//		Date date = new Date(cur_cal.get(Calendar.YEAR), cur_cal.get(Calendar.MONTH), cur_cal.get(Calendar.DATE), 8, 06);
		cal.set(cur_cal.get(Calendar.YEAR), cur_cal.get(Calendar.MONTH), cur_cal.get(Calendar.DATE), 19, 00); //set to trigger every day at 6:00 pm
		Logger.d(TAG, "Time to set is "+cal.getTime());
		intent.putExtra("profile_id", 2);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				cal.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
		/*alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), 1 * 60 * 60 * 1000, pendingIntent);*/
		Logger.d(TAG, "Alarm Manager is set to fire for every day at 6:00 pm hour");
	}

	/**
	 * Checks if the AlarmManager is already set 
	 * @param context
	 * @return <true> if AlarmManager is already set.
	 */
	public boolean isAlarmSet(Context context) {
		boolean alarmUp = (PendingIntent.getBroadcast(context, 0, 
				new Intent(context, TopSongBroadcastReceiver.class), 
		        PendingIntent.FLAG_NO_CREATE) != null);
		return alarmUp;
	}

}
