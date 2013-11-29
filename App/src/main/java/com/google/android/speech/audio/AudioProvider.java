package com.google.android.speech.audio;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AudioProvider
  extends ContentProvider
{
  private static int sCurrentFileIndex = 0;
  private UriMatcher mUriMatcher;
  
  private void addEncoding(String paramString, UriMatcher paramUriMatcher, AudioUtils.Encoding... paramVarArgs)
  {
    for (int i = 0; i < 5; i++)
    {
      int j = paramVarArgs.length;
      for (int k = 0; k < j; k++)
      {
        AudioUtils.Encoding localEncoding = paramVarArgs[k];
        paramUriMatcher.addURI(paramString, "NoteToSelfOriginalAudio" + i + "." + localEncoding.getExt(), localEncoding.getCode());
      }
    }
  }
  
  private static byte[] getAudio(AudioUtils.Encoding paramEncoding, byte[] paramArrayOfByte)
    throws IOException
  {
    return AudioUtils.encode(paramEncoding, paramArrayOfByte);
  }
  
  private File getAudioFile(Uri paramUri)
  {
    return getContext().getFileStreamPath(getAudioFileName(paramUri));
  }
  
  private static String getAudioFileName(Uri paramUri)
  {
    return paramUri.getLastPathSegment();
  }
  
  private static Uri getAudioFileUri(Context paramContext, AudioUtils.Encoding paramEncoding)
  {
    sCurrentFileIndex = (1 + sCurrentFileIndex) % 5;
    return new Uri.Builder().scheme("content").authority(getAuthority(paramContext)).path("NoteToSelfOriginalAudio" + sCurrentFileIndex + '.' + paramEncoding.getExt()).build();
  }
  
  private static String getAuthority(Context paramContext)
  {
    return paramContext.getPackageName() + ".AudioProvider";
  }
  
  public static Uri insert(Context paramContext, AudioStore.AudioRecording paramAudioRecording)
  {
    if (paramAudioRecording == null) {
      return null;
    }
    AudioUtils.Encoding localEncoding = AudioUtils.getAmrEncodingForRecording(paramAudioRecording);
    try
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("type", localEncoding.getMimeType());
      localContentValues.put("data", getAudio(localEncoding, paramAudioRecording.getAudio()));
      localContentValues.put("ext", localEncoding.getExt());
      Uri localUri = paramContext.getContentResolver().insert(getAudioFileUri(paramContext, localEncoding), localContentValues);
      return localUri;
    }
    catch (IOException localIOException)
    {
      Log.i("AudioProvider", "Unable to add the audio", localIOException);
    }
    return null;
  }
  
  private ParcelFileDescriptor openAudioFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    if (!"r".equals(paramString)) {
      throw new IllegalArgumentException("Bad mode: " + paramString);
    }
    return ParcelFileDescriptor.open(getAudioFile(paramUri), 268435456);
  }
  
  private Cursor queryAudioFile(Uri paramUri)
  {
    MatrixCursor localMatrixCursor = new MatrixCursor(new String[] { "_display_name", "_size" }, 1);
    long l = getAudioFile(paramUri).length();
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = getAudioFileName(paramUri);
    arrayOfObject[1] = Long.valueOf(l);
    localMatrixCursor.addRow(arrayOfObject);
    return localMatrixCursor;
  }
  
  /* Error */
  private boolean saveAudioFile(Uri paramUri, ContentValues paramContentValues)
  {
    // Byte code:
    //   0: aload_2
    //   1: ldc 133
    //   3: invokevirtual 229	android/content/ContentValues:getAsByteArray	(Ljava/lang/String;)[B
    //   6: astore_3
    //   7: aload_0
    //   8: aload_1
    //   9: invokespecial 187	com/google/android/speech/audio/AudioProvider:getAudioFile	(Landroid/net/Uri;)Ljava/io/File;
    //   12: astore 4
    //   14: new 231	java/io/FileOutputStream
    //   17: dup
    //   18: aload 4
    //   20: invokespecial 234	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   23: astore 5
    //   25: aload 5
    //   27: aload_3
    //   28: invokevirtual 238	java/io/FileOutputStream:write	([B)V
    //   31: aload 5
    //   33: invokevirtual 241	java/io/FileOutputStream:close	()V
    //   36: iconst_1
    //   37: ireturn
    //   38: astore 6
    //   40: aload 5
    //   42: invokevirtual 241	java/io/FileOutputStream:close	()V
    //   45: aload 6
    //   47: athrow
    //   48: astore 9
    //   50: ldc 158
    //   52: new 19	java/lang/StringBuilder
    //   55: dup
    //   56: invokespecial 20	java/lang/StringBuilder:<init>	()V
    //   59: ldc 243
    //   61: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: aload 4
    //   66: invokevirtual 246	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   69: invokevirtual 40	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   72: aload 9
    //   74: invokestatic 249	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   77: pop
    //   78: iconst_0
    //   79: ireturn
    //   80: astore 7
    //   82: ldc 158
    //   84: new 19	java/lang/StringBuilder
    //   87: dup
    //   88: invokespecial 20	java/lang/StringBuilder:<init>	()V
    //   91: ldc 251
    //   93: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: aload 4
    //   98: invokevirtual 246	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   101: invokevirtual 40	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   104: aload 7
    //   106: invokestatic 249	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   109: pop
    //   110: goto -32 -> 78
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	113	0	this	AudioProvider
    //   0	113	1	paramUri	Uri
    //   0	113	2	paramContentValues	ContentValues
    //   6	22	3	arrayOfByte	byte[]
    //   12	85	4	localFile	File
    //   23	18	5	localFileOutputStream	java.io.FileOutputStream
    //   38	8	6	localObject	Object
    //   80	25	7	localIOException	IOException
    //   48	25	9	localFileNotFoundException	FileNotFoundException
    // Exception table:
    //   from	to	target	type
    //   25	31	38	finally
    //   14	25	48	java/io/FileNotFoundException
    //   31	36	48	java/io/FileNotFoundException
    //   40	48	48	java/io/FileNotFoundException
    //   14	25	80	java/io/IOException
    //   31	36	80	java/io/IOException
    //   40	48	80	java/io/IOException
  }
  
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException();
  }
  
  public String getType(Uri paramUri)
  {
    return AudioUtils.Encoding.fromCode(this.mUriMatcher.match(paramUri)).getMimeType();
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    if (this.mUriMatcher.match(paramUri) > 0)
    {
      if (saveAudioFile(paramUri, paramContentValues)) {
        return paramUri;
      }
      return null;
    }
    throw new IllegalArgumentException("Unknown URI: " + paramUri);
  }
  
  public boolean onCreate()
  {
    String str = getAuthority(getContext());
    this.mUriMatcher = new UriMatcher(-1);
    UriMatcher localUriMatcher = this.mUriMatcher;
    AudioUtils.Encoding[] arrayOfEncoding = new AudioUtils.Encoding[3];
    arrayOfEncoding[0] = AudioUtils.Encoding.WAV;
    arrayOfEncoding[1] = AudioUtils.Encoding.AMR;
    arrayOfEncoding[2] = AudioUtils.Encoding.AMRWB;
    addEncoding(str, localUriMatcher, arrayOfEncoding);
    return true;
  }
  
  public ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    if (this.mUriMatcher.match(paramUri) > 0) {
      return openAudioFile(paramUri, paramString);
    }
    throw new IllegalArgumentException("Unknown URI: " + paramUri);
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    if (this.mUriMatcher.match(paramUri) > 0) {
      return queryAudioFile(paramUri);
    }
    throw new IllegalArgumentException("Unknown URI: " + paramUri);
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.audio.AudioProvider
 * JD-Core Version:    0.7.0.1
 */