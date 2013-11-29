package com.google.android.velvet.presenter.inappwebpage;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.util.Printer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.Window;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.LazyString;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.PrefixPrinter;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.SendGoogleFeedback;
import com.google.android.velvet.ui.InAppWebPageActivity;
import com.google.android.velvet.ui.WebViewWrapper;
import com.google.android.velvet.util.IntentUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class InAppWebPagePresenter
{
  private final InAppWebPageActivity mActivity;
  private final Context mAppContext;
  private final ContentRetriever mContentRetriever;
  private final Display mDisplay;
  private LazyString mErrorDumpMessage = null;
  private final HttpHelper mHttpHelper;
  private boolean mInitialized = false;
  private ContentListener mLastUriCallback = null;
  @Nullable
  private Runnable mLoadingIndicatorTimer = null;
  private final PrintableLog mLog = new PrintableLog(null);
  private final WebViewPageController.Factory mPageControllerFactory;
  private int mRequestLogId = 0;
  private RequestStack mRequestStack;
  private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
  private final SearchUrlHelper mUrlHelper;
  
  InAppWebPagePresenter(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, HttpHelper paramHttpHelper, SearchUrlHelper paramSearchUrlHelper, Context paramContext, WebViewPageController.Factory paramFactory, ContentRetriever paramContentRetriever, Display paramDisplay, InAppWebPageActivity paramInAppWebPageActivity)
  {
    this.mActivity = paramInAppWebPageActivity;
    this.mAppContext = paramContext;
    this.mUiThreadExecutor = paramScheduledSingleThreadedExecutor;
    this.mHttpHelper = paramHttpHelper;
    this.mUrlHelper = paramSearchUrlHelper;
    this.mPageControllerFactory = paramFactory;
    this.mContentRetriever = paramContentRetriever;
    this.mDisplay = paramDisplay;
  }
  
  private void addFeedbackOption(Menu paramMenu, int paramInt)
  {
    paramMenu.add(0, 0, paramInt, 2131363570).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
    {
      public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem)
      {
        InAppWebPagePresenter.this.dump("", new LogPrinter(3, "Velvet.InAppWebPagePresenter"));
        SendGoogleFeedback.launchGoogleFeedback(InAppWebPagePresenter.this.mActivity, InAppWebPagePresenter.this.mActivity.getWindow().getDecorView().getRootView());
        return true;
      }
    });
  }
  
  private void addRefreshOption(Menu paramMenu, int paramInt)
  {
    paramMenu.add(0, 2131296284, paramInt, 2131362185).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
    {
      public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem)
      {
        InAppWebPagePresenter.this.loadCurrentRequest();
        return true;
      }
    });
  }
  
  private void cancelLoadingIndicatorTimer()
  {
    if (this.mLoadingIndicatorTimer != null)
    {
      this.mUiThreadExecutor.cancelExecute(this.mLoadingIndicatorTimer);
      this.mLoadingIndicatorTimer = null;
    }
  }
  
  private void checkContainsOnlySecureGoogleUris(RequestStack paramRequestStack)
  {
    Iterator localIterator = paramRequestStack.iterator();
    while (localIterator.hasNext())
    {
      Request localRequest = (Request)localIterator.next();
      if (!this.mUrlHelper.isSecureGoogleUri(localRequest.getUri())) {
        throw new IllegalArgumentException("Untrusted URI found in request history");
      }
    }
  }
  
  private Intent getRelaunchIntent()
  {
    return new Intent("com.google.android.velvet.ui.InAppWebPageActivity.ACTION_RELAUNCH").putExtra("com.google.android.velvet.ui.InAppWebPageActivity.EXTRA_REQUEST_STACK", this.mRequestStack).setClass(this.mActivity, InAppWebPageActivity.class);
  }
  
  private void handleExternalNavigation(Uri paramUri)
  {
    try
    {
      Query localQuery = this.mUrlHelper.getQueryFromUrl(Query.EMPTY, paramUri);
      if (localQuery != null)
      {
        Intent localIntent1 = IntentUtils.createResumeVelvetWithQueryIntent(this.mAppContext, localQuery, getRelaunchIntent());
        this.mActivity.startActivity(localIntent1);
        this.mActivity.finish();
        return;
      }
      Intent localIntent2 = this.mUrlHelper.getExternalIntentForUri(paramUri);
      this.mActivity.startActivity(localIntent2);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Log.e("Velvet.InAppWebPagePresenter", "No activity found to open: " + paramUri.toString());
      this.mActivity.showErrorToast(2131363215);
      return;
    }
    catch (URISyntaxException localURISyntaxException)
    {
      Log.e("Velvet.InAppWebPagePresenter", "Invalid URI " + paramUri.toString());
      this.mActivity.showErrorToast(2131363215);
    }
  }
  
  private void handleNewRequest(Request paramRequest)
  {
    this.mRequestStack.pushNewCurrentRequest(paramRequest);
    loadCurrentRequest();
  }
  
  private void initializeLoadingIndicatorTimer()
  {
    if (this.mLoadingIndicatorTimer != null) {}
    this.mLoadingIndicatorTimer = new Runnable()
    {
      public void run()
      {
        InAppWebPagePresenter.this.mDisplay.showLoadingIndicator();
        InAppWebPagePresenter.access$402(InAppWebPagePresenter.this, null);
      }
    };
    this.mUiThreadExecutor.executeDelayed(this.mLoadingIndicatorTimer, 3000L);
  }
  
  private void loadCurrentRequest()
  {
    if (this.mLastUriCallback != null) {
      this.mLastUriCallback.stop();
    }
    if (this.mRequestStack.isEmpty())
    {
      this.mActivity.finish();
      return;
    }
    Request localRequest = this.mRequestStack.getCurrentRequest();
    cancelLoadingIndicatorTimer();
    this.mDisplay.clearUi();
    initializeLoadingIndicatorTimer();
    this.mRequestLogId = (1 + this.mRequestLogId);
    ContentRetrieverListener localContentRetrieverListener = new ContentRetrieverListener(this.mRequestLogId);
    this.mLastUriCallback = new ContentListener(localRequest, this.mContentRetriever.retrieveContent(localRequest.getUri(), localContentRetrieverListener));
  }
  
  private void loadInWebView(Request paramRequest, Content paramContent)
  {
    if (this.mRequestStack.getCurrentRequest() == paramRequest) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      WebViewWrapper localWebViewWrapper = this.mActivity.createNewWebViewWrapper();
      WebViewPageController localWebViewPageController = this.mPageControllerFactory.create(localWebViewWrapper, paramRequest, paramContent);
      PageControllerListener localPageControllerListener = new PageControllerListener(this.mRequestLogId, localWebViewPageController);
      localWebViewPageController.setListener(new PageControllerListenerWrapper(this.mUiThreadExecutor, localPageControllerListener));
      this.mDisplay.setWebViewPageController(localWebViewPageController);
      localWebViewPageController.loadRequestInWebView();
      return;
    }
  }
  
  private void showError(int paramInt)
  {
    this.mDisplay.showError(paramInt);
  }
  
  private void showLoadFailedError()
  {
    cancelLoadingIndicatorTimer();
    if (this.mHttpHelper.haveNetworkConnection()) {}
    for (int i = 2131363294;; i = 2131363295)
    {
      showError(i);
      return;
    }
  }
  
  private void showWebView()
  {
    cancelLoadingIndicatorTimer();
    this.mDisplay.showWebView();
  }
  
  private void titleChanged()
  {
    this.mDisplay.titleChanged();
  }
  
  public void addFixedOptionsMenuItems(Menu paramMenu)
  {
    addRefreshOption(paramMenu, 196608);
    addFeedbackOption(paramMenu, 196608);
  }
  
  public void destroy()
  {
    if (this.mLastUriCallback != null) {
      this.mLastUriCallback.stop();
    }
    this.mDisplay.destroy();
  }
  
  public void dump(String paramString, Printer paramPrinter)
  {
    PrefixPrinter localPrefixPrinter = new PrefixPrinter(paramPrinter, paramString);
    localPrefixPrinter.println("InAppWebPagePresenter:");
    localPrefixPrinter.addToPrefix("  ");
    if (this.mErrorDumpMessage != null) {
      localPrefixPrinter.println("Error: " + this.mErrorDumpMessage);
    }
    this.mDisplay.dump(localPrefixPrinter);
    this.mRequestStack.dump(localPrefixPrinter);
    localPrefixPrinter.println("Log:");
    localPrefixPrinter.addToPrefix("  ");
    synchronized (this.mLog)
    {
      Iterator localIterator = this.mLog.iterator();
      if (localIterator.hasNext()) {
        localPrefixPrinter.println(localIterator.next().toString());
      }
    }
  }
  
  public void goBack()
  {
    if (this.mRequestStack == null) {
      this.mActivity.finish();
    }
    while ((this.mDisplay.maybeGoBackInWebView()) || (this.mRequestStack.isEmpty())) {
      return;
    }
    this.mRequestStack.popCurrentRequest();
    loadCurrentRequest();
  }
  
  public void goUp()
  {
    this.mActivity.finish();
  }
  
  public void relaunch(Bundle paramBundle)
  {
    if (!this.mInitialized)
    {
      RequestStack localRequestStack = (RequestStack)paramBundle.getParcelable("com.google.android.velvet.ui.InAppWebPageActivity.EXTRA_REQUEST_STACK");
      Preconditions.checkNotNull(localRequestStack);
      checkContainsOnlySecureGoogleUris(localRequestStack);
      this.mRequestStack = localRequestStack;
      loadCurrentRequest();
      this.mInitialized = true;
    }
  }
  
  public void restoreState(Bundle paramBundle)
  {
    if (!this.mInitialized) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      if (paramBundle != null)
      {
        this.mRequestStack = ((RequestStack)paramBundle.getParcelable("InAppWebPage.RequestStack"));
        if (this.mRequestStack != null)
        {
          loadCurrentRequest();
          this.mInitialized = true;
        }
      }
      return;
    }
  }
  
  public void saveState(Bundle paramBundle)
  {
    paramBundle.putParcelable("InAppWebPage.RequestStack", this.mRequestStack);
  }
  
  public void start(@Nullable Request paramRequest)
  {
    if (!this.mInitialized)
    {
      this.mRequestStack = new RequestStack();
      if (paramRequest != null) {
        this.mRequestStack.pushNewCurrentRequest(paramRequest);
      }
      loadCurrentRequest();
      this.mInitialized = true;
    }
  }
  
  public void tryAgain()
  {
    loadCurrentRequest();
  }
  
  public void updateDynamicOptionsMenuItems(Menu paramMenu)
  {
    this.mDisplay.updateDynamicOptionsMenuItems(paramMenu);
  }
  
  private class ContentListener
    implements FutureCallback<Content>
  {
    private final ListenableFuture<Content> mFuture;
    private final Request mRequest;
    private boolean mStopped = false;
    
    public ContentListener(ListenableFuture<Content> paramListenableFuture)
    {
      this.mRequest = paramListenableFuture;
      Object localObject;
      this.mFuture = localObject;
      Futures.addCallback(this.mFuture, this, InAppWebPagePresenter.this.mUiThreadExecutor);
    }
    
    public void onFailure(Throwable paramThrowable)
    {
      if ((!this.mStopped) && (!(paramThrowable instanceof CancellationException)))
      {
        Log.w("Velvet.InAppWebPagePresenter", "Unexpected exception from UriRequestMaker", paramThrowable);
        InAppWebPagePresenter.this.showLoadFailedError();
      }
    }
    
    public void onSuccess(Content paramContent)
    {
      if (!this.mStopped) {
        InAppWebPagePresenter.this.loadInWebView(this.mRequest, paramContent);
      }
    }
    
    public void stop()
    {
      this.mStopped = true;
      this.mFuture.cancel(true);
    }
  }
  
  private class ContentRetrieverListener
    implements ContentRetriever.Listener
  {
    private final int mLogId;
    
    public ContentRetrieverListener(int paramInt)
    {
      this.mLogId = paramInt;
    }
    
    public void log(Object paramObject)
    {
      InAppWebPagePresenter.PrintableLog localPrintableLog = InAppWebPagePresenter.this.mLog;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(this.mLogId);
      arrayOfObject[1] = paramObject;
      localPrintableLog.add(new LazyString("[%d] %s", arrayOfObject));
    }
  }
  
  private class PageControllerListener
    implements WebViewPageController.Listener
  {
    private final int mLogId;
    private final WebViewPageController mPageController;
    
    public PageControllerListener(int paramInt, WebViewPageController paramWebViewPageController)
    {
      this.mLogId = paramInt;
      this.mPageController = paramWebViewPageController;
    }
    
    private boolean isCurrentController()
    {
      return this.mPageController == InAppWebPagePresenter.this.mDisplay.getCurrentWebViewPageController();
    }
    
    public void loadUriInApp(Request paramRequest)
    {
      if (isCurrentController()) {
        InAppWebPagePresenter.this.handleNewRequest(paramRequest);
      }
    }
    
    public void log(Object paramObject)
    {
      InAppWebPagePresenter.PrintableLog localPrintableLog = InAppWebPagePresenter.this.mLog;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(this.mLogId);
      arrayOfObject[1] = paramObject;
      localPrintableLog.add(new LazyString("[%d] %s", arrayOfObject));
    }
    
    public void pageLoadFailed(Object paramObject)
    {
      if (isCurrentController())
      {
        InAppWebPagePresenter.this.showLoadFailedError();
        InAppWebPagePresenter.access$1002(InAppWebPagePresenter.this, new LazyString("%s", new Object[] { paramObject }));
        InAppWebPagePresenter.this.mLog.add(InAppWebPagePresenter.this.mErrorDumpMessage);
      }
    }
    
    public void pageReady()
    {
      if (isCurrentController()) {
        InAppWebPagePresenter.this.showWebView();
      }
    }
    
    public void titleChanged()
    {
      if (isCurrentController()) {
        InAppWebPagePresenter.this.titleChanged();
      }
    }
    
    public void userNavigation(Uri paramUri)
    {
      if (isCurrentController()) {
        InAppWebPagePresenter.this.handleExternalNavigation(paramUri);
      }
    }
  }
  
  private class PageControllerListenerWrapper
    implements WebViewPageController.Listener
  {
    private final WebViewPageController.Listener mDelegate;
    private final Executor mUiExecutor;
    
    public PageControllerListenerWrapper(Executor paramExecutor, WebViewPageController.Listener paramListener)
    {
      this.mUiExecutor = paramExecutor;
      this.mDelegate = paramListener;
    }
    
    public void loadUriInApp(final Request paramRequest)
    {
      this.mUiExecutor.execute(new Runnable()
      {
        public void run()
        {
          InAppWebPagePresenter.PageControllerListenerWrapper.this.mDelegate.loadUriInApp(paramRequest);
        }
      });
    }
    
    public void log(Object paramObject)
    {
      this.mDelegate.log(paramObject);
    }
    
    public void pageLoadFailed(final Object paramObject)
    {
      this.mUiExecutor.execute(new Runnable()
      {
        public void run()
        {
          InAppWebPagePresenter.PageControllerListenerWrapper.this.mDelegate.pageLoadFailed(paramObject);
        }
      });
    }
    
    public void pageReady()
    {
      this.mUiExecutor.execute(new Runnable()
      {
        public void run()
        {
          InAppWebPagePresenter.PageControllerListenerWrapper.this.mDelegate.pageReady();
        }
      });
    }
    
    public void titleChanged()
    {
      this.mUiExecutor.execute(new Runnable()
      {
        public void run()
        {
          InAppWebPagePresenter.PageControllerListenerWrapper.this.mDelegate.titleChanged();
        }
      });
    }
    
    public void userNavigation(final Uri paramUri)
    {
      this.mUiExecutor.execute(new Runnable()
      {
        public void run()
        {
          InAppWebPagePresenter.PageControllerListenerWrapper.this.mDelegate.userNavigation(paramUri);
        }
      });
    }
  }
  
  private static class PrintableLog
    implements Iterable<Object>
  {
    private int MAX_CAPACITY = 40;
    private boolean elementsRemoved = false;
    private LinkedList<Object> mLog = Lists.newLinkedList();
    
    public void add(Object paramObject)
    {
      try
      {
        this.mLog.add(paramObject);
        if (this.mLog.size() > this.MAX_CAPACITY)
        {
          this.mLog.removeFirst();
          this.elementsRemoved = true;
        }
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    public Iterator<Object> iterator()
    {
      if (this.elementsRemoved)
      {
        String[] arrayOfString = new String[1];
        arrayOfString[0] = ("[History log trimmed to " + this.MAX_CAPACITY + " elements]");
        return Iterators.concat(Iterators.forArray(arrayOfString), this.mLog.iterator());
      }
      return this.mLog.iterator();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.inappwebpage.InAppWebPagePresenter
 * JD-Core Version:    0.7.0.1
 */