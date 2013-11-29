package com.google.android.search.core.summons.icing;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ContactsDataHandler
{
  private Cursor mCursor;
  private final Map<String, DataHandler> mHandlers = new HashMap();
  private int mMimetypeIndex;
  private final Set<String> mNeededColumns;
  
  public ContactsDataHandler()
  {
    this.mHandlers.put("vnd.android.cursor.item/email_v2", new EmailDataHandler(null));
    this.mHandlers.put("vnd.android.cursor.item/nickname", new NicknameDataHandler());
    this.mHandlers.put("vnd.android.cursor.item/note", new NoteDataHandler());
    this.mHandlers.put("vnd.android.cursor.item/organization", new OrganizationDataHandler(null));
    this.mHandlers.put("vnd.android.cursor.item/phone_v2", new PhoneHandler(null));
    this.mHandlers.put("vnd.android.cursor.item/note", new NoteDataHandler());
    this.mHandlers.put("vnd.android.cursor.item/postal-address_v2", new StructuredPostalHandler(null));
    this.mHandlers.put("vnd.android.cursor.item/name", new StructuredNameHandler());
    this.mNeededColumns = new HashSet();
    this.mNeededColumns.add("mimetype");
    Iterator localIterator = this.mHandlers.values().iterator();
    while (localIterator.hasNext()) {
      ((DataHandler)localIterator.next()).addNeededColumns(this.mNeededColumns);
    }
  }
  
  public Set<String> getNeededColumns()
  {
    return this.mNeededColumns;
  }
  
  public void handleCurrentRow(ContactsHelper.ContactBuilder paramContactBuilder)
  {
    if (this.mCursor != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Cursor must be set");
      String str = this.mCursor.getString(this.mMimetypeIndex);
      DataHandler localDataHandler = (DataHandler)this.mHandlers.get(str);
      if (localDataHandler != null) {
        localDataHandler.addData(paramContactBuilder, this.mCursor);
      }
      return;
    }
  }
  
  public void setCursor(Cursor paramCursor)
  {
    this.mCursor = paramCursor;
    this.mMimetypeIndex = paramCursor.getColumnIndex("mimetype");
  }
  
  private static abstract class DataHandler
  {
    public abstract void addData(ContactsHelper.ContactBuilder paramContactBuilder, Cursor paramCursor);
    
    public abstract void addNeededColumns(Collection<String> paramCollection);
    
    protected final String getColumnString(Cursor paramCursor, String paramString)
    {
      int i = paramCursor.getColumnIndex(paramString);
      if (i == -1)
      {
        Log.e("Icing.ContactsDataHandler", "Requested column " + paramString + " didn't exist in the cursor.");
        return "";
      }
      return paramCursor.getString(i);
    }
  }
  
  private static final class EmailDataHandler
    extends ContactsDataHandler.DataHandler
  {
    private static final String[] COLUMNS = { "data1", "data2", "data3" };
    
    private EmailDataHandler()
    {
      super();
    }
    
    public void addData(ContactsHelper.ContactBuilder paramContactBuilder, Cursor paramCursor)
    {
      String str = getColumnString(paramCursor, "data1");
      if (!TextUtils.isEmpty(str)) {
        paramContactBuilder.addEmail(str, getColumnString(paramCursor, "data2"), getColumnString(paramCursor, "data3"));
      }
    }
    
    public void addNeededColumns(Collection<String> paramCollection)
    {
      String[] arrayOfString = COLUMNS;
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++) {
        paramCollection.add(arrayOfString[j]);
      }
    }
  }
  
  private static final class NicknameDataHandler
    extends ContactsDataHandler.SingleColumnDataHandler
  {
    protected NicknameDataHandler()
    {
      super();
    }
    
    protected void addSingleColumnStringData(ContactsHelper.ContactBuilder paramContactBuilder, String paramString)
    {
      paramContactBuilder.addNickname(paramString);
    }
  }
  
  private static final class NoteDataHandler
    extends ContactsDataHandler.SingleColumnDataHandler
  {
    protected NoteDataHandler()
    {
      super();
    }
    
    protected void addSingleColumnStringData(ContactsHelper.ContactBuilder paramContactBuilder, String paramString)
    {
      paramContactBuilder.addNote(paramString);
    }
  }
  
  private static final class OrganizationDataHandler
    extends ContactsDataHandler.DataHandler
  {
    private static final String[] COLUMNS = { "data4", "data1", "data8", "data7", "data5", "data9", "data6" };
    private final StringBuilder mSb = new StringBuilder();
    
    private OrganizationDataHandler()
    {
      super();
    }
    
    public void addData(ContactsHelper.ContactBuilder paramContactBuilder, Cursor paramCursor)
    {
      this.mSb.setLength(0);
      String[] arrayOfString = COLUMNS;
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        String str = getColumnString(paramCursor, arrayOfString[j]);
        if (!TextUtils.isEmpty(str))
        {
          if (this.mSb.length() != 0) {
            this.mSb.append(", ");
          }
          this.mSb.append(str);
        }
      }
      if (this.mSb.length() > 0) {
        paramContactBuilder.addOrganization(this.mSb.toString());
      }
    }
    
    public void addNeededColumns(Collection<String> paramCollection)
    {
      String[] arrayOfString = COLUMNS;
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++) {
        paramCollection.add(arrayOfString[j]);
      }
    }
  }
  
  private static final class PhoneHandler
    extends ContactsDataHandler.DataHandler
  {
    private static final String[] COLUMNS = { "data1", "data2", "data3" };
    
    private PhoneHandler()
    {
      super();
    }
    
    public void addData(ContactsHelper.ContactBuilder paramContactBuilder, Cursor paramCursor)
    {
      String str = getColumnString(paramCursor, "data1");
      if (!TextUtils.isEmpty(str)) {
        paramContactBuilder.addPhoneNumber(str, getColumnString(paramCursor, "data2"), getColumnString(paramCursor, "data3"));
      }
    }
    
    public void addNeededColumns(Collection<String> paramCollection)
    {
      String[] arrayOfString = COLUMNS;
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++) {
        paramCollection.add(arrayOfString[j]);
      }
    }
  }
  
  private static abstract class SingleColumnDataHandler
    extends ContactsDataHandler.DataHandler
  {
    private final String mColumn;
    
    protected SingleColumnDataHandler(String paramString)
    {
      super();
      this.mColumn = paramString;
    }
    
    public final void addData(ContactsHelper.ContactBuilder paramContactBuilder, Cursor paramCursor)
    {
      String str = getColumnString(paramCursor, this.mColumn);
      if (!TextUtils.isEmpty(str)) {
        addSingleColumnStringData(paramContactBuilder, str);
      }
    }
    
    public final void addNeededColumns(Collection<String> paramCollection)
    {
      paramCollection.add(this.mColumn);
    }
    
    protected abstract void addSingleColumnStringData(ContactsHelper.ContactBuilder paramContactBuilder, String paramString);
  }
  
  private static final class StructuredNameHandler
    extends ContactsDataHandler.SingleColumnDataHandler
  {
    protected StructuredNameHandler()
    {
      super();
    }
    
    protected void addSingleColumnStringData(ContactsHelper.ContactBuilder paramContactBuilder, String paramString)
    {
      paramContactBuilder.addGivenName(paramString);
    }
  }
  
  private static final class StructuredPostalHandler
    extends ContactsDataHandler.DataHandler
  {
    private static final String[] COLUMNS = { "data1", "data2", "data3" };
    
    private StructuredPostalHandler()
    {
      super();
    }
    
    public void addData(ContactsHelper.ContactBuilder paramContactBuilder, Cursor paramCursor)
    {
      String str = getColumnString(paramCursor, "data1");
      if (!TextUtils.isEmpty(str)) {
        paramContactBuilder.addPostalAddress(str, getColumnString(paramCursor, "data2"), getColumnString(paramCursor, "data3"));
      }
    }
    
    public void addNeededColumns(Collection<String> paramCollection)
    {
      String[] arrayOfString = COLUMNS;
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++) {
        paramCollection.add(arrayOfString[j]);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.ContactsDataHandler
 * JD-Core Version:    0.7.0.1
 */