package com.google.android.sidekick.main;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import com.google.android.gms.location.reporting.ReportingState;
import com.google.android.gms.location.reporting.ReportingState.Setting;
import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.search.core.GmsClientWrapper.GmsFuture;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.main.inject.WidgetManager;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.common.base.Optional;
import com.google.geo.sidekick.Sidekick.LayoutInfo;
import com.google.geo.sidekick.Sidekick.SensorSignals;
import com.google.geo.sidekick.Sidekick.TimestampedLocation;
import com.google.protobuf.micro.ByteStringMicro;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class SensorSignalsOracle
{
  private byte[] mBeaconData = null;
  private final Clock mClock;
  private final Context mContext;
  private Optional<Long> mDebugUserTimeMillis = Optional.absent();
  private final DeviceCapabilityManager mDeviceCapabilityManager;
  private final GmsLocationReportingHelper mGmsLocationReportingHelper;
  private final LocationOracle mLocationOracle;
  private final WidgetManager mWidgetManager;
  private final WifiManager mWifiManager;
  
  public SensorSignalsOracle(Context paramContext, Clock paramClock, LocationOracle paramLocationOracle, DeviceCapabilityManager paramDeviceCapabilityManager, WidgetManager paramWidgetManager, GmsLocationReportingHelper paramGmsLocationReportingHelper, WifiManager paramWifiManager)
  {
    this.mContext = paramContext;
    this.mClock = paramClock;
    this.mLocationOracle = paramLocationOracle;
    this.mDeviceCapabilityManager = paramDeviceCapabilityManager;
    this.mWidgetManager = paramWidgetManager;
    this.mGmsLocationReportingHelper = paramGmsLocationReportingHelper;
    this.mWifiManager = paramWifiManager;
  }
  
  private long getUserTimeMillis()
  {
    return this.mClock.currentTimeMillis();
  }
  
  private boolean isWifiEnabled()
  {
    int i = this.mWifiManager.getWifiState();
    return (i == 3) || (i == 2);
  }
  
  public Sidekick.SensorSignals buildCurrentSensorSignals(@Nullable Sidekick.SensorSignals paramSensorSignals, boolean paramBoolean)
  {
    
    Sidekick.SensorSignals localSensorSignals;
    GmsClientWrapper.GmsFuture localGmsFuture;
    if (paramSensorSignals == null)
    {
      localSensorSignals = new Sidekick.SensorSignals();
      localGmsFuture = this.mGmsLocationReportingHelper.getCurrentAccountReportingState();
      String str1 = Locale.getDefault().toString();
      localSensorSignals.setLocale(str1);
      long l1 = getUserTimeMillis();
      long l2 = l1 / 1000L;
      localSensorSignals.setTimestampSeconds(l2);
      int i = TimeZone.getDefault().getOffset(l1) / 1000;
      localSensorSignals.setTimezoneOffsetSeconds(i);
      String str2 = TimeZone.getDefault().getID();
      if (str2 != null) {
        localSensorSignals.setTimezoneId(str2);
      }
      if (!DateFormat.is24HourFormat(this.mContext)) {
        break label195;
      }
    }
    label195:
    for (int j = 2;; j = 1)
    {
      localSensorSignals.setTimeFormat(j);
      List localList = this.mLocationOracle.getBestLocations();
      if ((!paramBoolean) || (localList.isEmpty())) {
        break label201;
      }
      Iterator localIterator = LocationUtilities.locationsToTimestampedLocations(localList).iterator();
      while (localIterator.hasNext())
      {
        Sidekick.TimestampedLocation localTimestampedLocation = (Sidekick.TimestampedLocation)localIterator.next();
        localSensorSignals.addTimestampedLocation(localTimestampedLocation);
      }
      localSensorSignals = paramSensorSignals;
      break;
    }
    label201:
    boolean bool1 = isWifiEnabled();
    localSensorSignals.setWifiEnabled(bool1);
    if (Build.VERSION.SDK_INT >= 18)
    {
      boolean bool3 = this.mWifiManager.isScanAlwaysAvailable();
      localSensorSignals.setWifiScanAlwaysAvailable(bool3);
    }
    localSensorSignals.setHasRearFacingCamera(false);
    int k = this.mWidgetManager.getWidgetInstallCount();
    localSensorSignals.setNumberWidgetsInstalled(k);
    Resources localResources = this.mContext.getResources();
    DisplayMetrics localDisplayMetrics = localResources.getDisplayMetrics();
    int m = Math.max(localDisplayMetrics.heightPixels, localDisplayMetrics.widthPixels);
    int n = Math.min(localDisplayMetrics.heightPixels, localDisplayMetrics.widthPixels);
    int i1 = LayoutUtils.getContentPadding(this.mContext, localDisplayMetrics.widthPixels);
    int i2 = localDisplayMetrics.widthPixels - i1 * 2;
    int i3 = localResources.getInteger(2131427457);
    if (i3 > 1) {
      i2 = (i2 - localResources.getDimensionPixelSize(2131689594) * (i3 - 1)) / i3;
    }
    Sidekick.LayoutInfo localLayoutInfo = new Sidekick.LayoutInfo().setScreenPixelsLongestEdge(m).setScreenPixelsShortestEdge(n).setScreenDensity(localDisplayMetrics.density).setCardWidthPixelsLandscape(i2).setCardWidthPixelsPortrait(i2);
    localSensorSignals.setLayoutInfo(localLayoutInfo);
    if (this.mBeaconData != null)
    {
      ByteStringMicro localByteStringMicro = ByteStringMicro.copyFrom(this.mBeaconData);
      localSensorSignals.setNearbySpotsBeaconData(localByteStringMicro);
    }
    ReportingState localReportingState = (ReportingState)localGmsFuture.safeGet(1000L, TimeUnit.MILLISECONDS);
    if ((localReportingState != null) && (!localReportingState.isDeferringToMaps()))
    {
      boolean bool2 = ReportingState.Setting.isOn(localReportingState.getReportingEnabled());
      localSensorSignals.setUserLocationReportingEnabled(bool2);
    }
    return localSensorSignals;
  }
  
  void clearUserTimeMillis()
  {
    this.mDebugUserTimeMillis = Optional.absent();
  }
  
  void setBeacondData(byte[] paramArrayOfByte)
  {
    this.mBeaconData = paramArrayOfByte;
  }
  
  void setUserTimeMillis(long paramLong)
  {
    this.mDebugUserTimeMillis = Optional.of(Long.valueOf(paramLong));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.SensorSignalsOracle
 * JD-Core Version:    0.7.0.1
 */