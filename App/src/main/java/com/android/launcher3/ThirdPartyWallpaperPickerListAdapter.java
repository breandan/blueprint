package com.android.launcher3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ThirdPartyWallpaperPickerListAdapter
  extends BaseAdapter
  implements ListAdapter
{
  private final int mIconSize;
  private final LayoutInflater mInflater;
  private final PackageManager mPackageManager;
  private List<ThirdPartyWallpaperTile> mThirdPartyWallpaperPickers = new ArrayList();
  
  public ThirdPartyWallpaperPickerListAdapter(Context paramContext)
  {
    this.mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    this.mPackageManager = paramContext.getPackageManager();
    this.mIconSize = paramContext.getResources().getDimensionPixelSize(2131689514);
    PackageManager localPackageManager = this.mPackageManager;
    List localList1 = localPackageManager.queryIntentActivities(new Intent("android.intent.action.SET_WALLPAPER"), 0);
    Intent localIntent = new Intent("android.intent.action.GET_CONTENT");
    localIntent.setType("image/*");
    List localList2 = localPackageManager.queryIntentActivities(localIntent, 0);
    ComponentName[] arrayOfComponentName = new ComponentName[localList2.size()];
    for (int i = 0; i < localList2.size(); i++)
    {
      ActivityInfo localActivityInfo = ((ResolveInfo)localList2.get(i)).activityInfo;
      arrayOfComponentName[i] = new ComponentName(localActivityInfo.packageName, localActivityInfo.name);
    }
    Iterator localIterator1 = localList1.iterator();
    while (localIterator1.hasNext())
    {
      ResolveInfo localResolveInfo = (ResolveInfo)localIterator1.next();
      String str = new ComponentName(localResolveInfo.activityInfo.packageName, localResolveInfo.activityInfo.name).getPackageName();
      if ((!str.equals(paramContext.getPackageName())) && (!str.equals("com.android.launcher")) && (!str.equals("com.android.wallpaper.livepicker")))
      {
        Iterator localIterator2 = localList2.iterator();
        for (;;)
        {
          if (localIterator2.hasNext()) {
            if (str.equals(((ResolveInfo)localIterator2.next()).activityInfo.packageName)) {
              break;
            }
          }
        }
        List localList3 = this.mThirdPartyWallpaperPickers;
        ThirdPartyWallpaperTile localThirdPartyWallpaperTile = new ThirdPartyWallpaperTile(localResolveInfo);
        localList3.add(localThirdPartyWallpaperTile);
      }
    }
  }
  
  public int getCount()
  {
    return this.mThirdPartyWallpaperPickers.size();
  }
  
  public ThirdPartyWallpaperTile getItem(int paramInt)
  {
    return (ThirdPartyWallpaperTile)this.mThirdPartyWallpaperPickers.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {}
    for (View localView = this.mInflater.inflate(2130968922, paramViewGroup, false);; localView = paramView)
    {
      WallpaperPickerActivity.setWallpaperItemPaddingToZero((FrameLayout)localView);
      ResolveInfo localResolveInfo = ((ThirdPartyWallpaperTile)this.mThirdPartyWallpaperPickers.get(paramInt)).mResolveInfo;
      TextView localTextView = (TextView)localView.findViewById(2131297229);
      localTextView.setText(localResolveInfo.loadLabel(this.mPackageManager));
      Drawable localDrawable = localResolveInfo.loadIcon(this.mPackageManager);
      localDrawable.setBounds(new Rect(0, 0, this.mIconSize, this.mIconSize));
      localTextView.setCompoundDrawables(null, localDrawable, null, null);
      return localView;
    }
  }
  
  public static class ThirdPartyWallpaperTile
    extends WallpaperPickerActivity.WallpaperTileInfo
  {
    private ResolveInfo mResolveInfo;
    
    public ThirdPartyWallpaperTile(ResolveInfo paramResolveInfo)
    {
      this.mResolveInfo = paramResolveInfo;
    }
    
    public void onClick(WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      ComponentName localComponentName = new ComponentName(this.mResolveInfo.activityInfo.packageName, this.mResolveInfo.activityInfo.name);
      Intent localIntent = new Intent("android.intent.action.SET_WALLPAPER");
      localIntent.setComponent(localComponentName);
      Utilities.startActivityForResultSafely(paramWallpaperPickerActivity, localIntent, 6);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.ThirdPartyWallpaperPickerListAdapter
 * JD-Core Version:    0.7.0.1
 */