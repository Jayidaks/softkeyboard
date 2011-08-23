package com.anysoftkeyboard.ui.tutorials;

import com.anysoftkeyboard.ui.MainForm;
import com.menny.android.anysoftkeyboard.AnyApplication;
import com.menny.android.anysoftkeyboard.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class ChangeLogActivity extends BaseTutorialActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View content = getWindow().getDecorView();
		//now to listen on all known buttons
		for(View touchable : content.getTouchables())
		{
			switch(touchable.getId())
			{
			case R.id.goto_settings_button:
			case R.id.market_search_button:
				touchable.setOnClickListener(this);
				break;
			}
		}
		
		final CheckBox showNotifications = (CheckBox)findViewById(R.id.show_notifications_next_time);
		showNotifications.setChecked(AnyApplication.getConfig().getShowVersionNotification());
		
		showNotifications.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AnyApplication.getConfig().setShowVersionNotification(!AnyApplication.getConfig().getShowVersionNotification());
			}
		});
	}

	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.goto_settings_button:
			MainForm.startSettings(getApplicationContext());
			break;
		case R.id.market_search_button:
			try
			{
				MainForm.searchMarketForAddons(getApplicationContext());
			}
			catch(Exception ex)
			{
				Log.e("ASK - Tutorial", "Failed to launch Market!", ex);
			}
			break;
		}
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.changelog;
	}

	@Override
	protected int getTitleResId() {
		return R.string.changelog;
	}
}
