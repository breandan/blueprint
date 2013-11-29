package com.google.android.velvet.ui;

public final class SavedStateTracker
{
  private boolean mStateSaved;
  
  private void noteStateNotSaved()
  {
    this.mStateSaved = false;
  }
  
  public boolean haveSavedState()
  {
    return this.mStateSaved;
  }
  
  public void onActivityResult()
  {
    noteStateNotSaved();
  }
  
  public void onCreate()
  {
    noteStateNotSaved();
  }
  
  public void onNewIntent()
  {
    noteStateNotSaved();
  }
  
  public void onRestart()
  {
    noteStateNotSaved();
  }
  
  public void onResume()
  {
    noteStateNotSaved();
  }
  
  public void onSaveInstanceState()
  {
    this.mStateSaved = true;
  }
  
  public void onStart()
  {
    noteStateNotSaved();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.ui.SavedStateTracker
 * JD-Core Version:    0.7.0.1
 */