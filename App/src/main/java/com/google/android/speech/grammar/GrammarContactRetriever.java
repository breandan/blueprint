package com.google.android.speech.grammar;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import com.google.android.search.core.SearchConfig;
import com.google.android.speech.contacts.ContactRetriever;
import com.google.android.velvet.util.Cursors.CursorRowHandler;
import com.google.common.collect.Lists;
import java.util.List;

public class GrammarContactRetriever
{
  private static final String[] COLS = { "display_name", "times_contacted", "last_time_contacted" };
  private final SearchConfig mConfig;
  protected final ContactRetriever mContactRetriever;
  
  public GrammarContactRetriever(ContentResolver paramContentResolver, SearchConfig paramSearchConfig)
  {
    this.mContactRetriever = new ContactRetriever(paramContentResolver);
    this.mConfig = paramSearchConfig;
  }
  
  public List<GrammarContact> getContacts()
  {
    GrammarContactRowHandler localGrammarContactRowHandler = new GrammarContactRowHandler();
    this.mContactRetriever.getContacts(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, this.mConfig.getAbnfCompilerNumContacts(), COLS, localGrammarContactRowHandler);
    return localGrammarContactRowHandler.mResults;
  }
  
  private static class GrammarContactRowHandler
    implements Cursors.CursorRowHandler
  {
    private final long mNow = System.currentTimeMillis();
    private List<GrammarContact> mResults = Lists.newArrayList();
    
    public void handleCurrentRow(Cursor paramCursor)
    {
      String str = paramCursor.getString(0);
      if (str != null)
      {
        this.mResults.add(new GrammarContact(str, paramCursor.getInt(1), this.mNow - paramCursor.getLong(2)));
        return;
      }
      Log.w("GrammarContactRetriever", "Provider returned null display name.");
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.grammar.GrammarContactRetriever
 * JD-Core Version:    0.7.0.1
 */