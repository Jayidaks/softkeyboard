package com.menny.android.anysoftkeyboard.theme;

import com.menny.android.anysoftkeyboard.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class PopupKeyboardLayoutInflaterFactory implements Factory {

	private ThemeResources mResources;
	private static final String THEMEABLE_KEYBOARDVIEW_CLASS = ThemeableKeyboardView.class.getCanonicalName();
	private static final String CLOSE_BUTTON_TAG_NAME = "com.menny.android.anysoftkeyboard.theme.PopupKeyboardCloseButton";
	private static final String LINEAR_LAYOUT_TAG_NAME = "com.menny.android.anysoftkeyboard.theme.PopupKeyboardLinearLayout";

	public PopupKeyboardLayoutInflaterFactory(ThemeResources resources) {
		mResources = resources;

	}

	public View onCreateView(String name, Context context, AttributeSet attrs) {
		if(THEMEABLE_KEYBOARDVIEW_CLASS.equals(name)){
			// Put mThemeResources as a fall back option
			ThemeResources theme = new ThemeResources(context, attrs, mResources);

			View view = new ThemeableKeyboardView(context, theme, attrs);
			// Put on an ID on it so we can find it!
			view.setId(R.id.keyboardView);
			return view;
		}
		else if(CLOSE_BUTTON_TAG_NAME.equals(name)){
			ImageButton button = new ImageButton(context, attrs);
			if(button.getDrawable() == null) {
				button.setImageDrawable(mResources.getDrawable(R.id.theme_drawableKeyboardKeyClose));
			}
			// Put on an ID on it so we can find it!
			button.setId(R.id.button_close);

			return button;
		}
		else if(LINEAR_LAYOUT_TAG_NAME.equals(name)) {
			LinearLayout layout = new LinearLayout(context, attrs);
			if(layout.getBackground() == null) {
				layout.setBackgroundDrawable(mResources.getDrawable(R.id.theme_drawableKeyboardPopupPanelBackground));
			}
			return layout;
		}

		return null;
	}

}
