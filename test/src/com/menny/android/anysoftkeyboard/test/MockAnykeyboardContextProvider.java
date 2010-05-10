package com.menny.android.anysoftkeyboard.test;

import android.content.Context;
import android.content.SharedPreferences;

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;

public class MockAnykeyboardContextProvider implements
		AnyKeyboardContextProvider {

	private final Context context;

	public MockAnykeyboardContextProvider(Context context) {
		this.context = context;
	}

	@Override
	public void appendCharactersToInput(CharSequence text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteLastCharactersFromInput(int lenght) {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getApplicationContext() {
		return context;
	}

	@Override
	public SharedPreferences getSharedPreferences() {

		return context.getSharedPreferences("ASK", 0);
	}

	@Override
	public void performLengthyOperation(int textResId, Runnable thingToDo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showToastMessage(int resId, boolean forShortTime) {
		// TODO Auto-generated method stub

	}

}
