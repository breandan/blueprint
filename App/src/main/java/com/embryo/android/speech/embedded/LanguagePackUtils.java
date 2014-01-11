package com.embryo.android.speech.embedded;

import com.embryo.common.collect.Maps;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LanguagePackUtils {
    public static String buildDownloadFilename(com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack paramLanguagePack) {
        return paramLanguagePack.getLanguagePackId() + ".zip";
    }

    public static com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack findById(String paramString, List<com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack> paramList) {
        int i = paramList.size();
        for (int j = 0; j < i; j++) {
            com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack localLanguagePack = paramList.get(j);
            if (localLanguagePack.getLanguagePackId().equals(paramString)) {
                return localLanguagePack;
            }
        }
        return null;
    }

    public static Map<String, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack> getCompatibleLanguagePacks(Map<String, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack> paramMap, List<com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack> paramList, int[] paramArrayOfInt, int paramInt) {
        HashMap localHashMap = Maps.newHashMap();
        Iterator localIterator1 = paramMap.values().iterator();
        while (localIterator1.hasNext()) {
            maybeAddCompatible(localHashMap, (com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack) localIterator1.next(), paramArrayOfInt, paramInt);
        }
        Iterator localIterator2 = paramList.iterator();
        while (localIterator2.hasNext()) {
            maybeAddCompatible(localHashMap, (com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack) localIterator2.next(), paramArrayOfInt, paramInt);
        }
        return localHashMap;
    }

    public static boolean isCompatible(com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack languagePack, int[] supportedVersions, int deviceClass) {
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

    private static void maybeAddCompatible(HashMap<String, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack> paramHashMap, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack paramLanguagePack, int[] paramArrayOfInt, int paramInt) {
        String str = paramLanguagePack.getBcp47Locale();
        if ((isCompatible(paramLanguagePack, paramArrayOfInt, paramInt)) && ((!paramHashMap.containsKey(str)) || (((com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack) paramHashMap.get(str)).getVersion() < paramLanguagePack.getVersion()))) {
            paramHashMap.put(str, paramLanguagePack);
        }
    }

    public static Comparator<com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack> newLanguagePackComparator(final com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration config) {
        return new Comparator<com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack>() {
            @Override
            public int compare(com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack lhs, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack rhs) {
                String a = com.embryo.android.speech.utils.SpokenLanguageUtils.getDisplayName(config, lhs.getBcp47Locale());
                String b = com.embryo.android.speech.utils.SpokenLanguageUtils.getDisplayName(config, rhs.getBcp47Locale());

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

 * Qualified Name:     LanguagePackUtils

 * JD-Core Version:    0.7.0.1

 */