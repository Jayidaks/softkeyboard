
package com.menny.android.anysoftkeyboard.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ThemeSwitcher {

    private static final String TAG = "ThemeSwitcher";

    private static ThemeResources mThemeResources;

    public static void resetCachedTheme() {
        mThemeResources = null;
    }

    public static synchronized ThemeResources getThemeResources(Context askContext) {
        if (mThemeResources == null) {
            ThemeResources.initAttributeInfos(askContext);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(askContext);
            String id = sp.getString("themes", null);
            if (id == null) {
                Log.e(TAG, "Could not find theme setting value.");
                return null;
            }

            ThemeResourcesBuilder builder = ThemeResourcesBuildersFactory
                    .getAllBuilders(askContext).get(id);
            mThemeResources = builder.createThemeResources(askContext,
                    ThemeResourcesBuildersFactory.getFallbackThemeResources());
        }
        return mThemeResources;
    }
}
