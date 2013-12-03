package com.google.android.voicesearch;

import com.google.android.search.core.SearchError;
import com.google.android.search.shared.api.Query;
import com.google.android.voicesearch.fragments.action.VoiceAction;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

public abstract interface CardController {
    public abstract void cancelAction();

    public abstract boolean cancelCountDown();

    public abstract int getActionTypeLog();

    public abstract Executor getBackgroundExecutor();

    public abstract CharSequence getDisplayPrompt(VoiceAction paramVoiceAction);

    @Deprecated
    public abstract Query getQuery();

    public abstract boolean isFollowOnEnabledForRequest();

    @Deprecated
    public abstract boolean isTtsPlaying();

    public abstract void logAttach();

    public abstract void logCancelCountDownByUser();

    public abstract void logExecute(boolean paramBoolean);

    public abstract void logOpenExternalApp();

    public abstract void mentionEntity(@Nullable Object paramObject);

    public abstract void onCardActionComplete();

    public abstract void onDismissed(VoiceAction paramVoiceAction);

    public abstract void onUserInteraction();

    public abstract void post(MainContentPresenter.Transaction paramTransaction);

    public abstract void removeVoiceAction(VoiceAction paramVoiceAction);

    public abstract void retryError(SearchError paramSearchError);

    public abstract boolean showCard(VoiceAction paramVoiceAction);

    public abstract void showToast(int paramInt);

    public abstract long startCountDown(VoiceAction paramVoiceAction, Runnable paramRunnable);

    public abstract boolean takeStartCountDown(VoiceAction paramVoiceAction);

    public abstract void updateActionTts(VoiceAction paramVoiceAction);

    public abstract void updateCardDecision(VoiceAction paramVoiceAction);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.CardController

 * JD-Core Version:    0.7.0.1

 */