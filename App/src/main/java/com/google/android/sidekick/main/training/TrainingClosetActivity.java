package com.google.android.sidekick.main.training;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.preferences.NowTrainingQuestionsFragment;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.search.core.preferences.cards.MyPlacesSettingsFragment;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.velvet.VelvetServices;
import java.util.Locale;
import javax.annotation.Nullable;

public class TrainingClosetActivity
  extends Activity
{
  private void showPlacesScreen(boolean paramBoolean)
  {
    startFragment(new MyPlacesSettingsFragment(), paramBoolean);
  }
  
  private void startFragment(Fragment paramFragment, boolean paramBoolean)
  {
    FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
    if (paramBoolean)
    {
      localFragmentTransaction.replace(2131296821, paramFragment);
      localFragmentTransaction.addToBackStack(null);
    }
    for (;;)
    {
      localFragmentTransaction.commit();
      return;
      localFragmentTransaction.add(2131296821, paramFragment, "TrainingClosetActivity.BaseFragment");
    }
  }
  
  private void startFragmentWithArgs(Bundle paramBundle, boolean paramBoolean)
  {
    startFragment(Fragment.instantiate(this, NowTrainingQuestionsFragment.class.getCanonicalName(), paramBundle), paramBoolean);
  }
  
  public SharedPreferences getSharedPreferences(String paramString, int paramInt)
  {
    return VelvetServices.get().getCoreServices().getPredictiveCardsPreferences().getWorkingPreferences();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    showHeader(null, null);
    setContentView(2130968763);
    Bundle localBundle1 = new Bundle();
    Bundle localBundle2 = getIntent().getExtras();
    if (localBundle2 != null) {
      if (localBundle2.getBoolean("com.google.android.apps.sidekick.training.EXTRA_SHOW_PLACES")) {
        if (getFragmentManager().findFragmentByTag("TrainingClosetActivity.BaseFragment") == null) {
          showPlacesScreen(false);
        }
      }
    }
    while (getFragmentManager().findFragmentByTag("TrainingClosetActivity.BaseFragment") != null)
    {
      return;
      localBundle1.putByteArray("com.google.android.search.core.preferences.ARGUMENT_QUESTION", localBundle2.getByteArray("com.google.android.apps.sidekick.training.EXTRA_TARGET_QUESTION"));
    }
    startFragmentWithArgs(localBundle1, false);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getItemId() == 16908332)
    {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  public void onPause()
  {
    SidekickInjector localSidekickInjector = VelvetServices.get().getSidekickInjector();
    TrainingQuestionManager localTrainingQuestionManager = localSidekickInjector.getTrainingQuestionManager();
    EntryProvider localEntryProvider = localSidekickInjector.getEntryProvider();
    if (localTrainingQuestionManager.isDirty()) {
      localEntryProvider.invalidateWithImmediateRefresh();
    }
    for (;;)
    {
      super.onPause();
      return;
      localEntryProvider.refreshNowIfDelayedRefreshInFlight();
    }
  }
  
  public void showClosetScreen(Bundle paramBundle)
  {
    startFragmentWithArgs(paramBundle, true);
  }
  
  public void showHeader(@Nullable String paramString1, @Nullable String paramString2)
  {
    ActionBar localActionBar = getActionBar();
    if (localActionBar != null)
    {
      if (paramString1 != null) {
        paramString1 = paramString1.toUpperCase(Locale.getDefault());
      }
      localActionBar.setTitle(paramString1);
    }
  }
  
  public void showPlacesScreen()
  {
    showPlacesScreen(true);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.training.TrainingClosetActivity
 * JD-Core Version:    0.7.0.1
 */