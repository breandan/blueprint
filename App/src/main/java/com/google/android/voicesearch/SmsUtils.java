package com.google.android.voicesearch;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class SmsUtils {
    private static final Uri SMS_CONTENT_URI = Uri.parse("content://sms");

    private static long getOrCreateThreadId(Context paramContext, String paramString) {
        String[] arrayOfString = normalizeAddresses(paramString).split(",");
        HashSet localHashSet = new HashSet();
        int i = arrayOfString.length;
        for (int j = 0; j < i; j++) {
            localHashSet.add(arrayOfString[j]);
        }
        return getOrCreateThreadId(paramContext, localHashSet);
    }

    private static long getOrCreateThreadId(Context paramContext, Set<String> paramSet) {
        try {
            Method localMethod = Class.forName("android.provider.Telephony$Threads").getMethod("getOrCreateThreadId", new Class[]{Context.class, Set.class});
            long l = ((Long) localMethod.invoke(localMethod, new Object[]{paramContext, paramSet})).longValue();
            return l;
        } catch (IllegalArgumentException localIllegalArgumentException) {
            Log.e("SmsUtils", "error interacting with sms database", localIllegalArgumentException);
            return -1L;
        } catch (IllegalAccessException localIllegalAccessException) {
            for (; ; ) {
                Log.e("SmsUtils", "error interacting with sms database", localIllegalAccessException);
            }
        } catch (InvocationTargetException localInvocationTargetException) {
            for (; ; ) {
                Log.e("SmsUtils", "error interacting with sms database", localInvocationTargetException);
            }
        } catch (SecurityException localSecurityException) {
            for (; ; ) {
                Log.e("SmsUtils", "error interacting with sms database", localSecurityException);
            }
        } catch (NoSuchMethodException localNoSuchMethodException) {
            for (; ; ) {
                Log.e("SmsUtils", "error interacting with sms database", localNoSuchMethodException);
            }
        } catch (ClassNotFoundException localClassNotFoundException) {
            for (; ; ) {
                Log.e("SmsUtils", "error interacting with sms database", localClassNotFoundException);
            }
        }
    }

    public static void insertSentSmsIntoDatabase(Context paramContext, String paramString1, String paramString2) {
        long l = getOrCreateThreadId(paramContext, paramString2);
        if (l == -1L) {
            Log.e("SmsUtils", "not inserting into sms database");
            return;
        }
        ContentValues localContentValues = new ContentValues(6);
        localContentValues.put("thread_id", Long.valueOf(l));
        localContentValues.put("body", paramString1);
        localContentValues.put("type", Integer.valueOf(2));
        localContentValues.put("read", Integer.valueOf(1));
        localContentValues.put("seen", Integer.valueOf(1));
        localContentValues.put("address", paramString2);
        try {
            paramContext.getContentResolver().insert(SMS_CONTENT_URI, localContentValues);
            return;
        } catch (Exception localException) {
            Log.e("SmsUtils", "error inserting into sms database", localException);
        }
    }

    private static String normalizeAddresses(String paramString) {
        String[] arrayOfString = paramString.split(",");
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < arrayOfString.length; i++) {
            localStringBuilder.append(PhoneNumberUtils.stripSeparators(arrayOfString[i]));
            if (i != -1 + arrayOfString.length) {
                localStringBuilder.append(",");
            }
        }
        return localStringBuilder.toString();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.SmsUtils

 * JD-Core Version:    0.7.0.1

 */