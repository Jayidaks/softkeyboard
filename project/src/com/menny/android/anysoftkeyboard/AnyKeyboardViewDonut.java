package com.menny.android.anysoftkeyboard;

import com.menny.android.anysoftkeyboard.theme.ThemeResources;

import android.content.Context;
import android.util.AttributeSet;

public class AnyKeyboardViewDonut extends AnyKeyboardView
{



//	public AnyKeyboardViewDonut(Context askContext, Context externalContext,
//			AttributeSet attrs, int defStyle) {
//		super(askContext, externalContext, attrs, defStyle);
//	}
//
//	public AnyKeyboardViewDonut(Context askContext, Context externalContext,
//			AttributeSet attrs) {
//		super(askContext, externalContext, attrs);
//	}

//	public AnyKeyboardViewDonut(Context context, AttributeSet attrs,
//			int defStyle) {
//		super(context, attrs, defStyle);
//		//  Auto-generated constructor stub
//	}
//
//	public AnyKeyboardViewDonut(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		//  Auto-generated constructor stub
//	}

	public AnyKeyboardViewDonut(Context askContext, ThemeResources resoures,
			AttributeSet attrs, int defStyle) {
		super(askContext, resoures, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AnyKeyboardViewDonut(Context askContext, ThemeResources resources,
			AttributeSet attrs) {
		super(askContext, resources, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void requestRedraw() {
		super.invalidateAllKeys();
	}
}