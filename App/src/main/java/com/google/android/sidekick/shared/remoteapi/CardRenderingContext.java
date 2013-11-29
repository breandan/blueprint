package com.google.android.sidekick.shared.remoteapi;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import javax.annotation.Nullable;

public class CardRenderingContext
  implements Parcelable
{
  public static final Parcelable.Creator<CardRenderingContext> CREATOR = new Parcelable.Creator()
  {
    public CardRenderingContext createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CardRenderingContext(paramAnonymousParcel, null);
    }
    
    public CardRenderingContext[] newArray(int paramAnonymousInt)
    {
      return new CardRenderingContext[paramAnonymousInt];
    }
  };
  public static final CardRenderingContext EMPTY_CARD_RENDERING_CONTEXT = new CardRenderingContext()
  {
    @Nullable
    public <T extends Parcelable> T putSpecificRenderingContextIfAbsent(String paramAnonymousString, T paramAnonymousT)
    {
      throw new UnsupportedOperationException("EMPTY_CARD_RENDERING_CONTEXT is immutable");
    }
  };
  private Location mCurrentLocation;
  private final Object mLock = new Object();
  private Location mRefreshLocation;
  private final Bundle mRenderingContexts;
  
  public CardRenderingContext()
  {
    this.mCurrentLocation = null;
    this.mRefreshLocation = null;
    this.mRenderingContexts = new Bundle();
  }
  
  public CardRenderingContext(@Nullable Location paramLocation1, @Nullable Location paramLocation2)
  {
    this.mCurrentLocation = paramLocation1;
    this.mRefreshLocation = paramLocation2;
    this.mRenderingContexts = new Bundle();
  }
  
  private CardRenderingContext(Parcel paramParcel)
  {
    this.mRenderingContexts = paramParcel.readBundle(CardRenderingContext.class.getClassLoader());
    this.mCurrentLocation = ((Location)paramParcel.readParcelable(Location.class.getClassLoader()));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @Nullable
  public Location getCurrentLocation()
  {
    synchronized (this.mLock)
    {
      Location localLocation = this.mCurrentLocation;
      return localLocation;
    }
  }
  
  @Nullable
  public Location getRefreshLocation()
  {
    synchronized (this.mLock)
    {
      Location localLocation = this.mRefreshLocation;
      return localLocation;
    }
  }
  
  @Nullable
  public <T extends Parcelable> T getSpecificRenderingContext(String paramString)
  {
    synchronized (this.mLock)
    {
      Parcelable localParcelable = this.mRenderingContexts.getParcelable(paramString);
      return localParcelable;
    }
  }
  
  @Nullable
  public <T extends Parcelable> T putSpecificRenderingContextIfAbsent(String paramString, T paramT)
  {
    synchronized (this.mLock)
    {
      if (!this.mRenderingContexts.containsKey(paramString))
      {
        this.mRenderingContexts.putParcelable(paramString, paramT);
        return paramT;
      }
      Parcelable localParcelable = this.mRenderingContexts.getParcelable(paramString);
      return localParcelable;
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    synchronized (this.mLock)
    {
      paramParcel.writeBundle(this.mRenderingContexts);
      paramParcel.writeParcelable(this.mCurrentLocation, 0);
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.remoteapi.CardRenderingContext
 * JD-Core Version:    0.7.0.1
 */