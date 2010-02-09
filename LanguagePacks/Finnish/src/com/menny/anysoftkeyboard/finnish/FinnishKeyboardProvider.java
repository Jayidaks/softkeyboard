package com.menny.anysoftkeyboard.finnish;

import android.net.Uri;

public class FinnishKeyboardProvider extends KeyboardProvider {	
	public static final String AUTHORITY = "com.anysoftkeyboard.keyboard.finnish";
	public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/items");
	
	@Override
	protected String getKeyboardLayoutId() {
		return "finnish_qwerty";
	}
	
	@Override
	protected int getKeyboardSortValue() {
		return 121;
	}

	@Override
	protected String getKeyboardEnabledPrefKey() {
		return "finnish_keyboard";
	}

	@Override
	protected String getKeyboardNameResId() {
		return "finnish_keyboard";
	}
	
	@Override
	protected String getPackageName()
	{
		return "com.menny.anysoftkeyboard.finnish";
	}

	@Override
	protected String getDefaultDictionary() {
		return "Finnish";
	}
}