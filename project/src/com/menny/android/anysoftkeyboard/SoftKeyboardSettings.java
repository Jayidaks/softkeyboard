
package com.menny.android.anysoftkeyboard;

import com.menny.android.anysoftkeyboard.AnySoftKeyboardConfiguration.AnySoftKeyboardConfigurationImpl;
import com.menny.android.anysoftkeyboard.keyboards.KeyboardBuildersFactory;
import com.menny.android.anysoftkeyboard.keyboards.KeyboardBuildersFactory.KeyboardBuilder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

import java.util.ArrayList;

public class SoftKeyboardSettings extends PreferenceActivity {

	// Number of preferences without loading external keyboards
	private int mDefaultPreferencesCount = 0;

	@Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(AnySoftKeyboardConfigurationImpl.PREF_XML_RES_ID);
        final PreferenceCategory keyboards = (PreferenceCategory)super.findPreference("prefs_keyboards_screen");
        mDefaultPreferencesCount = keyboards.getPreferenceCount();


        String version = "";
        try {
			final PackageInfo info = getPackageInfo(getApplicationContext());
			version = info.versionName + " (release "+info.versionCode+")";
		} catch (final NameNotFoundException e) {
			Log.e("AnySoftKeyboard", "Failed to locate package information! This is very weird... I'm installed.");
		}

		final Preference label = super.findPreference("prefs_title_key");
		label.setSummary(label.getSummary()+version);


		final Preference searcher = (Preference)super.findPreference("search_for_addons");
		searcher.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				if (preference.getKey().equals("search_for_addons"))
				{
					try
					{
						MainForm.searchMarketForAddons(SoftKeyboardSettings.this.getApplicationContext());
					}
					catch(Exception ex)
					{
						Log.e("ASK-SETTINGS", "Failed to launch market!", ex);
					}
					return true;
				}
				return false;
			}
		});
		
		final Preference helper = (Preference)super.findPreference("prefs_help_key");
		helper.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				if (preference.getKey().equals("prefs_help_key"))
				{
					//http://s.evendanan.net/ask_settings
					Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://s.evendanan.net/ask_settings"));
					startActivity(browserIntent);
					return true;
				}
				return false;
			}
		});
		
    }

	@Override
	protected void onResume() {
		super.onResume();

		//first resetting maybe the user has installed a new keyboard
		KeyboardBuildersFactory.resetBuildersCache();
		//getting all keyboards
		final ArrayList<KeyboardBuilder> creators = KeyboardBuildersFactory.getAllBuilders(getApplicationContext());
		final PreferenceCategory keyboards = (PreferenceCategory)super.findPreference("prefs_keyboards_screen");

		removeNonDefaultPreferences();

		for(final KeyboardBuilder creator : creators)
		{
		    final Context creatorContext = creator.getPackageContext() == null?
		            getApplicationContext() : creator.getPackageContext();

			if (creatorContext == getApplicationContext() && creator.getKeyboardNameResId() == R.string.eng_keyboard) {
                continue;//english is an internal keyboard, and is on by default.
            }
			final CheckBoxPreference checkBox = new CheckBoxPreference(getApplicationContext());
			/*
			 * <CheckBoxPreference
				android:key="eng_keyboard"
				android:title="@string/eng_keyboard"
				android:persistent="true"
				android:defaultValue="true"
				android:summaryOn="QWERTY Latin keyboard"
				android:summaryOff="QWERTY Latin keyboard"
				/>
			 */
			checkBox.setKey(creator.getId());
			checkBox.setTitle(creatorContext.getText(creator.getKeyboardNameResId()));
			checkBox.setPersistent(true);
			checkBox.setDefaultValue(false);
			checkBox.setSummaryOn(creator.getDescription());
			checkBox.setSummaryOff(creator.getDescription());

			keyboards.addPreference(checkBox);
		}
	}

	private void removeNonDefaultPreferences() {
		// We keep the preferences defined in the xml, everything else goes
		final PreferenceCategory keyboards = (PreferenceCategory)super.findPreference("prefs_keyboards_screen");
		while(keyboards.getPreferenceCount() > mDefaultPreferencesCount)
		{
			keyboards.removePreference(keyboards.getPreference(mDefaultPreferencesCount));
		}
	}

	public static PackageInfo getPackageInfo(Context context) throws NameNotFoundException {
		return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	}
}
