package com.google.android.sidekick.main.calendar;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarInfo;
import com.google.android.apps.sidekick.calendar.Calendar.ClientActions;
import com.google.android.apps.sidekick.calendar.Calendar.EventData;
import com.google.android.apps.sidekick.calendar.Calendar.ServerData;
import com.google.android.apps.sidekick.calendar.Calendar.ServerData.LatLng;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ProtoUtils;
import com.google.android.sidekick.main.sync.RepeatedMessageInfo;
import com.google.android.sidekick.shared.util.CalendarDataUtil;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.CalendarEntry;
import com.google.geo.sidekick.Sidekick.ClientUserData;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Notification;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.NextMeeting.CalendarAccount;
import com.google.geo.sidekick.Sidekick.TriggerCondition;
import com.google.geo.sidekick.Sidekick.UploadCalendarData;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

class CalendarDataProviderImpl
  implements CalendarDataProvider
{
  private static final String TAG = Tag.getTag(CalendarDataProviderImpl.class);
  private final Context mAppContext;
  private final Clock mClock;
  private final AtomicBoolean mInitialized = new AtomicBoolean();
  private final CountDownLatch mInitializedLatch = new CountDownLatch(1);
  private volatile CalendarMemoryStore mMemoryStore = CalendarMemoryStore.EMPTY;
  private final PredictiveCardsPreferences mPredictiveCardsPreferences;
  
  public CalendarDataProviderImpl(Context paramContext, Clock paramClock)
  {
    this.mAppContext = paramContext.getApplicationContext();
    this.mClock = paramClock;
    this.mPredictiveCardsPreferences = VelvetServices.get().getCoreServices().getPredictiveCardsPreferences();
  }
  
  private Collection<Calendar.ServerData> convertEntryTreeToServerData(Sidekick.EntryTree paramEntryTree)
  {
    if (!paramEntryTree.hasRoot()) {
      return ImmutableList.of();
    }
    ArrayList localArrayList = Lists.newArrayList();
    recursiveEntryNodeConversion(paramEntryTree.getRoot(), localArrayList);
    return localArrayList;
  }
  
  /* Error */
  private void flushToDisk()
  {
    // Byte code:
    //   0: new 121	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet
    //   3: dup
    //   4: invokespecial 122	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet:<init>	()V
    //   7: astore_1
    //   8: aload_0
    //   9: getfield 41	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mMemoryStore	Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   12: invokevirtual 126	com/google/android/sidekick/main/calendar/CalendarMemoryStore:values	()Ljava/util/Collection;
    //   15: invokeinterface 132 1 0
    //   20: astore_2
    //   21: aload_2
    //   22: invokeinterface 137 1 0
    //   27: ifeq +20 -> 47
    //   30: aload_1
    //   31: aload_2
    //   32: invokeinterface 141 1 0
    //   37: checkcast 143	com/google/android/apps/sidekick/calendar/Calendar$CalendarData
    //   40: invokevirtual 147	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet:addCalendarData	(Lcom/google/android/apps/sidekick/calendar/Calendar$CalendarData;)Lcom/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet;
    //   43: pop
    //   44: goto -23 -> 21
    //   47: aload_0
    //   48: getfield 41	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mMemoryStore	Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   51: invokevirtual 150	com/google/android/sidekick/main/calendar/CalendarMemoryStore:getCalendarInfos	()Ljava/util/Collection;
    //   54: invokeinterface 132 1 0
    //   59: astore_3
    //   60: aload_3
    //   61: invokeinterface 137 1 0
    //   66: ifeq +20 -> 86
    //   69: aload_1
    //   70: aload_3
    //   71: invokeinterface 141 1 0
    //   76: checkcast 152	com/google/android/apps/sidekick/calendar/Calendar$CalendarInfo
    //   79: invokevirtual 156	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet:addCalendarInfo	(Lcom/google/android/apps/sidekick/calendar/Calendar$CalendarInfo;)Lcom/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet;
    //   82: pop
    //   83: goto -23 -> 60
    //   86: aconst_null
    //   87: astore 4
    //   89: new 158	java/io/BufferedOutputStream
    //   92: dup
    //   93: aload_0
    //   94: getfield 61	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mAppContext	Landroid/content/Context;
    //   97: ldc 160
    //   99: iconst_0
    //   100: invokevirtual 164	android/content/Context:openFileOutput	(Ljava/lang/String;I)Ljava/io/FileOutputStream;
    //   103: invokespecial 167	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   106: astore 5
    //   108: aload_1
    //   109: invokevirtual 171	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet:toByteArray	()[B
    //   112: astore 9
    //   114: aload 9
    //   116: arraylength
    //   117: ldc 172
    //   119: if_icmpge +16 -> 135
    //   122: aload 5
    //   124: aload 9
    //   126: invokevirtual 176	java/io/BufferedOutputStream:write	([B)V
    //   129: aload 5
    //   131: invokestatic 182	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   134: return
    //   135: getstatic 30	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:TAG	Ljava/lang/String;
    //   138: new 184	java/lang/StringBuilder
    //   141: dup
    //   142: invokespecial 185	java/lang/StringBuilder:<init>	()V
    //   145: ldc 187
    //   147: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: aload 9
    //   152: arraylength
    //   153: invokevirtual 194	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   156: ldc 196
    //   158: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   161: invokevirtual 200	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   164: invokestatic 206	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   167: pop
    //   168: goto -39 -> 129
    //   171: astore 7
    //   173: aload 5
    //   175: astore 4
    //   177: getstatic 30	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:TAG	Ljava/lang/String;
    //   180: ldc 208
    //   182: aload 7
    //   184: invokestatic 211	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   187: pop
    //   188: aload 4
    //   190: invokestatic 182	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   193: return
    //   194: astore 6
    //   196: aload 4
    //   198: invokestatic 182	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   201: aload 6
    //   203: athrow
    //   204: astore 6
    //   206: aload 5
    //   208: astore 4
    //   210: goto -14 -> 196
    //   213: astore 7
    //   215: aconst_null
    //   216: astore 4
    //   218: goto -41 -> 177
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	221	0	this	CalendarDataProviderImpl
    //   7	102	1	localCalendarDataSet	com.google.android.apps.sidekick.calendar.Calendar.CalendarDataSet
    //   20	12	2	localIterator1	Iterator
    //   59	12	3	localIterator2	Iterator
    //   87	130	4	localObject1	Object
    //   106	101	5	localBufferedOutputStream	java.io.BufferedOutputStream
    //   194	8	6	localObject2	Object
    //   204	1	6	localObject3	Object
    //   171	12	7	localIOException1	java.io.IOException
    //   213	1	7	localIOException2	java.io.IOException
    //   112	39	9	arrayOfByte	byte[]
    // Exception table:
    //   from	to	target	type
    //   108	129	171	java/io/IOException
    //   135	168	171	java/io/IOException
    //   89	108	194	finally
    //   177	188	194	finally
    //   108	129	204	finally
    //   135	168	204	finally
    //   89	108	213	java/io/IOException
  }
  
  private Iterable<Calendar.CalendarData> getActiveCalendarDataWithIds()
  {
    NowConfigurationPreferences localNowConfigurationPreferences = this.mPredictiveCardsPreferences.getWorkingPreferences();
    return getNotDisabledCalendarsWithIds(this.mAppContext, localNowConfigurationPreferences, this.mAppContext.getString(2131362103), this.mMemoryStore.getCalendarInfos(), this.mMemoryStore.values());
  }
  
  private static Collection<Calendar.CalendarData> getNotDisabledCalendarsWithIds(Context paramContext, NowConfigurationPreferences paramNowConfigurationPreferences, String paramString, Collection<Calendar.CalendarInfo> paramCollection, Collection<Calendar.CalendarData> paramCollection1)
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(paramCollection1.size());
    Iterator localIterator = paramCollection1.iterator();
    while (localIterator.hasNext())
    {
      Calendar.CalendarData localCalendarData = (Calendar.CalendarData)localIterator.next();
      long l = localCalendarData.getEventData().getCalendarDbId();
      if (l != 0L)
      {
        Calendar.CalendarInfo localCalendarInfo = CalendarDataUtil.getCalendarInfoForDbId(l, paramCollection);
        if (localCalendarInfo != null)
        {
          Sidekick.SidekickConfiguration.NextMeeting.CalendarAccount localCalendarAccount = (Sidekick.SidekickConfiguration.NextMeeting.CalendarAccount)paramNowConfigurationPreferences.getMessage(paramString, PredictiveCardsPreferences.REPEATED_MESSAGE_INFO.primaryKeyFor(CalendarDataUtil.toCalendarAccount(localCalendarInfo)));
          if ((localCalendarAccount == null) || (localCalendarAccount.getOn())) {
            localCalendarData.setId(localCalendarInfo.getId());
          }
        }
      }
      else
      {
        localArrayList.add(localCalendarData);
      }
    }
    return localArrayList;
  }
  
  private Long getNotificationTimeSecs(Sidekick.Entry paramEntry)
  {
    if ((paramEntry.hasNotification()) && (paramEntry.getNotification().getType() != 4) && (paramEntry.getNotification().hasTriggerCondition()))
    {
      Sidekick.TriggerCondition localTriggerCondition = paramEntry.getNotification().getTriggerCondition();
      Iterator localIterator = localTriggerCondition.getConditionList().iterator();
      while (localIterator.hasNext()) {
        if ((((Integer)localIterator.next()).intValue() == 5) && (localTriggerCondition.hasTimeSeconds())) {
          return Long.valueOf(localTriggerCondition.getTimeSeconds());
        }
      }
    }
    return null;
  }
  
  /* Error */
  private void readFromDisk()
  {
    // Byte code:
    //   0: new 341	com/google/android/sidekick/main/calendar/CalendarMemoryStore$Builder
    //   3: dup
    //   4: invokespecial 342	com/google/android/sidekick/main/calendar/CalendarMemoryStore$Builder:<init>	()V
    //   7: astore_1
    //   8: new 344	java/io/File
    //   11: dup
    //   12: aload_0
    //   13: getfield 61	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mAppContext	Landroid/content/Context;
    //   16: invokevirtual 348	android/content/Context:getFilesDir	()Ljava/io/File;
    //   19: ldc 160
    //   21: invokespecial 351	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   24: invokevirtual 354	java/io/File:length	()J
    //   27: lstore_2
    //   28: lload_2
    //   29: ldc2_w 355
    //   32: lcmp
    //   33: ifle +40 -> 73
    //   36: getstatic 30	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:TAG	Ljava/lang/String;
    //   39: new 184	java/lang/StringBuilder
    //   42: dup
    //   43: invokespecial 185	java/lang/StringBuilder:<init>	()V
    //   46: ldc_w 358
    //   49: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: lload_2
    //   53: invokevirtual 361	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   56: ldc 196
    //   58: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: invokevirtual 200	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   64: invokestatic 206	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   67: pop
    //   68: aload_0
    //   69: invokevirtual 364	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:clearData	()V
    //   72: return
    //   73: lload_2
    //   74: lconst_1
    //   75: lcmp
    //   76: ifge +8 -> 84
    //   79: aload_0
    //   80: invokevirtual 364	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:clearData	()V
    //   83: return
    //   84: aconst_null
    //   85: astore 4
    //   87: aload_0
    //   88: getfield 61	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mAppContext	Landroid/content/Context;
    //   91: ldc 160
    //   93: invokevirtual 368	android/content/Context:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
    //   96: astore 4
    //   98: lload_2
    //   99: l2i
    //   100: newarray byte
    //   102: astore 9
    //   104: iconst_0
    //   105: istore 10
    //   107: lload_2
    //   108: l2i
    //   109: istore 11
    //   111: aload 4
    //   113: aload 9
    //   115: iload 10
    //   117: iload 11
    //   119: invokevirtual 374	java/io/FileInputStream:read	([BII)I
    //   122: istore 12
    //   124: iload 12
    //   126: iconst_1
    //   127: if_icmpge +19 -> 146
    //   130: iload 10
    //   132: iconst_1
    //   133: if_icmpge +35 -> 168
    //   136: aload_0
    //   137: invokevirtual 364	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:clearData	()V
    //   140: aload 4
    //   142: invokestatic 182	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   145: return
    //   146: iload 11
    //   148: iload 12
    //   150: isub
    //   151: istore 11
    //   153: iload 10
    //   155: iload 12
    //   157: iadd
    //   158: istore 10
    //   160: iload 11
    //   162: ifgt -51 -> 111
    //   165: goto -35 -> 130
    //   168: new 121	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet
    //   171: dup
    //   172: invokespecial 122	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet:<init>	()V
    //   175: astore 13
    //   177: aload 13
    //   179: aload 9
    //   181: iconst_0
    //   182: iload 10
    //   184: invokestatic 380	com/google/protobuf/micro/CodedInputStreamMicro:newInstance	([BII)Lcom/google/protobuf/micro/CodedInputStreamMicro;
    //   187: invokevirtual 384	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet:mergeFrom	(Lcom/google/protobuf/micro/CodedInputStreamMicro;)Lcom/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet;
    //   190: pop
    //   191: aload 13
    //   193: invokevirtual 387	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet:getCalendarDataList	()Ljava/util/List;
    //   196: invokeinterface 320 1 0
    //   201: astore 15
    //   203: aload 15
    //   205: invokeinterface 137 1 0
    //   210: ifeq +49 -> 259
    //   213: aload_1
    //   214: aload 15
    //   216: invokeinterface 141 1 0
    //   221: checkcast 143	com/google/android/apps/sidekick/calendar/Calendar$CalendarData
    //   224: invokevirtual 390	com/google/android/sidekick/main/calendar/CalendarMemoryStore$Builder:add	(Lcom/google/android/apps/sidekick/calendar/Calendar$CalendarData;)Z
    //   227: ifne -24 -> 203
    //   230: getstatic 30	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:TAG	Ljava/lang/String;
    //   233: ldc_w 392
    //   236: invokestatic 206	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   239: pop
    //   240: goto -37 -> 203
    //   243: astore 8
    //   245: aload 4
    //   247: invokestatic 182	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   250: aload_0
    //   251: aload_1
    //   252: invokevirtual 396	com/google/android/sidekick/main/calendar/CalendarMemoryStore$Builder:build	()Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   255: putfield 41	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mMemoryStore	Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   258: return
    //   259: aload_1
    //   260: aload 13
    //   262: invokevirtual 399	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet:getCalendarInfoList	()Ljava/util/List;
    //   265: invokevirtual 403	com/google/android/sidekick/main/calendar/CalendarMemoryStore$Builder:addAll	(Ljava/lang/Iterable;)Z
    //   268: ifne +13 -> 281
    //   271: getstatic 30	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:TAG	Ljava/lang/String;
    //   274: ldc_w 405
    //   277: invokestatic 206	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   280: pop
    //   281: aload_1
    //   282: aload 13
    //   284: invokevirtual 408	com/google/android/apps/sidekick/calendar/Calendar$CalendarDataSet:getGettingEventsFailed	()Z
    //   287: invokevirtual 412	com/google/android/sidekick/main/calendar/CalendarMemoryStore$Builder:setGettingEventsFailed	(Z)V
    //   290: aload 4
    //   292: invokestatic 182	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   295: goto -45 -> 250
    //   298: astore 6
    //   300: getstatic 30	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:TAG	Ljava/lang/String;
    //   303: ldc_w 414
    //   306: aload 6
    //   308: invokestatic 211	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   311: pop
    //   312: aload_0
    //   313: invokevirtual 364	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:clearData	()V
    //   316: aload 4
    //   318: invokestatic 182	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   321: return
    //   322: astore 5
    //   324: aload 4
    //   326: invokestatic 182	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   329: aload 5
    //   331: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	332	0	this	CalendarDataProviderImpl
    //   7	275	1	localBuilder	CalendarMemoryStore.Builder
    //   27	81	2	l	long
    //   85	240	4	localFileInputStream	java.io.FileInputStream
    //   322	8	5	localObject	Object
    //   298	9	6	localIOException	java.io.IOException
    //   243	1	8	localFileNotFoundException	java.io.FileNotFoundException
    //   102	78	9	arrayOfByte	byte[]
    //   105	78	10	i	int
    //   109	52	11	j	int
    //   122	36	12	k	int
    //   175	108	13	localCalendarDataSet	com.google.android.apps.sidekick.calendar.Calendar.CalendarDataSet
    //   201	14	15	localIterator	Iterator
    // Exception table:
    //   from	to	target	type
    //   87	104	243	java/io/FileNotFoundException
    //   111	124	243	java/io/FileNotFoundException
    //   136	140	243	java/io/FileNotFoundException
    //   168	203	243	java/io/FileNotFoundException
    //   203	240	243	java/io/FileNotFoundException
    //   259	281	243	java/io/FileNotFoundException
    //   281	290	243	java/io/FileNotFoundException
    //   87	104	298	java/io/IOException
    //   111	124	298	java/io/IOException
    //   136	140	298	java/io/IOException
    //   168	203	298	java/io/IOException
    //   203	240	298	java/io/IOException
    //   259	281	298	java/io/IOException
    //   281	290	298	java/io/IOException
    //   87	104	322	finally
    //   111	124	322	finally
    //   136	140	322	finally
    //   168	203	322	finally
    //   203	240	322	finally
    //   259	281	322	finally
    //   281	290	322	finally
    //   300	316	322	finally
  }
  
  private void recursiveEntryNodeConversion(Sidekick.EntryTreeNode paramEntryTreeNode, Collection<Calendar.ServerData> paramCollection)
  {
    if (paramEntryTreeNode.getChildCount() > 0)
    {
      Iterator localIterator2 = paramEntryTreeNode.getChildList().iterator();
      while (localIterator2.hasNext()) {
        recursiveEntryNodeConversion((Sidekick.EntryTreeNode)localIterator2.next(), paramCollection);
      }
    }
    Iterator localIterator1 = paramEntryTreeNode.getEntryList().iterator();
    while (localIterator1.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator1.next();
      if ((localEntry.getType() == 14) && (localEntry.hasCalendarEntry()))
      {
        Sidekick.CalendarEntry localCalendarEntry = localEntry.getCalendarEntry();
        if (TextUtils.isEmpty(localCalendarEntry.getHash()))
        {
          Log.w(TAG, "Received CalendarEntry from server without hash");
        }
        else
        {
          Calendar.ServerData localServerData = new Calendar.ServerData();
          localServerData.setServerHash(localCalendarEntry.getHash());
          if (localCalendarEntry.hasTravelTimeSeconds()) {
            localServerData.setTravelTimeMinutes(localCalendarEntry.getTravelTimeSeconds() / 60);
          }
          if (localCalendarEntry.hasLocation())
          {
            if ((!localCalendarEntry.getLocation().hasLat()) || (!localCalendarEntry.getLocation().hasLng())) {
              break label284;
            }
            localServerData.setIsGeocodable(true).setGeocodedLatLng(new Calendar.ServerData.LatLng().setLat(localCalendarEntry.getLocation().getLat()).setLng(localCalendarEntry.getLocation().getLng()));
          }
          for (;;)
          {
            Long localLong = getNotificationTimeSecs(localEntry);
            if (localLong != null) {
              localServerData.setNotifyTimeSeconds(localLong.longValue());
            }
            localServerData.setEntryFromServer(stripEntryFromServer(localEntry));
            paramCollection.add(localServerData);
            break;
            label284:
            localServerData.setIsGeocodable(false);
          }
        }
      }
    }
  }
  
  private Sidekick.Entry stripEntryFromServer(Sidekick.Entry paramEntry)
  {
    Sidekick.Entry localEntry = (Sidekick.Entry)ProtoUtils.copyOf(paramEntry);
    Sidekick.CalendarEntry localCalendarEntry = paramEntry.getCalendarEntry();
    localEntry.setCalendarEntry(new Sidekick.CalendarEntry().setHash(localCalendarEntry.getHash()));
    Iterator localIterator = localEntry.getEntryActionList().iterator();
    Sidekick.Action localAction;
    do
    {
      boolean bool = localIterator.hasNext();
      localObject = null;
      if (!bool) {
        break;
      }
      localAction = (Sidekick.Action)localIterator.next();
    } while (localAction.getType() != 12);
    Object localObject = localAction;
    localEntry.clearEntryAction();
    if (localObject != null) {
      localEntry.addEntryAction(localObject);
    }
    return localEntry;
  }
  
  private boolean updateWithNewServerData(Collection<Calendar.ServerData> paramCollection)
  {
    if (!waitForInitialization()) {
      return false;
    }
    CalendarMemoryStore.MergeFromServerBuilder localMergeFromServerBuilder;
    try
    {
      localMergeFromServerBuilder = this.mMemoryStore.mergeFromServerBuilder();
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext()) {
        if (!localMergeFromServerBuilder.update((Calendar.ServerData)localIterator.next())) {
          Log.e(TAG, "ServerData from server contains invalid data");
        }
      }
      bool = localMergeFromServerBuilder.wasChanged();
    }
    finally {}
    boolean bool;
    if (bool)
    {
      this.mMemoryStore = localMergeFromServerBuilder.build();
      flushToDisk();
    }
    return bool;
  }
  
  private boolean waitForInitialization()
  {
    try
    {
      this.mInitializedLatch.await();
      return true;
    }
    catch (InterruptedException localInterruptedException)
    {
      Log.w(TAG, "Initialization latch wait interrupted");
      Thread.currentThread().interrupt();
    }
    return false;
  }
  
  public void addCalendarDataToClientUserData(Sidekick.ClientUserData paramClientUserData)
  {
    Iterator localIterator = getCalendarDataForRefresh().iterator();
    while (localIterator.hasNext()) {
      paramClientUserData.addCalendarData((Sidekick.UploadCalendarData)localIterator.next());
    }
    paramClientUserData.setGettingCalendarEventsFailed(didGettingEventsFail());
  }
  
  public void clearAllEventNotifiedMarkers()
  {
    int i = 0;
    for (;;)
    {
      CalendarMemoryStore.Builder localBuilder;
      Calendar.CalendarData localCalendarData1;
      try
      {
        localBuilder = new CalendarMemoryStore.Builder();
        Iterator localIterator = this.mMemoryStore.values().iterator();
        if (!localIterator.hasNext()) {
          break label157;
        }
        localCalendarData1 = (Calendar.CalendarData)localIterator.next();
        if ((!localCalendarData1.hasClientActions()) || (!localCalendarData1.getClientActions().getIsNotified()) || (localCalendarData1.getClientActions().getIsDismissed()) || (localCalendarData1.getClientActions().getIsNotificationDismissed())) {
          break label199;
        }
        localCalendarData2 = new Calendar.CalendarData();
      }
      finally {}
      try
      {
        localCalendarData2.mergeFrom(localCalendarData1.toByteArray());
        localCalendarData2.getClientActions().setIsNotified(false);
        i = 1;
      }
      catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
      {
        Log.w(TAG, "Failed to clear calendar notification.", localInvalidProtocolBufferMicroException);
        localCalendarData2 = localCalendarData1;
        continue;
      }
      localBuilder.add(localCalendarData2);
      continue;
      label157:
      if (i != 0)
      {
        localBuilder.addAll(this.mMemoryStore.getCalendarInfos());
        localBuilder.setGettingEventsFailed(this.mMemoryStore.didGettingEventsFail());
        this.mMemoryStore = localBuilder.build();
        flushToDisk();
      }
      return;
      label199:
      Calendar.CalendarData localCalendarData2 = localCalendarData1;
    }
  }
  
  public void clearData()
  {
    this.mAppContext.deleteFile("calendar_store");
    this.mMemoryStore = CalendarMemoryStore.EMPTY;
  }
  
  public boolean didGettingEventsFail()
  {
    return this.mMemoryStore.didGettingEventsFail();
  }
  
  public Calendar.CalendarData getCalendarDataByServerHash(String paramString)
  {
    Calendar.CalendarData localCalendarData = this.mMemoryStore.getByServerHash(paramString);
    if ((localCalendarData != null) && (localCalendarData.hasClientActions()) && (localCalendarData.getClientActions().getIsDismissed())) {
      localCalendarData = null;
    }
    return localCalendarData;
  }
  
  public Iterable<Sidekick.UploadCalendarData> getCalendarDataForNotify()
  {
    return Iterables.transform(Iterables.filter(getActiveCalendarDataWithIds(), MaybeNotifiable.INSTANCE), ToUploadCalendarData.INSTANCE);
  }
  
  public Iterable<Sidekick.UploadCalendarData> getCalendarDataForRefresh()
  {
    return Iterables.transform(Iterables.filter(getActiveCalendarDataWithIds(), NotDismissed.INSTANCE), ToUploadCalendarData.INSTANCE);
  }
  
  public Collection<Calendar.CalendarInfo> getCalendarsList()
  {
    return this.mMemoryStore.getCalendarInfos();
  }
  
  public Long getEarliestNotificationTimeSecs()
  {
    long l = 9223372036854775807L;
    Iterator localIterator = Iterables.filter(getActiveCalendarDataWithIds(), NotificationPending.INSTANCE).iterator();
    while (localIterator.hasNext()) {
      l = Math.min(l, ((Calendar.CalendarData)localIterator.next()).getServerData().getNotifyTimeSeconds());
    }
    if (l == 9223372036854775807L) {
      return null;
    }
    return Long.valueOf(l);
  }
  
  public Iterable<Calendar.CalendarData> getNotifyingCalendarData()
  {
    return Iterables.filter(getActiveCalendarDataWithIds(), new NotifyingAt(this.mClock.currentTimeMillis() / 1000L));
  }
  
  public void initialize()
  {
    if (!this.mInitialized.getAndSet(true)) {
      new AsyncTask()
      {
        protected Void doInBackground(Void... paramAnonymousVarArgs)
        {
          CalendarDataProviderImpl.this.readFromDisk();
          CalendarDataProviderImpl.this.mInitializedLatch.countDown();
          return null;
        }
      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
  }
  
  /* Error */
  public boolean markEventAsDismissed(long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 41	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mMemoryStore	Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   6: lload_1
    //   7: aconst_null
    //   8: iconst_1
    //   9: invokestatic 750	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   12: aconst_null
    //   13: invokevirtual 754	com/google/android/sidekick/main/calendar/CalendarMemoryStore:setClientAction	(JLjava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;)Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   16: astore 4
    //   18: aload 4
    //   20: ifnull +13 -> 33
    //   23: aload_0
    //   24: aload 4
    //   26: putfield 41	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mMemoryStore	Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   29: aload_0
    //   30: invokespecial 576	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:flushToDisk	()V
    //   33: aload 4
    //   35: ifnull +11 -> 46
    //   38: iconst_1
    //   39: istore 5
    //   41: aload_0
    //   42: monitorexit
    //   43: iload 5
    //   45: ireturn
    //   46: iconst_0
    //   47: istore 5
    //   49: goto -8 -> 41
    //   52: astore_3
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_3
    //   56: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	this	CalendarDataProviderImpl
    //   0	57	1	paramLong	long
    //   52	4	3	localObject	Object
    //   16	18	4	localCalendarMemoryStore	CalendarMemoryStore
    //   39	9	5	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	18	52	finally
    //   23	33	52	finally
  }
  
  /* Error */
  public boolean markEventAsNotified(long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 41	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mMemoryStore	Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   6: lload_1
    //   7: iconst_1
    //   8: invokestatic 750	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   11: aconst_null
    //   12: aconst_null
    //   13: invokevirtual 754	com/google/android/sidekick/main/calendar/CalendarMemoryStore:setClientAction	(JLjava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;)Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   16: astore 4
    //   18: aload 4
    //   20: ifnull +13 -> 33
    //   23: aload_0
    //   24: aload 4
    //   26: putfield 41	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mMemoryStore	Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   29: aload_0
    //   30: invokespecial 576	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:flushToDisk	()V
    //   33: aload 4
    //   35: ifnull +11 -> 46
    //   38: iconst_1
    //   39: istore 5
    //   41: aload_0
    //   42: monitorexit
    //   43: iload 5
    //   45: ireturn
    //   46: iconst_0
    //   47: istore 5
    //   49: goto -8 -> 41
    //   52: astore_3
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_3
    //   56: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	this	CalendarDataProviderImpl
    //   0	57	1	paramLong	long
    //   52	4	3	localObject	Object
    //   16	18	4	localCalendarMemoryStore	CalendarMemoryStore
    //   39	9	5	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	18	52	finally
    //   23	33	52	finally
  }
  
  /* Error */
  public boolean markEventNotificationAsDismissed(long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 41	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mMemoryStore	Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   6: lload_1
    //   7: aconst_null
    //   8: aconst_null
    //   9: iconst_1
    //   10: invokestatic 750	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   13: invokevirtual 754	com/google/android/sidekick/main/calendar/CalendarMemoryStore:setClientAction	(JLjava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;)Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   16: astore 4
    //   18: aload 4
    //   20: ifnull +13 -> 33
    //   23: aload_0
    //   24: aload 4
    //   26: putfield 41	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:mMemoryStore	Lcom/google/android/sidekick/main/calendar/CalendarMemoryStore;
    //   29: aload_0
    //   30: invokespecial 576	com/google/android/sidekick/main/calendar/CalendarDataProviderImpl:flushToDisk	()V
    //   33: aload 4
    //   35: ifnull +11 -> 46
    //   38: iconst_1
    //   39: istore 5
    //   41: aload_0
    //   42: monitorexit
    //   43: iload 5
    //   45: ireturn
    //   46: iconst_0
    //   47: istore 5
    //   49: goto -8 -> 41
    //   52: astore_3
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_3
    //   56: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	this	CalendarDataProviderImpl
    //   0	57	1	paramLong	long
    //   52	4	3	localObject	Object
    //   16	18	4	localCalendarMemoryStore	CalendarMemoryStore
    //   39	9	5	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	18	52	finally
    //   23	33	52	finally
  }
  
  public boolean updateWithNewEntryTreeFromServer(Sidekick.EntryTree paramEntryTree)
  {
    Collection localCollection = convertEntryTreeToServerData(paramEntryTree);
    if (localCollection.isEmpty()) {
      return false;
    }
    return updateWithNewServerData(localCollection);
  }
  
  public boolean updateWithNewEventData(Collection<Calendar.EventData> paramCollection, Collection<Calendar.CalendarInfo> paramCollection1, boolean paramBoolean)
  {
    if (!waitForInitialization()) {
      return false;
    }
    CalendarMemoryStore.MergeFromEventBuilder localMergeFromEventBuilder;
    try
    {
      localMergeFromEventBuilder = this.mMemoryStore.mergeFromEventBuilder();
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext()) {
        if (!localMergeFromEventBuilder.update((Calendar.EventData)localIterator.next())) {
          Log.e(TAG, "EventData from calendar contains invalid data");
        }
      }
      if (localMergeFromEventBuilder.updateAll(paramCollection1)) {
        break label95;
      }
    }
    finally {}
    Log.e(TAG, "CalendarInfo from calendar contains invalid data");
    label95:
    localMergeFromEventBuilder.setGettingEventsFailed(paramBoolean);
    boolean bool = localMergeFromEventBuilder.wasChanged();
    if (bool)
    {
      this.mMemoryStore = localMergeFromEventBuilder.build();
      flushToDisk();
    }
    return bool;
  }
  
  private static class MaybeNotifiable
    implements Predicate<Calendar.CalendarData>
  {
    static final MaybeNotifiable INSTANCE = new MaybeNotifiable();
    
    public boolean apply(Calendar.CalendarData paramCalendarData)
    {
      int i;
      if ((!paramCalendarData.hasServerData()) || (!paramCalendarData.getServerData().hasIsGeocodable()) || ((paramCalendarData.hasServerData()) && (paramCalendarData.getServerData().getIsGeocodable())))
      {
        i = 1;
        if ((!paramCalendarData.hasIsPotentialDuplicate()) || (!paramCalendarData.getIsPotentialDuplicate())) {
          break label118;
        }
      }
      label118:
      for (int j = 1;; j = 0)
      {
        if (((paramCalendarData.hasClientActions()) && (paramCalendarData.getClientActions().getIsDismissed())) || ((paramCalendarData.hasClientActions()) && (paramCalendarData.getClientActions().getIsNotified())) || (!paramCalendarData.hasEventData()) || (!paramCalendarData.getEventData().hasWhereField()) || (i == 0) || (j != 0)) {
          break label123;
        }
        return true;
        i = 0;
        break;
      }
      label123:
      return false;
    }
  }
  
  private static class NotDismissed
    implements Predicate<Calendar.CalendarData>
  {
    static final NotDismissed INSTANCE = new NotDismissed();
    
    public boolean apply(Calendar.CalendarData paramCalendarData)
    {
      int i;
      if ((paramCalendarData.hasIsPotentialDuplicate()) && (paramCalendarData.getIsPotentialDuplicate()))
      {
        i = 1;
        if ((!paramCalendarData.hasClientActions()) || (!paramCalendarData.getClientActions().getIsDismissed())) {
          break label50;
        }
      }
      label50:
      for (int j = 1;; j = 0)
      {
        if ((j != 0) || (i != 0)) {
          break label55;
        }
        return true;
        i = 0;
        break;
      }
      label55:
      return false;
    }
  }
  
  private static class NotificationPending
    implements Predicate<Calendar.CalendarData>
  {
    static final NotificationPending INSTANCE = new NotificationPending();
    
    public boolean apply(Calendar.CalendarData paramCalendarData)
    {
      return ((!paramCalendarData.hasClientActions()) || (!paramCalendarData.getClientActions().getIsDismissed())) && ((!paramCalendarData.hasClientActions()) || (!paramCalendarData.getClientActions().getIsNotified())) && (paramCalendarData.hasServerData()) && (paramCalendarData.getServerData().hasNotifyTimeSeconds());
    }
  }
  
  private static class NotifyingAt
    implements Predicate<Calendar.CalendarData>
  {
    private final long mQueryTimeSecs;
    
    NotifyingAt(long paramLong)
    {
      this.mQueryTimeSecs = paramLong;
    }
    
    public boolean apply(Calendar.CalendarData paramCalendarData)
    {
      return (CalendarDataProviderImpl.NotificationPending.INSTANCE.apply(paramCalendarData)) && (paramCalendarData.getServerData().getNotifyTimeSeconds() - 60L <= this.mQueryTimeSecs);
    }
  }
  
  private static class ToUploadCalendarData
    implements Function<Calendar.CalendarData, Sidekick.UploadCalendarData>
  {
    static final ToUploadCalendarData INSTANCE = new ToUploadCalendarData();
    
    public Sidekick.UploadCalendarData apply(Calendar.CalendarData paramCalendarData)
    {
      Calendar.EventData localEventData = paramCalendarData.getEventData();
      Calendar.ServerData localServerData = paramCalendarData.getServerData();
      Sidekick.Location localLocation = new Sidekick.Location();
      boolean bool1 = localEventData.hasWhereField();
      int i = 0;
      if (bool1)
      {
        boolean bool2 = localEventData.getPotentiallyGeocodableWhereField();
        i = 0;
        if (bool2)
        {
          localLocation.setName(localEventData.getWhereField());
          i = 1;
        }
      }
      if ((localServerData.getIsGeocodable()) && (localServerData.getGeocodedLatLng().hasLat()) && (localServerData.getGeocodedLatLng().hasLng()))
      {
        localLocation.setLat(localServerData.getGeocodedLatLng().getLat());
        localLocation.setLng(localServerData.getGeocodedLatLng().getLng());
        i = 1;
      }
      Sidekick.UploadCalendarData localUploadCalendarData = new Sidekick.UploadCalendarData();
      localUploadCalendarData.setHash(localServerData.getServerHash()).setStartTimeSeconds(localEventData.getStartTimeSeconds()).setEndTimeSeconds(localEventData.getEndTimeSeconds()).setSelfAttendeeStatus(localEventData.getSelfAttendeeStatus());
      String str = CalendarDataUtil.getHashString(paramCalendarData.getId());
      if (str != null) {
        localUploadCalendarData.setAccountHash(str);
      }
      if (i != 0) {
        localUploadCalendarData.setLocation(localLocation);
      }
      return localUploadCalendarData;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.calendar.CalendarDataProviderImpl
 * JD-Core Version:    0.7.0.1
 */