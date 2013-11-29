package com.google.android.sidekick.shared.cards;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext;
import com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext.SharedPreferenceContextEditor;
import com.google.android.sidekick.shared.renderingcontext.SharedTrafficContext;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.android.sidekick.shared.util.ViewInMapsAction;
import com.google.common.base.Strings;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.SharedTrafficCardEntry;
import java.util.concurrent.TimeUnit;

public class SharedTrafficEntryAdapter
  extends BaseEntryAdapter
{
  private final Clock mClock;
  private Sidekick.Location mEntryLocation;
  private Sidekick.SharedTrafficCardEntry mSharedTrafficCardEntry;
  
  SharedTrafficEntryAdapter(Sidekick.Entry paramEntry, Clock paramClock, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    if (!paramEntry.hasSharedTrafficCardEntry()) {
      throw new IllegalArgumentException("entry was expected to have shared_traffic_card_entry: " + paramEntry);
    }
    this.mClock = paramClock;
    captureTrafficCardEntry();
  }
  
  private CharSequence appendLearnMoreLink(Context paramContext, String paramString)
  {
    String str1 = paramContext.getString(2131362563);
    String str2 = paramContext.getString(2131362565);
    return Html.fromHtml(paramString + " <a href=\"" + str2 + "\"><u>" + str1 + "</u></a>");
  }
  
  private void bubbleDown(View paramView)
  {
    View localView = paramView.findViewById(2131296337);
    if (localView != null) {
      localView.setVisibility(8);
    }
  }
  
  private void bubbleForNotSharingLocationOrCommute(final Activity paramActivity, final PredictiveCardContainer paramPredictiveCardContainer, final View paramView)
  {
    if (this.mSharedTrafficCardEntry.hasInterestedInProducerText())
    {
      ((ViewStub)paramView.findViewById(2131296336)).inflate();
      TextView localTextView = (TextView)paramView.findViewById(2131296457);
      Button localButton1 = (Button)paramView.findViewById(2131296462);
      Button localButton2 = (Button)paramView.findViewById(2131296463);
      localTextView.setText(appendLearnMoreLink(paramActivity, this.mSharedTrafficCardEntry.getInterestedInProducerText()));
      localButton2.setText(2131363438);
      localButton1.setText(2131363439);
      localButton2.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          SharedTrafficEntryAdapter.this.updateInSettings(paramActivity.getApplicationContext(), paramPredictiveCardContainer, false);
          SharedTrafficEntryAdapter.this.bubbleDown(paramView);
        }
      });
      localButton1.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          SharedTrafficEntryAdapter.this.updateInSettings(paramActivity.getApplicationContext(), paramPredictiveCardContainer, true);
          SharedTrafficEntryAdapter.this.bubbleDown(paramView);
          paramPredictiveCardContainer.dismissEntry(SharedTrafficEntryAdapter.this.getEntry());
        }
      });
    }
  }
  
  private void captureTrafficCardEntry()
  {
    this.mSharedTrafficCardEntry = getEntry().getSharedTrafficCardEntry();
    this.mEntryLocation = ((Sidekick.Location)com.google.android.shared.util.ProtoUtils.copyOf(this.mSharedTrafficCardEntry.getSharerLocation())).setName(this.mSharedTrafficCardEntry.getSharerName());
  }
  
  public static String confirmationPresentationCountKey(Sidekick.SharedTrafficCardEntry paramSharedTrafficCardEntry)
  {
    return confirmationPresentationCountKey(getPrimaryKey(paramSharedTrafficCardEntry));
  }
  
  public static String confirmationPresentationCountKey(String paramString)
  {
    return "presentation.count" + paramString;
  }
  
  public static String getPrimaryKey(Sidekick.SharedTrafficCardEntry paramSharedTrafficCardEntry)
  {
    return Long.toString(paramSharedTrafficCardEntry.getSharerObfuscatedGaiaId());
  }
  
  private View populateView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    updateTitle(paramContext, paramView);
    updateTimeFrom(paramContext, paramView);
    updateFreshness(paramContext, paramView);
    updateMap(paramContext, paramPredictiveCardContainer, paramView);
    return paramView;
  }
  
  private boolean showHideQuestion(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    if (SharedTrafficContext.fromCardContainer(paramPredictiveCardContainer).isHiddenSet(getPrimaryKey(this.mSharedTrafficCardEntry))) {}
    SharedPreferencesContext localSharedPreferencesContext;
    String str;
    int i;
    do
    {
      return false;
      localSharedPreferencesContext = SharedPreferencesContext.fromCardContainer(paramPredictiveCardContainer);
      str = confirmationPresentationCountKey(this.mSharedTrafficCardEntry);
      i = localSharedPreferencesContext.getInt(str).intValue();
    } while (i >= 3);
    localSharedPreferencesContext.edit().putInt(str, i + 1).apply();
    return true;
  }
  
  private void updateFreshness(Context paramContext, View paramView)
  {
    TextView localTextView = (TextView)paramView.findViewById(2131296999);
    long l1 = this.mSharedTrafficCardEntry.getLastUpdatedTimestampSeconds();
    long l2 = TimeUnit.SECONDS.toMillis(l1);
    long l3 = this.mClock.currentTimeMillis();
    if (l3 - l2 > TimeUnit.MINUTES.toMillis(1L))
    {
      localTextView.setText(paramContext.getString(2131362190, new Object[] { DateUtils.getRelativeTimeSpanString(l2, l3, 60000L, 262144) }));
      return;
    }
    if (l1 > 0L)
    {
      localTextView.setText(paramContext.getString(2131362192));
      return;
    }
    localTextView.setVisibility(8);
  }
  
  private void updateInSettings(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, boolean paramBoolean)
  {
    paramPredictiveCardContainer.setTrafficSharerHiddenState(this.mSharedTrafficCardEntry.getSharerObfuscatedGaiaId(), paramBoolean);
  }
  
  private void updateMap(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = new Sidekick.FrequentPlaceEntry().setFrequentPlace(new Sidekick.FrequentPlace().setLocation(this.mEntryLocation));
    ImageView localImageView = (ImageView)paramView.findViewById(2131296742);
    localImageView.setVisibility(0);
    Sidekick.Location localLocation = LocationUtilities.androidLocationToSidekickLocation(paramPredictiveCardContainer.getCardRenderingContext().getRefreshLocation());
    paramPredictiveCardContainer.getStaticMapLoader().loadMap(localLocation, localFrequentPlaceEntry, false, localImageView);
    paramView.findViewById(2131297000).setVisibility(0);
    if (this.mSharedTrafficCardEntry.hasSharerPhoto())
    {
      String str = com.google.android.sidekick.shared.util.ProtoUtils.getPhotoUrl(this.mSharedTrafficCardEntry.getSharerPhoto());
      if (str != null)
      {
        ViewStub localViewStub = (ViewStub)paramView.findViewById(2131297001);
        if (localViewStub != null) {
          localViewStub.inflate();
        }
        ((WebImageView)paramView.findViewById(2131296787)).setImageUrl(str, paramPredictiveCardContainer.getImageLoader());
      }
    }
  }
  
  private void updateTimeFrom(Context paramContext, View paramView)
  {
    TextView localTextView = (TextView)paramView.findViewById(2131296325);
    int i = this.mSharedTrafficCardEntry.getCommuteState();
    Sidekick.CommuteSummary localCommuteSummary = this.mSharedTrafficCardEntry.getCommuteSummary();
    String str1 = this.mSharedTrafficCardEntry.getLabelOfDestination();
    if ((i == 2) && (localCommuteSummary != null) && (!Strings.isNullOrEmpty(str1)))
    {
      String str2 = TimeUtilities.getEtaString(paramContext, localCommuteSummary.getTravelTimeWithoutDelayInMinutes() + localCommuteSummary.getTrafficDelayInMinutes(), true);
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = str2;
      arrayOfObject[1] = BidiUtils.unicodeWrap(str1);
      localTextView.setText(paramContext.getString(2131362191, arrayOfObject));
      localTextView.setVisibility(0);
      return;
    }
    localTextView.setVisibility(8);
  }
  
  private void updateTitle(Context paramContext, View paramView)
  {
    ((TextView)paramView.findViewById(2131296451)).setText(this.mSharedTrafficCardEntry.getCommuteStateText());
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    return populateView(paramContext, paramPredictiveCardContainer, paramLayoutInflater.inflate(2130968828, paramViewGroup, false));
  }
  
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296312);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    new ViewInMapsAction(paramContext, getActivityHelper(), this.mEntryLocation).run();
  }
  
  public void registerActions(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    super.registerActions(paramActivity, paramPredictiveCardContainer, paramView);
    if (showHideQuestion(paramActivity, paramPredictiveCardContainer)) {
      bubbleForNotSharingLocationOrCommute(paramActivity, paramPredictiveCardContainer, paramView);
    }
  }
  
  public void replaceEntry(Sidekick.Entry paramEntry)
  {
    super.replaceEntry(paramEntry);
    captureTrafficCardEntry();
  }
  
  public View updateView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, View paramView, Sidekick.Entry paramEntry)
  {
    return populateView(paramContext, paramPredictiveCardContainer, paramView);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.SharedTrafficEntryAdapter
 * JD-Core Version:    0.7.0.1
 */