package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.client.TvRecognitionManager;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.TvRecognitionCard;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.Tag;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.TvRecognitionEntry;
import java.util.Observable;
import java.util.Observer;

public class TvRecognitionEntryAdapter
  extends BaseEntryAdapter
{
  private static final String TAG = Tag.getTag(TvRecognitionEntryAdapter.class);
  private final String mDeviceName;
  
  public TvRecognitionEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mDeviceName = paramEntry.getTvRecognitionEntry().getNetworkDeviceName();
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    TvRecognitionCard localTvRecognitionCard = (TvRecognitionCard)paramLayoutInflater.inflate(2130968903, null);
    localTvRecognitionCard.setDeviceName(this.mDeviceName);
    final TvRecognitionManager localTvRecognitionManager = paramPredictiveCardContainer.getTvRecognitionManager();
    localTvRecognitionCard.setOnDetectClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 106)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        if (localTvRecognitionManager == null)
        {
          Toast.makeText(paramContext, "not supported", 0).show();
          return;
        }
        localTvRecognitionManager.startRecognition();
      }
    });
    if (localTvRecognitionManager == null)
    {
      localTvRecognitionCard.reset();
      return localTvRecognitionCard;
    }
    localTvRecognitionCard.setSpeechLevelSource(localTvRecognitionManager.getSpeechLevelSource());
    localTvRecognitionCard.addOnAttachStateChangeListener(new CardUpdater(localTvRecognitionCard, localTvRecognitionManager));
    updateView(localTvRecognitionCard, localTvRecognitionManager);
    return localTvRecognitionCard;
  }
  
  void updateView(TvRecognitionCard paramTvRecognitionCard, TvRecognitionManager paramTvRecognitionManager)
  {
    paramTvRecognitionCard.setSpeechLevelSource(paramTvRecognitionManager.getSpeechLevelSource());
    int i = paramTvRecognitionManager.getRecognitionState();
    switch (i)
    {
    default: 
      Log.e(TAG, "Unknown state: " + i);
      return;
    case 1: 
      paramTvRecognitionCard.reset();
      return;
    case 2: 
      paramTvRecognitionCard.showDetecting();
      return;
    }
    paramTvRecognitionCard.showError();
  }
  
  private class CardUpdater
    implements View.OnAttachStateChangeListener, Observer
  {
    private final TvRecognitionCard mCard;
    private final TvRecognitionManager mRecognitionManager;
    
    public CardUpdater(TvRecognitionCard paramTvRecognitionCard, TvRecognitionManager paramTvRecognitionManager)
    {
      this.mCard = paramTvRecognitionCard;
      this.mRecognitionManager = paramTvRecognitionManager;
    }
    
    public void onViewAttachedToWindow(View paramView)
    {
      this.mRecognitionManager.addObserver(this);
      TvRecognitionEntryAdapter.this.updateView(this.mCard, this.mRecognitionManager);
    }
    
    public void onViewDetachedFromWindow(View paramView)
    {
      this.mRecognitionManager.deleteObserver(this);
    }
    
    public void update(Observable paramObservable, Object paramObject)
    {
      TvRecognitionEntryAdapter.this.updateView(this.mCard, this.mRecognitionManager);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.TvRecognitionEntryAdapter
 * JD-Core Version:    0.7.0.1
 */