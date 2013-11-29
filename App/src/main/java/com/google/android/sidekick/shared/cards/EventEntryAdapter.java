package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.MapsLauncher;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EventEntry;
import com.google.geo.sidekick.Sidekick.Location;
import javax.annotation.Nullable;

public class EventEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.EventEntry mEventEntry;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  
  EventEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    this.mEventEntry = paramEntry.getEventEntry();
  }
  
  @Nullable
  public Uri getImageUri(Context paramContext)
  {
    if (this.mEventEntry.hasImage()) {
      return this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, this.mEventEntry.getImage(), 2131689806, 2131689794);
    }
    return null;
  }
  
  @Nullable
  public CharSequence getOnCardJustification()
  {
    return this.mEventEntry.getOnCardJustification();
  }
  
  public CharSequence getStartDate(Context paramContext)
  {
    long l = 1000L * this.mEventEntry.getStartTimeSeconds();
    return DateUtils.formatDateRange(paramContext, l, l, 18);
  }
  
  public CharSequence getTitle()
  {
    return this.mEventEntry.getTitle();
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968671, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(getTitle());
    if (this.mEventEntry.hasImage()) {
      this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localView.findViewById(2131296551), this.mEventEntry.getImage(), 2131689782, 2131689783);
    }
    CharSequence localCharSequence1 = getReminderFormattedEventDate();
    if (!TextUtils.isEmpty(localCharSequence1))
    {
      TextView localTextView2 = (TextView)localView.findViewById(2131296321);
      localTextView2.setText(localCharSequence1);
      localTextView2.setVisibility(0);
    }
    for (;;)
    {
      if (this.mEventEntry.hasLocation())
      {
        Button localButton = (Button)localView.findViewById(2131296555);
        localButton.setText(this.mEventEntry.getLocation().getName());
        localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 56)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            MapsLauncher.start(paramContext, EventEntryAdapter.this.getActivityHelper(), EventEntryAdapter.this.mEventEntry.getLocation());
          }
        });
        localButton.setVisibility(0);
      }
      return localView;
      ((TextView)localView.findViewById(2131296553)).setText(getStartDate(paramContext));
      CharSequence localCharSequence2 = getOnCardJustification();
      if (!TextUtils.isEmpty(localCharSequence2))
      {
        TextView localTextView1 = (TextView)localView.findViewById(2131296554);
        localTextView1.setText(localCharSequence2);
        localTextView1.setVisibility(0);
      }
    }
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mEventEntry.hasViewAction()) && (this.mEventEntry.getViewAction().hasUri())) {
      openUrl(paramContext, this.mEventEntry.getViewAction().getUri());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.EventEntryAdapter
 * JD-Core Version:    0.7.0.1
 */