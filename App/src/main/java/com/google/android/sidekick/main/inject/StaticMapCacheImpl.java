package com.google.android.sidekick.main.inject;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.entry.EntryProviderObserver;
import com.google.android.sidekick.shared.util.StaticMapKey;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.io.Closeables;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.SensorSignals;
import com.google.geo.sidekick.Sidekick.StaticMapQuery;
import com.google.geo.sidekick.Sidekick.StaticMapResponse;
import com.google.geo.sidekick.Sidekick.TimestampedLocation;
import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import javax.annotation.Nullable;

public class StaticMapCacheImpl
  implements StaticMapCache
{
  private static final String TAG = Tag.getTag(StaticMapCacheImpl.class);
  private final Context mAppContext;
  private final LruCache<StaticMapKey, Bitmap> mCache;
  private final int mMapHeight;
  private final int mMapWidth;
  private final NetworkClient mNetworkClient;
  
  public StaticMapCacheImpl(EntryProvider paramEntryProvider, Context paramContext, NetworkClient paramNetworkClient)
  {
    this.mAppContext = paramContext;
    this.mNetworkClient = paramNetworkClient;
    this.mCache = new LruCache(15);
    paramEntryProvider.registerEntryProviderObserver(new CacheInvalidator(null));
    this.mMapWidth = LayoutUtils.getCardWidth(paramContext);
    this.mMapHeight = paramContext.getResources().getDimensionPixelSize(2131689723);
  }
  
  private Bitmap fetchFromNetwork(Sidekick.Location paramLocation, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, boolean paramBoolean)
  {
    ExtraPreconditions.checkNotMainThread();
    Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = new Sidekick.FrequentPlaceEntry();
    try
    {
      localFrequentPlaceEntry.mergeFrom(paramFrequentPlaceEntry.toByteArray());
      label22:
      if (localFrequentPlaceEntry.hasFrequentPlace()) {
        localFrequentPlaceEntry.getFrequentPlace().clearPlaceData().clearAlternatePlaceData();
      }
      if (!paramBoolean) {
        localFrequentPlaceEntry.clearRoute();
      }
      Sidekick.StaticMapQuery localStaticMapQuery = new Sidekick.StaticMapQuery().setWidth(this.mMapWidth).setHeight(this.mMapHeight).setPlaceEntry(localFrequentPlaceEntry);
      if (paramLocation != null) {
        localStaticMapQuery.setStartLocation(paramLocation);
      }
      Sidekick.ResponsePayload localResponsePayload = this.mNetworkClient.sendRequestWithLocation(new Sidekick.RequestPayload().setStaticMapQuery(localStaticMapQuery));
      if ((localResponsePayload != null) && (localResponsePayload.hasStaticMapResponse()))
      {
        ByteStringMicro localByteStringMicro = localResponsePayload.getStaticMapResponse().getMapPng();
        return BitmapFactory.decodeByteArray(localByteStringMicro.toByteArray(), 0, localByteStringMicro.size());
      }
      return null;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      break label22;
    }
  }
  
  public Bitmap get(Sidekick.Location paramLocation, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, boolean paramBoolean)
  {
    ExtraPreconditions.checkNotMainThread();
    StaticMapKey localStaticMapKey = new StaticMapKey(paramLocation, paramFrequentPlaceEntry, paramBoolean);
    Bitmap localBitmap = (Bitmap)this.mCache.get(localStaticMapKey);
    if (localBitmap == null)
    {
      localBitmap = fetchFromNetwork(paramLocation, paramFrequentPlaceEntry, paramBoolean);
      if (localBitmap != null) {
        this.mCache.put(localStaticMapKey, localBitmap);
      }
    }
    return localBitmap;
  }
  
  public Bitmap getSampleMap()
  {
    InputStream localInputStream = null;
    Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = new Sidekick.FrequentPlaceEntry();
    try
    {
      localInputStream = this.mAppContext.getAssets().open("sample_route.txt.bin");
      localFrequentPlaceEntry.mergeFrom(CodedInputStreamMicro.newInstance(localInputStream));
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      for (;;)
      {
        Sidekick.StaticMapQuery localStaticMapQuery;
        Sidekick.Location localLocation;
        Sidekick.TimestampedLocation localTimestampedLocation;
        Sidekick.SensorSignals localSensorSignals;
        Sidekick.RequestPayload localRequestPayload;
        Sidekick.ResponsePayload localResponsePayload;
        ByteStringMicro localByteStringMicro;
        Log.w(TAG, "File not found: ", localFileNotFoundException);
        Closeables.closeQuietly(localInputStream);
      }
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      for (;;)
      {
        Log.w(TAG, "IO Exception: ", localInvalidProtocolBufferMicroException);
        Closeables.closeQuietly(localInputStream);
      }
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        Log.w(TAG, "IO Exception: ", localIOException);
        Closeables.closeQuietly(localInputStream);
      }
    }
    finally
    {
      Closeables.closeQuietly(localInputStream);
    }
    localStaticMapQuery = new Sidekick.StaticMapQuery().setWidth(this.mMapWidth).setHeight(this.mMapHeight).setPlaceEntry(localFrequentPlaceEntry);
    localLocation = new Sidekick.Location().setLat(37.429099999999998D).setLng(-122.1692D);
    localTimestampedLocation = new Sidekick.TimestampedLocation().setLocation(localLocation);
    localSensorSignals = new Sidekick.SensorSignals().addTimestampedLocation(localTimestampedLocation);
    localRequestPayload = new Sidekick.RequestPayload().setStaticMapQuery(localStaticMapQuery).setSensorSignals(localSensorSignals);
    localResponsePayload = this.mNetworkClient.sendRequestWithoutLocation(localRequestPayload);
    if ((localResponsePayload != null) && (localResponsePayload.hasStaticMapResponse()))
    {
      localByteStringMicro = localResponsePayload.getStaticMapResponse().getMapPng();
      return BitmapFactory.decodeByteArray(localByteStringMicro.toByteArray(), 0, localByteStringMicro.size());
    }
    return null;
  }
  
  private class CacheInvalidator
    implements EntryProviderObserver
  {
    private CacheInvalidator() {}
    
    public void onEntriesAdded(Sidekick.EntryTree paramEntryTree) {}
    
    public void onEntryDismissed(Sidekick.Entry paramEntry, @Nullable Collection<Sidekick.Entry> paramCollection) {}
    
    public void onEntryUpdate(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2, Sidekick.Entry paramEntry3) {}
    
    public void onInvalidated()
    {
      StaticMapCacheImpl.this.mCache.evictAll();
    }
    
    public void onRefreshed(Bundle paramBundle)
    {
      StaticMapCacheImpl.this.mCache.evictAll();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.StaticMapCacheImpl
 * JD-Core Version:    0.7.0.1
 */