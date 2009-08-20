package com.menny.android.anysoftkeyboard.keyboards;

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;
import com.menny.android.anysoftkeyboard.R;
import com.menny.android.anysoftkeyboard.Dictionary.Dictionary;

public class ArabicKeyboard extends AnyKeyboard/* implements HardKeyboardTranslator*/
{
	public ArabicKeyboard(AnyKeyboardContextProvider context) 
	{
		super(context, R.xml.arabic_qwerty, false, R.string.arabic_keyboard, false, Dictionary.Language.None);
	}

	@Override
	public int getKeyboardIcon() 
	{
		return R.drawable.ar;
	}
}
