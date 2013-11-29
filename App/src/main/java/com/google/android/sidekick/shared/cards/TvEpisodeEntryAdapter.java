package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.TvEpisodeEntry;
import javax.annotation.Nullable;

public class TvEpisodeEntryAdapter
  extends BaseEntryAdapter
{
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final Sidekick.TvEpisodeEntry mTvEpisodeEntry;
  
  TvEpisodeEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    this.mTvEpisodeEntry = paramEntry.getTvEpisodeEntry();
  }
  
  @Nullable
  public CharSequence getEpisodeInfo()
  {
    if (this.mTvEpisodeEntry.hasEpisodeInfo()) {
      return this.mTvEpisodeEntry.getEpisodeInfo();
    }
    return null;
  }
  
  @Nullable
  public CharSequence getFormattedShowtimeAndStation()
  {
    if (this.mTvEpisodeEntry.hasFormattedShowtimeAndStation()) {
      return this.mTvEpisodeEntry.getFormattedShowtimeAndStation();
    }
    return null;
  }
  
  @Nullable
  public Uri getPhotoUri(Context paramContext)
  {
    if (this.mTvEpisodeEntry.hasImage()) {
      return this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, this.mTvEpisodeEntry.getImage(), 2131689806, 2131689794);
    }
    return null;
  }
  
  public CharSequence getTitle()
  {
    return this.mTvEpisodeEntry.getTitle();
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968896, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(getTitle());
    if (this.mTvEpisodeEntry.hasImage()) {
      this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localView.findViewById(2131297165), this.mTvEpisodeEntry.getImage(), 2131689761, 2131689762);
    }
    CharSequence localCharSequence1 = getFormattedShowtimeAndStation();
    if (!TextUtils.isEmpty(localCharSequence1))
    {
      TextView localTextView3 = (TextView)localView.findViewById(2131297167);
      localTextView3.setText(localCharSequence1);
      localTextView3.setVisibility(0);
    }
    CharSequence localCharSequence2 = getReminderFormattedEventDate();
    CharSequence localCharSequence3 = getEpisodeInfo();
    if (!TextUtils.isEmpty(localCharSequence2))
    {
      localTextView2 = (TextView)localView.findViewById(2131296321);
      localTextView2.setText(localCharSequence2);
      localTextView2.setVisibility(0);
    }
    while (TextUtils.isEmpty(localCharSequence3))
    {
      TextView localTextView2;
      return localView;
    }
    TextView localTextView1 = (TextView)localView.findViewById(2131297168);
    localTextView1.setText(localCharSequence3);
    localTextView1.setVisibility(0);
    return localView;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mTvEpisodeEntry.hasTitle()) {
      paramPredictiveCardContainer.startWebSearch(this.mTvEpisodeEntry.getTitle() + " " + paramContext.getString(2131362782), null);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.TvEpisodeEntryAdapter
 * JD-Core Version:    0.7.0.1
 */