package com.google.android.sidekick.main.file;

import android.content.Context;
import android.util.Log;
import com.google.android.sidekick.main.inject.SignedCipherHelper;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.io.Closeables;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileBytesReader
{
  private static final String TAG = Tag.getTag(FileBytesReader.class);
  private final Context mAppContext;
  private final SignedCipherHelper mSignedCipherHelper;
  
  public FileBytesReader(Context paramContext, SignedCipherHelper paramSignedCipherHelper)
  {
    this.mAppContext = paramContext;
    this.mSignedCipherHelper = paramSignedCipherHelper;
  }
  
  public byte[] readEncryptedFileBytes(String paramString, int paramInt)
  {
    byte[] arrayOfByte = readFileBytes(paramString, paramInt);
    if (arrayOfByte != null)
    {
      arrayOfByte = this.mSignedCipherHelper.decryptBytes(arrayOfByte);
      if (arrayOfByte == null) {
        Log.e(TAG, "Failed to read file: " + paramString + " crypto failed");
      }
    }
    return arrayOfByte;
  }
  
  public byte[] readFileBytes(String paramString, int paramInt)
  {
    localFileInputStream = null;
    for (;;)
    {
      try
      {
        File localFile = new File(this.mAppContext.getFilesDir(), paramString);
        boolean bool1 = localFile.isFile();
        if (!bool1)
        {
          Closeables.closeQuietly(null);
          return null;
        }
        long l = localFile.length();
        boolean bool2 = l < paramInt;
        localFileInputStream = null;
        if (!bool2)
        {
          Log.e(TAG, "Data is too large (" + l + " bytes) to read to disk: " + paramString);
          Closeables.closeQuietly(null);
          return null;
        }
        localFileInputStream = this.mAppContext.openFileInput(paramString);
        byte[] arrayOfByte2 = new byte[(int)l];
        i = 0;
        j = (int)l;
        k = localFileInputStream.read(arrayOfByte2, i, j);
        if (k >= 1) {
          continue;
        }
        arrayOfByte1 = arrayOfByte2;
      }
      catch (IOException localIOException)
      {
        int i;
        int j;
        int k;
        Log.e(TAG, "Failed to read file: " + paramString);
        Closeables.closeQuietly(localFileInputStream);
        byte[] arrayOfByte1 = null;
        continue;
      }
      finally
      {
        Closeables.closeQuietly(localFileInputStream);
      }
      return arrayOfByte1;
      j -= k;
      i += k;
      if (j > 0) {}
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.file.FileBytesReader
 * JD-Core Version:    0.7.0.1
 */