package com.anysoftkeyboard.ui.settings;

import java.util.ArrayList;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.anysoftkeyboard.quicktextkeys.QuickTextKey;
import com.anysoftkeyboard.quicktextkeys.QuickTextKeyFactory;
import com.anysoftkeyboard.ui.MainForm;
import com.menny.android.anysoftkeyboard.R;

/**
 *
 * @author Malcolm
 */
public class QuickTextKeys extends PreferenceActivity {

	@Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.layout.prefs_quick_text_keys);

		final Preference searcher = (Preference) findPreference("search_for_quick_text_keys_packs");
		searcher.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				if (preference.getKey().equals("search_for_quick_text_keys_packs")) {
					try {
						MainForm.searchMarketForAddons(QuickTextKeys.this.getApplicationContext());
					} catch (Exception ex) {
						Log.e("ASK-SETTINGS", "Failed to launch market!", ex);
					}
					return true;
				}
				return false;
			}
		});
    }

	@Override
	protected void onResume() {
		super.onResume();
		final ArrayList<QuickTextKey> keys = QuickTextKeyFactory.getAllAvailableQuickKeys(getApplicationContext());

		String[] ids = new String[keys.size()];
		String[] names = new String[keys.size()];
		int entryPos = 0;
		for (QuickTextKey aKey : keys) {
			ids[entryPos] = aKey.getId();
			names[entryPos] = aKey.getName();
			entryPos++;
		}
		ListPreference keysList = (ListPreference)
					findPreference(getString(R.string.settings_key_active_quick_text_key));
		keysList.setEntries(names);
		keysList.setEntryValues(ids);
	}
}