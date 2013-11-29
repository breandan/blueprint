package com.google.android.sidekick.shared.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.TextView;
import com.google.android.search.shared.ui.BakedBezierInterpolator;
import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.search.shared.ui.SuggestionGridLayout.OnDismissListener;
import com.google.android.search.shared.ui.SuggestionGridLayout.OnStackChangeListener;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.ui.CoScrollContainer;
import com.google.android.shared.ui.OnScrollViewHider;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.shared.util.Animations;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.shared.util.SendGoogleFeedback;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.remoteapi.LoggingRequest;
import com.google.android.sidekick.shared.ui.NowProgressBar;
import com.google.android.sidekick.shared.ui.PredictiveCardWrapper;
import com.google.android.sidekick.shared.util.IntentDispatcherUtil;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.sidekick.shared.util.WebSearchUtils;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

public class NowClientCardsView
  extends FrameLayout
  implements SuggestionGridLayout.OnDismissListener, SuggestionGridLayout.OnStackChangeListener, PredictiveCardRefreshManager.PredictiveCardsPresenter, ScrollableCardView
{
  private static final String TAG = Tag.getTag(NowClientCardsView.class);
  private Activity mActivity;
  private WebImageView mContextImage;
  private boolean mDelaySlowMeasureAndLayout;
  private DoodleListener mDoodleListener;
  private View mFooter;
  private OnScrollViewHider mFooterHider;
  private Rect mInsets;
  private IntentStarter mIntentStarter;
  private boolean mIsViewVisible;
  private int mLastHeightMeasureSpec;
  private int mLastWidthMeasureSpec;
  private PopupMenu mMenu;
  private NowCardsViewWrapper mNowCardsView;
  private NowRemoteClient mNowRemoteClient;
  private NowRemoteClient.NowRemoteClientLock mNowRemoteClientLock;
  private NowProgressBar mProgressBar;
  private Interpolator mProximityToNowInterpolator;
  private PredictiveCardRefreshManager mRefreshManager;
  private ImageButton mRemindersButton;
  private boolean mRemindersEnabled;
  private CoScrollContainer mScrollViewControl;
  private SuggestionGridLayout mSuggestionGridLayout;
  private ImageButton mTrainingButton;
  private View mTrainingPeekFrame;
  private UndoDismissManager mUndoDismissManager;
  
  public NowClientCardsView(Context paramContext)
  {
    super(paramContext);
  }
  
  public NowClientCardsView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public NowClientCardsView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void onTrainingButtonPressed()
  {
    IntentDispatcherUtil.dispatchIntent(getContext(), "com.google.android.googlequicksearchbox.TRAINING_CLOSET");
  }
  
  private void setDelaySlowMeasureAndLayout(boolean paramBoolean)
  {
    if (paramBoolean == this.mDelaySlowMeasureAndLayout) {}
    do
    {
      return;
      this.mDelaySlowMeasureAndLayout = paramBoolean;
    } while (paramBoolean);
    requestLayout();
    invalidate();
  }
  
  private void updateRemindersVisibility()
  {
    this.mRemindersEnabled = this.mNowRemoteClient.getConfiguration().getBoolean("CONFIGURATION_REMINDERS_ENABLED", false);
    ImageButton localImageButton = this.mRemindersButton;
    boolean bool = this.mRemindersEnabled;
    int i = 0;
    if (bool) {}
    for (;;)
    {
      localImageButton.setVisibility(i);
      return;
      i = 4;
    }
  }
  
  public void addEntries(List<EntryItemStack> paramList, CardRenderingContext paramCardRenderingContext)
  {
    this.mNowCardsView.addCards(this.mActivity, paramList, paramCardRenderingContext, true, false, true, null, null, null, -1);
  }
  
  public void dismissEntry(Sidekick.Entry paramEntry, @Nullable Collection<Sidekick.Entry> paramCollection)
  {
    this.mNowCardsView.dismissEntry(paramEntry, paramCollection);
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    this.mUndoDismissManager.handleTouchEvent(paramMotionEvent);
    return super.dispatchTouchEvent(paramMotionEvent);
  }
  
  public IntentStarter getIntentStarter()
  {
    return this.mIntentStarter;
  }
  
  @Nullable
  public View getRemindersFooterView()
  {
    return this.mRemindersButton;
  }
  
  @Nullable
  public View getRemindersPeekView()
  {
    return findViewById(2131296944);
  }
  
  public ScrollViewControl getScrollViewControl()
  {
    return this.mScrollViewControl;
  }
  
  public SuggestionGridLayout getSuggestionGridLayout()
  {
    return this.mSuggestionGridLayout;
  }
  
  @Nullable
  public View getTrainingFooterIcon()
  {
    return this.mTrainingButton;
  }
  
  @Nullable
  public View getTrainingPeekIcon()
  {
    return findViewById(2131297130);
  }
  
  @Nullable
  public View getTrainingPeekView()
  {
    return this.mTrainingPeekFrame;
  }
  
  public void hideViewsForSearch()
  {
    if (getVisibility() != 8) {
      setDelaySlowMeasureAndLayout(true);
    }
    this.mSuggestionGridLayout.animate().translationY(getHeight()).setInterpolator(BakedBezierInterpolator.INSTANCE).setListener(new AnimatorListenerAdapter()
    {
      public void onAnimationCancel(Animator paramAnonymousAnimator)
      {
        NowClientCardsView.this.mSuggestionGridLayout.setTranslationY(0.0F);
        NowClientCardsView.this.setDelaySlowMeasureAndLayout(false);
      }
      
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        NowClientCardsView.this.mSuggestionGridLayout.setVisibility(4);
        NowClientCardsView.this.setDelaySlowMeasureAndLayout(false);
      }
    });
    Animations.fadeOutAndHide(this.mProgressBar).setInterpolator(BakedBezierInterpolator.INSTANCE).withLayer();
    Animations.fadeOutAndHide(this.mContextImage).setInterpolator(BakedBezierInterpolator.INSTANCE).withLayer();
  }
  
  public boolean isAttached()
  {
    return true;
  }
  
  public boolean isContextHeaderVisible()
  {
    return isAttached();
  }
  
  public boolean isPredictiveOnlyMode()
  {
    return true;
  }
  
  public boolean isVisible()
  {
    return this.mIsViewVisible;
  }
  
  void layoutIfGone()
  {
    if (getVisibility() == 8)
    {
      setVisibility(0);
      requestLayout();
      post(new Runnable()
      {
        public void run()
        {
          if (!NowClientCardsView.this.mIsViewVisible) {
            NowClientCardsView.this.setVisibility(8);
          }
        }
      });
    }
  }
  
  public void notifyCardVisible(PredictiveCardWrapper paramPredictiveCardWrapper)
  {
    this.mNowCardsView.notifyCardVisible(paramPredictiveCardWrapper);
  }
  
  public boolean onBackPressed()
  {
    boolean bool = this.mNowCardsView.commitAllFeedback(true);
    if (bool) {
      this.mNowRemoteClient.logAction(LoggingRequest.forAnalyticsAction("BACK_BUTTON_CLOSE_FEEDBACK", null));
    }
    return bool;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mSuggestionGridLayout = ((SuggestionGridLayout)findViewById(2131296790));
    this.mScrollViewControl = ((CoScrollContainer)findViewById(2131296788));
    this.mProgressBar = ((NowProgressBar)findViewById(2131296791));
    this.mContextImage = ((WebImageView)findViewById(2131296484));
    this.mSuggestionGridLayout.setOnDismissListener(this);
    this.mSuggestionGridLayout.setOnStackChangeListener(this);
    int i = LayoutUtils.getContextHeaderSize(getContext()).y;
    ViewGroup.LayoutParams localLayoutParams = this.mContextImage.getLayoutParams();
    if (localLayoutParams.height != i)
    {
      localLayoutParams.height = i;
      this.mContextImage.setLayoutParams(localLayoutParams);
      this.mContextImage.setVisibility(0);
    }
    this.mFooter = ((View)Preconditions.checkNotNull(findViewById(2131296820)));
    this.mFooterHider = new OnScrollViewHider(this.mFooter, this.mScrollViewControl, false);
    this.mFooterHider.setStickiness(2, true, true);
    this.mProximityToNowInterpolator = new AccelerateInterpolator();
    this.mRemindersButton = ((ImageButton)this.mFooter.findViewById(2131296719));
    this.mRemindersButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        NowClientCardsView.this.onRemindersButtonPressed();
      }
    });
    this.mTrainingButton = ((ImageButton)this.mFooter.findViewById(2131296720));
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        NowClientCardsView.this.onTrainingButtonPressed();
      }
    };
    this.mTrainingButton.setOnClickListener(local2);
    this.mTrainingPeekFrame = findViewById(2131297129);
    this.mTrainingPeekFrame.setOnClickListener(local2);
    this.mFooter.findViewById(2131296722).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        NowClientCardsView.this.showOptionsMenu(paramAnonymousView);
      }
    });
  }
  
  public void onHide()
  {
    if (!this.mIsViewVisible) {}
    do
    {
      return;
      this.mIsViewVisible = false;
      this.mRefreshManager.removeViewActionListeners();
      this.mRefreshManager.recordViewEndTimes();
      this.mRefreshManager.unregisterPullToRefreshHandler();
      if (this.mNowRemoteClientLock != null)
      {
        this.mNowRemoteClientLock.release();
        this.mNowRemoteClientLock = null;
      }
    } while (this.mMenu == null);
    this.mMenu.dismiss();
    this.mMenu = null;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((this.mDelaySlowMeasureAndLayout) && (!paramBoolean)) {
      return;
    }
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (this.mDelaySlowMeasureAndLayout)
    {
      if ((paramInt1 == this.mLastWidthMeasureSpec) && (paramInt2 == this.mLastHeightMeasureSpec))
      {
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        return;
      }
      this.mDelaySlowMeasureAndLayout = false;
    }
    measureChildWithMargins(this.mFooter, paramInt1, 0, paramInt2, 0);
    if (this.mInsets != null) {}
    for (int i = this.mInsets.bottom;; i = 0)
    {
      this.mSuggestionGridLayout.setPadding(this.mSuggestionGridLayout.getPaddingLeft(), this.mSuggestionGridLayout.getPaddingTop(), this.mSuggestionGridLayout.getPaddingRight(), i + this.mFooter.getMeasuredHeight());
      super.onMeasure(paramInt1, paramInt2);
      this.mLastWidthMeasureSpec = paramInt1;
      this.mLastHeightMeasureSpec = paramInt2;
      return;
    }
  }
  
  protected void onRemindersButtonPressed()
  {
    IntentDispatcherUtil.dispatchIntent(getContext(), "com.google.android.googlequicksearchbox.MY_REMINDERS");
  }
  
  public void onShow()
  {
    if (this.mIsViewVisible) {
      return;
    }
    this.mIsViewVisible = true;
    if (getVisibility() == 8) {
      setVisibility(0);
    }
    if (this.mNowRemoteClientLock == null)
    {
      this.mNowRemoteClientLock = this.mNowRemoteClient.newConnectionLock(TAG);
      this.mNowRemoteClientLock.acquire();
    }
    this.mRefreshManager.addViewActionListeners();
    this.mRefreshManager.recordViewStartTimes();
    this.mRefreshManager.registerPullToRefreshHandler(this.mProgressBar);
  }
  
  public void onViewsDismissed(PendingViewDismiss paramPendingViewDismiss)
  {
    this.mNowCardsView.onViewsDismissed(paramPendingViewDismiss);
  }
  
  public void populateView(@Nullable List<EntryItemStack> paramList, CardRenderingContext paramCardRenderingContext)
  {
    this.mNowCardsView.addCards(this.mActivity, paramList, paramCardRenderingContext, false, true, true, null, null, null, -1);
    this.mNowCardsView.hideTrainingPeekViewIfVisible();
    updateRemindersVisibility();
  }
  
  public boolean preStackViewOrderChange(List<View> paramList)
  {
    return this.mNowCardsView.commitFeedbackFromViews(paramList);
  }
  
  public void removeCard(EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    this.mNowCardsView.removeCard(paramEntryCardViewAdapter);
  }
  
  public void resetView()
  {
    this.mSuggestionGridLayout.removeAllViews();
    this.mNowCardsView.hideTrainingPeekViewIfVisible();
    stopProgressBar();
  }
  
  public void setAllowedSwipeDirections(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mSuggestionGridLayout.setAllowedSwipeDirections(paramBoolean1, paramBoolean2);
  }
  
  public void setContextHeader(Drawable paramDrawable, boolean paramBoolean, @Nullable View.OnClickListener paramOnClickListener)
  {
    this.mContextImage.setImageDrawable(paramDrawable);
    this.mContextImage.setOnClickListener(paramOnClickListener);
    WebImageView localWebImageView = this.mContextImage;
    boolean bool;
    if (paramOnClickListener != null)
    {
      bool = true;
      localWebImageView.setClickable(bool);
      this.mContextImage.setVisibility(0);
      this.mContextImage.setFocusableInTouchMode(paramBoolean);
      if (!paramBoolean) {
        break label97;
      }
      this.mContextImage.setContentDescription(getResources().getString(2131363299));
    }
    for (;;)
    {
      if (this.mDoodleListener != null) {
        this.mDoodleListener.onDoodleChanged(paramBoolean);
      }
      return;
      bool = false;
      break;
      label97:
      this.mContextImage.setContentDescription(null);
    }
  }
  
  public void setDependencies(Activity paramActivity, IntentStarter paramIntentStarter, PredictiveCardRefreshManager paramPredictiveCardRefreshManager, NowCardsViewWrapper paramNowCardsViewWrapper, NowRemoteClient paramNowRemoteClient, UndoDismissManager paramUndoDismissManager)
  {
    this.mActivity = ((Activity)Preconditions.checkNotNull(paramActivity));
    this.mIntentStarter = ((IntentStarter)Preconditions.checkNotNull(paramIntentStarter));
    this.mRefreshManager = ((PredictiveCardRefreshManager)Preconditions.checkNotNull(paramPredictiveCardRefreshManager));
    this.mNowCardsView = ((NowCardsViewWrapper)Preconditions.checkNotNull(paramNowCardsViewWrapper));
    this.mNowRemoteClient = ((NowRemoteClient)Preconditions.checkNotNull(paramNowRemoteClient));
    this.mUndoDismissManager = ((UndoDismissManager)Preconditions.checkNotNull(paramUndoDismissManager));
    this.mNowCardsView.setWindowInsets(this.mInsets);
    this.mNowCardsView.registerListeners();
    updateRemindersVisibility();
  }
  
  public void setDoodleListener(DoodleListener paramDoodleListener)
  {
    this.mDoodleListener = paramDoodleListener;
  }
  
  public void setInsets(Rect paramRect)
  {
    this.mInsets = paramRect;
    this.mFooterHider.setOffsetFromEdge(paramRect.bottom, true);
    this.mNowCardsView.setWindowInsets(paramRect);
    this.mUndoDismissManager.setBottomInset(paramRect.bottom);
  }
  
  public void setProximityToNow(float paramFloat)
  {
    if (paramFloat < 1.0E-006F)
    {
      setVisibility(8);
      return;
    }
    if (getVisibility() == 8) {
      setVisibility(0);
    }
    float f = this.mProximityToNowInterpolator.getInterpolation(paramFloat);
    this.mContextImage.setAlpha(f);
    this.mProgressBar.setAlpha(f);
    this.mFooter.setAlpha(f);
    getBackground().setAlpha((int)(0.5D + 255.0F * f));
  }
  
  public void showError(int paramInt)
  {
    resetView();
    View localView = LayoutInflater.from(getContext()).inflate(2130968670, this.mSuggestionGridLayout, false);
    ((TextView)localView.findViewById(2131296549)).setText(paramInt);
    localView.findViewById(2131296550).setVisibility(8);
    this.mNowCardsView.tagAsPredictiveView(localView);
    this.mSuggestionGridLayout.addView(localView);
  }
  
  public void showOptIn()
  {
    resetView();
    View localView = LayoutInflater.from(getContext()).inflate(2130968766, this.mSuggestionGridLayout, false);
    ((Button)localView.findViewById(2131296668)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent();
        localIntent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.velvet.tg.FirstRunActivity");
        localIntent.putExtra("skip_to_end", true);
        NowClientCardsView.this.getContext().startActivity(localIntent);
      }
    });
    this.mNowCardsView.tagAsPredictiveView(localView);
    this.mSuggestionGridLayout.addView(localView);
  }
  
  public void showOptionsMenu(View paramView)
  {
    this.mMenu = new PopupMenu(getContext(), paramView);
    this.mMenu.setOnDismissListener(new PopupMenu.OnDismissListener()
    {
      public void onDismiss(PopupMenu paramAnonymousPopupMenu)
      {
        NowClientCardsView.access$302(NowClientCardsView.this, null);
      }
    });
    Menu localMenu = this.mMenu.getMenu();
    localMenu.add(2131361967).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
    {
      public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem)
      {
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.velvet.ui.settings.SettingsActivity");
        NowClientCardsView.this.getContext().startActivity(localIntent);
        return true;
      }
    });
    localMenu.add(2131363570).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
    {
      public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem)
      {
        NowClientCardsView.this.mNowRemoteClient.logAction(LoggingRequest.forAnalyticsAction("BUTTON_PRESS", "SEND_FEEDBACK"));
        SendGoogleFeedback.launchGoogleFeedback(NowClientCardsView.this.getContext(), NowClientCardsView.this.mScrollViewControl);
        NowClientCardsView.this.mNowCardsView.commitAllFeedback(false);
        NowClientCardsView.this.mRefreshManager.refreshCards(2, true);
        return true;
      }
    });
    localMenu.add(2131363441).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
    {
      public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem)
      {
        new NowClientCardsView.HelpLauncher(NowClientCardsView.this, null).execute(new Void[0]);
        return true;
      }
    });
    this.mMenu.show();
  }
  
  public void showRemindersPeekAnimation()
  {
    if (this.mRemindersEnabled) {
      this.mNowCardsView.flashReminderIcon();
    }
  }
  
  public void showSinglePromoCard(EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    this.mNowCardsView.showSinglePromoCard(this.mActivity, paramEntryCardViewAdapter);
  }
  
  public void showViewsForSearch()
  {
    this.mSuggestionGridLayout.animate().translationY(0.0F).setInterpolator(BakedBezierInterpolator.INSTANCE).setListener(new AnimatorListenerAdapter()
    {
      public void onAnimationCancel(Animator paramAnonymousAnimator)
      {
        NowClientCardsView.this.mSuggestionGridLayout.setTranslationY(NowClientCardsView.this.getHeight());
      }
      
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        NowClientCardsView.this.layoutIfGone();
      }
      
      public void onAnimationStart(Animator paramAnonymousAnimator)
      {
        NowClientCardsView.this.mSuggestionGridLayout.setVisibility(0);
      }
    });
    Animations.showAndFadeIn(this.mProgressBar).setInterpolator(BakedBezierInterpolator.INSTANCE).withLayer();
    Animations.showAndFadeIn(this.mContextImage).setInterpolator(BakedBezierInterpolator.INSTANCE).withLayer();
  }
  
  public void startProgressBar()
  {
    this.mProgressBar.start();
  }
  
  public boolean startWebSearch(String paramString, @Nullable Location paramLocation)
  {
    WebSearchUtils.startWebSearch(this.mActivity, paramString, paramLocation);
    return true;
  }
  
  public void stopProgressBar()
  {
    this.mProgressBar.stop();
  }
  
  public void updateEntry(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2, Sidekick.Entry paramEntry3)
  {
    this.mNowCardsView.updateEntry(paramEntry1, paramEntry2, paramEntry3);
  }
  
  public static abstract interface DoodleListener
  {
    public abstract void onDoodleChanged(boolean paramBoolean);
  }
  
  private class HelpLauncher
    extends AsyncTask<Void, Void, Intent>
  {
    private HelpLauncher() {}
    
    protected Intent doInBackground(Void... paramVarArgs)
    {
      return NowClientCardsView.this.mNowRemoteClient.getHelpIntent("main");
    }
    
    protected void onPostExecute(Intent paramIntent)
    {
      NowClientCardsView.this.mIntentStarter.startActivity(new Intent[] { paramIntent });
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.NowClientCardsView
 * JD-Core Version:    0.7.0.1
 */