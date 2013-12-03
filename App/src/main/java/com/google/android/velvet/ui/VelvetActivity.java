package com.google.android.velvet.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.search.core.SearchController;
import com.google.android.search.core.debug.DebugDialogFragment;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.search.core.util.LoggingIntentStarter;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.ui.SuggestionUiUtils;
import com.google.android.shared.ui.CoScrollContainer;
import com.google.android.shared.ui.OnScrollViewFader;
import com.google.android.shared.ui.OnScrollViewHider;
import com.google.android.shared.util.ActivityIntentStarter;
import com.google.android.shared.util.CancellableSingleThreadedExecutor;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.Util;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.velvet.presenter.ContextHeaderUi;
import com.google.android.velvet.presenter.FooterUi;
import com.google.android.velvet.presenter.VelvetPresenter;
import com.google.android.velvet.presenter.VelvetSearchPlateUi;
import com.google.android.velvet.presenter.VelvetUi;
import com.google.android.velvet.ui.widget.VelvetSearchPlate;
import com.google.android.velvet.util.VelvetStartupLatencyTracker;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class VelvetActivity
        extends SavedStateTrackingActivity
        implements VelvetUi {
    private static int sActivityCount;
    private ContextHeaderView mContextHeader;
    private OnScrollViewHider mContextHeaderHider;
    private boolean mDisableSearchPlateShieldFading;
    private VelvetFactory mFactory;
    private boolean mFadeOutSearchHeader;
    private FooterView mFooter;
    private OnScrollViewHider mFooterHider;
    private ActivityIntentStarter mIntentStarter;
    private MainContentView mMainContentBack;
    private MainContentView mMainContentFront;
    private PopupMenu mMenu;
    private final Runnable mOnFocusGainedTask = new Runnable() {
        public void run() {
            VelvetActivity.this.mPresenter.onWindowFocusChanged(true);
        }
    };
    private VelvetPresenter mPresenter;
    private CoScrollContainer mScrollView;
    private SearchController mSearchController;
    private VelvetSearchPlate mSearchPlate;
    private OnScrollViewFader mSearchPlateBgFader;
    private OnScrollViewHider mSearchPlateHider;
    private int mSearchPlateOffset;
    private View mSearchPlateShield;
    private OnScrollViewFader mSearchPlateShieldFader;
    private View mSearchPlateStrongShield;
    private OnScrollViewFader mSearchPlateStrongShieldFader;
    private int mSearchPlateVerticalMargin;
    private boolean mShowContextHeader;
    private View mTheGoogleLogo;
    private VelvetTopLevelContainer mTopLevelContainer;
    private View mTrainingPeekFrame;
    private CancellableSingleThreadedExecutor mUiThread;

    public VelvetActivity() {
        sActivityCount = 1 + sActivityCount;
        VelvetStrictMode.onStartupPoint(2);
    }

    private void closeMenu() {
        if (this.mMenu != null) {
            this.mMenu.dismiss();
            this.mMenu = null;
        }
    }

    private void commitAndExecutePendingTransactions(FragmentTransaction paramFragmentTransaction) {
        if (haveSavedState()) {
            Log.w("Velvet.VelvetActivity", "have saved state, may see UI issues later");
            paramFragmentTransaction.commitAllowingStateLoss();
        }
        for (; ; ) {
            if (!getFragmentManager().executePendingTransactions()) {
                Log.w("Velvet.VelvetActivity", "executePendingTransactions returned false?");
            }
            return;
            paramFragmentTransaction.commit();
        }
    }

    private View inflateStub(int paramInt) {
        return ((ViewStub) Preconditions.checkNotNull(findViewById(paramInt))).inflate();
    }

    private RetainedFragment maybeAddRetainedFragment() {
        RetainedFragment localRetainedFragment1 = (RetainedFragment) getFragmentManager().findFragmentByTag("RetainedFragment");
        if (localRetainedFragment1 == null) {
            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            RetainedFragment localRetainedFragment2 = new RetainedFragment();
            localFragmentTransaction.add(localRetainedFragment2, "RetainedFragment");
            commitAndExecutePendingTransactions(localFragmentTransaction);
            return localRetainedFragment2;
        }
        return localRetainedFragment1;
    }

    private int pxToDp(int paramInt) {
        float f = getResources().getDisplayMetrics().density;
        return (int) (paramInt / f);
    }

    private void setResultsAreaSizePx(int paramInt1, int paramInt2) {
        final int i = pxToDp(paramInt1);
        final int j = pxToDp(paramInt2);
        this.mUiThread.execute(new Runnable() {
            public void run() {
                VelvetActivity.this.mPresenter.onBrowserDimensionsAvailable(new Point(i, j));
            }
        });
    }

    private void updateSearchPlateFading() {
        if ((this.mShowContextHeader) && (this.mFadeOutSearchHeader)) {
            this.mSearchPlateBgFader.setFadePoints(this.mContextHeader.getBottom(), 50 + this.mContextHeader.getBottom(), 0.94F, 1.0F);
            return;
        }
        this.mSearchPlateBgFader.setFixedAlpha(1.0F);
    }

    private void updateSearchPlateOffset(boolean paramBoolean) {
        if (this.mShowContextHeader) {
        }
        for (int i = this.mTheGoogleLogo.getBottom() - this.mSearchPlateVerticalMargin; ; i = 0) {
            this.mSearchPlateOffset = i;
            this.mSearchPlateHider.setOffsetFromEdge(this.mSearchPlateOffset, paramBoolean);
            updateSearchPlateFading();
            updateSearchPlateShieldFading();
            return;
        }
    }

    private void updateSearchPlateShieldFading() {
        if (this.mDisableSearchPlateShieldFading) {
            this.mSearchPlateShieldFader.setFixedAlpha(0.0F);
            return;
        }
        this.mSearchPlateShieldFader.setFadePoints(this.mSearchPlateOffset, this.mSearchPlateOffset + this.mSearchPlateShield.getMeasuredHeight());
    }

    private void updateStrongShieldFade(boolean paramBoolean1, boolean paramBoolean2) {
        if (this.mShowContextHeader) {
            if ((paramBoolean1) || (!paramBoolean2)) {
                this.mSearchPlateStrongShield.animate().cancel();
                this.mSearchPlateStrongShieldFader.setFixedAlpha(0.0F);
                return;
            }
            this.mSearchPlateStrongShield.animate().alpha(0.0F).setStartDelay(300L).withEndAction(new Runnable() {
                public void run() {
                    VelvetActivity.this.updateStrongShieldFade(true, false);
                }
            });
            return;
        }
        int i = this.mSearchPlateStrongShield.getBottom();
        if (paramBoolean1) {
            this.mSearchPlateStrongShield.animate().cancel();
            this.mSearchPlateStrongShieldFader.setFadePoints(i + 1, i);
            return;
        }
        this.mSearchPlateStrongShield.animate().alpha(0.0F).setStartDelay(0L).withEndAction(new Runnable() {
            public void run() {
                VelvetActivity.this.updateStrongShieldFade(true, false);
            }
        });
    }

    public void assertNotInLayout() {
        Util.assertNotInLayout(this.mTopLevelContainer);
    }

    public void clearIntent() {
        setIntent(null);
    }

    public void doABarrelRoll() {
        findViewById(2131297192).animate().rotationBy(360.0F).setDuration(500L).start();
    }

    public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
        super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        dumpActivityState(paramString, paramPrintWriter);
    }

    public void dumpActivityState(String paramString, PrintWriter paramPrintWriter) {
        this.mPresenter.dump(paramString, paramPrintWriter);
        DumpUtils.println(paramPrintWriter, new Object[]{paramString, "VelvetActivity state:"});
        String str = paramString + "  ";
        if (this.mMainContentFront != null) {
            DumpUtils.println(paramPrintWriter, new Object[]{str, "Current front content: "});
            this.mMainContentFront.dump(str, paramPrintWriter);
        }
        DumpUtils.println(paramPrintWriter, new Object[]{str, "Current back content: "});
        this.mMainContentBack.dump(str, paramPrintWriter);
        paramPrintWriter.println();
    }

    public void finalize() {
        sActivityCount = -1 + sActivityCount;
    }

    public void finish() {
        super.finish();
    }

    public Activity getActivity() {
        return this;
    }

    public ContextHeaderUi getContextHeaderUi() {
        return this.mContextHeader;
    }

    public FooterUi getFooterUi() {
        return this.mFooter;
    }

    public IntentStarter getIntentStarter() {
        return this.mIntentStarter;
    }

    public MainContentView getMainContentBack() {
        return this.mMainContentBack;
    }

    public MainContentView getMainContentFront() {
        if (this.mMainContentFront == null) {
            this.mMainContentFront = ((MainContentView) inflateStub(2131297193));
            this.mMainContentFront.setVelvetActivity(this);
            this.mMainContentFront.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                    if (paramAnonymousMotionEvent.getAction() != 3) {
                        return VelvetActivity.this.mPresenter.onMainViewTouched();
                    }
                    return false;
                }
            });
        }
        return this.mMainContentFront;
    }

    public View getReminderPeekView() {
        return findViewById(2131296944);
    }

    public View getRemindersFooterIcon() {
        return findViewById(2131296719);
    }

    public CoScrollContainer getScrollingContainer() {
        return this.mScrollView;
    }

    public SearchController getSearchController() {
        return this.mSearchController;
    }

    public int getSearchPlateHeight() {
        return this.mSearchPlate.getMeasuredHeight();
    }

    public View getTrainingFooterIcon() {
        return findViewById(2131296720);
    }

    public View getTrainingPeekIcon() {
        return findViewById(2131297130);
    }

    public View getTrainingPeekView() {
        return this.mTrainingPeekFrame;
    }

    public VelvetSearchPlateUi getVelvetSearchPlateUi() {
        return this.mSearchPlate;
    }

    public void indicateRemoveFromHistoryFailed() {
        SuggestionUiUtils.showRemoveFromHistoryFailedToast(this);
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        this.mIntentStarter.onActivityResultDelegate(paramInt1, paramInt2, paramIntent);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void onCreate(Bundle paramBundle) {
        VelvetStartupLatencyTracker.registerActivityCreate();
        EventLogger.recordLatencyStart(4);
        EventLogger.recordBreakdownEvent(18);
        this.mSearchPlateVerticalMargin = getResources().getDimensionPixelSize(2131689574);
        VelvetServices localVelvetServices = VelvetServices.get();
        this.mFactory = localVelvetServices.getFactory();
        this.mPresenter = this.mFactory.createPresenter(this);
        this.mIntentStarter = new LoggingIntentStarter(this, 100);
        super.onCreate(paramBundle);
        this.mIntentStarter.restoreInstanceState(paramBundle);
        this.mUiThread = localVelvetServices.getAsyncServices().getUiThreadExecutor();
        this.mSearchController = maybeAddRetainedFragment().getSearchController();
        this.mPresenter.onCreate(paramBundle, this.mSearchController);
        setContentView(2130968907);
        this.mTopLevelContainer = ((VelvetTopLevelContainer) findViewById(2131297192));
        this.mTopLevelContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8) {
                VelvetActivity.this.setResultsAreaSizePx(paramAnonymousInt3 - paramAnonymousInt1, paramAnonymousInt4 - paramAnonymousInt2);
            }
        });
        this.mTopLevelContainer.setPreImeKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent) {
                return VelvetActivity.this.mPresenter.onKeyPreIme(paramAnonymousInt, paramAnonymousKeyEvent);
            }
        });
        this.mScrollView = ((CoScrollContainer) findViewById(2131296788));
        this.mContextHeader = ((ContextHeaderView) Preconditions.checkNotNull(findViewById(2131296483)));
        this.mContextHeaderHider = new OnScrollViewHider(this.mContextHeader, this.mScrollView, true);
        this.mContextHeaderHider.setStickiness(3, false, true);
        this.mTheGoogleLogo = findViewById(2131296485);
        this.mContextHeader.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8) {
                VelvetActivity.this.updateSearchPlateOffset(true);
                VelvetActivity.this.updateStrongShieldFade(true, false);
            }
        });
        this.mMainContentBack = ((MainContentView) Preconditions.checkNotNull(findViewById(2131296789)));
        this.mMainContentBack.setVelvetActivity(this);
        this.mSearchPlate = ((VelvetSearchPlate) Preconditions.checkNotNull(findViewById(2131297194)));
        this.mSearchPlateHider = new OnScrollViewHider(this.mSearchPlate, this.mScrollView, true);
        this.mSearchPlateBgFader = new OnScrollViewFader((View) Preconditions.checkNotNull(findViewById(2131297197)), this.mScrollView);
        this.mSearchPlateBgFader.setFadeBackgroundOnly(true);
        this.mFooter = ((FooterView) Preconditions.checkNotNull(findViewById(2131296640)));
        this.mFooterHider = new OnScrollViewHider(this.mFooter, this.mScrollView, false);
        this.mFooterHider.setForceShowOrHideOnScrollFinishedDelegate(this.mSearchPlateHider);
        this.mFooterHider.setRevealAtScrollEndDelegate(this.mSearchPlateHider);
        this.mSearchPlateShield = findViewById(2131297196);
        this.mSearchPlateShieldFader = new OnScrollViewFader(this.mSearchPlateShield, this.mScrollView);
        this.mSearchPlateStrongShield = findViewById(2131297195);
        this.mSearchPlateStrongShieldFader = new OnScrollViewFader(this.mSearchPlateStrongShield, this.mScrollView);
        this.mTrainingPeekFrame = findViewById(2131297129);
        this.mTrainingPeekFrame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                VelvetActivity.this.mPresenter.onTrainingButtonPressed();
            }
        });
        this.mScrollView.setInterceptedTouchEventListener(new View.OnTouchListener() {
            public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                if (paramAnonymousMotionEvent.getAction() != 3) {
                    VelvetActivity.this.mPresenter.onMainViewTouched();
                }
                return false;
            }
        });
        this.mPresenter.onPostCreate(paramBundle);
        EventLogger.recordBreakdownEvent(19);
        VelvetStrictMode.onStartupPoint(3);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        this.mPresenter.onCreateOptionsMenu(paramMenu);
        return super.onCreateOptionsMenu(paramMenu);
    }

    public void onDestroy() {
        this.mUiThread.cancelExecute(this.mOnFocusGainedTask);
        this.mPresenter.onDestroy();
        super.onDestroy();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        boolean bool = this.mPresenter.onKeyDown(paramInt, paramKeyEvent);
        if (!bool) {
            bool = super.onKeyDown(paramInt, paramKeyEvent);
        }
        return bool;
    }

    public void onNewIntent(Intent paramIntent) {
        super.onNewIntent(paramIntent);
        this.mPresenter.onNewIntent(paramIntent);
    }

    public void onPause() {
        closeMenu();
        this.mPresenter.onPause();
        EventLogger.resetOneOff();
        super.onPause();
    }

    public void onRestart() {
        VelvetStartupLatencyTracker.registerActivityRestart();
        EventLogger.recordLatencyStart(7);
        EventLogger.recordBreakdownEvent(27);
        super.onRestart();
    }

    public void onResume() {
        EventLogger.recordLatencyStart(8);
        EventLogger.recordBreakdownEvent(23);
        super.onResume();
        this.mPresenter.onResume();
        EventLogger.recordBreakdownEvent(24);
        VelvetStrictMode.onStartupPoint(5);
    }

    public void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        this.mPresenter.onSaveInstanceState(paramBundle);
        this.mIntentStarter.onSaveInstanceState(paramBundle);
    }

    public void onStart() {
        EventLogger.recordBreakdownEvent(21);
        super.onStart();
        this.mPresenter.onStart();
        EventLogger.recordBreakdownEvent(22);
        VelvetStrictMode.onStartupPoint(4);
    }

    public void onStop() {
        this.mPresenter.onStop();
        this.mMainContentBack.flushAllTransactions();
        if (this.mMainContentFront != null) {
            this.mMainContentFront.flushAllTransactions();
        }
        super.onStop();
    }

    public void onWindowFocusChanged(boolean paramBoolean) {
        super.onWindowFocusChanged(paramBoolean);
        if (paramBoolean) {
            this.mUiThread.execute(this.mOnFocusGainedTask);
            return;
        }
        this.mUiThread.cancelExecute(this.mOnFocusGainedTask);
        this.mPresenter.onWindowFocusChanged(false);
    }

    public void setFadeSearchPlateOverHeader(boolean paramBoolean) {
        if (this.mFadeOutSearchHeader != paramBoolean) {
            this.mFadeOutSearchHeader = paramBoolean;
            updateSearchPlateFading();
        }
    }

    public void setFooterStickiness(int paramInt, boolean paramBoolean) {
        VelvetTopLevelContainer localVelvetTopLevelContainer = this.mTopLevelContainer;
        if ((paramInt == 2) || (paramInt == 4)) {
        }
        for (boolean bool = true; ; bool = false) {
            localVelvetTopLevelContainer.setIncludeFooterPadding(true, bool);
            this.mFooterHider.setStickiness(paramInt, paramBoolean, true);
            return;
        }
    }

    public void setSearchPlateStickiness(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
        this.mSearchPlateHider.setStickiness(paramInt, paramBoolean1, paramBoolean2);
        if (paramInt == 2) {
        }
        for (boolean bool = true; ; bool = false) {
            this.mDisableSearchPlateShieldFading = bool;
            updateSearchPlateShieldFading();
            return;
        }
    }

    public void setShowContextHeader(boolean paramBoolean1, boolean paramBoolean2) {
        OnScrollViewHider localOnScrollViewHider;
        if (paramBoolean1 != this.mShowContextHeader) {
            this.mShowContextHeader = paramBoolean1;
            boolean bool1 = false;
            if (!paramBoolean2) {
                this.mContextHeaderHider.setPartialHide(this.mTheGoogleLogo.getBottom());
                this.mContextHeaderHider.setFadeWithTranslation(true);
                boolean bool2 = this.mMainContentBack.isRunningDisappearTransitions();
                bool1 = false;
                if (bool2) {
                    this.mContextHeaderHider.setAnimationStartDelay(300);
                    this.mSearchPlateHider.setAnimationStartDelay(300);
                    bool1 = true;
                }
            }
            updateStrongShieldFade(paramBoolean2, bool1);
            this.mTopLevelContainer.setContextHeaderShown(this.mShowContextHeader);
            localOnScrollViewHider = this.mContextHeaderHider;
            if (!paramBoolean1) {
                break label160;
            }
        }
        label160:
        for (int i = 2; ; i = 3) {
            localOnScrollViewHider.setStickiness(i, paramBoolean2, true);
            updateSearchPlateOffset(paramBoolean2);
            if (!paramBoolean2) {
                this.mContextHeaderHider.setAnimationStartDelay(0);
                this.mSearchPlateHider.setAnimationStartDelay(0);
                this.mContextHeaderHider.setPartialHide(0);
                this.mContextHeaderHider.setFadeWithTranslation(false);
            }
            return;
        }
    }

    public void showDebugDialog(String paramString) {
        DebugDialogFragment localDebugDialogFragment = new DebugDialogFragment();
        localDebugDialogFragment.setText(paramString);
        localDebugDialogFragment.show(getFragmentManager(), "debug_dialog");
    }

    public void showDogfoodIndicator() {
        this.mTopLevelContainer.showDogfoodIndicator();
    }

    public void showErrorToast(int paramInt) {
        Toast.makeText(this, paramInt, 0).show();
    }

    public void showFooter() {
        this.mFooterHider.show();
    }

    public void showOptionsMenu(View paramView) {
        this.mMenu = new PopupMenu(this, paramView);
        onCreatePanelMenu(0, this.mMenu.getMenu());
        onPreparePanel(0, null, this.mMenu.getMenu());
        this.mMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem) {
                return VelvetActivity.this.onMenuItemSelected(0, paramAnonymousMenuItem);
            }
        });
        this.mMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            public void onDismiss(PopupMenu paramAnonymousPopupMenu) {
                VelvetActivity.access$402(VelvetActivity.this, null);
            }
        });
        this.mMenu.show();
    }

    public void showRemoveFromHistoryDialog(Suggestion paramSuggestion, Runnable paramRunnable) {
        SuggestionUiUtils.showRemoveFromHistoryDialog(this, paramSuggestion, paramRunnable);
    }

    public void showSearchPlate() {
        this.mSearchPlateHider.show();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.VelvetActivity

 * JD-Core Version:    0.7.0.1

 */