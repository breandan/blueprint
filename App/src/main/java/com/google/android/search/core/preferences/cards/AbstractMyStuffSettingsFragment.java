package com.google.android.search.core.preferences.cards;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import com.google.android.velvet.Help;

public abstract class AbstractMyStuffSettingsFragment
  extends CardSettingsFragment
{
  private static final String HELP_CONTEXT = "profile";
  
  public static void addOrFixHelpMenuItem(Context paramContext, Menu paramMenu, String paramString)
  {
    if (paramMenu.findItem(2131297282) == null)
    {
      new Help(paramContext).addHelpMenuItem(paramMenu, paramString);
      return;
    }
    new Help(paramContext).setHelpMenuItemIntent(paramMenu, paramString);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setHasOptionsMenu(true);
  }
  
  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    addOrFixHelpMenuItem(getActivity().getApplicationContext(), paramMenu, "profile");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.AbstractMyStuffSettingsFragment
 * JD-Core Version:    0.7.0.1
 */