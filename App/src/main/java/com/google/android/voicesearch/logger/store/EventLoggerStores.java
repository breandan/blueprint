package com.google.android.voicesearch.logger.store;

import com.google.android.search.core.EventLoggerStore;

public final class EventLoggerStores {
    private static final MainEventLoggerStore sMainEventLoggerStore = new MainEventLoggerStore();

    public static EventLoggerStore createEventStore() {
        return sMainEventLoggerStore;
    }

    public static MainEventLoggerStore getMainEventLoggerStore() {
        return sMainEventLoggerStore;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.logger.store.EventLoggerStores

 * JD-Core Version:    0.7.0.1

 */