package com.embryo.android.speech.listeners;

import com.embryo.android.speech.exception.RecognizeException;
import com.embryo.speech.recognizer.api.RecognizerProtos;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeRecognitionEventListener
        implements RecognitionEventListener {
    private final List<RecognitionEventListener> mListeners = new ArrayList();

    public void add(RecognitionEventListener paramRecognitionEventListener) {
        Preconditions.checkNotNull(paramRecognitionEventListener);
        if (!this.mListeners.contains(paramRecognitionEventListener)) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mListeners.add(paramRecognitionEventListener);
            return;
        }
    }

    public void onBeginningOfSpeech(long paramLong) {
        Iterator localIterator = this.mListeners.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEventListener) localIterator.next()).onBeginningOfSpeech(paramLong);
        }
    }

    public void onDone() {
        Iterator localIterator = this.mListeners.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEventListener) localIterator.next()).onDone();
        }
    }

    public void onEndOfSpeech() {
        Iterator localIterator = this.mListeners.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEventListener) localIterator.next()).onEndOfSpeech();
        }
    }

    public void onError(RecognizeException paramRecognizeException) {
        Iterator localIterator = this.mListeners.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEventListener) localIterator.next()).onError(paramRecognizeException);
        }
    }

    public void onMediaDataResult(byte[] paramArrayOfByte) {
        Iterator localIterator = this.mListeners.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEventListener) localIterator.next()).onMediaDataResult(paramArrayOfByte);
        }
    }

    public void onNoSpeechDetected() {
        Iterator localIterator = this.mListeners.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEventListener) localIterator.next()).onNoSpeechDetected();
        }
    }

    public void onReadyForSpeech() {
        Iterator localIterator = this.mListeners.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEventListener) localIterator.next()).onReadyForSpeech();
        }
    }

    public void onRecognitionCancelled() {
        Iterator localIterator = this.mListeners.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEventListener) localIterator.next()).onRecognitionCancelled();
        }
    }

    public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        Iterator localIterator = this.mListeners.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEventListener) localIterator.next()).onRecognitionResult(paramRecognitionEvent);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     CompositeRecognitionEventListener

 * JD-Core Version:    0.7.0.1

 */