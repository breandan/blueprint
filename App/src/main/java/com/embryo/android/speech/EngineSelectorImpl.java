package com.embryo.android.speech;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class EngineSelectorImpl
        implements EngineSelector {
    private final com.embryo.android.speech.params.SessionParams mSessionParams;
    private final SpeechSettings mSpeechSettings;

    public EngineSelectorImpl(com.embryo.android.speech.params.SessionParams paramSessionParams, SpeechSettings paramSpeechSettings) {
        this.mSessionParams = paramSessionParams;
        this.mSpeechSettings = paramSpeechSettings;
    }

    private boolean shouldUseEmbeddedRecognitionEngine(boolean paramBoolean) {
        return paramBoolean;
    }

    private boolean shouldUseMusicDetectorRecognitionEngine() {
        return false;
    }

    public List<Integer> getEngineList() {
        ArrayList localArrayList = Lists.newArrayListWithExpectedSize(3);
        if (shouldUseEmbeddedRecognitionEngine(true)) {
            localArrayList.add(Integer.valueOf(1));
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

 * Qualified Name:     EngineSelectorImpl

 * JD-Core Version:    0.7.0.1

 */