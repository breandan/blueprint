package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReadNotificationCard
  extends AbstractCardView<ReadNotificationController>
{
  public ReadNotificationCard(Context paramContext)
  {
    super(paramContext);
  }
  
  public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = new View(paramContext);
    localView.setVisibility(8);
    return localView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.ReadNotificationCard
 * JD-Core Version:    0.7.0.1
 */