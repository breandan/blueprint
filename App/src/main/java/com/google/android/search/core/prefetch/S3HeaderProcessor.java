package com.google.android.search.core.prefetch;

import android.util.Log;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.SplitIterator;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;

public class S3HeaderProcessor
{
  private boolean mComplete;
  private Consumer<Map<String, List<String>>> mConsumer;
  private final AtomicBoolean mConsumerWasNotified = new AtomicBoolean(false);
  private volatile RecognizeException mError;
  private String mFirstHeaderFragment;
  private final StringBuilder mHeaderBuilder = new StringBuilder(2048);
  private volatile Map<String, List<String>> mHeaders;
  private volatile int mResponseCode = -1;
  
  @Nonnull
  private String getCompleteHeaders()
  {
    Preconditions.checkState(this.mComplete);
    if (this.mHeaderBuilder.length() > 0) {
      return this.mHeaderBuilder.toString();
    }
    return (String)Preconditions.checkNotNull(this.mFirstHeaderFragment);
  }
  
  static void parseResponseHeader(String paramString, Map<String, List<String>> paramMap)
  {
    int i = paramString.indexOf(':');
    if (i < 1)
    {
      Log.e("Velvet.S3HeaderProcessor", "Skipping invalid header: " + paramString);
      return;
    }
    String str1 = paramString.substring(0, i).trim();
    String str2 = paramString.substring(i + 1).trim();
    if ((str1.isEmpty()) || (str2.isEmpty()))
    {
      Log.e("Velvet.S3HeaderProcessor", "Skipping invalid header: " + paramString);
      return;
    }
    if (paramMap.containsKey(str1))
    {
      ((List)paramMap.get(str1)).add(str2);
      return;
    }
    paramMap.put(str1, Lists.newArrayList(new String[] { str2 }));
  }
  
  static int parseStatusLine(String paramString)
  {
    int i;
    if ((paramString == null) || (!paramString.startsWith("HTTP/")))
    {
      Log.e("Velvet.S3HeaderProcessor", "Invalid status line: " + paramString);
      i = -1;
    }
    int k;
    do
    {
      return i;
      int j = 1 + paramString.indexOf(" ");
      if (j == 0)
      {
        Log.e("Velvet.S3HeaderProcessor", "Invalid status line: " + paramString);
        return -1;
      }
      if (paramString.charAt(j - 2) != '1') {}
      k = j + 3;
      if (k > paramString.length()) {
        k = paramString.length();
      }
      i = Integer.parseInt(paramString.substring(j, k));
    } while (k + 1 > paramString.length());
    paramString.substring(k + 1);
    return i;
  }
  
  void appendHeaderFragment(@Nonnull String paramString)
  {
    if (!this.mComplete) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      Preconditions.checkNotNull(paramString);
      if (this.mFirstHeaderFragment != null) {
        break;
      }
      this.mFirstHeaderFragment = paramString;
      return;
    }
    if (this.mHeaderBuilder.length() == 0) {
      this.mHeaderBuilder.append(this.mFirstHeaderFragment);
    }
    this.mHeaderBuilder.append(paramString);
  }
  
  Map<String, List<String>> getHeaders()
  {
    return this.mHeaders;
  }
  
  int getResponseCode()
  {
    return this.mResponseCode;
  }
  
  void notifyConsumerIfFirstTime()
  {
    Preconditions.checkState(this.mComplete);
    if (this.mConsumerWasNotified.compareAndSet(false, true))
    {
      if (this.mError != null)
      {
        Log.w("Velvet.S3HeaderProcessor", "Error parsing response headers: " + this.mError.getMessage());
        this.mConsumer.consume(null);
      }
      this.mConsumer.consume(this.mHeaders);
    }
  }
  
  void processHeaders()
  {
    if (this.mComplete) {}
    HashMap localHashMap;
    SplitIterator localSplitIterator;
    do
    {
      return;
      this.mComplete = true;
      localHashMap = Maps.newHashMap();
      localSplitIterator = SplitIterator.splitOnCharTrimOmitEmptyStrings(getCompleteHeaders(), '\n');
      if (!localSplitIterator.hasNext()) {
        break label103;
      }
      this.mResponseCode = parseStatusLine((String)localSplitIterator.next());
      if (this.mResponseCode == 200) {
        break;
      }
    } while (this.mResponseCode != 302);
    BugLogger.record(9460428);
    return;
    while (localSplitIterator.hasNext()) {
      parseResponseHeader((String)localSplitIterator.next(), localHashMap);
    }
    label103:
    Log.e("Velvet.S3HeaderProcessor", "Malformed headers: no status line");
    if (localHashMap.isEmpty()) {
      localHashMap = null;
    }
    this.mHeaders = localHashMap;
  }
  
  void reportError(RecognizeException paramRecognizeException)
  {
    this.mError = paramRecognizeException;
    this.mComplete = true;
    notifyConsumerIfFirstTime();
  }
  
  void setConsumer(@Nonnull Consumer<Map<String, List<String>>> paramConsumer)
  {
    if (this.mConsumer == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mConsumer = ((Consumer)Preconditions.checkNotNull(paramConsumer));
      return;
    }
  }
  
  public String toString()
  {
    return "S3HeaderProcessor{mComplete:" + this.mComplete + ", mResponseCode:" + this.mResponseCode + "}";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.S3HeaderProcessor
 * JD-Core Version:    0.7.0.1
 */