package com.menny.android.anysoftkeyboard.keyboards;


import android.content.res.Configuration;
import android.view.KeyEvent;

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;
import com.menny.android.anysoftkeyboard.R;
import com.menny.android.anysoftkeyboard.keyboards.AnyKeyboard.HardKeyboardTranslator;

public class RussianPhoneticKeyboard extends LatinKeyboard implements HardKeyboardTranslator
{
	
	private static final HardKeyboardSequenceHandler msKeySequenceHandler;
	static
	{
		msKeySequenceHandler = new HardKeyboardSequenceHandler();
		msKeySequenceHandler.addQwertyTranslation("\u044f\u0448\u0435\u0440\u0442\u044b\u0443\u0438\u043e\u043f\u0430\u0441\u0434\u0444\u0433\u0447\u0439\u043a\u043b\u0437\u0445\u0446\u0432\u0431\u043d\u043c");
		msKeySequenceHandler.addSequence(new int[]{KeyEvent.KEYCODE_COMMA, KeyEvent.KEYCODE_COMMA}, (char)1100);

		msKeySequenceHandler.addSequence(new int[]{KeyEvent.KEYCODE_U, KeyEvent.KEYCODE_U}, (char)1102);
		msKeySequenceHandler.addSequence(new int[]{KeyEvent.KEYCODE_W, KeyEvent.KEYCODE_W}, (char)1097);
		msKeySequenceHandler.addSequence(new int[]{KeyEvent.KEYCODE_E, KeyEvent.KEYCODE_E}, (char)1101);
		msKeySequenceHandler.addSequence(new int[]{KeyEvent.KEYCODE_Z, KeyEvent.KEYCODE_H}, (char)1078);
		msKeySequenceHandler.addSequence(new int[]{KeyEvent.KEYCODE_PERIOD, KeyEvent.KEYCODE_PERIOD}, (char)1098);
	}
	
	public RussianPhoneticKeyboard(AnyKeyboardContextProvider context) 
	{
		super(context, getKeyboardId(context));
	}
	
	private static int getKeyboardId(AnyKeyboardContextProvider context) 
	{
		//4 rows only in portrait mode only
		final boolean inPortraitMode = 
			(context.getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
		
		if (inPortraitMode && context.getSharedPreferences().getBoolean("ru_keyboard_4_row", true))
			return R.xml.russian_ph_qwerty_4_rows;
		else
			return R.xml.russian_ph_qwerty;
	}
	
	public void translatePhysicalCharacter(HardKeyboardAction action) 
	{
		if (action.isAltActive())
			return;
		else
		{
			char translated = msKeySequenceHandler.getSequenceCharacter((char)action.getKeyCode(), getASKContext());
			if (translated != 0)
			{
				if (action.isShiftActive())
					translated = Character.toUpperCase(translated);
				action.setNewKeyCode(translated);
			}
		}
	}
}
