package com.google.android.search.core.preferences.cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import com.google.android.search.core.preferences.PreferencesUtil;

public class NotificationsSettingsFragment
  extends CardSettingsFragment
{
  private RingtonePreference mRingtonePreference;
  
  private void configureRingtonePreference(final Context paramContext)
  {
    this.mRingtonePreference = ((RingtonePreference)findPreference(paramContext.getString(2131362110)));
    this.mRingtonePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
    {
      public boolean onPreferenceChange(Preference paramAnonymousPreference, Object paramAnonymousObject)
      {
        String str = (String)paramAnonymousObject;
        Uri localUri = null;
        if (str != null)
        {
          boolean bool = TextUtils.isEmpty(str);
          localUri = null;
          if (!bool) {
            localUri = Uri.parse(str);
          }
        }
        PreferencesUtil.updateRingtoneSummary(paramContext, paramAnonymousPreference, localUri);
        return true;
      }
    });
  }
  
  protected void addCardEnablementSwitch(String paramString) {}
  
  protected int getPreferenceResourceId()
  {
    return 2131099668;
  }
  
  protected boolean isOverallPreferenceEnabled(SharedPreferences paramSharedPreferences, String paramString)
  {
    return paramSharedPreferences.getInt(paramString, 1) == 1;
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mRingtonePreference.onActivityResult(paramInt1, paramInt2, paramIntent);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    configureRingtonePreference(getActivity().getApplicationContext());
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    this.mRingtonePreference.setOnPreferenceChangeListener(null);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.NotificationsSettingsFragment
 * JD-Core Version:    0.7.0.1
 */