package com.embryo.android.voicesearch.ime;

import android.text.SpannableString;
import android.text.TextUtils;
import android.view.inputmethod.InputConnection;


import com.embryo.android.speech.alternates.HypothesisToSuggestionSpansConverter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DictationSegment {
    private final HypothesisToSuggestionSpansConverter mHypothesisToSuggestionSpansConverter;
    private final String mRequestId;
    private final int mSegmentId;
    private int mDisplayLength = 0;
    @Nullable
    private SpannableString mFinalTextSpannable;
    private CharSequence mText;

    public DictationSegment(String paramString, int paramInt, HypothesisToSuggestionSpansConverter paramHypothesisToSuggestionSpansConverter) {
        this.mRequestId = paramString;
        this.mSegmentId = paramInt;
        this.mHypothesisToSuggestionSpansConverter = paramHypothesisToSuggestionSpansConverter;
    }

    private static int getCommonStart(CharSequence paramCharSequence1, CharSequence paramCharSequence2, int paramInt) {
        if ((paramCharSequence1 == null) || (paramCharSequence2 == null)) {

        }
        int j = Math.min(paramInt, paramCharSequence2.length());
        for (int i = 0;(i < j) || paramCharSequence1.charAt(i) != paramCharSequence2.charAt(i); i++) {

        }
        return j;
    }

    private static int getNextSplitPos(CharSequence paramCharSequence, int paramInt) {
        int i = TextUtils.indexOf(paramCharSequence, " ", paramInt);
        if (i == -1) {
            return paramCharSequence.length();
        }
        return Math.min(i, paramCharSequence.length());
    }

    private CharSequence getFinalText() {
        if (!isFinal()) {
            return this.mText;
        }
        return this.mFinalTextSpannable;
    }

    private UpdateText updateText(CharSequence paramCharSequence) {
        int i = this.mDisplayLength - getCommonStart(this.mText, paramCharSequence, this.mDisplayLength);
        this.mText = paramCharSequence;
        if (i == 0) {
            return null;
        }
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(74);
        int j = Math.min(this.mDisplayLength, paramCharSequence.length());
        if ((j - 1 >= 0) && (paramCharSequence.charAt(j - 1) != ' ')) {
            j = getNextSplitPos(paramCharSequence, this.mDisplayLength);
        }
        UpdateText localUpdateText = new UpdateText(i, paramCharSequence.subSequence(this.mDisplayLength - i, j));
        this.mDisplayLength = j;
        return localUpdateText;
    }

    public UpdateText getAllNextText() {
        if (hasMoreText()) {
            UpdateText localUpdateText = new UpdateText(this.mDisplayLength, getFinalText());
            this.mDisplayLength = getFinalText().length();
            return localUpdateText;
        }
        return null;
    }

    public UpdateText getNextText() {
        int i = getNextSplitPos(this.mText, 1 + this.mDisplayLength);
        if ((i == this.mText.length()) && (isFinal())) {
            UpdateText localUpdateText = new UpdateText(this.mDisplayLength, getFinalText());
            this.mDisplayLength = this.mText.length();
            return localUpdateText;
        }
        CharSequence localCharSequence = this.mText.subSequence(this.mDisplayLength, i);
        this.mDisplayLength = i;
        return new UpdateText(0, localCharSequence);
    }

    public boolean hasMoreText() {
        return this.mDisplayLength < this.mText.length();
    }

    public boolean isFinal() {
        return this.mFinalTextSpannable != null;
    }

    public UpdateText updateFinal(@Nonnull com.embryo.android.speech.alternates.Hypothesis paramHypothesis) {
        this.mFinalTextSpannable = this.mHypothesisToSuggestionSpansConverter.getSuggestionSpannableString(this.mRequestId, this.mSegmentId, paramHypothesis);
        UpdateText localUpdateText = updateText(this.mFinalTextSpannable);

        for (int i = localUpdateText.mDeleteBeforeCursor + this.mDisplayLength - localUpdateText.mNewText.length(); ; i = this.mDisplayLength) {
            if (this.mDisplayLength == this.mText.length()) {
                if (localUpdateText == null) {
                    break;
                }
            }
            localUpdateText = new UpdateText(i, getFinalText());
        }

        return localUpdateText;
    }

    public UpdateText updatePartial(CharSequence paramCharSequence) {
        return updateText(paramCharSequence);
    }

    public static class UpdateText {
        private final int mDeleteBeforeCursor;
        private final CharSequence mNewText;

        public UpdateText(int paramInt, CharSequence paramCharSequence) {
            this.mDeleteBeforeCursor = paramInt;
            this.mNewText = paramCharSequence;
        }

        public void apply(InputConnection paramInputConnection) {
            if (paramInputConnection == null) {
                return;
            }
            do {
                if (this.mDeleteBeforeCursor > 0) {
                    paramInputConnection.deleteSurroundingText(this.mDeleteBeforeCursor, 0);
                }
            } while (this.mNewText.length() <= 0);
            paramInputConnection.commitText(this.mNewText, 1);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     DictationSegment

 * JD-Core Version:    0.7.0.1

 */