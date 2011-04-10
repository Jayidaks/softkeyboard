/*
 * Copyright (C) 2011 AnySoftKeyboard
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
package com.anysoftkeyboard.keyboards.views;


import android.content.Context;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class AnyKeyboardViewV8 extends AnyKeyboardView {

	
	public AnyKeyboardViewV8(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AnyKeyboardViewV8(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	protected String getKeyboardViewNameForLogging()
    {
    	return "AnyKeyboardViewV8";
    }
	
	protected GestureDetector createGestureDetector(GestureDetector.SimpleOnGestureListener listener) {
		final boolean ignoreMultitouch = true;
		return new GestureDetector(getContext(), listener, null, ignoreMultitouch);
	}
	
	protected boolean systemHasDistinctMultitouch(Context context) {
		PackageManager pkg = context.getPackageManager();
		return pkg.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT);
	}

	protected int getPointerIndexForAction(MotionEvent me) {
		return me.getActionIndex();
	}

	protected int getActionWithNoPointerInformation(MotionEvent me) {
		return me.getActionMasked();
	}
}
