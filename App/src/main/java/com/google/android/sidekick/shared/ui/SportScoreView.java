package com.google.android.sidekick.shared.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

public class SportScoreView
  extends TextView
{
  private final int mAnimationTime = getResources().getInteger(17694722);
  private String mScore;
  
  public SportScoreView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public void setScore(final String paramString, boolean paramBoolean)
  {
    this.mScore = paramString;
    if (!paramBoolean)
    {
      setText(paramString);
      return;
    }
    animate().rotationX(90.0F).setDuration(this.mAnimationTime).setListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        SportScoreView.this.setText(paramString);
        SportScoreView.this.setRotationX(-90.0F);
        SportScoreView.this.animate().rotationX(0.0F).setDuration(SportScoreView.this.mAnimationTime).setListener(null);
      }
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.SportScoreView
 * JD-Core Version:    0.7.0.1
 */