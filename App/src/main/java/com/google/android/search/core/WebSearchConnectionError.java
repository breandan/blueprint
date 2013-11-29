package com.google.android.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.search.core.util.HttpHelper.HttpException;

public class WebSearchConnectionError
  extends SearchError
{
  public static final Parcelable.Creator<WebSearchConnectionError> CREATOR = new Parcelable.Creator()
  {
    public WebSearchConnectionError createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WebSearchConnectionError(paramAnonymousParcel.readInt(), paramAnonymousParcel.readString());
    }
    
    public WebSearchConnectionError[] newArray(int paramAnonymousInt)
    {
      return new WebSearchConnectionError[paramAnonymousInt];
    }
  };
  private int mHttpErrorCode;
  private String mHttpErrorMessage;
  
  public WebSearchConnectionError(int paramInt, String paramString)
  {
    this.mHttpErrorCode = paramInt;
    this.mHttpErrorMessage = paramString;
  }
  
  public WebSearchConnectionError(Exception paramException, String paramString)
  {
    if ((paramException instanceof HttpHelper.HttpException)) {}
    for (this.mHttpErrorCode = ((HttpHelper.HttpException)paramException).getStatusCode();; this.mHttpErrorCode = 400)
    {
      this.mHttpErrorMessage = paramString;
      return;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CharSequence getErrorMessage()
  {
    return super.getErrorMessage();
  }
  
  public int getErrorMessageResId()
  {
    return 2131363292;
  }
  
  public int getErrorTypeForLogs()
  {
    return 1;
  }
  
  public boolean isAuthError()
  {
    return this.mHttpErrorCode == 401;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mHttpErrorCode);
    paramParcel.writeString(this.mHttpErrorMessage);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.WebSearchConnectionError
 * JD-Core Version:    0.7.0.1
 */