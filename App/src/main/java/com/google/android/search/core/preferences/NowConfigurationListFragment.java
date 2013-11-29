package com.google.android.search.core.preferences;

import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NowConfigurationListFragment
  extends ListFragment
{
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    ListView localListView = getListView();
    int i = (int)getResources().getDimension(2131689839);
    localListView.setPadding(i, 0, i, 0);
    localListView.setClipToPadding(false);
    localListView.setScrollBarStyle(33554432);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(2130968764, paramViewGroup, false);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.NowConfigurationListFragment
 * JD-Core Version:    0.7.0.1
 */