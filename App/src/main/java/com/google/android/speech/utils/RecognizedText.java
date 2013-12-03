package com.google.android.speech.utils;

import android.util.Pair;

import com.google.common.collect.ImmutableList;

import java.util.List;

import javax.annotation.Nullable;

public class RecognizedText {
    @Nullable
    private ImmutableList<Hypothesis> mAllHypotheses;
    private final StringBuilder mStable = new StringBuilder();

    private ImmutableList<Hypothesis> extractHypotheses(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        ImmutableList.Builder localBuilder = ImmutableList.builder();
        if (paramRecognitionEvent.hasCombinedResult()) {
            RecognizerProtos.RecognitionResult localRecognitionResult = paramRecognitionEvent.getCombinedResult();
            for (int i = 0; i < localRecognitionResult.getHypothesisCount(); i++) {
                localBuilder.add(extractHypothesis(localRecognitionResult.getHypothesis(i)));
            }
        }
        return localBuilder.build();
    }

    private Hypothesis extractHypothesis(RecognizerProtos.Hypothesis paramHypothesis) {
        String str;
        if (paramHypothesis.hasText()) {
            str = paramHypothesis.getText();
            if (!paramHypothesis.hasAlternates()) {
                break label39;
            }
        }
        label39:
        for (List localList = paramHypothesis.getAlternates().getSpanList(); ; localList = null) {
            return Hypothesis.fromAlternateSpanProtos(str, localList);
            str = "";
            break;
        }
    }

    private void updateStableResults(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        if (paramRecognitionEvent.hasResult()) {
            RecognizerProtos.RecognitionResult localRecognitionResult = paramRecognitionEvent.getResult();
            if (localRecognitionResult.getHypothesisCount() > 0) {
                RecognizerProtos.Hypothesis localHypothesis = localRecognitionResult.getHypothesis(0);
                if (localHypothesis.hasText()) {
                    this.mStable.append(localHypothesis.getText());
                }
            }
        }
    }

    public String getStableForErrorReporting() {
        return this.mStable.toString();
    }

    public boolean hasCompletedRecognition() {
        return this.mAllHypotheses != null;
    }

    public ImmutableList<Hypothesis> updateFinal(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        this.mAllHypotheses = extractHypotheses(paramRecognitionEvent);
        this.mStable.delete(0, this.mStable.length());
        if (this.mAllHypotheses.size() > 0) {
            this.mStable.append(((Hypothesis) this.mAllHypotheses.get(0)).getText());
        }
        return this.mAllHypotheses;
    }

    public Pair<String, String> updateInProgress(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        updateStableResults(paramRecognitionEvent);
        boolean bool = paramRecognitionEvent.hasPartialResult();
        StringBuilder localStringBuilder1 = null;
        StringBuilder localStringBuilder2 = null;
        if (bool) {
            int i = 0;
            RecognizerProtos.PartialResult localPartialResult = paramRecognitionEvent.getPartialResult();
            int j = localPartialResult.getPartCount();
            int k = 0;
            if (k < j) {
                RecognizerProtos.PartialPart localPartialPart = localPartialResult.getPart(k);
                if (localPartialPart.hasText()) {
                    if ((i != 0) || (!localPartialPart.hasStability()) || (localPartialPart.getStability() < 0.9D)) {
                        break label119;
                    }
                    if (localStringBuilder1 == null) {
                        localStringBuilder1 = new StringBuilder(this.mStable);
                    }
                    localStringBuilder1.append(localPartialPart.getText());
                }
                for (; ; ) {
                    k++;
                    break;
                    label119:
                    if (localStringBuilder2 == null) {
                        localStringBuilder2 = new StringBuilder();
                    }
                    localStringBuilder2.append(localPartialPart.getText());
                    i = 1;
                }
            }
        }
        String str1;
        if (localStringBuilder1 == null) {
            str1 = this.mStable.toString();
            if (localStringBuilder2 != null) {
                break label189;
            }
        }
        label189:
        for (String str2 = ""; ; str2 = localStringBuilder2.toString()) {
            return Pair.create(str1, str2);
            str1 = localStringBuilder1.toString();
            break;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.utils.RecognizedText

 * JD-Core Version:    0.7.0.1

 */