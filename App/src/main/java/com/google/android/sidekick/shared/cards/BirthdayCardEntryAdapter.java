package com.google.android.sidekick.shared.cards;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.QuickContact;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.search.shared.ui.CrossfadingWebImageView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.IntentUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.android.sidekick.shared.util.GooglePlusIntents;
import com.google.geo.sidekick.Sidekick.BirthdayCardEntry;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Photo;
import java.text.NumberFormat;
import javax.annotation.Nullable;

public class BirthdayCardEntryAdapter
  extends BaseEntryAdapter
{
  private static final String[] CONTACT_PROJECTION = { "lookup" };
  private final Sidekick.BirthdayCardEntry mBirthdayCardEntry;
  private boolean mCardClickedBeforeReady;
  private View mCardView;
  private Uri mContactLookupUri;
  private final FifeImageUrlUtil mFifeImageUrlUtil;
  private final IntentUtils mIntentUtils;
  private boolean mIsAsyncTaskFinished;
  
  public BirthdayCardEntryAdapter(Sidekick.Entry paramEntry, IntentUtils paramIntentUtils, FifeImageUrlUtil paramFifeImageUrlUtil, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mBirthdayCardEntry = paramEntry.getBirthdayCardEntry();
    this.mIntentUtils = paramIntentUtils;
    this.mFifeImageUrlUtil = paramFifeImageUrlUtil;
  }
  
  @Nullable
  public Uri getPhotoUri(Context paramContext)
  {
    if (this.mBirthdayCardEntry.hasPhoto())
    {
      Sidekick.Photo localPhoto = this.mBirthdayCardEntry.getPhoto();
      if (localPhoto.getUrlType() == 2)
      {
        int i = paramContext.getResources().getDimensionPixelSize(2131689806);
        int j = paramContext.getResources().getDimensionPixelSize(2131689794);
        return this.mFifeImageUrlUtil.setImageUrlSmartCrop(i, j, localPhoto.getUrl());
      }
      if (localPhoto.getUrlType() == 0)
      {
        String str = localPhoto.getUrl();
        if (str.startsWith("//")) {
          str = "https:" + str;
        }
        return Uri.parse(str);
      }
    }
    return null;
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if ((this.mBirthdayCardEntry.hasOwnBirthday()) && (this.mBirthdayCardEntry.getOwnBirthday()))
    {
      this.mCardView = paramLayoutInflater.inflate(2130968905, paramViewGroup, false);
      ((TextView)this.mCardView.findViewById(2131296451)).setText(2131362661);
      if (this.mBirthdayCardEntry.hasOwnBirthdaySeconds())
      {
        TextView localTextView = (TextView)this.mCardView.findViewById(2131297190);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = NumberFormat.getInstance().format(this.mBirthdayCardEntry.getOwnBirthdaySeconds());
        localTextView.setText(paramContext.getString(2131362662, arrayOfObject));
        localTextView.setVisibility(0);
      }
      if (this.mBirthdayCardEntry.hasBirthdayDoodle())
      {
        WebImageView localWebImageView = (WebImageView)this.mCardView.findViewById(2131297189);
        localWebImageView.setImageUrl(this.mBirthdayCardEntry.getBirthdayDoodle().getUrl(), paramPredictiveCardContainer.getImageLoader());
        localWebImageView.setVisibility(0);
      }
      return this.mCardView;
    }
    this.mCardView = paramLayoutInflater.inflate(2130968698, paramViewGroup, false);
    ((TextView)this.mCardView.findViewById(2131296646)).setText(this.mBirthdayCardEntry.getName());
    Uri localUri = getPhotoUri(paramContext);
    if (localUri != null)
    {
      CrossfadingWebImageView localCrossfadingWebImageView = (CrossfadingWebImageView)this.mCardView.findViewById(2131296383);
      localCrossfadingWebImageView.setImageUri(localUri, paramPredictiveCardContainer.getImageLoader());
      localCrossfadingWebImageView.setVisibility(0);
    }
    if (this.mBirthdayCardEntry.hasPhone())
    {
      FetchLookupId localFetchLookupId = new FetchLookupId(paramContext);
      Sidekick.BirthdayCardEntry[] arrayOfBirthdayCardEntry = new Sidekick.BirthdayCardEntry[1];
      arrayOfBirthdayCardEntry[0] = this.mBirthdayCardEntry;
      localFetchLookupId.execute(arrayOfBirthdayCardEntry);
    }
    if (GooglePlusIntents.canSendBirthdayIntent(paramContext, this.mIntentUtils))
    {
      this.mCardView.findViewById(2131296386).setVisibility(0);
      Button localButton = (Button)this.mCardView.findViewById(2131296647);
      localButton.setVisibility(0);
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 43)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          BirthdayCardEntryAdapter.this.getActivityHelper().safeStartActivityWithMessage(paramContext, GooglePlusIntents.getBirthdayIntent(BirthdayCardEntryAdapter.this.mBirthdayCardEntry.getSenderFocusId(), BirthdayCardEntryAdapter.this.mBirthdayCardEntry.getRecipientFocusId(), BirthdayCardEntryAdapter.this.mBirthdayCardEntry.getName(), paramContext.getString(2131362661)), 2131362666);
        }
      });
    }
    return this.mCardView;
  }
  
  @Nullable
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296645);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (!this.mIsAsyncTaskFinished) {
      this.mCardClickedBeforeReady = true;
    }
    do
    {
      return;
      if (this.mContactLookupUri != null)
      {
        ContactsContract.QuickContact.showQuickContact(paramContext, this.mCardView, this.mContactLookupUri, 2, new String[0]);
        return;
      }
    } while (!this.mBirthdayCardEntry.hasPlusUrl());
    openUrl(paramContext, this.mBirthdayCardEntry.getPlusUrl());
  }
  
  private class FetchLookupId
    extends AsyncTask<Sidekick.BirthdayCardEntry, Void, String>
  {
    private final Context mContext;
    
    public FetchLookupId(Context paramContext)
    {
      this.mContext = paramContext;
    }
    
    private String fetchLookupId(Sidekick.BirthdayCardEntry paramBirthdayCardEntry)
    {
      ContentResolver localContentResolver = this.mContext.getContentResolver();
      String str1 = paramBirthdayCardEntry.getEmail();
      String str2 = paramBirthdayCardEntry.getPhone();
      Uri localUri;
      if (!TextUtils.isEmpty(str1)) {
        localUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_LOOKUP_URI, Uri.encode(str1));
      }
      for (;;)
      {
        Cursor localCursor = null;
        try
        {
          localCursor = localContentResolver.query(localUri, BirthdayCardEntryAdapter.CONTACT_PROJECTION, null, null, null);
          if ((localCursor != null) && (localCursor.moveToFirst()))
          {
            String str3 = localCursor.getString(localCursor.getColumnIndex("lookup"));
            return str3;
            if (!TextUtils.isEmpty(str2))
            {
              localUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(paramBirthdayCardEntry.getPhone()));
              continue;
            }
            return null;
          }
          return null;
        }
        finally
        {
          if (localCursor != null) {
            localCursor.close();
          }
        }
      }
    }
    
    protected String doInBackground(Sidekick.BirthdayCardEntry... paramVarArgs)
    {
      return fetchLookupId(paramVarArgs[0]);
    }
    
    public void onPostExecute(String paramString)
    {
      if (paramString == null) {}
      do
      {
        return;
        BirthdayCardEntryAdapter.access$102(BirthdayCardEntryAdapter.this, Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, paramString));
        BirthdayCardEntryAdapter.access$202(BirthdayCardEntryAdapter.this, true);
      } while (!BirthdayCardEntryAdapter.this.mCardClickedBeforeReady);
      BirthdayCardEntryAdapter.this.mCardView.performClick();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.BirthdayCardEntryAdapter
 * JD-Core Version:    0.7.0.1
 */