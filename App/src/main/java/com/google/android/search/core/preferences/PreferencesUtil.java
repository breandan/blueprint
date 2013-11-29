package com.google.android.search.core.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.RingtonePreference;
import android.preference.TwoStatePreference;
import android.provider.Settings.System;
import android.text.TextUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class PreferencesUtil
{
  private static Uri getRingtoneUriFromPreference(Preference paramPreference)
  {
    String str = paramPreference.getSharedPreferences().getString(paramPreference.getKey(), null);
    if (str != null) {
      return Uri.parse(str);
    }
    return Settings.System.DEFAULT_NOTIFICATION_URI;
  }
  
  private static CharSequence getRollupSummary(Preference paramPreference)
  {
    if ((!paramPreference.isEnabled()) && (!(paramPreference instanceof PreferenceCategory))) {}
    do
    {
      TwoStatePreference localTwoStatePreference;
      do
      {
        return null;
        if ((paramPreference instanceof ListPreference)) {
          return ((ListPreference)paramPreference).getSummary();
        }
        if ((paramPreference instanceof PreferenceGroup))
        {
          updatePreferenceGroupSummary((PreferenceGroup)paramPreference);
          CharSequence localCharSequence = paramPreference.getSummary();
          if (paramPreference.getContext().getString(2131362181).equals(localCharSequence)) {
            localCharSequence = null;
          }
          return localCharSequence;
        }
        if (!(paramPreference instanceof TwoStatePreference)) {
          break;
        }
        localTwoStatePreference = (TwoStatePreference)paramPreference;
        if (localTwoStatePreference.isChecked())
        {
          if (!TextUtils.isEmpty(localTwoStatePreference.getSummaryOn())) {
            return localTwoStatePreference.getSummaryOn();
          }
          return localTwoStatePreference.getTitle();
        }
      } while (TextUtils.isEmpty(localTwoStatePreference.getSummaryOff()));
      return localTwoStatePreference.getSummaryOff();
    } while (!(paramPreference instanceof RingtonePreference));
    Uri localUri = getRingtoneUriFromPreference(paramPreference);
    updateRingtoneSummary(paramPreference.getContext(), paramPreference, localUri);
    return null;
  }
  
  private static void updatePreferenceGroupSummary(PreferenceGroup paramPreferenceGroup)
  {
    ArrayList localArrayList = Lists.newArrayList();
    if (!updatePreferenceSummary(paramPreferenceGroup)) {}
    while (paramPreferenceGroup.getPreferenceCount() == 0) {
      return;
    }
    for (int i = 0; i < paramPreferenceGroup.getPreferenceCount(); i++)
    {
      CharSequence localCharSequence = getRollupSummary(paramPreferenceGroup.getPreference(i));
      if (localCharSequence != null) {
        localArrayList.add(localCharSequence);
      }
    }
    if (localArrayList.size() > 0)
    {
      paramPreferenceGroup.setSummary(Joiner.on(", ").join(localArrayList));
      return;
    }
    paramPreferenceGroup.setSummary(2131362181);
  }
  
  public static void updatePreferenceSummaries(Preference paramPreference)
  {
    updatePreferenceValue(paramPreference);
    if ((paramPreference instanceof PreferenceGroup))
    {
      updatePreferenceGroupSummary((PreferenceGroup)paramPreference);
      return;
    }
    updatePreferenceSummary(paramPreference);
  }
  
  private static boolean updatePreferenceSummary(Preference paramPreference)
  {
    boolean bool = true;
    if (!TextUtils.isEmpty(paramPreference.getKey()))
    {
      SharedPreferences localSharedPreferences = paramPreference.getSharedPreferences();
      try
      {
        if (!localSharedPreferences.getBoolean(paramPreference.getKey(), true))
        {
          if (!paramPreference.getContext().getString(2131362180).equals(paramPreference.getSummary()))
          {
            paramPreference.getExtras().putCharSequence("configured_summary", paramPreference.getSummary());
            paramPreference.setSummary(2131362180);
          }
        }
        else
        {
          CharSequence localCharSequence = paramPreference.getExtras().getCharSequence("configured_summary", null);
          if (localCharSequence == null) {
            return bool;
          }
          paramPreference.setSummary(localCharSequence);
          return bool;
        }
      }
      catch (ClassCastException localClassCastException)
      {
        return bool;
      }
      bool = false;
    }
    return bool;
  }
  
  private static void updatePreferenceValue(Preference paramPreference)
  {
    int i = 1;
    if (!paramPreference.isPersistent()) {}
    for (;;)
    {
      return;
      SharedPreferences localSharedPreferences = paramPreference.getSharedPreferences();
      if ((paramPreference instanceof PreferenceGroup))
      {
        updatePreferenceValueGroup((PreferenceGroup)paramPreference);
        return;
      }
      if ((paramPreference instanceof NotificationGroupPreference))
      {
        NotificationGroupPreference localNotificationGroupPreference = (NotificationGroupPreference)paramPreference;
        if (localSharedPreferences.getInt(paramPreference.getKey(), i) == i) {}
        for (;;)
        {
          localNotificationGroupPreference.setChecked(i);
          return;
          int j = 0;
        }
      }
      if ((paramPreference instanceof TwoStatePreference))
      {
        ((TwoStatePreference)paramPreference).setChecked(localSharedPreferences.getBoolean(paramPreference.getKey(), false));
        return;
      }
      if ((paramPreference instanceof ListPreference))
      {
        ListPreference localListPreference = (ListPreference)paramPreference;
        CharSequence[] arrayOfCharSequence1 = localListPreference.getEntries();
        CharSequence[] arrayOfCharSequence2 = localListPreference.getEntryValues();
        String str = localSharedPreferences.getString(paramPreference.getKey(), null);
        if (str != null) {
          for (int k = 0; k < arrayOfCharSequence1.length; k++) {
            if (arrayOfCharSequence2[k].equals(str)) {
              localListPreference.setSummary(arrayOfCharSequence1[k]);
            }
          }
        }
      }
    }
  }
  
  private static void updatePreferenceValueGroup(PreferenceGroup paramPreferenceGroup)
  {
    for (int i = 0; i < paramPreferenceGroup.getPreferenceCount(); i++) {
      updatePreferenceValue(paramPreferenceGroup.getPreference(i));
    }
  }
  
  public static void updateRingtoneSummary(Context paramContext, Preference paramPreference, Uri paramUri)
  {
    new UpdateRingtoneTask(paramContext, paramPreference, paramUri).execute(new Void[0]);
  }
  
  static class UpdateRingtoneTask
    extends AsyncTask<Void, Void, String>
  {
    private final Context mContext;
    private final Preference mPreference;
    private final Uri mRingtoneUri;
    
    public UpdateRingtoneTask(Context paramContext, Preference paramPreference, Uri paramUri)
    {
      this.mContext = paramContext;
      this.mPreference = paramPreference;
      this.mRingtoneUri = paramUri;
    }
    
    protected String doInBackground(Void... paramVarArgs)
    {
      Uri localUri = this.mRingtoneUri;
      String str = null;
      if (localUri != null)
      {
        boolean bool = TextUtils.isEmpty(this.mRingtoneUri.getPath());
        str = null;
        if (!bool)
        {
          Ringtone localRingtone = RingtoneManager.getRingtone(this.mContext, this.mRingtoneUri);
          str = null;
          if (localRingtone != null) {
            str = localRingtone.getTitle(this.mContext);
          }
        }
      }
      if (str == null) {
        str = this.mContext.getString(2131362212);
      }
      return str;
    }
    
    protected void onPostExecute(String paramString)
    {
      if (PreferencesUtil.getRingtoneUriFromPreference(this.mPreference).equals(this.mRingtoneUri)) {
        this.mPreference.setSummary(paramString);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.PreferencesUtil
 * JD-Core Version:    0.7.0.1
 */