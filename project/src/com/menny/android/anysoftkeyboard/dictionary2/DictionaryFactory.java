package com.menny.android.anysoftkeyboard.dictionary2;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;
import com.menny.android.anysoftkeyboard.AnySoftKeyboardConfiguration;
import com.menny.android.anysoftkeyboard.dictionary2.ExternalDictionaryFactory.DictionaryBuilder;

public class DictionaryFactory
{
    private static UserDictionaryBase msUserDictionary = null;
    private static final List<Dictionary> msDictionaries;

    // Maps id to specific index in msDictionaries
    private static final Map<String, Integer> msDictionariesById;
    // Maps language to specific index in msDictionaries
    private static final Map<String, Integer> msDictionariesByLanguage;

    static
    {
        msDictionaries = new ArrayList<Dictionary>();
        msDictionariesById = new HashMap<String, Integer>();
        msDictionariesByLanguage = new HashMap<String, Integer>();
    }

    public synchronized static UserDictionaryBase createUserDictionary(AnyKeyboardContextProvider context)
    {
        if (msUserDictionary == null)
        {
            try
            {
                msUserDictionary = new AndroidUserDictionary(context);
                msUserDictionary.loadDictionary();
            }
            catch(final Exception ex)
            {
                Log.w("AnySoftKeyboard", "Failed to load 'AndroidUserDictionary' (could be that the platform does not support it). Will use fall-back dictionary. Error:"+ex.getMessage());
                try {
                    msUserDictionary = new FallbackUserDictionary(context);
                    msUserDictionary.loadDictionary();
                } catch (final Exception e) {
                    Log.e("AnySoftKeyboard", "Failed to load failback user dictionary!");
                    e.printStackTrace();
                }
            }
        }
        return msUserDictionary;
    }

    public synchronized static Dictionary getDictionaryByLanguage(final String language, AnyKeyboardContextProvider context){
        return getDictionaryImpl(language, null, context);
    }
    public synchronized static Dictionary getDictionaryById(final String id, AnyKeyboardContextProvider context){
        return getDictionaryImpl(null, id, context);
    }


    private synchronized static Dictionary getDictionaryImpl(final String language, final String id, AnyKeyboardContextProvider context)
    {
        final String languageFormat = language == null ? "(null)" : language;
        final String idFormat = id == null ? "(null)" : id;

        if (language != null && msDictionariesByLanguage.containsKey(language)) {
            return msDictionaries.get(msDictionariesByLanguage.get(language));
        }
        if (id != null && msDictionariesById.containsKey(id)) {
            return msDictionaries.get(msDictionariesById.get(id));
        }

        Dictionary dict = null;

        try
        {
            if(id == null) {
                if ((language == null) || (language.length() == 0 || ("none".equalsIgnoreCase(language)))) {
                    return null;
                }
            } else {
                dict = locateDictionaryByIdInFactory(language, context);
            }

            if(language == null) {
                if ((id == null) || (id.length() == 0 || ("none".equalsIgnoreCase(id)))) {
                    return null;
                }
            } else {
                dict = locateDictionaryByLanguageInFactory(language, context);
            }


//            if (language.equalsIgnoreCase("English")) {
//                dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("en_binary.mp3"));
//            } else if (language.equalsIgnoreCase("Hebrew")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("he_binary.mp3"));
//            } else if (language.equalsIgnoreCase("French")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("fr_binary.mp3"));
//            } else if (language.equalsIgnoreCase("German")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("de_binary.mp3"));
//            } else if (language.equalsIgnoreCase("Spanish")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("es_binary.mp3"));
//            } else if (language.equalsIgnoreCase("Swedish")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("sv_binary.mp3"));
//            } else if (language.equalsIgnoreCase("Russian")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("ru_binary.mp3"));
//            } else if (language.equalsIgnoreCase("Finnish")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("fi_binary.mp3"));
//            } else if (language.equalsIgnoreCase("Dutch")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("nl_binary.mp3"));
//            } else if (language.equalsIgnoreCase("Slovenian")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("sl_binary.mp3"));
//            } else if (language.equalsIgnoreCase("Portuguese")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("pt_binary.mp3"));
//            } else if (language.equalsIgnoreCase("Bulgarian")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("bg_binary.mp3"));
//            } else if (language.equalsIgnoreCase("Ukrainian")) {
//            	dict = new BinaryDictionary(context.getApplicationContext().getAssets().openFd("uk_binary.mp3"));
//            } else {
//                return null;
//            }
            if (dict == null)
            {

                Log.d("DictionaryFactory",
                        MessageFormat.format("Could not locate dictionary for language {0} and id {1}. Maybe it was not loaded yet (installed recently?)",
                                new Object[]{languageFormat, idFormat}));
                ExternalDictionaryFactory.resetBuildersCache();
                //trying again
                if(id != null) {
                    dict = locateDictionaryByIdInFactory(language, context);
                }
                else if(language != null) {
                    dict = locateDictionaryByLanguageInFactory(language, context);
                }

                if (dict == null)
                    Log.w("DictionaryFactory",
                            MessageFormat.format("Could not locate dictionary for language {0} and id {1}.",
                                    new Object[]{languageFormat, idFormat}));
            }
            //checking again, cause it may have loaded the second try.
            if (dict != null)
            {
                final Dictionary dictToLoad = dict;
                final Thread loader = new Thread()
                {
                    @Override
                    public void run()
                    {
                        try {
                            dictToLoad.loadDictionary();
                        } catch (final Exception e) {
                            Log.e("DictionaryFactory", MessageFormat.format(
                                    "Failed load dictionary for language {0} with id {1}! Will reset the map. Error:{2}",
                                    new Object[]{languageFormat, idFormat, e.getMessage()}));
                            e.printStackTrace();
                            if(id != null) {
                                removeDictionaryById(id);
                            }else {
                                removeDictionaryByLanguage(language);
                            }
                        }
                    }
                };
                //a little less...
                loader.setPriority(Thread.NORM_PRIORITY - 1);
                loader.start();

                if(id != null) {
                    addDictionaryById(id, dict);
                }else {
                    addDictionaryByLanguage(language, dict);
                }

            }
        }
        catch(final Exception ex)
        {
            Log.e("DictionaryFactory", "Failed to load main dictionary for: "+language);
            ex.printStackTrace();
        }

        return dict;
    }


    private static Dictionary locateDictionaryByLanguageInFactory(final String language,
            AnyKeyboardContextProvider context)
            throws Exception {
        Dictionary dict = null;
        final ArrayList<DictionaryBuilder> allBuilders = ExternalDictionaryFactory.getAllBuilders(context.getApplicationContext());

        for(DictionaryBuilder builder : allBuilders)
        {
            if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()){
                Log.d("DictionaryFactory", MessageFormat.format("Checking if builder ''{0}'' with key ''{1}'' matches key ''{2}'''",
                        new Object[] {builder.getId(), builder.getLanguage(), language}));
            }
            if (builder.getLanguage().equalsIgnoreCase(language))
            {
                dict = builder.createDictionary();
                break;
            }
        }
        return dict;
    }

    private static Dictionary locateDictionaryByIdInFactory(final String id,
            AnyKeyboardContextProvider context)
            throws Exception {
        Dictionary dict = null;
        final ArrayList<DictionaryBuilder> allBuilders = ExternalDictionaryFactory.getAllBuilders(context.getApplicationContext());

        for(DictionaryBuilder builder : allBuilders)
        {
            if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()){
                Log.d("DictionaryFactory", MessageFormat.format("Checking if builder ''{0}'' with key ''{1}'' matches id ''{2}'''",
                        new Object[] {builder.getId(), builder.getLanguage(), id}));
            }
            if (builder.getId().equalsIgnoreCase(id))
            {
                dict = builder.createDictionary();
                break;
            }
        }
        return dict;
    }

    public synchronized static void addDictionaryByLanguage(String language, Dictionary dictionary)
    {
        // if there is previous mapping, remove it
        if (msDictionariesByLanguage.containsKey(language))
        {
            removeDictionaryByLanguage(language);
        }
        int position = msDictionaries.size();
        msDictionaries.add(dictionary);
        assert msDictionaries.get(position) == dictionary;

        msDictionariesByLanguage.put(language, position);
    }

    public synchronized static void addDictionaryById(String id, Dictionary dictionary)
    {
        // if there is previous mapping, remove it
        if (msDictionariesById.containsKey(id))
        {
            removeDictionaryById(id);
        }
        int position = msDictionaries.size();
        msDictionaries.add(dictionary);
        assert msDictionaries.get(position) == dictionary;

        msDictionariesById.put(id, position);
    }

    public synchronized static void removeDictionaryByLanguage(String language)
    {
        if (msDictionariesByLanguage.containsKey(language))
        {
            final int index = msDictionariesByLanguage.get(language);
            final Dictionary dict = msDictionaries.get(index);
            dict.close();
            msDictionaries.remove(index);
            msDictionariesById.remove(language);
            Collection<Integer> languageMappings = msDictionariesByLanguage.values();
            // Note that changes in this collection are mapped back to the map which
            // is what we want
            languageMappings.remove(index);
        }
    }

    public synchronized static void removeDictionaryById(String id)
    {
        if (msDictionariesById.containsKey(id))
        {
            final int index = msDictionariesById.get(id);
            final Dictionary dict = msDictionaries.get(index);
            dict.close();
            msDictionaries.remove(index);
            msDictionariesById.remove(id);
            Collection<Integer> idMappings = msDictionariesById.values();
            // Note that changes in this collection are mapped back to the map which
            // is what we want
            idMappings.remove(index);
        }
    }

    public synchronized static void close() {
        if (msUserDictionary != null) {
            msUserDictionary.close();
        }
        for(final Dictionary dict : msDictionaries) {
            dict.close();
        }

        msUserDictionary = null;
        msDictionaries.clear();
        msDictionariesById.clear();
        msDictionariesByLanguage.clear();
    }


    public static void releaseAllDictionaries()
    {
        close();
    }


    public synchronized static void onLowMemory(Dictionary currentlyUsedDictionary) {
        //I'll clear all dictionaries but the required.
        Dictionary dictToKeep = null;
        int index = msDictionaries.indexOf(currentlyUsedDictionary);
        if(index >= 0) {
            dictToKeep = msDictionaries.get(index);
        }

        String idMappingToDict = null;
        String languageMappingToDict = null;

        // We search first the id->dictionary mapping and if not found
        // then language->dictionary mapping
        {
        Iterator<Entry<String, Integer>> idIterator = msDictionariesById.entrySet().iterator();
        while(idIterator.hasNext()) {
            Entry<String, Integer> value = idIterator.next();
            if(value.getValue() == index) {
                idMappingToDict = value.getKey();
                break;
            }
        }
        }

        if(idMappingToDict == null){
            Iterator<Entry<String, Integer>> languageIterator = msDictionariesByLanguage.entrySet().iterator();
            while(languageIterator.hasNext()) {
                Entry<String, Integer> value = languageIterator.next();
                if(value.getValue() == index) {
                    languageMappingToDict = value.getKey();
                    break;
                }
            }
        }

        assert idMappingToDict != null || languageMappingToDict != null;

        msDictionaries.clear();
        msDictionariesByLanguage.clear();
        msDictionariesById.clear();

        if (dictToKeep != null)
        {
            if(idMappingToDict != null){
            	addDictionaryById(idMappingToDict, currentlyUsedDictionary);
            }else{
            	addDictionaryByLanguage(languageMappingToDict, currentlyUsedDictionary);
            }
        }
    }
//
//    private enum LanguageStrings
//    {
//        None,
//        English,
//        Hebrew,
//        French,
//        German,
//        Spanish,
//        Russian,
//        Arabic,
//        Lao,
//        Swedish,
//        Finnish,
//        Dutch,
//        Slovenian,
//        Portuguese,
//        Bulgarian,
//        Thai,
//        Ukrainian
//    }
//    public static List<String> getKnownDictionariesNames() {
//        final ArrayList<String> list = new ArrayList<String>();
//        for(final LanguageStrings lang : LanguageStrings.values()) {
//            list.add(lang.toString());
//        }
//
//        return list;
//    }
}
