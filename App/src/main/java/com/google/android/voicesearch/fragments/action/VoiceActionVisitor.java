package com.google.android.voicesearch.fragments.action;

import com.google.android.search.core.SearchError;

public abstract interface VoiceActionVisitor<T>
{
  public abstract T visit(SearchError paramSearchError);
  
  public abstract T visit(AddEventAction paramAddEventAction);
  
  public abstract T visit(AgendaAction paramAgendaAction);
  
  public abstract T visit(CancelAction paramCancelAction);
  
  public abstract T visit(EmailAction paramEmailAction);
  
  public abstract T visit(HelpAction paramHelpAction);
  
  public abstract T visit(LocalResultsAction paramLocalResultsAction);
  
  public abstract T visit(OpenUrlAction paramOpenUrlAction);
  
  public abstract T visit(PhoneCallAction paramPhoneCallAction);
  
  public abstract T visit(PlayMediaAction paramPlayMediaAction);
  
  public abstract T visit(PuntAction paramPuntAction);
  
  public abstract T visit(ReadNotificationAction paramReadNotificationAction);
  
  public abstract T visit(SelfNoteAction paramSelfNoteAction);
  
  public abstract T visit(SetAlarmAction paramSetAlarmAction);
  
  public abstract T visit(SetReminderAction paramSetReminderAction);
  
  public abstract T visit(ShowContactInformationAction paramShowContactInformationAction);
  
  public abstract T visit(SmsAction paramSmsAction);
  
  public abstract T visit(SocialUpdateAction paramSocialUpdateAction);
  
  public abstract T visit(StopNavigationAction paramStopNavigationAction);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.action.VoiceActionVisitor
 * JD-Core Version:    0.7.0.1
 */