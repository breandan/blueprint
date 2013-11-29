package com.google.android.search.shared.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.os.Build.VERSION;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrailleStreamingTextView
  extends TextView
  implements StreamingTextView
{
  private static final Property<BrailleStreamingTextView, Integer> STREAM_POSITION_PROPERTY = new Property(Integer.class, "streamPosition")
  {
    public Integer get(BrailleStreamingTextView paramAnonymousBrailleStreamingTextView)
    {
      return Integer.valueOf(paramAnonymousBrailleStreamingTextView.mStreamPosition);
    }
    
    public void set(BrailleStreamingTextView paramAnonymousBrailleStreamingTextView, Integer paramAnonymousInteger)
    {
      BrailleStreamingTextView.access$002(paramAnonymousBrailleStreamingTextView, paramAnonymousInteger.intValue());
    }
  };
  private Bitmap mOneDot;
  private final Random mRandom = new Random();
  private int mStreamPosition;
  private ObjectAnimator mStreamingAnimation;
  private Bitmap mTwoDot;
  
  public BrailleStreamingTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private void addDottySpans(SpannableStringBuilder paramSpannableStringBuilder, String paramString, int paramInt)
  {
    Matcher localMatcher = SPLIT_PATTERN.matcher(paramString);
    while (localMatcher.find())
    {
      int i = paramInt + localMatcher.start();
      int j = paramInt + localMatcher.end();
      paramSpannableStringBuilder.setSpan(new DottySpan(paramString.charAt(localMatcher.start()), i), i, j, 33);
    }
  }
  
  private void cancelStreamAnimation()
  {
    if (this.mStreamingAnimation != null) {
      this.mStreamingAnimation.cancel();
    }
  }
  
  private void updateText(CharSequence paramCharSequence)
  {
    setText(paramCharSequence);
    bringPointIntoView(length());
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mOneDot = BitmapFactory.decodeResource(getResources(), 2130838090);
    this.mTwoDot = BitmapFactory.decodeResource(getResources(), 2130838091);
    reset();
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(StreamingTextView.class.getCanonicalName());
  }
  
  public void reset()
  {
    this.mStreamPosition = -1;
    cancelStreamAnimation();
    setText("");
  }
  
  public void setFinalRecognizedText(String paramString)
  {
    updateText(paramString);
  }
  
  protected void startStreamAnimation()
  {
    cancelStreamAnimation();
    int i = this.mStreamPosition;
    int j = length();
    int k = j - i;
    if (k > 0)
    {
      if (this.mStreamingAnimation == null)
      {
        this.mStreamingAnimation = new ObjectAnimator();
        this.mStreamingAnimation.setTarget(this);
        this.mStreamingAnimation.setProperty(STREAM_POSITION_PROPERTY);
      }
      this.mStreamingAnimation.setIntValues(new int[] { i, j });
      this.mStreamingAnimation.setDuration(50L * k);
      this.mStreamingAnimation.setInterpolator(null);
      this.mStreamingAnimation.start();
    }
  }
  
  public void updateRecognizedText(String paramString1, String paramString2)
  {
    if (paramString1 == null) {
      paramString1 = "";
    }
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramString1);
    if (paramString2 != null)
    {
      int i = localSpannableStringBuilder.length();
      localSpannableStringBuilder.append(paramString2);
      addDottySpans(localSpannableStringBuilder, paramString2, i);
    }
    this.mStreamPosition = Math.max(paramString1.length(), this.mStreamPosition);
    updateText(new SpannedString(localSpannableStringBuilder));
    startStreamAnimation();
  }
  
  private class DottySpan
    extends ReplacementSpan
  {
    private final int mPosition;
    private final int mSeed;
    
    public DottySpan(int paramInt1, int paramInt2)
    {
      this.mSeed = paramInt1;
      this.mPosition = paramInt2;
    }
    
    public void draw(Canvas paramCanvas, CharSequence paramCharSequence, int paramInt1, int paramInt2, float paramFloat, int paramInt3, int paramInt4, int paramInt5, Paint paramPaint)
    {
      int i = (int)paramPaint.measureText(paramCharSequence, paramInt1, paramInt2);
      int j = BrailleStreamingTextView.this.mOneDot.getWidth();
      int k = j * 2;
      int m = i / k;
      int n = i % k / 2;
      boolean bool = isLayoutRtl(BrailleStreamingTextView.this);
      BrailleStreamingTextView.this.mRandom.setSeed(this.mSeed);
      int i1 = paramPaint.getAlpha();
      int i2 = 0;
      if ((i2 >= m) || (i2 + this.mPosition >= BrailleStreamingTextView.this.mStreamPosition))
      {
        paramPaint.setAlpha(i1);
        return;
      }
      float f1 = n + i2 * k + j / 2;
      float f2;
      if (bool)
      {
        f2 = paramFloat + i - f1 - j;
        label149:
        paramPaint.setAlpha(63 * (1 + BrailleStreamingTextView.this.mRandom.nextInt(4)));
        if (!BrailleStreamingTextView.this.mRandom.nextBoolean()) {
          break label228;
        }
        paramCanvas.drawBitmap(BrailleStreamingTextView.this.mTwoDot, f2, paramInt4 - BrailleStreamingTextView.this.mTwoDot.getHeight(), paramPaint);
      }
      for (;;)
      {
        i2++;
        break;
        f2 = paramFloat + f1;
        break label149;
        label228:
        paramCanvas.drawBitmap(BrailleStreamingTextView.this.mOneDot, f2, paramInt4 - BrailleStreamingTextView.this.mOneDot.getHeight(), paramPaint);
      }
    }
    
    public int getSize(Paint paramPaint, CharSequence paramCharSequence, int paramInt1, int paramInt2, Paint.FontMetricsInt paramFontMetricsInt)
    {
      return (int)paramPaint.measureText(paramCharSequence, paramInt1, paramInt2);
    }
    
    protected boolean isLayoutRtl(View paramView)
    {
      if (Build.VERSION.SDK_INT >= 17) {
        return 1 == paramView.getLayoutDirection();
      }
      return false;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.BrailleStreamingTextView
 * JD-Core Version:    0.7.0.1
 */