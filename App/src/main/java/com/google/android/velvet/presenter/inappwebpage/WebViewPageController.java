package com.google.android.velvet.presenter.inappwebpage;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.search.core.AgsaExtJavascriptInterface;
import com.google.android.search.core.JavascriptExtensions;
import com.google.android.search.core.JavascriptExtensions.PageEventListener;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.util.LazyString;
import com.google.android.shared.util.IntentStarter;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.ui.WebViewWrapper;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public class WebViewPageController {
    private final boolean DBG = false;
    private final String TAG = "Velvet.WebViewPageController";
    private Set<String> mAllInAppUriPatterns = Collections.synchronizedSet(Sets.newHashSet());
    private final Content mContent;
    private final AtomicBoolean mContentUsed = new AtomicBoolean(false);
    private Set<DynamicOptionsMenuItem> mDynamicOptionsMenuItems = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private Listener mListener;
    @Nullable
    private volatile String mTitle = null;
    private final SearchUrlHelper mUrlHelper;
    private final WebViewSyncControl mWebViewSyncControl = new WebViewSyncControl(null);
    private final WebViewWrapper mWebViewWrapper;

    private WebViewPageController(SearchUrlHelper paramSearchUrlHelper, WebViewWrapper paramWebViewWrapper, Request paramRequest, Content paramContent) {
        this.mUrlHelper = ((SearchUrlHelper) Preconditions.checkNotNull(paramSearchUrlHelper));
        this.mWebViewWrapper = ((WebViewWrapper) Preconditions.checkNotNull(paramWebViewWrapper));
        this.mContent = ((Content) Preconditions.checkNotNull(paramContent));
        this.mAllInAppUriPatterns.addAll(paramRequest.getInitialInAppUriPatterns());
    }

    private boolean handleUserNavigation(Uri paramUri) {
        if (shouldOpenUriInApp(paramUri)) {
            Request localRequest = new Request(paramUri, ImmutableSet.copyOf(this.mAllInAppUriPatterns));
            this.mListener.loadUriInApp(localRequest);
        }
        for (; ; ) {
            return true;
            this.mListener.userNavigation(paramUri);
        }
    }

    private boolean isInAppUri(Uri paramUri) {
        synchronized (this.mAllInAppUriPatterns) {
            Iterator localIterator = this.mAllInAppUriPatterns.iterator();
            while (localIterator.hasNext()) {
                if (Pattern.matches((String) localIterator.next(), paramUri.toString())) {
                    return true;
                }
            }
            return false;
        }
    }

    private void log(Object paramObject) {
        if (this.mListener != null) {
            this.mListener.log(paramObject);
        }
    }

    private void registerWebViewCallbacks(VelvetFactory paramVelvetFactory, IntentStarter paramIntentStarter) {
        this.mWebViewWrapper.setWebViewClient(new InAppWebViewClient(null));
        this.mWebViewWrapper.setWebChromeClient(new InAppWebChromeClient(null));
        AgsaExtJavascriptInterface localAgsaExtJavascriptInterface = paramVelvetFactory.createJavascriptExtensions(paramIntentStarter, JavascriptExtensions.permissiveTrustPolicy(), new JavascriptExtensionsListener(null));
        this.mWebViewWrapper.addJavascriptInterface(localAgsaExtJavascriptInterface, "agsa_ext");
    }

    private boolean shouldOpenUriInApp(Uri paramUri) {
        return (this.mUrlHelper.isSecureGoogleUri(paramUri)) && (isInAppUri(paramUri));
    }

    public void addDynamicMenuItems(Menu paramMenu, int paramInt) {
        synchronized (this.mDynamicOptionsMenuItems) {
            Iterator localIterator = this.mDynamicOptionsMenuItems.iterator();
            if (localIterator.hasNext()) {
                ((DynamicOptionsMenuItem) localIterator.next()).addToMenu(paramMenu, paramInt);
            }
        }
    }

    public void destroy() {
        this.mWebViewWrapper.destroy();
        this.mContent.release();
    }

    @Nullable
    public String getTitle() {
        return this.mTitle;
    }

    public WebViewWrapper getWebViewWrapper() {
        return this.mWebViewWrapper;
    }

    public boolean goBack() {
        if (this.mWebViewWrapper.canGoBack()) {
            this.mWebViewWrapper.goBack();
            return true;
        }
        return false;
    }

    public void loadRequestInWebView() {
        Preconditions.checkNotNull(this.mListener);
        this.mWebViewWrapper.loadUrl(this.mContent.getResolvedUri().toString());
    }

    public void setListener(Listener paramListener) {
        if (this.mListener == null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mListener = ((Listener) Preconditions.checkNotNull(paramListener));
            return;
        }
    }

    private class DynamicOptionsMenuItem {
        public final String mDisplayText;
        public final Intent mExternalIntent;
        public final Uri mItemUri;
        public final boolean mOpenInApp;
        public final int mOrder;

        public DynamicOptionsMenuItem(String paramString, int paramInt, Uri paramUri, boolean paramBoolean)
                throws URISyntaxException {
            this.mDisplayText = paramString;
            this.mOrder = paramInt;
            this.mItemUri = paramUri;
            this.mOpenInApp = paramBoolean;
            this.mExternalIntent = WebViewPageController.this.mUrlHelper.getExternalIntentForUri(paramUri);
        }

        private WebViewPageController getContainingObject() {
            return WebViewPageController.this;
        }

        public void addToMenu(Menu paramMenu, int paramInt) {
            MenuItem localMenuItem = paramMenu.add(paramInt, 0, this.mOrder, this.mDisplayText);
            if (!this.mOpenInApp) {
                localMenuItem.setIntent(this.mExternalIntent);
                return;
            }
            localMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem) {
                    WebViewPageController.this.mListener.loadUriInApp(new Request(WebViewPageController.DynamicOptionsMenuItem.this.mItemUri, ImmutableSet.of()));
                    return true;
                }
            });
        }

        public boolean equals(@Nullable Object paramObject) {
            boolean bool1 = paramObject instanceof DynamicOptionsMenuItem;
            boolean bool2 = false;
            if (bool1) {
                DynamicOptionsMenuItem localDynamicOptionsMenuItem = (DynamicOptionsMenuItem) paramObject;
                WebViewPageController localWebViewPageController1 = getContainingObject();
                WebViewPageController localWebViewPageController2 = localDynamicOptionsMenuItem.getContainingObject();
                bool2 = false;
                if (localWebViewPageController1 == localWebViewPageController2) {
                    boolean bool3 = Objects.equal(this.mDisplayText, localDynamicOptionsMenuItem.mDisplayText);
                    bool2 = false;
                    if (bool3) {
                        int i = this.mOrder;
                        int j = localDynamicOptionsMenuItem.mOrder;
                        bool2 = false;
                        if (i == j) {
                            boolean bool4 = Objects.equal(this.mItemUri, localDynamicOptionsMenuItem.mItemUri);
                            bool2 = false;
                            if (bool4) {
                                boolean bool5 = this.mOpenInApp;
                                boolean bool6 = localDynamicOptionsMenuItem.mOpenInApp;
                                bool2 = false;
                                if (bool5 == bool6) {
                                    bool2 = true;
                                }
                            }
                        }
                    }
                }
            }
            return bool2;
        }

        public int hashCode() {
            Object[] arrayOfObject = new Object[5];
            arrayOfObject[0] = WebViewPageController.this;
            arrayOfObject[1] = this.mDisplayText;
            arrayOfObject[2] = Integer.valueOf(this.mOrder);
            arrayOfObject[3] = this.mItemUri;
            arrayOfObject[4] = Boolean.valueOf(this.mOpenInApp);
            return Objects.hashCode(arrayOfObject);
        }
    }

    public static class Factory {
        private final IntentStarter mIntentStarter;
        private final SearchUrlHelper mUrlHelper;
        private final VelvetFactory mVelvetFactory;

        public Factory(VelvetFactory paramVelvetFactory, IntentStarter paramIntentStarter, SearchUrlHelper paramSearchUrlHelper) {
            this.mVelvetFactory = paramVelvetFactory;
            this.mIntentStarter = paramIntentStarter;
            this.mUrlHelper = paramSearchUrlHelper;
        }

        public WebViewPageController create(WebViewWrapper paramWebViewWrapper, Request paramRequest, Content paramContent) {
            WebViewPageController localWebViewPageController = new WebViewPageController(this.mUrlHelper, paramWebViewWrapper, paramRequest, paramContent, null);
            localWebViewPageController.registerWebViewCallbacks(this.mVelvetFactory, this.mIntentStarter);
            return localWebViewPageController;
        }
    }

    private class InAppWebChromeClient
            extends WebChromeClient {
        private InAppWebChromeClient() {
        }

        public void onReceivedTitle(WebView paramWebView, String paramString) {
            WebViewPageController.access$1102(WebViewPageController.this, paramString);
            WebViewPageController.this.mListener.titleChanged();
        }
    }

    private class InAppWebViewClient
            extends WebViewClient {
        private InAppWebViewClient() {
        }

        public void onPageFinished(WebView paramWebView, String paramString) {
            if (WebViewPageController.this.mWebViewSyncControl.pageLoadFinished(paramString)) {
                WebViewPageController.this.mListener.pageReady();
            }
        }

        public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2) {
            WebViewPageController.Listener localListener = WebViewPageController.this.mListener;
            Object[] arrayOfObject = new Object[3];
            arrayOfObject[0] = Integer.valueOf(paramInt);
            arrayOfObject[1] = paramString1;
            arrayOfObject[2] = paramString2;
            localListener.pageLoadFailed(new LazyString("Page load failed: errorCode %d, description %s, failingUrl %s", arrayOfObject));
        }

        public void onReceivedHttpAuthRequest(WebView paramWebView, HttpAuthHandler paramHttpAuthHandler, String paramString1, String paramString2) {
            WebViewPageController.this.mListener.pageLoadFailed("Page load failed - received HTTP Auth request");
        }

        public void onReceivedLoginRequest(WebView paramWebView, String paramString1, String paramString2, String paramString3) {
            WebViewPageController.this.mListener.pageLoadFailed("Page load failed - received Login request");
        }

        public void onReceivedSslError(WebView paramWebView, SslErrorHandler paramSslErrorHandler, SslError paramSslError) {
            WebViewPageController.this.mListener.pageLoadFailed("Page load failed - received SSL error");
        }

        public WebResourceResponse shouldInterceptRequest(WebView paramWebView, String paramString) {
            if (WebViewPageController.this.mContent.getResolvedUri().toString().equals(paramString)) {
                if (WebViewPageController.this.mContentUsed.compareAndSet(false, true)) {
                    return WebViewPageController.this.mContent.getWebPage().toWebResourceResponse();
                }
                WebViewPageController localWebViewPageController = WebViewPageController.this;
                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0] = SearchUrlHelper.safeLogUrl(paramString);
                localWebViewPageController.log(new LazyString("Content for %s apparently requested multiple times", arrayOfObject));
            }
            return null;
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            return WebViewPageController.this.handleUserNavigation(Uri.parse(paramString));
        }
    }

    private class JavascriptExtensionsListener
            implements JavascriptExtensions.PageEventListener {
        private JavascriptExtensionsListener() {
        }

        public void addInAppUrlPattern(String paramString) {
            WebViewPageController.this.mAllInAppUriPatterns.add(paramString);
        }

        public void addOptionsMenuItem(String paramString1, int paramInt, String paramString2, boolean paramBoolean) {
            try {
                Uri localUri = Uri.parse(paramString2);
                if ((paramBoolean) && (WebViewPageController.this.mUrlHelper.isSecureGoogleUri(localUri))) {
                }
                for (boolean bool = true; ; bool = false) {
                    WebViewPageController.DynamicOptionsMenuItem localDynamicOptionsMenuItem = new WebViewPageController.DynamicOptionsMenuItem(WebViewPageController.this, paramString1, paramInt, localUri, bool);
                    WebViewPageController.this.mDynamicOptionsMenuItems.add(localDynamicOptionsMenuItem);
                    return;
                }
                return;
            } catch (URISyntaxException localURISyntaxException) {
                Log.w("Velvet.WebViewPageController", "Ignoring Menu Item with invalid URI " + SearchUrlHelper.safeLogUrl(paramString2));
            }
        }

        public void delayedPageLoad() {
            WebViewPageController.this.mWebViewSyncControl.delayedPageLoad();
        }

        public void loadUriInApp(Uri paramUri) {
            WebViewPageController.this.mListener.loadUriInApp(new Request(paramUri, ImmutableSet.of()));
        }

        public void pageReady() {
            if (WebViewPageController.this.mWebViewSyncControl.pageReady()) {
                WebViewPageController.this.mListener.pageReady();
            }
        }
    }

    public static abstract interface Listener {
        public abstract void loadUriInApp(Request paramRequest);

        public abstract void log(Object paramObject);

        public abstract void pageLoadFailed(Object paramObject);

        public abstract void pageReady();

        public abstract void titleChanged();

        public abstract void userNavigation(Uri paramUri);
    }

    private class WebViewSyncControl {
        private boolean mDelayedPageLoad = false;
        private boolean mPageShown = false;

        private WebViewSyncControl() {
        }

        private boolean takeNotifyPageReady() {
            if (!this.mPageShown) {
                this.mPageShown = true;
                WebViewPageController.this.log("Page ready to be shown");
                return true;
            }
            return false;
        }

        public void delayedPageLoad() {
            try {
                this.mDelayedPageLoad = true;
                WebViewPageController.this.log("delayedPageLoad");
                return;
            } finally {
                localObject =finally;
                throw localObject;
            }
        }

        public boolean pageLoadFinished(String paramString) {
            try {
                WebViewPageController localWebViewPageController = WebViewPageController.this;
                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0] = SearchUrlHelper.safeLogUrl(paramString);
                localWebViewPageController.log(new LazyString("pageLoadFinished %s", arrayOfObject));
                boolean bool1 = this.mDelayedPageLoad;
                boolean bool2 = false;
                if (!bool1) {
                    boolean bool3 = takeNotifyPageReady();
                    bool2 = bool3;
                }
                return bool2;
            } finally {
            }
        }

        public boolean pageReady() {
            try {
                this.mDelayedPageLoad = true;
                WebViewPageController.this.log("pageReady");
                boolean bool = takeNotifyPageReady();
                return bool;
            } finally {
                localObject =finally;
                throw localObject;
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.inappwebpage.WebViewPageController

 * JD-Core Version:    0.7.0.1

 */