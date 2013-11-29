package com.google.android.speech.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import com.google.android.search.core.Feature;
import com.google.android.search.core.RelationshipManager;
import com.google.android.velvet.util.Cursors;
import com.google.android.velvet.util.Cursors.CursorRowHandler;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.majel.proto.ActionV2Protos.ActionContact;
import com.google.majel.proto.ContactProtos.ContactQuery;
import com.google.majel.proto.ContactProtos.ContactType;
import com.google.majel.proto.ContactProtos.RecognizedName;
import com.google.speech.logs.VoicesearchClientLogProto.ContactLookupInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ProviderContactLookup
  extends ContactLookup
{
  private static final String[] CONTACT_COLS = { "_id", "lookup", "display_name", "times_contacted" };
  private static final String[] EMAIL_COLS;
  private static final String[] FAVORITES_RETRIEVAL = { "display_name", "times_contacted", "last_time_contacted" };
  private static final String[] PHONE_COLS = { "contact_id", "lookup", "display_name", "times_contacted", "data1", "data3", "data2", "is_super_primary" };
  private static final String[] POSTAL_ADDRESS_COLS;
  private final ContentResolver mContentResolver;
  private final RelationshipManager mRelationshipManager;
  private final RelationshipNameLookup mRelationshipNameLookup;
  
  static
  {
    EMAIL_COLS = new String[] { "contact_id", "lookup", "display_name", "times_contacted", "data1", "data3", "data2", "is_super_primary" };
    POSTAL_ADDRESS_COLS = new String[] { "contact_id", "lookup", "display_name", "times_contacted", "data1", "data3", "data2", "is_super_primary" };
  }
  
  public ProviderContactLookup(ContentResolver paramContentResolver, RelationshipNameLookup paramRelationshipNameLookup, RelationshipManager paramRelationshipManager)
  {
    this.mContentResolver = paramContentResolver;
    this.mRelationshipNameLookup = paramRelationshipNameLookup;
    this.mRelationshipManager = paramRelationshipManager;
  }
  
  private List<Data> createDatas(ContactProtos.ContactQuery paramContactQuery)
  {
    Object localObject;
    ArrayList localArrayList1;
    if (paramContactQuery.getVerboseNameCount() > 0)
    {
      List localList = paramContactQuery.getVerboseNameList();
      localObject = Lists.newArrayListWithCapacity(localList.size());
      localArrayList1 = Lists.newArrayListWithCapacity(localList.size());
      Iterator localIterator = localList.iterator();
      if (localIterator.hasNext())
      {
        ContactProtos.RecognizedName localRecognizedName = (ContactProtos.RecognizedName)localIterator.next();
        ((List)localObject).add(localRecognizedName.getValue());
        if (localRecognizedName.hasMatchConfidence()) {}
        for (float f = localRecognizedName.getMatchConfidence();; f = 1.0F)
        {
          localArrayList1.add(Float.valueOf(f));
          break;
        }
      }
    }
    else
    {
      localObject = paramContactQuery.getNameList();
      localArrayList1 = Lists.newArrayListWithCapacity(((List)localObject).size());
      for (int i = 0; i < ((List)localObject).size(); i++) {
        localArrayList1.add(Float.valueOf(1.0F));
      }
    }
    ArrayList localArrayList2 = Lists.newArrayListWithCapacity(((List)localObject).size());
    for (int j = 0; j < ((List)localObject).size(); j++)
    {
      localArrayList2.add(new SimpleData((String)((List)localObject).get(j), paramContactQuery.getContactType(), ((Float)localArrayList1.get(j)).floatValue()));
      String str = this.mRelationshipNameLookup.getCanonicalRelationshipName((String)((List)localObject).get(j));
      if (str != null) {
        localArrayList2.add(new SimpleData(str, paramContactQuery.getContactType(), ((Float)localArrayList1.get(j)).floatValue()));
      }
    }
    return localArrayList2;
  }
  
  private static List<Data> createDatas(List<ActionV2Protos.ActionContact> paramList, String paramString)
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      ActionV2Protos.ActionContact localActionContact = (ActionV2Protos.ActionContact)localIterator.next();
      if (localActionContact.getName() != null) {
        localArrayList.add(new ActionContactData(localActionContact, paramString, null));
      } else {
        Log.w("ContactLookup", "Cannot perform contact lookups on contacts with no display name.");
      }
    }
    return localArrayList;
  }
  
  private static Cursors.CursorRowHandler createRowHandlerForContact(List<Contact> paramList, final ContactLookup.Mode paramMode)
  {
    new Cursors.CursorRowHandler()
    {
      public void handleCurrentRow(Cursor paramAnonymousCursor)
      {
        String str1 = paramAnonymousCursor.getString(2);
        if (str1 != null)
        {
          List localList = this.val$results;
          ContactLookup.Mode localMode = paramMode;
          long l = paramAnonymousCursor.getLong(0);
          String str2 = paramAnonymousCursor.getString(1);
          int i = paramAnonymousCursor.getInt(3);
          String str3;
          String str4;
          if (this.val$hasValues)
          {
            str3 = paramAnonymousCursor.getString(4);
            boolean bool1 = this.val$hasValues;
            str4 = null;
            if (bool1) {
              str4 = paramAnonymousCursor.getString(5);
            }
            if (!this.val$hasValues) {
              break label181;
            }
          }
          label181:
          for (int j = paramAnonymousCursor.getInt(6);; j = 0)
          {
            boolean bool2 = this.val$hasValues;
            boolean bool3 = false;
            if (bool2)
            {
              int k = paramAnonymousCursor.getInt(7);
              bool3 = false;
              if (k != 0) {
                bool3 = true;
              }
            }
            localList.add(new Contact(localMode, l, str2, str1, i, str3, str4, j, bool3));
            return;
            str3 = null;
            break;
          }
        }
        Log.e("ContactLookup", "Provider returned contact with no display name.");
      }
    };
  }
  
  private List<Contact> fetchContactDetails(long paramLong, @Nullable String paramString, @Nonnull ContactLookup.Mode paramMode)
  {
    MyMode localMyMode = toMyMode(paramMode);
    String str1 = localMyMode.getTypeColumn();
    boolean bool;
    String str2;
    if ((paramString == null) || (str1 != null))
    {
      bool = true;
      Preconditions.checkArgument(bool);
      str2 = PhoneActionUtils.typeStringToAndroidTypeColumn(paramMode, paramString);
      if ((str1 != null) && (str2 != null)) {
        break label141;
      }
    }
    for (String str3 = "contact_id = ?";; str3 = "contact_id = ? AND " + str1 + " = " + str2)
    {
      try
      {
        ContentResolver localContentResolver = this.mContentResolver;
        Uri localUri = localMyMode.getContentUri();
        String[] arrayOfString1 = localMyMode.getCols();
        String[] arrayOfString2 = new String[1];
        arrayOfString2[0] = Long.toString(paramLong);
        Cursor localCursor2 = localContentResolver.query(localUri, arrayOfString1, str3, arrayOfString2, null);
        localCursor1 = localCursor2;
      }
      catch (Exception localException)
      {
        for (;;)
        {
          ArrayList localArrayList;
          label141:
          Log.w("ContactLookup", "Exception querying contacts provider: " + localException.getMessage());
          Cursor localCursor1 = null;
        }
      }
      localArrayList = Lists.newArrayList();
      if (localCursor1 != null) {
        Cursors.iterateCursor(createRowHandlerForContact(localArrayList, paramMode), localCursor1);
      }
      return Contact.filterUnique(localArrayList);
      bool = false;
      break;
    }
  }
  
  private final List<Contact> fetchContactDetailsWithPreferredType(long paramLong, String paramString, @Nonnull ContactLookup.Mode paramMode)
  {
    List localList = fetchContactDetails(paramLong, paramString, paramMode);
    if ((localList.isEmpty()) && (paramString != null)) {
      localList = fetchContactDetails(paramLong, null, paramMode);
    }
    return localList;
  }
  
  private Person fetchPersonFromId(long paramLong)
  {
    MyMode localMyMode = MyMode.PERSON;
    try
    {
      ContentResolver localContentResolver = this.mContentResolver;
      Uri localUri = localMyMode.getContentUri();
      String[] arrayOfString1 = localMyMode.getCols();
      String[] arrayOfString2 = new String[1];
      arrayOfString2[0] = Long.toString(paramLong);
      Cursor localCursor2 = localContentResolver.query(localUri, arrayOfString1, "_id = ?", arrayOfString2, null);
      localCursor1 = localCursor2;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        Person localPerson;
        Log.w("ContactLookup", "Exception querying contacts provider: " + localException.getMessage());
        Cursor localCursor1 = null;
      }
    }
    localPerson = null;
    if (localCursor1 != null)
    {
      localCursor1.moveToFirst();
      localPerson = new Person(paramLong, localCursor1.getString(localCursor1.getColumnIndex("lookup")), localCursor1.getString(localCursor1.getColumnIndex("display_name")));
      localCursor1.close();
    }
    return localPerson;
  }
  
  private List<Contact> findAllByDisplayNameAndSpecifiedFilters(Data paramData, ContactLookup.Mode paramMode, String paramString)
  {
    EventLogger.recordSpeechEvent(1);
    ArrayList localArrayList = Lists.newArrayList();
    Cursors.CursorRowHandler localCursorRowHandler = createRowHandlerForContact(localArrayList, paramMode);
    String str = maybeNormalizeName(paramData.getName());
    Cursor localCursor1 = searchContacts(str, paramString, null, paramMode);
    if (localCursor1 != null) {
      Cursors.iterateCursor(localCursorRowHandler, localCursor1);
    }
    if (localArrayList.isEmpty())
    {
      Set localSet = this.mRelationshipManager.getContactLookupKeyForRelationship(str);
      if (localSet != null)
      {
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext())
        {
          Cursor localCursor2 = findContactByContactLookupKey((String)localIterator.next());
          Preconditions.checkNotNull(localCursor2);
          Cursors.iterateCursor(localCursorRowHandler, localCursor2);
        }
      }
    }
    EventLogger.recordSpeechEvent(2);
    return localArrayList;
  }
  
  private List<Person> findAllByDisplayNameInternal(@Nonnull ContactLookup.Mode paramMode, List<Data> paramList)
  {
    Preconditions.checkNotNull(paramMode, "must specify a valid Mode argument");
    ArrayList localArrayList = Lists.newArrayList();
    VoicesearchClientLogProto.ContactLookupInfo localContactLookupInfo = new VoicesearchClientLogProto.ContactLookupInfo();
    if (Feature.LOG_CONTACT_DATA.isEnabled())
    {
      int k = 0;
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext()) {
        if (((Data)localIterator.next()).getConfidenceScore() < 1.0F) {
          k++;
        }
      }
      localContactLookupInfo.setExactQueryCount(paramList.size() - k);
      localContactLookupInfo.setFuzzyQueryCount(k);
    }
    int i = 0;
    if (i < paramList.size())
    {
      Data localData2 = (Data)paramList.get(i);
      String str1 = localData2.getType(paramMode);
      if (str1 == null) {}
      for (String str2 = null;; str2 = String.format(Locale.US, "%s = %s", new Object[] { "data2", str1 }))
      {
        localArrayList.addAll(findAllByDisplayNameAndSpecifiedFilters(localData2, paramMode, str2));
        i++;
        break;
      }
    }
    if (Feature.LOG_CONTACT_DATA.isEnabled()) {
      localContactLookupInfo.setNameTypeMatchCount(localArrayList.size());
    }
    if (localArrayList.isEmpty())
    {
      for (int j = 0; j < paramList.size(); j++)
      {
        Data localData1 = (Data)paramList.get(j);
        if (localData1.getType(paramMode) != null) {
          localArrayList.addAll(findAllByDisplayNameAndSpecifiedFilters(localData1, paramMode, null));
        }
      }
      if (Feature.LOG_CONTACT_DATA.isEnabled()) {
        localContactLookupInfo.setNameMatchCount(localArrayList.size());
      }
    }
    List localList1 = Contact.filterUnique(localArrayList);
    if (Feature.LOG_CONTACT_DATA.isEnabled()) {
      localContactLookupInfo.setContactDedupCount(localList1.size());
    }
    List localList2 = Person.normalizeContacts(localList1);
    if (Feature.LOG_CONTACT_DATA.isEnabled())
    {
      localContactLookupInfo.setPeopleFoundCount(localList2.size());
      EventLogger.recordClientEvent(124, localContactLookupInfo);
    }
    return localList2;
  }
  
  @Nullable
  private Cursor findContactByContactLookupKey(String paramString)
  {
    Uri localUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    String[] arrayOfString = { paramString };
    return runQuery(localUri, MyMode.PHONE_NUMBER.getCols(), "lookup = ?", arrayOfString, null);
  }
  
  @Nullable
  private Cursor searchContacts(String paramString1, String paramString2, String[] paramArrayOfString, ContactLookup.Mode paramMode)
  {
    MyMode localMyMode = toMyMode(paramMode);
    return runQuery(Uri.withAppendedPath(localMyMode.getContentFilterUri(), Uri.encode(paramString1)).buildUpon().appendQueryParameter("limit", String.valueOf(100)).build(), localMyMode.getCols(), paramString2, paramArrayOfString, null);
  }
  
  private static MyMode toMyMode(ContactLookup.Mode paramMode)
  {
    if (paramMode == ContactLookup.Mode.EMAIL) {
      return MyMode.EMAIL;
    }
    if (paramMode == ContactLookup.Mode.PHONE_NUMBER) {
      return MyMode.PHONE_NUMBER;
    }
    if (paramMode == ContactLookup.Mode.POSTAL_ADDRESS) {
      return MyMode.POSTAL_ADDRESS;
    }
    if (paramMode == ContactLookup.Mode.PERSON) {
      return MyMode.PERSON;
    }
    throw new IllegalArgumentException("Unknown mode: " + paramMode);
  }
  
  public Person fetchContactInfo(long paramLong)
  {
    return fetchPersonFromId(paramLong);
  }
  
  public List<Contact> fetchEmailAddresses(long paramLong, String paramString)
  {
    return fetchContactDetailsWithPreferredType(paramLong, paramString, ContactLookup.Mode.EMAIL);
  }
  
  public List<Contact> fetchPhoneNumbers(long paramLong, String paramString)
  {
    return fetchContactDetailsWithPreferredType(paramLong, paramString, ContactLookup.Mode.PHONE_NUMBER);
  }
  
  public List<Contact> fetchPostalAddresses(long paramLong, String paramString)
  {
    return fetchContactDetailsWithPreferredType(paramLong, paramString, ContactLookup.Mode.POSTAL_ADDRESS);
  }
  
  public List<Person> findAllByDisplayName(@Nonnull ContactLookup.Mode paramMode, List<ActionV2Protos.ActionContact> paramList, @Nullable String paramString)
  {
    return findAllByDisplayNameInternal(paramMode, createDatas(paramList, paramString));
  }
  
  public List<Person> findAllByDisplayName(ContactProtos.ContactQuery paramContactQuery)
  {
    Preconditions.checkState(Feature.CONTACT_REFERENCE.isEnabled());
    ArrayList localArrayList1 = Lists.newArrayList();
    if (paramContactQuery.getContactMethodCount() == 0) {
      localArrayList1.add(ContactLookup.Mode.PERSON);
    }
    ArrayList localArrayList2;
    for (;;)
    {
      List localList = createDatas(paramContactQuery);
      localArrayList2 = Lists.newArrayList();
      Iterator localIterator2 = localArrayList1.iterator();
      while (localIterator2.hasNext()) {
        localArrayList2.addAll(findAllByDisplayNameInternal((ContactLookup.Mode)localIterator2.next(), localList));
      }
      Iterator localIterator1 = paramContactQuery.getContactMethodList().iterator();
      while (localIterator1.hasNext()) {
        switch (((Integer)localIterator1.next()).intValue())
        {
        default: 
          break;
        case 1: 
          localArrayList1.add(ContactLookup.Mode.PHONE_NUMBER);
          break;
        case 2: 
          localArrayList1.add(ContactLookup.Mode.EMAIL);
          break;
        case 3: 
          localArrayList1.add(ContactLookup.Mode.POSTAL_ADDRESS);
        }
      }
    }
    PersonMergeStrategy[] arrayOfPersonMergeStrategy = new PersonMergeStrategy[1];
    arrayOfPersonMergeStrategy[0] = new PersonMergeStrategy.MergeById();
    return Person.mergePersons(localArrayList2, arrayOfPersonMergeStrategy);
  }
  
  public List<Person> findAllByPhoneNumber(String paramString)
  {
    final ArrayList localArrayList = Lists.newArrayList();
    Uri localUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(paramString));
    String[] arrayOfString = { "_id", "lookup", "display_name", "times_contacted", "number", "label", "type" };
    Cursor localCursor = this.mContentResolver.query(localUri, arrayOfString, null, null, null, null);
    Cursors.CursorRowHandler local2 = new Cursors.CursorRowHandler()
    {
      public void handleCurrentRow(Cursor paramAnonymousCursor)
      {
        String str = paramAnonymousCursor.getString(paramAnonymousCursor.getColumnIndex("display_name"));
        if (str != null)
        {
          localArrayList.add(new Contact(ContactLookup.Mode.PHONE_NUMBER, paramAnonymousCursor.getLong(paramAnonymousCursor.getColumnIndex("_id")), paramAnonymousCursor.getString(paramAnonymousCursor.getColumnIndex("lookup")), str, paramAnonymousCursor.getInt(paramAnonymousCursor.getColumnIndex("times_contacted")), paramAnonymousCursor.getString(paramAnonymousCursor.getColumnIndex("number")), paramAnonymousCursor.getString(paramAnonymousCursor.getColumnIndex("label")), paramAnonymousCursor.getInt(paramAnonymousCursor.getColumnIndex("type")), false));
          return;
        }
        Log.e("ContactLookup", "Provider returned contact with no display name.");
      }
    };
    if (localCursor != null) {
      Cursors.iterateCursor(local2, localCursor);
    }
    return Person.normalizeContacts(localArrayList);
  }
  
  public List<String> findFavoriteContactNames(int paramInt1, int paramInt2)
  {
    int i = Math.min(paramInt1, 100);
    Uri localUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI.buildUpon().appendQueryParameter("limit", String.valueOf(i)).build();
    String[] arrayOfString = { "0" };
    Cursor localCursor = this.mContentResolver.query(localUri, FAVORITES_RETRIEVAL, "times_contacted > ?", arrayOfString, "times_contacted DESC, last_time_contacted DESC");
    FavoriteContactRowHandler localFavoriteContactRowHandler = new FavoriteContactRowHandler();
    Cursors.iterateCursor(localFavoriteContactRowHandler, localCursor);
    ArrayList localArrayList = Lists.newArrayList();
    Collections.sort(localFavoriteContactRowHandler.mResults, new FavoriteContact.WeightComparator());
    for (int j = 0; j < Math.min(localFavoriteContactRowHandler.mResults.size(), paramInt2); j++) {
      localArrayList.add(((FavoriteContact)localFavoriteContactRowHandler.mResults.get(j)).getName());
    }
    return localArrayList;
  }
  
  public boolean hasMatchingContacts(String paramString)
  {
    ArrayList localArrayList = Lists.newArrayList();
    ContactLookup.Mode localMode = ContactLookup.Mode.PERSON;
    Cursors.CursorRowHandler localCursorRowHandler = createRowHandlerForContact(localArrayList, localMode);
    String str = maybeNormalizeName(paramString);
    String[] arrayOfString = { "" };
    arrayOfString[0] = ("%" + str + "%");
    Cursor localCursor = searchContacts(str, "display_name LIKE ?", arrayOfString, localMode);
    if (localCursor != null) {
      Cursors.iterateCursor(localCursorRowHandler, localCursor);
    }
    return localArrayList.size() > 0;
  }
  
  @Nullable
  protected Cursor runQuery(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    try
    {
      Cursor localCursor = this.mContentResolver.query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
      return localCursor;
    }
    catch (Exception localException)
    {
      Log.w("ContactLookup", "Exception querying content provider: " + localException.getMessage());
    }
    return null;
  }
  
  private static class ActionContactData
    implements ProviderContactLookup.Data
  {
    private final ActionV2Protos.ActionContact mActionContact;
    private final String mPreferredType;
    
    private ActionContactData(ActionV2Protos.ActionContact paramActionContact, String paramString)
    {
      this.mActionContact = paramActionContact;
      this.mPreferredType = paramString;
    }
    
    public float getConfidenceScore()
    {
      return 1.0F;
    }
    
    @Nonnull
    public String getName()
    {
      return this.mActionContact.getName();
    }
    
    public String getType(ContactLookup.Mode paramMode)
    {
      String str = PhoneActionUtils.getContactType(paramMode, this.mActionContact);
      if (str == null) {
        str = this.mPreferredType;
      }
      return str;
    }
  }
  
  private static abstract interface Data
  {
    public abstract float getConfidenceScore();
    
    public abstract String getName();
    
    public abstract String getType(ContactLookup.Mode paramMode);
  }
  
  private static class FavoriteContactRowHandler
    implements Cursors.CursorRowHandler
  {
    private final long mNow = System.currentTimeMillis();
    private List<FavoriteContact> mResults = Lists.newArrayList();
    private int maxTimesContacted = 0;
    
    public void handleCurrentRow(Cursor paramCursor)
    {
      String str = paramCursor.getString(0);
      int i = paramCursor.getInt(1);
      long l = paramCursor.getLong(2);
      if (this.maxTimesContacted == 0) {
        this.maxTimesContacted = i;
      }
      if (str != null) {
        this.mResults.add(new FavoriteContact(str, i, this.mNow - l, this.maxTimesContacted));
      }
    }
  }
  
  private static enum MyMode
  {
    private final String[] mCols;
    private final Uri mContentFilterUri;
    private final Uri mContentUri;
    private final String mTypeColumn;
    
    static
    {
      PERSON = new MyMode("PERSON", 3, ProviderContactLookup.CONTACT_COLS, ContactsContract.Contacts.CONTENT_FILTER_URI, ContactsContract.Contacts.CONTENT_URI, null);
      MyMode[] arrayOfMyMode = new MyMode[4];
      arrayOfMyMode[0] = EMAIL;
      arrayOfMyMode[1] = PHONE_NUMBER;
      arrayOfMyMode[2] = POSTAL_ADDRESS;
      arrayOfMyMode[3] = PERSON;
      $VALUES = arrayOfMyMode;
    }
    
    private MyMode(String[] paramArrayOfString, Uri paramUri1, Uri paramUri2, String paramString)
    {
      this.mCols = paramArrayOfString;
      this.mContentFilterUri = paramUri1;
      this.mTypeColumn = paramString;
      this.mContentUri = paramUri2;
    }
    
    private boolean hasValueColumns()
    {
      return this.mCols.length > 3;
    }
    
    public String[] getCols()
    {
      return this.mCols;
    }
    
    public Uri getContentFilterUri()
    {
      return this.mContentFilterUri;
    }
    
    public Uri getContentUri()
    {
      return this.mContentUri;
    }
    
    public String getTypeColumn()
    {
      return this.mTypeColumn;
    }
  }
  
  private static class SimpleData
    implements ProviderContactLookup.Data
  {
    private final float mConfidenceScore;
    private final String mName;
    private final ContactProtos.ContactType mType;
    
    SimpleData(String paramString, ContactProtos.ContactType paramContactType, float paramFloat)
    {
      this.mName = paramString;
      this.mType = paramContactType;
      this.mConfidenceScore = paramFloat;
    }
    
    public float getConfidenceScore()
    {
      return this.mConfidenceScore;
    }
    
    public String getName()
    {
      return this.mName;
    }
    
    public String getType(ContactLookup.Mode paramMode)
    {
      int i = PhoneActionUtils.contactTypeToAndroidTypeColumn(paramMode, this.mType);
      if (i != 0) {
        return String.valueOf(i);
      }
      return null;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.ProviderContactLookup
 * JD-Core Version:    0.7.0.1
 */