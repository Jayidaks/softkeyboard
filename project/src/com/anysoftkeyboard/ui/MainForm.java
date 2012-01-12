package com.anysoftkeyboard.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.anysoftkeyboard.ui.settings.MainSettings;
import com.anysoftkeyboard.ui.tutorials.ChangeLogActivity;
import com.anysoftkeyboard.ui.tutorials.WelcomeHowToNoticeActivity;
import com.menny.android.anysoftkeyboard.R;

public class MainForm extends Activity implements OnClickListener {

	private ViewFlipper mPager;
    private Drawable mSelectedTabBottomDrawable;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        
        mSelectedTabBottomDrawable =  getResources().getDrawable(R.drawable.selected_tab);
        mSelectedTabBottomDrawable.setBounds(0, 0, getWindowManager().getDefaultDisplay().getWidth(), getResources().getDimensionPixelOffset(R.dimen.selected_tab_drawable_height));
        
        mPager = (ViewFlipper)findViewById(R.id.main_pager);
        
        findViewById(R.id.main_tab_text_1).setOnClickListener(this);
        findViewById(R.id.main_tab_text_2).setOnClickListener(this);
        findViewById(R.id.main_tab_text_3).setOnClickListener(this);
	    
		findViewById(R.id.goto_settings_button).setOnClickListener(this);
		findViewById(R.id.goto_changelog_button).setOnClickListener(this);
		findViewById(R.id.goto_howto_form).setOnClickListener(this);
        
        setSelectedTab(0);
    }
	

	void setSelectedTab(int index) {
        ((TextView)findViewById(R.id.main_tab_text_1)).setCompoundDrawables(null, null, null, index == 0? mSelectedTabBottomDrawable : null);
        ((TextView)findViewById(R.id.main_tab_text_2)).setCompoundDrawables(null, null, null, index == 1? mSelectedTabBottomDrawable : null);
        ((TextView)findViewById(R.id.main_tab_text_3)).setCompoundDrawables(null, null, null, index == 2? mSelectedTabBottomDrawable : null);
        
        if (mPager.getDisplayedChild() != index)
        	mPager.setDisplayedChild(index);
}
	
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.main_tab_text_1:
            setSelectedTab(0);
            break;
    case R.id.main_tab_text_2:
            setSelectedTab(1);
            break;
    case R.id.main_tab_text_3:
            setSelectedTab(2);
            break;
		case R.id.goto_howto_form:
			Intent i = new Intent(getApplicationContext(), WelcomeHowToNoticeActivity.class);
			startActivity(i);
			break;
		case R.id.goto_settings_button:
			startSettings(getApplicationContext());
			break;
		case R.id.market_search_button:
			try
			{
				searchMarketForAddons(getApplicationContext(), "");
			}
			catch(Exception ex)
			{
				Log.e("MainForm", "Failed to launch Market! ", ex);
			}
			break;
		case R.id.goto_changelog_button:
			showChangelog(getApplicationContext());
			break;
		}
	}

	public static void searchMarketForAddons(Context applicationContext, String additionalQueryString) throws android.content.ActivityNotFoundException {
		Intent search = new Intent(Intent.ACTION_VIEW);
		search.setData(Uri.parse("market://search?q=AnySoftKeyboard"+additionalQueryString));
		search.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		applicationContext.startActivity(search);
	}
	
	public static void showChangelog(Context applicationContext) {
		Intent intent = new Intent(applicationContext, ChangeLogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		applicationContext.startActivity(intent);
	}
	
	public static void startSettings(Context applicationContext) {
		Intent intent = new Intent(applicationContext, MainSettings.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		applicationContext.startActivity(intent);
	}
}
