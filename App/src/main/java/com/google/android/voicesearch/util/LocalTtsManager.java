package com.google.android.voicesearch.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.ConditionVariable;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.util.Pair;

import com.google.android.voicesearch.audio.AudioRouter;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nullable;

public class LocalTtsManager {
    static final String GOOGLE_TTS_ENGINE = "com.google.android.tts";
    private AudioManager mAudioManager;
    private final AudioRouter mAudioRouter;
    private final HashMap<String, EnqueuedUtterance> mCallbacksMap;
    private final Context mContext;
    private final Object mLock = new Object();
    private int mSavedBluetoothVolume = -1;
    private boolean mStopPending;
    private TextToSpeech mTextToSpeech;
    private final Executor mTtsExecutor;
    private final Executor mUiThread;
    private int mUtteranceIdSequence;
    private final UtteranceProgressListener mUtteranceProgressListener = new UtteranceProgressListener() {
        public void onDone(String paramAnonymousString) {
            LocalTtsManager.this.onUtteranceCompleted(paramAnonymousString);
        }

        public void onError(String paramAnonymousString) {
            synchronized (LocalTtsManager.this.mLock) {
                LocalTtsManager.EnqueuedUtterance localEnqueuedUtterance1 = (LocalTtsManager.EnqueuedUtterance) LocalTtsManager.this.mCallbacksMap.get(paramAnonymousString);
                LocalTtsManager.EnqueuedUtterance localEnqueuedUtterance2 = null;
                if (localEnqueuedUtterance1 != null) {
                    int i = localEnqueuedUtterance1.synthesisMode;
                    localEnqueuedUtterance2 = null;
                    if (i == 1) {
                        LocalTtsManager.this.mCallbacksMap.remove(paramAnonymousString);
                        localEnqueuedUtterance2 = localEnqueuedUtterance1;
                    }
                }
                if (localEnqueuedUtterance2 != null) {
                    LocalTtsManager.this.enqueue(localEnqueuedUtterance2.utterance, localEnqueuedUtterance2.audioStream, localEnqueuedUtterance2.completionCallback, 3);
                    return;
                }
            }
            LocalTtsManager.this.onUtteranceCompleted(paramAnonymousString);
        }

        public void onStart(String paramAnonymousString) {
        }
    };
    private Locale mVoiceLocale;
    private final Settings mVoiceSettings;

    public LocalTtsManager(Context paramContext, Executor paramExecutor1, Executor paramExecutor2, AudioRouter paramAudioRouter, Settings paramSettings) {
        this.mContext = paramContext;
        this.mUiThread = paramExecutor1;
        this.mTtsExecutor = paramExecutor2;
        this.mAudioRouter = paramAudioRouter;
        this.mVoiceSettings = paramSettings;
        this.mCallbacksMap = new HashMap();
    }

    private void adjustBluetoothScoVolume() {
        if (18 <= 0) {
        }
        int i;
        do {
            return;
            if (this.mAudioManager == null) {
                this.mAudioManager = ((AudioManager) this.mContext.getSystemService("audio"));
            }
            i = this.mAudioManager.getStreamVolume(6);
            int j = this.mAudioManager.getStreamMaxVolume(6);
            int k = j - (1 + 18 / (50 / j));
            if (i > k) {
                this.mAudioManager.setStreamVolume(6, k, 0);
            }
        } while (this.mSavedBluetoothVolume != -1);
        this.mSavedBluetoothVolume = i;
    }

    private static HashMap<String, String> createParamsWithUtteranceId(String paramString, int paramInt1, int paramInt2) {
        HashMap localHashMap = Maps.newHashMap();
        localHashMap.put("utteranceId", paramString);
        localHashMap.put("streamType", String.valueOf(paramInt1));
        if ((paramInt2 == 1) || (paramInt2 == 2)) {
            localHashMap.put("networkTts", "true");
        }
        while (paramInt2 != 3) {
            return localHashMap;
        }
        localHashMap.put("embeddedTts", "true");
        return localHashMap;
    }

    private void enqueue(final String paramString, final int paramInt1, @Nullable final Runnable paramRunnable, final int paramInt2) {
        this.mTtsExecutor.execute(new Runnable() {
            public void run() {
                LocalTtsManager.this.internalEnqueue(paramString, paramInt1, paramRunnable, paramInt2);
            }
        });
    }

    static String getCurrentEngineName(TextToSpeech paramTextToSpeech) {
        try {
            String str = (String) TextToSpeech.class.getMethod("getCurrentEngine", new Class[0]).invoke(paramTextToSpeech, new Object[0]);
            return str;
        } catch (Exception localException) {
            Log.w("LocalTtsManager", "Error invoking getCurrentEngine()", localException);
        }
        return null;
    }

    private void internalEnqueue(String paramString, int paramInt1, Runnable paramRunnable, int paramInt2) {
        this.mStopPending = false;
        maybeCreateTtsAndWaitForInit();
        String str;
        synchronized (this.mLock) {
            StringBuilder localStringBuilder = new StringBuilder().append("utterance:");
            int i = this.mUtteranceIdSequence;
            this.mUtteranceIdSequence = (i + 1);
            str = i;
        }
        synchronized (this.mLock) {
            this.mCallbacksMap.put(str, new EnqueuedUtterance(paramString, paramInt1, paramRunnable, paramInt2));
            if ((this.mTextToSpeech == null) || (this.mStopPending)) {
                this.mStopPending = false;
                onUtteranceCompleted(str);
                return;
                localObject2 =finally;
                throw localObject2;
            }
        }
        this.mAudioRouter.onStartTtsPlayback();
        adjustBluetoothScoVolume();
        if (this.mTextToSpeech.speak(paramString, 0, createParamsWithUtteranceId(str, paramInt1, paramInt2)) != 0) {
            onUtteranceCompleted(str);
        }
        this.mAudioRouter.onStopTtsPlayback();
    }

    private void maybeCreateTtsAndWaitForInit() {
        Locale localLocale = this.mVoiceSettings.getSpokenLocale();
        if ((this.mTextToSpeech != null) && (this.mVoiceLocale != null) && (this.mVoiceLocale.equals(localLocale))) {
        }
        do {
            return;
            this.mTextToSpeech = createAndInitTextToSpeech(localLocale);
            this.mVoiceLocale = localLocale;
        } while (this.mTextToSpeech == null);
        if (this.mTextToSpeech.isLanguageAvailable(localLocale) >= 0) {
            this.mTextToSpeech.setLanguage(localLocale);
        }
        this.mTextToSpeech.setOnUtteranceProgressListener(this.mUtteranceProgressListener);
    }

    private void onUtteranceCompleted(final String paramString) {
        this.mTtsExecutor.execute(new Runnable() {
            public void run() {
                LocalTtsManager.this.restoreBluetoothScoVolume();
            }
        });
        synchronized (this.mLock) {
            final EnqueuedUtterance localEnqueuedUtterance = (EnqueuedUtterance) this.mCallbacksMap.get(paramString);
            if ((localEnqueuedUtterance != null) && (localEnqueuedUtterance.completionCallback != null)) {
                this.mUiThread.execute(new Runnable() {
                    public void run() {
                        synchronized (LocalTtsManager.this.mLock) {
                            if (LocalTtsManager.this.mCallbacksMap.remove(paramString) != localEnqueuedUtterance) {
                                return;
                            }
                            localEnqueuedUtterance.completionCallback.run();
                            return;
                        }
                    }
                });
            }
            return;
        }
    }

    private void restoreBluetoothScoVolume() {
        if (this.mSavedBluetoothVolume != -1) {
            this.mAudioManager.setStreamVolume(6, this.mSavedBluetoothVolume, 0);
            this.mSavedBluetoothVolume = -1;
        }
    }

    private static boolean supportsFeature(TextToSpeech paramTextToSpeech, Locale paramLocale, String paramString) {
        Set localSet = paramTextToSpeech.getFeatures(paramLocale);
        if (localSet == null) {
            return false;
        }
        return localSet.contains(paramString);
    }

    @Deprecated
    public void clearCallbacksAndStop() {
        synchronized (this.mLock) {
            this.mCallbacksMap.clear();
            stop();
            return;
        }
    }

    TextToSpeech createAndInitTextToSpeech(Locale paramLocale) {
        try {
            this.mContext.getPackageManager().getApplicationInfo("com.google.android.tts", 0);
            localObject = "com.google.android.tts";
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            for (; ; ) {
                Pair localPair;
                String str1;
                String str2;
                label66:
                TextToSpeech localTextToSpeech1;
                label119:
                label125:
                TextToSpeech localTextToSpeech2;
                Object localObject = null;
            }
        }
        localPair = createAndInitTextToSpeechEngine((String) localObject);
        str1 = (String) localPair.second;
        if (localObject == null) {
            if (localPair.first == null) {
                localObject = str1;
            }
        } else {
            if ((localObject == null) || (((String) localObject).equals(str1))) {
                break label119;
            }
            str2 = str1;
            localTextToSpeech1 = (TextToSpeech) localPair.first;
            if ((localTextToSpeech1 == null) || (localTextToSpeech1.isLanguageAvailable(paramLocale) < 0) || (!supportsFeature(localTextToSpeech1, paramLocale, "embeddedTts"))) {
                break label125;
            }
        }
        do {
            return localTextToSpeech1;
            localObject = getCurrentEngineName((TextToSpeech) localPair.first);
            break;
            str2 = null;
            break label66;
            localTextToSpeech2 = null;
            if (str2 != null) {
                localTextToSpeech2 = (TextToSpeech) createAndInitTextToSpeechEngine(str2).first;
            }
            if ((localTextToSpeech2 != null) && (localTextToSpeech2.isLanguageAvailable(paramLocale) >= 0) && (supportsFeature(localTextToSpeech2, paramLocale, "embeddedTts"))) {
                return localTextToSpeech2;
            }
        }
        while ((localTextToSpeech1 != null) && (localTextToSpeech1.isLanguageAvailable(paramLocale) >= 0) && (supportsFeature(localTextToSpeech1, paramLocale, "networkTts")));
        if ((localTextToSpeech2 != null) && (localTextToSpeech2.isLanguageAvailable(paramLocale) >= 0) && (supportsFeature(localTextToSpeech2, paramLocale, "networkTts"))) {
            return localTextToSpeech2;
        }
        if (localTextToSpeech1 != null) {
            Log.w("LocalTtsManager", "No TTS available for " + paramLocale + ". Using " + (String) localObject + " in its default locale");
            return localTextToSpeech1;
        }
        if (localTextToSpeech2 != null) {
            Log.w("LocalTtsManager", "No TTS available for " + paramLocale + ". Using " + str2 + " in its default locale");
            return localTextToSpeech2;
        }
        Log.w("LocalTtsManager", "No TTS available");
        return null;
    }

    protected Pair<TextToSpeech, String> createAndInitTextToSpeechEngine(@Nullable String paramString) {
        final ConditionVariable localConditionVariable = new ConditionVariable();
        final AtomicBoolean localAtomicBoolean = new AtomicBoolean(false);
        TextToSpeech localTextToSpeech = new TextToSpeech(this.mContext, new TextToSpeech.OnInitListener() {
            public void onInit(int paramAnonymousInt) {
                if (paramAnonymousInt == 0) {
                    localAtomicBoolean.set(true);
                }
                for (; ; ) {
                    localConditionVariable.open();
                    return;
                    localAtomicBoolean.set(false);
                }
            }
        }, paramString);
        localConditionVariable.block();
        if (localAtomicBoolean.get()) {
            return Pair.create(localTextToSpeech, localTextToSpeech.getDefaultEngine());
        }
        return Pair.create(null, localTextToSpeech.getDefaultEngine());
    }

    public void enqueue(int paramInt, @Nullable Runnable paramRunnable) {
        enqueue(this.mContext.getString(paramInt), paramRunnable);
    }

    public void enqueue(String paramString, @Nullable Runnable paramRunnable) {
        enqueue(paramString, paramRunnable, 0);
    }

    public void enqueue(String paramString, @Nullable Runnable paramRunnable, int paramInt) {
        enqueue(paramString, this.mAudioRouter.getOutputStream(), paramRunnable, paramInt);
    }

    void setTextToSpeechForTesting(TextToSpeech paramTextToSpeech) {
        this.mTextToSpeech = paramTextToSpeech;
    }

    public void stop() {
        this.mTtsExecutor.execute(new Runnable() {
            public void run() {
                if (LocalTtsManager.this.mTextToSpeech == null) {
                    LocalTtsManager.access$602(LocalTtsManager.this, true);
                }
                for (; ; ) {
                    LocalTtsManager.this.restoreBluetoothScoVolume();
                    return;
                    LocalTtsManager.this.mTextToSpeech.stop();
                }
            }
        });
    }

    private static class EnqueuedUtterance {
        final int audioStream;
        final Runnable completionCallback;
        final int synthesisMode;
        final String utterance;

        EnqueuedUtterance(String paramString, int paramInt1, Runnable paramRunnable, int paramInt2) {
            this.utterance = paramString;
            this.audioStream = paramInt1;
            this.completionCallback = paramRunnable;
            this.synthesisMode = paramInt2;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.util.LocalTtsManager

 * JD-Core Version:    0.7.0.1

 */