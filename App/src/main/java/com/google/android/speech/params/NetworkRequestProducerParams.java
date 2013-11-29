package com.google.android.speech.params;

import com.google.android.speech.SpeechSettings;
import com.google.android.speech.helper.AuthTokenHelper;
import com.google.android.speech.helper.SpeechLocationHelper;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.voicesearch.speechservice.s3.PinholeParamsBuilder;
import com.google.common.base.Supplier;
import java.util.List;

public class NetworkRequestProducerParams
{
  private final AuthTokenHelper mAuthTokenHelper;
  private final DeviceParams mDeviceParams;
  private final SpeechLocationHelper mLocationHelper;
  private final NetworkInformation mNetworkInformation;
  private final PinholeParamsBuilder mPinholeParamsBuilder;
  private final SpeechSettings mSpeechSettings;
  private final Supplier<List<String>> mTopContactNamesSupplier;
  
  public NetworkRequestProducerParams(AuthTokenHelper paramAuthTokenHelper, NetworkInformation paramNetworkInformation, PinholeParamsBuilder paramPinholeParamsBuilder, SpeechLocationHelper paramSpeechLocationHelper, SpeechSettings paramSpeechSettings, DeviceParams paramDeviceParams, Supplier<List<String>> paramSupplier)
  {
    this.mAuthTokenHelper = paramAuthTokenHelper;
    this.mNetworkInformation = paramNetworkInformation;
    this.mPinholeParamsBuilder = paramPinholeParamsBuilder;
    this.mLocationHelper = paramSpeechLocationHelper;
    this.mSpeechSettings = paramSpeechSettings;
    this.mDeviceParams = paramDeviceParams;
    this.mTopContactNamesSupplier = paramSupplier;
  }
  
  public AuthTokenHelper getAuthTokenHelper()
  {
    return this.mAuthTokenHelper;
  }
  
  public DeviceParams getDeviceParams()
  {
    return this.mDeviceParams;
  }
  
  public SpeechLocationHelper getLocationHelper()
  {
    return this.mLocationHelper;
  }
  
  public NetworkInformation getNetworkInformation()
  {
    return this.mNetworkInformation;
  }
  
  public PinholeParamsBuilder getPinholeParamsBuilder()
  {
    return this.mPinholeParamsBuilder;
  }
  
  public SpeechSettings getSpeechSettings()
  {
    return this.mSpeechSettings;
  }
  
  public Supplier<List<String>> getTopContactNamesSupplier()
  {
    return this.mTopContactNamesSupplier;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.params.NetworkRequestProducerParams
 * JD-Core Version:    0.7.0.1
 */