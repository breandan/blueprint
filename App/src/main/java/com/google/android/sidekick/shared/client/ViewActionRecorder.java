package com.google.android.sidekick.shared.client;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.shared.ui.ScrollViewControl.ScrollListener;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.ExecutedUserActionWriter;
import com.google.android.sidekick.shared.ui.PredictiveCardWrapper;
import com.google.android.sidekick.shared.util.ExecutedUserActionBuilder;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;

public class ViewActionRecorder
{
  private final Context mAppContext;
  private final Clock mClock;
  private final ExecutedUserActionWriter mExecutedUserActionWriter;
  private final View.OnLayoutChangeListener mLayoutChangeListener = new View.OnLayoutChangeListener()
  {
    public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
    {
      ViewActionRecorder.this.recordViewEndTimes();
      if (ViewActionRecorder.this.mScrollableCardView.isPredictiveOnlyMode()) {
        ViewActionRecorder.this.recordViewStartTimes();
      }
    }
  };
  private final ScrollViewControl.ScrollListener mScrollListener = new ScrollViewControl.ScrollListener()
  {
    private int mPrevScrollY = 0;
    private boolean mScrolling = false;
    
    public void onOverscroll(int paramAnonymousInt) {}
    
    public void onOverscrollFinished() {}
    
    public void onOverscrollStarted() {}
    
    public void onScrollAnimationFinished()
    {
      ViewActionRecorder.this.recordViewStartTimes();
      this.mScrolling = false;
    }
    
    public void onScrollChanged(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if ((!this.mScrolling) && (this.mPrevScrollY != paramAnonymousInt1))
      {
        ViewActionRecorder.this.recordViewEndTimes();
        this.mScrolling = true;
      }
      this.mPrevScrollY = paramAnonymousInt1;
    }
    
    public void onScrollFinished()
    {
      ViewActionRecorder.this.recordViewStartTimes();
      this.mScrolling = false;
    }
    
    public void onScrollMarginConsumed(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2) {}
  };
  private ScrollableCardView mScrollableCardView;
  
  public ViewActionRecorder(Context paramContext, Clock paramClock, ExecutedUserActionWriter paramExecutedUserActionWriter)
  {
    this.mAppContext = paramContext;
    this.mClock = paramClock;
    this.mExecutedUserActionWriter = paramExecutedUserActionWriter;
  }
  
  @Nullable
  private View getCardView(View paramView)
  {
    if ((paramView instanceof PredictiveCardWrapper)) {
      return ((PredictiveCardWrapper)paramView).getCardView();
    }
    return null;
  }
  
  @Nullable
  private List<View> getChildEntryViews(View paramView)
  {
    return (List)paramView.getTag(2131296281);
  }
  
  private static boolean isCardVisible(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 < 0) {}
    int i;
    int j;
    do
    {
      return false;
      i = paramInt1 + paramView.getHeight();
      j = Math.max(paramInt2, paramInt1);
    } while ((Math.min(paramInt3, i) - j < paramView.getHeight() / 2) && ((paramInt1 > paramInt2) || (i < paramInt3)));
    return true;
  }
  
  private boolean isExpanded(View paramView)
  {
    Boolean localBoolean = (Boolean)paramView.getTag(2131296283);
    if (localBoolean == null) {
      return true;
    }
    return localBoolean.booleanValue();
  }
  
  private Sidekick.ExecutedUserAction maybeCreateUserActionForEntryView(View paramView, boolean paramBoolean, long paramLong)
  {
    Long localLong = (Long)paramView.getTag(2131296279);
    if (localLong != null)
    {
      long l = paramLong - localLong.longValue();
      if (l >= 500L)
      {
        Sidekick.Entry localEntry = (Sidekick.Entry)paramView.getTag(2131296280);
        Iterator localIterator = localEntry.getEntryActionList().iterator();
        Sidekick.Action localAction;
        do
        {
          boolean bool = localIterator.hasNext();
          localExecutedUserAction = null;
          if (!bool) {
            break;
          }
          localAction = (Sidekick.Action)localIterator.next();
        } while (localAction.getType() != 21);
        Sidekick.ExecutedUserAction localExecutedUserAction = new ExecutedUserActionBuilder(localEntry, localAction, paramLong).withCardHeight(paramView.getHeight()).withPortrait(paramBoolean).withExecutionTimeMs(l).build();
        paramView.setTag(2131296279, null);
        return localExecutedUserAction;
      }
    }
    return null;
  }
  
  private boolean maybeRecordViewStartTime(ScrollViewControl paramScrollViewControl, int paramInt1, int paramInt2, View paramView, long paramLong)
  {
    if ((paramView.getTag(2131296279) == null) && (isCardVisible(paramView, paramScrollViewControl.getDescendantTop(paramView), paramInt1, paramInt2)))
    {
      paramView.setTag(2131296279, Long.valueOf(paramLong));
      return true;
    }
    return false;
  }
  
  public void addViewActionListeners()
  {
    this.mScrollableCardView.getScrollViewControl().addScrollListener(this.mScrollListener);
    this.mScrollableCardView.getSuggestionGridLayout().addOnLayoutChangeListener(this.mLayoutChangeListener);
  }
  
  public void recordViewEndTimes()
  {
    long l = this.mClock.currentTimeMillis();
    DisplayMetrics localDisplayMetrics = this.mAppContext.getResources().getDisplayMetrics();
    boolean bool;
    LinkedList localLinkedList;
    int i;
    if (localDisplayMetrics.heightPixels > localDisplayMetrics.widthPixels)
    {
      bool = true;
      localLinkedList = Lists.newLinkedList();
      i = this.mScrollableCardView.getSuggestionGridLayout().getChildCount();
    }
    for (int j = 0;; j++)
    {
      if (j >= i) {
        break label196;
      }
      View localView = getCardView(this.mScrollableCardView.getSuggestionGridLayout().getChildAt(j));
      if (localView != null)
      {
        Sidekick.ExecutedUserAction localExecutedUserAction1 = maybeCreateUserActionForEntryView(localView, bool, l);
        if (localExecutedUserAction1 != null) {
          localLinkedList.add(localExecutedUserAction1);
        }
        List localList = getChildEntryViews(localView);
        if (localList != null)
        {
          Iterator localIterator = localList.iterator();
          for (;;)
          {
            if (localIterator.hasNext())
            {
              Sidekick.ExecutedUserAction localExecutedUserAction2 = maybeCreateUserActionForEntryView((View)localIterator.next(), bool, l);
              if (localExecutedUserAction2 != null)
              {
                localLinkedList.add(localExecutedUserAction2);
                continue;
                bool = false;
                break;
              }
            }
          }
        }
      }
    }
    label196:
    if (localLinkedList.size() > 0)
    {
      RecordViewsTask localRecordViewsTask = new RecordViewsTask(localLinkedList, this.mExecutedUserActionWriter, null);
      localRecordViewsTask.execute(new Void[0]);
    }
  }
  
  public void recordViewStartTimes()
  {
    if ((this.mScrollableCardView.isVisible()) && (this.mScrollableCardView.isPredictiveOnlyMode()))
    {
      long l = this.mClock.currentTimeMillis();
      ScrollViewControl localScrollViewControl = this.mScrollableCardView.getScrollViewControl();
      SuggestionGridLayout localSuggestionGridLayout = this.mScrollableCardView.getSuggestionGridLayout();
      int i = localScrollViewControl.getScrollY();
      int j = i + localScrollViewControl.getViewportHeight();
      int k = localSuggestionGridLayout.getChildCount();
      for (int m = 0; m < k; m++)
      {
        View localView1 = localSuggestionGridLayout.getChildAt(m);
        View localView2 = getCardView(localView1);
        if ((localView2 != null) && (localView1.getHeight() > 0) && (localSuggestionGridLayout.isViewLocallyVisible(localView1)) && (isExpanded(localView2)))
        {
          if ((maybeRecordViewStartTime(localScrollViewControl, i, j, localView2, l)) && ((localView1 instanceof PredictiveCardWrapper))) {
            this.mScrollableCardView.notifyCardVisible((PredictiveCardWrapper)localView1);
          }
          List localList = getChildEntryViews(localView2);
          if (localList != null)
          {
            Iterator localIterator = localList.iterator();
            while (localIterator.hasNext()) {
              maybeRecordViewStartTime(localScrollViewControl, i, j, (View)localIterator.next(), l);
            }
          }
        }
      }
    }
  }
  
  public void removeViewActionListeners()
  {
    this.mScrollableCardView.getScrollViewControl().removeScrollListener(this.mScrollListener);
    this.mScrollableCardView.getSuggestionGridLayout().removeOnLayoutChangeListener(this.mLayoutChangeListener);
  }
  
  public void setScrollableCardView(ScrollableCardView paramScrollableCardView)
  {
    this.mScrollableCardView = ((ScrollableCardView)Preconditions.checkNotNull(paramScrollableCardView));
  }
  
  private static class RecordViewsTask
    extends AsyncTask<Void, Void, Void>
  {
    private final ExecutedUserActionWriter mExecutedUserActionWriter;
    private final List<Sidekick.ExecutedUserAction> mViewActions;
    
    private RecordViewsTask(List<Sidekick.ExecutedUserAction> paramList, ExecutedUserActionWriter paramExecutedUserActionWriter)
    {
      this.mViewActions = paramList;
      this.mExecutedUserActionWriter = paramExecutedUserActionWriter;
    }
    
    protected Void doInBackground(Void... paramVarArgs)
    {
      this.mExecutedUserActionWriter.saveExecutedUserActions(this.mViewActions);
      return null;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.ViewActionRecorder
 * JD-Core Version:    0.7.0.1
 */