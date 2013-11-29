package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.WalletOfferEntry;
import javax.annotation.Nullable;

public class WalletOfferEntryAdapter
  extends BaseEntryAdapter
{
  static final int MIN_DAYS_TO_EXPIRATION = 10;
  static final int MIN_HOURS_TO_EXPIRATION = 6;
  private final Clock mClock;
  private final DateUtilsWrapper mDateUtilsWrapper;
  private final FifeImageUrlUtil mFifeImageUrlUtil;
  private final Sidekick.WalletOfferEntry mOfferEntry;
  
  WalletOfferEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, Clock paramClock, FifeImageUrlUtil paramFifeImageUrlUtil)
  {
    this(paramEntry, paramActivityHelper, paramClock, paramFifeImageUrlUtil, new DateUtilsWrapper());
  }
  
  WalletOfferEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, Clock paramClock, FifeImageUrlUtil paramFifeImageUrlUtil, DateUtilsWrapper paramDateUtilsWrapper)
  {
    super(paramEntry, paramActivityHelper);
    this.mOfferEntry = paramEntry.getWalletOfferEntry();
    this.mFifeImageUrlUtil = paramFifeImageUrlUtil;
    this.mDateUtilsWrapper = paramDateUtilsWrapper;
    this.mClock = paramClock;
  }
  
  private void createBody(View paramView, Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    if ((this.mOfferEntry.hasBarcodeImage()) || (this.mOfferEntry.hasBarcodeAlternateText())) {}
    for (int i = 1;; i = 0)
    {
      if (this.mOfferEntry.hasBarcodeImage())
      {
        WebImageView localWebImageView = (WebImageView)paramView.findViewById(2131297216);
        localWebImageView.setImageUri(Uri.parse(this.mOfferEntry.getBarcodeImage().getUrl()), paramPredictiveCardContainer.getImageLoader());
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(localWebImageView.getLayoutParams());
        localLayoutParams.gravity = 1;
        if (this.mOfferEntry.hasBarcodeAspectRatio()) {
          localLayoutParams.height = ((int)(localLayoutParams.width / this.mOfferEntry.getBarcodeAspectRatio()));
        }
        localWebImageView.setLayoutParams(localLayoutParams);
        localWebImageView.setVisibility(0);
      }
      if (this.mOfferEntry.hasBarcodeAlternateText())
      {
        TextView localTextView3 = (TextView)paramView.findViewById(2131297210);
        localTextView3.setText(this.mOfferEntry.getBarcodeAlternateText());
        localTextView3.setVisibility(0);
        if (!this.mOfferEntry.hasBarcodeImage()) {
          setTextTopPadding(localTextView3, paramContext.getResources().getDimensionPixelSize(2131689826));
        }
      }
      boolean bool = this.mOfferEntry.hasOfferProviderMsg();
      TextView localTextView1 = (TextView)paramView.findViewById(2131297218);
      if (bool)
      {
        localTextView1.setText(this.mOfferEntry.getOfferProviderMsg());
        localTextView1.setVisibility(0);
      }
      if (i == 0)
      {
        if (this.mOfferEntry.hasRedemptionCode())
        {
          TextView localTextView2 = (TextView)paramView.findViewById(2131297217);
          localTextView2.setText(this.mOfferEntry.getRedemptionCode());
          localTextView2.setVisibility(0);
        }
        if (!bool) {
          break;
        }
        setTextTopPadding(localTextView1, paramContext.getResources().getDimensionPixelSize(2131689827));
      }
      return;
    }
    paramView.findViewById(2131296370).setVisibility(8);
  }
  
  private String getPlural(int paramInt1, int paramInt2, Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt2);
    return localResources.getQuantityString(paramInt1, paramInt2, arrayOfObject);
  }
  
  private void setTextTopPadding(TextView paramTextView, int paramInt)
  {
    paramTextView.setPadding(paramTextView.getPaddingLeft(), paramInt, paramTextView.getPaddingRight(), paramTextView.getPaddingBottom());
  }
  
  @Nullable
  String getExpirationMsg(long paramLong, Context paramContext)
  {
    long l1 = this.mClock.currentTimeMillis();
    long l2 = paramLong - l1;
    if (l2 < 0L) {}
    long l4;
    do
    {
      long l3;
      int i;
      do
      {
        return null;
        l3 = l2 / 3600000L;
        l4 = l2 / 86400000L;
        if (l3 != 0L) {
          break;
        }
        i = (int)(l2 / 60000L);
      } while (i <= 0);
      return getPlural(2131558441, i, paramContext);
      if (l3 < 6L) {
        return getPlural(2131558440, (int)l3, paramContext);
      }
      if (this.mDateUtilsWrapper.isToday(paramLong)) {
        return getPlural(2131558440, (int)l3, paramContext);
      }
      if (TimeUtilities.isTomorrow(paramLong, l1)) {
        return paramContext.getResources().getString(2131362791);
      }
    } while (l4 >= 10L);
    return getPlural(2131558439, (int)l4, paramContext);
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968916, paramViewGroup, false);
    WebImageView localWebImageView;
    Sidekick.Photo localPhoto;
    int i;
    if (this.mOfferEntry.hasTitleImage())
    {
      localWebImageView = (WebImageView)localView.findViewById(2131297212);
      localPhoto = this.mOfferEntry.getTitleImage();
      if (localPhoto.getUrlType() != 1) {
        break label292;
      }
      i = paramContext.getResources().getDimensionPixelSize(2131689823);
    }
    label292:
    for (Uri localUri = this.mFifeImageUrlUtil.setImageUrlWidthHeight(i, i, localPhoto.getUrl());; localUri = Uri.parse(localPhoto.getUrl()))
    {
      UriLoader localUriLoader = paramPredictiveCardContainer.getImageLoader();
      localWebImageView.setImageUri(localUri, localUriLoader);
      localWebImageView.setVisibility(0);
      if (this.mOfferEntry.hasIssuerName())
      {
        TextView localTextView2 = (TextView)localView.findViewById(2131297213);
        localTextView2.setText(this.mOfferEntry.getIssuerName());
        localTextView2.setVisibility(0);
      }
      if (this.mOfferEntry.hasTitle()) {
        ((TextView)localView.findViewById(2131297214)).setText(this.mOfferEntry.getTitle());
      }
      if (this.mOfferEntry.hasExpirationTimeMs())
      {
        String str = getExpirationMsg(this.mOfferEntry.getExpirationTimeMs(), paramContext);
        if (str != null)
        {
          TextView localTextView1 = (TextView)localView.findViewById(2131297215);
          localTextView1.setText(str);
          localTextView1.setVisibility(0);
        }
      }
      createBody(localView, paramContext, paramPredictiveCardContainer);
      if (this.mOfferEntry.hasLinkToWallet())
      {
        Button localButton = (Button)localView.findViewById(2131297211);
        localButton.setVisibility(0);
        localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 108)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            WalletOfferEntryAdapter.this.openUrl(paramContext, WalletOfferEntryAdapter.this.mOfferEntry.getLinkToWallet());
          }
        });
      }
      return localView;
    }
  }
  
  public static class DateUtilsWrapper
  {
    public boolean isToday(long paramLong)
    {
      return DateUtils.isToday(paramLong);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.WalletOfferEntryAdapter
 * JD-Core Version:    0.7.0.1
 */