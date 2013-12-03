package com.google.android.shared.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Util {
    private static final Pattern ACCEPTED_URI_SCHEMA = Pattern.compile("(?i)((?:http|https):\\/\\/|(?:.*:.*@))(.*)");
    public static final int SDK_INT = Build.VERSION.SDK_INT;
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static boolean arrayContains(String[] paramArrayOfString, String paramString) {
        int i = paramArrayOfString.length;
        for (int j = 0; j < i; j++) {
            if (TextUtils.equals(paramArrayOfString[j], paramString)) {
                return true;
            }
        }
        return false;
    }

    public static <T> ArrayList<T> asArrayList(List<T> paramList) {
        if ((paramList == null) || ((paramList instanceof ArrayList))) {
            return (ArrayList) paramList;
        }
        return Lists.newArrayList(paramList);
    }

    public static String asString(Object paramObject) {
        if (paramObject == null) {
            return null;
        }
        return paramObject.toString();
    }

    @TargetApi(18)
    public static void assertNotInLayout(View paramView) {
        if (SDK_INT >= 18) {
            if (paramView.isInLayout()) {
                break label22;
            }
        }
        label22:
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            return;
        }
    }

    public static String boundedDebugString(String paramString, int paramInt) {
        if (paramString == null) {
            paramString = "null";
            return paramString;
        }
        if (paramInt >= 0) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            if (paramString.length() <= paramInt) {
                break;
            }
            return paramString.substring(0, paramInt) + " ...";
        }
    }

    public static Uri.Builder buildUriFromParent(Uri paramUri) {
        Uri.Builder localBuilder = paramUri.buildUpon().path("/");
        List localList = paramUri.getPathSegments();
        for (int i = 0; i < -1 + localList.size(); i++) {
            localBuilder.appendEncodedPath((String) localList.get(i));
        }
        return localBuilder;
    }

    @Nullable
    public static String bundleToString(@Nullable Bundle paramBundle) {
        if (paramBundle == null) {
            return null;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append('{');
        int i = 1;
        Iterator localIterator = paramBundle.keySet().iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            if (i == 0) {
                localStringBuilder.append(", ");
            }
            Object localObject = paramBundle.get(str);
            localStringBuilder.append(str).append("=").append(localObject);
            i = 0;
        }
        localStringBuilder.append('}');
        return localStringBuilder.toString();
    }

    public static Map<String, String> bundleToStringMap(@Nullable Bundle paramBundle) {
        HashMap localHashMap = Maps.newHashMap();
        if (paramBundle != null) {
            Iterator localIterator = paramBundle.keySet().iterator();
            while (localIterator.hasNext()) {
                String str1 = (String) localIterator.next();
                String str2 = paramBundle.getString(str1);
                if (str2 != null) {
                    localHashMap.put(str1, str2);
                }
            }
        }
        return localHashMap;
    }

    public static void closeQuietly(@Nullable AssetFileDescriptor paramAssetFileDescriptor) {
        if (paramAssetFileDescriptor != null) {
        }
        try {
            paramAssetFileDescriptor.close();
            return;
        } catch (IOException localIOException) {
        }
    }

    public static final int compareAsStrings(Object paramObject1, Object paramObject2) {
        String str1 = asString(paramObject1);
        String str2 = asString(paramObject2);
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return 1;
        }
        if (str2 == null) {
            return -1;
        }
        return str1.compareTo(str2);
    }

    public static <T> Set<T> copyOf(@Nullable Set<T> paramSet) {
        HashSet localHashSet = Sets.newHashSet();
        if (paramSet != null) {
            localHashSet.addAll(paramSet);
        }
        return localHashSet;
    }

    public static boolean equalsIgnoreCase(String paramString1, String paramString2) {
        if ((paramString1 == null) && (paramString2 == null)) {
            return true;
        }
        if ((paramString1 == null) || (paramString2 == null)) {
            return false;
        }
        return paramString1.equalsIgnoreCase(paramString2);
    }

    public static String getLocaleString() {
        Locale localLocale = Locale.getDefault();
        StringBuilder localStringBuilder = new StringBuilder(localLocale.getLanguage().toLowerCase(Locale.US));
        String str = localLocale.getCountry();
        if (!TextUtils.isEmpty(str)) {
            localStringBuilder.append('-');
            localStringBuilder.append(str.toUpperCase(Locale.US));
        }
        return localStringBuilder.toString();
    }

    public static Pair<Resources, Integer> getResourceId(Context paramContext, Uri paramUri)
            throws FileNotFoundException {
        if (!"android.resource".equals(paramUri.getScheme())) {
            throw new FileNotFoundException("Not an android.resource URI: " + paramUri);
        }
        String str = paramUri.getAuthority();
        if (TextUtils.isEmpty(str)) {
            throw new FileNotFoundException("No authority: " + paramUri);
        }
        Resources localResources;
        List localList;
        try {
            localResources = paramContext.getPackageManager().getResourcesForApplication(str);
            localList = paramUri.getPathSegments();
            if (localList == null) {
                throw new FileNotFoundException("No path: " + paramUri);
            }
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            throw new FileNotFoundException("Failed to get resources: " + localNameNotFoundException);
        }
        int i = localList.size();
        if (i == 1) {
        }
        int j;
        for (; ; ) {
            try {
                int k = Integer.parseInt((String) localList.get(0));
                j = k;
                if (j != 0) {
                    break;
                }
                throw new FileNotFoundException("No resource found for: " + paramUri);
            } catch (NumberFormatException localNumberFormatException) {
                throw new FileNotFoundException("Single path segment is not a resource ID: " + paramUri);
            }
            if (i == 2) {
                j = localResources.getIdentifier((String) localList.get(1), (String) localList.get(0), str);
            } else {
                throw new FileNotFoundException("More than two path segments: " + paramUri);
            }
        }
        return new Pair(localResources, Integer.valueOf(j));
    }

    public static Uri getResourceUri(Context paramContext, int paramInt) {
        try {
            Uri localUri = getResourceUri(paramContext.getResources(), paramContext.getPackageName(), paramInt);
            return localUri;
        } catch (Resources.NotFoundException localNotFoundException) {
            Log.e("Search.Util", "Resource not found: " + paramInt + " in " + paramContext.getPackageName());
        }
        return null;
    }

    public static Uri getResourceUri(PackageManager paramPackageManager, ApplicationInfo paramApplicationInfo, int paramInt) {
        try {
            Uri localUri = getResourceUri(paramPackageManager.getResourcesForApplication(paramApplicationInfo), paramApplicationInfo.packageName, paramInt);
            return localUri;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            Log.e("Search.Util", "Resources not found for " + paramApplicationInfo.packageName);
            return null;
        } catch (Resources.NotFoundException localNotFoundException) {
            Log.e("Search.Util", "Resource not found: " + paramInt + " in " + paramApplicationInfo.packageName);
        }
        return null;
    }

    private static Uri getResourceUri(Resources paramResources, String paramString, int paramInt)
            throws Resources.NotFoundException {
        return makeResourceUri(paramString, paramResources.getResourcePackageName(paramInt), paramResources.getResourceTypeName(paramInt), paramResources.getResourceEntryName(paramInt));
    }

    public static void hideSoftKeyboard(Context paramContext, View paramView) {
        ((InputMethodManager) paramContext.getSystemService("input_method")).hideSoftInputFromWindow(paramView.getWindowToken(), 0);
    }

    public static boolean isEmpty(Object paramObject) {
        return (paramObject == null) || (TextUtils.isEmpty(paramObject.toString()));
    }

    public static boolean isEnterKey(KeyEvent paramKeyEvent) {
        int i = paramKeyEvent.getKeyCode();
        return (i == 66) || (i == 160);
    }

    public static boolean isLowRamDevice(Context paramContext) {
        int i = SDK_INT;
        boolean bool = false;
        if (i >= 19) {
            bool = ((ActivityManager) paramContext.getSystemService("activity")).isLowRamDevice();
        }
        return bool;
    }

    public static final String[] jsonToStringArray(String paramString) {
        if (!TextUtils.isEmpty(paramString)) {
            ArrayList localArrayList = Lists.newArrayList();
            JsonReader localJsonReader = new JsonReader(new StringReader(paramString));
            try {
                localJsonReader.beginArray();
                while (localJsonReader.peek() != JsonToken.END_ARRAY) {
                    localArrayList.add(localJsonReader.nextString());
                }
                String[] arrayOfString;
                return new String[0];
            } catch (IOException localIOException) {
                Log.w("Search.Util", "IOException reading string map from JSON", localIOException);
                return null;
                localJsonReader.endArray();
                arrayOfString = (String[]) localArrayList.toArray(new String[localArrayList.size()]);
                return arrayOfString;
            } finally {
                Closeables.closeQuietly(localJsonReader);
            }
        }
    }

    /* Error */
    public static final Map<String, String> jsonToStringMap(String paramString) {
        // Byte code:
        //   0: invokestatic 181	com/google/common/collect/Maps:newHashMap	()Ljava/util/HashMap;
        //   3: astore_1
        //   4: aload_0
        //   5: invokestatic 253	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
        //   8: ifne +71 -> 79
        //   11: new 416	android/util/JsonReader
        //   14: dup
        //   15: new 418	java/io/StringReader
        //   18: dup
        //   19: aload_0
        //   20: invokespecial 419	java/io/StringReader:<init>	(Ljava/lang/String;)V
        //   23: invokespecial 422	android/util/JsonReader:<init>	(Ljava/io/Reader;)V
        //   26: astore_2
        //   27: aload_2
        //   28: invokevirtual 466	android/util/JsonReader:beginObject	()V
        //   31: aload_2
        //   32: invokevirtual 429	android/util/JsonReader:peek	()Landroid/util/JsonToken;
        //   35: getstatic 469	android/util/JsonToken:NAME	Landroid/util/JsonToken;
        //   38: if_acmpne +43 -> 81
        //   41: aload_1
        //   42: aload_2
        //   43: invokevirtual 472	android/util/JsonReader:nextName	()Ljava/lang/String;
        //   46: aload_2
        //   47: invokevirtual 438	android/util/JsonReader:nextString	()Ljava/lang/String;
        //   50: invokeinterface 191 3 0
        //   55: pop
        //   56: goto -25 -> 31
        //   59: astore 4
        //   61: ldc_w 338
        //   64: ldc_w 443
        //   67: aload 4
        //   69: invokestatic 447	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   72: pop
        //   73: aconst_null
        //   74: astore_1
        //   75: aload_2
        //   76: invokestatic 452	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   79: aload_1
        //   80: areturn
        //   81: aload_2
        //   82: invokevirtual 475	android/util/JsonReader:endObject	()V
        //   85: aload_2
        //   86: invokestatic 452	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   89: aload_1
        //   90: areturn
        //   91: astore_3
        //   92: aload_2
        //   93: invokestatic 452	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   96: aload_3
        //   97: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	98	0	paramString	String
        //   3	87	1	localHashMap	HashMap
        //   26	67	2	localJsonReader	JsonReader
        //   91	6	3	localObject	Object
        //   59	9	4	localIOException	IOException
        // Exception table:
        //   from	to	target	type
        //   27	31	59	java/io/IOException
        //   31	56	59	java/io/IOException
        //   81	85	59	java/io/IOException
        //   27	31	91	finally
        //   31	56	91	finally
        //   61	73	91	finally
        //   81	85	91	finally
    }

    public static byte[] loadBytesFromRawResource(Resources paramResources, int paramInt) {
        InputStream localInputStream = null;
        try {
            localInputStream = paramResources.openRawResource(paramInt);
            byte[] arrayOfByte = ByteStreams.toByteArray(localInputStream);
            return arrayOfByte;
        } catch (IOException localIOException) {
            Log.e("Search.Util", "Failed to load raw resource " + paramInt, localIOException);
            return null;
        } finally {
            Closeables.closeQuietly(localInputStream);
        }
    }

    public static Drawable loadDrawableResource(Context paramContext, Uri paramUri)
            throws FileNotFoundException {
        Pair localPair = getResourceId(paramContext, paramUri);
        try {
            Drawable localDrawable = ((Resources) localPair.first).getDrawable(((Integer) localPair.second).intValue());
            return localDrawable;
        } catch (Resources.NotFoundException localNotFoundException) {
            throw new FileNotFoundException("Resource does not exist: " + paramUri);
        }
    }

    @Nonnull
    public static Map<String, String> makeMapFromString(char paramChar1, char paramChar2, String paramString) {
        HashMap localHashMap = Maps.newHashMap();
        if (!TextUtils.isEmpty(paramString)) {
            String str1 = Pattern.quote(String.valueOf(paramChar1));
            String str2 = Pattern.quote(String.valueOf(paramChar2));
            String[] arrayOfString1 = paramString.split(str1);
            int i = arrayOfString1.length;
            int j = 0;
            if (j < i) {
                String str3 = arrayOfString1[j].trim();
                if (TextUtils.isEmpty(str3)) {
                }
                for (; ; ) {
                    j++;
                    break;
                    String[] arrayOfString2 = str3.split(str2);
                    if (arrayOfString2.length != 2) {
                        throw new IllegalArgumentException("Cannot parse key-value pair: " + str3);
                    }
                    localHashMap.put(arrayOfString2[0].trim(), arrayOfString2[1].trim());
                }
            }
        }
        return localHashMap;
    }

    private static Uri makeResourceUri(String paramString1, String paramString2, String paramString3, String paramString4) {
        Uri.Builder localBuilder = new Uri.Builder();
        localBuilder.scheme("android.resource");
        localBuilder.encodedAuthority(paramString1);
        localBuilder.appendEncodedPath(paramString3);
        if (!paramString1.equals(paramString2)) {
            localBuilder.appendEncodedPath(paramString2 + ":" + paramString4);
        }
        for (; ; ) {
            return localBuilder.build();
            localBuilder.appendEncodedPath(paramString4);
        }
    }

    public static String removeTrailingSuffix(String paramString1, String paramString2) {
        if (paramString1.endsWith(paramString2)) {
            paramString1 = paramString1.substring(0, paramString1.length() - paramString2.length());
        }
        return paramString1;
    }

    public static Uri smartUrlFilter(String paramString) {
        String str1 = paramString.trim();
        int i;
        if (str1.indexOf(' ') != -1) {
            i = 1;
            if (i == 0) {
                break label28;
            }
        }
        label28:
        do {
            return null;
            i = 0;
            break;
            Matcher localMatcher = ACCEPTED_URI_SCHEMA.matcher(str1);
            if (localMatcher.matches()) {
                String str2 = localMatcher.group(1);
                String str3 = str2.toLowerCase();
                if (!str3.equals(str2)) {
                    str1 = str3 + localMatcher.group(2);
                }
                return Uri.parse(str1);
            }
        } while (!Patterns.WEB_URL.matcher(str1).matches());
        return Uri.parse(URLUtil.guessUrl(str1));
    }

    public static Bundle stringMapToBundle(@Nullable Map<String, String> paramMap) {
        Bundle localBundle = new Bundle();
        if (paramMap != null) {
            Iterator localIterator = paramMap.entrySet().iterator();
            while (localIterator.hasNext()) {
                Map.Entry localEntry = (Map.Entry) localIterator.next();
                localBundle.putString((String) localEntry.getKey(), (String) localEntry.getValue());
            }
        }
        return localBundle;
    }

    public static String toResourceUriString(String paramString, int paramInt) {
        return "android.resource://" + paramString + "/" + paramInt;
    }

    public static String toResourceUriString(String paramString1, String paramString2) {
        if ((!TextUtils.isEmpty(paramString2)) && (Character.isDigit(paramString2.charAt(0)))) {
            paramString2 = "android.resource://" + paramString1 + "/" + paramString2;
        }
        return paramString2;
    }

    public static String[] tokenize(String paramString) {
        return paramString.split(" ");
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.Util

 * JD-Core Version:    0.7.0.1

 */