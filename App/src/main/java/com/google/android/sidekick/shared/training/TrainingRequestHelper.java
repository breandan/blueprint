package com.google.android.sidekick.shared.training;

import com.google.android.apps.sidekick.training.Training.QuestionWithEntry;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ActionsQuery;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import com.google.geo.sidekick.Sidekick.Question;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class TrainingRequestHelper
{
  public static Sidekick.ActionsQuery buildAnsweredQuestionsQuery(Iterable<Training.QuestionWithEntry> paramIterable)
  {
    Sidekick.ActionsQuery localActionsQuery = new Sidekick.ActionsQuery();
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      Training.QuestionWithEntry localQuestionWithEntry = (Training.QuestionWithEntry)localIterator.next();
      Sidekick.Question localQuestion = localQuestionWithEntry.getQuestion();
      Sidekick.Action localAction = new Sidekick.Action();
      localAction.setType(33);
      Sidekick.ExecutedUserAction localExecutedUserAction = new Sidekick.ExecutedUserAction().setAction(localAction).setTimestampSeconds(TimeUnit.MILLISECONDS.toSeconds(localQuestion.getAnswerTimestampMillis())).setAnsweredTrainingQuestion(localQuestion);
      if (localQuestionWithEntry.getEntry() != null) {
        localExecutedUserAction.setEntry(localQuestionWithEntry.getEntry());
      }
      localActionsQuery.addExecutedUserAction(localExecutedUserAction);
    }
    return localActionsQuery;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.training.TrainingRequestHelper
 * JD-Core Version:    0.7.0.1
 */