package com.menny.android.anysoftkeyboard.keyboards;

import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;
import com.menny.android.anysoftkeyboard.AnySoftKeyboardConfiguration;
import com.menny.android.anysoftkeyboard.R;
import com.menny.android.anysoftkeyboard.keyboards.AnyKeyboard.HardKeyboardTranslator;


public class ExternalAnyKeyboard extends AnyKeyboard implements HardKeyboardTranslator {

	private final static String TAG = "ASK - EAK";

	private static final String TAG_ROW = "Row";
    private static final String TAG_KEY = "Key";

	private static class KeyboardMetadata
	{
		public int keysCount = 0;
		public int rowHeight = 0;
		public int rowWidth = 0;
		public int verticalGap = 0;
		public boolean isTopRow = false;
	}

	private static final String XML_TRANSLATION_TAG = "PhysicalTranslation";
	private static final String XML_QWERTY_ATTRIBUTE = "QwertyTranslation";
	private static final String XML_SEQUENCE_TAG = "SequenceMapping";
	private static final String XML_KEYS_ATTRIBUTE = "keySequence";
	private static final String XML_ALT_ATTRIBUTE = "altModifier";
	private static final String XML_SHIFT_ATTRIBUTE = "shiftModifier";
	private static final String XML_TARGET_ATTRIBUTE = "targetChar";
	private static final String XML_TARGET_CHAR_CODE_ATTRIBUTE = "targetCharCode";
	private final String mPrefId;
	private final int mNameResId;
	private final int mIconId;
	private final String mDefaultDictionary;
	private final HardKeyboardSequenceHandler mHardKeyboardTranslator;
	private final String mAdditionalIsLetterExceptions;

	private int mGenericRowsHeight = 0;
	private int mTopRowKeysCount = 0;
	// max(generic row widths)
	private int mMaxGenericRowsWidth = 0;

	public ExternalAnyKeyboard(AnyKeyboardContextProvider askContext,
			Context context, int xmlLayoutResId, int xmlLandscapeResId,
			String prefId, int nameResId, int iconResId,
			int qwertyTranslationId, String defaultDictionary,
			String additionalIsLetterExceptions) {
		this(askContext, context, xmlLayoutResId, xmlLandscapeResId, prefId,
				nameResId, iconResId, qwertyTranslationId, defaultDictionary,
				additionalIsLetterExceptions, true);
	}

	public ExternalAnyKeyboard(AnyKeyboardContextProvider askContext, Context context,
			int xmlLayoutResId,
			int xmlLandscapeResId,
			String prefId,
			int nameResId,
			int iconResId,
			int qwertyTranslationId,
			String defaultDictionary,
			String additionalIsLetterExceptions,
			boolean addGenericRows) {
		super(askContext, context, getKeyboardId(askContext.getApplicationContext(), xmlLayoutResId, xmlLandscapeResId));
		mPrefId = prefId;
		mNameResId = nameResId;
		mIconId = iconResId;
		mDefaultDictionary = defaultDictionary;
		if (qwertyTranslationId != -1)
		{
			Log.d(TAG, "Creating qwerty mapping:"+qwertyTranslationId);
			mHardKeyboardTranslator = createPhysicalTranslatorFromResourceId(context, qwertyTranslationId);
		}
		else
		{
			mHardKeyboardTranslator = null;
		}

		mAdditionalIsLetterExceptions = additionalIsLetterExceptions;

		if(addGenericRows) {
			addGenericRows(askContext, context);
		}
	}

	private void addGenericRows(AnyKeyboardContextProvider askContext, Context context) {
		final String keysMode = AnySoftKeyboardConfiguration.getInstance().getChangeLayoutKeysSize();
		final KeyboardMetadata topMd;
        if (keysMode.equals("None"))
        {
        	topMd = null;
        }
        else if (keysMode.equals("Big"))
        {
        	topMd = addKeyboardRow(askContext.getApplicationContext(), R.xml.generic_top_row);
        }
        else
        {
        	topMd = addKeyboardRow(askContext.getApplicationContext(), R.xml.generic_half_top_row);
        }
        
		if (topMd != null)
			fixKeyboardDueToGenericRow(topMd);

		KeyboardMetadata bottomMd = addKeyboardRow(askContext.getApplicationContext(), R.xml.generic_bottom_row);
		fixKeyboardDueToGenericRow(bottomMd);
	}

    private void fixKeyboardDueToGenericRow(KeyboardMetadata md) {
    	mGenericRowsHeight += md.rowHeight + md.verticalGap;
    	if (md.isTopRow)
    	{
    		mTopRowKeysCount += md.keysCount;
    		List<Key> keys = getKeys();
    		for(int keyIndex = md.keysCount; keyIndex < keys.size(); keyIndex++)
            {
    			final Key key = keys.get(keyIndex);
    			key.y += md.rowHeight + md.verticalGap;
    			if (key instanceof LessSensitiveAnyKey)
            		((LessSensitiveAnyKey)key).resetSenitivity();//reseting cause the key may be offseted now (generic rows)
            }
    	} else {
    		// The height should not include any gap below that last row
    		// this corresponds to
    		// mTotalHeight = y - mDefaultVerticalGap;
    		// in the Keyboard class from Android sources

    		// Note that we are using keyboard default vertical gap (instead of row vertical gap)
    		// as this is done also in Android sources.
    		mGenericRowsHeight -= getVerticalGap();
    	}
	}

	private KeyboardMetadata addKeyboardRow(Context context, int rowResId) {
		XmlResourceParser parser = context.getResources().getXml(rowResId);
    	List<Key> keys = getKeys();
        boolean inKey = false;
        boolean inRow = false;
        boolean leftMostKey = false;

        int row = 0;
        int x = 0;
        int y = 0;
        Key key = null;
        Row currentRow = null;
        Resources res = context.getResources();

        KeyboardMetadata m = new KeyboardMetadata();

        try {
            int event;
            while ((event = parser.next()) != XmlResourceParser.END_DOCUMENT) {
                if (event == XmlResourceParser.START_TAG) {
                    String tag = parser.getName();
                    if (TAG_ROW.equals(tag)) {
                        inRow = true;
                        x = 0;
                        currentRow = createRowFromXml(res, parser);
                        m.isTopRow = currentRow.rowEdgeFlags == Keyboard.EDGE_TOP;
                        if (!m.isTopRow) {
                        	//the bottom row Y should be last
                        	// The last coordinate is height + keyboard's default vertical gap
                        	// since  mTotalHeight = y - mDefaultVerticalGap; (see loadKeyboard
                        	// in the android sources)
                        	// We use our overriden getHeight method which
                        	// is just fixed so that it includes the first generic row.
                        	y = getHeight() + getVerticalGap();
                        }
                        m.rowHeight = currentRow.defaultHeight;
                        m.verticalGap = currentRow.verticalGap;
                   } else if (TAG_KEY.equals(tag)) {
                        inKey = true;
                        key = createKeyFromXml(res, currentRow, x, y, parser);
                        if (m.isTopRow)
                        	keys.add(m.keysCount, key);
                        else
                        	keys.add(key);
                        m.keysCount++;
                    }
                } else if (event == XmlResourceParser.END_TAG) {
                    if (inKey) {
                        inKey = false;
                        x += (key.gap + key.width);
                        if (x > m.rowWidth) {
                        	m.rowWidth = x;
                        	// We keep generic row max width updated
                    		mMaxGenericRowsWidth = Math.max(mMaxGenericRowsWidth, m.rowWidth);
                        }
                    } else if (inRow) {
                        inRow = false;
                        y += currentRow.verticalGap;
                        y += currentRow.defaultHeight;
                        row++;
                    } else {
                        // TODO: error or extend?
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Parse error:" + e);
            e.printStackTrace();
        }
        //mTotalHeight = y - mDefaultVerticalGap;
        return m;
    }

    /*required overrides*/

    @Override
    public int getHeight() {
    	return super.getHeight() + mGenericRowsHeight;
    }

    // minWidth is actually 'total width', see android framework source code
    @Override
    public int getMinWidth() {
    	return Math.max(mMaxGenericRowsWidth, super.getMinWidth());
    }

    @Override
    public int getShiftKeyIndex() {
    	return super.getShiftKeyIndex() + mTopRowKeysCount;
    }

	private HardKeyboardSequenceHandler createPhysicalTranslatorFromResourceId(Context context, int qwertyTranslationId) {
		HardKeyboardSequenceHandler translator = new HardKeyboardSequenceHandler();
		XmlPullParser parser = context.getResources().getXml(qwertyTranslationId);
		final String TAG = "ASK Hard Translation Parser";
		try {
            int event;
            boolean inTranslations = false;
            while ((event = parser.next()) != XmlPullParser.END_DOCUMENT)
            {
            	String tag = parser.getName();
                if (event == XmlPullParser.START_TAG) {
                    if (XML_TRANSLATION_TAG.equals(tag)) {
                    	inTranslations = true;
                    	AttributeSet attrs = Xml.asAttributeSet(parser);
                    	final String qwerty = attrs.getAttributeValue(null, XML_QWERTY_ATTRIBUTE);
                    	translator.addQwertyTranslation(qwerty);
                    	if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Starting parsing "+XML_TRANSLATION_TAG+". Qwerty:"+qwerty);
                    }
                    else if (inTranslations && XML_SEQUENCE_TAG.equals(tag))
                    {
                    	if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Starting parsing "+XML_SEQUENCE_TAG);
                    	AttributeSet attrs = Xml.asAttributeSet(parser);

                    	final int[] keyCodes = getKeyCodesFromPhysicalSequence(attrs.getAttributeValue(null, XML_KEYS_ATTRIBUTE));
                    	final boolean isAlt = attrs.getAttributeBooleanValue(null, XML_ALT_ATTRIBUTE, false);
                    	final boolean isShift = attrs.getAttributeBooleanValue(null, XML_SHIFT_ATTRIBUTE, false);
                    	final String targetChar = attrs.getAttributeValue(null, XML_TARGET_ATTRIBUTE);
                    	final String targetCharCode = attrs.getAttributeValue(null, XML_TARGET_CHAR_CODE_ATTRIBUTE);
                        final String target;
                        if (targetChar == null)
                        	target = Character.toString((char)Integer.parseInt(targetCharCode));
                        else
                        	target = targetChar;
                    	//asserting
                        if ((keyCodes == null) || (keyCodes.length == 0) || (target == null))
                        {
                            Log.e(TAG, "Physical translator sequence does not include mandatory fields "+XML_KEYS_ATTRIBUTE+" or "+XML_TARGET_ATTRIBUTE);
                        }
                        else
                        {
                        	if (!isAlt && !isShift)
                        	{
	                        	if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Physical translation details: keys:"+printInts(keyCodes)+" target:"+target);
	                        	translator.addSequence(keyCodes, target.charAt(0));
                        	}
                        	else if (isAlt)
                        	{
                        		final int keyCode = keyCodes[0];
                        		if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Physical translation details: ALT+key:"+keyCode+" target:"+target);
	                        	translator.addAltMapping(keyCode, target.charAt(0));
                        	}
                        	else if (isShift)
                        	{
                        		final int keyCode = keyCodes[0];
                        		if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Physical translation details: ALT+key:"+keyCode+" target:"+target);
	                        	translator.addShiftMapping(keyCode, target.charAt(0));
                        	}
                        }
                    }
                }
                else if (event == XmlPullParser.END_TAG) {
                	if (XML_TRANSLATION_TAG.equals(tag)) {
                    	inTranslations = false;
                    	if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Finished parsing "+XML_TRANSLATION_TAG);
                    	break;
                    }
                	else if (inTranslations && XML_SEQUENCE_TAG.equals(tag))
                    {
                		if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Finished parsing "+XML_SEQUENCE_TAG);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Parse error:" + e);
            e.printStackTrace();
        }
		return translator;
	}

	private String printInts(int[] keyCodes) {
		String r = "";
		for(int code : keyCodes)
			r += (Integer.toString(code)+",");

		return r;
	}

	private int[] getKeyCodesFromPhysicalSequence(String keyCodesArray) {
		String[] splitted = keyCodesArray.split(",");
		int[] keyCodes = new int[splitted.length];
		for (int i = 0; i < keyCodes.length; i++) {
			try {
				keyCodes[i] = Integer.parseInt(splitted[i]);//try parsing as an integer
			} catch (final NumberFormatException nfe) {//no an integer
				final String v = splitted[i];
				try {
					keyCodes[i] = android.view.KeyEvent.class.getField(v)
							.getInt(null);//here comes the reflection
				} catch (final Exception ex) {//crap :(
					throw new RuntimeException(ex);//bum
				}
			}
		}

		return keyCodes;
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

		if (AnySoftKeyboardConfiguration.getInstance().getDEBUG())
		{
			Log.d(TAG, "inPortraitMode:"+inPortraitMode+" portrait ID:"+portraitId+" landscape ID:"+landscapeId);
		}
		
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
			final char translated;
			if (action.isAltActive())
				translated = mHardKeyboardTranslator.getAltCharacter(action.getKeyCode());
			else if (action.isShiftActive())
			{
				//shift is a special case, we might have a special shift mapping, but
				//we may not... in the latter case, we'll use the regualr translation
				//but upper-case the output
				char shiftTranslation = mHardKeyboardTranslator.getShiftCharacter(action.getKeyCode());
				if (shiftTranslation <= 0)
				{
					shiftTranslation = mHardKeyboardTranslator.getSequenceCharacter(action.getKeyCode(), getASKContext());
					//uppercasing
					shiftTranslation = Character.toUpperCase(shiftTranslation);
				}
				
				translated = shiftTranslation;
			}
			else
				translated = mHardKeyboardTranslator.getSequenceCharacter(action.getKeyCode(), getASKContext());

			if (translated != 0)
				action.setNewKeyCode(translated);
		}
	}

	@Override
	public boolean isLetter(char keyValue) {
		if (mAdditionalIsLetterExceptions == null)
			return super.isLetter(keyValue);
		else
			return super.isLetter(keyValue) ||
				(mAdditionalIsLetterExceptions.indexOf(keyValue) >= 0);
	}

	protected void setPopupKeyChars(Key aKey)
	{
		if (aKey.popupResId > 0)
			return;//if the keyboard XML already specified the popup, then no need to override

		//filling popup res for external keyboards
		if ((aKey.popupCharacters != null) && (aKey.popupCharacters.length() > 0))
			aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;

		if ((aKey.codes != null) && (aKey.codes.length > 0))
        {
			switch((char)aKey.codes[0])
			{
				case 'a':
					aKey.popupCharacters = "\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u0105";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'c':
					aKey.popupCharacters = "\u00e7\u0107\u0109\u010d";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'd':
					aKey.popupCharacters = "\u0111";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'e':
					aKey.popupCharacters = "\u00e8\u00e9\u00ea\u00eb\u0119\u20ac";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'g':
					aKey.popupCharacters = "\u011d";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'h':
					aKey.popupCharacters = "\u0125";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'i':
					aKey.popupCharacters = "\u00ec\u00ed\u00ee\u00ef\u0142";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'j':
					aKey.popupCharacters = "\u0135";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'l':
					aKey.popupCharacters = "\u0142";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'o':
					aKey.popupCharacters = "\u00f2\u00f3\u00f4\u00f5\u00f6\u00f8\u0151\u0153";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 's':
					aKey.popupCharacters = "\u00a7\u00df\u015b\u015d\u0161";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'u':
					aKey.popupCharacters = "\u00f9\u00fa\u00fb\u00fc\u016d\u0171";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'n':
					aKey.popupCharacters = "\u00f1";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'y':
					aKey.popupCharacters = "\u00fd\u00ff";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				case 'z':
					aKey.popupCharacters = "\u017c\u017e";
					aKey.popupResId = com.menny.android.anysoftkeyboard.R.xml.popup;
					break;
				default:
					super.setPopupKeyChars(aKey);
			}
        }
	}
}
