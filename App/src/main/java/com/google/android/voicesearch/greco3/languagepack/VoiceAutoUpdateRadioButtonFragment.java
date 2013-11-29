package com.google.android.voicesearch.greco3.languagepack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.settings.Settings;

public class VoiceAutoUpdateRadioButtonFragment
  extends Fragment
{
  private Settings mSettings;
  
  private void initButton(RadioGroup paramRadioGroup, int paramInt1, int paramInt2, final int paramInt3)
  {
    RadioButton localRadioButton = (RadioButton)paramRadioGroup.findViewById(paramInt1);
    if (paramInt2 == paramInt3) {}
    for (boolean bool = true;; bool = false)
    {
      localRadioButton.setChecked(bool);
      localRadioButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          if (((RadioButton)paramAnonymousView).isChecked()) {
            VoiceAutoUpdateRadioButtonFragment.this.mSettings.setLanguagePacksAutoUpdate(paramInt3);
          }
        }
      });
      return;
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mSettings = VelvetServices.get().getVoiceSearchServices().getSettings();
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    RadioGroup localRadioGroup = (RadioGroup)paramLayoutInflater.inflate(2130968910, paramViewGroup, false);
    int i = this.mSettings.getLanguagePacksAutoUpdate();
    initButton(localRadioGroup, 2131297202, i, 0);
    initButton(localRadioGroup, 2131297203, i, 1);
    initButton(localRadioGroup, 2131297204, i, 2);
    return localRadioGroup;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.VoiceAutoUpdateRadioButtonFragment
 * JD-Core Version:    0.7.0.1
 */