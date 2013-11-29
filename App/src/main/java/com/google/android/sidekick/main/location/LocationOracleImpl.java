package com.google.android.sidekick.main.location;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.apps.sidekick.LocationOracleStore.AndroidLocationProto;
import com.google.android.apps.sidekick.LocationOracleStore.LocationOracleData;
import com.google.android.search.core.GmsClientWrapper.GmsFuture;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.LocationSettings.Observer;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.Clock.TimeResetListener;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ExtraPreconditions.ThreadCheck;
import com.google.android.shared.util.StateMachine;
import com.google.android.shared.util.StateMachine.Builder;
import com.google.android.sidekick.main.file.FileBytesReader;
import com.google.android.sidekick.main.file.FileBytesWriter;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.location.GmsLocationProvider;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class LocationOracleImpl
  implements LocationOracle
{
  static final long LOCATION_QUERY_INTERVAL_IN_MILLIS = 285000L;
  private static final String TAG = Tag.getTag(LocationOracleImpl.class);
  private Handler mBgHandler;
  private HandlerThread mBgThread;
  private final ExtraPreconditions.ThreadCheck mBgThreadCheck = ExtraPreconditions.createSameThreadCheck();
  private final Runnable mCheckSettingTask = new CheckSettingTask(null);
  private final Clock mClock;
  private final DebugFeatures mDebugFeatures;
  private List<Location> mDebugLocations;
  private final FileBytesReader mFileReader;
  private final FileBytesWriter mFileWriter;
  private final Set<LocationOracle.LightweightGeofencer> mGeofencers = Sets.newHashSet();
  private final GmsLocationProvider mGmsLocationProvider;
  private final GsaPreferenceController mGsaPreferenceController;
  private Location mLastGeofenceLocation;
  private int mLastLocationsFileHash;
  private Location mLastSignificantLocationChange;
  private final LocalBroadcastManager mLocalBroadcastManager;
  private final LocationManagerInjectable mLocationManager;
  final LocationQueue mLocationQueue;
  private final LocationSettings mLocationSettings;
  private final LocationStorage mLocationStorage;
  private final LocationWatchDog mLocationWatchDog = new LocationWatchDog();
  private final Object mLock = new Object();
  private final Runnable mPurgeLocationsTask = new PurgeLocationsTask(null);
  private final WeakHashMap<LocationOracle.RunningLock, String> mRunningLocks = new WeakHashMap();
  private final LocationSettings.Observer mSettingsObserver;
  private final StateMachine<State> mState = StateMachine.newBuilder(TAG, State.STOPPED).addTransition(State.STOPPED, State.STARTED).addTransition(State.STARTED, State.LISTENING).addTransition(State.STARTED, State.STOPPED).addTransition(State.LISTENING, State.STARTED).addTransition(State.LISTENING, State.STOPPED).setSingleThreadOnly(true).setStrictMode(true).build();
  private final Clock.TimeResetListener mTimeResetListener = new ClockResetListener(null);
  
  public LocationOracleImpl(LocationManagerInjectable paramLocationManagerInjectable, GmsLocationProvider paramGmsLocationProvider, Clock paramClock, FileBytesReader paramFileBytesReader, FileBytesWriter paramFileBytesWriter, LocationSettings paramLocationSettings, DebugFeatures paramDebugFeatures, GsaPreferenceController paramGsaPreferenceController, LocalBroadcastManager paramLocalBroadcastManager, LocationStorage paramLocationStorage)
  {
    this.mLocationQueue = new LocationQueue(paramClock);
    this.mLocationManager = paramLocationManagerInjectable;
    this.mGmsLocationProvider = paramGmsLocationProvider;
    this.mFileReader = paramFileBytesReader;
    this.mFileWriter = paramFileBytesWriter;
    this.mClock = paramClock;
    this.mLocationSettings = paramLocationSettings;
    this.mSettingsObserver = new SettingsObserver(null);
    this.mDebugFeatures = paramDebugFeatures;
    this.mGsaPreferenceController = paramGsaPreferenceController;
    this.mLocalBroadcastManager = paramLocalBroadcastManager;
    this.mLocationStorage = paramLocationStorage;
  }
  
  private static Location androidLocationFromProto(LocationOracleStore.AndroidLocationProto paramAndroidLocationProto)
  {
    Location localLocation = new Location(paramAndroidLocationProto.getProvider());
    localLocation.setLatitude(paramAndroidLocationProto.getLat());
    localLocation.setLongitude(paramAndroidLocationProto.getLng());
    localLocation.setTime(paramAndroidLocationProto.getTimestampMillis());
    if (paramAndroidLocationProto.hasAccuracyMeters()) {
      localLocation.setAccuracy(paramAndroidLocationProto.getAccuracyMeters());
    }
    return localLocation;
  }
  
  private static int androidLocationHashCode(Location paramLocation)
  {
    if (paramLocation.hasAccuracy()) {}
    for (Float localFloat = Float.valueOf(paramLocation.getAccuracy());; localFloat = null)
    {
      Object[] arrayOfObject = new Object[7];
      arrayOfObject[0] = paramLocation.getProvider();
      arrayOfObject[1] = Double.valueOf(paramLocation.getLatitude());
      arrayOfObject[2] = Double.valueOf(paramLocation.getLongitude());
      arrayOfObject[3] = Long.valueOf(paramLocation.getTime());
      arrayOfObject[4] = localFloat;
      arrayOfObject[5] = null;
      arrayOfObject[6] = null;
      return Objects.hashCode(arrayOfObject);
    }
  }
  
  private static LocationOracleStore.AndroidLocationProto androidLocationToProto(Location paramLocation)
  {
    LocationOracleStore.AndroidLocationProto localAndroidLocationProto1 = new LocationOracleStore.AndroidLocationProto();
    if (paramLocation.getProvider() != null) {}
    for (String str = paramLocation.getProvider();; str = "")
    {
      LocationOracleStore.AndroidLocationProto localAndroidLocationProto2 = localAndroidLocationProto1.setProvider(str).setLat(paramLocation.getLatitude()).setLng(paramLocation.getLongitude()).setTimestampMillis(paramLocation.getTime());
      if (paramLocation.hasAccuracy()) {
        localAndroidLocationProto2.setAccuracyMeters(paramLocation.getAccuracy());
      }
      return localAndroidLocationProto2;
    }
  }
  
  private void broadcastIfMoved(Location paramLocation)
  {
    this.mBgThreadCheck.check();
    this.mState.checkIn(State.LISTENING);
    if (locationChangedSignificantly(this.mLastSignificantLocationChange, paramLocation))
    {
      this.mLocationStorage.saveCurrentLocation(paramLocation, getPrefs(), "lastloc");
      this.mLastSignificantLocationChange = paramLocation;
      Intent localIntent = new Intent("com.google.android.apps.sidekick.LOCATION_CHANGED_SIGNIFICANTLY");
      localIntent.putExtra("location", paramLocation);
      this.mLocalBroadcastManager.sendBroadcast(localIntent);
    }
  }
  
  private Handler getBgHandler()
  {
    synchronized (this.mLock)
    {
      Preconditions.checkNotNull(this.mBgThread);
      for (;;)
      {
        Handler localHandler1 = this.mBgHandler;
        if (localHandler1 == null) {
          try
          {
            this.mLock.wait();
          }
          catch (InterruptedException localInterruptedException)
          {
            Log.w(TAG, "Interrupted while waiting for thread start", localInterruptedException);
          }
        }
      }
    }
    Handler localHandler2 = this.mBgHandler;
    return localHandler2;
  }
  
  private SharedPreferencesExt getPrefs()
  {
    return this.mGsaPreferenceController.getMainPreferences();
  }
  
  /* Error */
  private void initializeFromStorage()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 179	com/google/android/sidekick/main/location/LocationOracleImpl:mFileReader	Lcom/google/android/sidekick/main/file/FileBytesReader;
    //   4: ldc_w 463
    //   7: ldc_w 464
    //   10: invokevirtual 470	com/google/android/sidekick/main/file/FileBytesReader:readEncryptedFileBytes	(Ljava/lang/String;I)[B
    //   13: astore_1
    //   14: iconst_1
    //   15: istore_2
    //   16: aload_1
    //   17: ifnull +160 -> 177
    //   20: new 472	com/google/android/apps/sidekick/LocationOracleStore$LocationOracleData
    //   23: dup
    //   24: invokespecial 473	com/google/android/apps/sidekick/LocationOracleStore$LocationOracleData:<init>	()V
    //   27: astore_3
    //   28: aload_3
    //   29: aload_1
    //   30: invokevirtual 477	com/google/android/apps/sidekick/LocationOracleStore$LocationOracleData:mergeFrom	([B)Lcom/google/protobuf/micro/MessageMicro;
    //   33: pop
    //   34: aload_3
    //   35: invokevirtual 481	com/google/android/apps/sidekick/LocationOracleStore$LocationOracleData:getLocationCount	()I
    //   38: invokestatic 487	com/google/common/collect/Lists:newArrayListWithCapacity	(I)Ljava/util/ArrayList;
    //   41: astore 8
    //   43: aload_3
    //   44: invokevirtual 491	com/google/android/apps/sidekick/LocationOracleStore$LocationOracleData:getLocationList	()Ljava/util/List;
    //   47: invokeinterface 497 1 0
    //   52: astore 9
    //   54: aload 9
    //   56: invokeinterface 502 1 0
    //   61: ifeq +61 -> 122
    //   64: aload 9
    //   66: invokeinterface 506 1 0
    //   71: checkcast 284	com/google/android/apps/sidekick/LocationOracleStore$AndroidLocationProto
    //   74: invokestatic 508	com/google/android/sidekick/main/location/LocationOracleImpl:androidLocationFromProto	(Lcom/google/android/apps/sidekick/LocationOracleStore$AndroidLocationProto;)Landroid/location/Location;
    //   77: astore 15
    //   79: aload 8
    //   81: aload 15
    //   83: invokeinterface 512 2 0
    //   88: pop
    //   89: aload_0
    //   90: getfield 173	com/google/android/sidekick/main/location/LocationOracleImpl:mLocationQueue	Lcom/google/android/sidekick/main/location/LocationQueue;
    //   93: aload 15
    //   95: invokevirtual 515	com/google/android/sidekick/main/location/LocationQueue:addLocation	(Landroid/location/Location;)V
    //   98: goto -44 -> 54
    //   101: astore 5
    //   103: getstatic 78	com/google/android/sidekick/main/location/LocationOracleImpl:TAG	Ljava/lang/String;
    //   106: ldc_w 517
    //   109: invokestatic 521	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   112: pop
    //   113: iload_2
    //   114: ifeq +7 -> 121
    //   117: aload_0
    //   118: invokespecial 233	com/google/android/sidekick/main/location/LocationOracleImpl:queueCurrentLocation	()V
    //   121: return
    //   122: aload_0
    //   123: aload 8
    //   125: invokestatic 525	com/google/android/sidekick/main/location/LocationOracleImpl:locationListHashCode	(Ljava/util/List;)I
    //   128: putfield 527	com/google/android/sidekick/main/location/LocationOracleImpl:mLastLocationsFileHash	I
    //   131: aload_0
    //   132: getfield 173	com/google/android/sidekick/main/location/LocationOracleImpl:mLocationQueue	Lcom/google/android/sidekick/main/location/LocationQueue;
    //   135: invokevirtual 531	com/google/android/sidekick/main/location/LocationQueue:getBestLocation	()Landroid/location/Location;
    //   138: astore 10
    //   140: aload 10
    //   142: ifnull +35 -> 177
    //   145: aload_0
    //   146: getfield 183	com/google/android/sidekick/main/location/LocationOracleImpl:mClock	Lcom/google/android/shared/util/Clock;
    //   149: invokeinterface 536 1 0
    //   154: lstore 11
    //   156: aload 10
    //   158: invokevirtual 353	android/location/Location:getTime	()J
    //   161: lstore 13
    //   163: lload 11
    //   165: lload 13
    //   167: lsub
    //   168: ldc2_w 9
    //   171: lcmp
    //   172: ifle +14 -> 186
    //   175: iconst_1
    //   176: istore_2
    //   177: iload_2
    //   178: ifeq -57 -> 121
    //   181: aload_0
    //   182: invokespecial 233	com/google/android/sidekick/main/location/LocationOracleImpl:queueCurrentLocation	()V
    //   185: return
    //   186: iconst_0
    //   187: istore_2
    //   188: goto -11 -> 177
    //   191: astore 4
    //   193: iload_2
    //   194: ifeq +7 -> 201
    //   197: aload_0
    //   198: invokespecial 233	com/google/android/sidekick/main/location/LocationOracleImpl:queueCurrentLocation	()V
    //   201: aload 4
    //   203: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	204	0	this	LocationOracleImpl
    //   13	17	1	arrayOfByte	byte[]
    //   15	179	2	i	int
    //   27	17	3	localLocationOracleData	LocationOracleStore.LocationOracleData
    //   191	11	4	localObject	Object
    //   101	1	5	localInvalidProtocolBufferMicroException	com.google.protobuf.micro.InvalidProtocolBufferMicroException
    //   41	83	8	localArrayList	ArrayList
    //   52	13	9	localIterator	Iterator
    //   138	19	10	localLocation1	Location
    //   154	10	11	l1	long
    //   161	5	13	l2	long
    //   77	17	15	localLocation2	Location
    // Exception table:
    //   from	to	target	type
    //   20	54	101	com/google/protobuf/micro/InvalidProtocolBufferMicroException
    //   54	98	101	com/google/protobuf/micro/InvalidProtocolBufferMicroException
    //   122	140	101	com/google/protobuf/micro/InvalidProtocolBufferMicroException
    //   145	163	101	com/google/protobuf/micro/InvalidProtocolBufferMicroException
    //   20	54	191	finally
    //   54	98	191	finally
    //   103	113	191	finally
    //   122	140	191	finally
    //   145	163	191	finally
  }
  
  private boolean locationChangedSignificantly(@Nullable Location paramLocation1, @Nullable Location paramLocation2)
  {
    boolean bool = true;
    this.mBgThreadCheck.check();
    if (paramLocation2 == null) {
      bool = false;
    }
    while ((paramLocation1 == null) || (LocationUtilities.distanceBetween(paramLocation1, paramLocation2) > 10000.0F)) {
      return bool;
    }
    return false;
  }
  
  private static int locationListHashCode(List<Location> paramList)
  {
    int[] arrayOfInt = new int[paramList.size()];
    for (int i = -1 + paramList.size(); i >= 0; i--) {
      arrayOfInt[i] = androidLocationHashCode((Location)paramList.get(i));
    }
    return Arrays.hashCode(arrayOfInt);
  }
  
  private void queueCurrentLocation()
  {
    this.mBgThreadCheck.check();
    Location localLocation = (Location)this.mGmsLocationProvider.getLastLocation().safeGet(1000L, TimeUnit.MILLISECONDS);
    if (localLocation != null)
    {
      queueLocations(true, new Location[] { localLocation });
      return;
    }
    Location[] arrayOfLocation = new Location[2];
    arrayOfLocation[0] = this.mLocationManager.getLastKnownLocation("network");
    arrayOfLocation[1] = this.mLocationManager.getLastKnownLocation("gps");
    queueLocations(false, arrayOfLocation);
  }
  
  private void queueLocations(boolean paramBoolean, @Nullable Location... paramVarArgs)
  {
    this.mBgThreadCheck.check();
    int i = 0;
    int j = paramVarArgs.length;
    for (int k = 0; k < j; k++)
    {
      Location localLocation2 = paramVarArgs[k];
      if (localLocation2 != null)
      {
        this.mLocationQueue.addLocation(localLocation2);
        i = 1;
      }
    }
    if (i != 0)
    {
      writeQueuedLocationsToStorage();
      Location localLocation1 = getBestLocation();
      updateGeofencers(localLocation1);
      broadcastIfMoved(localLocation1);
      if (paramBoolean) {
        this.mLocationWatchDog.reset();
      }
    }
  }
  
  private void startGmsLocationUpdates()
  {
    this.mGmsLocationProvider.startBackgroundUpdates(285000L);
  }
  
  private void startInternal()
  {
    this.mState.checkIn(State.STOPPED);
    this.mState.moveTo(State.STARTED);
    this.mLocationSettings.addUseLocationObserver(this.mSettingsObserver);
    if (this.mLocationSettings.canUseLocationForGoogleApps()) {
      startListening();
    }
  }
  
  private void startListening()
  {
    this.mBgThreadCheck.check();
    if (!this.mLocationSettings.canUseLocationForGoogleApps()) {
      return;
    }
    this.mState.moveTo(State.LISTENING);
    startGmsLocationUpdates();
    this.mClock.registerTimeResetListener(this.mTimeResetListener);
    this.mLocationWatchDog.reset();
    initializeFromStorage();
    this.mLastGeofenceLocation = this.mLocationStorage.readCurrentLocation(getPrefs(), "lastgeofenceloc");
    this.mLastSignificantLocationChange = this.mLocationStorage.readCurrentLocation(getPrefs(), "lastloc");
  }
  
  private void startLocked()
  {
    ExtraPreconditions.checkHoldsLock(this.mLock);
    if (this.mBgThread == null)
    {
      this.mBgThread = new HandlerThread(TAG, 10)
      {
        protected void onLooperPrepared()
        {
          super.onLooperPrepared();
          LocationOracleImpl.this.mBgThreadCheck.check();
          synchronized (LocationOracleImpl.this.mLock)
          {
            LocationOracleImpl.access$1002(LocationOracleImpl.this, new Handler(Looper.myLooper()));
            LocationOracleImpl.this.mLock.notifyAll();
            LocationOracleImpl.this.startInternal();
            return;
          }
        }
      };
      this.mBgThread.start();
      return;
    }
    getBgHandler().post(new Runnable()
    {
      public void run()
      {
        LocationOracleImpl.this.startInternal();
      }
    });
  }
  
  private void stop()
  {
    getBgHandler().post(new Runnable()
    {
      public void run()
      {
        if (LocationOracleImpl.this.mState.isIn(LocationOracleImpl.State.LISTENING)) {
          LocationOracleImpl.this.stopListening();
        }
        for (;;)
        {
          LocationOracleImpl.this.mState.moveTo(LocationOracleImpl.State.STOPPED);
          LocationOracleImpl.this.mLocationSettings.removeUseLocationObserver(LocationOracleImpl.this.mSettingsObserver);
          LocationOracleImpl.this.mBgHandler.removeCallbacks(LocationOracleImpl.this.mCheckSettingTask);
          return;
          LocationOracleImpl.this.mState.checkIn(LocationOracleImpl.State.STARTED);
        }
      }
    });
  }
  
  private void stopGmsLocationUpdates()
  {
    this.mGmsLocationProvider.stopBackgroundUpdates();
  }
  
  private void stopListening()
  {
    this.mBgThreadCheck.check();
    updateGeofencers(null);
    broadcastIfMoved(null);
    this.mState.moveTo(State.STARTED);
    stopGmsLocationUpdates();
    this.mClock.unregisterTimeResetListener(this.mTimeResetListener);
    this.mBgHandler.removeCallbacks(this.mPurgeLocationsTask);
    this.mBgHandler.removeCallbacks(this.mLocationWatchDog);
    synchronized (this.mLock)
    {
      this.mDebugLocations = null;
      this.mLocationQueue.clearLocations();
      this.mFileWriter.deleteFile("loracle");
      return;
    }
  }
  
  private void updateGeofencers(@Nullable Location paramLocation)
  {
    this.mBgThreadCheck.check();
    this.mState.checkIn(State.LISTENING);
    if (((this.mLastGeofenceLocation == null) && (paramLocation == null)) || ((this.mLastGeofenceLocation != null) && (paramLocation != null) && (LocationUtilities.areLocationsEqual(this.mLastGeofenceLocation, paramLocation)))) {
      return;
    }
    synchronized (this.mLock)
    {
      Iterator localIterator = this.mGeofencers.iterator();
      if (localIterator.hasNext()) {
        ((LocationOracle.LightweightGeofencer)localIterator.next()).onLocationChanged(this.mLastGeofenceLocation, paramLocation);
      }
    }
    this.mLocationStorage.saveCurrentLocation(paramLocation, getPrefs(), "lastgeofenceloc");
    this.mLastGeofenceLocation = paramLocation;
  }
  
  private void writeQueuedLocationsToStorage()
  {
    this.mBgThreadCheck.check();
    List localList = Lists.reverse(this.mLocationQueue.getRawLocations());
    int i = locationListHashCode(localList);
    if (i == this.mLastLocationsFileHash) {}
    LocationOracleStore.LocationOracleData localLocationOracleData;
    do
    {
      return;
      ArrayList localArrayList = Lists.newArrayListWithCapacity(localList.size());
      Iterator localIterator1 = localList.iterator();
      while (localIterator1.hasNext()) {
        localArrayList.add(androidLocationToProto((Location)localIterator1.next()));
      }
      localLocationOracleData = new LocationOracleStore.LocationOracleData();
      Iterator localIterator2 = localArrayList.iterator();
      while (localIterator2.hasNext()) {
        localLocationOracleData.addLocation((LocationOracleStore.AndroidLocationProto)localIterator2.next());
      }
    } while (!this.mFileWriter.writeEncryptedFileBytes("loracle", localLocationOracleData.toByteArray(), 524288));
    this.mLastLocationsFileHash = i;
  }
  
  public void addLightweightGeofencer(LocationOracle.LightweightGeofencer paramLightweightGeofencer)
  {
    synchronized (this.mLock)
    {
      this.mGeofencers.add(paramLightweightGeofencer);
      return;
    }
  }
  
  public Location blockingUpdateBestLocation()
  {
    (Location)postAndWait(new Callable()
    {
      public Location call()
        throws Exception
      {
        if (LocationOracleImpl.this.mState.isIn(LocationOracleImpl.State.LISTENING)) {
          LocationOracleImpl.this.queueCurrentLocation();
        }
        return LocationOracleImpl.this.getBestLocation();
      }
    });
  }
  
  @Nullable
  public Location getBestLocation()
  {
    List localList = getBestLocations();
    if ((localList != null) && (!localList.isEmpty())) {
      return (Location)localList.get(-1 + localList.size());
    }
    Log.w(TAG, "Best location was null");
    return null;
  }
  
  public List<Location> getBestLocations()
  {
    synchronized (this.mLock)
    {
      if ((this.mDebugLocations != null) && (!this.mDebugLocations.isEmpty()))
      {
        List localList = this.mDebugLocations;
        return localList;
      }
      return this.mLocationQueue.getBestLocations();
    }
  }
  
  public boolean hasLocation()
  {
    return !getBestLocations().isEmpty();
  }
  
  public LocationOracle.RunningLock newRunningLock(final String paramString)
  {
    new LocationOracle.RunningLock()
    {
      public void acquire()
      {
        for (;;)
        {
          synchronized (LocationOracleImpl.this.mLock)
          {
            if (!LocationOracleImpl.this.mRunningLocks.containsKey(this))
            {
              bool = true;
              Preconditions.checkState(bool);
              LocationOracleImpl.this.mRunningLocks.put(this, paramString);
              if (LocationOracleImpl.this.mRunningLocks.size() == 1) {
                LocationOracleImpl.this.startLocked();
              }
              return;
            }
          }
          boolean bool = false;
        }
      }
      
      protected void finalize()
        throws Throwable
      {
        super.finalize();
        if (LocationOracleImpl.this.mRunningLocks.containsKey(this))
        {
          release();
          Log.e(LocationOracleImpl.TAG, "Forgot to release lock from: '" + paramString + "'");
        }
      }
      
      public void release()
      {
        synchronized (LocationOracleImpl.this.mLock)
        {
          Preconditions.checkState(LocationOracleImpl.this.mRunningLocks.containsKey(this));
          LocationOracleImpl.this.mRunningLocks.remove(this);
          if (LocationOracleImpl.this.mRunningLocks.size() == 0) {
            LocationOracleImpl.this.stop();
          }
          return;
        }
      }
    };
  }
  
  <T> T postAndWait(Callable<T> paramCallable)
  {
    FutureTask localFutureTask = new FutureTask(paramCallable);
    getBgHandler().post(localFutureTask);
    try
    {
      Object localObject = localFutureTask.get();
      return localObject;
    }
    catch (InterruptedException localInterruptedException)
    {
      Log.w(TAG, "Unexpected interruption", localInterruptedException);
      return null;
    }
    catch (ExecutionException localExecutionException)
    {
      for (;;)
      {
        Log.w(TAG, "Unexpected exception", localExecutionException);
      }
    }
  }
  
  public void postLocation(final Location paramLocation)
  {
    if (!this.mLocationSettings.canUseLocationForGoogleApps()) {
      return;
    }
    synchronized (this.mLock)
    {
      if (this.mRunningLocks.isEmpty())
      {
        Log.w(TAG, "Not started: ignore location");
        return;
      }
    }
    getBgHandler().post(new Runnable()
    {
      public void run()
      {
        if (!LocationOracleImpl.this.mState.isIn(LocationOracleImpl.State.LISTENING)) {
          return;
        }
        LocationOracleImpl localLocationOracleImpl = LocationOracleImpl.this;
        Location[] arrayOfLocation = new Location[1];
        arrayOfLocation[0] = paramLocation;
        localLocationOracleImpl.queueLocations(true, arrayOfLocation);
      }
    });
  }
  
  public void pushTestLocations(@Nullable final List<Location> paramList)
  {
    if (this.mDebugFeatures.teamDebugEnabled())
    {
      getBgHandler().post(new Runnable()
      {
        public void run()
        {
          if (LocationOracleImpl.this.mState.isIn(LocationOracleImpl.State.LISTENING)) {}
          synchronized (LocationOracleImpl.this.mLock)
          {
            LocationOracleImpl.access$2502(LocationOracleImpl.this, paramList);
            Location localLocation = LocationOracleImpl.this.getBestLocation();
            LocationOracleImpl.this.updateGeofencers(localLocation);
            LocationOracleImpl.this.broadcastIfMoved(localLocation);
            return;
          }
        }
      });
      return;
    }
    Log.d(TAG, "Can't set debug location if debug features aren't enabled");
  }
  
  public void requestRecentLocation(final long paramLong)
  {
    Location localLocation = getBestLocation();
    if ((localLocation != null) && (this.mClock.currentTimeMillis() - localLocation.getTime() <= paramLong)) {
      return;
    }
    getBgHandler().post(new Runnable()
    {
      public void run()
      {
        if (LocationOracleImpl.this.mState.notIn(LocationOracleImpl.State.LISTENING)) {}
        Location localLocation;
        do
        {
          return;
          LocationOracleImpl.this.startGmsLocationUpdates();
          LocationOracleImpl.this.queueCurrentLocation();
          localLocation = LocationOracleImpl.this.mLocationQueue.getBestLocation();
        } while ((localLocation == null) || (LocationOracleImpl.this.mClock.currentTimeMillis() - localLocation.getTime() >= paramLong));
      }
    });
  }
  
  private class CheckSettingTask
    implements Runnable
  {
    private CheckSettingTask() {}
    
    public void run()
    {
      boolean bool = LocationOracleImpl.this.mLocationSettings.canUseLocationForGoogleApps();
      if ((bool) && (LocationOracleImpl.this.mState.isIn(LocationOracleImpl.State.STARTED))) {
        LocationOracleImpl.this.startListening();
      }
      while ((bool) || (!LocationOracleImpl.this.mState.isIn(LocationOracleImpl.State.LISTENING))) {
        return;
      }
      LocationOracleImpl.this.stopListening();
    }
  }
  
  private class ClockResetListener
    implements Clock.TimeResetListener
  {
    private ClockResetListener() {}
    
    public void onTimeReset()
    {
      LocationOracleImpl.this.getBgHandler().removeCallbacks(LocationOracleImpl.this.mPurgeLocationsTask);
      LocationOracleImpl.this.getBgHandler().post(LocationOracleImpl.this.mPurgeLocationsTask);
    }
  }
  
  class LocationWatchDog
    implements Runnable
  {
    LocationWatchDog() {}
    
    void reset()
    {
      LocationOracleImpl.this.mBgHandler.removeCallbacks(LocationOracleImpl.this.mLocationWatchDog);
      LocationOracleImpl.this.mBgHandler.postDelayed(LocationOracleImpl.this.mLocationWatchDog, 570000L);
    }
    
    public void run()
    {
      reset();
      LocationOracleImpl.this.startGmsLocationUpdates();
    }
  }
  
  private class PurgeLocationsTask
    implements Runnable
  {
    private PurgeLocationsTask() {}
    
    public void run()
    {
      LocationOracleImpl.this.mLocationQueue.clearLocations();
      if (LocationOracleImpl.this.mState.isIn(LocationOracleImpl.State.LISTENING)) {
        LocationOracleImpl.this.queueCurrentLocation();
      }
    }
  }
  
  private class SettingsObserver
    implements LocationSettings.Observer
  {
    private SettingsObserver() {}
    
    public void onUseLocationChanged(boolean paramBoolean)
    {
      LocationOracleImpl.this.getBgHandler().post(LocationOracleImpl.this.mCheckSettingTask);
    }
  }
  
  private static enum State
  {
    static
    {
      STARTED = new State("STARTED", 1);
      LISTENING = new State("LISTENING", 2);
      State[] arrayOfState = new State[3];
      arrayOfState[0] = STOPPED;
      arrayOfState[1] = STARTED;
      arrayOfState[2] = LISTENING;
      $VALUES = arrayOfState;
    }
    
    private State() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.location.LocationOracleImpl
 * JD-Core Version:    0.7.0.1
 */