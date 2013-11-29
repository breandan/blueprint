package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.OpenUrlAction;

public class OpenUrlController
  extends AbstractCardController<OpenUrlAction, Ui>
{
  public OpenUrlController(CardController paramCardController)
  {
    super(paramCardController);
  }
  
  public void initUi()
  {
    Ui localUi = (Ui)getUi();
    OpenUrlAction localOpenUrlAction = (OpenUrlAction)getVoiceAction();
    localUi.setName(localOpenUrlAction.getTitle());
    localUi.setDisplayUrl(localOpenUrlAction.getDisplayLink());
    localUi.setPreviewUrl(localOpenUrlAction.getRenderedLink());
  }
  
  public static abstract interface Ui
    extends BaseCardUi, CountDownUi
  {
    public abstract void setDisplayUrl(String paramString);
    
    public abstract void setName(String paramString);
    
    public abstract void setPreviewUrl(String paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.OpenUrlController
 * JD-Core Version:    0.7.0.1
 */