package com.google.android.voicesearch.fragments.reminders;

import android.content.DialogInterface.OnCancelListener;
import com.android.recurrencepicker.EventRecurrence;
import com.google.android.speech.callback.SimpleCallback;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;
import java.util.List;
import javax.annotation.Nullable;

public abstract interface EditReminderUi
{
  public abstract void clearEmbeddedAction();
  
  public abstract void pickDate(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void pickLocation(@Nullable String paramString, int paramInt, SimpleCallback<EcoutezStructuredResponse.EcoutezLocalResult> paramSimpleCallback, @Nullable DialogInterface.OnCancelListener paramOnCancelListener);
  
  public abstract void pickTime(int paramInt1, int paramInt2);
  
  public abstract void revertLocationSelection();
  
  public abstract void setCustomDate(long paramLong);
  
  public abstract void setCustomLocation(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult);
  
  public abstract void setCustomTime(long paramLong);
  
  public abstract void setHomeAndWorkAddresses(@Nullable String paramString1, @Nullable String paramString2);
  
  public abstract void setLabel(String paramString);
  
  public abstract void setLocationAlias(int paramInt);
  
  public abstract void setLocationTriggerType(int paramInt);
  
  public abstract void setRecurrence(EventRecurrence paramEventRecurrence);
  
  public abstract void setSymbolicTimeAndAvailableOptions(SymbolicTime paramSymbolicTime, List<SymbolicTime> paramList);
  
  public abstract void setTriggerType(int paramInt);
  
  public abstract void showCustomDate();
  
  public abstract void showCustomLocation();
  
  public abstract void showCustomTime();
  
  public abstract void showDateToday();
  
  public abstract void showDateTomorrow();
  
  public abstract void showEmbeddedPhoneCallAction(String paramString);
  
  public abstract void showToast(int paramInt);
  
  public abstract void updateDateSpinner(long paramLong);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.reminders.EditReminderUi
 * JD-Core Version:    0.7.0.1
 */