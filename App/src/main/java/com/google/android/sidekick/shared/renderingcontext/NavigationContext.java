package com.google.android.sidekick.shared.renderingcontext;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Pair;
import android.util.SparseIntArray;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.geo.sidekick.Sidekick.Location;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

public final class NavigationContext
  implements Parcelable
{
  public static final String BUNDLE_KEY = NavigationContext.class.getName();
  public static final Parcelable.Creator<NavigationContext> CREATOR = new Parcelable.Creator()
  {
    public NavigationContext createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NavigationContext(paramAnonymousParcel, null);
    }
    
    public NavigationContext[] newArray(int paramAnonymousInt)
    {
      return new NavigationContext[paramAnonymousInt];
    }
  };
  private final Object mLock = new Object();
  private final Map<Pair<Double, Double>, Boolean> mShowNavigationMap = Maps.newHashMap();
  private final SparseIntArray mTravelModeMap = new SparseIntArray(2);
  
  public NavigationContext() {}
  
  private NavigationContext(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  private Pair<Double, Double> asPair(Sidekick.Location paramLocation)
  {
    return new Pair(Double.valueOf(paramLocation.getLat()), Double.valueOf(paramLocation.getLng()));
  }
  
  @Nullable
  public static NavigationContext fromRenderingContext(CardRenderingContext paramCardRenderingContext)
  {
    return (NavigationContext)paramCardRenderingContext.getSpecificRenderingContext(BUNDLE_KEY);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++)
    {
      Double localDouble1 = Double.valueOf(paramParcel.readDouble());
      Double localDouble2 = Double.valueOf(paramParcel.readDouble());
      Boolean localBoolean = (Boolean)paramParcel.readValue(null);
      this.mShowNavigationMap.put(new Pair(localDouble1, localDouble2), localBoolean);
    }
    int k = paramParcel.readInt();
    for (int m = 0; m < k; m++)
    {
      int n = paramParcel.readInt();
      int i1 = paramParcel.readInt();
      this.mTravelModeMap.append(n, i1);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @Nullable
  public Integer getTravelModePreference(int paramInt)
  {
    synchronized (this.mLock)
    {
      Integer localInteger = Integer.valueOf(this.mTravelModeMap.get(paramInt));
      return localInteger;
    }
  }
  
  public boolean haveCheckedNavigationTo(Sidekick.Location paramLocation)
  {
    synchronized (this.mLock)
    {
      boolean bool = this.mShowNavigationMap.containsKey(asPair(paramLocation));
      return bool;
    }
  }
  
  public void setShowNavigation(Sidekick.Location paramLocation, boolean paramBoolean)
  {
    synchronized (this.mLock)
    {
      this.mShowNavigationMap.put(asPair(paramLocation), Boolean.valueOf(paramBoolean));
      return;
    }
  }
  
  public void setTravelModePreference(int paramInt1, int paramInt2)
  {
    boolean bool1 = true;
    boolean bool2;
    if (paramInt1 != -1)
    {
      bool2 = bool1;
      Preconditions.checkArgument(bool2);
      if (paramInt2 == -1) {
        break label60;
      }
    }
    for (;;)
    {
      Preconditions.checkArgument(bool1);
      synchronized (this.mLock)
      {
        this.mTravelModeMap.append(paramInt1, paramInt2);
        return;
      }
      bool2 = false;
      break;
      label60:
      bool1 = false;
    }
  }
  
  public boolean shouldShowNavigation(Sidekick.Location paramLocation)
  {
    synchronized (this.mLock)
    {
      Boolean localBoolean = (Boolean)this.mShowNavigationMap.get(asPair(paramLocation));
      if (localBoolean == null)
      {
        bool = false;
        return bool;
      }
      boolean bool = localBoolean.booleanValue();
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    synchronized (this.mLock)
    {
      paramParcel.writeInt(this.mShowNavigationMap.size());
      Iterator localIterator = this.mShowNavigationMap.entrySet().iterator();
      if (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramParcel.writeDouble(((Double)((Pair)localEntry.getKey()).first).doubleValue());
        paramParcel.writeDouble(((Double)((Pair)localEntry.getKey()).second).doubleValue());
        paramParcel.writeValue(localEntry.getValue());
      }
    }
    paramParcel.writeInt(this.mTravelModeMap.size());
    for (int i = 0; i < this.mTravelModeMap.size(); i++)
    {
      paramParcel.writeInt(this.mTravelModeMap.keyAt(i));
      paramParcel.writeInt(this.mTravelModeMap.valueAt(i));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.renderingcontext.NavigationContext
 * JD-Core Version:    0.7.0.1
 */