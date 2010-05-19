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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The basic information of a plugin.
 */
public class ThemePluginInfo implements Parcelable {
    /**
     * The name of the provider.
     */
    public String mProviderName;

    /**
     * The id of the provider.
     */
    public String mId;

    /**
     * The name of the package that the plugin is in.
     */
    public String mPackageName;

    /**
     * The name of the class that implements {@link ThemePlugin} in this plugin.
     */
    public String mClassName;

    /**
     * The full path to the location of the package that the plugin is in.
     */
    public String mSrcPath;

    public ThemePluginInfo(String providerName, String packageName,
            String className, String srcPath, String providerId) {
        mProviderName = providerName;
        mPackageName = packageName;
        mClassName = className;
        mSrcPath = srcPath;
        mId = providerId;
    }

    public ThemePluginInfo(Parcel source) {
        mProviderName = source.readString();
        mPackageName = source.readString();
        mClassName = source.readString();
        mSrcPath = source.readString();
        mId = source.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mProviderName);
        dest.writeString(mPackageName);
        dest.writeString(mClassName);
        dest.writeString(mSrcPath);
        dest.writeString(mId);
    }

    public static final Parcelable.Creator<ThemePluginInfo> CREATOR
            = new Parcelable.Creator<ThemePluginInfo>() {
        public ThemePluginInfo createFromParcel(Parcel source) {
            return new ThemePluginInfo(source);
        }

        public ThemePluginInfo[] newArray(int size) {
            return new ThemePluginInfo[size];
        }
    };
}
