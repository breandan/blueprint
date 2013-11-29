package com.google.android.speech.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.velvet.util.Cursors;
import com.google.android.velvet.util.Cursors.CursorRowHandler;
import javax.annotation.Nullable;

public class ContactRetriever
{
  protected final ContentResolver mContentResolver;
  
  public ContactRetriever(ContentResolver paramContentResolver)
  {
    this.mContentResolver = paramContentResolver;
  }
  
  @Nullable
  private Cursor getCursor(Uri paramUri, int paramInt, String[] paramArrayOfString1, @Nullable String paramString1, @Nullable String[] paramArrayOfString2, @Nullable String paramString2)
  {
    Uri localUri = paramUri.buildUpon().appendQueryParameter("limit", String.valueOf(paramInt)).build();
    try
    {
      Cursor localCursor = this.mContentResolver.query(localUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
      return localCursor;
    }
    catch (Exception localException) {}
    return null;
  }
  
  public void getContacts(Uri paramUri, int paramInt, String[] paramArrayOfString, Cursors.CursorRowHandler paramCursorRowHandler)
  {
    getContacts(paramUri, paramInt, paramArrayOfString, null, null, "times_contacted DESC, last_time_contacted DESC", paramCursorRowHandler);
  }
  
  public void getContacts(Uri paramUri, int paramInt, String[] paramArrayOfString1, @Nullable String paramString1, @Nullable String[] paramArrayOfString2, String paramString2, Cursors.CursorRowHandler paramCursorRowHandler)
  {
    Cursors.iterateCursor(paramCursorRowHandler, getCursor(paramUri, paramInt, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.ContactRetriever
 * JD-Core Version:    0.7.0.1
 */