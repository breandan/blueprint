package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class EllipsizingTextView
  extends TextView
{
  private SpannableStringBuilder mFullText;
  private boolean mIsStale;
  private float mLineAdditionalVerticalPadding = 0.0F;
  private float mLineSpacingMultiplier = 1.0F;
  private boolean mProgrammaticChange;
  
  public EllipsizingTextView(Context paramContext)
  {
    super(paramContext);
  }
  
  public EllipsizingTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public EllipsizingTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private Layout createWorkingLayout(CharSequence paramCharSequence)
  {
    return new StaticLayout(paramCharSequence, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(), Layout.Alignment.ALIGN_NORMAL, this.mLineSpacingMultiplier, this.mLineAdditionalVerticalPadding, false);
  }
  
  private void resetText()
  {
    int i = getMaxLines();
    String str1 = this.mFullText.toString();
    SpannableStringBuilder localSpannableStringBuilder = this.mFullText;
    if (i != -1)
    {
      Layout localLayout = createWorkingLayout(str1);
      if (localLayout.getLineCount() > i)
      {
        String str2 = getContext().getString(2131362776);
        for (String str3 = str1.substring(0, localLayout.getLineEnd(i)); createWorkingLayout(str3 + str2).getLineCount() > i; str3 = str3.substring(0, -1 + str3.length())) {}
        int j = this.mFullText.length() - str3.length();
        localSpannableStringBuilder = truncateByN(this.mFullText, j).append(str2);
      }
    }
    if (!localSpannableStringBuilder.equals(getText())) {
      this.mProgrammaticChange = true;
    }
    try
    {
      setText(localSpannableStringBuilder);
      this.mProgrammaticChange = false;
      return;
    }
    finally
    {
      this.mProgrammaticChange = false;
    }
  }
  
  private SpannableStringBuilder truncateByN(SpannableStringBuilder paramSpannableStringBuilder, int paramInt)
  {
    int i = paramSpannableStringBuilder.length();
    SpannableStringBuilder localSpannableStringBuilder = paramSpannableStringBuilder.delete(i - paramInt, i);
    for (int j = i - paramInt; localSpannableStringBuilder.charAt(j - 1) == ' '; j--) {
      localSpannableStringBuilder = localSpannableStringBuilder.delete(j - 1, j);
    }
    return localSpannableStringBuilder;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (this.mIsStale) {
      resetText();
    }
    super.onDraw(paramCanvas);
  }
  
  protected void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    super.onTextChanged(paramCharSequence, paramInt1, paramInt2, paramInt3);
    if (!this.mProgrammaticChange)
    {
      this.mFullText = SpannableStringBuilder.valueOf(paramCharSequence);
      this.mIsStale = true;
    }
  }
  
  public void setEllipsize(TextUtils.TruncateAt paramTruncateAt) {}
  
  public void setLineSpacing(float paramFloat1, float paramFloat2)
  {
    this.mLineAdditionalVerticalPadding = paramFloat1;
    this.mLineSpacingMultiplier = paramFloat2;
    super.setLineSpacing(paramFloat1, paramFloat2);
  }
  
  public void setMaxLines(int paramInt)
  {
    super.setMaxLines(paramInt);
    this.mIsStale = true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.EllipsizingTextView
 * JD-Core Version:    0.7.0.1
 */