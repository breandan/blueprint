package com.google.android.search.gel;

import android.view.View;
import com.google.android.search.shared.api.ExternalGelSearch;

public abstract interface SearchOverlay
{
  public abstract void clearSearchPlate();
  
  public abstract View getContainer();
  
  public abstract ExternalGelSearch getGelSearch();
  
  public abstract void onDestroy();
  
  public abstract void onDoodleChanged(boolean paramBoolean);
  
  public abstract void onNowScroll(int paramInt);
  
  public abstract void onResume();
  
  public abstract void onWindowFocusChanged(boolean paramBoolean);
  
  public abstract void setHotwordDetectionEnabled(boolean paramBoolean);
  
  public abstract void setListener(Listener paramListener);
  
  public abstract void setProximityToNow(float paramFloat);
  
  public abstract void startTextSearch(boolean paramBoolean, String paramString);
  
  public abstract void startVoiceSearch(String paramString);
  
  public abstract boolean stopSearch(boolean paramBoolean);
  
  public static abstract interface Listener
  {
    public abstract void onSearchPlateShown(boolean paramBoolean);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.SearchOverlay
 * JD-Core Version:    0.7.0.1
 */