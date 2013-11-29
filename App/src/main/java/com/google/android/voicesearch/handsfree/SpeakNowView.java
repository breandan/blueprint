package com.google.android.voicesearch.handsfree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.common.base.Preconditions;

class SpeakNowView
  extends FrameLayout
  implements SpeakNowController.Ui
{
  private SpeakNowController mController;
  private final RecognizerViewHelper mRecognizerViewHelper;
  private final TextView mText;
  private final TextView mTitle;
  
  public SpeakNowView(Context paramContext)
  {
    super(paramContext);
    View localView = ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(2130968714, this);
    this.mTitle = ((TextView)findViewById(2131296382));
    this.mText = ((TextView)findViewById(2131296681));
    this.mRecognizerViewHelper = new RecognizerViewHelper(localView);
  }
  
  private void setText(int paramInt)
  {
    this.mText.setVisibility(0);
    this.mText.setText(paramInt);
  }
  
  private void setTitle(int paramInt)
  {
    this.mTitle.setVisibility(0);
    this.mTitle.setText(paramInt);
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(SpeakNowView.class.getCanonicalName());
  }
  
  public void setController(SpeakNowController paramSpeakNowController)
  {
    if (this.mController == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mController = ((SpeakNowController)Preconditions.checkNotNull(paramSpeakNowController));
      return;
    }
  }
  
  public void setLanguage(String paramString)
  {
    this.mRecognizerViewHelper.setLanguage(paramString);
  }
  
  public void showListening()
  {
    this.mRecognizerViewHelper.showListening();
  }
  
  public void showNotListening()
  {
    this.mRecognizerViewHelper.showNotListening();
  }
  
  public void showStart()
  {
    setTitle(2131363656);
    setText(2131363657);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.SpeakNowView
 * JD-Core Version:    0.7.0.1
 */