package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.cards.BaseEntryAdapter;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Movie;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.Rating;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

public class MovieRow
{
  private final BaseEntryAdapter mEntryItemAdapter;
  private final Sidekick.Movie mMovie;
  private final boolean mShowImage;
  private final boolean mShowTitleWithMenu;
  
  public MovieRow(BaseEntryAdapter paramBaseEntryAdapter, Sidekick.Movie paramMovie, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mEntryItemAdapter = paramBaseEntryAdapter;
    this.mMovie = paramMovie;
    this.mShowImage = paramBoolean1;
    this.mShowTitleWithMenu = paramBoolean2;
  }
  
  @Nullable
  public CharSequence getActorList()
  {
    if (this.mMovie.getActorCount() > 0) {
      return Joiner.on(", ").join(this.mMovie.getActorList());
    }
    return null;
  }
  
  @Nullable
  public String getImageUrl()
  {
    if ((this.mShowImage) && (this.mMovie.hasImage())) {
      return this.mMovie.getImage().getUrl();
    }
    return null;
  }
  
  public CharSequence getMovieTitle()
  {
    return Html.fromHtml(this.mMovie.getTitle());
  }
  
  @Nullable
  public CharSequence getOnCardJustification()
  {
    return this.mMovie.getOnCardJustification();
  }
  
  @Nullable
  public CharSequence getRatingAndMovieLength()
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(3);
    if ((this.mMovie.getRatingCount() > 0) && (!TextUtils.isEmpty(this.mMovie.getRating(0).getRating()))) {
      localArrayList.add(this.mMovie.getRating(0).getRating());
    }
    if (!TextUtils.isEmpty(this.mMovie.getMpaaRating())) {
      localArrayList.add(this.mMovie.getMpaaRating());
    }
    if (!TextUtils.isEmpty(this.mMovie.getLength())) {
      localArrayList.add(this.mMovie.getLength());
    }
    if (localArrayList.size() == 0) {
      return null;
    }
    return CardTextUtil.hyphenate(localArrayList);
  }
  
  @Nullable
  public CharSequence getShowtimes(Context paramContext)
  {
    if (this.mMovie.getShowtimeCount() > 0)
    {
      int i = paramContext.getResources().getColor(2131230842);
      Locale localLocale = Locale.US;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(0xFFFFFF & i);
      arrayOfObject[1] = this.mMovie.getShowtime(0);
      String str = String.format(localLocale, "<font color=\"#%1$h\">%2$s</font>", arrayOfObject);
      if (this.mMovie.getShowtimeCount() > 1) {
        str = Joiner.on(", ").join(str, Joiner.on(", ").join(this.mMovie.getShowtimeList().listIterator(1)), new Object[0]);
      }
      return Html.fromHtml(str);
    }
    return null;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968751, paramViewGroup, false);
    populateView(paramContext, paramPredictiveCardContainer, localView);
    return localView;
  }
  
  public void populateView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    Sidekick.Rating localRating;
    label204:
    String str2;
    label227:
    final String str3;
    label250:
    TextView localTextView5;
    int i;
    if (this.mShowTitleWithMenu)
    {
      ((ViewStub)paramView.findViewById(2131296799)).inflate();
      ((TextView)paramView.findViewById(2131296451)).setText(getMovieTitle());
      String str1 = getImageUrl();
      if (!TextUtils.isEmpty(str1))
      {
        paramView.findViewById(2131296333).setVisibility(0);
        ((WebImageView)paramView.findViewById(2131296796)).setImageUrl(str1, paramPredictiveCardContainer.getImageLoader());
        if (this.mMovie.hasTrailerUrl())
        {
          ImageButton localImageButton = (ImageButton)paramView.findViewById(2131296797);
          localImageButton.setVisibility(0);
          localImageButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, this.mEntryItemAdapter.getEntry(), 74)
          {
            public void onEntryClick(View paramAnonymousView)
            {
              MovieRow.this.mEntryItemAdapter.openUrl(paramContext, MovieRow.this.mMovie.getTrailerUrl());
            }
          });
        }
      }
      CharSequence localCharSequence1 = getActorList();
      if (!TextUtils.isEmpty(localCharSequence1))
      {
        TextView localTextView6 = (TextView)paramView.findViewById(2131296802);
        localTextView6.setText(localCharSequence1);
        localTextView6.setVisibility(0);
      }
      CharSequence localCharSequence2 = getRatingAndMovieLength();
      if (!TextUtils.isEmpty(localCharSequence2))
      {
        if (this.mMovie.getRatingCount() <= 0) {
          break label505;
        }
        localRating = this.mMovie.getRating(0);
        if ((localRating == null) || (TextUtils.isEmpty(localRating.getRating()))) {
          break label511;
        }
        str2 = localRating.getRating();
        if ((localRating == null) || (TextUtils.isEmpty(localRating.getUrl()))) {
          break label517;
        }
        str3 = localRating.getUrl();
        localTextView5 = (TextView)paramView.findViewById(2131296803);
        if ((str2 == null) || (str3 == null)) {
          break label543;
        }
        if (localRating.hasFreshness())
        {
          i = localRating.getFreshness();
          if (i != 2) {
            break label523;
          }
          LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localTextView5, 2130837812, 0, 0, 0);
        }
        label304:
        localTextView5.setEnabled(true);
        localTextView5.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, this.mEntryItemAdapter.getEntry(), 137)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            MovieRow.this.mEntryItemAdapter.openUrl(paramContext, str3);
          }
        });
        label337:
        localTextView5.setText(localCharSequence2);
        localTextView5.setVisibility(0);
      }
      CharSequence localCharSequence3 = getShowtimes(paramContext);
      if (!TextUtils.isEmpty(localCharSequence3))
      {
        TextView localTextView4 = (TextView)paramView.findViewById(2131296801);
        localTextView4.setText(localCharSequence3);
        localTextView4.setVisibility(0);
      }
      if (this.mMovie.hasWebUrl()) {
        paramView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, this.mEntryItemAdapter.getEntry(), 138)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            MovieRow.this.mEntryItemAdapter.openUrl(paramContext, MovieRow.this.mMovie.getWebUrl());
          }
        });
      }
      this.mEntryItemAdapter.getEntry();
      CharSequence localCharSequence4 = this.mEntryItemAdapter.getReminderFormattedEventDate();
      if (TextUtils.isEmpty(localCharSequence4)) {
        break label552;
      }
      TextView localTextView3 = (TextView)paramView.findViewById(2131296321);
      localTextView3.setText(localCharSequence4);
      localTextView3.setVisibility(0);
    }
    label505:
    label511:
    CharSequence localCharSequence5;
    label517:
    label523:
    label543:
    label552:
    do
    {
      return;
      TextView localTextView1 = (TextView)paramView.findViewById(2131296800);
      localTextView1.setText(getMovieTitle());
      localTextView1.setVisibility(0);
      break;
      localRating = null;
      break label204;
      str2 = null;
      break label227;
      str3 = null;
      break label250;
      if (i != 1) {
        break label304;
      }
      LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localTextView5, 2130837811, 0, 0, 0);
      break label304;
      localTextView5.setBackgroundColor(0);
      break label337;
      localCharSequence5 = getOnCardJustification();
    } while (TextUtils.isEmpty(localCharSequence5));
    TextView localTextView2 = (TextView)paramView.findViewById(2131296554);
    localTextView2.setText(localCharSequence5);
    localTextView2.setVisibility(0);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.MovieRow
 * JD-Core Version:    0.7.0.1
 */