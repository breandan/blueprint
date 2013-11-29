package com.google.android.search.core.google;

import android.content.Context;
import com.google.android.gsf.GoogleSettingsContract.Partner;

public class PartnerInfo
{
  private final Context mContext;
  
  public PartnerInfo(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  public String getClientId()
  {
    return GoogleSettingsContract.Partner.getString(this.mContext.getContentResolver(), "client_id");
  }
  
  public String getSearchClientId()
  {
    return GoogleSettingsContract.Partner.getString(this.mContext.getContentResolver(), "search_client_id");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.PartnerInfo
 * JD-Core Version:    0.7.0.1
 */