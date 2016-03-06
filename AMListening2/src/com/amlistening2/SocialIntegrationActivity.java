package com.amlistening2;

import java.io.IOException;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.service.textservice.SpellCheckerService.Session;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amlistening2.commons.AmListen2Util;
import com.amlistening2.commons.Logger;
import com.amlistening2.dao.IMusicSocialDAO;
import com.amlistening2.dao.MusicSocialDao;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class SocialIntegrationActivity extends Activity {
	private static final String FACEBOOK_APPID = "182008371950901";
	private SharedPreferences mPrefs;
	private static Facebook facebook = null;
	private static final String TAG = "SocialIntegrationActivity";

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.hold, R.anim.slideout);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.activity_social_integration);
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
		facebook = getFacebookInstance();
		try {
			initilizeFacebook(); // get permissions for facebook
			// start Facebook Login
			Intent intent = getIntent();
			final String currentTrack = intent
					.getStringExtra(AmListen2Util.SOCIAL_POST_TRACK_MESSAGE); // get
																				// the
																				// track
																				// name
																				// to
																				// be
			final long trackId = intent.getLongExtra(
					AmListen2Util.SHARED_PREF_TRACK_ID, -1); // posted

			Logger.d(TAG, "Track: " + currentTrack);
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			if (currentTrack != null && !AmListen2Util.isEmpty(currentTrack)) {
				final EditText textView = (EditText) findViewById(R.id.socialPostMessage);
				textView.setText(currentTrack);
				Button yesButton = (Button) findViewById(R.id.button_yes);
				Button noButton = (Button) findViewById(R.id.button_no);
				yesButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						postMessageOnWall(textView.getText().toString(),
								trackId);
						finish();
					}
				});
				noButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});
			} else {
				Toast.makeText(getApplicationContext(),
						"There is no song playing now", Toast.LENGTH_LONG)
						.show();
				setVisible(false);
				Log.w(TAG, "####Status update from my app######");
			}
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e), e);
		}
	}

	/*
	 * private void initilizeFacebook() { session = Session.getActiveSession();
	 * if (session == null) { if (savedInstanceState != null) { session =
	 * Session.restoreSession(getActivity(), null, statusCallback,
	 * savedInstanceState); } if (session == null) { session = new
	 * Session(getActivity()); } Session.setActiveSession(session); if
	 * (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
	 * session.openForRead(new
	 * Session.OpenRequest(this).setCallback(statusCallback)); } }
	 * 
	 * if (access_token != null) { facebook.setAccessToken(access_token); } if
	 * (expires != 0) { facebook.setAccessExpires(expires); }
	 *//** Only call authorize if the access_token has expired. */
	/*
	 * 
	 * if (!facebook.isSessionValid()) {
	 * 
	 * facebook.authorize(this, new String[] { "publish_actions" }, new
	 * DialogListener() {
	 * 
	 * @Override public void onComplete(Bundle values) { Logger.d(ACCOUNT_SERVICE,
	 * "1"); SharedPreferences.Editor editor = mPrefs.edit();
	 * Logger.d(ACCOUNT_SERVICE, "" + "2"); editor.putString("access_token",
	 * facebook.getAccessToken()); Logger.d(ACCOUNT_SERVICE, "3");
	 * editor.putLong("access_expires", facebook.getAccessExpires());
	 * editor.commit(); Logger.d(ACCOUNT_SERVICE, "got Authorization"); }
	 * 
	 * @Override public void onFacebookError(FacebookError error) {
	 * Toast.makeText(getBaseContext(), "FacebookError",
	 * Toast.LENGTH_LONG).show(); }
	 * 
	 * @Override public void onError(DialogError e) {
	 * Toast.makeText(getBaseContext(), "De", Toast.LENGTH_LONG).show(); }
	 * 
	 * @Override public void onCancel() { } }); } }
	 */
	private void initilizeFacebook() {
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		/** Only call authorize if the access_token has expired. */

		if (!facebook.isSessionValid()) {

			facebook.authorize(this, new String[] { "publish_actions" },
					new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
							Logger.d(ACCOUNT_SERVICE, "1");
							SharedPreferences.Editor editor = mPrefs.edit();
							Logger.d(ACCOUNT_SERVICE, "" + "2");
							editor.putString("access_token",
									facebook.getAccessToken());
							Logger.d(ACCOUNT_SERVICE, "3");
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();
							Logger.d(ACCOUNT_SERVICE, "got Authorization");
						}

						@Override
						public void onFacebookError(FacebookError error) {
							Toast.makeText(getBaseContext(), "FacebookError",
									Toast.LENGTH_LONG).show();
							Log.e(TAG, error.getMessage());
						}

						@Override
						public void onError(DialogError e) {
							Toast.makeText(getBaseContext(), "De",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onCancel() {
						}
					});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_social_integration, menu);
		return true;
	}

	public static Facebook getFacebookInstance() {
		if (facebook == null) {
			facebook = new Facebook("182008371950901");
		}
		return facebook;
	}

	public static void initializeFacebook() {

	}

	public void postMessageOnWall(String msg, long trackId) {
		if (facebook.isSessionValid()) {
			try {
				Bundle parameters = new Bundle();
				parameters.putString("access_token", facebook.getAccessToken());
				parameters.putString("message", msg);
				parameters.putString("name", "value");
				JSONObject me = new JSONObject(facebook.request("me"));
				/*
				 * parameters.putString("place", "www.google.com"); // place to
				 * // get // facebook // page id // which is // set to // place.
				 * // This is // must to // include // tags
				 * Logger.d("SOCIAL_PAGE_ID", me.getString("id"));
				 * parameters.putString("tags", "1234475315");
				 */
				String response = facebook.request("me/feed", parameters,
						"POST");
				/*
				 * mPrefs = getSharedPreferences(AmListen2Util.SHARED_PREF_FILE,
				 * 0); trackId = mPrefs
				 * .getLong(AmListen2Util.SHARED_PREF_TRACK_ID, -1);
				 */
				if (response != null
						&& response.contains("Duplicate status message")) {
					showToast("You have already posted similar post. The current post is ignored");
				} else if (response != null && !response.contains("error")) {
					IMusicSocialDAO dao = new MusicSocialDao(
							getApplicationContext());
					dao.updateSocialPostStatus(Long.valueOf(trackId), true);
					showToast("Status has been posted on your Facebook wall !!!");
				} else {
					showToast("Error: " + response);
					Log.e("Error posting on wall", response);
				}
			} catch (IOException e) {
				Log.e("MUSIC_SOCIAL", Log.getStackTraceString(e));
			} catch (Exception e) {
				Log.e("MUSIC_SOCIAL", Log.getStackTraceString(e));
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Session.getActiveSession().onActivityResult(this, requestCode,
		// resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public void showToast(final String txt) {
		Toast.makeText(this.getApplicationContext(), txt, Toast.LENGTH_LONG)
				.show();
	}
}
