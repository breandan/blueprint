package com.google.android.velvet.actions;

import com.google.common.base.Preconditions;

public class TtsConstants
{
  public static final DisambiguationConstants CALL_DISAMBIG;
  public static final TtsConstants CALL_GET_NAME;
  public static final TtsConstants EMAIL_CONFIRMATION;
  public static final DisambiguationConstants EMAIL_DISAMBIG;
  public static final TtsConstants EMAIL_GET_NAME = new TtsConstants(2131363256, new int[] { 2131363257, 2131363258, 2131363259, 2131363260 });
  public static final DisambiguationConstants PERSON_DISAMBIG = new DisambiguationConstants(new TtsConstants(2131363267, new int[] { 2131363268, 2131363269, 2131363270 }), null, null, null);
  public static final TtsConstants SMS_CONFIRMATION;
  public static final DisambiguationConstants SMS_DISAMBIG;
  public static final TtsConstants SMS_GET_NAME;
  private final int mDisplayResId;
  private final int[] mTtsResIds;
  
  static
  {
    CALL_GET_NAME = new TtsConstants(2131363238, new int[] { 2131363239, 2131363240, 2131363259, 2131363241 });
    SMS_GET_NAME = new TtsConstants(2131363245, new int[] { 2131363246, 2131363247, 2131363259, 2131363248 });
    EMAIL_CONFIRMATION = new TtsConstants(2131363263, new int[] { 2131363401, 2131363401, 2131363407 });
    SMS_CONFIRMATION = new TtsConstants(2131363251, new int[] { 2131363403, 2131363403, 2131363408 });
    EMAIL_DISAMBIG = new DisambiguationConstants(new TtsConstants(2131363267, new int[] { 2131363268, 2131363269, 2131363260 }), new TtsConstants(2131363271, new int[] { 2131363272, 2131363275, 2131363276 }), new TtsConstants(2131363279, new int[] { 2131363279, 2131363280, 2131363276 }), null);
    CALL_DISAMBIG = new DisambiguationConstants(new TtsConstants(2131363267, new int[] { 2131363268, 2131363269, 2131363241 }), new TtsConstants(2131363271, new int[] { 2131363272, 2131363273, 2131363274 }), new TtsConstants(2131363277, new int[] { 2131363277, 2131363278, 2131363274 }), null);
    SMS_DISAMBIG = new DisambiguationConstants(new TtsConstants(2131363267, new int[] { 2131363268, 2131363269, 2131363248 }), new TtsConstants(2131363271, new int[] { 2131363272, 2131363273, 2131363274 }), new TtsConstants(2131363277, new int[] { 2131363277, 2131363278, 2131363274 }), null);
  }
  
  private TtsConstants(int paramInt, int[] paramArrayOfInt)
  {
    this.mDisplayResId = paramInt;
    this.mTtsResIds = paramArrayOfInt;
  }
  
  public int getDisplayText()
  {
    return this.mDisplayResId;
  }
  
  public int getTts(int paramInt)
  {
    if (paramInt >= 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      return this.mTtsResIds[java.lang.Math.min(-1 + this.mTtsResIds.length, paramInt)];
    }
  }
  
  public boolean shouldStartFollowOn(int paramInt)
  {
    if (paramInt >= 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      if (paramInt >= -1 + this.mTtsResIds.length) {
        break;
      }
      return true;
    }
    return false;
  }
  
  public static class DisambiguationConstants
  {
    public final TtsConstants personDisambig;
    public final TtsConstants singleTypeDisambig;
    public final TtsConstants typeDisambig;
    
    private DisambiguationConstants(TtsConstants paramTtsConstants1, TtsConstants paramTtsConstants2, TtsConstants paramTtsConstants3)
    {
      this.personDisambig = paramTtsConstants1;
      this.typeDisambig = paramTtsConstants2;
      this.singleTypeDisambig = paramTtsConstants3;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.actions.TtsConstants
 * JD-Core Version:    0.7.0.1
 */