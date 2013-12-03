package com.google.android.voicesearch.serviceapi;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.speech.utils.RecognizedText;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.util.ErrorUtils;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

class ListenerAdapter
        extends RecognitionEventListenerAdapter {
    private final RecognitionService.Callback mCallback;
    private final boolean mDictationRequested;
    private final OnDoneListener mOnDoneListener;
    private final boolean mPartialsRequested;
    private boolean mRecognitionCompleteReceived;
    private final RecognizedText mRecognizedText;
    private final ExtraPreconditions.ThreadCheck mThreadCheck;

    public ListenerAdapter(RecognitionService.Callback paramCallback, OnDoneListener paramOnDoneListener, boolean paramBoolean1, boolean paramBoolean2) {
        this.mCallback = ((RecognitionService.Callback) Preconditions.checkNotNull(paramCallback));
        this.mOnDoneListener = ((OnDoneListener) Preconditions.checkNotNull(paramOnDoneListener));
        this.mDictationRequested = paramBoolean1;
        this.mPartialsRequested = paramBoolean2;
        this.mThreadCheck = ExtraPreconditions.createSameThreadCheck();
        this.mRecognizedText = new RecognizedText();
    }

    private void processCombinedResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        this.mRecognitionCompleteReceived = true;
        List localList = paramRecognitionEvent.getCombinedResult().getHypothesisList();
        ArrayList localArrayList = new ArrayList(localList.size());
        float[] arrayOfFloat = new float[localList.size()];
        for (int i = 0; i < localList.size(); i++) {
            RecognizerProtos.Hypothesis localHypothesis = (RecognizerProtos.Hypothesis) localList.get(i);
            localArrayList.add(localHypothesis.getText());
            arrayOfFloat[i] = localHypothesis.getConfidence();
        }
        Bundle localBundle = new Bundle();
        localBundle.putStringArrayList("results_recognition", localArrayList);
        localBundle.putFloatArray("confidence_scores", arrayOfFloat);
        EventLogger.recordClientEvent(60);
        try {
            this.mCallback.results(localBundle);
            return;
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "#result remote callback failed", localRemoteException);
        }
    }

    private void processDictationResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        Bundle localBundle;
        if (paramRecognitionEvent.hasResult()) {
            RecognizerProtos.RecognitionResult localRecognitionResult = paramRecognitionEvent.getResult();
            int i = localRecognitionResult.getHypothesisCount();
            if (i > 0) {
                ArrayList localArrayList = new ArrayList(i);
                float[] arrayOfFloat = new float[i];
                for (int j = 0; j < i; j++) {
                    RecognizerProtos.Hypothesis localHypothesis = localRecognitionResult.getHypothesis(j);
                    localArrayList.add(localHypothesis.getText());
                    arrayOfFloat[j] = localHypothesis.getConfidence();
                }
                localBundle = new Bundle();
                localBundle.putStringArrayList("results_recognition", localArrayList);
                localBundle.putFloatArray("confidence_scores", arrayOfFloat);
            }
        }
        try {
            this.mCallback.partialResults(localBundle);
            return;
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "#partialResults remote callback failed", localRemoteException);
        }
    }

    private void processPartialResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        Pair localPair = this.mRecognizedText.updateInProgress(paramRecognitionEvent);
        ArrayList localArrayList = new ArrayList(1);
        localArrayList.add(localPair.first);
        Bundle localBundle = new Bundle();
        localBundle.putStringArrayList("results_recognition", localArrayList);
        try {
            this.mCallback.partialResults(localBundle);
            return;
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "#partialResults remote callback failed", localRemoteException);
        }
    }

    public void onBeginningOfSpeech(long paramLong) {
        this.mThreadCheck.check();
        try {
            this.mCallback.beginningOfSpeech();
            return;
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "beginningOfSpeech callback failed", localRemoteException);
        }
    }

    public void onDone() {
        this.mThreadCheck.check();
        if (!this.mRecognitionCompleteReceived) {
        }
        try {
            this.mCallback.error(7);
            this.mOnDoneListener.onDone();
            return;
        } catch (RemoteException localRemoteException) {
            for (; ; ) {
                Log.w("ListenerAdapter", "#error remote callback failed", localRemoteException);
            }
        }
    }

    public void onEndOfSpeech() {
        this.mThreadCheck.check();
        try {
            this.mCallback.endOfSpeech();
            return;
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "endOfSpeech callback failed", localRemoteException);
        }
    }

    public void onError(RecognizeException paramRecognizeException) {
        this.mThreadCheck.check();
        Preconditions.checkNotNull(paramRecognizeException);
        Log.e("ListenerAdapter", "onError", paramRecognizeException);
        try {
            EventLogger.recordClientEvent(59);
            this.mCallback.error(ErrorUtils.getSpeechRecognizerError(paramRecognizeException));
            this.mOnDoneListener.onDone();
            return;
        } catch (RemoteException localRemoteException) {
            for (; ; ) {
                Log.w("ListenerAdapter", "error callback failed", localRemoteException);
            }
        }
    }

    public void onNoSpeechDetected() {
        this.mThreadCheck.check();
        if (!this.mRecognitionCompleteReceived) {
        }
        try {
            this.mCallback.error(6);
            this.mOnDoneListener.onDone();
            return;
        } catch (RemoteException localRemoteException) {
            for (; ; ) {
                Log.w("ListenerAdapter", "#error remote callback failed", localRemoteException);
            }
        }
    }

    public void onReadyForSpeech() {
        this.mThreadCheck.check();
        try {
            Bundle localBundle = new Bundle();
            this.mCallback.readyForSpeech(localBundle);
            return;
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "readyForSpeech callback failed", localRemoteException);
        }
    }

    public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        this.mThreadCheck.check();
        if (paramRecognitionEvent.getEventType() == 0) {
            if (!this.mDictationRequested) {
                break label58;
            }
            processDictationResult(paramRecognitionEvent);
        }
        for (; ; ) {
            if ((paramRecognitionEvent.getEventType() == 1) && (paramRecognitionEvent.hasCombinedResult()) && (paramRecognitionEvent.getCombinedResult().getHypothesisCount() > 0)) {
                processCombinedResult(paramRecognitionEvent);
            }
            return;
            label58:
            if (this.mPartialsRequested) {
                processPartialResult(paramRecognitionEvent);
            }
        }
    }

    public void sendRmsValue(float paramFloat) {
        try {
            this.mCallback.rmsChanged(paramFloat);
            return;
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "rmsChanged callback failed", localRemoteException);
        }
    }

    static abstract interface OnDoneListener {
        public abstract void onDone();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.serviceapi.ListenerAdapter

 * JD-Core Version:    0.7.0.1

 */