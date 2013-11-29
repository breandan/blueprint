package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.google.android.search.core.util.SimpleCallbackFuture;
import com.google.android.shared.util.IntentStarter;
import com.google.android.speech.helper.AccountHelper;
import com.google.android.voicesearch.fragments.action.SelfNoteAction;
import com.google.android.voicesearch.util.EmailSender;
import com.google.android.voicesearch.util.EmailSender.Email;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SelfNoteActionExecutor
  extends IntentActionExecutor<SelfNoteAction>
{
  private final AccountHelper mAccountHelper;
  private final EmailSender mEmailSender;
  private final String mNoteToSelfEmailSubject;
  
  public SelfNoteActionExecutor(IntentStarter paramIntentStarter, AccountHelper paramAccountHelper, EmailSender paramEmailSender, String paramString)
  {
    super(paramIntentStarter);
    this.mAccountHelper = paramAccountHelper;
    this.mEmailSender = paramEmailSender;
    this.mNoteToSelfEmailSubject = paramString;
  }
  
  private Intent[] getIntents(SelfNoteAction paramSelfNoteAction, boolean paramBoolean)
  {
    EmailSender.Email localEmail = new EmailSender.Email();
    localEmail.subject = this.mNoteToSelfEmailSubject;
    localEmail.body = paramSelfNoteAction.getNote();
    Future localFuture = paramSelfNoteAction.getAudioUri();
    SimpleCallbackFuture localSimpleCallbackFuture = new SimpleCallbackFuture();
    this.mAccountHelper.getMainGmailAccount(localSimpleCallbackFuture);
    if (localFuture != null) {}
    try
    {
      localEmail.attachment = ((Uri)localFuture.get());
      String str = null;
      try
      {
        str = (String)localSimpleCallbackFuture.get();
        if (str != null) {
          localEmail.to = new String[] { str };
        }
      }
      catch (InterruptedException localInterruptedException1)
      {
        for (;;)
        {
          Log.i("SelfNoteActionExecutor", "Unable to get account");
        }
      }
      catch (ExecutionException localExecutionException1)
      {
        for (;;)
        {
          Log.i("SelfNoteActionExecutor", "Unable to get account");
        }
      }
      return this.mEmailSender.getIntents(localEmail, paramBoolean, str, true);
    }
    catch (InterruptedException localInterruptedException2)
    {
      for (;;)
      {
        Log.i("SelfNoteActionExecutor", "Unable to attach the audio", localInterruptedException2);
      }
    }
    catch (ExecutionException localExecutionException2)
    {
      for (;;)
      {
        Log.i("SelfNoteActionExecutor", "Unable to attach the audio", localExecutionException2);
      }
    }
  }
  
  protected Intent[] getExecuteIntents(SelfNoteAction paramSelfNoteAction)
  {
    return getIntents(paramSelfNoteAction, true);
  }
  
  protected Intent[] getOpenExternalAppIntents(SelfNoteAction paramSelfNoteAction)
  {
    return getIntents(paramSelfNoteAction, false);
  }
  
  protected Intent[] getProberIntents(SelfNoteAction paramSelfNoteAction)
  {
    return this.mEmailSender.createSelfNoteProbeIntents();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.SelfNoteActionExecutor
 * JD-Core Version:    0.7.0.1
 */