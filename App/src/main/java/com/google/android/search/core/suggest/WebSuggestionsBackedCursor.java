package com.google.android.search.core.suggest;

import android.database.AbstractCursor;
import android.database.CursorIndexOutOfBoundsException;
import com.google.android.search.shared.api.Suggestion;

public class WebSuggestionsBackedCursor
  extends AbstractCursor
{
  private static final String[] COLUMNS = { "_id", "suggest_text_1", "suggest_text_2", "suggest_text_2_url", "suggest_icon_1", "suggest_icon_2", "suggest_intent_action", "suggest_intent_data", "suggest_intent_extra_data", "suggest_intent_query", "suggest_format", "suggest_shortcut_id", "suggest_spinner_while_refreshing" };
  private final SuggestionList mSuggestions;
  
  public WebSuggestionsBackedCursor(SuggestionList paramSuggestionList)
  {
    this.mSuggestions = paramSuggestionList;
  }
  
  private String asString(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    return paramObject.toString();
  }
  
  private Suggestion get()
  {
    return this.mSuggestions.get(getPosition());
  }
  
  public String[] getColumnNames()
  {
    return COLUMNS;
  }
  
  public int getCount()
  {
    return this.mSuggestions.getCount();
  }
  
  public double getDouble(int paramInt)
  {
    try
    {
      double d = Double.valueOf(getString(paramInt)).doubleValue();
      return d;
    }
    catch (NumberFormatException localNumberFormatException) {}
    return 0.0D;
  }
  
  public float getFloat(int paramInt)
  {
    try
    {
      float f = Float.valueOf(getString(paramInt)).floatValue();
      return f;
    }
    catch (NumberFormatException localNumberFormatException) {}
    return 0.0F;
  }
  
  public int getInt(int paramInt)
  {
    if (paramInt == 0) {
      return getPosition();
    }
    try
    {
      int i = Integer.valueOf(getString(paramInt)).intValue();
      return i;
    }
    catch (NumberFormatException localNumberFormatException) {}
    return 0;
  }
  
  public long getLong(int paramInt)
  {
    try
    {
      long l = Long.valueOf(getString(paramInt)).longValue();
      return l;
    }
    catch (NumberFormatException localNumberFormatException) {}
    return 0L;
  }
  
  public short getShort(int paramInt)
  {
    try
    {
      short s = Short.valueOf(getString(paramInt)).shortValue();
      return s;
    }
    catch (NumberFormatException localNumberFormatException) {}
    return 0;
  }
  
  public String getString(int paramInt)
  {
    Suggestion localSuggestion = get();
    if (paramInt < COLUMNS.length)
    {
      String str = null;
      switch (paramInt)
      {
      default: 
        throw new CursorIndexOutOfBoundsException("Requested column " + paramInt + " of " + COLUMNS.length);
      case 0: 
        str = String.valueOf(getPosition());
      case 5: 
      case 10: 
        return str;
      case 1: 
        return asString(localSuggestion.getSuggestionText1());
      case 2: 
        return asString(localSuggestion.getSuggestionText2());
      case 3: 
        return localSuggestion.getSuggestionText2Url();
      case 4: 
        return localSuggestion.getSuggestionIcon1();
      case 6: 
        return localSuggestion.getSuggestionIntentAction();
      case 7: 
        return localSuggestion.getSuggestionIntentDataString();
      case 8: 
        return localSuggestion.getSuggestionIntentExtraData();
      case 9: 
        return localSuggestion.getSuggestionQuery();
      case 11: 
        return "_-1";
      }
      return String.valueOf(false);
    }
    throw new IndexOutOfBoundsException();
  }
  
  public boolean isNull(int paramInt)
  {
    return getString(paramInt) == null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.WebSuggestionsBackedCursor
 * JD-Core Version:    0.7.0.1
 */