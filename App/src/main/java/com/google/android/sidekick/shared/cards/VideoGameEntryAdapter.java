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
import com.google.geo.sidekick.Sidekick.VideoGameEntry;
import javax.annotation.Nullable;

public class VideoGameEntryAdapter
  extends BaseEntryAdapter
{
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final Sidekick.VideoGameEntry mVideoGameEntry;
  
  VideoGameEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    this.mVideoGameEntry = paramEntry.getVideoGameEntry();
  }
  
  @Nullable
  public CharSequence getAvailability()
  {
    if (this.mVideoGameEntry.hasAvailability()) {
      return this.mVideoGameEntry.getAvailability();
    }
    return null;
  }
  
  @Nullable
  public Uri getPhotoUri(Context paramContext)
  {
    if (this.mVideoGameEntry.hasImage()) {
      return this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, this.mVideoGameEntry.getImage(), 2131689806, 2131689794);
    }
    return null;
  }
  
  public CharSequence getTitle()
  {
    return this.mVideoGameEntry.getTitle();
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968909, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(getTitle());
    if (this.mVideoGameEntry.hasImage()) {
      this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localView.findViewById(2131297198), this.mVideoGameEntry.getImage(), 2131689772, 2131689773);
    }
    CharSequence localCharSequence1 = getReminderFormattedEventDate();
    CharSequence localCharSequence2 = getAvailability();
    if (!TextUtils.isEmpty(localCharSequence1))
    {
      localTextView2 = (TextView)localView.findViewById(2131296321);
      localTextView2.setText(localCharSequence1);
      localTextView2.setVisibility(0);
    }
    while (TextUtils.isEmpty(localCharSequence2))
    {
      TextView localTextView2;
      return localView;
    }
    TextView localTextView1 = (TextView)localView.findViewById(2131297200);
    localTextView1.setText(localCharSequence2);
    localTextView1.setVisibility(0);
    return localView;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mVideoGameEntry.hasTitle()) {
      paramPredictiveCardContainer.startWebSearch(this.mVideoGameEntry.getTitle() + " " + paramContext.getString(2131362783), null);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.VideoGameEntryAdapter
 * JD-Core Version:    0.7.0.1
 */