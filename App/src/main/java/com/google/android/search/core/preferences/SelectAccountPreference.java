package com.google.android.search.core.preferences;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.Toast;
import com.google.android.sidekick.main.inject.NetworkClient;

public class SelectAccountPreference
  extends ListPreference
{
  private final Context mContext;
  NetworkClient mNetworkClient;
  
  public SelectAccountPreference(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
  }
  
  public SelectAccountPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }
  
  void setNetworkClient(NetworkClient paramNetworkClient)
  {
    this.mNetworkClient = paramNetworkClient;
  }
  
  protected void showDialog(Bundle paramBundle)
  {
    if (!this.mNetworkClient.isNetworkAvailable())
    {
      Toast.makeText(this.mContext, 2131362182, 0).show();
      return;
    }
    super.showDialog(paramBundle);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.SelectAccountPreference
 * JD-Core Version:    0.7.0.1
 */