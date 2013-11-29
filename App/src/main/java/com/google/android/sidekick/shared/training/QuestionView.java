package com.google.android.sidekick.shared.training;

import com.google.android.sidekick.shared.remoteapi.TrainingQuestion;
import com.google.geo.sidekick.Sidekick.Entry;

public abstract interface QuestionView
{
  public abstract void setEntry(Sidekick.Entry paramEntry);
  
  public abstract void setListener(QuestionViewListener paramQuestionViewListener);
  
  public abstract void setTrainingQuestion(TrainingQuestion paramTrainingQuestion);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.training.QuestionView
 * JD-Core Version:    0.7.0.1
 */