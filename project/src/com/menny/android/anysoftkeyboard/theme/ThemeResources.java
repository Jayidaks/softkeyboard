package com.menny.android.anysoftkeyboard.theme;

/*
 * Copyright (C) 2008 Esmertec AG.
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.menny.android.anysoftkeyboard.AnySoftKeyboardConfiguration;
import com.menny.android.anysoftkeyboard.R;
import com.menny.android.anysoftkeyboard.dictionary.ExternalDictionaryFactory.DictionaryBuilder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * The provider specific branding resources.
 */
public class ThemeResources {
	private static final String TAG = "ASK_ThemeResources";

	private Map<Integer, Integer> mResMapping;
	private Resources mPackageRes;

	private ThemeResources mDefaultRes;

	private static String XML_RESOURCES_TAG = "resources";
	private static String XML_KEYBOARD_VIEW_STYLE_TAG = "android.inputmethodserice.KeyboardView";

	private Drawable mKeyBackground;

	private Integer mVerticalCorrection;

	private Integer mPreviewOffset;

	private Integer mPreviewHeight;

	private Integer mKeyTextSize;

	private Integer mKeyTextColor;

	private Integer mLabelTextSize;

	private Integer mPopupLayout;

	private Integer mShadowColor;

	private Float mShadowRadius;

	private Float mBackgroundDimAmount;

	private PopupWindow mPreviewPopup;

	private Boolean mShowPreview;

	private Integer mPreviewTextSizeLarge;

	private TextView mPreviewText;


	public void parseResMappingFromXml(Context context, XmlPullParser parser) {

//		int previewLayout = 0;
		TypedArray a = null;

		try {
			int event;
//			boolean inMappings = false;
			while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
				final String tag = parser.getName();
				if (event == XmlPullParser.START_TAG) {
					if (XML_KEYBOARD_VIEW_STYLE_TAG.equals(tag)) {
						// inMappings = true;
						if (AnySoftKeyboardConfiguration.getInstance()
								.getDEBUG()) {
						 	Log.d(TAG, "Starting parsing " + XML_RESOURCES_TAG);
						}
						AttributeSet attrs = Xml.asAttributeSet(parser);

						//TODO: xml parsing instead of styledattributes
						// styledattributes has many good features:
						// * parses references correctly @android:color/transparent, but also allow in-place values (important for better
						// keybard compatibility)

						// BUT styled attributes are not portable and I think they cannot be used in a 3rd party component
						// (I couldn't make it work)

						// so we need to do the heavylifting our selves,
						// parsing the xml, and we have to understand references too

						// Problem with xml parsing, how to interpret dimension units if they are given in-place?
						a = context.obtainStyledAttributes(attrs,
								R.styleable.KeyboardView);
//
//						Resources res = context.getResources();
//						DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//						int n = attrs.getAttributeCount();
//
//						for (int i = 0; i < n; i++) {
//							String name = attrs.getAttributeName(i);
//							int referenceId = attrs.getAttributeResourceValue(i, 0);
//							boolean isReference = (referenceId != 0);
//
//							if("keyBackground".equals(name)){
//								if(isReference) {
//									mKeyBackground = res.getDrawable(referenceId);
//								}
//							}else if("verticalCorrection".equals(name)){
//								if(isReference) {
//									res.getDimensionPixelOffset(referenceId);
//								}else {
//
//								}
//							}else if("keyPreviewLayout".equals(name)){
//
//							}else if("keyPreviewOffset".equals(name)){
//
//							}else if("keyPreviewHeight".equals(name)){
//
//							}else if("keyTextSize".equals(name)){
//
//							}else if("keyTextColor".equals(name)){
//
//							}else if("labelTextSize".equals(name)){
//
//							}else if("popupLayout".equals(name)){
//
//							}else if("shadowColor".equals(name)){
//
//							}else if("shadowRadius".equals(name)){
//
//							}
//
//						}

						int n = a.getIndexCount();

						for (int i = 0; i < n; i++) {
							int attr = a.getIndex(i);

							switch (attr) {
							case R.styleable.KeyboardView_keyBackground:
								Log.d(TAG, "KeyboardView_keyBackground");
								mKeyBackground = a.getDrawable(attr);
								break;
							case R.styleable.KeyboardView_verticalCorrection:
								Log.d(TAG, "KeyboardView_verticalCorrection");
								mVerticalCorrection = a
										.getDimensionPixelOffset(attr, 0);
								break;
							case R.styleable.KeyboardView_keyPreviewLayout:
								Log.d(TAG, "KeyboardView_keyPreviewLayout");
								int previewLayout = a.getResourceId(attr, 0);

								mPreviewPopup = new PopupWindow(context);
								if (previewLayout != 0) {
									LayoutInflater inflater = (LayoutInflater) context
											.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
									mPreviewText = (TextView) inflater.inflate(
											previewLayout, null);
									mPreviewTextSizeLarge = (int) mPreviewText
											.getTextSize();
									mPreviewPopup.setContentView(mPreviewText);
									mPreviewPopup.setBackgroundDrawable(null);
									mShowPreview = true;
								} else {
									mShowPreview = false;
								}

								break;
							case R.styleable.KeyboardView_keyPreviewOffset:
								Log.d(TAG, "KeyboardView_keyPreviewOffset");
								mPreviewOffset = a.getDimensionPixelOffset(
										attr, Integer.MIN_VALUE);
								if (mPreviewOffset == Integer.MIN_VALUE) {
									mPreviewOffset = null;
								}
								break;
							case R.styleable.KeyboardView_keyPreviewHeight:
								Log.d(TAG, "KeyboardView_keyPreviewHeight");
								mPreviewHeight = a.getDimensionPixelSize(attr,
										Integer.MIN_VALUE);
								if (mPreviewHeight == Integer.MIN_VALUE) {
									mPreviewHeight = null;
								}
								break;
							case R.styleable.KeyboardView_keyTextSize:
								Log.d(TAG, "KeyboardView_keyTextSize");
								mKeyTextSize = a.getDimensionPixelSize(attr,
										Integer.MIN_VALUE);
								if (mKeyTextSize == Integer.MIN_VALUE) {
									mKeyTextSize = null;
								}
								break;
							case R.styleable.KeyboardView_keyTextColor:
								Log.d(TAG, "KeyboardView_keyTextColor");
								mKeyTextColor = a.getColor(attr,
										Integer.MIN_VALUE);
								if (mKeyTextColor == Integer.MIN_VALUE) {
									mKeyTextColor = null;
								}
								break;
							case R.styleable.KeyboardView_labelTextSize:
								Log.d(TAG, "KeyboardView_labelTextSize");
								mLabelTextSize = a.getDimensionPixelSize(attr,
										Integer.MIN_VALUE);
								if (mLabelTextSize == Integer.MIN_VALUE) {
									mLabelTextSize = null;
								}
								break;
							case R.styleable.KeyboardView_popupLayout:
								Log.d(TAG, "KeyboardView_popupLayout");
								mPopupLayout = a.getResourceId(attr,
										Integer.MIN_VALUE);
								if (mPopupLayout == Integer.MIN_VALUE) {
									mPopupLayout = null;
								}
								break;
							case R.styleable.KeyboardView_shadowColor:
								Log.d(TAG, "KeyboardView_shadowColor");
								mShadowColor = a.getColor(attr,
										Integer.MIN_VALUE);
								if (mShadowColor == Integer.MIN_VALUE) {
									mShadowColor = null;
								}
								break;
							case R.styleable.KeyboardView_shadowRadius:
								Log.d(TAG, "KeyboardView_shadowRadius");
								mShadowRadius = a.getFloat(attr, Float.NaN);
								if (mShadowRadius == Float.NaN) {
									mShadowRadius = null;
								}
								break;
							}
						}

						break;
					}

				}
//				else if (event == XmlPullParser.END_TAG) {
//					if (XML_RESOURCES_TAG.equals(tag)) {
//						inMappings = false;
//						if (AnySoftKeyboardConfiguration.getInstance()
//								.getDEBUG()) {
//							Log.d(TAG, "Finished parsing " + XML_RESOURCES_TAG);
//						}
//						break;
//					}
//					// else if (inMappings && XML_RESMAPPING_TAG.equals(tag)) {
//					// if (AnySoftKeyboardConfiguration.getInstance()
//					// .getDEBUG()) {
//					// Log
//					// .d(TAG, "Finished parsing "
//					// + XML_RESMAPPING_TAG);
//					// }
//					// }
//				}



			} // while-loop
		} catch (final IOException e) {
			Log.e(TAG, "IO error:" + e);
			e.printStackTrace();
		} catch (final XmlPullParserException e) {
			Log.e(TAG, "Parse error:" + e);
			e.printStackTrace();
		}

		a = context.obtainStyledAttributes(R.styleable.Theme);
		mBackgroundDimAmount = a.getFloat(
				R.styleable.Theme_backgroundDimAmount, 0.5f);
	}

	/**
	 * Creates a new ThemeResources of a specific plug-in.
	 *
	 * @param context
	 *            The current application context.
	 * @param pluginInfo
	 *            The info about the plug-in.
	 * @param defaultRes
	 *            The default branding resources. If the resource is not found
	 *            in the plug-in, the default resource will be returned.
	 */
	public ThemeResources(Context context, ThemePluginInfo pluginInfo,
			ThemeResources defaultRes) {
		mDefaultRes = defaultRes;

		PackageManager pm = context.getPackageManager();
		try {
			mPackageRes = pm
					.getResourcesForApplication(pluginInfo.mPackageName);
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Can not load resources from package: "
					+ pluginInfo.mPackageName);
		}
		Intent intent = new Intent("com.menny.android.anysoftkeyboard.THEME");
		//TODO: FIX
		// intent.setComponent(new ComponentName(pluginInfo.mPackageName,
		// pluginInfo.mProviderName));

		List<ResolveInfo> receivers = pm.queryBroadcastReceivers(intent,
				PackageManager.GET_META_DATA);

		if (receivers.size() == 0)
			Log
					.e(
							TAG,
							MessageFormat
									.format(
											"Could not find receivers from the specified package: {0}/{1}",
											new Object[] {
													pluginInfo.mPackageName,
													pluginInfo.mProviderName }));

		ActivityInfo ai = receivers.get(0).activityInfo;
		Context externalContext = null;
		try {
			externalContext = context.createPackageContext(ai.packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final XmlPullParser theme = ai.loadXmlMetaData(context
				.getPackageManager(),
				"com.menny.android.anysoftkeyboard.themes");

		parseResMappingFromXml(externalContext, theme);
	}

	/**
	 * Creates a new ThemeResources from xml parser
	 *
	 * @param context
	 * @param parser
	 * @param defaultRes
	 */
	public ThemeResources(Context context, XmlPullParser parser,
			ThemeResources defaultRes) {
		mDefaultRes = defaultRes;
		mPackageRes = context.getResources();

		parseResMappingFromXml(context, parser);
	}


	/**
	 * @return the mKeyBackground
	 */
	public Drawable getKeyBackground() {
		if (mKeyBackground != null) {
			return mKeyBackground;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getKeyBackground();
		} else {
			Log.w(TAG, "Could not find suitable KeyBackground.");
			return null;
		}
	}

	/**
	 * @return the mVerticalCorrection
	 */
	public Integer getVerticalCorrection() {
		if (mVerticalCorrection != null) {
			return mVerticalCorrection;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getVerticalCorrection();
		} else {
			Log.w(TAG, "Could not find suitable VerticalCorrection.");
			return 0;
		}
	}

	/**
	 * @return the mPreviewOffset
	 */
	public Integer getPreviewOffset() {
		if (mPreviewOffset != null) {
			return mPreviewOffset;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getPreviewOffset();
		} else {
			Log.w(TAG, "Could not find suitable PreviewOffset.");
			return 0;
		}
	}

	/**
	 * @return the mPreviewHeight
	 */
	public Integer getPreviewHeight() {
		if (mPreviewHeight != null) {
			return mPreviewHeight;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getPreviewHeight();
		} else {
			Log.w(TAG, "Could not find suitable PreviewHeight.");
			return 80;
		}
	}

	/**
	 * @return the mKeyTextSize
	 */
	public Integer getKeyTextSize() {
		if (mKeyTextSize != null) {
			return mKeyTextSize;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getKeyTextSize();
		} else {
			Log.w(TAG, "Could not find suitable KeyTextSize.");
			return 18;
		}
	}

	/**
	 * @return the mKeyTextColor
	 */
	public Integer getKeyTextColor() {
		if (mKeyTextColor != null) {
			return mKeyTextColor;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getKeyTextColor();
		} else {
			Log.w(TAG, "Could not find suitable KeyTextColor.");
			return 0xFF000000;
		}
	}

	/**
	 * @return the mLabelTextSize
	 */
	public Integer getLabelTextSize() {
		if (mLabelTextSize != null) {
			return mLabelTextSize;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getLabelTextSize();
		} else {
			Log.w(TAG, "Could not find suitable LabelTextSize.");
			return 14;
		}
	}

	/**
	 * @return the mPopupLayout
	 */
	public Integer getPopupLayout() {
		if (mPopupLayout != null) {
			return mPopupLayout;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getPopupLayout();
		} else {
			Log.w(TAG, "Could not find suitable PopupLayout.");
			return 0;
		}
	}

	/**
	 * @return the mShadowColor
	 */
	public Integer getShadowColor() {
		if (mShadowColor != null) {
			return mShadowColor;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getShadowColor();
		} else {
			Log.w(TAG, "Could not find suitable ShadowColor.");
			return 0;
		}
	}

	/**
	 * @return the mShadowRadius
	 */
	public Float getShadowRadius() {
		if (mShadowRadius != null) {
			return mShadowRadius;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getShadowRadius();
		} else {
			Log.w(TAG, "Could not find suitable getShadowRadius.");
			return 0f;
		}
	}

	/**
	 * @return the mPreviewPopup
	 */
	public PopupWindow getPreviewPopup() {
		if (mPreviewPopup != null) {
			return mPreviewPopup;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getPreviewPopup();
		} else {
			Log.w(TAG, "Could not find suitable PreviewPopup.");
			return null;
		}
	}

	/**
	 * @return the mShowPreview
	 */
	public Boolean getShowPreview() {
		if (mShowPreview != null) {
			return mShowPreview;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getShowPreview();
		} else {
			Log.w(TAG, "Could not find suitable ShowPreview.");
			return true;
		}
	}

	/**
	 * @return the mPreviewTextSizeLarge
	 */
	public Integer getPreviewTextSizeLarge() {
		if (mPreviewTextSizeLarge != null) {
			return mPreviewTextSizeLarge;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getPreviewTextSizeLarge();
		} else {
			Log.w(TAG, "Could not find suitable PreviewTextSizeLarge.");
			return 0;
		}
	}

	/**
	 * @return the mPreviewText
	 */
	public TextView getPreviewText() {
		if (mPreviewText != null) {
			return mPreviewText;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getPreviewText();
		} else {
			Log.w(TAG, "Could not find suitable PreviewText.");
			return null;
		}
	}

}