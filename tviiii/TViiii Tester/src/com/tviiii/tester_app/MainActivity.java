package com.tviiii.tester_app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.HashMap;

import com.tviiii.tester_app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
		
		findViewById(R.id.do_login_test_button).setOnClickListener(this);
		
		final Dialog progress = createSpinningDialog("Reading defaults");
		progress.show();
		new AsyncTask<Void, Void, Void>()
		{
			CharSequence loginFileContent = "";
			
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
			};
		}.execute();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		String text = "Key pressed! Value "+keyCode+", which is ";
		if (mKeyCodeNames.containsKey(keyCode))
		{
			text += mKeyCodeNames.get(keyCode);
		}
		else
		{
			text += "UNKNOWN API KEYCODE!";
		}
		mKeyCodeText.setText(text);
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.do_login_test_button:
			startLoginBroadcastTest();
			break;
		}
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


	protected Dialog createSpinningDialog(String title) {
		final Dialog progress = new Dialog(this);
		progress.setContentView(R.layout.please_wait);
		progress.setTitle(title);
		progress.show();
		return progress;
	}
}
