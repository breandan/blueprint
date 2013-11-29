package com.google.android.search.core.service;

import android.content.Context;
import android.content.Intent;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.api.SuggestionLogInfo;
import com.google.android.search.shared.service.SearchServiceUiCallback;
import java.util.List;

public class DefaultServiceUiCallback
  implements SearchServiceUiCallback
{
  private final Context mContext;
  
  public DefaultServiceUiCallback(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  public void hideSuggestions() {}
  
  public void launchIntent(Intent paramIntent)
  {
    this.mContext.startActivity(paramIntent);
  }
  
  public void onRemoveSuggestionFromHistoryFailed() {}
  
  public boolean resolveIntent(Intent paramIntent)
  {
    return false;
  }
  
  public void setExternalFlags(int paramInt, String paramString) {}
  
  public void setFinalRecognizedText(String paramString) {}
  
  public void setQuery(Query paramQuery) {}
  
  public void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean) {}
  
  public void showErrorMessage(String paramString) {}
  
  public void showRecognitionState(int paramInt) {}
  
  public void showSuggestions(Query paramQuery, List<Suggestion> paramList, boolean paramBoolean, SuggestionLogInfo paramSuggestionLogInfo) {}
  
  public void updateRecognizedText(String paramString1, String paramString2) {}
  
  public void updateSpeechLevel(int paramInt) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.service.DefaultServiceUiCallback
 * JD-Core Version:    0.7.0.1
 */