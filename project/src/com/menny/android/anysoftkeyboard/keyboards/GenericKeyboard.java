package com.menny.android.anysoftkeyboard.keyboards;

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;
import com.menny.android.anysoftkeyboard.Dictionary.Dictionary;

public class GenericKeyboard extends AnyKeyboard 
{
	public GenericKeyboard(AnyKeyboardContextProvider context, int xmlLayoutResId,
			boolean supportsShift, int keyboardNameId) 
	{
		super(context, xmlLayoutResId, supportsShift, keyboardNameId, true, Dictionary.Language.None);
	}	
}
