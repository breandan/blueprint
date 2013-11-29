package com.google.android.sidekick.shared.client;

import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.sidekick.shared.ui.PredictiveCardWrapper;

public abstract interface ScrollableCardView
{
  public abstract ScrollViewControl getScrollViewControl();
  
  public abstract SuggestionGridLayout getSuggestionGridLayout();
  
  public abstract boolean isPredictiveOnlyMode();
  
  public abstract boolean isVisible();
  
  public abstract void notifyCardVisible(PredictiveCardWrapper paramPredictiveCardWrapper);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.ScrollableCardView
 * JD-Core Version:    0.7.0.1
 */