package com.google.android.shared.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import java.util.Locale;

public class LayoutUtils
{
  public static int getCardWidth(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    DisplayMetrics localDisplayMetrics = localResources.getDisplayMetrics();
    int i = localResources.getInteger(2131427457);
    return getMaxContentWidth(paramContext, localDisplayMetrics.widthPixels, false) / i;
  }
  
  public static int getContentPadding(Context paramContext, int paramInt)
  {
    int i = getMaxContentWidth(paramContext, paramInt, false);
    return Math.max((int)paramContext.getResources().getDimension(2131689656), (paramInt - i) / 2);
  }
  
  public static int getContentPaddingToMatchPortrait(Context paramContext, int paramInt)
  {
    DisplayMetrics localDisplayMetrics = paramContext.getResources().getDisplayMetrics();
    if (localDisplayMetrics.widthPixels > localDisplayMetrics.heightPixels) {}
    for (int i = localDisplayMetrics.heightPixels + paramContext.getResources().getDimensionPixelSize(2131689579);; i = localDisplayMetrics.widthPixels)
    {
      int j = getMaxContentWidth(paramContext, i, false);
      return Math.max((int)paramContext.getResources().getDimension(2131689656), (paramInt - j) / 2);
    }
  }
  
  public static Point getContextHeaderSize(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    DisplayMetrics localDisplayMetrics = localResources.getDisplayMetrics();
    return new Point(localDisplayMetrics.widthPixels, Math.max(24 * localDisplayMetrics.heightPixels / 100, localResources.getDimensionPixelSize(2131689603)));
  }
  
  public static int getMaxContentWidth(Context paramContext, int paramInt, boolean paramBoolean)
  {
    Resources localResources = paramContext.getResources();
    int i = (int)localResources.getDimension(2131689655);
    int j = localResources.getInteger(2131427438);
    if (paramBoolean) {}
    for (int k = (int)localResources.getDimension(2131689656);; k = 0) {
      return Math.min(paramInt, Math.max(paramInt * j / 100, i) + k * 2);
    }
  }
  
  public static int getPaddingEnd(View paramView)
  {
    if (Build.VERSION.SDK_INT >= 17) {
      return paramView.getPaddingEnd();
    }
    return paramView.getPaddingRight();
  }
  
  public static int getPaddingStart(View paramView)
  {
    if (Build.VERSION.SDK_INT >= 17) {
      return paramView.getPaddingStart();
    }
    return paramView.getPaddingLeft();
  }
  
  @TargetApi(17)
  public static boolean isDefaultLocaleRtl()
  {
    if (Build.VERSION.SDK_INT >= 17) {
      return 1 == TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
    }
    return false;
  }
  
  @TargetApi(17)
  public static boolean isLayoutRtl(View paramView)
  {
    if (Build.VERSION.SDK_INT >= 17) {
      return 1 == paramView.getLayoutDirection();
    }
    return false;
  }
  
  @TargetApi(17)
  public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView paramTextView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (Build.VERSION.SDK_INT >= 17)
    {
      paramTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    }
    paramTextView.setCompoundDrawablesWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static void setMarginsRelative(ViewGroup.MarginLayoutParams paramMarginLayoutParams, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramMarginLayoutParams.setMargins(paramInt1, paramInt2, paramInt3, paramInt4);
    if (Build.VERSION.SDK_INT >= 17)
    {
      paramMarginLayoutParams.setMarginStart(paramInt1);
      paramMarginLayoutParams.setMarginEnd(paramInt3);
    }
  }
  
  public static void setPaddingRelative(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (Build.VERSION.SDK_INT >= 17)
    {
      paramView.setPaddingRelative(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    }
    paramView.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.LayoutUtils
 * JD-Core Version:    0.7.0.1
 */