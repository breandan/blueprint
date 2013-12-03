package com.google.android.voicesearch;

import android.app.DialogFragment;
import android.content.Context;
import android.widget.Toast;

import com.google.android.search.core.Feature;
import com.google.android.search.core.SearchError;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.search.core.discoursecontext.Mention;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Clock;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.actions.CardDecision;
import com.google.android.velvet.actions.CardDecisionFactory;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.common.base.Supplier;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nullable;

public class DialogCardController
        implements CardController {
    private final ExecutorService mBackgroundExecutor;
    private CardDecision mCardDecision;
    private final CardDecisionFactory mCardDecisionFactory;
    private final Clock mClock;
    protected final Context mContext;
    private final Supplier<DiscourseContext> mDiscourseContextSupplier;
    private DialogFragment mFragment;
    private boolean mShown;

    public DialogCardController(Context paramContext, VoiceSearchServices paramVoiceSearchServices, CardDecisionFactory paramCardDecisionFactory, Clock paramClock, Supplier<DiscourseContext> paramSupplier) {
        this.mContext = paramContext;
        this.mBackgroundExecutor = paramVoiceSearchServices.getExecutorService();
        this.mCardDecisionFactory = paramCardDecisionFactory;
        this.mClock = paramClock;
        this.mDiscourseContextSupplier = paramSupplier;
    }

    public void cancelAction() {
    }

    public boolean cancelCountDown() {
        return false;
    }

    public int getActionTypeLog() {
        return 0;
    }

    public Executor getBackgroundExecutor() {
        return this.mBackgroundExecutor;
    }

    public CharSequence getDisplayPrompt(VoiceAction paramVoiceAction) {
        if (this.mCardDecision != null) {
            return this.mCardDecision.getDisplayPrompt();
        }
        return null;
    }

    @Deprecated
    public Query getQuery() {
        return null;
    }

    public boolean isFollowOnEnabledForRequest() {
        return false;
    }

    @Deprecated
    public boolean isTtsPlaying() {
        return false;
    }

    public void logAttach() {
    }

    public void logCancelCountDownByUser() {
    }

    public void logExecute(boolean paramBoolean) {
    }

    public void logOpenExternalApp() {
    }

    public void mentionEntity(@Nullable Object paramObject) {
        if ((Feature.DISCOURSE_CONTEXT.isEnabled()) && (paramObject != null)) {
            ((DiscourseContext) this.mDiscourseContextSupplier.get()).mention(paramObject, new Mention(this.mClock.currentTimeMillis()));
        }
    }

    public void onCardActionComplete() {
        this.mFragment.dismiss();
    }

    public void onDismissed(VoiceAction paramVoiceAction) {
    }

    public void onUserInteraction() {
    }

    public void post(MainContentPresenter.Transaction paramTransaction) {
        while (!paramTransaction.prepare()) {
        }
        paramTransaction.commit(null);
    }

    public void removeVoiceAction(VoiceAction paramVoiceAction) {
    }

    public void retryError(SearchError paramSearchError) {
    }

    public void setFragment(DialogFragment paramDialogFragment) {
        this.mFragment = paramDialogFragment;
    }

    public boolean showCard(VoiceAction paramVoiceAction) {
        updateCardDecision(paramVoiceAction);
        if (!this.mShown) {
            this.mShown = true;
            return true;
        }
        return false;
    }

    public void showToast(int paramInt) {
        Toast.makeText(this.mContext, paramInt, 0).show();
    }

    public long startCountDown(VoiceAction paramVoiceAction, Runnable paramRunnable) {
        return 0L;
    }

    public boolean takeStartCountDown(VoiceAction paramVoiceAction) {
        return false;
    }

    public void updateActionTts(VoiceAction paramVoiceAction) {
    }

    public void updateCardDecision(VoiceAction paramVoiceAction) {
        this.mCardDecision = this.mCardDecisionFactory.makeDecision(paramVoiceAction, ActionData.NONE, null);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.DialogCardController

 * JD-Core Version:    0.7.0.1

 */