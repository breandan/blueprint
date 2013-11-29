package com.google.android.search.core;

import com.google.android.search.core.state.QueryState;
import com.google.android.search.shared.api.Query;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.fragments.action.VoiceActionVisitor;

public abstract class SearchError
  implements VoiceAction
{
  public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor)
  {
    return paramVoiceActionVisitor.visit(this);
  }
  
  public boolean canExecute()
  {
    return false;
  }
  
  public int getButtonTextId()
  {
    return 0;
  }
  
  public int getErrorExplanationResId()
  {
    return 0;
  }
  
  public int getErrorImageResId()
  {
    return 0;
  }
  
  public CharSequence getErrorMessage()
  {
    return null;
  }
  
  public int getErrorMessageResId()
  {
    return 0;
  }
  
  public int getErrorTitleResId()
  {
    return 0;
  }
  
  public abstract int getErrorTypeForLogs();
  
  public boolean isAuthError()
  {
    return false;
  }
  
  public boolean isRetriable()
  {
    return true;
  }
  
  public void retry(QueryState paramQueryState, Query paramQuery)
  {
    paramQueryState.retry(paramQuery);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.SearchError
 * JD-Core Version:    0.7.0.1
 */