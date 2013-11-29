package com.google.android.search.core.preferences;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.FavoriteContactNamesSupplier;
import com.google.android.speech.debug.DebugAudioLogger;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.speech.utils.ProtoBufUtils;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.personalization.PersonalizationHelper;
import com.google.android.voicesearch.settings.Settings;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Debug;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.DebugServer;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Personalization;
import java.util.List;

public class DebugVoiceController
  extends SettingsControllerBase
  implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
{
  private static final CharSequence[] EMPTY_OPTIONS = new CharSequence[0];
  private final Context mContext;
  private final DebugFeatures mDebugFeatures;
  private final NetworkInformation mNetworkInformation;
  private final PersonalizationHelper mPersonalizationHelper;
  private final SearchConfig mSearchConfig;
  private final Settings mVoiceSettings;
  
  public DebugVoiceController(DebugFeatures paramDebugFeatures, Settings paramSettings, PersonalizationHelper paramPersonalizationHelper, NetworkInformation paramNetworkInformation, Context paramContext, SearchConfig paramSearchConfig)
  {
    this.mDebugFeatures = paramDebugFeatures;
    this.mVoiceSettings = paramSettings;
    this.mPersonalizationHelper = paramPersonalizationHelper;
    this.mNetworkInformation = paramNetworkInformation;
    this.mContext = paramContext;
    this.mSearchConfig = paramSearchConfig;
  }
  
  private String getCurrentDebugServer()
  {
    if (!TextUtils.isEmpty(this.mVoiceSettings.getS3ServerOverride())) {
      return this.mVoiceSettings.getS3ServerOverride();
    }
    if (!TextUtils.isEmpty(this.mSearchConfig.getS3ServerOverride())) {
      return this.mSearchConfig.getS3ServerOverride();
    }
    return "Production SSL/HTTPS";
  }
  
  private CharSequence[] getDebugServerEntries(List<GstaticConfiguration.DebugServer> paramList)
  {
    CharSequence[] arrayOfCharSequence = new CharSequence[1 + paramList.size()];
    for (int i = 0; i < -1 + arrayOfCharSequence.length; i++) {
      arrayOfCharSequence[i] = ((GstaticConfiguration.DebugServer)paramList.get(i)).getLabel();
    }
    arrayOfCharSequence[(-1 + arrayOfCharSequence.length)] = "Clear override";
    return arrayOfCharSequence;
  }
  
  private void handleDebugServer(ListPreference paramListPreference)
  {
    if (!TextUtils.isEmpty(this.mVoiceSettings.getDebugS3SandboxOverride()))
    {
      paramListPreference.setSummary("Manual sandbox override in use");
      paramListPreference.setEntries(EMPTY_OPTIONS);
      paramListPreference.setEntryValues(EMPTY_OPTIONS);
      paramListPreference.setEnabled(false);
      return;
    }
    GstaticConfiguration.Configuration localConfiguration = this.mVoiceSettings.getConfiguration();
    if (localConfiguration.hasDebug())
    {
      List localList = localConfiguration.getDebug().getDebugServerList();
      paramListPreference.setEntryValues(getDebugServerEntries(localList));
      paramListPreference.setEntries(getDebugServerEntries(localList));
      paramListPreference.setSummary(getCurrentDebugServer());
      paramListPreference.setValue(getCurrentDebugServer());
      return;
    }
    paramListPreference.setSummary("[DEBUG] Config sync error! Try checking in.");
    paramListPreference.setEntryValues(EMPTY_OPTIONS);
    paramListPreference.setEntries(EMPTY_OPTIONS);
    paramListPreference.setEnabled(false);
  }
  
  private void handlePersonalization(CheckBoxPreference paramCheckBoxPreference)
  {
    paramCheckBoxPreference.setChecked(this.mPersonalizationHelper.isPersonalizationAvailable());
  }
  
  private void handlePersonalizationChange(CheckBoxPreference paramCheckBoxPreference, Boolean paramBoolean)
  {
    boolean bool = paramBoolean.booleanValue();
    int i = 0;
    if (bool) {
      i = this.mNetworkInformation.getSimMcc();
    }
    updatePersonalizationMcc(i);
    handlePersonalization(paramCheckBoxPreference);
  }
  
  private void handleRecognitionEngineRestrict(Preference paramPreference, String paramString)
  {
    String[] arrayOfString1 = this.mContext.getResources().getStringArray(2131492943);
    String[] arrayOfString2 = this.mContext.getResources().getStringArray(2131492942);
    if (paramString == null) {
      paramPreference.setSummary(arrayOfString2[0]);
    }
    for (;;)
    {
      return;
      for (int i = 0; i < arrayOfString1.length; i++) {
        if (paramString.equals(arrayOfString1[i]))
        {
          paramPreference.setSummary(arrayOfString2[i]);
          return;
        }
      }
    }
  }
  
  private void handleTopContacts(ListPreference paramListPreference)
  {
    List localList = new FavoriteContactNamesSupplier(VelvetServices.get().getGsaConfigFlags(), ContactLookup.newInstance(this.mContext)).get();
    String[] arrayOfString = (String[])localList.toArray(new String[localList.size()]);
    if (arrayOfString.length > 0)
    {
      paramListPreference.setEntries(arrayOfString);
      paramListPreference.setEntryValues(arrayOfString);
      return;
    }
    paramListPreference.setEntries(new CharSequence[] { "No top contacts for you!" });
    paramListPreference.setEntryValues(new CharSequence[] { "No top contacts for you!" });
  }
  
  private void sendConfiguration()
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("text/plain");
    localIntent.putExtra("android.intent.extra.SUBJECT", "Configuration:" + this.mVoiceSettings.getConfigurationTimestamp());
    localIntent.putExtra("android.intent.extra.TEXT", ProtoBufUtils.toString(this.mVoiceSettings.getConfiguration()));
    this.mContext.startActivity(localIntent);
  }
  
  private void showEditExperiment(final Preference paramPreference)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.mContext);
    final EditText localEditText = new EditText(this.mContext);
    localEditText.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    localEditText.setText(paramPreference.getSummary());
    localBuilder.setView(localEditText);
    localBuilder.setPositiveButton(17039370, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        GstaticConfiguration.Configuration localConfiguration = DebugVoiceController.this.mVoiceSettings.getOverrideConfiguration();
        localConfiguration.setId(localEditText.getText().toString());
        DebugVoiceController.this.mVoiceSettings.setOverrideConfiguration(localConfiguration);
        paramPreference.setSummary(localEditText.getText().toString());
      }
    });
    localBuilder.setCancelable(true);
    localBuilder.show();
  }
  
  private void updatePersonalizationMcc(int paramInt)
  {
    GstaticConfiguration.Configuration localConfiguration = this.mVoiceSettings.getOverrideConfiguration();
    localConfiguration.setPersonalization(new GstaticConfiguration.Personalization().addMccCountryCodes(paramInt).setDashboardUrl("http://www.google.com").setMoreInfoUrl("http://www.google.com"));
    this.mVoiceSettings.setOverrideConfiguration(localConfiguration);
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return !this.mDebugFeatures.dogfoodDebugEnabled();
  }
  
  public void handlePreference(Preference paramPreference)
  {
    if (paramPreference.getKey().equals("debugS3Server"))
    {
      handleDebugServer((ListPreference)paramPreference);
      paramPreference.setOnPreferenceChangeListener(this);
    }
    do
    {
      return;
      if (paramPreference.getKey().equals("debugS3Logging"))
      {
        paramPreference.setOnPreferenceChangeListener(this);
        return;
      }
      if (paramPreference.getKey().equals("debugPersonalization"))
      {
        paramPreference.setOnPreferenceChangeListener(this);
        handlePersonalization((CheckBoxPreference)paramPreference);
        return;
      }
      if (paramPreference.getKey().equals("debugTopContacts"))
      {
        handleTopContacts((ListPreference)paramPreference);
        return;
      }
      if (paramPreference.getKey().equals("debugConfigurationDate"))
      {
        paramPreference.setSummary(this.mVoiceSettings.getConfigurationTimestamp());
        paramPreference.setOnPreferenceClickListener(this);
        return;
      }
      if (paramPreference.getKey().equals("debugConfigurationExperiment"))
      {
        paramPreference.setSummary(this.mVoiceSettings.getConfiguration().getId());
        paramPreference.setOnPreferenceClickListener(this);
        return;
      }
      if (paramPreference.getKey().equals("debugSendLoggedAudio"))
      {
        paramPreference.setOnPreferenceClickListener(this);
        return;
      }
      if (paramPreference.getKey().equals("audioLoggingEnabled"))
      {
        paramPreference.setOnPreferenceChangeListener(this);
        return;
      }
    } while (!paramPreference.getKey().equals("debugRecognitionEngineRestrict"));
    paramPreference.setOnPreferenceChangeListener(this);
    handleRecognitionEngineRestrict(paramPreference, this.mVoiceSettings.getDebugRecognitionEngineRestrict());
  }
  
  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    if (paramPreference.getKey().equals("debugPersonalization")) {
      handlePersonalizationChange((CheckBoxPreference)paramPreference, (Boolean)paramObject);
    }
    do
    {
      return true;
      if (!paramPreference.getKey().equals("audioLoggingEnabled")) {
        break;
      }
    } while (!Boolean.FALSE.equals(paramObject));
    DebugAudioLogger.clearAllLoggedData(this.mContext);
    return true;
    if (paramPreference.getKey().equals("debugRecognitionEngineRestrict"))
    {
      handleRecognitionEngineRestrict(paramPreference, (String)paramObject);
      return true;
    }
    if (paramPreference.getKey().equals("debugS3Server"))
    {
      String str = (String)paramObject;
      if ("Clear override".equals(str)) {
        this.mVoiceSettings.clearS3ServerOverride();
      }
      for (;;)
      {
        handleDebugServer((ListPreference)paramPreference);
        return false;
        this.mVoiceSettings.setS3ServerOverride(str);
      }
    }
    if (paramPreference.getKey().equals("debugS3Logging"))
    {
      Boolean localBoolean = (Boolean)paramObject;
      this.mVoiceSettings.setS3DebugLoggingEnabled(localBoolean.booleanValue());
      return true;
    }
    return false;
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    if (paramPreference.getKey().equals("debugConfigurationDate"))
    {
      sendConfiguration();
      return true;
    }
    if (paramPreference.getKey().equals("debugConfigurationExperiment"))
    {
      showEditExperiment(paramPreference);
      return true;
    }
    if (paramPreference.getKey().equals("debugSendLoggedAudio"))
    {
      DebugAudioLogger.sendLoggedAudio(this.mContext);
      return true;
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.DebugVoiceController
 * JD-Core Version:    0.7.0.1
 */