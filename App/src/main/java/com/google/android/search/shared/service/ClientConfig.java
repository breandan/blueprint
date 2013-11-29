package com.google.android.search.shared.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ClientConfig
  implements Parcelable
{
  public static final Parcelable.Creator<ClientConfig> CREATOR = new Parcelable.Creator()
  {
    public ClientConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ClientConfig(paramAnonymousParcel.readInt());
    }
    
    public ClientConfig[] newArray(int paramAnonymousInt)
    {
      return new ClientConfig[paramAnonymousInt];
    }
  };
  private final int mFlags;
  
  public ClientConfig(int paramInt)
  {
    this.mFlags = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean isEyesFree()
  {
    return (0x2 & this.mFlags) > 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mFlags);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.service.ClientConfig
 * JD-Core Version:    0.7.0.1
 */