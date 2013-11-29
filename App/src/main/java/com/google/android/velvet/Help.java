package com.google.android.velvet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import com.google.android.gsf.HelpUrl;
import com.google.android.shared.util.IntentStarter;
import com.google.common.base.Supplier;

public class Help
{
  private final Context mContext;
  
  public Help(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  private void addClickListenerToMenuItem(Menu paramMenu, int paramInt, Supplier<String> paramSupplier, IntentStarter paramIntentStarter, boolean paramBoolean)
  {
    MenuItem localMenuItem = paramMenu.findItem(paramInt);
    localMenuItem.setOnMenuItemClickListener(new HelpClickListener(paramSupplier, paramIntentStarter));
    if (paramBoolean) {
      localMenuItem.setShowAsAction(2);
    }
  }
  
  private void addMenuItem(Menu paramMenu, int paramInt1, int paramInt2, Supplier<String> paramSupplier, IntentStarter paramIntentStarter, boolean paramBoolean)
  {
    if (HelpUrl.getHelpUrl(this.mContext, "notused") == null) {
      return;
    }
    new MenuInflater(this.mContext).inflate(paramInt1, paramMenu);
    addClickListenerToMenuItem(paramMenu, paramInt2, paramSupplier, paramIntentStarter, paramBoolean);
  }
  
  public void addHelpMenuItem(Menu paramMenu, Supplier<String> paramSupplier, IntentStarter paramIntentStarter)
  {
    addHelpMenuItem(paramMenu, paramSupplier, paramIntentStarter, false);
  }
  
  public void addHelpMenuItem(Menu paramMenu, Supplier<String> paramSupplier, IntentStarter paramIntentStarter, boolean paramBoolean)
  {
    addMenuItem(paramMenu, 2131886082, 2131297282, paramSupplier, paramIntentStarter, paramBoolean);
  }
  
  public void addHelpMenuItem(Menu paramMenu, String paramString)
  {
    new MenuInflater(this.mContext).inflate(2131886082, paramMenu);
    setHelpMenuItemIntent(paramMenu, paramString);
  }
  
  public Intent getHelpIntent(String paramString)
  {
    Uri localUri = getHelpUrl(paramString);
    if (localUri == null) {
      return null;
    }
    return new Intent("android.intent.action.VIEW", localUri);
  }
  
  public Uri getHelpUrl(String paramString)
  {
    String str = "gqsb_" + paramString;
    return HelpUrl.getHelpUrl(this.mContext, str);
  }
  
  public void setHelpMenuItemIntent(Menu paramMenu, String paramString)
  {
    Intent localIntent = getHelpIntent(paramString);
    if (localIntent != null) {
      paramMenu.findItem(2131297282).setIntent(localIntent);
    }
  }
  
  private class HelpClickListener
    implements MenuItem.OnMenuItemClickListener
  {
    private final Supplier<String> mHelpContextSupplier;
    private final IntentStarter mIntentStarter;
    
    public HelpClickListener(IntentStarter paramIntentStarter)
    {
      this.mHelpContextSupplier = paramIntentStarter;
      Object localObject;
      this.mIntentStarter = localObject;
    }
    
    public boolean onMenuItemClick(MenuItem paramMenuItem)
    {
      Intent localIntent = Help.this.getHelpIntent((String)this.mHelpContextSupplier.get());
      if (localIntent == null) {
        return false;
      }
      this.mIntentStarter.startActivity(new Intent[] { localIntent });
      return true;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.Help
 * JD-Core Version:    0.7.0.1
 */