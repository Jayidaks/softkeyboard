package com.anysoftkeyboard.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.anysoftkeyboard.AnySoftKeyboard;

public class KeyPressSimulationRequestReceiver extends BroadcastReceiver {
	
	private static final String COM_ANYSOFTKEYBOARD_PRESS_KEY = "com.anysoftkeyboard.PRESS_KEY";

	private static final String TAG = "ASK key_press";
	
	private final AnySoftKeyboard mIme;
	
	public KeyPressSimulationRequestReceiver(AnySoftKeyboard ime)
	{
		mIme = ime;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null || intent.getData() == null || context == null)
			return;
		int key_code = intent.getIntExtra("key_code", 0);
		if (key_code <= 0)
			return;
		Log.d(TAG, "Got a request to send key-press: "+key_code);
		//this could be a fail! Since it has a SOFT_KEYBOARD flag.
		mIme.sendDownUpKeyEvents(key_code);
	}

	public IntentFilter createFilterToRegisterOn() {
		
		IntentFilter filter = new IntentFilter();
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		
		filter.addAction(COM_ANYSOFTKEYBOARD_PRESS_KEY);
		
		filter.addDataScheme("package");
		
		return filter;
	}
}
