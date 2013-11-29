package com.google.android.sidekick.shared.util;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nullable;

public class CardTextUtil
{
  public static CharSequence color(String paramString, int paramInt)
  {
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramString);
    localSpannableStringBuilder.setSpan(new ForegroundColorSpan(paramInt), 0, paramString.length(), 17);
    return localSpannableStringBuilder;
  }
  
  public static CharSequence hyphenate(Iterable<? extends CharSequence> paramIterable)
  {
    ArrayList localArrayList = null;
    Object localObject = null;
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      CharSequence localCharSequence = (CharSequence)localIterator.next();
      if (!TextUtils.isEmpty(localCharSequence)) {
        if (localObject == null)
        {
          localObject = localCharSequence;
        }
        else
        {
          if (localArrayList == null)
          {
            localArrayList = Lists.newArrayListWithExpectedSize(7);
            localArrayList.add(localObject);
          }
          localArrayList.add(" - ");
          localArrayList.add(localCharSequence);
        }
      }
    }
    if (localArrayList == null)
    {
      if (localObject == null) {
        localObject = "";
      }
      return localObject;
    }
    return TextUtils.concat((CharSequence[])localArrayList.toArray(new CharSequence[localArrayList.size()]));
  }
  
  public static CharSequence hyphenate(@Nullable CharSequence... paramVarArgs)
  {
    CharSequence[] arrayOfCharSequence = null;
    int i = 0;
    Object localObject = null;
    int j = paramVarArgs.length;
    int k = 0;
    if (k < j)
    {
      CharSequence localCharSequence = paramVarArgs[k];
      if (!TextUtils.isEmpty(localCharSequence))
      {
        if (localObject != null) {
          break label50;
        }
        localObject = localCharSequence;
      }
      for (;;)
      {
        i++;
        k++;
        break;
        label50:
        if (arrayOfCharSequence == null)
        {
          arrayOfCharSequence = new CharSequence[-1 + 2 * paramVarArgs.length];
          Arrays.fill(arrayOfCharSequence, " - ");
          arrayOfCharSequence[0] = localObject;
        }
        arrayOfCharSequence[(i * 2)] = localCharSequence;
      }
    }
    switch (i)
    {
    default: 
      localObject = TextUtils.concat((CharSequence[])Arrays.copyOfRange(arrayOfCharSequence, 0, -1 + i * 2));
    case 1: 
      return localObject;
    }
    return "";
  }
  
  public static TextView setHyphenatedTextView(View paramView, int paramInt, Iterable<? extends CharSequence> paramIterable)
  {
    CharSequence localCharSequence = hyphenate(paramIterable);
    if (TextUtils.isEmpty(localCharSequence)) {
      return null;
    }
    return setTextView(paramView, paramInt, localCharSequence);
  }
  
  @Nullable
  public static TextView setHyphenatedTextView(View paramView, int paramInt, @Nullable CharSequence... paramVarArgs)
  {
    if (paramVarArgs.length == 0) {}
    CharSequence localCharSequence;
    do
    {
      return null;
      localCharSequence = hyphenate(paramVarArgs);
    } while (TextUtils.isEmpty(localCharSequence));
    return setTextView(paramView, paramInt, localCharSequence);
  }
  
  public static TextView setTextView(View paramView, int paramInt, CharSequence paramCharSequence)
  {
    TextView localTextView = (TextView)paramView.findViewById(paramInt);
    localTextView.setText(paramCharSequence);
    localTextView.setVisibility(0);
    return localTextView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.CardTextUtil
 * JD-Core Version:    0.7.0.1
 */