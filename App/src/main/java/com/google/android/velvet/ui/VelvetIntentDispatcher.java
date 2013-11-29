package com.google.android.velvet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.search.core.preferences.cards.TrafficCardSettingsFragment;
import com.google.android.sidekick.main.RemindersListActivity;
import com.google.android.sidekick.main.training.TrainingClosetActivity;
import com.google.android.velvet.ui.settings.SettingsActivity;

public class VelvetIntentDispatcher
  extends Activity
{
  private void showMyPlaces()
  {
    Intent localIntent = new Intent(this, TrainingClosetActivity.class);
    localIntent.putExtra("com.google.android.apps.sidekick.training.EXTRA_SHOW_PLACES", true);
    startActivity(localIntent);
  }
  
  private void showRemindersList()
  {
    startActivity(new Intent(this, RemindersListActivity.class));
  }
  
  private void showTrafficCardSettings()
  {
    Intent localIntent = new Intent(this, SettingsActivity.class);
    localIntent.putExtra(":android:show_fragment", TrafficCardSettingsFragment.class.getName());
    startActivity(localIntent);
  }
  
  private void showTrainingQuestionCloset()
  {
    Intent localIntent = new Intent(this, TrainingClosetActivity.class);
    if (getIntent().getExtras() != null)
    {
      byte[] arrayOfByte = getIntent().getExtras().getByteArray("com.google.android.googlequicksearchbox.EXTRA_TRAINING_CLOSET_QUESTION");
      if (arrayOfByte != null) {
        localIntent.putExtra("com.google.android.apps.sidekick.training.EXTRA_TARGET_QUESTION", arrayOfByte);
      }
    }
    startActivity(localIntent);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    for (;;)
    {
      String str;
      try
      {
        Intent localIntent = getIntent();
        if (localIntent == null) {
          return;
        }
        str = localIntent.getAction();
        if ("com.google.android.googlequicksearchbox.MY_REMINDERS".equals(str))
        {
          showRemindersList();
          return;
        }
        if ("com.google.android.googlequicksearchbox.TRAINING_CLOSET".equals(str))
        {
          showTrainingQuestionCloset();
          continue;
        }
        if (!"com.google.android.googlequicksearchbox.MY_PLACES".equals(str)) {
          break label85;
        }
      }
      finally
      {
        finish();
      }
      showMyPlaces();
      continue;
      label85:
      if ("com.google.android.googlequicksearchbox.TRAFFIC_CARD_SETTINGS".equals(str)) {
        showTrafficCardSettings();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.ui.VelvetIntentDispatcher
 * JD-Core Version:    0.7.0.1
 */