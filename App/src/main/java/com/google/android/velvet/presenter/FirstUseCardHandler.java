package com.google.android.velvet.presenter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.android.shared.util.Clock;

public class FirstUseCardHandler
{
  private final Clock mClock;
  private final SharedPreferences mPrefs;
  
  public FirstUseCardHandler(SharedPreferences paramSharedPreferences, Clock paramClock)
  {
    this.mPrefs = paramSharedPreferences;
    this.mClock = paramClock;
  }
  
  private boolean isDismissed(FirstUseCardType paramFirstUseCardType)
  {
    return this.mPrefs.getBoolean(paramFirstUseCardType.getDismissedPrefKey(), false);
  }
  
  public void recordBackOfCardShown()
  {
    this.mPrefs.edit().putBoolean("back_of_card_shown", true).apply();
  }
  
  public void recordCardSwipedForDismiss()
  {
    this.mPrefs.edit().putBoolean("card_swiped_for_dismiss", true).apply();
  }
  
  public void recordDismiss(FirstUseCardType paramFirstUseCardType)
  {
    this.mPrefs.edit().putBoolean(paramFirstUseCardType.getDismissedPrefKey(), true).apply();
    recordCardSwipedForDismiss();
  }
  
  public void recordTrainingQuestionAnswered()
  {
    this.mPrefs.edit().putBoolean("training_question_answered", true).apply();
  }
  
  public void recordView(FirstUseCardType paramFirstUseCardType)
  {
    String str = paramFirstUseCardType.getFirstViewPrefKey();
    long l = this.mClock.currentTimeMillis();
    this.mPrefs.edit().putLong(str, l).apply();
  }
  
  public boolean shouldShowBackOfCardTutorialCard()
  {
    if (isDismissed(FirstUseCardType.BACK_OF_CARD_TUTORIAL_CARD)) {}
    while (this.mPrefs.getBoolean("back_of_card_shown", false)) {
      return false;
    }
    return this.mPrefs.getBoolean("training_question_answered", false);
  }
  
  public boolean shouldShowFirstUseCard(FirstUseCardType paramFirstUseCardType)
  {
    if (isDismissed(paramFirstUseCardType)) {}
    long l;
    do
    {
      return false;
      String str = paramFirstUseCardType.getFirstViewPrefKey();
      l = this.mPrefs.getLong(str, 0L);
      if (l == 0L) {
        break;
      }
    } while (this.mClock.currentTimeMillis() - l >= 5000L);
    return true;
    return true;
  }
  
  public boolean shouldShowSwipeTutorialCard(int paramInt)
  {
    if (this.mPrefs.getBoolean("card_swiped_for_dismiss", false)) {}
    while ((paramInt < 3) || (this.mPrefs.getLong(FirstUseCardType.INTRO_CARD.getFirstViewPrefKey(), 0L) == 0L)) {
      return false;
    }
    return true;
  }
  
  public static enum FirstUseCardType
  {
    private final String mPrefPrefix;
    
    static
    {
      BACK_OF_CARD_TUTORIAL_CARD = new FirstUseCardType("BACK_OF_CARD_TUTORIAL_CARD", 3, "backofcard_");
      FirstUseCardType[] arrayOfFirstUseCardType = new FirstUseCardType[4];
      arrayOfFirstUseCardType[0] = INTRO_CARD;
      arrayOfFirstUseCardType[1] = OUTRO_CARD;
      arrayOfFirstUseCardType[2] = SWIPE_TUTORIAL_CARD;
      arrayOfFirstUseCardType[3] = BACK_OF_CARD_TUTORIAL_CARD;
      $VALUES = arrayOfFirstUseCardType;
    }
    
    private FirstUseCardType(String paramString)
    {
      this.mPrefPrefix = paramString;
    }
    
    public static FirstUseCardType getByOrdinal(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return null;
      case 0: 
        return INTRO_CARD;
      case 1: 
        return OUTRO_CARD;
      case 2: 
        return SWIPE_TUTORIAL_CARD;
      }
      return BACK_OF_CARD_TUTORIAL_CARD;
    }
    
    public String getDismissedPrefKey()
    {
      return this.mPrefPrefix + "card_dismissed";
    }
    
    public String getFirstViewPrefKey()
    {
      return this.mPrefPrefix + "card_first_view";
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.FirstUseCardHandler
 * JD-Core Version:    0.7.0.1
 */