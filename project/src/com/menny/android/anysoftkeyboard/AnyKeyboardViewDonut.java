package com.menny.android.anysoftkeyboard;

import android.content.Context;
import android.util.AttributeSet;

public class AnyKeyboardViewDonut extends AnyKeyboardView
{

	public AnyKeyboardViewDonut(Context askContext, Context externalContext,
			AttributeSet attrs, int defStyle) {
		super(askContext, externalContext, attrs, defStyle);
	}

	public AnyKeyboardViewDonut(Context askContext, Context externalContext,
			AttributeSet attrs) {
		super(askContext, externalContext, attrs);
	}

	@Override
	public void requestRedraw() {
		super.invalidateAllKeys();
	}
}