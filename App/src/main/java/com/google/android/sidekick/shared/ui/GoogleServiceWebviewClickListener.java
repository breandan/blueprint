package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.GoogleServiceWebviewUtil;
import com.google.android.sidekick.shared.util.Tag;
import com.google.geo.sidekick.Sidekick.Entry;
import javax.annotation.Nullable;

public class GoogleServiceWebviewClickListener
  extends EntryClickListener
{
  private static final String TAG = Tag.getTag(GoogleServiceWebviewClickListener.class);
  private final PredictiveCardContainer mCardContainer;
  private final Context mContext;
  private final boolean mEnableJavascript;
  @Nullable
  private final EntryCardViewAdapter mEntryAdapter;
  @Nullable
  private final Sidekick.Entry mRecordActionEntry;
  @Nullable
  private final String mService;
  private final String mTargetUrl;
  private final String mTitle;
  @Nullable
  private final String[] mWebviewUrlPrefixes;
  
  public GoogleServiceWebviewClickListener(Context paramContext, String paramString1, String paramString2, boolean paramBoolean, @Nullable EntryCardViewAdapter paramEntryCardViewAdapter, int paramInt, @Nullable String paramString3, @Nullable String[] paramArrayOfString, @Nullable Sidekick.Entry paramEntry, PredictiveCardContainer paramPredictiveCardContainer)
  {
    super(paramPredictiveCardContainer, paramEntryCardViewAdapter.getEntry(), paramInt);
    this.mContext = paramContext;
    this.mTargetUrl = paramString1;
    this.mTitle = paramString2;
    this.mEnableJavascript = paramBoolean;
    this.mEntryAdapter = paramEntryCardViewAdapter;
    this.mService = paramString3;
    this.mWebviewUrlPrefixes = paramArrayOfString;
    this.mCardContainer = paramPredictiveCardContainer;
    this.mRecordActionEntry = paramEntry;
  }
  
  public void onEntryClick(View paramView)
  {
    Intent localIntent = GoogleServiceWebviewUtil.createIntent(Uri.parse(this.mTargetUrl));
    localIntent.putExtra("webview_service", this.mService).putExtra("webview_title", this.mTitle).putExtra("enable_javascript", this.mEnableJavascript).putExtra("webview_url_prefixes", this.mWebviewUrlPrefixes);
    this.mContext.startActivity(localIntent);
    if (this.mRecordActionEntry != null) {
      this.mCardContainer.logAction(this.mRecordActionEntry, 20, null);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.GoogleServiceWebviewClickListener
 * JD-Core Version:    0.7.0.1
 */