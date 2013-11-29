package com.google.android.sidekick.shared.remoteapi;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.common.collect.Maps;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public class LoggingRequest
  implements Parcelable
{
  public static final Parcelable.Creator<LoggingRequest> CREATOR = new Parcelable.Creator()
  {
    public LoggingRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new LoggingRequest(paramAnonymousParcel);
    }
    
    public LoggingRequest[] newArray(int paramAnonymousInt)
    {
      return new LoggingRequest[paramAnonymousInt];
    }
  };
  public String mAnalyticsActionType;
  public Sidekick.ClickAction mClickAction;
  public Sidekick.Entry mEntry;
  public int mEntryActionType;
  public String mLabel;
  public Map<String, Integer> mLabelCountMap;
  public int mType;
  
  private LoggingRequest() {}
  
  public LoggingRequest(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public static LoggingRequest forAnalyticsAction(String paramString1, String paramString2)
  {
    LoggingRequest localLoggingRequest = new LoggingRequest();
    localLoggingRequest.mType = 2;
    localLoggingRequest.mAnalyticsActionType = paramString1;
    localLoggingRequest.mLabel = paramString2;
    return localLoggingRequest;
  }
  
  public static LoggingRequest forMetricsAction(Sidekick.Entry paramEntry, int paramInt, @Nullable Sidekick.ClickAction paramClickAction)
  {
    LoggingRequest localLoggingRequest = new LoggingRequest();
    localLoggingRequest.mType = 1;
    localLoggingRequest.mEntry = paramEntry;
    localLoggingRequest.mEntryActionType = paramInt;
    localLoggingRequest.mClickAction = paramClickAction;
    return localLoggingRequest;
  }
  
  public static LoggingRequest forStackRender(String paramString, Map<String, Integer> paramMap)
  {
    LoggingRequest localLoggingRequest = new LoggingRequest();
    localLoggingRequest.mType = 4;
    localLoggingRequest.mAnalyticsActionType = paramString;
    localLoggingRequest.mLabelCountMap = paramMap;
    return localLoggingRequest;
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    this.mType = paramParcel.readInt();
    this.mEntry = ((Sidekick.Entry)ProtoParcelable.readProtoFromParcel(paramParcel, Sidekick.Entry.class));
    this.mClickAction = ((Sidekick.ClickAction)ProtoParcelable.readProtoFromParcel(paramParcel, Sidekick.ClickAction.class));
    this.mAnalyticsActionType = paramParcel.readString();
    this.mLabel = paramParcel.readString();
    this.mEntryActionType = paramParcel.readInt();
    int i = paramParcel.readInt();
    if (i > 0)
    {
      this.mLabelCountMap = Maps.newHashMapWithExpectedSize(i);
      Bundle localBundle = paramParcel.readBundle();
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        this.mLabelCountMap.put(str, Integer.valueOf(localBundle.getInt(str)));
      }
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mType);
    ProtoParcelable.writeProtoToParcel(this.mEntry, paramParcel);
    ProtoParcelable.writeProtoToParcel(this.mClickAction, paramParcel);
    paramParcel.writeString(this.mAnalyticsActionType);
    paramParcel.writeString(this.mLabel);
    paramParcel.writeInt(this.mEntryActionType);
    if ((this.mLabelCountMap == null) || (this.mLabelCountMap.isEmpty()))
    {
      paramParcel.writeInt(0);
      return;
    }
    paramParcel.writeInt(this.mLabelCountMap.size());
    Bundle localBundle = new Bundle();
    Iterator localIterator = this.mLabelCountMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localBundle.putInt(str, ((Integer)this.mLabelCountMap.get(str)).intValue());
    }
    paramParcel.writeBundle(localBundle);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.remoteapi.LoggingRequest
 * JD-Core Version:    0.7.0.1
 */