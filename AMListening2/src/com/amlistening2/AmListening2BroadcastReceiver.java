package com.amlistening2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.amlistening2.commons.AmListen2Util;
import com.amlistening2.commons.Logger;
import com.amlistening2.commons.TrackInfoTO;
import com.amlistening2.dao.IMusicSocialDAO;
import com.amlistening2.dao.MusicSocialDao;

/**
 * @author Vijay Chiliveri (cvijaymanu@gmail.com)
 * 
 */
public class AmListening2BroadcastReceiver extends BroadcastReceiver {

	public TrackInfoTO infoTO;
	private static final String TAG = "AmListening2BroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action;
		String cmd;
		
		infoTO = new TrackInfoTO();
		try {
			action = intent.getAction();
			cmd = intent.getStringExtra("command");
			Logger.d("AMListening2Service", action + " / " + cmd);
			Logger.d("AMListening2Service",
					"Categories / " + intent.getCategories());
			infoTO.setTrackArtist(intent.getStringExtra("artist"));
			infoTO.setTrackAlbum(intent.getStringExtra("album"));
			infoTO.setTrackName(intent.getStringExtra("track"));
			if (AmListen2Util.DEBUG) {
				Logger.d(TAG,
						infoTO.getTrackName() + ":" + infoTO.getTrackAlbum()
								+ ":" + infoTO.getTrackArtist());
			}
			loadTrackInfo(context, infoTO);
			if (infoTO.getTrackId() != 0) {
				updateTrackInfo(context, infoTO);
			} else {
				insertTrackInfo(context, infoTO);
			}

			shareCurrentTrackPreference(context,
					AmListen2Util.getPostSocialMessage(infoTO, true),
					infoTO.getTrackId());

			
		} catch (Exception e) {
			Log.e("AmListening2BroadcastReceiver", Log.getStackTraceString(e));
		}
	}

	private void shareCurrentTrackPreference(Context context,
			String currentTrack, long trackId) {
		SharedPreferences preferences = context.getSharedPreferences(
				AmListen2Util.SHARED_PREF_FILE, 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(AmListen2Util.SHARED_PREF_TRACK_NAME, currentTrack);
		editor.putLong(AmListen2Util.SHARED_PREF_TRACK_ID, trackId);
		editor.commit();
	}

/*	private SharedPreferences getSharedPref(Context context) {
		return context.getSharedPreferences(AmListen2Util.SHARED_PREF_FILE, 0);
	}
*/
	private void insertTrackInfo(Context context, TrackInfoTO socialTO) {
		IMusicSocialDAO dao = new MusicSocialDao(context);
		dao.insertTrackInfo(socialTO);
	}

	private void updateTrackInfo(Context context, TrackInfoTO socialTO) {
		IMusicSocialDAO dao = new MusicSocialDao(context);
		socialTO.setTotalPlayedCount(socialTO.getTotalPlayedCount() + 1);// increment
																			// the
		// play count

		dao.updateTrackRecord(socialTO);
	}

	private void loadTrackInfo(Context context, TrackInfoTO infoTO) {
		try {
			IMusicSocialDAO dao = new MusicSocialDao(context);
			dao.trackLookup(infoTO);
		} catch (Exception e) {
			Log.e("loadTrackInfo", e.getMessage());
		}
	}

	/*
	 * private boolean postSocial(Context context, TrackInfoTO socialTO) {
	 * boolean postSocial = false; try { IMusicSocialDAO dao = new
	 * MusicSocialDao(context);
	 * 
	 * dao.trackLookup(socialTO); postSocial =
	 * AmListen2Util.isFavTrack(socialTO); } catch (Exception e) {
	 * Log.e("loadTrackInfo", e.getMessage()); } return postSocial; }
	 */

/*	private long lookupDuration(Context context, String artist, String track) {
		final String[] columns = new String[] { MediaStore.Audio.AudioColumns.DURATION };

		// Search the artist/title on external storage
		Cursor cur = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				columns,
				MediaStore.Audio.AudioColumns.ARTIST + " = ? and "
						+ MediaColumns.TITLE + " = ?",
				new String[] { artist, track }, null);

		if (cur != null && cur.moveToFirst()) {
			return cur.getLong(cur
					.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
		}

		// Search the artist/title on internal storage
		cur = context.getContentResolver().query(
				MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
				columns,
				MediaStore.Audio.AudioColumns.ARTIST + " = ? and "
						+ MediaColumns.TITLE + " = ?",
				new String[] { artist, track }, null);

		if (cur != null && cur.moveToFirst()) {
			return cur.getLong(cur
					.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
		}

		// If we're allowed to connect to the network, look up the track info on
		// Last.fm
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			boolean scrobbleWifiOnly = PreferenceManager
					.getDefaultSharedPreferences(context).getBoolean(
							"scrobble_wifi_only", false);
			if (cm.getBackgroundDataSetting()
					&& ni.isConnected()
					&& (!scrobbleWifiOnly || (scrobbleWifiOnly && ni.getType() == ConnectivityManager.TYPE_WIFI))) {
				
				 * LastFmServer server = AndroidLastFmServerFactory.getServer();
				 * try { Track t = server.getTrackInfo(artist, track, "");
				 * return Long.parseLong(t.getDuration()); } catch (IOException
				 * e) { } catch (WSError e) { }
				 }
		}
		return 0;
	}*/
}
