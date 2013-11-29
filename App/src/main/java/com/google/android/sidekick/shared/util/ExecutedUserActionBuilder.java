package com.google.android.sidekick.shared.util;

import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.PlaceData;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.TimeZone;

public class ExecutedUserActionBuilder
{
  private final Sidekick.Action mAction;
  private int mCardHeight = -1;
  private Sidekick.ClickAction mClickAction = null;
  private Sidekick.PlaceData mCustomPlace = null;
  private final Sidekick.Entry mEntry;
  private long mExecutionTimeMs = -1L;
  private Boolean mPortrait = null;
  private final long mTimestampMs;
  
  public ExecutedUserActionBuilder(Sidekick.Entry paramEntry, Sidekick.Action paramAction, long paramLong)
  {
    this.mEntry = paramEntry;
    this.mAction = paramAction;
    this.mTimestampMs = paramLong;
  }
  
  static Sidekick.Entry stripCommuteSummayWaypoints(Sidekick.Entry paramEntry)
  {
    if ((!paramEntry.hasFrequentPlaceEntry()) || (paramEntry.getFrequentPlaceEntry().getRouteCount() == 0)) {
      return paramEntry;
    }
    try
    {
      Sidekick.Entry localEntry = Sidekick.Entry.parseFrom(paramEntry.toByteArray());
      Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = localEntry.getFrequentPlaceEntry();
      for (int i = 0; i < localFrequentPlaceEntry.getRouteCount(); i++) {
        localFrequentPlaceEntry.getRoute(i).clearWaypoints();
      }
      return localEntry;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {}
    return paramEntry;
  }
  
  public Sidekick.ExecutedUserAction build()
  {
    Sidekick.ExecutedUserAction localExecutedUserAction = new Sidekick.ExecutedUserAction().setAction(this.mAction).setEntry(stripCommuteSummayWaypoints(this.mEntry)).setTimestampSeconds(this.mTimestampMs / 1000L).setTimezoneOffsetSeconds((int)(TimeZone.getDefault().getOffset(this.mTimestampMs) / 1000L));
    if (this.mExecutionTimeMs > 0L) {
      localExecutedUserAction.setExecutionTimeMs(this.mExecutionTimeMs);
    }
    if (this.mCardHeight > 0) {
      localExecutedUserAction.setCardHeightPixels(this.mCardHeight);
    }
    if (this.mPortrait != null) {
      localExecutedUserAction.setIsPortrait(this.mPortrait.booleanValue());
    }
    if (this.mCustomPlace != null) {
      localExecutedUserAction.setCustomPlace(this.mCustomPlace);
    }
    if (this.mClickAction != null) {
      localExecutedUserAction.setClickTarget(this.mClickAction);
    }
    return localExecutedUserAction;
  }
  
  public ExecutedUserActionBuilder withCardHeight(int paramInt)
  {
    this.mCardHeight = paramInt;
    return this;
  }
  
  public ExecutedUserActionBuilder withClickAction(Sidekick.ClickAction paramClickAction)
  {
    this.mClickAction = paramClickAction;
    return this;
  }
  
  public ExecutedUserActionBuilder withCustomPlace(Sidekick.PlaceData paramPlaceData)
  {
    this.mCustomPlace = paramPlaceData;
    return this;
  }
  
  public ExecutedUserActionBuilder withExecutionTimeMs(long paramLong)
  {
    this.mExecutionTimeMs = paramLong;
    return this;
  }
  
  public ExecutedUserActionBuilder withPortrait(boolean paramBoolean)
  {
    this.mPortrait = Boolean.valueOf(paramBoolean);
    return this;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.ExecutedUserActionBuilder
 * JD-Core Version:    0.7.0.1
 */