package com.embryo.android.speech.grammar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import com.embryo.android.speech.embedded.Greco3Container;
import com.embryo.android.speech.embedded.Greco3DataManager;
import com.embryo.android.speech.embedded.Greco3Grammar;
import com.embryo.android.speech.embedded.Greco3Preferences;

import android.os.Process;
import com.google.common.io.Files;
import com.google.speech.embedded.Greco3Recognizer;
import com.google.speech.embedded.OfflineActionsManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class GrammarCompilationService extends IntentService {
    private static boolean sGrammarCompilationAlarmSet;
    private Greco3Container mGreco3Container;
    private final MessageDigest mMd5Digest;
    private OfflineActionsManager mOfflineActionsManager;

    public GrammarCompilationService() {
        super("");

        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            Log.w("VS.GrammarCompilationService", "MD5 message digests not supported.");
            digest = null;
        }

        mMd5Digest = digest;
    }

    public void onCreate(Greco3Container container, OfflineActionsManager oam)
    {
        super.onCreate();
        this.mGreco3Container = container;
        this.mOfflineActionsManager = oam;
    }

    private static String createNewRevisionId() {
        long l = System.currentTimeMillis();
        return "v" + l;
    }


    private byte[] digest(String paramString) {
        if (this.mMd5Digest == null) {
            return null;
        }
        this.mMd5Digest.reset();
        byte[] arrayOfByte = paramString.getBytes(Charset.forName("UTF-8"));
        return this.mMd5Digest.digest(arrayOfByte);
    }


    public boolean doCompile(String paramString, Greco3Grammar paramGreco3Grammar, Resources resources, String packageName) {
        Log.i("VS.GrammarCompilationService", "Compiling grammar for: " + paramString + ", type=" + paramGreco3Grammar);
        Greco3Preferences localGreco3Preferences = this.mGreco3Container.getGreco3Preferences();
        Greco3DataManager localGreco3DataManager = this.mGreco3Container.getGreco3DataManager();

        HandsFreeGrammarCompiler localHandsFreeGrammarCompiler = new HandsFreeGrammarCompiler(localGreco3DataManager, new TextGrammarLoader(resources, packageName), paramGreco3Grammar);
        localGreco3DataManager.blockingUpdateResources(false);
        if (!localGreco3DataManager.hasResources(paramString, com.embryo.android.speech.embedded.Greco3Mode.COMPILER)) {
            Log.i("VS.GrammarCompilationService", "No grammar compilation resources, aborting.");
            return false;
        }
        com.embryo.android.speech.embedded.Greco3DataManager.LocaleResources localLocaleResources = localGreco3DataManager.getResources(paramString);
        String str1 = localGreco3Preferences.getCompiledGrammarRevisionId(paramGreco3Grammar);
        byte[] arrayOfByte1 = null;
        if (str1 != null) {
            arrayOfByte1 = getDigestForPath(localLocaleResources.getGrammarPath(paramGreco3Grammar, str1));
        }
        String str2 = localHandsFreeGrammarCompiler.buildGrammar(paramString);
        if (str2 == null) {
            return false;
        }
        byte[] arrayOfByte2 = digest(str2);
        if ((arrayOfByte2 != null) && (Arrays.equals(arrayOfByte2, arrayOfByte1))) {
            return true;
        }
        String str3 = createNewRevisionId();
        File localFile1 = localGreco3DataManager.createOuputPathForGrammarCache(paramGreco3Grammar, paramString);
        File localFile2 = localGreco3DataManager.createOutputPathForGrammar(paramGreco3Grammar, paramString, str3);
        if ((localFile2 == null) || (localFile1 == null)) {
            Log.e("VS.GrammarCompilationService", "Unable to create output directories: dir is null");
            return false;
        }
        if (!localHandsFreeGrammarCompiler.compileGrammar(str2, paramString, localFile2, localFile1)) {
            return false;
        }
        try {
            Files.write(arrayOfByte2, new File(localFile2, "digest"));
            Files.write(localLocaleResources.getLanguageMetadata().toByteArray(), new File(localFile2, "metadata"));
            localGreco3Preferences.setCompiledGrammarRevisionId(paramGreco3Grammar, str3);
            localGreco3DataManager.blockingUpdateResources(true);
            return true;
        } catch (IOException localIOException) {
            Log.e("VS.GrammarCompilationService", "Error writing compiled digest/metadata :" + localIOException);
        }
        return false;
    }

    private byte[] getDigestForPath(String absolutePath) {
        if (absolutePath != null) {
            try {
                return Files.toByteArray(new File(absolutePath, "digest"));
            } catch (IOException ioe) {
            }
        }
        return null;
    }

    private static PendingIntent getPendingIntentForLocale(Context paramContext, String paramString, Greco3Grammar paramGreco3Grammar) {
        return PendingIntent.getService(paramContext, 0, getStartServiceIntent(paramContext, paramString, paramGreco3Grammar), 268435456);
    }

    public static Intent getStartServiceIntent(Context paramContext, String paramString, Greco3Grammar paramGreco3Grammar) {
        Intent localIntent = new Intent(paramContext, GrammarCompilationService.class);
        localIntent.putExtra("compilation_locale", paramString);
        localIntent.putExtra("grammar_type", paramGreco3Grammar.getDirectoryName());
        return localIntent;
    }

    public synchronized static boolean isGrammarCompilationAlarmSet() {
        boolean bool = sGrammarCompilationAlarmSet;
        return bool;
    }

    private static boolean isStaleRevision(String revisionId) {
        if (revisionId == null) {
            return true;
        }
        long parsedTime = 0xffffffff;
        try {
            parsedTime = Long.parseLong(revisionId.substring(0x1));
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Invalid revisionId:" + revisionId, nfe);
        }
        if (parsedTime < 0x0) {
            throw new IllegalArgumentException("Invalid revisionId:" + revisionId + ", negative.");
        }
        boolean localboolean1 = parsedTime < (System.currentTimeMillis() - 0x2932e000);
        return false;
    }

    public static synchronized void maybeSchedulePeriodicCompilation(String existingRevision, Context ctx, String locale, Greco3Grammar grammar, long interval) {
        if ((!sGrammarCompilationAlarmSet) && (!isStaleRevision(existingRevision))) {
            return;
        }

        if (interval <= 0L) {
            interval = 604800000L;
        }

        PendingIntent localPendingIntent = getPendingIntentForLocale(ctx, locale, grammar);
        AlarmManager localAlarmManager = (AlarmManager) ctx.getSystemService("alarm");
        localAlarmManager.cancel(localPendingIntent);
        localAlarmManager.setInexactRepeating(1, 1800000L + System.currentTimeMillis(), interval, localPendingIntent);
        sGrammarCompilationAlarmSet = true;
    }

    public static void startCompilationForLocale(Context paramContext, String paramString, Greco3Grammar paramGreco3Grammar) {
        paramContext.startService(getStartServiceIntent(paramContext, paramString, paramGreco3Grammar));
    }

    public void onHandleIntent(Intent paramIntent) {
        Process.setThreadPriority(10);
        Greco3Recognizer.maybeLoadSharedLibrary();
        String str = paramIntent.getStringExtra("compilation_locale");
        Greco3Grammar localGreco3Grammar = Greco3Grammar.fromDirectoryName(paramIntent.getStringExtra("grammar_type"));
        this.mOfflineActionsManager.notifyStart(localGreco3Grammar);
        boolean bool = false;
        try {
            com.embryo.android.voicesearch.logger.EventLogger.recordLatencyStart(2);
            bool = doCompile(str, localGreco3Grammar, getResources(), getPackageName());
            com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(82);
            return;
        } finally {
            this.mOfflineActionsManager.notifyDone(localGreco3Grammar, bool);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     GrammarCompilationService

 * JD-Core Version:    0.7.0.1

 */
