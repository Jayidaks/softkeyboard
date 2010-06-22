
package com.menny.android.anysoftkeyboard.theme;

import com.menny.android.anysoftkeyboard.AnySoftKeyboardConfiguration;
import com.menny.android.anysoftkeyboard.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ThemeResourcesBuildersFactory {

    private static final String TAG = "ASK ThemeResourcesBuildersFactory";

    private static final String XML_THEMES_TAG = "Themes";

    private static final String XML_THEME_TAG = "Theme";

    private static final String XML_PREF_ID_ATTRIBUTE = "id";

    private static final String XML_NAME_RES_ID_ATTRIBUTE = "nameResId";

    private static final String XML_DESCRIPTION_ATTRIBUTE = "description";

    private static final String XML_INDEX_ATTRIBUTE = "index";

    private static final String XML_THEME_DEFINITION_RES_ID_ATTRIBUTE = "definitionResId";

    private static final String XML_PREVIEW_RES_ID_ATTRIBUTE = "previewResId";

    public interface ExternalThemeResourcesBuilder {
        /**
         * This is the interface name that a broadcast receiver implementing an
         * external theme should say that it supports -- that is, this is the
         * action it uses for its intent filter.
         */
        public static final String RECEIVER_INTERFACE = "com.menny.android.anysoftkeyboard.THEME";

        /**
         * Name under which an external theme broadcast receiver component
         * publishes information about itself.
         */
        public static final String RECEIVER_META_DATA = "com.menny.android.anysoftkeyboard.themes";

    }

    private static Map<String, ThemeResourcesBuilder> ms_builders = null;

    private static ThemeResources ms_fallback_themeresources = null;

    public synchronized static void resetBuildersCache() {
        ms_builders = null;
        ms_fallback_themeresources = null;
        ThemeSwitcher.resetCachedTheme();
    }

    public static ThemeResources getFallbackThemeResources() {
        return ms_fallback_themeresources;
    }

    public synchronized static Map<String, ThemeResourcesBuilder> getAllBuilders(
            final Context context) {
        ThemeResources.initAttributeInfos(context);
        if (ms_builders == null) {
            // Stable ordering is guaranteed by LinkedHashMap
            ms_builders = new LinkedHashMap<String, ThemeResourcesBuilder>();
            List<ThemeResourcesBuilder> internalThemes = getThemePluginsFromResId(context,
                    R.xml.themes);
            List<ThemeResourcesBuilder> externalThemes = getAllExternalKeyboardCreators(context);

            if (internalThemes.size() > 0) {
                ms_fallback_themeresources = internalThemes.get(0).createThemeResources(context,
                        null);
            } else {
                Log.w(TAG, "No fallback ThemeResources found!");
            }

            for (ThemeResourcesBuilder builder : internalThemes) {
                ms_builders.put(builder.mId, builder);
            }

            // sorting the keyboards according to the requested
            // sort order (from minimum to maximum)
            Collections.sort(externalThemes, new Comparator<ThemeResourcesBuilder>() {
                public int compare(ThemeResourcesBuilder k1, ThemeResourcesBuilder k2) {

                    String key1 = k1.mPackageName + String.format("%08d%n", k1.mThemeIndex);
                    String key2 = k2.mPackageName + String.format("%08d%n", k2.mThemeIndex);

                    int value = key2.compareToIgnoreCase(key1);

                    // Log.d(TAG,
                    // "Collections.sort: "+key1+" vs "+key2+" = "+value);

                    return value;
                }
            });

            for (ThemeResourcesBuilder builder : externalThemes) {
                ms_builders.put(builder.mId, builder);
            }

        }

        return ms_builders;
    }

    private static List<ThemeResourcesBuilder> getAllExternalKeyboardCreators(Context context) {

        final List<ResolveInfo> broadcastReceivers = context.getPackageManager()
                .queryBroadcastReceivers(
                        new Intent(ExternalThemeResourcesBuilder.RECEIVER_INTERFACE),
                        PackageManager.GET_META_DATA);

        if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) {
            Log.d(TAG, "Number of potential external keyboard packages found: "
                    + broadcastReceivers.size());
        }

        final ArrayList<ThemeResourcesBuilder> externalKeyboardCreators = new ArrayList<ThemeResourcesBuilder>();
        for (final ResolveInfo receiver : broadcastReceivers) {
            // If activityInfo is null, we are probably dealing with a service.
            if (receiver.activityInfo == null) {
                Log.e(TAG, "BroadcastReceiver has null ActivityInfo. Receiver's label is "
                        + receiver.loadLabel(context.getPackageManager()));
                Log.e(TAG, "Is the external keyboard a service instead of BroadcastReceiver?");
                // Skip to next receiver
                continue;
            }

            try {
                final Context externalPackageContext = context.createPackageContext(
                        receiver.activityInfo.packageName, PackageManager.GET_META_DATA);
                final ArrayList<ThemeResourcesBuilder> packageKeyboardCreators = getThemePluginsFromActivityInfo(
                        externalPackageContext, receiver.activityInfo);

                externalKeyboardCreators.addAll(packageKeyboardCreators);
            } catch (final NameNotFoundException e) {
                Log.e(TAG, "Did not find package: " + receiver.activityInfo.packageName);
            }

        }
        if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) {
            Log.d(TAG, "Number of external keyboard creators successfully parsed: "
                    + externalKeyboardCreators.size());
        }

        return externalKeyboardCreators;
    }

    private static ArrayList<ThemeResourcesBuilder> getThemePluginsFromResId(Context context,
            int themesResId) {
        final XmlPullParser allThemes = context.getResources().getXml(themesResId);
        return parseThemeCreatorsFromXml(context, allThemes, context.getPackageName());
    }

    private static ArrayList<ThemeResourcesBuilder> getThemePluginsFromActivityInfo(
            Context context, ActivityInfo ai) {
        final XmlPullParser allThemes = ai.loadXmlMetaData(context.getPackageManager(),
                ExternalThemeResourcesBuilder.RECEIVER_META_DATA);
        return parseThemeCreatorsFromXml(context, allThemes, ai.packageName);
    }

    private static ArrayList<ThemeResourcesBuilder> parseThemeCreatorsFromXml(Context context,
            XmlPullParser allThemes, String packageName) {
        final ArrayList<ThemeResourcesBuilder> themes = new ArrayList<ThemeResourcesBuilder>();
        try {
            int event;
            boolean inThemes = false;
            while ((event = allThemes.next()) != XmlPullParser.END_DOCUMENT) {
                final String tag = allThemes.getName();
                if (event == XmlPullParser.START_TAG) {
                    if (XML_THEMES_TAG.equals(tag)) {
                        inThemes = true;
                        if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) {
                            Log.d(TAG, "Starting parsing " + XML_THEMES_TAG);
                        }
                    } else if (inThemes && XML_THEME_TAG.equals(tag)) {
                        if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) {
                            Log.d(TAG, "Starting parsing " + XML_THEME_TAG);
                        }

                        final AttributeSet attrs = Xml.asAttributeSet(allThemes);

                        final String prefId = attrs.getAttributeValue(null, XML_PREF_ID_ATTRIBUTE);
                        final int nameId = attrs.getAttributeResourceValue(null,
                                XML_NAME_RES_ID_ATTRIBUTE, -1);
                        final String description = attrs.getAttributeValue(null,
                                XML_DESCRIPTION_ATTRIBUTE);
                        final int themeIndex = attrs.getAttributeResourceValue(null,
                                XML_INDEX_ATTRIBUTE, 1);
                        final int themeDefinitionResId = attrs.getAttributeResourceValue(null,
                                XML_THEME_DEFINITION_RES_ID_ATTRIBUTE, -1);
                        final int previewDrawableResId = attrs.getAttributeResourceValue(null,
                                XML_PREVIEW_RES_ID_ATTRIBUTE, -1);

                        // asserting
                        if ((prefId == null) || (nameId == -1) || (themeDefinitionResId == -1)) {
                            Log
                                    .e(TAG,
                                            "External Theme does not include all mandatory details! Will not create Theme.");
                        } else {
                            if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) {
                                Log.d(TAG, "External Theme details: prefId:" + prefId + " nameId:"
                                        + nameId + " themeDefinitionResId:" + themeDefinitionResId
                                        + " themeIndex:" + themeIndex + " previewDrawableResId:"
                                        + previewDrawableResId);
                            }
                            final ThemeResourcesBuilder creator = new ThemeResourcesBuilder(
                                    packageName, prefId);
                            creator.mDescription = description;
                            creator.mNameResId = nameId;
                            creator.mPreviewDrawableResId = previewDrawableResId;
                            creator.mThemeDefinitionResId = themeDefinitionResId;
                            creator.mThemeIndex = themeIndex;

                            if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) {
                                Log.d(TAG, "External keyboard " + prefId + " will have a creator.");
                            }
                            themes.add(creator);
                        }

                    }
                } else if (event == XmlPullParser.END_TAG) {
                    if (XML_THEMES_TAG.equals(tag)) {
                        inThemes = false;
                        if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) {
                            Log.d(TAG, "Finished parsing " + XML_THEMES_TAG);
                        }
                        break;
                    } else if (inThemes && XML_THEME_TAG.equals(tag)) {
                        if (AnySoftKeyboardConfiguration.getInstance().getDEBUG()) {
                            Log.d(TAG, "Finished parsing " + XML_THEME_TAG);
                        }
                    }
                }
            }
        } catch (final IOException e) {
            Log.e(TAG, "IO error:" + e);
            e.printStackTrace();
        } catch (final XmlPullParserException e) {
            Log.e(TAG, "Parse error:" + e);
            e.printStackTrace();
        }

        return themes;
    }

}
