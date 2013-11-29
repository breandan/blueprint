package com.google.android.sidekick.shared.renderingcontext;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;

public class SharedTrafficContext
  implements Parcelable
{
  public static final String BUNDLE_KEY = SharedTrafficContext.class.getName();
  public static final Parcelable.Creator<SharedTrafficContext> CREATOR = new Parcelable.Creator()
  {
    public SharedTrafficContext createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SharedTrafficContext(paramAnonymousParcel, null);
    }
    
    public SharedTrafficContext[] newArray(int paramAnonymousInt)
    {
      return new SharedTrafficContext[paramAnonymousInt];
    }
  };
  private final Map<String, Boolean> mHiddenSharersMap = Maps.newHashMap();
  private final Object mLock = new Object();
  
  public SharedTrafficContext() {}
  
  private SharedTrafficContext(Parcel paramParcel) {}
  
  @Nullable
  public static SharedTrafficContext fromCardContainer(PredictiveCardContainer paramPredictiveCardContainer)
  {
    return (SharedTrafficContext)paramPredictiveCardContainer.getCardRenderingContext().getSpecificRenderingContext(BUNDLE_KEY);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean isHiddenSet(String paramString)
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        Boolean localBoolean = (Boolean)this.mHiddenSharersMap.get(paramString);
        if (localBoolean != null)
        {
          bool = localBoolean.booleanValue();
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  public void setIsHidden(String paramString, boolean paramBoolean)
  {
    synchronized (this.mLock)
    {
      this.mHiddenSharersMap.put(paramString, Boolean.valueOf(paramBoolean));
      return;
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.renderingcontext.SharedTrafficContext
 * JD-Core Version:    0.7.0.1
 */