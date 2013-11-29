package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.MovieRow;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Movie;
import com.google.geo.sidekick.Sidekick.MovieListEntry;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.Iterator;
import java.util.List;

public class MovieListEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.MovieListEntry mMovieListEntry;
  
  MovieListEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mMovieListEntry = paramEntry.getMovieListEntry();
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968752, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(2131362581);
    if (this.mMovieListEntry.hasTheaterName())
    {
      TextView localTextView = (TextView)localView.findViewById(2131296804);
      localTextView.setText(Html.fromHtml(this.mMovieListEntry.getTheaterName()));
      localTextView.setVisibility(0);
    }
    boolean bool = true;
    Iterator localIterator1 = this.mMovieListEntry.getMovieEntryList().iterator();
    while (localIterator1.hasNext())
    {
      Sidekick.Movie localMovie = (Sidekick.Movie)localIterator1.next();
      if ((!localMovie.hasImage()) || (!localMovie.getImage().hasUrl())) {
        bool = false;
      }
    }
    LinearLayout localLinearLayout = (LinearLayout)localView.findViewById(2131296805);
    Iterator localIterator2 = this.mMovieListEntry.getMovieEntryList().iterator();
    while (localIterator2.hasNext()) {
      localLinearLayout.addView(new MovieRow(this, (Sidekick.Movie)localIterator2.next(), bool, false).getView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, localLinearLayout));
    }
    if (this.mMovieListEntry.hasMoreUrl())
    {
      Button localButton = (Button)localView.findViewById(2131296806);
      localButton.setVisibility(0);
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 71)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          MovieListEntryAdapter.this.openUrl(paramContext, MovieListEntryAdapter.this.mMovieListEntry.getMoreUrl());
        }
      });
    }
    return localView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.MovieListEntryAdapter
 * JD-Core Version:    0.7.0.1
 */