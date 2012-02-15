package com.anysoftkeyboard.receivers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;

import com.anysoftkeyboard.AnySoftKeyboard;

public class TViiiiLoginReceiver extends BroadcastReceiver {
	
	private static final String TAG = "ASK TViiiiLogin";
	
	private static final Object ROOT_NODE_TAG = "LookAndFeel";

	private static final Object CONFIG_NODE_TAG = "Configuration";
	
	public TViiiiLoginReceiver(AnySoftKeyboard ime)
	{
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "TViiii Login broadcast receivered!");
		if (intent == null || intent.getData() == null || context == null)
			return;
		URI pathToNewConfigFile = (URI)intent.getSerializableExtra("anysoftkeyboard_config_file_uri");
		if (pathToNewConfigFile == null)
			return;
		Log.d(TAG, "TViiii Login gave me a new keyboard configuration file at "+pathToNewConfigFile.toString());
		
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
			Log.e(TAG, "TViiii Login gave me a new keyboard configuration file "+pathToNewConfigFile.toString()+" which does not exist!!! Can not recover.");
			return;
		}
		if (!inputFile.isFile())
		{
			Log.e(TAG, "TViiii Login gave me a new keyboard configuration file "+pathToNewConfigFile.toString()+" which is not a file!!! Can not recover.");
			return;
		}
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser xml = factory.newPullParser();
		FileReader inputConfig = new FileReader(inputFile);
		xml.setInput(inputConfig);
		/*
<LookAndFeel>
	<Configuration key=”key_name” type=”string|boolean|integer” value=”configuration value”/>
	<Configuration key=”key_name_another” type=”string|boolean|integer” value=”configuration value another”/>
</LookAndFeel>
			 */
		int event;
        boolean inRoot = false;
        while ((event = xml.next()) != XmlPullParser.END_DOCUMENT) {
            final String tag = xml.getName();
            if (event == XmlPullParser.START_TAG) {
                if (ROOT_NODE_TAG.equals(tag)) {
                    inRoot = true;
                } else if (inRoot && CONFIG_NODE_TAG.equals(tag)) {
                	final AttributeSet attrs = Xml.asAttributeSet(xml);
                	String key = attrs.getAttributeValue("", "key");
                	String type = attrs.getAttributeValue("", "type");
                	String value = attrs.getAttributeValue("", "value");
                	
                	if (TextUtils.isEmpty(key) || TextUtils.isEmpty(type))
                	{
                		Log.e(TAG, "Missing key or type attributes! Skipping");
                	}
                	else
                	{
                		Log.i(TAG, String.format("Will write key '%s' of type %s as value '%s'", key, type, value));
                		if (type.equalsIgnoreCase("boolean"))
                		{
                			editor.putBoolean(key, Boolean.parseBoolean(value));
                		}
                		else if (type.equalsIgnoreCase("integer"))
                		{
                			editor.putInt(key, Integer.parseInt(value));
                		}
                		else if (type.equalsIgnoreCase("string"))
                		{
                			editor.putString(key, value);
                		}
                		else
                		{
                			Log.e(TAG, "Could not determined key value!");
                		}
                	}
                }
            } else if (event == XmlPullParser.END_TAG) {
                if (ROOT_NODE_TAG.equals(tag)) {
                    inRoot = false;
                    break;
                }
            }
        }
		//hopefully, a commit will trigger re-read by listeners
		editor.commit();
	}
}
