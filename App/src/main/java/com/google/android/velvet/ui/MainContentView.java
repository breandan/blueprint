package com.google.android.velvet.ui;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.search.shared.ui.SuggestionGridLayout.OnDismissListener;
import com.google.android.search.shared.ui.SuggestionGridLayout.OnStackChangeListener;
import com.google.android.shared.ui.ChildPaddingLayout;
import com.google.android.shared.ui.CoScrollContainer;
import com.google.android.shared.ui.CoScrollContainer.LayoutParams;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.shared.ui.ScrollViewControl.ScrollListener;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.client.UndoDismissManager;
import com.google.android.sidekick.shared.ui.NowProgressBar;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.presenter.MainContentPresenter;
import com.google.android.velvet.presenter.MainContentUi;
import com.google.common.collect.Lists;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Nullable;

public class MainContentView
        extends ChildPaddingLayout
        implements MainContentUi {
    private VelvetActivity mActivity;
    private final View.OnAttachStateChangeListener mAttachListener = new View.OnAttachStateChangeListener() {
        public void onViewAttachedToWindow(View paramAnonymousView) {
            MainContentView.access$1002(MainContentView.this, true);
            MainContentView.this.maybePostCommitTransactions(false);
        }

        public void onViewDetachedFromWindow(View paramAnonymousView) {
            MainContentView.access$1002(MainContentView.this, false);
        }
    };
    private boolean mAttachedToWindow;
    private LayoutTransition mCardsLayoutTransition;
    private SuggestionGridLayout mCardsView;
    private Clock mClock;
    private final Runnable mCommitTransactionsRunnable = new Runnable() {
        public void run() {
            MainContentView.access$602(MainContentView.this, false);
            MainContentView.this.commitTransactions();
        }
    };
    private boolean mCommitTransactionsRunnablePosted;
    private boolean mCommittingTransactions;
    private final View.OnLayoutChangeListener mContainerLayoutChangeListener = new View.OnLayoutChangeListener() {
        public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8) {
            MainContentView.this.maybePostCommitTransactions(true);
        }
    };
    private boolean mNotifyScrollPosted;
    private final Runnable mNotifyScrolledRunnable = new Runnable() {
        public void run() {
            MainContentView.access$202(MainContentView.this, false);
            if (MainContentView.this.isAttached()) {
                MainContentView.this.mPresenter.onViewScrolled(MainContentView.this.mScrolling);
            }
        }
    };
    private int mNumDisappearTransitions;
    private final List<MainContentPresenter.Transaction> mPendingTransactions = Lists.newLinkedList();
    private MainContentPresenter mPresenter;
    private NowProgressBar mProgressBar;
    private final ScrollViewControl.ScrollListener mScrollListener = new ScrollViewControl.ScrollListener() {
        public void onOverscroll(int paramAnonymousInt) {
        }

        public void onOverscrollFinished() {
        }

        public void onOverscrollStarted() {
        }

        public void onScrollAnimationFinished() {
            MainContentView.this.maybePostNotifyScroll(false);
            MainContentView.this.maybePostCommitTransactions(false);
        }

        public void onScrollChanged(int paramAnonymousInt1, int paramAnonymousInt2) {
            MainContentView.this.maybePostNotifyScroll(true);
        }

        public void onScrollFinished() {
            MainContentView.this.maybePostNotifyScroll(false);
        }

        public void onScrollMarginConsumed(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2) {
            MainContentView.this.maybePostNotifyScroll(true);
        }
    };
    private boolean mScrolling;
    private CoScrollContainer mScrollingContainer;
    private final LayoutTransition.TransitionListener mTransitionListener = new LayoutTransition.TransitionListener() {
        public void endTransition(LayoutTransition paramAnonymousLayoutTransition, ViewGroup paramAnonymousViewGroup, View paramAnonymousView, int paramAnonymousInt) {
            if (paramAnonymousInt == 3) {
                MainContentView.access$810(MainContentView.this);
            }
            MainContentView.this.checkLayoutTransitionsComplete();
        }

        public void startTransition(LayoutTransition paramAnonymousLayoutTransition, ViewGroup paramAnonymousViewGroup, View paramAnonymousView, int paramAnonymousInt) {
            if (paramAnonymousInt == 3) {
                MainContentView.access$808(MainContentView.this);
            }
        }
    };
    private ScheduledSingleThreadedExecutor mUiExecutor;
    private UndoDismissManager mUndoDismissManager;

    public MainContentView(Context paramContext) {
        this(paramContext, null);
    }

    public MainContentView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public MainContentView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    private boolean areTransitionsRunningOrLayoutPending(boolean paramBoolean) {
        return (!this.mAttachedToWindow) || (isRunningLayoutTransition()) || ((this.mScrollingContainer != null) && ((this.mScrollingContainer.isAnimatingScroll()) || ((!paramBoolean) && (this.mScrollingContainer.isLayoutRequested()))));
    }

    private void checkLayoutTransitionsComplete() {
        if ((isAttached()) && (!isRunningLayoutTransition())) {
            onLayoutTransitionFinished();
        }
    }

    private void commitTransactions() {
        long l = 5L + this.mClock.uptimeMillis();
        ExtraPreconditions.checkMainThread();
        if ((this.mCardsView == null) || (!this.mAttachedToWindow)) {
            return;
        }
        if (this.mCommitTransactionsRunnablePosted) {
            this.mUiExecutor.cancelExecute(this.mCommitTransactionsRunnable);
            this.mCommitTransactionsRunnablePosted = false;
        }
        int i;
        MainContentPresenter.Transaction localTransaction;
        if ((!this.mCommittingTransactions) && (!areTransitionsRunningOrLayoutPending(false))) {
            this.mCommittingTransactions = true;
            i = 1;
            if (this.mPendingTransactions.size() > 0) {
                localTransaction = (MainContentPresenter.Transaction) this.mPendingTransactions.remove(0);
                boolean bool1 = false;
                while (!bool1) {
                    bool1 = localTransaction.prepare();
                    logTimeSpent(localTransaction, "prepare()", l);
                    if (this.mClock.uptimeMillis() >= l) {
                        i = 0;
                    }
                }
                if (i == 0) {
                    break label210;
                }
                localTransaction.commit(this);
                logTimeSpent(localTransaction, "commit()", l);
                if (!areTransitionsRunningOrLayoutPending(false)) {
                    break label187;
                }
            }
        }
        for (; ; ) {
            this.mCommittingTransactions = false;
            logTimeSpent(null, "commitTransactions", l);
            return;
            label187:
            boolean bool2 = this.mClock.uptimeMillis() < l;
            localTransaction = null;
            if (!bool2) {
                i = 0;
            }
            label210:
            if (i != 0) {
                break;
            }
            if (localTransaction != null) {
                this.mPendingTransactions.add(0, localTransaction);
            }
            if (this.mPendingTransactions.size() > 0) {
                this.mCommitTransactionsRunnablePosted = true;
                this.mUiExecutor.execute(this.mCommitTransactionsRunnable);
            }
        }
    }

    private final boolean isAttached() {
        return this.mPresenter != null;
    }

    private boolean isMainContentBack() {
        return getId() == 2131296789;
    }

    private boolean isMainContentFront() {
        return getId() == 2131296792;
    }

    private void logTimeSpent(MainContentPresenter.Transaction paramTransaction, String paramString, long paramLong) {
    }

    private void maybePostCommitTransactions(boolean paramBoolean) {
        if ((!this.mCommitTransactionsRunnablePosted) && (!this.mCommittingTransactions) && (!areTransitionsRunningOrLayoutPending(paramBoolean)) && (isAttached())) {
            this.mCommitTransactionsRunnablePosted = true;
            this.mUiExecutor.execute(this.mCommitTransactionsRunnable);
        }
    }

    private void maybePostNotifyScroll(boolean paramBoolean) {
        if ((!this.mNotifyScrollPosted) && ((this.mScrolling) || (paramBoolean))) {
            this.mNotifyScrollPosted = true;
            this.mUiExecutor.executeDelayed(this.mNotifyScrolledRunnable, 100L);
        }
        this.mScrolling = paramBoolean;
    }

    public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
        this.mUndoDismissManager.handleTouchEvent(paramMotionEvent);
        return super.dispatchTouchEvent(paramMotionEvent);
    }

    void dump(String paramString, PrintWriter paramPrintWriter) {
        String str1 = paramString + "  ";
        paramPrintWriter.print(str1);
        paramPrintWriter.println("MainContentView State:");
        String str2 = str1 + "  ";
        new StringBuilder().append(str2).append("  ").toString();
    }

    void flushAllTransactions() {

        if ((this.mCardsView == null) || (!this.mAttachedToWindow)) {
        }
        for (; ; ) {
            return;
            if (this.mCommitTransactionsRunnablePosted) {
                this.mUiExecutor.cancelExecute(this.mCommitTransactionsRunnable);
                this.mCommitTransactionsRunnablePosted = false;
            }
            while (this.mPendingTransactions.size() > 0) {
                MainContentPresenter.Transaction localTransaction = (MainContentPresenter.Transaction) this.mPendingTransactions.remove(0);
                for (boolean bool = false; !bool; bool = localTransaction.prepare()) {
                }
                localTransaction.commit(this);
            }
        }
    }

    public SuggestionGridLayout getCardsView() {
        return this.mCardsView;
    }

    protected Clock getClock() {
        return VelvetServices.get().getCoreServices().getClock();
    }

    public float getMainContentBackCollapsibleMarginRatio() {
        if (isMainContentBack()) {
            return ((CoScrollContainer.LayoutParams) getLayoutParams()).getCollapsibleMarginRatio();
        }
        return 0.0F;
    }

    @Nullable
    public NowProgressBar getProgressBar() {
        return this.mProgressBar;
    }

    public float getRelativeScrollDistanceFromTop(View paramView) {
        ScrollViewControl localScrollViewControl = getScrollViewControl();
        int i = localScrollViewControl.getDescendantTop(paramView);
        if (i < 0) {
            return 3.4028235E+38F;
        }
        return (i - localScrollViewControl.getScrollY()) / localScrollViewControl.getViewportHeight();
    }

    public ScrollViewControl getScrollViewControl() {
        return getScrollingContainer();
    }

    public CoScrollContainer getScrollingContainer() {
        if ((isMainContentBack()) && (this.mScrollingContainer == null)) {
            this.mScrollingContainer = ((CoScrollContainer) getParent());
            this.mScrollingContainer.addScrollListener(this.mScrollListener);
            this.mScrollingContainer.addOnLayoutChangeListener(this.mContainerLayoutChangeListener);
        }
        return this.mScrollingContainer;
    }

    protected ScheduledSingleThreadedExecutor getUiExecutor() {
        return VelvetServices.get().getAsyncServices().getUiThreadExecutor();
    }

    public boolean isRunningDisappearTransitions() {
        return this.mNumDisappearTransitions != 0;
    }

    public boolean isRunningLayoutTransition() {
        if (this.mCardsView != null) {
        }
        for (LayoutTransition localLayoutTransition = this.mCardsView.getLayoutTransition(); localLayoutTransition == null; localLayoutTransition = null) {
            return false;
        }
        return localLayoutTransition.isRunning();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mCardsView = ((SuggestionGridLayout) findViewById(2131296790));
        this.mCardsView.setOnDismissListener(new SuggestionGridLayout.OnDismissListener() {
            public void onViewsDismissed(PendingViewDismiss paramAnonymousPendingViewDismiss) {
                if (MainContentView.this.isAttached()) {
                    MainContentView.this.mPresenter.onViewsDismissed(paramAnonymousPendingViewDismiss);
                }
            }
        });
        this.mCardsView.setOnStackChangeListener(new SuggestionGridLayout.OnStackChangeListener() {
            public boolean preStackViewOrderChange(List<View> paramAnonymousList) {
                if (MainContentView.this.isAttached()) {
                    return MainContentView.this.mPresenter.preStackViewOrderChange(paramAnonymousList);
                }
                return false;
            }
        });
        this.mCardsLayoutTransition = this.mCardsView.getLayoutTransition();
        this.mCardsLayoutTransition.setAnimateParentHierarchy(false);
        this.mCardsLayoutTransition.addTransitionListener(this.mTransitionListener);
        addOnAttachStateChangeListener(this.mAttachListener);
        this.mProgressBar = ((NowProgressBar) findViewById(2131296791));
        this.mClock = getClock();
        this.mUiExecutor = getUiExecutor();
        this.mPendingTransactions.clear();
        this.mUndoDismissManager = VelvetServices.get().getSidekickInjector().getUndoDismissManager();
    }

    void onLayoutTransitionFinished() {
        maybePostCommitTransactions(false);
    }

    public void post(MainContentPresenter.Transaction paramTransaction) {
        this.mPendingTransactions.add(paramTransaction);
        commitTransactions();
    }

    public final void setHeaderAndFooterPadding(int paramInt1, int paramInt2) {
        if (this.mCardsView != null) {
            this.mCardsView.setPadding(this.mCardsView.getPaddingLeft(), paramInt1, this.mCardsView.getPaddingRight(), paramInt2);
        }
        if (this.mProgressBar != null) {
            FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams) this.mProgressBar.getLayoutParams();
            localLayoutParams.topMargin = (paramInt1 - getResources().getDimensionPixelSize(2131689580));
            this.mProgressBar.setLayoutParams(localLayoutParams);
        }
    }

    public void setMainContentBackCollapsibleMargin(int paramInt) {
        CoScrollContainer.LayoutParams localLayoutParams;
        if (isMainContentBack()) {
            localLayoutParams = (CoScrollContainer.LayoutParams) getLayoutParams();
            if (paramInt > 0) {
                localLayoutParams.setParams(6, paramInt);
            }
        } else {
            return;
        }
        localLayoutParams.resetParams();
    }

    public void setMainContentFrontScrimVisible(boolean paramBoolean) {
        if (isMainContentFront()) {
            if (!paramBoolean) {
                break label28;
            }
        }
        label28:
        for (int i = getResources().getColor(2131230800); ; i = 0) {
            setBackgroundColor(i);
            return;
        }
    }

    public void setPresenter(MainContentPresenter paramMainContentPresenter) {
        this.mPresenter = paramMainContentPresenter;
    }

    public void setSearchPlateStuckToScrollingView(boolean paramBoolean) {
        if (paramBoolean) {
            this.mActivity.setSearchPlateStickiness(2, true, false);
            return;
        }
        this.mActivity.setSearchPlateStickiness(this.mPresenter.getVelvetPresenter().getSearchPlateStickiness(), false, false);
    }

    void setVelvetActivity(VelvetActivity paramVelvetActivity) {
        this.mActivity = paramVelvetActivity;
    }

    public void showToast(int paramInt) {
        Toast.makeText(getContext(), paramInt, 0).show();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.MainContentView

 * JD-Core Version:    0.7.0.1

 */