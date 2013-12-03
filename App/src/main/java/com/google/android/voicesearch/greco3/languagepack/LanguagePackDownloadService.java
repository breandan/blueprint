package com.google.android.voicesearch.greco3.languagepack;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.android.shared.util.Util;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.network.IoUtils;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nullable;

public class LanguagePackDownloadService
        extends IntentService {
    static final String ACTION_DOWNLOAD_MANIFEST = "com.google.android.speech.embedded.download_manifest";
    static final int SERVICE_ID = 325633;
    @Nullable
    private DownloadJob mCurrentDownloadJob = null;
    private Greco3DataManager mDataManager;
    private DownloadManager mDownloadManager;
    private BroadcastReceiver mDownloadReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
            if ("android.intent.action.DOWNLOAD_COMPLETE".equals(paramAnonymousIntent.getAction())) {
                LanguagePackDownloadService.this.handleComplete(paramAnonymousIntent.getLongExtra("extra_download_id", -9223372036854775808L));
            }
        }
    };
    protected AtomicBoolean mForeground = new AtomicBoolean(false);
    private List<DownloadJob> mJobQueue = Lists.newArrayList();
    protected final List<LanguagePackFile> mLanguagePackFiles = Lists.newArrayList();
    private Notification.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private final ExecutorService mPollingExecutor = Executors.newSingleThreadExecutor();

    public LanguagePackDownloadService() {
        super("LanguagePackDownloadService");
    }

    private DownloadManager.Request buildLanguagePackFileRequest(LanguagePackFile paramLanguagePackFile) {
        DownloadManager.Request localRequest = new DownloadManager.Request(paramLanguagePackFile.downloadUri);
        localRequest.setVisibleInDownloadsUi(false);
        localRequest.setNotificationVisibility(2);
        localRequest.setAllowedOverMetered(this.mCurrentDownloadJob.mDownloadRequest.isAllowedOverMetered());
        try {
            localRequest.setDestinationInExternalFilesDir(getApplicationContext(), this.mCurrentDownloadJob.mDownloadRequest.getCacheDir(), paramLanguagePackFile.downloadName);
            return localRequest;
        } catch (NullPointerException localNullPointerException) {
            Log.w("LanguagePackDownloadService", "Error from #setDestinationInExternalFilesDir :" + localNullPointerException.getMessage());
            return localRequest;
        } catch (IllegalStateException localIllegalStateException) {
            Log.w("LanguagePackDownloadService", "Error from #setDestinationInExternalFilesDir :" + localIllegalStateException.getMessage());
        }
        return localRequest;
    }

    private void downloadFiles() {
        Iterator localIterator = this.mLanguagePackFiles.iterator();
        while (localIterator.hasNext()) {
            LanguagePackFile localLanguagePackFile = (LanguagePackFile) localIterator.next();
            if ((localLanguagePackFile.downloadId == -9223372036854775808L) && (!inExistingManifest(localLanguagePackFile))) {
                localLanguagePackFile.downloadId = this.mDownloadManager.enqueue(buildLanguagePackFileRequest(localLanguagePackFile));
            }
        }
    }

    private Uri getDownloadUri(String paramString) {
        return Util.buildUriFromParent(this.mCurrentDownloadJob.mDownloadRequest.getUri()).appendEncodedPath(paramString).build();
    }

    private int getKbSoFar() {
        int i = 0;
        DownloadManager.Query localQuery = new DownloadManager.Query();
        localQuery.setFilterById(getLanguagePackFilesIds());
        Cursor localCursor = this.mDownloadManager.query(localQuery);
        localCursor.moveToPosition(-1);
        while (localCursor.moveToNext()) {
            i += localCursor.getInt(localCursor.getColumnIndex("bytes_so_far"));
        }
        localCursor.close();
        return i / 1024;
    }

    private long[] getLanguagePackFilesIds() {
        long[] arrayOfLong = new long[this.mLanguagePackFiles.size()];
        for (int i = 0; i < this.mLanguagePackFiles.size(); i++) {
            arrayOfLong[i] = ((LanguagePackFile) this.mLanguagePackFiles.get(i)).downloadId;
        }
        return arrayOfLong;
    }

    private void handleComplete(long paramLong) {
        if (paramLong == this.mCurrentDownloadJob.mManifestDownloadId) {
            processDownloadedManifest(paramLong);
            return;
        }
        Iterator localIterator = this.mLanguagePackFiles.iterator();
        LanguagePackFile localLanguagePackFile;
        do {
            boolean bool1 = localIterator.hasNext();
            i = 0;
            bool2 = false;
            if (!bool1) {
                break;
            }
            localLanguagePackFile = (LanguagePackFile) localIterator.next();
        } while (paramLong != localLanguagePackFile.downloadId);
        int i = 1;
        boolean bool2 = processDownloadedFile(localLanguagePackFile);
        if (i != 0) {
            if (bool2) {
                maybeProcessAllFilesDownloaded();
                return;
            }
            cleanUpCurrentRequest();
            maybeStartNext();
            return;
        }
        Log.w("LanguagePackDownloadService", "Received download complete broadcast for unknown ID.");
    }

    private boolean inExistingManifest(LanguagePackFile paramLanguagePackFile) {
        return false;
    }

    private void maybeProcessAllFilesDownloaded() {
        Iterator localIterator1 = this.mLanguagePackFiles.iterator();
        while (localIterator1.hasNext()) {
            if (!((LanguagePackFile) localIterator1.next()).downloadComplete) {
                return;
            }
        }
        File localFile1 = LanguagePackDownloadUtils.getStagingDir(this, this.mCurrentDownloadJob.mBcp47Locale);
        boolean bool = new File(this.mDownloadManager.getUriForDownloadedFile(this.mCurrentDownloadJob.mManifestDownloadId).toString()).renameTo(new File(localFile1, this.mCurrentDownloadJob.mDownloadRequest.getUri().getLastPathSegment()));
        int i = 0;
        if (!bool) {
            notifyFailed("Couldn't move the manifest to the staging directory.");
            i = 1;
        }
        if (i == 0) {
            Iterator localIterator2 = this.mLanguagePackFiles.iterator();
            while (localIterator2.hasNext()) {
                LanguagePackFile localLanguagePackFile = (LanguagePackFile) localIterator2.next();
                if (!localLanguagePackFile.getUncompressedFile(this.mDownloadManager).renameTo(new File(localFile1, localLanguagePackFile.fileName))) {
                    Object[] arrayOfObject = new Object[1];
                    arrayOfObject[0] = localLanguagePackFile.fileName;
                    notifyFailed(String.format("Couldn't move file %s to the staging directory.", arrayOfObject));
                    i = 1;
                }
            }
        }
        File localFile2 = LanguagePackDownloadUtils.getDestinationDir(this, this.mCurrentDownloadJob.mBcp47Locale);
        if ((i == 0) && (localFile2.exists()) && ((!LanguagePackDownloadUtils.deleteExistingFilesOrCreateDirectory(localFile2)) || (!localFile2.delete()))) {
            notifyFailed("There was a problem deleting the existing files or directory.");
            i = 1;
        }
        if (i == 0) {
            if (localFile1.renameTo(localFile2)) {
                break label291;
            }
            notifyFailed("The staging directory couldn't be moved to the destination directory.");
        }
        for (; ; ) {
            cleanUpCurrentRequest();
            maybeStartNext();
            return;
            label291:
            this.mDataManager.blockingUpdateResources(true);
            notifyComplete();
        }
    }

    private void maybeStartNext() {
        if (this.mJobQueue.size() > 0) {
            this.mCurrentDownloadJob = ((DownloadJob) this.mJobQueue.remove(0));
            this.mCurrentDownloadJob.mManifestDownloadId = this.mDownloadManager.enqueue(this.mCurrentDownloadJob.mDownloadRequest);
        }
    }

    private void notifyComplete() {
        Intent localIntent = new Intent("com.google.android.speech.embedded.download_manifest_complete");
        localIntent.putExtra("com.google.android.speech.embedded.download_manifest_request", this.mCurrentDownloadJob.mDownloadRequest);
        sendBroadcast(localIntent);
    }

    private void notifyFailed(String paramString) {
        Log.e("LanguagePackDownloadService", paramString);
        Intent localIntent = new Intent("com.google.android.speech.embedded.download_manifest_failed");
        if (this.mCurrentDownloadJob != null) {
            localIntent.putExtra("com.google.android.speech.embedded.download_manifest_request", this.mCurrentDownloadJob.mDownloadRequest);
        }
        sendBroadcast(localIntent);
    }

    private boolean processDownloadedFile(LanguagePackFile paramLanguagePackFile) {
        paramLanguagePackFile.downloadComplete = true;
        boolean bool1 = paramLanguagePackFile.isGz;
        String str = null;
        if (bool1) {
            boolean bool2 = uncompress(paramLanguagePackFile);
            str = null;
            if (!bool2) {
                str = "Unable to decompress.";
            }
        }
        if ((str == null) && (!checkChecksum(paramLanguagePackFile.getUncompressedFile(this.mDownloadManager), paramLanguagePackFile.checksum))) {
            str = "Checksum did not match.";
        }
        if (str != null) {
            paramLanguagePackFile.downloadComplete = false;
            notifyFailed(paramLanguagePackFile.downloadName + ": " + str);
            return false;
        }
        return true;
    }

    private void processDownloadedManifest(long paramLong) {
        List localList = readLinesFromDownload(paramLong);
        if ((localList != null) && (localList.size() > 0)) {
            Iterator localIterator = localList.iterator();
            while (localIterator.hasNext()) {
                String str = (String) localIterator.next();
                this.mLanguagePackFiles.add(new LanguagePackFile(str));
            }
            this.mForeground.set(true);
            initNotificationBuilder(this.mCurrentDownloadJob.mDownloadRequest);
            startForegroundInternal();
            pollForProgressWhileForeground(this.mCurrentDownloadJob.mDownloadRequest);
            downloadFiles();
        }
    }

    public static void startFrom(Context paramContext, ParcelableDownloadRequest paramParcelableDownloadRequest, String paramString) {
        Intent localIntent = new Intent("com.google.android.speech.embedded.download_manifest");
        localIntent.setClass(paramContext, LanguagePackDownloadService.class);
        localIntent.putExtra("com.google.android.speech.embedded.download_manifest_request", paramParcelableDownloadRequest);
        localIntent.putExtra("com.google.android.speech.embedded.download_manifest_bcp47_Locale", paramString);
        paramContext.startService(localIntent);
    }

    private boolean uncompress(LanguagePackFile paramLanguagePackFile) {
        File localFile1 = newFile(this.mDownloadManager.getUriForDownloadedFile(paramLanguagePackFile.downloadId).toString());
        File localFile2 = paramLanguagePackFile.getUncompressedFile(this.mDownloadManager);
        if (localFile2 != null) {
            return IoUtils.uncompress(localFile1, localFile2);
        }
        return false;
    }

    /* Error */
    boolean checkChecksum(File paramFile, String paramString) {
        // Byte code:
        //   0: aconst_null
        //   1: astore_3
        //   2: new 477	java/io/BufferedInputStream
        //   5: dup
        //   6: new 479	java/io/FileInputStream
        //   9: dup
        //   10: aload_1
        //   11: invokespecial 482	java/io/FileInputStream:<init>	(Ljava/io/File;)V
        //   14: invokespecial 485	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
        //   17: astore 4
        //   19: invokestatic 491	com/google/common/hash/Hashing:sha1	()Lcom/google/common/hash/HashFunction;
        //   22: invokeinterface 497 1 0
        //   27: astore 8
        //   29: sipush 1024
        //   32: newarray byte
        //   34: astore 9
        //   36: aload 4
        //   38: aload 9
        //   40: invokevirtual 501	java/io/BufferedInputStream:read	([B)I
        //   43: istore 10
        //   45: iload 10
        //   47: iconst_m1
        //   48: if_icmpeq +32 -> 80
        //   51: aload 8
        //   53: aload 9
        //   55: iconst_0
        //   56: iload 10
        //   58: invokeinterface 507 4 0
        //   63: pop
        //   64: goto -28 -> 36
        //   67: astore 6
        //   69: aload 4
        //   71: astore 7
        //   73: aload 7
        //   75: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   78: iconst_0
        //   79: ireturn
        //   80: aload 8
        //   82: invokeinterface 517 1 0
        //   87: invokevirtual 520	com/google/common/hash/HashCode:toString	()Ljava/lang/String;
        //   90: astore 12
        //   92: new 348	java/lang/String
        //   95: dup
        //   96: aload_2
        //   97: bipush 8
        //   99: invokestatic 526	android/util/Base64:decode	(Ljava/lang/String;I)[B
        //   102: invokespecial 529	java/lang/String:<init>	([B)V
        //   105: invokevirtual 532	java/lang/String:trim	()Ljava/lang/String;
        //   108: astore 13
        //   110: aload 12
        //   112: invokevirtual 532	java/lang/String:trim	()Ljava/lang/String;
        //   115: aload 13
        //   117: invokevirtual 535	java/lang/String:equals	(Ljava/lang/Object;)Z
        //   120: istore 14
        //   122: aload 4
        //   124: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   127: iload 14
        //   129: ireturn
        //   130: astore 5
        //   132: aload_3
        //   133: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   136: aload 5
        //   138: athrow
        //   139: astore 5
        //   141: aload 4
        //   143: astore_3
        //   144: goto -12 -> 132
        //   147: astore 15
        //   149: aconst_null
        //   150: astore 7
        //   152: goto -79 -> 73
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	155	0	this	LanguagePackDownloadService
        //   0	155	1	paramFile	File
        //   0	155	2	paramString	String
        //   1	143	3	localObject1	Object
        //   17	125	4	localBufferedInputStream1	java.io.BufferedInputStream
        //   130	7	5	localObject2	Object
        //   139	1	5	localObject3	Object
        //   67	1	6	localIOException1	java.io.IOException
        //   71	80	7	localBufferedInputStream2	java.io.BufferedInputStream
        //   27	54	8	localHasher	com.google.common.hash.Hasher
        //   34	20	9	arrayOfByte	byte[]
        //   43	14	10	i	int
        //   90	21	12	str1	String
        //   108	8	13	str2	String
        //   120	8	14	bool	boolean
        //   147	1	15	localIOException2	java.io.IOException
        // Exception table:
        //   from	to	target	type
        //   19	36	67	java/io/IOException
        //   36	45	67	java/io/IOException
        //   51	64	67	java/io/IOException
        //   80	122	67	java/io/IOException
        //   2	19	130	finally
        //   19	36	139	finally
        //   36	45	139	finally
        //   51	64	139	finally
        //   80	122	139	finally
        //   2	19	147	java/io/IOException
    }

    void cleanUpCurrentRequest() {
        if (getLanguagePackFilesIds().length > 0) {
            this.mDownloadManager.remove(getLanguagePackFilesIds());
        }
        Iterator localIterator = this.mLanguagePackFiles.iterator();
        while (localIterator.hasNext()) {
            LanguagePackFile localLanguagePackFile = (LanguagePackFile) localIterator.next();
            if ((localLanguagePackFile.isGz) && (localLanguagePackFile.downloadComplete)) {
                File localFile2 = localLanguagePackFile.getUncompressedFile(this.mDownloadManager);
                if (localFile2 != null) {
                    localFile2.delete();
                }
            }
        }
        this.mLanguagePackFiles.clear();
        if (this.mCurrentDownloadJob != null) {
            File localFile1 = LanguagePackDownloadUtils.getStagingDir(this, this.mCurrentDownloadJob.mBcp47Locale);
            if (localFile1.exists()) {
                LanguagePackDownloadUtils.deleteExistingFilesOrCreateDirectory(localFile1);
                localFile1.delete();
            }
        }
        if (this.mForeground.get()) {
            this.mForeground.set(false);
            stopForegroundInternal();
        }
        this.mCurrentDownloadJob = null;
    }

    void initNotificationBuilder(ParcelableDownloadRequest paramParcelableDownloadRequest) {
        Context localContext = getApplicationContext();
        this.mNotificationBuilder.setContentTitle(paramParcelableDownloadRequest.getTitle(localContext));
        this.mNotificationBuilder.setContentText(paramParcelableDownloadRequest.getDescription(localContext));
        this.mNotificationBuilder.setProgress(1, 0, false);
    }

    protected Notification.Builder makeNotificationBuilder() {
        Context localContext = getApplicationContext();
        Notification.Builder localBuilder = new Notification.Builder(localContext);
        localBuilder.setOngoing(true);
        localBuilder.setContentIntent(PendingIntent.getActivity(localContext, 0, InstallActivity.getStartIntent(localContext), 0));
        return localBuilder;
    }

    protected File newFile(String paramString) {
        return new File(paramString);
    }

    public void onCreate() {
        super.onCreate();
        this.mDownloadManager = ((DownloadManager) getSystemService("download"));
        getApplicationContext().registerReceiver(this.mDownloadReceiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
        this.mNotificationManager = ((NotificationManager) getSystemService("notification"));
        this.mNotificationBuilder = makeNotificationBuilder();
        this.mDataManager = VelvetServices.get().getVoiceSearchServices().getGreco3Container().getGreco3DataManager();
    }

    public void onDestroy() {
        getApplicationContext().unregisterReceiver(this.mDownloadReceiver);
        cleanUpCurrentRequest();
    }

    protected void onHandleIntent(Intent paramIntent) {
        ParcelableDownloadRequest localParcelableDownloadRequest = (ParcelableDownloadRequest) paramIntent.getParcelableExtra("com.google.android.speech.embedded.download_manifest_request");
        String str = paramIntent.getStringExtra("com.google.android.speech.embedded.download_manifest_bcp47_Locale");
        if ((localParcelableDownloadRequest == null) || (str == null)) {
            notifyFailed("Cannot download: request or locale extra missing.");
            return;
        }
        DownloadJob localDownloadJob = new DownloadJob(localParcelableDownloadRequest, str);
        if (this.mCurrentDownloadJob == null) {
            this.mCurrentDownloadJob = localDownloadJob;
            this.mCurrentDownloadJob.mManifestDownloadId = this.mDownloadManager.enqueue(this.mCurrentDownloadJob.mDownloadRequest);
            return;
        }
        this.mJobQueue.add(localDownloadJob);
    }

    Future<?> pollForProgressWhileForeground(final ParcelableDownloadRequest paramParcelableDownloadRequest) {
        this.mPollingExecutor.submit(new Runnable() {
            public void run() {
                while (LanguagePackDownloadService.this.mForeground.get()) {
                    LanguagePackDownloadService.this.mNotificationBuilder.setProgress(paramParcelableDownloadRequest.getSizeKb(), LanguagePackDownloadService.this.getKbSoFar(), false);
                    LanguagePackDownloadService.this.mNotificationManager.notify(325633, LanguagePackDownloadService.this.mNotificationBuilder.build());
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException localInterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }

    /* Error */
    protected List<String> readLinesFromDownload(long paramLong) {
        // Byte code:
        //   0: invokestatic 70	com/google/common/collect/Lists:newArrayList	()Ljava/util/ArrayList;
        //   3: astore_3
        //   4: aconst_null
        //   5: astore 4
        //   7: aconst_null
        //   8: astore 5
        //   10: new 670	java/io/FileReader
        //   13: dup
        //   14: aload_0
        //   15: getfield 206	com/google/android/voicesearch/greco3/languagepack/LanguagePackDownloadService:mDownloadManager	Landroid/app/DownloadManager;
        //   18: lload_1
        //   19: invokevirtual 674	android/app/DownloadManager:openDownloadedFile	(J)Landroid/os/ParcelFileDescriptor;
        //   22: invokevirtual 680	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
        //   25: invokespecial 683	java/io/FileReader:<init>	(Ljava/io/FileDescriptor;)V
        //   28: astore 6
        //   30: new 685	java/io/BufferedReader
        //   33: dup
        //   34: aload 6
        //   36: invokespecial 688	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
        //   39: astore 7
        //   41: aload 7
        //   43: invokevirtual 691	java/io/BufferedReader:readLine	()Ljava/lang/String;
        //   46: astore 11
        //   48: aload 11
        //   50: ifnull +44 -> 94
        //   53: aload_3
        //   54: aload 11
        //   56: invokeinterface 431 2 0
        //   61: pop
        //   62: goto -21 -> 41
        //   65: astore 10
        //   67: aload 7
        //   69: astore 5
        //   71: aload 6
        //   73: astore 4
        //   75: aload_0
        //   76: ldc_w 693
        //   79: invokespecial 335	com/google/android/voicesearch/greco3/languagepack/LanguagePackDownloadService:notifyFailed	(Ljava/lang/String;)V
        //   82: aload 4
        //   84: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   87: aload 5
        //   89: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   92: aconst_null
        //   93: areturn
        //   94: aload 6
        //   96: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   99: aload 7
        //   101: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   104: aload_3
        //   105: areturn
        //   106: astore 16
        //   108: aload_0
        //   109: ldc_w 693
        //   112: invokespecial 335	com/google/android/voicesearch/greco3/languagepack/LanguagePackDownloadService:notifyFailed	(Ljava/lang/String;)V
        //   115: aload 4
        //   117: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   120: aload 5
        //   122: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   125: aconst_null
        //   126: areturn
        //   127: astore 9
        //   129: aload 4
        //   131: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   134: aload 5
        //   136: invokestatic 513	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   139: aload 9
        //   141: athrow
        //   142: astore 9
        //   144: aload 6
        //   146: astore 4
        //   148: aconst_null
        //   149: astore 5
        //   151: goto -22 -> 129
        //   154: astore 9
        //   156: aload 7
        //   158: astore 5
        //   160: aload 6
        //   162: astore 4
        //   164: goto -35 -> 129
        //   167: astore 14
        //   169: aload 6
        //   171: astore 4
        //   173: aconst_null
        //   174: astore 5
        //   176: goto -68 -> 108
        //   179: astore 8
        //   181: aload 7
        //   183: astore 5
        //   185: aload 6
        //   187: astore 4
        //   189: goto -81 -> 108
        //   192: astore 15
        //   194: aconst_null
        //   195: astore 5
        //   197: aconst_null
        //   198: astore 4
        //   200: goto -125 -> 75
        //   203: astore 13
        //   205: aload 6
        //   207: astore 4
        //   209: aconst_null
        //   210: astore 5
        //   212: goto -137 -> 75
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	215	0	this	LanguagePackDownloadService
        //   0	215	1	paramLong	long
        //   3	102	3	localArrayList	java.util.ArrayList
        //   5	203	4	localObject1	Object
        //   8	203	5	localObject2	Object
        //   28	178	6	localFileReader	java.io.FileReader
        //   39	143	7	localBufferedReader	java.io.BufferedReader
        //   179	1	8	localIOException1	java.io.IOException
        //   127	13	9	localObject3	Object
        //   142	1	9	localObject4	Object
        //   154	1	9	localObject5	Object
        //   65	1	10	localFileNotFoundException1	java.io.FileNotFoundException
        //   46	9	11	str	String
        //   203	1	13	localFileNotFoundException2	java.io.FileNotFoundException
        //   167	1	14	localIOException2	java.io.IOException
        //   192	1	15	localFileNotFoundException3	java.io.FileNotFoundException
        //   106	1	16	localIOException3	java.io.IOException
        // Exception table:
        //   from	to	target	type
        //   41	48	65	java/io/FileNotFoundException
        //   53	62	65	java/io/FileNotFoundException
        //   10	30	106	java/io/IOException
        //   10	30	127	finally
        //   75	82	127	finally
        //   108	115	127	finally
        //   30	41	142	finally
        //   41	48	154	finally
        //   53	62	154	finally
        //   30	41	167	java/io/IOException
        //   41	48	179	java/io/IOException
        //   53	62	179	java/io/IOException
        //   10	30	192	java/io/FileNotFoundException
        //   30	41	203	java/io/FileNotFoundException
    }

    protected void startForegroundInternal() {
        startForeground(325633, this.mNotificationBuilder.build());
    }

    protected void stopForegroundInternal() {
        stopForeground(true);
    }

    private static class DownloadJob {
        final String mBcp47Locale;
        final ParcelableDownloadRequest mDownloadRequest;
        long mManifestDownloadId = -9223372036854775808L;

        public DownloadJob(ParcelableDownloadRequest paramParcelableDownloadRequest, String paramString) {
            this.mDownloadRequest = paramParcelableDownloadRequest;
            this.mBcp47Locale = paramString;
        }
    }

    class LanguagePackFile {
        final String checksum;
        boolean downloadComplete = false;
        long downloadId = -9223372036854775808L;
        final String downloadName;
        final Uri downloadUri;
        final String fileName;
        final boolean isGz;
        private File uncompressedFile = null;

        LanguagePackFile(String paramString) {
            int i = paramString.length();
            if (paramString.endsWith(".gz")) {
                this.isGz = true;
                this.fileName = paramString.substring(0, i - 32);
            }
            for (this.checksum = paramString.substring(i - 31, i - 3); ; this.checksum = paramString.substring(i - 28, i)) {
                this.downloadName = paramString;
                this.downloadUri = LanguagePackDownloadService.this.getDownloadUri(paramString);
                return;
                this.isGz = false;
                this.fileName = paramString.substring(0, i - 29);
            }
        }

        @Nullable
        private File getUncompressedFile(DownloadManager paramDownloadManager) {
            if (!this.downloadComplete) {
                return null;
            }
            Uri localUri1;
            Uri localUri2;
            if (this.uncompressedFile == null) {
                localUri1 = paramDownloadManager.getUriForDownloadedFile(this.downloadId);
                if (!this.isGz) {
                    break label92;
                }
                localUri2 = Util.buildUriFromParent(localUri1).appendEncodedPath(this.fileName + "-" + this.checksum).build();
            }
            label92:
            for (this.uncompressedFile = LanguagePackDownloadService.this.newFile(localUri2.toString()); ; this.uncompressedFile = LanguagePackDownloadService.this.newFile(localUri1.toString())) {
                return this.uncompressedFile;
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.LanguagePackDownloadService

 * JD-Core Version:    0.7.0.1

 */