
package com.anysoftkeyboard.ng.views.settings;

import java.util.ArrayList;

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

import com.anysoftkeyboard.ng.R;
import com.anysoftkeyboard.ng.keyboards.KeyboardBuildersFactory;
import com.anysoftkeyboard.ng.keyboards.KeyboardBuildersFactory.KeyboardBuilder;
import com.anysoftkeyboard.ng.views.MainForm;

public class Tweaks extends PreferenceActivity {

	@Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.layout.prefs_tweaks);
    }
}
