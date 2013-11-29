package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.geo.sidekick.Sidekick.ImageLureCardEntry;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.Iterator;
import java.util.List;

public class ImageLureCardUtil
{
  public static View createView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Sidekick.ImageLureCardEntry paramImageLureCardEntry, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator, int paramInt)
  {
    View localView1 = paramLayoutInflater.inflate(2130968718, paramViewGroup, false);
    TextView localTextView = (TextView)localView1.findViewById(2131296451);
    localTextView.setText(Html.fromHtml(paramImageLureCardEntry.getTitle()));
    if (paramImageLureCardEntry.hasJustification()) {
      CardTextUtil.setTextView(localView1, 2131296699, paramImageLureCardEntry.getJustification());
    }
    boolean bool = TextUtils.isEmpty(paramImageLureCardEntry.getText());
    View localView2 = null;
    if (!bool)
    {
      localView2 = localView1.findViewById(2131296700);
      CardTextUtil.setTextView(localView1, 2131296700, Html.fromHtml(paramImageLureCardEntry.getText()));
    }
    int i = Math.min(paramImageLureCardEntry.getPhotoCount(), paramInt);
    int n;
    if (i > 0)
    {
      LinearLayout localLinearLayout = (LinearLayout)localView1.findViewById(2131296701);
      localLinearLayout.setVisibility(0);
      int k = paramContext.getResources().getDimensionPixelSize(getImageHeightResId(i));
      int m = 0;
      Iterator localIterator = paramImageLureCardEntry.getPhotoList().iterator();
      do
      {
        if (!localIterator.hasNext()) {
          break;
        }
        Sidekick.Photo localPhoto = (Sidekick.Photo)localIterator.next();
        m++;
        if (m != i) {
          break label261;
        }
        n = 1;
        View localView3 = paramLayoutInflater.inflate(2130968719, localLinearLayout, false);
        localView3.getLayoutParams().height = k;
        paramPhotoWithAttributionDecorator.decorateWithoutClickHandlers(paramContext, paramPredictiveCardContainer, localView3, localPhoto, 0, 0);
        if (n != 0) {
          localView3.setPadding(0, 0, 0, 0);
        }
        localLinearLayout.addView(localView3);
      } while (n == 0);
    }
    for (;;)
    {
      return localView1;
      label261:
      n = 0;
      break;
      if (localView2 != null) {}
      for (Object localObject = localView2; localObject != null; localObject = localTextView)
      {
        int j = paramContext.getResources().getDimensionPixelSize(2131689721);
        LayoutUtils.setPaddingRelative((View)localObject, LayoutUtils.getPaddingStart((View)localObject), ((View)localObject).getPaddingTop(), LayoutUtils.getPaddingEnd((View)localObject), j);
        return localView1;
      }
    }
  }
  
  private static int getImageHeightResId(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 2131689876;
    case 1: 
      return 2131689879;
    case 2: 
      return 2131689878;
    }
    return 2131689877;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.ImageLureCardUtil
 * JD-Core Version:    0.7.0.1
 */