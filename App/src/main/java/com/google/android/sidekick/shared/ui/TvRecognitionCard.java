package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.search.shared.ui.AudioProgressRenderer;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.SpeechLevelSource;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;

public class TvRecognitionCard
  extends LinearLayout
{
  private AudioProgressRenderer mAnimationView;
  private List<TextView> mDescriptionFields;
  private Button mDetectButton;
  private String mDeviceName;
  private TextView mTitle;
  
  public TvRecognitionCard(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TvRecognitionCard(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private CharSequence createErrorBullet(int paramInt)
  {
    int i = getResources().getDimensionPixelOffset(2131689749);
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(getContext().getString(paramInt));
    localSpannableStringBuilder.setSpan(new BulletSpan(i), 0, localSpannableStringBuilder.length(), 0);
    return localSpannableStringBuilder;
  }
  
  private String getTvDescription()
  {
    if (this.mDeviceName != null)
    {
      Context localContext = getContext();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = BidiUtils.unicodeWrap(this.mDeviceName);
      return localContext.getString(2131362804, arrayOfObject);
    }
    return getContext().getString(2131362805);
  }
  
  private void setDescription(CharSequence... paramVarArgs)
  {
    Iterator localIterator = this.mDescriptionFields.iterator();
    int i = paramVarArgs.length;
    for (int j = 0;; j++)
    {
      CharSequence localCharSequence;
      if (j < i)
      {
        localCharSequence = paramVarArgs[j];
        if (localIterator.hasNext()) {}
      }
      else
      {
        while (localIterator.hasNext()) {
          ((TextView)localIterator.next()).setVisibility(8);
        }
      }
      TextView localTextView = (TextView)localIterator.next();
      localTextView.setText(localCharSequence);
      localTextView.setVisibility(0);
    }
  }
  
  private void setDetecting(boolean paramBoolean)
  {
    Button localButton = this.mDetectButton;
    int i;
    AudioProgressRenderer localAudioProgressRenderer;
    int j;
    if (paramBoolean)
    {
      i = 8;
      localButton.setVisibility(i);
      localAudioProgressRenderer = this.mAnimationView;
      j = 0;
      if (!paramBoolean) {
        break label54;
      }
    }
    for (;;)
    {
      localAudioProgressRenderer.setVisibility(j);
      if (!paramBoolean) {
        break label61;
      }
      this.mAnimationView.startAnimation();
      return;
      i = 0;
      break;
      label54:
      j = 8;
    }
    label61:
    this.mAnimationView.stopAnimation();
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTitle = ((TextView)findViewById(2131296451));
    this.mDescriptionFields = ImmutableList.of((TextView)findViewById(2131297183), (TextView)findViewById(2131297184), (TextView)findViewById(2131297185));
    this.mDetectButton = ((Button)findViewById(2131297186));
    this.mAnimationView = ((AudioProgressRenderer)findViewById(2131297187));
  }
  
  public void reset()
  {
    this.mTitle.setText(2131362803);
    CharSequence[] arrayOfCharSequence = new CharSequence[1];
    arrayOfCharSequence[0] = getTvDescription();
    setDescription(arrayOfCharSequence);
    setDetecting(false);
  }
  
  public void setDeviceName(String paramString)
  {
    this.mDeviceName = paramString;
  }
  
  public void setOnDetectClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mDetectButton.setOnClickListener(paramOnClickListener);
  }
  
  public void setSpeechLevelSource(SpeechLevelSource paramSpeechLevelSource)
  {
    this.mAnimationView.setSpeechLevelSource(paramSpeechLevelSource);
  }
  
  public void showDetecting()
  {
    this.mTitle.setText(2131362807);
    setDescription(new CharSequence[0]);
    setDetecting(true);
  }
  
  public void showError()
  {
    this.mTitle.setText(2131362808);
    CharSequence[] arrayOfCharSequence = new CharSequence[3];
    arrayOfCharSequence[0] = createErrorBullet(2131362809);
    arrayOfCharSequence[1] = createErrorBullet(2131362810);
    arrayOfCharSequence[2] = createErrorBullet(2131362811);
    setDescription(arrayOfCharSequence);
    setDetecting(false);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.TvRecognitionCard
 * JD-Core Version:    0.7.0.1
 */