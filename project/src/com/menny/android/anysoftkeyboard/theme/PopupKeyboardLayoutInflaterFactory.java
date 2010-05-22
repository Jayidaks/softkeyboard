package com.menny.android.anysoftkeyboard.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class PopupKeyboardLayoutInflaterFactory implements Factory {

	private ThemeResources mResources;
	private static final String THEMEABLE_KEYBOARDVIEW_CLASS = ThemeableKeyboardView.class.getCanonicalName();
//	private static final String CLOSE_BUTTON_TAG_NAME = "com.menny.android.anysoftkeyboard.theme.PopupKeyboardCloseButton";
//	private static final String LINEAR_LAYOUT_TAG_NAME = "com.menny.android.anysoftkeyboard.theme.PopupKeyboardLinearLayout";

	public PopupKeyboardLayoutInflaterFactory(ThemeResources resources) {
		mResources = resources;

	}

	public View onCreateView(String name, Context context, AttributeSet attrs) {
		if(THEMEABLE_KEYBOARDVIEW_CLASS.equals(name)){
			// Put mThemeResources as a fall back option
			ThemeResources theme = new ThemeResources(context, attrs, mResources);

			return new ThemeableKeyboardView(context, theme, attrs);
		}
//		TODO: if we have our custom close button defined, use ThemeResources to provide BackgroundDrawable if there is none given.
//		TODO: Using this method we can provide runtime decision what Drawable to use in the closebutton *without* even touching the layout.
//		TODO: better keyboard skins don't have a layout and there we must use this approach.
//		else if(CLOSE_BUTTON_TAG_NAME.equals(name)){
//			ImageButton button = new ImageButton(context, attrs);
//			if(button.getDrawable() == null) {
//				//button.setImageResource(mResources.getCloseButtonDrawable());
//			}
//
//		}
//		TODO: Linear layout should inherit background of the main keyboard, if none is defined!
//		else if(LINEAR_LAYOUT_TAG_NAME.equals(name)) {
//			LinearLayout layout = new LinearLayout(context, attrs);
//			if(layout.getBackground() == null) {
//				layout.setBackgroundDrawable(mResources.getPopupKeyboardPanelBackground())
//			}
//		}

		return null;
	}

}
