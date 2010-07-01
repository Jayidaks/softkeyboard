package com.anysoftkeyboard.dictionarycreator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;

public class ParserThread extends Thread {

	private final UI mUi;
	private final int mTotalChars;
	private final InputStreamReader mInput;
	private final OutputStreamWriter mOutput;
	private final HashSet<Character> mLangChars;
	private final HashSet<Character> mLangInnerChars;
	private final HashMap<String, Integer> mWords;
	
	public ParserThread(InputStreamReader input, int totalChars, OutputStreamWriter output, UI ui, String languageCharacters, String additionalInnerCharacters) {
		mUi = ui;
		mInput = input;
		mTotalChars = totalChars;
		mOutput = output;
		
		mLangChars = new HashSet<Character>(languageCharacters.length());
		for (int i = 0; i < languageCharacters.length(); ++i)
			mLangChars.add(languageCharacters.charAt(i));
		
		mLangInnerChars = new HashSet<Character>(additionalInnerCharacters.length());
		for (int i = 0; i < additionalInnerCharacters.length(); ++i)
			mLangInnerChars.add(additionalInnerCharacters.charAt(i));
		
		mWords = new HashMap<String, Integer>();
	}
	
	private final static int LOOKING_FOR_WORD_START = 0;
	private final static int INSIDE_WORD = 1;
	
	@Override
	public void run() {
		super.run();
		mUi.updateProgressState("Parser started", 0);
		try
		{
			StringBuilder word = new StringBuilder();
			int state = LOOKING_FOR_WORD_START;
			
			int read = 0;
			int intChar;
			while((intChar = mInput.read()) > 0)
			{
				if ((read % 50000) == 0)
				{
					mUi.updateProgressState("Parsing (have "+mWords.size()+" words)...", (int)((100f * (float)read) / (float)mTotalChars));
				}
				char currentChar = (char)intChar;
				read++;
				switch(state)
				{
				case LOOKING_FOR_WORD_START:
					if (mLangChars.contains(currentChar))
					{
						word.append(currentChar);
						state = INSIDE_WORD;
					}
					break;
				case INSIDE_WORD:
					if (mLangChars.contains(currentChar) || mLangInnerChars.contains(currentChar))
					{
						word.append(currentChar);
					}
					else
					{
						//removing all none chars from the end.
						while(mLangInnerChars.contains(word.charAt(word.length()-1)))
							word.setLength(word.length()-1);
						
						String wordFound = word.toString().toLowerCase();
						if (mWords.containsKey(wordFound))
						{
							mWords.put(wordFound, mWords.get(wordFound)+1);
						}
						else
						{
							mWords.put(wordFound, 1);
						}
						word.setLength(0);
						state = LOOKING_FOR_WORD_START;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			mUi.showErrorMessage(e.getMessage());
		}
		finally
		{
			mUi.updateProgressState("Parser ending...", 0);
			try {
				mInput.close();
				mOutput.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
				mOutput.write("<wordlist>\n");
				for(String word : mWords.keySet())
				{
					mOutput.write("<w f=\""+mWords.get(word)+"\">"+word+"</w>\n");
				}
				mOutput.write("</wordlist>\n");
				mOutput.flush();
				mOutput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mUi.showErrorMessage(e.getMessage());
			}
			mUi.updateProgressState("Parser ended. Found "+mWords.size()+" words.", 100);
			mUi.onEnded();
		}
	}

}
