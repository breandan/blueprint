package com.google.android.speech.network.request;

import android.text.TextUtils;
import com.google.android.speech.params.DeviceParams;
import com.google.android.speech.utils.NetworkInformation;
import com.google.audio.ears.proto.EarsService.EarsLookupRequest;
import com.google.audio.ears.proto.EarsService.EarsStreamRequest;
import com.google.speech.speech.s3.SoundSearch.SoundSearchInfo;
import javax.annotation.Nonnull;

public class SoundSearchInfoBuilderTask
  extends BaseRequestBuilderTask<SoundSearch.SoundSearchInfo>
{
  private final DeviceParams mDeviceParams;
  private final NetworkInformation mNetworkInformation;
  private final int mRequestType;
  private final boolean mTtsEnabled;
  
  public SoundSearchInfoBuilderTask(boolean paramBoolean, NetworkInformation paramNetworkInformation, @Nonnull DeviceParams paramDeviceParams, int paramInt)
  {
    super("SoundSearchInfoBuilderTask");
    this.mTtsEnabled = paramBoolean;
    this.mNetworkInformation = paramNetworkInformation;
    this.mDeviceParams = paramDeviceParams;
    this.mRequestType = paramInt;
  }
  
  protected SoundSearch.SoundSearchInfo build()
  {
    EarsService.EarsLookupRequest localEarsLookupRequest = new EarsService.EarsLookupRequest();
    String str1 = this.mNetworkInformation.getDeviceCountryCode();
    if (!str1.isEmpty())
    {
      localEarsLookupRequest.setClientCountryCode(str1);
      if (this.mRequestType != 0) {
        break label132;
      }
    }
    label132:
    for (int i = 0;; i = 1)
    {
      localEarsLookupRequest.addDesiredResultType(i);
      EarsService.EarsStreamRequest localEarsStreamRequest = new EarsService.EarsStreamRequest();
      localEarsStreamRequest.setAudioContainer(4);
      localEarsStreamRequest.setAudioEncoding(4);
      SoundSearch.SoundSearchInfo localSoundSearchInfo = new SoundSearch.SoundSearchInfo();
      localSoundSearchInfo.setLookupRequest(localEarsLookupRequest);
      localSoundSearchInfo.setStreamRequest(localEarsStreamRequest);
      localSoundSearchInfo.setTtsOutputEnabled(this.mTtsEnabled);
      return localSoundSearchInfo;
      String str2 = this.mDeviceParams.getSearchDomainCountryCode();
      if (TextUtils.isEmpty(str2)) {
        break;
      }
      localEarsLookupRequest.setClientCountryCode(str2);
      break;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.request.SoundSearchInfoBuilderTask
 * JD-Core Version:    0.7.0.1
 */