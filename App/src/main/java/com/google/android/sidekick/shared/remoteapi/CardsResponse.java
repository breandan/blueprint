package com.google.android.sidekick.shared.remoteapi;

import android.content.Intent;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.Photo;
import javax.annotation.Nullable;

public class CardsResponse
  implements Parcelable
{
  public static final Parcelable.Creator<CardsResponse> CREATOR = new Parcelable.Creator()
  {
    public CardsResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CardsResponse(paramAnonymousParcel, null);
    }
    
    public CardsResponse[] newArray(int paramAnonymousInt)
    {
      return new CardsResponse[paramAnonymousInt];
    }
  };
  public CardRenderingContext mCardRenderingContext;
  public long mChangeTimeMillis;
  public Sidekick.Photo mContextImage;
  public String mContextImageQuery;
  public Sidekick.EntryResponse mEntryResponse;
  public String mGooglePlayServicesActionString;
  public String mGooglePlayServicesErrorString;
  public Intent mGooglePlayServicesRecoveryIntent;
  public boolean mIncludesMore;
  public Location mRefreshLocation;
  public long mRefreshTimeMillis;
  public int mResponseCode;
  public boolean mShowBackOfCardTutorial;
  public boolean mShowFirstUseIntro;
  public boolean mShowFirstUseOutro;
  public boolean mShowSwipeTutorial;
  
  public CardsResponse() {}
  
  private CardsResponse(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    int i = 1;
    this.mResponseCode = paramParcel.readInt();
    this.mEntryResponse = ((Sidekick.EntryResponse)ProtoParcelable.readProtoFromParcel(paramParcel, Sidekick.EntryResponse.class));
    this.mRefreshLocation = ((Location)paramParcel.readParcelable(Location.class.getClassLoader()));
    this.mCardRenderingContext = ((CardRenderingContext)paramParcel.readParcelable(CardRenderingContext.class.getClassLoader()));
    this.mGooglePlayServicesErrorString = paramParcel.readString();
    this.mGooglePlayServicesActionString = paramParcel.readString();
    this.mGooglePlayServicesRecoveryIntent = ((Intent)paramParcel.readParcelable(Intent.class.getClassLoader()));
    this.mRefreshTimeMillis = paramParcel.readLong();
    this.mChangeTimeMillis = paramParcel.readLong();
    int j;
    int k;
    label150:
    int m;
    label167:
    int n;
    if (paramParcel.readInt() == i)
    {
      j = i;
      this.mIncludesMore = j;
      this.mContextImage = ((Sidekick.Photo)ProtoParcelable.readProtoFromParcel(paramParcel, Sidekick.Photo.class));
      this.mContextImageQuery = paramParcel.readString();
      if (paramParcel.readInt() != i) {
        break label209;
      }
      k = i;
      this.mShowFirstUseIntro = k;
      if (paramParcel.readInt() != i) {
        break label215;
      }
      m = i;
      this.mShowFirstUseOutro = m;
      if (paramParcel.readInt() != i) {
        break label221;
      }
      n = i;
      label184:
      this.mShowSwipeTutorial = n;
      if (paramParcel.readInt() != i) {
        break label227;
      }
    }
    for (;;)
    {
      this.mShowBackOfCardTutorial = i;
      return;
      j = 0;
      break;
      label209:
      k = 0;
      break label150;
      label215:
      m = 0;
      break label167;
      label221:
      n = 0;
      break label184;
      label227:
      i = 0;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean entriesIncludeMore()
  {
    return this.mIncludesMore;
  }
  
  public CardRenderingContext getCardRenderingContext()
  {
    return this.mCardRenderingContext;
  }
  
  public long getChangeTimeMillis()
  {
    return this.mChangeTimeMillis;
  }
  
  @Nullable
  public Sidekick.Photo getContextImage()
  {
    return this.mContextImage;
  }
  
  @Nullable
  public String getContextImageQuery()
  {
    return this.mContextImageQuery;
  }
  
  public Sidekick.EntryResponse getEntryResponse()
  {
    return this.mEntryResponse;
  }
  
  @Nullable
  public String getGooglePlayServicesActionString()
  {
    return this.mGooglePlayServicesActionString;
  }
  
  @Nullable
  public String getGooglePlayServicesErrorString()
  {
    return this.mGooglePlayServicesErrorString;
  }
  
  @Nullable
  public Intent getGooglePlayServicesRecoveryIntent()
  {
    return this.mGooglePlayServicesRecoveryIntent;
  }
  
  public int getResponseCode()
  {
    return this.mResponseCode;
  }
  
  public boolean showBackOfCardTutorial()
  {
    return this.mShowBackOfCardTutorial;
  }
  
  public boolean showFirstUseIntro()
  {
    return this.mShowFirstUseIntro;
  }
  
  public boolean showFirstUseOutro()
  {
    return this.mShowFirstUseOutro;
  }
  
  public boolean showSwipeTutorial()
  {
    return this.mShowSwipeTutorial;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    paramParcel.writeInt(this.mResponseCode);
    ProtoParcelable.writeProtoToParcel(this.mEntryResponse, paramParcel);
    paramParcel.writeParcelable(this.mRefreshLocation, 0);
    paramParcel.writeParcelable(this.mCardRenderingContext, 0);
    paramParcel.writeString(this.mGooglePlayServicesErrorString);
    paramParcel.writeString(this.mGooglePlayServicesActionString);
    paramParcel.writeParcelable(this.mGooglePlayServicesRecoveryIntent, 0);
    paramParcel.writeLong(this.mRefreshTimeMillis);
    paramParcel.writeLong(this.mChangeTimeMillis);
    int j;
    int k;
    label119:
    int m;
    label135:
    int n;
    if (this.mIncludesMore)
    {
      j = i;
      paramParcel.writeInt(j);
      ProtoParcelable.writeProtoToParcel(this.mContextImage, paramParcel);
      paramParcel.writeString(this.mContextImageQuery);
      if (!this.mShowFirstUseIntro) {
        break label176;
      }
      k = i;
      paramParcel.writeInt(k);
      if (!this.mShowFirstUseOutro) {
        break label182;
      }
      m = i;
      paramParcel.writeInt(m);
      if (!this.mShowSwipeTutorial) {
        break label188;
      }
      n = i;
      label151:
      paramParcel.writeInt(n);
      if (!this.mShowBackOfCardTutorial) {
        break label194;
      }
    }
    for (;;)
    {
      paramParcel.writeInt(i);
      return;
      j = 0;
      break;
      label176:
      k = 0;
      break label119;
      label182:
      m = 0;
      break label135;
      label188:
      n = 0;
      break label151;
      label194:
      i = 0;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.remoteapi.CardsResponse
 * JD-Core Version:    0.7.0.1
 */