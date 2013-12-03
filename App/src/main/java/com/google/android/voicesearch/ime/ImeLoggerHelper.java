package com.google.android.voicesearch.ime;

import android.view.inputmethod.EditorInfo;

import com.google.android.shared.util.TextUtil;
import com.google.android.voicesearch.VoiceSearchClock;
import com.google.android.voicesearch.logger.EventLogger;

public class ImeLoggerHelper {
    private boolean mActive;
    private boolean mFirstImeRun;
    private long mStartTimestamp;
    private boolean mWaitingForResult;

    public void onDone() {
        EventLogger.recordClientEvent(39);
    }

    public void onEndSession() {
        if (this.mStartTimestamp > 0L) {
            EventLogger.recordClientEvent(40);
            this.mStartTimestamp = 0L;
        }
    }

    public void onError() {
        EventLogger.recordClientEvent(38);
    }

    public void onFinishInput() {
        if (this.mActive) {
            if (this.mWaitingForResult) {
                EventLogger.recordClientEvent(42);
            }
            EventLogger.recordClientEvent(41);
        }
        this.mActive = false;
    }

    public void onHideWindow() {
        onEndSession();
    }

    public void onInterrupt() {
        EventLogger.recordClientEvent(42);
    }

    public void onPauseRecognition() {
        EventLogger.recordClientEvent(63);
    }

    public void onRestartRecognition() {
        EventLogger.recordClientEvent(64);
    }

    public void onShowWindow() {
        this.mFirstImeRun = true;
    }

    public void onStartInputView(EditorInfo paramEditorInfo) {
        String str = "";
        if (paramEditorInfo != null) {
            str = TextUtil.safeToString(paramEditorInfo.packageName);
        }
        EventLogger.recordClientEvent(35, str);
        if (this.mFirstImeRun) {
            this.mStartTimestamp = VoiceSearchClock.elapsedRealtime();
        }
        for (; ; ) {
            this.mActive = true;
            this.mFirstImeRun = false;
            this.mWaitingForResult = false;
            return;
            EventLogger.recordClientEvent(36);
        }
    }

    public void setWaitingForResult(boolean paramBoolean) {
        this.mWaitingForResult = paramBoolean;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ime.ImeLoggerHelper

 * JD-Core Version:    0.7.0.1

 */