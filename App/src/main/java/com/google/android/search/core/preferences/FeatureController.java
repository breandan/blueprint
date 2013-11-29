package com.google.android.search.core.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;
import android.preference.SwitchPreference;
import android.view.View;
import android.widget.Toast;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.Feature;
import com.google.android.shared.util.TextUtil;
import com.google.android.shared.util.Util;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FeatureController
  extends SettingsControllerBase
  implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
{
  private final Context mContext;
  private final CoreSearchServices mCoreSearchServices;
  private final HashSet<String> mNewDisabledFeatures;
  private final HashSet<String> mNewEnabledFeatures;
  private final SharedPreferences mPreferences;
  
  public FeatureController(Context paramContext, SharedPreferences paramSharedPreferences, CoreSearchServices paramCoreSearchServices)
  {
    this.mContext = paramContext;
    this.mPreferences = paramSharedPreferences;
    this.mCoreSearchServices = paramCoreSearchServices;
    this.mNewEnabledFeatures = Sets.newHashSet();
    this.mNewDisabledFeatures = Sets.newHashSet();
  }
  
  private EditTextPreference createExperimentsPreference()
  {
    EditTextPreference localEditTextPreference = new EditTextPreference(this.mContext);
    localEditTextPreference.setTitle(2131363144);
    localEditTextPreference.setDialogTitle(2131363144);
    localEditTextPreference.setDialogMessage(2131363145);
    String str = Joiner.on(",").join(this.mPreferences.getStringSet("now_opted_in_experiments", ImmutableSet.of()));
    localEditTextPreference.setSummary(str);
    localEditTextPreference.setText(str);
    localEditTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
    {
      public boolean onPreferenceChange(Preference paramAnonymousPreference, Object paramAnonymousObject)
      {
        HashSet localHashSet = Sets.newHashSet();
        String str1 = ((String)paramAnonymousObject).trim();
        if (!str1.isEmpty())
        {
          String[] arrayOfString = str1.split(",");
          int i = arrayOfString.length;
          int j = 0;
          while (j < i)
          {
            String str3 = arrayOfString[j];
            try
            {
              Integer.parseInt(str3);
              localHashSet.add(str3);
              j++;
            }
            catch (NumberFormatException localNumberFormatException)
            {
              Toast.makeText(FeatureController.this.mContext, 2131363146, 1).show();
              return false;
            }
          }
        }
        FeatureController.this.mPreferences.edit().putStringSet("now_opted_in_experiments", localHashSet).apply();
        String str2 = Joiner.on(",").join(localHashSet);
        ((EditTextPreference)paramAnonymousPreference).setSummary(str2);
        ((EditTextPreference)paramAnonymousPreference).setText(str2);
        return true;
      }
    });
    return localEditTextPreference;
  }
  
  private Preference createPreference(Feature paramFeature)
  {
    SwitchPreference local2 = new SwitchPreference(this.mContext)
    {
      protected void onBindView(View paramAnonymousView)
      {
        setOnPreferenceChangeListener(FeatureController.this);
        super.onBindView(paramAnonymousView);
      }
    };
    local2.setTitle(TextUtil.convertUpperCaseToHumanReadable(paramFeature.name(), false));
    local2.setKey(paramFeature.name());
    local2.setChecked(paramFeature.isEnabled());
    local2.setPersistent(false);
    return local2;
  }
  
  public void handlePreference(Preference paramPreference)
  {
    Iterator localIterator = Feature.configurableFeatures().iterator();
    while (localIterator.hasNext())
    {
      Feature localFeature = (Feature)localIterator.next();
      ((PreferenceGroup)paramPreference).addPreference(createPreference(localFeature));
    }
    Preference localPreference = new Preference(this.mContext);
    localPreference.setTitle(2131363141);
    localPreference.setOnPreferenceClickListener(this);
    ((PreferenceGroup)paramPreference).addPreference(localPreference);
    ((PreferenceGroup)paramPreference).addPreference(createExperimentsPreference());
  }
  
  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    String str = paramPreference.getKey();
    if (((Boolean)paramObject).booleanValue())
    {
      this.mNewDisabledFeatures.remove(str);
      this.mNewEnabledFeatures.add(str);
    }
    for (;;)
    {
      paramPreference.setSummary(2131363147);
      return true;
      this.mNewDisabledFeatures.add(str);
      this.mNewEnabledFeatures.remove(str);
    }
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    Set localSet = Util.copyOf(this.mPreferences.getStringSet("enabled_features", null));
    localSet.removeAll(this.mNewDisabledFeatures);
    localSet.addAll(this.mNewEnabledFeatures);
    HashSet localHashSet = Sets.newHashSet();
    Iterator localIterator = Feature.configurableFeatures().iterator();
    while (localIterator.hasNext()) {
      localHashSet.add(((Feature)localIterator.next()).name());
    }
    localSet.retainAll(localHashSet);
    this.mPreferences.edit().putStringSet("enabled_features", localSet).commit();
    System.exit(0);
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.FeatureController
 * JD-Core Version:    0.7.0.1
 */