package com.google.android.sidekick.shared.util;

import com.google.common.base.Objects;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import javax.annotation.Nullable;

public class StaticMapKey
{
  private final ProtoKey<Sidekick.FrequentPlaceEntry> mFrequentPlaceEntryKey;
  private final boolean mShowRoute;
  private final ProtoKey<Sidekick.Location> mStartLocationKey;
  
  public StaticMapKey(@Nullable Sidekick.Location paramLocation, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, boolean paramBoolean)
  {
    this.mStartLocationKey = new ProtoKey(paramLocation);
    this.mFrequentPlaceEntryKey = new ProtoKey(paramFrequentPlaceEntry);
    this.mShowRoute = paramBoolean;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {}
    StaticMapKey localStaticMapKey;
    do
    {
      return true;
      if (!(paramObject instanceof StaticMapKey)) {
        return false;
      }
      localStaticMapKey = (StaticMapKey)paramObject;
    } while ((this.mShowRoute == localStaticMapKey.mShowRoute) && (this.mStartLocationKey.equals(localStaticMapKey.mStartLocationKey)) && (this.mFrequentPlaceEntryKey.equals(localStaticMapKey.mFrequentPlaceEntryKey)));
    return false;
  }
  
  public Sidekick.FrequentPlaceEntry getFrequentPlaceEntry()
  {
    return (Sidekick.FrequentPlaceEntry)this.mFrequentPlaceEntryKey.getProto();
  }
  
  @Nullable
  public Sidekick.Location getLocation()
  {
    return (Sidekick.Location)this.mStartLocationKey.getProto();
  }
  
  public int hashCode()
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = this.mStartLocationKey;
    arrayOfObject[1] = this.mFrequentPlaceEntryKey;
    arrayOfObject[2] = Boolean.valueOf(this.mShowRoute);
    return Objects.hashCode(arrayOfObject);
  }
  
  public boolean isShowRoute()
  {
    return this.mShowRoute;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.StaticMapKey
 * JD-Core Version:    0.7.0.1
 */