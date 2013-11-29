package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.LocalResultUtils;
import com.google.android.voicesearch.fragments.action.LocalResultsAction;
import com.google.android.voicesearch.util.MapUtil;
import com.google.common.base.Preconditions;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResults;
import java.util.List;
import javax.annotation.Nullable;

public class LocalResultsActionExecutor
  extends IntentActionExecutor<LocalResultsAction>
{
  public LocalResultsActionExecutor(IntentStarter paramIntentStarter)
  {
    super(paramIntentStarter);
  }
  
  protected Intent[] getExecuteIntents(LocalResultsAction paramLocalResultsAction)
  {
    int i = 1;
    EcoutezStructuredResponse.EcoutezLocalResults localEcoutezLocalResults = paramLocalResultsAction.getResults();
    if (localEcoutezLocalResults.getLocalResultCount() == i)
    {
      Preconditions.checkState(i);
      if (localEcoutezLocalResults.getOriginCount() <= 0) {
        break label57;
      }
    }
    label57:
    for (EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult = localEcoutezLocalResults.getLocalResult(0);; localEcoutezLocalResult = null)
    {
      return LocalResultUtils.createIntentsForAction(localEcoutezLocalResults.getActionType(), localEcoutezLocalResult, localEcoutezLocalResults.getLocalResult(0), paramLocalResultsAction.getTransportationMethod());
      int j = 0;
      break;
    }
  }
  
  protected Intent[] getOpenExternalAppIntents(LocalResultsAction paramLocalResultsAction)
  {
    return null;
  }
  
  @Nullable
  protected Intent[] getProberIntents(LocalResultsAction paramLocalResultsAction)
  {
    EcoutezStructuredResponse.EcoutezLocalResults localEcoutezLocalResults = paramLocalResultsAction.getResults();
    List localList = LocalResultUtils.createProbeIntentsForAction(localEcoutezLocalResults.getActionType(), localEcoutezLocalResults);
    return (Intent[])localList.toArray(new Intent[localList.size()]);
  }
  
  public void openMaps(LocalResultsAction paramLocalResultsAction)
  {
    this.mIntentStarter.startActivity(MapUtil.getMapsIntents(paramLocalResultsAction.getResults().getMapsUrl()));
  }
  
  public void openResult(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
  {
    this.mIntentStarter.startActivity(MapUtil.getMapsIntents(paramEcoutezLocalResult.getMapsUrl()));
  }
  
  public void performAction(LocalResultsAction paramLocalResultsAction, EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
  {
    EcoutezStructuredResponse.EcoutezLocalResults localEcoutezLocalResults = paramLocalResultsAction.getResults();
    if (localEcoutezLocalResults.getOriginCount() != 0) {}
    for (EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult = localEcoutezLocalResults.getOrigin(0);; localEcoutezLocalResult = null)
    {
      this.mIntentStarter.startActivity(LocalResultUtils.createIntentsForAction(localEcoutezLocalResults.getActionType(), localEcoutezLocalResult, paramEcoutezLocalResult, paramLocalResultsAction.getTransportationMethod()));
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.LocalResultsActionExecutor
 * JD-Core Version:    0.7.0.1
 */