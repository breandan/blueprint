package com.android.inputmethodcommon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import java.util.List;

class InputMethodSettingsImpl
{
  private Context mContext;
  private InputMethodInfo mImi;
  private InputMethodManager mImm;
  private int mInputMethodSettingsCategoryTitleRes;
  private Drawable mSubtypeEnablerIcon;
  private int mSubtypeEnablerIconRes;
  private Preference mSubtypeEnablerPreference;
  private CharSequence mSubtypeEnablerTitle;
  private int mSubtypeEnablerTitleRes;
  
  private static String getEnabledSubtypesLabel(Context paramContext, InputMethodManager paramInputMethodManager, InputMethodInfo paramInputMethodInfo)
  {
    if ((paramContext == null) || (paramInputMethodManager == null) || (paramInputMethodInfo == null)) {
      return null;
    }
    List localList = paramInputMethodManager.getEnabledInputMethodSubtypeList(paramInputMethodInfo, true);
    StringBuilder localStringBuilder = new StringBuilder();
    int i = localList.size();
    for (int j = 0; j < i; j++)
    {
      InputMethodSubtype localInputMethodSubtype = (InputMethodSubtype)localList.get(j);
      if (localStringBuilder.length() > 0) {
        localStringBuilder.append(", ");
      }
      localStringBuilder.append(localInputMethodSubtype.getDisplayName(paramContext, paramInputMethodInfo.getPackageName(), paramInputMethodInfo.getServiceInfo().applicationInfo));
    }
    return localStringBuilder.toString();
  }
  
  private static InputMethodInfo getMyImi(Context paramContext, InputMethodManager paramInputMethodManager)
  {
    List localList = paramInputMethodManager.getInputMethodList();
    for (int i = 0; i < localList.size(); i++)
    {
      InputMethodInfo localInputMethodInfo = (InputMethodInfo)localList.get(i);
      if (((InputMethodInfo)localList.get(i)).getPackageName().equals(paramContext.getPackageName())) {
        return localInputMethodInfo;
      }
    }
    return null;
  }
  
  private CharSequence getSubtypeEnablerTitle(Context paramContext)
  {
    if (this.mSubtypeEnablerTitleRes != 0) {
      return paramContext.getString(this.mSubtypeEnablerTitleRes);
    }
    return this.mSubtypeEnablerTitle;
  }
  
  public boolean init(final Context paramContext, PreferenceScreen paramPreferenceScreen)
  {
    this.mContext = paramContext;
    this.mImm = ((InputMethodManager)paramContext.getSystemService("input_method"));
    this.mImi = getMyImi(paramContext, this.mImm);
    if ((this.mImi == null) || (this.mImi.getSubtypeCount() <= 1)) {
      return false;
    }
    this.mSubtypeEnablerPreference = new Preference(paramContext);
    this.mSubtypeEnablerPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
    {
      public boolean onPreferenceClick(Preference paramAnonymousPreference)
      {
        CharSequence localCharSequence = InputMethodSettingsImpl.this.getSubtypeEnablerTitle(paramContext);
        Intent localIntent = new Intent("android.settings.INPUT_METHOD_SUBTYPE_SETTINGS");
        localIntent.putExtra("input_method_id", InputMethodSettingsImpl.this.mImi.getId());
        if (!TextUtils.isEmpty(localCharSequence)) {
          localIntent.putExtra("android.intent.extra.TITLE", localCharSequence);
        }
        localIntent.setFlags(337641472);
        paramContext.startActivity(localIntent);
        return true;
      }
    });
    paramPreferenceScreen.addPreference(this.mSubtypeEnablerPreference);
    updateSubtypeEnabler();
    return true;
  }
  
  public void setInputMethodSettingsCategoryTitle(int paramInt)
  {
    this.mInputMethodSettingsCategoryTitleRes = paramInt;
    updateSubtypeEnabler();
  }
  
  public void setSubtypeEnablerTitle(int paramInt)
  {
    this.mSubtypeEnablerTitleRes = paramInt;
    updateSubtypeEnabler();
  }
  
  public void updateSubtypeEnabler()
  {
    if (this.mSubtypeEnablerPreference != null)
    {
      if (this.mSubtypeEnablerTitleRes == 0) {
        break label75;
      }
      this.mSubtypeEnablerPreference.setTitle(this.mSubtypeEnablerTitleRes);
    }
    label75:
    do
    {
      for (;;)
      {
        String str = getEnabledSubtypesLabel(this.mContext, this.mImm, this.mImi);
        if (!TextUtils.isEmpty(str)) {
          this.mSubtypeEnablerPreference.setSummary(str);
        }
        if (this.mSubtypeEnablerIconRes == 0) {
          break;
        }
        this.mSubtypeEnablerPreference.setIcon(this.mSubtypeEnablerIconRes);
        return;
        if (!TextUtils.isEmpty(this.mSubtypeEnablerTitle)) {
          this.mSubtypeEnablerPreference.setTitle(this.mSubtypeEnablerTitle);
        }
      }
    } while (this.mSubtypeEnablerIcon == null);
    this.mSubtypeEnablerPreference.setIcon(this.mSubtypeEnablerIcon);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.inputmethodcommon.InputMethodSettingsImpl
 * JD-Core Version:    0.7.0.1
 */