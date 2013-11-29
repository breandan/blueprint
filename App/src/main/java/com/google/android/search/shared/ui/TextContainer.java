package com.google.android.search.shared.ui;

import android.animation.Animator.AnimatorListener;
import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Animations;
import com.google.android.shared.util.LayoutUtils;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public class TextContainer
  extends FrameLayout
{
  private boolean mAnimatingQueryRewrite;
  private Animator.AnimatorListener mAnimatorListener;
  private RelativeLayout.LayoutParams mDefaultParams;
  private TextView mDisplayText;
  private RelativeLayout.LayoutParams mFollowOnParams;
  private int mMode;
  private QueryRewritingView mQueryRewritingView;
  private SimpleSearchText mSearchBox;
  private RelativeLayout.LayoutParams mSoundInputParams;
  private StreamingTextView mStreamingText;
  private int mStreamingTextPaddingEnd;
  private View mStreamingTextView;
  private RelativeLayout.LayoutParams mVoiceInputParams;
  
  public TextContainer(Context paramContext)
  {
    super(paramContext);
  }
  
  public TextContainer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TextContainer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void animateQuery(final Query paramQuery)
  {
    if (this.mAnimatingQueryRewrite)
    {
      Log.w("TextContainer", "animateQuery was called while still animating.");
      this.mSearchBox.setQuery(paramQuery);
      resetQueryRewriteAnimation();
      return;
    }
    this.mAnimatingQueryRewrite = true;
    QueryRewritingView.Callback local1 = new QueryRewritingView.Callback()
    {
      public void onAnimationComplete()
      {
        TextContainer.this.resetQueryRewriteAnimation();
      }
      
      public void onSetupComplete()
      {
        TextContainer.this.mSearchBox.setAlpha(0.0F);
        TextContainer.this.mSearchBox.setSuggestionsEnabled(false);
        TextContainer.this.mSearchBox.setQuery(paramQuery);
        TextContainer.this.getQueryRewritingView().switchText(paramQuery.getQueryString(), TextContainer.this.mSearchBox.getLayout(), TextContainer.this.mSearchBox.getTotalPaddingTop());
      }
    };
    int i = this.mSearchBox.getTotalPaddingTop();
    getQueryRewritingView().start(this.mSearchBox.getText().toString(), this.mSearchBox.getLayout(), this.mSearchBox.getPaint(), i, local1);
  }
  
  private int getPixelSize(int paramInt)
  {
    return getContext().getResources().getDimensionPixelSize(paramInt);
  }
  
  private void hideView(View paramView, boolean paramBoolean)
  {
    if (paramView.getVisibility() != 0) {
      return;
    }
    if (paramBoolean)
    {
      paramView.setAlpha(0.0F);
      paramView.setVisibility(4);
      return;
    }
    Animations.fadeOutAndHide(paramView).setDuration(50L).setStartDelay(50L);
  }
  
  private boolean isAlignedEndToParent()
  {
    if (LayoutUtils.isLayoutRtl(this)) {
      return getLeft() - this.mVoiceInputParams.leftMargin == 0;
    }
    return getRight() + this.mVoiceInputParams.rightMargin == ((View)getParent()).getWidth();
  }
  
  private void resetQueryRewriteAnimation()
  {
    this.mAnimatingQueryRewrite = false;
    this.mSearchBox.setAlpha(1.0F);
    getQueryRewritingView().removeAllViews();
    if (this.mAnimatorListener != null) {
      this.mAnimatorListener.onAnimationEnd(null);
    }
  }
  
  private void showView(View paramView, boolean paramBoolean)
  {
    if (paramView.getVisibility() == 0) {
      return;
    }
    if (paramBoolean)
    {
      paramView.setVisibility(0);
      paramView.setAlpha(1.0F);
      return;
    }
    Animations.showAndFadeIn(paramView).setDuration(50L).setStartDelay(50L);
  }
  
  protected QueryRewritingView getQueryRewritingView()
  {
    if (this.mQueryRewritingView == null)
    {
      QueryRewritingViewImpl localQueryRewritingViewImpl = new QueryRewritingViewImpl(getContext());
      addView(localQueryRewritingViewImpl, new FrameLayout.LayoutParams(-1, -1));
      this.mQueryRewritingView = localQueryRewritingViewImpl;
    }
    return this.mQueryRewritingView;
  }
  
  public boolean isAnimatingQueryRewrite()
  {
    return this.mAnimatingQueryRewrite;
  }
  
  public void onFinishInflate()
  {
    super.onFinishInflate();
    this.mSearchBox = ((SimpleSearchText)Preconditions.checkNotNull(findViewById(2131296723)));
    this.mDisplayText = ((TextView)Preconditions.checkNotNull(findViewById(2131296525)));
    this.mStreamingTextView = ((View)Preconditions.checkNotNull(findViewById(2131296526)));
    this.mStreamingText = ((StreamingTextView)this.mStreamingTextView);
    int i;
    int j;
    label80:
    int k;
    if (Build.VERSION.SDK_INT >= 17)
    {
      i = 1;
      if (i == 0) {
        break label437;
      }
      j = 20;
      if (i == 0) {
        break label443;
      }
      k = 16;
      label87:
      if (i == 0) {
        break label448;
      }
    }
    label437:
    label443:
    label448:
    for (int m = 17;; m = 1)
    {
      this.mVoiceInputParams = new RelativeLayout.LayoutParams(-1, getPixelSize(2131689602));
      this.mVoiceInputParams.addRule(j);
      this.mVoiceInputParams.addRule(k, 2131296973);
      int n = getPixelSize(2131689613);
      LayoutUtils.setMarginsRelative(this.mVoiceInputParams, n, 0, n, n);
      this.mSoundInputParams = new RelativeLayout.LayoutParams(-1, -2);
      this.mSoundInputParams.addRule(j);
      this.mSoundInputParams.addRule(2, 2131296969);
      LayoutUtils.setMarginsRelative(this.mSoundInputParams, n, 0, n, n + getPixelSize(2131689644));
      this.mDefaultParams = new RelativeLayout.LayoutParams(-1, -2);
      this.mDefaultParams.addRule(m, 2131296524);
      this.mDefaultParams.addRule(k, 2131296966);
      this.mDefaultParams.addRule(15);
      LayoutUtils.setMarginsRelative(this.mDefaultParams, getPixelSize(2131689614), 0, getPixelSize(2131689615), 0);
      this.mFollowOnParams = new RelativeLayout.LayoutParams(-1, getContext().getResources().getDimensionPixelSize(2131689620));
      this.mFollowOnParams.addRule(15);
      this.mFollowOnParams.addRule(k, 2131296966);
      this.mFollowOnParams.addRule(m, 2131296524);
      LayoutUtils.setMarginsRelative(this.mFollowOnParams, getPixelSize(2131689614), 0, getPixelSize(2131689615), 0);
      this.mStreamingTextPaddingEnd = (getPixelSize(2131689618) + getPixelSize(2131689612) - getPixelSize(2131689613));
      getLayoutTransition().enableTransitionType(4);
      getLayoutTransition().disableTransitionType(3);
      getLayoutTransition().disableTransitionType(2);
      getLayoutTransition().disableTransitionType(0);
      getLayoutTransition().disableTransitionType(1);
      getLayoutTransition().setAnimateParentHierarchy(false);
      return;
      i = 0;
      break;
      j = 9;
      break label80;
      k = 0;
      break label87;
    }
  }
  
  public void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (getLayoutParams().height == -2)
    {
      int i = this.mMode;
      int j = 0;
      if (i == 11)
      {
        FrameLayout.LayoutParams localLayoutParams2 = (FrameLayout.LayoutParams)this.mSearchBox.getLayoutParams();
        j = this.mSearchBox.getMeasuredHeight() + localLayoutParams2.topMargin + localLayoutParams2.bottomMargin;
      }
      for (int k = 0; k < getChildCount(); k++)
      {
        View localView = getChildAt(k);
        if (localView.getVisibility() == 0)
        {
          FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams)localView.getLayoutParams();
          j = Math.max(j, this.mSearchBox.getMeasuredHeight() + localLayoutParams1.topMargin + localLayoutParams1.bottomMargin);
        }
      }
      setMeasuredDimension(getMeasuredWidth(), j);
    }
  }
  
  public void resetRecognizedText()
  {
    this.mDisplayText.setAlpha(1.0F);
    this.mDisplayText.setVisibility(0);
    this.mStreamingText.reset();
  }
  
  public void setAnimatorListener(@Nullable Animator.AnimatorListener paramAnimatorListener)
  {
    this.mAnimatorListener = paramAnimatorListener;
  }
  
  public void setFinalRecognizedText(CharSequence paramCharSequence)
  {
    hideView(this.mDisplayText, true);
    this.mStreamingText.setFinalRecognizedText(paramCharSequence.toString());
    if (this.mMode == 9)
    {
      this.mStreamingText.reset();
      showView(this.mSearchBox, true);
    }
  }
  
  public void setMode(int paramInt)
  {
    if (paramInt == this.mMode) {
      return;
    }
    this.mMode = paramInt;
    if ((paramInt == 3) || (paramInt == 4) || (paramInt == 10))
    {
      showView(this.mDisplayText, false);
      this.mStreamingText.reset();
      this.mStreamingText.setTextSize(0, getContext().getResources().getDimension(2131689599));
      this.mStreamingText.setGravity(8388691);
      LayoutUtils.setPaddingRelative(this.mStreamingTextView, 0, 0, 0, 0);
      showView(this.mStreamingTextView, true);
      hideView(this.mSearchBox, true);
      setLayoutParams(this.mVoiceInputParams);
      ((FrameLayout.LayoutParams)this.mStreamingTextView.getLayoutParams()).gravity = 80;
      return;
    }
    if ((paramInt == 5) || (paramInt == 6))
    {
      showView(this.mDisplayText, false);
      hideView(this.mStreamingTextView, true);
      hideView(this.mSearchBox, true);
      setLayoutParams(this.mSoundInputParams);
      return;
    }
    if (paramInt == 9)
    {
      hideView(this.mDisplayText, true);
      this.mStreamingText.reset();
      this.mStreamingText.setTextSize(0, getContext().getResources().getDimension(2131689598));
      showView(this.mStreamingTextView, true);
      LayoutUtils.setPaddingRelative(this.mStreamingTextView, 0, 0, 0, 0);
      hideView(this.mSearchBox, true);
      setLayoutParams(this.mFollowOnParams);
      ((FrameLayout.LayoutParams)this.mStreamingTextView.getLayoutParams()).gravity = 17;
      this.mStreamingText.setGravity(8388627);
      return;
    }
    if (paramInt == 11)
    {
      hideView(this.mDisplayText, true);
      hideView(this.mStreamingTextView, true);
      hideView(this.mSearchBox, true);
      setLayoutParams(this.mDefaultParams);
      return;
    }
    int i;
    TextView localTextView;
    if (isAlignedEndToParent())
    {
      i = -this.mStreamingTextPaddingEnd;
      setLayoutParams(this.mDefaultParams);
      LayoutUtils.setPaddingRelative(this.mStreamingTextView, 0, 0, i, 0);
      this.mStreamingText.setTextSize(0, getContext().getResources().getDimension(2131689598));
      ((FrameLayout.LayoutParams)this.mStreamingTextView.getLayoutParams()).gravity = 17;
      this.mStreamingText.setGravity(8388627);
      localTextView = this.mDisplayText;
      if (paramInt == 1) {
        break label463;
      }
    }
    label463:
    for (boolean bool = true;; bool = false)
    {
      hideView(localTextView, bool);
      hideView(this.mStreamingTextView, false);
      showView(this.mSearchBox, false);
      return;
      i = 0;
      break;
    }
  }
  
  public void setQuery(Query paramQuery)
  {
    Editable localEditable = this.mSearchBox.getText();
    String str = paramQuery.getQueryString();
    if ((paramQuery.isRewritten()) && (!TextUtils.isEmpty(localEditable)) && (!TextUtils.isEmpty(str)) && (!TextUtils.equals(localEditable, str)))
    {
      animateQuery(paramQuery);
      return;
    }
    this.mSearchBox.setQuery(paramQuery);
  }
  
  public void updateRecognizedText(String paramString1, String paramString2)
  {
    if ((!TextUtils.isEmpty(paramString1)) || (!TextUtils.isEmpty(paramString2))) {
      hideView(this.mDisplayText, true);
    }
    if (this.mMode == 9) {
      hideView(this.mSearchBox, true);
    }
    this.mStreamingText.updateRecognizedText(paramString1, paramString2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.TextContainer
 * JD-Core Version:    0.7.0.1
 */