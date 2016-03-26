package com.amlistening2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ShareActionProvider;

import com.amlistening2.commons.AmListen2Util;
import com.amlistening2.commons.Logger;
import com.amlistening2.service.Amlistening2Service;
import com.tapfortap.TapForTap;


/**
 * @author Vijay Chiliveri (cvijaymanu@gmail.com)
 * 
 */
@SuppressLint("NewApi")
public class AmListening2Activity extends Activity {

	SharedPreferences preferences;
	final String welcomeScreenShownPref = "welcomeScreenShown";
	Amlistening2Service service = null;
	private ShareActionProvider shareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_am_listening2);
		service = new Amlistening2Service();
		
		Intent intent = new Intent(this, AmListening2IntentService.class);
		startService(intent);
		TopSongBroadcastReceiver topSongBroadcastReceiver = new TopSongBroadcastReceiver();
		boolean isAlarmSchd = topSongBroadcastReceiver.isAlarmSet(getApplicationContext());
		if(!isAlarmSchd){
			new TopSongBroadcastReceiver().setAlarm(getApplicationContext());	
		}
		Logger.d(getLocalClassName(), "Started the service");
		// show welcome screen based on selection
		preferences = getSharedPreferences(AmListen2Util.SHARED_PREF_FILE, 0);// 0
																				// means
																				// mode
																				// private

		// second argument is the default to use if the preference can't be
		// found
		Boolean welcomeScreenShown = preferences.getBoolean(
				welcomeScreenShownPref, false);

		if (!welcomeScreenShown) {
			// here you can launch another activity if you like
			// the code below will display a popup

			String whatsNewTitle = getResources().getString(
					R.string.whatsNewTitle);
			String whatsNewText = getResources().getString(
					R.string.whatsNewText);
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(whatsNewTitle)
					.setMessage(whatsNewText)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean(welcomeScreenShownPref, true);
			editor.commit(); // Very important to save the preference
		}

		Button currentSong = (Button) findViewById(R.id.Current_Playing_Song_Button);
		Button topSongToday = (Button) findViewById(R.id.Top_Song_Today_Button);
		Button topSongWeek = (Button) findViewById(R.id.Top_Song_Week_Button);
		Button topSongMonth = (Button) findViewById(R.id.Top_Song_Month_Button);
		currentSong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String postMessage = preferences.getString(
						AmListen2Util.SHARED_PREF_TRACK_NAME, null);
				long trackId = preferences.getLong(
						AmListen2Util.SHARED_PREF_TRACK_ID, -1);
				Context context = getApplicationContext();
				if (!AmListen2Util.isEmpty(postMessage)) {
					Intent intent2 = new Intent(context,
							SocialIntegrationActivity.class);
					intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent2.putExtra(AmListen2Util.SOCIAL_POST_TRACK_MESSAGE,
							postMessage);
					intent2.putExtra(AmListen2Util.SHARED_PREF_TRACK_ID, trackId);
					context.startActivity(intent2);
				} else {
					Intent intent3 = new Intent(context, NoSongsActivity.class);
					intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent3.putExtra(AmListen2Util.TOP_SONG_TYPE,
							AmListen2Util.TOP_SONG_CURRENT);
					context.startActivity(intent3);
				}
				finish();
			}
		});
		topSongToday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				service.getTopSong(getApplicationContext(),
						AmListen2Util.TOP_SONG_TODAY);
				finish();
			}
		});
		topSongWeek.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				service.getTopSong(getApplicationContext(),
						AmListen2Util.TOP_SONG_WEEK);
				finish();
			}
		});
		topSongMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				service.getTopSong(getApplicationContext(),
						AmListen2Util.TOP_SONG_MONTH);
				finish();
			}
		});
		TapForTap.initialize(this, "b3cea95b8d726cfe6328820796791f68");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_am_listening2, menu);		
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			MenuItem item = menu.findItem(R.id.menu_item_share);
		shareActionProvider = (ShareActionProvider) item.getActionProvider();
		shareActionProvider.setShareIntent(createShareIntent());
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this,AboutActivity.class));
			overridePendingTransition(R.anim.slidein,R.anim.hold);
			return true;
		case R.id.menu_item_share:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "Loved this app, why don't you check this out: https://play.google.com/store/apps/details?id=com.amlistening2");
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out \"AmListening2\" app");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
		default:
		return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * This creates the message to be shared on tapping the share intent
	 * @return
	 */
	private Intent createShareIntent(){
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Loved this app, why don't you check this out: https://play.google.com/store/apps/details?id=com.amlistening2");
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out \"AmListening2\" app");
		sendIntent.setType("text/plain");
		return sendIntent;
	}
}
