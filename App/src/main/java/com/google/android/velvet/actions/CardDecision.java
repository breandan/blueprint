package com.google.android.velvet.actions;

import com.google.android.velvet.ActionData;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public class CardDecision
{
  public static final CardDecision NETWORK_DECISION = new CardDecision(null, null, false, false, false, false, 0L, 0);
  public static final CardDecision SUPPRESS_NETWORK_DECISION = new CardDecision(null, null, false, false, true, false, 0L, 0);
  private final long mCountDownDurationMs;
  private final String mDisplayPrompt;
  private final boolean mOverrideNetworkDecision;
  private final boolean mPlayTts;
  private final int mPromptedField;
  private final boolean mShouldAutoExecute;
  private final boolean mStartFollowOnVoiceSearch;
  private final String mVocalizedPrompt;
  
  public CardDecision(ActionData paramActionData, int paramInt) {}
  
  private CardDecision(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, long paramLong, int paramInt)
  {
    this.mDisplayPrompt = paramString1;
    this.mVocalizedPrompt = paramString2;
    this.mPlayTts = paramBoolean1;
    this.mStartFollowOnVoiceSearch = paramBoolean2;
    this.mOverrideNetworkDecision = paramBoolean3;
    this.mShouldAutoExecute = paramBoolean4;
    this.mCountDownDurationMs = paramLong;
    this.mPromptedField = paramInt;
  }
  
  public static Builder noPrompt()
  {
    return new Builder();
  }
  
  public static Builder prompt(String paramString1, String paramString2, int paramInt)
  {
    return new Builder().visualPrompt(paramString1, paramInt).vocalizedPrompt(paramString2, paramInt);
  }
  
  public static Builder visualPrompt(String paramString, int paramInt)
  {
    return new Builder().visualPrompt(paramString, paramInt);
  }
  
  public long getCountDownDurationMs()
  {
    return this.mCountDownDurationMs;
  }
  
  @Nullable
  public String getDisplayPrompt()
  {
    return this.mDisplayPrompt;
  }
  
  public int getPromptedField()
  {
    return this.mPromptedField;
  }
  
  @Nullable
  public String getVocalizedPrompt()
  {
    return this.mVocalizedPrompt;
  }
  
  public boolean shouldAutoExecute()
  {
    return this.mShouldAutoExecute;
  }
  
  public boolean shouldOverrideNetworkDecision()
  {
    return this.mOverrideNetworkDecision;
  }
  
  public boolean shouldPlayTts()
  {
    return this.mPlayTts;
  }
  
  public boolean shouldStartFollowOnVoiceSearch()
  {
    return this.mStartFollowOnVoiceSearch;
  }
  
  public String toString()
  {
    return "CardDecision[OverrideNetworkPrompt: " + this.mOverrideNetworkDecision + ", StartFollowOnVoiceSearch: " + this.mStartFollowOnVoiceSearch + ", PlayTts: " + this.mPlayTts + ", DisplayPrompt: " + this.mDisplayPrompt + ", VocalizedPrompt: " + this.mVocalizedPrompt + ", PromptedField: " + this.mPromptedField + "]";
  }
  
  public static class Builder
  {
    private long mCountDownDurationMs = 0L;
    private String mDisplayPrompt = null;
    private boolean mOverrideNetworkDecision = false;
    private boolean mPlayTts = false;
    private int mPromptedField = 0;
    private boolean mShouldAutoExecute = false;
    private boolean mStartFollowOnVoiceSearch = false;
    private String mVocalizedPrompt = null;
    
    public Builder autoExecute(long paramLong)
    {
      this.mShouldAutoExecute = true;
      this.mCountDownDurationMs = paramLong;
      return this;
    }
    
    public CardDecision build()
    {
      return new CardDecision(this.mDisplayPrompt, this.mVocalizedPrompt, this.mPlayTts, this.mStartFollowOnVoiceSearch, this.mOverrideNetworkDecision, this.mShouldAutoExecute, this.mCountDownDurationMs, this.mPromptedField, null);
    }
    
    public Builder overrideNetworkDecision()
    {
      this.mOverrideNetworkDecision = true;
      return this;
    }
    
    public Builder prompt(String paramString1, String paramString2, int paramInt)
    {
      this.mDisplayPrompt = ((String)Preconditions.checkNotNull(paramString1));
      this.mVocalizedPrompt = ((String)Preconditions.checkNotNull(paramString2));
      this.mPromptedField = paramInt;
      this.mPlayTts = true;
      return this;
    }
    
    public Builder startFollowOn()
    {
      this.mStartFollowOnVoiceSearch = true;
      return this;
    }
    
    public Builder visualPrompt(String paramString, int paramInt)
    {
      this.mDisplayPrompt = ((String)Preconditions.checkNotNull(paramString));
      this.mPromptedField = paramInt;
      return this;
    }
    
    public Builder vocalizedPrompt(String paramString, int paramInt)
    {
      this.mVocalizedPrompt = paramString;
      this.mPromptedField = paramInt;
      this.mPlayTts = true;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.actions.CardDecision
 * JD-Core Version:    0.7.0.1
 */