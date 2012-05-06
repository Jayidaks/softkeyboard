package com.anysoftkeyboard.receivers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

import org.xmlpull.v1.XmlPullParserException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class KeyRemappingReceiver extends BroadcastReceiver {
	
	private static final String TAG = "ASK KeyRemapping";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Key Remapping broadcast receivered!");
		if (intent == null || context == null)
			return;
		URI pathToNewConfigFile = (URI)intent.getSerializableExtra("anysoftkeyboard_config_file_uri");
		if (pathToNewConfigFile == null)
			return;
		Log.d(TAG, "Key Remapping gave me a new remapping configuration file at "+pathToNewConfigFile.toString());
		
		asynchLoadConfig(context, pathToNewConfigFile);
	}

	protected void asynchLoadConfig(final Context context, URI pathToNewConfigFile)
	{
		new AsyncTask<URI, Void, Void>()
		{
			@Override
			protected Void doInBackground(URI... params) {
				try
				{
					loadConfig(context, params[0]);
				}
				catch(Exception e)
				{
					Log.e(TAG, "Failed to load configuration due to an exception: "+e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
		}.execute(pathToNewConfigFile);
	}

	protected void loadConfig(final Context context, URI pathToNewConfigFile)
			throws FileNotFoundException, IOException, XmlPullParserException {
		File inputFile = new File(pathToNewConfigFile);
		if (!inputFile.exists())
		{
			Log.e(TAG, "Key Remapping gave me a new keyboard configuration file "+pathToNewConfigFile.toString()+" which does not exist!!! Can not recover.");
			return;
		}
		if (!inputFile.isFile())
		{
			Log.e(TAG, "Key Remapping gave me a new keyboard configuration file "+pathToNewConfigFile.toString()+" which is not a file!!! Can not recover.");
			return;
		}
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		//why like this? because if the the preferences value DOES NOT change,
		//no "onSharedPreferenceChanged" is called!
		int random_post_fix = (new Random()).nextInt(Integer.MAX_VALUE);
		editor.putString("global_translator_path_key", inputFile.getAbsolutePath());
		editor.putString("global_translator_path_key_random_post_fix", ""+random_post_fix);
		//hopefully, a commit will trigger re-read by listeners
		editor.commit();
	}
}
