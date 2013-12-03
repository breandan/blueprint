package com.google.android.voicesearch.ime;

import android.content.res.Configuration;
import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.google.android.voicesearch.logger.EventLoggerService;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class VoiceInputMethodService
        extends InputMethodService {
    private VoiceInputMethodManager mVoiceInputManager;

    protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
        super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        this.mVoiceInputManager.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }

    public void hideWindow() {
        this.mVoiceInputManager.handleHideWindow();
        super.hideWindow();
    }

    public void onConfigurationChanged(Configuration paramConfiguration) {
        this.mVoiceInputManager.handleConfigurationChanged(paramConfiguration);
        super.onConfigurationChanged(paramConfiguration);
    }

    public void onCreate() {
        super.onCreate();
        this.mVoiceInputManager = VoiceInputMethodManager.create(this);
    }

    public View onCreateInputView() {
        return this.mVoiceInputManager.handleCreateInputView();
    }

    public void onDestroy() {
        this.mVoiceInputManager.handleDestroy();
        super.onDestroy();
    }

    public boolean onEvaluateFullscreenMode() {
        if (this.mVoiceInputManager.isMaybeForceFullScreen()) {
            return super.onEvaluateFullscreenMode();
        }
        return false;
    }

    public void onFinishInput() {
        this.mVoiceInputManager.handleFinishInput();
        super.onFinishInput();
    }

    public void onFinishInputView(boolean paramBoolean) {
        this.mVoiceInputManager.handleFinishInputView(paramBoolean);
        super.onFinishInputView(paramBoolean);
    }

    public void onStartInputView(EditorInfo paramEditorInfo, boolean paramBoolean) {
        super.onStartInputView(paramEditorInfo, paramBoolean);
        EventLoggerService.cancelSendEvents(this);
        this.mVoiceInputManager.handleStartInputView(paramEditorInfo, paramBoolean);
    }

    public void onUpdateSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        super.onUpdateSelection(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
        this.mVoiceInputManager.handleUpdateSelection(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
    }

    public void onViewClicked(boolean paramBoolean) {
        this.mVoiceInputManager.handleViewClicked(paramBoolean);
    }

    public void showWindow(boolean paramBoolean) {
        this.mVoiceInputManager.handleShowWindow(paramBoolean);
        super.showWindow(paramBoolean);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ime.VoiceInputMethodService

 * JD-Core Version:    0.7.0.1

 */