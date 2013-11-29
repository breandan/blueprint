package com.google.android.search.core;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import com.google.android.sidekick.shared.util.Tag;
import java.util.Iterator;
import java.util.List;

public class GmmPrecacher
{
  static final String LOCATIONS_KEY = "locations";
  private static final String[] RESULT_MESSAGES = { "Success", "Invalid request", "Package or certificate invalid", "Invalid locations" };
  private static final String TAG = Tag.getTag(GmmPrecacher.class);
  private PendingIntent mIdentifierIntent;
  
  public void precache(Context paramContext, List<Location> paramList)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 1;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Location localLocation = (Location)localIterator.next();
      if (i == 0) {
        localStringBuilder.append(",");
      }
      localStringBuilder.append(localLocation.getLatitude()).append(",").append(localLocation.getLongitude());
      i = 0;
    }
    String str = localStringBuilder.toString();
    Intent localIntent1 = new Intent();
    this.mIdentifierIntent = PendingIntent.getActivity(paramContext.getApplicationContext(), 0, localIntent1, 0);
    Intent localIntent2 = new Intent("com.google.android.apps.maps.PREFETCH");
    localIntent2.setPackage("com.google.android.apps.maps");
    localIntent2.setFlags(268435456);
    localIntent2.putExtra("messenger", new Messenger(new ServiceHandler(null)));
    localIntent2.putExtra("sender", this.mIdentifierIntent);
    localIntent2.putExtra("locations", str);
    try
    {
      paramContext.startService(localIntent2);
      return;
    }
    catch (SecurityException localSecurityException) {}
  }
  
  private class ServiceHandler
    extends Handler
  {
    private ServiceHandler() {}
    
    public void handleMessage(Message paramMessage)
    {
      if (GmmPrecacher.this.mIdentifierIntent != null) {
        GmmPrecacher.this.mIdentifierIntent.cancel();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GmmPrecacher
 * JD-Core Version:    0.7.0.1
 */