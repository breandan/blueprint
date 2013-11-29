package com.google.android.sidekick.shared.util;

import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.shared.util.SplitIterator;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FifeImageUrlUtil
{
  private static final Pattern FIFE_HOSTED_IMAGE_URL_RE = Pattern.compile("^((http(s)?):)?\\/\\/((((lh[3-6]\\.((ggpht)|(googleusercontent)|(google)))|([1-4]\\.bp\\.blogspot)|(bp[0-3]\\.blogger))\\.com)|(www\\.google\\.com\\/visualsearch\\/lh))\\/");
  private static final Joiner JOIN_ON_SLASH = Joiner.on("/");
  
  private Uri setContentImageUrlOptions(String paramString, Uri paramUri)
  {
    ArrayList localArrayList = Lists.newArrayList(SplitIterator.splitOnCharOmitEmptyStrings(paramUri.getPath(), '='));
    String str = (String)localArrayList.get(0) + "=" + paramString;
    return paramUri.buildUpon().path(str).build();
  }
  
  private Uri setLegacyImageUrlOptions(String paramString, Uri paramUri)
  {
    int i = 1;
    String str = paramUri.getPath();
    ArrayList localArrayList = Lists.newArrayList(SplitIterator.splitOnCharOmitEmptyStrings(str, '/'));
    int j = localArrayList.size();
    int k = 0;
    if (j > 0)
    {
      boolean bool2 = ((String)localArrayList.get(0)).equals("image");
      k = 0;
      if (bool2)
      {
        localArrayList.remove(0);
        k = 1;
      }
    }
    int m = localArrayList.size();
    boolean bool1 = str.endsWith("/");
    int n;
    if ((!bool1) && (m == 5))
    {
      n = i;
      if (m != 4) {
        break label220;
      }
      label113:
      if (n != 0) {
        localArrayList.add(localArrayList.get(4));
      }
      if (i == 0) {
        break label225;
      }
      localArrayList.add(paramString);
    }
    for (;;)
    {
      if (k != 0) {
        localArrayList.add(0, "image");
      }
      if (bool1) {
        localArrayList.add("");
      }
      return paramUri.buildUpon().path("/" + JOIN_ON_SLASH.join(localArrayList)).build();
      n = 0;
      break;
      label220:
      i = 0;
      break label113;
      label225:
      localArrayList.set(4, paramString);
    }
  }
  
  public boolean isFifeHostedUrl(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    return FIFE_HOSTED_IMAGE_URL_RE.matcher(paramString).find();
  }
  
  public Uri setImageUriOptions(String paramString, Uri paramUri)
  {
    ArrayList localArrayList = Lists.newArrayList(SplitIterator.splitOnCharOmitEmptyStrings(paramUri.getPath(), '/'));
    int i = localArrayList.size();
    if ((localArrayList.size() > 1) && (((String)localArrayList.get(0)).equals("image"))) {
      i--;
    }
    if ((i >= 4) && (i <= 6)) {
      return setLegacyImageUrlOptions(paramString, paramUri);
    }
    if ((i == 1) || (i == 2)) {
      return setContentImageUrlOptions(paramString, paramUri);
    }
    return paramUri;
  }
  
  public Uri setImageUrlCenterCrop(int paramInt1, int paramInt2, String paramString)
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(paramInt1);
    arrayOfObject[1] = Integer.valueOf(paramInt2);
    return setImageUrlOptions(String.format(localLocale, "w%d-h%d-n", arrayOfObject), paramString);
  }
  
  public Uri setImageUrlOptions(String paramString1, String paramString2)
  {
    return setImageUriOptions(paramString1, Uri.parse(paramString2));
  }
  
  public Uri setImageUrlSmartCrop(int paramInt1, int paramInt2, String paramString)
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(paramInt1);
    arrayOfObject[1] = Integer.valueOf(paramInt2);
    return setImageUrlOptions(String.format(localLocale, "w%d-h%d-p", arrayOfObject), paramString);
  }
  
  public Uri setImageUrlWidthHeight(int paramInt1, int paramInt2, String paramString)
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(paramInt1);
    arrayOfObject[1] = Integer.valueOf(paramInt2);
    return setImageUrlOptions(String.format(localLocale, "w%d-h%d", arrayOfObject), paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.FifeImageUrlUtil
 * JD-Core Version:    0.7.0.1
 */