package com.google.android.sidekick.main.training;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public class IcebreakerSectionAdapter
  implements Parcelable
{
  public static final Parcelable.Creator<IcebreakerSectionAdapter> CREATOR = new Parcelable.Creator()
  {
    public IcebreakerSectionAdapter createFromParcel(Parcel paramAnonymousParcel)
    {
      IcebreakerSectionAdapter localIcebreakerSectionAdapter = new IcebreakerSectionAdapter(paramAnonymousParcel.readInt(), null);
      paramAnonymousParcel.readTypedList(localIcebreakerSectionAdapter.mGlobalQueue, TrainingQuestionNode.CREATOR);
      int i = paramAnonymousParcel.readInt();
      for (int j = 0; j < i; j++)
      {
        LinkedList localLinkedList = Lists.newLinkedList();
        paramAnonymousParcel.readTypedList(localLinkedList, TrainingQuestionNode.CREATOR);
        TrainingQuestionNode localTrainingQuestionNode = (TrainingQuestionNode)localLinkedList.poll();
        localIcebreakerSectionAdapter.mCurrentQuestions.put(j, localTrainingQuestionNode);
        localIcebreakerSectionAdapter.mEmptyQuestionSlots.delete(j);
        localIcebreakerSectionAdapter.mLocalQueues.put(j, localLinkedList);
      }
      return localIcebreakerSectionAdapter;
    }
    
    public IcebreakerSectionAdapter[] newArray(int paramAnonymousInt)
    {
      return new IcebreakerSectionAdapter[paramAnonymousInt];
    }
  };
  private static final String TAG = Tag.getTag(IcebreakerSectionAdapter.class);
  private SparseArray<TrainingQuestionNode> mCurrentQuestions;
  private SparseBooleanArray mEmptyQuestionSlots;
  private LinkedList<TrainingQuestionNode> mGlobalQueue;
  private SparseArray<LinkedList<TrainingQuestionNode>> mLocalQueues;
  private final int mMaxQuestionSlots;
  private final Set<Observer> mObservers = Sets.newHashSet();
  private TrainingQuestionManager mTrainingQuestionManager;
  
  private IcebreakerSectionAdapter(int paramInt)
  {
    this.mMaxQuestionSlots = paramInt;
    initializeQuestionSlots();
  }
  
  public IcebreakerSectionAdapter(int paramInt, TrainingQuestionManager paramTrainingQuestionManager, Collection<TrainingQuestionNode> paramCollection)
  {
    this.mMaxQuestionSlots = paramInt;
    this.mTrainingQuestionManager = paramTrainingQuestionManager;
    initializeQuestionSlots();
    initialize(paramCollection);
  }
  
  private void initialize(Iterable<TrainingQuestionNode> paramIterable)
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    Iterator localIterator1 = paramIterable.iterator();
    while (localIterator1.hasNext()) {
      localLinkedList.add((TrainingQuestionNode)localIterator1.next());
    }
    while (!localLinkedList.isEmpty())
    {
      TrainingQuestionNode localTrainingQuestionNode1 = (TrainingQuestionNode)localLinkedList.poll();
      if (localTrainingQuestionNode1.getQuestion().getAnswer() == null)
      {
        this.mGlobalQueue.add(localTrainingQuestionNode1);
      }
      else
      {
        Iterator localIterator2 = localTrainingQuestionNode1.getChildren().iterator();
        while (localIterator2.hasNext())
        {
          TrainingQuestionNode localTrainingQuestionNode2 = (TrainingQuestionNode)localIterator2.next();
          if (localTrainingQuestionNode2.isRequirementFulfilled()) {
            localLinkedList.add(localTrainingQuestionNode2);
          }
        }
      }
    }
    for (int i = 0; (i < this.mMaxQuestionSlots) && (!this.mGlobalQueue.isEmpty()); i++)
    {
      this.mCurrentQuestions.put(i, this.mGlobalQueue.poll());
      this.mEmptyQuestionSlots.delete(i);
    }
    notifyObservers();
  }
  
  private void initializeQuestionSlots()
  {
    this.mGlobalQueue = Lists.newLinkedList();
    this.mLocalQueues = new SparseArray();
    this.mCurrentQuestions = new SparseArray();
    this.mEmptyQuestionSlots = new SparseBooleanArray();
    for (int i = 0; i < this.mMaxQuestionSlots; i++) {
      this.mEmptyQuestionSlots.put(i, true);
    }
  }
  
  private void notifyObservers()
  {
    Iterator localIterator = this.mObservers.iterator();
    while (localIterator.hasNext()) {
      ((Observer)localIterator.next()).notifyChanged();
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @Nullable
  public TrainingQuestionNode getCurrentQuestion(int paramInt)
  {
    return (TrainingQuestionNode)this.mCurrentQuestions.get(paramInt);
  }
  
  public int getMaxQuestionSlots()
  {
    return this.mMaxQuestionSlots;
  }
  
  @Nullable
  public TrainingQuestionNode getNextQuestion(int paramInt)
  {
    LinkedList localLinkedList1 = (LinkedList)this.mLocalQueues.get(paramInt);
    if ((localLinkedList1 != null) && (!localLinkedList1.isEmpty()))
    {
      TrainingQuestionNode localTrainingQuestionNode2 = (TrainingQuestionNode)localLinkedList1.poll();
      this.mGlobalQueue.remove(localTrainingQuestionNode2);
      this.mCurrentQuestions.put(paramInt, localTrainingQuestionNode2);
      return localTrainingQuestionNode2;
    }
    if (!this.mGlobalQueue.isEmpty())
    {
      TrainingQuestionNode localTrainingQuestionNode1 = (TrainingQuestionNode)this.mGlobalQueue.poll();
      int i = 0;
      if (i < this.mMaxQuestionSlots)
      {
        LinkedList localLinkedList2 = (LinkedList)this.mLocalQueues.get(i);
        if (localLinkedList2 == null) {}
        for (;;)
        {
          i++;
          break;
          int j = localLinkedList2.indexOf(localTrainingQuestionNode1);
          if (j != -1) {
            localLinkedList2.remove(j);
          }
        }
      }
      this.mCurrentQuestions.put(paramInt, localTrainingQuestionNode1);
      this.mLocalQueues.delete(paramInt);
      return localTrainingQuestionNode1;
    }
    this.mCurrentQuestions.delete(paramInt);
    this.mEmptyQuestionSlots.put(paramInt, true);
    return null;
  }
  
  public void handleAnswerSelected(int paramInt, TrainingQuestionNode paramTrainingQuestionNode, Sidekick.Question.Answer paramAnswer)
  {
    this.mTrainingQuestionManager.setAnswer(paramTrainingQuestionNode.getQuestion().getQuestion(), paramAnswer, null);
    paramTrainingQuestionNode.getQuestion().updateAnswer(paramAnswer);
    Object localObject = null;
    LinkedList localLinkedList1 = (LinkedList)this.mLocalQueues.get(paramInt);
    if (localLinkedList1 == null)
    {
      localLinkedList1 = Lists.newLinkedList();
      this.mLocalQueues.put(paramInt, localLinkedList1);
    }
    LinkedList localLinkedList2 = Lists.newLinkedList();
    Iterator localIterator = paramTrainingQuestionNode.getChildren().iterator();
    while (localIterator.hasNext())
    {
      TrainingQuestionNode localTrainingQuestionNode2 = (TrainingQuestionNode)localIterator.next();
      if ((localTrainingQuestionNode2.getQuestion().getAnswer() == null) && (localTrainingQuestionNode2.isRequirementFulfilled())) {
        if (localObject == null) {
          localObject = localTrainingQuestionNode2;
        } else {
          localLinkedList2.add(localTrainingQuestionNode2);
        }
      }
    }
    for (int i = 0; (i < this.mEmptyQuestionSlots.size()) && (!localLinkedList2.isEmpty()); i++)
    {
      int j = this.mEmptyQuestionSlots.keyAt(i);
      TrainingQuestionNode localTrainingQuestionNode1 = (TrainingQuestionNode)localLinkedList2.poll();
      this.mCurrentQuestions.put(j, localTrainingQuestionNode1);
      this.mLocalQueues.put(j, null);
    }
    localLinkedList1.addAll(0, localLinkedList2);
    this.mGlobalQueue.addAll(localLinkedList2);
    if (localObject == null) {
      getNextQuestion(paramInt);
    }
    for (;;)
    {
      notifyObservers();
      return;
      this.mCurrentQuestions.put(paramInt, localObject);
    }
  }
  
  public boolean hasPendingQuestions()
  {
    return !this.mGlobalQueue.isEmpty();
  }
  
  public void registerObserver(Observer paramObserver)
  {
    this.mObservers.add(paramObserver);
  }
  
  public void setTrainingQuestionManager(TrainingQuestionManager paramTrainingQuestionManager)
  {
    this.mTrainingQuestionManager = paramTrainingQuestionManager;
  }
  
  public void unregisterObserver(Observer paramObserver)
  {
    this.mObservers.remove(paramObserver);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mMaxQuestionSlots);
    paramParcel.writeTypedList(this.mGlobalQueue);
    LinkedList localLinkedList1 = Lists.newLinkedList();
    int i = 0;
    while (i < this.mMaxQuestionSlots)
    {
      TrainingQuestionNode localTrainingQuestionNode = (TrainingQuestionNode)this.mCurrentQuestions.get(i);
      if (localTrainingQuestionNode == null)
      {
        i++;
      }
      else
      {
        LinkedList localLinkedList2 = (LinkedList)this.mLocalQueues.get(i);
        if (localLinkedList2 == null) {}
        for (LinkedList localLinkedList3 = Lists.newLinkedList();; localLinkedList3 = Lists.newLinkedList(localLinkedList2))
        {
          localLinkedList3.push(localTrainingQuestionNode);
          localLinkedList1.add(localLinkedList3);
          break;
        }
      }
    }
    paramParcel.writeInt(localLinkedList1.size());
    Iterator localIterator = localLinkedList1.iterator();
    while (localIterator.hasNext()) {
      paramParcel.writeTypedList((List)localIterator.next());
    }
  }
  
  public static abstract interface Observer
  {
    public abstract void notifyChanged();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.training.IcebreakerSectionAdapter
 * JD-Core Version:    0.7.0.1
 */