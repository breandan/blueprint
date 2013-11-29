package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.velvet.presenter.MainContentPresenter.Transaction;
import com.google.android.voicesearch.ui.ActionEditorView;
import com.google.android.voicesearch.ui.ActionEditorView.ActionEditorListener;
import com.google.common.base.Preconditions;

public abstract class AbstractCardView<CONTROL extends AbstractCardController>
  extends FrameLayout
  implements BaseCardUi
{
  private final String TAG = "AbstractCardView." + getClass().getSimpleName();
  private ActionEditorView mActionEditorView;
  private CONTROL mController;
  
  public AbstractCardView(Context paramContext)
  {
    super(paramContext);
    addView(onCreateView());
  }
  
  public static void clearTextViews(TextView... paramVarArgs)
  {
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++) {
      paramVarArgs[j].setText("");
    }
  }
  
  private View onCreateView()
  {
    View localView = onCreateView(getContext(), (LayoutInflater)getContext().getSystemService("layout_inflater"), this, null);
    localView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener()
    {
      public void onViewAttachedToWindow(View paramAnonymousView)
      {
        AbstractCardView.this.handleAttach();
      }
      
      public void onViewDetachedFromWindow(View paramAnonymousView)
      {
        Log.i(AbstractCardView.this.TAG, "#onViewDetachedFromWindow");
        AbstractCardView.this.handleDetach();
      }
    });
    return localView;
  }
  
  public void cancelCountDownAnimation()
  {
    this.mActionEditorView.cancelCountDownAnimation();
  }
  
  protected ActionEditorView createActionEditor(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, int paramInt)
  {
    this.mActionEditorView = ((ActionEditorView)paramLayoutInflater.inflate(2130968576, paramViewGroup, false));
    this.mActionEditorView.setMainContent(paramInt);
    return this.mActionEditorView;
  }
  
  protected void disableActionEditor(int paramInt)
  {
    Preconditions.checkNotNull(this.mActionEditorView);
    this.mActionEditorView.disable(paramInt);
  }
  
  public void dismissed()
  {
    this.mController.dismissed();
  }
  
  public CONTROL getController()
  {
    return (AbstractCardController)Preconditions.checkNotNull(this.mController);
  }
  
  public void handleAttach()
  {
    if (this.mController != null)
    {
      if (this.mActionEditorView != null) {
        this.mActionEditorView.setActionEditorListener(new ActionEditorView.ActionEditorListener()
        {
          public void onBailOut()
          {
            AbstractCardView.this.mController.bailOut();
          }
          
          public void onCancel()
          {
            AbstractCardView.this.mController.cancel();
          }
          
          public void onCancelCountdown()
          {
            AbstractCardView.this.mController.cancelCountDownByUser();
          }
          
          public void onExecute(int paramAnonymousInt)
          {
            AbstractCardView.this.handleConfirmation(paramAnonymousInt);
          }
        });
      }
      this.mController.attach(this);
    }
  }
  
  protected void handleConfirmation(int paramInt)
  {
    if (paramInt == 1)
    {
      this.mController.bailOut();
      return;
    }
    this.mController.executeAction(false);
  }
  
  public void handleDetach()
  {
    if (this.mController != null) {
      this.mController.detach();
    }
  }
  
  public abstract View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle);
  
  protected void onWindowVisibilityChanged(int paramInt)
  {
    super.onWindowVisibilityChanged(paramInt);
    if (paramInt != 0) {
      getController().cancelCountDown();
    }
  }
  
  public void post(MainContentPresenter.Transaction paramTransaction)
  {
    getController().postTransaction(paramTransaction);
  }
  
  public void setConfirmIcon(int paramInt)
  {
    this.mActionEditorView.setConfirmIcon(paramInt);
  }
  
  public void setConfirmTag(int paramInt)
  {
    int i = 1;
    if ((paramInt == 0) || (paramInt == i) || (paramInt >= 100)) {}
    for (;;)
    {
      Preconditions.checkArgument(i);
      this.mActionEditorView.setConfirmTag(paramInt);
      return;
      i = 0;
    }
  }
  
  public void setConfirmText(int paramInt)
  {
    this.mActionEditorView.setConfirmText(paramInt);
  }
  
  public void setConfirmText(int paramInt, Object... paramVarArgs)
  {
    this.mActionEditorView.setConfirmText(getContext().getString(paramInt, paramVarArgs));
  }
  
  public final void setController(CONTROL paramCONTROL)
  {
    this.mController = paramCONTROL;
    if (getWindowToken() != null)
    {
      Log.i(this.TAG, "#handleAttach - setController");
      handleAttach();
    }
  }
  
  public void setFollowOnPrompt(CharSequence paramCharSequence)
  {
    if (this.mActionEditorView != null) {
      this.mActionEditorView.setFollowOnPrompt(paramCharSequence);
    }
  }
  
  public void setFollowOnPromptState(boolean paramBoolean)
  {
    if (this.mActionEditorView != null) {
      this.mActionEditorView.setFollowOnPromptState(paramBoolean);
    }
  }
  
  public void showConfirmBar(boolean paramBoolean)
  {
    this.mActionEditorView.showCountDownView(paramBoolean);
  }
  
  public void showDisabled()
  {
    if (this.mActionEditorView != null)
    {
      disableActionEditor(2131363677);
      return;
    }
    Log.w(this.TAG, "Can't disable UI.");
  }
  
  protected void showTextIfNonEmpty(TextView paramTextView, CharSequence paramCharSequence)
  {
    paramTextView.setText(paramCharSequence);
    if (TextUtils.isEmpty(paramCharSequence)) {}
    for (int i = 8;; i = 0)
    {
      paramTextView.setVisibility(i);
      return;
    }
  }
  
  public void showToast(int paramInt)
  {
    Toast.makeText(getContext(), paramInt, 0).show();
  }
  
  public void startCountDownAnimation(long paramLong)
  {
    this.mActionEditorView.startCountDownAnimation(paramLong);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.AbstractCardView
 * JD-Core Version:    0.7.0.1
 */