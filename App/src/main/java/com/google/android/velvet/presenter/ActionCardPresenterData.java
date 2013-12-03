package com.google.android.velvet.presenter;

import com.google.android.voicesearch.SearchCardController;
import com.google.android.voicesearch.fragments.AbstractCardController;
import com.google.android.voicesearch.fragments.AbstractCardView;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class ActionCardPresenterData {
    @Nonnull
    private final SearchCardController mCardController;
    @Nonnull
    private final AbstractCardController<VoiceAction, ?> mController;
    private int mState;
    @Nullable
    private AbstractCardView<?> mView;

    ActionCardPresenterData(SearchCardController paramSearchCardController, AbstractCardController<VoiceAction, ?> paramAbstractCardController) {
        this.mCardController = ((SearchCardController) Preconditions.checkNotNull(paramSearchCardController));
        this.mController = ((AbstractCardController) Preconditions.checkNotNull(paramAbstractCardController));
    }

    SearchCardController getCardController() {
        return this.mCardController;
    }

    AbstractCardController<VoiceAction, ?> getController() {
        return this.mController;
    }

    AbstractCardView<?> getView() {
        return this.mView;
    }

    boolean isCreateViewPending() {
        return this.mView == null;
    }

    boolean isRemovePending() {
        return this.mState == 2;
    }

    boolean isShowPending() {
        return (this.mView != null) && (this.mState == 0);
    }

    void remove() {
        this.mState = 2;
    }

    void setView(AbstractCardView<?> paramAbstractCardView) {
        if (this.mView == null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mView = ((AbstractCardView) Preconditions.checkNotNull(paramAbstractCardView));
            return;
        }
    }

    void show() {
        Preconditions.checkState(isShowPending());
        this.mState = 1;
    }

    public String toString() {
        StringBuilder localStringBuilder = new StringBuilder("CardData {");
        localStringBuilder.append("controller=").append(this.mController);
        localStringBuilder.append(", view=").append(this.mView);
        localStringBuilder.append(", state=");
        switch (this.mState) {
        }
        for (; ; ) {
            localStringBuilder.append("}");
            return localStringBuilder.toString();
            localStringBuilder.append("show_pending");
            continue;
            localStringBuilder.append("shown");
            continue;
            localStringBuilder.append("remove_pending");
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.ActionCardPresenterData

 * JD-Core Version:    0.7.0.1

 */