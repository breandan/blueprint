package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.BusinessData;
import com.google.geo.sidekick.Sidekick.BusinessData.BusinessTimeSeconds;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.PlaceData;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;

public class PlaceDataHelper
{
  private static final Double MAX_STAR_RATING = Double.valueOf(5.0D);
  private static final Double MIN_STAR_RATING;
  private static final DecimalFormat ONE_DP_FORMATTER = new DecimalFormat("0.0");
  private static final List<Integer> STAR_RATINGS;
  private final FifeImageUrlUtil mFifeImageUrlUtil;
  
  static
  {
    Integer[] arrayOfInteger = new Integer[9];
    arrayOfInteger[0] = Integer.valueOf(2130838049);
    arrayOfInteger[1] = Integer.valueOf(2130838050);
    arrayOfInteger[2] = Integer.valueOf(2130838051);
    arrayOfInteger[3] = Integer.valueOf(2130838052);
    arrayOfInteger[4] = Integer.valueOf(2130838053);
    arrayOfInteger[5] = Integer.valueOf(2130838054);
    arrayOfInteger[6] = Integer.valueOf(2130838055);
    arrayOfInteger[7] = Integer.valueOf(2130838056);
    arrayOfInteger[8] = Integer.valueOf(2130838057);
    STAR_RATINGS = Lists.newArrayList(arrayOfInteger);
    MIN_STAR_RATING = Double.valueOf(1.0D);
  }
  
  public PlaceDataHelper(FifeImageUrlUtil paramFifeImageUrlUtil)
  {
    this.mFifeImageUrlUtil = paramFifeImageUrlUtil;
  }
  
  static double getClippedStarRating(int paramInt)
  {
    return Math.max(MIN_STAR_RATING.doubleValue(), Math.min(MAX_STAR_RATING.doubleValue(), paramInt / 1000.0D));
  }
  
  static int getStarImageResource(double paramDouble)
  {
    boolean bool1 = true;
    int i = -2 + (int)Math.round(2.0D * paramDouble);
    boolean bool2;
    if (i >= 0)
    {
      bool2 = bool1;
      Assert.assertTrue(bool2);
      if (i >= STAR_RATINGS.size()) {
        break label65;
      }
    }
    for (;;)
    {
      Assert.assertTrue(bool1);
      return ((Integer)STAR_RATINGS.get(i)).intValue();
      bool2 = false;
      break;
      label65:
      bool1 = false;
    }
  }
  
  public static void populateStarRating(TextView paramTextView, ImageView paramImageView, int paramInt, boolean paramBoolean)
  {
    double d = getClippedStarRating(paramInt);
    if (paramBoolean)
    {
      paramTextView.setText(ONE_DP_FORMATTER.format(d));
      paramTextView.setVisibility(0);
    }
    paramImageView.setImageResource(getStarImageResource(d));
    paramImageView.setVisibility(0);
  }
  
  public void populateBusinessDataWithJustification(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, Sidekick.FrequentPlace paramFrequentPlace)
  {
    TextView localTextView = (TextView)paramView.findViewById(2131296351);
    Sidekick.BusinessData localBusinessData = paramFrequentPlace.getPlaceData().getBusinessData();
    if ((localBusinessData.hasBusinessTime()) && (localBusinessData.getBusinessTime().hasClosingTimeSeconds()))
    {
      long l = 1000L * localBusinessData.getBusinessTime().getClosingTimeSeconds();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = DateUtils.formatDateTime(paramContext, l, 1);
      localTextView.setText(paramContext.getString(2131362779, arrayOfObject));
      localTextView.setVisibility(0);
    }
    for (;;)
    {
      populateJustificationOrKnownForTerms(paramView, paramFrequentPlace);
      if (localBusinessData.hasCoverPhoto())
      {
        WebImageView localWebImageView = (WebImageView)paramView.findViewById(2131296350);
        localWebImageView.setVisibility(0);
        Sidekick.Photo localPhoto = localBusinessData.getCoverPhoto();
        Uri localUri = Uri.parse(localBusinessData.getCoverPhoto().getUrl());
        if (localPhoto.getUrlType() == 2)
        {
          int i = LayoutUtils.getCardWidth(paramContext) / 2;
          int j = (int)paramContext.getResources().getDimension(2131689597);
          localUri = this.mFifeImageUrlUtil.setImageUrlSmartCrop(i, j, localUri.toString());
        }
        localWebImageView.setImageUri(localUri, paramPredictiveCardContainer.getImageLoader());
      }
      return;
      if (localBusinessData.hasOpenUntil())
      {
        localTextView.setText(Html.fromHtml(localBusinessData.getOpenUntil()));
        localTextView.setVisibility(0);
      }
    }
  }
  
  void populateJustificationOrKnownForTerms(View paramView, Sidekick.FrequentPlace paramFrequentPlace)
  {
    Sidekick.BusinessData localBusinessData = paramFrequentPlace.getPlaceData().getBusinessData();
    if (paramFrequentPlace.hasJustification())
    {
      str = paramFrequentPlace.getJustification();
      if (!str.isEmpty())
      {
        localTextView = (TextView)paramView.findViewById(2131296353);
        localTextView.setText(str);
        localTextView.setVisibility(0);
      }
    }
    while (localBusinessData.getKnownForTermCount() <= 0)
    {
      String str;
      TextView localTextView;
      return;
    }
    CardTextUtil.setTextView(paramView, 2131296352, Html.fromHtml(CardTextUtil.hyphenate(localBusinessData.getKnownForTermList()).toString()));
  }
  
  public void populatePlaceReview(Context paramContext, View paramView, Sidekick.BusinessData paramBusinessData)
  {
    if (paramBusinessData.hasNumRatingStarsE3()) {
      populateStarRating((TextView)paramView.findViewById(2131296861), (ImageView)paramView.findViewById(2131296862), paramBusinessData.getNumRatingStarsE3(), true);
    }
    for (;;)
    {
      ArrayList localArrayList = Lists.newArrayListWithCapacity(2);
      if (paramBusinessData.hasNumReviews())
      {
        Resources localResources = paramContext.getResources();
        int i = paramBusinessData.getNumReviews();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(paramBusinessData.getNumReviews());
        localArrayList.add(localResources.getQuantityString(2131558433, i, arrayOfObject));
      }
      if (paramBusinessData.hasPriceLevel()) {
        localArrayList.add(paramBusinessData.getPriceLevel());
      }
      if (localArrayList.size() != 0) {
        CardTextUtil.setTextView(paramView, 2131296863, Html.fromHtml(CardTextUtil.hyphenate(localArrayList).toString()));
      }
      return;
      if (paramBusinessData.hasReviewScore())
      {
        TextView localTextView = (TextView)paramView.findViewById(2131296859);
        localTextView.setText(Integer.toString(paramBusinessData.getReviewScore()));
        localTextView.setVisibility(0);
        int j = paramView.getPaddingTop() + paramContext.getResources().getDimensionPixelSize(2131689812);
        paramView.setPadding(paramView.getPaddingLeft(), j, paramView.getPaddingRight(), paramView.getPaddingBottom());
      }
      if (paramBusinessData.getZagatRated()) {
        ((TextView)paramView.findViewById(2131296860)).setVisibility(0);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.PlaceDataHelper
 * JD-Core Version:    0.7.0.1
 */