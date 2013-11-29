package com.google.android.velvet.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchController;
import com.google.android.search.core.state.DiscoveryState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.core.state.VelvetEventBus.Observer;
import com.google.android.search.core.util.HelpActionUtils;
import com.google.android.search.shared.api.SearchPlateUi;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.speech.contacts.ContactRetriever;
import com.google.android.velvet.ActionDiscoveryData;
import com.google.android.velvet.VelvetApplication;
import com.google.android.velvet.ui.MainContentView;
import com.google.android.voicesearch.fragments.SimpleHelpCard;
import com.google.android.voicesearch.fragments.SimpleHelpController;
import com.google.android.voicesearch.fragments.VoiceSearchController;
import com.google.android.voicesearch.fragments.action.HelpAction;
import com.google.android.voicesearch.util.ExampleContactHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.majel.proto.ActionV2Protos.HelpAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActionDiscoveryPresenter
  extends MainContentPresenter
  implements VelvetEventBus.Observer, Consumer<ActionV2Protos.HelpAction>
{
  private int mCardColumn;
  private final Context mContext;
  private final CoreSearchServices mCoreSearchServices;
  private ExampleContactHelper mExampleContactHelper;
  private boolean mHelpFeaturesAdded;
  private boolean mIntroductionCardAdded;
  private boolean mMarginApplied;
  private final ScheduledSingleThreadedExecutor mUiExecutor;
  
  public ActionDiscoveryPresenter(MainContentView paramMainContentView, Context paramContext, CoreSearchServices paramCoreSearchServices, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    super("actiondiscovery", paramMainContentView);
    this.mContext = paramContext;
    this.mCoreSearchServices = paramCoreSearchServices;
    this.mUiExecutor = paramScheduledSingleThreadedExecutor;
  }
  
  private ExampleContactHelper getExampleContactHelper()
  {
    if (this.mExampleContactHelper == null) {
      this.mExampleContactHelper = new ExampleContactHelper(new ContactRetriever(this.mContext.getContentResolver()), true);
    }
    return this.mExampleContactHelper;
  }
  
  private boolean isAttachedAndCanShowCards()
  {
    return (isAttached()) && (!getEventBus().getDiscoveryState().shouldHideAll());
  }
  
  private void onDiscoveryStateChanged()
  {
    DiscoveryState localDiscoveryState = getEventBus().getDiscoveryState();
    if (localDiscoveryState.shouldHideAll())
    {
      postResetScroll();
      postRemoveAll();
      return;
    }
    if (localDiscoveryState.shouldShowIntroCard())
    {
      postResetScroll();
      showHelpCards(false);
      return;
    }
    if (localDiscoveryState.shouldShowAll())
    {
      showHelpCards(true);
      return;
    }
    Log.w("Velvet.ActionDiscoveryPresenter", "Unhandled unknown discovery state!");
  }
  
  private void pauseRecognition()
  {
    getVelvetPresenter().getSearchPlateUi().showRecognitionState(7);
    getVelvetPresenter().getSearchController().getVoiceSearchController().cancel(true, true);
  }
  
  private void postRemoveAll()
  {
    this.mHelpFeaturesAdded = false;
    this.mIntroductionCardAdded = false;
    postRemoveAllViews();
  }
  
  private void postRemoveCollapsibleMargin()
  {
    post(new MainContentPresenter.Transaction()
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        paramAnonymousMainContentUi.setMainContentBackCollapsibleMargin(0);
      }
    });
    this.mMarginApplied = false;
  }
  
  private void showHelpCards(boolean paramBoolean)
  {
    ExtraPreconditions.checkMainThread();
    ActionDiscoveryData localActionDiscoveryData = this.mCoreSearchServices.getActionDiscoveryData();
    if (localActionDiscoveryData.haveNow())
    {
      ActionV2Protos.HelpAction localHelpAction = localActionDiscoveryData.getNow();
      if (localHelpAction != null)
      {
        boolean bool1 = this.mIntroductionCardAdded;
        boolean bool2 = false;
        if (!bool1)
        {
          this.mIntroductionCardAdded = true;
          bool2 = true;
        }
        boolean bool3 = false;
        if (paramBoolean)
        {
          boolean bool4 = this.mHelpFeaturesAdded;
          bool3 = false;
          if (!bool4)
          {
            this.mHelpFeaturesAdded = true;
            bool3 = true;
          }
        }
        if ((bool2) || (bool3)) {
          executeHelpActionTask(localHelpAction, bool2, bool3);
        }
      }
      return;
    }
    localActionDiscoveryData.getLater(this);
  }
  
  public boolean consume(ActionV2Protos.HelpAction paramHelpAction)
  {
    if (paramHelpAction != null) {
      this.mUiExecutor.execute(new Runnable()
      {
        public void run()
        {
          if (ActionDiscoveryPresenter.this.isAttached()) {
            ActionDiscoveryPresenter.this.onDiscoveryStateChanged();
          }
        }
      });
    }
    return true;
  }
  
  View createCard(final SimpleHelpController paramSimpleHelpController)
  {
    SimpleHelpCard localSimpleHelpCard = new SimpleHelpCard(this.mContext);
    localSimpleHelpCard.setOnRefreshExample(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramSimpleHelpController.showNextExample();
      }
    });
    paramSimpleHelpController.attachUi(localSimpleHelpCard);
    return localSimpleHelpCard;
  }
  
  SimpleHelpController createController(HelpAction paramHelpAction)
  {
    return new SimpleHelpController(paramHelpAction, DateFormat.is24HourFormat(this.mContext));
  }
  
  void executeHelpActionTask(ActionV2Protos.HelpAction paramHelpAction, boolean paramBoolean1, boolean paramBoolean2)
  {
    new ProcessHelpActionTask(paramHelpAction, paramBoolean1, paramBoolean2).execute(new Void[0]);
  }
  
  protected void onPostAttach(Bundle paramBundle)
  {
    postSetMatchPortraitMode(getVelvetPresenter().getConfig().shouldMatchPortraitWidthInLandscape());
    if (getVelvetPresenter().getConfig().shouldCenterResultCardInLandscape()) {}
    for (int i = -1;; i = 0)
    {
      this.mCardColumn = i;
      getEventBus().addObserver(this);
      return;
    }
  }
  
  protected void onPreDetach()
  {
    getEventBus().removeObserver(this);
    postSetMatchPortraitMode(false);
    postRemoveAll();
    if (this.mMarginApplied) {
      postRemoveCollapsibleMargin();
    }
    postResetScroll();
  }
  
  public void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    if (paramEvent.hasDiscoveryChanged()) {
      onDiscoveryStateChanged();
    }
  }
  
  public void onViewScrolled(boolean paramBoolean)
  {
    if (getMainContentBackCollapsibleMarginRatio() < 1.0F)
    {
      pauseRecognition();
      getEventBus().getDiscoveryState().onPeekCardDragged();
    }
    if ((this.mMarginApplied) && (!paramBoolean))
    {
      pauseRecognition();
      postRemoveCollapsibleMargin();
      getEventBus().getDiscoveryState().onPeekCardDragged();
    }
  }
  
  protected void setLastVisibleCard(View paramView, MainContentUi paramMainContentUi)
  {
    DiscoveryState localDiscoveryState = getEventBus().getDiscoveryState();
    if ((paramView != null) && (!this.mMarginApplied) && (localDiscoveryState.shouldPeek()))
    {
      localDiscoveryState.onDiscoveryCardsPeeked();
      paramMainContentUi.setMainContentBackCollapsibleMargin(Math.max(1, getScrollViewControl().getViewportHeight() - paramMainContentUi.getCardsView().getPaddingTop() - getDimensionPixelSize(2131689718) - getDimensionPixelSize(2131689608)));
      this.mMarginApplied = true;
    }
  }
  
  private class AddCardsTransaction
    extends MainContentPresenter.Transaction
  {
    private List<View> mCards;
    private Iterator<SimpleHelpController> mControllers;
    
    public AddCardsTransaction()
    {
      super();
      Iterable localIterable;
      if (localIterable.size() > 0) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkArgument(bool);
        this.mControllers = Lists.newArrayList(localIterable).iterator();
        this.mCards = Lists.newArrayListWithCapacity(localIterable.size());
        return;
      }
    }
    
    public void commit(MainContentUi paramMainContentUi)
    {
      if (!ActionDiscoveryPresenter.this.isAttachedAndCanShowCards()) {
        return;
      }
      SuggestionGridLayout localSuggestionGridLayout = paramMainContentUi.getCardsView();
      Iterator localIterator = this.mCards.iterator();
      while (localIterator.hasNext()) {
        localSuggestionGridLayout.addViewToColumn((View)localIterator.next(), ActionDiscoveryPresenter.this.mCardColumn);
      }
      ActionDiscoveryPresenter.this.setLastVisibleCard((View)this.mCards.get(-1 + this.mCards.size()), paramMainContentUi);
    }
    
    public boolean prepare()
    {
      if (!ActionDiscoveryPresenter.this.isAttachedAndCanShowCards()) {
        return true;
      }
      while (this.mControllers.hasNext()) {
        this.mCards.add(ActionDiscoveryPresenter.this.createCard((SimpleHelpController)this.mControllers.next()));
      }
      if (!this.mControllers.hasNext()) {}
      for (boolean bool = true;; bool = false) {
        return bool;
      }
    }
  }
  
  class ProcessHelpActionTask
    extends AsyncTask<Void, Void, List<HelpAction>>
  {
    private final boolean mFeatureCards;
    private final ActionV2Protos.HelpAction mHelpAction;
    private final boolean mIntroCard;
    
    ProcessHelpActionTask(ActionV2Protos.HelpAction paramHelpAction, boolean paramBoolean1, boolean paramBoolean2)
    {
      this.mIntroCard = paramBoolean1;
      this.mFeatureCards = paramBoolean2;
      this.mHelpAction = paramHelpAction;
    }
    
    protected List<HelpAction> doInBackground(Void... paramVarArgs)
    {
      ArrayList localArrayList = Lists.newArrayList();
      if (this.mIntroCard) {
        HelpActionUtils.processIntroduction(this.mHelpAction, localArrayList);
      }
      if (this.mFeatureCards) {
        HelpActionUtils.processFeatures(this.mHelpAction, ActionDiscoveryPresenter.this.getExampleContactHelper(), ActionDiscoveryPresenter.this.mCoreSearchServices.getDeviceCapabilityManager(), VelvetApplication.getVersionCode(), localArrayList);
      }
      return localArrayList;
    }
    
    protected void onPostExecute(List<HelpAction> paramList)
    {
      if ((!paramList.isEmpty()) && (ActionDiscoveryPresenter.this.isAttachedAndCanShowCards()))
      {
        ArrayList localArrayList = Lists.newArrayListWithCapacity(paramList.size());
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          HelpAction localHelpAction = (HelpAction)localIterator.next();
          localArrayList.add(ActionDiscoveryPresenter.this.createController(localHelpAction));
        }
        ActionDiscoveryPresenter.this.post(new ActionDiscoveryPresenter.AddCardsTransaction(ActionDiscoveryPresenter.this, localArrayList));
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.ActionDiscoveryPresenter
 * JD-Core Version:    0.7.0.1
 */