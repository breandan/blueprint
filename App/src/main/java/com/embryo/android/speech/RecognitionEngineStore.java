package com.embryo.android.speech;

import android.util.Pair;

import java.util.List;

public abstract interface RecognitionEngineStore {
    public abstract List<Pair<Integer, com.embryo.android.speech.engine.RecognitionEngine>> getEngines(List<Integer> paramList);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionEngineStore

 * JD-Core Version:    0.7.0.1

 */