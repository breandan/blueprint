package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityManager;

public class AccessibilityManagerCompat
{
  private static final AccessibilityManagerVersionImpl IMPL = new AccessibilityManagerStubImpl();
  
  static
  {
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new AccessibilityManagerIcsImpl();
      return;
    }
  }
  
  public static boolean isTouchExplorationEnabled(AccessibilityManager paramAccessibilityManager)
  {
    return IMPL.isTouchExplorationEnabled(paramAccessibilityManager);
  }
  
  static class AccessibilityManagerIcsImpl
    extends AccessibilityManagerCompat.AccessibilityManagerStubImpl
  {
    public boolean isTouchExplorationEnabled(AccessibilityManager paramAccessibilityManager)
    {
      return AccessibilityManagerCompatIcs.isTouchExplorationEnabled(paramAccessibilityManager);
    }
  }
  
  static class AccessibilityManagerStubImpl
    implements AccessibilityManagerCompat.AccessibilityManagerVersionImpl
  {
    public boolean isTouchExplorationEnabled(AccessibilityManager paramAccessibilityManager)
    {
      return false;
    }
  }
  
  static abstract interface AccessibilityManagerVersionImpl
  {
    public abstract boolean isTouchExplorationEnabled(AccessibilityManager paramAccessibilityManager);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityManagerCompat
 * JD-Core Version:    0.7.0.1
 */