package com.android.launcher3;

import android.app.WallpaperInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public class LiveWallpaperListAdapter
  extends BaseAdapter
  implements ListAdapter
{
  private final LayoutInflater mInflater;
  private final PackageManager mPackageManager;
  private List<LiveWallpaperTile> mWallpapers;
  
  public LiveWallpaperListAdapter(Context paramContext)
  {
    this.mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    this.mPackageManager = paramContext.getPackageManager();
    List localList = this.mPackageManager.queryIntentServices(new Intent("android.service.wallpaper.WallpaperService"), 128);
    this.mWallpapers = new ArrayList();
    new LiveWallpaperEnumerator(paramContext).execute(new List[] { localList });
  }
  
  public int getCount()
  {
    if (this.mWallpapers == null) {
      return 0;
    }
    return this.mWallpapers.size();
  }
  
  public LiveWallpaperTile getItem(int paramInt)
  {
    return (LiveWallpaperTile)this.mWallpapers.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView;
    LiveWallpaperTile localLiveWallpaperTile;
    ImageView localImageView2;
    if (paramView == null)
    {
      localView = this.mInflater.inflate(2130968921, paramViewGroup, false);
      WallpaperPickerActivity.setWallpaperItemPaddingToZero((FrameLayout)localView);
      localLiveWallpaperTile = (LiveWallpaperTile)this.mWallpapers.get(paramInt);
      localLiveWallpaperTile.setView(localView);
      ImageView localImageView1 = (ImageView)localView.findViewById(2131297228);
      localImageView2 = (ImageView)localView.findViewById(2131297230);
      if (localLiveWallpaperTile.mThumbnail == null) {
        break label130;
      }
      localImageView1.setImageDrawable(localLiveWallpaperTile.mThumbnail);
      localImageView2.setVisibility(8);
    }
    for (;;)
    {
      ((TextView)localView.findViewById(2131297229)).setText(localLiveWallpaperTile.mInfo.loadLabel(this.mPackageManager));
      return localView;
      localView = paramView;
      break;
      label130:
      localImageView2.setImageDrawable(localLiveWallpaperTile.mInfo.loadIcon(this.mPackageManager));
      localImageView2.setVisibility(0);
    }
  }
  
  private class LiveWallpaperEnumerator
    extends AsyncTask<List<ResolveInfo>, LiveWallpaperListAdapter.LiveWallpaperTile, Void>
  {
    private Context mContext;
    private int mWallpaperPosition;
    
    public LiveWallpaperEnumerator(Context paramContext)
    {
      this.mContext = paramContext;
      this.mWallpaperPosition = 0;
    }
    
    protected Void doInBackground(List<ResolveInfo>... paramVarArgs)
    {
      final PackageManager localPackageManager = this.mContext.getPackageManager();
      List<ResolveInfo> localList = paramVarArgs[0];
      Collections.sort(localList, new Comparator()
      {
        final Collator mCollator = Collator.getInstance();
        
        public int compare(ResolveInfo paramAnonymousResolveInfo1, ResolveInfo paramAnonymousResolveInfo2)
        {
          return this.mCollator.compare(paramAnonymousResolveInfo1.loadLabel(localPackageManager), paramAnonymousResolveInfo2.loadLabel(localPackageManager));
        }
      });
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localIterator.next();
        try
        {
          WallpaperInfo localWallpaperInfo = new WallpaperInfo(this.mContext, localResolveInfo);
          Drawable localDrawable = localWallpaperInfo.loadThumbnail(localPackageManager);
          Intent localIntent = new Intent("android.service.wallpaper.WallpaperService");
          localIntent.setClassName(localWallpaperInfo.getPackageName(), localWallpaperInfo.getServiceName());
          publishProgress(new LiveWallpaperListAdapter.LiveWallpaperTile[] { new LiveWallpaperListAdapter.LiveWallpaperTile(localDrawable, localWallpaperInfo, localIntent) });
        }
        catch (XmlPullParserException localXmlPullParserException)
        {
          Log.w("LiveWallpaperListAdapter", "Skipping wallpaper " + localResolveInfo.serviceInfo, localXmlPullParserException);
        }
        catch (IOException localIOException)
        {
          Log.w("LiveWallpaperListAdapter", "Skipping wallpaper " + localResolveInfo.serviceInfo, localIOException);
        }
      }
      LiveWallpaperListAdapter.LiveWallpaperTile[] arrayOfLiveWallpaperTile = new LiveWallpaperListAdapter.LiveWallpaperTile[1];
      arrayOfLiveWallpaperTile[0] = ((LiveWallpaperListAdapter.LiveWallpaperTile)null);
      publishProgress(arrayOfLiveWallpaperTile);
      return null;
    }
    
    protected void onProgressUpdate(LiveWallpaperListAdapter.LiveWallpaperTile... paramVarArgs)
    {
      int i = paramVarArgs.length;
      int j = 0;
      LiveWallpaperListAdapter.LiveWallpaperTile localLiveWallpaperTile;
      if (j < i)
      {
        localLiveWallpaperTile = paramVarArgs[j];
        if (localLiveWallpaperTile == null) {
          LiveWallpaperListAdapter.this.notifyDataSetChanged();
        }
      }
      else
      {
        return;
      }
      if (LiveWallpaperListAdapter.LiveWallpaperTile.access$000(localLiveWallpaperTile) != null) {
        LiveWallpaperListAdapter.LiveWallpaperTile.access$000(localLiveWallpaperTile).setDither(true);
      }
      if (this.mWallpaperPosition < LiveWallpaperListAdapter.this.mWallpapers.size()) {
        LiveWallpaperListAdapter.this.mWallpapers.set(this.mWallpaperPosition, localLiveWallpaperTile);
      }
      for (;;)
      {
        this.mWallpaperPosition = (1 + this.mWallpaperPosition);
        j++;
        break;
        LiveWallpaperListAdapter.this.mWallpapers.add(localLiveWallpaperTile);
      }
    }
  }
  
  public static class LiveWallpaperTile
    extends WallpaperPickerActivity.WallpaperTileInfo
  {
    private WallpaperInfo mInfo;
    private Drawable mThumbnail;
    
    public LiveWallpaperTile(Drawable paramDrawable, WallpaperInfo paramWallpaperInfo, Intent paramIntent)
    {
      this.mThumbnail = paramDrawable;
      this.mInfo = paramWallpaperInfo;
    }
    
    public void onClick(WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      Intent localIntent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
      localIntent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", this.mInfo.getComponent());
      paramWallpaperPickerActivity.onLiveWallpaperPickerLaunch();
      Utilities.startActivityForResultSafely(paramWallpaperPickerActivity, localIntent, 7);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LiveWallpaperListAdapter
 * JD-Core Version:    0.7.0.1
 */