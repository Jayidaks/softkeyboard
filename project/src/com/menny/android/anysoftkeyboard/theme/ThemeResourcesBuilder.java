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

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;
import com.menny.android.anysoftkeyboard.keyboards.AnyKeyboard;
import com.menny.android.anysoftkeyboard.keyboards.ExternalAnyKeyboard;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The basic information of a theme plugin.
 */
public class ThemeResourcesBuilder {

	/**
	 * The id of the provider.
	 */
	public String mId;

	/**
	 * The name of the package that the plugin is in.
	 */
	public String mPackageName;

	public int mNameResId;
	public String mDescription;
	public int mThemeIndex;
	public int mThemeDefinitionResId;
	public int mPreviewDrawableResId;


	public ThemeResourcesBuilder(String packageName, String providerId) {
		mPackageName = packageName;
		mId = providerId;
	}

	public ThemeResourcesBuilder(Parcel source) {
		mPackageName = source.readString();
		mId = source.readString();
	}

	public ThemeResources createThemeResources(
			Context askContext, ThemeResources defaultRes) {
		return new ThemeResources(askContext, this,
				defaultRes);
	}
}
