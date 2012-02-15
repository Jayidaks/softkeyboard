package com.anysoftkeyboard;

import java.io.File;
import java.io.FileReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Environment;

import com.anysoftkeyboard.keyboards.HardKeyboardSequenceHandler;

public class GlobalTranslatorLoader {

	private static final String GLOBAL_REMAPPER_CONFIG = "ask_global_hardware_keys_remapping.xml";
	public static interface TranslatorLoadDoneListener
	{
		void onDone(HardKeyboardSequenceHandler translator);
	}
	
	private final TranslatorLoadDoneListener mListener;
	private boolean mShouldRun = false;
	private final Thread mThread = new Thread()
	{
		public void run() {
			loadTranslator();
		}
	};
	
	public GlobalTranslatorLoader(TranslatorLoadDoneListener listener)
	{
		mListener = listener;
	}
	
	protected void loadTranslator() {
		HardKeyboardSequenceHandler handler = null;
		
		while(mShouldRun)
		{
			try
			{
				File fileFolder = Environment.getExternalStorageDirectory();
				File configFile = new File(fileFolder, GLOBAL_REMAPPER_CONFIG);
				
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xml = factory.newPullParser();
				FileReader inputConfig = new FileReader(configFile);
				xml.setInput(inputConfig);
				
				handler = HardKeyboardSequenceHandler.createPhysicalTranslatorFromXmlPullParser(xml);
				mShouldRun = false;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				synchronized (mThread) {
					try {
						mThread.wait(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
