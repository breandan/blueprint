package com.google.android.velvet.presenter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.ui.CoScrollContainer;
import com.google.android.shared.util.IntentStarter;
import com.google.android.velvet.ui.MainContentView;

import java.io.PrintWriter;

public abstract interface VelvetUi {
    public abstract void assertNotInLayout();

    public abstract void clearIntent();

    public abstract void closeOptionsMenu();

    public abstract void doABarrelRoll();

    public abstract void dumpActivityState(String paramString, PrintWriter paramPrintWriter);

    public abstract void finish();

    public abstract Activity getActivity();

    public abstract ContextHeaderUi getContextHeaderUi();

    public abstract FooterUi getFooterUi();

    public abstract Intent getIntent();

    public abstract IntentStarter getIntentStarter();

    public abstract LayoutInflater getLayoutInflater();

    public abstract MainContentView getMainContentBack();

    public abstract MainContentView getMainContentFront();

    public abstract View getReminderPeekView();

    public abstract View getRemindersFooterIcon();

    public abstract CoScrollContainer getScrollingContainer();

    public abstract int getSearchPlateHeight();

    public abstract View getTrainingFooterIcon();

    public abstract View getTrainingPeekIcon();

    public abstract View getTrainingPeekView();

    public abstract VelvetSearchPlateUi getVelvetSearchPlateUi();

    public abstract void indicateRemoveFromHistoryFailed();

    public abstract boolean isChangingConfigurations();

    public abstract void overridePendingTransition(int paramInt1, int paramInt2);

    public abstract void setFadeSearchPlateOverHeader(boolean paramBoolean);

    public abstract void setFooterStickiness(int paramInt, boolean paramBoolean);

    public abstract void setSearchPlateStickiness(int paramInt, boolean paramBoolean1, boolean paramBoolean2);

    public abstract void setShowContextHeader(boolean paramBoolean1, boolean paramBoolean2);

    public abstract void showDebugDialog(String paramString);

    public abstract void showDogfoodIndicator();

    public abstract void showErrorToast(int paramInt);

    public abstract void showFooter();

    public abstract void showOptionsMenu(View paramView);

    public abstract void showRemoveFromHistoryDialog(Suggestion paramSuggestion, Runnable paramRunnable);

    public abstract void showSearchPlate();

    public abstract void startActivity(Intent paramIntent);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.VelvetUi

 * JD-Core Version:    0.7.0.1

 */