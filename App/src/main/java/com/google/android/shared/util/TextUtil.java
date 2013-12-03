package com.google.android.shared.util;

import android.text.Annotation;

public class TextUtil {
    public static String convertUpperCaseToHumanReadable(String paramString, boolean paramBoolean) {
        return convertUpperCaseToOther(paramString, true, paramBoolean, true);
    }

    private static String convertUpperCaseToOther(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
        int i = paramString.length();
        StringBuilder localStringBuilder = new StringBuilder(i);
        boolean bool = paramBoolean1;
        int j = 0;
        if (j < i) {
            char c = paramString.charAt(j);
            if (c == '_') {
                bool = paramBoolean2;
                if (paramBoolean3) {
                    localStringBuilder.append(' ');
                }
            }
            for (; ; ) {
                j++;
                break;
                if (bool) {
                    localStringBuilder.append(c);
                    bool = false;
                } else {
                    localStringBuilder.append(Character.toLowerCase(c));
                }
            }
        }
        return localStringBuilder.toString();
    }

    public static String createLinkTag(String paramString1, String paramString2) {
        return getStartLinkReplace(paramString2) + paramString1 + "</a>";
    }

    private static String getStartLinkReplace(String paramString) {
        return "<a href=\"" + paramString + "\">";
    }

    public static boolean isForceUppercase(Annotation paramAnnotation) {
        return "com.google.android.voicesearch.FORCE_UPPERCASE".equals(paramAnnotation.getKey());
    }

    public static String replaceLink(String paramString1, String paramString2) {
        return replaceTags(paramString1.replace("START_LINK", getStartLinkReplace(paramString2)).replace("END_LINK", "</a>"));
    }

    public static String replaceTags(String paramString) {
        return paramString.replace("NEW_LINE", "<br/>");
    }

    public static String safeToString(Object paramObject) {
        if (paramObject == null) {
            return "";
        }
        return paramObject.toString();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.TextUtil

 * JD-Core Version:    0.7.0.1

 */