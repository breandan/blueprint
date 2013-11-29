package com.google.android.sidekick.main.file;

import android.content.Context;
import android.util.Log;
import com.google.android.sidekick.main.inject.SignedCipherHelper;
import com.google.android.sidekick.shared.util.Tag;
import java.io.File;

public class FileBytesWriter
{
  private static final String TAG = Tag.getTag(FileBytesWriter.class);
  private final Context mAppContext;
  private final SignedCipherHelper mSignedCipherHelper;
  
  public FileBytesWriter(Context paramContext, SignedCipherHelper paramSignedCipherHelper)
  {
    this.mAppContext = paramContext;
    this.mSignedCipherHelper = paramSignedCipherHelper;
  }
  
  private String getNewTempFilename(String paramString)
  {
    return paramString + ".new";
  }
  
  public void deleteFile(String paramString)
  {
    new File(this.mAppContext.getFilesDir(), paramString).delete();
  }
  
  public boolean writeEncryptedFileBytes(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = this.mSignedCipherHelper.encryptBytes(paramArrayOfByte);
    if (arrayOfByte == null)
    {
      Log.e(TAG, "Failed to write file: " + paramString + " crypto failed");
      return false;
    }
    if (arrayOfByte.length > paramInt)
    {
      Log.e(TAG, "Data is too large (" + arrayOfByte.length + " bytes) to write to disk: " + paramString);
      return false;
    }
    return writeFileBytes(paramString, arrayOfByte);
  }
  
  /* Error */
  public boolean writeFileBytes(String paramString, byte[] paramArrayOfByte)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial 93	com/google/android/sidekick/main/file/FileBytesWriter:getNewTempFilename	(Ljava/lang/String;)Ljava/lang/String;
    //   7: astore 4
    //   9: aload_0
    //   10: getfield 26	com/google/android/sidekick/main/file/FileBytesWriter:mAppContext	Landroid/content/Context;
    //   13: aload 4
    //   15: iconst_0
    //   16: invokevirtual 97	android/content/Context:openFileOutput	(Ljava/lang/String;I)Ljava/io/FileOutputStream;
    //   19: astore 8
    //   21: new 99	java/io/BufferedOutputStream
    //   24: dup
    //   25: aload 8
    //   27: invokespecial 102	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   30: astore 9
    //   32: aload 9
    //   34: aload_2
    //   35: invokevirtual 106	java/io/BufferedOutputStream:write	([B)V
    //   38: aload 9
    //   40: invokevirtual 109	java/io/BufferedOutputStream:flush	()V
    //   43: aload 8
    //   45: invokevirtual 115	java/io/FileOutputStream:getFD	()Ljava/io/FileDescriptor;
    //   48: invokevirtual 120	java/io/FileDescriptor:sync	()V
    //   51: aload 9
    //   53: invokestatic 126	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   56: new 47	java/io/File
    //   59: dup
    //   60: aload_0
    //   61: getfield 26	com/google/android/sidekick/main/file/FileBytesWriter:mAppContext	Landroid/content/Context;
    //   64: invokevirtual 53	android/content/Context:getFilesDir	()Ljava/io/File;
    //   67: aload_1
    //   68: invokespecial 56	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   71: astore 11
    //   73: new 47	java/io/File
    //   76: dup
    //   77: aload_0
    //   78: getfield 26	com/google/android/sidekick/main/file/FileBytesWriter:mAppContext	Landroid/content/Context;
    //   81: invokevirtual 53	android/content/Context:getFilesDir	()Ljava/io/File;
    //   84: aload 4
    //   86: invokespecial 56	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   89: aload 11
    //   91: invokevirtual 130	java/io/File:renameTo	(Ljava/io/File;)Z
    //   94: ifne +80 -> 174
    //   97: getstatic 20	com/google/android/sidekick/main/file/FileBytesWriter:TAG	Ljava/lang/String;
    //   100: new 32	java/lang/StringBuilder
    //   103: dup
    //   104: invokespecial 33	java/lang/StringBuilder:<init>	()V
    //   107: ldc 132
    //   109: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: aload_1
    //   113: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: ldc 134
    //   118: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: invokevirtual 43	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   124: invokestatic 78	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   127: pop
    //   128: iconst_0
    //   129: ireturn
    //   130: astore 6
    //   132: getstatic 20	com/google/android/sidekick/main/file/FileBytesWriter:TAG	Ljava/lang/String;
    //   135: new 32	java/lang/StringBuilder
    //   138: dup
    //   139: invokespecial 33	java/lang/StringBuilder:<init>	()V
    //   142: ldc 136
    //   144: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: aload 4
    //   149: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: invokevirtual 43	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   155: invokestatic 78	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   158: pop
    //   159: aload_3
    //   160: invokestatic 126	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   163: iconst_0
    //   164: ireturn
    //   165: astore 5
    //   167: aload_3
    //   168: invokestatic 126	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   171: aload 5
    //   173: athrow
    //   174: iconst_1
    //   175: ireturn
    //   176: astore 5
    //   178: aload 9
    //   180: astore_3
    //   181: goto -14 -> 167
    //   184: astore 10
    //   186: aload 9
    //   188: astore_3
    //   189: goto -57 -> 132
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	192	0	this	FileBytesWriter
    //   0	192	1	paramString	String
    //   0	192	2	paramArrayOfByte	byte[]
    //   1	188	3	localObject1	Object
    //   7	141	4	str	String
    //   165	7	5	localObject2	Object
    //   176	1	5	localObject3	Object
    //   130	1	6	localIOException1	java.io.IOException
    //   19	25	8	localFileOutputStream	java.io.FileOutputStream
    //   30	157	9	localBufferedOutputStream	java.io.BufferedOutputStream
    //   184	1	10	localIOException2	java.io.IOException
    //   71	19	11	localFile	File
    // Exception table:
    //   from	to	target	type
    //   9	32	130	java/io/IOException
    //   9	32	165	finally
    //   132	159	165	finally
    //   32	51	176	finally
    //   32	51	184	java/io/IOException
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.file.FileBytesWriter
 * JD-Core Version:    0.7.0.1
 */