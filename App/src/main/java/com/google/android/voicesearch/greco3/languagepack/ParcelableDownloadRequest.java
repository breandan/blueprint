package com.google.android.voicesearch.greco3.languagepack;

import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public final class ParcelableDownloadRequest
  extends DownloadManager.Request
  implements Parcelable
{
  public static final Parcelable.Creator<ParcelableDownloadRequest> CREATOR = new Parcelable.Creator()
  {
    public ParcelableDownloadRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      String str = paramAnonymousParcel.readString();
      Uri localUri = Uri.parse(paramAnonymousParcel.readString());
      boolean bool1;
      if (paramAnonymousParcel.readByte() == 1)
      {
        bool1 = true;
        if (paramAnonymousParcel.readByte() != 1) {
          break label71;
        }
      }
      label71:
      for (boolean bool2 = true;; bool2 = false)
      {
        return new ParcelableDownloadRequest(str, localUri, bool1, bool2, paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readInt());
        bool1 = false;
        break;
      }
    }
    
    public ParcelableDownloadRequest[] newArray(int paramAnonymousInt)
    {
      return new ParcelableDownloadRequest[paramAnonymousInt];
    }
  };
  private final boolean mAllowMetered;
  private final String mCacheDir;
  private final String mDisplayName;
  private final String mDownloadFilename;
  private final String mLanguagePackId;
  private final boolean mNotificationVisible;
  private final int mSizeKb;
  private final Uri mUri;
  
  public ParcelableDownloadRequest(String paramString1, Uri paramUri, boolean paramBoolean1, boolean paramBoolean2, String paramString2, String paramString3, String paramString4, int paramInt)
  {
    super(paramUri);
    this.mLanguagePackId = paramString1;
    this.mUri = paramUri;
    this.mNotificationVisible = paramBoolean1;
    this.mAllowMetered = paramBoolean2;
    this.mDisplayName = paramString2;
    this.mCacheDir = paramString3;
    this.mDownloadFilename = paramString4;
    this.mSizeKb = paramInt;
  }
  
  public ParcelableDownloadRequest configure(Context paramContext)
  {
    setVisibleInDownloadsUi(false);
    if (this.mNotificationVisible)
    {
      setNotificationVisibility(0);
      setTitle(getTitle(paramContext));
      setDescription(getDescription(paramContext));
    }
    try
    {
      for (;;)
      {
        setDestinationInExternalFilesDir(paramContext, this.mCacheDir, this.mDownloadFilename);
        setAllowedOverMetered(this.mAllowMetered);
        return this;
        setNotificationVisibility(2);
      }
    }
    catch (NullPointerException localNullPointerException)
    {
      for (;;)
      {
        Log.w("ParcelableDownloadRequest", "Error from #setDestinationInExternalFilesDir :" + localNullPointerException.getMessage());
      }
    }
    catch (IllegalStateException localIllegalStateException)
    {
      for (;;)
      {
        Log.w("ParcelableDownloadRequest", "Error from #setDestinationInExternalFilesDir :" + localIllegalStateException.getMessage());
      }
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getCacheDir()
  {
    return this.mCacheDir;
  }
  
  public String getDescription(Context paramContext)
  {
    return paramContext.getString(2131363579);
  }
  
  public String getLanguagePackId()
  {
    return this.mLanguagePackId;
  }
  
  public int getSizeKb()
  {
    return this.mSizeKb;
  }
  
  public String getTitle(Context paramContext)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mDisplayName;
    return paramContext.getString(2131363578, arrayOfObject);
  }
  
  public Uri getUri()
  {
    return this.mUri;
  }
  
  public boolean isAllowedOverMetered()
  {
    return this.mAllowMetered;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    paramParcel.writeString(this.mLanguagePackId);
    paramParcel.writeString(this.mUri.toString());
    int j;
    if (this.mNotificationVisible)
    {
      j = i;
      paramParcel.writeByte((byte)j);
      if (!this.mAllowMetered) {
        break label90;
      }
    }
    for (;;)
    {
      paramParcel.writeByte((byte)i);
      paramParcel.writeString(this.mDisplayName);
      paramParcel.writeString(this.mCacheDir);
      paramParcel.writeString(this.mDownloadFilename);
      paramParcel.writeInt(this.mSizeKb);
      return;
      j = 0;
      break;
      label90:
      i = 0;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.ParcelableDownloadRequest
 * JD-Core Version:    0.7.0.1
 */