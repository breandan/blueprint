package com.google.android.voicesearch.ime;

import android.text.Spanned;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.speech.alternates.Hypothesis;
import com.google.android.speech.alternates.HypothesisToSuggestionSpansConverter;
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
    private boolean mCommitNewTextScheduled;
    private final long mDelayBetweenCommittingNewTextMsec;
    private int mDictationSegmentWithNewText;
    private final List<DictationSegment> mDictationSegments;
    private final HypothesisToSuggestionSpansConverter mHypothesisToSuggestionSpansConverter;
    private final VoiceImeInputMethodService mInputMethodService;
    private int mNextSegmentId;
    private String mRequestId;
    private final Settings mSettings;
    private final TextFormatter mTextFormatter;
    private final ScheduledSingleThreadedExecutor mUiExecutor;

    public DictationResultHandlerImpl(VoiceImeInputMethodService paramVoiceImeInputMethodService, HypothesisToSuggestionSpansConverter paramHypothesisToSuggestionSpansConverter, Settings paramSettings, TextFormatter paramTextFormatter, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor) {
        this.mInputMethodService = ((VoiceImeInputMethodService) Preconditions.checkNotNull(paramVoiceImeInputMethodService));
        this.mUiExecutor = ((ScheduledSingleThreadedExecutor) Preconditions.checkNotNull(paramScheduledSingleThreadedExecutor));
        this.mDictationSegments = new LinkedList();
        this.mSettings = paramSettings;
        this.mTextFormatter = paramTextFormatter;
        this.mHypothesisToSuggestionSpansConverter = paramHypothesisToSuggestionSpansConverter;
        this.mDelayBetweenCommittingNewTextMsec = this.mSettings.getConfiguration().getDictation().getDelayBetweenCommittingNewTextMsec();
        this.mNextSegmentId = -1;
    }

    private void apply(DictationSegment.UpdateText... paramVarArgs) {
        Preconditions.checkNotNull(paramVarArgs);
        Preconditions.checkState(Thread.holdsLock(this));
        InputConnection localInputConnection = this.mInputMethodService.getCurrentInputConnection();
        if (localInputConnection == null) {
            return;
        }
        localInputConnection.beginBatchEdit();
        try {
            this.mTextFormatter.handleCommit(localInputConnection, extractText(localInputConnection));
            int i = paramVarArgs.length;
            for (int j = 0; j < i; j++) {
                DictationSegment.UpdateText localUpdateText = paramVarArgs[j];
                if (localUpdateText != null) {
                    localUpdateText.apply(localInputConnection);
                }
            }
            return;
        } finally {
            localInputConnection.endBatchEdit();
        }
    }

    private void executeScheduledCommitNewText() {
        try {
            if (this.mCommitNewTextScheduled) {
                commitNewText();
            }
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    private ExtractedText extractText(InputConnection paramInputConnection) {
        ExtractedText localExtractedText;
        if (paramInputConnection == null) {
            localExtractedText = null;
        }
        do {
            return localExtractedText;
            ExtractedTextRequest localExtractedTextRequest = new ExtractedTextRequest();
            localExtractedTextRequest.flags = 1;
            localExtractedText = paramInputConnection.getExtractedText(localExtractedTextRequest, 0);
        }
        while ((localExtractedText != null) && (localExtractedText.text != null) && ((localExtractedText.text instanceof Spanned)));
        return null;
    }

    private void forceCommitAllText() {
        for (; ; ) {
            DictationSegment localDictationSegment = getDictationSegmentWithNewText();
            if (localDictationSegment == null) {
                break;
            }
            DictationSegment.UpdateText localUpdateText = localDictationSegment.getAllNextText();
            if (localUpdateText != null) {
                apply(new DictationSegment.UpdateText[]{localUpdateText});
            }
        }
    }

    private DictationSegment getDictationSegmentWithNewText() {
        DictationSegment localDictationSegment;
        if (this.mDictationSegments.size() == 0) {
            localDictationSegment = null;
        }
        do {
            return localDictationSegment;
            localDictationSegment = (DictationSegment) this.mDictationSegments.get(this.mDictationSegmentWithNewText);
        } while (localDictationSegment.hasMoreText());
        if ((localDictationSegment.isFinal()) && (this.mDictationSegmentWithNewText < -1 + this.mDictationSegments.size())) {
            this.mDictationSegmentWithNewText = (1 + this.mDictationSegmentWithNewText);
            return getDictationSegmentWithNewText();
        }
        return null;
    }

    private DictationSegment getPartialDictationSegment() {
        int i = this.mDictationSegments.size();
        DictationSegment localDictationSegment = null;
        if (i > 0) {
            localDictationSegment = (DictationSegment) this.mDictationSegments.get(-1 + this.mDictationSegments.size());
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

    /* Error */
    private void startDictation() {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 50	com/google/android/voicesearch/ime/DictationResultHandlerImpl:mInputMethodService	Lcom/google/android/voicesearch/ime/VoiceImeInputMethodService;
        //   6: invokeinterface 107 1 0
        //   11: astore_2
        //   12: aload_2
        //   13: ifnonnull +6 -> 19
        //   16: aload_0
        //   17: monitorexit
        //   18: return
        //   19: aload_2
        //   20: invokeinterface 113 1 0
        //   25: pop
        //   26: aload_2
        //   27: invokeinterface 197 1 0
        //   32: pop
        //   33: aload_0
        //   34: aload_2
        //   35: invokespecial 117	com/google/android/voicesearch/ime/DictationResultHandlerImpl:extractText	(Landroid/view/inputmethod/InputConnection;)Landroid/view/inputmethod/ExtractedText;
        //   38: astore 7
        //   40: aload 7
        //   42: ifnonnull +18 -> 60
        //   45: aload_2
        //   46: invokeinterface 131 1 0
        //   51: pop
        //   52: goto -36 -> 16
        //   55: astore_1
        //   56: aload_0
        //   57: monitorexit
        //   58: aload_1
        //   59: athrow
        //   60: aload 7
        //   62: getfield 200	android/view/inputmethod/ExtractedText:startOffset	I
        //   65: aload 7
        //   67: getfield 203	android/view/inputmethod/ExtractedText:selectionStart	I
        //   70: iadd
        //   71: iflt +72 -> 143
        //   74: aload 7
        //   76: getfield 203	android/view/inputmethod/ExtractedText:selectionStart	I
        //   79: aload 7
        //   81: getfield 206	android/view/inputmethod/ExtractedText:selectionEnd	I
        //   84: if_icmpge +59 -> 143
        //   87: ldc 208
        //   89: ldc 210
        //   91: invokestatic 216	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
        //   94: pop
        //   95: aload_2
        //   96: aload 7
        //   98: getfield 200	android/view/inputmethod/ExtractedText:startOffset	I
        //   101: aload 7
        //   103: getfield 203	android/view/inputmethod/ExtractedText:selectionStart	I
        //   106: iadd
        //   107: aload 7
        //   109: getfield 200	android/view/inputmethod/ExtractedText:startOffset	I
        //   112: aload 7
        //   114: getfield 203	android/view/inputmethod/ExtractedText:selectionStart	I
        //   117: iadd
        //   118: invokeinterface 220 3 0
        //   123: pop
        //   124: aload_2
        //   125: iconst_0
        //   126: aload 7
        //   128: getfield 206	android/view/inputmethod/ExtractedText:selectionEnd	I
        //   131: aload 7
        //   133: getfield 203	android/view/inputmethod/ExtractedText:selectionStart	I
        //   136: isub
        //   137: invokeinterface 223 3 0
        //   142: pop
        //   143: aload_0
        //   144: getfield 63	com/google/android/voicesearch/ime/DictationResultHandlerImpl:mTextFormatter	Lcom/google/android/voicesearch/ime/formatter/TextFormatter;
        //   147: aload 7
        //   149: invokeinterface 226 2 0
        //   154: aload_2
        //   155: invokeinterface 131 1 0
        //   160: pop
        //   161: goto -145 -> 16
        //   164: astore 4
        //   166: aload_2
        //   167: invokeinterface 131 1 0
        //   172: pop
        //   173: aload 4
        //   175: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	176	0	this	DictationResultHandlerImpl
        //   55	4	1	localObject1	Object
        //   11	156	2	localInputConnection	InputConnection
        //   164	10	4	localObject2	Object
        //   38	110	7	localExtractedText	ExtractedText
        // Exception table:
        //   from	to	target	type
        //   2	12	55	finally
        //   19	26	55	finally
        //   45	52	55	finally
        //   154	161	55	finally
        //   166	176	55	finally
        //   26	40	164	finally
        //   60	143	164	finally
        //   143	154	164	finally
    }

    /* Error */
    protected void commitNewText() {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: iconst_0
        //   4: putfield 133	com/google/android/voicesearch/ime/DictationResultHandlerImpl:mCommitNewTextScheduled	Z
        //   7: aload_0
        //   8: invokespecial 159	com/google/android/voicesearch/ime/DictationResultHandlerImpl:getDictationSegmentWithNewText	()Lcom/google/android/voicesearch/ime/DictationSegment;
        //   11: astore_2
        //   12: aload_2
        //   13: ifnonnull +6 -> 19
        //   16: aload_0
        //   17: monitorexit
        //   18: return
        //   19: aload_2
        //   20: invokevirtual 229	com/google/android/voicesearch/ime/DictationSegment:getNextText	()Lcom/google/android/voicesearch/ime/DictationSegment$UpdateText;
        //   23: astore_3
        //   24: aload_3
        //   25: ifnull +15 -> 40
        //   28: aload_0
        //   29: iconst_1
        //   30: anewarray 125	com/google/android/voicesearch/ime/DictationSegment$UpdateText
        //   33: dup
        //   34: iconst_0
        //   35: aload_3
        //   36: aastore
        //   37: invokespecial 167	com/google/android/voicesearch/ime/DictationResultHandlerImpl:apply	([Lcom/google/android/voicesearch/ime/DictationSegment$UpdateText;)V
        //   40: aload_0
        //   41: invokespecial 159	com/google/android/voicesearch/ime/DictationResultHandlerImpl:getDictationSegmentWithNewText	()Lcom/google/android/voicesearch/ime/DictationSegment;
        //   44: ifnull -28 -> 16
        //   47: aload_0
        //   48: iconst_1
        //   49: putfield 133	com/google/android/voicesearch/ime/DictationResultHandlerImpl:mCommitNewTextScheduled	Z
        //   52: aload_0
        //   53: getfield 54	com/google/android/voicesearch/ime/DictationResultHandlerImpl:mUiExecutor	Lcom/google/android/shared/util/ScheduledSingleThreadedExecutor;
        //   56: aload_0
        //   57: getfield 40	com/google/android/voicesearch/ime/DictationResultHandlerImpl:mCommitNewTextRunnable	Ljava/lang/Runnable;
        //   60: invokeinterface 233 2 0
        //   65: aload_0
        //   66: getfield 54	com/google/android/voicesearch/ime/DictationResultHandlerImpl:mUiExecutor	Lcom/google/android/shared/util/ScheduledSingleThreadedExecutor;
        //   69: aload_0
        //   70: getfield 40	com/google/android/voicesearch/ime/DictationResultHandlerImpl:mCommitNewTextRunnable	Ljava/lang/Runnable;
        //   73: aload_0
        //   74: getfield 85	com/google/android/voicesearch/ime/DictationResultHandlerImpl:mDelayBetweenCommittingNewTextMsec	J
        //   77: invokeinterface 237 4 0
        //   82: goto -66 -> 16
        //   85: astore_1
        //   86: aload_0
        //   87: monitorexit
        //   88: aload_1
        //   89: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	90	0	this	DictationResultHandlerImpl
        //   85	4	1	localObject	Object
        //   11	9	2	localDictationSegment	DictationSegment
        //   23	13	3	localUpdateText	DictationSegment.UpdateText
        // Exception table:
        //   from	to	target	type
        //   2	12	85	finally
        //   19	24	85	finally
        //   28	40	85	finally
        //   40	82	85	finally
    }

    public void handleError() {
        try {
            forceCommitAllText();
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    public void handlePartialRecognitionResult(String paramString) {
        try {
            String str = this.mTextFormatter.format(paramString);
            DictationSegment.UpdateText localUpdateText = getPartialDictationSegment().updatePartial(str);
            if (localUpdateText != null) {
                apply(new DictationSegment.UpdateText[]{localUpdateText});
            }
            if (!this.mCommitNewTextScheduled) {
                commitNewText();
            }
            return;
        } finally {
        }
    }

    public void handleRecognitionResult(Hypothesis paramHypothesis, @Nullable String paramString) {
        try {
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
            return;
        } finally {
        }
    }

    public void handleStop() {
        try {
            forceCommitAllText();
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    public void init(String paramString) {
        try {
            this.mRequestId = paramString;
            this.mNextSegmentId = 0;
            startDictation();
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    public void reset() {
        try {
            this.mNextSegmentId = -1;
            this.mCommitNewTextScheduled = false;
            this.mUiExecutor.cancelExecute(this.mCommitNewTextRunnable);
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ime.DictationResultHandlerImpl

 * JD-Core Version:    0.7.0.1

 */