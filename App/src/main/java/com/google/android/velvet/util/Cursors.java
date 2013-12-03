package com.google.android.velvet.util;

import android.database.Cursor;

public class Cursors {
    public static void closeQuietly(Cursor paramCursor) {
        if (paramCursor != null) {
            paramCursor.close();
        }
    }

    public static void iterateCursor(CursorRowHandler paramCursorRowHandler, Cursor paramCursor) {
        if (paramCursor != null) {
        }
        try {
            if ((paramCursor.moveToNext()) && (!Thread.currentThread().isInterrupted())) {
                paramCursorRowHandler.handleCurrentRow(paramCursor);
            }
            return;
        } finally {
            closeQuietly(paramCursor);
        }
    }

    public static abstract interface CursorRowHandler {
        public abstract void handleCurrentRow(Cursor paramCursor);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.util.Cursors

 * JD-Core Version:    0.7.0.1

 */