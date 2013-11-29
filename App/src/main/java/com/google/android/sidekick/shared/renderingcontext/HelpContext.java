package com.google.android.sidekick.shared.renderingcontext;

import android.net.Uri;
import android.os.Bundle;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import javax.annotation.Nullable;

public class HelpContext
{
  public static final String BUNDLE_KEY = HelpContext.class.getName();
  private final Bundle mBundle;
  
  private HelpContext(Bundle paramBundle)
  {
    this.mBundle = paramBundle;
  }
  
  @Nullable
  public static HelpContext fromCardContainer(PredictiveCardContainer paramPredictiveCardContainer)
  {
    return new HelpContext((Bundle)paramPredictiveCardContainer.getCardRenderingContext().getSpecificRenderingContext(BUNDLE_KEY));
  }
  
  public Uri getHelpUri(String paramString)
  {
    return (Uri)this.mBundle.getParcelable(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.renderingcontext.HelpContext
 * JD-Core Version:    0.7.0.1
 */