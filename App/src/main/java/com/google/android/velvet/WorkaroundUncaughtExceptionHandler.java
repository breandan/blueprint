package com.google.android.velvet;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class WorkaroundUncaughtExceptionHandler
        implements Thread.UncaughtExceptionHandler {
    private static Thread.UncaughtExceptionHandler sNewExceptionHandler = null;
    private static Thread.UncaughtExceptionHandler sOldExceptionHandler = null;
    private final Context mApp;

    private WorkaroundUncaughtExceptionHandler(VelvetApplication paramVelvetApplication) {
        this.mApp = paramVelvetApplication;
    }

    private void deleteFile(File paramFile, int paramInt1, int paramInt2) {
        if (paramInt1 != paramInt2) {
        }
        for (String str = " [bug " + paramInt1 + "]"; paramFile.delete(); str = "") {
            Log.i("Velvet.WorkaroundUncaughtExceptionHandler", "Deleted offending file: " + paramFile.getName() + str);
            return;
        }
        Log.e("Velvet.WorkaroundUncaughtExceptionHandler", "Failed to delete file: " + paramFile.getName() + str);
    }

    private void handleSharedPreferencesParseBug(int paramInt) {
        Log.e("Velvet.WorkaroundUncaughtExceptionHandler", "Intercepted bug " + paramInt + ", purging platform shared preferences.");
        File localFile1 = new File(this.mApp.getApplicationContext().getApplicationInfo().dataDir, "shared_prefs");
        String[] arrayOfString = localFile1.list();
        if ((arrayOfString == null) || (arrayOfString.length == 0)) {
            Log.e("Velvet.WorkaroundUncaughtExceptionHandler", "No files in the shared_prefs folder.");
            return;
        }
        int i = arrayOfString.length;
        int j = 0;
        label87:
        String str;
        if (j < i) {
            str = arrayOfString[j];
            if (str.endsWith(".xml")) {
                break label143;
            }
            Log.w("Velvet.WorkaroundUncaughtExceptionHandler", "Non-XML file: " + str);
        }
        for (; ; ) {
            j++;
            break label87;
            break;
            label143:
            File localFile2 = new File(localFile1, str);
            try {
                readMapXmlFromFile(localFile2);
                Log.i("Velvet.WorkaroundUncaughtExceptionHandler", "Keeping good file: " + str);
            } catch (NumberFormatException localNumberFormatException) {
                deleteFile(localFile2, 8584433, paramInt);
            } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
                deleteFile(localFile2, 9012715, paramInt);
            } catch (Throwable localThrowable) {
                Log.e("Velvet.WorkaroundUncaughtExceptionHandler", "Caught unexpected exception trying to read file: " + str, localThrowable);
            }
        }
    }

    public static void install(VelvetApplication paramVelvetApplication) {
        sNewExceptionHandler = new WorkaroundUncaughtExceptionHandler(paramVelvetApplication);
        sOldExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(sNewExceptionHandler);
    }

    /* Error */
    private void readMapXmlFromFile(File paramFile)
            throws Throwable {
        // Byte code:
        //   0: aload_0
        //   1: invokevirtual 158	java/lang/Object:getClass	()Ljava/lang/Class;
        //   4: invokevirtual 164	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
        //   7: ldc 166
        //   9: invokevirtual 172	java/lang/ClassLoader:loadClass	(Ljava/lang/String;)Ljava/lang/Class;
        //   12: astore_2
        //   13: aload_2
        //   14: ldc 174
        //   16: iconst_1
        //   17: anewarray 160	java/lang/Class
        //   20: dup
        //   21: iconst_0
        //   22: ldc 176
        //   24: aastore
        //   25: invokevirtual 180	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //   28: astore_3
        //   29: new 182	java/io/FileInputStream
        //   32: dup
        //   33: aload_1
        //   34: invokespecial 184	java/io/FileInputStream:<init>	(Ljava/io/File;)V
        //   37: astore 4
        //   39: new 186	java/io/BufferedInputStream
        //   42: dup
        //   43: aload 4
        //   45: invokespecial 189	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
        //   48: astore 5
        //   50: aload_3
        //   51: aload_2
        //   52: iconst_1
        //   53: anewarray 4	java/lang/Object
        //   56: dup
        //   57: iconst_0
        //   58: aload 5
        //   60: aastore
        //   61: invokevirtual 195	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   64: pop
        //   65: aload 5
        //   67: invokestatic 201	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   70: return
        //   71: astore 6
        //   73: aload 6
        //   75: invokevirtual 205	java/lang/reflect/InvocationTargetException:getCause	()Ljava/lang/Throwable;
        //   78: astore 8
        //   80: aload 8
        //   82: ifnull +16 -> 98
        //   85: aload 8
        //   87: athrow
        //   88: astore 7
        //   90: aload 4
        //   92: invokestatic 201	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   95: aload 7
        //   97: athrow
        //   98: aload 6
        //   100: astore 8
        //   102: goto -17 -> 85
        //   105: astore 7
        //   107: aload 5
        //   109: astore 4
        //   111: goto -21 -> 90
        //   114: astore 6
        //   116: aload 5
        //   118: astore 4
        //   120: goto -47 -> 73
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	123	0	this	WorkaroundUncaughtExceptionHandler
        //   0	123	1	paramFile	File
        //   12	40	2	localClass	java.lang.Class
        //   28	23	3	localMethod	java.lang.reflect.Method
        //   37	82	4	localObject1	Object
        //   48	69	5	localBufferedInputStream	java.io.BufferedInputStream
        //   71	28	6	localInvocationTargetException1	java.lang.reflect.InvocationTargetException
        //   114	1	6	localInvocationTargetException2	java.lang.reflect.InvocationTargetException
        //   88	8	7	localObject2	Object
        //   105	1	7	localObject3	Object
        //   78	23	8	localObject4	Object
        // Exception table:
        //   from	to	target	type
        //   39	50	71	java/lang/reflect/InvocationTargetException
        //   39	50	88	finally
        //   73	80	88	finally
        //   85	88	88	finally
        //   50	65	105	finally
        //   50	65	114	java/lang/reflect/InvocationTargetException
    }

    /* Error */
    public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
        // Byte code:
        //   0: aload_2
        //   1: instanceof 73
        //   4: ifeq +32 -> 36
        //   7: ldc 209
        //   9: aload_1
        //   10: invokevirtual 210	java/lang/Thread:getName	()Ljava/lang/String;
        //   13: invokevirtual 214	java/lang/String:equals	(Ljava/lang/Object;)Z
        //   16: ifeq +20 -> 36
        //   19: aload_0
        //   20: ldc 131
        //   22: invokespecial 216	com/google/android/velvet/WorkaroundUncaughtExceptionHandler:handleSharedPreferencesParseBug	(I)V
        //   25: getstatic 15	com/google/android/velvet/WorkaroundUncaughtExceptionHandler:sOldExceptionHandler	Ljava/lang/Thread$UncaughtExceptionHandler;
        //   28: aload_1
        //   29: aload_2
        //   30: invokeinterface 218 3 0
        //   35: return
        //   36: aload_2
        //   37: instanceof 75
        //   40: ifeq -15 -> 25
        //   43: ldc 209
        //   45: aload_1
        //   46: invokevirtual 210	java/lang/Thread:getName	()Ljava/lang/String;
        //   49: invokevirtual 214	java/lang/String:equals	(Ljava/lang/Object;)Z
        //   52: ifeq -27 -> 25
        //   55: aload_0
        //   56: ldc 134
        //   58: invokespecial 216	com/google/android/velvet/WorkaroundUncaughtExceptionHandler:handleSharedPreferencesParseBug	(I)V
        //   61: goto -36 -> 25
        //   64: astore 4
        //   66: getstatic 15	com/google/android/velvet/WorkaroundUncaughtExceptionHandler:sOldExceptionHandler	Ljava/lang/Thread$UncaughtExceptionHandler;
        //   69: aload_1
        //   70: aload_2
        //   71: invokeinterface 218 3 0
        //   76: return
        //   77: astore_3
        //   78: getstatic 15	com/google/android/velvet/WorkaroundUncaughtExceptionHandler:sOldExceptionHandler	Ljava/lang/Thread$UncaughtExceptionHandler;
        //   81: aload_1
        //   82: aload_2
        //   83: invokeinterface 218 3 0
        //   88: aload_3
        //   89: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	90	0	this	WorkaroundUncaughtExceptionHandler
        //   0	90	1	paramThread	Thread
        //   0	90	2	paramThrowable	Throwable
        //   77	12	3	localObject	Object
        //   64	1	4	localThrowable	Throwable
        // Exception table:
        //   from	to	target	type
        //   0	25	64	java/lang/Throwable
        //   36	61	64	java/lang/Throwable
        //   0	25	77	finally
        //   36	61	77	finally
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.WorkaroundUncaughtExceptionHandler

 * JD-Core Version:    0.7.0.1

 */