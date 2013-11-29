package com.google.android.sidekick.shared.cards;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext;
import com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext.SharedPreferenceContextEditor;
import com.google.android.sidekick.shared.ui.ConfirmHomeWorkEntryClickListener;
import com.google.android.sidekick.shared.ui.DeletePlaceEntryClickListener;
import com.google.android.sidekick.shared.ui.EditHomeWorkEntryClickListener;
import com.google.android.sidekick.shared.ui.RenameOrDeletePlaceEntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.IntentDispatcherUtil;
import com.google.android.sidekick.shared.util.MapsLauncher.TravelMode;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.TravelReport;
import com.google.android.sidekick.shared.util.ViewInMapsAction;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

public class GenericPlaceEntryAdapter
  extends AbstractPlaceEntryAdapter
{
  GenericPlaceEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, DirectionsLauncher paramDirectionsLauncher, ActivityHelper paramActivityHelper, Clock paramClock)
  {
    super(paramEntry, paramFrequentPlaceEntry, paramDirectionsLauncher, paramActivityHelper, paramClock);
  }
  
  private CharSequence appendLearnMoreLink(Context paramContext, int paramInt)
  {
    String str1 = paramContext.getString(paramInt);
    String str2 = paramContext.getString(2131362563);
    String str3 = paramContext.getString(2131362565);
    return Html.fromHtml(str1 + " <a href=\"" + str3 + "\"><u>" + str2 + "</u></a>");
  }
  
  private void bubbleDown(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    View localView = paramView.findViewById(2131296337);
    if (localView != null) {
      localView.setVisibility(8);
    }
    updateNavigation(paramContext, paramPredictiveCardContainer, paramView);
  }
  
  private View.OnClickListener cancelOnClick(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, final View paramView)
  {
    new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SharedPreferencesContext localSharedPreferencesContext = SharedPreferencesContext.fromCardContainer(paramPredictiveCardContainer);
        GenericPlaceEntryAdapter.this.noteThatUserRespondedToSharedLocationQuestion(paramContext, localSharedPreferencesContext, paramPredictiveCardContainer);
        GenericPlaceEntryAdapter.this.bubbleDown(paramContext, paramPredictiveCardContainer, paramView);
      }
    };
  }
  
  private void configurePlaceConfirmationBanner(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    ((ViewStub)paramView.findViewById(2131296336)).inflate();
    TextView localTextView = (TextView)paramView.findViewById(2131296457);
    Button localButton1 = (Button)paramView.findViewById(2131296462);
    Button localButton2 = (Button)paramView.findViewById(2131296463);
    int i = getRenameOrEditAction().getType();
    if ((i == 17) || (i == 18))
    {
      if (i == 17)
      {
        localTextView.setText(2131362528);
        localButton1.setText(2131362529);
      }
      for (;;)
      {
        localButton1.setOnClickListener(confirmExistingHomeWorkOnClick(paramActivity, paramPredictiveCardContainer));
        localButton2.setText(2131362532);
        localButton2.setOnClickListener(editHomeWorkOnClick(paramActivity, paramPredictiveCardContainer));
        return;
        localTextView.setText(2131362530);
        localButton1.setText(2131362531);
      }
    }
    localTextView.setText(2131362527);
    localButton1.setText(2131363439);
    localButton1.setOnClickListener(deletePlaceOnClick(paramActivity, paramPredictiveCardContainer));
    localButton2.setText(2131363438);
    localButton2.setOnClickListener(renamePlaceOnClick(paramActivity, paramPredictiveCardContainer));
  }
  
  private View.OnClickListener confirmExistingHomeWorkOnClick(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer)
  {
    Sidekick.Action localAction = getEditHomeOrWorkAction();
    return new ConfirmHomeWorkEntryClickListener(paramActivity, paramPredictiveCardContainer, getEntry(), localAction);
  }
  
  private boolean didUserRespondToSharedLocationQuestion(Context paramContext, SharedPreferencesContext paramSharedPreferencesContext)
  {
    return paramSharedPreferencesContext.getBoolean(paramContext.getString(2131362165)).booleanValue();
  }
  
  @Nullable
  private final Sidekick.Action getRenameOrEditAction()
  {
    Iterator localIterator = getEntry().getEntryActionList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Action localAction = (Sidekick.Action)localIterator.next();
      if ((localAction.getType() == 5) || (localAction.getType() == 17) || (localAction.getType() == 18)) {
        return localAction;
      }
    }
    return null;
  }
  
  private View.OnClickListener gotoTrafficCardSettings(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer)
  {
    new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        IntentDispatcherUtil.dispatchIntent(paramContext, "com.google.android.googlequicksearchbox.TRAFFIC_CARD_SETTINGS");
        SharedPreferencesContext localSharedPreferencesContext = SharedPreferencesContext.fromCardContainer(paramPredictiveCardContainer);
        GenericPlaceEntryAdapter.this.noteThatUserRespondedToSharedLocationQuestion(paramContext, localSharedPreferencesContext, paramPredictiveCardContainer);
      }
    };
  }
  
  private void hideActionButtonWithLabel(View paramView, int paramInt)
  {
    View localView = paramView.findViewById(paramInt);
    if (localView != null) {
      localView.setVisibility(8);
    }
  }
  
  private boolean isCommuteCard(Context paramContext)
  {
    Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = getFrequentPlaceEntry();
    return (localFrequentPlaceEntry != null) && (localFrequentPlaceEntry.getFrequentPlace().getIsCommuteDestination());
  }
  
  private boolean isCommuteSharingOn(Context paramContext, SharedPreferencesContext paramSharedPreferencesContext)
  {
    return paramSharedPreferencesContext.getBoolean(paramContext.getString(2131362163)).booleanValue();
  }
  
  private boolean isLocationSharingOn(Context paramContext, SharedPreferencesContext paramSharedPreferencesContext)
  {
    return paramSharedPreferencesContext.getBoolean(paramContext.getString(2131362164)).booleanValue();
  }
  
  private void noteThatUserRespondedToSharedLocationQuestion(Context paramContext, SharedPreferencesContext paramSharedPreferencesContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    String str = paramContext.getString(2131362165);
    paramSharedPreferencesContext.edit().putBoolean(str, true).apply();
  }
  
  private boolean showVogForCommuteSharing(Context paramContext, SharedPreferencesContext paramSharedPreferencesContext)
  {
    if (!isCommuteCard(paramContext)) {}
    while ((isCommuteSharingOn(paramContext, paramSharedPreferencesContext)) && (isLocationSharingOn(paramContext, paramSharedPreferencesContext))) {
      return false;
    }
    return true;
  }
  
  private View.OnClickListener turnOffCommuteSharingOnClick(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, final View paramView)
  {
    new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SharedPreferencesContext localSharedPreferencesContext = SharedPreferencesContext.fromCardContainer(paramPredictiveCardContainer);
        String str = paramContext.getString(2131362163);
        localSharedPreferencesContext.edit().putBoolean(str, false).apply();
        GenericPlaceEntryAdapter.this.noteThatUserRespondedToSharedLocationQuestion(paramContext, localSharedPreferencesContext, paramPredictiveCardContainer);
        GenericPlaceEntryAdapter.this.hideActionButtonWithLabel(paramView, 2131296287);
        GenericPlaceEntryAdapter.this.bubbleDown(paramContext, paramPredictiveCardContainer, paramView);
      }
    };
  }
  
  private void updateNavigation(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (shouldShowNavigation(paramContext))
    {
      TravelReport localTravelReport = getTravelReport();
      if (mightShowNavigateButtonFor(localTravelReport, paramPredictiveCardContainer))
      {
        NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
        MapsLauncher.TravelMode localTravelMode = this.mDirectionsLauncher.getTravelMode(localNavigationContext, localTravelReport.getRoute());
        if (!this.mDirectionsLauncher.modeSupportsNavigation(localTravelMode)) {
          break label104;
        }
        ((Button)paramView.findViewById(2131296334)).setVisibility(0);
      }
    }
    for (;;)
    {
      if (mightShowNavigateButtonFor(getAlternateTravelReport(), paramPredictiveCardContainer)) {
        ((Button)paramView.findViewById(2131296285)).setVisibility(0);
      }
      return;
      label104:
      ((Button)paramView.findViewById(2131296335)).setVisibility(0);
    }
  }
  
  private void vogForNotSharingCommute(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    ((ViewStub)paramView.findViewById(2131296336)).inflate();
    TextView localTextView = (TextView)paramView.findViewById(2131296457);
    Button localButton1 = (Button)paramView.findViewById(2131296462);
    Button localButton2 = (Button)paramView.findViewById(2131296463);
    localTextView.setText(appendLearnMoreLink(paramActivity, 2131362521));
    localTextView.setMovementMethod(LinkMovementMethod.getInstance());
    localButton2.setText(2131362524);
    localButton1.setText(2131362523);
    localButton2.setOnClickListener(gotoTrafficCardSettings(paramActivity, paramPredictiveCardContainer));
    localButton1.setOnClickListener(turnOffCommuteSharingOnClick(paramActivity, paramPredictiveCardContainer, paramView));
  }
  
  private void vogForNotSharingLocation(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    ((ViewStub)paramView.findViewById(2131296336)).inflate();
    TextView localTextView = (TextView)paramView.findViewById(2131296457);
    Button localButton1 = (Button)paramView.findViewById(2131296462);
    Button localButton2 = (Button)paramView.findViewById(2131296463);
    localTextView.setText(2131362522);
    localTextView.setMovementMethod(LinkMovementMethod.getInstance());
    localButton2.setText(2131362526);
    localButton1.setText(2131362525);
    localButton2.setOnClickListener(gotoTrafficCardSettings(paramActivity, paramPredictiveCardContainer));
    localButton1.setOnClickListener(turnOffCommuteSharingOnClick(paramActivity, paramPredictiveCardContainer, paramView));
  }
  
  private void vogForNotSharingLocationOrCommute(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    ((ViewStub)paramView.findViewById(2131296336)).inflate();
    TextView localTextView = (TextView)paramView.findViewById(2131296457);
    Button localButton1 = (Button)paramView.findViewById(2131296462);
    Button localButton2 = (Button)paramView.findViewById(2131296463);
    localTextView.setText(appendLearnMoreLink(paramActivity, 2131362520));
    localTextView.setMovementMethod(LinkMovementMethod.getInstance());
    localButton2.setText(2131362524);
    localButton1.setText(2131362523);
    localButton2.setOnClickListener(gotoTrafficCardSettings(paramActivity, paramPredictiveCardContainer));
    localButton1.setOnClickListener(cancelOnClick(paramActivity, paramPredictiveCardContainer, paramView));
  }
  
  public View.OnClickListener deletePlaceOnClick(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    Sidekick.Action localAction = ProtoUtils.findAction(getEntry(), 14, new int[0]);
    if (localAction == null) {
      return null;
    }
    return new DeletePlaceEntryClickListener(paramContext, paramPredictiveCardContainer, getEntry(), localAction);
  }
  
  public View.OnClickListener editHomeWorkOnClick(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    Sidekick.Action localAction = getEditHomeOrWorkAction();
    if (localAction == null) {
      return null;
    }
    return new EditHomeWorkEntryClickListener(paramContext, paramPredictiveCardContainer, getEntry(), localAction);
  }
  
  @Nullable
  public final Sidekick.Action getEditHomeOrWorkAction()
  {
    Iterator localIterator = getEntry().getEntryActionList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Action localAction = (Sidekick.Action)localIterator.next();
      if ((localAction.getType() == 17) || (localAction.getType() == 18)) {
        return localAction;
      }
    }
    return null;
  }
  
  public String getLoggingName()
  {
    if (placeConfirmationRequested()) {}
    for (String str = "Unconfirmed";; str = "") {
      switch (getRenameOrEditAction().getType())
      {
      default: 
        if (getFrequentPlace().getSourceType() != 1) {
          break label104;
        }
        return "SearchHistoryPlace";
      }
    }
    return str + "Home";
    return str + "Work";
    label104:
    return str + "FrequentPlace";
  }
  
  public String getTitle(Context paramContext)
  {
    if ((placeConfirmationRequested()) && (getEditHomeOrWorkAction() == null))
    {
      int i = paramContext.getResources().getColor(2131230836);
      Locale localLocale = Locale.US;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(0xFFFFFF & i);
      arrayOfObject[1] = paramContext.getString(2131362613);
      return String.format(localLocale, "<font color=\"#%1$h\">%2$s</font>", arrayOfObject);
    }
    return super.getTitle(paramContext);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (placeConfirmationRequested())
    {
      new ViewInMapsAction(paramContext, getActivityHelper(), getLocation()).run();
      return;
    }
    super.launchDetails(paramContext, paramPredictiveCardContainer, paramView);
  }
  
  public void maybeShowFeedbackPrompt(PredictiveCardContainer paramPredictiveCardContainer, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater)
  {
    if (!getEntry().hasUserPrompt()) {}
    boolean bool1;
    boolean bool2;
    boolean bool3;
    do
    {
      Context localContext;
      SharedPreferencesContext localSharedPreferencesContext;
      do
      {
        return;
        localContext = paramViewGroup.getContext();
        localSharedPreferencesContext = SharedPreferencesContext.fromCardContainer(paramPredictiveCardContainer);
      } while (placeConfirmationRequested());
      if (!showVogForCommuteSharing(localContext, localSharedPreferencesContext)) {
        break;
      }
      bool1 = isCommuteSharingOn(localContext, localSharedPreferencesContext);
      bool2 = isLocationSharingOn(localContext, localSharedPreferencesContext);
      bool3 = didUserRespondToSharedLocationQuestion(localContext, localSharedPreferencesContext);
    } while (((!bool3) && (!bool1) && (!bool2)) || ((bool1) && (!bool2)) || ((!bool3) && (!bool1) && (bool2)));
    super.maybeShowFeedbackPrompt(paramPredictiveCardContainer, paramViewGroup, paramLayoutInflater);
  }
  
  public void registerActions(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    super.registerActions(paramActivity, paramPredictiveCardContainer, paramView);
    SharedPreferencesContext localSharedPreferencesContext = SharedPreferencesContext.fromCardContainer(paramPredictiveCardContainer);
    if (placeConfirmationRequested()) {
      configurePlaceConfirmationBanner(paramActivity, paramPredictiveCardContainer, paramView);
    }
    boolean bool1;
    boolean bool2;
    boolean bool3;
    do
    {
      do
      {
        return;
      } while (!showVogForCommuteSharing(paramActivity, localSharedPreferencesContext));
      bool1 = isCommuteSharingOn(paramActivity, localSharedPreferencesContext);
      bool2 = isLocationSharingOn(paramActivity, localSharedPreferencesContext);
      bool3 = didUserRespondToSharedLocationQuestion(paramActivity, localSharedPreferencesContext);
      if ((!bool3) && (!bool1) && (!bool2))
      {
        vogForNotSharingLocationOrCommute(paramActivity, paramPredictiveCardContainer, paramView);
        return;
      }
      if ((bool1) && (!bool2))
      {
        vogForNotSharingLocation(paramActivity, paramPredictiveCardContainer, paramView);
        return;
      }
    } while ((bool3) || (bool1) || (!bool2));
    vogForNotSharingCommute(paramActivity, paramPredictiveCardContainer, paramView);
  }
  
  public View.OnClickListener renamePlaceOnClick(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    Sidekick.Action localAction1 = getRenameOrEditAction();
    if (localAction1 == null) {}
    Sidekick.Action localAction2;
    do
    {
      return null;
      localAction2 = ProtoUtils.findAction(getEntry(), 14, new int[0]);
    } while (localAction2 == null);
    return new RenameOrDeletePlaceEntryClickListener(paramContext, paramPredictiveCardContainer, getEntry(), localAction1, localAction2);
  }
  
  protected boolean shouldShowNavigation(Context paramContext)
  {
    return (!placeConfirmationRequested()) && (!shouldShowTransitView());
  }
  
  protected boolean shouldShowRoute()
  {
    return !placeConfirmationRequested();
  }
  
  protected void updateActionButtons(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    super.updateActionButtons(paramActivity, paramPredictiveCardContainer, paramView);
    SharedPreferencesContext localSharedPreferencesContext = SharedPreferencesContext.fromCardContainer(paramPredictiveCardContainer);
    if ((isCommuteCard(paramActivity)) && (isCommuteSharingOn(paramActivity, localSharedPreferencesContext))) {
      addActionButton(paramActivity, paramView, 2130837633, paramActivity.getString(2131362598), 2131296287).setOnClickListener(gotoTrafficCardSettings(paramActivity, paramPredictiveCardContainer));
    }
  }
  
  public View updateView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, View paramView, Sidekick.Entry paramEntry)
  {
    updateTitle(paramView.getContext(), paramView);
    bubbleDown(paramContext, paramPredictiveCardContainer, paramView);
    showMap(paramView.getContext(), paramPredictiveCardContainer, paramView);
    return paramView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.GenericPlaceEntryAdapter
 * JD-Core Version:    0.7.0.1
 */