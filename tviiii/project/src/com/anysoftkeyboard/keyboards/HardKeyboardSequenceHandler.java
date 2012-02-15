package com.anysoftkeyboard.keyboards;

import java.io.IOException;
import java.security.InvalidParameterException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.anysoftkeyboard.api.KeyCodes;

import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;

import com.anysoftkeyboard.AnyKeyboardContextProvider;
import com.anysoftkeyboard.keyboards.KeyEventStateMachine.State;
import com.menny.android.anysoftkeyboard.AnyApplication;

public class HardKeyboardSequenceHandler
{
	private static final int[] msQwerty = new int[]{
		KeyEvent.KEYCODE_Q, KeyEvent.KEYCODE_W, KeyEvent.KEYCODE_E, KeyEvent.KEYCODE_R, KeyEvent.KEYCODE_T, KeyEvent.KEYCODE_Y,KeyEvent.KEYCODE_U,KeyEvent.KEYCODE_I,KeyEvent.KEYCODE_O,KeyEvent.KEYCODE_P,
		KeyEvent.KEYCODE_A,KeyEvent.KEYCODE_S,KeyEvent.KEYCODE_D,KeyEvent.KEYCODE_F,KeyEvent.KEYCODE_G,KeyEvent.KEYCODE_H,KeyEvent.KEYCODE_J,KeyEvent.KEYCODE_K,KeyEvent.KEYCODE_L,
		KeyEvent.KEYCODE_Z,KeyEvent.KEYCODE_X,KeyEvent.KEYCODE_C,KeyEvent.KEYCODE_V,KeyEvent.KEYCODE_B,KeyEvent.KEYCODE_N,KeyEvent.KEYCODE_M
	};
	
	//See 'getSequenceCharacter' function for usage for msSequenceLivingTime and mLastTypedKeyEventTime.
	//private static final long msSequenceLivingTime = 600;
	private long mLastTypedKeyEventTime;
	private final KeyEventStateMachine mCurrentSequence;
	
	public HardKeyboardSequenceHandler()
	{
		mCurrentSequence = new KeyEventStateMachine();
		mLastTypedKeyEventTime = System.currentTimeMillis();
	}

	public void addQwertyTranslation(String targetCharacters)
	{
		if (msQwerty.length != targetCharacters.length())
			throw new InvalidParameterException("'targetCharacters' should be the same lenght as the latin QWERTY keys strings: "+msQwerty);
		for(int qwertyIndex=0; qwertyIndex<msQwerty.length; qwertyIndex++)
		{
			char latinCharacter = (char)msQwerty[qwertyIndex];
			char otherCharacter = targetCharacters.charAt(qwertyIndex);
			if (otherCharacter > 0) {
				this.addSequence(new int[] { latinCharacter }, otherCharacter);
				this.addSequence(new int[] { KeyCodes.SHIFT, latinCharacter }, Character.toUpperCase(otherCharacter) );
			}
		}
	}

	public void addSequence(int[] sequence, Intent intent) {
		this.mCurrentSequence.addSequence(sequence, intent);
	}
	
	public void addSequence(int[] sequence, int result) {
		this.mCurrentSequence.addSequence(sequence, result);
	}
	
	public void addShiftSequence(int[] sequence, int result) {
		this.mCurrentSequence.addSpecialKeySequence(sequence, KeyCodes.SHIFT, result);
	}

	public void addAltSequence(int[] sequence, int result) {
		this.mCurrentSequence.addSpecialKeySequence(sequence, KeyCodes.ALT, result);
	}
	
	
	private State addNewKey(int currentKeyEvent) {
		//sequence does not live forever!
		//I say, let it live for msSequenceLivingTime milliseconds.
		long currentTime = System.currentTimeMillis();
		if ((currentTime - mLastTypedKeyEventTime) >= AnyApplication.getConfig().getMultiTapTimeout())
			mCurrentSequence.reset();
		mLastTypedKeyEventTime = currentTime;
		return mCurrentSequence.addKeyCode(currentKeyEvent);
	}

	public boolean addSpecialKey(int currentKeyEvent) {
		State result = this.addNewKey(currentKeyEvent);
		return (result != State.RESET);
	}
	
	public int getCurrentCharacter(int currentKeyEvent, AnyKeyboardContextProvider inputHandler) {
		State result = this.addNewKey(currentKeyEvent);
		if (result == State.FULLMATCH || result == State.PARTMATCH) {
			int mappedChar = mCurrentSequence.getCharacter();
			if (mappedChar < 0)
			{
				Intent intent = mCurrentSequence.getIntentForKey(mappedChar);
				if (intent != null)
				{
					inputHandler.getApplicationContext().sendBroadcast(intent);
				}
			}
			final int charactersToDelete = mCurrentSequence.getSequenceLength() - 1;
			
			if (charactersToDelete > 0)
				inputHandler.deleteLastCharactersFromInput(charactersToDelete);
			return mappedChar;
		}
		return 0;
	}
	
	
	
	private static final String XML_TRANSLATION_TAG = "PhysicalTranslation";
	private static final String XML_QWERTY_ATTRIBUTE = "QwertyTranslation";
	private static final String XML_SEQUENCE_TAG = "SequenceMapping";
	private static final String XML_KEYS_ATTRIBUTE = "keySequence";
	private static final String XML_ALT_ATTRIBUTE = "altModifier";
	private static final String XML_SHIFT_ATTRIBUTE = "shiftModifier";
	private static final String XML_TARGET_ATTRIBUTE = "targetChar";
	private static final String XML_TARGET_CHAR_CODE_ATTRIBUTE = "targetCharCode";
	

	public static HardKeyboardSequenceHandler createPhysicalTranslatorFromXmlPullParser(XmlPullParser parser) throws NumberFormatException, XmlPullParserException, IOException {
		HardKeyboardSequenceHandler translator = new HardKeyboardSequenceHandler();
		
		final String TAG = "ASK Hard Translation Parser";
		
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
                	if (qwerty != null)
                		translator.addQwertyTranslation(qwerty);
                }
                else if (inTranslations && XML_SEQUENCE_TAG.equals(tag))
                {
                	//if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Starting parsing "+XML_SEQUENCE_TAG);
                	AttributeSet attrs = Xml.asAttributeSet(parser);

                	final int[] keyCodes = getKeyCodesFromPhysicalSequence(attrs.getAttributeValue(null, XML_KEYS_ATTRIBUTE));
                	final boolean isAlt = attrs.getAttributeBooleanValue(null, XML_ALT_ATTRIBUTE, false);
                	final boolean isShift = attrs.getAttributeBooleanValue(null, XML_SHIFT_ATTRIBUTE, false);
                	final String targetChar = attrs.getAttributeValue(null, XML_TARGET_ATTRIBUTE);
                	final String targetCharCode = attrs.getAttributeValue(null, XML_TARGET_CHAR_CODE_ATTRIBUTE);
                    final Integer target;
                    if (targetCharCode == null)
                    	target = new Integer((int)targetChar.charAt(0));
                    else
                    	target = new Integer(Integer.parseInt(targetCharCode));
                    	
                	//asserting
                    if ((keyCodes == null) || (keyCodes.length == 0) || (target == null))
                    {
                        Log.e(TAG, "Physical translator sequence does not include mandatory fields "+XML_KEYS_ATTRIBUTE+" or "+XML_TARGET_ATTRIBUTE);
                    }
                    else
                    {
                    	if (!isAlt && !isShift)
                    	{
                        	//if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Physical translation details: keys:"+printInts(keyCodes)+" target:"+target);
                        	translator.addSequence(keyCodes, target.intValue());
                    	}
                    	else if (isAlt)
                    	{
                    		//if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Physical translation details: ALT+key:"+keyCode+" target:"+target);
                        	translator.addAltSequence(keyCodes, target.intValue());
                    	}
                    	else if (isShift)
                    	{
                    		//if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Physical translation details: ALT+key:"+keyCode+" target:"+target);
                        	translator.addShiftSequence(keyCodes, target.intValue());
                    	}
                    }
                }
            }
            else if (event == XmlPullParser.END_TAG) {
            	if (XML_TRANSLATION_TAG.equals(tag)) {
                	inTranslations = false;
                	//if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Finished parsing "+XML_TRANSLATION_TAG);
                	break;
                }
            	else if (inTranslations && XML_SEQUENCE_TAG.equals(tag))
                {
            		//if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Finished parsing "+XML_SEQUENCE_TAG);
                }
            }
        }
		return translator;
	}

	private static int[] getKeyCodesFromPhysicalSequence(String keyCodesArray) {
		String[] splitted = keyCodesArray.split(",");
		int[] keyCodes = new int[splitted.length];
		for (int i = 0; i < keyCodes.length; i++) {
			try {
				keyCodes[i] = Integer.parseInt(splitted[i]);//try parsing as an integer
			} catch (final NumberFormatException nfe) {//no an integer
				final String v = splitted[i];
				try {
					keyCodes[i] = android.view.KeyEvent.class.getField(v)
							.getInt(null);//here comes the reflection. No bother of performance.
					//First hit takes just 20 milliseconds, the next hits <2 Milliseconds.
				} catch (final Exception ex) {//crap :(
					throw new RuntimeException(ex);//bum
				}
			}
		}

		return keyCodes;
	}
}
