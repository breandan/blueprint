package com.google.android.voicesearch.ime.formatter;

import android.text.Annotation;
import android.text.Spanned;
import android.util.Log;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.InputConnection;
import com.google.android.shared.util.TextUtil;
import com.google.android.speech.alternates.Hypothesis;
import com.google.android.speech.alternates.Hypothesis.Span;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LatinTextFormatter
  implements TextFormatter
{
  private static boolean DEBUG = false;
  private boolean mFirstCharUppercase;
  private boolean mFirstCommit;
  private Set<Character> upperCaseChars = new HashSet();
  
  public LatinTextFormatter()
  {
    this.upperCaseChars.add(Character.valueOf('.'));
    this.upperCaseChars.add(Character.valueOf('!'));
    this.upperCaseChars.add(Character.valueOf('?'));
    this.upperCaseChars.add(Character.valueOf('\n'));
  }
  
  private String forceFirstCharUppercase(String paramString)
  {
    if ((paramString != null) && (paramString.length() > 0)) {
      paramString = Character.toUpperCase(paramString.charAt(0)) + paramString.substring(1);
    }
    return paramString;
  }
  
  private Hypothesis.Span formatHypothesisSpan(Hypothesis.Span paramSpan)
  {
    if (paramSpan.mUtf8Start != 0) {
      return paramSpan;
    }
    LinkedHashSet localLinkedHashSet = new LinkedHashSet();
    Iterator localIterator = paramSpan.mAlternates.iterator();
    while (localIterator.hasNext()) {
      localLinkedHashSet.add(format((String)localIterator.next()));
    }
    return paramSpan.withAlternates(ImmutableList.copyOf(localLinkedHashSet));
  }
  
  private boolean isAtBeginning(ExtractedText paramExtractedText)
  {
    return paramExtractedText.selectionStart + paramExtractedText.startOffset == 0;
  }
  
  private boolean isPrevCharForceUppercase(ExtractedText paramExtractedText)
  {
    for (int i = -1 + (paramExtractedText.selectionStart + paramExtractedText.startOffset); (i > 0) && (isSkipChar(paramExtractedText.text.charAt(i))); i--) {}
    return this.upperCaseChars.contains(Character.valueOf(paramExtractedText.text.charAt(i)));
  }
  
  private boolean isPrevTextForceUppercase(ExtractedText paramExtractedText)
  {
    if (!(paramExtractedText.text instanceof Spanned)) {}
    for (;;)
    {
      return false;
      Spanned localSpanned = (Spanned)paramExtractedText.text;
      for (Annotation localAnnotation : (Annotation[])localSpanned.getSpans(0, paramExtractedText.selectionStart + paramExtractedText.startOffset, Annotation.class)) {
        if ((localSpanned.getSpanEnd(localAnnotation) == paramExtractedText.selectionStart + paramExtractedText.startOffset) && (TextUtil.isForceUppercase(localAnnotation))) {
          return true;
        }
      }
    }
  }
  
  private boolean isSkipChar(char paramChar)
  {
    return (paramChar != '\n') && (Character.isWhitespace(paramChar));
  }
  
  public Hypothesis format(Hypothesis paramHypothesis)
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(paramHypothesis.getSpanCount());
    Iterator localIterator = paramHypothesis.getSpans().iterator();
    while (localIterator.hasNext()) {
      localArrayList.add(formatHypothesisSpan((Hypothesis.Span)localIterator.next()));
    }
    return Hypothesis.fromSpans(format(paramHypothesis.getText()), localArrayList);
  }
  
  public String format(String paramString)
  {
    if (!this.mFirstCharUppercase) {
      return paramString;
    }
    return forceFirstCharUppercase(paramString);
  }
  
  public void handleCommit(InputConnection paramInputConnection, ExtractedText paramExtractedText)
  {
    if (!this.mFirstCommit) {}
    while (paramExtractedText == null) {
      return;
    }
    int i = paramExtractedText.selectionStart;
    int j = i;
    if (i < 0)
    {
      Log.e("LatinTextFormatter", "Invalid selection start :" + i);
      return;
    }
    if ((i < paramExtractedText.text.length()) && (!Character.isWhitespace(paramExtractedText.text.charAt(i))))
    {
      if (DEBUG) {
        Log.d("LatinTextFormatter", "Add space before");
      }
      paramInputConnection.commitText(" ", 0);
    }
    if ((i > 0) && (!Character.isWhitespace(paramExtractedText.text.charAt(i - 1))))
    {
      if (DEBUG) {
        Log.d("LatinTextFormatter", "Add space after");
      }
      paramInputConnection.commitText(" ", 1);
      j++;
    }
    paramInputConnection.setSelection(j, j);
    this.mFirstCommit = false;
  }
  
  public void reset()
  {
    this.mFirstCharUppercase = false;
  }
  
  public void startDictation(ExtractedText paramExtractedText)
  {
    boolean bool = true;
    this.mFirstCommit = bool;
    this.mFirstCharUppercase = false;
    if (paramExtractedText == null) {
      return;
    }
    for (;;)
    {
      try
      {
        if ((isAtBeginning(paramExtractedText)) || (isPrevCharForceUppercase(paramExtractedText)) || (isPrevTextForceUppercase(paramExtractedText)))
        {
          this.mFirstCharUppercase = bool;
          return;
        }
      }
      catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
      {
        BugLogger.record(6309164);
        this.mFirstCharUppercase = false;
        return;
      }
      bool = false;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.ime.formatter.LatinTextFormatter
 * JD-Core Version:    0.7.0.1
 */