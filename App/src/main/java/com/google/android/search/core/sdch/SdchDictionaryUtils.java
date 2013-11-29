package com.google.android.search.core.sdch;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import com.google.common.collect.Maps;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class SdchDictionaryUtils
{
  static int findHeaderSize(byte[] paramArrayOfByte)
  {
    int i = -1;
    for (int j = 0;; j++) {
      if (j < paramArrayOfByte.length)
      {
        if ((paramArrayOfByte[j] == 10) && (j != -1 + paramArrayOfByte.length) && (paramArrayOfByte[(j + 1)] == 10)) {
          i = j - 1;
        }
      }
      else {
        return i + 1;
      }
    }
  }
  
  @Nullable
  static Pair<String, String> getClientAndServerHashes(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte;
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA256");
      arrayOfByte = localMessageDigest.digest(paramArrayOfByte);
      if (arrayOfByte.length < 12)
      {
        Log.e("Velvet.SdchDictionaryUtils", "Unexpected digest length: " + arrayOfByte.length);
        return null;
      }
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      Log.e("Velvet.SdchDictionaryUtils", "No SHA256 Implementation on platform: " + localNoSuchAlgorithmException.getMessage());
      return null;
    }
    String str1 = new String(Base64.encode(arrayOfByte, 0, 6, 10));
    String str2 = new String(Base64.encode(arrayOfByte, 6, 6, 10));
    if ((str1.length() != 8) || (str2.length() != 8))
    {
      Log.e("Velvet.SdchDictionaryUtils", "Unexpected hashes : " + str1 + ", " + str2);
      return null;
    }
    return Pair.create(str1, str2);
  }
  
  @Nonnull
  static Map<String, String> parseHeaders(String paramString)
  {
    String[] arrayOfString = paramString.split("\n");
    HashMap localHashMap = Maps.newHashMap();
    int i = arrayOfString.length;
    int j = 0;
    if (j < i)
    {
      String str1 = arrayOfString[j];
      if (TextUtils.isEmpty(str1)) {}
      for (;;)
      {
        j++;
        break;
        int k = str1.indexOf(':');
        if (k < 1)
        {
          Log.w("Velvet.SdchDictionaryUtils", "Invalid dictionary header: " + str1);
        }
        else
        {
          String str2 = str1.substring(0, k).trim().toLowerCase();
          String str3 = str1.substring(k + 1).trim().toLowerCase();
          if ((TextUtils.isEmpty(str2)) || (TextUtils.isEmpty(str3))) {
            Log.w("Velvet.SdchDictionaryUtils", "Invalid key value pair in dict header: " + str2 + ", " + str3);
          } else {
            localHashMap.put(str2, str3);
          }
        }
      }
    }
    return localHashMap;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.sdch.SdchDictionaryUtils
 * JD-Core Version:    0.7.0.1
 */