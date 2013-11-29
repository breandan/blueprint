package com.google.android.sidekick.shared.training;

import com.google.android.sidekick.shared.remoteapi.TrainingQuestion;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import javax.annotation.Nullable;

public abstract interface QuestionViewListener
{
  public abstract void onAnswerSelected(TrainingQuestion paramTrainingQuestion, Sidekick.Question.Answer paramAnswer, @Nullable Sidekick.Entry paramEntry);
  
  public abstract void onClientActionSelected(TrainingQuestion paramTrainingQuestion, int paramInt);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.training.QuestionViewListener
 * JD-Core Version:    0.7.0.1
 */