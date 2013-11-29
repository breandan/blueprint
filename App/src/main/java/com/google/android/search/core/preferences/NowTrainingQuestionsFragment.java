package com.google.android.search.core.preferences;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView.RecyclerListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarInfo;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.preferences.cards.AddStockDialogFragment;
import com.google.android.search.core.preferences.cards.AddStockDialogFragment.StockDataWithName;
import com.google.android.search.core.preferences.cards.AddStockDialogFragment.StockFragment;
import com.google.android.search.core.preferences.cards.AddStockDialogFragment.TickerFetcherFragment;
import com.google.android.search.core.preferences.cards.AddTeamDialogFragment;
import com.google.android.search.core.preferences.cards.AddTeamDialogFragment.SportTeamPlayerWithName;
import com.google.android.search.core.preferences.cards.SportsEntitiesProvider;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.Consumers;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.main.training.IcebreakerSectionAdapter;
import com.google.android.sidekick.main.training.IcebreakerSectionView;
import com.google.android.sidekick.main.training.IcebreakerSectionView.Listener;
import com.google.android.sidekick.main.training.TrainingClosetActivity;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion.Option;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.android.sidekick.shared.training.QuestionKey;
import com.google.android.sidekick.shared.training.QuestionView;
import com.google.android.sidekick.shared.training.QuestionViewListener;
import com.google.android.sidekick.shared.training.TrainingQuestionViewHelper;
import com.google.android.sidekick.shared.util.CalendarDataUtil;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import com.google.geo.sidekick.Sidekick.Question.Entity;
import com.google.geo.sidekick.Sidekick.QuestionNode;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.Sports.SportTeamPlayer;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.StockQuotes.StockData;
import com.google.geo.sidekick.Sidekick.SportsTeams;
import com.google.geo.sidekick.Sidekick.TrainingModeClosetResponse;
import com.google.geo.sidekick.Sidekick.TrainingModeClosetResponse.QuestionGroup;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public class NowTrainingQuestionsFragment
  extends NowConfigurationListFragment
  implements AbsListView.RecyclerListener, NowTrainingQuestionsFetcherFragment.Listener, AddStockDialogFragment.StockFragment, Consumer<Collection<TrainingQuestionNode>>
{
  private static final String TAG = Tag.getTag(NowTrainingQuestionsFragment.class);
  @Nullable
  private ClosetListAdapter mAdapter;
  private CalendarDataProvider mCalendarProvider;
  @Nullable
  private Sidekick.TrainingModeClosetResponse mCloset;
  @Nullable
  private byte[] mClosetSnapshot;
  private Set<QuestionKey> mExpandedQuestions;
  private int mNumPlaces;
  @Nullable
  private int[] mQuestionGroupPath;
  @Nullable
  private Sidekick.TrainingModeClosetResponse.QuestionGroup mQuestionGroupToDisplay;
  @Nullable
  private Sidekick.Question mQuestionToDisplay;
  @Nullable
  private Bundle mSavedInstanceState;
  @Nullable
  private SportsEntitiesProvider mSportsDataProvider;
  private TrainingQuestionManager mTrainingQuestionManager;
  private ScheduledSingleThreadedExecutor mUiExecutor;
  
  private void addIcebreakerListItem(List<ClosetListItem> paramList, Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, Map<QuestionKey, TrainingQuestionNode> paramMap)
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    Iterator localIterator = paramQuestionGroup.getQuestionNodeList().iterator();
    while (localIterator.hasNext())
    {
      TrainingQuestionNode localTrainingQuestionNode = (TrainingQuestionNode)paramMap.get(new QuestionKey(((Sidekick.QuestionNode)localIterator.next()).getQuestion()));
      if (localTrainingQuestionNode != null) {
        localLinkedList.add(localTrainingQuestionNode);
      }
    }
    int i = paramList.size();
    String str = "TRAINING_IB_" + i;
    IcebreakerSectionAdapter localIcebreakerSectionAdapter;
    if ((this.mSavedInstanceState != null) && (this.mSavedInstanceState.containsKey(str)))
    {
      localIcebreakerSectionAdapter = (IcebreakerSectionAdapter)this.mSavedInstanceState.getParcelable(str);
      localIcebreakerSectionAdapter.setTrainingQuestionManager(this.mTrainingQuestionManager);
    }
    for (;;)
    {
      paramList.add(ClosetListItem.newIcebreakerSectionListItem(localIcebreakerSectionAdapter));
      return;
      localIcebreakerSectionAdapter = new IcebreakerSectionAdapter(2, this.mTrainingQuestionManager, localLinkedList);
    }
  }
  
  private void addIcebreakerQuestionToQuestionGroup(TrainingQuestion paramTrainingQuestion, Sidekick.Question.Answer paramAnswer)
  {
    Collection localCollection = paramTrainingQuestion.getAttributes();
    boolean bool = localCollection.isEmpty();
    Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup = null;
    if (!bool) {
      localQuestionGroup = findQuestionGroupByAttributes(this.mCloset.getQuestionGroupList(), localCollection);
    }
    if (localQuestionGroup == null) {
      localQuestionGroup = findQuestionGroupByType(this.mCloset.getQuestionGroupList(), 4);
    }
    int i;
    List localList;
    if (localQuestionGroup != null)
    {
      paramTrainingQuestion.updateAnswer(paramAnswer);
      if (localQuestionGroup.getAttributeList().contains(Integer.valueOf(4))) {
        insertTrainingQuestionAlphabetical(localQuestionGroup, paramTrainingQuestion);
      }
      for (;;)
      {
        i = 0;
        localList = this.mAdapter.getRawItems();
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext())
        {
          ClosetListItem localClosetListItem = (ClosetListItem)localIterator.next();
          if ((localClosetListItem.mType == 2) && (isDescendent(localClosetListItem.mQuestionGroup, localQuestionGroup)))
          {
            ClosetListItem.access$1102(localClosetListItem, getAnsweredQuestionCount(localQuestionGroup, this.mCalendarProvider.getCalendarsList()));
            i = 1;
          }
        }
        prependQuestionToNodeList(localQuestionGroup, paramTrainingQuestion.getQuestion());
      }
      if (isCurrentlyDisplayingQuestionsInGroup(localQuestionGroup)) {
        onClosetLoaded(this.mCloset, this.mNumPlaces);
      }
    }
    for (;;)
    {
      onClosetUpdated();
      return;
      if (i != 0)
      {
        this.mAdapter.setItems(localList);
        continue;
        Log.e(TAG, "Could not find target group for icebreaker question");
      }
    }
  }
  
  private void addNewSportsTeam(AddTeamDialogFragment.SportTeamPlayerWithName paramSportTeamPlayerWithName, ClosetListItem paramClosetListItem)
  {
    TrainingQuestionNode localTrainingQuestionNode = paramClosetListItem.mTrainingQuestionNode;
    TrainingQuestion localTrainingQuestion = createNewQuestionForSportsTeam(paramSportTeamPlayerWithName, localTrainingQuestionNode);
    Sidekick.Question localQuestion = insertTrainingQuestionAlphabetical(localTrainingQuestionNode, localTrainingQuestion);
    this.mTrainingQuestionManager.setAnswer(localQuestion, localTrainingQuestion.getAnswer(), null);
  }
  
  private void addQuestionNodeListItems(List<ClosetListItem> paramList, Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, Map<QuestionKey, TrainingQuestionNode> paramMap)
  {
    int i = -1;
    if (paramQuestionGroup.getAttributeList().contains(Integer.valueOf(4))) {
      i = 7;
    }
    Iterator localIterator = paramQuestionGroup.getQuestionNodeList().iterator();
    while (localIterator.hasNext())
    {
      QuestionKey localQuestionKey = new QuestionKey(((Sidekick.QuestionNode)localIterator.next()).getQuestion());
      TrainingQuestionNode localTrainingQuestionNode = (TrainingQuestionNode)paramMap.get(localQuestionKey);
      if (localTrainingQuestionNode != null)
      {
        int j = getListItemType(localTrainingQuestionNode);
        if (j == -1)
        {
          Log.e(TAG, "Unexpected question type in closet: " + localTrainingQuestionNode.getQuestion().getType());
        }
        else
        {
          if ((j == 0) && (i != -1)) {
            j = i;
          }
          ClosetListItem localClosetListItem = ClosetListItem.newQuestionListItem(j, localTrainingQuestionNode);
          if ((this.mExpandedQuestions.contains(localQuestionKey)) || (localClosetListItem.shouldDisplay())) {
            paramList.add(localClosetListItem);
          }
        }
      }
    }
  }
  
  private ClosetListItem buildCategoryListItem(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup)
  {
    if (paramQuestionGroup.getGroupType() == 1) {}
    for (int i = this.mNumPlaces;; i = getAnsweredQuestionCount(paramQuestionGroup, this.mCalendarProvider.getCalendarsList())) {
      return ClosetListItem.newQuestionCategoryListItem(paramQuestionGroup, i);
    }
  }
  
  static int compareEntities(ProtoKey<Sidekick.Question.Entity> paramProtoKey1, ProtoKey<Sidekick.Question.Entity> paramProtoKey2)
  {
    String str1 = ((Sidekick.Question.Entity)paramProtoKey1.getProto()).getValue();
    String str2 = ((Sidekick.Question.Entity)paramProtoKey2.getProto()).getValue();
    int i;
    if ((str1 == null) && (str2 == null)) {
      i = 0;
    }
    do
    {
      return i;
      if ((str1 == null) && (str2 != null)) {
        return -1;
      }
      if ((str1 != null) && (str2 == null)) {
        return 1;
      }
      i = str1.compareToIgnoreCase(str2);
    } while (i != 0);
    return Integer.valueOf(paramProtoKey1.hashCode()).compareTo(Integer.valueOf(paramProtoKey2.hashCode()));
  }
  
  private static ClosetListItem createNewItem(TrainingQuestion paramTrainingQuestion)
  {
    return ClosetListItem.newQuestionListItem(7, new TrainingQuestionNode(paramTrainingQuestion, Lists.newArrayList()));
  }
  
  private static TrainingQuestion createNewQuestionForSportsTeam(AddTeamDialogFragment.SportTeamPlayerWithName paramSportTeamPlayerWithName, TrainingQuestionNode paramTrainingQuestionNode)
  {
    Sidekick.Question.Entity localEntity = new Sidekick.Question.Entity();
    localEntity.setSportTeamPlayer(paramSportTeamPlayerWithName.getSportTeamPlayer()).setValue(paramSportTeamPlayerWithName.toString());
    Sidekick.Question.Answer localAnswer = new Sidekick.Question.Answer().setYesNoAnswer(true);
    TrainingQuestion localTrainingQuestion = paramTrainingQuestionNode.getQuestion().createQuestionWithParam(localEntity);
    localTrainingQuestion.updateAnswer(localAnswer);
    return localTrainingQuestion;
  }
  
  private static TrainingQuestion createNewQuestionForStock(AddStockDialogFragment.StockDataWithName paramStockDataWithName, TrainingQuestionNode paramTrainingQuestionNode)
  {
    Sidekick.Question.Entity localEntity = new Sidekick.Question.Entity();
    localEntity.setStockData(paramStockDataWithName.getStockData()).setValue(paramStockDataWithName.getStockData().getSymbol());
    Sidekick.Question.Answer localAnswer = new Sidekick.Question.Answer().setYesNoAnswer(true);
    TrainingQuestion localTrainingQuestion = paramTrainingQuestionNode.getQuestion().createQuestionWithParam(localEntity);
    localTrainingQuestion.updateAnswer(localAnswer);
    return localTrainingQuestion;
  }
  
  private boolean extractTargetFromBundle(@Nullable Bundle paramBundle)
  {
    if (paramBundle == null) {}
    byte[] arrayOfByte;
    do
    {
      do
      {
        return false;
        if (paramBundle.containsKey("TRAINING_CLOSET_CATEGORY"))
        {
          this.mQuestionGroupPath = paramBundle.getIntArray("TRAINING_CLOSET_CATEGORY");
          return true;
        }
      } while (!paramBundle.containsKey("com.google.android.search.core.preferences.ARGUMENT_QUESTION"));
      arrayOfByte = paramBundle.getByteArray("com.google.android.search.core.preferences.ARGUMENT_QUESTION");
    } while (arrayOfByte == null);
    try
    {
      this.mQuestionToDisplay = Sidekick.Question.parseFrom(arrayOfByte);
      return true;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      Log.e(TAG, "Failed to parse target Question proto from bundle");
    }
    return false;
  }
  
  private static int findIcebreakerQuestionGroupIndex(List<Sidekick.TrainingModeClosetResponse.QuestionGroup> paramList)
  {
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      if (isIcebreakerQuestionGroup((Sidekick.TrainingModeClosetResponse.QuestionGroup)localIterator.next())) {
        return i;
      }
      i++;
    }
    return -1;
  }
  
  @Nullable
  private static Sidekick.Question findQuestionByKey(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, QuestionKey paramQuestionKey)
  {
    Iterator localIterator = paramQuestionGroup.getQuestionNodeList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.QuestionNode localQuestionNode = (Sidekick.QuestionNode)localIterator.next();
      if (paramQuestionKey.equals(new QuestionKey(localQuestionNode.getQuestion()))) {
        return localQuestionNode.getQuestion();
      }
    }
    return null;
  }
  
  @Nullable
  private static Sidekick.TrainingModeClosetResponse.QuestionGroup findQuestionGroupByAttributes(List<Sidekick.TrainingModeClosetResponse.QuestionGroup> paramList, Collection<Integer> paramCollection)
  {
    Preconditions.checkNotNull(paramCollection);
    findQuestionGroupByPredicate(paramList, new Predicate()
    {
      public boolean apply(Sidekick.TrainingModeClosetResponse.QuestionGroup paramAnonymousQuestionGroup)
      {
        return this.val$attributes.equals(paramAnonymousQuestionGroup.getAttributeList());
      }
    });
  }
  
  @Nullable
  private static Sidekick.TrainingModeClosetResponse.QuestionGroup findQuestionGroupByPredicate(List<Sidekick.TrainingModeClosetResponse.QuestionGroup> paramList, Predicate<Sidekick.TrainingModeClosetResponse.QuestionGroup> paramPredicate)
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    Iterator localIterator1 = paramList.iterator();
    while (localIterator1.hasNext()) {
      localLinkedList.add((Sidekick.TrainingModeClosetResponse.QuestionGroup)localIterator1.next());
    }
    while (!localLinkedList.isEmpty())
    {
      Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup = (Sidekick.TrainingModeClosetResponse.QuestionGroup)localLinkedList.poll();
      if (paramPredicate.apply(localQuestionGroup)) {
        return localQuestionGroup;
      }
      Iterator localIterator2 = localQuestionGroup.getChildList().iterator();
      while (localIterator2.hasNext()) {
        localLinkedList.add((Sidekick.TrainingModeClosetResponse.QuestionGroup)localIterator2.next());
      }
    }
    return null;
  }
  
  @Nullable
  private static Sidekick.TrainingModeClosetResponse.QuestionGroup findQuestionGroupByType(List<Sidekick.TrainingModeClosetResponse.QuestionGroup> paramList, int paramInt)
  {
    findQuestionGroupByPredicate(paramList, new Predicate()
    {
      public boolean apply(Sidekick.TrainingModeClosetResponse.QuestionGroup paramAnonymousQuestionGroup)
      {
        return paramAnonymousQuestionGroup.getGroupType() == this.val$groupType;
      }
    });
  }
  
  @Nullable
  private static Sidekick.TrainingModeClosetResponse.QuestionGroup findQuestionGroupForQuestionInNodeList(List<Sidekick.TrainingModeClosetResponse.QuestionGroup> paramList, Sidekick.Question paramQuestion)
  {
    findQuestionGroupByPredicate(paramList, new Predicate()
    {
      public boolean apply(Sidekick.TrainingModeClosetResponse.QuestionGroup paramAnonymousQuestionGroup)
      {
        return NowTrainingQuestionsFragment.findQuestionByKey(paramAnonymousQuestionGroup, this.val$questionKey) != null;
      }
    });
  }
  
  @Nullable
  private static Sidekick.TrainingModeClosetResponse.QuestionGroup findQuestionGroupThatPresentsQuestion(List<Sidekick.TrainingModeClosetResponse.QuestionGroup> paramList, Sidekick.Question paramQuestion)
  {
    findQuestionGroupByPredicate(paramList, new Predicate()
    {
      public boolean apply(Sidekick.TrainingModeClosetResponse.QuestionGroup paramAnonymousQuestionGroup)
      {
        Iterator localIterator = NowTrainingQuestionsFragment.getQuestionNodesToDisplay(paramAnonymousQuestionGroup).iterator();
        while (localIterator.hasNext())
        {
          Sidekick.QuestionNode localQuestionNode = (Sidekick.QuestionNode)localIterator.next();
          if (this.val$questionKey.equals(new QuestionKey(localQuestionNode.getQuestion()))) {
            return true;
          }
        }
        return false;
      }
    });
  }
  
  @Nullable
  private Collection<Sidekick.Question> findRawQuestions(TrainingQuestion paramTrainingQuestion)
  {
    LinkedList localLinkedList1 = Lists.newLinkedList();
    Iterator localIterator1 = this.mCloset.getQuestionGroupList().iterator();
    while (localIterator1.hasNext()) {
      localLinkedList1.add((Sidekick.TrainingModeClosetResponse.QuestionGroup)localIterator1.next());
    }
    QuestionKey localQuestionKey = new QuestionKey(paramTrainingQuestion.getQuestion());
    LinkedList localLinkedList2 = Lists.newLinkedList();
    while (!localLinkedList1.isEmpty())
    {
      Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup = (Sidekick.TrainingModeClosetResponse.QuestionGroup)localLinkedList1.poll();
      Iterator localIterator2 = localQuestionGroup.getQuestionNodeList().iterator();
      while (localIterator2.hasNext())
      {
        Sidekick.QuestionNode localQuestionNode = (Sidekick.QuestionNode)localIterator2.next();
        if (localQuestionKey.equals(new QuestionKey(localQuestionNode.getQuestion()))) {
          localLinkedList2.add(localQuestionNode.getQuestion());
        }
      }
      Iterator localIterator3 = localQuestionGroup.getChildList().iterator();
      while (localIterator3.hasNext()) {
        localLinkedList1.add((Sidekick.TrainingModeClosetResponse.QuestionGroup)localIterator3.next());
      }
    }
    return localLinkedList2;
  }
  
  private static int getAnsweredQuestionCount(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, Collection<Calendar.CalendarInfo> paramCollection)
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    localLinkedList.add(paramQuestionGroup);
    int i = 0;
    while (!localLinkedList.isEmpty())
    {
      Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup = (Sidekick.TrainingModeClosetResponse.QuestionGroup)localLinkedList.poll();
      boolean bool = localQuestionGroup.getAttributeList().contains(Integer.valueOf(4));
      Iterator localIterator1 = localQuestionGroup.getQuestionNodeList().iterator();
      while (localIterator1.hasNext())
      {
        Sidekick.QuestionNode localQuestionNode = (Sidekick.QuestionNode)localIterator1.next();
        if ((localQuestionNode.getQuestion().hasAnswer()) && (!isIcebreakerQuestionGroup(localQuestionGroup)) && ((!bool) || (localQuestionNode.getQuestion().getAnswer().getYesNoAnswer() == true)) && (!isUnknownCalendarQuestion(localQuestionNode, paramCollection))) {
          i++;
        }
      }
      Iterator localIterator2 = localQuestionGroup.getChildList().iterator();
      while (localIterator2.hasNext()) {
        localLinkedList.add((Sidekick.TrainingModeClosetResponse.QuestionGroup)localIterator2.next());
      }
    }
    return i;
  }
  
  private static int getListItemType(TrainingQuestionNode paramTrainingQuestionNode)
  {
    switch (paramTrainingQuestionNode.getQuestion().getType())
    {
    case 0: 
    default: 
      return -1;
    case 1: 
      return 0;
    case 2: 
      return 1;
    case -1: 
      return 4;
    }
    return 5;
  }
  
  @Nullable
  private static Sidekick.TrainingModeClosetResponse.QuestionGroup getQuestionGroupForPath(Sidekick.TrainingModeClosetResponse paramTrainingModeClosetResponse, int[] paramArrayOfInt)
  {
    List localList = paramTrainingModeClosetResponse.getQuestionGroupList();
    for (int i = 0;; i++)
    {
      int j;
      Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup;
      if (i < paramArrayOfInt.length)
      {
        j = paramArrayOfInt[i];
        if ((j < 0) || (j >= localList.size()))
        {
          String str = TAG;
          Object[] arrayOfObject = new Object[2];
          arrayOfObject[0] = Integer.valueOf(i);
          arrayOfObject[1] = Arrays.toString(paramArrayOfInt);
          Log.e(str, String.format("Invalid index at depth: %d, path: %s", arrayOfObject));
        }
      }
      else
      {
        localQuestionGroup = null;
      }
      do
      {
        return localQuestionGroup;
        localQuestionGroup = (Sidekick.TrainingModeClosetResponse.QuestionGroup)localList.get(j);
      } while (i == -1 + paramArrayOfInt.length);
      localList = localQuestionGroup.getChildList();
    }
  }
  
  private static boolean getQuestionGroupPath(LinkedList<Integer> paramLinkedList, Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, List<Sidekick.TrainingModeClosetResponse.QuestionGroup> paramList)
  {
    for (int i = 0; i < paramList.size(); i++)
    {
      paramLinkedList.push(Integer.valueOf(i));
      Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup = (Sidekick.TrainingModeClosetResponse.QuestionGroup)paramList.get(i);
      if (localQuestionGroup == paramQuestionGroup) {}
      while (getQuestionGroupPath(paramLinkedList, paramQuestionGroup, localQuestionGroup.getChildList())) {
        return true;
      }
      paramLinkedList.pop();
    }
    return false;
  }
  
  @Nullable
  private int[] getQuestionGroupPath(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup)
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    getQuestionGroupPath(localLinkedList, paramQuestionGroup, this.mCloset.getQuestionGroupList());
    int[] arrayOfInt;
    if (localLinkedList.isEmpty()) {
      arrayOfInt = null;
    }
    for (;;)
    {
      return arrayOfInt;
      Collections.reverse(localLinkedList);
      arrayOfInt = new int[localLinkedList.size()];
      for (int i = 0; i < localLinkedList.size(); i++) {
        arrayOfInt[i] = ((Integer)localLinkedList.get(i)).intValue();
      }
    }
  }
  
  @Nullable
  private Sidekick.TrainingModeClosetResponse.QuestionGroup getQuestionGroupToDisplay(Sidekick.TrainingModeClosetResponse paramTrainingModeClosetResponse)
  {
    if ((this.mQuestionGroupPath == null) && (this.mQuestionToDisplay == null)) {
      return null;
    }
    if (this.mQuestionToDisplay != null) {
      return findQuestionGroupThatPresentsQuestion(paramTrainingModeClosetResponse.getQuestionGroupList(), this.mQuestionToDisplay);
    }
    return getQuestionGroupForPath(paramTrainingModeClosetResponse, this.mQuestionGroupPath);
  }
  
  private static Collection<Sidekick.QuestionNode> getQuestionNodesToDisplay(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup)
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    localLinkedList.addAll(paramQuestionGroup.getQuestionNodeList());
    Iterator localIterator = paramQuestionGroup.getChildList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup = (Sidekick.TrainingModeClosetResponse.QuestionGroup)localIterator.next();
      if (isLeafGroup(localQuestionGroup)) {
        localLinkedList.addAll(localQuestionGroup.getQuestionNodeList());
      }
    }
    return localLinkedList;
  }
  
  private static void hideEditView(View paramView)
  {
    paramView.findViewById(2131297123).setVisibility(0);
    View localView = paramView.findViewById(2131297126);
    if (localView != null) {
      localView.setVisibility(8);
    }
  }
  
  private void initializeSportsEntitiesProvider()
  {
    this.mSportsDataProvider = new SportsEntitiesProvider(this);
    this.mSportsDataProvider.loadSportsData();
  }
  
  private static Sidekick.Question insertQuestionAlphabetical(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, Sidekick.Question paramQuestion, Sidekick.Question.Entity paramEntity)
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(1 + paramQuestionGroup.getQuestionNodeCount());
    Sidekick.Question localQuestion = paramQuestion;
    int i = 0;
    ProtoKey localProtoKey1 = new ProtoKey(paramEntity);
    Iterator localIterator1 = paramQuestionGroup.getQuestionNodeList().iterator();
    if (localIterator1.hasNext())
    {
      Sidekick.QuestionNode localQuestionNode = (Sidekick.QuestionNode)localIterator1.next();
      label79:
      Sidekick.Question.Entity localEntity;
      ProtoKey localProtoKey2;
      if (i == 0)
      {
        Iterator localIterator2 = localQuestionNode.getQuestion().getParameterList().iterator();
        if (localIterator2.hasNext())
        {
          localEntity = (Sidekick.Question.Entity)localIterator2.next();
          localProtoKey2 = new ProtoKey(localEntity);
          if (!localProtoKey1.equals(localProtoKey2)) {
            break label157;
          }
          localQuestionNode.getQuestion().setAnswer(paramQuestion.getAnswer());
          localQuestion = localQuestionNode.getQuestion();
        }
      }
      for (i = 1;; i = 1)
      {
        localArrayList.add(localQuestionNode);
        break;
        label157:
        if ((compareEntities(localProtoKey2, localProtoKey1) <= 0) && (!localEntity.getValue().isEmpty())) {
          break label79;
        }
        localArrayList.add(new Sidekick.QuestionNode().setQuestion(paramQuestion));
      }
    }
    setQuestionNodes(paramQuestionGroup, localArrayList);
    return localQuestion;
  }
  
  private Sidekick.Question insertTrainingQuestionAlphabetical(TrainingQuestionNode paramTrainingQuestionNode, TrainingQuestion paramTrainingQuestion)
  {
    Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup = findQuestionGroupForQuestionInNodeList(ImmutableList.of(this.mQuestionGroupToDisplay), paramTrainingQuestionNode.getQuestion().getQuestion());
    Sidekick.Question localQuestion1 = paramTrainingQuestion.getQuestion();
    if (localQuestionGroup != null)
    {
      Sidekick.Question localQuestion2 = insertTrainingQuestionAlphabetical(localQuestionGroup, paramTrainingQuestion);
      onClosetUpdated();
      return localQuestion2;
    }
    Log.e(TAG, "Could not find add entity QuestionGroup");
    return localQuestion1;
  }
  
  private Sidekick.Question insertTrainingQuestionAlphabetical(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, TrainingQuestion paramTrainingQuestion)
  {
    Sidekick.Question.Entity localEntity = paramTrainingQuestion.getPrimaryEntity();
    ProtoKey localProtoKey1 = new ProtoKey(localEntity);
    Preconditions.checkNotNull(localEntity);
    Sidekick.Question localQuestion1 = paramTrainingQuestion.getQuestion();
    Sidekick.Question localQuestion2 = insertQuestionAlphabetical(paramQuestionGroup, localQuestion1, localEntity);
    ClosetListItem localClosetListItem1 = createNewItem(paramTrainingQuestion);
    int i = getListAdapter().getCount();
    ArrayList localArrayList = Lists.newArrayListWithCapacity(i + 1);
    int j = 1;
    int k = 0;
    if (k < i)
    {
      ClosetListItem localClosetListItem2 = (ClosetListItem)getListAdapter().getItem(k);
      label130:
      ProtoKey localProtoKey2;
      if (j != 0)
      {
        if (localClosetListItem2.mType != 7) {
          break label227;
        }
        TrainingQuestionNode localTrainingQuestionNode = localClosetListItem2.mTrainingQuestionNode;
        Iterator localIterator = localTrainingQuestionNode.getQuestion().getQuestion().getParameterList().iterator();
        if (localIterator.hasNext())
        {
          localProtoKey2 = new ProtoKey((Sidekick.Question.Entity)localIterator.next());
          if (!localProtoKey1.equals(localProtoKey2)) {
            break label201;
          }
          localTrainingQuestionNode.getQuestion().updateAnswer(localQuestion1.getAnswer());
          j = 0;
        }
      }
      for (;;)
      {
        localArrayList.add(localClosetListItem2);
        k++;
        break;
        label201:
        if (compareEntities(localProtoKey2, localProtoKey1) <= 0) {
          break label130;
        }
        localArrayList.add(localClosetListItem1);
        j = 0;
        continue;
        label227:
        if ((localClosetListItem2.mType == 5) || (localClosetListItem2.mType == 4))
        {
          localArrayList.add(localClosetListItem1);
          j = 0;
        }
      }
    }
    this.mAdapter.setItems(localArrayList);
    return localQuestion2;
  }
  
  private boolean isCurrentlyDisplayingQuestionsInGroup(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup)
  {
    if (this.mQuestionGroupToDisplay == null) {}
    while ((isIcebreakerQuestionGroup(paramQuestionGroup)) || (!isLeafGroup(paramQuestionGroup))) {
      return false;
    }
    if (paramQuestionGroup == this.mQuestionGroupToDisplay) {
      return true;
    }
    Iterator localIterator = this.mQuestionGroupToDisplay.getChildList().iterator();
    while (localIterator.hasNext()) {
      if (paramQuestionGroup == (Sidekick.TrainingModeClosetResponse.QuestionGroup)localIterator.next()) {
        return true;
      }
    }
    return true;
  }
  
  private boolean isDescendent(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup1, Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup2)
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    localLinkedList.add(paramQuestionGroup1);
    while (!localLinkedList.isEmpty())
    {
      Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup = (Sidekick.TrainingModeClosetResponse.QuestionGroup)localLinkedList.poll();
      if (localQuestionGroup == paramQuestionGroup2) {
        return true;
      }
      Iterator localIterator = localQuestionGroup.getChildList().iterator();
      while (localIterator.hasNext()) {
        localLinkedList.add((Sidekick.TrainingModeClosetResponse.QuestionGroup)localIterator.next());
      }
    }
    return false;
  }
  
  private static boolean isIcebreakerQuestionGroup(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup)
  {
    return (paramQuestionGroup.hasGroupType()) && (paramQuestionGroup.getGroupType() == 5);
  }
  
  private static boolean isLeafGroup(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup)
  {
    return paramQuestionGroup.getChildCount() == 0;
  }
  
  private static boolean isUnknownCalendarQuestion(Sidekick.QuestionNode paramQuestionNode, Collection<Calendar.CalendarInfo> paramCollection)
  {
    if (paramQuestionNode.getQuestion().getParameterCount() > 0)
    {
      Iterator localIterator1 = paramQuestionNode.getQuestion().getParameterList().iterator();
      while (localIterator1.hasNext())
      {
        Sidekick.Question.Entity localEntity = (Sidekick.Question.Entity)localIterator1.next();
        if (localEntity.hasCalendarAccountHash())
        {
          String str = localEntity.getCalendarAccountHash();
          Iterator localIterator2 = paramCollection.iterator();
          do
          {
            if (!localIterator2.hasNext()) {
              break;
            }
          } while (!str.equals(CalendarDataUtil.getHashString(((Calendar.CalendarInfo)localIterator2.next()).getId())));
        }
      }
    }
    return false;
    return true;
  }
  
  private void loadClosetData(Bundle paramBundle)
  {
    if (!extractTargetFromBundle(paramBundle)) {
      extractTargetFromBundle(getArguments());
    }
    FragmentManager localFragmentManager = getActivity().getFragmentManager();
    NowTrainingQuestionsFetcherFragment localNowTrainingQuestionsFetcherFragment = (NowTrainingQuestionsFetcherFragment)localFragmentManager.findFragmentByTag("TRAINING_CLOSET_FETCHER");
    if (localNowTrainingQuestionsFetcherFragment == null)
    {
      localNowTrainingQuestionsFetcherFragment = new NowTrainingQuestionsFetcherFragment();
      localFragmentManager.beginTransaction().add(localNowTrainingQuestionsFetcherFragment, "TRAINING_CLOSET_FETCHER").commit();
    }
    localNowTrainingQuestionsFetcherFragment.setTargetFragment(this, 0);
    if (localNowTrainingQuestionsFetcherFragment.hasError()) {
      onError();
    }
    while (localNowTrainingQuestionsFetcherFragment.getCloset() == null) {
      return;
    }
    onClosetLoaded(localNowTrainingQuestionsFetcherFragment.getCloset(), localNowTrainingQuestionsFetcherFragment.getNumPlaces());
  }
  
  private void maybePrepareAutoCompleteFragment(List<ClosetListItem> paramList)
  {
    int i = 0;
    int j = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      ClosetListItem localClosetListItem = (ClosetListItem)localIterator.next();
      if (localClosetListItem.mType == 5) {
        i = 1;
      }
      if (localClosetListItem.mType == 4) {
        j = 1;
      }
    }
    if (i != 0) {
      prepareTickerFetcherFragment();
    }
    if (j != 0) {
      initializeSportsEntitiesProvider();
    }
  }
  
  private void onAddStockClick(int paramInt)
  {
    AddStockDialogFragment.newInstance(this, "stock_fetcher", Integer.valueOf(paramInt)).show(getActivity().getFragmentManager(), "add_stock_dialog_tag");
  }
  
  private void onAddTeamClick(final ClosetListItem paramClosetListItem)
  {
    if ((this.mSportsDataProvider == null) || (!this.mSportsDataProvider.isReady())) {
      return;
    }
    AddTeamDialogFragment local1 = new AddTeamDialogFragment()
    {
      public AdapterView.OnItemClickListener getOnItemClickListener()
      {
        new AdapterView.OnItemClickListener()
        {
          public void onItemClick(AdapterView<?> paramAnonymous2AdapterView, View paramAnonymous2View, int paramAnonymous2Int, long paramAnonymous2Long)
          {
            AddTeamDialogFragment.SportTeamPlayerWithName localSportTeamPlayerWithName = (AddTeamDialogFragment.SportTeamPlayerWithName)paramAnonymous2AdapterView.getAdapter().getItem(paramAnonymous2Int);
            ((NowTrainingQuestionsFragment)NowTrainingQuestionsFragment.1.this.getTargetFragment()).addNewSportsTeam(localSportTeamPlayerWithName, NowTrainingQuestionsFragment.1.this.val$listItem);
            NowTrainingQuestionsFragment.1.this.dismiss();
          }
        };
      }
    };
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("sports_entries_extra", this.mSportsDataProvider.getSportsEntities().toByteArray());
    local1.setArguments(localBundle);
    local1.setTargetFragment(this, 0);
    local1.show(getActivity().getFragmentManager(), "add_team_dialog_tag");
  }
  
  private void onCategoryClick(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup)
  {
    if (paramQuestionGroup.getGroupType() == 1)
    {
      showPlaces();
      return;
    }
    showCategoryInCloset(paramQuestionGroup);
  }
  
  private void onClosetUpdated()
  {
    updateRetainedFragmentClosetData(this.mCloset);
    this.mClosetSnapshot = this.mCloset.toByteArray();
  }
  
  private void onQuestionClick(View paramView, ClosetListItem paramClosetListItem)
  {
    QuestionKey localQuestionKey = new QuestionKey(paramClosetListItem.mTrainingQuestionNode.getQuestion().getQuestion());
    View localView = paramView.findViewById(2131297126);
    if ((localView == null) || (localView.getVisibility() != 0))
    {
      this.mExpandedQuestions.add(localQuestionKey);
      showEditView((ViewGroup)paramView, paramClosetListItem);
      return;
    }
    this.mExpandedQuestions.remove(localQuestionKey);
    hideEditView(paramView);
  }
  
  private void onQuestionsReady(Collection<TrainingQuestionNode> paramCollection)
  {
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator1 = paramCollection.iterator();
    while (localIterator1.hasNext())
    {
      TrainingQuestionNode localTrainingQuestionNode = (TrainingQuestionNode)localIterator1.next();
      localHashMap.put(new QuestionKey(localTrainingQuestionNode.getQuestion().getQuestion()), localTrainingQuestionNode);
    }
    ArrayList localArrayList = Lists.newArrayList();
    if (this.mQuestionGroupToDisplay == null)
    {
      int i = findIcebreakerQuestionGroupIndex(this.mCloset.getQuestionGroupList());
      if (i != -1) {
        addIcebreakerListItem(localArrayList, this.mCloset.getQuestionGroup(i), localHashMap);
      }
      int j = this.mCloset.getQuestionGroupCount();
      for (int k = 0; k < j; k++) {
        if (k != i) {
          localArrayList.add(buildCategoryListItem(this.mCloset.getQuestionGroup(k)));
        }
      }
    }
    Iterator localIterator2;
    if (isIcebreakerQuestionGroup(this.mQuestionGroupToDisplay))
    {
      addIcebreakerListItem(localArrayList, this.mQuestionGroupToDisplay, localHashMap);
      localIterator2 = this.mQuestionGroupToDisplay.getChildList().iterator();
    }
    for (;;)
    {
      if (!localIterator2.hasNext()) {
        break label301;
      }
      Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup = (Sidekick.TrainingModeClosetResponse.QuestionGroup)localIterator2.next();
      if (isLeafGroup(localQuestionGroup))
      {
        localArrayList.add(ClosetListItem.newSectionHeaderListItem(localQuestionGroup.getTitle()));
        if (isIcebreakerQuestionGroup(localQuestionGroup))
        {
          addIcebreakerListItem(localArrayList, localQuestionGroup, localHashMap);
          continue;
          addQuestionNodeListItems(localArrayList, this.mQuestionGroupToDisplay, localHashMap);
          break;
        }
        addQuestionNodeListItems(localArrayList, localQuestionGroup, localHashMap);
        continue;
      }
      localArrayList.add(buildCategoryListItem(localQuestionGroup));
    }
    label301:
    if (this.mAdapter == null)
    {
      this.mAdapter = new ClosetListAdapter(localArrayList);
      setListAdapter(this.mAdapter);
    }
    for (;;)
    {
      maybePrepareAutoCompleteFragment(localArrayList);
      return;
      this.mAdapter.setItems(localArrayList);
    }
  }
  
  private void prepareTickerFetcherFragment()
  {
    FragmentManager localFragmentManager = getFragmentManager();
    if ((AddStockDialogFragment.TickerFetcherFragment)localFragmentManager.findFragmentByTag("stock_fetcher") == null)
    {
      AddStockDialogFragment.TickerFetcherFragment localTickerFetcherFragment = new AddStockDialogFragment.TickerFetcherFragment();
      localFragmentManager.beginTransaction().add(localTickerFetcherFragment, "stock_fetcher").commit();
    }
  }
  
  private static void prependQuestionToNodeList(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, Sidekick.Question paramQuestion)
  {
    LinkedList localLinkedList = Lists.newLinkedList(paramQuestionGroup.getQuestionNodeList());
    localLinkedList.add(0, new Sidekick.QuestionNode().setQuestion(paramQuestion));
    setQuestionNodes(paramQuestionGroup, localLinkedList);
  }
  
  private static void setQuestionNodes(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, List<Sidekick.QuestionNode> paramList)
  {
    paramQuestionGroup.clearQuestionNode();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext()) {
      paramQuestionGroup.addQuestionNode((Sidekick.QuestionNode)localIterator.next());
    }
  }
  
  private void showCategoryInCloset(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup)
  {
    Bundle localBundle = new Bundle();
    if (this.mCloset != null)
    {
      int[] arrayOfInt = getQuestionGroupPath(paramQuestionGroup);
      if (arrayOfInt != null) {
        localBundle.putIntArray("TRAINING_CLOSET_CATEGORY", arrayOfInt);
      }
    }
    ((TrainingClosetActivity)getActivity()).showClosetScreen(localBundle);
  }
  
  private void showEditView(final ViewGroup paramViewGroup, final ClosetListItem paramClosetListItem)
  {
    final TrainingQuestion localTrainingQuestion = paramClosetListItem.mTrainingQuestionNode.getQuestion();
    ((ViewGroup)paramViewGroup.findViewById(2131297123)).setVisibility(8);
    ViewStub localViewStub = (ViewStub)paramViewGroup.findViewById(2131297125);
    View localView2;
    if (localViewStub != null)
    {
      Integer localInteger = TrainingQuestionViewHelper.getQuestionViewResourceId(localTrainingQuestion.getType());
      localQuestionView = null;
      if (localInteger != null)
      {
        localViewStub.setLayoutResource(localInteger.intValue());
        localView2 = localViewStub.inflate();
        TrainingQuestionViewHelper.setClosetTrainingQuestionBackground(localView2, getResources());
      }
    }
    for (QuestionView localQuestionView = (QuestionView)localView2;; localQuestionView = (QuestionView)paramViewGroup.findViewById(2131297126))
    {
      if (localQuestionView != null)
      {
        localQuestionView.setTrainingQuestion(localTrainingQuestion);
        localQuestionView.setListener(new QuestionViewListener()
        {
          public void onAnswerSelected(TrainingQuestion paramAnonymousTrainingQuestion, Sidekick.Question.Answer paramAnonymousAnswer, @Nullable Sidekick.Entry paramAnonymousEntry)
          {
            if (localTrainingQuestion == paramAnonymousTrainingQuestion) {}
            for (boolean bool = true;; bool = false)
            {
              Preconditions.checkArgument(bool);
              NowTrainingQuestionsFragment.this.mTrainingQuestionManager.setAnswer(paramAnonymousTrainingQuestion.getQuestion(), paramAnonymousAnswer, paramAnonymousEntry);
              paramAnonymousTrainingQuestion.updateAnswer(paramAnonymousAnswer);
              NowTrainingQuestionsFragment.this.updateRawQuestions(paramAnonymousTrainingQuestion, paramAnonymousAnswer);
              NowTrainingQuestionsFragment.this.mAdapter.onAnswerSelected(paramViewGroup, paramClosetListItem, paramAnonymousAnswer);
              return;
            }
          }
          
          public void onClientActionSelected(TrainingQuestion paramAnonymousTrainingQuestion, int paramAnonymousInt) {}
        });
        View localView1 = (View)localQuestionView;
        localView1.setVisibility(0);
        localView1.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            NowTrainingQuestionsFragment.this.onQuestionClick(paramViewGroup, paramClosetListItem);
          }
        });
      }
      return;
    }
  }
  
  private void showPlaces()
  {
    ((TrainingClosetActivity)getActivity()).showPlacesScreen();
  }
  
  private void updateRawQuestions(TrainingQuestion paramTrainingQuestion, Sidekick.Question.Answer paramAnswer)
  {
    Collection localCollection = findRawQuestions(paramTrainingQuestion);
    if (localCollection.isEmpty())
    {
      Log.e(TAG, "Could not find raw question: " + paramTrainingQuestion.getQuestion().getFingerprint());
      return;
    }
    Iterator localIterator = localCollection.iterator();
    while (localIterator.hasNext()) {
      ((Sidekick.Question)localIterator.next()).setAnswer(paramAnswer);
    }
    onClosetUpdated();
  }
  
  private void updateRetainedFragmentClosetData(Sidekick.TrainingModeClosetResponse paramTrainingModeClosetResponse)
  {
    NowTrainingQuestionsFetcherFragment localNowTrainingQuestionsFetcherFragment = (NowTrainingQuestionsFetcherFragment)getActivity().getFragmentManager().findFragmentByTag("TRAINING_CLOSET_FETCHER");
    if (localNowTrainingQuestionsFetcherFragment != null) {
      localNowTrainingQuestionsFetcherFragment.updateClosetData(paramTrainingModeClosetResponse);
    }
  }
  
  public void addStock(AddStockDialogFragment.StockDataWithName paramStockDataWithName, @Nullable Integer paramInteger)
  {
    TrainingQuestionNode localTrainingQuestionNode = ((ClosetListItem)getListAdapter().getItem(paramInteger.intValue())).mTrainingQuestionNode;
    TrainingQuestion localTrainingQuestion = createNewQuestionForStock(paramStockDataWithName, localTrainingQuestionNode);
    Sidekick.Question localQuestion = insertTrainingQuestionAlphabetical(localTrainingQuestionNode, localTrainingQuestion);
    this.mTrainingQuestionManager.setAnswer(localQuestion, localTrainingQuestion.getAnswer(), null);
  }
  
  public boolean consume(Collection<TrainingQuestionNode> paramCollection)
  {
    onQuestionsReady(paramCollection);
    return true;
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    getListView().setRecyclerListener(this);
    this.mSavedInstanceState = paramBundle;
    if ((paramBundle != null) && (paramBundle.containsKey("TRAINING_CLOSET_EXPANDED_QUESTIONS")))
    {
      Parcelable[] arrayOfParcelable = paramBundle.getParcelableArray("TRAINING_CLOSET_EXPANDED_QUESTIONS");
      this.mExpandedQuestions = Sets.newHashSet();
      for (int i = 0; i < arrayOfParcelable.length; i++) {
        this.mExpandedQuestions.add((QuestionKey)arrayOfParcelable[i]);
      }
    }
    loadClosetData(paramBundle);
  }
  
  public void onClosetLoaded(Sidekick.TrainingModeClosetResponse paramTrainingModeClosetResponse, int paramInt)
  {
    Preconditions.checkNotNull(paramTrainingModeClosetResponse);
    this.mQuestionGroupToDisplay = getQuestionGroupToDisplay(paramTrainingModeClosetResponse);
    if (this.mQuestionGroupToDisplay == null) {
      ((TrainingClosetActivity)getActivity()).showHeader(getString(2131362820), getString(2131362821));
    }
    byte[] arrayOfByte;
    for (;;)
    {
      arrayOfByte = paramTrainingModeClosetResponse.toByteArray();
      if ((this.mNumPlaces != paramInt) || (this.mClosetSnapshot == null) || (!Arrays.equals(this.mClosetSnapshot, arrayOfByte))) {
        break;
      }
      return;
      ((TrainingClosetActivity)getActivity()).showHeader(this.mQuestionGroupToDisplay.getTitle(), null);
    }
    this.mCloset = paramTrainingModeClosetResponse;
    this.mNumPlaces = paramInt;
    this.mClosetSnapshot = arrayOfByte;
    int i;
    if (this.mQuestionGroupToDisplay == null)
    {
      i = findIcebreakerQuestionGroupIndex(this.mCloset.getQuestionGroupList());
      localCollection = null;
      if (i == -1) {}
    }
    for (Collection localCollection = getQuestionNodesToDisplay(this.mCloset.getQuestionGroup(i)); localCollection != null; localCollection = getQuestionNodesToDisplay(this.mQuestionGroupToDisplay))
    {
      Consumers.addFutureConsumer(this.mTrainingQuestionManager.resolveQuestionsAsync(localCollection), this, this.mUiExecutor);
      return;
    }
    onQuestionsReady(Collections.emptyList());
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    SidekickInjector localSidekickInjector = VelvetServices.get().getSidekickInjector();
    this.mTrainingQuestionManager = localSidekickInjector.getTrainingQuestionManager();
    this.mUiExecutor = VelvetServices.get().getAsyncServices().getUiThreadExecutor();
    this.mExpandedQuestions = Sets.newHashSet();
    this.mCalendarProvider = localSidekickInjector.getCalendarDataProvider();
  }
  
  public void onError()
  {
    Toast.makeText(getActivity(), 2131362831, 0).show();
    onClosetLoaded(new Sidekick.TrainingModeClosetResponse(), 0);
  }
  
  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    ClosetListItem localClosetListItem = (ClosetListItem)getListAdapter().getItem(paramInt);
    switch (localClosetListItem.mType)
    {
    case 3: 
    case 6: 
    default: 
      return;
    case 2: 
      onCategoryClick(localClosetListItem.mQuestionGroup);
      return;
    case 0: 
    case 1: 
    case 7: 
      onQuestionClick(paramView, localClosetListItem);
      return;
    case 4: 
      onAddTeamClick(localClosetListItem);
      return;
    }
    onAddStockClick(paramInt);
  }
  
  public void onMovedToScrapHeap(View paramView)
  {
    if ((paramView instanceof IcebreakerSectionView)) {
      ((IcebreakerSectionView)paramView).setAdapter(null);
    }
  }
  
  public void onResume()
  {
    super.onResume();
    loadClosetData(null);
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    if (this.mQuestionGroupPath != null) {
      paramBundle.putIntArray("TRAINING_CLOSET_CATEGORY", this.mQuestionGroupPath);
    }
    if (this.mQuestionToDisplay != null) {
      paramBundle.putByteArray("com.google.android.search.core.preferences.ARGUMENT_QUESTION", this.mQuestionToDisplay.toByteArray());
    }
    Parcelable[] arrayOfParcelable = new Parcelable[this.mExpandedQuestions.size()];
    int i = 0;
    Iterator localIterator = this.mExpandedQuestions.iterator();
    while (localIterator.hasNext())
    {
      arrayOfParcelable[i] = ((QuestionKey)localIterator.next());
      i++;
    }
    paramBundle.putParcelableArray("TRAINING_CLOSET_EXPANDED_QUESTIONS", arrayOfParcelable);
    ListAdapter localListAdapter = getListAdapter();
    if (localListAdapter != null)
    {
      int j = localListAdapter.getCount();
      for (int k = 0; k < j; k++)
      {
        ClosetListItem localClosetListItem = (ClosetListItem)localListAdapter.getItem(k);
        if (localClosetListItem.mType == 6) {
          paramBundle.putParcelable("TRAINING_IB_" + k, localClosetListItem.mIcebreakerAdapter);
        }
      }
    }
  }
  
  private class ClosetListAdapter
    extends BaseAdapter
    implements Predicate<NowTrainingQuestionsFragment.ClosetListItem>
  {
    private ImmutableList<NowTrainingQuestionsFragment.ClosetListItem> mItems;
    private List<NowTrainingQuestionsFragment.ClosetListItem> mRawItems;
    
    public ClosetListAdapter()
    {
      Object localObject;
      this.mRawItems = localObject;
      this.mItems = ImmutableList.copyOf(Iterables.filter(this.mRawItems, this));
    }
    
    private View buildAddButton(View paramView, int paramInt)
    {
      if (paramView == null) {
        paramView = NowTrainingQuestionsFragment.this.getActivity().getLayoutInflater().inflate(2130968862, null);
      }
      TextView localTextView = (TextView)paramView;
      localTextView.setText(NowTrainingQuestionsFragment.this.getResources().getString(paramInt));
      return localTextView;
    }
    
    private View buildCategoryView(View paramView, NowTrainingQuestionsFragment.ClosetListItem paramClosetListItem)
    {
      if (paramView == null) {
        paramView = NowTrainingQuestionsFragment.this.getActivity().getLayoutInflater().inflate(2130968863, null);
      }
      Sidekick.TrainingModeClosetResponse.QuestionGroup localQuestionGroup = NowTrainingQuestionsFragment.ClosetListItem.access$200(paramClosetListItem);
      ((TextView)paramView.findViewById(16908310)).setText(localQuestionGroup.getTitle());
      TextView localTextView = (TextView)paramView.findViewById(2131297122);
      if (NowTrainingQuestionsFragment.ClosetListItem.access$1100(paramClosetListItem) > 0)
      {
        localTextView.setText(String.valueOf(NowTrainingQuestionsFragment.ClosetListItem.access$1100(paramClosetListItem)));
        localTextView.setVisibility(0);
        return paramView;
      }
      localTextView.setVisibility(4);
      return paramView;
    }
    
    private View buildIcebreakerSectionView(View paramView, NowTrainingQuestionsFragment.ClosetListItem paramClosetListItem)
    {
      if (paramView == null) {
        paramView = new IcebreakerSectionView(NowTrainingQuestionsFragment.this.getActivity());
      }
      IcebreakerSectionView localIcebreakerSectionView = (IcebreakerSectionView)paramView;
      localIcebreakerSectionView.setListener(new IcebreakerSectionView.Listener()
      {
        public void onIcebreakerAnswerSelected(TrainingQuestion paramAnonymousTrainingQuestion, Sidekick.Question.Answer paramAnonymousAnswer)
        {
          NowTrainingQuestionsFragment.this.addIcebreakerQuestionToQuestionGroup(paramAnonymousTrainingQuestion, paramAnonymousAnswer);
          NowTrainingQuestionsFragment.this.updateRawQuestions(paramAnonymousTrainingQuestion, paramAnonymousAnswer);
        }
        
        public void onMoreButtonClicked() {}
      });
      localIcebreakerSectionView.setAdapter(NowTrainingQuestionsFragment.ClosetListItem.access$100(paramClosetListItem));
      return paramView;
    }
    
    private View buildInterestView(View paramView, NowTrainingQuestionsFragment.ClosetListItem paramClosetListItem)
    {
      TrainingQuestion localTrainingQuestion = NowTrainingQuestionsFragment.ClosetListItem.access$300(paramClosetListItem).getQuestion();
      Sidekick.Question.Entity localEntity = localTrainingQuestion.getPrimaryEntity();
      if (localEntity == null)
      {
        Log.w(NowTrainingQuestionsFragment.TAG, "INTERESTED_IN question without entity: " + localTrainingQuestion.getQuestion().getFingerprint());
        return buildQuestionView(paramView, paramClosetListItem);
      }
      if (paramView == null) {
        paramView = NowTrainingQuestionsFragment.this.getActivity().getLayoutInflater().inflate(2130968866, null);
      }
      ((TextView)paramView.findViewById(2131297124)).setText(localEntity.getValue());
      ImageView localImageView = (ImageView)paramView.findViewById(16908294);
      if (localEntity.hasSportTeamPlayer())
      {
        setSportsIcon(localImageView, localEntity.getSportTeamPlayer());
        if (!NowTrainingQuestionsFragment.this.mExpandedQuestions.contains(new QuestionKey(localTrainingQuestion.getQuestion()))) {
          break label176;
        }
        NowTrainingQuestionsFragment.this.showEditView((ViewGroup)paramView, paramClosetListItem);
      }
      for (;;)
      {
        return paramView;
        localImageView.setVisibility(8);
        break;
        label176:
        NowTrainingQuestionsFragment.hideEditView(paramView);
      }
    }
    
    private View buildQuestionView(View paramView, NowTrainingQuestionsFragment.ClosetListItem paramClosetListItem)
    {
      if (paramView == null) {
        paramView = NowTrainingQuestionsFragment.this.getActivity().getLayoutInflater().inflate(2130968867, null);
      }
      updateDisplayView(paramView, paramClosetListItem);
      TrainingQuestion localTrainingQuestion = NowTrainingQuestionsFragment.ClosetListItem.access$300(paramClosetListItem).getQuestion();
      if (NowTrainingQuestionsFragment.this.mExpandedQuestions.contains(new QuestionKey(localTrainingQuestion.getQuestion())))
      {
        NowTrainingQuestionsFragment.this.showEditView((ViewGroup)paramView, paramClosetListItem);
        return paramView;
      }
      NowTrainingQuestionsFragment.hideEditView(paramView);
      return paramView;
    }
    
    private View buildSectionHeaderView(View paramView, String paramString)
    {
      if (paramView == null) {
        paramView = NowTrainingQuestionsFragment.this.getActivity().getLayoutInflater().inflate(2130968868, null);
      }
      ((TextView)paramView.findViewById(16908310)).setText(paramString);
      return paramView;
    }
    
    private void setSportsIcon(ImageView paramImageView, Sidekick.SidekickConfiguration.Sports.SportTeamPlayer paramSportTeamPlayer)
    {
      int i = TrainingQuestionViewHelper.getSportIconResourceId(paramSportTeamPlayer.getSport());
      if (i != 0)
      {
        paramImageView.setImageResource(i);
        paramImageView.setVisibility(0);
        return;
      }
      Log.e(NowTrainingQuestionsFragment.TAG, "Icon missing for sport: " + paramSportTeamPlayer.getSport());
      paramImageView.setVisibility(4);
    }
    
    private void updateDisplayView(View paramView, NowTrainingQuestionsFragment.ClosetListItem paramClosetListItem)
    {
      TextView localTextView1 = (TextView)paramView.findViewById(2131297127);
      TextView localTextView2 = (TextView)paramView.findViewById(2131297128);
      TrainingQuestion localTrainingQuestion = NowTrainingQuestionsFragment.ClosetListItem.access$300(paramClosetListItem).getQuestion();
      Preconditions.checkNotNull(localTextView1);
      Preconditions.checkNotNull(localTextView2);
      Preconditions.checkNotNull(localTrainingQuestion);
      localTextView1.setText(localTrainingQuestion.getQuestionString());
      String str = "";
      Sidekick.Question.Answer localAnswer = localTrainingQuestion.getAnswer();
      if (localAnswer != null) {
        switch (localTrainingQuestion.getType())
        {
        }
      }
      for (;;)
      {
        localTextView2.setText(str);
        return;
        if (localAnswer.getYesNoAnswer()) {}
        for (str = NowTrainingQuestionsFragment.this.getResources().getString(2131362825);; str = NowTrainingQuestionsFragment.this.getResources().getString(2131362826)) {
          break;
        }
        str = localTrainingQuestion.getMultipleChoiceOption(localAnswer.getMultipleChoiceAnswer()).getDisplayString();
      }
    }
    
    public boolean apply(NowTrainingQuestionsFragment.ClosetListItem paramClosetListItem)
    {
      return paramClosetListItem.shouldDisplay();
    }
    
    public int getCount()
    {
      return this.mItems.size();
    }
    
    public Object getItem(int paramInt)
    {
      return this.mItems.get(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public int getItemViewType(int paramInt)
    {
      return NowTrainingQuestionsFragment.ClosetListItem.access$000((NowTrainingQuestionsFragment.ClosetListItem)this.mItems.get(paramInt));
    }
    
    public List<NowTrainingQuestionsFragment.ClosetListItem> getRawItems()
    {
      return this.mRawItems;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      NowTrainingQuestionsFragment.ClosetListItem localClosetListItem = (NowTrainingQuestionsFragment.ClosetListItem)getItem(paramInt);
      switch (NowTrainingQuestionsFragment.ClosetListItem.access$000(localClosetListItem))
      {
      default: 
        return null;
      case 0: 
      case 1: 
        return buildQuestionView(paramView, localClosetListItem);
      case 2: 
        return buildCategoryView(paramView, localClosetListItem);
      case 3: 
        return buildSectionHeaderView(paramView, NowTrainingQuestionsFragment.ClosetListItem.access$1400(localClosetListItem));
      case 4: 
        return buildAddButton(paramView, 2131362827);
      case 5: 
        return buildAddButton(paramView, 2131362828);
      case 6: 
        return buildIcebreakerSectionView(paramView, localClosetListItem);
      }
      return buildInterestView(paramView, localClosetListItem);
    }
    
    public int getViewTypeCount()
    {
      return 8;
    }
    
    public void onAnswerSelected(View paramView, NowTrainingQuestionsFragment.ClosetListItem paramClosetListItem, Sidekick.Question.Answer paramAnswer)
    {
      QuestionKey localQuestionKey = new QuestionKey(NowTrainingQuestionsFragment.ClosetListItem.access$300(paramClosetListItem).getQuestion().getQuestion());
      if (NowTrainingQuestionsFragment.ClosetListItem.access$000(paramClosetListItem) == 7)
      {
        NowTrainingQuestionsFragment.hideEditView(paramView);
        NowTrainingQuestionsFragment.this.mExpandedQuestions.remove(localQuestionKey);
        if ((paramAnswer.hasYesNoAnswer()) && (!paramAnswer.getYesNoAnswer()))
        {
          this.mRawItems.remove(paramClosetListItem);
          this.mItems = ImmutableList.copyOf(Iterables.filter(this.mRawItems, this));
          notifyDataSetChanged();
        }
      }
      while ((NowTrainingQuestionsFragment.ClosetListItem.access$000(paramClosetListItem) != 1) && (NowTrainingQuestionsFragment.ClosetListItem.access$000(paramClosetListItem) != 0)) {
        return;
      }
      updateDisplayView(paramView.findViewById(2131297123), paramClosetListItem);
      NowTrainingQuestionsFragment.hideEditView(paramView);
      NowTrainingQuestionsFragment.this.mExpandedQuestions.remove(localQuestionKey);
    }
    
    public void setItems(List<NowTrainingQuestionsFragment.ClosetListItem> paramList)
    {
      this.mRawItems = paramList;
      this.mItems = ImmutableList.copyOf(Iterables.filter(this.mRawItems, this));
      notifyDataSetChanged();
    }
  }
  
  private static class ClosetListItem
  {
    private int mAnsweredQuestionCount;
    @Nullable
    private final IcebreakerSectionAdapter mIcebreakerAdapter;
    @Nullable
    private final Sidekick.TrainingModeClosetResponse.QuestionGroup mQuestionGroup;
    @Nullable
    private final String mString;
    @Nullable
    private final TrainingQuestionNode mTrainingQuestionNode;
    private final int mType;
    
    private ClosetListItem(int paramInt1, @Nullable String paramString, @Nullable TrainingQuestionNode paramTrainingQuestionNode, @Nullable IcebreakerSectionAdapter paramIcebreakerSectionAdapter, @Nullable Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, int paramInt2)
    {
      this.mType = paramInt1;
      this.mString = paramString;
      this.mTrainingQuestionNode = paramTrainingQuestionNode;
      this.mIcebreakerAdapter = paramIcebreakerSectionAdapter;
      this.mQuestionGroup = paramQuestionGroup;
      this.mAnsweredQuestionCount = paramInt2;
    }
    
    public static ClosetListItem newIcebreakerSectionListItem(IcebreakerSectionAdapter paramIcebreakerSectionAdapter)
    {
      return new ClosetListItem(6, null, null, paramIcebreakerSectionAdapter, null, 0);
    }
    
    public static ClosetListItem newQuestionCategoryListItem(Sidekick.TrainingModeClosetResponse.QuestionGroup paramQuestionGroup, int paramInt)
    {
      return new ClosetListItem(2, null, null, null, paramQuestionGroup, paramInt);
    }
    
    public static ClosetListItem newQuestionListItem(int paramInt, TrainingQuestionNode paramTrainingQuestionNode)
    {
      return new ClosetListItem(paramInt, null, paramTrainingQuestionNode, null, null, 0);
    }
    
    public static ClosetListItem newSectionHeaderListItem(String paramString)
    {
      return new ClosetListItem(3, paramString, null, null, null, 0);
    }
    
    public boolean shouldDisplay()
    {
      boolean bool1;
      if (this.mType == 7)
      {
        Sidekick.Question.Answer localAnswer = this.mTrainingQuestionNode.getQuestion().getAnswer();
        if (localAnswer != null)
        {
          boolean bool2 = localAnswer.getYesNoAnswer();
          bool1 = false;
          if (bool2 != true) {}
        }
        else
        {
          bool1 = true;
        }
      }
      int i;
      do
      {
        return bool1;
        if (this.mType != 2) {
          break label87;
        }
        if ((this.mQuestionGroup.getGroupType() == 1) || (this.mQuestionGroup.getQuestionNodeCount() > 0)) {
          break;
        }
        i = this.mQuestionGroup.getChildCount();
        bool1 = false;
      } while (i <= 0);
      return true;
      label87:
      return true;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.NowTrainingQuestionsFragment
 * JD-Core Version:    0.7.0.1
 */