package com.google.android.gms.appdatasearch;

import android.net.Uri;
import com.google.android.gms.internal.ds;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SyncContentProviderHelper
{
  public static final class SyncColumns
  {
    public static final List<String> TAGS_QUERY_COLUMN_NAMES = Collections.unmodifiableList(Arrays.asList(new String[] { "seqno", "action", "uri", "tag" }));
    private static final List<String> jQ = Collections.unmodifiableList(Arrays.asList(new String[] { "seqno", "action", "uri", "doc_score" }));
    
    public static String getColumnNameForSection(String paramString)
    {
      return "section_" + paramString;
    }
  }
  
  public static final class SyncQuery
  {
    private static final SyncQuery jU = new SyncQuery(-1, -1L, -1L);
    private final int jR;
    private final long jS;
    private final long jT;
    
    private SyncQuery(int paramInt, long paramLong1, long paramLong2)
    {
      if ((paramInt == -1) || (paramInt == 0) || (paramInt == i)) {}
      for (;;)
      {
        ds.m(i);
        this.jR = paramInt;
        this.jS = paramLong1;
        this.jT = paramLong2;
        return;
        i = 0;
      }
    }
    
    public static SyncQuery parse(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
    {
      if ((paramArrayOfString2 == null) || (paramArrayOfString2.length < 3)) {
        return jU;
      }
      boolean bool = "documents".equals(paramArrayOfString2[0]);
      int i = 0;
      if (bool) {}
      try
      {
        long l1;
        long l2;
        for (;;)
        {
          l1 = Long.parseLong(paramArrayOfString2[1]);
          l2 = Long.parseLong(paramArrayOfString2[2]);
          if ((l1 >= 0L) && (l2 >= 1L)) {
            break label91;
          }
          SyncQuery localSyncQuery1 = jU;
          return localSyncQuery1;
          if (!"tags".equals(paramArrayOfString2[0])) {
            break;
          }
          i = 1;
        }
        return jU;
        label91:
        SyncQuery localSyncQuery2 = new SyncQuery(i, l1, l2);
        return localSyncQuery2;
      }
      catch (NumberFormatException localNumberFormatException) {}
      return jU;
    }
    
    public long getLastSeqNo()
    {
      return this.jS;
    }
    
    public long getLimit()
    {
      return this.jT;
    }
    
    public boolean isValidDocumentsQuery()
    {
      return this.jR == 0;
    }
    
    public boolean isValidTagsQuery()
    {
      return this.jR == 1;
    }
    
    public String toString()
    {
      if (this.jR == -1) {
        return "SyncQuery[type=Unrecognized]";
      }
      if (this.jR == 0) {}
      for (String str = "Documents";; str = "Tags") {
        return "SyncQuery[type=" + str + ", lastSeqNo=" + this.jS + ", limit=" + this.jT + "]";
      }
    }
    
    public boolean wantsFullSync()
    {
      return (this.jS == 0L) && ((isValidDocumentsQuery()) || (isValidTagsQuery()));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.SyncContentProviderHelper
 * JD-Core Version:    0.7.0.1
 */