package com.google.android.voicesearch.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.LocalResultsAction;
import com.google.android.voicesearch.fragments.executor.LocalResultsActionExecutor;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResults;
import com.google.protobuf.micro.ByteStringMicro;
import java.util.List;

public class LocalResultsController
  extends AbstractCardController<LocalResultsAction, Ui>
{
  public LocalResultsController(CardController paramCardController)
  {
    super(paramCardController);
  }
  
  public void initUi()
  {
    final LocalResultsAction localLocalResultsAction = (LocalResultsAction)getVoiceAction();
    EcoutezStructuredResponse.EcoutezLocalResults localEcoutezLocalResults = localLocalResultsAction.getResults();
    final int i = localEcoutezLocalResults.getActionType();
    boolean bool1;
    int j;
    int k;
    Ui localUi;
    int n;
    label157:
    int i1;
    label160:
    final EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult;
    View.OnClickListener local2;
    View.OnClickListener local3;
    if (localEcoutezLocalResults.getLocalResultCount() > 1)
    {
      bool1 = true;
      j = LocalResultUtils.getActionIconImageResource(i);
      k = LocalResultUtils.getActionLabelStringId(i);
      View.OnClickListener local1 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          EventLogger.recordClientEvent(50, Integer.valueOf(LocalResultsController.this.getActionTypeLog()));
          ((LocalResultsActionExecutor)LocalResultsController.this.getActionExecutor()).openMaps((LocalResultsAction)LocalResultsController.this.getVoiceAction());
        }
      };
      localUi = (Ui)getUi();
      boolean bool2 = localEcoutezLocalResults.hasPreviewImage();
      int m = 0;
      if (bool2)
      {
        byte[] arrayOfByte = localEcoutezLocalResults.getPreviewImage().toByteArray();
        Bitmap localBitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
        m = 0;
        if (localBitmap != null)
        {
          localUi.setMapImageBitmap(localBitmap, local1);
          m = 1;
        }
      }
      if (m == 0) {
        localUi.setMapImageUrl(localEcoutezLocalResults.getPreviewImageUrl(), local1);
      }
      localUi.setActionTypeAndTransportationMethod(i, localLocalResultsAction.getTransportationMethod());
      if (localEcoutezLocalResults.getActionType() != 4) {
        break label292;
      }
      n = 1;
      i1 = 0;
      int i2 = localEcoutezLocalResults.getLocalResultList().size();
      if (i1 >= i2) {
        break label305;
      }
      localEcoutezLocalResult = localEcoutezLocalResults.getLocalResult(i1);
      local2 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          EventLogger.recordClientEvent(112, Integer.valueOf(LocalResultsController.this.getActionTypeLog()));
          ((LocalResultsActionExecutor)LocalResultsController.this.getActionExecutor()).openResult(localEcoutezLocalResult);
        }
      };
      local3 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          EventLogger.recordClientEvent(LocalResultUtils.getEventTypeLog(i), Integer.valueOf(LocalResultsController.this.getActionTypeLog()));
          ((LocalResultsActionExecutor)LocalResultsController.this.getActionExecutor()).performAction(localLocalResultsAction, localEcoutezLocalResult);
        }
      };
      if (n == 0) {
        break label298;
      }
    }
    label292:
    label298:
    for (String str = localEcoutezLocalResult.getPhoneNumber();; str = "")
    {
      localUi.addLocalResult(localEcoutezLocalResult.getTitle(), localEcoutezLocalResult.getAddress(), str, j, k, bool1, local2, local3);
      int i3 = -1 + localEcoutezLocalResults.getLocalResultList().size();
      if (i1 < i3) {
        localUi.addLocalResultDivider();
      }
      i1++;
      break label160;
      bool1 = false;
      break;
      n = 0;
      break label157;
    }
    label305:
    if ((bool1) || (!localLocalResultsAction.canExecute()))
    {
      localUi.hideConfirmation();
      return;
    }
    localUi.showConfirmation(LocalResultUtils.getActionIconImageResource(i), LocalResultUtils.getActionLabelStringId(i));
  }
  
  protected void onPreExecute()
  {
    EventLogger.recordClientEvent(LocalResultUtils.getEventTypeLog(((LocalResultsAction)getVoiceAction()).getResults().getActionType()), Integer.valueOf(getActionTypeLog()));
  }
  
  public void setTransportationMethod(int paramInt)
  {
    ((LocalResultsAction)getVoiceAction()).setTransportationMethod(paramInt);
  }
  
  public static abstract interface Ui
    extends BaseCardUi, CountDownUi
  {
    public abstract void addLocalResult(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, boolean paramBoolean, View.OnClickListener paramOnClickListener1, View.OnClickListener paramOnClickListener2);
    
    public abstract void addLocalResultDivider();
    
    public abstract void hideConfirmation();
    
    public abstract void setActionTypeAndTransportationMethod(int paramInt1, int paramInt2);
    
    public abstract void setMapImageBitmap(Bitmap paramBitmap, View.OnClickListener paramOnClickListener);
    
    public abstract void setMapImageUrl(String paramString, View.OnClickListener paramOnClickListener);
    
    public abstract void showConfirmation(int paramInt1, int paramInt2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.LocalResultsController
 * JD-Core Version:    0.7.0.1
 */