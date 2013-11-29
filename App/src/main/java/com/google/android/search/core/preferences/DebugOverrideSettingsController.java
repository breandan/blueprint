package com.google.android.search.core.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.SearchSettings;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.common.base.Joiner;
import com.google.gws.plugins.searchapp.GsaConfigurationProto.GsaExperiments;
import com.google.gws.plugins.searchapp.GsaConfigurationProto.KeyValuePair;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DebugOverrideSettingsController
  extends SettingsControllerBase
  implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
{
  private final Context mContext;
  private final CoreSearchServices mCoreSearchServices;
  private Set<String> mCustomOverridableFlags;
  private Map<String, GsaConfigurationProto.KeyValuePair> mGsaConfigFlags;
  private String mNewCustomOverridableFlag;
  
  public DebugOverrideSettingsController(Context paramContext, SharedPreferences paramSharedPreferences, CoreSearchServices paramCoreSearchServices)
  {
    this.mContext = paramContext;
    this.mCoreSearchServices = paramCoreSearchServices;
    this.mGsaConfigFlags = new HashMap(this.mCoreSearchServices.getGsaConfigFlags().getGsaConfigFlags());
    this.mCustomOverridableFlags = this.mCoreSearchServices.getSearchSettings().getDebugGsaConfigOverridableFlags();
    if (this.mCustomOverridableFlags == null) {
      this.mCustomOverridableFlags = new HashSet();
    }
    this.mNewCustomOverridableFlag = "";
  }
  
  private void handlePreferenceHelper(Preference paramPreference1, GsaConfigurationProto.KeyValuePair paramKeyValuePair, Preference paramPreference2, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "*" + paramKeyValuePair.getKey();; str = paramKeyValuePair.getKey())
    {
      paramPreference1.setTitle(str);
      paramPreference1.setKey(paramKeyValuePair.getKey());
      paramPreference1.setPersistent(false);
      paramPreference1.setOnPreferenceChangeListener(this);
      ((PreferenceGroup)paramPreference2).addPreference(paramPreference1);
      return;
    }
  }
  
  public void handlePreference(Preference paramPreference)
  {
    Iterator localIterator1 = this.mGsaConfigFlags.keySet().iterator();
    while (localIterator1.hasNext())
    {
      String str4 = (String)localIterator1.next();
      boolean bool2 = this.mCustomOverridableFlags.contains(str4);
      GsaConfigurationProto.KeyValuePair localKeyValuePair5 = (GsaConfigurationProto.KeyValuePair)this.mGsaConfigFlags.get(str4);
      if (localKeyValuePair5.hasBoolValue())
      {
        SwitchPreference local1 = new SwitchPreference(this.mContext)
        {
          protected void onBindView(View paramAnonymousView)
          {
            setOnPreferenceChangeListener(DebugOverrideSettingsController.this);
            super.onBindView(paramAnonymousView);
          }
        };
        local1.setChecked(localKeyValuePair5.getBoolValue());
        handlePreferenceHelper(local1, localKeyValuePair5, paramPreference, bool2);
      }
      else
      {
        EditTextPreference local2 = new EditTextPreference(this.mContext)
        {
          protected void onBindView(View paramAnonymousView)
          {
            setOnPreferenceChangeListener(DebugOverrideSettingsController.this);
            super.onBindView(paramAnonymousView);
          }
        };
        if (localKeyValuePair5.hasIntValue())
        {
          local2.setDefaultValue(Integer.toString(localKeyValuePair5.getIntValue()));
          local2.getEditText().setInputType(2);
          handlePreferenceHelper(local2, localKeyValuePair5, paramPreference, bool2);
        }
        else if (localKeyValuePair5.hasStringValue())
        {
          local2.setDefaultValue(localKeyValuePair5.getStringValue());
          handlePreferenceHelper(local2, localKeyValuePair5, paramPreference, bool2);
        }
      }
    }
    Iterator localIterator2 = this.mCustomOverridableFlags.iterator();
    while (localIterator2.hasNext())
    {
      String str2 = (String)localIterator2.next();
      if (!this.mGsaConfigFlags.containsKey(str2))
      {
        int i = this.mContext.getResources().getIdentifier(str2, "bool", "com.google.android.googlequicksearchbox");
        if (i != 0)
        {
          boolean bool1 = this.mContext.getResources().getBoolean(i);
          SwitchPreference local3 = new SwitchPreference(this.mContext)
          {
            protected void onBindView(View paramAnonymousView)
            {
              setOnPreferenceChangeListener(DebugOverrideSettingsController.this);
              super.onBindView(paramAnonymousView);
            }
          };
          local3.setChecked(bool1);
          GsaConfigurationProto.KeyValuePair localKeyValuePair4 = new GsaConfigurationProto.KeyValuePair().setKey(str2).setBoolValue(bool1);
          this.mGsaConfigFlags.put(localKeyValuePair4.getKey(), localKeyValuePair4);
          handlePreferenceHelper(local3, localKeyValuePair4, paramPreference, true);
        }
        else
        {
          EditTextPreference local4 = new EditTextPreference(this.mContext)
          {
            protected void onBindView(View paramAnonymousView)
            {
              setOnPreferenceChangeListener(DebugOverrideSettingsController.this);
              super.onBindView(paramAnonymousView);
            }
          };
          int j = this.mContext.getResources().getIdentifier(str2, "integer", "com.google.android.googlequicksearchbox");
          if (j != 0)
          {
            int m = this.mContext.getResources().getInteger(j);
            GsaConfigurationProto.KeyValuePair localKeyValuePair3 = new GsaConfigurationProto.KeyValuePair().setKey(str2).setIntValue(m);
            this.mGsaConfigFlags.put(localKeyValuePair3.getKey(), localKeyValuePair3);
            local4.setDefaultValue(Integer.toString(localKeyValuePair3.getIntValue()));
            local4.getEditText().setInputType(2);
            handlePreferenceHelper(local4, localKeyValuePair3, paramPreference, true);
          }
          else
          {
            int k = this.mContext.getResources().getIdentifier(str2, "string", "com.google.android.googlequicksearchbox");
            if (k != 0)
            {
              String str3 = this.mContext.getResources().getString(k);
              GsaConfigurationProto.KeyValuePair localKeyValuePair2 = new GsaConfigurationProto.KeyValuePair().setKey(str2).setStringValue(str3);
              this.mGsaConfigFlags.put(localKeyValuePair2.getKey(), localKeyValuePair2);
              local4.setDefaultValue(localKeyValuePair2.getStringValue());
              handlePreferenceHelper(local4, localKeyValuePair2, paramPreference, true);
            }
            if (this.mContext.getResources().getIdentifier(str2, "array", "com.google.android.googlequicksearchbox") != 0)
            {
              GsaConfigurationProto.KeyValuePair localKeyValuePair1 = new GsaConfigurationProto.KeyValuePair().setKey(str2).setStringValue("");
              this.mGsaConfigFlags.put(localKeyValuePair1.getKey(), localKeyValuePair1);
              local4.setDefaultValue(localKeyValuePair1.getStringValue());
              handlePreferenceHelper(local4, localKeyValuePair1, paramPreference, true);
            }
          }
        }
      }
    }
    EditTextPreference localEditTextPreference1 = new EditTextPreference(this.mContext);
    localEditTextPreference1.setTitle(2131363359);
    localEditTextPreference1.setDefaultValue("");
    localEditTextPreference1.setKey("add_custom_flag_key");
    localEditTextPreference1.setOnPreferenceChangeListener(this);
    ((PreferenceGroup)paramPreference).addPreference(localEditTextPreference1);
    Preference localPreference1 = new Preference(this.mContext);
    localPreference1.setTitle(2131363142);
    localPreference1.setSummary(2131363143);
    localPreference1.setOnPreferenceClickListener(this);
    ((PreferenceGroup)paramPreference).addPreference(localPreference1);
    String str1 = Joiner.on(",").join(this.mCoreSearchServices.getGsaConfigFlags().getExperimentIds());
    EditTextPreference localEditTextPreference2 = new EditTextPreference(this.mContext);
    localEditTextPreference2.setTitle(2131363150);
    localEditTextPreference2.setDefaultValue(str1);
    localEditTextPreference2.getEditText().setCursorVisible(false);
    ((PreferenceGroup)paramPreference).addPreference(localEditTextPreference2);
    Preference localPreference2 = new Preference(this.mContext);
    localPreference2.setTitle(2131363149);
    localPreference2.setKey("clear_local_overrides_key");
    localPreference2.setOnPreferenceClickListener(this);
    ((PreferenceGroup)paramPreference).addPreference(localPreference2);
    Preference localPreference3 = new Preference(this.mContext);
    localPreference3.setTitle(2131363148);
    localPreference3.setKey("gsa_home");
    localPreference3.setOnPreferenceClickListener(this);
    ((PreferenceGroup)paramPreference).addPreference(localPreference3);
  }
  
  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    if ((paramPreference.hasKey()) && (paramPreference.getKey().equals("add_custom_flag_key"))) {
      this.mNewCustomOverridableFlag = ((String)paramObject);
    }
    if (!this.mGsaConfigFlags.containsKey(paramPreference.getKey())) {}
    GsaConfigurationProto.KeyValuePair localKeyValuePair;
    do
    {
      return true;
      localKeyValuePair = (GsaConfigurationProto.KeyValuePair)this.mGsaConfigFlags.get(paramPreference.getKey());
      if (localKeyValuePair.hasBoolValue())
      {
        localKeyValuePair.setBoolValue(((Boolean)paramObject).booleanValue());
        return true;
      }
      if (localKeyValuePair.hasIntValue())
      {
        localKeyValuePair.setIntValue(Integer.parseInt((String)paramObject));
        return true;
      }
    } while (!localKeyValuePair.hasStringValue());
    localKeyValuePair.setStringValue((String)paramObject);
    return true;
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    if ((paramPreference.hasKey()) && (paramPreference.getKey().equals("gsa_home")))
    {
      this.mCoreSearchServices.getBackgroundTasks().forceRunInterruptingOngoing("send_gsa_home_request");
      return true;
    }
    if ((paramPreference.hasKey()) && (paramPreference.getKey().equals("clear_local_overrides_key")))
    {
      this.mCoreSearchServices.getSearchSettings().setGsaConfigOverride(new GsaConfigurationProto.GsaExperiments());
      this.mCoreSearchServices.getGsaConfigFlags().updateGsaConfig(this.mCoreSearchServices.getSearchSettings().getGsaConfigServer(), this.mCoreSearchServices.getSearchSettings().getGsaConfigOverride());
      this.mCoreSearchServices.getSearchSettings().setDebugGsaConfigOverridableFlags(null);
      return true;
    }
    if ((this.mNewCustomOverridableFlag != null) && (!TextUtils.isEmpty(this.mNewCustomOverridableFlag)))
    {
      this.mCustomOverridableFlags.add(this.mNewCustomOverridableFlag);
      this.mCoreSearchServices.getSearchSettings().setDebugGsaConfigOverridableFlags(this.mCustomOverridableFlags);
      if ((this.mContext instanceof Activity)) {
        ((Activity)this.mContext).recreate();
      }
    }
    GsaConfigurationProto.GsaExperiments localGsaExperiments = new GsaConfigurationProto.GsaExperiments();
    Iterator localIterator = this.mGsaConfigFlags.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localGsaExperiments.addKeyValuePair((GsaConfigurationProto.KeyValuePair)this.mGsaConfigFlags.get(str));
    }
    this.mCoreSearchServices.getSearchSettings().setGsaConfigOverride(localGsaExperiments);
    this.mCoreSearchServices.getGsaConfigFlags().updateGsaConfig(this.mCoreSearchServices.getSearchSettings().getGsaConfigServer(), this.mCoreSearchServices.getSearchSettings().getGsaConfigOverride());
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.DebugOverrideSettingsController
 * JD-Core Version:    0.7.0.1
 */