package com.google.android.velvet.presenter;

import android.os.Bundle;
import android.text.Spanned;

import com.google.android.search.shared.api.SearchPlateUi;

public abstract interface VelvetSearchPlateUi
        extends SearchPlateUi {
    public abstract void focusQueryAndShowKeyboard();

    public abstract void restoreInstanceState(Bundle paramBundle);

    public abstract void saveInstanceState(Bundle paramBundle);

    public abstract void setPresenter(SearchPlatePresenter paramSearchPlatePresenter);

    public abstract void setTextQueryCorrections(Spanned paramSpanned);

    public abstract void showProgress(boolean paramBoolean);

    public abstract void unfocusQueryAndHideKeyboard();
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.VelvetSearchPlateUi

 * JD-Core Version:    0.7.0.1

 */