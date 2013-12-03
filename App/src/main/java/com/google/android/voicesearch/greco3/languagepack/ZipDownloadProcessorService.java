package com.google.android.voicesearch.greco3.languagepack;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.embedded.LanguagePackUtils;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class ZipDownloadProcessorService
        extends IntentService {
    private static int NOTIFICATION_ID = 336627;
    private Greco3DataManager mDataManager;
    private LanguagePackUpdateController mDownloadHelper;
    private Settings mSettings;

    public ZipDownloadProcessorService() {
        super(ZipDownloadProcessorService.class.getSimpleName());
    }

    private Notification buildNotification(GstaticConfiguration.LanguagePack paramLanguagePack) {
        Notification.Builder localBuilder = new Notification.Builder(this);
        localBuilder.setWhen(System.currentTimeMillis());
        localBuilder.setOnlyAlertOnce(true);
        localBuilder.setAutoCancel(true);
        String str = SpokenLanguageUtils.getDisplayName(this.mSettings.getConfiguration(), paramLanguagePack.getBcp47Locale());
        localBuilder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, InstallActivity.class), 0));
        localBuilder.setSmallIcon(17301624);
        localBuilder.setContentTitle(getString(2131363580, new Object[]{str}));
        localBuilder.setContentText(getString(2131363581));
        return localBuilder.build();
    }

    private void deleteDownloadedFile(String paramString) {
        if (!new File(paramString).delete()) {
            Log.e("VS.DownloadProcessorService", "Unable to delete downloaded file:" + paramString);
        }
    }

    /* Error */
    private boolean handleCompletedDownload(Intent paramIntent) {
        // Byte code:
        //   0: iconst_1
        //   1: istore_2
        //   2: aload_1
        //   3: ldc 147
        //   5: ldc2_w 148
        //   8: invokevirtual 153	android/content/Intent:getLongExtra	(Ljava/lang/String;J)J
        //   11: lstore_3
        //   12: lload_3
        //   13: lconst_0
        //   14: lcmp
        //   15: ifge +30 -> 45
        //   18: ldc 124
        //   20: new 126	java/lang/StringBuilder
        //   23: dup
        //   24: invokespecial 128	java/lang/StringBuilder:<init>	()V
        //   27: ldc 155
        //   29: invokevirtual 134	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   32: lload_3
        //   33: invokevirtual 158	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
        //   36: invokevirtual 137	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   39: invokestatic 143	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
        //   42: pop
        //   43: iconst_0
        //   44: istore_2
        //   45: aload_0
        //   46: getfield 160	com/google/android/voicesearch/greco3/languagepack/ZipDownloadProcessorService:mDownloadHelper	Lcom/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController;
        //   49: lload_3
        //   50: invokevirtual 166	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController:getDownloadInfo	(J)Lcom/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo;
        //   53: astore 5
        //   55: aload 5
        //   57: ifnonnull +5 -> 62
        //   60: iconst_0
        //   61: istore_2
        //   62: iload_2
        //   63: ifeq +71 -> 134
        //   66: bipush 8
        //   68: aload 5
        //   70: getfield 171	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:status	I
        //   73: iand
        //   74: ifeq +28 -> 102
        //   77: aload_0
        //   78: aload 5
        //   80: getfield 175	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:fileName	Ljava/lang/String;
        //   83: aload 5
        //   85: getfield 179	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:languagePack	Lcom/google/wireless/voicesearch/proto/GstaticConfiguration$LanguagePack;
        //   88: invokespecial 183	com/google/android/voicesearch/greco3/languagepack/ZipDownloadProcessorService:processDownloadedFile	(Ljava/lang/String;Lcom/google/wireless/voicesearch/proto/GstaticConfiguration$LanguagePack;)Z
        //   91: ifeq +63 -> 154
        //   94: aload_0
        //   95: getfield 185	com/google/android/voicesearch/greco3/languagepack/ZipDownloadProcessorService:mDataManager	Lcom/google/android/speech/embedded/Greco3DataManager;
        //   98: iconst_1
        //   99: invokevirtual 191	com/google/android/speech/embedded/Greco3DataManager:blockingUpdateResources	(Z)V
        //   102: aload_0
        //   103: getfield 160	com/google/android/voicesearch/greco3/languagepack/ZipDownloadProcessorService:mDownloadHelper	Lcom/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController;
        //   106: aload 5
        //   108: getfield 179	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:languagePack	Lcom/google/wireless/voicesearch/proto/GstaticConfiguration$LanguagePack;
        //   111: invokevirtual 194	com/google/wireless/voicesearch/proto/GstaticConfiguration$LanguagePack:getLanguagePackId	()Ljava/lang/String;
        //   114: invokevirtual 197	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController:removeActiveDownload	(Ljava/lang/String;)V
        //   117: aload 5
        //   119: getfield 175	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:fileName	Ljava/lang/String;
        //   122: ifnull +12 -> 134
        //   125: aload_0
        //   126: aload 5
        //   128: getfield 175	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:fileName	Ljava/lang/String;
        //   131: invokespecial 199	com/google/android/voicesearch/greco3/languagepack/ZipDownloadProcessorService:deleteDownloadedFile	(Ljava/lang/String;)V
        //   134: aload 5
        //   136: ifnull +16 -> 152
        //   139: iload_2
        //   140: ifne +12 -> 152
        //   143: aload_0
        //   144: aload 5
        //   146: getfield 179	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:languagePack	Lcom/google/wireless/voicesearch/proto/GstaticConfiguration$LanguagePack;
        //   149: invokespecial 203	com/google/android/voicesearch/greco3/languagepack/ZipDownloadProcessorService:showDownloadFailedNotification	(Lcom/google/wireless/voicesearch/proto/GstaticConfiguration$LanguagePack;)V
        //   152: iload_2
        //   153: ireturn
        //   154: ldc 124
        //   156: new 126	java/lang/StringBuilder
        //   159: dup
        //   160: invokespecial 128	java/lang/StringBuilder:<init>	()V
        //   163: ldc 205
        //   165: invokevirtual 134	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   168: aload 5
        //   170: getfield 175	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:fileName	Ljava/lang/String;
        //   173: invokevirtual 134	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   176: invokevirtual 137	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   179: invokestatic 143	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
        //   182: pop
        //   183: goto -81 -> 102
        //   186: astore 6
        //   188: aload_0
        //   189: getfield 160	com/google/android/voicesearch/greco3/languagepack/ZipDownloadProcessorService:mDownloadHelper	Lcom/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController;
        //   192: aload 5
        //   194: getfield 179	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:languagePack	Lcom/google/wireless/voicesearch/proto/GstaticConfiguration$LanguagePack;
        //   197: invokevirtual 194	com/google/wireless/voicesearch/proto/GstaticConfiguration$LanguagePack:getLanguagePackId	()Ljava/lang/String;
        //   200: invokevirtual 197	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController:removeActiveDownload	(Ljava/lang/String;)V
        //   203: aload 5
        //   205: getfield 175	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:fileName	Ljava/lang/String;
        //   208: ifnull +12 -> 220
        //   211: aload_0
        //   212: aload 5
        //   214: getfield 175	com/google/android/voicesearch/greco3/languagepack/LanguagePackUpdateController$DownloadInfo:fileName	Ljava/lang/String;
        //   217: invokespecial 199	com/google/android/voicesearch/greco3/languagepack/ZipDownloadProcessorService:deleteDownloadedFile	(Ljava/lang/String;)V
        //   220: aload 6
        //   222: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	223	0	this	ZipDownloadProcessorService
        //   0	223	1	paramIntent	Intent
        //   1	152	2	bool	boolean
        //   11	39	3	l	long
        //   53	160	5	localDownloadInfo	LanguagePackUpdateController.DownloadInfo
        //   186	35	6	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   66	102	186	finally
        //   154	183	186	finally
    }

    private boolean handleFileAtFixedLocationForTesting(Intent paramIntent) {
        String str = paramIntent.getStringExtra("language_pack_id");
        if (processDownloadedFile(paramIntent.getStringExtra("location_abs"), LanguagePackUtils.findById(str, this.mSettings.getConfiguration().getEmbeddedRecognitionResourcesList()))) {
            this.mDataManager.blockingUpdateResources(true);
            this.mDownloadHelper.removeActiveDownload(str);
            return true;
        }
        return false;
    }

    private boolean processDownloadedFile(String paramString, GstaticConfiguration.LanguagePack paramLanguagePack) {
        File localFile = LanguagePackDownloadUtils.getDestinationDir(this, paramLanguagePack.getBcp47Locale());
        LanguagePackDownloadUtils.deleteExistingFilesOrCreateDirectory(localFile);
        int i = 0;
        try {
            i = processZipArchive(paramString, localFile);
            if (i != 1) {
                break label67;
            }
            writeMetadata(paramLanguagePack, localFile);
        } catch (IOException localIOException) {
            do {
                Log.e("VS.DownloadProcessorService", "Error processing download: " + localIOException);
            } while (i != 0);
        }
        return true;
        label67:
        return false;
    }

    /* Error */
    private int processZipArchive(String paramString, File paramFile)
            throws IOException {
        // Byte code:
        //   0: iconst_0
        //   1: istore_3
        //   2: new 253	java/util/zip/ZipFile
        //   5: dup
        //   6: new 117	java/io/File
        //   9: dup
        //   10: aload_1
        //   11: invokespecial 118	java/io/File:<init>	(Ljava/lang/String;)V
        //   14: iconst_1
        //   15: invokespecial 256	java/util/zip/ZipFile:<init>	(Ljava/io/File;I)V
        //   18: astore 4
        //   20: aload 4
        //   22: invokevirtual 260	java/util/zip/ZipFile:entries	()Ljava/util/Enumeration;
        //   25: astore 7
        //   27: aload 7
        //   29: invokeinterface 265 1 0
        //   34: ifeq +192 -> 226
        //   37: aload 7
        //   39: invokeinterface 269 1 0
        //   44: checkcast 271	java/util/zip/ZipEntry
        //   47: astore 9
        //   49: new 117	java/io/File
        //   52: dup
        //   53: aload 9
        //   55: invokevirtual 274	java/util/zip/ZipEntry:getName	()Ljava/lang/String;
        //   58: invokespecial 118	java/io/File:<init>	(Ljava/lang/String;)V
        //   61: invokevirtual 275	java/io/File:getName	()Ljava/lang/String;
        //   64: astore 10
        //   66: ldc_w 277
        //   69: aload 10
        //   71: invokevirtual 283	java/lang/String:equals	(Ljava/lang/Object;)Z
        //   74: ifeq +5 -> 79
        //   77: iconst_1
        //   78: istore_3
        //   79: new 117	java/io/File
        //   82: dup
        //   83: aload_2
        //   84: aload 10
        //   86: invokespecial 286	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
        //   89: astore 11
        //   91: aconst_null
        //   92: astore 12
        //   94: aconst_null
        //   95: astore 13
        //   97: aload 4
        //   99: aload 9
        //   101: invokevirtual 290	java/util/zip/ZipFile:getInputStream	(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;
        //   104: astore 12
        //   106: new 292	java/io/FileOutputStream
        //   109: dup
        //   110: aload 11
        //   112: invokespecial 295	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
        //   115: astore 17
        //   117: aload 12
        //   119: aload 17
        //   121: invokestatic 301	com/google/common/io/ByteStreams:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;)J
        //   124: pop2
        //   125: aload 12
        //   127: invokestatic 307	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   130: aload 17
        //   132: invokestatic 307	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   135: goto -108 -> 27
        //   138: astore 5
        //   140: aload 4
        //   142: astore 6
        //   144: aload 6
        //   146: ifnull +8 -> 154
        //   149: aload 6
        //   151: invokevirtual 310	java/util/zip/ZipFile:close	()V
        //   154: aload 5
        //   156: athrow
        //   157: astore 15
        //   159: ldc 124
        //   161: new 126	java/lang/StringBuilder
        //   164: dup
        //   165: invokespecial 128	java/lang/StringBuilder:<init>	()V
        //   168: ldc 248
        //   170: invokevirtual 134	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   173: aload 15
        //   175: invokevirtual 251	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   178: invokevirtual 137	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   181: invokestatic 143	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
        //   184: pop
        //   185: aload 12
        //   187: invokestatic 307	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   190: aload 13
        //   192: invokestatic 307	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   195: iconst_0
        //   196: istore 8
        //   198: aload 4
        //   200: ifnull +8 -> 208
        //   203: aload 4
        //   205: invokevirtual 310	java/util/zip/ZipFile:close	()V
        //   208: iload 8
        //   210: ireturn
        //   211: astore 14
        //   213: aload 12
        //   215: invokestatic 307	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   218: aload 13
        //   220: invokestatic 307	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   223: aload 14
        //   225: athrow
        //   226: iload_3
        //   227: ifeq +19 -> 246
        //   230: iconst_3
        //   231: istore 8
        //   233: aload 4
        //   235: ifnull -27 -> 208
        //   238: aload 4
        //   240: invokevirtual 310	java/util/zip/ZipFile:close	()V
        //   243: iload 8
        //   245: ireturn
        //   246: iconst_1
        //   247: istore 8
        //   249: goto -16 -> 233
        //   252: astore 5
        //   254: aconst_null
        //   255: astore 6
        //   257: goto -113 -> 144
        //   260: astore 14
        //   262: aload 17
        //   264: astore 13
        //   266: goto -53 -> 213
        //   269: astore 15
        //   271: aload 17
        //   273: astore 13
        //   275: goto -116 -> 159
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	278	0	this	ZipDownloadProcessorService
        //   0	278	1	paramString	String
        //   0	278	2	paramFile	File
        //   1	226	3	i	int
        //   18	221	4	localZipFile1	java.util.zip.ZipFile
        //   138	17	5	localObject1	Object
        //   252	1	5	localObject2	Object
        //   142	114	6	localZipFile2	java.util.zip.ZipFile
        //   25	13	7	localEnumeration	java.util.Enumeration
        //   196	52	8	j	int
        //   47	53	9	localZipEntry	java.util.zip.ZipEntry
        //   64	21	10	str	String
        //   89	22	11	localFile	File
        //   92	122	12	localInputStream	java.io.InputStream
        //   95	179	13	localObject3	Object
        //   211	13	14	localObject4	Object
        //   260	1	14	localObject5	Object
        //   157	17	15	localIOException1	IOException
        //   269	1	15	localIOException2	IOException
        //   115	157	17	localFileOutputStream	java.io.FileOutputStream
        // Exception table:
        //   from	to	target	type
        //   20	27	138	finally
        //   27	77	138	finally
        //   79	91	138	finally
        //   125	135	138	finally
        //   185	195	138	finally
        //   213	226	138	finally
        //   97	117	157	java/io/IOException
        //   97	117	211	finally
        //   159	185	211	finally
        //   2	20	252	finally
        //   117	125	260	finally
        //   117	125	269	java/io/IOException
    }

    private void showDownloadFailedNotification(GstaticConfiguration.LanguagePack paramLanguagePack) {
        NotificationManager localNotificationManager = (NotificationManager) getSystemService("notification");
        Notification localNotification = buildNotification(paramLanguagePack);
        localNotificationManager.notify(NOTIFICATION_ID, localNotification);
    }

    public static void start(Context paramContext, Intent paramIntent) {
        Intent localIntent = new Intent(paramContext, ZipDownloadProcessorService.class);
        localIntent.fillIn(paramIntent, 2);
        paramContext.startService(localIntent);
    }

    static void startForDebuggingWithFixedFile(Context paramContext, String paramString, File paramFile) {
        Log.i("VS.DownloadProcessorService", "Rejecting start() call for debug intent.");
    }

    private void writeMetadata(GstaticConfiguration.LanguagePack paramLanguagePack, File paramFile)
            throws IOException {
        File localFile = new File(paramFile, "metadata");
        Log.i("VS.DownloadProcessorService", "Writing to: " + localFile.getAbsolutePath());
        Files.write(paramLanguagePack.toByteArray(), localFile);
    }

    public void onCreate() {
        super.onCreate();
        VoiceSearchServices localVoiceSearchServices = VelvetServices.get().getVoiceSearchServices();
        this.mDownloadHelper = localVoiceSearchServices.getLanguageUpdateController();
        this.mDataManager = localVoiceSearchServices.getGreco3Container().getGreco3DataManager();
        this.mSettings = localVoiceSearchServices.getSettings();
    }

    protected void onHandleIntent(Intent paramIntent) {
        if (paramIntent.getStringExtra("language_pack_id") != null) {
            handleFileAtFixedLocationForTesting(paramIntent);
            return;
        }
        handleCompletedDownload(paramIntent);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.ZipDownloadProcessorService

 * JD-Core Version:    0.7.0.1

 */