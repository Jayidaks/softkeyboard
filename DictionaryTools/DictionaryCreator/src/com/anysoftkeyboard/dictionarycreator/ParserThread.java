package com.anysoftkeyboard.dictionarycreator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ParserThread extends Thread {

	private static class WordWithCount implements Comparable<WordWithCount>
	{
		public final String Word;
		private int mFreq;
		
		public WordWithCount(String word)
		{
			Word = word;
			mFreq = 1;
		}
		
		public int getFreq() {return mFreq;}
		
		public void addFreq() {mFreq++;}

		@Override
		public int compareTo(WordWithCount o) {
			return o.mFreq - mFreq;
		}
	}
	
	private final UI mUi;
	private final int mTotalChars;
	private final InputStreamReader mInput;
	private final int mTotalDictionaryChars;
	private final InputStreamReader mInputDictionary;
	private final OutputStreamWriter mOutput;
	private final HashSet<Character> mLangChars;
	private final HashSet<Character> mLangInnerChars;
	private final HashMap<String, WordWithCount> mWords;
	private final int mMaxWords;
	
	public ParserThread(InputStreamReader input, int totalChars, InputStreamReader dictionary, int totalDictionaryChars, OutputStreamWriter output, UI ui, String languageCharacters, String additionalInnerCharacters, int maxWords) {
		mUi = ui;
		mInput = input;
		mTotalChars = totalChars;
		mInputDictionary = dictionary;
		mTotalDictionaryChars = totalDictionaryChars;
		mOutput = output;
		mMaxWords = maxWords;
		
		mLangChars = new HashSet<Character>(languageCharacters.length());
		for (int i = 0; i < languageCharacters.length(); ++i)
			mLangChars.add(languageCharacters.charAt(i));
		
		mLangInnerChars = new HashSet<Character>(additionalInnerCharacters.length());
		for (int i = 0; i < additionalInnerCharacters.length(); ++i)
			mLangInnerChars.add(additionalInnerCharacters.charAt(i));
		
		mWords = new HashMap<String, WordWithCount>();
	}
	
	private final static int LOOKING_FOR_WORD_START = 0;
	private final static int INSIDE_WORD = 1;
	
	@Override
	public void run() {
		super.run();
		mUi.updateProgressState("Parser started", 0);
		
		try
		{
			if (mInputDictionary != null)
				addWordsFromInputStream(mInputDictionary, true, mTotalDictionaryChars);
			addWordsFromInputStream(mInput, (mInputDictionary == null), mTotalChars);
		} catch (IOException e) {
			e.printStackTrace();
			mUi.showErrorMessage(e.getMessage());
		}
		finally
		{
			mUi.updateProgressState("Parser ending...", 0);
			try {
				mInput.close();
				mUi.updateProgressState("Sorting words (have "+mWords.size()+" words)...", 0);
				List<WordWithCount> sortedList = removeNonFrequentWords();
				mUi.updateProgressState("Creating XML (will use "+mMaxWords+" words)...", 0);
				createXml(sortedList);
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

	private List<WordWithCount> removeNonFrequentWords() {
		List<WordWithCount> sortedList = new ArrayList<WordWithCount>(mMaxWords);
		sortedList.addAll(mWords.values());
		Collections.sort(sortedList);
		
		return sortedList;
	}

	private void createXml(List<WordWithCount> sortedList) throws IOException {
		mOutput.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		mOutput.write("<wordlist>\n");
		int wordIndex = 0;
		for(WordWithCount word : sortedList)
		{
			if (wordIndex > mMaxWords)
				break;
			
			mOutput.write("<w f=\""+word.getFreq()+"\">"+word.Word+"</w>\n");
			wordIndex++;
		}
		mOutput.write("</wordlist>\n");
	}

	private void addWordsFromInputStream(InputStreamReader input, boolean addFirst, int totalChars) throws IOException {
		StringBuilder word = new StringBuilder();
		int intChar;
		
		int state = LOOKING_FOR_WORD_START;
		int read = 0;
		while((intChar = input.read()) > 0)
		{
			if ((read % 50000) == 0)
			{
				mUi.updateProgressState("Parsing (have "+mWords.size()+" words)...", (int)((100f * (float)read) / (float)totalChars));
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
					addWord(word, addFirst);
					state = LOOKING_FOR_WORD_START;
				}
			}
		}
		//last word?
		if (word.length() > 0)
			addWord(word, addFirst);
	}

	private void addWord(StringBuilder word, boolean addFirst) {
		//removing all none chars from the end.
		while(mLangInnerChars.contains(word.charAt(word.length()-1)))
			word.setLength(word.length()-1);
		
		String wordFound = word.toString().toLowerCase();
		if (mWords.containsKey(wordFound))
		{//it must be inside already (from the dictionary)
			mWords.get(wordFound).addFreq();
		}
		else if (addFirst)
		{
			mWords.put(wordFound, new WordWithCount(wordFound));
		}
			
		word.setLength(0);
	}

}
