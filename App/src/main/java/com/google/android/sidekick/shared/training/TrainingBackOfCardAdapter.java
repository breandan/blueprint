package com.google.android.sidekick.shared.training;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.Consumers;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion.ActionQuestionOption;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.android.sidekick.shared.ui.CardBackTraining;
import com.google.android.sidekick.shared.ui.CardBackTraining.Listener;
import com.google.android.sidekick.shared.util.ActionLauncherUtil;
import com.google.android.sidekick.shared.util.IntentDispatcherUtil;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

public class TrainingBackOfCardAdapter
  implements Consumer<Collection<TrainingQuestionNode>>, BackOfCardAdapter, QuestionViewListener, CardBackTraining.Listener
{
  private static final String TAG = Tag.getTag(TrainingBackOfCardAdapter.class);
  private SparseBooleanArray mAnsweredQuestions;
  private CardBackTraining mBackOfCardView;
  private PredictiveCardContainer mCardContainer;
  private Context mContext;
  private int mCurrentQuestion = -1;
  private final EntryCardViewAdapter mEntryAdapter;
  private Runnable mHideBackOfCardRunnable;
  private LayoutInflater mInflater;
  private int mPendingActionOnPause = -1;
  private boolean mResumed;
  @Nullable
  private List<TrainingQuestionNode> mTrainingQuestions;
  private final ScheduledSingleThreadedExecutor mUiExecutor;
  
  public TrainingBackOfCardAdapter(EntryCardViewAdapter paramEntryCardViewAdapter, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    this.mEntryAdapter = paramEntryCardViewAdapter;
    this.mUiExecutor = paramScheduledSingleThreadedExecutor;
  }
  
  private void cancelHideBackOfCardRunnable()
  {
    if (this.mHideBackOfCardRunnable != null)
    {
      this.mUiExecutor.cancelExecute(this.mHideBackOfCardRunnable);
      this.mHideBackOfCardRunnable = null;
    }
  }
  
  private View createActionQuestionView(final TrainingQuestion paramTrainingQuestion)
  {
    View localView = this.mInflater.inflate(2130968870, this.mBackOfCardView, false);
    TrainingQuestionViewHelper.setQuestionAndJustification(localView, paramTrainingQuestion, this.mEntryAdapter.getEntry());
    ViewGroup localViewGroup = (ViewGroup)localView.findViewById(2131297131);
    Iterator localIterator = paramTrainingQuestion.getActionQuestionOptions().iterator();
    while (localIterator.hasNext())
    {
      final TrainingQuestion.ActionQuestionOption localActionQuestionOption = (TrainingQuestion.ActionQuestionOption)localIterator.next();
      Button localButton = (Button)this.mInflater.inflate(2130968871, localViewGroup, false);
      String str = localActionQuestionOption.getAction().getDisplayMessage();
      if (str != null) {
        localButton.setText(str.toUpperCase(Locale.getDefault()));
      }
      int i = TrainingQuestionViewHelper.getIconResourceId(localActionQuestionOption.getIconType());
      if (i != 0) {
        LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localButton, 0, i, 0, 0);
      }
      localViewGroup.addView(localButton);
      localButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          TrainingBackOfCardAdapter.this.mCardContainer.sendTrainingAction(TrainingBackOfCardAdapter.this.mEntryAdapter.getEntry(), paramTrainingQuestion.getQuestion(), localActionQuestionOption.getAction());
          if (localActionQuestionOption.getClientAction() != null)
          {
            TrainingBackOfCardAdapter.this.handleClientAction(paramTrainingQuestion, localActionQuestionOption.getClientAction().intValue());
            return;
          }
          TrainingBackOfCardAdapter.this.showNextQuestionOrHideIfLast();
        }
      });
    }
    return localView;
  }
  
  private View createAddButtonQuestionView(final TrainingQuestion paramTrainingQuestion)
  {
    boolean bool;
    ViewGroup localViewGroup;
    Button localButton;
    if (paramTrainingQuestion.getFulfillAction() != null)
    {
      bool = true;
      Preconditions.checkArgument(bool, "Question does not have fulfill action");
      localViewGroup = (ViewGroup)this.mInflater.inflate(2130968872, this.mBackOfCardView, false);
      localButton = (Button)localViewGroup.findViewById(2131296449);
      switch (paramTrainingQuestion.getType())
      {
      }
    }
    for (;;)
    {
      localButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          TrainingBackOfCardAdapter.this.handleClientAction(paramTrainingQuestion, paramTrainingQuestion.getFulfillAction().intValue());
        }
      });
      return localViewGroup;
      bool = false;
      break;
      localButton.setText(this.mContext.getResources().getString(2131362827).toUpperCase(Locale.getDefault()));
      continue;
      localButton.setText(this.mContext.getResources().getString(2131362828).toUpperCase(Locale.getDefault()));
    }
  }
  
  @Nullable
  private View createQuestionView(TrainingQuestionNode paramTrainingQuestionNode)
  {
    TrainingQuestion localTrainingQuestion = paramTrainingQuestionNode.getQuestion();
    switch (localTrainingQuestion.getType())
    {
    case 0: 
    case 3: 
    default: 
      throw new IllegalStateException("Don't know how to create view for question type: " + localTrainingQuestion.getType());
    case 1: 
    case 2: 
    case 5: 
      return createStandardQuestionView(localTrainingQuestion);
    case 4: 
      return createActionQuestionView(localTrainingQuestion);
    }
    return createAddButtonQuestionView(localTrainingQuestion);
  }
  
  private View createStandardQuestionView(TrainingQuestion paramTrainingQuestion)
  {
    QuestionView localQuestionView = (QuestionView)this.mInflater.inflate(TrainingQuestionViewHelper.getQuestionViewResourceId(paramTrainingQuestion.getType()).intValue(), null);
    localQuestionView.setEntry(this.mEntryAdapter.getEntry());
    localQuestionView.setTrainingQuestion(paramTrainingQuestion);
    localQuestionView.setListener(this);
    return (View)localQuestionView;
  }
  
  private void dispatchPlaceActionIntent(int paramInt)
  {
    Sidekick.Entry localEntry = this.mEntryAdapter.getEntry();
    Sidekick.Action localAction1 = ProtoUtils.findAction(localEntry, paramInt, new int[0]);
    Sidekick.Action localAction2 = null;
    if (paramInt == 5) {
      localAction2 = ProtoUtils.findAction(localEntry, 14, new int[0]);
    }
    if (localAction1 != null)
    {
      Intent localIntent = ActionLauncherUtil.createActionLauncherIntent(this.mContext);
      localIntent.putExtra("action_type", paramInt);
      ProtoUtils.putProtoExtra(localIntent, "entry", localEntry);
      ProtoUtils.putProtoExtra(localIntent, "action", localAction1);
      if (localAction2 != null) {
        ProtoUtils.putProtoExtra(localIntent, "delete_action", localAction2);
      }
      this.mContext.startActivity(localIntent);
      return;
    }
    Log.e(TAG, "Missing action type: " + paramInt);
  }
  
  private void forgetPlace()
  {
    dispatchPlaceActionIntent(14);
  }
  
  private int getNext()
  {
    boolean bool;
    int k;
    int j;
    if (this.mCurrentQuestion == -1)
    {
      bool = false;
      if (!bool) {
        k = this.mTrainingQuestions.size();
      }
    }
    else
    {
      for (j = 1 + this.mCurrentQuestion;; j++)
      {
        if (j == k) {
          break label80;
        }
        if ((isQuestionValid(j)) && (!this.mAnsweredQuestions.get(j)))
        {
          return j;
          bool = this.mAnsweredQuestions.get(this.mCurrentQuestion);
          break;
        }
      }
    }
    label80:
    int i = 0;
    if (bool) {}
    for (i = 1 + this.mAnsweredQuestions.indexOfKey(this.mCurrentQuestion);; i++)
    {
      if (i == this.mAnsweredQuestions.size()) {
        break label134;
      }
      j = this.mAnsweredQuestions.keyAt(i);
      if (isQuestionValid(j)) {
        break;
      }
    }
    label134:
    return -1;
  }
  
  private int getPrevious()
  {
    int j;
    if (this.mCurrentQuestion == -1)
    {
      j = -1;
      return j;
    }
    boolean bool = this.mAnsweredQuestions.get(this.mCurrentQuestion);
    if (bool) {
      for (int k = -1 + this.mAnsweredQuestions.indexOfKey(this.mCurrentQuestion);; k--)
      {
        if (k == -1) {
          break label73;
        }
        j = this.mAnsweredQuestions.keyAt(k);
        if (isQuestionValid(j)) {
          break;
        }
      }
    }
    label73:
    if (bool) {}
    for (int i = this.mTrainingQuestions.size();; i = this.mCurrentQuestion)
    {
      for (j = i - 1; (j != -1) && ((!isQuestionValid(j)) || (this.mAnsweredQuestions.get(j))); j--) {}
      break;
    }
  }
  
  private void handleClientAction(TrainingQuestion paramTrainingQuestion, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      Log.w(TAG, "Unrecognized client action: " + paramInt);
      return;
    case 1: 
      this.mPendingActionOnPause = paramInt;
      this.mCardContainer.toggleBackOfCard(this.mEntryAdapter);
      return;
    case 4: 
      this.mPendingActionOnPause = paramInt;
      showNextQuestionOrHideIfLast();
      return;
    case 2: 
    case 3: 
      IntentDispatcherUtil.dispatchTrainingClosetIntent(this.mContext, paramTrainingQuestion.getQuestion());
      return;
    case 5: 
      IntentDispatcherUtil.dispatchIntent(this.mContext, "com.google.android.googlequicksearchbox.MY_PLACES");
      return;
    case 6: 
      IntentDispatcherUtil.dispatchIntent(this.mContext, "com.google.android.googlequicksearchbox.TRAFFIC_CARD_SETTINGS");
      return;
    case 7: 
      renamePlace();
      return;
    }
    forgetPlace();
  }
  
  private void init()
  {
    Preconditions.checkState(this.mResumed);
    if (this.mTrainingQuestions != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      if ((this.mAnsweredQuestions != null) || (this.mCurrentQuestion != -1)) {
        break label123;
      }
      this.mAnsweredQuestions = new SparseBooleanArray();
      for (int i = 0; i < this.mTrainingQuestions.size(); i++)
      {
        TrainingQuestion localTrainingQuestion = ((TrainingQuestionNode)this.mTrainingQuestions.get(i)).getQuestion();
        if ((localTrainingQuestion.getAnswer() != null) || (!localTrainingQuestion.isAnswerable())) {
          this.mAnsweredQuestions.put(i, true);
        }
      }
    }
    this.mCurrentQuestion = getNext();
    label123:
    if (this.mCurrentQuestion != -1) {
      showCurrentQuestion(0);
    }
    if (this.mBackOfCardView.getVisibility() != 0)
    {
      this.mBackOfCardView.setVisibility(0);
      Animation localAnimation = AnimationUtils.loadAnimation(this.mContext, 2131034115);
      this.mBackOfCardView.startAnimation(localAnimation);
    }
  }
  
  public static boolean isEnabledFor(Sidekick.Entry paramEntry)
  {
    return paramEntry.getTrainingQuestionNodeCount() > 0;
  }
  
  private boolean isQuestionValid(int paramInt)
  {
    TrainingQuestionNode localTrainingQuestionNode = (TrainingQuestionNode)this.mTrainingQuestions.get(paramInt);
    return (isSupportedType(localTrainingQuestionNode.getQuestion().getType())) && (localTrainingQuestionNode.isVisible());
  }
  
  private static boolean isSupportedType(int paramInt)
  {
    switch (paramInt)
    {
    case 0: 
    case 3: 
    default: 
      return false;
    }
    return true;
  }
  
  private void renamePlace()
  {
    dispatchPlaceActionIntent(5);
  }
  
  private void showCurrentQuestion(int paramInt)
  {
    boolean bool1 = true;
    CardBackTraining localCardBackTraining = this.mBackOfCardView;
    View localView = createQuestionView((TrainingQuestionNode)this.mTrainingQuestions.get(this.mCurrentQuestion));
    boolean bool2;
    if (getNext() != -1)
    {
      bool2 = bool1;
      if (getPrevious() == -1) {
        break label65;
      }
    }
    for (;;)
    {
      localCardBackTraining.setQuestionView(localView, bool2, bool1, paramInt);
      return;
      bool2 = false;
      break;
      label65:
      bool1 = false;
    }
  }
  
  private void showNextQuestionOrHideIfLast()
  {
    if (getNext() != -1)
    {
      this.mBackOfCardView.showNextQuestionAfterDelay(250);
      return;
    }
    cancelHideBackOfCardRunnable();
    this.mHideBackOfCardRunnable = new Runnable()
    {
      public void run()
      {
        TrainingBackOfCardAdapter.this.mCardContainer.toggleBackOfCard(TrainingBackOfCardAdapter.this.mEntryAdapter);
      }
    };
    this.mUiExecutor.executeDelayed(this.mHideBackOfCardRunnable, 250L);
  }
  
  public void commitFeedback(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    if (this.mPendingActionOnPause != 4) {
      paramPredictiveCardContainer.sendPendingTrainingAnswers();
    }
  }
  
  public boolean consume(Collection<TrainingQuestionNode> paramCollection)
  {
    this.mTrainingQuestions = TrainingQuestionNode.flatten(paramCollection);
    if (this.mResumed) {
      init();
    }
    return true;
  }
  
  public void onAnswerSelected(TrainingQuestion paramTrainingQuestion, Sidekick.Question.Answer paramAnswer, @Nullable Sidekick.Entry paramEntry)
  {
    this.mCardContainer.setTrainingAnswer(paramTrainingQuestion.getQuestion(), paramAnswer, paramEntry);
    ((TrainingQuestionNode)this.mTrainingQuestions.get(this.mCurrentQuestion)).getQuestion().updateAnswer(paramAnswer);
    this.mCardContainer.pulseTrainingIcon();
    Integer localInteger = paramTrainingQuestion.getClientAction(paramAnswer);
    if (localInteger != null)
    {
      handleClientAction(paramTrainingQuestion, localInteger.intValue());
      return;
    }
    showNextQuestionOrHideIfLast();
  }
  
  public void onClientActionSelected(TrainingQuestion paramTrainingQuestion, int paramInt)
  {
    handleClientAction(paramTrainingQuestion, paramInt);
  }
  
  public void onNext()
  {
    int i = getNext();
    if (i != -1)
    {
      this.mCurrentQuestion = i;
      showCurrentQuestion(1);
    }
  }
  
  public void onPause()
  {
    this.mResumed = false;
    if (this.mPendingActionOnPause != -1) {
      switch (this.mPendingActionOnPause)
      {
      }
    }
    for (;;)
    {
      this.mPendingActionOnPause = -1;
      return;
      this.mCardContainer.dismissEntry(this.mEntryAdapter.getEntry(), false);
      continue;
      this.mCardContainer.refreshEntries();
    }
  }
  
  public void onPrevious()
  {
    int i = getPrevious();
    if (i != -1)
    {
      this.mCurrentQuestion = i;
      showCurrentQuestion(2);
    }
  }
  
  public void onResume()
  {
    this.mResumed = true;
    if (this.mTrainingQuestions != null) {
      init();
    }
  }
  
  public void populateBackOfCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater)
  {
    this.mContext = paramContext;
    this.mBackOfCardView = ((CardBackTraining)paramViewGroup);
    this.mBackOfCardView.setVisibility(8);
    this.mCardContainer = paramPredictiveCardContainer;
    this.mInflater = paramLayoutInflater;
    Consumers.addFutureConsumer(paramPredictiveCardContainer.resolveTrainingQuestionsAsync(this.mEntryAdapter.getEntry().getTrainingQuestionNodeList()), this, this.mUiExecutor);
    this.mBackOfCardView.setListener(this);
  }
  
  public void restoreViewState(Bundle paramBundle)
  {
    Preconditions.checkNotNull(paramBundle);
    int[] arrayOfInt = paramBundle.getIntArray("answered_questions");
    this.mAnsweredQuestions = new SparseBooleanArray(arrayOfInt.length);
    for (int i = 0; i < arrayOfInt.length; i++) {
      this.mAnsweredQuestions.append(arrayOfInt[i], true);
    }
    this.mCurrentQuestion = paramBundle.getInt("current_question");
  }
  
  public Bundle saveViewState()
  {
    Bundle localBundle = new Bundle();
    int[] arrayOfInt = new int[this.mAnsweredQuestions.size()];
    for (int i = 0; i < this.mAnsweredQuestions.size(); i++) {
      arrayOfInt[i] = this.mAnsweredQuestions.keyAt(i);
    }
    localBundle.putIntArray("answered_questions", arrayOfInt);
    localBundle.putInt("current_question", this.mCurrentQuestion);
    return localBundle;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.training.TrainingBackOfCardAdapter
 * JD-Core Version:    0.7.0.1
 */