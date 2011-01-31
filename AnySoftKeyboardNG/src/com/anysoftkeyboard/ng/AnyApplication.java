package com.anysoftkeyboard.ng;

import android.app.Application;

public class AnyApplication extends Application {

	private static AnySoftKeyboardConfiguration msConfig;

	@Override
	public void onCreate() {
		super.onCreate();
		
//		Thread.setDefaultUncaughtExceptionHandler(new ChewbaccaUncaughtExceptionHandler(this.getBaseContext(), null));
		
//		String t = getString(R.string.settings_key_top_keyboard_row_id);
//		Log.d("*****" ,"R.string.settings_key_top_keyboard_row_id: "+R.string.settings_key_top_keyboard_row_id+" == "+ t);

		msConfig = AnySoftKeyboardConfiguration.createInstance(this);
	}
	
	public static AnySoftKeyboardConfiguration getConfig()
	{
		return msConfig;
	}
	
}
