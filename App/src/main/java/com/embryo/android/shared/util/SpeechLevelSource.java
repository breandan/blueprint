package com.embryo.android.shared.util;

import com.google.common.base.Preconditions;

public class SpeechLevelSource {
    private volatile Listener mListener;
    private volatile int mSpeechLevel;

    private void maybeNotify() {
        Listener localListener = this.mListener;
        if (localListener != null) {
            localListener.onSpeechLevel(this.mSpeechLevel);
        }
    }

    public synchronized void clearListener(Listener paramListener) {
            if (this.mListener == paramListener) {
                this.mListener = null;
            }
    }

    public int getSpeechLevel() {
        return this.mSpeechLevel;
    }

    public void reset() {
        setSpeechLevel(-1);
    }

    public synchronized void setListener(Listener paramListener) {
            this.mListener = paramListener;
    }

    public void setSpeechLevel(int paramInt) {
        if (((paramInt >= 0) && (paramInt <= 100)) || (paramInt == -1)) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkArgument(bool);
            this.mSpeechLevel = paramInt;
            maybeNotify();
            return;
        }
    }

    public static abstract interface Listener {
        public abstract void onSpeechLevel(int paramInt);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     SpeechLevelSource

 * JD-Core Version:    0.7.0.1

 */
