package com.google.android.voicesearch.greco3.languagepack;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.velvet.VelvetServices;
import java.util.ArrayList;

public class InstallActivity
  extends Activity
{
  private TabsAdapter mTabsAdapter;
  private ViewPager mViewPager;
  
  public static Intent getStartIntent(Context paramContext)
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.setClass(paramContext, InstallActivity.class);
    localIntent.setFlags(268435456);
    return localIntent;
  }
  
  private static String makeFragmentName(int paramInt1, int paramInt2)
  {
    return "android:switcher:" + paramInt1 + ":" + paramInt2;
  }
  
  public static void start(Context paramContext)
  {
    paramContext.startActivity(getStartIntent(paramContext));
  }
  
  private static Bundle withType(int paramInt)
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("type", paramInt);
    return localBundle;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968731);
    this.mViewPager = ((ViewPager)findViewById(2131296736));
    ActionBar localActionBar = getActionBar();
    localActionBar.setNavigationMode(2);
    localActionBar.setTitle(2131363582);
    localActionBar.setDisplayOptions(14);
    this.mTabsAdapter = new TabsAdapter(this, this.mViewPager);
    this.mTabsAdapter.addTab(localActionBar.newTab().setText(2131363586), VoiceListFragment.class, withType(2));
    this.mTabsAdapter.addTab(localActionBar.newTab().setText(2131363585), VoiceListFragment.class, withType(1));
    GsaConfigFlags localGsaConfigFlags = VelvetServices.get().getCoreServices().getGsaConfigFlags();
    if ((Feature.LANGUAGE_PACK_AUTO_DOWNLOAD.isEnabled()) && (localGsaConfigFlags.isLangagePackAutoUpdateEnabled())) {
      this.mTabsAdapter.addTab(localActionBar.newTab().setText(2131363587), VoiceAutoUpdateRadioButtonFragment.class, new Bundle());
    }
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getItemId() == 16908332)
    {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  private static abstract class FragmentPagerAdapter
    extends PagerAdapter
  {
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private final FragmentManager mFragmentManager;
    
    public FragmentPagerAdapter(FragmentManager paramFragmentManager)
    {
      this.mFragmentManager = paramFragmentManager;
    }
    
    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      if (this.mCurTransaction == null) {
        this.mCurTransaction = this.mFragmentManager.beginTransaction();
      }
      this.mCurTransaction.detach((Fragment)paramObject);
    }
    
    public void finishUpdate(ViewGroup paramViewGroup)
    {
      if (this.mCurTransaction != null)
      {
        this.mCurTransaction.commitAllowingStateLoss();
        this.mCurTransaction = null;
        this.mFragmentManager.executePendingTransactions();
      }
    }
    
    public abstract Fragment getItem(int paramInt);
    
    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      if (this.mCurTransaction == null) {
        this.mCurTransaction = this.mFragmentManager.beginTransaction();
      }
      String str = InstallActivity.makeFragmentName(paramViewGroup.getId(), paramInt);
      Fragment localFragment = this.mFragmentManager.findFragmentByTag(str);
      if (localFragment != null) {
        this.mCurTransaction.attach(localFragment);
      }
      for (;;)
      {
        if (localFragment != this.mCurrentPrimaryItem)
        {
          localFragment.setMenuVisibility(false);
          localFragment.setUserVisibleHint(false);
        }
        return localFragment;
        localFragment = getItem(paramInt);
        this.mCurTransaction.add(paramViewGroup.getId(), localFragment, InstallActivity.makeFragmentName(paramViewGroup.getId(), paramInt));
      }
    }
    
    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return ((Fragment)paramObject).getView() == paramView;
    }
    
    public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader) {}
    
    public Parcelable saveState()
    {
      return null;
    }
    
    public void setPrimaryItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      Fragment localFragment = (Fragment)paramObject;
      if (localFragment != this.mCurrentPrimaryItem)
      {
        if (this.mCurrentPrimaryItem != null)
        {
          this.mCurrentPrimaryItem.setMenuVisibility(false);
          this.mCurrentPrimaryItem.setUserVisibleHint(false);
        }
        if (localFragment != null)
        {
          localFragment.setMenuVisibility(true);
          localFragment.setUserVisibleHint(true);
        }
        this.mCurrentPrimaryItem = localFragment;
      }
    }
    
    public void startUpdate(ViewGroup paramViewGroup) {}
  }
  
  private static class TabsAdapter
    extends InstallActivity.FragmentPagerAdapter
    implements ActionBar.TabListener, ViewPager.OnPageChangeListener
  {
    private final ActionBar mActionBar;
    private final Context mContext;
    private final ArrayList<TabInfo> mTabs = new ArrayList();
    private final ViewPager mViewPager;
    
    public TabsAdapter(Activity paramActivity, ViewPager paramViewPager)
    {
      super();
      this.mContext = paramActivity;
      this.mActionBar = paramActivity.getActionBar();
      this.mViewPager = paramViewPager;
      this.mViewPager.setAdapter(this);
      this.mViewPager.setOnPageChangeListener(this);
    }
    
    public void addTab(ActionBar.Tab paramTab, Class<?> paramClass, Bundle paramBundle)
    {
      TabInfo localTabInfo = new TabInfo(paramClass, paramBundle);
      paramTab.setTag(localTabInfo);
      paramTab.setTabListener(this);
      this.mTabs.add(localTabInfo);
      this.mActionBar.addTab(paramTab);
      notifyDataSetChanged();
    }
    
    public int getCount()
    {
      return this.mTabs.size();
    }
    
    public Fragment getItem(int paramInt)
    {
      TabInfo localTabInfo = (TabInfo)this.mTabs.get(paramInt);
      return Fragment.instantiate(this.mContext, localTabInfo.clss.getName(), localTabInfo.args);
    }
    
    public void onPageScrollStateChanged(int paramInt) {}
    
    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}
    
    public void onPageSelected(int paramInt)
    {
      this.mActionBar.setSelectedNavigationItem(paramInt);
    }
    
    public void onTabReselected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction) {}
    
    public void onTabSelected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction)
    {
      Object localObject = paramTab.getTag();
      for (int i = 0; i < this.mTabs.size(); i++) {
        if (this.mTabs.get(i) == localObject) {
          this.mViewPager.setCurrentItem(i);
        }
      }
    }
    
    public void onTabUnselected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction) {}
    
    static final class TabInfo
    {
      private final Bundle args;
      private final Class<?> clss;
      
      TabInfo(Class<?> paramClass, Bundle paramBundle)
      {
        this.clss = paramClass;
        this.args = paramBundle;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.InstallActivity
 * JD-Core Version:    0.7.0.1
 */