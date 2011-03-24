package com.menny.android.anysoftkeyboard.keyboards;

import android.content.Context;

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;
import com.menny.android.anysoftkeyboard.addons.AddOnImpl;

public class KeyboardAddOnAndBuilder extends AddOnImpl {

	public static final String KEYBOARD_PREF_PREFIX = "keyboard_";
	
	private final int mResId;
    private final int mLandscapeResId;
    private final int mIconResId;
    private final String mDefaultDictionary;
    private final int mQwertyTranslationId;
    private final String mAdditionalIsLetterExceptions;
    private final boolean mKeyboardDefaultEnabled;
    
    public KeyboardAddOnAndBuilder(Context packageContext, String id, int nameResId,
            int layoutResId, int landscapeLayoutResId,
            String defaultDictionary, int iconResId,
            int physicalTranslationResId,
            String additionalIsLetterExceptions,
            String description,
            int keyboardIndex,
            boolean keyboardDefaultEnabled) {
		super(packageContext, KEYBOARD_PREF_PREFIX+id, nameResId, description, keyboardIndex);
		
		mResId = layoutResId;
        if (landscapeLayoutResId == -1){
            mLandscapeResId = mResId;
        } else {
            mLandscapeResId = landscapeLayoutResId;
        }
		
        mDefaultDictionary = defaultDictionary;
        mIconResId = iconResId;
        mAdditionalIsLetterExceptions = additionalIsLetterExceptions;
        mQwertyTranslationId = physicalTranslationResId;
        mKeyboardDefaultEnabled = keyboardDefaultEnabled;
	}
    
    public boolean getKeyboardDefaultEnabled() {
    	return mKeyboardDefaultEnabled;
    }
    
    public AnyKeyboard createKeyboard(AnyKeyboardContextProvider askContext, int mode) {
        return new ExternalAnyKeyboard(askContext, getPackageContext(), mResId, mLandscapeResId, getId(), getNameResId(), mIconResId, mQwertyTranslationId, mDefaultDictionary, mAdditionalIsLetterExceptions, mode);
    }
}
