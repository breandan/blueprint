package com.google.android.search.core;

import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import java.util.Map;

public abstract class JsEventController
{
  private ScheduledSingleThreadedExecutor mUiThreadExecutor;
  
  public JsEventController(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    this.mUiThreadExecutor = paramScheduledSingleThreadedExecutor;
  }
  
  public void dispatchEvents(final Map<String, String> paramMap)
  {
    this.mUiThreadExecutor.execute(new Runnable()
    {
      public void run()
      {
        JsEventController.this.handleEvents(paramMap);
      }
    });
  }
  
  protected abstract void handleEvents(Map<String, String> paramMap);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.JsEventController
 * JD-Core Version:    0.7.0.1
 */