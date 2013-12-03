package com.google.android.shared.util;

import android.text.TextUtils;

import java.util.Locale;

public class LocaleUtils {
    public static Locale parseJavaLocale(String paramString, Locale paramLocale) {
        if ((paramString == null) || (TextUtils.isEmpty(paramString))) {
            return paramLocale;
        }
        int i = paramString.indexOf('_');
        if (i == -1) {
            return new Locale(paramString);
        }
        String str1 = paramString.substring(0, i);
        int j = i + 1;
        int k = paramString.indexOf('_', j);
        if (k == -1) {
            return new Locale(str1, paramString.substring(j));
        }
        String str2 = paramString.substring(j, k);
        int m = k + 1;
        int n = paramString.indexOf('_', m);
        if (n == -1) {
        }
        for (String str3 = paramString.substring(m); ; str3 = paramString.substring(m, n)) {
            return new Locale(str1, str2, str3);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.LocaleUtils

 * JD-Core Version:    0.7.0.1

 */