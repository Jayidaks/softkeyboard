package com.menny.android.anysoftkeyboard.test;

import junit.framework.Assert;
import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.menny.android.anysoftkeyboard.theme.Utils;



public class ThemeUtilsTest  extends InstrumentationTestCase {
	private static boolean mockTested = false;
	public class MockInflaterFactory implements LayoutInflater.Factory {
		@Override
		public View onCreateView(String name, Context context,
				AttributeSet attrs) {
			Assert.assertEquals(true, Utils.isAttributeDefined(attrs, "layout_width"));
			Assert.assertEquals(true, Utils.isAttributeDefined(attrs, "orientation"));
			Assert.assertEquals(false, Utils.isAttributeDefined(attrs, "DONOTEXIST"));
			mockTested = true;
			return null;
		}

	}
	public void testIsAttributeDefined(){
		Context context = getInstrumentation().getContext();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.setFactory(new MockInflaterFactory());
		View view = inflater.inflate(R.layout.layout_width_test, null);
		Assert.assertEquals(true, mockTested);
//				//the new layout will solve the "invalidateAllKeys" problem.
		//isAttributeDefined(AttributeSet attrs, String attr);
	}
}
