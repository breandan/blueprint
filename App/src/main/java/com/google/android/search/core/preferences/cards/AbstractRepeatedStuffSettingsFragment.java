package com.google.android.search.core.preferences.cards;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.NowConfigurationPreferences.ConfigurationEditor;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.sidekick.main.sync.RepeatedMessageInfo;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.protobuf.micro.MessageMicro;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

abstract class AbstractRepeatedStuffSettingsFragment<T extends MessageMicro>
  extends AbstractMyStuffSettingsFragment
  implements Preference.OnPreferenceClickListener
{
  private static final String ADD_PREFERENCE_KEY = "add_preference_key";
  protected static final String PRIMARY_KEY_KEY = "key";
  private static final String SETTING_PREFERENCE_KEY = "setting_preference_key";
  protected static final String TITLE_KEY = "title";
  private PreferenceCategory mCategory;
  private String mPreferenceKey;
  private boolean mStateSaved = false;
  
  private Preference createPreferenceFromSetting(T paramT)
  {
    Preference localPreference = createSettingPreference(getActivity(), paramT);
    localPreference.setKey(keyFor(paramT));
    localPreference.setPersistent(false);
    localPreference.setOnPreferenceClickListener(this);
    localPreference.getExtras().putBoolean("setting_preference_key", true);
    decorateSettingPreference(localPreference, paramT);
    return localPreference;
  }
  
  private String keyFor(T paramT)
  {
    return PredictiveCardsPreferences.REPEATED_MESSAGE_INFO.primaryKeyFor(paramT);
  }
  
  private boolean showRemoveDialog(Preference paramPreference)
  {
    RemoveSettingDialogFragment localRemoveSettingDialogFragment = RemoveSettingDialogFragment.newInstance(this, paramPreference.getTitle(), paramPreference.getKey());
    decorateRemoveDialog(localRemoveSettingDialogFragment, paramPreference);
    localRemoveSettingDialogFragment.show(getActivity().getFragmentManager(), "remove_stock_dialog_tag");
    return true;
  }
  
  protected void addSetting(T paramT)
  {
    String str = keyFor(paramT);
    if (this.mCategory.findPreference(str) != null) {
      return;
    }
    Preference localPreference = createPreferenceFromSetting(paramT);
    this.mCategory.addPreference(localPreference);
    getConfigurationPreferences().editConfiguration().addMessage(this.mPreferenceKey, paramT).apply();
  }
  
  protected Preference createSettingPreference(Context paramContext, T paramT)
  {
    return new Preference(paramContext);
  }
  
  protected abstract boolean decorateAddPreference(Preference paramPreference);
  
  protected void decorateRemoveDialog(DialogFragment paramDialogFragment, Preference paramPreference) {}
  
  protected abstract void decorateSettingPreference(Preference paramPreference, T paramT);
  
  protected abstract String getPreferencesKey();
  
  protected abstract Comparator<T> getSettingComparator();
  
  protected abstract Predicate<T> isSettingShown();
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.mStateSaved = false;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mPreferenceKey = getPreferencesKey();
    this.mCategory = ((PreferenceCategory)getPreferenceScreen().findPreference(getString(2131362045)));
    this.mCategory.setOrderingAsAdded(false);
    ArrayList localArrayList = Lists.newArrayList(Iterables.filter(getConfigurationPreferences().getMessages(this.mPreferenceKey), isSettingShown()));
    Collections.sort(localArrayList, getSettingComparator());
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      Preference localPreference2 = createPreferenceFromSetting((MessageMicro)localIterator.next());
      this.mCategory.addPreference(localPreference2);
    }
    Preference localPreference1 = new Preference(getActivity());
    localPreference1.setKey("add_preference_key");
    localPreference1.setPersistent(false);
    localPreference1.setOnPreferenceClickListener(this);
    localPreference1.setShouldDisableView(true);
    if (decorateAddPreference(localPreference1)) {
      getPreferenceScreen().addPreference(localPreference1);
    }
    this.mStateSaved = false;
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    this.mStateSaved = false;
    return localView;
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    if (this.mStateSaved) {}
    do
    {
      return false;
      if ("add_preference_key".equals(paramPreference.getKey())) {
        return showAddDialog(paramPreference);
      }
    } while (!paramPreference.getExtras().containsKey("setting_preference_key"));
    return showRemoveDialog(paramPreference);
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.mStateSaved = true;
  }
  
  protected void setHidden(String paramString, boolean paramBoolean)
  {
    Preference localPreference = this.mCategory.findPreference(paramString);
    if (localPreference != null)
    {
      this.mCategory.removePreference(localPreference);
      setPreferenceHidden(paramString, paramBoolean);
    }
  }
  
  protected void setPreferenceHidden(String paramString, boolean paramBoolean)
  {
    MessageMicro localMessageMicro = getConfigurationPreferences().getMessage(this.mPreferenceKey, paramString);
    if (localMessageMicro != null)
    {
      setSettingHidden(localMessageMicro, paramBoolean);
      getConfigurationPreferences().editConfiguration().updateMessage(this.mPreferenceKey, localMessageMicro).apply();
    }
  }
  
  protected abstract void setSettingHidden(T paramT, boolean paramBoolean);
  
  protected boolean showAddDialog(Preference paramPreference)
  {
    return false;
  }
  
  private static class RemoveSettingDialogFragment
    extends DialogFragment
  {
    private static RemoveSettingDialogFragment newInstance(Fragment paramFragment, CharSequence paramCharSequence, String paramString)
    {
      RemoveSettingDialogFragment localRemoveSettingDialogFragment = new RemoveSettingDialogFragment();
      Bundle localBundle = new Bundle();
      localBundle.putCharSequence("title", paramCharSequence);
      localBundle.putString("key", paramString);
      localRemoveSettingDialogFragment.setArguments(localBundle);
      localRemoveSettingDialogFragment.setTargetFragment(paramFragment, 0);
      return localRemoveSettingDialogFragment;
    }
    
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      CharSequence localCharSequence = getArguments().getCharSequence("title");
      final String str = getArguments().getString("key");
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
      localBuilder.setTitle(localCharSequence).setPositiveButton(2131362572, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          ((AbstractRepeatedStuffSettingsFragment)AbstractRepeatedStuffSettingsFragment.RemoveSettingDialogFragment.this.getTargetFragment()).setHidden(str, true);
        }
      }).setNegativeButton(2131363226, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {}
      });
      return localBuilder.create();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.AbstractRepeatedStuffSettingsFragment
 * JD-Core Version:    0.7.0.1
 */