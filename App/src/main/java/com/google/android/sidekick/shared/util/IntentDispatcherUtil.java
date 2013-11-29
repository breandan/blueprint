package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.Intent;
import com.google.geo.sidekick.Sidekick.Question;
import javax.annotation.Nullable;

public class IntentDispatcherUtil
{
  private static Intent createDispatcherIntent(String paramString)
  {
    Intent localIntent = new Intent(paramString);
    localIntent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.velvet.ui.VelvetIntentDispatcher");
    return localIntent;
  }
  
  public static void dispatchIntent(Context paramContext, String paramString)
  {
    paramContext.startActivity(createDispatcherIntent(paramString));
  }
  
  public static void dispatchTrainingClosetIntent(Context paramContext, @Nullable Sidekick.Question paramQuestion)
  {
    Intent localIntent = createDispatcherIntent("com.google.android.googlequicksearchbox.TRAINING_CLOSET");
    if (paramQuestion != null) {
      localIntent.putExtra("com.google.android.googlequicksearchbox.EXTRA_TRAINING_CLOSET_QUESTION", paramQuestion.toByteArray());
    }
    paramContext.startActivity(localIntent);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.IntentDispatcherUtil
 * JD-Core Version:    0.7.0.1
 */