package com.android.moduwkeyboard.keyboards;

import android.content.res.Resources;

import com.android.moduwkeyboard.AnyKeyboardContextProvider;
import com.android.moduwkeyboard.R;

public class GenericKeyboard extends ExternalAnyKeyboard 
{
	public GenericKeyboard(AnyKeyboardContextProvider context, int xmlLayoutResId, int nameResId, String prefKeyId, int mode) 
	{
		super(context, context.getApplicationContext(), xmlLayoutResId, xmlLayoutResId, prefKeyId, nameResId, -1, -1, null, null, mode);
	}
	
	public GenericKeyboard(AnyKeyboardContextProvider context, int xmlLayoutResId, int xmlLandscapeLayoutResId,  int nameResId, String prefKeyId, int mode) 
	{
		super(context, context.getApplicationContext(), xmlLayoutResId, xmlLandscapeLayoutResId, prefKeyId, nameResId, -1, -1, null, null, mode);
	}
	
	@Override
	public void initKeysMembers() {
		super.initKeysMembers();
		if (getKeyboardPrefId().equals("phone_symbols_keyboard"))
		{
			for(final Key key : getKeys())
	        {
	        	if ((key.codes != null) && (key.codes.length > 0))
	            {
	                final int primaryCode = key.codes[0];
	                if (primaryCode == 48)
	                {
	                	final Resources localResources = getASKContext().getApplicationContext().getResources();
	                    setKeyIcons(key, localResources, R.drawable.sym_keyboard_num0, R.drawable.sym_keyboard_num0_feedback);
	                	break;
	                }
	            }
	        }
		}
	}
}
