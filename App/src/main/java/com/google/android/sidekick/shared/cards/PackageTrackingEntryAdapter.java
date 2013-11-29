package com.google.android.sidekick.shared.cards;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.IntentUtils;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.renderingcontext.AccountContext;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.GoogleServiceWebviewClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.android.sidekick.shared.util.GoogleServiceWebviewUtil;
import com.google.android.sidekick.shared.util.MapsLauncher.TravelMode;
import com.google.android.sidekick.shared.util.MoonshineUtilities;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GmailReference;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.PackageItem;
import com.google.geo.sidekick.Sidekick.PackageTrackingEntry;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class PackageTrackingEntryAdapter
  extends BaseEntryAdapter
{
  private final DirectionsLauncher mDirectionsLauncher;
  private final FifeImageUrlUtil mFifeImageUrlUtil;
  private final IntentUtils mIntentUtils;
  private final Sidekick.PackageTrackingEntry mPackageTrackingEntry;
  
  PackageTrackingEntryAdapter(Sidekick.Entry paramEntry, IntentUtils paramIntentUtils, FifeImageUrlUtil paramFifeImageUrlUtil, DirectionsLauncher paramDirectionsLauncher, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mPackageTrackingEntry = paramEntry.getPackageTrackingEntry();
    this.mIntentUtils = paramIntentUtils;
    this.mFifeImageUrlUtil = paramFifeImageUrlUtil;
    this.mDirectionsLauncher = paramDirectionsLauncher;
  }
  
  private String estimatedArrivalDateText(Context paramContext)
  {
    if (isToday()) {
      return paramContext.getString(2131362742);
    }
    return getTimeString(paramContext, this.mPackageTrackingEntry.getEstimatedDeliverySecs());
  }
  
  private String getElapsedTimeString(Context paramContext)
  {
    long l = TimeUnit.SECONDS.toMillis(this.mPackageTrackingEntry.getLastUpdateTimeSecs());
    return TimeUtilities.getRelativeElapsedString(paramContext, System.currentTimeMillis() - l, true);
  }
  
  private int getEstimatedDeliveryDateColor(Context paramContext)
  {
    if (isToday()) {}
    for (int i = 2131230842;; i = 2131230834) {
      return paramContext.getResources().getColor(i);
    }
  }
  
  private String getItemFromText(int paramInt, Context paramContext)
  {
    if (paramInt == 0) {
      return paramContext.getString(2131362223);
    }
    Resources localResources = paramContext.getResources();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    return localResources.getQuantityString(2131558419, paramInt, arrayOfObject);
  }
  
  private Intent getShopperIntent(Account paramAccount, String paramString)
  {
    Intent localIntent = new Intent("com.google.android.apps.shopper.intent.action.ORDER_DETAILS");
    localIntent.setPackage("com.google.android.apps.shopper");
    localIntent.putExtra("order_id", paramString);
    localIntent.putExtra("account_for_order", paramAccount.name);
    return localIntent;
  }
  
  private String getStatus(Context paramContext)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mPackageTrackingEntry.getStatus();
    return paramContext.getString(2131362620, arrayOfObject);
  }
  
  private int getStatusTitleId()
  {
    if ((this.mPackageTrackingEntry.hasStatus()) && (this.mPackageTrackingEntry.hasStatusCode())) {}
    switch (this.mPackageTrackingEntry.getStatusCode())
    {
    case 2: 
    case 3: 
    default: 
      return 2131362615;
    case 0: 
      return 2131362621;
    case 1: 
      return 2131362622;
    case 4: 
      return 2131362623;
    case 5: 
      return 2131362624;
    case 7: 
      return 2131362625;
    }
    return 2131362626;
  }
  
  @Nullable
  private Integer getStatusUpdateColor()
  {
    if ((this.mPackageTrackingEntry.hasStatus()) && (this.mPackageTrackingEntry.hasStatusCode())) {}
    switch (this.mPackageTrackingEntry.getStatusCode())
    {
    default: 
      return null;
    case 4: 
      return Integer.valueOf(2131230842);
    case 5: 
      return Integer.valueOf(2131230842);
    case 6: 
      return Integer.valueOf(2131230840);
    case 0: 
    case 1: 
    case 7: 
      return Integer.valueOf(2131230842);
    }
    return Integer.valueOf(2131230840);
  }
  
  private CharSequence getStatusUpdateText(Context paramContext)
  {
    if (this.mPackageTrackingEntry.hasStatus())
    {
      Integer localInteger = getStatusUpdateColor();
      if (localInteger == null) {}
      for (Object localObject = this.mPackageTrackingEntry.getStatus();; localObject = CardTextUtil.color(this.mPackageTrackingEntry.getStatus(), paramContext.getResources().getColor(localInteger.intValue())))
      {
        boolean bool = this.mPackageTrackingEntry.hasLastUpdateTimeSecs();
        String str = null;
        if (bool) {
          str = getElapsedTimeString(paramContext);
        }
        return CardTextUtil.hyphenate(new CharSequence[] { localObject, str });
      }
    }
    return "";
  }
  
  private String getTimeString(Context paramContext, long paramLong)
  {
    return DateUtils.formatDateTime(paramContext, 1000L * paramLong, 18);
  }
  
  private boolean isToday()
  {
    long l = this.mPackageTrackingEntry.getEstimatedDeliverySecs();
    return DateUtils.isToday(TimeUnit.SECONDS.toMillis(l));
  }
  
  private void openCardDetailsPage(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    if ((this.mPackageTrackingEntry.hasSecondaryPageUrl()) && (!this.mPackageTrackingEntry.getSecondaryPageUrl().isEmpty()))
    {
      if (this.mPackageTrackingEntry.getSecondaryPageUrlRequiresGaiaLogin()) {
        openInWebView(paramContext, paramPredictiveCardContainer, this.mPackageTrackingEntry.getSecondaryPageUrl());
      }
    }
    else {
      return;
    }
    openUrl(paramContext, this.mPackageTrackingEntry.getSecondaryPageUrl());
  }
  
  private void openInWebView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, String paramString)
  {
    new GoogleServiceWebviewClickListener(paramContext, paramString, getTitle(paramContext).toString(), false, this, -1, null, GoogleServiceWebviewUtil.ALL_URL_PREFIXES, null, paramPredictiveCardContainer).onEntryClick(null);
  }
  
  private boolean shouldShowPickupLocation()
  {
    if (!this.mPackageTrackingEntry.hasPickupLocation()) {}
    Sidekick.Location localLocation;
    do
    {
      return false;
      localLocation = this.mPackageTrackingEntry.getPickupLocation();
      if ((localLocation.hasName()) && (!localLocation.getName().isEmpty())) {
        return true;
      }
    } while ((!localLocation.hasAddress()) || (localLocation.getAddress().isEmpty()));
    return true;
  }
  
  public CharSequence getTitle(Context paramContext)
  {
    if (this.mPackageTrackingEntry.hasStatus()) {
      return getStatus(paramContext);
    }
    return paramContext.getString(2131362615);
  }
  
  @Nullable
  public Integer getTitleColor(Context paramContext)
  {
    if ((this.mPackageTrackingEntry.hasStatus()) && (this.mPackageTrackingEntry.hasStatusCode())) {}
    switch (this.mPackageTrackingEntry.getStatusCode())
    {
    default: 
      return null;
    case 0: 
    case 1: 
      return Integer.valueOf(paramContext.getResources().getColor(2131230842));
    }
    return Integer.valueOf(paramContext.getResources().getColor(2131230840));
  }
  
  public View getView(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968769, paramViewGroup, false);
    TextView localTextView1 = (TextView)localView.findViewById(2131296830);
    TextView localTextView2 = (TextView)localView.findViewById(2131296831);
    int i = this.mPackageTrackingEntry.getItemsCount();
    int j;
    label640:
    Button localButton2;
    if (i > 0)
    {
      Sidekick.PackageItem localPackageItem = this.mPackageTrackingEntry.getItems(0);
      localTextView1.setText(Html.fromHtml(localPackageItem.getName()));
      localTextView1.setVisibility(0);
      if (localPackageItem.hasPhoto())
      {
        WebImageView localWebImageView = (WebImageView)localView.findViewById(2131296839);
        Uri localUri = Uri.parse(localPackageItem.getPhoto().getUrl());
        if (localPackageItem.getPhoto().getUrlType() == 1)
        {
          int k = Math.min((int)paramContext.getResources().getDimension(2131689787), 220);
          localUri = this.mFifeImageUrlUtil.setImageUrlCenterCrop(k, k, localPackageItem.getPhoto().getUrl());
        }
        UriLoader localUriLoader = paramPredictiveCardContainer.getImageLoader();
        localWebImageView.setImageUri(localUri, localUriLoader);
        localWebImageView.setVisibility(0);
      }
      localTextView2.setText(getStatusUpdateText(paramContext));
      if (this.mPackageTrackingEntry.hasFromAddress())
      {
        TextView localTextView7 = (TextView)localView.findViewById(2131296833);
        localTextView7.setText(getItemFromText(i, paramContext));
        localTextView7.setVisibility(0);
        TextView localTextView8 = (TextView)localView.findViewById(2131296834);
        localTextView8.setText(this.mPackageTrackingEntry.getFromAddress());
        localTextView8.setVisibility(0);
      }
      if (this.mPackageTrackingEntry.hasEstimatedDeliverySecs())
      {
        TextView localTextView5 = (TextView)localView.findViewById(2131296835);
        localTextView5.setText(paramContext.getString(2131362741));
        localTextView5.setVisibility(0);
        TextView localTextView6 = (TextView)localView.findViewById(2131296836);
        localTextView6.setText(estimatedArrivalDateText(paramContext));
        localTextView6.setTextColor(getEstimatedDeliveryDateColor(paramContext));
        localTextView6.setVisibility(0);
      }
      if (shouldShowPickupLocation())
      {
        TextView localTextView3 = (TextView)localView.findViewById(2131296837);
        localTextView3.setText(paramContext.getString(2131362743));
        localTextView3.setVisibility(0);
        final Sidekick.Location localLocation = this.mPackageTrackingEntry.getPickupLocation();
        StringBuilder localStringBuilder = new StringBuilder();
        if ((localLocation.hasName()) && (!localLocation.getName().isEmpty())) {
          localStringBuilder.append(localLocation.getName());
        }
        if ((localLocation.hasAddress()) && (!localLocation.getAddress().isEmpty()))
        {
          if (localStringBuilder.length() > 0) {
            localStringBuilder.append("\n");
          }
          localStringBuilder.append(localLocation.getAddress());
        }
        if ((this.mPackageTrackingEntry.hasPickupLocationAdditionalInformation()) && (!this.mPackageTrackingEntry.getPickupLocationAdditionalInformation().isEmpty()))
        {
          if (localStringBuilder.length() > 0) {
            localStringBuilder.append("\n");
          }
          localStringBuilder.append(this.mPackageTrackingEntry.getPickupLocationAdditionalInformation());
        }
        TextView localTextView4 = (TextView)localView.findViewById(2131296838);
        localTextView4.setText(localStringBuilder);
        localTextView4.setVisibility(0);
        Button localButton3 = (Button)localView.findViewById(2131296335);
        localButton3.setVisibility(0);
        localButton3.setText(2131362187);
        NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
        final MapsLauncher.TravelMode localTravelMode = this.mDirectionsLauncher.getTravelMode(localNavigationContext, null);
        boolean bool = this.mDirectionsLauncher.modeSupportsNavigation(localTravelMode);
        Sidekick.Entry localEntry = getEntry();
        if (!bool) {
          break label899;
        }
        j = 11;
        localButton3.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, localEntry, j)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            PackageTrackingEntryAdapter.this.mDirectionsLauncher.start(localLocation, null, localTravelMode, null);
          }
        });
      }
      if ((this.mPackageTrackingEntry.hasTrackingUrl()) && (!this.mPackageTrackingEntry.getTrackingUrl().isEmpty()))
      {
        localButton2 = (Button)localView.findViewById(2131296840);
        localButton2.setVisibility(0);
        localButton2.setText(this.mPackageTrackingEntry.getSecondaryPageUrlTitle());
        if (!this.mPackageTrackingEntry.getTrackingButtonUrlRequiresGaiaLogin()) {
          break label906;
        }
        LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localButton2, 2130837928, 0, 0, 0);
      }
    }
    for (;;)
    {
      localButton2.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 77)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          if ((PackageTrackingEntryAdapter.this.mPackageTrackingEntry.getPackageStatusUpdatesEnabled()) && (PackageTrackingEntryAdapter.this.mPackageTrackingEntry.hasOrderId()))
          {
            String str = PackageTrackingEntryAdapter.this.mPackageTrackingEntry.getOrderId();
            AccountContext localAccountContext = AccountContext.fromCardContainer(paramPredictiveCardContainer);
            Intent localIntent = PackageTrackingEntryAdapter.this.getShopperIntent(localAccountContext.getActiveUserAccount(), str);
            if (PackageTrackingEntryAdapter.this.mIntentUtils.isIntentHandled(paramContext, localIntent)) {
              try
              {
                paramContext.startActivity(localIntent);
                return;
              }
              catch (SecurityException localSecurityException)
              {
                PackageTrackingEntryAdapter.this.openUrlForButton(paramContext, paramPredictiveCardContainer);
                return;
              }
            }
            PackageTrackingEntryAdapter.this.openUrlForButton(paramContext, paramPredictiveCardContainer);
            return;
          }
          PackageTrackingEntryAdapter.this.openUrlForButton(paramContext, paramPredictiveCardContainer);
        }
      });
      Button localButton1 = (Button)localView.findViewById(2131296376);
      Sidekick.GmailReference localGmailReference = MoonshineUtilities.getEffectiveGmailReferenceAndSetText(paramContext, localButton1, this.mPackageTrackingEntry.getGmailReferenceList());
      if (localGmailReference != null)
      {
        localButton1.setVisibility(0);
        localButton1.setOnClickListener(new GoogleServiceWebviewClickListener(paramContext, localGmailReference.getEmailUrl(), getTitle(paramContext).toString(), false, this, 78, "mail", GoogleServiceWebviewUtil.GMAIL_URL_PREFIXES, null, paramPredictiveCardContainer));
      }
      return localView;
      localTextView1.setText(paramContext.getString(getStatusTitleId()));
      Integer localInteger = getStatusUpdateColor();
      if (localInteger != null) {
        localTextView1.setTextColor(paramContext.getResources().getColor(localInteger.intValue()));
      }
      localTextView1.setVisibility(0);
      localTextView2.setText(getElapsedTimeString(paramContext));
      break;
      label899:
      j = 120;
      break label640;
      label906:
      LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localButton2, 2130837706, 0, 0, 0);
    }
  }
  
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296829);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mPackageTrackingEntry.getPackageStatusUpdatesEnabled()) && (this.mPackageTrackingEntry.hasOrderId()))
    {
      String str = this.mPackageTrackingEntry.getOrderId();
      Intent localIntent = getShopperIntent(AccountContext.fromCardContainer(paramPredictiveCardContainer).getActiveUserAccount(), str);
      if (this.mIntentUtils.isIntentHandled(paramContext, localIntent)) {
        try
        {
          paramContext.startActivity(localIntent);
          return;
        }
        catch (SecurityException localSecurityException)
        {
          openCardDetailsPage(paramContext, paramPredictiveCardContainer);
          return;
        }
      }
      openCardDetailsPage(paramContext, paramPredictiveCardContainer);
      return;
    }
    openCardDetailsPage(paramContext, paramPredictiveCardContainer);
  }
  
  void openUrlForButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    if (this.mPackageTrackingEntry.getTrackingButtonUrlRequiresGaiaLogin())
    {
      openInWebView(paramContext, paramPredictiveCardContainer, this.mPackageTrackingEntry.getTrackingUrl());
      paramPredictiveCardContainer.logAnalyticsAction("PACKAGE_TRACKING_EMAIL_WEB_VIEW", getLoggingName());
      return;
    }
    openUrl(paramContext, this.mPackageTrackingEntry.getTrackingUrl());
    paramPredictiveCardContainer.logAnalyticsAction("PACKAGE_TRACKING_EMAIL_BROWSER", getLoggingName());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.PackageTrackingEntryAdapter
 * JD-Core Version:    0.7.0.1
 */