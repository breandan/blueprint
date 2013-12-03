package com.google.android.velvet.presenter.inappwebpage;

import android.app.Activity;
import android.content.Context;

import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.prefetch.AsyncHttpResponseFetcher;
import com.google.android.search.core.util.LoggingIntentStarter;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.ui.InAppWebPageActivity;

public class InAppWebPageFactory {
    private final AsyncServices mAsyncServices;
    private final CoreSearchServices mCoreSearchServices;
    private final VelvetFactory mVelvetFactory;

    public InAppWebPageFactory(VelvetFactory paramVelvetFactory, CoreSearchServices paramCoreSearchServices, AsyncServices paramAsyncServices) {
        this.mVelvetFactory = paramVelvetFactory;
        this.mCoreSearchServices = paramCoreSearchServices;
        this.mAsyncServices = paramAsyncServices;
    }

    private AsyncHttpResponseFetcher createAsyncHttpResponseFetcher() {
        return new AsyncHttpResponseFetcher(this.mCoreSearchServices.getConfig(), this.mCoreSearchServices.getHttpExecutor(), this.mCoreSearchServices.getHttpHelper(), this.mCoreSearchServices.getSdchManager());
    }

    private ContentRetriever createContentRetriver() {
        return new ContentRetriever(this.mAsyncServices.getPooledBackgroundExecutorService(), this.mCoreSearchServices.getCookies(), this.mCoreSearchServices.getGsaConfigFlags(), this.mCoreSearchServices.getSearchUrlHelper(), createAsyncHttpResponseFetcher());
    }

    private Display createDisplay(InAppWebPageActivity paramInAppWebPageActivity) {
        return new Display(paramInAppWebPageActivity);
    }

    private WebViewPageController.Factory createWebViewPageControllerFactory(Activity paramActivity) {
        return new WebViewPageController.Factory(this.mVelvetFactory, new LoggingIntentStarter(paramActivity, 0), this.mCoreSearchServices.getSearchUrlHelper());
    }

    public InAppWebPagePresenter createInAppWebPagePresenter(InAppWebPageActivity paramInAppWebPageActivity, Context paramContext) {
        return new InAppWebPagePresenter(this.mAsyncServices.getUiThreadExecutor(), this.mCoreSearchServices.getHttpHelper(), this.mCoreSearchServices.getSearchUrlHelper(), paramContext, createWebViewPageControllerFactory(paramInAppWebPageActivity), createContentRetriver(), createDisplay(paramInAppWebPageActivity), paramInAppWebPageActivity);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.inappwebpage.InAppWebPageFactory

 * JD-Core Version:    0.7.0.1

 */