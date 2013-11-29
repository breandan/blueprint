package com.google.android.search.shared.ui;

import android.text.SpannableString;
import android.text.Spanned;

public class LevenshteinSuggestionFormatter
  extends SuggestionFormatter
{
  public LevenshteinSuggestionFormatter(TextAppearanceFactory paramTextAppearanceFactory)
  {
    super(paramTextAppearanceFactory);
  }
  
  private String normalizeQuery(String paramString)
  {
    return paramString.toLowerCase();
  }
  
  int[] findMatches(LevenshteinDistance.Token[] paramArrayOfToken1, LevenshteinDistance.Token[] paramArrayOfToken2)
  {
    LevenshteinDistance localLevenshteinDistance = new LevenshteinDistance(paramArrayOfToken1, paramArrayOfToken2);
    localLevenshteinDistance.calculate();
    int i = paramArrayOfToken2.length;
    int[] arrayOfInt = new int[i];
    LevenshteinDistance.EditOperation[] arrayOfEditOperation = localLevenshteinDistance.getTargetOperations();
    int j = 0;
    if (j < i)
    {
      if (arrayOfEditOperation[j].getType() == 3) {
        arrayOfInt[j] = arrayOfEditOperation[j].getPosition();
      }
      for (;;)
      {
        j++;
        break;
        arrayOfInt[j] = -1;
      }
    }
    return arrayOfInt;
  }
  
  protected Spanned formatSuggestion(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    SpannableString localSpannableString = new SpannableString(paramString2);
    if (paramString1.isEmpty()) {
      applyTextStyle(paramInt1, localSpannableString, 0, localSpannableString.length());
    }
    for (;;)
    {
      return localSpannableString;
      LevenshteinDistance.Token[] arrayOfToken1 = tokenize(normalizeQuery(paramString1));
      LevenshteinDistance.Token[] arrayOfToken2 = tokenize(paramString2);
      int[] arrayOfInt = findMatches(arrayOfToken1, arrayOfToken2);
      int i = arrayOfInt.length;
      for (int j = 0; j < i; j++)
      {
        LevenshteinDistance.Token localToken = arrayOfToken2[j];
        int k = arrayOfInt[j];
        int m = 0;
        if (k >= 0) {
          m = arrayOfToken1[k].length();
        }
        applyTextStyle(paramInt2, localSpannableString, m + localToken.mStart, localToken.mEnd);
        applyTextStyle(paramInt1, localSpannableString, localToken.mStart, m + localToken.mStart);
      }
    }
  }
  
  LevenshteinDistance.Token[] tokenize(String paramString)
  {
    int i = 0;
    int j = paramString.length();
    char[] arrayOfChar = paramString.toCharArray();
    LevenshteinDistance.Token[] arrayOfToken1 = new LevenshteinDistance.Token[j];
    int k = 0;
    int i1;
    if (i < j)
    {
      while ((i < j) && ((arrayOfChar[i] == ' ') || (arrayOfChar[i] == '\t') || (arrayOfChar[i] == '"'))) {
        i++;
      }
      int m = i;
      while ((i < j) && (arrayOfChar[i] != ' ') && (arrayOfChar[i] != '\t') && (arrayOfChar[i] != '"')) {
        i++;
      }
      int n = i;
      if (m == n) {
        break label168;
      }
      i1 = k + 1;
      arrayOfToken1[k] = new LevenshteinDistance.Token(arrayOfChar, m, n);
    }
    for (;;)
    {
      k = i1;
      break;
      LevenshteinDistance.Token[] arrayOfToken2 = new LevenshteinDistance.Token[k];
      System.arraycopy(arrayOfToken1, 0, arrayOfToken2, 0, k);
      return arrayOfToken2;
      label168:
      i1 = k;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.LevenshteinSuggestionFormatter
 * JD-Core Version:    0.7.0.1
 */