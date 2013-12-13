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

    public void onStartInputView(EditorInfo editorInfo) {
        String triggerApplicationId = "";
        if (editorInfo != null) {
            triggerApplicationId = TextUtil.safeToString(editorInfo.packageName);
        }
        EventLogger.recordClientEvent(0x23, triggerApplicationId);
        if (mFirstImeRun) {
            mStartTimestamp = VoiceSearchClock.elapsedRealtime();
        } else {
            EventLogger.recordClientEvent(0x24);
        }
        mActive = true;
        mFirstImeRun = false;
        mWaitingForResult = false;
    }

    public void setWaitingForResult(boolean paramBoolean) {
        this.mWaitingForResult = paramBoolean;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ime.ImeLoggerHelper

 * JD-Core Version:    0.7.0.1

 */