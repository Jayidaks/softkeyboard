package com.anysoftkeyboard.keyboards;

import java.io.IOException;
import java.util.HashSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.anysoftkeyboard.AnyKeyboardContextProvider;
import com.anysoftkeyboard.api.KeyCodes;
import com.anysoftkeyboard.keyboardextensions.KeyboardExtension;
import com.anysoftkeyboard.keyboardextensions.KeyboardExtensionFactory;
import com.anysoftkeyboard.keyboards.AnyKeyboard.HardKeyboardTranslator;
import com.menny.android.anysoftkeyboard.AnyApplication;


public class ExternalAnyKeyboard extends AnyKeyboard implements HardKeyboardTranslator {

	public static final int KEYCODE_EXTENSION_KEYBOARD = -210;
	
	private final static String TAG = "ASK - EAK";
	
	private final String mPrefId;
	private final int mNameResId;
	private final int mIconId;
	private final String mDefaultDictionary;
	private final HardKeyboardSequenceHandler mHardKeyboardTranslator;
	private final HashSet<Character> mAdditionalIsLetterExceptions;
	private final HashSet<Character> mSentenceSeparators;
	
	//private Key mExtensionPopupKey; 
	private KeyboardExtension mExtensionLayout = null;
	
	//private static final int[] qwertKeysequence = new int[] { 45,51,33,46,48 };
	//private static final int[] dotKeysequence = new int[] { 56,56,56,56 };
	
	public ExternalAnyKeyboard(AnyKeyboardContextProvider askContext, Context context,
			int xmlLayoutResId,
			int xmlLandscapeResId,
			String prefId,
			int nameResId,
			int iconResId,
			int qwertyTranslationId,
			String defaultDictionary,
			String additionalIsLetterExceptions,
			String sentenceSeparators,
			int mode) 
	{
		super(askContext, context, getKeyboardId(askContext.getApplicationContext(), xmlLayoutResId, xmlLandscapeResId), mode);
		mPrefId = prefId;
		mNameResId = nameResId;
		mIconId = iconResId;
		mDefaultDictionary = defaultDictionary;
		if (qwertyTranslationId != -1)
		{
		    if (AnyApplication.DEBUG)Log.d(TAG, "Creating qwerty mapping:"+qwertyTranslationId);
		    XmlPullParser parser = context.getResources().getXml(qwertyTranslationId);
		    HardKeyboardSequenceHandler translator = null;
			try {
				translator = HardKeyboardSequenceHandler.createPhysicalTranslatorFromXmlPullParser(parser);
			} catch (NumberFormatException e) {
				translator = null;
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				translator = null;
				e.printStackTrace();
			} catch (IOException e) {
				translator = null;
				e.printStackTrace();
			}
			mHardKeyboardTranslator = translator;
		}
		else
		{
			mHardKeyboardTranslator = null;
		}

		mAdditionalIsLetterExceptions = new HashSet<Character>();
		if (additionalIsLetterExceptions != null)
		{
			for(int i=0;i<additionalIsLetterExceptions.length(); i++)
				mAdditionalIsLetterExceptions.add(additionalIsLetterExceptions.charAt(i));
		}
		mSentenceSeparators = new HashSet<Character>();
		if (sentenceSeparators != null)
		{
			for(int i=0;i<sentenceSeparators.length(); i++)
				mSentenceSeparators.add(sentenceSeparators.charAt(i));
		}
		setExtensionLayout(KeyboardExtensionFactory.getCurrentKeyboardExtension(askContext.getApplicationContext(), KeyboardExtension.TYPE_EXTENSION));
	}
	
	protected void setExtensionLayout(KeyboardExtension extKbd) {
		mExtensionLayout = extKbd;
    }
	
	public KeyboardExtension getExtensionLayout() {
        return mExtensionLayout;
    }
	

	@Override
	public String getDefaultDictionaryLocale() {
		return mDefaultDictionary;
	}

	@Override
	public String getKeyboardPrefId() {
		return mPrefId;
	}

	@Override
	public int getKeyboardIconResId() {
		return mIconId;
	}

	@Override
	protected int getKeyboardNameResId() {
		return mNameResId;
	}

	private static int getKeyboardId(Context context, int portraitId, int landscapeId)
	{
		final boolean inPortraitMode =
			(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);

		if (inPortraitMode)
			return portraitId;
		else
			return landscapeId;
	}

	//this class implements the HardKeyboardTranslator interface in an empty way, the physical keyboard is Latin...
	public void translatePhysicalCharacter(HardKeyboardAction action)
	{
		if (mHardKeyboardTranslator != null)
		{
			final int translated;
			if (action.isAltActive())
				if (!mHardKeyboardTranslator.addSpecialKey(KeyCodes.ALT))
					return;					
			if (action.isShiftActive())
				if (!mHardKeyboardTranslator.addSpecialKey(KeyCodes.SHIFT))
					return;

			translated = mHardKeyboardTranslator.getCurrentCharacter(action.getKeyCode(), getASKContext());
			 
			if (translated != 0)
				action.setNewKeyCode(translated);
		}
	}
	
	@Override
	public boolean isInnerWordLetter(char keyValue) {
		return super.isInnerWordLetter(keyValue) || mAdditionalIsLetterExceptions.contains(keyValue);
	}
	
	@Override
	public HashSet<Character> getSentenceSeparators() {
		return mSentenceSeparators;
	}

	protected void setPopupKeyChars(Key aKey)
	{
		if (aKey.popupResId > 0)
			return;//if the keyboard XML already specified the popup, then no need to override

		//filling popup res for external keyboards
		//if ((aKey.popupCharacters != null) && (aKey.popupCharacters.length() > 0)){
		if (aKey.popupCharacters != null){
		    if(aKey.popupCharacters.length() > 0){
		    	aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
		    }
			return;
		}

		if ((aKey.codes != null) && (aKey.codes.length > 0))
        {
			switch((char)aKey.codes[0])
			{
				case 'a':
					aKey.popupCharacters =  "\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u0105";//"àáâãäåæą";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'c':
					aKey.popupCharacters = "\u00e7\u0107\u0109\u010d";//"çćĉč";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'd':
					aKey.popupCharacters =  "\u0111"; //"đ";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'e':
					aKey.popupCharacters = "\u00e8\u00e9\u00ea\u00eb\u0119\u20ac\u0113";//"èéêëę€";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'g':
					aKey.popupCharacters =  "\u011d";//"ĝ";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'h':
					aKey.popupCharacters = "\u0125";//"ĥ";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'i':
					aKey.popupCharacters = "\u00ec\u00ed\u00ee\u00ef\u0142\u012B";//"ìíîïł";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'j':
					aKey.popupCharacters = "\u0135";//"ĵ";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'l':
					aKey.popupCharacters = "\u0142";//"ł";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'o':
					aKey.popupCharacters =  "\u00f2\u00f3\u00f4\u00f5\u00f6\u00f8\u0151\u0153\u014D";//"òóôõöøőœ";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 's':
					aKey.popupCharacters =  "\u00a7\u00df\u015b\u015d\u0161";//"§ßśŝš";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'u':
					aKey.popupCharacters = "\u00f9\u00fa\u00fb\u00fc\u016d\u0171\u016B";//"ùúûüŭű";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'n':
					aKey.popupCharacters =  "\u00f1";//"ñ";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'y':
					aKey.popupCharacters = "\u00fd\u00ff";//"ýÿ";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'z':
					aKey.popupCharacters = "\u017c\u017e\u017a";//"żžź";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				default:
					super.setPopupKeyChars(aKey);
			}
        }
	}
}
