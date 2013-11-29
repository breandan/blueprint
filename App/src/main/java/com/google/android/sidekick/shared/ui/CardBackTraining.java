package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import javax.annotation.Nullable;

public class CardBackTraining
  extends FrameLayout
  implements View.OnClickListener
{
  private View mCurrentView;
  @Nullable
  private Listener mListener;
  private ImageButton mNextButton;
  private ImageButton mPrevButton;
  private ViewGroup mQuestionContainer;
  private Runnable mShowNextQuestionRunnable;
  
  public CardBackTraining(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  private void cancelShowNextQuestionRunnable()
  {
    if (this.mShowNextQuestionRunnable != null)
    {
      removeCallbacks(this.mShowNextQuestionRunnable);
      this.mShowNextQuestionRunnable = null;
    }
  }
  
  private void hideView(View paramView, int paramInt)
  {
    Animation localAnimation = null;
    switch (paramInt)
    {
    default: 
      this.mQuestionContainer.removeView(paramView);
    }
    for (;;)
    {
      if (localAnimation != null)
      {
        localAnimation.setAnimationListener(new ViewRemover(this.mQuestionContainer, paramView));
        paramView.startAnimation(localAnimation);
      }
      return;
      localAnimation = AnimationUtils.loadAnimation(getContext(), 2131034118);
      continue;
      localAnimation = AnimationUtils.loadAnimation(getContext(), 2131034120);
    }
  }
  
  private void init()
  {
    setBackground(getResources().getDrawable(2130837539));
    LayoutInflater.from(getContext()).inflate(2130968626, this);
    this.mQuestionContainer = ((ViewGroup)findViewById(2131296453));
    this.mPrevButton = ((ImageButton)findViewById(2131296452));
    this.mNextButton = ((ImageButton)findViewById(2131296454));
    this.mPrevButton.setOnClickListener(this);
    this.mNextButton.setOnClickListener(this);
    setOnClickListener(this);
  }
  
  private void showView(View paramView, int paramInt)
  {
    Animation localAnimation = null;
    switch (paramInt)
    {
    }
    for (;;)
    {
      this.mQuestionContainer.addView(paramView);
      if (localAnimation != null) {
        paramView.startAnimation(localAnimation);
      }
      return;
      localAnimation = AnimationUtils.loadAnimation(getContext(), 2131034117);
      continue;
      localAnimation = AnimationUtils.loadAnimation(getContext(), 2131034119);
    }
  }
  
  private void updateScrollButtonVisibility(boolean paramBoolean1, boolean paramBoolean2)
  {
    ImageButton localImageButton1 = this.mNextButton;
    int i;
    ImageButton localImageButton2;
    int j;
    if (paramBoolean1)
    {
      i = 0;
      localImageButton1.setVisibility(i);
      localImageButton2 = this.mPrevButton;
      j = 0;
      if (!paramBoolean2) {
        break label45;
      }
    }
    for (;;)
    {
      localImageButton2.setVisibility(j);
      return;
      i = 4;
      break;
      label45:
      j = 4;
    }
  }
  
  public void onClick(View paramView)
  {
    if (paramView == this.mPrevButton)
    {
      cancelShowNextQuestionRunnable();
      if (this.mListener != null) {
        this.mListener.onPrevious();
      }
    }
    do
    {
      do
      {
        return;
        if (paramView != this.mNextButton) {
          break;
        }
        cancelShowNextQuestionRunnable();
      } while (this.mListener == null);
      this.mListener.onNext();
      return;
    } while (paramView != this);
  }
  
  protected void onDetachedFromWindow()
  {
    cancelShowNextQuestionRunnable();
    super.onDetachedFromWindow();
  }
  
  public void setListener(@Nullable Listener paramListener)
  {
    this.mListener = paramListener;
  }
  
  public void setQuestionView(View paramView, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
  {
    if (this.mCurrentView != null) {
      hideView(this.mCurrentView, paramInt);
    }
    showView(paramView, paramInt);
    updateScrollButtonVisibility(paramBoolean1, paramBoolean2);
    this.mCurrentView = paramView;
  }
  
  public void showNextQuestionAfterDelay(int paramInt)
  {
    cancelShowNextQuestionRunnable();
    this.mShowNextQuestionRunnable = new Runnable()
    {
      public void run()
      {
        if (CardBackTraining.this.mListener != null) {
          CardBackTraining.this.mListener.onNext();
        }
        CardBackTraining.access$102(CardBackTraining.this, null);
      }
    };
    postDelayed(this.mShowNextQuestionRunnable, paramInt);
  }
  
  public static abstract interface Listener
  {
    public abstract void onNext();
    
    public abstract void onPrevious();
  }
  
  private static class ViewRemover
    implements Animation.AnimationListener, Runnable
  {
    private final View mChild;
    private final ViewGroup mParent;
    
    public ViewRemover(ViewGroup paramViewGroup, View paramView)
    {
      this.mParent = paramViewGroup;
      this.mChild = paramView;
    }
    
    public void onAnimationEnd(Animation paramAnimation)
    {
      this.mChild.post(this);
    }
    
    public void onAnimationRepeat(Animation paramAnimation) {}
    
    public void onAnimationStart(Animation paramAnimation) {}
    
    public void run()
    {
      this.mParent.removeView(this.mChild);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.CardBackTraining
 * JD-Core Version:    0.7.0.1
 */