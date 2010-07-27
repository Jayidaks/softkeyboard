/*
 * Copyright (C) 2008-2009 Google Inc.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.menny.android.anysoftkeyboard.keyboards.AnyKeyboard;

public class AnyKeyboardView extends KeyboardView {

	public interface OnAnyKeyboardActionListener extends OnKeyboardActionListener
	{
		void startInputConnectionEdit();
		void endInputConnectionEdit();
	}
	private final static String TAG = "ASK AnyKeyboardView";
	static final int KEYCODE_OPTIONS = -100;
    //static final int KEYCODE_SHIFT_LONGPRESS = -101;
	static final int KEYCODE_SMILEY_LONGPRESS = -102;
    
    private Keyboard mPhoneKeyboard;

    public AnyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeStuff();
    }

	private void initializeStuff() {
		setPreviewEnabled(AnySoftKeyboardConfiguration.getInstance().getShowKeyPreview());
		setProximityCorrectionEnabled(true);
		//the vendor will add a function to the KeyboardView
		//which will allow keys with labels longer than 2 to also participate in
		//the shifted/unshifted label upper/lowercase switch
		try {
			Method ignorer = getClass().getMethod("ignoreLengthConditionCheck");
			ignorer.invoke(this);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public AnyKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeStuff();
    }
    
    private final Object mTouchLock = new Object();
    @Override
    public boolean onTouchEvent(MotionEvent me) {
    	synchronized (mTouchLock) {
    		OnAnyKeyboardActionListener ime = (OnAnyKeyboardActionListener)getOnKeyboardActionListener();
    		try
    		{
    			ime.startInputConnectionEdit();
    			return super.onTouchEvent(me);
    		}
    		catch(ArrayIndexOutOfBoundsException ex)
    		{
    			//due to an Android bug (see KeyboardView class functions 'getKeyIndices' - usage of arrayCopy
    			//and 'detectAndSendKey' usage of mTapCount) 
    			Log.w(TAG, "Got ArrayIndexOutOfBoundsException, and ignoring.");
    			ex.printStackTrace();
    			return true;
    		}
    		finally
    		{
    			ime.endInputConnectionEdit();
    		}
		}
    }
//    
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//    	Log.d(TAG, "onKeyDown:"+keyCode);
//    	return super.onKeyDown(keyCode, event);
//    }
//    
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//    	Log.d(TAG, "onKeyUp:"+keyCode);
//    	return super.onKeyUp(keyCode, event);
//    }
    
    public void setPhoneKeyboard(Keyboard phoneKeyboard) {
        mPhoneKeyboard = phoneKeyboard;
    }
    
    @Override
    protected boolean onLongPress(Key key) {
        if (key.codes[0] == 10) {
            getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null);
            return true;
        } else if (key.codes[0] == AnyKeyboard.KEYCODE_SMILEY) {
            getOnKeyboardActionListener().onKey(KEYCODE_SMILEY_LONGPRESS, null);
            return true;
        } else if (key.codes[0] == Keyboard.KEYCODE_SHIFT) {
          getOnKeyboardActionListener().onKey(AnyKeyboard.KEYCODE_LANG_CHANGE, null);
          return true;
        }else if (key.codes[0] == AnyKeyboard.KEYCODE_LANG_CHANGE) {
            getOnKeyboardActionListener().onKey(AnyKeyboard.KEYCODE_LANG_CHANGE, null);
            return true;
        } else if (key.codes[0] == '0' && getKeyboard() == mPhoneKeyboard) {
            // Long pressing on 0 in phone number keypad gives you a '+'.
            getOnKeyboardActionListener().onKey('+', null);
            return true;
        } else {
            return super.onLongPress(key);
        }
    }
    
    public void simulateLongPress(int keyCode)
    {
    	if (super.getKeyboard() == null)
    		return;
    	
    	for(Key key : super.getKeyboard().getKeys())
    	{
    		if (key.codes[0] == keyCode)
    		{
    			super.onLongPress(key);
    			return;
    		}
    	}
    }
    
    @Override
    public void setKeyboard(Keyboard keyboard) {
    	super.setKeyboard(keyboard);
    	setProximityCorrectionEnabled(((AnyKeyboard)keyboard).requiresProximityCorrection());
    }
    
    protected void requestSpecialKeysRedraw()
    {
    	super.invalidate();
    }
    
//    @Override
//    public boolean setShifted(boolean shifted) {
//    	final boolean res = super.setShifted(shifted);
//    	if (isShown())
//    		requestShiftKeyRedraw();
//    	
//    	return res;
//    }
    
    public void requestShiftKeyRedraw()
    {
    	
    	if (canInteractWithUi())
    		super.invalidate();
    }

	protected boolean canInteractWithUi() {
		IBinder ib = getWindowToken();
		return (ib != null && 
				ib.isBinderAlive() &&//has not been disposed 
				(this.getWidth() > 0));//the GUI has already been computed
	}
}

