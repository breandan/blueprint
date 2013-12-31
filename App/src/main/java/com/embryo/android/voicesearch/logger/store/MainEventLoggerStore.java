package com.embryo.android.voicesearch.logger.store;

import android.os.SystemClock;

import com.google.android.search.core.EventLoggerStore;

import java.util.ArrayList;

public class MainEventLoggerStore
        implements EventLoggerStore {
    private final ArrayList<Object> mDatas = new ArrayList();
    private final ArrayList<Integer> mEvents = new ArrayList();
    private final ArrayList<Long> mTime = new ArrayList();

    public synchronized void clearResults() {
            this.mEvents.clear();
            this.mTime.clear();
            this.mDatas.clear();
    }

    public synchronized void recordEvent(int paramInt, Object paramObject) {
            long l = SystemClock.elapsedRealtime();
            this.mEvents.add(Integer.valueOf(paramInt));
            this.mTime.add(Long.valueOf(l));
            this.mDatas.add(paramObject);
    }

    public static abstract interface Results {
        public abstract Object getData(int paramInt);

        public abstract int getEvent(int paramInt);

        public abstract long getTime(int paramInt);

        public abstract int size();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     MainEventLoggerStore

 * JD-Core Version:    0.7.0.1

 */
