package com.menny.android.anysoftkeyboard.keyboards;

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;
import com.menny.android.anysoftkeyboard.keyboards.AnyKeyboard.HardKeyboardTranslator;

public class LatinKeyboard extends AnyKeyboard implements HardKeyboardTranslator
{
	protected LatinKeyboard(AnyKeyboardContextProvider context, int keyboardLayoutId, int keyboardNameId, String keyboardPrefId) 
	{
		super(context, keyboardLayoutId, true, keyboardNameId, keyboardPrefId, true);
	}
		
	public char translatePhysicalCharacter(int keyCode, int metaKeys) 
	{
		//I'll return 0, so the caller will use defaults.
		return 0;
	}
	
	/*
	 * there are some keys which we'll like to expand, e.g.,
	 * lowercase:
		a: àâáäãæå
		e: éèêë
		u: ùûüú
		i: îïíì
		o: ôöòóõœø
		c: ç
		n: ñ
		y: ÿý
		s: ß§
		
		upper case:
		E: ÈÉÊË
		Y: ÝŸ
		U: ÛÙÛÜ
		I: ÎÌÏÍ
		O: ÒÓÔÖÕŒØ
		A: ÀÁÂÄÃÅÆ
		S: §
		C: Ç
		N: Ñ
	 */
	@Override
	protected void setKeyPopup(Key aKey, boolean shiftState) 
	{
		super.setKeyPopup(aKey, shiftState);
	
		switch((char)aKey.codes[0])
		{
			case 'a':
				if (shiftState)
					aKey.popupCharacters = "ÀÁÂÄÃÅÆ";
				else
					aKey.popupCharacters = "àâáäãæå";
				aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
				break;
			case 'c':
				if (shiftState)
					aKey.popupCharacters = "Ç";
				else
					aKey.popupCharacters = "ç";
				aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
				break;
			case 'e':
				if (shiftState)
					aKey.popupCharacters = "ÈÉÊË€";
				else
					aKey.popupCharacters = "éèêë€";
				aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
				break;
			case 'i':
				if (shiftState)
					aKey.popupCharacters = "ÎÌÏÍ";
				else
					aKey.popupCharacters = "îïíì";
				aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
				break;
			case 'o':
				if (shiftState)
					aKey.popupCharacters = "ÒÓÔÖÕŒØ";
				else
					aKey.popupCharacters = "ôöòóõœø";
				aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
				break;
			case 'u':
				if (shiftState)
					aKey.popupCharacters = "ÛÙÛÜ";
				else
					aKey.popupCharacters = "ùûüú";
				aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
				break;
			case 'n':
				if (shiftState)
					aKey.popupCharacters = "Ñ";
				else
					aKey.popupCharacters = "ñ";
				aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
				break;
			case 'y':
				if (shiftState)
					aKey.popupCharacters = "ÝŸ";
				else
					aKey.popupCharacters = "ÿý";
				aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
				break;
			case 's':
				if (shiftState)
					aKey.popupCharacters = "§";
				else
					aKey.popupCharacters = "ß§";
				aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
				break;
		}
	}
}
