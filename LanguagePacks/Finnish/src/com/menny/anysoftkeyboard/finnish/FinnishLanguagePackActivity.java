package com.menny.anysoftkeyboard.finnish;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class FinnishLanguagePackActivity extends Activity 
{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
 
        Uri data = Uri.parse("content://com.menny.android.anysoftkeyboard/"); 
        Intent intentResult = new Intent(null, data);
 
        intentResult.putExtra("keyboardContentProviderUri", FinnishKeyboardProvider.CONTENT_URI.toString());
        //intentResult.putExtra("dictionaryContentProviderUri", );
        setResult(RESULT_OK, intentResult);
        finish();
    }
}
