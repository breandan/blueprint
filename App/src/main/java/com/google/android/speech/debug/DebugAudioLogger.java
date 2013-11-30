package com.google.android.speech.debug;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.speech.SpeechSettings;
import com.google.android.speech.embedded.Greco3Grammar;
import com.google.android.speech.test.TestPlatformLog;
import com.google.common.io.Files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DebugAudioLogger {
    public static void clearAllLoggedData(Context paramContext) {
        File localFile = paramContext.getDir("debug", 0);
        File[] arrayOfFile = localFile.listFiles();
        int i = arrayOfFile.length;
        for (int j = 0; j < i; j++) {
            arrayOfFile[j].delete();
        }
        localFile.delete();
        new File(paramContext.getExternalCacheDir(), "logs.zip").delete();
    }

    public static void maybeDumpGrammar(String paramString, Greco3Grammar paramGreco3Grammar, Context paramContext, SpeechSettings paramSpeechSettings) {
        File localFile;
        if (shouldLog(paramSpeechSettings)) {
            localFile = paramContext.getDir("debug", 0);
        }
        try {
            Files.write(paramString, new File(localFile, "grammar-" + paramGreco3Grammar.name() + ".txt"), Charset.forName("UTF-8"));
            return;
        } catch (IOException localIOException) {
        }
    }

    public static InputStream maybeWrapInLogStream(InputStream paramInputStream, Context paramContext, SpeechSettings paramSpeechSettings) {
        String str2;
        if (shouldLog(paramSpeechSettings)) {
            String str1 = "mic-" + System.currentTimeMillis() + ".pcm";
            str2 = new File(paramContext.getDir("debug", 0), str1).getAbsolutePath();
            TestPlatformLog.logAudioPath(str2);
        }
        try {
            CopyInputStream localCopyInputStream = new CopyInputStream(paramInputStream, new BufferedOutputStream(new FileOutputStream(str2), 960000));
            paramInputStream = localCopyInputStream;
            return paramInputStream;
        } catch (FileNotFoundException localFileNotFoundException) {
            Log.e("VS.DebugLogger", "Error opening audio log file.", localFileNotFoundException);
        }
        return paramInputStream;
    }

    public static void sendLoggedAudio(Context paramContext) {
        File localFile1 = paramContext.getDir("debug", 0);
        File localFile2 = new File(paramContext.getExternalCacheDir(), "logs.zip");
        if (writeZipFile(localFile2, localFile1.listFiles())) {
            Intent localIntent = new Intent("android.intent.action.SEND");
            localIntent.setType("text/plain");
            localIntent.putExtra("android.intent.extra.SUBJECT", "Debug logged audio + grammar");
            localIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(localFile2));
            paramContext.startActivity(localIntent);
            Toast.makeText(paramContext, "Don't forget to turn off debug logging once your email has been sent.", 4000).show();
            return;
        }
        Toast.makeText(paramContext, "Unable to send logged audio", 4000).show();
    }

    private static boolean shouldLog(SpeechSettings paramSpeechSettings) {
        return paramSpeechSettings.isDebugAudioLoggingEnabled();
    }

    /* Error */
    private static boolean writeZipFile(File paramFile, File[] paramArrayOfFile) {
        // Byte code:
        //   0: aload_1
        //   1: ifnull +8 -> 9
        //   4: aload_1
        //   5: arraylength
        //   6: ifne +5 -> 11
        //   9: iconst_0
        //   10: ireturn
        //   11: sipush 4096
        //   14: newarray byte
        //   16: astore_2
        //   17: aconst_null
        //   18: astore_3
        //   19: aconst_null
        //   20: astore 4
        //   22: new 188	java/util/zip/ZipOutputStream
        //   25: dup
        //   26: new 110	java/io/FileOutputStream
        //   29: dup
        //   30: aload_0
        //   31: invokespecial 191	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
        //   34: invokespecial 194	java/util/zip/ZipOutputStream:<init>	(Ljava/io/OutputStream;)V
        //   37: astore 5
        //   39: aload_1
        //   40: arraylength
        //   41: istore 9
        //   43: iconst_0
        //   44: istore 10
        //   46: aconst_null
        //   47: astore 11
        //   49: iload 10
        //   51: iload 9
        //   53: if_icmpge +100 -> 153
        //   56: aload_1
        //   57: iload 10
        //   59: aaload
        //   60: astore 13
        //   62: new 196	java/io/FileInputStream
        //   65: dup
        //   66: aload 13
        //   68: invokespecial 197	java/io/FileInputStream:<init>	(Ljava/io/File;)V
        //   71: astore 4
        //   73: aload 5
        //   75: new 199	java/util/zip/ZipEntry
        //   78: dup
        //   79: aload 13
        //   81: invokevirtual 202	java/io/File:getName	()Ljava/lang/String;
        //   84: invokespecial 203	java/util/zip/ZipEntry:<init>	(Ljava/lang/String;)V
        //   87: invokevirtual 207	java/util/zip/ZipOutputStream:putNextEntry	(Ljava/util/zip/ZipEntry;)V
        //   90: aload 4
        //   92: aload_2
        //   93: invokevirtual 211	java/io/FileInputStream:read	([B)I
        //   96: istore 14
        //   98: iload 14
        //   100: ifle +33 -> 133
        //   103: aload 5
        //   105: aload_2
        //   106: iconst_0
        //   107: iload 14
        //   109: invokevirtual 214	java/util/zip/ZipOutputStream:write	([BII)V
        //   112: goto -22 -> 90
        //   115: astore 7
        //   117: aload 5
        //   119: astore 8
        //   121: aload 8
        //   123: invokestatic 220	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   126: aload 4
        //   128: invokestatic 220	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   131: iconst_0
        //   132: ireturn
        //   133: aload 5
        //   135: invokevirtual 223	java/util/zip/ZipOutputStream:closeEntry	()V
        //   138: aload 4
        //   140: invokevirtual 226	java/io/FileInputStream:close	()V
        //   143: iinc 10 1
        //   146: aload 4
        //   148: astore 11
        //   150: goto -101 -> 49
        //   153: aload 5
        //   155: invokevirtual 227	java/util/zip/ZipOutputStream:close	()V
        //   158: aload 5
        //   160: invokestatic 220	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   163: aload 11
        //   165: invokestatic 220	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   168: iconst_1
        //   169: ireturn
        //   170: astore 6
        //   172: aload_3
        //   173: invokestatic 220	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   176: aload 4
        //   178: invokestatic 220	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   181: aload 6
        //   183: athrow
        //   184: astore 6
        //   186: aload 5
        //   188: astore_3
        //   189: goto -17 -> 172
        //   192: astore 6
        //   194: aload 11
        //   196: astore 4
        //   198: aload 5
        //   200: astore_3
        //   201: goto -29 -> 172
        //   204: astore 15
        //   206: aconst_null
        //   207: astore 4
        //   209: aconst_null
        //   210: astore 8
        //   212: goto -91 -> 121
        //   215: astore 12
        //   217: aload 11
        //   219: astore 4
        //   221: aload 5
        //   223: astore 8
        //   225: goto -104 -> 121
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	228	0	paramFile	File
        //   0	228	1	paramArrayOfFile	File[]
        //   16	90	2	arrayOfByte	byte[]
        //   18	183	3	localObject1	Object
        //   20	200	4	localObject2	Object
        //   37	185	5	localZipOutputStream1	java.util.zip.ZipOutputStream
        //   170	12	6	localObject3	Object
        //   184	1	6	localObject4	Object
        //   192	1	6	localObject5	Object
        //   115	1	7	localIOException1	IOException
        //   119	105	8	localZipOutputStream2	java.util.zip.ZipOutputStream
        //   41	13	9	i	int
        //   44	100	10	j	int
        //   47	171	11	localObject6	Object
        //   215	1	12	localIOException2	IOException
        //   60	20	13	localFile	File
        //   96	12	14	k	int
        //   204	1	15	localIOException3	IOException
        // Exception table:
        //   from	to	target	type
        //   39	43	115	java/io/IOException
        //   73	90	115	java/io/IOException
        //   90	98	115	java/io/IOException
        //   103	112	115	java/io/IOException
        //   133	143	115	java/io/IOException
        //   22	39	170	finally
        //   39	43	184	finally
        //   73	90	184	finally
        //   90	98	184	finally
        //   103	112	184	finally
        //   133	143	184	finally
        //   56	73	192	finally
        //   153	158	192	finally
        //   22	39	204	java/io/IOException
        //   56	73	215	java/io/IOException
        //   153	158	215	java/io/IOException
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.debug.DebugAudioLogger

 * JD-Core Version:    0.7.0.1

 */