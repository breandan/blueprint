package com.google.android.sidekick.shared.util;

import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.google.android.shared.util.LayoutUtils;

public class PublicAlertsResponseUtil
{
  public static Uri getStaticMapUrl(Uri paramUri, double paramDouble, int paramInt1, int paramInt2)
  {
    int i = 1;
    if ((paramDouble > 1.5D) && (paramInt1 <= 512) && (paramInt2 <= 512)) {}
    for (i = 4;; i = 2) {
      do
      {
        return paramUri.buildUpon().appendQueryParameter("w", String.valueOf(paramInt1)).appendQueryParameter("h", String.valueOf(paramInt2)).appendQueryParameter("scale", String.valueOf(i)).build();
      } while ((paramDouble <= 1.0D) || (paramInt1 > 1024) || (paramInt2 > 1024));
    }
  }
  
  public static Uri getStaticMapUrl(Uri paramUri, ImageView paramImageView)
  {
    DisplayMetrics localDisplayMetrics = paramImageView.getResources().getDisplayMetrics();
    int i = LayoutUtils.getCardWidth(paramImageView.getContext());
    int j = paramImageView.getLayoutParams().height;
    return getStaticMapUrl(paramUri, localDisplayMetrics.density, i, j);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.PublicAlertsResponseUtil
 * JD-Core Version:    0.7.0.1
 */