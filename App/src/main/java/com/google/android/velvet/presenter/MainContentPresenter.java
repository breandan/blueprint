package com.google.android.velvet.presenter;

import android.view.View;
import android.view.ViewGroup;
import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.sidekick.shared.ui.NowProgressBar;
import com.google.android.velvet.ui.MainContentView;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MainContentPresenter
  extends VelvetFragmentPresenter
{
  private static final Transaction REMOVE_ALL_VIEWS = new Transaction("REMOVE ALL VIEWS")
  {
    public void commit(MainContentUi paramAnonymousMainContentUi)
    {
      paramAnonymousMainContentUi.getCardsView().removeAllViews();
    }
  };
  private static final Transaction RESET_SCROLL = new Transaction("RESET SCROLL")
  {
    public void commit(MainContentUi paramAnonymousMainContentUi)
    {
      paramAnonymousMainContentUi.getScrollViewControl().setScrollY(0);
    }
  };
  private final MainContentView mView;
  
  protected MainContentPresenter(String paramString, MainContentView paramMainContentView)
  {
    super(paramString);
    this.mView = paramMainContentView;
  }
  
  public ViewGroup getCardContainer()
  {
    Preconditions.checkState(isAttached());
    return this.mView.getCardsView();
  }
  
  protected float getMainContentBackCollapsibleMarginRatio()
  {
    Preconditions.checkState(isAttached());
    return this.mView.getMainContentBackCollapsibleMarginRatio();
  }
  
  @Nullable
  public NowProgressBar getProgressBar()
  {
    Preconditions.checkState(isAttached());
    return this.mView.getProgressBar();
  }
  
  protected float getRelativeScrollDistanceFromTop(@Nonnull View paramView)
  {
    Preconditions.checkState(isAttached());
    return this.mView.getRelativeScrollDistanceFromTop(paramView);
  }
  
  public void onViewScrolled(boolean paramBoolean) {}
  
  public void onViewsDismissed(PendingViewDismiss paramPendingViewDismiss) {}
  
  public void post(@Nonnull Transaction paramTransaction)
  {
    if (isAttached()) {
      this.mView.post(paramTransaction);
    }
  }
  
  protected void postAddViews(final int paramInt, @Nonnull final Iterable<? extends View> paramIterable)
  {
    Preconditions.checkNotNull(paramIterable);
    post(new Transaction("addViews", paramIterable, paramInt)
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        int i = paramInt;
        Iterator localIterator = paramIterable.iterator();
        while (localIterator.hasNext())
        {
          View localView = (View)localIterator.next();
          paramAnonymousMainContentUi.getCardsView().addView(localView, i);
          if (i != -1) {
            i++;
          }
        }
      }
    });
  }
  
  protected void postAddViews(int paramInt, @Nonnull View... paramVarArgs)
  {
    postAddViews(paramInt, Arrays.asList(paramVarArgs));
  }
  
  protected void postAddViews(@Nonnull View... paramVarArgs)
  {
    postAddViews(-1, paramVarArgs);
  }
  
  protected void postRemoveAllViews()
  {
    post(REMOVE_ALL_VIEWS);
  }
  
  protected void postRemoveViews(@Nonnull final View... paramVarArgs)
  {
    Preconditions.checkNotNull(paramVarArgs);
    post(new Transaction("removeViews", paramVarArgs)
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        for (View localView : paramVarArgs) {
          paramAnonymousMainContentUi.getCardsView().removeView(localView);
        }
      }
    });
  }
  
  protected void postResetScroll()
  {
    post(RESET_SCROLL);
  }
  
  protected void postRestoreSearchPlateStickiness()
  {
    post(new Transaction()
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        paramAnonymousMainContentUi.setSearchPlateStuckToScrollingView(false);
      }
    });
  }
  
  protected void postSetLayoutAnimationsEnabled(final boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "true";; str = "false")
    {
      post(new Transaction("setLayoutTransitionsEnabled", str)
      {
        public void commit(MainContentUi paramAnonymousMainContentUi)
        {
          paramAnonymousMainContentUi.getCardsView().setLayoutTransitionsEnabled(paramBoolean);
        }
      });
      return;
    }
  }
  
  protected void postSetLayoutTransitionStartDelay(final int paramInt, final long paramLong)
  {
    post(new Transaction()
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        paramAnonymousMainContentUi.getCardsView().setLayoutTransitionStartDelay(paramInt, paramLong);
      }
    });
  }
  
  protected void postSetMainContentFrontScrimVisible(final boolean paramBoolean)
  {
    post(new Transaction("setMainContentFrontScrimVisible")
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        paramAnonymousMainContentUi.setMainContentFrontScrimVisible(paramBoolean);
      }
    });
  }
  
  protected void postSetMatchPortraitMode(final boolean paramBoolean)
  {
    post(new Transaction()
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        paramAnonymousMainContentUi.setMatchPortraitMode(paramBoolean);
      }
    });
  }
  
  protected void postSetVisibility(@Nonnull final View paramView, final int paramInt)
  {
    Preconditions.checkNotNull(paramView);
    post(new Transaction("setVisibility", paramView, paramInt)
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        paramView.setVisibility(paramInt);
      }
    });
  }
  
  protected void postSmoothScrollTo(final int paramInt)
  {
    post(new Transaction("smoothScrollTo", paramInt)
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        paramAnonymousMainContentUi.getScrollViewControl().smoothScrollToY(paramInt);
      }
    });
  }
  
  public boolean preStackViewOrderChange(@Nonnull Iterable<View> paramIterable)
  {
    return false;
  }
  
  protected void resetChildDismissState(@Nonnull View paramView)
  {
    if (isAttached()) {
      this.mView.getCardsView().resetChildDismissState(paramView);
    }
  }
  
  protected void showToast(int paramInt)
  {
    if (isAttached()) {
      this.mView.showToast(paramInt);
    }
  }
  
  public static abstract class Transaction
  {
    private String mDesc;
    private Object mObj;
    private int mVal;
    
    protected Transaction()
    {
      this(null, null, 0);
    }
    
    protected Transaction(String paramString)
    {
      this(paramString, null, 0);
    }
    
    protected Transaction(String paramString, int paramInt)
    {
      this(paramString, null, paramInt);
    }
    
    protected Transaction(String paramString, Object paramObject)
    {
      this(paramString, paramObject, 0);
    }
    
    protected Transaction(String paramString, Object paramObject, int paramInt)
    {
      this.mDesc = paramString;
      this.mObj = paramObject;
      this.mVal = paramInt;
    }
    
    public abstract void commit(MainContentUi paramMainContentUi);
    
    public boolean prepare()
    {
      return true;
    }
    
    public String toString()
    {
      if (this.mDesc != null)
      {
        if (this.mObj != null) {
          return this.mDesc + "[" + this.mObj + "," + this.mVal + "]";
        }
        return this.mDesc + "[" + this.mVal + "]";
      }
      return getClass().getSimpleName();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.MainContentPresenter
 * JD-Core Version:    0.7.0.1
 */