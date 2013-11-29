package com.google.android.shared.util;

import android.util.Printer;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import javax.annotation.Nullable;

public class PrefixPrinter
  implements Printer
{
  private final Printer mDelegate;
  private String mPrefix;
  
  public PrefixPrinter(Printer paramPrinter)
  {
    this(paramPrinter, "");
  }
  
  public PrefixPrinter(Printer paramPrinter, @Nullable String paramString)
  {
    this.mDelegate = ((Printer)Preconditions.checkNotNull(paramPrinter));
    this.mPrefix = Strings.nullToEmpty(paramString);
  }
  
  public void addToPrefix(String paramString)
  {
    this.mPrefix += paramString;
  }
  
  public void println(String paramString)
  {
    this.mDelegate.println(this.mPrefix + paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.PrefixPrinter
 * JD-Core Version:    0.7.0.1
 */