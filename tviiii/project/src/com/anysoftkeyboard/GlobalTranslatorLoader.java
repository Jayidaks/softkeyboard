package com.anysoftkeyboard;

import java.io.File;
import java.io.FileReader;
import java.net.URI;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.res.Resources;
import android.util.Log;

import com.anysoftkeyboard.keyboards.HardKeyboardSequenceHandler;

public class GlobalTranslatorLoader {

	private static final String TAG = "ASK GlobalTranslatorLoader";
	
	public static interface TranslatorLoadDoneListener
	{
		void onDone(HardKeyboardSequenceHandler translator);
	}
	
	private final URI mRemappingFile;
	private final TranslatorLoadDoneListener mListener;
	
	private boolean mShouldRun = false;
	private int mErrorsLeft = 100;
	
	private final Thread mThread = new Thread()
	{
		public void run() {
			loadTranslator();
		}
	};
	private final Resources mResources;
	
	public GlobalTranslatorLoader(URI remappingFile, TranslatorLoadDoneListener listener, Resources res)
	{
		mRemappingFile = remappingFile;
		mListener = listener;
		mResources = res;
	}
	
	protected void loadTranslator() {
		HardKeyboardSequenceHandler handler = null;
		
		while(mShouldRun)
		{
			try
			{
				File configFile = new File(mRemappingFile);
				
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xml = factory.newPullParser();
				FileReader inputConfig = new FileReader(configFile);
				xml.setInput(inputConfig);
				
				handler = HardKeyboardSequenceHandler.createPhysicalTranslatorFromXmlPullParser(xml, mResources);
				mShouldRun = false;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				mErrorsLeft--;
				if (mErrorsLeft >= 0)
				{
					synchronized (mThread) {
						try {
							mThread.wait(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				else
				{
					Log.e(TAG, "Failed to load global physical keys translator. Giving up!");
					mShouldRun = false;
				}
			}
		}
		
		mListener.onDone(handler);
	}

	public void load() {mShouldRun = true; mThread.start();}
	public void stop() 
	{
		mShouldRun = false;
		synchronized (mThread) {
			mThread.notifyAll();
		}
	}
}
