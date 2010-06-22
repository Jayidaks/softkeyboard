
package com.menny.android.anysoftkeyboard;

import com.menny.android.anysoftkeyboard.keyboards.KeyboardBuildersFactory;
import com.menny.android.anysoftkeyboard.keyboards.KeyboardBuildersFactory.KeyboardBuilder;
import com.menny.android.anysoftkeyboard.theme.ThemeResourcesBuilder;
import com.menny.android.anysoftkeyboard.theme.ThemeResourcesBuildersFactory;
import com.menny.android.anysoftkeyboard.theme.ThemeSwitcher;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class SoftKeyboardSettings extends PreferenceActivity {

    private static final String TAG = "AnySoftKeyboard";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.layout.prefs);

        String version = "";
        try {
            final PackageInfo info = getPackageInfo(getApplicationContext());
            version = info.versionName + " (release " + info.versionCode + ")";
        } catch (final NameNotFoundException e) {
            Log.e("AnySoftKeyboard",
                    "Failed to locate package information! This is very weird... I'm installed.");
        }

        final Preference label = super.findPreference("prefs_title_key");
        label.setSummary(label.getSummary() + version);
        // first resetting maybe the user has installed a new keyboard
        KeyboardBuildersFactory.resetBuildersCache();
        // getting all keyboards
        final ArrayList<KeyboardBuilder> creators = KeyboardBuildersFactory
                .getAllBuilders(getApplicationContext());
        final PreferenceCategory keyboards = (PreferenceCategory) super
                .findPreference("prefs_keyboards_screen");

        for (final KeyboardBuilder creator : creators) {
            final Context creatorContext = creator.getPackageContext() == null ? getApplicationContext()
                    : creator.getPackageContext();

            if (creatorContext == getApplicationContext()
                    && creator.getKeyboardNameResId() == R.string.eng_keyboard) {
                continue;// english is an internal keyboard, and is on by
                // default.
            }
            final CheckBoxPreference checkBox = new CheckBoxPreference(getApplicationContext());
            /*
             * <CheckBoxPreference android:key="eng_keyboard"
             * android:title="@string/eng_keyboard" android:persistent="true"
             * android:defaultValue="true"
             * android:summaryOn="QWERTY Latin keyboard"
             * android:summaryOff="QWERTY Latin keyboard" />
             */
            checkBox.setKey(creator.getId());
            checkBox.setTitle(creatorContext.getText(creator.getKeyboardNameResId()));
            checkBox.setPersistent(true);
            checkBox.setDefaultValue(false);
            checkBox.setSummaryOn(creator.getDescription());
            checkBox.setSummaryOff(creator.getDescription());

            keyboards.addPreference(checkBox);
        }

        ThemeSwitcher.resetCachedTheme();
        ThemeResourcesBuildersFactory.resetBuildersCache();
        Map<String, ThemeResourcesBuilder> themeBuilders = ThemeResourcesBuildersFactory
                .getAllBuilders(getApplicationContext());
        {
            final ListPreference themes = (ListPreference) super.findPreference("themes");

            CharSequence[] entries = themes.getEntries();
            CharSequence[] entryValues = themes.getEntryValues();
            List<CharSequence> entriesList = new ArrayList<CharSequence>();
            List<CharSequence> entryValuesList = new ArrayList<CharSequence>();
            if (entries != null) {
                for (CharSequence seq : entries) {
                    entriesList.add(seq);
                }
            }
            if (entryValues != null) {
                for (CharSequence seq : entryValues) {
                    entryValuesList.add(seq);
                }
            }

            Set<Entry<String, ThemeResourcesBuilder>> themesEntrySet = themeBuilders.entrySet();
            for (Entry<String, ThemeResourcesBuilder> entry : themesEntrySet) {
                ThemeResourcesBuilder themeResourcesBuilder = entry.getValue();
                try {
                    CharSequence name = getApplicationContext().getPackageManager()
                            .getResourcesForApplication(themeResourcesBuilder.mPackageName)
                            .getText(themeResourcesBuilder.mNameResId);
                    entryValuesList.add(entry.getKey());
                    entriesList.add(name);
                } catch (NameNotFoundException e) {
                    Log.e(TAG, new StringBuffer("Could not find package ").append(
                            themeResourcesBuilder.mPackageName).append(" in theme with id ")
                            .append(entry.getKey()).toString());
                }
            }

            themes.setEntryValues(entryValuesList.toArray(new CharSequence[0]));
            themes.setEntries(entriesList.toArray(new CharSequence[0]));

            if (entryValuesList.size() > 0) {
                themes.setDefaultValue(entryValuesList.get(0));
            }
        }

        final Preference searcher = super.findPreference("search_for_addons");
        searcher.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getKey().equals("search_for_addons")) {
                    MainForm.searchMarketForAddons(SoftKeyboardSettings.this
                            .getApplicationContext());
                    return true;
                }
                return false;
            }
        });
    }

    public static PackageInfo getPackageInfo(Context context) throws NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    }
}
