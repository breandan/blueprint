package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.search.shared.ui.WebImageView.Listener;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.ui.ActionEditorView;

public class OpenUrlCard
  extends AbstractCardView<OpenUrlController>
  implements OpenUrlController.Ui
{
  private TextView mDisplayUrlView;
  private TextView mNameView;
  private WebImageView mPreviewView;
  
  public OpenUrlCard(Context paramContext)
  {
    super(paramContext);
  }
  
  public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    ActionEditorView localActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968765);
    this.mNameView = ((TextView)localActionEditorView.findViewById(2131296823));
    this.mDisplayUrlView = ((TextView)localActionEditorView.findViewById(2131296824));
    this.mPreviewView = ((WebImageView)localActionEditorView.findViewById(2131296822));
    this.mPreviewView.setOnDownloadListener(new WebImageView.Listener()
    {
      public void onImageDownloaded(Drawable paramAnonymousDrawable)
      {
        ((OpenUrlController)OpenUrlCard.this.getController()).uiReady();
      }
    });
    localActionEditorView.setConfirmText(2131363444);
    localActionEditorView.setConfirmIcon(2130837706);
    return localActionEditorView;
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(OpenUrlCard.class.getCanonicalName());
  }
  
  public void setDisplayUrl(String paramString)
  {
    this.mDisplayUrlView.setText(paramString);
  }
  
  public void setName(String paramString)
  {
    this.mNameView.setText(Html.fromHtml(paramString));
  }
  
  public void setPreviewUrl(String paramString)
  {
    WebImageView localWebImageView = this.mPreviewView;
    if (TextUtils.isEmpty(paramString)) {}
    for (int i = 8;; i = 0)
    {
      localWebImageView.setVisibility(i);
      VelvetServices localVelvetServices = VelvetServices.get();
      Uri localUri = localVelvetServices.getCoreServices().getSearchUrlHelper().maybeMakeAbsoluteUri(paramString);
      this.mPreviewView.setImageUri(localUri, localVelvetServices.getImageLoader());
      return;
    }
  }
  
  public void showDisabled()
  {
    disableActionEditor(2131363671);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.OpenUrlCard
 * JD-Core Version:    0.7.0.1
 */