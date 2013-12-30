package com.google.android.speech.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.shared.util.LocaleUtils;
import com.google.android.speech.SpeechSettings;
import com.google.common.base.Preconditions;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.annotation.Nullable;

public class SpokenLanguageUtils {
    private static final Locale DEFAULT_LOCALE = Locale.US;

    public static String getDefaultMainSpokenLanguageBcp47(String phoneJavaLocale, GstaticConfiguration.Configuration configuration) {
        if (configuration.getLanguagesList().iterator().hasNext()) {
            GstaticConfiguration.Language language = configuration.getLanguagesList().iterator().next();
            for (GstaticConfiguration.Dialect dialect : language.getDialectList()) {
                if (dialect.getJavaLocalesList().contains(phoneJavaLocale)) {
                    return dialect.getBcp47Locale();
                }
                if (phoneJavaLocale.contains("_")) {
                    phoneJavaLocale = phoneJavaLocale.substring(0x0, phoneJavaLocale.lastIndexOf(0x5f));
                    return getDefaultMainSpokenLanguageBcp47(phoneJavaLocale, configuration);
                }
            }
        }
        return "en-001";
    }

    public static GstaticConfiguration.Dialect getDialectByDisplayName(GstaticConfiguration.Configuration paramConfiguration, String paramString) {
        Iterator localIterator = paramConfiguration.getLanguagesList().iterator();
        while (localIterator.hasNext()) {
            GstaticConfiguration.Dialect localDialect = getDialectByDisplayName((GstaticConfiguration.Language) localIterator.next(), paramString);
            if (localDialect != null) {
                return localDialect;
            }
        }
        return null;
    }

    private static GstaticConfiguration.Dialect getDialectByDisplayName(GstaticConfiguration.Language paramLanguage, String paramString) {
        Iterator localIterator = paramLanguage.getDialectList().iterator();
        while (localIterator.hasNext()) {
            GstaticConfiguration.Dialect localDialect = (GstaticConfiguration.Dialect) localIterator.next();
            if (localDialect.getDisplayName().equals(paramString)) {
                return localDialect;
            }
        }
        return null;
    }

    public static CharSequence[] getDisplayNames(GstaticConfiguration.Dialect[] dialects) {
        CharSequence[] displayNames = new CharSequence[dialects.length];
        for (int i = 0x0; i < dialects.length; i = i + 0x1) {
            displayNames[i] = dialects[i].getDisplayName();
        }
        return displayNames;
    }

    public static String getDisplayName(GstaticConfiguration.Configuration configuration, String localeBcp47) {
        if (configuration.getLanguagesList().iterator().hasNext()) {
            GstaticConfiguration.Language language = configuration.getLanguagesList().iterator().next();
            for (GstaticConfiguration.Dialect mainLanguage : language.getDialectList()) {
                if (mainLanguage.getBcp47Locale().equals(localeBcp47)) {
                    return mainLanguage.getDisplayName();
                }
                Log.e("SpokenLanguageUtils", "No display name for: " + localeBcp47);
            }
        }
        return "";
    }

    public static ArrayList<String> getEmbeddedBcp47(GstaticConfiguration.Configuration paramConfiguration) {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = paramConfiguration.getEmbeddedRecognitionResourcesList().iterator();
        while (localIterator.hasNext()) {
            localArrayList.add(((GstaticConfiguration.LanguagePack) localIterator.next()).getBcp47Locale());
        }
        return localArrayList;
    }

    public static GstaticConfiguration.Dialect getLanguageDialect(GstaticConfiguration.Configuration configuration, String bcp47Locale) {
        if (configuration.getLanguagesList().iterator().hasNext()) {
            GstaticConfiguration.Language language = configuration.getLanguagesList().iterator().next();
            for (GstaticConfiguration.Dialect dialect : language.getDialectList()) {
                if (dialect.getBcp47Locale().equals(bcp47Locale)) {
                    return dialect;
                }
            }
        }
        return null;
    }

    public static String[] getLanguageDisplayNames(GstaticConfiguration.Configuration paramConfiguration, @Nullable String paramString) {
        Preconditions.checkNotNull(paramConfiguration);
        String[] arrayOfString = new String[paramConfiguration.getLanguagesCount()];
        int i = 0;
        if (i < arrayOfString.length) {
            if (paramConfiguration.getLanguages(i).getDialectCount() == 1) {
                arrayOfString[i] = paramConfiguration.getLanguages(i).getDialect(0).getDisplayName();
            }
            for (; ; ) {
                i++;
                break;
                if (paramString != null) {
                    arrayOfString[i] = (paramConfiguration.getLanguages(i).getDisplayName() + paramString);
                } else {
                    arrayOfString[i] = paramConfiguration.getLanguages(i).getDisplayName();
                }
            }
        }
        return arrayOfString;
    }

    public static Locale getMainJavaLocaleForBcp47(GstaticConfiguration.Configuration paramConfiguration, String paramString) {
        GstaticConfiguration.Dialect localDialect = getLanguageDialect(paramConfiguration, paramString);
        if ((localDialect == null) || (!localDialect.hasMainJavaLocale())) {
            return DEFAULT_LOCALE;
        }
        return LocaleUtils.parseJavaLocale(localDialect.getMainJavaLocale(), DEFAULT_LOCALE);
    }

    public static String getSpokenBcp47Locale(SpeechSettings paramSpeechSettings, @Nullable String paramString) {
        if (paramString != null) {
            if (getLanguageDialect(paramSpeechSettings.getConfiguration(), paramString) != null) {
                return paramString;
            }
            GstaticConfiguration.Dialect localDialect = getSpokenLanguageByJavaLocale(paramSpeechSettings.getConfiguration(), paramString);
            if (localDialect != null) {
                return localDialect.getBcp47Locale();
            }
        }
        return paramSpeechSettings.getSpokenLocaleBcp47();
    }

    public static String getSpokenBcp47Locale(GstaticConfiguration.Configuration paramConfiguration, String[] paramVarArgs) {
        int i = paramVarArgs.length;
        for (int j = 0; j < i; j++) {
            String str = getSpokenBcp47Locale(paramConfiguration, paramVarArgs[j]);
            if (str != null) {
                return str;
            }
        }
        return null;
    }

    public static GstaticConfiguration.Dialect getSpokenLanguageByBcp47Locale(GstaticConfiguration.Configuration configuration, String bcp47Locale) {
        if (configuration.getLanguagesList().iterator().hasNext()) {
            GstaticConfiguration.Language language = configuration.getLanguagesList().iterator().next();
            for (GstaticConfiguration.Dialect dialect : language.getDialectList()) {
                if (dialect.getBcp47Locale().equals(bcp47Locale)) {
                    return dialect;
                }
            }
        }
        return null;
    }

    public static String getSpokenBcp47Locale(GstaticConfiguration.Configuration configuration, String javaLocale) {
        if (configuration.getLanguagesList().iterator().hasNext()) {
            GstaticConfiguration.Language language = configuration.getLanguagesList().iterator().next();
            for (GstaticConfiguration.Dialect dialect : language.getDialectList()) {
                if (dialect.getJavaLocalesList().contains(javaLocale)) {
                    return dialect.getBcp47Locale();
                }
            }
        }
        return null;
    }

    public static GstaticConfiguration.Dialect getSpokenLanguageByJavaLocale(GstaticConfiguration.Configuration configuration, String javaLocale) {
        if (configuration.getLanguagesList().iterator().hasNext()) {
            GstaticConfiguration.Language language = configuration.getLanguagesList().iterator().next();
            for (GstaticConfiguration.Dialect dialect : language.getDialectList()) {
                if (dialect.getJavaLocalesList().contains(javaLocale)) {
                    return dialect;
                }
            }
        }

        return null;
    }

    public static ArrayList<String> getSupportedBcp47Locales(GstaticConfiguration.Configuration paramConfiguration) {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator1 = paramConfiguration.getLanguagesList().iterator();
        while (localIterator1.hasNext()) {
            Iterator localIterator2 = ((GstaticConfiguration.Language) localIterator1.next()).getDialectList().iterator();
            while (localIterator2.hasNext()) {
                localArrayList.add(((GstaticConfiguration.Dialect) localIterator2.next()).getBcp47Locale());
            }
        }
        return localArrayList;
    }

    public static ArrayList<String> getSupportedDisplayNames(GstaticConfiguration.Configuration paramConfiguration) {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator1 = paramConfiguration.getLanguagesList().iterator();
        while (localIterator1.hasNext()) {
            Iterator localIterator2 = ((GstaticConfiguration.Language) localIterator1.next()).getDialectList().iterator();
            while (localIterator2.hasNext()) {
                localArrayList.add(((GstaticConfiguration.Dialect) localIterator2.next()).getDisplayName());
            }
        }
        return localArrayList;
    }

    public static ArrayList<GstaticConfiguration.Dialect> getVoiceImeDialects(GstaticConfiguration.Configuration paramConfiguration) {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator1 = paramConfiguration.getLanguagesList().iterator();
        while (localIterator1.hasNext()) {
            Iterator localIterator2 = ((GstaticConfiguration.Language) localIterator1.next()).getDialectList().iterator();
            while (localIterator2.hasNext()) {
                GstaticConfiguration.Dialect localDialect = (GstaticConfiguration.Dialect) localIterator2.next();
                if (localDialect.getImeSupported()) {
                    localArrayList.add(localDialect);
                }
            }
        }
        return localArrayList;
    }

    public static GstaticConfiguration.Dialect getVoiceImeMainLanguage(GstaticConfiguration.Configuration configuration, String bcp47Locale) {
        if (configuration.getLanguagesList().iterator().hasNext()) {
            GstaticConfiguration.Language language = configuration.getLanguagesList().iterator().next();
            for (GstaticConfiguration.Dialect dialect : language.getDialectList()) {
                if ((dialect.getImeSupported()) && (dialect.getBcp47Locale().equals(bcp47Locale))) {
                    return dialect;
                }
            }
        }
        return null;
    }

    public static boolean isSupportedBcp47Locale(GstaticConfiguration.Configuration paramConfiguration, String paramString) {
        return !TextUtils.isEmpty(getDisplayName(paramConfiguration, paramString));
    }

}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.utils.SpokenLanguageUtils

 * JD-Core Version:    0.7.0.1

 */