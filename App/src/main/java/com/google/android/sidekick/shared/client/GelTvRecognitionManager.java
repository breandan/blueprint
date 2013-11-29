package com.google.android.sidekick.shared.client;

import android.util.Log;
import com.google.android.search.shared.api.ExternalGelSearch;
import com.google.android.search.shared.api.ExternalGelSearch.Callback;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Supplier;
import javax.annotation.Nullable;

public class GelTvRecognitionManager
  extends TvRecognitionManager
  implements ExternalGelSearch.Callback
{
  private static final String TAG = Tag.getTag(GelTvRecognitionManager.class);
  private final Supplier<ExternalGelSearch> mGelSearchSupplier;
  @Nullable
  private SpeechLevelSource mSpeechLevelSource;
  
  public GelTvRecognitionManager(Supplier<ExternalGelSearch> paramSupplier)
  {
    this.mGelSearchSupplier = paramSupplier;
  }
  
  @Nullable
  public SpeechLevelSource getSpeechLevelSource()
  {
    return this.mSpeechLevelSource;
  }
  
  public void onStatusChanged(int paramInt, SpeechLevelSource paramSpeechLevelSource)
  {
    this.mSpeechLevelSource = paramSpeechLevelSource;
    switch (paramInt)
    {
    default: 
      return;
    case 0: 
      setRecognitionState(1);
      return;
    case 1: 
      setRecognitionState(2);
      return;
    }
    setRecognitionState(3);
  }
  
  public void startRecognition()
  {
    ExternalGelSearch localExternalGelSearch = (ExternalGelSearch)this.mGelSearchSupplier.get();
    if (localExternalGelSearch == null)
    {
      Log.e(TAG, "Cannot start TV content recognition: search is not available.");
      return;
    }
    localExternalGelSearch.commit(Query.EMPTY.predictiveTvSearch(), this);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.GelTvRecognitionManager
 * JD-Core Version:    0.7.0.1
 */