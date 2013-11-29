package com.google.android.search.shared.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Handler;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import com.google.android.shared.util.Util;

public class QueryRewritingViewImpl
  extends FrameLayout
  implements QueryRewritingView
{
  private QueryRewritingView.Callback mCallback;
  private String[] mOldWords;
  private TextPaint mTextPaint;
  
  public QueryRewritingViewImpl(Context paramContext)
  {
    super(paramContext);
  }
  
  private void findCommonWords(String paramString, String[] paramArrayOfString, boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2, int[] paramArrayOfInt)
  {
    int[] arrayOfInt = new int[paramArrayOfString.length];
    int i = -1;
    for (int j = 0; j < paramArrayOfString.length; j++)
    {
      i = paramString.indexOf(paramArrayOfString[j], i + 1);
      arrayOfInt[j] = i;
    }
    int k = 0;
    if (k < this.mOldWords.length) {
      for (int m = 0;; m++) {
        if (m < paramArrayOfString.length)
        {
          if ((this.mOldWords[k].equalsIgnoreCase(paramArrayOfString[m])) && (paramArrayOfBoolean2[m] == 0))
          {
            paramArrayOfBoolean1[k] = true;
            paramArrayOfBoolean2[m] = true;
            paramArrayOfInt[k] = arrayOfInt[m];
          }
        }
        else
        {
          k++;
          break;
        }
      }
    }
  }
  
  private StaticLayout getStaticLayout(CharSequence paramCharSequence, boolean paramBoolean)
  {
    int i = (int)this.mTextPaint.measureText(paramCharSequence, 0, paramCharSequence.length());
    return new StaticLayout(paramCharSequence, this.mTextPaint, i, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, paramBoolean);
  }
  
  private void hideUnused(boolean[] paramArrayOfBoolean)
  {
    for (int i = 0; i < paramArrayOfBoolean.length; i++) {
      if (paramArrayOfBoolean[i] == 0) {
        getChildAt(i).animate().alpha(0.0F).setDuration(150L);
      }
    }
  }
  
  private void moveKept(boolean[] paramArrayOfBoolean, String paramString, Layout paramLayout, int[] paramArrayOfInt, int paramInt)
  {
    int i = 0;
    if (i < paramArrayOfBoolean.length)
    {
      StaticLayoutTextView localStaticLayoutTextView;
      if (paramArrayOfBoolean[i] != 0)
      {
        localStaticLayoutTextView = (StaticLayoutTextView)getChildAt(i);
        if (paramLayout.getLineForOffset(paramArrayOfInt[i]) != 0) {
          break label113;
        }
      }
      label113:
      for (boolean bool = true;; bool = false)
      {
        updatePadding(localStaticLayoutTextView, bool);
        float f = paramLayout.getPrimaryHorizontal(paramArrayOfInt[i]);
        int j = paramInt + paramLayout.getLineTop(paramLayout.getLineForOffset(paramArrayOfInt[i]));
        localStaticLayoutTextView.animate().translationX(f).translationY(j).setDuration(300L).setStartDelay(0L);
        i++;
        break;
      }
    }
  }
  
  private void notifyAnimationComplete()
  {
    if (this.mCallback != null) {
      this.mCallback.onAnimationComplete();
    }
  }
  
  private void notifySetupComplete()
  {
    if (this.mCallback != null) {
      this.mCallback.onSetupComplete();
    }
  }
  
  private void setup(String paramString, String[] paramArrayOfString, Layout paramLayout, int paramInt)
  {
    int i = -1;
    int j = 0;
    if (j < paramArrayOfString.length)
    {
      String str = paramArrayOfString[j];
      i = paramString.indexOf(str, i + 1);
      if (paramLayout.getLineForOffset(i) == 0) {}
      for (boolean bool = true;; bool = false)
      {
        StaticLayoutTextView localStaticLayoutTextView = newStaticLayoutTextView(str + " ", bool);
        localStaticLayoutTextView.setTranslationX(paramLayout.getPrimaryHorizontal(i));
        localStaticLayoutTextView.setTranslationY(paramInt + paramLayout.getLineTop(paramLayout.getLineForOffset(i)));
        addView(localStaticLayoutTextView);
        j++;
        break;
      }
    }
  }
  
  private void showNew(String paramString, String[] paramArrayOfString, boolean[] paramArrayOfBoolean, Layout paramLayout, int paramInt)
  {
    int i = -1;
    Object localObject = null;
    int j = 0;
    if (j < paramArrayOfString.length)
    {
      if (paramArrayOfBoolean[j] == 0)
      {
        i = paramString.indexOf(paramArrayOfString[j], i + 1);
        if (paramLayout.getLineForOffset(i) != 0) {
          break label162;
        }
      }
      label162:
      for (boolean bool = true;; bool = false)
      {
        StaticLayoutTextView localStaticLayoutTextView = newStaticLayoutTextView(paramArrayOfString[j] + " ", bool);
        localStaticLayoutTextView.setAlpha(0.0F);
        localStaticLayoutTextView.setTranslationX(paramLayout.getPrimaryHorizontal(i));
        localStaticLayoutTextView.setTranslationY(paramInt + paramLayout.getLineTop(paramLayout.getLineForOffset(i)));
        addView(localStaticLayoutTextView);
        if (localObject != null) {
          localObject.animate().alpha(1.0F).setDuration(300L).setStartDelay(300L);
        }
        localObject = localStaticLayoutTextView;
        j++;
        break;
      }
    }
    if (localObject != null)
    {
      localObject.animate().alpha(1.0F).setDuration(300L).setStartDelay(300L).setListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          QueryRewritingViewImpl.this.notifyAnimationComplete();
        }
      });
      return;
    }
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        QueryRewritingViewImpl.this.notifyAnimationComplete();
      }
    }, 300L);
  }
  
  public StaticLayoutTextView newStaticLayoutTextView(String paramString, boolean paramBoolean)
  {
    StaticLayout localStaticLayout = getStaticLayout(paramString, paramBoolean);
    return new StaticLayoutTextView(getContext(), localStaticLayout, paramBoolean);
  }
  
  public void start(String paramString, Layout paramLayout, TextPaint paramTextPaint, int paramInt, QueryRewritingView.Callback paramCallback)
  {
    this.mOldWords = Util.tokenize(paramString);
    this.mTextPaint = paramTextPaint;
    this.mCallback = paramCallback;
    setup(paramString, this.mOldWords, paramLayout, paramInt);
    notifySetupComplete();
  }
  
  public void switchText(String paramString, Layout paramLayout, int paramInt)
  {
    String[] arrayOfString = Util.tokenize(paramString);
    boolean[] arrayOfBoolean1 = new boolean[this.mOldWords.length];
    boolean[] arrayOfBoolean2 = new boolean[arrayOfString.length];
    int[] arrayOfInt = new int[this.mOldWords.length];
    findCommonWords(paramString, arrayOfString, arrayOfBoolean1, arrayOfBoolean2, arrayOfInt);
    hideUnused(arrayOfBoolean1);
    moveKept(arrayOfBoolean1, paramString, paramLayout, arrayOfInt, paramInt);
    showNew(paramString, arrayOfString, arrayOfBoolean2, paramLayout, paramInt);
  }
  
  public void updatePadding(StaticLayoutTextView paramStaticLayoutTextView, boolean paramBoolean)
  {
    if (paramBoolean != paramStaticLayoutTextView.mIncludesPadding)
    {
      paramStaticLayoutTextView.mStaticLayout = getStaticLayout(paramStaticLayoutTextView.mStaticLayout.getText(), paramBoolean);
      paramStaticLayoutTextView.mIncludesPadding = paramBoolean;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.QueryRewritingViewImpl
 * JD-Core Version:    0.7.0.1
 */