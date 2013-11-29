package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class PIMEUpdate
  implements SafeParcelable
{
  public static final h CREATOR = new h();
  final byte[] iN;
  final byte[] iO;
  final Bundle iP;
  public final boolean inputByUser;
  final int is;
  public final long score;
  public final int sourceClass;
  public final String sourceCorpusHandle;
  public final String sourcePackageName;
  
  PIMEUpdate(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2, String paramString1, String paramString2, boolean paramBoolean, Bundle paramBundle, long paramLong)
  {
    this.is = paramInt1;
    this.iN = paramArrayOfByte1;
    this.iO = paramArrayOfByte2;
    this.sourceClass = paramInt2;
    this.sourcePackageName = paramString1;
    this.sourceCorpusHandle = paramString2;
    this.inputByUser = paramBoolean;
    this.iP = paramBundle;
    this.score = paramLong;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    h.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.PIMEUpdate
 * JD-Core Version:    0.7.0.1
 */