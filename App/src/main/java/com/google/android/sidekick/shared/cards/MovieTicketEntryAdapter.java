package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.GoogleServiceWebviewClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.GoogleServiceWebviewUtil;
import com.google.android.sidekick.shared.util.MoonshineEventTicketUtils;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GmailReference;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Movie;
import com.google.geo.sidekick.Sidekick.MovieTicketEntry;
import com.google.geo.sidekick.Sidekick.Photo;

public class MovieTicketEntryAdapter
  extends BaseEntryAdapter
{
  private final Clock mClock;
  private final DirectionsLauncher mDirectionsLauncher;
  private final Sidekick.MovieTicketEntry mMovieEntry;
  
  MovieTicketEntryAdapter(Sidekick.Entry paramEntry, DirectionsLauncher paramDirectionsLauncher, ActivityHelper paramActivityHelper, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mMovieEntry = paramEntry.getMovieTicketEntry();
    this.mDirectionsLauncher = paramDirectionsLauncher;
    this.mClock = paramClock;
  }
  
  private void addGmailButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mMovieEntry.hasGmailReference()) && (this.mMovieEntry.getGmailReference().hasEmailUrl()))
    {
      String str = this.mMovieEntry.getGmailReference().getEmailUrl();
      Button localButton = (Button)paramView.findViewById(2131296376);
      localButton.setVisibility(0);
      localButton.setOnClickListener(new GoogleServiceWebviewClickListener(paramContext, str, this.mMovieEntry.getMovie().getTitle(), false, this, 72, "mail", GoogleServiceWebviewUtil.GMAIL_URL_PREFIXES, null, paramPredictiveCardContainer));
    }
  }
  
  private String getShowtimeText(Context paramContext)
  {
    boolean bool = this.mMovieEntry.hasShowtimeSeconds();
    String str = null;
    if (bool) {
      str = DateUtils.formatDateTime(paramContext, 1000L * this.mMovieEntry.getShowtimeSeconds(), 1);
    }
    return str;
  }
  
  private void maybeAddNavigateButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (showNavigation(paramPredictiveCardContainer)) {
      configureRouteButtons(paramView, this.mMovieEntry.getRoute(), paramContext, this.mDirectionsLauncher, paramPredictiveCardContainer, this.mMovieEntry.getTheater(), true);
    }
  }
  
  private void maybeAddTimeToLeaveBanner(Context paramContext, View paramView, PredictiveCardContainer paramPredictiveCardContainer)
  {
    long l = this.mClock.currentTimeMillis();
    if (this.mMovieEntry.hasShowtimeSeconds()) {}
    for (Long localLong1 = Long.valueOf(1000L * this.mMovieEntry.getShowtimeSeconds());; localLong1 = null)
    {
      boolean bool = this.mMovieEntry.hasDepartureTimeMs();
      Long localLong2 = null;
      if (bool) {
        localLong2 = Long.valueOf(this.mMovieEntry.getDepartureTimeMs());
      }
      if (MoonshineEventTicketUtils.shouldShowTtlBanner(l, localLong1, localLong2, showNavigation(paramPredictiveCardContainer))) {
        break;
      }
      return;
    }
    MoonshineEventTicketUtils.addTtlBanner(paramView, l - this.mMovieEntry.getDepartureTimeMs(), paramContext, this.mMovieEntry.getDepartureTimeMs());
  }
  
  private void populateMovieInfo(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, boolean paramBoolean)
  {
    final Sidekick.Movie localMovie = this.mMovieEntry.getMovie();
    ((TextView)paramView.findViewById(2131296451)).setText(Html.fromHtml(localMovie.getTitle()));
    if (localMovie.hasImage())
    {
      paramView.findViewById(2131296333).setVisibility(0);
      Sidekick.Photo localPhoto = localMovie.getImage();
      ((WebImageView)paramView.findViewById(2131296552)).setImageUrl(localPhoto.getUrl(), paramPredictiveCardContainer.getImageLoader());
      if (localMovie.hasTrailerUrl())
      {
        ImageButton localImageButton = (ImageButton)paramView.findViewById(2131296560);
        localImageButton.setVisibility(0);
        localImageButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 73)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            MovieTicketEntryAdapter.this.openUrl(paramContext, localMovie.getTrailerUrl());
          }
        });
      }
    }
  }
  
  private void populateShowtimeInfo(Context paramContext, View paramView)
  {
    if ((this.mMovieEntry.hasTheater()) && (this.mMovieEntry.getTheater().hasName()))
    {
      Sidekick.Location localLocation = this.mMovieEntry.getTheater();
      TextView localTextView2 = (TextView)paramView.findViewById(2131296575);
      localTextView2.setText(localLocation.getName());
      localTextView2.setVisibility(0);
    }
    String str1 = getShowtimeText(paramContext);
    if (str1 != null)
    {
      String str2 = paramContext.getString(2131362585, new Object[] { str1 });
      ((TextView)paramView.findViewById(2131296562)).setText(str2);
      TextView localTextView1 = (TextView)paramView.findViewById(2131296576);
      localTextView1.setText(DateUtils.formatDateTime(paramContext, 1000L * this.mMovieEntry.getShowtimeSeconds(), 524310));
      localTextView1.setVisibility(0);
    }
  }
  
  private void populateTicketInfo(View paramView, Context paramContext, LayoutInflater paramLayoutInflater)
  {
    TableLayout localTableLayout = (TableLayout)((ViewStub)paramView.findViewById(2131296571)).inflate();
    if (this.mMovieEntry.hasNumberOfTickets()) {
      ((TextView)localTableLayout.findViewById(2131296588)).setText(Integer.toString(this.mMovieEntry.getNumberOfTickets()));
    }
    if ((this.mMovieEntry.hasTheater()) && (this.mMovieEntry.getTheater().hasName())) {
      ((TextView)localTableLayout.findViewById(2131296575)).setText(this.mMovieEntry.getTheater().getName());
    }
    if (this.mMovieEntry.hasShowtimeSeconds())
    {
      String str = DateUtils.formatDateTime(paramContext, 1000L * this.mMovieEntry.getShowtimeSeconds(), 524310);
      ((TextView)localTableLayout.findViewById(2131296586)).setText(str);
    }
  }
  
  private View showBarCodeCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968673, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(Html.fromHtml(this.mMovieEntry.getMovie().getTitle()).toString());
    String str1 = getShowtimeText(paramContext);
    if (str1 != null)
    {
      String str2 = paramContext.getString(2131362585, new Object[] { str1 });
      ((TextView)localView.findViewById(2131296562)).setText(str2);
    }
    if ((this.mMovieEntry.hasBarcode()) && (this.mMovieEntry.getBarcode().hasUrl())) {
      MoonshineEventTicketUtils.addBarcode(paramPredictiveCardContainer, localView, this.mMovieEntry.getBarcode().getUrl());
    }
    if (this.mMovieEntry.hasConfirmationNumber()) {
      MoonshineEventTicketUtils.addTicketNumber(localView, this.mMovieEntry.getConfirmationNumber());
    }
    populateTicketInfo(localView, paramContext, paramLayoutInflater);
    populateMovieInfo(paramContext, paramPredictiveCardContainer, localView, true);
    return localView;
  }
  
  private View showMovieCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968674, paramViewGroup, false);
    populateMovieInfo(paramContext, paramPredictiveCardContainer, localView, false);
    populateShowtimeInfo(paramContext, localView);
    return localView;
  }
  
  private boolean showNavigation(PredictiveCardContainer paramPredictiveCardContainer)
  {
    NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
    return (this.mMovieEntry.hasTheater()) && (this.mMovieEntry.hasRoute()) && (localNavigationContext.shouldShowNavigation(this.mMovieEntry.getTheater()));
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (this.mMovieEntry.hasConfirmationNumber()) {}
    for (View localView = showBarCodeCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);; localView = showMovieCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup))
    {
      maybeAddTimeToLeaveBanner(paramContext, localView, paramPredictiveCardContainer);
      maybeAddNavigateButton(paramContext, paramPredictiveCardContainer, localView);
      addGmailButton(paramContext, paramPredictiveCardContainer, localView);
      return localView;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.MovieTicketEntryAdapter
 * JD-Core Version:    0.7.0.1
 */