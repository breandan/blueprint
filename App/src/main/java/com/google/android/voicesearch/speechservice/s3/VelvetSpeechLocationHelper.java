package com.google.android.voicesearch.speechservice.s3;

import android.location.Location;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.XGeoEncoder;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.speech.helper.SpeechLocationHelper;

public class VelvetSpeechLocationHelper
  implements SpeechLocationHelper
{
  private final LocationOracle mOracle;
  private final LocationSettings mSettings;
  
  public VelvetSpeechLocationHelper(LocationSettings paramLocationSettings, LocationOracle paramLocationOracle)
  {
    this.mSettings = paramLocationSettings;
    this.mOracle = paramLocationOracle;
  }
  
  public String getXGeoLocation()
  {
    boolean bool = this.mSettings.canUseLocationForSearch();
    String str = null;
    if (bool)
    {
      Location localLocation = this.mOracle.getBestLocation();
      str = null;
      if (localLocation != null) {
        str = XGeoEncoder.createHeader(false, localLocation, null);
      }
    }
    return str;
  }
  
  public boolean shouldSendLocation()
  {
    return this.mSettings.canUseLocationForSearch();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.speechservice.s3.VelvetSpeechLocationHelper
 * JD-Core Version:    0.7.0.1
 */