package com.google.android.sidekick.main.training;

import android.content.Context;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.android.sidekick.shared.training.QuestionKey;
import com.google.android.sidekick.shared.training.QuestionView;
import com.google.android.sidekick.shared.training.QuestionViewListener;
import com.google.android.sidekick.shared.training.TrainingQuestionViewHelper;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class IcebreakerSectionView
  extends LinearLayout
  implements View.OnClickListener, IcebreakerSectionAdapter.Observer
{
  private static final String TAG = Tag.getTag(IcebreakerSectionView.class);
  @Nullable
  private IcebreakerSectionAdapter mAdapter;
  private final List<View> mAnimatingViews = Lists.newLinkedList();
  private SparseArray<QuestionKey> mCurrentlyDisplayedQuestions;
  private LayoutInflater mLayoutInflater;
  @Nullable
  private Listener mListener;
  private Button mMoreButton;
  private List<Pair<TrainingQuestion, Sidekick.Question.Answer>> mPendingAnswers = Lists.newLinkedList();
  private ViewGroup mQuestionContainer;
  private int mQuestionContainerBottomMargin;
  
  public IcebreakerSectionView(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  private void addQuestionView(int paramInt, View paramView, boolean paramBoolean)
  {
    ViewGroup localViewGroup = (ViewGroup)this.mLayoutInflater.inflate(2130968865, null);
    localViewGroup.setTag("TAG_" + paramInt);
    localViewGroup.addView(paramView);
    this.mQuestionContainer.addView(localViewGroup);
    if (paramBoolean)
    {
      this.mAnimatingViews.add(paramView);
      Animation localAnimation = AnimationUtils.loadAnimation(getContext(), 2131034117);
      localAnimation.setAnimationListener(AnimationFinalizer.newAddingFinalizer(this, paramView));
      paramView.startAnimation(localAnimation);
    }
  }
  
  private View buildQuestionView(final int paramInt, final TrainingQuestionNode paramTrainingQuestionNode)
  {
    TrainingQuestion localTrainingQuestion = paramTrainingQuestionNode.getQuestion();
    Integer localInteger = TrainingQuestionViewHelper.getQuestionViewResourceId(localTrainingQuestion.getType());
    Preconditions.checkNotNull(localInteger, "Can't build view for invalid question type: " + localTrainingQuestion.getType());
    View localView = this.mLayoutInflater.inflate(localInteger.intValue(), null);
    TrainingQuestionViewHelper.setClosetTrainingQuestionBackground(localView, getResources());
    QuestionView localQuestionView = (QuestionView)localView;
    localQuestionView.setTrainingQuestion(localTrainingQuestion);
    localQuestionView.setListener(new QuestionViewListener()
    {
      public void onAnswerSelected(TrainingQuestion paramAnonymousTrainingQuestion, Sidekick.Question.Answer paramAnonymousAnswer, @Nullable Sidekick.Entry paramAnonymousEntry)
      {
        IcebreakerSectionView.this.mAdapter.handleAnswerSelected(paramInt, paramTrainingQuestionNode, paramAnonymousAnswer);
        if (IcebreakerSectionView.this.mListener != null) {
          IcebreakerSectionView.this.mPendingAnswers.add(Pair.create(paramAnonymousTrainingQuestion, paramAnonymousAnswer));
        }
      }
      
      public void onClientActionSelected(TrainingQuestion paramAnonymousTrainingQuestion, int paramAnonymousInt) {}
    });
    return localView;
  }
  
  private int getViewIndex(int paramInt)
  {
    return this.mQuestionContainer.indexOfChild(findViewWithTag("TAG_" + paramInt));
  }
  
  private void init()
  {
    setOrientation(1);
    this.mLayoutInflater = LayoutInflater.from(getContext());
    this.mLayoutInflater.inflate(2130968864, this);
    this.mQuestionContainer = ((ViewGroup)findViewById(2131296453));
    this.mQuestionContainerBottomMargin = ((LinearLayout.LayoutParams)this.mQuestionContainer.getLayoutParams()).bottomMargin;
    this.mMoreButton = ((Button)findViewById(2131296806));
    this.mMoreButton.setOnClickListener(this);
  }
  
  private static boolean isSupportedQuestionType(TrainingQuestion paramTrainingQuestion)
  {
    return TrainingQuestionViewHelper.getQuestionViewResourceId(paramTrainingQuestion.getType()) != null;
  }
  
  private void onAnimationFinished(View paramView)
  {
    this.mAnimatingViews.remove(paramView);
    if (this.mAnimatingViews.isEmpty())
    {
      if (this.mListener != null)
      {
        Iterator localIterator = this.mPendingAnswers.iterator();
        while (localIterator.hasNext())
        {
          Pair localPair = (Pair)localIterator.next();
          this.mListener.onIcebreakerAnswerSelected((TrainingQuestion)localPair.first, (Sidekick.Question.Answer)localPair.second);
        }
      }
      this.mPendingAnswers.clear();
    }
  }
  
  private void removeQuestionSlot(int paramInt)
  {
    int i = getViewIndex(paramInt);
    if (i != -1)
    {
      ViewGroup localViewGroup = (ViewGroup)this.mQuestionContainer.getChildAt(i);
      View localView = localViewGroup.getChildAt(0);
      this.mAnimatingViews.add(localView);
      Animation localAnimation = AnimationUtils.loadAnimation(getContext(), 2131034118);
      localAnimation.setAnimationListener(AnimationFinalizer.newRemovingFinalizer(this, localView, this.mQuestionContainer, localViewGroup));
      localView.startAnimation(localAnimation);
    }
  }
  
  private void replaceViewAt(int paramInt, View paramView)
  {
    ViewGroup localViewGroup = (ViewGroup)this.mQuestionContainer.getChildAt(paramInt);
    View localView = localViewGroup.getChildAt(0);
    this.mAnimatingViews.add(paramView);
    this.mAnimatingViews.add(localView);
    Animation localAnimation1 = AnimationUtils.loadAnimation(getContext(), 2131034118);
    localAnimation1.setAnimationListener(AnimationFinalizer.newRemovingFinalizer(this, localView, localViewGroup, localView));
    localView.startAnimation(localAnimation1);
    localViewGroup.addView(paramView);
    Animation localAnimation2 = AnimationUtils.loadAnimation(getContext(), 2131034117);
    localAnimation2.setAnimationListener(AnimationFinalizer.newAddingFinalizer(this, paramView));
    paramView.startAnimation(localAnimation2);
  }
  
  private void showNextQuestion(int paramInt)
  {
    for (TrainingQuestionNode localTrainingQuestionNode = this.mAdapter.getNextQuestion(paramInt); (localTrainingQuestionNode != null) && (!isSupportedQuestionType(localTrainingQuestionNode.getQuestion())); localTrainingQuestionNode = this.mAdapter.getNextQuestion(paramInt)) {}
    if (localTrainingQuestionNode == null)
    {
      removeQuestionSlot(paramInt);
      this.mCurrentlyDisplayedQuestions.remove(paramInt);
      return;
    }
    View localView = buildQuestionView(paramInt, localTrainingQuestionNode);
    replaceViewAt(getViewIndex(paramInt), localView);
    this.mCurrentlyDisplayedQuestions.put(paramInt, new QuestionKey(localTrainingQuestionNode.getQuestion().getQuestion()));
  }
  
  private void updateView(boolean paramBoolean)
  {
    int i = this.mAdapter.getMaxQuestionSlots();
    int j = 0;
    if (j < i)
    {
      TrainingQuestionNode localTrainingQuestionNode = this.mAdapter.getCurrentQuestion(j);
      QuestionKey localQuestionKey;
      if (localTrainingQuestionNode != null)
      {
        localQuestionKey = new QuestionKey(localTrainingQuestionNode.getQuestion().getQuestion());
        label47:
        if ((localQuestionKey == null) || (!localQuestionKey.equals(this.mCurrentlyDisplayedQuestions.get(j)))) {
          break label80;
        }
      }
      for (;;)
      {
        j++;
        break;
        localQuestionKey = null;
        break label47;
        label80:
        while ((localTrainingQuestionNode != null) && (!isSupportedQuestionType(localTrainingQuestionNode.getQuestion()))) {
          localTrainingQuestionNode = this.mAdapter.getNextQuestion(j);
        }
        if (localTrainingQuestionNode != null)
        {
          View localView = buildQuestionView(j, localTrainingQuestionNode);
          int k = getViewIndex(j);
          if (k == -1) {
            addQuestionView(j, localView, paramBoolean);
          }
          for (;;)
          {
            this.mCurrentlyDisplayedQuestions.put(j, localQuestionKey);
            break;
            replaceViewAt(k, localView);
          }
        }
        removeQuestionSlot(j);
        this.mCurrentlyDisplayedQuestions.remove(j);
      }
    }
    updateMoreButtonVisibility();
  }
  
  public void notifyChanged()
  {
    updateView(true);
  }
  
  public void onClick(View paramView)
  {
    for (int i = 0; i < this.mAdapter.getMaxQuestionSlots(); i++) {
      if (this.mAdapter.getCurrentQuestion(i) != null) {
        showNextQuestion(i);
      }
    }
    updateMoreButtonVisibility();
    if (this.mListener != null) {
      this.mListener.onMoreButtonClicked();
    }
  }
  
  public void setAdapter(@Nullable IcebreakerSectionAdapter paramIcebreakerSectionAdapter)
  {
    if (this.mAdapter != null) {
      this.mAdapter.unregisterObserver(this);
    }
    this.mAdapter = paramIcebreakerSectionAdapter;
    this.mQuestionContainer.removeAllViews();
    this.mCurrentlyDisplayedQuestions = new SparseArray();
    if (this.mAdapter != null)
    {
      this.mAdapter.registerObserver(this);
      updateView(false);
    }
  }
  
  public void setListener(@Nullable Listener paramListener)
  {
    this.mListener = paramListener;
  }
  
  public void updateMoreButtonVisibility()
  {
    boolean bool = this.mAdapter.hasPendingQuestions();
    Button localButton = this.mMoreButton;
    int i;
    LinearLayout.LayoutParams localLayoutParams;
    int j;
    int k;
    int m;
    int n;
    if (bool)
    {
      i = 0;
      localButton.setVisibility(i);
      localLayoutParams = (LinearLayout.LayoutParams)this.mQuestionContainer.getLayoutParams();
      j = localLayoutParams.leftMargin;
      k = localLayoutParams.topMargin;
      m = localLayoutParams.rightMargin;
      n = 0;
      if (!bool) {
        break label84;
      }
    }
    for (;;)
    {
      localLayoutParams.setMargins(j, k, m, n);
      return;
      i = 8;
      break;
      label84:
      n = this.mQuestionContainerBottomMargin;
    }
  }
  
  private static class AnimationFinalizer
    implements Animation.AnimationListener, Runnable
  {
    private final View mAnimatingView;
    private final IcebreakerSectionView mIcebreakerView;
    @Nullable
    private final ViewGroup mParent;
    @Nullable
    private final View mViewToRemove;
    
    private AnimationFinalizer(IcebreakerSectionView paramIcebreakerSectionView, View paramView1, @Nullable ViewGroup paramViewGroup, @Nullable View paramView2)
    {
      Preconditions.checkNotNull(paramIcebreakerSectionView);
      Preconditions.checkNotNull(paramView1);
      this.mIcebreakerView = paramIcebreakerSectionView;
      this.mAnimatingView = paramView1;
      this.mParent = paramViewGroup;
      this.mViewToRemove = paramView2;
    }
    
    public static AnimationFinalizer newAddingFinalizer(IcebreakerSectionView paramIcebreakerSectionView, View paramView)
    {
      return new AnimationFinalizer(paramIcebreakerSectionView, paramView, null, null);
    }
    
    public static AnimationFinalizer newRemovingFinalizer(IcebreakerSectionView paramIcebreakerSectionView, View paramView1, ViewGroup paramViewGroup, View paramView2)
    {
      return new AnimationFinalizer(paramIcebreakerSectionView, paramView1, paramViewGroup, paramView2);
    }
    
    public void onAnimationEnd(Animation paramAnimation)
    {
      this.mAnimatingView.post(this);
    }
    
    public void onAnimationRepeat(Animation paramAnimation) {}
    
    public void onAnimationStart(Animation paramAnimation) {}
    
    public void run()
    {
      this.mIcebreakerView.onAnimationFinished(this.mAnimatingView);
      if ((this.mParent != null) && (this.mViewToRemove != null)) {
        this.mParent.removeView(this.mViewToRemove);
      }
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void onIcebreakerAnswerSelected(TrainingQuestion paramTrainingQuestion, Sidekick.Question.Answer paramAnswer);
    
    public abstract void onMoreButtonClicked();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.training.IcebreakerSectionView
 * JD-Core Version:    0.7.0.1
 */