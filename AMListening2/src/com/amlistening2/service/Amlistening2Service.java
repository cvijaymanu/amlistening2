package com.amlistening2.service;

import android.content.Context;
import android.content.Intent;

import com.amlistening2.NoSongsActivity;
import com.amlistening2.R;
import com.amlistening2.SocialIntegrationActivity;
import com.amlistening2.commons.AmListen2Util;
import com.amlistening2.commons.TrackInfoTO;
import com.amlistening2.dao.IMusicSocialDAO;
import com.amlistening2.dao.MusicSocialDao;

/**
 * @author Vijay Chiliveri (cvijaymanu@gmail.com)
 * 
 */
public class Amlistening2Service {

	public void getTopSong(Context context, String songType) {
		TrackInfoTO infoTO = null;
		String postMessage = null;
		Intent intent2 = null;
		IMusicSocialDAO dao = new MusicSocialDao(context);
		infoTO = dao.getTopSongFor(songType);
		if (infoTO != null) {
			if (infoTO.getTotalPlayedCount() > 1) {
				postMessage = AmListen2Util.getPostSocialMessage(infoTO, false);

				intent2 = new Intent(context, SocialIntegrationActivity.class);
				intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent2.putExtra(AmListen2Util.SOCIAL_POST_TRACK_MESSAGE,
						postMessage);
				intent2.putExtra(AmListen2Util.SHARED_PREF_TRACK_ID,
						infoTO.getTrackId());
				context.startActivity(intent2);
			} else {
				/*
				 * Toast.makeText(context, "You do not have any top songs :(",
				 * Toast.LENGTH_LONG).show();
				 */
				intent2 = new Intent(context, NoSongsActivity.class);
				intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent2.putExtra(AmListen2Util.TOP_SONG_TYPE,
						songType);
				context.startActivity(intent2);
			}
		}
	}
}
