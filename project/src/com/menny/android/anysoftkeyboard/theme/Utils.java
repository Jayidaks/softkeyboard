package com.menny.android.anysoftkeyboard.theme;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class Utils {
	public static boolean isAttributeDefined(AttributeSet attrs, String attr){
		if(attrs == null || attr == null)
			return false;

		int count = attrs.getAttributeCount();
		for(int i = 0; i < count; i++){
			if(attr.equals(attrs.getAttributeName(i))) {
				return true;
			}
		}
		return false;
	}

	// See: http://www.anddev.org/xml_integer_array_resource_references_getintarray-t9268.html
	// getIntArray does not resolve references
	public static int[] getIntArrayOfReferences(Resources res, int resId){
		TypedArray ar = res.obtainTypedArray(resId);
		int len = ar.length();

		int[] resIds = new int[len];

		for (int i = 0; i < len; i++) {
		    resIds[i] = ar.getResourceId(i, 0);
		}

		ar.recycle();

		return resIds;
	}
}
