package com.google.android.speech.embedded;

import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.common.collect.Maps;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LanguagePackUtils {
    public static String buildDownloadFilename(GstaticConfiguration.LanguagePack paramLanguagePack) {
        return paramLanguagePack.getLanguagePackId() + ".zip";
    }

    public static GstaticConfiguration.LanguagePack findById(String paramString, List<GstaticConfiguration.LanguagePack> paramList) {
        int i = paramList.size();
        for (int j = 0; j < i; j++) {
            GstaticConfiguration.LanguagePack localLanguagePack = (GstaticConfiguration.LanguagePack) paramList.get(j);
            if (localLanguagePack.getLanguagePackId().equals(paramString)) {
                return localLanguagePack;
            }
        }
        return null;
    }

    public static Map<String, GstaticConfiguration.LanguagePack> getCompatibleLanguagePacks(Map<String, GstaticConfiguration.LanguagePack> paramMap, List<GstaticConfiguration.LanguagePack> paramList, int[] paramArrayOfInt, int paramInt) {
        HashMap localHashMap = Maps.newHashMap();
        Iterator localIterator1 = paramMap.values().iterator();
        while (localIterator1.hasNext()) {
            maybeAddCompatible(localHashMap, (GstaticConfiguration.LanguagePack) localIterator1.next(), paramArrayOfInt, paramInt);
        }
        Iterator localIterator2 = paramList.iterator();
        while (localIterator2.hasNext()) {
            maybeAddCompatible(localHashMap, (GstaticConfiguration.LanguagePack) localIterator2.next(), paramArrayOfInt, paramInt);
        }
        return localHashMap;
    }

    public static boolean isCompatible(GstaticConfiguration.LanguagePack languagePack, int[] supportedVersions, int deviceClass) {
        boolean compatibleWithEngine = false;
        int count = languagePack.getLanguagePackFormatVersionCount();
        if(count == 0) {
            return false;
        }
        int formatVersion = languagePack.getLanguagePackFormatVersion((count - 0x1));
        for(int i = 0x0; i < supportedVersions.length; i = i + 0x1) {
            if(supportedVersions[i] == formatVersion) {
                compatibleWithEngine = true;
                break;
            }
        }
        if(compatibleWithEngine) {
            if((deviceClass >= languagePack.getMinimumDeviceClass()) || (!languagePack.hasMinimumDeviceClass())) {
                return true;
            }
        }
        return false;
    }

    private static void maybeAddCompatible(HashMap<String, GstaticConfiguration.LanguagePack> paramHashMap, GstaticConfiguration.LanguagePack paramLanguagePack, int[] paramArrayOfInt, int paramInt) {
        String str = paramLanguagePack.getBcp47Locale();
        if ((isCompatible(paramLanguagePack, paramArrayOfInt, paramInt)) && ((!paramHashMap.containsKey(str)) || (((GstaticConfiguration.LanguagePack) paramHashMap.get(str)).getVersion() < paramLanguagePack.getVersion()))) {
            paramHashMap.put(str, paramLanguagePack);
        }
    }

    public static Comparator<GstaticConfiguration.LanguagePack> newLanguagePackComparator(final GstaticConfiguration.Configuration config) {
        return new Comparator<GstaticConfiguration.LanguagePack>() {
            @Override
            public int compare(GstaticConfiguration.LanguagePack lhs, GstaticConfiguration.LanguagePack rhs) {
                String a = SpokenLanguageUtils.getDisplayName(config, lhs.getBcp47Locale());
                String b = SpokenLanguageUtils.getDisplayName(config, rhs.getBcp47Locale());

                if(a == null) {
                    if(b == null)
                        return 0;
                    return -1;
                } else {
                    if(b == null) {
                        return 1;
                    }
                    return a.compareTo(b);
                }
            }
        };
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.LanguagePackUtils

 * JD-Core Version:    0.7.0.1

 */