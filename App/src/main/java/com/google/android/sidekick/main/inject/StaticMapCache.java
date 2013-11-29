package com.google.android.sidekick.main.inject;

import android.graphics.Bitmap;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;

public abstract interface StaticMapCache
{
  public abstract Bitmap get(Sidekick.Location paramLocation, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, boolean paramBoolean);
  
  public abstract Bitmap getSampleMap();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.StaticMapCache
 * JD-Core Version:    0.7.0.1
 */