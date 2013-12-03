package com.google.android.voicesearch.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import com.google.android.velvet.util.Cursors;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.majel.proto.CalendarProtos.AgendaItem;
import com.google.majel.proto.CalendarProtos.AgendaItemSubtitle;
import com.google.majel.proto.CalendarProtos.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

public class QueryCalendarUtil {
    private static final Comparator<CalendarProtos.AgendaItem> AGENDA_ITEM_ASCENDING = new Comparator() {
        public int compare(CalendarProtos.AgendaItem paramAnonymousAgendaItem1, CalendarProtos.AgendaItem paramAnonymousAgendaItem2) {
            int i = AgendaTimeUtil.compareDateTime(paramAnonymousAgendaItem1.getStartTime(), paramAnonymousAgendaItem2.getStartTime());
            if (i != 0) {
                return i;
            }
            return paramAnonymousAgendaItem1.getTitle().compareTo(paramAnonymousAgendaItem2.getTitle());
        }
    };
    private static final String[] ATTENDEES_QUERY_PROJECTION = {"attendeeName", "attendeeEmail"};
    private static final String[] EVENT_QUERY_PROJECTION = {"event_id", "title", "eventLocation", "begin", "end", "description", "calendar_displayName", "allDay", "startDay", "endDay", "hasAttendeeData"};

    private static void addAttendees(ContentResolver paramContentResolver, CalendarProtos.Event paramEvent) {
        Cursor localCursor = null;
        try {
            Uri localUri = CalendarContract.Attendees.CONTENT_URI;
            String[] arrayOfString = ATTENDEES_QUERY_PROJECTION;
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Long.valueOf(paramEvent.getEventId());
            localCursor = paramContentResolver.query(localUri, arrayOfString, String.format("(event_id=%d)", arrayOfObject), null, null);
            if (localCursor != null) {
                while (localCursor.moveToNext()) {
                    String str = localCursor.getString(localCursor.getColumnIndex("attendeeName"));
                    if (TextUtils.isEmpty(str)) {
                        str = localCursor.getString(localCursor.getColumnIndex("attendeeEmail"));
                    }
                    paramEvent.addAttendee(str);
                }
                paramEvent.setTotalAttendeeCount(paramEvent.getAttendeeCount());
            }
        } finally {
            Cursors.closeQuietly(localCursor);
        }
        Cursors.closeQuietly(localCursor);
    }

    static String[] getSelectionArgs(String[] paramArrayOfString, long paramLong, boolean paramBoolean) {
        int i = 1 + paramArrayOfString.length;
        if (!paramBoolean) {
            i += paramArrayOfString.length;
        }
        String[] arrayOfString = new String[i];
        arrayOfString[0] = Long.toString(paramLong);
        for (int j = 0; j < paramArrayOfString.length; j++) {
            arrayOfString[(j + 1)] = ('%' + paramArrayOfString[j] + '%');
            if (!paramBoolean) {
                arrayOfString[(j + 1 + paramArrayOfString.length)] = ('%' + paramArrayOfString[j] + '%');
            }
        }
        return arrayOfString;
    }

    static String getSelectionString(int paramInt, boolean paramBoolean) {
        StringBuilder localStringBuilder = new StringBuilder("(visible=1) AND (calendar_access_level=700) AND (selfAttendeeStatus!=2) AND end > ?");
        if (paramInt == 0) {
            return localStringBuilder.toString();
        }
        localStringBuilder.append(" AND (");
        localStringBuilder.append(getStringSelectors(paramInt, "title"));
        if (!paramBoolean) {
            localStringBuilder.append(" OR ");
            localStringBuilder.append(getStringSelectors(paramInt, "description"));
        }
        localStringBuilder.append(")");
        return localStringBuilder.toString();
    }

    private static String getStringSelectors(int paramInt, String paramString) {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < paramInt; i++) {
            localStringBuilder.append(paramString);
            localStringBuilder.append(" LIKE ?");
            if (i != paramInt - 1) {
                localStringBuilder.append(" AND ");
            }
        }
        return localStringBuilder.toString();
    }

    public static List<CalendarProtos.AgendaItem> mergeResults(@Nullable List<CalendarProtos.AgendaItem> paramList1, @Nullable List<CalendarProtos.AgendaItem> paramList2) {
        ArrayList localArrayList = Lists.newArrayList();
        long l1 = 0L;
        if (paramList1 != null) {
            Collections.sort(paramList1, AGENDA_ITEM_ASCENDING);
        }
        if (paramList2 != null) {
            Collections.sort(paramList2, AGENDA_ITEM_ASCENDING);
        }
        int i = 0;
        int j = 0;
        if (paramList2 != null) {
            i = 0;
            j = 0;
            if (paramList1 != null) {
                while ((paramList2.size() > j) && (paramList1.size() > i)) {
                    CalendarProtos.AgendaItem localAgendaItem1 = (CalendarProtos.AgendaItem) paramList2.get(j);
                    CalendarProtos.AgendaItem localAgendaItem2 = (CalendarProtos.AgendaItem) paramList1.get(i);
                    long l2 = AgendaTimeUtil.toUtcMillis(localAgendaItem1.getStartTime());
                    long l3 = AgendaTimeUtil.toUtcMillis(localAgendaItem2.getStartTime());
                    if ((l1 <= l2) && (l1 <= l3)) {
                    }
                    for (boolean bool = true; ; bool = false) {
                        Preconditions.checkState(bool, "Calendar events are out of order.");
                        l1 = Math.min(l2, l3);
                        if ((l2 != l3) || (!localAgendaItem1.getTitle().equals(localAgendaItem2.getTitle()))) {
                            break label281;
                        }
                        if (localAgendaItem1.hasEvent()) {
                            localAgendaItem1.getEvent().setEventId(localAgendaItem2.getEvent().getEventId());
                        }
                        if (TextUtils.isEmpty(localAgendaItem1.getTtsSingleItemDescription())) {
                            localAgendaItem1.setTtsSingleItemDescription(localAgendaItem2.getTtsSingleItemDescription());
                            Log.e("QueryCalendarUtil", "Using client TTS single item description.");
                        }
                        if (TextUtils.isEmpty(localAgendaItem1.getTtsMultipleItemDescription())) {
                            localAgendaItem1.setTtsMultipleItemDescription(localAgendaItem2.getTtsMultipleItemDescription());
                            Log.e("QueryCalendarUtil", "Using client TTS multiple item description.");
                        }
                        localArrayList.add(localAgendaItem1);
                        j++;
                        i++;
                        break;
                    }
                    label281:
                    if (l2 < l3) {
                        localArrayList.add(localAgendaItem1);
                        j++;
                    } else {
                        localArrayList.add(localAgendaItem2);
                        i++;
                    }
                }
            }
        }
        if (paramList2 != null) {
            for (int m = j; m < paramList2.size(); m++) {
                localArrayList.add(paramList2.get(m));
            }
        }
        if (paramList1 != null) {
            for (int k = i; k < paramList1.size(); k++) {
                localArrayList.add(paramList1.get(k));
            }
        }
        return localArrayList;
    }

    public static List<CalendarProtos.AgendaItem> queryCalendar(String paramString, boolean paramBoolean1, long paramLong1, long paramLong2, boolean paramBoolean2, ContentResolver paramContentResolver, int paramInt, Resources paramResources) {
        Uri.Builder localBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(localBuilder, paramLong1);
        ContentUris.appendId(localBuilder, paramLong2);
        Uri localUri = localBuilder.build();
        String[] arrayOfString = TextUtils.split(paramString, " ");
        String str1;
        Cursor localCursor;
        ArrayList localArrayList;
        label117:
        CalendarProtos.AgendaItem localAgendaItem;
        String str2;
        long l1;
        long l2;
        if (paramBoolean2) {
            str1 = "begin DESC";
            localCursor = paramContentResolver.query(localUri, EVENT_QUERY_PROJECTION, getSelectionString(arrayOfString.length, paramBoolean1), getSelectionArgs(arrayOfString, paramLong1, paramBoolean1), str1 + " LIMIT " + Math.min(paramInt, 500));
            localArrayList = Lists.newArrayList();
            if (localCursor == null) {
                break label610;
            }
            if (!localCursor.moveToNext()) {
                break label603;
            }
            localAgendaItem = new CalendarProtos.AgendaItem();
            localAgendaItem.setTitle(localCursor.getString(localCursor.getColumnIndex("title")));
            str2 = localCursor.getString(localCursor.getColumnIndex("eventLocation"));
            str3 = str2;
            l1 = localCursor.getLong(localCursor.getColumnIndex("begin"));
            l2 = localCursor.getLong(localCursor.getColumnIndex("end"));
            if (localCursor.getInt(localCursor.getColumnIndex("allDay")) != 0) {
                localAgendaItem.setAllDay(true);
                int i = localCursor.getInt(localCursor.getColumnIndex("startDay"));
                Time localTime = new Time();
                localTime.setJulianDay(i);
                l1 = localTime.toMillis(false);
                localTime.setJulianDay(1 + localCursor.getInt(localCursor.getColumnIndex("endDay")));
                l2 = localTime.toMillis(false);
                if (!TextUtils.isEmpty(str2)) {
                    break label578;
                }
            }
        }
        label578:
        for (String str3 = paramResources.getString(2131363681); ; str3 = String.format(paramResources.getString(2131363682), new Object[]{str2})) {
            if (!TextUtils.isEmpty(str3)) {
                CalendarProtos.AgendaItemSubtitle localAgendaItemSubtitle = new CalendarProtos.AgendaItemSubtitle();
                localAgendaItemSubtitle.addTextSegment(str3);
                localAgendaItem.setSubtitle(localAgendaItemSubtitle);
            }
            localAgendaItem.setStartTime(AgendaTimeUtil.toCalendarDateTime(l1));
            localAgendaItem.setEndTime(AgendaTimeUtil.toCalendarDateTime(l2));
            CalendarProtos.Event localEvent = new CalendarProtos.Event();
            localEvent.setEventId(localCursor.getLong(localCursor.getColumnIndex("event_id")));
            String str4 = localCursor.getString(localCursor.getColumnIndex("description"));
            if (!TextUtils.isEmpty(str4)) {
                String str6 = Html.fromHtml(str4).toString();
                if (!TextUtils.isEmpty(str6)) {
                    localEvent.setDescription(str6);
                }
            }
            if (!TextUtils.isEmpty(str2)) {
                localEvent.setLocation(str2);
            }
            String str5 = localCursor.getString(localCursor.getColumnIndex("calendar_displayName"));
            if (!TextUtils.isEmpty(str5)) {
                localEvent.setCalendarName(str5);
            }
            if (localCursor.getInt(localCursor.getColumnIndex("hasAttendeeData")) != 0) {
                addAttendees(paramContentResolver, localEvent);
            }
            localAgendaItem.setEvent(localEvent);
            localArrayList.add(localAgendaItem);
            break label117;
            str1 = "begin ASC";
            break;
        }
        label603:
        localCursor.close();
        label610:
        return localArrayList;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.util.QueryCalendarUtil

 * JD-Core Version:    0.7.0.1

 */