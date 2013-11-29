package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.AgendaAction;
import com.google.majel.proto.CalendarProtos.AgendaItem;
import java.util.List;

public class AgendaController
  extends AbstractCardController<AgendaAction, Ui>
{
  public AgendaController(CardController paramCardController)
  {
    super(paramCardController);
  }
  
  protected void initUi()
  {
    AgendaAction localAgendaAction = (AgendaAction)getVoiceAction();
    ((Ui)getUi()).setAgenda(localAgendaAction.getAgendaItems(), localAgendaAction.shouldAutoExpandFirst(), localAgendaAction.getBatchSize(), localAgendaAction.getStartTime(), localAgendaAction.getEndTime(), localAgendaAction.getSortReverse());
  }
  
  public boolean onBackPressed()
  {
    return ((Ui)getUi()).onBackPressed();
  }
  
  public void start()
  {
    showCard();
  }
  
  public static abstract interface Ui
    extends BaseCardUi
  {
    public abstract boolean onBackPressed();
    
    public abstract void setAgenda(List<CalendarProtos.AgendaItem> paramList, boolean paramBoolean1, int paramInt, long paramLong1, long paramLong2, boolean paramBoolean2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.AgendaController
 * JD-Core Version:    0.7.0.1
 */