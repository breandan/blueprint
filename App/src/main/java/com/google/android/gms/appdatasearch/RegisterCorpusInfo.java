package com.google.android.gms.appdatasearch;

import android.net.Uri;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dr;

public class RegisterCorpusInfo
  implements SafeParcelable
{
  public static final n CREATOR = new n();
  public final Uri contentProviderUri;
  public final GlobalSearchCorpusConfig globalSearchConfig;
  final int is;
  public final String name;
  public final RegisterSectionInfo[] sections;
  public final boolean trimmable;
  public final String version;
  
  RegisterCorpusInfo(int paramInt, String paramString1, String paramString2, Uri paramUri, RegisterSectionInfo[] paramArrayOfRegisterSectionInfo, GlobalSearchCorpusConfig paramGlobalSearchCorpusConfig, boolean paramBoolean)
  {
    this.is = paramInt;
    this.name = paramString1;
    this.version = paramString2;
    this.contentProviderUri = paramUri;
    this.sections = paramArrayOfRegisterSectionInfo;
    this.globalSearchConfig = paramGlobalSearchCorpusConfig;
    this.trimmable = paramBoolean;
  }
  
  public RegisterCorpusInfo(String paramString1, String paramString2, Uri paramUri, RegisterSectionInfo[] paramArrayOfRegisterSectionInfo, GlobalSearchCorpusConfig paramGlobalSearchCorpusConfig, boolean paramBoolean)
  {
    this(2, paramString1, paramString2, paramUri, paramArrayOfRegisterSectionInfo, paramGlobalSearchCorpusConfig, paramBoolean);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof RegisterCorpusInfo;
    boolean bool2 = false;
    if (bool1)
    {
      RegisterCorpusInfo localRegisterCorpusInfo = (RegisterCorpusInfo)paramObject;
      boolean bool3 = dr.equal(this.name, localRegisterCorpusInfo.name);
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = dr.equal(this.contentProviderUri, localRegisterCorpusInfo.contentProviderUri);
        bool2 = false;
        if (bool4)
        {
          boolean bool5 = dr.equal(this.sections, localRegisterCorpusInfo.sections);
          bool2 = false;
          if (bool5) {
            bool2 = true;
          }
        }
      }
    }
    return bool2;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    n.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.RegisterCorpusInfo
 * JD-Core Version:    0.7.0.1
 */