package com.google.android.voicesearch.handsfree.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import com.google.android.googlequicksearchbox.R.styleable;

public class QuotedTextView
  extends TextView
{
  private int mQuotedTextColor;
  
  public QuotedTextView(Context paramContext)
  {
    this(paramContext, null);
    Log.i("QuotedTextView", "QuotedTextView(Context context)");
  }
  
  public QuotedTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
    Log.i("QuotedTextView", "QuotedTextView(Context context, AttributeSet attrs)");
  }
  
  public QuotedTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    Log.i("QuotedTextView", "QuotedTextView(Context context, AttributeSet attrs, int defStyle)" + paramAttributeSet);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.QuotedTextView, paramInt, 0);
    CharSequence localCharSequence = localTypedArray.getText(0);
    this.mQuotedTextColor = localTypedArray.getColor(1, getCurrentTextColor());
    setQuotedText(localCharSequence);
    localTypedArray.recycle();
  }
  
  public void setQuotedText(CharSequence paramCharSequence)
  {
    if (paramCharSequence != null)
    {
      String str = getContext().getString(2131363659, new Object[] { paramCharSequence });
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(str);
      int i = str.indexOf(paramCharSequence.toString());
      localSpannableStringBuilder.setSpan(new ForegroundColorSpan(this.mQuotedTextColor), i, i + paramCharSequence.length(), 33);
      setText(localSpannableStringBuilder);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.ui.QuotedTextView
 * JD-Core Version:    0.7.0.1
 */