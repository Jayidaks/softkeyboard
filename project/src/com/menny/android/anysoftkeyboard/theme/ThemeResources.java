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
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.menny.android.anysoftkeyboard.AnySoftKeyboardConfiguration;
import com.menny.android.anysoftkeyboard.R;

/**
 * The provider specific branding resources.
 */
public class ThemeResources {
	private static final String TAG = "ASK_ThemeResources";

	private static String[] msThemeDrawableAttributeNames;
	private static int[] msThemeDrawableAttributeValues;
	private static String[] msThemeColorsAttributeNames;
	private static int[] msThemeColorsAttributeValues;
	private static String[] msThemeDimensionsAttributeNames;
	private static int[] msThemeDimensionsAttributeValues;
	private static String[] msThemeTypefacesAttributeNames;
	private static int[] msThemeTypefacesAttributeValues;

	// TODO add other arrays from arrays.xml

	private Map<Integer, Integer> mDrawablesMapping;
	private Map<Integer, Integer> mColorsMapping;
	private Map<Integer, Float> mDimensionsMapping;
	private Map<Integer, String> mTypefacesMapping;

	private Context mContext;

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

	private Integer mPopupLayoutResourceID;

	private Integer mShadowColor;

	private Float mShadowRadius;

	private PopupWindow mPreviewPopup;

	private Integer mPreviewTextSizeLarge;

	private TextView mPreviewText;

	private Integer mCandidatePreviewLayoutResourceID;

	private Integer mCandidateViewContainerLayoutResourceID;

	private Integer mKeyPreviewLayoutResourceID;

	// TODO: Add drawables for the keys:
	// close button, delete key, shift keys, etc.
	// Maybe it's good to use Map for this purpose to reduce boiler-plate
	// getter-code (use similar architecture as in ImPlugin in Android)
	// Map should have integers as keys (defined in ThemeResourceConstants) and
	// value is a drawable.

	private void parseResMappingFromXml(Context context,
			XmlResourceParser parser) {
		TypedArray a = null;
		try {
			int event;
			// boolean inMappings = false;
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
						parseResMappingFromXml(context, attrs);
						break;
					}

				}
			} // while-loop
		} catch (final IOException e) {
			Log.e(TAG, "IO error:" + e);
			e.printStackTrace();
		} catch (final XmlPullParserException e) {
			Log.e(TAG, "Parse error:" + e);
			e.printStackTrace();
		}

	}

	private void parseResMappingFromXml(Context context, AttributeSet attrs) {

		Resources res = context.getResources();
		int n = attrs.getAttributeCount();

		for (int i = 0; i < n; i++) {
			String name = attrs.getAttributeName(i);
			int referenceId = attrs.getAttributeResourceValue(i, 0);
			boolean isReference = (referenceId != 0);

			// XXX TODO shadowColor
			if (!isReference && "shadowColor".equals(name)) {
				Log.e(TAG, "Every theme attribute must be a reference!");
				break;
			}

			if ("keyBackground".equals(name)) {
				mKeyBackground = res.getDrawable(referenceId);

			} else if ("verticalCorrection".equals(name)) {
				mVerticalCorrection = res.getDimensionPixelOffset(referenceId);
			} else if ("keyPreviewLayout".equals(name)) {
				mKeyPreviewLayoutResourceID = referenceId;
				mPreviewPopup = new PopupWindow(context);

			} else if ("keyPreviewOffset".equals(name)) {
				mPreviewOffset = res.getDimensionPixelOffset(referenceId);
			} else if ("keyPreviewHeight".equals(name)) {
				mPreviewHeight = res.getDimensionPixelSize(referenceId);
			} else if ("keyTextSize".equals(name)) {
				mKeyTextSize = res.getDimensionPixelSize(referenceId);
			} else if ("keyTextColor".equals(name)) {
				mKeyTextColor = res.getColor(referenceId);
			} else if ("labelTextSize".equals(name)) {
				mLabelTextSize = res.getDimensionPixelSize(referenceId);
			} else if ("popupLayout".equals(name)) {
				mPopupLayoutResourceID = referenceId;
			} else if ("shadowColor".equals(name)) {
				mShadowColor = res.getColor(referenceId);
			} else if ("shadowRadius".equals(name)) {
				// TODO!!!
				// mShadowRadius
				mShadowRadius = 0f;
			}

			if ("candidatePreviewLayout".equals(name)) {
				mCandidatePreviewLayoutResourceID = referenceId;
			} else if ("candidateViewContainerLayout".equals(name)) {
				mCandidateViewContainerLayoutResourceID = referenceId;
			}

			if (msThemeDrawableAttributeNames == null) {
				Log.e(TAG, "Something wrong with initialization!");
			} else {
				int attributeCounts = msThemeDrawableAttributeNames.length;
				for (int j = 0; j < attributeCounts; j++) {
					String attribute = msThemeDrawableAttributeNames[j];
					Integer id = msThemeDrawableAttributeValues[j];
					if (attribute.equals(name)) {
						mDrawablesMapping.put(id, referenceId);
						break;
					}
				}
			}

			if (msThemeColorsAttributeNames == null) {
				Log.e(TAG, "Something wrong with initialization!");
			} else {
				int attributeCounts = msThemeColorsAttributeNames.length;
				for (int j = 0; j < attributeCounts; j++) {
					String attribute = msThemeColorsAttributeNames[j];
					Integer id = msThemeColorsAttributeValues[j];

					if (attribute.equals(name)) {
						mColorsMapping.put(id, res.getColor(referenceId));
						break;
					}
				}
			}

			if (msThemeDimensionsAttributeNames == null) {
				Log.e(TAG, "Something wrong with initialization!");
			} else {
				int attributeCounts = msThemeDimensionsAttributeNames.length;
				for (int j = 0; j < attributeCounts; j++) {
					String attribute = msThemeDimensionsAttributeNames[j];
					Integer id = msThemeDimensionsAttributeValues[j];
					if (attribute.equals(name)) {
						mDimensionsMapping.put(id, res
								.getDimension(referenceId));
						break;
					}
				}
			}

			if (msThemeTypefacesAttributeNames == null) {
				Log.e(TAG, "Something wrong with initialization!");
			} else {
				int attributeCounts = msThemeTypefacesAttributeNames.length;
				String pathToTypeface = attrs.getAttributeValue(i);
				if (pathToTypeface == null || pathToTypeface.length() == 0) {
					Log.w(TAG, "Invalid path for typeface");
					break;
				}

				for (int j = 0; j < attributeCounts; j++) {
					String attribute = msThemeTypefacesAttributeNames[j];
					Integer id = msThemeTypefacesAttributeValues[j];
					if (attribute.equals(name)) {
						mTypefacesMapping.put(id, pathToTypeface);
						break;
					}
				}
			}
		}
	}

	public static void initAttributeInfos(Context askContext) {
		if (askContext != null) {
			msThemeDrawableAttributeNames = askContext.getResources()
					.getStringArray(R.array.theme_drawables_attribute_names);
			msThemeDrawableAttributeValues = Utils.getIntArrayOfReferences(
					askContext.getResources(), R.array.theme_drawables_ids);
			msThemeColorsAttributeNames = askContext.getResources()
					.getStringArray(R.array.theme_colors_attribute_names);
			msThemeColorsAttributeValues = Utils.getIntArrayOfReferences(
					askContext.getResources(), R.array.theme_colors_ids);
			msThemeDimensionsAttributeNames = askContext.getResources()
					.getStringArray(R.array.theme_dimensions_attribute_names);
			msThemeDimensionsAttributeValues = Utils.getIntArrayOfReferences(
					askContext.getResources(), R.array.theme_dimensions_ids);
			msThemeTypefacesAttributeNames = askContext.getResources()
					.getStringArray(
							R.array.theme_font_typefaces_attribute_names);
			msThemeTypefacesAttributeValues = Utils
					.getIntArrayOfReferences(askContext.getResources(),
							R.array.theme_font_typefaces_ids);
		}
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
		mDrawablesMapping = new HashMap<Integer, Integer>();
		mColorsMapping = new HashMap<Integer, Integer>();
		mDimensionsMapping = new HashMap<Integer, Float>();
		mTypefacesMapping = new HashMap<Integer, String>();
		PackageManager pm = context.getPackageManager();

		Intent intent = new Intent("com.menny.android.anysoftkeyboard.THEME");
		// TODO: FIX
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

		final XmlResourceParser theme = ai.loadXmlMetaData(context
				.getPackageManager(),
				"com.menny.android.anysoftkeyboard.themes");
		mContext = externalContext;

		parseResMappingFromXml(externalContext, theme);
	}

	/**
	 * Creates a new ThemeResources from XmlResourceParser
	 *
	 * @param context
	 * @param parser
	 * @param defaultRes
	 */
	public ThemeResources(Context context, XmlResourceParser parser,
			ThemeResources defaultRes) {

		mDefaultRes = defaultRes;
		mContext = context;
		mDrawablesMapping = new HashMap<Integer, Integer>();
		mColorsMapping = new HashMap<Integer, Integer>();
		mDimensionsMapping = new HashMap<Integer, Float>();
		mTypefacesMapping = new HashMap<Integer, String>();

		parseResMappingFromXml(context, parser);
	}

	/**
	 * Creates a new ThemeResources from AttributeSet
	 *
	 * @param context
	 * @param parser
	 * @param defaultRes
	 */
	public ThemeResources(Context context, AttributeSet attrs,
			ThemeResources defaultRes) {
		mDefaultRes = defaultRes;
		mContext = context;
		mDrawablesMapping = new HashMap<Integer, Integer>();
		mColorsMapping = new HashMap<Integer, Integer>();
		mDimensionsMapping = new HashMap<Integer, Float>();
		mTypefacesMapping = new HashMap<Integer, String>();

		parseResMappingFromXml(context, attrs);
	}

	// /**
	// *
	// * @return
	// */
	// private Resources getResourcesForDrawable(int id) {
	// boolean fallback = true;
	// if (mContext != null) {
	// if (mDrawablesMapping.get(id) == null) {
	// // There is no Resources defined here so we must fallback for
	// // Context, too
	// fallback = true;
	// } else {
	// fallback = false;
	// }
	// }
	//
	// if (fallback && mDefaultRes != null) {
	// return mDefaultRes.getResourcesForDrawable(id);
	// } else if (!fallback) {
	// // Invariant: mContext != null
	// return mContext.getResources();
	// } else {
	// Log.w(TAG, "Could not find suitable Resources for drawable. id = " + id);
	// return null;
	// }
	// }

	// private AssetManager getAssetsForTypeface(int id) {
	// boolean fallback = true;
	// if (mContext != null) {
	// if (mTypefacesMapping.get(id) == null) {
	// // There is no Resources defined here so we must fallback for
	// // Context, too
	// fallback = true;
	// } else {
	// fallback = false;
	// }
	// }
	//
	// if (fallback && mDefaultRes != null) {
	// return mDefaultRes.getAssetsForTypeface(id);
	// } else if (!fallback) {
	// // Invariant: mContext != null
	// return mContext.getAssets();
	// } else {
	// Log.w(TAG, "Could not find suitable Assets for typeface. id = " + id);
	// return null;
	// }
	// }

	public Drawable getDrawable(int id) {
		Integer resId = mDrawablesMapping.get(id);

		if (resId != null) {
			Resources res = mContext.getResources();
			if (res == null) {
				Log
						.e(TAG,
								"Could not return Drawable since we have not Resources!");
				return null;
			}
			return res.getDrawable(resId);
		} else if (mDefaultRes != null) {
			return mDefaultRes.getDrawable(id);
		} else {
			Log.w(TAG, "Could not find suitable Drawable. id = " + id);
			return null;
		}
	}

	public Typeface getTypeface(int id) {
		String pathToTypeface = mTypefacesMapping.get(id);

		if (pathToTypeface != null) {
			AssetManager assets = mContext.getAssets();
			if (assets == null) {
				Log.e(TAG,
						"Could not return Typeface since we have not Assets!");
				return Typeface.DEFAULT;
			}
			Typeface typeface = Typeface
					.createFromAsset(assets, pathToTypeface);
			if (typeface == null) {
				Log.e(TAG, "Loading of typeface failed!");
				return Typeface.DEFAULT;
			} else {
				return typeface;
			}

		} else if (mDefaultRes != null) {
			return mDefaultRes.getTypeface(id);
		} else {
			Log.w(TAG, "Could not find suitable Typeface. id = " + id);
			return Typeface.DEFAULT;
		}
	}

	public Float getDimension(int id) {
		Float dimensionValue = mDimensionsMapping.get(id);

		if (dimensionValue != null) {
			return dimensionValue;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getDimension(id);
		} else {
			Log.w(TAG, "Could not find suitable Dimension. id = " + id);
			return null;
		}
	}

	public float getDimension(int id, float defValue) {
		Float val = getDimension(id);
		if (val == null)
			val = defValue;

		return val;
	}

	public Integer getColor(int id) {
		Integer color = mColorsMapping.get(id);

		if (color != null) {
			return color;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getColor(id);
		} else {
			Log.w(TAG, "Could not find suitable Color. id = " + id);
			return null;
		}
	}

	public Integer getColor(int id, int defValue) {
		Integer val = getColor(id);
		if (val == null)
			val = defValue;

		return val;
	}

	// for layout witdth + height
	// public int getDimension(int id) {
	// use Resources.getDimension
	// if (mVerticalCorrection != null) {
	// return mVerticalCorrection;
	// } else if (mDefaultRes != null) {
	// return mDefaultRes.getVerticalCorrection();
	// } else {
	// Log.w(TAG, "Could not find suitable VerticalCorrection.");
	// return 0;
	// }
	// }

	// // for verticalGap in keyboard
	// public int getDimensionOrFraction(int id, int defValue) {
	// Integer resId = mDrawablesMapping.get(id);
	//
	// if (resId != null) {
	// Resources res = getResourcesForDrawable(id);
	// if(res == null) {
	// Log.e(TAG, "Could not return Drawable since we have not Resources!");
	// return null;
	// }
	// //TODO: How to determine whether it is fraction or Dimension?
	// return res.getDimensionPixelOffset(resId);
	// } else if (mDefaultRes != null) {
	// return mDefaultRes.getDimensionOrFraction(id, defValue);
	// } else {
	// Log.w(TAG, "Could not find suitable Dimension or Fraction.");
	// return defValue;
	// }
	// }

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
	 * @return the mPopupLayoutResourceID
	 */
	public Integer getPopupLayoutResourceID() {
		if (mPopupLayoutResourceID != null) {
			return mPopupLayoutResourceID;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getPopupLayoutResourceID();
		} else {
			Log.w(TAG, "Could not find suitable PopupLayout.");
			return 0;
		}
	}

	/**
	 *
	 * @return Context of PopupLayout reference
	 */
	public Context getPopupLayoutContext() {
		boolean fallback = true;
		if (mContext != null) {
			if (mPopupLayoutResourceID == null) {
				// There is no PopupLayout defined here so we must fallback for
				// Context, too
				fallback = true;
			} else {
				fallback = false;
			}
		}

		if (fallback && mDefaultRes != null) {
			return mDefaultRes.getPopupLayoutContext();
		} else if (!fallback) {
			// Invariant: mContext != null
			return mContext;
		} else {
			Log.w(TAG, "Could not find suitable PopupLayoutContext.");
			return null;
		}
	}

	/**
	 * @return the mCandidatePreviewLayoutResourceID
	 */
	public Integer getCandidatePreviewLayoutResourceID() {
		if (mCandidatePreviewLayoutResourceID != null) {
			return mCandidatePreviewLayoutResourceID;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getCandidatePreviewLayoutResourceID();
		} else {
			Log.w(TAG, "Could not find suitable CandidatePreviewLayout.");
			return 0;
		}
	}

	/**
	 *
	 * @return Context of CandidatePreviewLayout reference
	 */
	public Context getCandidatePreviewLayoutContext() {
		boolean fallback = true;
		if (mContext != null) {
			if (mCandidatePreviewLayoutResourceID == null) {
				// There is no Layout defined here so we must fallback for
				// Context, too
				fallback = true;
			} else {
				fallback = false;
			}
		}

		if (fallback && mDefaultRes != null) {
			return mDefaultRes.getCandidatePreviewLayoutContext();
		} else if (!fallback) {
			// Invariant: mContext != null
			return mContext;
		} else {
			Log.w(TAG,
					"Could not find suitable CandidatePreviewLayout Context.");
			return null;
		}
	}

	/**
	 * @return the mCandidateViewContainerLayoutResourceID
	 */
	public Integer getCandidateViewContainerLayoutResourceID() {
		if (mCandidateViewContainerLayoutResourceID != null) {
			return mCandidateViewContainerLayoutResourceID;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getCandidateViewContainerLayoutResourceID();
		} else {
			Log
					.w(TAG,
							"Could not find suitable CandidateViewContainerLayoutResourceID.");
			return 0;
		}
	}

	/**
	 *
	 * @return Context of KeyPreviewLayout reference
	 */
	public Context getKeyPreviewLayoutContext() {
		boolean fallback = true;
		if (mContext != null) {
			if (mKeyPreviewLayoutResourceID == null) {
				// There is no Layout defined here so we must fallback for
				// Context, too
				fallback = true;
			} else {
				fallback = false;
			}
		}

		if (fallback && mDefaultRes != null) {
			return mDefaultRes.getKeyPreviewLayoutContext();
		} else if (!fallback) {
			// Invariant: mContext != null
			return mContext;
		} else {
			Log.w(TAG, "Could not find suitable KeyPreviewLayout Context.");
			return null;
		}
	}

	/**
	 * @return the mKeyPreviewLayoutResourceID
	 */
	public Integer getKeyPreviewLayoutResourceID() {
		if (mKeyPreviewLayoutResourceID != null) {
			return mKeyPreviewLayoutResourceID;
		} else if (mDefaultRes != null) {
			return mDefaultRes.getKeyPreviewLayoutResourceID();
		} else {
			Log.w(TAG, "Could not find suitable KeyPreviewLayoutResourceID.");
			return 0;
		}
	}

	/**
	 *
	 * @return Context of CandidateViewContainerLayout reference
	 */
	public Context getCandidateViewContainerLayoutContext() {
		boolean fallback = true;
		if (mContext != null) {
			if (mCandidateViewContainerLayoutResourceID == null) {
				// There is no Layout defined here so we must fallback for
				// Context, too
				fallback = true;
			} else {
				fallback = false;
			}
		}

		if (fallback && mDefaultRes != null) {
			return mDefaultRes.getCandidateViewContainerLayoutContext();
		} else if (!fallback) {
			// Invariant: mContext != null
			return mContext;
		} else {
			Log
					.w(TAG,
							"Could not find suitable CandidateViewContainerLayout Context.");
			return null;
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