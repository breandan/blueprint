package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SearchResults
  implements SafeParcelable, Iterable<Result>
{
  public static final u CREATOR = new u();
  final int is;
  final Bundle[] jA;
  final int jB;
  final int[] jC;
  final String[] jD;
  final int[] jw;
  final byte[] jx;
  final Bundle[] jy;
  final Bundle[] jz;
  final String mErrorMessage;
  
  SearchResults(int paramInt1, String paramString, int[] paramArrayOfInt1, byte[] paramArrayOfByte, Bundle[] paramArrayOfBundle1, Bundle[] paramArrayOfBundle2, Bundle[] paramArrayOfBundle3, int paramInt2, int[] paramArrayOfInt2, String[] paramArrayOfString)
  {
    this.is = paramInt1;
    this.mErrorMessage = paramString;
    this.jw = paramArrayOfInt1;
    this.jx = paramArrayOfByte;
    this.jy = paramArrayOfBundle1;
    this.jz = paramArrayOfBundle2;
    this.jA = paramArrayOfBundle3;
    this.jB = paramInt2;
    this.jC = paramArrayOfInt2;
    this.jD = paramArrayOfString;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getErrorMessage()
  {
    return this.mErrorMessage;
  }
  
  public int getNumResults()
  {
    return this.jB;
  }
  
  public boolean hasError()
  {
    return this.mErrorMessage != null;
  }
  
  public ResultIterator iterator()
  {
    if (hasError()) {
      return null;
    }
    return new ResultIterator();
  }
  
  public ResultIterator iterator(String paramString)
  {
    if (hasError()) {
      return null;
    }
    return new SinglePackageResultIterator(paramString, null);
  }
  
  public ResultIterator iterator(String paramString1, String paramString2)
  {
    if (hasError()) {
      return null;
    }
    return new SinglePackageResultIterator(paramString1, paramString2);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    u.a(this, paramParcel, paramInt);
  }
  
  public static final class BufferContentsAccessor
  {
    int jE = 0;
    final int[] jF;
    final byte[] jG;
    int mCurIdx = 0;
    
    public BufferContentsAccessor(int[] paramArrayOfInt, byte[] paramArrayOfByte)
    {
      this.jF = paramArrayOfInt;
      this.jG = paramArrayOfByte;
    }
    
    public String getContent(int paramInt)
    {
      if (paramInt < this.mCurIdx) {
        throw new IllegalArgumentException("idx cannot go backwards");
      }
      while (this.mCurIdx < paramInt)
      {
        this.jE += this.jF[this.mCurIdx];
        this.mCurIdx = (1 + this.mCurIdx);
      }
      try
      {
        String str = new String(this.jG, this.jE, this.jF[this.mCurIdx], "UTF-8");
        return str;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
      return null;
    }
  }
  
  public class Result
  {
    private final SearchResults.ResultIterator jH;
    private final int jI;
    
    Result(int paramInt, SearchResults.ResultIterator paramResultIterator)
    {
      this.jH = paramResultIterator;
      this.jI = paramInt;
    }
    
    private String aF()
    {
      return SearchResults.this.jD[SearchResults.this.jC[this.jI]];
    }
    
    public String getCorpus()
    {
      String str = aF();
      return str.substring(1 + str.indexOf('-'), str.length());
    }
    
    public String getPackageName()
    {
      String str = aF();
      return str.substring(0, str.indexOf('-'));
    }
    
    public int getPosition()
    {
      return this.jI;
    }
    
    public String getSection(String paramString)
    {
      Map localMap = SearchResults.ResultIterator.b(this.jH)[SearchResults.this.jC[this.jI]];
      HashMap localHashMap;
      if (localMap == null)
      {
        Map[] arrayOfMap = SearchResults.ResultIterator.b(this.jH);
        int i = SearchResults.this.jC[this.jI];
        localHashMap = new HashMap();
        arrayOfMap[i] = localHashMap;
      }
      for (Object localObject = localHashMap;; localObject = localMap)
      {
        SearchResults.BufferContentsAccessor localBufferContentsAccessor = (SearchResults.BufferContentsAccessor)((Map)localObject).get(paramString);
        if (localBufferContentsAccessor == null)
        {
          int[] arrayOfInt = SearchResults.this.jz[SearchResults.this.jC[this.jI]].getIntArray(paramString);
          byte[] arrayOfByte = SearchResults.this.jA[SearchResults.this.jC[this.jI]].getByteArray(paramString);
          if ((arrayOfInt == null) || (arrayOfByte == null)) {
            return null;
          }
          localBufferContentsAccessor = new SearchResults.BufferContentsAccessor(arrayOfInt, arrayOfByte);
          ((Map)localObject).put(paramString, localBufferContentsAccessor);
        }
        return localBufferContentsAccessor.getContent(this.jI);
      }
    }
    
    public String getUri()
    {
      if (SearchResults.ResultIterator.a(this.jH) == null)
      {
        if ((SearchResults.this.jw == null) || (SearchResults.this.jx == null)) {
          return null;
        }
        SearchResults.ResultIterator.a(this.jH, new SearchResults.BufferContentsAccessor(SearchResults.this.jw, SearchResults.this.jx));
      }
      return SearchResults.ResultIterator.a(this.jH).getContent(this.jI);
    }
  }
  
  public class ResultIterator
    implements Iterator<SearchResults.Result>
  {
    private SearchResults.BufferContentsAccessor jK;
    private final Map<String, SearchResults.BufferContentsAccessor>[] jL = new Map[SearchResults.this.jD.length];
    protected int mCurIdx;
    
    public ResultIterator() {}
    
    public int getCount()
    {
      return SearchResults.this.getNumResults();
    }
    
    public boolean hasNext()
    {
      return (!SearchResults.this.hasError()) && (this.mCurIdx < SearchResults.this.getNumResults());
    }
    
    protected void moveToNext()
    {
      this.mCurIdx = (1 + this.mCurIdx);
    }
    
    public SearchResults.Result next()
    {
      SearchResults.Result localResult = new SearchResults.Result(SearchResults.this, this.mCurIdx, this);
      moveToNext();
      return localResult;
    }
    
    public void remove()
    {
      throw new IllegalStateException("remove not supported");
    }
  }
  
  private final class SinglePackageResultIterator
    extends SearchResults.ResultIterator
  {
    private final String iX;
    private final String iY;
    
    SinglePackageResultIterator(String paramString1, String paramString2)
    {
      super();
      this.iX = paramString1;
      this.iY = paramString2;
      this.mCurIdx = -1;
      moveToNext();
    }
    
    private boolean F(int paramInt)
    {
      return (SearchResults.this.jD[SearchResults.this.jC[paramInt]].startsWith(this.iX)) && ((this.iY == null) || (SearchResults.this.jD[SearchResults.this.jC[paramInt]].endsWith(this.iY)));
    }
    
    public int getCount()
    {
      int i = 0;
      int j = super.getCount();
      for (int k = 0; k < j; k++) {
        if (F(k)) {
          i++;
        }
      }
      return i;
    }
    
    protected void moveToNext()
    {
      do
      {
        this.mCurIdx = (1 + this.mCurIdx);
      } while ((this.mCurIdx < SearchResults.this.getNumResults()) && (!F(this.mCurIdx)));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.SearchResults
 * JD-Core Version:    0.7.0.1
 */