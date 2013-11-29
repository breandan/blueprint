package com.google.android.search.core.debug;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.common.base.Strings;

public class DebugDialogFragment
  extends DialogFragment
{
  private String mText;
  
  private void share()
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("text/plain");
    localIntent.putExtra("android.intent.extra.TEXT", this.mText);
    startActivity(localIntent);
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    Dialog localDialog = super.onCreateDialog(paramBundle);
    localDialog.requestWindowFeature(1);
    return localDialog;
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    if ((paramBundle != null) && (!Strings.isNullOrEmpty(paramBundle.getString("TEXT")))) {
      this.mText = paramBundle.getString("TEXT");
    }
    View localView = paramLayoutInflater.inflate(2130968657, paramViewGroup, false);
    ((Button)localView.findViewById(2131296515)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        DebugDialogFragment.this.share();
      }
    });
    TextView localTextView = (TextView)localView.findViewById(2131296516);
    if (!TextUtils.isEmpty(this.mText)) {
      localTextView.setText(this.mText);
    }
    return localView;
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("TEXT", this.mText);
  }
  
  public void setText(String paramString)
  {
    this.mText = paramString;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.debug.DebugDialogFragment
 * JD-Core Version:    0.7.0.1
 */