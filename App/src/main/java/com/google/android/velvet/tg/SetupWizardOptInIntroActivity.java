package com.google.android.velvet.tg;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExecutorAsyncTask;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.velvet.VelvetServices;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nullable;

public class SetupWizardOptInIntroActivity
  extends Activity
  implements View.OnClickListener
{
  private Account mAccount;
  private AccountConfigurationHelper mAccountConfigurationHelper;
  private ExecutorService mBgExecutor;
  private Clock mClock;
  private View mContentContainer;
  private boolean mDarkTheme;
  private boolean mDisableBack;
  private LoginHelper mLoginHelper;
  private Button mNextButton;
  private ProgressBar mProgressBar;
  private SampleCardsView mSampleCardsView;
  private View mStaticDivider;
  private TextView mTitle;
  private ScheduledSingleThreadedExecutor mUiExecutor;
  private UserInteractionLogger mUserInteractionLogger;
  private long mWaitStartTimeMs;
  
  private boolean initDirButton(Button paramButton, boolean paramBoolean1, boolean paramBoolean2)
  {
    Drawable[] arrayOfDrawable;
    for (;;)
    {
      Resources localResources;
      int i;
      String str4;
      int j;
      try
      {
        localResources = getApplicationContext().getPackageManager().getResourcesForApplication("com.google.android.setupwizard");
        String[] arrayOfString = { "normal", "disabled", "focused", "pressed", "disabled_focused" };
        arrayOfDrawable = new Drawable[arrayOfString.length];
        i = 0;
        if (i >= arrayOfString.length) {
          break;
        }
        String str1 = arrayOfString[i];
        StringBuilder localStringBuilder1 = new StringBuilder().append("btn_dir_");
        if (paramBoolean1)
        {
          str2 = "next_";
          StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append(str1);
          if (!paramBoolean2) {
            break label192;
          }
          str3 = "_dark";
          str4 = str3;
          j = localResources.getIdentifier(str4, "drawable", "com.google.android.setupwizard");
          if (j != 0) {
            break label199;
          }
          Log.w("SetupWizardOptInIntroActivity", "Failed to find resource: " + str4);
          return false;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        Log.w("SetupWizardOptInIntroActivity", "Failed to get resources for: com.google.android.setupwizard");
        return false;
      }
      String str2 = "prev_";
      continue;
      label192:
      String str3 = "_light";
      continue;
      try
      {
        label199:
        Drawable localDrawable = localResources.getDrawable(j);
        if (localDrawable == null)
        {
          Log.w("SetupWizardOptInIntroActivity", "Failed to get drawable: " + str4);
          return false;
        }
        arrayOfDrawable[i] = localDrawable;
        i++;
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        Log.w("SetupWizardOptInIntroActivity", "Failed to get drawable: " + str4);
        return false;
      }
    }
    StateListDrawable localStateListDrawable = new StateListDrawable();
    localStateListDrawable.addState(new int[] { -16842909, 16842910 }, arrayOfDrawable[0]);
    localStateListDrawable.addState(new int[] { -16842909, -16842910 }, arrayOfDrawable[1]);
    localStateListDrawable.addState(new int[] { 16842919 }, arrayOfDrawable[3]);
    localStateListDrawable.addState(new int[] { 16842908, 16842910 }, arrayOfDrawable[2]);
    localStateListDrawable.addState(new int[] { 16842910 }, arrayOfDrawable[0]);
    localStateListDrawable.addState(new int[] { 16842908 }, arrayOfDrawable[4]);
    localStateListDrawable.addState(new int[0], arrayOfDrawable[1]);
    this.mNextButton.setBackground(localStateListDrawable);
    return true;
  }
  
  private void loadAccountAsync()
  {
    new LoadAccountTask(this.mUiExecutor, this.mBgExecutor).execute(new Void[0]);
  }
  
  private void scheduleFinish()
  {
    scheduleWaitTransition(new Runnable()
    {
      public void run()
      {
        SetupWizardOptInIntroActivity.this.finish();
      }
    });
  }
  
  private void scheduleIntroScreen()
  {
    scheduleWaitTransition(new Runnable()
    {
      public void run()
      {
        SetupWizardOptInIntroActivity.this.showIntroScreen();
      }
    });
  }
  
  private void scheduleWaitTransition(Runnable paramRunnable)
  {
    long l = this.mClock.elapsedRealtime() - this.mWaitStartTimeMs;
    if (l >= 1000L)
    {
      paramRunnable.run();
      return;
    }
    this.mUiExecutor.executeDelayed(paramRunnable, 1000L - l);
  }
  
  private void showIntroScreen()
  {
    this.mTitle.setText(getString(2131362859));
    if (this.mSampleCardsView == null)
    {
      ((ViewStub)findViewById(2131296998)).inflate();
      this.mSampleCardsView = ((SampleCardsView)findViewById(2131296596));
      this.mSampleCardsView.init(getLayoutInflater(), this.mClock);
      if (!initDirButton(this.mNextButton, true, this.mDarkTheme))
      {
        this.mNextButton.setText(getApplicationContext().getString(2131362866));
        this.mNextButton.getLayoutParams().width = -2;
        this.mNextButton.getLayoutParams().height = -2;
      }
    }
    switchMode(false);
  }
  
  private void showWaitScreen()
  {
    this.mTitle.setText(2131362867);
    switchMode(true);
  }
  
  private void switchMode(boolean paramBoolean)
  {
    View localView1 = this.mContentContainer;
    int i;
    int j;
    label31:
    ProgressBar localProgressBar;
    int k;
    if (paramBoolean)
    {
      i = 8;
      localView1.setVisibility(i);
      View localView2 = this.mStaticDivider;
      if (!paramBoolean) {
        break label64;
      }
      j = 8;
      localView2.setVisibility(j);
      localProgressBar = this.mProgressBar;
      k = 0;
      if (!paramBoolean) {
        break label70;
      }
    }
    for (;;)
    {
      localProgressBar.setVisibility(k);
      return;
      i = 0;
      break;
      label64:
      j = 0;
      break label31;
      label70:
      k = 8;
    }
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    switch (paramInt1)
    {
    default: 
      super.onActivityResult(paramInt1, paramInt2, paramIntent);
    }
    do
    {
      return;
    } while (paramInt2 == 10);
    setResult(paramInt2);
    finish();
  }
  
  public void onBackPressed()
  {
    if (!this.mDisableBack)
    {
      setResult(10);
      finish();
    }
  }
  
  public void onClick(View paramView)
  {
    if (paramView == this.mNextButton)
    {
      this.mUserInteractionLogger.logAnalyticsAction("BUTTON_PRESS", "SETUP_WIZARD_INTRO_NEXT");
      Intent localIntent = new Intent(this, SetupWizardOptInActivity.class);
      localIntent.putExtras(getIntent());
      localIntent.putExtra("optin_account", this.mAccount);
      startActivityForResult(localIntent, 1001);
    }
  }
  
  protected void onCreate(@Nullable Bundle paramBundle)
  {
    this.mDarkTheme = SetupWizardOptInActivity.initTheme(this, getIntent());
    super.onCreate(paramBundle);
    VelvetServices localVelvetServices = VelvetServices.get();
    this.mClock = localVelvetServices.getCoreServices().getClock();
    this.mUserInteractionLogger = localVelvetServices.getCoreServices().getUserInteractionLogger();
    this.mUiExecutor = localVelvetServices.getAsyncServices().getUiThreadExecutor();
    this.mBgExecutor = localVelvetServices.getAsyncServices().getScheduledBackgroundExecutorService();
    this.mLoginHelper = localVelvetServices.getCoreServices().getLoginHelper();
    this.mAccountConfigurationHelper = new AccountConfigurationHelper(localVelvetServices.getCoreServices().getGmsLocationReportingHelper(), localVelvetServices.getCoreServices().getNowOptInSettings());
    View localView = getLayoutInflater().inflate(2130968827, null);
    setContentView(localView);
    this.mDisableBack = getIntent().getBooleanExtra("noBack", false);
    if (this.mDisableBack) {
      localView.setSystemUiVisibility(4194304);
    }
    this.mTitle = ((TextView)findViewById(2131296382));
    this.mProgressBar = ((ProgressBar)findViewById(2131296791));
    this.mStaticDivider = findViewById(2131296993);
    this.mContentContainer = findViewById(2131296591);
    this.mNextButton = ((Button)findViewById(2131296454));
    this.mNextButton.setOnClickListener(this);
    if (paramBundle != null) {
      this.mAccount = ((Account)paramBundle.getParcelable("account"));
    }
  }
  
  public void onResume()
  {
    super.onResume();
    this.mUserInteractionLogger.logView("SETUP_WIZARD_INTRO");
    if (this.mAccount != null)
    {
      showIntroScreen();
      return;
    }
    showWaitScreen();
    this.mWaitStartTimeMs = this.mClock.elapsedRealtime();
    loadAccountAsync();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mAccount != null) {
      paramBundle.putParcelable("account", this.mAccount);
    }
  }
  
  private class LoadAccountTask
    extends ExecutorAsyncTask<Void, AccountConfigurationHelper.AnnotatedAccount>
  {
    public LoadAccountTask(Executor paramExecutor1, Executor paramExecutor2)
    {
      super(paramExecutor2);
    }
    
    @Nullable
    protected AccountConfigurationHelper.AnnotatedAccount doInBackground(Void... paramVarArgs)
    {
      Account localAccount = SetupWizardOptInIntroActivity.this.mLoginHelper.getAccount();
      if (localAccount == null) {}
      List localList;
      do
      {
        return null;
        localList = SetupWizardOptInIntroActivity.this.mAccountConfigurationHelper.updateAccountConfigurations(new Account[] { localAccount });
      } while (localList.isEmpty());
      return (AccountConfigurationHelper.AnnotatedAccount)localList.get(0);
    }
    
    protected void onPostExecute(@Nullable AccountConfigurationHelper.AnnotatedAccount paramAnnotatedAccount)
    {
      if ((paramAnnotatedAccount == null) || (!paramAnnotatedAccount.isNowEnabled()))
      {
        SetupWizardOptInIntroActivity.this.scheduleFinish();
        return;
      }
      SetupWizardOptInIntroActivity.access$402(SetupWizardOptInIntroActivity.this, paramAnnotatedAccount.getAccount());
      SetupWizardOptInIntroActivity.this.scheduleIntroScreen();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.tg.SetupWizardOptInIntroActivity
 * JD-Core Version:    0.7.0.1
 */