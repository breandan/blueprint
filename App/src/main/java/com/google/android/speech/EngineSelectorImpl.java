package com.google.android.speech;

import android.util.Log;

import com.google.android.speech.params.SessionParams;
import com.google.common.collect.Lists;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.util.ArrayList;
import java.util.List;

public class EngineSelectorImpl
        implements EngineSelector {
    private final boolean mHaveNetworkConnection;
    private final SessionParams mSessionParams;
    private final SpeechSettings mSpeechSettings;

    public EngineSelectorImpl(SessionParams paramSessionParams, SpeechSettings paramSpeechSettings, boolean paramBoolean) {
        this.mSessionParams = paramSessionParams;
        this.mSpeechSettings = paramSpeechSettings;
        this.mHaveNetworkConnection = paramBoolean;
    }

    private GstaticConfiguration.Configuration getConfiguration() {
        return this.mSpeechSettings.getConfiguration();
    }

    private boolean isMusicDetectorEnabledForHotword() {
        if ((this.mSessionParams.getMode() != 6) || (!this.mSessionParams.shouldUseMusicHotworder()) || (!getConfiguration().hasSoundSearch())) {
            return false;
        }
        return getConfiguration().getSoundSearch().getEnableMusicHotworder();
    }

    private boolean isMusicDetectorEnabledForVoiceActions() {
        if ((this.mSessionParams.getMode() != 2) || (!getConfiguration().hasSoundSearch())) {
            return false;
        }
        return getConfiguration().getSoundSearch().getEnableMusicDetector();
    }

    private boolean shouldUseEmbeddedRecognitionEngine(boolean paramBoolean) {
        return (!SessionParams.isSoundSearch(this.mSessionParams.getMode())) && (!this.mSessionParams.isResendingAudio()) && ((paramBoolean) || (!this.mSpeechSettings.isNetworkRecognitionOnlyForDebug()));
    }

    private boolean shouldUseMusicDetectorRecognitionEngine() {
        return (this.mSpeechSettings.isSoundSearchEnabled()) && ((isMusicDetectorEnabledForVoiceActions()) || (isMusicDetectorEnabledForHotword()));
    }

    private boolean shouldUseNetworkRecognitionEngine() {
        return (this.mSessionParams.getMode() != 6) && (!this.mSpeechSettings.isEmbeddedRecognitionOnlyForDebug()) && ((this.mSessionParams.isResendingAudio()) || (this.mHaveNetworkConnection));
    }

    public List<Integer> getEngineList() {
        ArrayList localArrayList = Lists.newArrayListWithExpectedSize(3);
        if (shouldUseEmbeddedRecognitionEngine(true)) {
            localArrayList.add(Integer.valueOf(1));
        }
        if (shouldUseNetworkRecognitionEngine()) {
            localArrayList.add(Integer.valueOf(2));
        }
        if (shouldUseMusicDetectorRecognitionEngine()) {
            localArrayList.add(Integer.valueOf(3));
        }
        return localArrayList;
    }

    public int getPrimaryEngine() {
        int i = 1;
        switch (this.mSessionParams.getMode()) {
            default:
                if (shouldUseNetworkRecognitionEngine()) {
                    i = 2;
                }
                break;
        }
        do {
            do {
                return i;
            } while (shouldUseEmbeddedRecognitionEngine(false));
            if (shouldUseNetworkRecognitionEngine()) {
                return 2;
            }
            Log.w("EngineSelectorImpl", "No primary engine for mode: " + this.mSessionParams.getMode());
            return 0;
            if (!shouldUseEmbeddedRecognitionEngine(false)) {
                break;
            }
        } while (this.mHaveNetworkConnection);
        Log.i("EngineSelectorImpl", "Offline: Embedded engine only");
        return i;
        Log.w("EngineSelectorImpl", "No primary engine for mode: " + this.mSessionParams.getMode());
        return 0;
    }

    public int getSecondaryEngine() {
        int i = this.mSessionParams.getMode();
        int j = 0;
        switch (i) {
            default:
                int m = getPrimaryEngine();
                j = 0;
                if (m == 2) {
                    boolean bool2 = shouldUseEmbeddedRecognitionEngine(false);
                    j = 0;
                    if (bool2) {
                        j = 1;
                    }
                }
                break;
        }
        boolean bool1;
        do {
            int k;
            do {
                return j;
                k = getPrimaryEngine();
                j = 0;
            } while (k != 1);
            bool1 = shouldUseNetworkRecognitionEngine();
            j = 0;
        } while (!bool1);
        return 2;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.EngineSelectorImpl

 * JD-Core Version:    0.7.0.1

 */