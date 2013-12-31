package com.embryo.android.speech.internal;

import com.embryo.android.speech.callback.RecognitionEngineCallback;
import com.embryo.android.speech.embedded.Greco3CallbackFactory;

public class DefaultCallbackFactory
        implements Greco3CallbackFactory {
    public com.embryo.android.speech.embedded.Greco3Callback create(RecognitionEngineCallback paramRecognitionEngineCallback, com.embryo.android.speech.embedded.Greco3Mode paramGreco3Mode) {
        return new Greco3CallbackImpl(paramGreco3Mode, paramRecognitionEngineCallback);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     DefaultCallbackFactory

 * JD-Core Version:    0.7.0.1

 */