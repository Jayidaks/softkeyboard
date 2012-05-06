package com.tviiii.tester_app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import com.tviiii.tester_app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	protected static final String TAG = "TViiii Tester";

	private final HashMap<Integer, String> mKeyCodeNames = new HashMap<Integer, String>();
	
	private TextView mKeyCodeText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		mKeyCodeText = (TextView)findViewById(R.id.key_code_text);
		
		EditText testingBox = (EditText)findViewById(R.id.key_code_testing_box);
		testingBox.setKeyListener(new KeyListener() {
			
			@Override
			public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
				logKeyCode(keyCode);
				return true;
			}
			
			@Override
			public boolean onKeyOther(View view, Editable text, KeyEvent event) {
				logKeyCode(event.getKeyCode());
				return true;
			}
			
			@Override
			public boolean onKeyDown(View view, Editable text, int keyCode,
					KeyEvent event) {
				logKeyCode(keyCode);				
				return true;
			}

			protected void logKeyCode(int keyCode) {
				String log = "Key pressed! Value "+keyCode+", which is ";
				if (mKeyCodeNames.containsKey(keyCode))
				{
					log += mKeyCodeNames.get(keyCode);
				}
				else
				{
					log += "AN UNKNOWN API KEYCODE!";
				}
				mKeyCodeText.setText(log);
			}
			
			@Override
			public int getInputType() {
				return 0;
			}
			
			@Override
			public void clearMetaKeyState(View view, Editable content, int states) {
			}
		});
		
		findViewById(R.id.do_login_test_button).setOnClickListener(this);
		findViewById(R.id.do_remapping_test_button).setOnClickListener(this);
		findViewById(R.id.do_key_press_test_button).setOnClickListener(this);
		findViewById(R.id.key_code_dump_button).setOnClickListener(this);
		
		final Dialog progress = createSpinningDialog("Reading defaults");
		progress.show();
		new AsyncTask<Void, Void, Void>()
		{
			CharSequence loginFileContent = "";
			CharSequence remappingFileContent = "";
			
			@Override
			protected Void doInBackground(Void... params) {
				Field[] keyCodes = KeyEvent.class.getFields();
				for(Field keyCode : keyCodes)
				{
					final int modifier = keyCode.getModifiers();
					if ((modifier & Modifier.STATIC) != 0 &&
						(modifier & Modifier.PUBLIC) != 0 &&
						(modifier & Modifier.FINAL) != 0)
					{
						if (keyCode.getName().startsWith("KEYCODE_"))
						{
							try {
								mKeyCodeNames.put(keyCode.getInt(null), keyCode.getName());
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				try
				{
					BufferedReader in = new BufferedReader(new InputStreamReader(getAssets().open("login_file_content.txt")));

		            String line;
		            StringBuilder buffer = new StringBuilder();

		            while ((line = in.readLine()) != null)
		            {
		                buffer.append(line).append('\n');
		            }
		            
					in.close();
					loginFileContent = buffer;
					
					in = new BufferedReader(new InputStreamReader(getAssets().open("remapping_file_content.txt")));

					buffer = new StringBuilder();

		            while ((line = in.readLine()) != null)
		            {
		                buffer.append(line).append('\n');
		            }
		            
					in.close();
					remappingFileContent = buffer;
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
			
			protected void onPostExecute(Void result) {
				progress.dismiss();
				EditText loginFileContentEditor = (EditText)findViewById(R.id.login_file_content);
				loginFileContentEditor.setText(loginFileContent);
				
				EditText remappingFileContentEditor = (EditText)findViewById(R.id.remapping_file_content);
				remappingFileContentEditor.setText(remappingFileContent);
			};
		}.execute();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.do_login_test_button:
			startLoginBroadcastTest();
			break;
		case R.id.do_remapping_test_button:
			startRemappingBroadcastTest();
			break;
		case R.id.do_key_press_test_button:
			startKeyPressBroadcastTest();
			break;
		case R.id.key_code_dump_button:
			dumpKeyCodes();
			break;
		}
	}


	private void dumpKeyCodes() {
		final Dialog progress = createSpinningDialog("Saving keycodes...");

		new AsyncTask<Void, Void, Void>()
		{
			Exception backgroundException = null;
			final File configFile = new File(Environment.getExternalStorageDirectory(), "key_codes.csv");
			
			@Override
			protected Void doInBackground(Void... params) {
				try
				{
					configFile.delete();
					FileWriter writer = new FileWriter(configFile, false);
					writer.write("Key Code Value, Key Code Name");
					writer.write('\n');
					for(Entry<Integer, String> pair: mKeyCodeNames.entrySet())
					{
						String content = pair.getKey().toString()+","+pair.getValue();
						Log.i(TAG, content);
						writer.write(content);
						writer.write('\n');
					}
					writer.flush();
					writer.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					backgroundException = e;
				}
				return null;
			}
			
			protected void onPostExecute(Void result) {
				progress.dismiss();
				if (backgroundException != null)
				{
					AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
							.setTitle("Error")
							.setMessage("Failed to do keycode dumping due to: "+backgroundException.getMessage())
							.create();
					dialog.show();
				}
				else
				{
					AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
							.setTitle("Done")
							.setMessage("Dumped key code values to: "+configFile.getAbsolutePath())
							.create();
					dialog.show();
				}
			};
		}.execute();
	}

	private void startLoginBroadcastTest() {
		EditText loginFileContentEditor = (EditText)findViewById(R.id.login_file_content);
		String loginFileContent = loginFileContentEditor.getEditableText().toString();
		final Dialog progress = createSpinningDialog("Login test");
		
		new AsyncTask<String, Void, Void>()
		{
			Exception backgroundException = null;
			final File configFile = new File(Environment.getExternalStorageDirectory(), "ask_look_and_feel.xml");
			
			@Override
			protected Void doInBackground(String... params) {
				String loginFileContent = params[0];
				try
				{
					configFile.delete();
					FileWriter writer = new FileWriter(configFile, false);
					writer.write(loginFileContent);
					writer.flush();
					writer.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					backgroundException = e;
				}
				return null;
			}
			
			protected void onPostExecute(Void result) {
				progress.dismiss();
				if (backgroundException != null)
				{
					AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
							.setTitle("Error")
							.setMessage("Failed to do login test due to: "+backgroundException.getMessage())
							.create();
					dialog.show();
				}
				else
				{
					Intent loginDone = new Intent("com.tviiii.api.LOGIN");
					URI askConfigFile = configFile.toURI();
					loginDone.putExtra("anysoftkeyboard_config_file_uri", askConfigFile);
					getApplicationContext().sendBroadcast(loginDone);
				}
			};
		}.execute(loginFileContent);
	}
	
	private void startRemappingBroadcastTest() {
		EditText remappingFileContentEditor = (EditText)findViewById(R.id.remapping_file_content);
		String remappingFileContent = remappingFileContentEditor.getEditableText().toString();
		final Dialog progress = createSpinningDialog("Remapping test");
		
		new AsyncTask<String, Void, Void>()
		{
			Exception backgroundException = null;
			int randomPostFix = new Random().nextInt(Integer.MAX_VALUE);
			
			final File configFile = new File(Environment.getExternalStorageDirectory(), "ask_remapping_"+randomPostFix+".xml");
			
			@Override
			protected Void doInBackground(String... params) {
				String fileContent = params[0];
				try
				{
					configFile.delete();
					FileWriter writer = new FileWriter(configFile, false);
					writer.write(fileContent);
					writer.flush();
					writer.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					backgroundException = e;
				}
				return null;
			}
			
			protected void onPostExecute(Void result) {
				progress.dismiss();
				if (backgroundException != null)
				{
					AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
							.setTitle("Error")
							.setMessage("Failed to do remapping test due to: "+backgroundException.getMessage())
							.create();
					dialog.show();
				}
				else
				{
					Intent remappingRequired = new Intent("com.tviiii.api.KEY_REMAPPING");
					URI askConfigFile = configFile.toURI();
					remappingRequired.putExtra("anysoftkeyboard_config_file_uri", askConfigFile);
					getApplicationContext().sendBroadcast(remappingRequired);
				}
			};
		}.execute(remappingFileContent);
	}
	

	
	private void startKeyPressBroadcastTest() {
		try
		{
			EditText keyPressEditor = (EditText)findViewById(R.id.key_code_to_send);
			String keyCode = keyPressEditor.getEditableText().toString();
			keyPressEditor.setText("");
			keyPressEditor.requestFocus();
			int keyCodeToSend = Integer.parseInt(keyCode);
			Intent keyPressRequired = new Intent("com.anysoftkeyboard.PRESS_KEY");
			keyPressRequired.putExtra("key_code", keyCodeToSend);
			getApplicationContext().sendBroadcast(keyPressRequired);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
					.setTitle("Error")
					.setMessage("Failed to do key press test due to: "+e.getMessage())
					.create();
			dialog.show();
		}
	}


	protected Dialog createSpinningDialog(String title) {
		final Dialog progress = new Dialog(this);
		progress.setContentView(R.layout.please_wait);
		progress.setTitle(title);
		progress.show();
		return progress;
	}
}
