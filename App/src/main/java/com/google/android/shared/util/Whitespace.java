package com.google.android.shared.util;

public class Whitespace
{
  static String collapseRangeFrom(CharSequence paramCharSequence, int paramInt1, int paramInt2, char paramChar)
  {
    int i = indexOfNonMatchIn(paramCharSequence, paramInt1, paramInt2);
    if (i == -1)
    {
      if (paramInt2 == paramInt1) {
        return "";
      }
      return String.valueOf(paramChar);
    }
    StringBuilder localStringBuilder = null;
    if (i != paramInt1)
    {
      if (paramCharSequence.charAt(i - 1) == paramChar) {
        break label103;
      }
      localStringBuilder = new StringBuilder(1 + (paramInt2 - i)).append(paramChar);
    }
    for (;;)
    {
      int j = indexIn(paramCharSequence, i + 1, paramInt2);
      if (j == -1)
      {
        if (localStringBuilder == null)
        {
          return paramCharSequence.subSequence(paramInt1, paramInt2).toString();
          label103:
          paramInt1 = i - 1;
          localStringBuilder = null;
        }
        else
        {
          return localStringBuilder.append(paramCharSequence, i, paramInt2).toString();
        }
      }
      else
      {
        if (localStringBuilder != null) {
          localStringBuilder.append(paramCharSequence, i, j);
        }
        i = indexOfNonMatchIn(paramCharSequence, j + 1, paramInt2);
        if (i == -1)
        {
          if (localStringBuilder == null)
          {
            if (paramCharSequence.charAt(j) == paramChar) {
              return paramCharSequence.subSequence(paramInt1, j + 1).toString();
            }
            localStringBuilder = new StringBuilder(1 + (j - paramInt1)).append(paramCharSequence, paramInt1, j);
          }
          return paramChar;
        }
        if ((localStringBuilder == null) && ((i - j != 1) || (paramCharSequence.charAt(j) != paramChar))) {
          localStringBuilder = new StringBuilder(paramInt2 - paramInt1).append(paramCharSequence, paramInt1, j);
        }
        if (localStringBuilder != null) {
          localStringBuilder.append(paramChar);
        }
      }
    }
  }
  
  static int indexIn(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; i < paramInt2; i++) {
      if (matches(paramCharSequence.charAt(i))) {
        return i;
      }
    }
    return -1;
  }
  
  static int indexOfNonMatchIn(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; i < paramInt2; i++) {
      if (!matches(paramCharSequence.charAt(i))) {
        return i;
      }
    }
    return -1;
  }
  
  static int lastIndexOfNonMatchIn(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    for (int i = paramInt2 - 1; i >= paramInt1; i--) {
      if (!matches(paramCharSequence.charAt(i))) {
        return i;
      }
    }
    return -1;
  }
  
  public static boolean matches(char paramChar)
  {
    return Character.isWhitespace(paramChar);
  }
  
  public static boolean matchesAllOf(CharSequence paramCharSequence)
  {
    int i = indexOfNonMatchIn(paramCharSequence, 0, paramCharSequence.length());
    boolean bool = false;
    if (i == -1) {
      bool = true;
    }
    return bool;
  }
  
  public static String trimAndCollapseFrom(CharSequence paramCharSequence, char paramChar)
  {
    int i = indexOfNonMatchIn(paramCharSequence, 0, paramCharSequence.length());
    if (i == -1) {
      return "";
    }
    return collapseRangeFrom(paramCharSequence, i, 1 + lastIndexOfNonMatchIn(paramCharSequence, 0, paramCharSequence.length()), paramChar);
  }
  
  public static String trimLeadingAndCollapseFrom(CharSequence paramCharSequence, char paramChar)
  {
    int i = indexOfNonMatchIn(paramCharSequence, 0, paramCharSequence.length());
    if (i == -1) {
      return "";
    }
    return collapseRangeFrom(paramCharSequence, i, paramCharSequence.length(), paramChar);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.Whitespace
 * JD-Core Version:    0.7.0.1
 */