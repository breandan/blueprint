package com.google.android.sidekick.shared.remoteapi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;

public class TrainingQuestionNode
  implements Parcelable
{
  public static final Parcelable.Creator<TrainingQuestionNode> CREATOR = new Parcelable.Creator()
  {
    public TrainingQuestionNode createFromParcel(Parcel paramAnonymousParcel)
    {
      TrainingQuestion localTrainingQuestion = (TrainingQuestion)paramAnonymousParcel.readParcelable(TrainingQuestion.class.getClassLoader());
      ArrayList localArrayList1 = Lists.newArrayList();
      paramAnonymousParcel.readTypedList(localArrayList1, TrainingQuestionNode.CREATOR);
      int i = paramAnonymousParcel.readInt();
      ArrayList localArrayList2 = Lists.newArrayListWithCapacity(i);
      for (int j = 0; j < i; j++) {
        localArrayList2.add(ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, Sidekick.Question.Answer.class));
      }
      TrainingQuestionNode localTrainingQuestionNode = new TrainingQuestionNode(localTrainingQuestion, localArrayList2);
      Iterator localIterator = localArrayList1.iterator();
      while (localIterator.hasNext()) {
        localTrainingQuestionNode.addChild((TrainingQuestionNode)localIterator.next());
      }
      return localTrainingQuestionNode;
    }
    
    public TrainingQuestionNode[] newArray(int paramAnonymousInt)
    {
      return new TrainingQuestionNode[paramAnonymousInt];
    }
  };
  @Nullable
  private List<TrainingQuestionNode> mChildren = null;
  @Nullable
  private TrainingQuestionNode mParent = null;
  @Nullable
  private final List<Sidekick.Question.Answer> mRequiredParentAnswers;
  private final TrainingQuestion mTrainingQuestion;
  
  public TrainingQuestionNode(TrainingQuestion paramTrainingQuestion, @Nullable List<Sidekick.Question.Answer> paramList)
  {
    Preconditions.checkNotNull(paramTrainingQuestion);
    this.mTrainingQuestion = paramTrainingQuestion;
    this.mRequiredParentAnswers = paramList;
  }
  
  private static boolean answerEquals(int paramInt, Sidekick.Question.Answer paramAnswer1, Sidekick.Question.Answer paramAnswer2)
  {
    boolean bool = true;
    switch (paramInt)
    {
    default: 
      Log.e("Sidekick_TrainingQuestionNode", "Trying to compare unsupported question type: " + paramInt);
      bool = false;
    }
    do
    {
      do
      {
        return bool;
      } while (paramAnswer1.getYesNoAnswer() == paramAnswer2.getYesNoAnswer());
      return false;
    } while (paramAnswer1.getMultipleChoiceAnswer() == paramAnswer2.getMultipleChoiceAnswer());
    return false;
  }
  
  public static List<TrainingQuestionNode> flatten(Collection<TrainingQuestionNode> paramCollection)
  {
    LinkedList localLinkedList = Lists.newLinkedList(paramCollection);
    ArrayList localArrayList = Lists.newArrayListWithExpectedSize(localLinkedList.size());
    while (!localLinkedList.isEmpty())
    {
      TrainingQuestionNode localTrainingQuestionNode = (TrainingQuestionNode)localLinkedList.remove(0);
      localArrayList.add(localTrainingQuestionNode);
      if (localTrainingQuestionNode.mChildren != null) {
        localLinkedList.addAll(0, localTrainingQuestionNode.mChildren);
      }
    }
    return localArrayList;
  }
  
  public void addChild(TrainingQuestionNode paramTrainingQuestionNode)
  {
    if (paramTrainingQuestionNode.mParent == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Child should not have a parent yet");
      if (this.mChildren == null) {
        this.mChildren = Lists.newLinkedList();
      }
      paramTrainingQuestionNode.mParent = this;
      this.mChildren.add(paramTrainingQuestionNode);
      return;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<TrainingQuestionNode> getChildren()
  {
    if (this.mChildren != null) {
      return this.mChildren;
    }
    return Collections.emptyList();
  }
  
  @Nullable
  TrainingQuestionNode getParent()
  {
    return this.mParent;
  }
  
  public TrainingQuestion getQuestion()
  {
    return this.mTrainingQuestion;
  }
  
  public boolean isRequirementFulfilled()
  {
    if (this.mParent != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      if ((this.mRequiredParentAnswers != null) && (!this.mRequiredParentAnswers.isEmpty())) {
        break;
      }
      return true;
    }
    Sidekick.Question.Answer localAnswer1 = this.mParent.getQuestion().getAnswer();
    if (localAnswer1 != null)
    {
      Iterator localIterator = this.mRequiredParentAnswers.iterator();
      while (localIterator.hasNext())
      {
        Sidekick.Question.Answer localAnswer2 = (Sidekick.Question.Answer)localIterator.next();
        if (answerEquals(this.mParent.getQuestion().getType(), localAnswer2, localAnswer1)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean isVisible()
  {
    if (this.mParent == null) {}
    while ((isRequirementFulfilled()) && (this.mParent.isVisible())) {
      return true;
    }
    return false;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(this.mTrainingQuestion, 0);
    paramParcel.writeTypedList(this.mChildren);
    paramParcel.writeInt(this.mRequiredParentAnswers.size());
    Iterator localIterator = this.mRequiredParentAnswers.iterator();
    while (localIterator.hasNext()) {
      ProtoParcelable.writeProtoToParcel((Sidekick.Question.Answer)localIterator.next(), paramParcel);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode
 * JD-Core Version:    0.7.0.1
 */