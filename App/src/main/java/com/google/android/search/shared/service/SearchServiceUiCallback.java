package com.google.android.search.shared.service;

import android.content.Intent;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.api.SuggestionLogInfo;
import java.util.List;

public abstract interface SearchServiceUiCallback
{
  public abstract void hideSuggestions();
  
  public abstract void launchIntent(Intent paramIntent);
  
  public abstract void onRemoveSuggestionFromHistoryFailed();
  
  public abstract boolean resolveIntent(Intent paramIntent);
  
  public abstract void setExternalFlags(int paramInt, String paramString);
  
  public abstract void setFinalRecognizedText(String paramString);
  
  public abstract void setQuery(Query paramQuery);
  
  public abstract void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean);
  
  public abstract void showErrorMessage(String paramString);
  
  public abstract void showRecognitionState(int paramInt);
  
  public abstract void showSuggestions(Query paramQuery, List<Suggestion> paramList, boolean paramBoolean, SuggestionLogInfo paramSuggestionLogInfo);
  
  public abstract void updateRecognizedText(String paramString1, String paramString2);
  
  public abstract void updateSpeechLevel(int paramInt);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.service.SearchServiceUiCallback
 * JD-Core Version:    0.7.0.1
 */