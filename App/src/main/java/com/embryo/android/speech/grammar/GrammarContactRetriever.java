package com.embryo.android.speech.grammar;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.google.android.speech.contacts.ContactRetriever;
import com.google.android.velvet.util.Cursors;
import com.google.common.collect.Lists;

import java.util.List;

public class GrammarContactRetriever {
    private static final String[] COLS = {"display_name", "times_contacted", "last_time_contacted"};
    protected final ContactRetriever mContactRetriever;

    public GrammarContactRetriever(ContentResolver paramContentResolver) {
        this.mContactRetriever = new ContactRetriever(paramContentResolver);
    }

    private static class GrammarContactRowHandler
            implements Cursors.CursorRowHandler {
        private final long mNow = System.currentTimeMillis();
        private List<GrammarContact> mResults = Lists.newArrayList();

        public void handleCurrentRow(Cursor paramCursor) {
            String str = paramCursor.getString(0);
            if (str != null) {
                this.mResults.add(new GrammarContact(str, paramCursor.getInt(1), this.mNow - paramCursor.getLong(2)));
                return;
            }
            Log.w("GrammarContactRetriever", "Provider returned null display name.");
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     GrammarContactRetriever

 * JD-Core Version:    0.7.0.1

 */