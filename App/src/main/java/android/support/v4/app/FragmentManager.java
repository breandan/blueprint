package android.support.v4.app;

public abstract class FragmentManager
{
  public abstract FragmentTransaction beginTransaction();
  
  public abstract boolean executePendingTransactions();
  
  public abstract Fragment findFragmentByTag(String paramString);
  
  public static abstract interface OnBackStackChangedListener
  {
    public abstract void onBackStackChanged();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.app.FragmentManager
 * JD-Core Version:    0.7.0.1
 */