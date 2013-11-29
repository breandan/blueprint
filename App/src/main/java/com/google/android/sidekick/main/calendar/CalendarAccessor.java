package com.google.android.sidekick.main.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.net.Uri.Builder;
import android.provider.CalendarContract.Attendees;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import android.util.Log;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarInfo;
import com.google.android.apps.sidekick.calendar.Calendar.EventData;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.Whitespace;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.util.Cursors;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

class CalendarAccessor
{
  static final int ACCOUNT_NAME_IDX = 1;
  private static final String[] ATTENDEES_QUERY_PROJECTION = { "attendeeName" };
  private static final String[] CALENDARS_LIST_QUERY_PROJECTION = { "_id", "account_name", "calendar_displayName", "name", "cal_sync1" };
  private static final String[] CALENDAR_IDS_QUERY_PROJECTION = { "_id", "calendar_id" };
  private static Pattern CALENDAR_URI_PATTERN = null;
  static final int CAL_SYNC1_IDX = 4;
  static final int DISPLAY_NAME_IDX = 2;
  static final String FALLBACK_ID_FMT = "%s_%s";
  static final int ID_IDX = 0;
  static final int NAME_IDX = 3;
  private static final String TAG = Tag.getTag(CalendarAccessor.class);
  private static final String[] UPCOMING_EVENTS_QUERY_PROJECTION = { "event_id", "title", "eventLocation", "begin", "end", "accessLevel", "selfAttendeeStatus" };
  private final Context mAppContext;
  private final AtomicBoolean mGettingEventsFailed = new AtomicBoolean();
  
  public CalendarAccessor(Context paramContext)
  {
    this.mAppContext = paramContext.getApplicationContext();
  }
  
  /* Error */
  private void addCalendarDbIdToEvents(ContentResolver paramContentResolver, Collection<Calendar.EventData> paramCollection)
  {
    // Byte code:
    //   0: aload_2
    //   1: ifnull +12 -> 13
    //   4: aload_2
    //   5: invokeinterface 106 1 0
    //   10: ifeq +4 -> 14
    //   13: return
    //   14: invokestatic 112	com/google/common/collect/Sets:newHashSet	()Ljava/util/HashSet;
    //   17: astore_3
    //   18: aload_2
    //   19: invokeinterface 116 1 0
    //   24: astore 4
    //   26: aload 4
    //   28: invokeinterface 121 1 0
    //   33: ifeq +29 -> 62
    //   36: aload_3
    //   37: aload 4
    //   39: invokeinterface 125 1 0
    //   44: checkcast 127	com/google/android/apps/sidekick/calendar/Calendar$EventData
    //   47: invokevirtual 131	com/google/android/apps/sidekick/calendar/Calendar$EventData:getEventId	()J
    //   50: invokestatic 137	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   53: invokeinterface 143 2 0
    //   58: pop
    //   59: goto -33 -> 26
    //   62: bipush 44
    //   64: invokestatic 149	com/google/common/base/Joiner:on	(C)Lcom/google/common/base/Joiner;
    //   67: aload_3
    //   68: invokevirtual 153	com/google/common/base/Joiner:join	(Ljava/lang/Iterable;)Ljava/lang/String;
    //   71: astore 5
    //   73: aconst_null
    //   74: astore 6
    //   76: invokestatic 159	com/google/common/collect/Maps:newHashMap	()Ljava/util/HashMap;
    //   79: astore 7
    //   81: aload_0
    //   82: aload_1
    //   83: getstatic 165	android/provider/CalendarContract$Events:CONTENT_URI	Landroid/net/Uri;
    //   86: getstatic 79	com/google/android/sidekick/main/calendar/CalendarAccessor:CALENDAR_IDS_QUERY_PROJECTION	[Ljava/lang/String;
    //   89: getstatic 171	java/util/Locale:US	Ljava/util/Locale;
    //   92: ldc 173
    //   94: iconst_1
    //   95: anewarray 4	java/lang/Object
    //   98: dup
    //   99: iconst_0
    //   100: aload 5
    //   102: aastore
    //   103: invokestatic 177	java/lang/String:format	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   106: invokespecial 181	com/google/android/sidekick/main/calendar/CalendarAccessor:executeQuery	(Landroid/content/ContentResolver;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   109: astore 6
    //   111: aload 6
    //   113: ifnull +57 -> 170
    //   116: aload 6
    //   118: invokeinterface 186 1 0
    //   123: ifeq +47 -> 170
    //   126: aload 7
    //   128: aload 6
    //   130: iconst_0
    //   131: invokeinterface 190 2 0
    //   136: invokestatic 137	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   139: aload 6
    //   141: iconst_1
    //   142: invokeinterface 190 2 0
    //   147: invokestatic 137	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   150: invokeinterface 196 3 0
    //   155: pop
    //   156: aload 6
    //   158: invokeinterface 199 1 0
    //   163: istore 10
    //   165: iload 10
    //   167: ifne -41 -> 126
    //   170: aload 6
    //   172: invokestatic 205	com/google/android/velvet/util/Cursors:closeQuietly	(Landroid/database/Cursor;)V
    //   175: aload 7
    //   177: invokeinterface 206 1 0
    //   182: ifne -169 -> 13
    //   185: aload_2
    //   186: invokeinterface 116 1 0
    //   191: astore 11
    //   193: aload 11
    //   195: invokeinterface 121 1 0
    //   200: ifeq -187 -> 13
    //   203: aload 11
    //   205: invokeinterface 125 1 0
    //   210: checkcast 127	com/google/android/apps/sidekick/calendar/Calendar$EventData
    //   213: astore 12
    //   215: aload 7
    //   217: aload 12
    //   219: invokevirtual 131	com/google/android/apps/sidekick/calendar/Calendar$EventData:getEventId	()J
    //   222: invokestatic 137	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   225: invokeinterface 210 2 0
    //   230: checkcast 133	java/lang/Long
    //   233: astore 13
    //   235: aload 13
    //   237: ifnull -44 -> 193
    //   240: aload 12
    //   242: aload 13
    //   244: invokevirtual 213	java/lang/Long:longValue	()J
    //   247: invokevirtual 217	com/google/android/apps/sidekick/calendar/Calendar$EventData:setCalendarDbId	(J)Lcom/google/android/apps/sidekick/calendar/Calendar$EventData;
    //   250: pop
    //   251: goto -58 -> 193
    //   254: astore 8
    //   256: aload 6
    //   258: invokestatic 205	com/google/android/velvet/util/Cursors:closeQuietly	(Landroid/database/Cursor;)V
    //   261: aload 8
    //   263: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	264	0	this	CalendarAccessor
    //   0	264	1	paramContentResolver	ContentResolver
    //   0	264	2	paramCollection	Collection<Calendar.EventData>
    //   17	51	3	localHashSet	java.util.HashSet
    //   24	14	4	localIterator1	java.util.Iterator
    //   71	30	5	str	String
    //   74	183	6	localCursor	Cursor
    //   79	137	7	localHashMap	java.util.HashMap
    //   254	8	8	localObject	Object
    //   163	3	10	bool	boolean
    //   191	13	11	localIterator2	java.util.Iterator
    //   213	28	12	localEventData	Calendar.EventData
    //   233	10	13	localLong	Long
    // Exception table:
    //   from	to	target	type
    //   81	111	254	finally
    //   116	126	254	finally
    //   126	165	254	finally
  }
  
  private Cursor calendarsListQuery(ContentResolver paramContentResolver)
  {
    return executeQuery(paramContentResolver, CalendarContract.Calendars.CONTENT_URI, CALENDARS_LIST_QUERY_PROJECTION, "(visible=1) AND (calendar_access_level=700)");
  }
  
  private boolean containsAttendees(String paramString, List<String> paramList)
  {
    ArrayList localArrayList = Lists.newArrayList(paramString.split("\\s*,\\s*"));
    localArrayList.retainAll(paramList);
    return !localArrayList.isEmpty();
  }
  
  @Nullable
  private Cursor executeQuery(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString, String paramString)
  {
    try
    {
      Cursor localCursor = paramContentResolver.query(paramUri, paramArrayOfString, paramString, null, null);
      return localCursor;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localObject = localIllegalArgumentException;
      Log.w(TAG, "Calendar ContentProvider query failed: " + ((Exception)localObject).getMessage());
      return null;
    }
    catch (NullPointerException localNullPointerException)
    {
      for (;;)
      {
        localObject = localNullPointerException;
      }
    }
    catch (SQLException localSQLException)
    {
      for (;;)
      {
        localObject = localSQLException;
      }
    }
    catch (SecurityException localSecurityException)
    {
      for (;;)
      {
        localObject = localSecurityException;
      }
    }
    catch (Exception localException)
    {
      for (;;)
      {
        Object localObject = localException;
      }
    }
  }
  
  private List<String> getAttendeeNames(long paramLong)
  {
    ContentResolver localContentResolver = this.mAppContext.getContentResolver();
    Cursor localCursor = null;
    ArrayList localArrayList = Lists.newArrayList();
    try
    {
      Uri localUri = CalendarContract.Attendees.CONTENT_URI;
      String[] arrayOfString = ATTENDEES_QUERY_PROJECTION;
      Locale localLocale = Locale.US;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Long.valueOf(paramLong);
      localCursor = executeQuery(localContentResolver, localUri, arrayOfString, String.format(localLocale, "(event_id=%d)", arrayOfObject));
      if ((localCursor != null) && (localCursor.moveToFirst()))
      {
        boolean bool;
        do
        {
          localArrayList.add(localCursor.getString(0));
          bool = localCursor.moveToNext();
        } while (bool);
      }
      return localArrayList;
    }
    finally
    {
      Cursors.closeQuietly(localCursor);
    }
  }
  
  public static boolean isEmptyOrWhitespace(String paramString)
  {
    return (paramString == null) || (Whitespace.matchesAllOf(paramString));
  }
  
  static List<Calendar.CalendarInfo> readCalendarsFromCursor(@Nullable Cursor paramCursor)
  {
    if ((paramCursor == null) || (!paramCursor.moveToFirst())) {
      return ImmutableList.of();
    }
    ArrayList localArrayList = Lists.newArrayListWithCapacity(paramCursor.getCount());
    if (CALENDAR_URI_PATTERN == null) {
      CALENDAR_URI_PATTERN = Pattern.compile("^.*@(?:\\w[\\w-]*\\.)+\\w[\\w-]+$");
    }
    String str1 = paramCursor.getString(1);
    String str2 = paramCursor.getString(2);
    if (str2 == null) {
      str2 = paramCursor.getString(3);
    }
    String str3 = paramCursor.getString(4);
    if ((str3 != null) && (CALENDAR_URI_PATTERN.matcher(str3).matches())) {}
    for (String str4 = str3;; str4 = String.format("%s_%s", new Object[] { str2, str1 }))
    {
      localArrayList.add(new Calendar.CalendarInfo().setDbId(paramCursor.getLong(0)).setAccountOwner(Strings.nullToEmpty(str1)).setDisplayName(Strings.nullToEmpty(str2)).setId(str4));
      if (paramCursor.moveToNext()) {
        break;
      }
      return localArrayList;
    }
  }
  
  private Cursor upcomingEventsQuery(ContentResolver paramContentResolver, long paramLong, int paramInt)
  {
    Uri.Builder localBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
    ContentUris.appendId(localBuilder, paramLong);
    ContentUris.appendId(localBuilder, paramLong + paramInt);
    return executeQuery(paramContentResolver, localBuilder.build(), UPCOMING_EVENTS_QUERY_PROJECTION, "(visible=1) AND (calendar_access_level=700) AND (allDay=0) AND (selfAttendeeStatus!=2)");
  }
  
  public boolean didGettingEventsFail()
  {
    return this.mGettingEventsFailed.get();
  }
  
  public Collection<Calendar.CalendarInfo> getCalendarsList()
  {
    ExtraPreconditions.checkNotMainThread();
    ContentResolver localContentResolver = this.mAppContext.getContentResolver();
    Cursor localCursor = null;
    try
    {
      localCursor = calendarsListQuery(localContentResolver);
      List localList = readCalendarsFromCursor(localCursor);
      return localList;
    }
    finally
    {
      Cursors.closeQuietly(localCursor);
    }
  }
  
  public Collection<Calendar.EventData> getUpcomingEvents(long paramLong, int paramInt)
  {
    ExtraPreconditions.checkNotMainThread();
    ContentResolver localContentResolver = this.mAppContext.getContentResolver();
    Cursor localCursor = null;
    for (;;)
    {
      try
      {
        localCursor = upcomingEventsQuery(localContentResolver, paramLong, paramInt);
        AtomicBoolean localAtomicBoolean = this.mGettingEventsFailed;
        boolean bool1;
        if (localCursor == null)
        {
          bool1 = true;
          localAtomicBoolean.set(bool1);
          if ((localCursor == null) || (!localCursor.moveToFirst()))
          {
            ImmutableList localImmutableList = ImmutableList.of();
            return localImmutableList;
          }
        }
        else
        {
          bool1 = false;
          continue;
        }
        ArrayList localArrayList = Lists.newArrayListWithCapacity(localCursor.getCount());
        long l1 = localCursor.getLong(3) / 1000L;
        long l2 = l1 << 16 ^ localCursor.getLong(0);
        Calendar.EventData localEventData = new Calendar.EventData().setProviderId(l2).setEventId(localCursor.getLong(0)).setStartTimeSeconds(l1).setEndTimeSeconds(localCursor.getLong(4) / 1000L).setAccessLevel(localCursor.getInt(5)).setSelfAttendeeStatus(localCursor.getInt(6));
        String str1 = localCursor.getString(1);
        String str2 = localCursor.getString(2);
        if (!isEmptyOrWhitespace(str1))
        {
          localEventData.setTitle(str1);
          List localList = getAttendeeNames(localEventData.getEventId());
          localEventData.setNumberOfAttendees(localList.size());
          if (!isEmptyOrWhitespace(str2))
          {
            localEventData.setWhereField(str2);
            if (!containsAttendees(str2, localList))
            {
              bool2 = true;
              localEventData.setPotentiallyGeocodableWhereField(bool2);
            }
          }
          else
          {
            localArrayList.add(localEventData);
            if (localCursor.moveToNext()) {
              continue;
            }
            addCalendarDbIdToEvents(localContentResolver, localArrayList);
            return localArrayList;
          }
        }
        else
        {
          localEventData.setTitle(this.mAppContext.getString(2131362424));
          continue;
        }
        boolean bool2 = false;
      }
      finally
      {
        Cursors.closeQuietly(localCursor);
      }
    }
  }
  
  public void registerEventsObserver(ContentObserver paramContentObserver)
  {
    this.mAppContext.getContentResolver().registerContentObserver(CalendarContract.Events.CONTENT_URI, true, paramContentObserver);
  }
  
  public void unregisterEventsObserver(ContentObserver paramContentObserver)
  {
    this.mAppContext.getContentResolver().unregisterContentObserver(paramContentObserver);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.calendar.CalendarAccessor
 * JD-Core Version:    0.7.0.1
 */