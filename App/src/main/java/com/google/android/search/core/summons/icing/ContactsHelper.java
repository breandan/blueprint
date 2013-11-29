package com.google.android.search.core.summons.icing;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.SystemClock;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.DeletedContacts;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.shared.util.Util;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Preconditions;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class ContactsHelper
{
  static final boolean DELTA_API_SUPPORTED;
  private static final String[] SINGLE_ARG;
  private final ContentResolver mContentResolver;
  
  static
  {
    if (Util.SDK_INT >= 18) {}
    for (boolean bool = true;; bool = false)
    {
      DELTA_API_SUPPORTED = bool;
      SINGLE_ARG = new String[] { "" };
      return;
    }
  }
  
  ContactsHelper(ContentResolver paramContentResolver)
  {
    this.mContentResolver = paramContentResolver;
  }
  
  private static ContentValues buildContentValues(long paramLong1, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt, long paramLong2, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12, String paramString13, String paramString14, String paramString15, String paramString16)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.clear();
    localContentValues.put("contact_id", Long.valueOf(paramLong1));
    localContentValues.put("lookup_key", paramString1);
    localContentValues.put("icon_uri", paramString2);
    localContentValues.put("display_name", paramString3);
    localContentValues.put("given_names", paramString4);
    localContentValues.put("times_contacted", Integer.valueOf(paramInt));
    localContentValues.put("score", Long.valueOf(paramLong2));
    localContentValues.put("emails", paramString5);
    localContentValues.put("emails_types", paramString6);
    localContentValues.put("emails_labels", paramString7);
    localContentValues.put("nickname", paramString8);
    localContentValues.put("note", paramString9);
    localContentValues.put("organization", paramString10);
    localContentValues.put("phone_numbers", paramString11);
    localContentValues.put("phone_number_types", paramString12);
    localContentValues.put("phone_number_labels", paramString13);
    localContentValues.put("postal_address", paramString14);
    localContentValues.put("postal_address_types", paramString15);
    localContentValues.put("postal_address_labels", paramString16);
    return localContentValues;
  }
  
  private static String filterUnwantedContactIds(Iterable<Long> paramIterable)
  {
    return "contact_id NOT IN (" + TextUtils.join(",", paramIterable) + ")";
  }
  
  private static String filterWantedContactIds(Iterable<Long> paramIterable)
  {
    return "contact_id IN (" + TextUtils.join(",", paramIterable) + ")";
  }
  
  private static long getContactId(ContentValues paramContentValues)
  {
    Long localLong = paramContentValues.getAsLong("contact_id");
    if (localLong == null) {
      return 0L;
    }
    return localLong.longValue();
  }
  
  private static String[] getContactsColumns()
  {
    if (DELTA_API_SUPPORTED) {
      return getContactsColumnsV18();
    }
    return new String[] { "_id" };
  }
  
  @TargetApi(18)
  private static String[] getContactsColumnsV18()
  {
    return new String[] { "_id", "contact_last_updated_timestamp" };
  }
  
  private int getCurrentContactCount(SQLiteDatabase paramSQLiteDatabase)
  {
    Cursor localCursor = paramSQLiteDatabase.rawQuery("SELECT COUNT(*) FROM contacts", null);
    try
    {
      if (localCursor.moveToNext())
      {
        int i = localCursor.getInt(0);
        return i;
      }
      return -1;
    }
    finally
    {
      localCursor.close();
    }
  }
  
  @TargetApi(18)
  private long getDeletedContactIdsSinceV18(long paramLong, Set<Long> paramSet)
  {
    Preconditions.checkState(DELTA_API_SUPPORTED, "Delta API not supported");
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Long.toString(paramLong);
    long l1 = paramLong;
    Cursor localCursor = null;
    try
    {
      localCursor = this.mContentResolver.query(ContactsContract.DeletedContacts.CONTENT_URI, new String[] { "contact_id", "contact_deleted_timestamp" }, "contact_deleted_timestamp>?", arrayOfString, null);
      if (localCursor == null)
      {
        Log.i("Icing.ContactsHelper", "Could not fetch deleted contacts - no contacts provider present?");
        return l1;
      }
      int i = localCursor.getColumnIndex("contact_id");
      int j = localCursor.getColumnIndex("contact_deleted_timestamp");
      long l2 = 0L;
      while (localCursor.moveToNext())
      {
        l2 += 1L;
        paramSet.add(Long.valueOf(localCursor.getLong(i)));
        long l3 = Math.max(l1, localCursor.getLong(j));
        l1 = l3;
      }
      return l1;
    }
    finally
    {
      if (localCursor != null) {
        localCursor.close();
      }
    }
  }
  
  @TargetApi(18)
  private static String getLastUpdatedSelectionV18(long paramLong)
  {
    return "contact_last_updated_timestamp>" + String.valueOf(paramLong);
  }
  
  @TargetApi(18)
  private static long getLastUpdatedTimestampV18(Cursor paramCursor)
  {
    int i = paramCursor.getColumnIndex("contact_last_updated_timestamp");
    if (i != -1) {
      return paramCursor.getLong(i);
    }
    return 0L;
  }
  
  private SharedPreferences getPreferences()
  {
    return VelvetServices.get().getPreferenceController().getMainPreferences();
  }
  
  private Pair<CursorIterator, Long> getWantedContactsIterator(long paramLong)
  {
    String str = "";
    if ((DELTA_API_SUPPORTED) && (paramLong != -1L)) {
      str = getLastUpdatedSelectionV18(paramLong);
    }
    Uri localUri = ContactsContract.Contacts.CONTENT_URI.buildUpon().appendQueryParameter("directory", String.valueOf(0L)).build();
    Cursor localCursor = this.mContentResolver.query(localUri, getContactsColumns(), str, null, null);
    if (localCursor == null)
    {
      Log.w("Icing.ContactsHelper", "Could not query ContactsProvider; disabled? Wiping local DB.");
      return new Pair(new EmptyContactsProviderIterator(null), Long.valueOf(paramLong));
    }
    ArrayList localArrayList = new ArrayList();
    long l = paramLong;
    try
    {
      int i = localCursor.getColumnIndex("_id");
      while (localCursor.moveToNext())
      {
        int j = localCursor.getInt(i);
        if (DELTA_API_SUPPORTED) {
          l = Math.max(l, getLastUpdatedTimestampV18(localCursor));
        }
        localArrayList.add(Integer.valueOf(j));
      }
    }
    finally
    {
      localCursor.close();
    }
    Collections.sort(localArrayList);
    return new Pair(new BatchingContactsProviderIterator(this.mContentResolver, localArrayList), Long.valueOf(l));
  }
  
  private static boolean isValidContact(ContentValues paramContentValues)
  {
    if (getContactId(paramContentValues) == 0L) {}
    while ((paramContentValues.get("lookup_key") == null) || (paramContentValues.get("display_name") == null)) {
      return false;
    }
    return true;
  }
  
  public static String[] sectionToList(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    return paramString.split("", -1);
  }
  
  void createContactsTable(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS contacts");
    paramSQLiteDatabase.execSQL("CREATE TABLE contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT,contact_id INTEGER,lookup_key TEXT,icon_uri TEXT,display_name TEXT,given_names TEXT,times_contacted TEXT,score INTEGER,emails TEXT,emails_types TEXT,emails_labels TEXT,nickname TEXT,note TEXT,organization TEXT,phone_numbers TEXT,phone_number_types TEXT,phone_number_labels TEXT,postal_address TEXT,postal_address_types TEXT,postal_address_labels TEXT)");
  }
  
  void dump(SQLiteDatabase paramSQLiteDatabase, String paramString, PrintWriter paramPrintWriter, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str1 = "extensive";; str1 = "simple")
    {
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = paramString;
      arrayOfObject1[1] = ("ContactsHelper (" + str1 + ") state:");
      DumpUtils.println(paramPrintWriter, arrayOfObject1);
      String str2 = paramString + "  ";
      try
      {
        int i = getCurrentContactCount(paramSQLiteDatabase);
        Object[] arrayOfObject3 = new Object[2];
        arrayOfObject3[0] = str2;
        arrayOfObject3[1] = ("Contact count: " + i);
        DumpUtils.println(paramPrintWriter, arrayOfObject3);
        Object[] arrayOfObject4 = new Object[3];
        arrayOfObject4[0] = str2;
        arrayOfObject4[1] = "Last delta update timestamp: ";
        arrayOfObject4[2] = DumpUtils.formatTimestampISO8301(getPreferences().getLong("key_last_contacts_delta_delete_timestamp", 0L));
        DumpUtils.println(paramPrintWriter, arrayOfObject4);
        Object[] arrayOfObject5 = new Object[3];
        arrayOfObject5[0] = str2;
        arrayOfObject5[1] = "Last delta delete timestamp: ";
        arrayOfObject5[2] = DumpUtils.formatTimestampISO8301(getPreferences().getLong("key_last_contacts_delta_update_timestamp", 0L));
        DumpUtils.println(paramPrintWriter, arrayOfObject5);
        DumpUtils.println(paramPrintWriter, new Object[0]);
        if (paramBoolean) {
          DumpUtils.dumpSqliteTable(paramSQLiteDatabase, str2, paramPrintWriter, "contacts");
        }
        return;
      }
      catch (Exception localException)
      {
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = str2;
        arrayOfObject2[1] = ("Exception while dumping state" + localException);
        DumpUtils.println(paramPrintWriter, arrayOfObject2);
      }
    }
  }
  
  int updateContacts(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    long l1 = getPreferences().getLong("key_last_contacts_delta_delete_timestamp", 0L);
    long l2 = getPreferences().getLong("key_last_contacts_delta_update_timestamp", 0L);
    if ((paramBoolean) && (!DELTA_API_SUPPORTED))
    {
      Log.w("Icing.ContactsHelper", "Delta update requested but no delta API present");
      return 0;
    }
    if ((paramBoolean) && (!getPreferences().contains("key_last_contacts_delta_update_timestamp")))
    {
      paramBoolean = false;
      Log.i("Icing.ContactsHelper", "Delta update with no prior full sync - doing full sync instead.");
    }
    SystemClock.elapsedRealtime();
    HashMap localHashMap = new HashMap();
    HashSet localHashSet1 = new HashSet();
    long l3;
    if (paramBoolean) {
      l3 = l2;
    }
    CursorIterator localCursorIterator;
    long l4;
    for (;;)
    {
      Pair localPair = getWantedContactsIterator(l3);
      localCursorIterator = (CursorIterator)localPair.first;
      l4 = Math.max(l2, ((Long)localPair.second).longValue());
      try
      {
        while (localCursorIterator.hasNext())
        {
          ContentValues localContentValues3 = (ContentValues)localCursorIterator.next();
          long l7 = getContactId(localContentValues3);
          if (isValidContact(localContentValues3)) {
            localHashMap.put(Long.valueOf(l7), localContentValues3);
          }
          localHashSet1.add(Long.valueOf(l7));
        }
        l3 = -1L;
      }
      finally
      {
        localCursorIterator.close();
      }
    }
    localCursorIterator.close();
    paramSQLiteDatabase.beginTransaction();
    int i = 0;
    if (paramBoolean) {}
    label469:
    label479:
    int k;
    for (;;)
    {
      long l5;
      Set localSet;
      int j;
      ContactsDatabaseIterator localContactsDatabaseIterator;
      try
      {
        l5 = getDeletedContactIdsSinceV18(l1, localHashSet1);
        localSet = localHashMap.keySet();
        if (!paramBoolean) {
          break label469;
        }
        HashSet localHashSet2 = new HashSet(localHashSet1);
        localHashSet2.removeAll(localSet);
        str1 = filterWantedContactIds(localHashSet2);
        j = 0 + paramSQLiteDatabase.delete("contacts", str1, null);
        if (!paramBoolean) {
          break label593;
        }
        str2 = filterWantedContactIds(localSet);
        localContactsDatabaseIterator = new ContactsDatabaseIterator(paramSQLiteDatabase, str2);
        try
        {
          if (!localContactsDatabaseIterator.hasNext()) {
            break label479;
          }
          ContentValues localContentValues1 = (ContentValues)localContactsDatabaseIterator.next();
          long l6 = getContactId(localContentValues1);
          ContentValues localContentValues2 = (ContentValues)localHashMap.get(Long.valueOf(l6));
          if (!localContentValues1.equals(localContentValues2))
          {
            SINGLE_ARG[0] = String.valueOf(l6);
            i += paramSQLiteDatabase.update("contacts", localContentValues2, "contact_id=?", SINGLE_ARG);
          }
          localHashMap.remove(Long.valueOf(l6));
          continue;
          localObject2 = finally;
        }
        finally
        {
          localContactsDatabaseIterator.close();
        }
        l5 = l4;
      }
      finally
      {
        paramSQLiteDatabase.endTransaction();
      }
      continue;
      String str1 = filterUnwantedContactIds(localSet);
      continue;
      localContactsDatabaseIterator.close();
      Iterator localIterator = localHashMap.values().iterator();
      if (localIterator.hasNext())
      {
        if (paramSQLiteDatabase.insert("contacts", null, (ContentValues)localIterator.next()) != -1L) {
          break label609;
        }
        k = 0;
        break;
      }
      getPreferences().edit().putLong("key_last_contacts_delta_update_timestamp", l4).putLong("key_last_contacts_delta_delete_timestamp", l5).apply();
      paramSQLiteDatabase.setTransactionSuccessful();
      paramSQLiteDatabase.endTransaction();
      SystemClock.elapsedRealtime();
      return i + j;
      label593:
      String str2 = null;
    }
    for (;;)
    {
      i += k;
      break;
      label609:
      k = 1;
    }
  }
  
  void upgradeDbTo3(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS contacts");
    paramSQLiteDatabase.execSQL("CREATE TABLE contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT,contact_id INTEGER,lookup_key TEXT,icon_uri TEXT,display_name TEXT,times_contacted TEXT,emails TEXT,nickname TEXT,note TEXT,organization TEXT,phone_numbers TEXT,postal_address TEXT)");
  }
  
  void upgradeDbTo4(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("ALTER TABLE contacts ADD COLUMN score INTEGER");
  }
  
  void upgradeDbTo5(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("ALTER TABLE contacts ADD COLUMN given_names TEXT");
  }
  
  void upgradeDbTo6(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS contacts_appdatasearch_seqno_table");
  }
  
  void upgradeDbTo8(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("ALTER TABLE contacts ADD COLUMN phone_number_types TEXT");
    paramSQLiteDatabase.execSQL("ALTER TABLE contacts ADD COLUMN phone_number_labels TEXT");
  }
  
  public void upgradeDbTo9(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("ALTER TABLE contacts ADD COLUMN emails_types TEXT");
    paramSQLiteDatabase.execSQL("ALTER TABLE contacts ADD COLUMN emails_labels TEXT");
    paramSQLiteDatabase.execSQL("ALTER TABLE contacts ADD COLUMN postal_address_types TEXT");
    paramSQLiteDatabase.execSQL("ALTER TABLE contacts ADD COLUMN postal_address_labels TEXT");
  }
  
  private static final class BatchingContactsProviderIterator
    implements ContactsHelper.CursorIterator
  {
    private final List<Integer> mContactIds;
    private final ContentResolver mContentResolver;
    private ContactsHelper.CursorIterator mCurrentIterator;
    private final ContactsDataHandler mDataHandler;
    private long mMinBytesRead = 9223372036854775807L;
    private int mNextIndex;
    private int mNumQueriesMade = 0;
    private final String mOrderBy;
    private final String[] mProjection;
    
    public BatchingContactsProviderIterator(ContentResolver paramContentResolver, List<Integer> paramList)
    {
      this.mContentResolver = paramContentResolver;
      this.mContactIds = paramList;
      this.mDataHandler = new ContactsDataHandler();
      this.mCurrentIterator = new ContactsHelper.EmptyContactsProviderIterator(null);
      Set localSet = this.mDataHandler.getNeededColumns();
      localSet.add("contact_id");
      localSet.add("lookup");
      localSet.add("photo_thumb_uri");
      localSet.add("display_name");
      localSet.add("times_contacted");
      localSet.add("last_time_contacted");
      localSet.add("starred");
      localSet.add("raw_contact_id");
      this.mProjection = ((String[])localSet.toArray(new String[localSet.size()]));
      this.mOrderBy = "contact_id,is_super_primary DESC,is_primary DESC,raw_contact_id";
    }
    
    private void ensureCurrent()
    {
      Cursor localCursor;
      if ((!this.mCurrentIterator.hasNext()) && (this.mNextIndex < this.mContactIds.size()))
      {
        this.mCurrentIterator.close();
        int i = Math.min(250 + this.mNextIndex, this.mContactIds.size());
        List localList = this.mContactIds.subList(this.mNextIndex, i);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = TextUtils.join(",", localList);
        String str = String.format("contact_id IN (%s)", arrayOfObject);
        this.mNextIndex = i;
        localCursor = this.mContentResolver.query(ContactsContract.Data.CONTENT_URI, this.mProjection, str, null, this.mOrderBy);
        this.mNumQueriesMade = (1 + this.mNumQueriesMade);
        if (localCursor == null)
        {
          Log.w("Icing.ContactsHelper", "Could not query ContactsProvider; disabled? Give up.");
          this.mCurrentIterator = new ContactsHelper.EmptyContactsProviderIterator(null);
          this.mNextIndex = this.mContactIds.size();
        }
      }
      else
      {
        return;
      }
      this.mCurrentIterator = new ContactsHelper.NonEmptyContactsProviderIterator(localCursor, this.mDataHandler);
    }
    
    public void close()
    {
      this.mCurrentIterator.close();
    }
    
    public boolean hasNext()
    {
      ensureCurrent();
      return this.mCurrentIterator.hasNext();
    }
    
    public ContentValues next()
    {
      ensureCurrent();
      return (ContentValues)this.mCurrentIterator.next();
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
  static final class ContactBuilder
  {
    private long mContactId;
    private String mDisplayName;
    private final StringBuilder mEmails = new StringBuilder();
    private final StringBuilder mEmailsLabels = new StringBuilder();
    private final StringBuilder mEmailsTypes = new StringBuilder();
    private final StringBuilder mGivenNames = new StringBuilder();
    private String mIconUri;
    private String mLookupKey;
    private final StringBuilder mNicknames = new StringBuilder();
    private final StringBuilder mNotes = new StringBuilder();
    private final StringBuilder mOrganizations = new StringBuilder();
    private final StringBuilder mPhoneNumbers = new StringBuilder();
    private final StringBuilder mPhoneNumbersLabels = new StringBuilder();
    private final StringBuilder mPhoneNumbersTypes = new StringBuilder();
    private final StringBuilder mPostalAddresses = new StringBuilder();
    private final StringBuilder mPostalAddressesLabels = new StringBuilder();
    private final StringBuilder mPostalAddressesTypes = new StringBuilder();
    private long mScore;
    private int mTimesContacted;
    
    private void addGenericRepeatedField(StringBuilder paramStringBuilder, String paramString)
    {
      if (paramString != null) {
        paramStringBuilder.append(paramString);
      }
      paramStringBuilder.append("");
    }
    
    private static String getValue(StringBuilder paramStringBuilder)
    {
      int i = "".length();
      if (paramStringBuilder.length() > i) {
        return paramStringBuilder.substring(0, paramStringBuilder.length() - i);
      }
      return null;
    }
    
    public void addEmail(String paramString1, String paramString2, String paramString3)
    {
      addGenericRepeatedField(this.mEmails, paramString1);
      addGenericRepeatedField(this.mEmailsTypes, paramString2);
      addGenericRepeatedField(this.mEmailsLabels, paramString3);
    }
    
    public void addGivenName(String paramString)
    {
      addGenericRepeatedField(this.mGivenNames, paramString);
    }
    
    public void addNickname(String paramString)
    {
      addGenericRepeatedField(this.mNicknames, paramString);
    }
    
    public void addNote(String paramString)
    {
      addGenericRepeatedField(this.mNotes, paramString);
    }
    
    public void addOrganization(String paramString)
    {
      addGenericRepeatedField(this.mOrganizations, paramString);
    }
    
    public void addPhoneNumber(String paramString1, String paramString2, String paramString3)
    {
      addGenericRepeatedField(this.mPhoneNumbers, paramString1);
      addGenericRepeatedField(this.mPhoneNumbersTypes, paramString2);
      addGenericRepeatedField(this.mPhoneNumbersLabels, paramString3);
    }
    
    public void addPostalAddress(String paramString1, String paramString2, String paramString3)
    {
      addGenericRepeatedField(this.mPostalAddresses, paramString1);
      addGenericRepeatedField(this.mPostalAddressesTypes, paramString2);
      addGenericRepeatedField(this.mPostalAddressesLabels, paramString3);
    }
    
    public void clear()
    {
      this.mGivenNames.setLength(0);
      this.mEmails.setLength(0);
      this.mEmailsTypes.setLength(0);
      this.mEmailsLabels.setLength(0);
      this.mPhoneNumbers.setLength(0);
      this.mPhoneNumbersTypes.setLength(0);
      this.mPhoneNumbersLabels.setLength(0);
      this.mNicknames.setLength(0);
      this.mNotes.setLength(0);
      this.mOrganizations.setLength(0);
      this.mPostalAddresses.setLength(0);
      this.mPostalAddressesTypes.setLength(0);
      this.mPostalAddressesLabels.setLength(0);
      this.mContactId = 0L;
      this.mLookupKey = null;
      this.mDisplayName = null;
      this.mIconUri = null;
      this.mTimesContacted = 0;
      this.mScore = 0L;
    }
    
    public ContentValues getContentValues()
    {
      return ContactsHelper.buildContentValues(this.mContactId, this.mLookupKey, this.mIconUri, this.mDisplayName, getValue(this.mGivenNames), this.mTimesContacted, this.mScore, getValue(this.mEmails), getValue(this.mEmailsTypes), getValue(this.mEmailsLabels), getValue(this.mNicknames), getValue(this.mNotes), getValue(this.mOrganizations), getValue(this.mPhoneNumbers), getValue(this.mPhoneNumbersTypes), getValue(this.mPhoneNumbersLabels), getValue(this.mPostalAddresses), getValue(this.mPostalAddressesTypes), getValue(this.mPostalAddressesLabels));
    }
    
    public void setContactId(long paramLong)
    {
      this.mContactId = paramLong;
    }
    
    public void setDisplayName(String paramString)
    {
      this.mDisplayName = paramString;
    }
    
    public void setIconUri(String paramString)
    {
      this.mIconUri = paramString;
    }
    
    public void setLookupKey(String paramString)
    {
      this.mLookupKey = paramString;
    }
    
    public void setScore(long paramLong)
    {
      this.mScore = paramLong;
    }
    
    public void setTimesContacted(int paramInt)
    {
      this.mTimesContacted = paramInt;
    }
  }
  
  private static abstract class ContactIteratorBase
    implements Iterator<ContentValues>
  {
    private ContentValues mNext;
    
    private final void ensureNext()
    {
      if (this.mNext == null) {
        this.mNext = getNext();
      }
    }
    
    protected abstract ContentValues getNext();
    
    public final boolean hasNext()
    {
      ensureNext();
      return this.mNext != null;
    }
    
    public final ContentValues next()
    {
      ensureNext();
      ContentValues localContentValues = this.mNext;
      if (localContentValues == null) {
        throw new NoSuchElementException();
      }
      this.mNext = null;
      return localContentValues;
    }
    
    public final void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
  static abstract interface Contacts
  {
    public static abstract interface Columns
    {
      public static final String[] ALL_COLUMNS = { "_id", "contact_id", "lookup_key", "icon_uri", "display_name", "given_names", "times_contacted", "score", "emails", "emails_types", "emails_labels", "nickname", "note", "organization", "phone_numbers", "phone_number_types", "phone_number_labels", "postal_address", "postal_address_types", "postal_address_labels" };
    }
  }
  
  private static final class ContactsDatabaseIterator
    extends ContactsHelper.ContactIteratorBase
    implements ContactsHelper.CursorIterator
  {
    private final int mContactIdIndex;
    private final Cursor mCursor;
    private final int mDisplayNameIndex;
    private final int mEmailIndex;
    private final int mEmailLabelsIndex;
    private final int mEmailTypesIndex;
    private final int mGivenNamesIndex;
    private final int mIconUriIndex;
    private final int mLookupKeyIndex;
    private final int mNicknameIndex;
    private final int mNoteIndex;
    private final int mOrganizationIndex;
    private final int mPhoneNumberIndex;
    private final int mPhoneNumberLabelsIndex;
    private final int mPhoneNumberTypesIndex;
    private final int mPostalAddressIndex;
    private final int mPostalAddressLabelsIndex;
    private final int mPostalAddressTypesIndex;
    private final int mScoreIndex;
    private final int mTimesContactedIndex;
    
    public ContactsDatabaseIterator(SQLiteDatabase paramSQLiteDatabase, String paramString)
    {
      super();
      this.mCursor = paramSQLiteDatabase.query("contacts", ContactsHelper.Contacts.Columns.ALL_COLUMNS, paramString, null, null, null, null);
      this.mContactIdIndex = this.mCursor.getColumnIndex("contact_id");
      this.mLookupKeyIndex = this.mCursor.getColumnIndex("lookup_key");
      this.mIconUriIndex = this.mCursor.getColumnIndex("icon_uri");
      this.mDisplayNameIndex = this.mCursor.getColumnIndex("display_name");
      this.mGivenNamesIndex = this.mCursor.getColumnIndex("given_names");
      this.mTimesContactedIndex = this.mCursor.getColumnIndex("times_contacted");
      this.mScoreIndex = this.mCursor.getColumnIndex("score");
      this.mEmailIndex = this.mCursor.getColumnIndex("emails");
      this.mEmailTypesIndex = this.mCursor.getColumnIndex("emails_types");
      this.mEmailLabelsIndex = this.mCursor.getColumnIndex("emails_labels");
      this.mNicknameIndex = this.mCursor.getColumnIndex("nickname");
      this.mNoteIndex = this.mCursor.getColumnIndex("note");
      this.mOrganizationIndex = this.mCursor.getColumnIndex("organization");
      this.mPhoneNumberIndex = this.mCursor.getColumnIndex("phone_numbers");
      this.mPhoneNumberTypesIndex = this.mCursor.getColumnIndex("phone_number_types");
      this.mPhoneNumberLabelsIndex = this.mCursor.getColumnIndex("phone_number_labels");
      this.mPostalAddressIndex = this.mCursor.getColumnIndex("postal_address");
      this.mPostalAddressTypesIndex = this.mCursor.getColumnIndex("postal_address_types");
      this.mPostalAddressLabelsIndex = this.mCursor.getColumnIndex("postal_address_labels");
    }
    
    public void close()
    {
      this.mCursor.close();
    }
    
    protected ContentValues getNext()
    {
      if (this.mCursor.moveToNext()) {
        return ContactsHelper.buildContentValues(this.mCursor.getLong(this.mContactIdIndex), this.mCursor.getString(this.mLookupKeyIndex), this.mCursor.getString(this.mIconUriIndex), this.mCursor.getString(this.mDisplayNameIndex), this.mCursor.getString(this.mGivenNamesIndex), this.mCursor.getInt(this.mTimesContactedIndex), this.mCursor.getLong(this.mScoreIndex), this.mCursor.getString(this.mEmailIndex), this.mCursor.getString(this.mEmailTypesIndex), this.mCursor.getString(this.mEmailLabelsIndex), this.mCursor.getString(this.mNicknameIndex), this.mCursor.getString(this.mNoteIndex), this.mCursor.getString(this.mOrganizationIndex), this.mCursor.getString(this.mPhoneNumberIndex), this.mCursor.getString(this.mPhoneNumberTypesIndex), this.mCursor.getString(this.mPhoneNumberLabelsIndex), this.mCursor.getString(this.mPostalAddressIndex), this.mCursor.getString(this.mPostalAddressTypesIndex), this.mCursor.getString(this.mPostalAddressLabelsIndex));
      }
      return null;
    }
  }
  
  private static abstract interface CursorIterator
    extends Iterator<ContentValues>
  {
    public abstract void close();
  }
  
  private static final class EmptyContactsProviderIterator
    implements ContactsHelper.CursorIterator
  {
    public void close() {}
    
    public boolean hasNext()
    {
      return false;
    }
    
    public ContentValues next()
    {
      throw new NoSuchElementException();
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
  private static final class NonEmptyContactsProviderIterator
    extends ContactsHelper.ContactIteratorBase
    implements ContactsHelper.CursorIterator
  {
    private final ContactsHelper.ContactBuilder mBuilder;
    private final int mContactIdIndex;
    private long mCurrentContactId;
    private final Cursor mCursor;
    private final ContactsDataHandler mDataHandler;
    private final int mDisplayNameIndex;
    private final int mLastTimeContactedIndex;
    private final int mLookupKeyIndex;
    private final int mStarredIndex;
    private final int mThumbnailUriIndex;
    private final int mTimesContactedIndex;
    
    public NonEmptyContactsProviderIterator(Cursor paramCursor, ContactsDataHandler paramContactsDataHandler)
    {
      super();
      this.mCursor = ((Cursor)Preconditions.checkNotNull(paramCursor));
      this.mDataHandler = paramContactsDataHandler;
      this.mDataHandler.setCursor(this.mCursor);
      this.mBuilder = new ContactsHelper.ContactBuilder();
      this.mContactIdIndex = paramCursor.getColumnIndex("contact_id");
      this.mLookupKeyIndex = paramCursor.getColumnIndex("lookup");
      this.mThumbnailUriIndex = paramCursor.getColumnIndex("photo_thumb_uri");
      this.mDisplayNameIndex = paramCursor.getColumnIndex("display_name");
      this.mTimesContactedIndex = paramCursor.getColumnIndex("times_contacted");
      this.mLastTimeContactedIndex = paramCursor.getColumnIndex("last_time_contacted");
      this.mStarredIndex = paramCursor.getColumnIndex("starred");
      this.mCurrentContactId = -1L;
    }
    
    public void close()
    {
      this.mCursor.close();
    }
    
    protected ContentValues getNext()
    {
      ContentValues localContentValues = null;
      while ((localContentValues == null) && (this.mCursor.moveToNext()))
      {
        long l1 = this.mCursor.getLong(this.mContactIdIndex);
        if (l1 != this.mCurrentContactId)
        {
          if (this.mCurrentContactId != -1L)
          {
            localContentValues = this.mBuilder.getContentValues();
            this.mBuilder.clear();
          }
          this.mCurrentContactId = l1;
          this.mBuilder.setContactId(this.mCurrentContactId);
          this.mBuilder.setLookupKey(this.mCursor.getString(this.mLookupKeyIndex));
          this.mBuilder.setIconUri(this.mCursor.getString(this.mThumbnailUriIndex));
          this.mBuilder.setDisplayName(this.mCursor.getString(this.mDisplayNameIndex));
          int i = this.mCursor.getInt(this.mTimesContactedIndex);
          this.mCursor.getLong(this.mLastTimeContactedIndex);
          this.mCursor.getInt(this.mStarredIndex);
          this.mBuilder.setTimesContacted(i);
          long l2 = i + 1;
          this.mBuilder.setScore(l2);
        }
        this.mDataHandler.handleCurrentRow(this.mBuilder);
      }
      if ((localContentValues == null) && (this.mCursor.isAfterLast()) && (this.mCurrentContactId != -1L))
      {
        localContentValues = this.mBuilder.getContentValues();
        this.mBuilder.clear();
        this.mCurrentContactId = -1L;
      }
      return localContentValues;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.ContactsHelper
 * JD-Core Version:    0.7.0.1
 */