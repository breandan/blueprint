package com.google.android.speech.contacts;

import java.text.DecimalFormat;
import java.util.Comparator;

public class FavoriteContact
{
  private double mLastTimeContactedWeight;
  private int mMaxTimesContacted;
  private String mName;
  private long mTimeSinceLastContact;
  private int mTimesContacted;
  private double mTimesContactedWeight;
  private double mWeight;
  
  public FavoriteContact(String paramString, int paramInt1, long paramLong, int paramInt2)
  {
    this.mName = paramString;
    this.mTimesContacted = paramInt1;
    this.mTimeSinceLastContact = Math.max(paramLong, 0L);
    this.mMaxTimesContacted = paramInt2;
    this.mWeight = calculateAffinity();
  }
  
  private double calculateAffinity()
  {
    this.mTimesContactedWeight = ((1.0D + this.mTimesContacted) / (1 + this.mMaxTimesContacted));
    this.mLastTimeContactedWeight = Math.pow(0.5D, Math.min(this.mTimeSinceLastContact, 15552000000L) / 864000000.0D);
    return 0.5D * (this.mLastTimeContactedWeight + this.mTimesContactedWeight);
  }
  
  public String getName()
  {
    return this.mName;
  }
  
  public double getWeight()
  {
    return this.mWeight;
  }
  
  public String toString()
  {
    DecimalFormat localDecimalFormat = new DecimalFormat("#.00000");
    String str1 = localDecimalFormat.format(this.mWeight);
    String str2 = localDecimalFormat.format(this.mTimesContactedWeight);
    String str3 = localDecimalFormat.format(this.mLastTimeContactedWeight);
    return this.mName + " [" + str1 + "/" + str2 + "/" + str3 + "] " + this.mTimesContacted + "/" + this.mMaxTimesContacted + " " + this.mTimeSinceLastContact;
  }
  
  public static class WeightComparator
    implements Comparator<FavoriteContact>
  {
    public int compare(FavoriteContact paramFavoriteContact1, FavoriteContact paramFavoriteContact2)
    {
      double d1 = paramFavoriteContact1.getWeight();
      double d2 = paramFavoriteContact2.getWeight();
      if (d1 > d2) {
        return -1;
      }
      if (d1 < d2) {
        return 1;
      }
      return 0;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.FavoriteContact
 * JD-Core Version:    0.7.0.1
 */