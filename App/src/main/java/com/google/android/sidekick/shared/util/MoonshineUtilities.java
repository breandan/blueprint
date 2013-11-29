package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.widget.Button;
import com.google.android.shared.util.BidiUtils;
import com.google.geo.sidekick.Sidekick.GmailReference;
import java.util.List;
import javax.annotation.Nullable;

public class MoonshineUtilities
{
  @Nullable
  public static Sidekick.GmailReference getEffectiveGmailReference(List<Sidekick.GmailReference> paramList)
  {
    Sidekick.GmailReference localGmailReference;
    if ((paramList == null) || (paramList.isEmpty())) {
      localGmailReference = null;
    }
    do
    {
      return localGmailReference;
      localGmailReference = (Sidekick.GmailReference)paramList.get(-1 + paramList.size());
    } while ((localGmailReference.getEmailUrl() != null) && (localGmailReference.getSenderEmailAddress() != null));
    return null;
  }
  
  @Nullable
  public static Sidekick.GmailReference getEffectiveGmailReferenceAndSetText(Context paramContext, Button paramButton, List<Sidekick.GmailReference> paramList)
  {
    Sidekick.GmailReference localGmailReference = getEffectiveGmailReference(paramList);
    if (localGmailReference != null)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = BidiUtils.unicodeWrapLtr(localGmailReference.getSenderEmailAddress());
      paramButton.setText(paramContext.getString(2131362630, arrayOfObject));
    }
    return localGmailReference;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.MoonshineUtilities
 * JD-Core Version:    0.7.0.1
 */