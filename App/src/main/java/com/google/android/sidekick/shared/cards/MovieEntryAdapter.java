package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.MovieRow;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Movie;
import com.google.geo.sidekick.Sidekick.MovieEntry;
import javax.annotation.Nullable;

public class MovieEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.MovieEntry mMovieEntry;
  private final MovieRow mMovieRow;
  
  MovieEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mMovieEntry = paramEntry.getMovieEntry();
    this.mMovieRow = new MovieRow(this, this.mMovieEntry.getMovieEntry(), true, true);
  }
  
  @Nullable
  public CharSequence getDescription(Context paramContext)
  {
    CharSequence localCharSequence1 = this.mMovieRow.getShowtimes(paramContext);
    if (!TextUtils.isEmpty(localCharSequence1)) {
      return localCharSequence1;
    }
    CharSequence localCharSequence2 = this.mMovieRow.getActorList();
    if (!TextUtils.isEmpty(localCharSequence2)) {
      return localCharSequence2;
    }
    return this.mMovieRow.getRatingAndMovieLength();
  }
  
  @Nullable
  public String getImageUrl()
  {
    return this.mMovieRow.getImageUrl();
  }
  
  @Nullable
  public CharSequence getOnCardJustification()
  {
    return this.mMovieRow.getOnCardJustification();
  }
  
  public CharSequence getTitle()
  {
    return this.mMovieRow.getMovieTitle();
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968750, paramViewGroup, false);
    this.mMovieRow.populateView(paramContext, paramPredictiveCardContainer, localView);
    localView.findViewById(2131296798).setBackgroundColor(0);
    return localView;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    Sidekick.Movie localMovie = this.mMovieEntry.getMovieEntry();
    if (localMovie.hasWebUrl()) {
      openUrl(paramContext, localMovie.getWebUrl());
    }
    while (TextUtils.isEmpty(getTitle())) {
      return;
    }
    paramPredictiveCardContainer.startWebSearch(getTitle() + " " + paramContext.getString(2131362784), null);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.MovieEntryAdapter
 * JD-Core Version:    0.7.0.1
 */