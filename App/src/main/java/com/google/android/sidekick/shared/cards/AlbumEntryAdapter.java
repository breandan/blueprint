package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.AlbumEntry;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class AlbumEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.AlbumEntry mAlbumEntry;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  
  AlbumEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    this.mAlbumEntry = paramEntry.getAlbumEntry();
  }
  
  @Nullable
  public CharSequence getArtistName()
  {
    if (this.mAlbumEntry.hasArtist()) {
      return this.mAlbumEntry.getArtist();
    }
    return null;
  }
  
  @Nullable
  public CharSequence getAvailability()
  {
    if (this.mAlbumEntry.hasAvailability()) {
      return this.mAlbumEntry.getAvailability();
    }
    return null;
  }
  
  @Nullable
  public Uri getPhotoUri(Context paramContext)
  {
    if (this.mAlbumEntry.hasImage()) {
      return this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, this.mAlbumEntry.getImage(), 2131689806, 2131689794);
    }
    return null;
  }
  
  public CharSequence getTitle()
  {
    return this.mAlbumEntry.getTitle();
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968592, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(getTitle());
    if (this.mAlbumEntry.hasImage()) {
      this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localView.findViewById(2131296317), this.mAlbumEntry.getImage(), 2131689759, 2131689760);
    }
    CharSequence localCharSequence1 = getArtistName();
    if (!TextUtils.isEmpty(localCharSequence1))
    {
      TextView localTextView3 = (TextView)localView.findViewById(2131296319);
      localTextView3.setText(localCharSequence1);
      localTextView3.setVisibility(0);
    }
    CharSequence localCharSequence2 = getReminderFormattedEventDate();
    CharSequence localCharSequence3 = getAvailability();
    if (!TextUtils.isEmpty(localCharSequence2))
    {
      TextView localTextView2 = (TextView)localView.findViewById(2131296321);
      localTextView2.setText(localCharSequence2);
      localTextView2.setVisibility(0);
    }
    for (;;)
    {
      if ((this.mAlbumEntry.hasPlayStoreViewAction()) && (this.mAlbumEntry.getPlayStoreViewAction().hasUri()))
      {
        Button localButton = (Button)localView.findViewById(2131296323);
        localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 38)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            AlbumEntryAdapter.this.openUrl(paramContext, AlbumEntryAdapter.this.mAlbumEntry.getPlayStoreViewAction().getUri());
          }
        });
        localButton.setVisibility(0);
        localView.findViewById(2131296322).setVisibility(0);
      }
      return localView;
      if (!TextUtils.isEmpty(localCharSequence3))
      {
        TextView localTextView1 = (TextView)localView.findViewById(2131296320);
        localTextView1.setText(localCharSequence3);
        localTextView1.setVisibility(0);
      }
    }
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    ArrayList localArrayList = Lists.newArrayListWithExpectedSize(2);
    if (this.mAlbumEntry.hasTitle()) {
      localArrayList.add(this.mAlbumEntry.getTitle());
    }
    if (this.mAlbumEntry.hasArtist()) {
      localArrayList.add(this.mAlbumEntry.getArtist());
    }
    paramPredictiveCardContainer.startWebSearch(TextUtils.join(" ", localArrayList), null);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.AlbumEntryAdapter
 * JD-Core Version:    0.7.0.1
 */