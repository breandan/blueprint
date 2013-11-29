package com.google.android.velvet.presenter.inappwebpage;

import android.util.Printer;
import android.view.Menu;
import com.google.android.velvet.ui.InAppWebPageActivity;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public class Display
{
  static final int ERROR = 3;
  static final int LOADING = 2;
  static final int NOTHING = 0;
  static final int OPTIONS_MENU_GROUP_DYNAMIC = 1;
  static final int WEB_VIEW = 1;
  private final InAppWebPageActivity mActivity;
  private int mVisible = 0;
  @Nullable
  private WebViewPageController mWebViewPageController = null;
  
  public Display(InAppWebPageActivity paramInAppWebPageActivity)
  {
    this.mActivity = paramInAppWebPageActivity;
  }
  
  private void clearWebViewPageController()
  {
    if ((this.mWebViewPageController != null) && (this.mVisible != 1)) {
      this.mWebViewPageController.destroy();
    }
    this.mWebViewPageController = null;
  }
  
  public void clearUi()
  {
    switch (this.mVisible)
    {
    }
    for (;;)
    {
      clearWebViewPageController();
      this.mVisible = 0;
      return;
      this.mActivity.hideLoadingIndicator();
      continue;
      this.mActivity.hideError();
      continue;
      this.mActivity.setTitle(null);
      this.mActivity.removeWebView(this.mWebViewPageController.getWebViewWrapper());
      this.mWebViewPageController = null;
    }
  }
  
  public void destroy()
  {
    clearWebViewPageController();
  }
  
  public void dump(Printer paramPrinter)
  {
    switch (this.mVisible)
    {
    default: 
      return;
    case 0: 
      paramPrinter.println("DisplayState: NOTHING");
      return;
    case 1: 
      paramPrinter.println("DisplayState: WEB_VIEW");
      return;
    case 2: 
      paramPrinter.println("DisplayState: LOADING");
      return;
    }
    paramPrinter.println("DisplayState: ERROR");
  }
  
  public WebViewPageController getCurrentWebViewPageController()
  {
    return this.mWebViewPageController;
  }
  
  public int getVisibleComponent()
  {
    return this.mVisible;
  }
  
  public boolean maybeGoBackInWebView()
  {
    if (this.mVisible == 1) {
      return this.mWebViewPageController.goBack();
    }
    return false;
  }
  
  public void setWebViewPageController(WebViewPageController paramWebViewPageController)
  {
    Preconditions.checkNotNull(paramWebViewPageController);
    if (this.mWebViewPageController == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mWebViewPageController = paramWebViewPageController;
      return;
    }
  }
  
  public void showError(int paramInt)
  {
    if (this.mVisible == 0) {
      this.mActivity.showError(paramInt);
    }
    for (;;)
    {
      clearWebViewPageController();
      this.mVisible = 3;
      return;
      if (this.mVisible == 2)
      {
        this.mActivity.hideLoadingIndicator();
        this.mActivity.showError(paramInt);
      }
      else if (this.mVisible == 1)
      {
        this.mActivity.removeWebView(this.mWebViewPageController.getWebViewWrapper());
        this.mActivity.showError(paramInt);
        this.mActivity.setTitle(null);
      }
    }
  }
  
  public void showLoadingIndicator()
  {
    if (this.mVisible == 0)
    {
      this.mActivity.showLoadingIndicator();
      this.mVisible = 2;
    }
  }
  
  public void showWebView()
  {
    boolean bool;
    if (this.mWebViewPageController != null)
    {
      bool = true;
      Preconditions.checkState(bool);
      if (this.mVisible != 0) {
        break label59;
      }
      this.mActivity.setTitle(this.mWebViewPageController.getTitle());
      this.mActivity.showWebView(this.mWebViewPageController.getWebViewWrapper());
      this.mVisible = 1;
    }
    label59:
    while (this.mVisible != 2)
    {
      return;
      bool = false;
      break;
    }
    this.mActivity.hideLoadingIndicator();
    this.mActivity.setTitle(this.mWebViewPageController.getTitle());
    this.mActivity.showWebView(this.mWebViewPageController.getWebViewWrapper());
    this.mVisible = 1;
  }
  
  public void titleChanged()
  {
    if (this.mVisible == 1) {
      this.mActivity.setTitle(this.mWebViewPageController.getTitle());
    }
  }
  
  public void updateDynamicOptionsMenuItems(Menu paramMenu)
  {
    paramMenu.removeGroup(1);
    if (this.mVisible == 1) {
      this.mWebViewPageController.addDynamicMenuItems(paramMenu, 1);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.inappwebpage.Display
 * JD-Core Version:    0.7.0.1
 */