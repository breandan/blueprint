package com.google.android.search.core.util;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.VoiceCorrectionSpan;
import com.google.quality.genie.proto.QueryAlternativesProto.QueryAlternatives;
import com.google.quality.genie.proto.QueryAlternativesProto.QueryAlternatives.QuerySegment;
import com.google.quality.genie.proto.QueryAlternativesProto.QueryAlternatives.QuerySegmentAlternatives;
import java.util.List;

public class VoiceCorrectionUtils
{
  static boolean compareTokens(List<String> paramList, String[] paramArrayOfString)
  {
    if (paramList.size() != paramArrayOfString.length) {
      return false;
    }
    for (int i = 0; i < paramArrayOfString.length; i++) {
      if (!TextUtils.equals(((String)paramList.get(i)).toLowerCase(), paramArrayOfString[i].toLowerCase())) {
        return false;
      }
    }
    return true;
  }
  
  static int[] computeTokenOffsets(String paramString, String[] paramArrayOfString)
  {
    int[] arrayOfInt = new int[paramArrayOfString.length];
    arrayOfInt[0] = paramString.indexOf(paramArrayOfString[0]);
    for (int i = 1; i < paramArrayOfString.length; i++) {
      arrayOfInt[i] = paramString.indexOf(paramArrayOfString[i], arrayOfInt[(i - 1)] + paramArrayOfString[(i - 1)].length());
    }
    return arrayOfInt;
  }
  
  public static Spanned getSpannedString(Query paramQuery, QueryAlternativesProto.QueryAlternatives paramQueryAlternatives)
  {
    String str = paramQuery.getQueryString();
    Object localObject;
    if (TextUtils.isEmpty(str)) {
      localObject = null;
    }
    for (;;)
    {
      return localObject;
      if ((paramQueryAlternatives == null) || (paramQueryAlternatives.getQueryTokenCount() == 0)) {
        return null;
      }
      if (paramQueryAlternatives.getQuerySegmentAlternativesCount() == 0) {
        return null;
      }
      List localList1 = paramQueryAlternatives.getQueryTokenList();
      String[] arrayOfString = tokenize(str);
      if (!compareTokens(localList1, arrayOfString)) {
        return null;
      }
      localObject = new SpannableStringBuilder(paramQuery.getQueryChars());
      int[] arrayOfInt = computeTokenOffsets(str, arrayOfString);
      int i = paramQueryAlternatives.getQuerySegmentAlternativesCount();
      for (int j = 0; j < i; j++)
      {
        QueryAlternativesProto.QueryAlternatives.QuerySegmentAlternatives localQuerySegmentAlternatives = paramQueryAlternatives.getQuerySegmentAlternatives(j);
        QueryAlternativesProto.QueryAlternatives.QuerySegment localQuerySegment = localQuerySegmentAlternatives.getQuerySegment();
        List localList2 = localQuerySegmentAlternatives.getAlternativeList();
        int k = localQuerySegment.getStartToken();
        int m = -1 + localQuerySegment.getEndToken();
        int n = arrayOfInt[k];
        int i1 = arrayOfInt[m] + arrayOfString[m].length();
        ((SpannableStringBuilder)localObject).setSpan(new VoiceCorrectionSpan(localList2), n, i1, 17);
      }
    }
  }
  
  static String[] tokenize(String paramString)
  {
    return paramString.split("\\s+");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.VoiceCorrectionUtils
 * JD-Core Version:    0.7.0.1
 */