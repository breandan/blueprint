package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.GoogleServiceWebviewClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CarRentalEntryUtil;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.GoogleServiceWebviewUtil;
import com.google.geo.sidekick.Sidekick.CarRentalEntry;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GmailReference;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.TimeWithZone;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class CarRentalEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.CarRentalEntry mCarRentalEntry;
  private final Clock mClock;
  private final DirectionsLauncher mDirectionsLauncher;
  @Nullable
  private final Sidekick.Location mRentalLocation;
  
  public CarRentalEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, DirectionsLauncher paramDirectionsLauncher, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mCarRentalEntry = paramEntry.getCarRentalEntry();
    this.mDirectionsLauncher = paramDirectionsLauncher;
    this.mRentalLocation = CarRentalEntryUtil.getCarRentalLocation(this.mCarRentalEntry);
    this.mClock = paramClock;
  }
  
  private void addDateTimeToCard(Context paramContext, View paramView, long paramLong, int paramInt1, int paramInt2)
  {
    long l = TimeUnit.SECONDS.toMillis(paramLong);
    setVisibleWithText(paramView, paramInt1, DateUtils.formatDateTime(paramContext, l, 524310));
    setVisibleWithText(paramView, paramInt2, DateUtils.formatDateTime(paramContext, l, 1));
  }
  
  private void addGmailButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mCarRentalEntry.hasGmailReference()) && (this.mCarRentalEntry.getGmailReference().hasEmailUrl()))
    {
      String str = this.mCarRentalEntry.getGmailReference().getEmailUrl();
      Button localButton = (Button)paramView.findViewById(2131296376);
      localButton.setVisibility(0);
      localButton.setOnClickListener(new GoogleServiceWebviewClickListener(paramContext, str, this.mCarRentalEntry.getTitle(), false, this, 50, "mail", GoogleServiceWebviewUtil.GMAIL_URL_PREFIXES, null, paramPredictiveCardContainer));
    }
  }
  
  private void getDropoffCardUniqueElements(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    maybeAddTimeToLeaveBanner(paramContext, paramView, paramPredictiveCardContainer);
    if (this.mCarRentalEntry.hasReturnTime())
    {
      addDateTimeToCard(paramContext, paramView, this.mCarRentalEntry.getReturnTime().getSeconds(), 2131296430, 2131296433);
      ((TextView)paramView.findViewById(2131296433));
      if (!isLateForDropoff()) {
        break label179;
      }
    }
    label179:
    for (int i = 2131230869;; i = 2131230868)
    {
      int j = paramContext.getResources().getColor(i);
      ((TextView)paramView.findViewById(2131296433)).setTextColor(j);
      setLocationWithAddress(this.mRentalLocation, 2131296431, 2131296434, paramView, paramContext);
      if (this.mCarRentalEntry.hasReturnPhone())
      {
        Button localButton = (Button)paramView.findViewById(2131296436);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = BidiUtils.unicodeWrap(this.mCarRentalEntry.getProviderName());
        localButton.setText(paramContext.getString(2131362835, arrayOfObject));
        localButton.setVisibility(0);
        localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 49)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            Intent localIntent = new Intent("android.intent.action.CALL");
            String str = CarRentalEntryAdapter.this.mCarRentalEntry.getReturnPhone();
            localIntent.setData(Uri.parse("tel:" + str));
            paramContext.startActivity(localIntent);
          }
        });
      }
      return;
    }
  }
  
  private void getPickupCardUniqueElements(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mCarRentalEntry.hasConfirmationNumber())
    {
      ((TextView)paramView.findViewById(2131296439)).setVisibility(0);
      setVisibleWithText(paramView, 2131296441, this.mCarRentalEntry.getConfirmationNumber());
    }
    if (this.mCarRentalEntry.hasPickupTime()) {
      addDateTimeToCard(paramContext, paramView, this.mCarRentalEntry.getPickupTime().getSeconds(), 2131296443, 2131296446);
    }
    if (this.mCarRentalEntry.hasPickupLocation()) {
      setLocationWithAddress(this.mCarRentalEntry.getPickupLocation(), 2131296444, 2131296447, paramView, paramContext);
    }
    if (this.mCarRentalEntry.hasManageReservationUrl())
    {
      final String str = this.mCarRentalEntry.getManageReservationUrl();
      Button localButton = (Button)paramView.findViewById(2131296448);
      if (localButton != null)
      {
        localButton.setVisibility(0);
        localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 116)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            CarRentalEntryAdapter.this.openUrl(paramContext, str);
          }
        });
      }
    }
  }
  
  private boolean isLateForDropoff()
  {
    if (!this.mCarRentalEntry.hasDepartureTimeMs()) {}
    long l;
    do
    {
      return false;
      l = this.mCarRentalEntry.getDepartureTimeMs();
    } while (this.mClock.currentTimeMillis() - l <= 0L);
    return true;
  }
  
  private void maybeAddNavigateButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (showNavigation(paramPredictiveCardContainer)) {
      configureRouteButtons(paramView, this.mCarRentalEntry.getRoute(), paramContext, this.mDirectionsLauncher, paramPredictiveCardContainer, this.mRentalLocation, true);
    }
  }
  
  private void maybeAddTimeToLeaveBanner(Context paramContext, View paramView, PredictiveCardContainer paramPredictiveCardContainer)
  {
    if (!this.mCarRentalEntry.hasDepartureTimeMs()) {}
    long l1;
    long l2;
    do
    {
      return;
      l1 = this.mCarRentalEntry.getDepartureTimeMs();
      l2 = this.mClock.currentTimeMillis() - l1;
    } while ((l2 < -900000L) || (!showNavigation(paramPredictiveCardContainer)));
    TextView localTextView = (TextView)paramView.findViewById(2131296424);
    if (l2 >= 0L) {
      localTextView.setText(paramContext.getString(2131362584));
    }
    for (;;)
    {
      localTextView.setVisibility(0);
      return;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = DateUtils.formatDateTime(paramContext, l1, 1);
      localTextView.setText(paramContext.getString(2131362583, arrayOfObject));
    }
  }
  
  private void setLocationWithAddress(@Nullable Sidekick.Location paramLocation, int paramInt1, int paramInt2, View paramView, Context paramContext)
  {
    if (paramLocation != null)
    {
      if (paramLocation.hasName()) {
        setVisibleWithText(paramView, paramInt1, paramLocation.getName());
      }
      setVisibleWithText(paramView, paramInt2, paramLocation.getAddress());
    }
  }
  
  private void setVisibleWithText(View paramView, int paramInt, String paramString)
  {
    TextView localTextView = (TextView)paramView.findViewById(paramInt);
    localTextView.setText(paramString);
    localTextView.setVisibility(0);
  }
  
  private boolean showNavigation(PredictiveCardContainer paramPredictiveCardContainer)
  {
    NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
    return (this.mRentalLocation != null) && (this.mCarRentalEntry.hasRoute()) && (localNavigationContext.shouldShowNavigation(this.mRentalLocation));
  }
  
  boolean areLocationsSame(Sidekick.Location paramLocation1, Sidekick.Location paramLocation2)
  {
    if ((paramLocation1 == null) && (paramLocation2 == null)) {}
    while (((paramLocation1 != null) && (paramLocation2 != null) && (paramLocation1.hasLat()) && (paramLocation2.hasLat()) && (Math.abs(paramLocation1.getLat() - paramLocation2.getLat()) < 1.0E-006D) && (paramLocation1.hasLng()) && (paramLocation2.hasLng()) && (Math.abs(paramLocation1.getLng() - paramLocation2.getLng()) < 1.0E-006D)) || (isAddressSame(paramLocation1, paramLocation2))) {
      return true;
    }
    return false;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView;
    if (this.mCarRentalEntry.getType() == 1)
    {
      localView = paramLayoutInflater.inflate(2130968621, paramViewGroup, false);
      getPickupCardUniqueElements(paramContext, paramPredictiveCardContainer, localView);
    }
    for (;;)
    {
      if (this.mCarRentalEntry.hasTitle()) {
        setVisibleWithText(localView, 2131296451, this.mCarRentalEntry.getTitle());
      }
      if (this.mCarRentalEntry.hasSubtitle()) {
        setVisibleWithText(localView, 2131296425, this.mCarRentalEntry.getSubtitle());
      }
      if (this.mCarRentalEntry.hasRenterName()) {
        setVisibleWithText(localView, 2131296427, this.mCarRentalEntry.getRenterName());
      }
      if (this.mCarRentalEntry.hasRoute()) {
        maybeAddNavigateButton(paramContext, paramPredictiveCardContainer, localView);
      }
      if (this.mCarRentalEntry.hasGmailReference()) {
        addGmailButton(paramContext, paramPredictiveCardContainer, localView);
      }
      return localView;
      localView = paramLayoutInflater.inflate(2130968620, paramViewGroup, false);
      getDropoffCardUniqueElements(paramContext, paramPredictiveCardContainer, localView);
    }
  }
  
  boolean isAddressSame(Sidekick.Location paramLocation1, Sidekick.Location paramLocation2)
  {
    if ((paramLocation1 == null) && (paramLocation2 == null)) {}
    while ((paramLocation1 != null) && (paramLocation2 != null) && (paramLocation1.hasAddress()) && (paramLocation1.getAddress().equals(paramLocation2.getAddress()))) {
      return true;
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.CarRentalEntryAdapter
 * JD-Core Version:    0.7.0.1
 */