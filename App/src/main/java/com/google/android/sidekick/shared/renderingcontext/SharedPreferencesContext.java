package com.google.android.sidekick.shared.renderingcontext;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

public class SharedPreferencesContext
{
  public static final String BUNDLE_KEY = SharedPreferencesContext.class.getName();
  private static final String TAG = Tag.getTag(SharedPreferencesContext.class);
  private final PredictiveCardContainer mCardContainer;
  private final Bundle mPreferencesBundle;
  
  private SharedPreferencesContext(Bundle paramBundle, PredictiveCardContainer paramPredictiveCardContainer)
  {
    this.mPreferencesBundle = paramBundle;
    this.mCardContainer = paramPredictiveCardContainer;
  }
  
  @Nullable
  public static SharedPreferencesContext fromCardContainer(PredictiveCardContainer paramPredictiveCardContainer)
  {
    return new SharedPreferencesContext((Bundle)paramPredictiveCardContainer.getCardRenderingContext().getSpecificRenderingContext(BUNDLE_KEY), paramPredictiveCardContainer);
  }
  
  public static SharedPreferences.Editor updatePreferences(Bundle paramBundle, SharedPreferences.Editor paramEditor)
  {
    Iterator localIterator = paramBundle.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      if (str1.length() < 2)
      {
        Log.w(TAG, "Illegal key: " + str1);
      }
      else
      {
        String str2 = str1.substring(0, 1);
        String str3 = str1.substring(1);
        if ("b".equals(str2)) {
          paramEditor.putBoolean(str3, paramBundle.getBoolean(str1));
        } else if ("s".equals(str2)) {
          paramEditor.putString(str3, paramBundle.getString(str1));
        } else if ("i".equals(str2)) {
          paramEditor.putInt(str3, paramBundle.getInt(str1));
        } else if ("l".equals(str2)) {
          paramEditor.putLong(str3, paramBundle.getLong(str1));
        } else if ("f".equals(str2)) {
          paramEditor.putFloat(str3, paramBundle.getFloat(str1));
        } else if ("r".equals(str2)) {
          paramEditor.remove(str3);
        } else {
          Log.e(TAG, "Unrecognized prefix: " + str2);
        }
      }
    }
    return paramEditor;
  }
  
  public SharedPreferenceContextEditor edit()
  {
    return new SharedPreferenceContextEditor();
  }
  
  public Boolean getBoolean(String paramString)
  {
    Preconditions.checkState(this.mPreferencesBundle.containsKey(paramString));
    return Boolean.valueOf(this.mPreferencesBundle.getBoolean(paramString));
  }
  
  public Integer getInt(String paramString)
  {
    Preconditions.checkState(this.mPreferencesBundle.containsKey(paramString));
    return Integer.valueOf(this.mPreferencesBundle.getInt(paramString));
  }
  
  public class SharedPreferenceContextEditor
  {
    private final Bundle mBundle = new Bundle();
    private final Object mLock = new Object();
    
    public SharedPreferenceContextEditor() {}
    
    public void apply()
    {
      synchronized (this.mLock)
      {
        SharedPreferencesContext.this.mCardContainer.savePreferences(this.mBundle);
        return;
      }
    }
    
    public SharedPreferenceContextEditor putBoolean(String paramString, boolean paramBoolean)
    {
      synchronized (this.mLock)
      {
        this.mBundle.putBoolean("b" + paramString, paramBoolean);
        return this;
      }
    }
    
    public SharedPreferenceContextEditor putInt(String paramString, int paramInt)
    {
      synchronized (this.mLock)
      {
        this.mBundle.putInt("i" + paramString, paramInt);
        return this;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext
 * JD-Core Version:    0.7.0.1
 */