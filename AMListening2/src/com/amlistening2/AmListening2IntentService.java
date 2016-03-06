/**
 * 
 */
package com.amlistening2;

import android.app.IntentService;
import android.content.Intent;

import com.amlistening2.commons.Logger;

/**
 * @author vijju
 * 
 */
public class AmListening2IntentService extends IntentService {


	public AmListening2IntentService() {
		super("AmListening2IntentService");
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent arg0) {
		Logger.d("AMListening2Service", "invoked");
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

}
