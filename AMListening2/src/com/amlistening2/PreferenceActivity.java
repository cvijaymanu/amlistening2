package com.amlistening2;

import java.util.List;

import android.os.Bundle;
import android.widget.Button;

/**
 * @author Vijay Chiliveri (cvijaymanu@gmail.com)
 * 
 */
public class PreferenceActivity extends android.preference.PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   Button button = new Button(this);
	   button.setText("Settings");
	   setListFooter(button);
	   addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	public void onBuildHeaders(List<Header> target) {
		// TODO Auto-generated method stub
		super.onBuildHeaders(target);
		loadHeadersFromResource(R.xml.preferences, target);
	}
}
