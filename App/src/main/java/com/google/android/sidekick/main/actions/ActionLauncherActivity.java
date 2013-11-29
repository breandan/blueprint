package com.google.android.sidekick.main.actions;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.utils.ProtoBufUtils;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.fragments.VoiceActionDialogFragment;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import javax.annotation.Nullable;

public class ActionLauncherActivity
  extends Activity
  implements FragmentManager.OnBackStackChangedListener
{
  private static final String TAG = Tag.getTag(ActionLauncherActivity.class);
  private int mStartBackStackDepth;
  
  private Fragment getReminderEditOrDeleteFragment(Intent paramIntent)
  {
    Bundle localBundle = paramIntent.getExtras();
    Sidekick.Entry localEntry = (Sidekick.Entry)ProtoUtils.getProtoExtra(localBundle, "entry", Sidekick.Entry.class);
    Sidekick.Action localAction1 = (Sidekick.Action)ProtoUtils.getProtoExtra(localBundle, "action", Sidekick.Action.class);
    Sidekick.Action localAction2 = (Sidekick.Action)ProtoUtils.getProtoExtra(localBundle, "delete_action", Sidekick.Action.class);
    EditReminderDialogFragment localEditReminderDialogFragment;
    if ((localAction1 != null) && (localEntry.getReminderEntry() != null)) {
      localEditReminderDialogFragment = EditReminderDialogFragment.newInstance(null, localEntry);
    }
    do
    {
      return localEditReminderDialogFragment;
      localEditReminderDialogFragment = null;
    } while (localAction2 == null);
    return DeleteReminderDialogFragment.newInstance(null, localEntry);
  }
  
  private void launchReminderSmartActionFragment(Intent paramIntent, final int paramInt)
  {
    final Sidekick.Entry localEntry = (Sidekick.Entry)ProtoUtils.getProtoExtra(paramIntent.getExtras(), "entry", Sidekick.Entry.class);
    VelvetServices localVelvetServices = VelvetServices.get();
    final ContactLookup localContactLookup = localVelvetServices.getVoiceSearchServices().getContactLookup();
    new AsyncTask()
    {
      protected VoiceAction doInBackground(Void... paramAnonymousVarArgs)
      {
        return this.val$reminderSmartActionUtil.extractVoiceAction(localEntry, paramInt, localContactLookup);
      }
      
      protected void onPostExecute(@Nullable VoiceAction paramAnonymousVoiceAction)
      {
        if (paramAnonymousVoiceAction == null)
        {
          Log.e(ActionLauncherActivity.TAG, "Failed to extract voice action:\n" + ProtoBufUtils.toString(localEntry));
          ActionLauncherActivity.this.finish();
          return;
        }
        VoiceActionDialogFragment localVoiceActionDialogFragment = VoiceActionDialogFragment.newInstance(paramAnonymousVoiceAction);
        ActionLauncherActivity.this.showFragment(localVoiceActionDialogFragment, "reminder_smart_action_dialog");
      }
    }.execute(new Void[0]);
  }
  
  private void setUpFromIntent(Intent paramIntent)
  {
    this.mStartBackStackDepth = getFragmentManager().getBackStackEntryCount();
    Object localObject = null;
    String str = null;
    int i;
    if (paramIntent != null)
    {
      i = paramIntent.getIntExtra("action_type", -1);
      localObject = null;
      str = null;
      switch (i)
      {
      }
    }
    while (localObject == null)
    {
      Log.e(TAG, "Failed to create a fragment for Intent " + paramIntent);
      finish();
      return;
      localObject = DeletePlaceDialogFragment.newInstance(paramIntent);
      str = "delete_place_dialog";
      continue;
      localObject = RenamePlaceDialogFragment.newInstance(paramIntent);
      str = "rename_place_dialog";
      continue;
      localObject = EditHomeWorkDialogFragment.newInstance(this, paramIntent);
      str = "edit_home_work";
      continue;
      localObject = EditHomeWorkWorkerFragment.newInstance(paramIntent);
      str = "confirm_home_work";
      continue;
      localObject = getReminderEditOrDeleteFragment(paramIntent);
      str = "reminder_edit_or_delete";
      continue;
      launchReminderSmartActionFragment(paramIntent, i);
      return;
    }
    Preconditions.checkNotNull(str);
    showFragment((Fragment)localObject, str);
  }
  
  private void showFragment(@Nullable Fragment paramFragment, String paramString)
  {
    FragmentTransaction localFragmentTransaction;
    if (paramFragment != null)
    {
      localFragmentTransaction = getFragmentManager().beginTransaction();
      localFragmentTransaction.addToBackStack(paramString);
      if ((paramFragment instanceof DialogFragment)) {
        ((DialogFragment)paramFragment).show(localFragmentTransaction, paramString);
      }
    }
    else
    {
      return;
    }
    localFragmentTransaction.add(paramFragment, paramString);
    localFragmentTransaction.commit();
  }
  
  @Nullable
  public VoiceAction getCurrentVoiceAction()
  {
    VoiceActionDialogFragment localVoiceActionDialogFragment = (VoiceActionDialogFragment)getFragmentManager().findFragmentByTag("reminder_smart_action_dialog");
    if (localVoiceActionDialogFragment != null) {
      return localVoiceActionDialogFragment.getVoiceAction();
    }
    return null;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    VoiceActionDialogFragment localVoiceActionDialogFragment = (VoiceActionDialogFragment)getFragmentManager().findFragmentByTag("reminder_smart_action_dialog");
    if (localVoiceActionDialogFragment != null)
    {
      localVoiceActionDialogFragment.onActivityResult(paramInt1, paramInt2, paramIntent);
      return;
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }
  
  public void onBackStackChanged()
  {
    if (getFragmentManager().getBackStackEntryCount() == this.mStartBackStackDepth) {
      finish();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    if (paramBundle == null) {
      setUpFromIntent(getIntent());
    }
    getFragmentManager().addOnBackStackChangedListener(this);
    super.onCreate(paramBundle);
  }
  
  protected void onDestroy()
  {
    getFragmentManager().removeOnBackStackChangedListener(this);
    super.onDestroy();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.ActionLauncherActivity
 * JD-Core Version:    0.7.0.1
 */