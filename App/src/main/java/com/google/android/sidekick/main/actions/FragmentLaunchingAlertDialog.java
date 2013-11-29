package com.google.android.sidekick.main.actions;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.google.common.base.Preconditions;

public class FragmentLaunchingAlertDialog
  extends Dialog
  implements FragmentManager.OnBackStackChangedListener, DialogInterface.OnCancelListener, DialogInterface.OnShowListener
{
  private final DialogInterface.OnClickListener NO_OP_CLICK_LISTENER = new DialogInterface.OnClickListener()
  {
    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {}
  };
  private AlertDialog mAlertDialog;
  private final AlertDialog.Builder mAlertDialogBuilder;
  private boolean mCreated;
  private final FragmentManager mFragmentManager;
  private View.OnClickListener mNegativeClickListener;
  private View.OnClickListener mNeutralClickListener;
  private DialogInterface.OnShowListener mOnShowListener;
  private View.OnClickListener mPositiveClickListener;
  private int mStartBackStackDepth;
  private int mWindowSoftInputMode = 0;
  
  protected FragmentLaunchingAlertDialog(Context paramContext, FragmentManager paramFragmentManager, int paramInt)
  {
    super(paramContext);
    this.mFragmentManager = paramFragmentManager;
    this.mAlertDialogBuilder = new AlertDialog.Builder(paramContext).setMessage(paramInt);
  }
  
  public void dismiss()
  {
    if (this.mCreated) {
      this.mAlertDialog.dismiss();
    }
    super.dismiss();
  }
  
  public View findViewById(int paramInt)
  {
    View localView = super.findViewById(paramInt);
    if ((localView == null) && (this.mCreated)) {
      localView = this.mAlertDialog.findViewById(paramInt);
    }
    return localView;
  }
  
  public Button getButton(int paramInt)
  {
    if (!this.mCreated) {
      return null;
    }
    return this.mAlertDialog.getButton(paramInt);
  }
  
  public void hide()
  {
    if (this.mCreated) {
      this.mAlertDialog.hide();
    }
    super.hide();
  }
  
  public void onBackStackChanged()
  {
    if (this.mFragmentManager.getBackStackEntryCount() == this.mStartBackStackDepth) {
      dismiss();
    }
  }
  
  public void onCancel(DialogInterface paramDialogInterface)
  {
    cancel();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!this.mCreated)
    {
      TextView localTextView = new TextView(getContext());
      localTextView.setVisibility(8);
      setContentView(localTextView);
      getWindow().setLayout(1, 1);
      this.mAlertDialog = this.mAlertDialogBuilder.create();
      if (this.mWindowSoftInputMode != 0) {
        this.mAlertDialog.getWindow().setSoftInputMode(this.mWindowSoftInputMode);
      }
      this.mAlertDialog.setOnCancelListener(this);
      this.mCreated = true;
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (this.mCreated) {
      return this.mAlertDialog.onKeyDown(paramInt, paramKeyEvent);
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if (this.mCreated) {
      return this.mAlertDialog.onKeyUp(paramInt, paramKeyEvent);
    }
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  public void onShow(DialogInterface paramDialogInterface)
  {
    this.mStartBackStackDepth = this.mFragmentManager.getBackStackEntryCount();
    this.mFragmentManager.addOnBackStackChangedListener(this);
    if (this.mOnShowListener != null) {
      this.mOnShowListener.onShow(paramDialogInterface);
    }
  }
  
  protected void onStart()
  {
    super.onStart();
    super.setOnShowListener(this);
  }
  
  protected void onStop()
  {
    super.onStop();
    this.mFragmentManager.removeOnBackStackChangedListener(this);
  }
  
  public void setNegativeButton(int paramInt, View.OnClickListener paramOnClickListener)
  {
    if (!this.mCreated) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Cannot set button. Dialog already created.");
      this.mAlertDialogBuilder.setNegativeButton(paramInt, this.NO_OP_CLICK_LISTENER);
      this.mNegativeClickListener = paramOnClickListener;
      return;
    }
  }
  
  public void setNeutralButton(int paramInt, View.OnClickListener paramOnClickListener)
  {
    if (!this.mCreated) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Cannot set button. Dialog already created.");
      this.mAlertDialogBuilder.setNeutralButton(paramInt, this.NO_OP_CLICK_LISTENER);
      this.mNeutralClickListener = paramOnClickListener;
      return;
    }
  }
  
  public void setOnShowListener(DialogInterface.OnShowListener paramOnShowListener)
  {
    this.mOnShowListener = paramOnShowListener;
  }
  
  public void setPositiveButton(int paramInt, View.OnClickListener paramOnClickListener)
  {
    if (!this.mCreated) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Cannot set button. Dialog already created.");
      this.mAlertDialogBuilder.setPositiveButton(paramInt, this.NO_OP_CLICK_LISTENER);
      this.mPositiveClickListener = paramOnClickListener;
      return;
    }
  }
  
  public void setView(View paramView)
  {
    if (!this.mCreated) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Cannot set view. Dialog already created.");
      this.mAlertDialogBuilder.setView(paramView);
      return;
    }
  }
  
  public void setWindowSoftInputMode(int paramInt)
  {
    if (!this.mCreated) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Cannot set softInputMode. Dialog already created.");
      this.mWindowSoftInputMode = paramInt;
      return;
    }
  }
  
  public void show()
  {
    super.show();
    this.mAlertDialog.show();
    if (this.mPositiveClickListener != null) {
      this.mAlertDialog.getButton(-1).setOnClickListener(this.mPositiveClickListener);
    }
    if (this.mNegativeClickListener != null) {
      this.mAlertDialog.getButton(-2).setOnClickListener(this.mNegativeClickListener);
    }
    if (this.mNeutralClickListener != null) {
      this.mAlertDialog.getButton(-3).setOnClickListener(this.mNeutralClickListener);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.FragmentLaunchingAlertDialog
 * JD-Core Version:    0.7.0.1
 */