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
import com.google.geo.sidekick.Sidekick.BookEntry;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class BookEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.BookEntry mBookEntry;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  
  BookEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    this.mBookEntry = paramEntry.getBookEntry();
  }
  
  @Nullable
  public CharSequence getAuthorName()
  {
    if (this.mBookEntry.getAuthorCount() > 0) {
      return this.mBookEntry.getAuthor(0);
    }
    return null;
  }
  
  @Nullable
  public CharSequence getAvailability()
  {
    if (this.mBookEntry.hasAvailability()) {
      return this.mBookEntry.getAvailability();
    }
    return null;
  }
  
  @Nullable
  public Uri getPhotoUri(Context paramContext)
  {
    if (this.mBookEntry.hasImage()) {
      return this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, this.mBookEntry.getImage(), 2131689806, 2131689794);
    }
    return null;
  }
  
  public CharSequence getTitle()
  {
    return this.mBookEntry.getTitle();
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968606, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(getTitle());
    if (this.mBookEntry.hasImage()) {
      this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localView.findViewById(2131296377), this.mBookEntry.getImage(), 2131689757, 2131689758);
    }
    CharSequence localCharSequence1 = getAuthorName();
    if (!TextUtils.isEmpty(localCharSequence1))
    {
      TextView localTextView3 = (TextView)localView.findViewById(2131296379);
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
      if ((this.mBookEntry.hasPlayStoreViewAction()) && (this.mBookEntry.getPlayStoreViewAction().hasUri()))
      {
        Button localButton = (Button)localView.findViewById(2131296323);
        localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 45)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            BookEntryAdapter.this.openUrl(paramContext, BookEntryAdapter.this.mBookEntry.getPlayStoreViewAction().getUri());
          }
        });
        localButton.setVisibility(0);
        localView.findViewById(2131296381).setVisibility(0);
      }
      return localView;
      if (!TextUtils.isEmpty(localCharSequence3))
      {
        TextView localTextView1 = (TextView)localView.findViewById(2131296380);
        localTextView1.setText(localCharSequence3);
        localTextView1.setVisibility(0);
      }
    }
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    ArrayList localArrayList = Lists.newArrayListWithExpectedSize(2);
    if (this.mBookEntry.hasTitle()) {
      localArrayList.add(this.mBookEntry.getTitle());
    }
    Iterator localIterator = this.mBookEntry.getAuthorList().iterator();
    while (localIterator.hasNext()) {
      localArrayList.add((String)localIterator.next());
    }
    paramPredictiveCardContainer.startWebSearch(TextUtils.join(" ", localArrayList), null);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.BookEntryAdapter
 * JD-Core Version:    0.7.0.1
 */