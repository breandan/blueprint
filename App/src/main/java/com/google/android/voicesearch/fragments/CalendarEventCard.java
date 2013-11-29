package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.voicesearch.ui.ActionEditorView;

public class CalendarEventCard
  extends AbstractCardView<CalendarEventController>
  implements CalendarEventController.Ui
{
  private TextView mLocationView;
  private TextView mTimeView;
  private boolean mTitleSet;
  private TextView mTitleView;
  
  public CalendarEventCard(Context paramContext)
  {
    super(paramContext);
  }
  
  private void checkUiReady()
  {
    if (this.mTitleSet) {
      ((CalendarEventController)getController()).uiReady();
    }
  }
  
  public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    ActionEditorView localActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968613);
    this.mTitleView = ((TextView)localActionEditorView.findViewById(2131296414));
    this.mTimeView = ((TextView)localActionEditorView.findViewById(2131296413));
    this.mLocationView = ((TextView)localActionEditorView.findViewById(2131296415));
    return localActionEditorView;
  }
  
  public void setLocation(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      this.mLocationView.setVisibility(8);
      return;
    }
    this.mLocationView.setText(paramString);
    this.mLocationView.setVisibility(0);
  }
  
  public void setTime(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      this.mTimeView.setVisibility(8);
      return;
    }
    this.mTimeView.setText(paramString);
    this.mTimeView.setVisibility(0);
  }
  
  public void setTitle(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      this.mTitleView.setText(getContext().getString(2131363536));
    }
    for (;;)
    {
      this.mTitleSet = true;
      checkUiReady();
      return;
      this.mTitleView.setText(paramString);
    }
  }
  
  public void showCreateEvent()
  {
    setConfirmText(2131363537);
    setConfirmIcon(2130837692);
  }
  
  public void showDisabled()
  {
    disableActionEditor(2131363675);
  }
  
  public void showEditEvent()
  {
    setConfirmText(2131363538);
    setConfirmIcon(2130837648);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.CalendarEventCard
 * JD-Core Version:    0.7.0.1
 */