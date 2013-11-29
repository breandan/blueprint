package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.voicesearch.ui.ActionEditorView;

public class OpenAppPlayMediaCard
  extends PlayMediaCard<OpenAppPlayMediaController>
  implements OpenAppPlayMediaController.Ui
{
  private ActionEditorView mContent;
  private TextView mDeveloperView;
  private TextView mTitleView;
  
  public OpenAppPlayMediaCard(Context paramContext)
  {
    super(paramContext);
  }
  
  public void hideCountDownView()
  {
    this.mContent.showCountDownView(false);
  }
  
  public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    ActionEditorView localActionEditorView = createMediaActionEditor(paramContext, paramLayoutInflater, paramViewGroup, paramBundle, 2130968781, 2131363452);
    this.mTitleView = ((TextView)localActionEditorView.findViewById(2131296864));
    this.mDeveloperView = ((TextView)localActionEditorView.findViewById(2131296866));
    this.mContent = localActionEditorView;
    return localActionEditorView;
  }
  
  public void setTitle(String paramString)
  {
    this.mTitleView.setText(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.OpenAppPlayMediaCard
 * JD-Core Version:    0.7.0.1
 */