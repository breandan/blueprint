package com.google.android.voicesearch.util;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;
import android.util.LongSparseArray;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.contacts.ContactRetriever;
import com.google.android.velvet.util.Cursors.CursorRowHandler;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

public class ExampleContactHelper
{
  private static final String[] COLUMNS = { "contact_id" };
  private static final Joiner COMMA_DELIMITED_JOINER = Joiner.on(",");
  private static final String[] GET_FIRST_NAME_COLUMNS = { "contact_id", "data2" };
  private static final String[] GET_FIRST_NAME_SELECTION_ARGS = { "vnd.android.cursor.item/name" };
  private final Map<Uri, List<Contact>> mCache = Maps.newHashMap();
  private final LongSparseArray<Integer> mContactIdUseCount = new LongSparseArray();
  private final ContactRetriever mContactRetriever;
  private final LongSparseArray<String> mFirstNames = new LongSparseArray();
  private final boolean mShuffle;
  
  public ExampleContactHelper(ContactRetriever paramContactRetriever, boolean paramBoolean)
  {
    this.mContactRetriever = paramContactRetriever;
    this.mShuffle = paramBoolean;
  }
  
  @Nonnull
  private List<Contact> retrieveContactsForUri(Uri paramUri)
  {
    ExtraPreconditions.checkNotMainThread();
    ArrayList localArrayList = Lists.newArrayListWithExpectedSize(10);
    ContactRowHandler localContactRowHandler = new ContactRowHandler(null);
    this.mContactRetriever.getContacts(paramUri, 10, COLUMNS, localContactRowHandler);
    if (!localContactRowHandler.contactIds.isEmpty()) {
      synchronized (this.mFirstNames)
      {
        updateFirstNames(localContactRowHandler.contactIds);
        Iterator localIterator = localContactRowHandler.contactIds.iterator();
        while (localIterator.hasNext())
        {
          long l = ((Long)localIterator.next()).longValue();
          if (this.mFirstNames.indexOfKey(l) >= 0) {
            localArrayList.add(new Contact(l, (String)this.mFirstNames.get(l)));
          }
        }
      }
    }
    if ((!localArrayList.isEmpty()) && (this.mShuffle)) {
      Collections.shuffle(localArrayList);
    }
    return localArrayList;
  }
  
  private void updateFirstNames(Set<Long> paramSet)
  {
    if (!paramSet.isEmpty()) {}
    HashSet localHashSet;
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      localHashSet = Sets.newHashSet(paramSet);
      for (int i = 0; i < this.mFirstNames.size(); i++) {
        localHashSet.remove(Long.valueOf(this.mFirstNames.keyAt(i)));
      }
    }
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = COMMA_DELIMITED_JOINER.join(localHashSet);
    String str = String.format("contact_id IN (%s) AND mimetype = ?", arrayOfObject);
    FirstNameRowHandler localFirstNameRowHandler = new FirstNameRowHandler(null);
    this.mContactRetriever.getContacts(ContactsContract.Data.CONTENT_URI, 10, GET_FIRST_NAME_COLUMNS, str, GET_FIRST_NAME_SELECTION_ARGS, null, localFirstNameRowHandler);
    for (int j = 0; j < localFirstNameRowHandler.firstNames.size(); j++) {
      this.mFirstNames.put(localFirstNameRowHandler.firstNames.keyAt(j), localFirstNameRowHandler.firstNames.valueAt(j));
    }
  }
  
  @Nonnull
  public List<Contact> getContacts(Uri paramUri)
  {
    if (this.mCache.containsKey(paramUri)) {
      return (List)this.mCache.get(paramUri);
    }
    List localList = retrieveContactsForUri(paramUri);
    this.mCache.put(paramUri, localList);
    return localList;
  }
  
  public static class Contact
    implements Parcelable
  {
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator()
    {
      public ExampleContactHelper.Contact createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ExampleContactHelper.Contact(paramAnonymousParcel.readLong(), paramAnonymousParcel.readString());
      }
      
      public ExampleContactHelper.Contact[] newArray(int paramAnonymousInt)
      {
        return new ExampleContactHelper.Contact[paramAnonymousInt];
      }
    };
    @Nonnull
    public final String firstName;
    public final long id;
    
    public Contact(long paramLong, String paramString)
    {
      this.id = paramLong;
      this.firstName = ((String)Preconditions.checkNotNull(paramString));
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeLong(this.id);
      paramParcel.writeString(this.firstName);
    }
  }
  
  private class ContactRowHandler
    implements Cursors.CursorRowHandler
  {
    private final Set<Long> contactIds = Sets.newHashSet();
    
    private ContactRowHandler() {}
    
    public void handleCurrentRow(Cursor paramCursor)
    {
      long l = paramCursor.getLong(0);
      this.contactIds.add(Long.valueOf(l));
    }
  }
  
  private class FirstNameRowHandler
    implements Cursors.CursorRowHandler
  {
    private final LongSparseArray<String> firstNames = new LongSparseArray();
    
    private FirstNameRowHandler() {}
    
    public void handleCurrentRow(Cursor paramCursor)
    {
      long l = paramCursor.getLong(0);
      String str = paramCursor.getString(1);
      if (!TextUtils.isEmpty(str)) {
        this.firstNames.put(l, str);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.util.ExampleContactHelper
 * JD-Core Version:    0.7.0.1
 */