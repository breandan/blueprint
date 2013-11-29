package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.recurrencepicker.EventRecurrence;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.voicesearch.fragments.reminders.EditReminderPresenter;
import com.google.android.voicesearch.fragments.reminders.EditReminderView;
import com.google.android.voicesearch.fragments.reminders.SymbolicTime;
import com.google.android.voicesearch.ui.ActionEditorView;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;
import java.util.List;
import javax.annotation.Nullable;

public class SetReminderCard
  extends AbstractCardView<SetReminderController>
  implements SetReminderController.Ui
{
  private EditReminderView mEditReminderView;
  private ActionEditorView mMainContent;
  
  public SetReminderCard(Context paramContext)
  {
    super(paramContext);
  }
  
  public void clearEmbeddedAction()
  {
    this.mEditReminderView.clearEmbeddedAction();
  }
  
  public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mMainContent = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968664);
    this.mEditReminderView = ((EditReminderView)this.mMainContent.findViewById(2131296531));
    this.mMainContent.setContentClickable(false);
    return this.mMainContent;
  }
  
  public void pickDate(int paramInt1, int paramInt2, int paramInt3)
  {
    this.mEditReminderView.pickDate(paramInt1, paramInt2, paramInt3);
  }
  
  public void pickLocation(@Nullable String paramString, int paramInt, SimpleCallback<EcoutezStructuredResponse.EcoutezLocalResult> paramSimpleCallback, @Nullable DialogInterface.OnCancelListener paramOnCancelListener)
  {
    this.mEditReminderView.pickLocation(paramString, paramInt, paramSimpleCallback, paramOnCancelListener);
  }
  
  public void pickTime(int paramInt1, int paramInt2)
  {
    this.mEditReminderView.pickTime(paramInt1, paramInt2);
  }
  
  public void revertLocationSelection()
  {
    this.mEditReminderView.revertLocationSelection();
  }
  
  public void setCustomDate(long paramLong)
  {
    this.mEditReminderView.setCustomDate(paramLong);
  }
  
  public void setCustomLocation(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
  {
    this.mEditReminderView.setCustomLocation(paramEcoutezLocalResult);
  }
  
  public void setCustomTime(long paramLong)
  {
    this.mEditReminderView.setCustomTime(paramLong);
  }
  
  public void setHomeAndWorkAddresses(@Nullable String paramString1, @Nullable String paramString2)
  {
    this.mEditReminderView.setHomeAndWorkAddresses(paramString1, paramString2);
  }
  
  public void setLabel(String paramString)
  {
    ActionEditorView localActionEditorView = this.mMainContent;
    if (!TextUtils.isEmpty(paramString)) {}
    for (boolean bool = true;; bool = false)
    {
      localActionEditorView.setConfirmationEnabled(bool);
      this.mEditReminderView.setLabel(paramString);
      return;
    }
  }
  
  public void setLocationAlias(int paramInt)
  {
    this.mEditReminderView.setLocationAlias(paramInt);
  }
  
  public void setLocationTriggerType(int paramInt)
  {
    this.mEditReminderView.setLocationTriggerType(paramInt);
  }
  
  public void setPresenter(EditReminderPresenter paramEditReminderPresenter)
  {
    this.mEditReminderView.setPresenter(paramEditReminderPresenter);
  }
  
  public void setRecurrence(EventRecurrence paramEventRecurrence)
  {
    this.mEditReminderView.setRecurrence(paramEventRecurrence);
  }
  
  public void setSymbolicTimeAndAvailableOptions(SymbolicTime paramSymbolicTime, List<SymbolicTime> paramList)
  {
    this.mEditReminderView.setSymbolicTimeAndAvailableOptions(paramSymbolicTime, paramList);
  }
  
  public void setTriggerType(int paramInt)
  {
    this.mEditReminderView.setTriggerType(paramInt);
    if (paramInt == 1)
    {
      setConfirmIcon(2130837688);
      setConfirmText(2131363501);
    }
    while (paramInt != 2) {
      return;
    }
    setConfirmIcon(2130837686);
    setConfirmText(2131363502);
  }
  
  public void showCustomDate()
  {
    this.mEditReminderView.showCustomDate();
  }
  
  public void showCustomLocation()
  {
    this.mEditReminderView.showCustomLocation();
  }
  
  public void showCustomTime()
  {
    this.mEditReminderView.showCustomTime();
  }
  
  public void showDateToday()
  {
    this.mEditReminderView.showDateToday();
  }
  
  public void showDateTomorrow()
  {
    this.mEditReminderView.showDateTomorrow();
  }
  
  public void showEmbeddedPhoneCallAction(String paramString)
  {
    this.mEditReminderView.showEmbeddedPhoneCallAction(paramString);
  }
  
  public void showSaving()
  {
    showToast(2131363515);
  }
  
  public void updateDateSpinner(long paramLong)
  {
    this.mEditReminderView.updateDateSpinner(paramLong);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.SetReminderCard
 * JD-Core Version:    0.7.0.1
 */