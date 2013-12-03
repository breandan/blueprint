package com.google.android.voicesearch.ime;

import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import com.google.android.shared.util.TextUtil;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.speech.params.SessionParams;

import java.util.concurrent.Executor;

public class VoiceRecognitionHandler {
    private static boolean DEBUG = false;
    private VoiceInputMethodManager.DictationListener mDictationListener;
    private final VoiceImeInputMethodService mInputMethodService;
    private SessionParams mSessionParams;
    private boolean mStarted;
    private final Executor mUiThreadExecutor;

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
        InputConnection localInputConnection = this.mInputMethodService.getCurrentInputConnection();
        if (localInputConnection == null) {
        }
        ExtractedText localExtractedText;
        do {
            return false;
            localExtractedText = localInputConnection.getExtractedText(new ExtractedTextRequest(), 0);
        } while ((localExtractedText == null) || ((0x1 & localExtractedText.flags) <= 0));
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

    public SessionParams createSessionParams(String paramString, boolean paramBoolean) {
        return new SessionParams.Builder().setSpokenBcp47Locale(paramString).setProfanityFilterEnabled(paramBoolean).setRecognitionContext(createRecognitionContext()).setMode(3).setStopOnEndOfSpeech(false).setGreco3Mode(Greco3Mode.DICTATION).setSuggestionsEnabled(isSuggestionEnabled()).build();
    }

    public SessionParams getSessionParams() {
        return this.mSessionParams;
    }

    public boolean isWaitingForResults() {
        return (this.mDictationListener != null) && (this.mDictationListener.isValid());
    }

    public void startRecognizer(SessionParams paramSessionParams, VoiceInputMethodManager.DictationListener paramDictationListener) {
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

 * Qualified Name:     com.google.android.voicesearch.ime.VoiceRecognitionHandler

 * JD-Core Version:    0.7.0.1

 */