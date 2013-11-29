package com.google.android.search.core.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Map;
import java.util.Set;

public abstract interface SharedPreferencesExt
  extends SharedPreferences
{
  public abstract void allowWrites();
  
  public abstract void delayWrites();
  
  public abstract Editor edit();
  
  public abstract Map<String, ?> getAllByKeyPrefix(String paramString);
  
  public abstract byte[] getBytes(String paramString, byte[] paramArrayOfByte);
  
  public static abstract interface Editor
    extends SharedPreferences.Editor
  {
    public abstract Editor clear();
    
    public abstract Editor putBoolean(String paramString, boolean paramBoolean);
    
    public abstract Editor putBytes(String paramString, byte[] paramArrayOfByte);
    
    public abstract Editor putFloat(String paramString, float paramFloat);
    
    public abstract Editor putInt(String paramString, int paramInt);
    
    public abstract Editor putLong(String paramString, long paramLong);
    
    public abstract Editor putString(String paramString1, String paramString2);
    
    public abstract Editor putStringSet(String paramString, Set<String> paramSet);
    
    public abstract Editor remove(String paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.SharedPreferencesExt
 * JD-Core Version:    0.7.0.1
 */