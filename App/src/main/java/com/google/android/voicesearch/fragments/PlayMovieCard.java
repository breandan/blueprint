package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.android.voicesearch.ui.ActionEditorView;

public class PlayMovieCard
  extends PlayMediaCard<PlayMovieController>
  implements PlayMovieController.Ui
{
  private TextView mExpiryTime;
  private TextView mGenreView;
  private TextView mReleaseAndRuntimeInfo;
  private TextView mTitleView;
  
  public PlayMovieCard(Context paramContext)
  {
    super(paramContext);
  }
  
  public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    ActionEditorView localActionEditorView = createMediaActionEditor(paramContext, paramLayoutInflater, paramViewGroup, paramBundle, 2130968784, 2131363450);
    this.mTitleView = ((TextView)localActionEditorView.findViewById(2131296873));
    this.mGenreView = ((TextView)localActionEditorView.findViewById(2131296877));
    this.mReleaseAndRuntimeInfo = ((TextView)localActionEditorView.findViewById(2131296875));
    this.mExpiryTime = ((TextView)localActionEditorView.findViewById(2131296876));
    return localActionEditorView;
  }
  
  public void setGenre(String paramString)
  {
    showTextIfNonEmpty(this.mGenreView, paramString);
  }
  
  public void setReleaseAndRuntimeInfo(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 > 0)
    {
      this.mExpiryTime.setVisibility(0);
      TextView localTextView2 = this.mExpiryTime;
      Context localContext2 = getContext();
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = TimeUtilities.getEtaString(getContext(), paramInt3, false);
      localTextView2.setText(localContext2.getString(2131363457, arrayOfObject2));
    }
    if ((paramInt1 != 0) && (paramInt2 != 0))
    {
      this.mReleaseAndRuntimeInfo.setVisibility(0);
      TextView localTextView1 = this.mReleaseAndRuntimeInfo;
      Context localContext1 = getContext();
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = Integer.valueOf(paramInt1);
      arrayOfObject1[1] = Integer.valueOf(paramInt2);
      localTextView1.setText(localContext1.getString(2131363458, arrayOfObject1));
    }
  }
  
  public void setTitle(String paramString)
  {
    this.mTitleView.setText(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.PlayMovieCard
 * JD-Core Version:    0.7.0.1
 */