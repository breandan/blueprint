package com.google.android.sidekick.main.calendar;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarInfo;
import com.google.android.apps.sidekick.calendar.Calendar.ClientActions;
import com.google.android.apps.sidekick.calendar.Calendar.EventData;
import com.google.android.apps.sidekick.calendar.Calendar.ServerData;
import com.google.android.apps.sidekick.calendar.Calendar.ServerData.LatLng;
import com.google.android.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class CalendarMemoryStore
{
  static final CalendarMemoryStore EMPTY = new CalendarMemoryStore(ImmutableMap.of(), ImmutableMap.of(), ImmutableList.of(), false);
  private static final String TAG = Tag.getTag(CalendarMemoryStore.class);
  private final ImmutableList<Calendar.CalendarInfo> mCalendarInfos;
  private final Map<String, Calendar.CalendarData> mDataMapByHash;
  private final Map<Long, Calendar.CalendarData> mDataMapByProviderId;
  private final boolean mGettingEventsFailed;
  
  private CalendarMemoryStore(Map<Long, Calendar.CalendarData> paramMap, Map<String, Calendar.CalendarData> paramMap1, Collection<Calendar.CalendarInfo> paramCollection, boolean paramBoolean)
  {
    this.mDataMapByProviderId = ImmutableMap.copyOf(paramMap);
    this.mDataMapByHash = ImmutableMap.copyOf(paramMap1);
    this.mCalendarInfos = ImmutableList.copyOf(paramCollection);
    this.mGettingEventsFailed = paramBoolean;
    if (this.mDataMapByProviderId.size() != this.mDataMapByHash.size()) {
      Log.w(TAG, "CalendarMemoryStore maps have inconsistent sizes.");
    }
  }
  
  static int calculateCalendarInfoHash(Calendar.CalendarInfo paramCalendarInfo)
  {
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = paramCalendarInfo.getAccountOwner();
    arrayOfObject[1] = Long.valueOf(paramCalendarInfo.getDbId());
    arrayOfObject[2] = paramCalendarInfo.getDisplayName();
    arrayOfObject[3] = paramCalendarInfo.getId();
    return Objects.hashCode(arrayOfObject);
  }
  
  static String calculateHashForDuplicateDetection(Calendar.EventData paramEventData)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA1");
      localMessageDigest.update(TextUtils.join("|", ImmutableList.of(paramEventData.getTitle(), Long.valueOf(paramEventData.getStartTimeSeconds()), Long.valueOf(paramEventData.getEndTimeSeconds()), paramEventData.getWhereField())).getBytes());
      return Base64.encodeToString(localMessageDigest.digest(), 3);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      Log.e(TAG, "Could not find 'SHA1' message digest: " + localNoSuchAlgorithmException);
    }
    return null;
  }
  
  static String calculateServerHash(Calendar.EventData paramEventData)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA1");
      localMessageDigest.update(TextUtils.join("|", ImmutableList.of(Long.valueOf(paramEventData.getProviderId()), Long.valueOf(paramEventData.getEventId()), paramEventData.getTitle(), Long.valueOf(paramEventData.getStartTimeSeconds()), Long.valueOf(paramEventData.getEndTimeSeconds()), paramEventData.getWhereField(), Integer.valueOf(paramEventData.getNumberOfAttendees()), Integer.valueOf(paramEventData.getSelfAttendeeStatus()), Long.valueOf(paramEventData.getCalendarDbId()))).getBytes());
      return Base64.encodeToString(localMessageDigest.digest(), 3);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      Log.e(TAG, "Could not find 'SHA1' message digest: " + localNoSuchAlgorithmException);
    }
    return null;
  }
  
  private Set<Long> getProviderIdSet()
  {
    return this.mDataMapByProviderId.keySet();
  }
  
  private static boolean isValidData(Calendar.CalendarData paramCalendarData)
  {
    return (paramCalendarData.hasEventData()) && (isValidData(paramCalendarData.getEventData())) && (paramCalendarData.hasServerData()) && (isValidData(paramCalendarData.getServerData()));
  }
  
  private static boolean isValidData(Calendar.CalendarInfo paramCalendarInfo)
  {
    return (paramCalendarInfo.hasDbId()) && (!Strings.isNullOrEmpty(paramCalendarInfo.getId()));
  }
  
  private static boolean isValidData(Calendar.EventData paramEventData)
  {
    return paramEventData.hasProviderId();
  }
  
  private static boolean isValidData(Calendar.ServerData paramServerData)
  {
    return paramServerData.hasServerHash();
  }
  
  private static boolean latLngEqual(Calendar.ServerData paramServerData1, Calendar.ServerData paramServerData2)
  {
    boolean bool1 = paramServerData1.getIsGeocodable();
    boolean bool2 = false;
    if (bool1)
    {
      boolean bool3 = paramServerData1.hasGeocodedLatLng();
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = paramServerData2.getIsGeocodable();
        bool2 = false;
        if (bool4)
        {
          boolean bool5 = paramServerData2.hasGeocodedLatLng();
          bool2 = false;
          if (bool5)
          {
            Calendar.ServerData.LatLng localLatLng1 = paramServerData1.getGeocodedLatLng();
            Calendar.ServerData.LatLng localLatLng2 = paramServerData2.getGeocodedLatLng();
            boolean bool6 = localLatLng1.getLat() < localLatLng2.getLat();
            bool2 = false;
            if (!bool6)
            {
              boolean bool7 = localLatLng1.getLng() < localLatLng2.getLng();
              bool2 = false;
              if (!bool7) {
                bool2 = true;
              }
            }
          }
        }
      }
    }
    return bool2;
  }
  
  public boolean didGettingEventsFail()
  {
    return this.mGettingEventsFailed;
  }
  
  Calendar.CalendarData getByProviderId(long paramLong)
  {
    return (Calendar.CalendarData)this.mDataMapByProviderId.get(Long.valueOf(paramLong));
  }
  
  Calendar.CalendarData getByServerHash(String paramString)
  {
    return (Calendar.CalendarData)this.mDataMapByHash.get(paramString);
  }
  
  public Collection<Calendar.CalendarInfo> getCalendarInfos()
  {
    return this.mCalendarInfos;
  }
  
  MergeFromEventBuilder mergeFromEventBuilder()
  {
    return new MergeFromEventBuilder(this, null);
  }
  
  MergeFromServerBuilder mergeFromServerBuilder()
  {
    return new MergeFromServerBuilder(this, null);
  }
  
  CalendarMemoryStore setClientAction(long paramLong, Boolean paramBoolean1, Boolean paramBoolean2, Boolean paramBoolean3)
  {
    Calendar.CalendarData localCalendarData1 = getByProviderId(paramLong);
    if (localCalendarData1 == null) {}
    Calendar.ClientActions localClientActions1;
    do
    {
      return null;
      localClientActions1 = localCalendarData1.getClientActions();
    } while ((localClientActions1 != null) && ((paramBoolean2 == null) || (localClientActions1.getIsDismissed() == paramBoolean2.booleanValue())) && ((paramBoolean1 == null) || (localClientActions1.getIsNotified() == paramBoolean1.booleanValue())) && ((paramBoolean3 == null) || (localClientActions1.getIsNotificationDismissed() == paramBoolean3.booleanValue())));
    HashMap localHashMap1 = Maps.newHashMap(this.mDataMapByProviderId);
    HashMap localHashMap2 = Maps.newHashMap(this.mDataMapByHash);
    Calendar.ClientActions localClientActions2 = new Calendar.ClientActions();
    if (localClientActions1 != null) {}
    try
    {
      localClientActions2.mergeFrom(localClientActions1.toByteArray());
      if (paramBoolean2 != null) {
        localClientActions2.setIsDismissed(paramBoolean2.booleanValue());
      }
      if (paramBoolean1 != null) {
        localClientActions2.setIsNotified(paramBoolean1.booleanValue());
      }
      if (paramBoolean3 != null) {
        localClientActions2.setIsNotificationDismissed(paramBoolean3.booleanValue());
      }
      localCalendarData2 = new Calendar.CalendarData();
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException2)
    {
      try
      {
        Calendar.CalendarData localCalendarData2;
        localCalendarData2.mergeFrom(localCalendarData1.toByteArray());
        localCalendarData2.setClientActions(localClientActions2);
        localHashMap1.put(Long.valueOf(paramLong), localCalendarData2);
        localHashMap2.put(localCalendarData2.getServerData().getServerHash(), localCalendarData2);
        return new CalendarMemoryStore(localHashMap1, localHashMap2, this.mCalendarInfos, this.mGettingEventsFailed);
        localInvalidProtocolBufferMicroException2 = localInvalidProtocolBufferMicroException2;
        Log.w(TAG, "Error in merging proto buffer", localInvalidProtocolBufferMicroException2);
      }
      catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException1)
      {
        for (;;)
        {
          Log.w(TAG, "Error in merging proto buffer", localInvalidProtocolBufferMicroException1);
        }
      }
    }
  }
  
  int size()
  {
    return this.mDataMapByProviderId.size();
  }
  
  Collection<Calendar.CalendarData> values()
  {
    return this.mDataMapByProviderId.values();
  }
  
  static class Builder
  {
    private final Map<Long, Calendar.CalendarData> mDataByIdMap = Maps.newHashMap();
    private boolean mGettingEventsFailed;
    private final Map<Long, Calendar.CalendarInfo> mInfoByIdMap = Maps.newHashMap();
    
    private Map<String, Calendar.CalendarData> buildDataByHashMap()
    {
      HashMap localHashMap = Maps.newHashMap();
      Iterator localIterator = this.mDataByIdMap.values().iterator();
      while (localIterator.hasNext())
      {
        Calendar.CalendarData localCalendarData = (Calendar.CalendarData)localIterator.next();
        if (localHashMap.put(localCalendarData.getServerData().getServerHash(), localCalendarData) != null) {
          Log.w(CalendarMemoryStore.TAG, "Duplicate calendar entry by hash: " + localCalendarData.getServerData().getServerHash());
        }
      }
      return localHashMap;
    }
    
    boolean add(Calendar.CalendarData paramCalendarData)
    {
      if (!CalendarMemoryStore.isValidData(paramCalendarData)) {
        return false;
      }
      if (this.mDataByIdMap.put(Long.valueOf(paramCalendarData.getEventData().getProviderId()), paramCalendarData) != null) {
        Log.w(CalendarMemoryStore.TAG, "Duplicate calendar entry by provider ID: " + paramCalendarData.getEventData().getProviderId());
      }
      return true;
    }
    
    boolean add(Calendar.CalendarInfo paramCalendarInfo)
    {
      if (!CalendarMemoryStore.isValidData(paramCalendarInfo)) {
        return false;
      }
      if (this.mInfoByIdMap.put(Long.valueOf(paramCalendarInfo.getDbId()), paramCalendarInfo) != null) {
        Log.w(CalendarMemoryStore.TAG, "Duplicate calendar info by DB ID: " + paramCalendarInfo.getDbId());
      }
      return true;
    }
    
    boolean addAll(Iterable<Calendar.CalendarInfo> paramIterable)
    {
      boolean bool = true;
      Iterator localIterator = paramIterable.iterator();
      while (localIterator.hasNext()) {
        bool &= add((Calendar.CalendarInfo)localIterator.next());
      }
      return bool;
    }
    
    CalendarMemoryStore build()
    {
      return new CalendarMemoryStore(this.mDataByIdMap, buildDataByHashMap(), this.mInfoByIdMap.values(), this.mGettingEventsFailed, null);
    }
    
    void setGettingEventsFailed(boolean paramBoolean)
    {
      this.mGettingEventsFailed = paramBoolean;
    }
  }
  
  static class MergeFromEventBuilder
  {
    private final CalendarMemoryStore.Builder mBuilder = new CalendarMemoryStore.Builder();
    private boolean mChanged = false;
    private final Set<String> mEventDataForDuplicateDetection = Sets.newHashSet();
    private final Set<Long> mNewIds = Sets.newHashSet();
    private final CalendarMemoryStore mOldMemoryStore;
    
    private MergeFromEventBuilder(CalendarMemoryStore paramCalendarMemoryStore)
    {
      this.mOldMemoryStore = paramCalendarMemoryStore;
    }
    
    private boolean serverDataHasGeocode(Calendar.ServerData paramServerData)
    {
      return (paramServerData.getIsGeocodable()) && (paramServerData.getGeocodedLatLng().hasLat()) && (paramServerData.getGeocodedLatLng().hasLng());
    }
    
    private boolean serverDataHasTravelData(Calendar.ServerData paramServerData)
    {
      return (paramServerData.hasTravelTimeMinutes()) || (paramServerData.hasNotifyTimeSeconds());
    }
    
    CalendarMemoryStore build()
    {
      return this.mBuilder.build();
    }
    
    void setGettingEventsFailed(boolean paramBoolean)
    {
      if (this.mBuilder.mGettingEventsFailed != paramBoolean)
      {
        CalendarMemoryStore.Builder.access$702(this.mBuilder, paramBoolean);
        this.mChanged = true;
      }
    }
    
    boolean update(Calendar.EventData paramEventData)
    {
      if (!CalendarMemoryStore.isValidData(paramEventData)) {}
      String str1;
      do
      {
        return false;
        this.mNewIds.add(Long.valueOf(paramEventData.getProviderId()));
        str1 = CalendarMemoryStore.calculateServerHash(paramEventData);
      } while (str1 == null);
      String str2 = CalendarMemoryStore.calculateHashForDuplicateDetection(paramEventData);
      boolean bool1 = false;
      Calendar.CalendarData localCalendarData1;
      if (str2 != null)
      {
        if (!this.mEventDataForDuplicateDetection.add(str2)) {
          bool1 = true;
        }
      }
      else
      {
        localCalendarData1 = this.mOldMemoryStore.getByProviderId(paramEventData.getProviderId());
        if (localCalendarData1 != null) {
          break label135;
        }
        this.mBuilder.add(new Calendar.CalendarData().setEventData(paramEventData).setServerData(new Calendar.ServerData().setServerHash(str1)).setIsPotentialDuplicate(bool1));
        this.mChanged = true;
      }
      for (;;)
      {
        return true;
        bool1 = false;
        break;
        label135:
        boolean bool2;
        if (localCalendarData1.hasIsPotentialDuplicate()) {
          bool2 = localCalendarData1.getIsPotentialDuplicate();
        }
        for (;;)
        {
          if ((str1.equals(localCalendarData1.getServerData().getServerHash())) && (bool1 == bool2))
          {
            if (bool1 == bool2)
            {
              this.mBuilder.add(localCalendarData1);
              break;
              bool2 = false;
              continue;
            }
            Calendar.CalendarData localCalendarData3 = (Calendar.CalendarData)ProtoUtils.copyOf(localCalendarData1);
            localCalendarData3.setIsPotentialDuplicate(bool1);
            this.mBuilder.add(localCalendarData3);
            this.mChanged = true;
            break;
          }
        }
        Calendar.CalendarData localCalendarData2 = (Calendar.CalendarData)ProtoUtils.copyOf(localCalendarData1);
        localCalendarData2.setEventData(paramEventData);
        Calendar.ServerData localServerData = new Calendar.ServerData();
        try
        {
          localServerData.mergeFrom(localCalendarData1.getServerData().toByteArray());
          localServerData.setServerHash(str1);
          localEventData = localCalendarData1.getEventData();
          if (!localEventData.getWhereField().equals(paramEventData.getWhereField()))
          {
            if ((serverDataHasGeocode(localServerData)) || (serverDataHasTravelData(localServerData))) {
              localServerData.clearGeocodedLatLng().clearIsGeocodable().clearTravelTimeMinutes().clearNotifyTimeSeconds().setDataClearedBecauseEventChanged(true);
            }
            localCalendarData2.setServerData(localServerData);
            localCalendarData2.setIsPotentialDuplicate(bool1);
            this.mBuilder.add(localCalendarData2);
            this.mChanged = true;
          }
        }
        catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
        {
          for (;;)
          {
            Calendar.EventData localEventData;
            localServerData.clear();
            continue;
            if ((localEventData.getStartTimeSeconds() != paramEventData.getStartTimeSeconds()) && (serverDataHasTravelData(localServerData))) {
              localServerData.clearTravelTimeMinutes().clearNotifyTimeSeconds().setDataClearedBecauseEventChanged(true);
            }
          }
        }
      }
    }
    
    boolean updateAll(Collection<Calendar.CalendarInfo> paramCollection)
    {
      Collection localCollection = this.mOldMemoryStore.getCalendarInfos();
      if (localCollection.size() != paramCollection.size()) {
        this.mChanged = true;
      }
      for (;;)
      {
        return this.mBuilder.addAll(paramCollection);
        HashSet localHashSet = Sets.newHashSet();
        Iterator localIterator1 = localCollection.iterator();
        while (localIterator1.hasNext()) {
          localHashSet.add(Integer.valueOf(CalendarMemoryStore.calculateCalendarInfoHash((Calendar.CalendarInfo)localIterator1.next())));
        }
        Iterator localIterator2 = paramCollection.iterator();
        if (localIterator2.hasNext())
        {
          if (localHashSet.contains(Integer.valueOf(CalendarMemoryStore.calculateCalendarInfoHash((Calendar.CalendarInfo)localIterator2.next())))) {
            break;
          }
          this.mChanged = true;
        }
      }
    }
    
    boolean wasChanged()
    {
      if (!this.mChanged)
      {
        HashSet localHashSet = Sets.newHashSet(this.mOldMemoryStore.getProviderIdSet());
        localHashSet.removeAll(this.mNewIds);
        if (localHashSet.isEmpty()) {
          break label50;
        }
      }
      label50:
      for (boolean bool = true;; bool = false)
      {
        this.mChanged = bool;
        return this.mChanged;
      }
    }
  }
  
  static class MergeFromServerBuilder
  {
    private final Set<Long> changedIds = Sets.newHashSet();
    private final CalendarMemoryStore.Builder mBuilder = new CalendarMemoryStore.Builder();
    private final CalendarMemoryStore mOldMemoryStore;
    
    private MergeFromServerBuilder(CalendarMemoryStore paramCalendarMemoryStore)
    {
      this.mOldMemoryStore = paramCalendarMemoryStore;
    }
    
    private static boolean doubleE7Eq(double paramDouble1, double paramDouble2)
    {
      return Math.abs(paramDouble1 - paramDouble2) < 10000000.0D;
    }
    
    private static boolean latLngE7Eq(Calendar.ServerData.LatLng paramLatLng1, Calendar.ServerData.LatLng paramLatLng2)
    {
      return (doubleE7Eq(paramLatLng1.getLat(), paramLatLng2.getLat())) && (doubleE7Eq(paramLatLng1.getLng(), paramLatLng2.getLng()));
    }
    
    private boolean serverDataEquivalent(Calendar.ServerData paramServerData1, Calendar.ServerData paramServerData2)
    {
      if ((paramServerData1.getDataClearedBecauseEventChanged()) || (paramServerData2.getDataClearedBecauseEventChanged())) {}
      while ((!paramServerData1.getServerHash().equals(paramServerData2.getServerHash())) || (paramServerData1.hasIsGeocodable() != paramServerData2.hasIsGeocodable()) || ((paramServerData1.hasIsGeocodable()) && ((paramServerData1.getIsGeocodable() != paramServerData2.getIsGeocodable()) || ((paramServerData1.getIsGeocodable()) && ((!paramServerData1.hasGeocodedLatLng()) || (!paramServerData2.hasGeocodedLatLng()) || (!latLngE7Eq(paramServerData1.getGeocodedLatLng(), paramServerData2.getGeocodedLatLng())))))) || (paramServerData1.hasTravelTimeMinutes() != paramServerData2.hasTravelTimeMinutes()) || (paramServerData1.getTravelTimeMinutes() != paramServerData2.getTravelTimeMinutes()) || (paramServerData1.hasNotifyTimeSeconds() != paramServerData2.hasNotifyTimeSeconds()) || (paramServerData1.getNotifyTimeSeconds() != paramServerData2.getNotifyTimeSeconds())) {
        return false;
      }
      return true;
    }
    
    CalendarMemoryStore build()
    {
      if (!wasChanged()) {
        return this.mOldMemoryStore;
      }
      HashSet localHashSet = Sets.newHashSet(this.mOldMemoryStore.getProviderIdSet());
      localHashSet.removeAll(this.changedIds);
      Iterator localIterator = localHashSet.iterator();
      while (localIterator.hasNext())
      {
        Long localLong = (Long)localIterator.next();
        this.mBuilder.add(this.mOldMemoryStore.getByProviderId(localLong.longValue()));
      }
      this.mBuilder.addAll(this.mOldMemoryStore.getCalendarInfos());
      CalendarMemoryStore.Builder.access$702(this.mBuilder, this.mOldMemoryStore.mGettingEventsFailed);
      return this.mBuilder.build();
    }
    
    boolean update(Calendar.ServerData paramServerData)
    {
      if (!CalendarMemoryStore.isValidData(paramServerData)) {
        return false;
      }
      if (paramServerData.hasDataClearedBecauseEventChanged())
      {
        Log.w(CalendarMemoryStore.TAG, "Incoming ServerData has dataClearedBecauseEventChanged; unexpected!");
        return false;
      }
      Calendar.CalendarData localCalendarData1 = this.mOldMemoryStore.getByServerHash(paramServerData.getServerHash());
      if (localCalendarData1 != null)
      {
        Calendar.ServerData localServerData1 = localCalendarData1.getServerData();
        if ((localServerData1.hasTravelTimeMinutes()) && (localServerData1.hasNotifyTimeSeconds()) && (CalendarMemoryStore.latLngEqual(paramServerData, localServerData1)))
        {
          boolean bool = paramServerData.hasTravelTimeMinutes();
          Calendar.ServerData localServerData2 = null;
          if (!bool)
          {
            localServerData2 = new Calendar.ServerData();
            ProtoUtils.copyOf(paramServerData, localServerData2);
            localServerData2.setTravelTimeMinutes(localServerData1.getTravelTimeMinutes());
          }
          if (!paramServerData.hasNotifyTimeSeconds())
          {
            if (localServerData2 == null)
            {
              localServerData2 = new Calendar.ServerData();
              ProtoUtils.copyOf(paramServerData, localServerData2);
            }
            localServerData2.setNotifyTimeSeconds(localServerData1.getNotifyTimeSeconds());
          }
          if (localServerData2 != null) {
            paramServerData = localServerData2;
          }
        }
        if (!serverDataEquivalent(localServerData1, paramServerData))
        {
          Calendar.CalendarData localCalendarData2 = new Calendar.CalendarData();
          ProtoUtils.copyOf(localCalendarData1, localCalendarData2);
          localCalendarData2.setServerData(paramServerData);
          this.mBuilder.add(localCalendarData2);
          this.changedIds.add(Long.valueOf(localCalendarData1.getEventData().getProviderId()));
        }
      }
      return true;
    }
    
    boolean wasChanged()
    {
      return !this.changedIds.isEmpty();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.calendar.CalendarMemoryStore
 * JD-Core Version:    0.7.0.1
 */