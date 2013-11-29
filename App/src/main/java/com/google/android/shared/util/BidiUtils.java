package com.google.android.shared.util;

import android.support.v4.text.BidiFormatter;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import java.text.NumberFormat;
import java.util.Locale;

public class BidiUtils
{
  private static final Locale ARABIC = new Locale("ar");
  private static Locale sDefaultLocale = Locale.getDefault();
  private static BidiFormatter sFormatter = BidiFormatter.getInstance();
  
  public static String formatAndUnicodeWrapNumber(NumberFormat paramNumberFormat, double paramDouble)
  {
    return getFormatter().unicodeWrap(paramNumberFormat.format(paramDouble), TextDirectionHeuristicsCompat.LTR);
  }
  
  public static String formatAndUnicodeWrapPercent(double paramDouble, int paramInt1, int paramInt2)
  {
    NumberFormat localNumberFormat = NumberFormat.getPercentInstance();
    localNumberFormat.setMinimumFractionDigits(paramInt1);
    localNumberFormat.setMaximumFractionDigits(paramInt2);
    if (ARABIC.getLanguage().equals(Locale.getDefault().getLanguage())) {}
    for (TextDirectionHeuristicCompat localTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;; localTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR) {
      return getFormatter().unicodeWrap(localNumberFormat.format(paramDouble / 100.0D), localTextDirectionHeuristicCompat);
    }
  }
  
  public static BidiFormatter getFormatter()
  {
    Locale localLocale = Locale.getDefault();
    if (!sDefaultLocale.equals(localLocale))
    {
      sDefaultLocale = localLocale;
      sFormatter = BidiFormatter.getInstance(localLocale);
    }
    return sFormatter;
  }
  
  public static String unicodeWrap(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return getFormatter().unicodeWrap(paramString);
  }
  
  public static String unicodeWrapLtr(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return getFormatter().unicodeWrap(paramString, TextDirectionHeuristicsCompat.LTR);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.BidiUtils
 * JD-Core Version:    0.7.0.1
 */