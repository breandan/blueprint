package com.google.android.search.core.preferences;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.shared.util.Consumer;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.ui.InAppWebPageActivity;

public class ManageSearchHistoryHelper
  extends ProgressDialog
  implements DialogInterface.OnCancelListener
{
  private boolean mCancelled;
  private final GsaConfigFlags mFlags;
  private final LoginHelper mLoginHelper;
  private final SearchUrlHelper mUrlHelper;
  
  public ManageSearchHistoryHelper(Context paramContext, GsaConfigFlags paramGsaConfigFlags, LoginHelper paramLoginHelper, SearchUrlHelper paramSearchUrlHelper)
  {
    super(paramContext);
    this.mFlags = paramGsaConfigFlags;
    this.mLoginHelper = paramLoginHelper;
    this.mUrlHelper = paramSearchUrlHelper;
    String str = this.mLoginHelper.getAccountName();
    setTitle(2131363176);
    setMessage(getContext().getResources().getString(2131363177, new Object[] { str }));
    setIndeterminate(true);
    setCancelable(true);
    setOnCancelListener(this);
    setButton(-2, getContext().getResources().getString(2131363178), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        ManageSearchHistoryHelper.this.cancelled();
      }
    });
  }
  
  private void cancelled()
  {
    this.mCancelled = true;
    dismiss();
  }
  
  public void onCancel(DialogInterface paramDialogInterface)
  {
    cancelled();
  }
  
  public void start()
  {
    show();
    final Uri localUri = this.mUrlHelper.formatUrlForSearchDomain(this.mFlags.getManageSearchHistoryUrlFormat());
    this.mLoginHelper.getGaiaWebLoginLink(localUri, "hist", new Consumer()
    {
      public boolean consume(Uri paramAnonymousUri)
      {
        if (ManageSearchHistoryHelper.this.mCancelled) {
          return false;
        }
        if (paramAnonymousUri == null) {
          paramAnonymousUri = localUri;
        }
        ManageSearchHistoryHelper.this.dismiss();
        ActivityHelper localActivityHelper = VelvetServices.get().getSidekickInjector().getActivityHelper();
        if (ManageSearchHistoryHelper.this.mFlags.isSearchHistoryInAppEnabled())
        {
          Intent localIntent = new Intent("android.intent.action.VIEW", paramAnonymousUri).setClass(ManageSearchHistoryHelper.this.getContext(), InAppWebPageActivity.class);
          localActivityHelper.safeStartActivity(ManageSearchHistoryHelper.this.getContext(), localIntent);
          return true;
        }
        localActivityHelper.safeViewUri(ManageSearchHistoryHelper.this.getContext(), paramAnonymousUri, true);
        return true;
      }
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.ManageSearchHistoryHelper
 * JD-Core Version:    0.7.0.1
 */