package com.google.android.speech.utils;

import android.util.Pair;

import com.google.android.speech.alternates.Hypothesis;
import com.google.common.collect.ImmutableList;
import com.google.speech.common.Alternates;
import com.google.speech.recognizer.api.RecognizerProtos;

import javax.annotation.Nullable;

public class RecognizedText {
    private final StringBuilder mStable = new StringBuilder();
    @Nullable
    private ImmutableList<Hypothesis> mAllHypotheses;

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

    private Hypothesis extractHypothesis(RecognizerProtos.Hypothesis hypothesis) {
        String text = hypothesis.hasText() ? hypothesis.getText() : "";
        Iterable<Alternates.AlternateSpan> spans = hypothesis.hasAlternates() ? hypothesis.getAlternates().getSpanList() : null;
        return Hypothesis.fromAlternateSpanProtos(text, spans);
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
            this.mStable.append(this.mAllHypotheses.get(0).getText());
        }
        return this.mAllHypotheses;
    }

    public Pair<String, String> updateInProgress(RecognizerProtos.RecognitionEvent event) {
        updateStableResults(event);
        StringBuilder lowConfidence = null;
        StringBuilder highConfidence = null;
        if (event.hasPartialResult()) {
            boolean foundUnstable = false;
            RecognizerProtos.PartialResult partialResult = event.getPartialResult();
            int partCount = partialResult.getPartCount();
            for (int i = 0x0; i < partCount; i = i + 0x1) {
                RecognizerProtos.PartialPart partialPart = partialResult.getPart(i);
                if (partialPart.hasText()) {
                    if ((!foundUnstable) && (partialPart.hasStability()) && (partialPart.getStability() >= 0.9)) {
                        if (highConfidence == null) {
                        }
                        highConfidence.append(partialPart.getText());
                    } else if (lowConfidence == null) {
                    }
                    lowConfidence.append(partialPart.getText());
                    foundUnstable = true;
                }
            }
        }
        Pair.create(highConfidence.toString(), lowConfidence != null ? highConfidence.toString() : lowConfidence.toString());
        return Pair.create(highConfidence.toString(), lowConfidence != null ? highConfidence.toString() : lowConfidence.toString());
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.utils.RecognizedText

 * JD-Core Version:    0.7.0.1

 */