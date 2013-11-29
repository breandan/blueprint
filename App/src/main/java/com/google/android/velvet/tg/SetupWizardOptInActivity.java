package com.google.android.velvet.tg;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SetupWizardOptInActivity
  extends BaseOptInActivity
{
  private Account mAccount;
  private ProgressBar mProgressBar;
  private View mStaticDivider;
  
  public SetupWizardOptInActivity()
  {
    super("SETUP_WIZARD");
  }
  
  public static boolean initTheme(Activity paramActivity, Intent paramIntent)
  {
    if ("holo_light".equals(paramIntent.getStringExtra("theme")))
    {
      paramActivity.setTheme(2131623976);
      return false;
    }
    return true;
  }
  
  private void showProgressBar(boolean paramBoolean)
  {
    int i = 8;
    ProgressBar localProgressBar = this.mProgressBar;
    int j;
    View localView;
    if (paramBoolean)
    {
      j = 0;
      localProgressBar.setVisibility(j);
      localView = this.mStaticDivider;
      if (!paramBoolean) {
        break label44;
      }
    }
    for (;;)
    {
      localView.setVisibility(i);
      return;
      j = i;
      break;
      label44:
      i = 0;
    }
  }
  
  protected void finishOptIn(int paramInt)
  {
    if (isFinishing()) {
      return;
    }
    if (paramInt == this.OPTIN_STATUS_ERROR)
    {
      showProgressBar(false);
      setButtonsEnabled(true);
      Toast.makeText(getApplicationContext(), getString(2131362173), 0).show();
      return;
    }
    if (paramInt == this.OPTIN_STATUS_SUCCESS) {}
    for (int i = 11;; i = 12)
    {
      setResult(i);
      finish();
      return;
    }
  }
  
  protected void handleOptIn()
  {
    showProgressBar(true);
    optInAccount(this.mAccount);
  }
  
  protected void initializeOptInView()
  {
    super.initializeOptInView();
    this.mProgressBar = ((ProgressBar)findViewById(2131296791));
    this.mStaticDivider = findViewById(2131296993);
    TextView localTextView1 = (TextView)findViewById(2131296995);
    SpannableStringBuilder localSpannableStringBuilder1 = new SpannableStringBuilder();
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = getResources().getString(2131362472);
    addBullets(localSpannableStringBuilder1, arrayOfString1);
    localTextView1.setText(localSpannableStringBuilder1);
    TextView localTextView2 = (TextView)findViewById(2131296996);
    SpannableStringBuilder localSpannableStringBuilder2 = new SpannableStringBuilder();
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = getResources().getString(2131362473);
    addBullets(localSpannableStringBuilder2, arrayOfString2);
    localTextView2.setText(localSpannableStringBuilder2);
  }
  
  public void onBackPressed()
  {
    setResult(10);
    finish();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    initTheme(this, getIntent());
    super.onCreate(paramBundle);
    this.mAccount = ((Account)getIntent().getParcelableExtra("optin_account"));
    if (this.mAccount == null)
    {
      Log.e("SetupWizardOptInActivity", "Missing account extra");
      finishOptIn(this.OPTIN_STATUS_CANCELED);
      return;
    }
    setContentView(2130968826);
    initializeOptInView();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.tg.SetupWizardOptInActivity
 * JD-Core Version:    0.7.0.1
 */