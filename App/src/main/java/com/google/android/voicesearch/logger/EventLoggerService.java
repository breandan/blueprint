package com.google.android.voicesearch.logger;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.android.search.core.GsaConfigFlags;
import com.google.android.speech.logger.LogSender;
import com.google.android.speech.logs.S3LogSender;
import com.google.android.velvet.VelvetApplication;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.logger.store.EventLoggerStores;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.io.Closeables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class EventLoggerService
        extends IntentService {
    public static String SEND_EVENTS = "send_events";
    private static volatile boolean sTesting = false;
    private String mApplicationVersionCode;
    private String mApplicationVersionName;
    private GsaConfigFlags mGsaConfigFlags;
    private LogSender mLogSender;
    private Settings mSettings;

    public EventLoggerService() {
        super("EventLoggerService");
    }

    /* Error */
    private void addStoredClientLogs(ArrayList<VoicesearchClientLogProto.VoiceSearchClientLog> paramArrayList) {
        // Byte code:
        //   0: aconst_null
        //   1: astore_2
        //   2: new 38	java/io/DataInputStream
        //   5: dup
        //   6: aload_0
        //   7: ldc 40
        //   9: invokevirtual 44	com/google/android/voicesearch/logger/EventLoggerService:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
        //   12: invokespecial 47	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
        //   15: astore_3
        //   16: aload_3
        //   17: invokevirtual 51	java/io/DataInputStream:available	()I
        //   20: ifle +33 -> 53
        //   23: aload_1
        //   24: iconst_0
        //   25: aload_3
        //   26: invokestatic 57	com/google/protobuf/micro/CodedInputStreamMicro:newInstance	(Ljava/io/InputStream;)Lcom/google/protobuf/micro/CodedInputStreamMicro;
        //   29: invokestatic 63	com/google/speech/logs/VoicesearchClientLogProto$VoiceSearchClientLog:parseFrom	(Lcom/google/protobuf/micro/CodedInputStreamMicro;)Lcom/google/speech/logs/VoicesearchClientLogProto$VoiceSearchClientLog;
        //   32: invokevirtual 69	java/util/ArrayList:add	(ILjava/lang/Object;)V
        //   35: goto -19 -> 16
        //   38: astore 7
        //   40: aload_3
        //   41: astore 8
        //   43: aload 8
        //   45: invokestatic 75	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   48: aload_0
        //   49: invokespecial 78	com/google/android/voicesearch/logger/EventLoggerService:deleteBufferFile	()V
        //   52: return
        //   53: aload_3
        //   54: invokestatic 75	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   57: goto -9 -> 48
        //   60: astore 4
        //   62: ldc 27
        //   64: ldc 80
        //   66: aload 4
        //   68: invokestatic 86	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   71: pop
        //   72: aload_2
        //   73: invokestatic 75	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   76: goto -28 -> 48
        //   79: astore 5
        //   81: aload_2
        //   82: invokestatic 75	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
        //   85: aload 5
        //   87: athrow
        //   88: astore 5
        //   90: aload_3
        //   91: astore_2
        //   92: goto -11 -> 81
        //   95: astore 4
        //   97: aload_3
        //   98: astore_2
        //   99: goto -37 -> 62
        //   102: astore 9
        //   104: aconst_null
        //   105: astore 8
        //   107: goto -64 -> 43
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	110	0	this	EventLoggerService
        //   0	110	1	paramArrayList	ArrayList<VoicesearchClientLogProto.VoiceSearchClientLog>
        //   1	98	2	localObject1	Object
        //   15	83	3	localDataInputStream1	java.io.DataInputStream
        //   60	7	4	localIOException1	IOException
        //   95	1	4	localIOException2	IOException
        //   79	7	5	localObject2	Object
        //   88	1	5	localObject3	Object
        //   38	1	7	localFileNotFoundException1	java.io.FileNotFoundException
        //   41	65	8	localDataInputStream2	java.io.DataInputStream
        //   102	1	9	localFileNotFoundException2	java.io.FileNotFoundException
        // Exception table:
        //   from	to	target	type
        //   16	35	38	java/io/FileNotFoundException
        //   2	16	60	java/io/IOException
        //   2	16	79	finally
        //   62	72	79	finally
        //   16	35	88	finally
        //   16	35	95	java/io/IOException
        //   2	16	102	java/io/FileNotFoundException
    }

    public static void cancelSendEvents(Context paramContext) {
        if (!sTesting) {
            ((AlarmManager) paramContext.getSystemService("alarm")).cancel(createPendingIntent(paramContext));
        }
    }

    private static PendingIntent createPendingIntent(Context paramContext) {
        Intent localIntent = new Intent(paramContext, EventLoggerService.class);
        localIntent.setAction(SEND_EVENTS);
        return PendingIntent.getService(paramContext, 0, localIntent, 268435456);
    }

    private void deleteBufferFile() {
        File localFile = new File(getFilesDir(), "event_logger_buffer");
        if (localFile.exists()) {
            localFile.delete();
        }
    }

    public static void disableSendEventsForTesting() {
        sTesting = true;
    }

    public static void enableSendEventsAfterTesting() {
        sTesting = false;
    }

    private int getImeLangCount() {
        return ((InputMethodManager) getSystemService("input_method")).getEnabledInputMethodSubtypeList(null, false).size();
    }

    private int getVoiceSearchLangCount() {
        if (this.mSettings.isDefaultSpokenLanguage()) {
            return 0;
        }
        return 1;
    }

    public static void scheduleSendEvents(Context paramContext) {
        if (!sTesting) {
            PendingIntent localPendingIntent = createPendingIntent(paramContext);
            ((AlarmManager) paramContext.getSystemService("alarm")).set(3, 10000L + SystemClock.elapsedRealtime(), localPendingIntent);
        }
    }

    private void sendClientLogs(ArrayList<VoicesearchClientLogProto.VoiceSearchClientLog> paramArrayList) {
        addStoredClientLogs(paramArrayList);
        try {
            if ((!ActivityManager.isUserAMonkey()) && (!ActivityManager.isRunningInTestHarness())) {
                this.mLogSender.send(paramArrayList);
            }
            updateSettings(paramArrayList);
            return;
        } catch (IOException localIOException) {
            for (; ; ) {
                Log.w("EventLoggerService", "Unable to send logs " + localIOException.getMessage());
                storeClientLogs(paramArrayList);
            }
        }
    }

    private void sendEvents() {
        sendClientLogs(new ClientLogsBuilder(this.mApplicationVersionCode, this.mApplicationVersionName, this.mSettings.getInstallId(), getPackageName(), getImeLangCount(), getVoiceSearchLangCount(), this.mSettings.getExperimentIds(), this.mGsaConfigFlags).build(EventLoggerStores.getMainEventLoggerStore().getAndClearResults()));
    }

    private void storeClientLogs(ArrayList<VoicesearchClientLogProto.VoiceSearchClientLog> paramArrayList) {
        FileOutputStream localFileOutputStream = null;
        try {
            File localFile = new File(getFilesDir(), "event_logger_buffer");
            long l = 0L;
            boolean bool = localFile.exists();
            localFileOutputStream = null;
            if (bool) {
                l = localFile.length();
            }
            localFileOutputStream = openFileOutput("event_logger_buffer", 32768);
            for (int i = 0; (i < paramArrayList.size()) && (l < 10240L); i++) {
                localFileOutputStream.write(((VoicesearchClientLogProto.VoiceSearchClientLog) paramArrayList.get(i)).toByteArray());
            }
            localFileOutputStream.flush();
            localFileOutputStream.close();
            return;
        } catch (IOException localIOException) {
            Log.e("EventLoggerService", "Error saving logs", localIOException);
            return;
        } finally {
            Closeables.closeQuietly(localFileOutputStream);
        }
    }

    private void updateSettings(ArrayList<VoicesearchClientLogProto.VoiceSearchClientLog> paramArrayList) {
        Iterator localIterator1 = paramArrayList.iterator();
        for (; ; ) {
            if (!localIterator1.hasNext()) {
                return;
            }
            Iterator localIterator2 = ((VoicesearchClientLogProto.VoiceSearchClientLog) localIterator1.next()).getBundledClientEventsList().iterator();
            if (localIterator2.hasNext()) {
                if (((VoicesearchClientLogProto.ClientEvent) localIterator2.next()).getEventType() != 5) {
                    break;
                }
                this.mSettings.setHasEverUsedVoiceSearch();
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        VelvetServices localVelvetServices = VelvetServices.get();
        VoiceSearchServices localVoiceSearchServices = localVelvetServices.getVoiceSearchServices();
        this.mSettings = localVoiceSearchServices.getSettings();
        this.mLogSender = new S3LogSender(this.mSettings, localVoiceSearchServices.getConnectionFactory());
        this.mApplicationVersionCode = VelvetApplication.getVersionCodeString();
        this.mApplicationVersionName = VelvetApplication.getVersionName();
        this.mGsaConfigFlags = localVelvetServices.getGsaConfigFlags();
        localVoiceSearchServices.getAudioManager();
    }

    protected void onHandleIntent(Intent paramIntent) {
        sendEvents();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.logger.EventLoggerService

 * JD-Core Version:    0.7.0.1

 */