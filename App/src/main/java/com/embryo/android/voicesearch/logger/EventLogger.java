package com.embryo.android.voicesearch.logger;

import android.util.SparseArray;

import com.google.android.search.core.EventLoggerStore;
import com.embryo.android.voicesearch.logger.store.EventLoggerStores;
import com.google.common.base.Preconditions;

public final class EventLogger {
    private static volatile EventLoggerStore sEventLoggerStore;
    private static SparseArray<Boolean> sOneOffEvents = new SparseArray();

    public static EventLoggerStore getStore() {
        return sEventLoggerStore;
    }

    public static void setStore(EventLoggerStore paramEventLoggerStore) {
        sEventLoggerStore = Preconditions.checkNotNull(paramEventLoggerStore);
    }

    public static void init() {
        setStore(EventLoggerStores.createEventStore());
    }

    public static void logTextSearchStart(String paramString) {
        recordSpeechEvent(9);
        recordSpeechEvent(3, paramString);
        recordClientEvent(19);
    }

    private static void record(int group, int source, int event, Object data, boolean oneOff) {
        Preconditions.checkArgument((0xfffffff & group) != 0);
        Preconditions.checkArgument((-0xf000001 & source) != 0);
        Preconditions.checkArgument((-0x1000000 & event) != 0);
        int wholeEvent = (group | source) | event;
        if ((oneOff) && (!shouldLog(wholeEvent))) {
            return;
        }
        sEventLoggerStore.recordEvent(wholeEvent, data);
    }

    public static void recordBreakdownEvent(int paramInt) {
        record(1342177280, 0, paramInt, null, false);
    }

    public static void recordBreakdownEvent(int paramInt, Object paramObject) {
        record(1342177280, 0, paramInt, paramObject, false);
    }

    public static void recordClientEvent(int paramInt) {
        recordClientEvent(paramInt, null);
    }

    public static void recordClientEvent(int paramInt, Object paramObject) {
        record(268435456, 0, paramInt, paramObject, false);
    }

    public static void recordClientEventWithSource(int paramInt1, int paramInt2, Object paramObject) {
        record(268435456, paramInt2, paramInt1, paramObject, false);
    }

    public static void recordLatencyStart(int paramInt) {
        record(1073741824, 0, paramInt, null, false);
    }

    public static void recordOneOffBreakdownEvent(int paramInt) {
        record(1342177280, 0, paramInt, null, true);
    }

    public static void recordSpeechEvent(int paramInt) {
        recordSpeechEvent(paramInt, null);
    }

    public static void recordSpeechEvent(int paramInt, Object paramObject) {
        record(536870912, 0, paramInt, paramObject, false);
    }

    public static void resetOneOff() {
        sOneOffEvents.clear();
    }

    private static synchronized boolean shouldLog(int wholeEvent) {
        if (sOneOffEvents.get(wholeEvent) != null) {
            return sOneOffEvents.get(wholeEvent);
        } else {
            sOneOffEvents.put(wholeEvent, Boolean.TRUE);
            return true;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     EventLogger

 * JD-Core Version:    0.7.0.1

 */