package com.google.android.velvet.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.PrintWriterPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.search.core.UserAgentHelper;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.presenter.inappwebpage.InAppWebPagePresenter;
import com.google.android.velvet.presenter.inappwebpage.Request;
import com.google.android.velvet.ui.widget.TextScalingWebview;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class InAppWebPageActivity
        extends Activity {
    private View mErrorCard;
    private TextView mErrorMessageView;
    private int mFadeDuration;
    private View mLoadingIndicator;
    private InAppWebPagePresenter mPresenter;
    private UserAgentHelper mUserAgentHelper;
    private FrameLayout mWebViewContainer;

    private Animator.AnimatorListener destroyOnAnimationEnd(final WebView paramWebView) {
        new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator paramAnonymousAnimator) {
                if (paramWebView.getAlpha() < 0.05F) {
                    InAppWebPageActivity.this.mWebViewContainer.removeView(paramWebView);
                    paramWebView.destroy();
                }
            }
        };
    }

    private Animator.AnimatorListener makeInvisibleOnAnimationEnd(final View paramView) {
        new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator paramAnonymousAnimator) {
                if (paramView.getAlpha() < 0.05F) {
                    paramView.setVisibility(4);
                }
            }
        };
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public WebViewWrapper createNewWebViewWrapper() {
        TextScalingWebview localTextScalingWebview = new TextScalingWebview(this);
        WebSettings localWebSettings = localTextScalingWebview.getSettings();
        localWebSettings.setJavaScriptEnabled(true);
        this.mUserAgentHelper.onWebViewCreated(localTextScalingWebview);
        localWebSettings.setUserAgentString(this.mUserAgentHelper.getUserAgent());
        return new WebViewWrapper(localTextScalingWebview);
    }

    public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
        super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        this.mPresenter.dump(paramString, new PrintWriterPrinter(paramPrintWriter));
    }

    public void hideError() {
        this.mErrorCard.animate().cancel();
        this.mErrorCard.setVisibility(4);
        this.mErrorCard.setAlpha(0.0F);
    }

    public void hideLoadingIndicator() {
        this.mLoadingIndicator.animate().cancel();
        this.mLoadingIndicator.animate().alpha(0.0F).setDuration(this.mFadeDuration).setListener(makeInvisibleOnAnimationEnd(this.mLoadingIndicator));
    }

    public void onBackPressed() {
        this.mPresenter.goBack();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.mFadeDuration = getResources().getInteger(17694720);
        VelvetServices localVelvetServices = VelvetServices.get();
        this.mUserAgentHelper = localVelvetServices.getCoreServices().getUserAgentHelper();
        setContentView(2130968728);
        this.mWebViewContainer = ((FrameLayout) findViewById(2131296724));
        this.mErrorCard = findViewById(2131296725);
        this.mErrorMessageView = ((TextView) this.mErrorCard.findViewById(2131296549));
        Button localButton = (Button) this.mErrorCard.findViewById(2131296550);
        localButton.setText(2131363288);
        localButton.setOnClickListener(new TryAgainButtonListener(null));
        this.mLoadingIndicator = findViewById(2131296726);
        this.mPresenter = localVelvetServices.getFactory().createInAppWebPagePresenter(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        if (paramBundle != null) {
            this.mPresenter.restoreState(paramBundle);
        }
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        this.mPresenter.addFixedOptionsMenuItems(paramMenu);
        return true;
    }

    protected void onDestroy() {
        this.mPresenter.destroy();
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        if (paramMenuItem.getItemId() == 16908332) {
            this.mPresenter.goUp();
            return true;
        }
        return super.onOptionsItemSelected(paramMenuItem);
    }

    protected void onPause() {
        int i = this.mWebViewContainer.getChildCount();
        for (int j = 0; j < i; j++) {
            ((WebView) this.mWebViewContainer.getChildAt(j)).onPause();
        }
        super.onPause();
    }

    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        this.mPresenter.updateDynamicOptionsMenuItems(paramMenu);
        return true;
    }

    protected void onResume() {
        super.onResume();
        int i = this.mWebViewContainer.getChildCount();
        for (int j = 0; j < i; j++) {
            ((WebView) this.mWebViewContainer.getChildAt(j)).onResume();
        }
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        this.mPresenter.saveState(paramBundle);
        super.onSaveInstanceState(paramBundle);
    }

    protected void onStart() {
        super.onStart();
        Intent localIntent = getIntent();
        if (localIntent == null) {
            this.mPresenter.start(null);
            return;
        }
        if ("com.google.android.velvet.ui.InAppWebPageActivity.ACTION_RELAUNCH".equals(localIntent.getAction())) {
            this.mPresenter.relaunch(localIntent.getExtras());
            return;
        }
        this.mPresenter.start(Request.forIntent(localIntent));
    }

    public void removeWebView(WebViewWrapper paramWebViewWrapper) {
        WebView localWebView = paramWebViewWrapper.getWebView();
        localWebView.animate().cancel();
        localWebView.animate().alpha(0.0F).setDuration(this.mFadeDuration).setListener(destroyOnAnimationEnd(localWebView));
    }

    public void showError(int paramInt) {
        this.mErrorCard.animate().cancel();
        this.mErrorMessageView.setText(paramInt);
        this.mErrorCard.setVisibility(0);
        this.mErrorCard.animate().alpha(1.0F).setDuration(this.mFadeDuration);
    }

    public void showErrorToast(int paramInt) {
        Toast.makeText(this, paramInt, 0).show();
    }

    public void showLoadingIndicator() {
        this.mLoadingIndicator.animate().cancel();
        this.mLoadingIndicator.setVisibility(0);
        this.mLoadingIndicator.animate().alpha(1.0F).setDuration(this.mFadeDuration);
    }

    public void showWebView(WebViewWrapper paramWebViewWrapper) {
        WebView localWebView = paramWebViewWrapper.getWebView();
        localWebView.setAlpha(0.0F);
        this.mWebViewContainer.addView(localWebView);
        localWebView.animate().alpha(1.0F).setDuration(this.mFadeDuration);
    }

    private class TryAgainButtonListener
            implements View.OnClickListener {
        private TryAgainButtonListener() {
        }

        public void onClick(View paramView) {
            InAppWebPageActivity.this.mPresenter.tryAgain();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.InAppWebPageActivity

 * JD-Core Version:    0.7.0.1

 */