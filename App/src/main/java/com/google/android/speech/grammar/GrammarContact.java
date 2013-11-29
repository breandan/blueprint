package com.google.android.speech.grammar;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.Nonnull;

public class GrammarContact
{
  private static final Joiner SPACE_JOINER = Joiner.on(" ");
  @Nonnull
  final String displayName;
  final long lastTimeContactedElapsed;
  final int timesContacted;
  double weight;
  
  public GrammarContact(@Nonnull String paramString, int paramInt, long paramLong)
  {
    this.displayName = ((String)Preconditions.checkNotNull(paramString));
    this.timesContacted = paramInt;
    this.lastTimeContactedElapsed = paramLong;
  }
  
  private void addToken(String paramString, double paramDouble, Map<String, GrammarToken> paramMap)
  {
    if (paramMap.containsKey(paramString))
    {
      ((GrammarToken)paramMap.get(paramString)).add(paramDouble);
      return;
    }
    paramMap.put(paramString, new GrammarToken(paramString, paramDouble));
  }
  
  public void addTokens(Map<String, GrammarToken> paramMap)
  {
    String[] arrayOfString = GrammarBuilder.getWords(GrammarBuilder.stripAbnfTokens(this.displayName));
    if ((arrayOfString == null) || (arrayOfString.length == 0)) {}
    do
    {
      return;
      addToken(arrayOfString[0], this.weight, paramMap);
    } while (arrayOfString.length <= 1);
    addToken(arrayOfString[(-1 + arrayOfString.length)], this.weight, paramMap);
    addToken(SPACE_JOINER.join(arrayOfString), this.weight, paramMap);
  }
  
  public String toString()
  {
    return "GrammarContact[" + this.displayName + ",#" + this.timesContacted + ",last-time=" + this.lastTimeContactedElapsed + ",weigth=" + this.weight + "]";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.grammar.GrammarContact
 * JD-Core Version:    0.7.0.1
 */