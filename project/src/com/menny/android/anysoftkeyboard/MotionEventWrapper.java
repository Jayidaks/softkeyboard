package com.menny.android.anysoftkeyboard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;
import android.view.MotionEvent;

public class MotionEventWrapper {
	private static String TAG = "ASK";
	private static Method mMotionEvent_getPointerCount;

	static {
		initCompatibility();
	};

	private static void initCompatibility() {
		try {
			mMotionEvent_getPointerCount = MotionEvent.class.getMethod(
					"getPointerCount", (Class[]) null);
			/* success, this is a newer device */
		} catch (NoSuchMethodException nsme) {
			/* failure, must be older device */
		}
	}

	private static int getPointerCountImpl(MotionEvent motionevent) {
		try {
			Integer i = (Integer) mMotionEvent_getPointerCount.invoke(motionevent, (Object[]) null);
			int ret = i;
			return ret;
		} catch (InvocationTargetException ite) {
			/* unexpected checked exception; wrap and re-throw */
			throw new RuntimeException(ite);
		} catch (IllegalAccessException ie) {
			Log.e(TAG, "unexpected " + ie);
		}
		return -1;
	}

	public static int getPointerCount(MotionEvent motionevent) {
		int ret = -1;
		if(isGetPointerCountSupported()){
			ret = getPointerCountImpl(motionevent);
		}
		return ret;
	}

	public static boolean isGetPointerCountSupported() {
		return mMotionEvent_getPointerCount != null;
	}

}
