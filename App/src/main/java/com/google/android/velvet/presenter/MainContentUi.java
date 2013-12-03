package com.google.android.velvet.presenter;

import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.shared.ui.CoScrollContainer;
import com.google.android.shared.ui.ScrollViewControl;

public abstract interface MainContentUi {
    public abstract SuggestionGridLayout getCardsView();

    public abstract ScrollViewControl getScrollViewControl();

    public abstract CoScrollContainer getScrollingContainer();

    public abstract void setMainContentBackCollapsibleMargin(int paramInt);

    public abstract void setMainContentFrontScrimVisible(boolean paramBoolean);

    public abstract void setMatchPortraitMode(boolean paramBoolean);

    public abstract void setSearchPlateStuckToScrollingView(boolean paramBoolean);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.MainContentUi

 * JD-Core Version:    0.7.0.1

 */