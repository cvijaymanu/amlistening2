/**
 * 
 */
package com.amlistening2.commons;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author vijju
 * 
 */
public class AmListen2Util {
	// logging
	public static boolean DEBUG = false;

	public static final String MUSIC_META_CHANGED = "com.android.music.metachanged";
	public static final String MUSIC_PLAYSTATE_CHANGED = "com.android.music.playstatechanged";
	public static final String MUSIC_PLAYBACK_COMPLETED = "com.android.music.playbackcomplete";
	public static final String MUSIC_QUEUE_CHANGED = "com.android.music.queuechanged";
	public static final String SHARED_PREF_FILE = "AMLISTENING2_FILE";
	public static final String SHARED_PREF_TRACK_NAME = "AMLISTENING2_CURRENT_TRACK";
	public static final String SHARED_PREF_TRACK_ID = "AMLISTENING2_CURRENT_TRACK_ID";
//	public static final String SHARED_PREF_ = "AMLISTENING2_CURRENT_TRACK_ID";
	public static final String SOCIAL_POST_TRACK_MESSAGE = "currentTrack";
	public static final String TOP_SONG_TYPE = "TOP_SONG_TYPE";

	// date format
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String IS_USER_NOTIFIED_TOP_SONG = "IS_USER_NOTIFIED_TOP_SONG";
	public static final String USER_NOTIFIED_DATE = "USER_NOTIFIED_DATE";

	// top songs
	public static final String TOP_SONG_CURRENT = "current";
	public static final String TOP_SONG_TODAY = "today";
	public static final String TOP_SONG_WEEK = "week";
	public static final String TOP_SONG_MONTH = "month";
	
	//symbols
	public static final String HEADPHONE = "☊";
	public static final String MUSIC_SYMBOL = "♫";

	public static boolean isEmpty(String text) {
		boolean status = true;
		if (text != null && text.trim() != "") {
			status = false;
		}
		return status;
	}

	public static String getPostSocialMessage(TrackInfoTO socialTO,
			boolean isCurrentTrack) {
		String statusMessage = null;
		if (isCurrentTrack) {
			statusMessage = "is listening to";
		} else if (socialTO.getTotalPlayedCount() <= 20) {
			statusMessage = "is loving";
		} else if (socialTO.getTotalPlayedCount() > 20
				&& socialTO.getTotalPlayedCount() <= 40) {
			statusMessage = "is loving it, did you listen to";
		} else if (socialTO.getTotalPlayedCount() > 40
				&& socialTO.getTotalPlayedCount() <= 60) {
			statusMessage = "is addicted to";
		} else if (socialTO.getTotalPlayedCount() > 60) {
			statusMessage = "cant stop listening to";
		}
		statusMessage = (AmListen2Util.isEmpty(socialTO.getTrackName())) ? ""
				: statusMessage + " the song \"" + socialTO.getTrackName();
		if (!AmListen2Util.isEmpty(socialTO.getTrackAlbum())) {
			if (AmListen2Util.isEmpty(socialTO.getTrackName())) {
				statusMessage += " the album \""
						+ socialTO.getTrackAlbum().trim();
			} else {
				statusMessage += "\" from \"" + socialTO.getTrackAlbum().trim();
			}
		}
		if (!AmListen2Util.isEmpty(socialTO.getTrackArtist())) {
			if (AmListen2Util.isEmpty(socialTO.getTrackAlbum())
					&& AmListen2Util.isEmpty(socialTO.getTrackName())) {
				statusMessage += " artist \""
						+ socialTO.getTrackArtist().trim() + "\" songs";
			} else {
				statusMessage += "\" by \"" + socialTO.getTrackArtist().trim()
						+ "\"";
			}
		} else {
			if (!AmListen2Util.isEmpty(statusMessage)) {
				statusMessage += "\""; // closing the quotes which were opened
										// adding album/track name
			}
		}
		return statusMessage;
	}

	/**
	 * @param socialTO
	 * @return
	 */
	public static boolean isFavTrack(TrackInfoTO socialTO) {
		boolean isFavTrack = false;
		/*
		 * if (socialTO.getPlayedCount() == 5 || socialTO.getPlayedCount() == 40
		 * || socialTO.getPlayedCount() == 60 || socialTO.getPlayedCount() == 80
		 * || socialTO.getPlayedCount() == 100 || socialTO.getPlayedCount() ==
		 * 120) { // if(true){ isFavTrack = true; }
		 */
		if (socialTO.getTrackId() != 0 && !socialTO.postToFacebook()) {
			if (socialTO.getTodayPlayedCount() == 5
					|| socialTO.getWeekPlayedCount() == 15
					|| socialTO.getTotalPlayedCount() == 100) {
				isFavTrack = true;
			}
		}
		return isFavTrack;
	}

	/**
	 * @return
	 */
	public static String getTodayFormattedDate() {
		return new SimpleDateFormat(DATE_FORMAT, Locale.US).format(Calendar
				.getInstance().getTime());
	}
	
	/**
	 * @return
	 */
	public static Calendar getTodayFormattedDateInstance() {
		return new SimpleDateFormat(DATE_FORMAT, Locale.US).getCalendar();
	}

/*	public static void updateUserNotifiedStatus(Context context,
			boolean isUserNotified) {
		SharedPreferences preferences = context.getSharedPreferences(
				AmListen2Util.SHARED_PREF_FILE, 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(AmListen2Util.IS_USER_NOTIFIED_TOP_SONG,
				isUserNotified);
		editor.commit();
	}*/
	
/*	public static void updateUserNotifiedDate(Context context,
			String notifiedDate) {
		SharedPreferences preferences = context.getSharedPreferences(
				AmListen2Util.SHARED_PREF_FILE, 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(AmListen2Util.USER_NOTIFIED_DATE,
				notifiedDate);
		editor.commit();
	}*/

}
