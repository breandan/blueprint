package com.google.android.velvet.presenter;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.shared.api.Query;
import com.google.android.velvet.Corpora;
import com.google.android.velvet.Corpus;
import java.util.Iterator;

public class FooterPresenter
  extends VelvetFragmentPresenter
{
  private final Corpora mCorpora;
  private final DataSetObserver mCorporaObserver = new DataSetObserver()
  {
    public void onChanged()
    {
      FooterPresenter.this.updateCorpora();
    }
  };
  private final FooterUi mFooterUi;
  private String mSelectedCorpusId;
  
  public FooterPresenter(Corpora paramCorpora, FooterUi paramFooterUi)
  {
    super("footer");
    this.mFooterUi = paramFooterUi;
    this.mCorpora = paramCorpora;
    this.mFooterUi.setPresenter(this);
  }
  
  private void updateCorpora()
  {
    if (this.mFooterUi.isCorpusBarLoaded())
    {
      updateCorpora(this.mCorpora.getWebCorpus());
      updateCorpora(this.mCorpora.getSummonsCorpus());
    }
  }
  
  private void updateCorpora(Corpus paramCorpus)
  {
    if (paramCorpus != null)
    {
      this.mFooterUi.removeCorpusSelectors(paramCorpus);
      this.mFooterUi.addCorpusSelector(paramCorpus);
      Iterator localIterator = this.mCorpora.getSubCorpora(paramCorpus).iterator();
      while (localIterator.hasNext())
      {
        Corpus localCorpus = (Corpus)localIterator.next();
        if (localCorpus.isEnabled()) {
          this.mFooterUi.addCorpusSelector(localCorpus);
        }
      }
    }
  }
  
  public void onCorpusBarLoaded()
  {
    updateCorpora();
    if ((this.mSelectedCorpusId != null) && (this.mCorpora.areWebCorporaLoaded())) {
      this.mFooterUi.setSelectedCorpus(this.mCorpora.getCorpus(this.mSelectedCorpusId));
    }
  }
  
  public void onCorpusClicked(Corpus paramCorpus)
  {
    this.mFooterUi.setSelectedCorpus(paramCorpus);
    QueryState localQueryState = getEventBus().getQueryState();
    Query localQuery = localQueryState.get().withCorpus(paramCorpus.getIdentifier());
    if (localQuery != localQueryState.get()) {
      localQueryState.commit(localQuery);
    }
  }
  
  public void onMenuButtonClick(View paramView)
  {
    getVelvetPresenter().onMenuButtonClick(paramView);
  }
  
  protected void onPostAttach(Bundle paramBundle)
  {
    this.mCorpora.registerObserver(this.mCorporaObserver);
  }
  
  protected void onPreDetach()
  {
    this.mCorpora.unregisterObserver(this.mCorporaObserver);
  }
  
  public void onRemindersButtonPressed()
  {
    getVelvetPresenter().onRemindersButtonPressed();
  }
  
  public void onTrainingButtonPressed()
  {
    getVelvetPresenter().onTrainingButtonPressed();
  }
  
  public void updateUi(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
  {
    this.mSelectedCorpusId = getEventBus().getQueryState().get().getCorpusId();
    this.mFooterUi.setShowCorpusBar(paramBoolean1);
    if (this.mFooterUi.isCorpusBarLoaded())
    {
      if (!paramBoolean1) {
        break label94;
      }
      if (this.mCorpora.areWebCorporaLoaded()) {
        this.mFooterUi.setSelectedCorpus(this.mCorpora.getCorpus(this.mSelectedCorpusId));
      }
    }
    for (;;)
    {
      this.mFooterUi.setShowTgFooterButton(paramBoolean2);
      this.mFooterUi.setRemindersButtonVisibility(paramInt);
      return;
      label94:
      this.mFooterUi.resetShowMoreCorpora();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.FooterPresenter
 * JD-Core Version:    0.7.0.1
 */