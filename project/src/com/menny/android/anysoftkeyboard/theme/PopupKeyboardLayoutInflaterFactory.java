package com.menny.android.anysoftkeyboard.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.LayoutInflater.Factory;

public class PopupKeyboardLayoutInflaterFactory implements Factory {

	private ThemeResources mResources;
	private static final String THEMEABLE_KEYBOARDVIEW_CLASS = ThemeableKeyboardView.class.getCanonicalName();

	public PopupKeyboardLayoutInflaterFactory(ThemeResources resources) {
		mResources = resources;

	}

	public View onCreateView(String name, Context context, AttributeSet attrs) {
		if(THEMEABLE_KEYBOARDVIEW_CLASS.equals(name)){
			// Put mThemeResources as a fall back option
			ThemeResources theme = new ThemeResources(context, attrs, mResources);

			return new ThemeableKeyboardView(context, theme, attrs);
		}

		return null;
	}

}
