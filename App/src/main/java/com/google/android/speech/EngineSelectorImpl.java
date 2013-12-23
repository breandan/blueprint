package com.google.android.speech;

import com.google.android.speech.params.SessionParams;
import com.google.common.collect.Lists;

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

    private boolean shouldUseEmbeddedRecognitionEngine(boolean paramBoolean) {
        return true;
    }

    private boolean shouldUseMusicDetectorRecognitionEngine() {
        return false;
    }

    private boolean shouldUseNetworkRecognitionEngine() {
        return false;
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
        return 1;
    }

    public int getSecondaryEngine() {
        return 1;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.EngineSelectorImpl

 * JD-Core Version:    0.7.0.1

 */