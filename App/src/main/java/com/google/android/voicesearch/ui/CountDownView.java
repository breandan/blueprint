package com.google.android.voicesearch.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CountDownView
  extends RelativeLayout
{
  private ActionEditorView.ActionEditorListener mActionEditorListener;
  private ImageView mAppIcon;
  private ViewGroup mAppIconView;
  private View mCancelCountdownButton;
  private View mConfirmBar;
  private TextView mConfirmButton;
  private ObjectAnimator mCountDownAnimator;
  private ProgressBar mCountDownBar;
  
  public CountDownView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CountDownView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 2131624093);
  }
  
  public CountDownView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void cancelCountDownAnimation()
  {
    if (this.mCountDownAnimator != null)
    {
      this.mCountDownAnimator.cancel();
      this.mCountDownBar.setProgress(0);
    }
    if (this.mCountDownBar.getVisibility() == 0)
    {
      this.mCountDownBar.setVisibility(4);
      this.mCancelCountdownButton.setVisibility(4);
    }
  }
  
  protected void grayOutConfirmIcon()
  {
    if (this.mAppIcon != null) {
      this.mAppIcon.setColorFilter(-3355444);
    }
  }
  
  protected void onFinishInflate()
  {
    this.mCountDownBar = ((ProgressBar)findViewById(2131296497));
    this.mAppIconView = ((ViewGroup)findViewById(2131296494));
    this.mAppIcon = ((ImageView)findViewById(2131296495));
    this.mConfirmBar = findViewById(2131296492);
    this.mConfirmBar.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Object localObject = CountDownView.this.getTag();
        if ((localObject instanceof Integer)) {}
        for (int i = ((Integer)localObject).intValue();; i = 0)
        {
          if (CountDownView.this.mActionEditorListener != null) {
            CountDownView.this.mActionEditorListener.onExecute(i);
          }
          return;
        }
      }
    });
    this.mConfirmButton = ((TextView)findViewById(2131296496));
    this.mCancelCountdownButton = findViewById(2131296493);
    if (this.mCancelCountdownButton != null) {
      this.mCancelCountdownButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          if (CountDownView.this.mActionEditorListener != null) {
            CountDownView.this.mActionEditorListener.onCancelCountdown();
          }
        }
      });
    }
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(CountDownView.class.getCanonicalName());
  }
  
  public void setActionEditorListener(ActionEditorView.ActionEditorListener paramActionEditorListener)
  {
    this.mActionEditorListener = paramActionEditorListener;
  }
  
  public void setConfirmIcon(int paramInt)
  {
    this.mAppIcon.setImageDrawable(getResources().getDrawable(paramInt));
    if (this.mAppIcon.getParent() != this.mAppIconView) {
      setConfirmIcon(this.mAppIcon);
    }
  }
  
  public void setConfirmIcon(View paramView)
  {
    this.mAppIconView.removeAllViews();
    this.mAppIconView.addView(paramView);
  }
  
  public void setConfirmText(int paramInt)
  {
    this.mConfirmButton.setText(paramInt);
  }
  
  public void setConfirmText(String paramString)
  {
    this.mConfirmButton.setText(paramString);
  }
  
  public void setConfirmationEnabled(boolean paramBoolean)
  {
    this.mConfirmBar.setEnabled(paramBoolean);
  }
  
  public void setNoConfirmIcon()
  {
    this.mAppIconView.removeAllViews();
  }
  
  public void startCountDownAnimation(long paramLong)
  {
    setVisibility(0);
    this.mCountDownBar.setVisibility(0);
    this.mCancelCountdownButton.setVisibility(0);
    ProgressBar localProgressBar = this.mCountDownBar;
    Property localProperty = Property.of(ProgressBar.class, Integer.class, "progress");
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = 0;
    arrayOfInt[1] = this.mCountDownBar.getMax();
    this.mCountDownAnimator = ObjectAnimator.ofInt(localProgressBar, localProperty, arrayOfInt);
    this.mCountDownAnimator.setDuration(paramLong);
    this.mCountDownAnimator.start();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.ui.CountDownView
 * JD-Core Version:    0.7.0.1
 */