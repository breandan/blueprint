package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.PublicAlertsResponseUtil;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.PublicAlertEntry;
import java.util.concurrent.TimeUnit;

public class PublicAlertEntryAdapter
  extends BaseEntryAdapter
{
  private final Clock mClock;
  private Sidekick.PublicAlertEntry mPublicAlertEntry;
  
  public PublicAlertEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mPublicAlertEntry = paramEntry.getPublicAlertEntry();
    this.mClock = paramClock;
  }
  
  private String getTimePublisherText(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (this.mPublicAlertEntry.hasIssuedTimeSec())
    {
      long l = TimeUnit.MILLISECONDS.toSeconds(this.mClock.currentTimeMillis());
      localStringBuilder.append(TimeUtilities.getRelativeElapsedString(paramContext, TimeUnit.SECONDS.toMillis(l - this.mPublicAlertEntry.getIssuedTimeSec()), false));
    }
    if (localStringBuilder.length() > 0) {
      localStringBuilder.append(" &ndash; ");
    }
    if (this.mPublicAlertEntry.hasPublisher()) {
      localStringBuilder.append(this.mPublicAlertEntry.getPublisher());
    }
    return localStringBuilder.toString();
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968794, paramViewGroup, false);
    TextView localTextView1 = (TextView)localView.findViewById(2131296451);
    localTextView1.setText(Html.fromHtml(this.mPublicAlertEntry.getTitle()));
    localTextView1.setTextColor(paramContext.getResources().getColor(2131230818));
    localTextView1.setVisibility(0);
    if (this.mPublicAlertEntry.hasLocation())
    {
      TextView localTextView4 = (TextView)localView.findViewById(2131296898);
      localTextView4.setText(Html.fromHtml(this.mPublicAlertEntry.getLocation().getName()));
      localTextView4.setVisibility(0);
    }
    if (this.mPublicAlertEntry.hasTimePublisher())
    {
      TextView localTextView3 = (TextView)localView.findViewById(2131296899);
      localTextView3.setText(Html.fromHtml(getTimePublisherText(paramContext)));
      localTextView3.setVisibility(0);
    }
    if (this.mPublicAlertEntry.hasText())
    {
      TextView localTextView2 = (TextView)localView.findViewById(2131296900);
      localTextView2.setText(Html.fromHtml(this.mPublicAlertEntry.getText()));
      localTextView2.setVisibility(0);
    }
    if ((this.mPublicAlertEntry.hasAlertUrl()) && (this.mPublicAlertEntry.hasAlertUrlLabel()))
    {
      Button localButton = (Button)localView.findViewById(2131296618);
      localButton.setVisibility(0);
      localButton.setText(this.mPublicAlertEntry.getAlertUrlLabel());
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 81)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          PublicAlertEntryAdapter.this.openUrl(paramContext, PublicAlertEntryAdapter.this.mPublicAlertEntry.getAlertUrl());
        }
      });
    }
    if (this.mPublicAlertEntry.hasImage())
    {
      String str = ProtoUtils.getPhotoUrl(this.mPublicAlertEntry.getImage());
      if (!TextUtils.isEmpty(str))
      {
        WebImageView localWebImageView = (WebImageView)localView.findViewById(2131296901);
        localWebImageView.setImageUri(PublicAlertsResponseUtil.getStaticMapUrl(Uri.parse(str), localWebImageView), paramPredictiveCardContainer.getImageLoader());
        localWebImageView.setVisibility(0);
      }
      return localView;
    }
    localView.findViewById(2131296902).setVisibility(0);
    return localView;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mPublicAlertEntry.hasAlertUrl()) {
      openUrl(paramContext, this.mPublicAlertEntry.getAlertUrl());
    }
  }
  
  public void replaceEntry(Sidekick.Entry paramEntry)
  {
    super.replaceEntry(paramEntry);
    this.mPublicAlertEntry = paramEntry.getPublicAlertEntry();
  }
  
  public View updateView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, View paramView, Sidekick.Entry paramEntry)
  {
    return getView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.PublicAlertEntryAdapter
 * JD-Core Version:    0.7.0.1
 */