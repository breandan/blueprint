package com.google.android.velvet.presenter;

import android.graphics.drawable.Drawable;

import com.google.android.shared.util.ExtraPreconditions;

import javax.annotation.Nullable;

public class ContextHeaderPresenter {
    private boolean mEnabled;
    private final ContextHeaderUi mUi;

    public ContextHeaderPresenter(ContextHeaderUi paramContextHeaderUi) {
        this.mUi = paramContextHeaderUi;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setContextHeader(Drawable paramDrawable, boolean paramBoolean, @Nullable View.OnClickListener paramOnClickListener) {
        this.mUi.setContextImageDrawable(paramDrawable, paramBoolean, paramOnClickListener);
    }

    public void setEnabled(boolean paramBoolean) {
        ExtraPreconditions.checkMainThread();
        this.mEnabled = paramBoolean;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.ContextHeaderPresenter

 * JD-Core Version:    0.7.0.1

 */