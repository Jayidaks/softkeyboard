package com.menny.android.anysoftkeyboard.keyboards;

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;
import com.menny.android.anysoftkeyboard.theme.ThemeResources;

public class GenericKeyboard extends AnyKeyboard
{
	private final int mNameResId;
	private final String mPrefId;

	public GenericKeyboard(AnyKeyboardContextProvider context, int xmlLayoutResId, int nameResId, String prefKeyId, ThemeResources themeResources)
	{
		super(context, context.getApplicationContext(), xmlLayoutResId, themeResources);
		mNameResId = nameResId;
		mPrefId = prefKeyId;
	}

	@Override
	public String getDefaultDictionaryLocale() {
		return null;
	}

	@Override
	public int getKeyboardIconResId() {
		return -1;
	}

	@Override
	protected int getKeyboardNameResId() {
		return mNameResId;
	}

	@Override
	public String getKeyboardPrefId() {
		return mPrefId;
	}
}
