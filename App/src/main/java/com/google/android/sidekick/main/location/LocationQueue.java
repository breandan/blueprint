package com.google.android.sidekick.main.location;

import android.location.Location;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.util.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class LocationQueue
{
  private static final String TAG = Tag.getTag(LocationQueue.class);
  private final Clock mClock;
  private final LinkedList<Location> mLocations = new LinkedList();
  
  public LocationQueue(Clock paramClock)
  {
    this.mClock = paramClock;
  }
  
  void addLocation(Location paramLocation)
  {
    label148:
    label278:
    for (;;)
    {
      try
      {
        boolean bool = paramLocation.hasAccuracy();
        if (!bool) {
          return;
        }
        if (paramLocation.getTime() > this.mClock.currentTimeMillis()) {
          paramLocation.setTime(this.mClock.currentTimeMillis());
        }
        paramLocation.setTime(1000L * ((500L + paramLocation.getTime()) / 1000L));
        if (this.mLocations.isEmpty())
        {
          this.mLocations.addFirst(paramLocation);
          long l = this.mClock.currentTimeMillis() - 1200000L;
          if ((!this.mLocations.isEmpty()) && (((Location)this.mLocations.getLast()).getTime() < l))
          {
            this.mLocations.removeLast();
            continue;
          }
          continue;
        }
        i = 1;
      }
      finally {}
      int i;
      int j = 0;
      Location localLocation;
      if (j < this.mLocations.size())
      {
        localLocation = (Location)this.mLocations.get(j);
        if (localLocation.getTime() != paramLocation.getTime()) {
          break label239;
        }
        if (paramLocation.getAccuracy() >= localLocation.getAccuracy()) {
          break label274;
        }
        this.mLocations.remove(j);
        this.mLocations.add(j, paramLocation);
        break label274;
      }
      for (;;)
      {
        if (i == 0) {
          break label278;
        }
        this.mLocations.addLast(paramLocation);
        break;
        label239:
        if (localLocation.getTime() < paramLocation.getTime())
        {
          this.mLocations.add(j, paramLocation);
          i = 0;
        }
        else
        {
          j++;
          break label148;
          i = 0;
        }
      }
    }
  }
  
  void clearLocations()
  {
    try
    {
      this.mLocations.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  Location getBestLocation()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: invokevirtual 105	com/google/android/sidekick/main/location/LocationQueue:getBestLocationsInIntervals	(I)Ljava/util/List;
    //   7: astore_2
    //   8: aload_2
    //   9: invokeinterface 108 1 0
    //   14: istore_3
    //   15: iload_3
    //   16: ifeq +11 -> 27
    //   19: aconst_null
    //   20: astore 4
    //   22: aload_0
    //   23: monitorexit
    //   24: aload 4
    //   26: areturn
    //   27: aload_2
    //   28: iconst_0
    //   29: invokeinterface 109 2 0
    //   34: checkcast 36	android/location/Location
    //   37: astore 4
    //   39: goto -17 -> 22
    //   42: astore_1
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_1
    //   46: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	47	0	this	LocationQueue
    //   42	4	1	localObject	Object
    //   7	21	2	localList	List
    //   14	2	3	bool	boolean
    //   20	18	4	localLocation	Location
    // Exception table:
    //   from	to	target	type
    //   2	15	42	finally
    //   27	39	42	finally
  }
  
  List<Location> getBestLocations()
  {
    ArrayList localArrayList;
    try
    {
      localArrayList = new ArrayList();
      Iterator localIterator = getBestLocationsInIntervals(3).iterator();
      while (localIterator.hasNext())
      {
        Location localLocation = (Location)localIterator.next();
        if (localLocation != null) {
          localArrayList.add(localLocation);
        }
      }
    }
    finally {}
    return localArrayList;
  }
  
  List<Location> getBestLocationsInIntervals(int paramInt)
  {
    for (;;)
    {
      long l;
      Object localObject2;
      Location localLocation2;
      try
      {
        LinkedList localLinkedList = new LinkedList();
        l = this.mClock.currentTimeMillis();
        localObject2 = null;
        if (paramInt > 3) {
          paramInt = 3;
        }
        Iterator localIterator1 = this.mLocations.iterator();
        if (localIterator1.hasNext())
        {
          localLocation2 = (Location)localIterator1.next();
          if (localLinkedList.size() < paramInt) {}
        }
        else
        {
          if ((localObject2 != null) && (localLinkedList.size() < paramInt)) {
            localLinkedList.addFirst(localObject2);
          }
          Iterator localIterator2 = localLinkedList.iterator();
          if (localIterator2.hasNext())
          {
            Location localLocation1 = (Location)localIterator2.next();
            if (localLocation1 == null) {
              continue;
            }
          }
          return localLinkedList;
        }
        if (localLocation2.getTime() > l) {
          continue;
        }
        if (localLocation2.getTime() < l - 300000L)
        {
          if (localObject2 == null) {
            break label225;
          }
          localLinkedList.addFirst(localObject2);
          break label225;
        }
        if (localLocation2.getAccuracy() >= 100.0F) {
          break label253;
        }
        if (localLocation2 == null) {
          break label239;
        }
        localLinkedList.addFirst(localLocation2);
      }
      finally {}
      float f1 = localObject2.getAccuracy();
      float f2 = localLocation2.getAccuracy();
      if (f1 > f2)
      {
        localObject2 = localLocation2;
        continue;
        label225:
        l -= 300000L;
        localObject2 = null;
        continue;
        label239:
        l -= 300000L;
        localObject2 = null;
        continue;
        label253:
        if (localObject2 == null) {
          localObject2 = localLocation2;
        }
      }
    }
  }
  
  List<Location> getRawLocations()
  {
    try
    {
      List localList = Collections.unmodifiableList(this.mLocations);
      return localList;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.location.LocationQueue
 * JD-Core Version:    0.7.0.1
 */