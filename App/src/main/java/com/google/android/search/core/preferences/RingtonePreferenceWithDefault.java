package com.google.android.search.core.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.util.AttributeSet;

public class RingtonePreferenceWithDefault
  extends RingtonePreference
{
  private boolean mHasCustomDefault;
  
  public RingtonePreferenceWithDefault(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public RingtonePreferenceWithDefault(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public RingtonePreferenceWithDefault(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private void init()
  {
    Uri localUri;
    if ((getShowDefault()) && (!this.mHasCustomDefault))
    {
      localUri = RingtoneManager.getDefaultUri(getRingtoneType());
      if (localUri == null) {
        break label37;
      }
    }
    label37:
    for (String str = localUri.toString();; str = null)
    {
      setDefaultValue(str);
      return;
    }
  }
  
  protected Object onGetDefaultValue(TypedArray paramTypedArray, int paramInt)
  {
    this.mHasCustomDefault = true;
    return super.onGetDefaultValue(paramTypedArray, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.RingtonePreferenceWithDefault
 * JD-Core Version:    0.7.0.1
 */