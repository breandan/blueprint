package com.google.android.shared.util;

import android.text.Annotation;

public class TextUtil {

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