package com.google.android.voicesearch.ime;

import android.os.SystemClock;

import com.google.android.shared.util.ExtraPreconditions;

public class TemporaryData<DATA> {
    private DATA mData;
    private long mExpiredTimestamp;
    private final ExtraPreconditions.ThreadCheck mSameThread = ExtraPreconditions.createSameThreadCheck();

    public TemporaryData(DATA paramDATA) {
        this.mData = paramDATA;
        this.mExpiredTimestamp = 0L;
    }

    public void extend() {
        this.mSameThread.check();
        this.mExpiredTimestamp = (500L + SystemClock.elapsedRealtime());
    }

    public void forceExpire() {
        this.mSameThread.check();
        this.mExpiredTimestamp = 0L;
    }

    public DATA getData() {
        this.mSameThread.check();
        return this.mData;
    }

    public boolean isExpired() {
        this.mSameThread.check();
        return SystemClock.elapsedRealtime() > this.mExpiredTimestamp;
    }

    public void setData(DATA paramDATA) {
        this.mSameThread.check();
        this.mData = paramDATA;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ime.TemporaryData

 * JD-Core Version:    0.7.0.1

 */