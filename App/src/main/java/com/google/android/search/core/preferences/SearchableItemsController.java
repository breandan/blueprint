package com.google.android.search.core.preferences;

import android.content.Context;
import android.database.DataSetObserver;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.SearchSettingsImpl;
import com.google.android.search.core.summons.Source;
import com.google.android.search.core.summons.SourceUtil;
import com.google.android.search.core.summons.Sources;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class SearchableItemsController
  extends SettingsControllerBase
  implements Preference.OnPreferenceChangeListener
{
  private final Context mContext;
  private final GlobalSearchServices mGss;
  private PreferenceGroup mPreferenceGroup;
  private DataSetObserver mSourcesObserver;
  
  public SearchableItemsController(SearchSettings paramSearchSettings, GlobalSearchServices paramGlobalSearchServices, Context paramContext)
  {
    super(paramSearchSettings);
    this.mGss = paramGlobalSearchServices;
    this.mContext = paramContext;
  }
  
  private Preference createSourcePreference(Source paramSource)
  {
    SearchableItemPreference localSearchableItemPreference = new SearchableItemPreference(this.mContext);
    localSearchableItemPreference.setKey(SearchSettingsImpl.getSourceEnabledPreference(paramSource));
    localSearchableItemPreference.setDefaultValue(Boolean.valueOf(paramSource.isEnabledByDefault()));
    localSearchableItemPreference.setOnPreferenceChangeListener(this);
    localSearchableItemPreference.setTitle(SourceUtil.getLabel(this.mContext, paramSource));
    CharSequence localCharSequence = SourceUtil.getTextFromResource(this.mContext, paramSource, paramSource.getSettingsDescriptionResourceId());
    localSearchableItemPreference.setSummaryOn(localCharSequence);
    localSearchableItemPreference.setSummaryOff(localCharSequence);
    localSearchableItemPreference.setIcon(SourceUtil.getIcon(this.mContext, paramSource));
    return localSearchableItemPreference;
  }
  
  private void setCurrentSources(Collection<Source> paramCollection)
  {
    HashSet localHashSet;
    for (;;)
    {
      try
      {
        Preconditions.checkNotNull(this.mPreferenceGroup);
        localHashSet = Sets.newHashSet();
        int i = 0;
        if (i < this.mPreferenceGroup.getPreferenceCount())
        {
          localHashSet.add(this.mPreferenceGroup.getPreference(i));
          i++;
          continue;
        }
        Iterator localIterator1 = paramCollection.iterator();
        if (!localIterator1.hasNext()) {
          break;
        }
        Source localSource = (Source)localIterator1.next();
        String str = SearchSettingsImpl.getSourceEnabledPreference(localSource);
        Preference localPreference2 = this.mPreferenceGroup.findPreference(str);
        if (localPreference2 != null)
        {
          localHashSet.remove(localPreference2);
          continue;
        }
        localPreference3 = createSourcePreference(localSource);
      }
      finally {}
      Preference localPreference3;
      this.mPreferenceGroup.addPreference(localPreference3);
    }
    Iterator localIterator2 = localHashSet.iterator();
    while (localIterator2.hasNext())
    {
      Preference localPreference1 = (Preference)localIterator2.next();
      this.mPreferenceGroup.removePreference(localPreference1);
    }
  }
  
  public void handlePreference(Preference paramPreference)
  {
    this.mPreferenceGroup = ((PreferenceGroup)paramPreference);
    this.mPreferenceGroup.setOrderingAsAdded(false);
    final Sources localSources = this.mGss.getSources();
    if (this.mSourcesObserver == null)
    {
      this.mSourcesObserver = new DataSetObserver()
      {
        public void onChanged()
        {
          SearchableItemsController.this.setCurrentSources(localSources.getSources());
        }
      };
      localSources.registerDataSetObserver(this.mSourcesObserver);
    }
    setCurrentSources(localSources.getSources());
  }
  
  public void onDestroy()
  {
    if (this.mSourcesObserver != null)
    {
      this.mGss.getSources().unregisterDataSetObserver(this.mSourcesObserver);
      this.mSourcesObserver = null;
    }
    super.onDestroy();
  }
  
  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    getSettings().broadcastSettingsChanged();
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.SearchableItemsController
 * JD-Core Version:    0.7.0.1
 */