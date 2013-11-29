package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.voicesearch.ui.ActionEditorView;

public class PuntCard
  extends AbstractCardView<PuntController>
  implements PuntController.Ui
{
  private View mDividerView;
  private ActionEditorView mMainContent;
  private TextView mMessageView;
  private TextView mQueryView;
  
  public PuntCard(Context paramContext)
  {
    super(paramContext);
  }
  
  public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mMainContent = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968795);
    this.mQueryView = ((TextView)this.mMainContent.findViewById(2131296653));
    this.mMessageView = ((TextView)this.mMainContent.findViewById(2131296903));
    this.mDividerView = this.mMainContent.findViewById(2131296482);
    this.mMainContent.setContentClickable(false);
    this.mMainContent.showCountDownView(false);
    this.mMainContent.setNoConfirmIcon();
    return this.mMainContent;
  }
  
  public void setMessageId(int paramInt)
  {
    this.mMessageView.setVisibility(0);
    this.mMessageView.setText(paramInt);
  }
  
  public void setMessageText(CharSequence paramCharSequence)
  {
    this.mMessageView.setVisibility(0);
    this.mMessageView.setText(paramCharSequence);
  }
  
  public void setNoQuery()
  {
    this.mQueryView.setVisibility(8);
    this.mDividerView.setVisibility(8);
  }
  
  public void setQuery(CharSequence paramCharSequence)
  {
    this.mQueryView.setVisibility(0);
    this.mDividerView.setVisibility(0);
    this.mQueryView.setText(getResources().getString(2131363622, new Object[] { paramCharSequence }));
  }
  
  public void showActionButton(int paramInt1, int paramInt2)
  {
    setConfirmText(paramInt2);
    if (paramInt1 != 0) {
      setConfirmIcon(paramInt1);
    }
    this.mMainContent.showCountDownView(true);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.PuntCard
 * JD-Core Version:    0.7.0.1
 */