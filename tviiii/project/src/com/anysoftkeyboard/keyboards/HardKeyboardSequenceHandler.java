package com.anysoftkeyboard.keyboards;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.anysoftkeyboard.api.KeyCodes;

import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
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

	private static final String TAG = "HardKeyboardSequenceHandler";

	private static final String ASK_REQUIRED_TO_START_ACTIVITY_THIS_INTENT = "ASK_REQUIRED_TO_START_ACTIVITY_THIS_INTENT";
	
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

	public void addSequence(int[] sequence, List<Intent> intents) {
		Log.d(TAG, "seq "+sequence[0]+" will produce intents "+intents.size());
		this.mCurrentSequence.addSequence(sequence, intents);
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
				List<Intent> intents = mCurrentSequence.getIntentForKey(mappedChar);
				if (intents != null && intents.size() > 0)
				{
					for(Intent intent : intents)
					{
						if (TextUtils.isEmpty(intent.getStringExtra(ASK_REQUIRED_TO_START_ACTIVITY_THIS_INTENT)))
						{
							Log.d(TAG, "Broadcasting intent "+intent);
							inputHandler.getApplicationContext().sendBroadcast(intent);
						}
						else
						{
							Log.d(TAG, "startActivity intent "+intent);
							inputHandler.getApplicationContext().startActivity(intent);
						}
					}
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
	private static final String XML_TARGET_KEY_CODE_ATTRIBUTE = "targetKeyCode";
	

	public static HardKeyboardSequenceHandler createPhysicalTranslatorFromXmlPullParser(XmlPullParser parser, Resources resources) throws NumberFormatException, XmlPullParserException, IOException {
		HardKeyboardSequenceHandler translator = new HardKeyboardSequenceHandler();
		
		final String TAG = "ASK Hard Translation Parser";
		
		int event;
        boolean inTranslations = false;
        boolean inSequence = false;
        boolean inIntent = false;
        
        int[] keyCodes = null;
    	boolean isAlt = false;
    	boolean isShift = false;
    	String targetChar = null;
    	String targetCharCode = null;
    	String targetKeyCode = null;
    	List<Intent> intents = null;
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
                	inSequence = true;
                	AttributeSet attrs = Xml.asAttributeSet(parser);
                	
                	intents =  new ArrayList<Intent>();
                	
                	keyCodes = getKeyCodesFromPhysicalSequence(attrs.getAttributeValue(null, XML_KEYS_ATTRIBUTE));
                	isAlt = attrs.getAttributeBooleanValue(null, XML_ALT_ATTRIBUTE, false);
                	isShift = attrs.getAttributeBooleanValue(null, XML_SHIFT_ATTRIBUTE, false);
                	targetChar = attrs.getAttributeValue(null, XML_TARGET_ATTRIBUTE);
                	targetCharCode = attrs.getAttributeValue(null, XML_TARGET_CHAR_CODE_ATTRIBUTE);
                	targetKeyCode = attrs.getAttributeValue(null, XML_TARGET_KEY_CODE_ATTRIBUTE);
                }
                else if (inTranslations && inSequence && "intent".equals(tag))
                {
                	inIntent = true;
                	AttributeSet attrs = Xml.asAttributeSet(parser);
                	String startActivity = attrs.getAttributeValue(null, "startActivity");
                	String action = attrs.getAttributeValue(null, "action");
                	String pkg = attrs.getAttributeValue(null, "package");
                	String activityClass = attrs.getAttributeValue(null, "class");
                	
                	final Intent intent;
                	if (!TextUtils.isEmpty(action))
                	{
                		intent = new Intent(action);
                	}
                	else if (!TextUtils.isEmpty(pkg) && !TextUtils.isEmpty(activityClass))
                	{
                		intent = new Intent();
                		intent.setClassName(pkg, activityClass);
                	}
                	else throw new RuntimeException("Intent should either have 'action' or 'package' and 'class' attributes!");
                	
                	if (!TextUtils.isEmpty(startActivity) && startActivity.equals("true"))
                		intent.putExtra(ASK_REQUIRED_TO_START_ACTIVITY_THIS_INTENT, ASK_REQUIRED_TO_START_ACTIVITY_THIS_INTENT);
                	
                	intents.add(intent);
                }
                else if (inTranslations && inSequence && inIntent && "extra".equals(tag))
                {
                	AttributeSet attrs = Xml.asAttributeSet(parser);
                	/*
                	<intent
        			android:action="com.tviiii.rf.SEND">
        			<extra android:name="RF_FREQ" android:value="0x67FF" />
        		</intent>*/
                	String name = attrs.getAttributeValue(null, "name");
                	String value = attrs.getAttributeValue(null, "value");
                	Intent intent = intents.get(intents.size() - 1);
                	intent.putExtra(name, value);
                }
                else if (inTranslations && inSequence && inIntent && "category".equals(tag))
                {
                	AttributeSet attrs = Xml.asAttributeSet(parser);
                	/*
                	<intent
        			android:action="com.tviiii.rf.SEND">
        			<extra android:name="RF_FREQ" android:value="0x67FF" />
        		</intent>*/
                	String value = attrs.getAttributeValue(null, "value");
                	Intent intent = intents.get(intents.size() - 1);
                	intent.addCategory(value);
                }
            }
            else if (event == XmlPullParser.END_TAG) {
            	if (XML_TRANSLATION_TAG.equals(tag)) {
                	inTranslations = false;
                	//if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) Log.d(TAG, "Finished parsing "+XML_TRANSLATION_TAG);
                	break;
                }
            	else if (inIntent && "intent".equals(tag))
            	{
            		inIntent = false;
            	}
            	else if (inTranslations && XML_SEQUENCE_TAG.equals(tag))
                {
            		inSequence = false;
            		
            		final Integer target;
                    if (targetChar != null)
                    {
                    	target = new Integer((int)targetChar.charAt(0));
                    }
                    else if (targetCharCode != null)
                    {
                    	target = new Integer(Integer.parseInt(targetCharCode));
                    }
                    else if (targetKeyCode != null)
                    {
                    	int[] keyCodesValues = getKeyCodesFromPhysicalSequence(targetKeyCode);
                    	if (keyCodes.length > 0)
                    	{
                    		KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCodesValues[0]);
                    		target = new Integer(keyEvent.getUnicodeChar());
                    	}
                    	else
                    	{
                    		target = -1;
                    	}
                    }
                    else
                    {
                    	target = -1;
                    }
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
                    		if (intents.size() > 0)
                    		{
                    			translator.addSequence(keyCodes, intents);
                    		}
                    		else
                    		{
                    			translator.addSequence(keyCodes, target.intValue());
                    		}
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
