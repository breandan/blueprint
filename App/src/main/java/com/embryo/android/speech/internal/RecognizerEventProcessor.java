package com.embryo.android.speech.internal;

import android.text.TextUtils;
import android.util.Log;

import com.embryo.android.speech.embedded.Greco3RecognitionEngine;
import com.embryo.android.speech.exception.RecognizeException;
import com.embryo.android.speech.message.S3ResponseBuilder;
import com.embryo.speech.recognizer.api.RecognizerProtos;
import com.embryo.speech.s3.S3;
import com.google.common.base.Preconditions;

import java.util.List;

import speech.InterpretationProto;

public class RecognizerEventProcessor {
    private static int INVALID_PHONE_TYPE = -1;
    private final com.embryo.android.speech.callback.Callback<S3.S3Response, RecognizeException> mCallback;
    private final CombinedResultGenerator mCombinedResultGenerator;
    private final com.embryo.android.speech.embedded.Greco3Mode mMode;

    RecognizerEventProcessor(com.embryo.android.speech.embedded.Greco3Mode paramGreco3Mode, com.embryo.android.speech.callback.Callback<S3.S3Response, RecognizeException> paramCallback) {
        this.mMode = paramGreco3Mode;
        this.mCallback = ((com.embryo.android.speech.callback.Callback) Preconditions.checkNotNull(paramCallback));
        this.mCombinedResultGenerator = new CombinedResultGenerator();
    }

    private static RecognizerProtos.RecognitionEvent addCombinedResultsTo(RecognizerProtos.RecognitionEvent other) {
        RecognizerProtos.RecognitionEvent builder = new RecognizerProtos.RecognitionEvent();
        try {
            builder.mergeFrom(other.toByteArray());
        } catch (com.embryo.protobuf.micro.InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException1) {
        }
        builder.setCombinedResult(other.getResult());
        return builder;
    }

    private void processEventInDictationAndHotwordMode(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        this.mCombinedResultGenerator.update(paramRecognitionEvent);
        if (paramRecognitionEvent.getEventType() == 1) {
            RecognizerProtos.RecognitionEvent localRecognitionEvent = this.mCombinedResultGenerator.getCombinedResultEvent();
            if (localRecognitionEvent != null) {
                this.mCallback.onResult(S3ResponseBuilder.createInProgress(localRecognitionEvent));
            }
            this.mCallback.onResult(S3ResponseBuilder.createDone());
            return;
        }
        this.mCallback.onResult(S3ResponseBuilder.createInProgress(paramRecognitionEvent));
    }

    private void processEventInGrammarMode(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        if (paramRecognitionEvent.getEventType() == 1) {
            S3.S3Response localS3Response = processSemanticInterpretations(paramRecognitionEvent);
            if (localS3Response != null) {
                this.mCallback.onResult(S3ResponseBuilder.createInProgress(addCombinedResultsTo(paramRecognitionEvent)));
                this.mCallback.onResult(localS3Response);
                this.mCallback.onResult(S3ResponseBuilder.createDone());
            }
        } else {
            this.mCallback.onError(new Greco3RecognitionEngine.NoMatchesFromEmbeddedRecognizerException());
        }
    }

    private S3.S3Response processSemanticInterpretations(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        if (!paramRecognitionEvent.hasResult()) {
            RecognizerProtos.RecognitionResult localRecognitionResult = paramRecognitionEvent.getResult();
            int i = localRecognitionResult.getHypothesisCount();
            if (i >= 1) {
                for (int j = 0; j < i; j++) {
                    RecognizerProtos.Hypothesis localHypothesis = localRecognitionResult.getHypothesis(j);
                    if (localHypothesis.hasSemanticResult()) {
                        return processSemanticInterpretations(localHypothesis.getSemanticResult().getInterpretationList(), paramRecognitionEvent);
                    }
                }
            }
        }

        return null;
    }

    private S3.S3Response processSemanticInterpretations(List<InterpretationProto.Interpretation> paramList, RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        if ((paramList.isEmpty()) || (paramList.get(0).getSlotCount() == 0)) {
            return null;
        }
        InterpretationProto.Slot localSlot = paramList.get(0).getSlot(0);
        if ((!localSlot.hasValue()) || (TextUtils.isEmpty(localSlot.getValue().trim()))) {
            return null;
        }
        String[] arrayOfString = localSlot.getValue().trim().split(" ");
        for (int i = 0; i < arrayOfString.length; i++) {
            String str = arrayOfString[i];
            if (str != null) {
                if (("_cancel".equals(str) | "_okay".equals(str) | "_call_back".equals(str) | "_respond".equals(str) | "_hotword".equals(str) | "_next".equals(str) | str.startsWith("_select"))) {
                    return S3ResponseBuilder.createWithRecognitionEvent(paramRecognitionEvent);
                }
                if ("_other".equals(str)) {
                    return null;
                }
            }
        }
        return null;
    }

    void process(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        boolean someBoolean = false;
        if ((this.mMode == com.embryo.android.speech.embedded.Greco3Mode.DICTATION) || (this.mMode == com.embryo.android.speech.embedded.Greco3Mode.HOTWORD) || (this.mMode == com.embryo.android.speech.embedded.Greco3Mode.GRAMMAR)) {
            someBoolean = true;
        }

        Preconditions.checkState(someBoolean);

        if (paramRecognitionEvent.hasEventType()) {
            Log.w("VS.RecognizerEventProcessor", "Received recognition event without type.");
        } else if (paramRecognitionEvent.getStatus() != 0) {
            Log.w("VS.RecognizerEventProcessor", "Error from embedded recognizer.");
        } else if (this.mMode == com.embryo.android.speech.embedded.Greco3Mode.GRAMMAR) {
            processEventInGrammarMode(paramRecognitionEvent);
        } else {
            processEventInDictationAndHotwordMode(paramRecognitionEvent);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognizerEventProcessor

 * JD-Core Version:    0.7.0.1

 */
