package com.google.android.velvet.presenter;

import com.google.android.velvet.Corpus;

public abstract interface FooterUi
{
  public abstract void addCorpusSelector(Corpus paramCorpus);
  
  public abstract boolean isCorpusBarLoaded();
  
  public abstract void removeCorpusSelectors(Corpus paramCorpus);
  
  public abstract void resetShowMoreCorpora();
  
  public abstract void setPresenter(FooterPresenter paramFooterPresenter);
  
  public abstract void setRemindersButtonVisibility(int paramInt);
  
  public abstract void setSelectedCorpus(Corpus paramCorpus);
  
  public abstract void setShowCorpusBar(boolean paramBoolean);
  
  public abstract void setShowTgFooterButton(boolean paramBoolean);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.FooterUi
 * JD-Core Version:    0.7.0.1
 */