package com.embryo.android.voicesearch.ime;

import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import com.embryo.android.shared.util.TextUtil;
import com.embryo.android.speech.embedded.Greco3Mode;
import com.embryo.android.speech.params.SessionParams;
import com.embryo.speech.common.proto.RecognitionContextProto;

import java.util.concurrent.Executor;

public class VoiceRecognitionHandler {
    private static boolean DEBUG = false;
    private final VoiceImeInputMethodService mInputMethodService;
    private final Executor mUiThreadExecutor;
    private com.embryo.android.voicesearch.ime.VoiceInputMethodManager.DictationListener mDictationListener;
    private SessionParams mSessionParams;
    private boolean mStarted;

    public VoiceRecognitionHandler(VoiceImeInputMethodService paramVoiceImeInputMethodService, Executor paramExecutor) {
        this.mInputMethodService = paramVoiceImeInputMethodService;
        this.mUiThreadExecutor = paramExecutor;
    }

    private RecognitionContextProto.RecognitionContext createRecognitionContext() {
        EditorInfo localEditorInfo = this.mInputMethodService.getCurrentInputEditorInfo();
        if (localEditorInfo == null) {
            return null;
        }
        return new RecognitionContextProto.RecognitionContext().setLabel(TextUtil.safeToString(localEditorInfo.label)).setHint(TextUtil.safeToString(localEditorInfo.hintText)).setApplicationName(TextUtil.safeToString(localEditorInfo.packageName)).setFieldId(TextUtil.safeToString(Integer.valueOf(localEditorInfo.fieldId))).setFieldName(TextUtil.safeToString(localEditorInfo.fieldName)).setInputType(localEditorInfo.inputType).setImeOptions(localEditorInfo.imeOptions).setSingleLine(isSingleLine());
    }

    private boolean isSingleLine() {
        InputConnection ic = mInputMethodService.getCurrentInputConnection();
        if (ic == null) {
            return false;
        }
        ExtractedText et = ic.getExtractedText(new ExtractedTextRequest(), 0);
        if (et == null || (et.flags & 0x1) <= 0) {
            return false;
        }
        return true;
    }

    private boolean isSuggestionEnabled() {
        EditorInfo localEditorInfo = this.mInputMethodService.getCurrentInputEditorInfo();
        return (localEditorInfo != null) && ((0x80000 & localEditorInfo.inputType) == 0);
    }

    private void startListening(SessionParams paramSessionParams) {
        this.mSessionParams = paramSessionParams;
        this.mInputMethodService.getRecognizer().startListening(this.mSessionParams, this.mDictationListener, this.mUiThreadExecutor, null);
        this.mStarted = true;
    }

    public void cancelRecognition() {
        if (DEBUG) {
            Log.i("VoiceRecognitionHandler", "#cancelRecognition");
        }
        if (this.mStarted) {
            this.mInputMethodService.getRecognizer().cancel(this.mDictationListener);
            this.mDictationListener.invalidate();
            this.mDictationListener = null;
            this.mStarted = false;
        }
    }

    public SessionParams getSessionParams() {
        return this.mSessionParams;
    }

    public boolean isWaitingForResults() {
        return (this.mDictationListener != null) && (this.mDictationListener.isValid());
    }

    public void startRecognizer(SessionParams paramSessionParams, com.embryo.android.voicesearch.ime.VoiceInputMethodManager.DictationListener paramDictationListener) {
        if (DEBUG) {
            Log.d("VoiceRecognitionHandler", "startRecognizer");
        }
        if (this.mDictationListener != null) {
            this.mDictationListener.invalidate();
        }
        this.mDictationListener = paramDictationListener;
        startListening(paramSessionParams);
    }

    public void stopListening() {
        if (this.mStarted) {
            this.mInputMethodService.getRecognizer().stopListening(this.mDictationListener);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     VoiceRecognitionHandler

 * JD-Core Version:    0.7.0.1

 */