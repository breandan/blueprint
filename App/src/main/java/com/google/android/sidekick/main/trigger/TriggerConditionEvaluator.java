package com.google.android.sidekick.main.trigger;

import android.app.PendingIntent;
import android.location.Location;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.geo.sidekick.Sidekick.DiscUnion;
import com.google.geo.sidekick.Sidekick.DiscUnion.RadiusGroup;
import com.google.geo.sidekick.Sidekick.TriggerCondition;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class TriggerConditionEvaluator
{
  private final LocationOracle mLocationOracle;
  
  public TriggerConditionEvaluator(LocationOracle paramLocationOracle)
  {
    this.mLocationOracle = paramLocationOracle;
  }
  
  private boolean evaluateNow(Sidekick.TriggerCondition paramTriggerCondition)
  {
    Location localLocation = this.mLocationOracle.getBestLocation();
    return (evaluateRegion(paramTriggerCondition, localLocation)) || (evaluateLocation(paramTriggerCondition, localLocation)) || (evaluateTime(paramTriggerCondition, localLocation));
  }
  
  private boolean locationIn(Sidekick.DiscUnion.RadiusGroup paramRadiusGroup, Location paramLocation)
  {
    float f = paramRadiusGroup.getRadiusMeters() + paramLocation.getAccuracy();
    Iterator localIterator = paramRadiusGroup.getCentersLatLngE7List().iterator();
    while (localIterator.hasNext())
    {
      Location localLocation = new Location("");
      localLocation.setLatitude(1.0E-007D * ((Integer)localIterator.next()).intValue());
      localLocation.setLongitude(1.0E-007D * ((Integer)localIterator.next()).intValue());
      if (paramLocation.distanceTo(localLocation) < f) {
        return true;
      }
    }
    return false;
  }
  
  public boolean evaluate(Sidekick.TriggerCondition paramTriggerCondition, @Nullable PendingIntent paramPendingIntent)
  {
    return evaluateNow(paramTriggerCondition);
  }
  
  boolean evaluateLocation(Sidekick.TriggerCondition paramTriggerCondition, @Nullable Location paramLocation)
  {
    return false;
  }
  
  boolean evaluateRegion(Sidekick.TriggerCondition paramTriggerCondition, @Nullable Location paramLocation)
  {
    if ((paramLocation == null) || (!paramTriggerCondition.getConditionList().contains(Integer.valueOf(8)))) {
      break label45;
    }
    label21:
    List localList;
    do
    {
      return false;
      localList = paramTriggerCondition.getDiscRegionList();
    } while (localList.size() == 0);
    Iterator localIterator1 = localList.iterator();
    label45:
    Iterator localIterator2;
    label75:
    do
    {
      break label75;
      if (!localIterator1.hasNext()) {
        break label21;
      }
      localIterator2 = ((Sidekick.DiscUnion)localIterator1.next()).getRadiusGroupsList().iterator();
      if (!localIterator2.hasNext()) {
        break;
      }
    } while (!locationIn((Sidekick.DiscUnion.RadiusGroup)localIterator2.next(), paramLocation));
    return true;
  }
  
  boolean evaluateTime(Sidekick.TriggerCondition paramTriggerCondition, @Nullable Location paramLocation)
  {
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.trigger.TriggerConditionEvaluator
 * JD-Core Version:    0.7.0.1
 */