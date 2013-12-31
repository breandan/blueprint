package com.embryo.android.speech.endpointing;

public abstract interface EndpointerEventProcessor {
    public abstract void process(com.embryo.speech.recognizer.api.RecognizerProtos.EndpointerEvent paramEndpointerEvent);

    public abstract void updateProgress(int paramInt, long paramLong);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     EndpointerEventProcessor

 * JD-Core Version:    0.7.0.1

 */