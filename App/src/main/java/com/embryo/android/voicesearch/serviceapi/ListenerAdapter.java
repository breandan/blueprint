package com.embryo.android.voicesearch.serviceapi;

import android.os.Bundle;
import android.os.RemoteException;
import android.speech.RecognitionService;
import android.util.Log;
import android.util.Pair;

import com.embryo.android.speech.utils.RecognizedText;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

class ListenerAdapter
        extends com.embryo.android.speech.listeners.RecognitionEventListenerAdapter {
    private final RecognitionService.Callback mCallback;
    private final boolean mDictationRequested;
    private final OnDoneListener mOnDoneListener;
    private final boolean mPartialsRequested;
    private final RecognizedText mRecognizedText;
    private final com.embryo.android.shared.util.ExtraPreconditions.ThreadCheck mThreadCheck;
    private boolean mRecognitionCompleteReceived;

    public ListenerAdapter(RecognitionService.Callback paramCallback, OnDoneListener paramOnDoneListener, boolean paramBoolean1, boolean paramBoolean2) {
        this.mCallback = checkNotNull(paramCallback);
        this.mOnDoneListener = checkNotNull(paramOnDoneListener);
        this.mDictationRequested = paramBoolean1;
        this.mPartialsRequested = paramBoolean2;
        this.mThreadCheck = com.embryo.android.shared.util.ExtraPreconditions.createSameThreadCheck();
        this.mRecognizedText = new RecognizedText();
    }

    private void processCombinedResult(com.embryo.speech.recognizer.api.RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        this.mRecognitionCompleteReceived = true;
        List localList = paramRecognitionEvent.getCombinedResult().getHypothesisList();
        ArrayList localArrayList = new ArrayList(localList.size());
        float[] arrayOfFloat = new float[localList.size()];
        for (int i = 0; i < localList.size(); i++) {
            com.embryo.speech.recognizer.api.RecognizerProtos.Hypothesis localHypothesis = (com.embryo.speech.recognizer.api.RecognizerProtos.Hypothesis) localList.get(i);
            localArrayList.add(localHypothesis.getText());
            arrayOfFloat[i] = localHypothesis.getConfidence();
        }
        Bundle localBundle = new Bundle();
        localBundle.putStringArrayList("results_recognition", localArrayList);
        localBundle.putFloatArray("confidence_scores", arrayOfFloat);
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(60);
        try {
            this.mCallback.results(localBundle);
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "#result remote callback failed", localRemoteException);
        }
    }

    private void processDictationResult(com.embryo.speech.recognizer.api.RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        Bundle localBundle = null;
        if (paramRecognitionEvent.hasResult()) {
            com.embryo.speech.recognizer.api.RecognizerProtos.RecognitionResult localRecognitionResult = paramRecognitionEvent.getResult();
            int i = localRecognitionResult.getHypothesisCount();
            if (i > 0) {
                ArrayList localArrayList = new ArrayList(i);
                float[] arrayOfFloat = new float[i];
                for (int j = 0; j < i; j++) {
                    com.embryo.speech.recognizer.api.RecognizerProtos.Hypothesis localHypothesis = localRecognitionResult.getHypothesis(j);
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
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "#partialResults remote callback failed", localRemoteException);
        }
    }

    private void processPartialResult(com.embryo.speech.recognizer.api.RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        Pair localPair = this.mRecognizedText.updateInProgress(paramRecognitionEvent);
        ArrayList localArrayList = new ArrayList(1);
        localArrayList.add(localPair.first);
        Bundle localBundle = new Bundle();
        localBundle.putStringArrayList("results_recognition", localArrayList);
        try {
            this.mCallback.partialResults(localBundle);
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "#partialResults remote callback failed", localRemoteException);
        }
    }

    public void onBeginningOfSpeech(long paramLong) {
        this.mThreadCheck.check();
        try {
            this.mCallback.beginningOfSpeech();
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "beginningOfSpeech callback failed", localRemoteException);
        }
    }

    public void onDone() {
        this.mThreadCheck.check();
        if (!this.mRecognitionCompleteReceived) {
            try {
                this.mCallback.error(7);
            } catch (RemoteException localRemoteException) {
                Log.w("ListenerAdapter", "#error remote callback failed", localRemoteException);
            }
        }
        this.mOnDoneListener.onDone();
    }

    public void onEndOfSpeech() {
        this.mThreadCheck.check();
        try {
            this.mCallback.endOfSpeech();
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "endOfSpeech callback failed", localRemoteException);
        }
    }

    public void onError(com.embryo.android.speech.exception.RecognizeException paramRecognizeException) {
        this.mThreadCheck.check();
        checkNotNull(paramRecognizeException);
        Log.e("ListenerAdapter", "onError", paramRecognizeException);
        try {
            com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(59);
            this.mCallback.error(com.embryo.android.voicesearch.util.ErrorUtils.getSpeechRecognizerError(paramRecognizeException));
            this.mOnDoneListener.onDone();
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "error callback failed", localRemoteException);
        }
    }

    public void onNoSpeechDetected() {
        mThreadCheck.check();
        if(!mRecognitionCompleteReceived) {
            try {
                mCallback.error(0x6);
            } catch(RemoteException e) {
                Log.w("ListenerAdapter", "#error remote callback failed", e);
            }
        }
        mOnDoneListener.onDone();
    }

    public void onReadyForSpeech() {
        this.mThreadCheck.check();
        try {
            Bundle localBundle = new Bundle();
            this.mCallback.readyForSpeech(localBundle);
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "readyForSpeech callback failed", localRemoteException);
        }
    }

    public void onRecognitionResult(com.embryo.speech.recognizer.api.RecognizerProtos.RecognitionEvent recognitionEvent) {
        mThreadCheck.check();
        if (recognitionEvent.getEventType() == 0) {
            if (mDictationRequested) {
                processDictationResult(recognitionEvent);
            } else if (mPartialsRequested) {
                processPartialResult(recognitionEvent);
            }
        }
        if ((recognitionEvent.getEventType() == 0x1) && (recognitionEvent.hasCombinedResult()) && (recognitionEvent.getCombinedResult().getHypothesisCount() > 0)) {
            processCombinedResult(recognitionEvent);
        }
    }

    public void sendRmsValue(float paramFloat) {
        try {
            this.mCallback.rmsChanged(paramFloat);
        } catch (RemoteException localRemoteException) {
            Log.w("ListenerAdapter", "rmsChanged callback failed", localRemoteException);
        }
    }

    static abstract interface OnDoneListener {
        public abstract void onDone();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ListenerAdapter

 * JD-Core Version:    0.7.0.1

 */