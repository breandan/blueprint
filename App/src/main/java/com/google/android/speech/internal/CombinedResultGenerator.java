package com.google.android.speech.internal;

import android.text.TextUtils;

import com.google.common.collect.Lists;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.speech.recognizer.api.RecognizerProtos;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

public class CombinedResultGenerator {
    private final List<RecognizerProtos.RecognitionEvent> mRecognitionEvents = Lists.newArrayList();

    private RecognizerProtos.RecognitionEvent handleMultipleRecognitionEvents() {
        StringBuilder localStringBuilder = new StringBuilder();
        float f = 3.4028235E+38F;
        Iterator localIterator = this.mRecognitionEvents.iterator();
        while (localIterator.hasNext()) {
            RecognizerProtos.RecognitionEvent localRecognitionEvent3 = (RecognizerProtos.RecognitionEvent) localIterator.next();
            if (localRecognitionEvent3.hasResult()) {
                RecognizerProtos.RecognitionResult localRecognitionResult2 = localRecognitionEvent3.getResult();
                if (localRecognitionResult2.getHypothesisCount() > 0) {
                    RecognizerProtos.Hypothesis localHypothesis = localRecognitionResult2.getHypothesis(0);
                    localStringBuilder.append(localHypothesis.getText());
                    f = Math.min(f, localHypothesis.getConfidence());
                }
            }
        }
        RecognizerProtos.RecognitionEvent localRecognitionEvent1 = (RecognizerProtos.RecognitionEvent) this.mRecognitionEvents.get(-1 + this.mRecognitionEvents.size());
        RecognizerProtos.RecognitionEvent localRecognitionEvent2 = new RecognizerProtos.RecognitionEvent();
        try {
            localRecognitionEvent2.mergeFrom(localRecognitionEvent1.toByteArray());
            if (!TextUtils.isEmpty(localStringBuilder.toString())) {
                RecognizerProtos.RecognitionResult localRecognitionResult1 = new RecognizerProtos.RecognitionResult();
                localRecognitionResult1.addHypothesis(new RecognizerProtos.Hypothesis().setConfidence(f).setText(localStringBuilder.toString()));
                localRecognitionEvent2.setCombinedResult(localRecognitionResult1);
            }
            return localRecognitionEvent2;
        } catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {
        }
        return localRecognitionEvent1;
    }

    private RecognizerProtos.RecognitionEvent handleSingleRecognitionEvent() {
        RecognizerProtos.RecognitionEvent localRecognitionEvent = (RecognizerProtos.RecognitionEvent) this.mRecognitionEvents.get(0);
        if (localRecognitionEvent.hasResult()) {
            localRecognitionEvent.setCombinedResult(localRecognitionEvent.getResult());
        }
        return localRecognitionEvent;
    }

    @Nullable
    public RecognizerProtos.RecognitionEvent getCombinedResultEvent() {
        if (this.mRecognitionEvents.isEmpty()) {
            return null;
        }
        if (this.mRecognitionEvents.size() == 1) {
            return handleSingleRecognitionEvent();
        }
        return handleMultipleRecognitionEvents();
    }

    public void update(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        this.mRecognitionEvents.add(paramRecognitionEvent);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.internal.CombinedResultGenerator

 * JD-Core Version:    0.7.0.1

 */