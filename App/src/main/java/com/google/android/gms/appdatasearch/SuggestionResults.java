package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Iterator;

public class SuggestionResults
  implements SafeParcelable, Iterable<Result>
{
  public static final y CREATOR = new y();
  final int is;
  final String[] jN;
  final String[] jO;
  final String mErrorMessage;
  
  SuggestionResults(int paramInt, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    this.is = paramInt;
    this.mErrorMessage = paramString;
    this.jN = paramArrayOfString1;
    this.jO = paramArrayOfString2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean hasError()
  {
    return this.mErrorMessage != null;
  }
  
  public Iterator<Result> iterator()
  {
    if (hasError()) {
      return null;
    }
    return new ResultIterator();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    y.a(this, paramParcel, paramInt);
  }
  
  public class Result
  {
    private final int jI;
    
    Result(int paramInt)
    {
      this.jI = paramInt;
    }
  }
  
  public final class ResultIterator
    implements Iterator<SuggestionResults.Result>
  {
    private int mCurIdx = 0;
    
    public ResultIterator() {}
    
    public boolean hasNext()
    {
      return this.mCurIdx < SuggestionResults.this.jN.length;
    }
    
    public SuggestionResults.Result next()
    {
      SuggestionResults localSuggestionResults = SuggestionResults.this;
      int i = this.mCurIdx;
      this.mCurIdx = (i + 1);
      return new SuggestionResults.Result(localSuggestionResults, i);
    }
    
    public void remove()
    {
      throw new IllegalStateException("remove not supported");
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.SuggestionResults
 * JD-Core Version:    0.7.0.1
 */