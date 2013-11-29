package com.google.android.voicesearch.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Attendees;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.util.SimpleCallbackFuture;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.helper.AccountHelper;
import com.google.android.velvet.actions.Disambiguation.Candidate;
import com.google.common.base.Preconditions;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CalendarHelper
{
  private static final String[] PROJECTION = { "_id" };
  private final AccountHelper mAccountHelper;
  private final Executor mBackgroundExecutor;
  private final ContentResolver mContentResolver;
  private final boolean mEnableAttendees;
  private final Executor mUiThreadExecutor;
  
  public CalendarHelper(AccountHelper paramAccountHelper, ContentResolver paramContentResolver, Executor paramExecutor1, Executor paramExecutor2)
  {
    this.mAccountHelper = paramAccountHelper;
    this.mContentResolver = paramContentResolver;
    this.mUiThreadExecutor = paramExecutor1;
    this.mBackgroundExecutor = paramExecutor2;
    this.mEnableAttendees = false;
  }
  
  public static Reminder createDefaultReminder()
  {
    return new Reminder(15, 0);
  }
  
  public static Intent createViewCalendarIntent(long paramLong)
  {
    Uri.Builder localBuilder = CalendarContract.CONTENT_URI.buildUpon();
    localBuilder.appendPath("time");
    ContentUris.appendId(localBuilder, paramLong);
    return new Intent("android.intent.action.VIEW").setData(localBuilder.build());
  }
  
  public static Intent createViewEventIntent(long paramLong)
  {
    Uri localUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, paramLong);
    return new Intent("android.intent.action.VIEW").setData(localUri);
  }
  
  private long getCalendarId(String paramString1, String paramString2)
  {
    Preconditions.checkNotNull(paramString1);
    if (paramString2 == null) {
      paramString2 = paramString1;
    }
    Cursor localCursor = this.mContentResolver.query(CalendarContract.Calendars.CONTENT_URI, PROJECTION, "((account_name = ?) AND (account_type = ?) AND (ownerAccount = ?))", new String[] { paramString2, "com.google", paramString1 }, null);
    if (localCursor == null) {
      return -1L;
    }
    if (!localCursor.moveToFirst())
    {
      localCursor.close();
      return -1L;
    }
    long l = localCursor.getLong(0);
    localCursor.close();
    return l;
  }
  
  public static long getDefaultEndTimeMs(long paramLong)
  {
    if (paramLong == 0L) {
      return 0L;
    }
    return 3600000L + paramLong;
  }
  
  public static long getDefaultStartTimeMs(long paramLong)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    localCalendar.set(12, 0);
    localCalendar.set(13, 0);
    localCalendar.set(14, 0);
    localCalendar.add(10, 1);
    return localCalendar.getTimeInMillis();
  }
  
  private void insertAttendees(long paramLong, List<Attendee> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Attendee localAttendee = (Attendee)localIterator.next();
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("attendeeName", localAttendee.getName());
      localContentValues.put("attendeeEmail", localAttendee.getEmailAddress());
      localContentValues.put("attendeeRelationship", Integer.valueOf(1));
      localContentValues.put("attendeeType", Integer.valueOf(2));
      localContentValues.put("attendeeStatus", Integer.valueOf(3));
      localContentValues.put("event_id", Long.valueOf(paramLong));
      this.mContentResolver.insert(CalendarContract.Attendees.CONTENT_URI, localContentValues);
    }
  }
  
  private void insertReminders(long paramLong, List<Reminder> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Reminder localReminder = (Reminder)localIterator.next();
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("event_id", Long.valueOf(paramLong));
      localContentValues.put("minutes", Integer.valueOf(localReminder.getMinutesInAdvance()));
      localContentValues.put("method", Integer.valueOf(localReminder.getMethod()));
      this.mContentResolver.insert(CalendarContract.Reminders.CONTENT_URI, localContentValues);
    }
  }
  
  public boolean addEvent(String paramString1, String paramString2, String paramString3, String paramString4, long paramLong1, long paramLong2, List<Attendee> paramList, List<Reminder> paramList1)
  {
    if (paramString1 == null)
    {
      SimpleCallbackFuture localSimpleCallbackFuture = new SimpleCallbackFuture();
      this.mAccountHelper.getMainGmailAccount(localSimpleCallbackFuture);
      String str;
      try
      {
        str = (String)localSimpleCallbackFuture.get();
        if (str == null) {
          return false;
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        Log.e("CalendarHelper", "Can't get account.", localInterruptedException);
        return false;
      }
      catch (ExecutionException localExecutionException)
      {
        Log.e("CalendarHelper", "Can't get account.", localExecutionException);
        return false;
      }
      paramString1 = str;
    }
    long l = getCalendarId(paramString1, paramString2);
    if (l != -1L)
    {
      insertEvent(l, paramString3, paramString4, paramLong1, paramLong2, paramList, paramList1);
      return true;
    }
    return false;
  }
  
  public Intent createAddEventIntent(String paramString1, String paramString2, long paramLong1, long paramLong2, List<Attendee> paramList)
  {
    Intent localIntent = new Intent("android.intent.action.INSERT").setData(CalendarContract.Events.CONTENT_URI);
    if (paramString1 != null) {
      localIntent.putExtra("title", paramString1);
    }
    if (paramString2 != null) {
      localIntent.putExtra("eventLocation", paramString2);
    }
    if (paramLong1 != 0L) {
      localIntent.putExtra("beginTime", paramLong1);
    }
    if (paramLong2 != 0L) {
      localIntent.putExtra("endTime", paramLong2);
    }
    if ((this.mEnableAttendees) && (paramList != null) && (!paramList.isEmpty()))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        Attendee localAttendee = (Attendee)localIterator.next();
        if (localStringBuilder.length() > 0) {
          localStringBuilder.append(',');
        }
        localStringBuilder.append(localAttendee.getEmailAddress());
      }
      localIntent.putExtra("android.intent.extra.EMAIL", localStringBuilder.toString());
    }
    return localIntent;
  }
  
  void insertEvent(long paramLong1, String paramString1, String paramString2, long paramLong2, long paramLong3, List<Attendee> paramList, List<Reminder> paramList1)
  {
    ExtraPreconditions.checkNotMainThread();
    Preconditions.checkNotNull(paramString1);
    boolean bool1;
    if (paramLong2 != 0L)
    {
      bool1 = true;
      Preconditions.checkArgument(bool1);
      if (paramLong3 == 0L) {
        break label173;
      }
    }
    label173:
    for (boolean bool2 = true;; bool2 = false)
    {
      Preconditions.checkArgument(bool2);
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("calendar_id", Long.valueOf(paramLong1));
      localContentValues.put("dtstart", Long.valueOf(paramLong2));
      localContentValues.put("dtend", Long.valueOf(paramLong3));
      localContentValues.put("title", paramString1);
      if (paramString2 != null) {
        localContentValues.put("eventLocation", paramString2);
      }
      localContentValues.put("eventTimezone", TimeZone.getDefault().getID());
      long l = Long.parseLong(this.mContentResolver.insert(CalendarContract.Events.CONTENT_URI, localContentValues).getLastPathSegment());
      if (this.mEnableAttendees) {
        insertAttendees(l, paramList);
      }
      insertReminders(l, paramList1);
      return;
      bool1 = false;
      break;
    }
  }
  
  public static class Attendee
  {
    private final String mEmailAddress;
    private final String mName;
    
    public String getEmailAddress()
    {
      return this.mEmailAddress;
    }
    
    public String getName()
    {
      return this.mName;
    }
  }
  
  public static class CalendarEvent
    implements Parcelable, Disambiguation.Candidate<CalendarEvent>
  {
    public static final Parcelable.Creator<CalendarEvent> CREATOR = new Parcelable.Creator()
    {
      public CalendarHelper.CalendarEvent createFromParcel(Parcel paramAnonymousParcel)
      {
        return new CalendarHelper.CalendarEvent(paramAnonymousParcel.readLong(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong());
      }
      
      public CalendarHelper.CalendarEvent[] newArray(int paramAnonymousInt)
      {
        return new CalendarHelper.CalendarEvent[paramAnonymousInt];
      }
    };
    private final long mEndTimeMs;
    private final long mId;
    private final String mLocation;
    private final long mStartTimeMs;
    private final String mSummary;
    
    public CalendarEvent(long paramLong1, String paramString1, String paramString2, long paramLong2, long paramLong3)
    {
      this.mId = paramLong1;
      this.mSummary = paramString1;
      this.mLocation = paramString2;
      this.mStartTimeMs = paramLong2;
      this.mEndTimeMs = paramLong3;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public long getEndTimeMs()
    {
      return this.mEndTimeMs;
    }
    
    public long getId()
    {
      return this.mId;
    }
    
    public String getLocation()
    {
      return this.mLocation;
    }
    
    public CalendarEvent getSelectedItem()
    {
      return this;
    }
    
    public long getStartTimeMs()
    {
      return this.mStartTimeMs;
    }
    
    public String getSummary()
    {
      return this.mSummary;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeLong(this.mId);
      paramParcel.writeString(this.mSummary);
      paramParcel.writeString(this.mLocation);
      paramParcel.writeLong(this.mStartTimeMs);
      paramParcel.writeLong(this.mEndTimeMs);
    }
  }
  
  class EventAdder
    implements Runnable
  {
    final List<CalendarHelper.Attendee> mAttendees;
    final String mCalendar;
    final SimpleCallback<Boolean> mCallback;
    final long mEndTimeMs;
    final String mLocation;
    final String mOwner;
    final List<CalendarHelper.Reminder> mReminders;
    final long mStartTimeMs;
    final String mSummary;
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof EventAdder;
      boolean bool2 = false;
      if (bool1)
      {
        EventAdder localEventAdder = (EventAdder)paramObject;
        boolean bool3 = TextUtils.equals(this.mOwner, localEventAdder.mOwner);
        bool2 = false;
        if (bool3)
        {
          boolean bool4 = TextUtils.equals(this.mCalendar, localEventAdder.mCalendar);
          bool2 = false;
          if (bool4)
          {
            boolean bool5 = TextUtils.equals(this.mSummary, localEventAdder.mSummary);
            bool2 = false;
            if (bool5)
            {
              boolean bool6 = TextUtils.equals(this.mLocation, localEventAdder.mLocation);
              bool2 = false;
              if (bool6)
              {
                boolean bool7 = this.mStartTimeMs < localEventAdder.mStartTimeMs;
                bool2 = false;
                if (!bool7)
                {
                  boolean bool8 = this.mEndTimeMs < localEventAdder.mEndTimeMs;
                  bool2 = false;
                  if (!bool8) {
                    if (this.mAttendees != localEventAdder.mAttendees)
                    {
                      List localList2 = this.mAttendees;
                      bool2 = false;
                      if (localList2 != null)
                      {
                        boolean bool10 = this.mAttendees.equals(localEventAdder.mAttendees);
                        bool2 = false;
                        if (!bool10) {}
                      }
                    }
                    else if (this.mReminders != localEventAdder.mReminders)
                    {
                      List localList1 = this.mReminders;
                      bool2 = false;
                      if (localList1 != null)
                      {
                        boolean bool9 = this.mReminders.equals(localEventAdder.mReminders);
                        bool2 = false;
                        if (!bool9) {}
                      }
                    }
                    else
                    {
                      SimpleCallback localSimpleCallback1 = this.mCallback;
                      SimpleCallback localSimpleCallback2 = localEventAdder.mCallback;
                      bool2 = false;
                      if (localSimpleCallback1 == localSimpleCallback2) {
                        bool2 = true;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      return bool2;
    }
    
    public void run()
    {
      final long l = this.this$0.getCalendarId(this.mOwner, this.mCalendar);
      if (l != -1L) {
        this.this$0.insertEvent(l, this.mSummary, this.mLocation, this.mStartTimeMs, this.mEndTimeMs, this.mAttendees, this.mReminders);
      }
      if (this.mCallback != null) {
        this.this$0.mUiThreadExecutor.execute(new Runnable()
        {
          public void run()
          {
            SimpleCallback localSimpleCallback = CalendarHelper.EventAdder.this.mCallback;
            if (l != -1L) {}
            for (boolean bool = true;; bool = false)
            {
              localSimpleCallback.onResult(Boolean.valueOf(bool));
              return;
            }
          }
        });
      }
    }
  }
  
  public static class Reminder
    implements Parcelable
  {
    public static final Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator()
    {
      public CalendarHelper.Reminder createFromParcel(Parcel paramAnonymousParcel)
      {
        return new CalendarHelper.Reminder(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
      }
      
      public CalendarHelper.Reminder[] newArray(int paramAnonymousInt)
      {
        return new CalendarHelper.Reminder[paramAnonymousInt];
      }
    };
    private final int mMethod;
    private final int mMinutesInAdvance;
    
    public Reminder(int paramInt1, int paramInt2)
    {
      this.mMinutesInAdvance = paramInt1;
      this.mMethod = paramInt2;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getMethod()
    {
      return this.mMethod;
    }
    
    public int getMinutesInAdvance()
    {
      return this.mMinutesInAdvance;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(this.mMinutesInAdvance);
      paramParcel.writeInt(this.mMethod);
    }
    
    public static class Builder
    {
      private int mMethod = 0;
      private int mMinutesInAdvance = 15;
      
      public CalendarHelper.Reminder build()
      {
        return new CalendarHelper.Reminder(this.mMinutesInAdvance, this.mMethod);
      }
      
      public void setMethod(int paramInt)
      {
        this.mMethod = paramInt;
      }
      
      public void setMinutesInAdvance(int paramInt)
      {
        this.mMinutesInAdvance = paramInt;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.util.CalendarHelper
 * JD-Core Version:    0.7.0.1
 */