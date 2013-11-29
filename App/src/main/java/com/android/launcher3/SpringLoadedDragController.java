package com.android.launcher3;

public class SpringLoadedDragController
  implements OnAlarmListener
{
  final long ENTER_SPRING_LOAD_CANCEL_HOVER_TIME = 950L;
  final long ENTER_SPRING_LOAD_HOVER_TIME = 500L;
  final long EXIT_SPRING_LOAD_HOVER_TIME = 200L;
  Alarm mAlarm;
  private Launcher mLauncher;
  private CellLayout mScreen;
  
  public SpringLoadedDragController(Launcher paramLauncher)
  {
    this.mLauncher = paramLauncher;
    this.mAlarm = new Alarm();
    this.mAlarm.setOnAlarmListener(this);
  }
  
  public void cancel()
  {
    this.mAlarm.cancelAlarm();
  }
  
  public void onAlarm(Alarm paramAlarm)
  {
    if (this.mScreen != null)
    {
      Workspace localWorkspace = this.mLauncher.getWorkspace();
      int i = localWorkspace.indexOfChild(this.mScreen);
      if (i != localWorkspace.getCurrentPage()) {
        localWorkspace.snapToPage(i);
      }
      return;
    }
    this.mLauncher.getDragController().cancelDrag();
  }
  
  public void setAlarm(CellLayout paramCellLayout)
  {
    this.mAlarm.cancelAlarm();
    Alarm localAlarm = this.mAlarm;
    if (paramCellLayout == null) {}
    for (long l = 950L;; l = 500L)
    {
      localAlarm.setAlarm(l);
      this.mScreen = paramCellLayout;
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.SpringLoadedDragController
 * JD-Core Version:    0.7.0.1
 */