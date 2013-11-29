package com.google.android.voicesearch.greco3.languagepack;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.speech.embedded.LanguagePackUtils;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.collect.Lists;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executor;

public class VoiceListFragment
  extends ListFragment
{
  private GstaticConfiguration.Configuration mConfiguration;
  private LanguagePackUpdateController mController;
  private final LanguagePackUpdateController.Listener mControllerListener = new LanguagePackUpdateController.Listener()
  {
    public void onDownloadFailed(final GstaticConfiguration.LanguagePack paramAnonymousLanguagePack)
    {
      VoiceListFragment.this.mUiThread.execute(new Runnable()
      {
        public void run()
        {
          VoiceListFragment localVoiceListFragment = VoiceListFragment.this;
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = SpokenLanguageUtils.getDisplayName(VoiceListFragment.this.mConfiguration, paramAnonymousLanguagePack.getBcp47Locale());
          String str = localVoiceListFragment.getString(2131363652, arrayOfObject);
          Toast.makeText(VoiceListFragment.this.getActivity(), str, 1).show();
        }
      });
    }
    
    public void onLanguageListChanged()
    {
      VoiceListFragment.this.mUiThread.execute(new Runnable()
      {
        public void run()
        {
          VoiceListFragment.this.updateInternal();
        }
      });
    }
  };
  private ArrayList<GstaticConfiguration.LanguagePack> mLanguageList;
  private VoiceListAdapter mListAdapter;
  private int mType;
  private Executor mUiThread;
  
  private void addAllSorted(Collection<GstaticConfiguration.LanguagePack> paramCollection)
  {
    this.mLanguageList.clear();
    ArrayList localArrayList = Lists.newArrayList(paramCollection);
    Collections.sort(localArrayList, LanguagePackUtils.newLanguagePackComparator(this.mConfiguration));
    this.mLanguageList.addAll(localArrayList);
  }
  
  private void updateInternal()
  {
    this.mLanguageList.clear();
    if (this.mType == 2) {
      addAllSorted(this.mController.getInstalledLanguages().values());
    }
    for (;;)
    {
      this.mListAdapter.notifyDataSetChanged();
      return;
      addAllSorted(this.mController.getCompatibleLanguages().values());
    }
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setListAdapter(this.mListAdapter);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mType = getArguments().getInt("type");
    VoiceSearchServices localVoiceSearchServices = VelvetServices.get().getVoiceSearchServices();
    this.mController = localVoiceSearchServices.getLanguageUpdateController();
    this.mConfiguration = localVoiceSearchServices.getSettings().getConfiguration();
    this.mUiThread = localVoiceSearchServices.getMainThreadExecutor();
    this.mListAdapter = new VoiceListAdapter();
    this.mLanguageList = Lists.newArrayList();
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130968913, paramViewGroup, false);
    TextView localTextView = (TextView)localView.findViewById(16908292);
    if (this.mType == 2)
    {
      localTextView.setText(2131363584);
      return localView;
    }
    localTextView.setText(2131363583);
    return localView;
  }
  
  public void onPause()
  {
    this.mController.unregisterListener(this.mControllerListener);
    super.onPause();
  }
  
  public void onResume()
  {
    super.onResume();
    this.mController.registerListener(this.mControllerListener);
    if (!this.mController.isInitialized())
    {
      this.mController.initialize();
      return;
    }
    updateInternal();
  }
  
  private final class VoiceListAdapter
    extends BaseAdapter
  {
    private final LayoutInflater mInflater = LayoutInflater.from(VoiceListFragment.this.getActivity());
    
    VoiceListAdapter() {}
    
    public boolean areAllItemsEnabled()
    {
      return false;
    }
    
    public int getCount()
    {
      return VoiceListFragment.this.mLanguageList.size();
    }
    
    public Object getItem(int paramInt)
    {
      return VoiceListFragment.this.mLanguageList.get(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      LanguagePackListItem localLanguagePackListItem;
      View localView;
      if (paramView != null)
      {
        localLanguagePackListItem = (LanguagePackListItem)paramView;
        localView = localLanguagePackListItem.findViewById(2131296737);
        if (paramInt != 0) {
          break label85;
        }
        localView.setVisibility(8);
      }
      for (;;)
      {
        localLanguagePackListItem.update(VoiceListFragment.this.mController, VoiceListFragment.this.mConfiguration, (GstaticConfiguration.LanguagePack)VoiceListFragment.this.mLanguageList.get(paramInt));
        return localLanguagePackListItem;
        localLanguagePackListItem = (LanguagePackListItem)this.mInflater.inflate(2130968732, paramViewGroup, false);
        break;
        label85:
        localView.setVisibility(0);
      }
    }
    
    public boolean isEnabled(int paramInt)
    {
      return false;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.VoiceListFragment
 * JD-Core Version:    0.7.0.1
 */