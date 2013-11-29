package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.List;

public class RegisterSectionInfo
  implements SafeParcelable
{
  public static final o CREATOR = new o();
  public final SectionFeature[] features;
  public final String format;
  public final boolean indexPrefixes;
  final int is;
  public final String name;
  public final boolean noIndex;
  public final String subsectionSeparator;
  public final int weight;
  
  RegisterSectionInfo(int paramInt1, String paramString1, String paramString2, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, String paramString3, SectionFeature[] paramArrayOfSectionFeature)
  {
    this.is = paramInt1;
    this.name = paramString1;
    this.format = paramString2;
    this.noIndex = paramBoolean1;
    this.weight = paramInt2;
    this.indexPrefixes = paramBoolean2;
    this.subsectionSeparator = paramString3;
    this.features = paramArrayOfSectionFeature;
  }
  
  public RegisterSectionInfo(String paramString1, String paramString2, boolean paramBoolean1, int paramInt, boolean paramBoolean2, String paramString3, SectionFeature[] paramArrayOfSectionFeature)
  {
    this(2, paramString1, paramString2, paramBoolean1, paramInt, paramBoolean2, paramString3, paramArrayOfSectionFeature);
  }
  
  public static Builder builder(String paramString)
  {
    return new Builder(paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof RegisterSectionInfo;
    boolean bool2 = false;
    if (bool1)
    {
      RegisterSectionInfo localRegisterSectionInfo = (RegisterSectionInfo)paramObject;
      boolean bool3 = this.name.equals(localRegisterSectionInfo.name);
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = this.format.equals(localRegisterSectionInfo.format);
        bool2 = false;
        if (bool4)
        {
          boolean bool5 = this.noIndex;
          boolean bool6 = localRegisterSectionInfo.noIndex;
          bool2 = false;
          if (bool5 == bool6) {
            bool2 = true;
          }
        }
      }
    }
    return bool2;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    o.a(this, paramParcel, paramInt);
  }
  
  public static final class Builder
  {
    private String jm;
    private boolean jn;
    private int jo;
    private boolean jp;
    private String jq;
    private final List<SectionFeature> jr;
    private final String mName;
    
    public Builder(String paramString)
    {
      this.mName = paramString;
      this.jo = 1;
      this.jr = new ArrayList();
    }
    
    public Builder addFeature(SectionFeature paramSectionFeature)
    {
      for (int i = 0; i < this.jr.size(); i++) {
        if (((SectionFeature)this.jr.get(i)).id == paramSectionFeature.id) {
          throw new IllegalStateException("Feature " + paramSectionFeature.id + " already exists");
        }
      }
      this.jr.add(paramSectionFeature);
      return this;
    }
    
    public RegisterSectionInfo build()
    {
      return new RegisterSectionInfo(this.mName, this.jm, this.jn, this.jo, this.jp, this.jq, (SectionFeature[])this.jr.toArray(new SectionFeature[this.jr.size()]));
    }
    
    public Builder format(String paramString)
    {
      this.jm = paramString;
      return this;
    }
    
    public Builder indexPrefixes(boolean paramBoolean)
    {
      this.jp = paramBoolean;
      return this;
    }
    
    public Builder noIndex(boolean paramBoolean)
    {
      this.jn = paramBoolean;
      return this;
    }
    
    public Builder subsectionSeparator(String paramString)
    {
      this.jq = paramString;
      return this;
    }
    
    public Builder weight(int paramInt)
    {
      this.jo = paramInt;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.RegisterSectionInfo
 * JD-Core Version:    0.7.0.1
 */