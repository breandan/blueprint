package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.View;
import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.Interest.Filter;
import com.google.geo.sidekick.Sidekick.Photo;
import javax.annotation.Nullable;

public class SecondScreenUtil
{
  public static Intent createIntent(Context paramContext, Options paramOptions)
  {
    Intent localIntent = new Intent();
    localIntent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.sidekick.main.secondscreen.SecondScreenActivity");
    localIntent.putExtra("options", paramOptions);
    return localIntent;
  }
  
  public static Intent createTvIntent(Context paramContext, Sidekick.Interest paramInterest)
  {
    return createIntent(paramContext, new Options().setInterest(paramInterest).setTitle(paramContext.getString(2131362812)).setHelpContextId("tv").setFlags(7));
  }
  
  public static Intent createTvIntent(Context paramContext, String paramString)
  {
    return createTvIntent(paramContext, new Sidekick.Interest().setTargetDisplay(7).setFilter(new Sidekick.Interest.Filter().setTvContentId(paramString)).addEntryTypeRestrict(65));
  }
  
  public static void launchSecondScreen(Context paramContext, Options paramOptions)
  {
    paramContext.startActivity(createIntent(paramContext, paramOptions));
  }
  
  public static class Options
    implements Parcelable
  {
    public static final Parcelable.Creator<Options> CREATOR = new Parcelable.Creator()
    {
      public SecondScreenUtil.Options createFromParcel(Parcel paramAnonymousParcel)
      {
        return SecondScreenUtil.Options.access$100(SecondScreenUtil.Options.access$000(new SecondScreenUtil.Options().setFlags(paramAnonymousParcel.readInt()).setInterest((Sidekick.Interest)ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, Sidekick.Interest.class)).setTitle(paramAnonymousParcel.readString()).setHelpContextId(paramAnonymousParcel.readString()).setContextHeaderImage((Sidekick.Photo)ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, Sidekick.Photo.class)), (Sidekick.Entry)ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, Sidekick.Entry.class)), (Point)paramAnonymousParcel.readParcelable(Point.class.getClassLoader())).setLureWidth(paramAnonymousParcel.readInt()).setClickPoint((Point)paramAnonymousParcel.readParcelable(Point.class.getClassLoader()));
      }
      
      public SecondScreenUtil.Options[] newArray(int paramAnonymousInt)
      {
        return new SecondScreenUtil.Options[paramAnonymousInt];
      }
    };
    private Point mClickPoint;
    private Sidekick.Photo mContextHeaderImage;
    private int mFlags;
    private String mHelpContextId;
    private Sidekick.Interest mInterest;
    private Sidekick.Entry mLureEntry;
    private Point mLureTopLeft;
    private int mLureWidth;
    private String mTitle;
    
    private Options setLureEntry(Sidekick.Entry paramEntry)
    {
      this.mLureEntry = paramEntry;
      return this;
    }
    
    private Options setLureTopLeft(Point paramPoint)
    {
      this.mLureTopLeft = paramPoint;
      return this;
    }
    
    private Options setLureWidth(int paramInt)
    {
      this.mLureWidth = paramInt;
      return this;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    @Nullable
    public Sidekick.Photo getContextHeaderImage()
    {
      return this.mContextHeaderImage;
    }
    
    @Nullable
    public String getHelpContextId()
    {
      return this.mHelpContextId;
    }
    
    @Nullable
    public Sidekick.Interest getInterest()
    {
      return this.mInterest;
    }
    
    @Nullable
    public Sidekick.Entry getLureEntry()
    {
      return this.mLureEntry;
    }
    
    @Nullable
    public String getTitle()
    {
      return this.mTitle;
    }
    
    public boolean isCardDismissDisabled()
    {
      return (0x1 & this.mFlags) != 0;
    }
    
    public boolean isRefreshDisabled()
    {
      return (0x2 & this.mFlags) != 0;
    }
    
    public boolean isTvButtonEnabled()
    {
      return (0x4 & this.mFlags) != 0;
    }
    
    public Options setClickPoint(Point paramPoint)
    {
      this.mClickPoint = paramPoint;
      return this;
    }
    
    public Options setContextHeaderImage(Sidekick.Photo paramPhoto)
    {
      this.mContextHeaderImage = paramPhoto;
      return this;
    }
    
    public Options setFlags(int paramInt)
    {
      this.mFlags = paramInt;
      return this;
    }
    
    public Options setHelpContextId(String paramString)
    {
      this.mHelpContextId = paramString;
      return this;
    }
    
    public Options setInterest(Sidekick.Interest paramInterest)
    {
      this.mInterest = paramInterest;
      return this;
    }
    
    public Options setLure(View paramView, Sidekick.Entry paramEntry, Context paramContext)
    {
      this.mLureTopLeft = new Point();
      int[] arrayOfInt = new int[2];
      paramView.getLocationInWindow(arrayOfInt);
      this.mLureTopLeft.x = arrayOfInt[0];
      this.mLureTopLeft.y = arrayOfInt[1];
      int i = paramContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
      if (i > 0)
      {
        Point localPoint = this.mLureTopLeft;
        localPoint.y -= paramContext.getResources().getDimensionPixelSize(i);
      }
      this.mLureWidth = paramView.getWidth();
      this.mLureEntry = paramEntry;
      return this;
    }
    
    public Options setTitle(String paramString)
    {
      this.mTitle = paramString;
      return this;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(this.mFlags);
      ProtoParcelable.writeProtoToParcel(this.mInterest, paramParcel);
      paramParcel.writeString(this.mTitle);
      paramParcel.writeString(this.mHelpContextId);
      ProtoParcelable.writeProtoToParcel(this.mContextHeaderImage, paramParcel);
      ProtoParcelable.writeProtoToParcel(this.mLureEntry, paramParcel);
      paramParcel.writeParcelable(this.mLureTopLeft, 0);
      paramParcel.writeInt(this.mLureWidth);
      paramParcel.writeParcelable(this.mClickPoint, 0);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.SecondScreenUtil
 * JD-Core Version:    0.7.0.1
 */