package com.embryo.android.voicesearch.ime;

import android.text.Spanned;
import android.util.Log;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import com.embryo.android.shared.util.ScheduledSingleThreadedExecutor;
import com.embryo.android.speech.alternates.Hypothesis;
import com.google.speech.alternates.HypothesisToSuggestionSpansConverter;
import com.embryo.android.voicesearch.ime.formatter.TextFormatter;
import com.embryo.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

public class DictationResultHandlerImpl {
    private final Runnable mCommitNewTextRunnable = new Runnable() {
        public void run() {
            DictationResultHandlerImpl.this.executeScheduledCommitNewText();
        }
    };

    private final long mDelayBetweenCommittingNewTextMsec;
    private final List<com.embryo.android.voicesearch.ime.DictationSegment> mDictationSegments;
    private final HypothesisToSuggestionSpansConverter mHypothesisToSuggestionSpansConverter;
    private final VoiceImeInputMethodService mInputMethodService;
    private final Settings mSettings;
    private final TextFormatter mTextFormatter;
    private final ScheduledSingleThreadedExecutor mUiExecutor;
    private boolean mCommitNewTextScheduled;
    private int mDictationSegmentWithNewText;
    private int mNextSegmentId;
    private String mRequestId;

    public DictationResultHandlerImpl(VoiceImeInputMethodService paramVoiceImeInputMethodService, HypothesisToSuggestionSpansConverter paramHypothesisToSuggestionSpansConverter, Settings paramSettings, TextFormatter paramTextFormatter, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor) {
        this.mInputMethodService = Preconditions.checkNotNull(paramVoiceImeInputMethodService);
        this.mUiExecutor = Preconditions.checkNotNull(paramScheduledSingleThreadedExecutor);
        this.mDictationSegments = new LinkedList();
        this.mSettings = paramSettings;
        this.mTextFormatter = paramTextFormatter;
        this.mHypothesisToSuggestionSpansConverter = paramHypothesisToSuggestionSpansConverter;
        this.mDelayBetweenCommittingNewTextMsec = this.mSettings.getConfiguration().getDictation().getDelayBetweenCommittingNewTextMsec();
        this.mNextSegmentId = -1;
    }

    private void apply(com.embryo.android.voicesearch.ime.DictationSegment.UpdateText... updateTexts) {
        Preconditions.checkNotNull(updateTexts);
        Preconditions.checkState(Thread.holdsLock(this));
        InputConnection ic = mInputMethodService.getCurrentInputConnection();
        if(ic == null) {
            return;
        }
        ic.beginBatchEdit();
        mTextFormatter.handleCommit(ic, extractText(ic));
        for(com.embryo.android.voicesearch.ime.DictationSegment.UpdateText updateText : updateTexts) {
            if(updateText != null) {
                updateText.apply(ic);
            }
        }
        ic.endBatchEdit();
    }

    private synchronized void executeScheduledCommitNewText() {
        if (this.mCommitNewTextScheduled) {
            commitNewText();
        }
    }

    private ExtractedText extractText(InputConnection ic) {
        if (ic == null) {
            return null;
        }
        ExtractedTextRequest etr = new ExtractedTextRequest();
        etr.flags = 1;
        ExtractedText et = ic.getExtractedText(etr, 0x0);
        if ((et == null) || (et.text == null) || !(et.text instanceof Spanned)) {
            return null;
        }
        return et;
    }

    private void forceCommitAllText() {
        com.embryo.android.voicesearch.ime.DictationSegment dictationSegment = null;
        while(dictationSegment != null) {
            com.embryo.android.voicesearch.ime.DictationSegment.UpdateText updateText = dictationSegment.getAllNextText();
            if(updateText != null) {
                apply(new com.embryo.android.voicesearch.ime.DictationSegment.UpdateText[] {updateText});
            }
        }
    }

    private com.embryo.android.voicesearch.ime.DictationSegment getDictationSegmentWithNewText() {
        if (mDictationSegments.size() == 0) {
            return null;
        }
        com.embryo.android.voicesearch.ime.DictationSegment dictationSegment = mDictationSegments.get(mDictationSegmentWithNewText);
        if (!dictationSegment.hasMoreText()) {
            if ((dictationSegment.isFinal()) && (mDictationSegmentWithNewText < (mDictationSegments.size() - 0x1))) {
                mDictationSegmentWithNewText = (mDictationSegmentWithNewText + 0x1);
                return dictationSegment;
            }
            return null;
        }
        return dictationSegment;
    }

    private com.embryo.android.voicesearch.ime.DictationSegment getPartialDictationSegment() {
        int i = this.mDictationSegments.size();
        com.embryo.android.voicesearch.ime.DictationSegment localDictationSegment = null;
        if (i > 0) {
            localDictationSegment = this.mDictationSegments.get(-1 + this.mDictationSegments.size());
            if (localDictationSegment.isFinal()) {
                localDictationSegment = null;
            }
        }
        if (localDictationSegment == null) {
            localDictationSegment = new com.embryo.android.voicesearch.ime.DictationSegment(this.mRequestId, this.mNextSegmentId, this.mHypothesisToSuggestionSpansConverter);
            this.mNextSegmentId = (1 + this.mNextSegmentId);
            this.mDictationSegments.add(localDictationSegment);
        }
        return localDictationSegment;
    }

    private void startDictation() {
        InputConnection ic = mInputMethodService.getCurrentInputConnection();
        if(ic == null) {
            return;
        }
        ic.beginBatchEdit();
        ic.finishComposingText();
        ExtractedText extractedText = extractText(ic);
        if(extractedText == null) {
        }
        if(((extractedText.startOffset + extractedText.selectionStart) >= 0) && (extractedText.selectionStart < extractedText.selectionEnd)) {
            Log.i("DictationResultHandlerImpl", "Removing selected text");
            ic.setSelection((extractedText.startOffset + extractedText.selectionStart), (extractedText.startOffset + extractedText.selectionStart));
            ic.deleteSurroundingText(0x0, (extractedText.selectionEnd - extractedText.selectionStart));
        }
        mTextFormatter.startDictation(extractedText);
        ic.endBatchEdit();
        ic.endBatchEdit();
    }

    protected void commitNewText() {
        mCommitNewTextScheduled = false;
        com.embryo.android.voicesearch.ime.DictationSegment dictationSegment = getDictationSegmentWithNewText();
        if(dictationSegment == null) {
            return;
        }
        com.embryo.android.voicesearch.ime.DictationSegment.UpdateText updateText = dictationSegment.getNextText();
        if(updateText != null) {
            apply(new com.embryo.android.voicesearch.ime.DictationSegment.UpdateText[] {updateText});
        }
        dictationSegment = getDictationSegmentWithNewText();
        if(dictationSegment != null) {
            mCommitNewTextScheduled = true;
            mUiExecutor.cancelExecute(mCommitNewTextRunnable);
            mUiExecutor.executeDelayed(mCommitNewTextRunnable, mDelayBetweenCommittingNewTextMsec);
        }
    }

    public synchronized void handleError() {
        forceCommitAllText();
    }

    public synchronized void handlePartialRecognitionResult(String paramString) {
        String str = this.mTextFormatter.format(paramString);
        com.embryo.android.voicesearch.ime.DictationSegment.UpdateText localUpdateText = getPartialDictationSegment().updatePartial(str);
        if (localUpdateText != null) {
            apply(new com.embryo.android.voicesearch.ime.DictationSegment.UpdateText[]{localUpdateText});
        }
        if (!this.mCommitNewTextScheduled) {
            commitNewText();
        }
    }

    public synchronized void handleRecognitionResult(Hypothesis paramHypothesis, @Nullable String paramString) {
        Hypothesis localHypothesis = this.mTextFormatter.format(paramHypothesis);
        this.mTextFormatter.reset();
        com.embryo.android.voicesearch.ime.DictationSegment localDictationSegment = getPartialDictationSegment();
        com.embryo.android.voicesearch.ime.DictationSegment.UpdateText localUpdateText1 = localDictationSegment.updateFinal(localHypothesis);
        com.embryo.android.voicesearch.ime.DictationSegment.UpdateText localUpdateText2 = null;
        if (paramString != null) {
            localUpdateText2 = localDictationSegment.updatePartial(paramString);
        }
        if (localUpdateText1 != null) {
            apply(new com.embryo.android.voicesearch.ime.DictationSegment.UpdateText[]{localUpdateText1, localUpdateText2});
        }
        if (!this.mCommitNewTextScheduled) {
            commitNewText();
        }
    }

    public synchronized void handleStop() {
        forceCommitAllText();
    }

    public synchronized void init(String paramString) {
        this.mRequestId = paramString;
        this.mNextSegmentId = 0;
        startDictation();
    }

    public synchronized void reset() {
        this.mNextSegmentId = -1;
        this.mCommitNewTextScheduled = false;
        this.mUiExecutor.cancelExecute(this.mCommitNewTextRunnable);
    }
}