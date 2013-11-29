package com.google.android.speech.embedded;

import com.google.speech.grammar.pumpkin.Validator;

public class PumpkinDummyValidator
  extends Validator
{
  public float getPosterior(String paramString)
  {
    return PumpkinTagger.moreTokensHigherProbability(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.embedded.PumpkinDummyValidator
 * JD-Core Version:    0.7.0.1
 */