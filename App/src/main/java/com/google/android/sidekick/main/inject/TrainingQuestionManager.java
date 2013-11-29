package com.google.android.sidekick.main.inject;

import android.os.AsyncTask;
import com.google.android.apps.sidekick.training.Training.QuestionWithEntry;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import com.google.geo.sidekick.Sidekick.QuestionNode;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.TrainingModeDataResponse;
import com.google.geo.sidekick.Sidekick.TrainingModeMetadata;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

public abstract interface TrainingQuestionManager
{
  public abstract void clearData();
  
  public abstract void clearDataSync();
  
  public abstract Iterable<Training.QuestionWithEntry> getPendingAnsweredQuestionsWithEntries();
  
  public abstract Sidekick.TrainingModeMetadata getTrainingModeMetadata();
  
  public abstract boolean isDirty();
  
  public abstract List<TrainingQuestionNode> resolveQuestions(Collection<Sidekick.QuestionNode> paramCollection);
  
  public abstract ListenableFuture<Collection<TrainingQuestionNode>> resolveQuestionsAsync(Collection<Sidekick.QuestionNode> paramCollection);
  
  public abstract AsyncTask<Void, Void, Sidekick.ResponsePayload> sendAction(Sidekick.Entry paramEntry, Sidekick.Question paramQuestion, Sidekick.Action paramAction);
  
  public abstract void sendAnswers();
  
  public abstract void sendAnswersSync();
  
  public abstract void setAnswer(Sidekick.Question paramQuestion, Sidekick.Question.Answer paramAnswer, @Nullable Sidekick.Entry paramEntry);
  
  public abstract void updateFromServerResponse(Sidekick.TrainingModeDataResponse paramTrainingModeDataResponse, Iterable<Training.QuestionWithEntry> paramIterable);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.TrainingQuestionManager
 * JD-Core Version:    0.7.0.1
 */