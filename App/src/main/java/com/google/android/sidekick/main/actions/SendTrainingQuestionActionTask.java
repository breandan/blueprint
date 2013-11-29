package com.google.android.sidekick.main.actions;

import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import com.google.geo.sidekick.Sidekick.Question;

public class SendTrainingQuestionActionTask
  extends RecordActionTask
{
  private Sidekick.Question mAnsweredTrainingQuestion;
  
  public SendTrainingQuestionActionTask(NetworkClient paramNetworkClient, Sidekick.Entry paramEntry, Sidekick.Action paramAction, Clock paramClock, Sidekick.Question paramQuestion)
  {
    super(paramNetworkClient, paramEntry, paramAction, paramClock);
    Preconditions.checkNotNull(paramQuestion);
    this.mAnsweredTrainingQuestion = paramQuestion;
  }
  
  protected Sidekick.ExecutedUserAction buildExecutedAction(Sidekick.Action paramAction, long paramLong)
  {
    return super.buildExecutedAction(paramAction, paramLong).setAnsweredTrainingQuestion(this.mAnsweredTrainingQuestion);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.SendTrainingQuestionActionTask
 * JD-Core Version:    0.7.0.1
 */