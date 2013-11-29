package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.WalletLoyaltyEntry;

public class WalletLoyaltyEntryAdapter
  extends BaseEntryAdapter
{
  private final FifeImageUrlUtil mFifeImageUrlUtil;
  private final Sidekick.WalletLoyaltyEntry mLoyaltyEntry;
  
  WalletLoyaltyEntryAdapter(Sidekick.Entry paramEntry, FifeImageUrlUtil paramFifeImageUrlUtil, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mLoyaltyEntry = paramEntry.getWalletLoyaltyEntry();
    this.mFifeImageUrlUtil = paramFifeImageUrlUtil;
  }
  
  private void setTextTopPadding(TextView paramTextView, int paramInt)
  {
    paramTextView.setPadding(paramTextView.getPaddingLeft(), paramInt, paramTextView.getPaddingRight(), paramTextView.getPaddingBottom());
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968915, paramViewGroup, false);
    if (this.mLoyaltyEntry.hasProgramName())
    {
      TextView localTextView3 = (TextView)localView.findViewById(2131296658);
      localTextView3.setText(this.mLoyaltyEntry.getProgramName());
      localTextView3.setVisibility(0);
    }
    if (this.mLoyaltyEntry.hasPointsMsg())
    {
      TextView localTextView2 = (TextView)localView.findViewById(2131297207);
      localTextView2.setText(this.mLoyaltyEntry.getPointsMsg());
      localTextView2.setVisibility(0);
      if (!this.mLoyaltyEntry.hasBarcodeImage())
      {
        int k = paramContext.getResources().getDimensionPixelSize(2131689831);
        localTextView2.setPadding(localTextView2.getPaddingLeft(), localTextView2.getPaddingTop(), localTextView2.getPaddingRight(), k);
      }
    }
    WebImageView localWebImageView2;
    Sidekick.Photo localPhoto;
    int i;
    int j;
    if (this.mLoyaltyEntry.hasProgramLogoImage())
    {
      localWebImageView2 = (WebImageView)localView.findViewById(2131297208);
      localPhoto = this.mLoyaltyEntry.getProgramLogoImage();
      if (localPhoto.getUrlType() != 1) {
        break label463;
      }
      i = paramContext.getResources().getDimensionPixelSize(2131689829);
      j = paramContext.getResources().getDimensionPixelSize(2131689830);
    }
    label463:
    for (Uri localUri = this.mFifeImageUrlUtil.setImageUrlCenterCrop(i, j, localPhoto.getUrl());; localUri = Uri.parse(localPhoto.getUrl()))
    {
      UriLoader localUriLoader = paramPredictiveCardContainer.getImageLoader();
      localWebImageView2.setImageUri(localUri, localUriLoader);
      localWebImageView2.setVisibility(0);
      if (this.mLoyaltyEntry.hasBarcodeImage())
      {
        WebImageView localWebImageView1 = (WebImageView)localView.findViewById(2131297209);
        localWebImageView1.setImageUri(Uri.parse(this.mLoyaltyEntry.getBarcodeImage().getUrl()), paramPredictiveCardContainer.getImageLoader());
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(localWebImageView1.getLayoutParams());
        localLayoutParams.gravity = 1;
        if (this.mLoyaltyEntry.hasBarcodeAspectRatio()) {
          localLayoutParams.height = ((int)(localLayoutParams.width / this.mLoyaltyEntry.getBarcodeAspectRatio()));
        }
        localWebImageView1.setLayoutParams(localLayoutParams);
        localWebImageView1.setVisibility(0);
      }
      if (this.mLoyaltyEntry.hasBarcodeAlternateText())
      {
        TextView localTextView1 = (TextView)localView.findViewById(2131297210);
        localTextView1.setText(this.mLoyaltyEntry.getBarcodeAlternateText());
        localTextView1.setVisibility(0);
        if (!this.mLoyaltyEntry.hasBarcodeImage()) {
          setTextTopPadding(localTextView1, paramContext.getResources().getDimensionPixelSize(2131689826));
        }
      }
      if (this.mLoyaltyEntry.hasLinkToWallet())
      {
        Button localButton = (Button)localView.findViewById(2131297211);
        localButton.setVisibility(0);
        localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 107)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            WalletLoyaltyEntryAdapter.this.openUrl(paramContext, WalletLoyaltyEntryAdapter.this.mLoyaltyEntry.getLinkToWallet());
          }
        });
      }
      return localView;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.WalletLoyaltyEntryAdapter
 * JD-Core Version:    0.7.0.1
 */