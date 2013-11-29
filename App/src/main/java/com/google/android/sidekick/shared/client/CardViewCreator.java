package com.google.android.sidekick.shared.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.training.BackOfCardAdapter;
import com.google.android.sidekick.shared.ui.PredictiveCardWrapper;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardViewCreator
  implements Runnable
{
  private static final String TAG = Tag.getTag(CardViewCreator.class);
  private final Activity mActivity;
  private final boolean mCanDismiss;
  private boolean mCanceled = false;
  private final PredictiveCardContainer mCardContainer;
  private final boolean mClearViewFirst;
  private final List<EntryItemStack> mEntries;
  private final LayoutInflater mLayoutInflater;
  private final int mNumEntries;
  private final NowCardsViewWrapper mParentView;
  private int mPreparedEntries;
  private List<EntryCardViewAdapter> mPreparingStack;
  private final boolean mScrollToCards;
  private final Sidekick.Entry mScrollToEntry;
  private final int mScrollToPosition;
  private final ArrayList<View>[] mStacks;
  private final Map<Long, Bundle> mState;
  private final ScheduledSingleThreadedExecutor mUiThread;
  private final View[] mViews;
  private final List<View> mViewsToRemove;
  
  public CardViewCreator(Activity paramActivity, LayoutInflater paramLayoutInflater, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, NowCardsViewWrapper paramNowCardsViewWrapper, PredictiveCardContainer paramPredictiveCardContainer, List<EntryItemStack> paramList, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Map<Long, Bundle> paramMap, List<View> paramList1, Sidekick.Entry paramEntry, int paramInt)
  {
    this.mActivity = paramActivity;
    this.mUiThread = paramScheduledSingleThreadedExecutor;
    this.mLayoutInflater = paramLayoutInflater;
    this.mParentView = paramNowCardsViewWrapper;
    this.mCardContainer = paramPredictiveCardContainer;
    this.mEntries = paramList;
    this.mScrollToCards = paramBoolean1;
    this.mClearViewFirst = paramBoolean2;
    this.mCanDismiss = paramBoolean3;
    this.mScrollToEntry = paramEntry;
    this.mScrollToPosition = paramInt;
    this.mState = paramMap;
    this.mViewsToRemove = paramList1;
    this.mNumEntries = this.mEntries.size();
    this.mViews = new View[this.mNumEntries];
    this.mStacks = new ArrayList[this.mNumEntries];
  }
  
  private void prepareNextView()
  {
    EntryCardViewAdapter localEntryCardViewAdapter;
    PredictiveCardWrapper localPredictiveCardWrapper;
    if (this.mPreparingStack != null)
    {
      localEntryCardViewAdapter = (EntryCardViewAdapter)this.mPreparingStack.get(-1 + (this.mPreparingStack.size() - this.mStacks[this.mPreparedEntries].size()));
      if (localEntryCardViewAdapter != null)
      {
        localPredictiveCardWrapper = this.mParentView.createPredictiveCardForAdapter(this.mActivity, this.mLayoutInflater, localEntryCardViewAdapter, this.mParentView.getLayout(), this.mCardContainer);
        this.mParentView.tagAsPredictiveView(localPredictiveCardWrapper);
        if (this.mState != null) {
          restoreCardState(localPredictiveCardWrapper, localEntryCardViewAdapter);
        }
        if (this.mPreparingStack == null) {
          break label263;
        }
        this.mStacks[this.mPreparedEntries].add(localPredictiveCardWrapper);
        if (this.mStacks[this.mPreparedEntries].size() != this.mPreparingStack.size()) {
          break label161;
        }
        this.mPreparingStack = null;
      }
    }
    for (;;)
    {
      this.mPreparedEntries = (1 + this.mPreparedEntries);
      label161:
      return;
      List localList = ((EntryItemStack)this.mEntries.get(this.mPreparedEntries)).getEntriesToShow();
      boolean bool = localList.isEmpty();
      localEntryCardViewAdapter = null;
      if (bool) {
        break;
      }
      if (localList.size() == 1)
      {
        localEntryCardViewAdapter = (EntryCardViewAdapter)localList.get(0);
        break;
      }
      this.mPreparingStack = localList;
      this.mStacks[this.mPreparedEntries] = Lists.newArrayListWithExpectedSize(localList.size());
      localEntryCardViewAdapter = (EntryCardViewAdapter)localList.get(-1 + localList.size());
      break;
      label263:
      this.mViews[this.mPreparedEntries] = localPredictiveCardWrapper;
    }
  }
  
  private void restoreCardState(PredictiveCardWrapper paramPredictiveCardWrapper, EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    long l = ProtoUtils.getEntryHash(paramEntryCardViewAdapter.getEntry());
    Bundle localBundle = (Bundle)this.mState.get(Long.valueOf(l));
    if (localBundle != null)
    {
      SparseArray localSparseArray = localBundle.getSparseParcelableArray("card:views");
      if (localSparseArray != null) {
        paramPredictiveCardWrapper.restoreHierarchyState(localSparseArray);
      }
      if (localBundle.getBoolean("card_expanded"))
      {
        BackOfCardAdapter localBackOfCardAdapter = paramEntryCardViewAdapter.createBackOfCardAdapter(this.mUiThread);
        View localView2 = this.mParentView.createCardSettingsForAdapter(localBackOfCardAdapter);
        paramPredictiveCardWrapper.showSettingsView(this.mCardContainer, localView2, localBackOfCardAdapter);
      }
      int i = localBundle.getInt("card:focusedViewId", -1);
      if (i != -1)
      {
        View localView1 = paramPredictiveCardWrapper.findViewById(i);
        if (localView1 != null) {
          localView1.requestFocus();
        }
      }
    }
  }
  
  public void cancel()
  {
    this.mCanceled = true;
  }
  
  public void run()
  {
    if (this.mCanceled) {
      return;
    }
    long l1 = System.currentTimeMillis();
    int i = 0;
    label14:
    if (this.mPreparedEntries < this.mNumEntries) {}
    for (;;)
    {
      try
      {
        prepareNextView();
        j = 0;
        long l2 = System.currentTimeMillis() - l1;
        i++;
        if ((j == 0) && (l2 < 5L)) {
          break label14;
        }
        if (j != 0) {
          break;
        }
        this.mUiThread.execute(this);
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        if (this.mCanceled)
        {
          Log.e(TAG, "Caught exception in canceled CardViewCreator", localRuntimeException);
          return;
        }
        throw localRuntimeException;
      }
      this.mParentView.addCardViews(this.mNumEntries, this.mViews, this.mStacks, this.mViewsToRemove, this.mScrollToPosition, this.mScrollToEntry, this.mScrollToCards, this.mClearViewFirst, this.mCanDismiss, this);
      int j = 1;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.CardViewCreator
 * JD-Core Version:    0.7.0.1
 */