package com.google.android.search.core.sdch;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import com.google.android.shared.util.Clock;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

class SdchDictionary
{
  private final int mDictionaryOffset;
  private final DictionaryMetadata mMetadata;
  private final byte[] mVcDiffBytes;
  
  SdchDictionary(DictionaryMetadata paramDictionaryMetadata, byte[] paramArrayOfByte, int paramInt)
  {
    this.mMetadata = paramDictionaryMetadata;
    this.mVcDiffBytes = paramArrayOfByte;
    this.mDictionaryOffset = paramInt;
  }
  
  public static SdchDictionary parseAndValidateFrom(String paramString, byte[] paramArrayOfByte, Clock paramClock)
  {
    int i = SdchDictionaryUtils.findHeaderSize(paramArrayOfByte);
    if (i == 0) {
      Log.w("Velvet.SdchDictionary", "Invalid header for dictionary: " + paramString.toString());
    }
    String str1;
    String str2;
    String str3;
    Pair localPair;
    do
    {
      return null;
      if (i + 2 >= paramArrayOfByte.length)
      {
        Log.w("Velvet.SdchDictionary", "Empty dictionary: " + paramString.toString());
        return null;
      }
      Map localMap = SdchDictionaryUtils.parseHeaders(new String(paramArrayOfByte, 0, i));
      str1 = (String)localMap.get("domain");
      str2 = (String)localMap.get("path");
      str3 = (String)localMap.get("format-version");
      if (str1 == null)
      {
        Log.w("Velvet.SdchDictionary", "Dictionary missing Domain header");
        return null;
      }
      if (str2 == null)
      {
        Log.w("Velvet.SdchDictionary", "Dictionary missing path header");
        return null;
      }
      if ((str3 != null) && (!"1.0".equals(str3)))
      {
        Log.w("Velvet.SdchDictionary", "Unsupported dictionary format: " + str3);
        return null;
      }
      if (!Uri.parse(paramString).getHost().endsWith(str1))
      {
        Log.w("Velvet.SdchDictionary", "URL: " + paramString.toString() + " cannot set :" + str1);
        return null;
      }
      localPair = SdchDictionaryUtils.getClientAndServerHashes(paramArrayOfByte);
    } while (localPair == null);
    DictionaryMetadata localDictionaryMetadata = new DictionaryMetadata();
    localDictionaryMetadata.setClientHash((String)localPair.first);
    localDictionaryMetadata.setServerHash((String)localPair.second);
    localDictionaryMetadata.setDomain(str1);
    localDictionaryMetadata.setPath(str2);
    if (str3 != null) {
      localDictionaryMetadata.setFormatVersion(str3);
    }
    for (;;)
    {
      localDictionaryMetadata.setFetchTimeMs(paramClock.currentTimeMillis());
      return new SdchDictionary(localDictionaryMetadata, paramArrayOfByte, i + 2);
      localDictionaryMetadata.setFormatVersion("1.0");
    }
  }
  
  public static SdchDictionary parseFrom(InputStream paramInputStream)
    throws IOException
  {
    DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
    byte[] arrayOfByte1 = new byte[localDataInputStream.readInt()];
    localDataInputStream.readFully(arrayOfByte1);
    DictionaryMetadata localDictionaryMetadata = DictionaryMetadata.parseFrom(arrayOfByte1);
    byte[] arrayOfByte2 = new byte[localDataInputStream.readInt()];
    localDataInputStream.readFully(arrayOfByte2);
    return new SdchDictionary(localDictionaryMetadata, arrayOfByte2, 0);
  }
  
  byte[] getBytes()
  {
    return this.mVcDiffBytes;
  }
  
  int getLength()
  {
    return this.mVcDiffBytes.length - this.mDictionaryOffset;
  }
  
  DictionaryMetadata getMetadata()
  {
    return this.mMetadata;
  }
  
  int getOffset()
  {
    return this.mDictionaryOffset;
  }
  
  public void serializeTo(OutputStream paramOutputStream)
    throws IOException
  {
    DataOutputStream localDataOutputStream = new DataOutputStream(paramOutputStream);
    localDataOutputStream.writeInt(this.mMetadata.getSerializedSize());
    localDataOutputStream.write(this.mMetadata.toByteArray());
    localDataOutputStream.writeInt(getLength());
    localDataOutputStream.write(this.mVcDiffBytes, getOffset(), getLength());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.sdch.SdchDictionary
 * JD-Core Version:    0.7.0.1
 */