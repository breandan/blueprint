package com.google.android.speech.network.request;

import android.os.Build;
import android.util.DisplayMetrics;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.params.DeviceParams;
import com.google.speech.s3.S3.S3ClientInfo;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class S3ClientInfoBuilderTask
  extends BaseRequestBuilderTask<S3.S3ClientInfo>
{
  private final String mAppId;
  private final DeviceParams mDeviceParams;
  private final SpeechSettings mSpeechSettings;
  @Nullable
  private final String mTriggerApplicationId;
  
  public S3ClientInfoBuilderTask(@Nonnull SpeechSettings paramSpeechSettings, @Nonnull String paramString1, @Nonnull DeviceParams paramDeviceParams, @Nullable String paramString2)
  {
    super("S3ClientInfoBuilderTask");
    this.mSpeechSettings = paramSpeechSettings;
    this.mAppId = paramString1;
    this.mDeviceParams = paramDeviceParams;
    this.mTriggerApplicationId = paramString2;
  }
  
  protected S3.S3ClientInfo build()
  {
    S3.S3ClientInfo localS3ClientInfo = new S3.S3ClientInfo().setClientId("").setPlatformId("Android").setPlatformVersion(Build.DISPLAY).setApplicationId(this.mAppId).setApplicationVersion(this.mDeviceParams.getApplicationVersion()).setUserAgent(this.mDeviceParams.getUserAgent()).setDeviceModel(Build.MODEL);
    Iterator localIterator = this.mSpeechSettings.getExperimentIds().iterator();
    while (localIterator.hasNext()) {
      localS3ClientInfo.addExperimentId((String)localIterator.next());
    }
    DisplayMetrics localDisplayMetrics = this.mDeviceParams.getDisplayMetrics();
    if (localDisplayMetrics != null) {
      localS3ClientInfo.setDeviceDisplayWidthPixels(localDisplayMetrics.widthPixels).setDeviceDisplayHeightPixels(localDisplayMetrics.heightPixels).setDeviceDisplayDensityDpi(localDisplayMetrics.densityDpi);
    }
    if (this.mTriggerApplicationId != null) {
      localS3ClientInfo.setTriggerApplicationId(this.mTriggerApplicationId);
    }
    return localS3ClientInfo;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.request.S3ClientInfoBuilderTask
 * JD-Core Version:    0.7.0.1
 */