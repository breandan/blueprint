package com.google.android.search.shared.api;

public abstract interface SearchPlateUi
  extends RecognitionUi
{
  public abstract void setExternalFlags(int paramInt, String paramString, boolean paramBoolean);
  
  public abstract void setQuery(Query paramQuery);
  
  public abstract void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean);
  
  public abstract void showErrorMessage(String paramString);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.api.SearchPlateUi
 * JD-Core Version:    0.7.0.1
 */