package com.google.android.velvet.presenter;

import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.core.state.VelvetEventBus.Observer;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.VoiceCorrectionSpan;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.ui.MainContentView;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.speech.logs.VoicesearchClientLogProto.GwsCorrectionData;
import java.util.ArrayList;
import java.util.List;

public class VoiceCorrectionPresenter
  extends MainContentPresenter
  implements VelvetEventBus.Observer
{
  private Query mCommittedQuery;
  private Query mLastSeenQuery;
  private final View.OnClickListener mOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (VoiceCorrectionPresenter.this.mCommittedQuery == null) {
        return;
      }
      String str = ((TextView)paramAnonymousView.findViewById(2131296681)).getText().toString();
      VoiceCorrectionPresenter.this.logClick(str);
      QueryState localQueryState = VoiceCorrectionPresenter.this.getEventBus().getQueryState();
      localQueryState.switchQuery(VoiceCorrectionPresenter.this.mCommittedQuery, localQueryState.get().withReplacedSelection(str).withoutVoiceCorrection());
    }
  };
  private List<String> mSuggestions;
  private final UpdateSuggestionsTransaction mUpdateSuggestionsTransaction = new UpdateSuggestionsTransaction(null);
  private View mView;
  
  public VoiceCorrectionPresenter(MainContentView paramMainContentView)
  {
    super("voicecorrection", paramMainContentView);
  }
  
  private void logClick(String paramString)
  {
    Query localQuery = getEventBus().getQueryState().get();
    int i = localQuery.getSelectionStart();
    int j = localQuery.getSelectionEnd();
    VoicesearchClientLogProto.GwsCorrectionData localGwsCorrectionData = new VoicesearchClientLogProto.GwsCorrectionData();
    localGwsCorrectionData.setStart(i);
    localGwsCorrectionData.setLength(j - i);
    localGwsCorrectionData.setOldText(localQuery.getQueryString().substring(i, j));
    localGwsCorrectionData.setNewText(paramString);
    EventLogger.recordClientEvent(138, localGwsCorrectionData);
  }
  
  private void updateSuggestions(List<String> paramList)
  {
    this.mSuggestions = paramList;
    post(this.mUpdateSuggestionsTransaction);
  }
  
  protected void onPostAttach(Bundle paramBundle)
  {
    this.mView = getFactory().createVoiceCorrectionView(this, getCardContainer());
    this.mView.setVisibility(4);
    View[] arrayOfView = new View[1];
    arrayOfView[0] = this.mView;
    postAddViews(arrayOfView);
    this.mCommittedQuery = getEventBus().getQueryState().getCommittedQuery();
    getEventBus().addObserver(this);
  }
  
  protected void onPreDetach()
  {
    getEventBus().removeObserver(this);
    this.mCommittedQuery = null;
    postRemoveAllViews();
  }
  
  public void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    if (!paramEvent.hasQueryChanged()) {}
    Query localQuery;
    do
    {
      return;
      localQuery = getEventBus().getQueryState().get();
    } while (localQuery == this.mLastSeenQuery);
    this.mLastSeenQuery = localQuery;
    ArrayList localArrayList = new ArrayList();
    if (!(localQuery.getQueryChars() instanceof Spanned))
    {
      updateSuggestions(localArrayList);
      return;
    }
    int i = localQuery.getSelectionStart();
    int j = localQuery.getSelectionEnd();
    Spanned localSpanned = (Spanned)localQuery.getQueryChars();
    for (VoiceCorrectionSpan localVoiceCorrectionSpan : (VoiceCorrectionSpan[])localSpanned.getSpans(i, j, VoiceCorrectionSpan.class)) {
      if ((localSpanned.getSpanStart(localVoiceCorrectionSpan) == i) && (localSpanned.getSpanEnd(localVoiceCorrectionSpan) == j)) {
        localArrayList.addAll(localVoiceCorrectionSpan.getCorrection());
      }
    }
    updateSuggestions(localArrayList);
  }
  
  private class UpdateSuggestionsTransaction
    extends MainContentPresenter.Transaction
  {
    private UpdateSuggestionsTransaction() {}
    
    public void commit(MainContentUi paramMainContentUi)
    {
      if (!VoiceCorrectionPresenter.this.isAttached()) {
        return;
      }
      int i = Math.min(3, VoiceCorrectionPresenter.this.mSuggestions.size());
      if (i == 0)
      {
        VoiceCorrectionPresenter.this.mView.setVisibility(4);
        return;
      }
      VoiceCorrectionPresenter.this.mView.setVisibility(0);
      LinearLayout localLinearLayout = (LinearLayout)VoiceCorrectionPresenter.this.mView;
      int j = localLinearLayout.getChildCount();
      for (int k = 0; k < Math.min(i, j); k++)
      {
        View localView2 = localLinearLayout.getChildAt(k);
        localView2.setVisibility(0);
        ((TextView)localView2.findViewById(2131296681)).setText((CharSequence)VoiceCorrectionPresenter.this.mSuggestions.get(k));
      }
      for (int m = i - (i - j); m < i; m++)
      {
        View localView1 = VoiceCorrectionPresenter.this.getFactory().createVoiceCorrectionSuggestionView(VoiceCorrectionPresenter.this);
        ((TextView)localView1.findViewById(2131296681)).setText((CharSequence)VoiceCorrectionPresenter.this.mSuggestions.get(m));
        localView1.setOnClickListener(VoiceCorrectionPresenter.this.mOnClickListener);
        localLinearLayout.addView(localView1);
      }
      for (int n = i; n < j; n++) {
        localLinearLayout.getChildAt(n).setVisibility(8);
      }
      localLinearLayout.getChildAt(0).findViewById(2131296482).setVisibility(4);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.VoiceCorrectionPresenter
 * JD-Core Version:    0.7.0.1
 */