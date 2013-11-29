package com.google.android.velvet.gallery;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import com.android.ex.photo.PhotoViewActivity;
import com.android.ex.photo.PhotoViewCallbacks.CursorChangedListener;
import com.android.ex.photo.loaders.PhotoBitmapLoaderInterface.BitmapResult;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.SearchControllerCache;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Preconditions;

public class VelvetPhotoViewActivity
  extends PhotoViewActivity
  implements PhotoViewCallbacks.CursorChangedListener
{
  static final String EXTRA_NEEDS_SEARCH_CONTROLLER = "needsSearchController";
  static final String EXTRA_SEARCH_CONTROLLER_TOKEN = "searchControllerToken";
  private static final String[] PHOTO_PROJECTION = { "uri", "_display_name", "contentUri", "thumbnailUri", "contentType", "loadingIndicator", "domain", "width", "height", "source", "id" };
  private final View.OnClickListener mAttributionClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      Intent localIntent = new Intent("android.intent.action.VIEW");
      localIntent.setFlags(524288);
      localIntent.setData(Uri.parse(VelvetPhotoViewActivity.this.mCurrentSourceUrl));
      try
      {
        VelvetPhotoViewActivity.this.startActivity(localIntent);
        return;
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        Log.w("Velvet.VelvetPhotoViewActivity", "No activity found for " + VelvetPhotoViewActivity.this.mCurrentSourceUrl);
      }
    }
  };
  private String mCurrentDimensionsText = "";
  private String mCurrentDomainText = "";
  private String mCurrentImageUri = "";
  private String mCurrentSourceUrl = "";
  private String mCurrentTitle = "";
  private boolean mCursorChanged;
  private boolean mDownloadsEnabled;
  private boolean mFetchMore = true;
  private String mFocusId;
  private ImageMetadataController mImageMetadataController;
  private SearchControllerCache mSearchControllerCache;
  private int mSearchControllerToken;
  private ShareActionProvider mShareActionProvider;
  private Intent mShareIntent;
  
  public static Intent createPhotoViewIntent(String paramString1, String paramString2, int paramInt)
  {
    Intent localIntent = getPartialIntent(paramString1, paramInt);
    localIntent.putExtra("selectedId", paramString2);
    return localIntent;
  }
  
  private static Intent getPartialIntent(String paramString, int paramInt)
  {
    Intent localIntent = new Intent();
    localIntent.setComponent(new ComponentName(paramString, VelvetPhotoViewActivity.class.getName()));
    localIntent.setAction("android.intent.action.VIEW");
    localIntent.setFlags(524288);
    localIntent.putExtra("photos_uri", "content://com.google.android.velvet.gallery.ImageProvider/images");
    localIntent.putExtra("projection", PHOTO_PROJECTION);
    localIntent.putExtra("max_scale", 1000.0F);
    localIntent.putExtra("needsSearchController", true);
    localIntent.putExtra("searchControllerToken", paramInt);
    return localIntent;
  }
  
  private void hideDetails(boolean paramBoolean)
  {
    final View localView = findViewById(2131296648);
    if (localView == null) {
      return;
    }
    if (paramBoolean)
    {
      localView.setVisibility(0);
      localView.animate().translationY(localView.getHeight()).withEndAction(new Runnable()
      {
        public void run()
        {
          localView.setVisibility(8);
        }
      });
      return;
    }
    localView.setTranslationY(0.0F);
    localView.setVisibility(8);
  }
  
  private void setPhotoIndexById(Cursor paramCursor, String paramString)
  {
    int i = paramCursor.getColumnIndex("id");
    paramCursor.moveToPosition(-1);
    while (paramCursor.moveToNext()) {
      if (paramCursor.getString(i).equals(paramString)) {
        setPhotoIndex(paramCursor.getPosition());
      }
    }
  }
  
  private void setShareIntent(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("text/plain");
    localIntent.putExtra("android.intent.extra.TEXT", paramString);
    if (this.mShareActionProvider == null)
    {
      this.mShareIntent = localIntent;
      return;
    }
    this.mShareActionProvider.setShareIntent(localIntent);
  }
  
  private void showDetails(boolean paramBoolean)
  {
    View localView = findViewById(2131296648);
    if (localView == null) {
      return;
    }
    if (paramBoolean)
    {
      localView.setVisibility(0);
      localView.animate().translationY(0.0F);
      return;
    }
    localView.setTranslationY(0.0F);
    localView.setVisibility(0);
  }
  
  private void startDownload(String paramString)
  {
    DownloadManager localDownloadManager = (DownloadManager)getSystemService("download");
    Uri localUri = Uri.parse(paramString);
    DownloadManager.Request localRequest = new DownloadManager.Request(localUri);
    if (!TextUtils.isEmpty(this.mCurrentTitle)) {
      localRequest.setTitle(this.mCurrentTitle);
    }
    localRequest.setDescription(this.mCurrentDomainText);
    String str = localUri.getLastPathSegment();
    if (str == null) {
      str = getResources().getString(2131363286);
    }
    localRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, str);
    localRequest.allowScanningByMediaScanner();
    localRequest.setNotificationVisibility(1);
    localDownloadManager.enqueue(localRequest);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    requestWindowFeature(5);
    Bundle localBundle = getIntent().getExtras();
    if (localBundle.containsKey("selectedId")) {
      this.mFocusId = localBundle.getString("selectedId");
    }
    if (localBundle.containsKey("fetchMore")) {
      this.mFetchMore = localBundle.getBoolean("fetchMore");
    }
    boolean bool;
    label105:
    View localView;
    if (localBundle.getBoolean("needsSearchController"))
    {
      this.mSearchControllerToken = localBundle.getInt("searchControllerToken", -1);
      if (this.mSearchControllerToken != -1)
      {
        bool = true;
        Preconditions.checkState(bool);
        this.mSearchControllerCache = VelvetServices.get().getCoreServices().getSearchControllerCache();
        this.mDownloadsEnabled = VelvetServices.get().getCoreServices().getGsaConfigFlags().areImageDownloadsEnabled();
        super.onCreate(paramBundle);
        ViewGroup localViewGroup = (ViewGroup)findViewById(2131296845);
        getLayoutInflater().inflate(2130968699, localViewGroup);
        if (paramBundle != null)
        {
          this.mCursorChanged = paramBundle.getBoolean("seenFocus", true);
          this.mCurrentSourceUrl = paramBundle.getString("detailLink");
          this.mCurrentDomainText = paramBundle.getString("detailDomain");
          this.mCurrentDimensionsText = paramBundle.getString("detailDimensions");
          this.mCurrentTitle = paramBundle.getString("title");
          this.mCurrentImageUri = paramBundle.getString("fullSizeUrl");
          localView = findViewById(2131296648);
          if (!isFullScreen()) {
            break label297;
          }
          localView.setTranslationY(localView.getHeight());
        }
      }
    }
    for (;;)
    {
      this.mSearchControllerToken = -1;
      if (this.mFetchMore) {
        this.mImageMetadataController = VelvetServices.get().getCoreServices().getImageMetadataController();
      }
      addCursorListener(this);
      return;
      bool = false;
      break;
      this.mSearchControllerToken = -1;
      break label105;
      label297:
      localView.setTranslationY(0.0F);
    }
  }
  
  public Loader<PhotoBitmapLoaderInterface.BitmapResult> onCreateBitmapLoader(int paramInt, Bundle paramBundle, String paramString)
  {
    return new VelvetPhotoBitmapLoader(this, paramString);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131886084, paramMenu);
    this.mShareActionProvider = ((ShareActionProvider)paramMenu.findItem(2131297283).getActionProvider());
    if (this.mShareIntent != null)
    {
      this.mShareActionProvider.setShareIntent(this.mShareIntent);
      this.mShareIntent = null;
    }
    this.mShareActionProvider = ((ShareActionProvider)paramMenu.findItem(2131297283).getActionProvider());
    return true;
  }
  
  public void onCursorChanged(Cursor paramCursor)
  {
    if (!this.mCursorChanged)
    {
      this.mCursorChanged = true;
      setPhotoIndexById(paramCursor, this.mFocusId);
    }
  }
  
  protected void onDestroy()
  {
    if ((!isChangingConfigurations()) && (this.mSearchControllerToken != -1)) {
      this.mSearchControllerCache.releaseToken(this.mSearchControllerToken);
    }
    super.onDestroy();
  }
  
  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    Bundle localBundle = paramIntent.getExtras();
    if (localBundle.containsKey("photo_index")) {
      setPhotoIndex(localBundle.getInt("photo_index"));
    }
    if (localBundle.containsKey("selectedId"))
    {
      String str = localBundle.getString("selectedId");
      Cursor localCursor = getCursor();
      if (localCursor != null) {
        setPhotoIndexById(localCursor, str);
      }
    }
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default: 
      return super.onOptionsItemSelected(paramMenuItem);
    }
    startDownload(this.mCurrentImageUri);
    return true;
  }
  
  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    super.onPrepareOptionsMenu(paramMenu);
    boolean bool1 = TextUtils.isEmpty(this.mCurrentImageUri);
    if (this.mDownloadsEnabled)
    {
      MenuItem localMenuItem = paramMenu.findItem(2131297284);
      if (!bool1) {}
      for (boolean bool2 = true;; bool2 = false)
      {
        localMenuItem.setEnabled(bool2);
        return true;
      }
    }
    paramMenu.removeItem(2131297284);
    return true;
  }
  
  protected void onResume()
  {
    super.onResume();
    View localView = findViewById(2131296648);
    TextView localTextView1 = (TextView)findViewById(2131296649);
    TextView localTextView2 = (TextView)findViewById(2131296651);
    if ((localView != null) && (localTextView1 != null) && (localTextView2 != null))
    {
      localView.setOnClickListener(this.mAttributionClickListener);
      localTextView1.setText(this.mCurrentDomainText);
      localTextView2.setText(this.mCurrentDimensionsText);
    }
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("seenFocus", this.mCursorChanged);
    paramBundle.putString("detailLink", this.mCurrentSourceUrl);
    paramBundle.putString("detailDomain", this.mCurrentDomainText);
    paramBundle.putString("detailDimensions", this.mCurrentDimensionsText);
    paramBundle.putString("fullSizeUrl", this.mCurrentImageUri);
    paramBundle.putString("title", this.mCurrentTitle);
  }
  
  protected void setLightsOutMode(boolean paramBoolean)
  {
    super.setLightsOutMode(paramBoolean);
    if (paramBoolean)
    {
      hideDetails(true);
      return;
    }
    showDetails(true);
  }
  
  protected void updateActionBar()
  {
    super.updateActionBar();
    Cursor localCursor = getCursorAtProperPosition();
    View localView = findViewById(2131296648);
    TextView localTextView1 = (TextView)findViewById(2131296649);
    TextView localTextView2 = (TextView)findViewById(2131296651);
    this.mCurrentDomainText = localCursor.getString(localCursor.getColumnIndex("domain"));
    this.mCurrentSourceUrl = localCursor.getString(localCursor.getColumnIndex("source"));
    this.mCurrentImageUri = localCursor.getString(localCursor.getColumnIndex("contentUri"));
    this.mCurrentTitle = localCursor.getString(localCursor.getColumnIndex("_display_name"));
    int i = localCursor.getInt(localCursor.getColumnIndex("width"));
    int j = localCursor.getInt(localCursor.getColumnIndex("height"));
    getActionBar().setTitle(this.mCurrentTitle);
    getActionBar().setSubtitle(null);
    localTextView1.setText(this.mCurrentDomainText);
    this.mCurrentDimensionsText = "";
    if ((i > 0) && (j > 0))
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(i);
      arrayOfObject[1] = Integer.valueOf(j);
      this.mCurrentDimensionsText = getString(2131363282, arrayOfObject);
    }
    if (TextUtils.isEmpty(this.mCurrentDimensionsText)) {
      localTextView2.setVisibility(8);
    }
    for (;;)
    {
      localView.setOnClickListener(this.mAttributionClickListener);
      setShareIntent(this.mCurrentImageUri);
      if (this.mFetchMore)
      {
        int k = localCursor.getCount();
        if ((k > 0) && (k - localCursor.getPosition() < 8)) {
          this.mImageMetadataController.fetchMoreImages();
        }
      }
      return;
      localTextView2.setText(this.mCurrentDimensionsText);
      localTextView2.setVisibility(0);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.gallery.VelvetPhotoViewActivity
 * JD-Core Version:    0.7.0.1
 */