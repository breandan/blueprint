package com.google.android.sidekick.shared.renderingcontext;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.common.collect.Maps;
import com.google.protobuf.micro.MessageMicro;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

public class CalendarDataContext
  implements Parcelable
{
  public static final String BUNDLE_KEY = CalendarDataContext.class.getName();
  public static final Parcelable.Creator<CalendarDataContext> CREATOR = new Parcelable.Creator()
  {
    public CalendarDataContext createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CalendarDataContext(paramAnonymousParcel, null);
    }
    
    public CalendarDataContext[] newArray(int paramAnonymousInt)
    {
      return new CalendarDataContext[paramAnonymousInt];
    }
  };
  private final Map<String, Calendar.CalendarData> mCalendarDataMap = Maps.newHashMap();
  private final Object mLock = new Object();
  
  public CalendarDataContext() {}
  
  private CalendarDataContext(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  @Nullable
  public static CalendarDataContext fromCardContainer(PredictiveCardContainer paramPredictiveCardContainer)
  {
    return (CalendarDataContext)paramPredictiveCardContainer.getCardRenderingContext().getSpecificRenderingContext(BUNDLE_KEY);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++)
    {
      String str = paramParcel.readString();
      Calendar.CalendarData localCalendarData = (Calendar.CalendarData)ProtoParcelable.readProtoFromParcel(paramParcel, Calendar.CalendarData.class);
      this.mCalendarDataMap.put(str, localCalendarData);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Calendar.CalendarData getCalendarData(String paramString)
  {
    synchronized (this.mLock)
    {
      Calendar.CalendarData localCalendarData = (Calendar.CalendarData)this.mCalendarDataMap.get(paramString);
      return localCalendarData;
    }
  }
  
  public void setCalendarData(String paramString, Calendar.CalendarData paramCalendarData)
  {
    synchronized (this.mLock)
    {
      this.mCalendarDataMap.put(paramString, paramCalendarData);
      return;
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    synchronized (this.mLock)
    {
      paramParcel.writeInt(this.mCalendarDataMap.size());
      Iterator localIterator = this.mCalendarDataMap.entrySet().iterator();
      if (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramParcel.writeString((String)localEntry.getKey());
        ProtoParcelable.writeProtoToParcel((MessageMicro)localEntry.getValue(), paramParcel);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.renderingcontext.CalendarDataContext
 * JD-Core Version:    0.7.0.1
 */