package com.google.android.voicesearch.ime;

import android.text.Spanned;
import android.util.Log;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.speech.alternates.Hypothesis;
import com.google.android.voicesearch.ime.formatter.TextFormatter;
import com.google.android.voicesearch.settings.Settings;
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
    private final List<DictationSegment> mDictationSegments;
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

    private void apply(DictationSegment.UpdateText... updateTexts) {
        Preconditions.checkNotNull(updateTexts);
        Preconditions.checkState(Thread.holdsLock(this));
        InputConnection ic = mInputMethodService.getCurrentInputConnection();
        if(ic == null) {
            return;
        }
        ic.beginBatchEdit();
        mTextFormatter.handleCommit(ic, extractText(ic));
        for(DictationSegment.UpdateText updateText : updateTexts) {
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
        DictationSegment dictationSegment = null;
        while(dictationSegment != null) {
            DictationSegment.UpdateText updateText = dictationSegment.getAllNextText();
            if(updateText != null) {
                apply(new DictationSegment.UpdateText[] {updateText});
            }
        }
    }

    private DictationSegment getDictationSegmentWithNewText() {
        if (mDictationSegments.size() == 0) {
            return null;
        }
        DictationSegment dictationSegment = (DictationSegment) mDictationSegments.get(mDictationSegmentWithNewText);
        if (!dictationSegment.hasMoreText()) {
            if ((dictationSegment.isFinal()) && (mDictationSegmentWithNewText < (mDictationSegments.size() - 0x1))) {
                mDictationSegmentWithNewText = (mDictationSegmentWithNewText + 0x1);
                return dictationSegment;
            }
            return null;
        }
        return dictationSegment;
    }

    private DictationSegment getPartialDictationSegment() {
        int i = this.mDictationSegments.size();
        DictationSegment localDictationSegment = null;
        if (i > 0) {
            localDictationSegment = this.mDictationSegments.get(-1 + this.mDictationSegments.size());
            if (localDictationSegment.isFinal()) {
                localDictationSegment = null;
            }
        }
        if (localDictationSegment == null) {
            localDictationSegment = new DictationSegment(this.mRequestId, this.mNextSegmentId, this.mHypothesisToSuggestionSpansConverter);
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
        DictationSegment dictationSegment = getDictationSegmentWithNewText();
        if(dictationSegment == null) {
            return;
        }
        DictationSegment.UpdateText updateText = dictationSegment.getNextText();
        if(updateText != null) {
            apply(new DictationSegment.UpdateText[] {updateText});
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
        DictationSegment.UpdateText localUpdateText = getPartialDictationSegment().updatePartial(str);
        if (localUpdateText != null) {
            apply(new DictationSegment.UpdateText[]{localUpdateText});
        }
        if (!this.mCommitNewTextScheduled) {
            commitNewText();
        }
    }

    public synchronized void handleRecognitionResult(Hypothesis paramHypothesis, @Nullable String paramString) {
        Hypothesis localHypothesis = this.mTextFormatter.format(paramHypothesis);
        this.mTextFormatter.reset();
        DictationSegment localDictationSegment = getPartialDictationSegment();
        DictationSegment.UpdateText localUpdateText1 = localDictationSegment.updateFinal(localHypothesis);
        DictationSegment.UpdateText localUpdateText2 = null;
        if (paramString != null) {
            localUpdateText2 = localDictationSegment.updatePartial(paramString);
        }
        if (localUpdateText1 != null) {
            apply(new DictationSegment.UpdateText[]{localUpdateText1, localUpdateText2});
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