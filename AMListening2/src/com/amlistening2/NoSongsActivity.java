package com.amlistening2;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.amlistening2.commons.AmListen2Util;

/**
 * @author Vijay Chiliveri (cvijaymanu@gmail.com)
 * 
 */
public class NoSongsActivity extends Activity {

	String songType = null;
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_no_songs);
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		Intent intent = getIntent();
		songType = intent.getStringExtra(AmListen2Util.TOP_SONG_TYPE);
		TextView textView = (TextView) findViewById(R.id.textView_no_songs_message);
		textView.setText(getNoSongsMessage(songType));
	}
	private int getNoSongsMessage(String songType) {
		int messageID=0;
		if (songType != null
				&& songType.equalsIgnoreCase(AmListen2Util.TOP_SONG_CURRENT)) {
			messageID = R.string.no_songs_current;
		} else if (songType != null
				&& songType.equalsIgnoreCase(AmListen2Util.TOP_SONG_TODAY)) {
			messageID = R.string.no_songs_today;
		} else if (songType != null
				&& songType.equalsIgnoreCase(AmListen2Util.TOP_SONG_WEEK)) {
			messageID = R.string.no_songs_week;
		} else if (songType != null
				&& songType.equalsIgnoreCase(AmListen2Util.TOP_SONG_MONTH)) {
			messageID = R.string.no_songs_month;
		}
		return messageID;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean returnValue = false;
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, AmListening2Activity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			overridePendingTransition(R.anim.slidein, R.anim.hold);
			finish();
			returnValue = true;
		default:
			returnValue = super.onOptionsItemSelected(item);
		}
		return returnValue;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, AmListening2Activity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		overridePendingTransition(R.anim.slidein, R.anim.hold);
	}

}
