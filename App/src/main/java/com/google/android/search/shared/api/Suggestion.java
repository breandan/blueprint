package com.google.android.search.shared.api;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.shared.util.SpannedCharSequences;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import java.util.Date;

public class Suggestion
  implements Parcelable, Comparable<Suggestion>
{
  public static final Parcelable.Creator<Suggestion> CREATOR = new Parcelable.Creator()
  {
    public Suggestion createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Suggestion(paramAnonymousParcel, null);
    }
    
    public Suggestion[] newArray(int paramAnonymousInt)
    {
      return new Suggestion[paramAnonymousInt];
    }
  };
  private static final Ordering<String> STRING_ORDERING = Ordering.natural().nullsFirst();
  private static final Ordering<Object> TO_STRING_ORDERING = Ordering.usingToString().nullsFirst();
  private final int mFlags;
  private final int mHashCode;
  private final String mIcon1;
  private final String mIntentAction;
  private final ComponentName mIntentComponent;
  private final String mIntentData;
  private final String mIntentExtraData;
  private final long mLastAccessTime;
  private final String mLogType;
  private final String mSourceCanonicalName;
  private final String mSourceIcon;
  private final String mSourcePackageName;
  private String mSuggestionKey;
  private final String mSuggestionQuery;
  private final CharSequence mText1;
  private final CharSequence mText2;
  private final String mText2Url;
  
  private Suggestion(Parcel paramParcel)
  {
    this.mSourcePackageName = paramParcel.readString();
    this.mSourceCanonicalName = paramParcel.readString();
    this.mText1 = SpannedCharSequences.readFromParcel(paramParcel);
    this.mText2 = SpannedCharSequences.readFromParcel(paramParcel);
    this.mText2Url = paramParcel.readString();
    this.mIcon1 = paramParcel.readString();
    this.mSourceIcon = paramParcel.readString();
    this.mLastAccessTime = paramParcel.readLong();
    this.mIntentAction = paramParcel.readString();
    this.mIntentData = paramParcel.readString();
    this.mIntentExtraData = paramParcel.readString();
    this.mIntentComponent = ((ComponentName)paramParcel.readParcelable(ComponentName.class.getClassLoader()));
    this.mSuggestionQuery = paramParcel.readString();
    this.mLogType = paramParcel.readString();
    this.mFlags = paramParcel.readInt();
    this.mHashCode = calculateHashCode();
  }
  
  public Suggestion(String paramString1, String paramString2, String paramString3, CharSequence paramCharSequence1, CharSequence paramCharSequence2, String paramString4, String paramString5, long paramLong, String paramString6, String paramString7, String paramString8, ComponentName paramComponentName, String paramString9, String paramString10, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, boolean paramBoolean7, boolean paramBoolean8, boolean paramBoolean9, boolean paramBoolean10, boolean paramBoolean11, boolean paramBoolean12)
  {
    this.mSourcePackageName = paramString2;
    this.mSourceCanonicalName = paramString1;
    this.mSourceIcon = paramString3;
    this.mText1 = paramCharSequence1;
    this.mText2 = paramCharSequence2;
    this.mText2Url = paramString4;
    this.mIcon1 = paramString5;
    this.mLastAccessTime = paramLong;
    this.mIntentAction = paramString6;
    this.mIntentData = paramString7;
    this.mIntentExtraData = paramString8;
    this.mIntentComponent = paramComponentName;
    this.mSuggestionQuery = paramString9;
    this.mLogType = paramString10;
    int i;
    int j;
    label101:
    int m;
    label116:
    int i1;
    label132:
    int i3;
    label148:
    int i5;
    label164:
    int i7;
    label180:
    int i9;
    label197:
    int i11;
    label214:
    int i13;
    label231:
    int i15;
    label248:
    int i16;
    if (paramBoolean1)
    {
      i = 1;
      if (!paramBoolean4) {
        break label289;
      }
      j = 2;
      int k = i | j;
      if (!paramBoolean6) {
        break label295;
      }
      m = 4;
      int n = k | m;
      if (!paramBoolean7) {
        break label301;
      }
      i1 = 8;
      int i2 = n | i1;
      if (!paramBoolean5) {
        break label307;
      }
      i3 = 16;
      int i4 = i2 | i3;
      if (!paramBoolean2) {
        break label313;
      }
      i5 = 32;
      int i6 = i4 | i5;
      if (!paramBoolean3) {
        break label319;
      }
      i7 = 64;
      int i8 = i6 | i7;
      if (!paramBoolean8) {
        break label325;
      }
      i9 = 128;
      int i10 = i8 | i9;
      if (!paramBoolean9) {
        break label331;
      }
      i11 = 256;
      int i12 = i10 | i11;
      if (!paramBoolean10) {
        break label337;
      }
      i13 = 512;
      int i14 = i12 | i13;
      if (!paramBoolean11) {
        break label343;
      }
      i15 = 1024;
      i16 = i14 | i15;
      if (!paramBoolean12) {
        break label349;
      }
    }
    label289:
    label295:
    label301:
    label307:
    label313:
    label319:
    label325:
    label331:
    label337:
    label343:
    label349:
    for (int i17 = 2048;; i17 = 0)
    {
      this.mFlags = (i17 | i16);
      this.mHashCode = calculateHashCode();
      return;
      i = 0;
      break;
      j = 0;
      break label101;
      m = 0;
      break label116;
      i1 = 0;
      break label132;
      i3 = 0;
      break label148;
      i5 = 0;
      break label164;
      i7 = 0;
      break label180;
      i9 = 0;
      break label197;
      i11 = 0;
      break label214;
      i13 = 0;
      break label231;
      i15 = 0;
      break label248;
    }
  }
  
  private int calculateHashCode()
  {
    Object[] arrayOfObject = new Object[9];
    arrayOfObject[0] = this.mIcon1;
    arrayOfObject[1] = this.mText1;
    arrayOfObject[2] = this.mText2;
    arrayOfObject[3] = this.mIntentAction;
    arrayOfObject[4] = this.mIntentData;
    arrayOfObject[5] = this.mIntentExtraData;
    arrayOfObject[6] = this.mLogType;
    arrayOfObject[7] = this.mSourceCanonicalName;
    arrayOfObject[8] = this.mSuggestionQuery;
    return Objects.hashCode(arrayOfObject);
  }
  
  private String createSuggestionKey()
  {
    if (isNowPromo()) {
      return "NowPromo";
    }
    if (!isWebSearchSuggestion())
    {
      String str1 = makeKeyComponent(getSuggestionIntentAction());
      String str2 = makeKeyComponent(normalizeUrl(this.mIntentData));
      String str3 = makeKeyComponent(getSuggestionQuery());
      StringBuilder localStringBuilder = new StringBuilder(2 + str1.length() + str2.length() + str3.length());
      localStringBuilder.append(str1).append('#').append(str2).append('#');
      if (!"android.intent.action.VIEW".equals(str1)) {
        localStringBuilder.append(str3);
      }
      return localStringBuilder.toString();
    }
    return this.mText1 + '#' + this.mText2 + '#' + this.mText2Url;
  }
  
  private boolean isFlagSet(int paramInt)
  {
    return (paramInt & this.mFlags) != 0;
  }
  
  private static String makeKeyComponent(String paramString)
  {
    if (paramString == null) {
      paramString = "";
    }
    return paramString;
  }
  
  static String normalizeUrl(String paramString)
  {
    int i;
    String str;
    if (paramString != null)
    {
      i = paramString.indexOf("://");
      if (i != -1) {
        break label81;
      }
      str = "http://" + paramString;
    }
    for (int j = "http".length() + "://".length();; j = i + "://".length())
    {
      int k = str.length();
      if (str.indexOf('/', j) == k - 1) {
        k--;
      }
      paramString = str.substring(0, k);
      return paramString;
      label81:
      str = paramString;
    }
  }
  
  public int compareTo(Suggestion paramSuggestion)
  {
    if (this == paramSuggestion) {
      return 0;
    }
    if (paramSuggestion == null) {
      return -1;
    }
    return ComparisonChain.start().compare(this.mText1, paramSuggestion.mText1, TO_STRING_ORDERING).compare(this.mText2, paramSuggestion.mText2, TO_STRING_ORDERING).compare(this.mIcon1, paramSuggestion.mIcon1, TO_STRING_ORDERING).compare(this.mIntentAction, paramSuggestion.mIntentAction, STRING_ORDERING).compare(this.mIntentData, paramSuggestion.mIntentData, STRING_ORDERING).compare(this.mIntentExtraData, paramSuggestion.mIntentExtraData, STRING_ORDERING).compare(this.mLogType, paramSuggestion.mLogType, STRING_ORDERING).compare(this.mSourcePackageName, paramSuggestion.mSourcePackageName, STRING_ORDERING).compare(this.mSuggestionQuery, paramSuggestion.mSuggestionQuery, STRING_ORDERING).compare(this.mFlags, paramSuggestion.mFlags).result();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    do
    {
      return true;
      if (!(paramObject instanceof Suggestion)) {
        return false;
      }
    } while (compareTo((Suggestion)paramObject) == 0);
    return false;
  }
  
  public long getLastAccessTime()
  {
    return this.mLastAccessTime;
  }
  
  public String getSourceCanonicalName()
  {
    return this.mSourceCanonicalName;
  }
  
  public String getSourceIcon()
  {
    return this.mSourceIcon;
  }
  
  public String getSourcePackageName()
  {
    return this.mSourcePackageName;
  }
  
  public String getSuggestionIcon1()
  {
    return this.mIcon1;
  }
  
  public String getSuggestionIntentAction()
  {
    return this.mIntentAction;
  }
  
  public ComponentName getSuggestionIntentComponent()
  {
    return this.mIntentComponent;
  }
  
  public String getSuggestionIntentDataString()
  {
    return this.mIntentData;
  }
  
  public String getSuggestionIntentExtraData()
  {
    return this.mIntentExtraData;
  }
  
  public String getSuggestionKey()
  {
    try
    {
      if (this.mSuggestionKey == null) {
        this.mSuggestionKey = createSuggestionKey();
      }
      String str = this.mSuggestionKey;
      return str;
    }
    finally {}
  }
  
  public String getSuggestionLogType()
  {
    return this.mLogType;
  }
  
  public String getSuggestionQuery()
  {
    return this.mSuggestionQuery;
  }
  
  public CharSequence getSuggestionText1()
  {
    return this.mText1;
  }
  
  public CharSequence getSuggestionText2()
  {
    return this.mText2;
  }
  
  public String getSuggestionText2Url()
  {
    return this.mText2Url;
  }
  
  public boolean hasFullSizeIcon()
  {
    return isFlagSet(128);
  }
  
  public int hashCode()
  {
    return this.mHashCode;
  }
  
  public boolean isApplicationSuggestion()
  {
    return isFlagSet(2);
  }
  
  public boolean isContactSuggestion()
  {
    return isFlagSet(16);
  }
  
  public boolean isCorrectionSuggestion()
  {
    return isFlagSet(64);
  }
  
  public boolean isFromDeviceHistory()
  {
    return isFlagSet(1024);
  }
  
  public boolean isFromIcing()
  {
    return isFlagSet(256);
  }
  
  public boolean isHistorySuggestion()
  {
    return isFlagSet(1);
  }
  
  public boolean isNavSuggestion()
  {
    return isFlagSet(8);
  }
  
  public boolean isNowPromo()
  {
    return isFlagSet(512);
  }
  
  public boolean isUndoRewrite()
  {
    return isFlagSet(2048);
  }
  
  public boolean isWebSearchSuggestion()
  {
    return isFlagSet(4);
  }
  
  public boolean isWordByWordSuggestion()
  {
    return isFlagSet(32);
  }
  
  public String toString()
  {
    return Objects.toStringHelper(this).add("source", this.mSourceCanonicalName).add("text1", this.mText1).add("text2", this.mText2).add("intentAction", this.mIntentAction).add("intentData", this.mIntentData).add("query", this.mSuggestionQuery).add("logtype", this.mLogType).add("lastAccesstTime", new Date(this.mLastAccessTime)).toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.mSourcePackageName);
    paramParcel.writeString(this.mSourceCanonicalName);
    SpannedCharSequences.writeToParcel(this.mText1, paramParcel, paramInt);
    SpannedCharSequences.writeToParcel(this.mText2, paramParcel, paramInt);
    paramParcel.writeString(this.mText2Url);
    paramParcel.writeString(this.mIcon1);
    paramParcel.writeString(this.mSourceIcon);
    paramParcel.writeLong(this.mLastAccessTime);
    paramParcel.writeString(this.mIntentAction);
    paramParcel.writeString(this.mIntentData);
    paramParcel.writeString(this.mIntentExtraData);
    paramParcel.writeParcelable(this.mIntentComponent, paramInt);
    paramParcel.writeString(this.mSuggestionQuery);
    paramParcel.writeString(this.mLogType);
    paramParcel.writeInt(this.mFlags);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.api.Suggestion
 * JD-Core Version:    0.7.0.1
 */