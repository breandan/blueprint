package com.google.android.e100;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.search.shared.ui.RecognizerView;
import com.google.android.search.shared.ui.SoundLevels;
import com.google.android.search.shared.ui.StreamingTextView;
import com.google.common.base.Preconditions;

public class E100SearchPlate
  extends RelativeLayout
{
  private TextView mDisplayText;
  private boolean mFinalRecognizedTextIsSet;
  private int mRecognitionState;
  private RecognizerView mRecognizerView;
  private SoundLevels mSoundLevels;
  private StreamingTextView mStreamingTextView;
  
  public E100SearchPlate(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public E100SearchPlate(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public E100SearchPlate(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void onFinishInflate()
  {
    super.onFinishInflate();
    this.mDisplayText = ((TextView)Preconditions.checkNotNull(findViewById(2131296525)));
    this.mStreamingTextView = ((StreamingTextView)Preconditions.checkNotNull(findViewById(2131296526)));
    this.mSoundLevels = ((SoundLevels)Preconditions.checkNotNull(findViewById(2131296527)));
    this.mRecognizerView = ((RecognizerView)Preconditions.checkNotNull(findViewById(2131296528)));
    ((View)Preconditions.checkNotNull(findViewById(2131296974))).setVisibility(4);
    setRecognitionState(7);
  }
  
  public void setRecognitionState(int paramInt)
  {
    this.mRecognitionState = paramInt;
    switch (this.mRecognitionState)
    {
    }
    for (;;)
    {
      if (this.mRecognitionState != 5) {
        this.mDisplayText.setVisibility(0);
      }
      this.mFinalRecognizedTextIsSet = false;
      return;
      this.mDisplayText.setText(2131363577);
      this.mStreamingTextView.reset();
      this.mSoundLevels.setEnabled(false);
      this.mRecognizerView.setState(1);
      continue;
      this.mDisplayText.setText(2131363575);
      this.mStreamingTextView.reset();
      this.mSoundLevels.setEnabled(false);
      this.mRecognizerView.setState(0);
      continue;
      this.mDisplayText.setText(2131363577);
      this.mStreamingTextView.reset();
      this.mSoundLevels.setEnabled(false);
      this.mRecognizerView.setState(1);
      continue;
      this.mDisplayText.setText(2131363461);
      this.mStreamingTextView.reset();
      this.mSoundLevels.setEnabled(true);
      this.mRecognizerView.setState(2);
      continue;
      this.mRecognizerView.setState(3);
      this.mSoundLevels.setEnabled(true);
      continue;
      this.mDisplayText.setText(2131363598);
      this.mSoundLevels.setEnabled(false);
      this.mRecognizerView.setState(4);
      continue;
      this.mDisplayText.setText(2131363575);
      this.mStreamingTextView.reset();
      this.mSoundLevels.setEnabled(false);
      this.mRecognizerView.setState(0);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.e100.E100SearchPlate
 * JD-Core Version:    0.7.0.1
 */