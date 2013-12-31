package com.embryo.android.voicesearch.ime;

import android.view.inputmethod.EditorInfo;

import com.embryo.android.shared.util.TextUtil;
import com.embryo.android.voicesearch.VoiceSearchClock;

public class ImeLoggerHelper {
    private boolean mActive;
    private boolean mFirstImeRun;
    private long mStartTimestamp;
    private boolean mWaitingForResult;

    public void onDone() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(39);
    }

    public void onEndSession() {
        if (this.mStartTimestamp > 0L) {
            com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(40);
            this.mStartTimestamp = 0L;
        }
    }

    public void onError() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(38);
    }

    public void onFinishInput() {
        if (this.mActive) {
            if (this.mWaitingForResult) {
                com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(42);
            }
            com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(41);
        }
        this.mActive = false;
    }

    public void onHideWindow() {
        onEndSession();
    }

    public void onInterrupt() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(42);
    }

    public void onPauseRecognition() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(63);
    }

    public void onRestartRecognition() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(64);
    }

    public void onShowWindow() {
        this.mFirstImeRun = true;
    }

    public void onStartInputView(EditorInfo editorInfo) {
        String triggerApplicationId = "";
        if (editorInfo != null) {
            triggerApplicationId = TextUtil.safeToString(editorInfo.packageName);
        }
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(0x23, triggerApplicationId);
        if (mFirstImeRun) {
            mStartTimestamp = VoiceSearchClock.elapsedRealtime();
        } else {
            com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(0x24);
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

 * Qualified Name:     ImeLoggerHelper

 * JD-Core Version:    0.7.0.1

 */