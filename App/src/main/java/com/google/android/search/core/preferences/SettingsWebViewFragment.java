package com.google.android.search.core.preferences;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.shared.util.SendGoogleFeedback;
import com.google.android.velvet.Help;

public class SettingsWebViewFragment
  extends Fragment
{
  private View mContainerView;
  private int mEndPadding;
  private int mStartPadding;
  
  @SuppressLint({"SetJavaScriptEnabled"})
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130968825, paramViewGroup, false);
    WebView localWebView = (WebView)localView.findViewById(2131296989);
    final ProgressBar localProgressBar = (ProgressBar)localView.findViewById(2131296988);
    localWebView.getSettings().setJavaScriptEnabled(true);
    localWebView.setWebViewClient(new WebViewClient()
    {
      public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
      {
        super.onPageFinished(paramAnonymousWebView, paramAnonymousString);
        localProgressBar.setVisibility(8);
      }
    });
    localWebView.loadUrl(new Help(getActivity()).getHelpUrl("settings").toString());
    final Activity localActivity = getActivity();
    ((Button)localView.findViewById(2131296990)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SendGoogleFeedback.launchGoogleFeedback(localActivity, localActivity.getWindow().getDecorView().getRootView());
      }
    });
    return localView;
  }
  
  public void onDestroyView()
  {
    super.onDestroyView();
    if (this.mContainerView != null) {
      LayoutUtils.setPaddingRelative(this.mContainerView, this.mStartPadding, this.mContainerView.getPaddingTop(), this.mEndPadding, this.mContainerView.getPaddingBottom());
    }
  }
  
  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ViewGroup localViewGroup = (ViewGroup)paramView.getParent();
    this.mContainerView = localViewGroup;
    this.mStartPadding = LayoutUtils.getPaddingStart(localViewGroup);
    this.mEndPadding = LayoutUtils.getPaddingEnd(localViewGroup);
    LayoutUtils.setPaddingRelative(localViewGroup, 0, localViewGroup.getPaddingTop(), 0, localViewGroup.getPaddingBottom());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.SettingsWebViewFragment
 * JD-Core Version:    0.7.0.1
 */