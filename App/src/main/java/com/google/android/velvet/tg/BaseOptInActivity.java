package com.google.android.velvet.tg;

import android.accounts.Account;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.BulletSpan;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.shared.util.ExecutorAsyncTask;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.main.NowOptInHelper;
import com.google.android.sidekick.main.UserClientIdManager;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Preconditions;

import java.util.Locale;
import java.util.concurrent.Executor;

public abstract class BaseOptInActivity
        extends Activity
        implements View.OnClickListener {
    protected int OPTIN_STATUS_CANCELED = 2;
    protected int OPTIN_STATUS_ERROR = 1;
    protected int OPTIN_STATUS_SUCCESS = 0;
    private final String mAnalyticsActionPrefix;
    Executor mBgExecutor;
    private PredictiveCardsPreferences mCardsPrefs;
    private TextView mLearnMoreLink;
    LoginHelper mLoginHelper;
    private Button mMaybeLaterButton;
    private NetworkClient mNetworkClient;
    private NowOptInHelper mNowOptInHelper;
    NowOptInSettings mNowOptInSettings;
    private Button mOptInButton;
    private OptInTask mOptInTask;
    ScheduledSingleThreadedExecutor mUiExecutor;
    private UserClientIdManager mUserClientIdManager;
    private UserInteractionLogger mUserInteractionLogger;

    protected BaseOptInActivity(String paramString) {
        this.mAnalyticsActionPrefix = paramString;
    }

    private void handleOptInInternal() {
        logAnalyticsButtonPress("ACCEPT_OPT_IN");
        setButtonsEnabled(false);
        handleOptIn();
    }

    protected void addBullets(SpannableStringBuilder paramSpannableStringBuilder, String... paramVarArgs) {
        int i = paramSpannableStringBuilder.length();
        int j = 1;
        int k = paramVarArgs.length;
        int m = 0;
        while (m < k) {
            String str = paramVarArgs[m];
            if (j == 0) {
                paramSpannableStringBuilder.append("\n");
            }
            paramSpannableStringBuilder.append(str);
            m++;
            j = 0;
        }
        paramSpannableStringBuilder.setSpan(new LeadingMarginSpan.Standard(16), i, paramSpannableStringBuilder.length(), 33);
        int n = getResources().getDimensionPixelSize(2131689864);
        int i1 = paramVarArgs.length;
        for (int i2 = 0; i2 < i1; i2++) {
            int i3 = i + paramVarArgs[i2].length();
            paramSpannableStringBuilder.setSpan(new BulletSpan(n), i, i3, 33);
            i = i3 + 1;
        }
    }

    protected abstract void finishOptIn(int paramInt);

    protected void handleDecline() {
        logAnalyticsButtonPress("DECLINE_OPT_IN");
        Account localAccount = this.mLoginHelper.getAccount();
        if (localAccount != null) {
            this.mNowOptInSettings.optAccountOut(localAccount);
        }
        this.mNowOptInSettings.setFirstRunScreensShown();
        this.mCardsPrefs.clearWorkingConfiguration();
        finishOptIn(this.OPTIN_STATUS_CANCELED);
    }

    protected abstract void handleOptIn();

    protected void initializeOptInView() {
        this.mOptInButton = ((Button) findViewById(2131296601));
        this.mMaybeLaterButton = ((Button) findViewById(2131296600));
        this.mLearnMoreLink = ((TextView) findViewById(2131296603));
        if (Locale.getDefault().getCountry().equals("KR")) {
            this.mOptInButton.setText(2131362490);
            TextView localTextView = (TextView) findViewById(2131296598);
            String str = getString(2131362000);
            localTextView.setMovementMethod(LinkMovementMethod.getInstance());
            localTextView.setText(Html.fromHtml(getString(2131362491, new Object[]{str})));
            localTextView.setVisibility(0);
        }
        this.mOptInButton.setOnClickListener(this);
        this.mMaybeLaterButton.setOnClickListener(this);
        this.mLearnMoreLink.setOnClickListener(this);
    }

    protected void injectBaseDependencies(NowOptInSettings paramNowOptInSettings, LoginHelper paramLoginHelper, UserInteractionLogger paramUserInteractionLogger, PredictiveCardsPreferences paramPredictiveCardsPreferences, UserClientIdManager paramUserClientIdManager, NetworkClient paramNetworkClient, NowOptInHelper paramNowOptInHelper, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor) {
        this.mNowOptInSettings = paramNowOptInSettings;
        this.mLoginHelper = paramLoginHelper;
        this.mUserInteractionLogger = paramUserInteractionLogger;
        this.mCardsPrefs = paramPredictiveCardsPreferences;
        this.mUserClientIdManager = paramUserClientIdManager;
        this.mNetworkClient = paramNetworkClient;
        this.mNowOptInHelper = paramNowOptInHelper;
        this.mUiExecutor = paramScheduledSingleThreadedExecutor;
        this.mBgExecutor = paramExecutor;
    }

    protected boolean isNetworkAvailable() {
        return this.mNetworkClient.isNetworkAvailable();
    }

    protected void loadDependencies() {
        VelvetServices localVelvetServices = VelvetServices.get();
        injectBaseDependencies(localVelvetServices.getCoreServices().getNowOptInSettings(), localVelvetServices.getCoreServices().getLoginHelper(), localVelvetServices.getCoreServices().getUserInteractionLogger(), localVelvetServices.getCoreServices().getPredictiveCardsPreferences(), localVelvetServices.getSidekickInjector().getUserClientIdManager(), localVelvetServices.getSidekickInjector().getNetworkClient(), localVelvetServices.getSidekickInjector().getNowOptInHelper(), localVelvetServices.getAsyncServices().getUiThreadExecutor(), localVelvetServices.getAsyncServices().getPooledBackgroundExecutorService());
    }

    protected void logAnalyticsButtonPress(String paramString) {
        this.mUserInteractionLogger.logAnalyticsAction("BUTTON_PRESS", this.mAnalyticsActionPrefix + "_" + paramString);
    }

    protected void logAnalyticsView(String paramString) {
        if (paramString != null) {
            this.mUserInteractionLogger.logView(this.mAnalyticsActionPrefix + "_" + paramString);
            return;
        }
        this.mUserInteractionLogger.logView(this.mAnalyticsActionPrefix);
    }

    public void onClick(View paramView) {
        if (paramView == this.mOptInButton) {
            handleOptInInternal();
        }
        do {
            return;
            if (paramView == this.mMaybeLaterButton) {
                handleDecline();
                return;
            }
        } while (paramView != this.mLearnMoreLink);
        logAnalyticsButtonPress("LEARN_MORE");
        new LearnMoreDialog().show(getFragmentManager(), null);
    }

    public void onCreate(Bundle paramBundle) {
        if (this.mNowOptInSettings == null) {
            loadDependencies();
        }
        super.onCreate(paramBundle);
        setContentView(2130968687);
    }

    public void onDestroy() {
        if (this.mOptInTask != null) {
            this.mOptInTask.cancel();
            this.mOptInTask = null;
        }
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        logAnalyticsView(null);
    }

    protected void optInAccount(Account paramAccount) {
        Preconditions.checkNotNull(paramAccount);
        if (!isNetworkAvailable()) {
            finishOptIn(this.OPTIN_STATUS_ERROR);
            return;
        }
        this.mUserClientIdManager.setNeedToRegenerateAndStoreRandomClientId();
        this.mOptInTask = new OptInTask(paramAccount, this.mUiExecutor, this.mBgExecutor);
        this.mOptInTask.execute(new Void[0]);
    }

    protected void setButtonsEnabled(boolean paramBoolean) {
        this.mOptInButton.setEnabled(paramBoolean);
        this.mMaybeLaterButton.setEnabled(paramBoolean);
    }

    public static class LearnMoreDialog
            extends DialogFragment {
        public Dialog onCreateDialog(Bundle paramBundle) {
            WebView localWebView = new WebView(getActivity());
            VelvetServices.get().getCoreServices().getUserAgentHelper().onWebViewCreated(localWebView);
            Activity localActivity = getActivity();
            Object[] arrayOfObject1 = new Object[2];
            if (LayoutUtils.isDefaultLocaleRtl()) {
            }
            for (String str = "rtl"; ; str = "ltr") {
                arrayOfObject1[0] = str;
                Object[] arrayOfObject2 = new Object[2];
                arrayOfObject2[0] = localActivity.getString(2131362518);
                arrayOfObject2[1] = localActivity.getString(2131362001);
                arrayOfObject1[1] = localActivity.getString(2131362517, arrayOfObject2);
                localWebView.loadData(String.format("<div dir=\"%s\">%s</div>", arrayOfObject1), "text/html; charset=utf-8", "utf-8");
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
                localBuilder.setView(localWebView);
                localBuilder.setPositiveButton(getActivity().getString(17039370), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        BaseOptInActivity.LearnMoreDialog.this.dismiss();
                    }
                });
                return localBuilder.create();
            }
        }

        public void onStart() {
            super.onStart();
            Dialog localDialog = getDialog();
            if (localDialog != null) {
                localDialog.getWindow().setLayout(-1, -1);
            }
        }
    }

    class OptInTask
            extends ExecutorAsyncTask<Void, Boolean> {
        private final Account mAccount;

        public OptInTask(Account paramAccount, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor) {
            super(paramExecutor);
            this.mAccount = paramAccount;
        }

        protected Boolean doInBackground(Void... paramVarArgs) {
            return Boolean.valueOf(BaseOptInActivity.this.mNowOptInHelper.optIn(this.mAccount));
        }

        protected void onPostExecute(Boolean paramBoolean) {
            BaseOptInActivity localBaseOptInActivity;
            if (BaseOptInActivity.this.mOptInTask == this) {
                localBaseOptInActivity = BaseOptInActivity.this;
                if (!paramBoolean.booleanValue()) {
                    break label37;
                }
            }
            label37:
            for (int i = BaseOptInActivity.this.OPTIN_STATUS_SUCCESS; ; i = BaseOptInActivity.this.OPTIN_STATUS_ERROR) {
                localBaseOptInActivity.finishOptIn(i);
                return;
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.tg.BaseOptInActivity

 * JD-Core Version:    0.7.0.1

 */