package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.SelfNoteAction;

public class SelfNoteController
  extends AbstractCardController<SelfNoteAction, Ui>
{
  public SelfNoteController(CardController paramCardController)
  {
    super(paramCardController);
  }
  
  public void initUi()
  {
    if (((SelfNoteAction)getVoiceAction()).canExecute()) {
      ((Ui)getUi()).showSaveNote();
    }
    for (;;)
    {
      ((Ui)getUi()).setNoteText(((SelfNoteAction)getVoiceAction()).getNote());
      return;
      ((Ui)getUi()).showNewNote();
    }
  }
  
  public static abstract interface Ui
    extends BaseCardUi, CountDownUi
  {
    public abstract void setNoteText(String paramString);
    
    public abstract void showNewNote();
    
    public abstract void showSaveNote();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.SelfNoteController
 * JD-Core Version:    0.7.0.1
 */