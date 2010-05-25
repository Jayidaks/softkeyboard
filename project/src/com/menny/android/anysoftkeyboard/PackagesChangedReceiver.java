package com.menny.android.anysoftkeyboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.menny.android.anysoftkeyboard.dictionary.DictionaryFactory;
import com.menny.android.anysoftkeyboard.keyboards.KeyboardBuildersFactory;

public class PackagesChangedReceiver extends BroadcastReceiver {

	private static final String TAG = "ASK PkgChanged";
//	private static final Intent msDictionaryIntent = new Intent(ExternalDictionaryFactory.DictionaryBuilder.RECEIVER_INTERFACE);
//	private static final Intent msKeyboardIntent = new Intent(KeyboardBuildersFactory.KeyboardBuilder.RECEIVER_INTERFACE);
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null || intent.getData() == null || context == null)
			return;

		Log.d(TAG, new StringBuffer("Packages (").append(intent.getData())
				.append(") have been changed.").toString());
		boolean isDictionary = true;
		boolean isKeyboard = true;
//		msDictionaryIntent.setPackage(intent.getPackage());
//		msKeyboardIntent.setPackage(intent.getPackage());
//		
//		boolean isDictionary = (context.getPackageManager()
//				.queryBroadcastReceivers(msDictionaryIntent,
//						PackageManager.GET_RECEIVERS).size() > 0);
//		//if a dictionary was changed, we want to rebuild the keyboards, so they'll point  
//		//to the new dictionary.
//		boolean isKeyboard = isDictionary || 
//			(context.getPackageManager()
//				.queryBroadcastReceivers(msKeyboardIntent,
//						PackageManager.GET_RECEIVERS).size() > 0);
//		
//		isDictionary = isDictionary
//				|| context.getPackageName().equals(intent.getPackage());
//
//		isKeyboard = isKeyboard
//				|| context.getPackageName().equals(intent.getPackage());

		Log.d(TAG, new StringBuffer("Packages (").append(intent.getData())
				.append(") have been changed. Is dictionary ").append(
						isDictionary).append(", isKeyboard ")
				.append(isKeyboard).toString());

		if (isKeyboard) {
			Log.i(TAG,"Rebuilding Keyboards since (keyboard) packages have been changed.");
			
			KeyboardSwitcher keyboardSwitcher = KeyboardSwitcher.getInstance();
			if (keyboardSwitcher != null) {
				KeyboardBuildersFactory.resetBuildersCache();
				KeyboardSwitcher.getInstance().makeKeyboards(true);
			}
		}

		if (isDictionary) {
			Log.i(TAG, "Refreshing dictionaries since (dictionary) packages have been changed.");

			DictionaryFactory.releaseAllDictionaries();
			AnySoftKeyboard softKeyboard = AnySoftKeyboard.getInstance();
			if (softKeyboard != null) {
				softKeyboard.setMainDictionaryForCurrentKeyboard();
			}
		}
	}
}
