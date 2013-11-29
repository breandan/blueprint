package com.google.android.search.core.util;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.VoiceCorrectionSpan;
import com.google.quality.genie.proto.QueryAlternativesProto;
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
  
  public static Spanned getSpannedString(Query query, QueryAlternativesProto.QueryAlternatives queryAlternatives)
  {
      String queryString = query.getQueryString();
      if(TextUtils.isEmpty(queryString)) {
          return null;
      }
      if((queryAlternatives == null) || (queryAlternatives.getQueryTokenCount() == 0)) {
          return null;
      }
      if(queryAlternatives.getQuerySegmentAlternativesCount() == 0) {
          return null;
      }
      List<String> tokensSeenByServer = queryAlternatives.getQueryTokenList();
      String[] tokensSeenByClient = tokenize(queryString);
      if(!compareTokens(tokensSeenByServer, tokensSeenByClient)) {
          return null;
      }
      SpannableStringBuilder spanned = new SpannableStringBuilder(query.getQueryChars());
      int[] tokenOffsets = computeTokenOffsets(queryString, tokensSeenByClient);
      int segmentCount = queryAlternatives.getQuerySegmentAlternativesCount();
      for(int i = 0x0; i < segmentCount; i = i + 0x1) {
          QueryAlternativesProto.QueryAlternatives.QuerySegmentAlternatives alternatives = queryAlternatives.getQuerySegmentAlternatives(i);
          QueryAlternativesProto.QueryAlternatives.QuerySegment segment = alternatives.getQuerySegment();
          List<String> alternates = alternatives.getAlternativeList();
          int tokenStart = segment.getStartToken();
          int tokenEnd = segment.getEndToken() - 0x1;
          int offsetStart = tokenOffsets[tokenStart];
          int offsetEnd = tokenOffsets[tokenEnd] + tokensSeenByClient[tokenEnd].length();
          spanned.setSpan(new VoiceCorrectionSpan(alternates), offsetStart, offsetEnd, 0x11);
      }
      return spanned;
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