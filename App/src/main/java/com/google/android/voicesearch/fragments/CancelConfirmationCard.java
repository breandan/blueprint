package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.search.shared.ui.SuggestionGridLayout.LayoutParams;
import com.google.android.search.shared.ui.SuggestionGridLayout.LayoutParams.AnimationType;
import com.google.android.voicesearch.ui.ActionEditorView;

public class CancelConfirmationCard
  extends AbstractCardView<CancelController>
  implements CancelController.Ui
{
  private TextView mTitle;
  
  public CancelConfirmationCard(Context paramContext)
  {
    super(paramContext);
  }
  
  public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    ActionEditorView localActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968619);
    this.mTitle = ((TextView)localActionEditorView.findViewById(2131296382));
    showConfirmBar(false);
    localActionEditorView.setContentClickable(false);
    SuggestionGridLayout.LayoutParams localLayoutParams = new SuggestionGridLayout.LayoutParams(-1, -2, 0);
    localLayoutParams.appearAnimationType = SuggestionGridLayout.LayoutParams.AnimationType.SLIDE_DOWN;
    localLayoutParams.canDismiss = false;
    setLayoutParams(localLayoutParams);
    return localActionEditorView;
  }
  
  public void showMessage(int paramInt)
  {
    this.mTitle.setText(paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.CancelConfirmationCard
 * JD-Core Version:    0.7.0.1
 */