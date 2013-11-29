package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public class StopNavigationAction
  implements VoiceAction
{
  public static final Parcelable.Creator<StopNavigationAction> CREATOR = new Parcelable.Creator()
  {
    public StopNavigationAction createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StopNavigationAction();
    }
    
    public StopNavigationAction[] newArray(int paramAnonymousInt)
    {
      return new StopNavigationAction[paramAnonymousInt];
    }
  };
  
  public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor)
  {
    return paramVoiceActionVisitor.visit(this);
  }
  
  public boolean canExecute()
  {
    return true;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.action.StopNavigationAction
 * JD-Core Version:    0.7.0.1
 */