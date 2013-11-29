package com.google.android.voicesearch.ime.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.voicesearch.ime.ImeUtils;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Dialect;

public class LanguageSpinner
  extends Spinner
{
  private Callback mCallback;
  private GstaticConfiguration.Dialect mCurrentDialect;
  private GstaticConfiguration.Dialect[] mDialects;
  private AlertDialog mDialog;
  
  public LanguageSpinner(Context paramContext)
  {
    super(paramContext);
  }
  
  public LanguageSpinner(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }
  
  public LanguageSpinner(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public LanguageSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public LanguageSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private GstaticConfiguration.Dialect getCurrentDialect(String paramString, GstaticConfiguration.Dialect[] paramArrayOfDialect)
  {
    int i = paramArrayOfDialect.length;
    for (int j = 0; j < i; j++)
    {
      GstaticConfiguration.Dialect localDialect = paramArrayOfDialect[j];
      if (localDialect.getBcp47Locale().equals(paramString)) {
        return localDialect;
      }
    }
    return paramArrayOfDialect[0];
  }
  
  public void onDetachedFromWindow()
  {
    if (this.mDialog != null) {
      this.mDialog.hide();
    }
    super.onDetachedFromWindow();
  }
  
  public boolean performClick()
  {
    if (this.mDialects == null)
    {
      Log.e("VS.LanguageSpinner", "#performClick: missing call to setLanguages");
      return true;
    }
    EventLogger.recordClientEvent(67);
    this.mCallback.onDisplayDialectSelectionPopup();
    CharSequence[] arrayOfCharSequence1 = SpokenLanguageUtils.getDisplayNames(this.mDialects);
    CharSequence[] arrayOfCharSequence2 = new CharSequence[1 + arrayOfCharSequence1.length];
    System.arraycopy(arrayOfCharSequence1, 0, arrayOfCharSequence2, 0, arrayOfCharSequence1.length);
    if (arrayOfCharSequence1.length == 1) {}
    for (int i = 2131363572;; i = 2131363573)
    {
      arrayOfCharSequence2[arrayOfCharSequence1.length] = getContext().getString(i);
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext()).setItems(arrayOfCharSequence2, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          if (LanguageSpinner.this.mCallback != null)
          {
            if (paramAnonymousInt < LanguageSpinner.this.mDialects.length) {
              LanguageSpinner.this.mCallback.onUpdateDialect(LanguageSpinner.this.mDialects[paramAnonymousInt]);
            }
          }
          else {
            return;
          }
          ImeUtils.showImeSubtypeSetting(LanguageSpinner.this.getContext());
        }
      }).setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          if (LanguageSpinner.this.mCallback != null) {
            LanguageSpinner.this.mCallback.onUpdateDialect(LanguageSpinner.this.mCurrentDialect);
          }
        }
      });
      localBuilder.setTitle(getContext().getString(2131363571));
      this.mDialog = localBuilder.create();
      Window localWindow = this.mDialog.getWindow();
      WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
      localLayoutParams.token = getWindowToken();
      localLayoutParams.type = 1003;
      localWindow.setAttributes(localLayoutParams);
      localWindow.addFlags(131072);
      this.mDialog.show();
      return true;
    }
  }
  
  public void setCallback(Callback paramCallback)
  {
    this.mCallback = paramCallback;
  }
  
  public void setLanguages(String paramString, GstaticConfiguration.Dialect[] paramArrayOfDialect)
  {
    if (paramArrayOfDialect.length > 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mCurrentDialect = getCurrentDialect(paramString, paramArrayOfDialect);
      this.mDialects = paramArrayOfDialect;
      Context localContext = getContext();
      String[] arrayOfString = new String[1];
      arrayOfString[0] = this.mCurrentDialect.getDisplayName();
      setAdapter(new ArrayAdapter(localContext, 2130968723, arrayOfString));
      return;
    }
  }
  
  public static abstract interface Callback
  {
    public abstract void onDisplayDialectSelectionPopup();
    
    public abstract void onUpdateDialect(GstaticConfiguration.Dialect paramDialect);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.ime.view.LanguageSpinner
 * JD-Core Version:    0.7.0.1
 */