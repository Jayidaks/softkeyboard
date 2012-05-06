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
		if (intent == null || context == null)
			return;
		Object key_code = intent.getExtras().get("key_code");
		if (key_code == null)
			return;
		String key_code_string = key_code.toString();
		int key_code_value = Integer.parseInt(key_code_string);
		
		if (key_code_value <= 0)
			return;
		Log.d(TAG, "Got a request to send key-press: "+key_code_value);
		//this could be a fail! Since it has a SOFT_KEYBOARD flag.
		mIme.sendDownUpKeyEvents(key_code_value);
	}

	public IntentFilter createFilterToRegisterOn() {
		
		IntentFilter filter = new IntentFilter();		
		filter.addAction(COM_ANYSOFTKEYBOARD_PRESS_KEY);
		return filter;
	}
}
