/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.menny.android.anysoftkeyboard;

import android.inputmethodservice.Keyboard;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import com.menny.android.anysoftkeyboard.keyboards.AnyKeyboard;
import com.menny.android.anysoftkeyboard.keyboards.GenericKeyboard;
import com.menny.android.anysoftkeyboard.keyboards.KeyboardFactory;
import com.menny.android.anysoftkeyboard.keyboards.AnyKeyboard.HardKeyboardTranslator;
import com.menny.android.anysoftkeyboard.keyboards.KeyboardFactory.KeyboardCreator;

public class KeyboardSwitcher 
{
	public enum NextKeyboardType
	{
		Symbols,
		Alphabet,
		AlphabetSupportsPhysical
	}
    public static final int MODE_TEXT = 1;
    public static final int MODE_SYMBOLS = 2;
    public static final int MODE_PHONE = 3;
    public static final int MODE_URL = 4;
    public static final int MODE_EMAIL = 5;
    public static final int MODE_IM = 6;
//    
//    public static final int MODE_TEXT_QWERTY = 0;
//    public static final int MODE_TEXT_ALPHA = 1;
//    public static final int MODE_TEXT_COUNT = 2;
    
    AnyKeyboardView mInputView;
    AnySoftKeyboard mContext;
    
//    private LatinKeyboard mPhoneKeyboard;
//    private LatinKeyboard mPhoneSymbolsKeyboard;
//    private LatinKeyboard mSymbolsKeyboard;
//    private LatinKeyboard mSymbolsShiftedKeyboard;
//    private LatinKeyboard mQwertyKeyboard;
//    private LatinKeyboard mAlphaKeyboard;
//    private LatinKeyboard mUrlKeyboard;
//    private LatinKeyboard mEmailKeyboard;
//    private LatinKeyboard mIMKeyboard;
    
    private static final int SYMBOLS_KEYBOARD_REGULAR_INDEX = 0;
    private static final int SYMBOLS_KEYBOARD_SHIFTED_INDEX = 1;
    private static final int SYMBOLS_KEYBOARD_PHONE_INDEX = 2;
    
    private int mLastSelectedSymbolsKeyboard = 0;
    private AnyKeyboard[] mSymbolsKeyboardsArray;
    //my working keyboards
    private AnyKeyboard[] mAlphabetKeyboards = null;
    private KeyboardCreator[] mAlphabetKeyboardsCreators = null;
    
    private int mLastSelectedKeyboard = 0;
    
    //private int mMode;
    private int mImeOptions;
    //private int mTextMode = MODE_TEXT_QWERTY;
    private boolean mAlphabetMode = true;;
    private int mLastDisplayWidth;

    KeyboardSwitcher(AnySoftKeyboard context) {
        mContext = context;
    }

    void setInputView(AnyKeyboardView inputView) {
        mInputView = inputView;
        if ((mInputView != null) && (mSymbolsKeyboardsArray != null))
    		mInputView.setPhoneKeyboard(mSymbolsKeyboardsArray[SYMBOLS_KEYBOARD_PHONE_INDEX]);
    }
    
    private AnyKeyboard getSymbolsKeyboard(int keyboardIndex)
    {
    	makeKeyboards(false);
    	AnyKeyboard keyboard = mSymbolsKeyboardsArray[keyboardIndex];
    	if (keyboard == null)
    	{
	    	switch(keyboardIndex)
	    	{
	    		case SYMBOLS_KEYBOARD_REGULAR_INDEX:
	    			keyboard = new GenericKeyboard(mContext, R.xml.symbols, false, -1);
	    			break;
	    		case SYMBOLS_KEYBOARD_SHIFTED_INDEX:
	    			keyboard = new GenericKeyboard(mContext, R.xml.symbols_shift, false, -1);
	    			break;
	    		case SYMBOLS_KEYBOARD_PHONE_INDEX:
	    			keyboard = new GenericKeyboard(mContext, R.xml.simple_numbers, false, -1);
	    			if (mInputView != null)
	            		mInputView.setPhoneKeyboard(keyboard);
	    			break;
	    	}
	    	mSymbolsKeyboardsArray[keyboardIndex] = keyboard;
    	}
    	return keyboard;
    }
    
    private AnyKeyboard[] getAlphabetKeyboards()
    {
    	makeKeyboards(false);
    	return mAlphabetKeyboards;
    }
    
    synchronized void makeKeyboards(boolean force) {
        // Configuration change is coming after the keyboard gets recreated. So don't rely on that.
        // If keyboards have already been made, check if we have a screen width change and 
        // create the keyboard layouts again at the correct orientation
    	int displayWidth = mContext.getMaxWidth();
        if ((mAlphabetKeyboards != null) || (mSymbolsKeyboardsArray != null)) 
        {
        	if (AnySoftKeyboard.getDEBUG())
            	Log.d("AnySoftKeyboard", "makeKeyboards: force:"+force+" maxWidth:"+displayWidth+" mLastDisplayWidth"+mLastDisplayWidth);
            if ((!force) && (displayWidth == mLastDisplayWidth)) return;
            mAlphabetKeyboards = null;
            mSymbolsKeyboardsArray = null;
        }
        mLastDisplayWidth = displayWidth;
        
        if ((mAlphabetKeyboards == null) || (mSymbolsKeyboardsArray == null))
        {
        	Log.d("AnySoftKeyboard", "makeKeyboards: force:"+force+" maxWidth:"+displayWidth+" mLastDisplayWidth"+mLastDisplayWidth+". Creating keyboards.");
        	mContext.performLengthyOperation(R.string.lengthy_creating_keyboard_operation, 
        			new Runnable()
        	{
        		public void run()
        		{
        			mAlphabetKeyboardsCreators = KeyboardFactory.createAlphaBetKeyboards(mContext);
        			mAlphabetKeyboards = new AnyKeyboard[mAlphabetKeyboardsCreators.length];
        	        if (mLastSelectedKeyboard >= mAlphabetKeyboards.length)
        	        	mLastSelectedKeyboard = 0;
        	        
        	        mSymbolsKeyboardsArray = new AnyKeyboard[3];
                	if (mLastSelectedSymbolsKeyboard >= mSymbolsKeyboardsArray.length)
                		mLastSelectedSymbolsKeyboard = 0;
                	//freeing old keyboards.
                	System.gc();
        		}
        	});
        }
    }

    void setKeyboardMode(int mode, EditorInfo attr) {
        //mMode = mode;
        mImeOptions = (attr == null)? 0 : attr.imeOptions;
        AnyKeyboard keyboard = null;
        
        switch (mode) {
        case MODE_SYMBOLS:
        case MODE_PHONE:
            keyboard = (mode == MODE_PHONE)?
            		getSymbolsKeyboard(SYMBOLS_KEYBOARD_PHONE_INDEX)
            		: getSymbolsKeyboard(0);
            mAlphabetMode = false;
            break;
//        case MODE_TEXT:
//        case MODE_URL:
//        case MODE_EMAIL:
//        case MODE_IM:
        default:
        	keyboard = getAlphabetKeyboard(mLastSelectedKeyboard);
        	mAlphabetMode = true;
        	break;            
        }
        if (mInputView != null)
        {
        	mInputView.setKeyboard(keyboard);
        	keyboard.setShifted(mInputView.isShifted());
        }
        keyboard.setShiftLocked(keyboard.isShiftLocked());
        keyboard.setImeOptions(mContext.getResources()/*, mMode*/, (attr == null)? 0 : attr.imeOptions);
        keyboard.setTextVariation(mContext.getResources(), (attr == null)? 0 : attr.inputType);
    }

//    int getKeyboardMode() {
//        return mMode;
//    }
//    
//    boolean isTextMode() {
//        return mMode == MODE_TEXT;
//    }
//    
//    int getTextMode() {
//        return mTextMode;
//    }
//    
//    void setTextMode(int position) {
//        if (position < MODE_TEXT_COUNT && position >= 0) {
//            mTextMode = position;
//        }
//        if (isTextMode()) {
//            setKeyboardMode(MODE_TEXT, mImeOptions);
//        }
//    }
//
//    int getTextModeCount() {
//        return MODE_TEXT_COUNT;
//    }

    boolean isAlphabetMode() {
    	return mAlphabetMode;
/*        Keyboard current = mInputView.getKeyboard();
        for(AnyKeyboard enabledKeyboard : mKeyboards)
        {
        	if (enabledKeyboard == current)
        		return true;
        }
        return false;*/
//        if (current == mQwertyKeyboard
//                || current == mAlphaKeyboard
//                || current == mUrlKeyboard
//                || current == mIMKeyboard
//                || current == mEmailKeyboard) {
//            return true;
//        }
//        return false;
    }

    void toggleShift() 
    {
        Keyboard currentKeyboard = mInputView.getKeyboard();
        
        //AnyKeyboard[] symbols = getSymbolsKeyboards(); 
        if (currentKeyboard == mSymbolsKeyboardsArray[SYMBOLS_KEYBOARD_REGULAR_INDEX]) 
        {
        	mLastSelectedSymbolsKeyboard = 1;
        }
        else if (currentKeyboard == mSymbolsKeyboardsArray[SYMBOLS_KEYBOARD_SHIFTED_INDEX]) 
        {
        	mLastSelectedSymbolsKeyboard = 0;
        }
        else return;
        
        AnyKeyboard nextKeyboard = getSymbolsKeyboard(mLastSelectedSymbolsKeyboard);
        boolean shiftStateToSet = currentKeyboard == mSymbolsKeyboardsArray[SYMBOLS_KEYBOARD_REGULAR_INDEX];
    	currentKeyboard.setShifted(shiftStateToSet);
        mInputView.setKeyboard(nextKeyboard);
        nextKeyboard.setShifted(shiftStateToSet);
        nextKeyboard.setImeOptions(mContext.getResources()/*, mMode*/, mImeOptions);
    }
    
    private AnyKeyboard nextAlphabetKeyboard(EditorInfo currentEditorInfo, boolean supportsPhysical)
    {
    	final int keyboardsCount = getAlphabetKeyboards().length;
    	AnyKeyboard current;
    	if (isAlphabetMode())
    		mLastSelectedKeyboard++;
    	
    	mAlphabetMode = true;
    	
    	if (mLastSelectedKeyboard >= keyboardsCount)
			mLastSelectedKeyboard = 0;
    	
    	current = getAlphabetKeyboard(mLastSelectedKeyboard);
    	//returning to the regular symbols keyboard, no matter what
    	mLastSelectedSymbolsKeyboard = 0;
    	
    	if (supportsPhysical)
    	{
    		int testsLeft = keyboardsCount;
    		while(!(current instanceof HardKeyboardTranslator) && (testsLeft > 0))
    		{
    			mLastSelectedKeyboard++;
        		if (mLastSelectedKeyboard >= keyboardsCount)
        			mLastSelectedKeyboard = 0;
        		current = getAlphabetKeyboard(mLastSelectedKeyboard);
        		testsLeft--;
    		}
    		//if we scanned all keyboards... we screwed...
    		if (testsLeft == 0)
    		{
    			Log.w("AnySoftKeyboard", "Could not locate the next physical keyboard. Will continue with "+current.getKeyboardName());
    		}
    	}
    	
    	return setKeyboard(currentEditorInfo, current);
    }

    private AnyKeyboard nextSymbolsKeyboard(EditorInfo currentEditorInfo)
    {
    	//AnyKeyboard[] symbolsKeyboards = getSymbolsKeyboards();
    	AnyKeyboard current;
    	if (!isAlphabetMode())
    		mLastSelectedSymbolsKeyboard++;
    	
    	mAlphabetMode = false;
    	
    	if (mLastSelectedSymbolsKeyboard >= mSymbolsKeyboardsArray.length)
			mLastSelectedSymbolsKeyboard = 0;
    	
    	current = getSymbolsKeyboard(mLastSelectedSymbolsKeyboard);
    	
    	return setKeyboard(currentEditorInfo, current);
    }
    
	private AnyKeyboard setKeyboard(EditorInfo currentEditorInfo,
			AnyKeyboard current) {
		if (mInputView != null)
			mInputView.setKeyboard(current);
    	//all keyboards start as un-shifted, except the second symbols
		//due to lazy loading the keyboards, the symbols may not be created yet.
    	current.setShifted(current == mSymbolsKeyboardsArray[SYMBOLS_KEYBOARD_SHIFTED_INDEX]);
    	
    	current.setImeOptions(mContext.getResources(), currentEditorInfo.imeOptions);
    	current.setTextVariation(mContext.getResources(), currentEditorInfo.inputType);
		
    	return current;
	}
    
	public AnyKeyboard getCurrentKeyboard() 
	{
		if (isAlphabetMode())
			return getAlphabetKeyboard(mLastSelectedKeyboard);
		else
			return getSymbolsKeyboard(mLastSelectedSymbolsKeyboard);
	}

	private synchronized AnyKeyboard getAlphabetKeyboard(int index) {
		AnyKeyboard[] keyboards = getAlphabetKeyboards();
		if (index >= keyboards.length)
			index = 0;
		
		AnyKeyboard keyboard = keyboards[index];
		if (keyboard == null)
		{
			KeyboardCreator creator = mAlphabetKeyboardsCreators[index];
			Log.d("AnySoftKeyboard", "About to create keyboard: "+creator.getKeyboardPrefId());
			mAlphabetKeyboards[index] = creator.createKeyboard(mContext);
			return mAlphabetKeyboards[index];
		}
		else
			return keyboard;
	}

	public AnyKeyboard nextKeyboard(EditorInfo currentEditorInfo, NextKeyboardType type) 
	{
		switch(type)
		{
			case Alphabet:
			case AlphabetSupportsPhysical:
				return nextAlphabetKeyboard(currentEditorInfo, (type == NextKeyboardType.AlphabetSupportsPhysical));
			case Symbols:
				return nextSymbolsKeyboard(currentEditorInfo);
			default:
				return nextAlphabetKeyboard(currentEditorInfo, false);
		}
	}

	public boolean isCurrentKeyboardPhysical() 
	{
		AnyKeyboard current = getCurrentKeyboard();
		return (current != null) && (current instanceof HardKeyboardTranslator);
	}

	public void onLowMemory() {
		//if I'm in alphabet mode, then we'll clear all symbols
		//else, we'll keep the current keyboard
		for(int index=0; index<mSymbolsKeyboardsArray.length; index++)
		{
			AnyKeyboard current = mSymbolsKeyboardsArray[index];
			if ((current != null) && (isAlphabetMode() || (mLastSelectedSymbolsKeyboard!=index)))
			{
				Log.i("AnySoftKeyboard", "KeyboardSwitcher::onLowMemory: Removing "+current.getKeyboardName());
				mSymbolsKeyboardsArray[index] = null;
			}
		}
		//in alphabet we are a bit cautious..
		//just removing the not selected keyboards.
		for(int index=0; index<mAlphabetKeyboards.length; index++)
		{
			AnyKeyboard current = mAlphabetKeyboards[index];
			if ((current != null) && (mLastSelectedKeyboard!=index))
			{
				Log.i("AnySoftKeyboard", "KeyboardSwitcher::onLowMemory: Removing "+current.getKeyboardName());
				mAlphabetKeyboards[index] = null;
			}
		}
	}

//    void toggleSymbols() {
//        Keyboard current = mInputView.getKeyboard();
//        if (mSymbolsKeyboard == null) {
//            mSymbolsKeyboard = new LatinKeyboard(mContext, R.xml.kbd_symbols);
//        }
//        if (mSymbolsShiftedKeyboard == null) {
//            mSymbolsShiftedKeyboard = new LatinKeyboard(mContext, R.xml.kbd_symbols_shift);
//        }
//        if (current == mSymbolsKeyboard || current == mSymbolsShiftedKeyboard) {
//            setKeyboardMode(mMode, mImeOptions); // Could be qwerty, alpha, url, email or im
//            return;
//        } else if (current == mPhoneKeyboard) {
//            current = mPhoneSymbolsKeyboard;
//            mPhoneSymbolsKeyboard.setImeOptions(mContext.getResources(), mMode, mImeOptions);
//        } else if (current == mPhoneSymbolsKeyboard) {
//            current = mPhoneKeyboard;
//            mPhoneKeyboard.setImeOptions(mContext.getResources(), mMode, mImeOptions);
//        } else {
//            current = mSymbolsKeyboard;
//            mSymbolsKeyboard.setImeOptions(mContext.getResources(), mMode, mImeOptions);
//        }
//        mInputView.setKeyboard(current);
//        if (current == mSymbolsKeyboard) {
//            current.setShifted(false);
//        }
//    }
}
