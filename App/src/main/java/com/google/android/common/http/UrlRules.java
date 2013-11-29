package com.google.android.common.http;

import android.content.ContentResolver;
import android.util.Log;
import com.google.android.gsf.Gservices;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlRules
{
  private static UrlRules sCachedRules = new UrlRules(new Rule[0]);
  static Object sCachedVersionToken;
  private final Pattern mPattern;
  private final Rule[] mRules;
  
  public UrlRules(Rule[] paramArrayOfRule)
  {
    Arrays.sort(paramArrayOfRule);
    StringBuilder localStringBuilder = new StringBuilder("(\\Q");
    for (int i = 0; i < paramArrayOfRule.length; i++)
    {
      if (i > 0) {
        localStringBuilder.append("\\E)|(\\Q");
      }
      String str = paramArrayOfRule[i].mPrefix;
      int k;
      for (int j = 0;; j = k + 2)
      {
        k = str.indexOf("\\E", j);
        if (k == -1) {
          break;
        }
        localStringBuilder.append(str, j, k + 2).append("\\\\E\\Q");
      }
      localStringBuilder.append(str, j, str.length());
    }
    this.mPattern = Pattern.compile("\\E)");
    this.mRules = paramArrayOfRule;
  }
  
  public static UrlRules getRules(ContentResolver paramContentResolver)
  {
    boolean bool;
    Object localObject2;
    UrlRules localUrlRules;
    Object localObject3;
    int i;
    try
    {
      bool = Log.isLoggable("UrlRules", 2);
      localObject2 = Gservices.getVersionToken(paramContentResolver);
      if (localObject2 == sCachedVersionToken)
      {
        if (bool) {
          Log.v("UrlRules", "Using cached rules, versionToken: " + localObject2);
        }
        localUrlRules = sCachedRules;
        return localUrlRules;
      }
      if (bool) {
        Log.v("UrlRules", "Scanning for Gservices \"url:*\" rules");
      }
      Map localMap = Gservices.getStringsByPrefix(paramContentResolver, new String[] { "url:" });
      localObject3 = new Rule[localMap.size()];
      i = 0;
      Iterator localIterator = localMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        try
        {
          String str1 = ((String)localEntry.getKey()).substring(4);
          String str2 = (String)localEntry.getValue();
          if ((str2 != null) && (str2.length() != 0))
          {
            if (bool) {
              Log.v("UrlRules", "  Rule " + str1 + ": " + str2);
            }
            localObject3[i] = new Rule(str1, str2);
            i++;
          }
        }
        catch (RuleFormatException localRuleFormatException)
        {
          Log.e("UrlRules", "Invalid rule from Gservices", localRuleFormatException);
        }
      }
      if (localObject3.length != i) {}
    }
    finally {}
    for (;;)
    {
      sCachedRules = new UrlRules((Rule[])localObject3);
      sCachedVersionToken = localObject2;
      if (bool) {
        Log.v("UrlRules", "New rules stored, versionToken: " + localObject2);
      }
      localUrlRules = sCachedRules;
      break;
      Rule[] arrayOfRule = (Rule[])Arrays.copyOf((Object[])localObject3, i);
      localObject3 = arrayOfRule;
    }
  }
  
  public Rule matchRule(String paramString)
  {
    Matcher localMatcher = this.mPattern.matcher(paramString);
    if (localMatcher.lookingAt()) {
      for (int i = 0; i < this.mRules.length; i++) {
        if (localMatcher.group(i + 1) != null) {
          return this.mRules[i];
        }
      }
    }
    return Rule.DEFAULT;
  }
  
  public static class Rule
    implements Comparable<Rule>
  {
    public static final Rule DEFAULT = new Rule();
    public final boolean mBlock;
    public final String mName;
    public final String mPrefix;
    public final String mRewrite;
    
    private Rule()
    {
      this.mName = "DEFAULT";
      this.mPrefix = "";
      this.mRewrite = null;
      this.mBlock = false;
    }
    
    public Rule(String paramString1, String paramString2)
      throws UrlRules.RuleFormatException
    {
      this.mName = paramString1;
      int i = paramString2.length();
      int j = indexOfNonSpace(paramString2, 0);
      if (j == i) {
        throw new UrlRules.RuleFormatException("Empty rule");
      }
      if (j != 0) {
        throw new UrlRules.RuleFormatException("Rule with leading whitespace: " + paramString2);
      }
      int k = indexOfSpace(paramString2, j + 1);
      this.mPrefix = paramString2.substring(j, k);
      String str = null;
      boolean bool = false;
      while (k != i)
      {
        int m = indexOfNonSpace(paramString2, k + 1);
        if (m != i)
        {
          k = indexOfSpace(paramString2, m + 1);
          int n = k - m;
          if (("rewrite".length() == n) && ("rewrite".regionMatches(true, 0, paramString2, m, n)) && (k != i))
          {
            int i1 = indexOfNonSpace(paramString2, k + 1);
            if (i1 != i)
            {
              k = indexOfSpace(paramString2, i1 + 1);
              str = paramString2.substring(i1, k);
              continue;
            }
          }
          if (("block".length() == n) && ("block".regionMatches(true, 0, paramString2, m, n))) {
            bool = true;
          } else {
            throw new UrlRules.RuleFormatException("Illegal rule: " + paramString2);
          }
        }
      }
      this.mRewrite = str;
      this.mBlock = bool;
    }
    
    private static final int indexOfNonSpace(String paramString, int paramInt)
    {
      int i = paramString.length();
      while ((paramInt != i) && (paramString.charAt(paramInt) == ' ')) {
        paramInt++;
      }
      return paramInt;
    }
    
    private static final int indexOfSpace(String paramString, int paramInt)
    {
      int i = paramString.indexOf(' ', paramInt);
      if (i != -1) {
        return i;
      }
      return paramString.length();
    }
    
    public String apply(String paramString)
    {
      if (this.mBlock) {
        paramString = null;
      }
      while (this.mRewrite == null) {
        return paramString;
      }
      return this.mRewrite + paramString.substring(this.mPrefix.length());
    }
    
    public int compareTo(Rule paramRule)
    {
      return paramRule.mPrefix.compareTo(this.mPrefix);
    }
  }
  
  public static class RuleFormatException
    extends Exception
  {
    public RuleFormatException(String paramString)
    {
      super();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.common.http.UrlRules
 * JD-Core Version:    0.7.0.1
 */