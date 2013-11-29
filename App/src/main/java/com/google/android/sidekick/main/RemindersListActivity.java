package com.google.android.sidekick.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import java.util.Locale;

public class RemindersListActivity
  extends Activity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968763);
    ActionBar localActionBar = getActionBar();
    if (localActionBar != null) {
      localActionBar.setTitle(getString(2131363134).toUpperCase(Locale.getDefault()));
    }
    FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
    Fragment localFragment = getFragmentManager().findFragmentByTag(RemindersListFragment.TAG);
    if (localFragment == null) {
      localFragmentTransaction.add(2131296821, Fragment.instantiate(this, RemindersListFragment.class.getCanonicalName()), RemindersListFragment.TAG);
    }
    for (;;)
    {
      localFragmentTransaction.commit();
      return;
      localFragmentTransaction.replace(2131296821, localFragment, RemindersListFragment.TAG);
    }
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
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.RemindersListActivity
 * JD-Core Version:    0.7.0.1
 */