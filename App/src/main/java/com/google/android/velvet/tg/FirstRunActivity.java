package com.google.android.velvet.tg;

import android.accounts.Account;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.googlequicksearchbox.SearchActivity;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExecutorAsyncTask;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

public class FirstRunActivity
        extends BaseOptInActivity {
    protected static final String ACCOUNT_SELECTOR_DIALOG_TAG = "selectAccount";
    private AccountConfigurationHelper mAccountConfigurationHelper;
    private List<AccountConfigurationHelper.AnnotatedAccount> mAnnotatedAccounts;
    private VelvetBackgroundTasks mBackgroundTasks;
    private Clock mClock;
    private Button mNextButton;
    private String mOptInAccountName;
    private boolean mShowingOptInView;
    private boolean mSinglePageMode;
    private boolean mSkipToOptIn;

    public FirstRunActivity() {
        super("FIRST_RUN");
    }

    @Nullable
    private AccountConfigurationHelper.AnnotatedAccount findAnnotatedAccount(String paramString) {
        Iterator localIterator = this.mAnnotatedAccounts.iterator();
        while (localIterator.hasNext()) {
            AccountConfigurationHelper.AnnotatedAccount localAnnotatedAccount = (AccountConfigurationHelper.AnnotatedAccount) localIterator.next();
            if (paramString.equals(localAnnotatedAccount.getAccount().name)) {
                return localAnnotatedAccount;
            }
        }
        return null;
    }

    private List<AccountConfigurationHelper.AnnotatedAccount> getEnabledAccounts() {
        ArrayList localArrayList = Lists.newArrayListWithExpectedSize(this.mAnnotatedAccounts.size());
        Iterator localIterator = this.mAnnotatedAccounts.iterator();
        while (localIterator.hasNext()) {
            AccountConfigurationHelper.AnnotatedAccount localAnnotatedAccount = (AccountConfigurationHelper.AnnotatedAccount) localIterator.next();
            if (localAnnotatedAccount.isNowEnabled()) {
                localArrayList.add(localAnnotatedAccount);
            }
        }
        return localArrayList;
    }

    private void initIntentParams() {
        this.mSkipToOptIn = getIntent().getBooleanExtra("skip_to_end", false);
        this.mSinglePageMode = getIntent().getBooleanExtra("single_page", false);
        this.mOptInAccountName = getIntent().getStringExtra("account_name");
    }

    private void initSampleCards() {
        ((SampleCardsView) findViewById(2131296596)).init(getLayoutInflater(), this.mClock);
    }

    private void showInitialView() {
        if ((this.mShowingOptInView) || (this.mSkipToOptIn) || (this.mSinglePageMode)) {
            showOptInView();
            return;
        }
        showIntroView();
    }

    private void showIntroView() {
        setContentView(2130968679);
        logAnalyticsView("INTRO");
        initSampleCards();
        this.mNextButton = ((Button) findViewById(2131296454));
        this.mNextButton.setOnClickListener(this);
        this.mNextButton.requestFocus();
        this.mShowingOptInView = false;
    }

    private void showNetworkErrorToast() {
        Toast.makeText(this, 2131362173, 0).show();
    }

    private void showOptInView() {
        int i;
        if (this.mSinglePageMode) {
            i = 2130968686;
            setContentView(i);
            if (!this.mSinglePageMode) {
                break label67;
            }
        }
        label67:
        for (String str = "SINGLE_PAGE"; ; str = "OPTIN") {
            logAnalyticsView(str);
            if (this.mSinglePageMode) {
                initSampleCards();
            }
            initializeOptInView();
            findViewById(2131296601).requestFocus();
            this.mShowingOptInView = true;
            return;
            i = 2130968680;
            break;
        }
    }

    protected void finishOptIn(int paramInt) {
        if (!this.mSkipToOptIn) {
            this.mBackgroundTasks.forceRunInterruptingOngoing("refresh_search_domain_and_cookies");
        }
        Intent localIntent = new Intent(this, SearchActivity.class);
        if (paramInt == this.OPTIN_STATUS_SUCCESS) {
            localIntent.setAction("android.intent.action.ASSIST");
        }
        for (; ; ) {
            localIntent.setFlags(67108864);
            localIntent.putExtra("from-first-run", true);
            startActivity(localIntent);
            finish();
            return;
            if (paramInt == this.OPTIN_STATUS_ERROR) {
                showNetworkErrorToast();
            }
        }
    }

    protected void handleOptIn() {
        if (this.mAnnotatedAccounts.size() == 0) {
            Toast.makeText(this, 2131362345, 0).show();
            startActivity(new Intent("android.settings.ADD_ACCOUNT_SETTINGS"));
            finish();
            return;
        }
        if (this.mOptInAccountName != null) {
            AccountConfigurationHelper.AnnotatedAccount localAnnotatedAccount = findAnnotatedAccount(this.mOptInAccountName);
            if (localAnnotatedAccount != null) {
                optInAccount(localAnnotatedAccount.getAccount());
                return;
            }
        }
        List localList = getEnabledAccounts();
        if (localList.size() == 1) {
            Account localAccount = ((AccountConfigurationHelper.AnnotatedAccount) localList.get(0)).getAccount();
            if (localAccount.equals(this.mLoginHelper.getAccount())) {
                optInAccount(localAccount);
                return;
            }
        }
        new AccountSelectorDialog().show(getFragmentManager(), "selectAccount");
    }

    protected void initializeOptInView() {
        super.initializeOptInView();
        TextView localTextView = (TextView) findViewById(2131296602);
        SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(getResources().getString(2131362471));
        localSpannableStringBuilder.setSpan(new StyleSpan(1), 0, -1 + localSpannableStringBuilder.length(), 33);
        localSpannableStringBuilder.append("\n");
        String[] arrayOfString = new String[2];
        arrayOfString[0] = getResources().getString(2131362472);
        arrayOfString[1] = getResources().getString(2131362473);
        addBullets(localSpannableStringBuilder, arrayOfString);
        localTextView.setText(localSpannableStringBuilder);
    }

    protected void injectDependencies(Clock paramClock, VelvetBackgroundTasks paramVelvetBackgroundTasks, AccountConfigurationHelper paramAccountConfigurationHelper) {
        this.mClock = paramClock;
        this.mBackgroundTasks = paramVelvetBackgroundTasks;
        this.mAccountConfigurationHelper = paramAccountConfigurationHelper;
    }

    protected void loadDependencies() {
        super.loadDependencies();
        VelvetServices localVelvetServices = VelvetServices.get();
        injectDependencies(localVelvetServices.getCoreServices().getClock(), localVelvetServices.getCoreServices().getBackgroundTasks(), new AccountConfigurationHelper(localVelvetServices.getCoreServices().getGmsLocationReportingHelper(), localVelvetServices.getCoreServices().getNowOptInSettings()));
    }

    public void onBackPressed() {
        if (this.mSkipToOptIn) {
            handleDecline();
            return;
        }
        if ((!this.mSinglePageMode) && (this.mShowingOptInView)) {
            showIntroView();
            return;
        }
        super.onBackPressed();
    }

    public void onClick(View paramView) {
        if ((this.mNextButton != null) && (paramView == this.mNextButton)) {
            showOptInView();
            logAnalyticsButtonPress("NEXT");
            return;
        }
        super.onClick(paramView);
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        initIntentParams();
        if (!isNetworkAvailable()) {
            finishOptIn(this.OPTIN_STATUS_ERROR);
            return;
        }
        if (paramBundle != null) {
            this.mShowingOptInView = paramBundle.getBoolean("optin", false);
            boolean[] arrayOfBoolean = paramBundle.getBooleanArray("enabled");
            Parcelable[] arrayOfParcelable = paramBundle.getParcelableArray("accounts");
            if ((arrayOfBoolean != null) && (arrayOfParcelable != null) && (arrayOfBoolean.length == arrayOfParcelable.length)) {
                this.mAnnotatedAccounts = Lists.newArrayList();
                for (int i = 0; i < arrayOfBoolean.length; i++) {
                    Account localAccount = (Account) arrayOfParcelable[i];
                    this.mAnnotatedAccounts.add(new AccountConfigurationHelper.AnnotatedAccount(localAccount, arrayOfBoolean[i]));
                }
            }
        }
        if (this.mAnnotatedAccounts == null) {
            new UpdateAccountConfigurationsTask(this.mLoginHelper.getAllAccounts(), this.mUiExecutor, this.mBgExecutor).execute(new Void[0]);
            return;
        }
        showInitialView();
    }

    protected void onNewIntent(Intent paramIntent) {
        setIntent(paramIntent);
        initIntentParams();
        if (this.mAnnotatedAccounts != null) {
            showInitialView();
        }
    }

    public void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putBoolean("optin", this.mShowingOptInView);
        if (this.mAnnotatedAccounts != null) {
            boolean[] arrayOfBoolean = new boolean[this.mAnnotatedAccounts.size()];
            Account[] arrayOfAccount = new Account[this.mAnnotatedAccounts.size()];
            for (int i = 0; i < this.mAnnotatedAccounts.size(); i++) {
                AccountConfigurationHelper.AnnotatedAccount localAnnotatedAccount = (AccountConfigurationHelper.AnnotatedAccount) this.mAnnotatedAccounts.get(i);
                arrayOfBoolean[i] = localAnnotatedAccount.isNowEnabled();
                arrayOfAccount[i] = localAnnotatedAccount.getAccount();
            }
            paramBundle.putBooleanArray("enabled", arrayOfBoolean);
            paramBundle.putParcelableArray("accounts", arrayOfAccount);
        }
    }

    public static class AccountSelectorDialog
            extends DialogFragment {
        public void onCancel(DialogInterface paramDialogInterface) {
            ((FirstRunActivity) getActivity()).setButtonsEnabled(true);
            super.onCancel(paramDialogInterface);
        }

        public Dialog onCreateDialog(Bundle paramBundle) {
            final List localList = ((FirstRunActivity) getActivity()).getEnabledAccounts();
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
            localBuilder.setTitle(2131362346);
            localBuilder.setAdapter(new ArrayAdapter(getActivity(), 17367043, localList), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    AccountConfigurationHelper.AnnotatedAccount localAnnotatedAccount = (AccountConfigurationHelper.AnnotatedAccount) localList.get(paramAnonymousInt);
                    ((FirstRunActivity) FirstRunActivity.AccountSelectorDialog.this.getActivity()).optInAccount(localAnnotatedAccount.getAccount());
                }
            });
            return localBuilder.create();
        }
    }

    class UpdateAccountConfigurationsTask
            extends ExecutorAsyncTask<Void, List<AccountConfigurationHelper.AnnotatedAccount>> {
        private final Account[] mAccounts;

        public UpdateAccountConfigurationsTask(Account[] paramArrayOfAccount, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor) {
            super(paramExecutor);
            this.mAccounts = paramArrayOfAccount;
        }

        protected List<AccountConfigurationHelper.AnnotatedAccount> doInBackground(Void... paramVarArgs) {
            return FirstRunActivity.this.mAccountConfigurationHelper.updateAccountConfigurations(this.mAccounts);
        }

        protected void onPostExecute(List<AccountConfigurationHelper.AnnotatedAccount> paramList) {
            FirstRunActivity.access$102(FirstRunActivity.this, paramList);
            int i;
            int j;
            if (FirstRunActivity.this.mOptInAccountName != null) {
                AccountConfigurationHelper.AnnotatedAccount localAnnotatedAccount2 = FirstRunActivity.this.findAnnotatedAccount(FirstRunActivity.this.mOptInAccountName);
                if (localAnnotatedAccount2 == null) {
                    Log.w("FirstRunActivity", "Failed to retrieve account configuration for " + FirstRunActivity.this.mOptInAccountName);
                    FirstRunActivity.this.finishOptIn(FirstRunActivity.this.OPTIN_STATUS_ERROR);
                    return;
                }
                if (!localAnnotatedAccount2.isNowEnabled()) {
                    FirstRunActivity.this.finishOptIn(FirstRunActivity.this.OPTIN_STATUS_CANCELED);
                }
            } else {
                i = 0;
                j = 0;
                Account[] arrayOfAccount = this.mAccounts;
                int k = arrayOfAccount.length;
                int m = 0;
                if (m < k) {
                    Account localAccount = arrayOfAccount[m];
                    AccountConfigurationHelper.AnnotatedAccount localAnnotatedAccount1 = FirstRunActivity.this.findAnnotatedAccount(localAccount.name);
                    if (localAnnotatedAccount1 == null) {
                        Log.w("FirstRunActivity", "Failed to retrieve account configuration for " + localAccount.name);
                        j = 1;
                    }
                    for (; ; ) {
                        m++;
                        break;
                        if (localAnnotatedAccount1.isNowEnabled()) {
                            i = 1;
                        }
                    }
                }
                if (this.mAccounts.length != 0) {
                    break label240;
                }
                FirstRunActivity.this.mNowOptInSettings.setFirstRunScreensShown();
            }
            for (; ; ) {
                FirstRunActivity.this.showInitialView();
                return;
                label240:
                if (i == 0) {
                    FirstRunActivity localFirstRunActivity = FirstRunActivity.this;
                    if (j != 0) {
                    }
                    for (int n = FirstRunActivity.this.OPTIN_STATUS_ERROR; ; n = FirstRunActivity.this.OPTIN_STATUS_CANCELED) {
                        localFirstRunActivity.finishOptIn(n);
                        if (j != 0) {
                            break;
                        }
                        FirstRunActivity.this.mNowOptInSettings.setFirstRunScreensShown();
                        return;
                    }
                }
                if (j != 0) {
                    FirstRunActivity.this.showNetworkErrorToast();
                }
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.tg.FirstRunActivity

 * JD-Core Version:    0.7.0.1

 */