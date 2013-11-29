package com.google.android.voicesearch.personalization;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;

public class PersonalizationOptInActivity
  extends Activity
{
  private PersonalizationDialogHelper mDialogHelper;
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mDialogHelper = new PersonalizationDialogHelper(this, VelvetServices.get().getVoiceSearchServices().getSettings());
  }
  
  protected void onResume()
  {
    super.onResume();
    int i = 2;
    if (getIntent().hasExtra("PERSONALIZATION_OPT_IN_ENABLE")) {
      if (!getIntent().getBooleanExtra("PERSONALIZATION_OPT_IN_ENABLE", false)) {
        break label53;
      }
    }
    label53:
    for (i = 0;; i = 1)
    {
      this.mDialogHelper.createDialog(i, new PersonalizationDialogHelper.Callbacks()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          PersonalizationOptInActivity.this.setResult(0);
        }
        
        public void onDismiss(DialogInterface paramAnonymousDialogInterface)
        {
          PersonalizationOptInActivity.this.finish();
        }
      }).show();
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.personalization.PersonalizationOptInActivity
 * JD-Core Version:    0.7.0.1
 */