package com.google.android.velvet.presenter;

import android.view.View;
import com.google.android.search.core.Feature;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchError;
import com.google.android.search.core.state.ActionState;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.UiState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.core.state.VelvetEventBus.Observer;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.search.shared.ui.SuggestionGridLayout.LayoutParams;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.ui.MainContentView;
import com.google.android.voicesearch.CardFactory;
import com.google.android.voicesearch.SearchCardController;
import com.google.android.voicesearch.fragments.AbstractCardController;
import com.google.android.voicesearch.fragments.AbstractCardView;
import com.google.android.voicesearch.fragments.ControllerFactory;
import com.google.android.voicesearch.fragments.action.CancelAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public abstract class AbstractActionCardsPresenter
  extends MainContentPresenter
  implements VelvetEventBus.Observer
{
  private final List<ActionCardPresenterData> mCardDatas = Lists.newArrayList();
  private final CardFactory mCardFactory;
  private ControllerFactory mControllerFactory;
  private ActionData mCurrentActionData;
  private UpdateCardsTransaction mLatestUpdateCardsTransaction;
  private boolean mPostTransactionPending;
  private final VelvetFactory mVelvetFactory;
  
  protected AbstractActionCardsPresenter(String paramString, MainContentView paramMainContentView, VelvetFactory paramVelvetFactory, CardFactory paramCardFactory)
  {
    super(paramString, paramMainContentView);
    this.mVelvetFactory = paramVelvetFactory;
    this.mCardFactory = paramCardFactory;
  }
  
  private ActionCardPresenterData createData(Query paramQuery, VoiceAction paramVoiceAction)
  {
    if (this.mCurrentActionData == null) {
      Preconditions.checkState(paramVoiceAction instanceof SearchError);
    }
    for (ActionData localActionData = ActionData.NONE;; localActionData = this.mCurrentActionData)
    {
      SearchCardController localSearchCardController = this.mVelvetFactory.createCardController(this, getEventBus(), paramQuery, localActionData);
      AbstractCardController localAbstractCardController = getControllerFactory().createController(paramVoiceAction, localSearchCardController);
      localAbstractCardController.setVoiceAction(paramVoiceAction);
      return new ActionCardPresenterData(localSearchCardController, localAbstractCardController);
    }
  }
  
  private ControllerFactory getControllerFactory()
  {
    if (this.mControllerFactory == null) {
      this.mControllerFactory = this.mVelvetFactory.createControllerFactory(getVelvetPresenter().getIntentStarter());
    }
    return this.mControllerFactory;
  }
  
  private void markCurrentUpdateCardsTransactionStale()
  {
    if (this.mLatestUpdateCardsTransaction != null) {
      UpdateCardsTransaction.access$002(this.mLatestUpdateCardsTransaction, true);
    }
  }
  
  private static void maybeUpdateActionDataForCancelButton(ActionCardPresenterData paramActionCardPresenterData, Query paramQuery, ActionData paramActionData)
  {
    if (paramActionCardPresenterData.getController().getVoiceAction() == CancelAction.CANCEL_BUTTON) {
      paramActionCardPresenterData.getCardController().setQuery(paramQuery, paramActionData);
    }
  }
  
  private void postUpdateCardsTransaction()
  {
    markCurrentUpdateCardsTransactionStale();
    this.mLatestUpdateCardsTransaction = new UpdateCardsTransaction();
    post(this.mLatestUpdateCardsTransaction);
  }
  
  protected void clearActionOnly()
  {
    this.mCardDatas.clear();
  }
  
  public boolean onBackPressed()
  {
    boolean bool = false;
    Iterator localIterator = this.mCardDatas.iterator();
    while (localIterator.hasNext())
    {
      ActionCardPresenterData localActionCardPresenterData = (ActionCardPresenterData)localIterator.next();
      if (localActionCardPresenterData.getController().isAttached()) {
        bool |= localActionCardPresenterData.getController().onBackPressed();
      }
    }
    return bool;
  }
  
  protected void onBeforeCardsShown(MainContentUi paramMainContentUi) {}
  
  public void onPause()
  {
    super.onPause();
    getEventBus().getActionState().cancelCardCountDown();
  }
  
  protected void onPreDetach()
  {
    markCurrentUpdateCardsTransactionStale();
    getEventBus().getActionState().clearReadiness(this.mCurrentActionData);
  }
  
  public void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    if (paramEvent.hasTtsChanged())
    {
      Iterator localIterator7 = this.mCardDatas.iterator();
      while (localIterator7.hasNext()) {
        ((ActionCardPresenterData)localIterator7.next()).getController().onTtsPlayStateChanged();
      }
    }
    if ((!paramEvent.hasActionChanged()) && (!paramEvent.hasUiChanged())) {}
    for (;;)
    {
      return;
      ActionState localActionState = getEventBus().getActionState();
      Query localQuery = getEventBus().getQueryState().getCommittedQuery();
      this.mCurrentActionData = localActionState.getActionData();
      List localList = localActionState.getVoiceActions();
      if ((localList != null) && (!localList.isEmpty()) && (!this.mCardDatas.isEmpty()))
      {
        VoiceAction localVoiceAction3 = (VoiceAction)localList.get(0);
        if (((localVoiceAction3 instanceof SearchError)) && (((ActionCardPresenterData)this.mCardDatas.get(0)).getController().getVoiceAction() != localVoiceAction3))
        {
          ActionCardPresenterData localActionCardPresenterData5 = createData(localQuery, localVoiceAction3);
          localActionCardPresenterData5.getController().start();
          this.mCardDatas.add(0, localActionCardPresenterData5);
          this.mPostTransactionPending = true;
        }
      }
      if ((localList == null) || (localList.isEmpty()))
      {
        int i = 0;
        Iterator localIterator1 = this.mCardDatas.iterator();
        while (localIterator1.hasNext())
        {
          ActionCardPresenterData localActionCardPresenterData1 = (ActionCardPresenterData)localIterator1.next();
          if (!localActionCardPresenterData1.isRemovePending())
          {
            i = 1;
            localActionCardPresenterData1.remove();
          }
        }
        if (i != 0)
        {
          this.mPostTransactionPending = false;
          postUpdateCardsTransaction();
        }
      }
      else
      {
        ArrayList localArrayList = Lists.newArrayListWithExpectedSize(localList.size());
        getControllerFactory();
        ImmutableList localImmutableList = ImmutableList.copyOf(localList);
        Iterator localIterator2 = this.mCardDatas.iterator();
        int j = 1;
        int k = 0;
        int m = 0;
        HashSet localHashSet = Sets.newHashSet();
        Iterator localIterator3 = this.mCardDatas.iterator();
        while (localIterator3.hasNext())
        {
          ActionCardPresenterData localActionCardPresenterData4 = (ActionCardPresenterData)localIterator3.next();
          if (!localActionCardPresenterData4.isRemovePending()) {
            localHashSet.add(localActionCardPresenterData4.getController().getVoiceAction());
          }
        }
        Iterator localIterator4 = localImmutableList.iterator();
        while (localIterator4.hasNext())
        {
          VoiceAction localVoiceAction1 = (VoiceAction)localIterator4.next();
          if (localList.contains(localVoiceAction1))
          {
            ActionCardPresenterData localActionCardPresenterData2;
            AbstractCardController localAbstractCardController;
            VoiceAction localVoiceAction2;
            label521:
            for (;;)
            {
              if (localIterator2.hasNext()) {}
              for (localActionCardPresenterData2 = (ActionCardPresenterData)localIterator2.next();; localActionCardPresenterData2 = null)
              {
                if ((localActionCardPresenterData2 != null) && (localActionCardPresenterData2.isRemovePending())) {
                  break label521;
                }
                if (j == 0) {
                  break label677;
                }
                if (localActionCardPresenterData2 == null) {
                  break label674;
                }
                localAbstractCardController = localActionCardPresenterData2.getController();
                localVoiceAction2 = localAbstractCardController.getVoiceAction();
                if (localVoiceAction2 != localVoiceAction1) {
                  break label523;
                }
                maybeUpdateActionDataForCancelButton(localActionCardPresenterData2, localQuery, this.mCurrentActionData);
                break;
              }
            }
            label523:
            if (localHashSet.contains(localVoiceAction1))
            {
              while ((localActionCardPresenterData2.getController().getVoiceAction() != localVoiceAction1) && (localIterator2.hasNext()))
              {
                k = 1;
                localActionCardPresenterData2.remove();
                localActionCardPresenterData2 = (ActionCardPresenterData)localIterator2.next();
              }
              if (localActionCardPresenterData2.getController().getVoiceAction() == localVoiceAction1) {
                maybeUpdateActionDataForCancelButton(localActionCardPresenterData2, localQuery, this.mCurrentActionData);
              }
            }
            else if ((Feature.FOLLOW_ON.isEnabled()) && (this.mCurrentActionData != null) && (this.mCurrentActionData.isFollowOnForPreviousAction()) && (localVoiceAction2.getClass() == localVoiceAction1.getClass()))
            {
              localAbstractCardController.setVoiceAction(localVoiceAction1);
              localActionCardPresenterData2.getCardController().setQuery(localQuery, this.mCurrentActionData);
              localAbstractCardController.start();
              continue;
            }
            label674:
            j = 0;
            label677:
            if (localActionCardPresenterData2 != null)
            {
              k = 1;
              localActionCardPresenterData2.remove();
            }
            ActionCardPresenterData localActionCardPresenterData3 = createData(localQuery, localVoiceAction1);
            localActionCardPresenterData3.getController().start();
            m = 1;
            localArrayList.add(localActionCardPresenterData3);
          }
        }
        while (localIterator2.hasNext())
        {
          k = 1;
          ((ActionCardPresenterData)localIterator2.next()).remove();
        }
        this.mCardDatas.addAll(localArrayList);
        if ((m != 0) || (k != 0)) {
          this.mPostTransactionPending = true;
        }
        if ((this.mPostTransactionPending) && (getEventBus().getUiState().shouldShowCards()))
        {
          this.mPostTransactionPending = false;
          postUpdateCardsTransaction();
        }
        if (localActionState.isReady()) {
          if (localActionState.takeCountDownCancelled())
          {
            Iterator localIterator6 = this.mCardDatas.iterator();
            while (localIterator6.hasNext()) {
              ((ActionCardPresenterData)localIterator6.next()).getController().cancelCountDown();
            }
          }
          else if (localActionState.takeCountDownCancelledByUser())
          {
            Iterator localIterator5 = this.mCardDatas.iterator();
            while (localIterator5.hasNext()) {
              ((ActionCardPresenterData)localIterator5.next()).getController().cancelCountDownByUser();
            }
          }
        }
      }
    }
  }
  
  public void onViewScrolled(boolean paramBoolean)
  {
    if (getScrollViewControl().getScrollY() > 0) {
      getEventBus().getActionState().cancelCardCountDownByUser();
    }
  }
  
  public void onViewsDismissed(PendingViewDismiss paramPendingViewDismiss)
  {
    Iterator localIterator = paramPendingViewDismiss.getDismissedViews().iterator();
    while (localIterator.hasNext())
    {
      View localView = (View)localIterator.next();
      if ((localView instanceof AbstractCardView)) {
        ((AbstractCardView)localView).dismissed();
      }
    }
  }
  
  protected void setLastVisibleCard(@Nullable View paramView, MainContentUi paramMainContentUi) {}
  
  class UpdateCardsTransaction
    extends MainContentPresenter.Transaction
  {
    private boolean mAddingViews;
    private View mLastShownCard;
    private boolean mNeedsUpdate;
    private int mNumPreparedControllers = 0;
    private boolean mStale;
    
    UpdateCardsTransaction() {}
    
    private int getCardColumn()
    {
      if ((AbstractActionCardsPresenter.this.isAttached()) && (AbstractActionCardsPresenter.this.getVelvetPresenter().getConfig().shouldCenterResultCardInLandscape())) {
        return -1;
      }
      return 0;
    }
    
    public void commit(MainContentUi paramMainContentUi)
    {
      if ((!this.mStale) && (this.mNeedsUpdate))
      {
        AbstractActionCardsPresenter.this.onBeforeCardsShown(paramMainContentUi);
        if (this.mAddingViews) {
          paramMainContentUi.getScrollViewControl().smoothScrollToY(0);
        }
        SuggestionGridLayout localSuggestionGridLayout = paramMainContentUi.getCardsView();
        Iterator localIterator1 = AbstractActionCardsPresenter.this.mCardDatas.iterator();
        while (localIterator1.hasNext())
        {
          ActionCardPresenterData localActionCardPresenterData2 = (ActionCardPresenterData)localIterator1.next();
          if (localActionCardPresenterData2.isRemovePending())
          {
            localSuggestionGridLayout.removeView(localActionCardPresenterData2.getView());
            localIterator1.remove();
          }
        }
        int i = 0;
        Iterator localIterator2 = AbstractActionCardsPresenter.this.mCardDatas.iterator();
        while (localIterator2.hasNext())
        {
          ActionCardPresenterData localActionCardPresenterData1 = (ActionCardPresenterData)localIterator2.next();
          if (localActionCardPresenterData1.isShowPending())
          {
            localActionCardPresenterData1.show();
            AbstractCardView localAbstractCardView = localActionCardPresenterData1.getView();
            localSuggestionGridLayout.addViewWithIndexAndColumn(localAbstractCardView, i, getCardColumn());
            ((SuggestionGridLayout.LayoutParams)localAbstractCardView.getLayoutParams()).removeOnDismiss = false;
          }
          i++;
        }
        AbstractActionCardsPresenter.this.setLastVisibleCard(this.mLastShownCard, paramMainContentUi);
      }
    }
    
    public boolean prepare()
    {
      if (this.mStale) {}
      int i;
      do
      {
        return true;
        i = AbstractActionCardsPresenter.this.mCardDatas.size();
        int j = 1;
        if ((j != 0) && (this.mNumPreparedControllers < i))
        {
          ActionCardPresenterData localActionCardPresenterData = (ActionCardPresenterData)AbstractActionCardsPresenter.this.mCardDatas.get(this.mNumPreparedControllers);
          if ((localActionCardPresenterData.isRemovePending()) && (!this.mNeedsUpdate)) {
            this.mNeedsUpdate = true;
          }
          while (localActionCardPresenterData.isRemovePending())
          {
            this.mNumPreparedControllers = (1 + this.mNumPreparedControllers);
            break;
          }
          if (localActionCardPresenterData.isCreateViewPending())
          {
            localActionCardPresenterData.setView(AbstractActionCardsPresenter.this.mCardFactory.createCard(localActionCardPresenterData.getController()));
            this.mAddingViews = true;
            this.mNeedsUpdate = true;
            j = 0;
          }
          for (;;)
          {
            this.mLastShownCard = localActionCardPresenterData.getView();
            break;
            if (localActionCardPresenterData.isShowPending()) {
              this.mAddingViews = true;
            }
          }
        }
      } while (this.mNumPreparedControllers == i);
      return false;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.AbstractActionCardsPresenter
 * JD-Core Version:    0.7.0.1
 */