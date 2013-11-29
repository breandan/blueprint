package com.android.launcher3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class FolderEditText
  extends EditText
{
  private Folder mFolder;
  
  public FolderEditText(Context paramContext)
  {
    super(paramContext);
  }
  
  public FolderEditText(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public FolderEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public boolean onKeyPreIme(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getKeyCode() == 4) {
      this.mFolder.doneEditingFolderName(true);
    }
    return super.onKeyPreIme(paramInt, paramKeyEvent);
  }
  
  public void setFolder(Folder paramFolder)
  {
    this.mFolder = paramFolder;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.FolderEditText
 * JD-Core Version:    0.7.0.1
 */