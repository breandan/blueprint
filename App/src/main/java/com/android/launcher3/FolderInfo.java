package com.android.launcher3;

import android.content.ContentValues;
import java.util.ArrayList;

class FolderInfo
  extends ItemInfo
{
  ArrayList<ShortcutInfo> contents = new ArrayList();
  ArrayList<FolderListener> listeners = new ArrayList();
  boolean opened;
  
  FolderInfo()
  {
    this.itemType = 2;
  }
  
  public void add(ShortcutInfo paramShortcutInfo)
  {
    this.contents.add(paramShortcutInfo);
    for (int i = 0; i < this.listeners.size(); i++) {
      ((FolderListener)this.listeners.get(i)).onAdd(paramShortcutInfo);
    }
    itemsChanged();
  }
  
  void addListener(FolderListener paramFolderListener)
  {
    this.listeners.add(paramFolderListener);
  }
  
  void itemsChanged()
  {
    for (int i = 0; i < this.listeners.size(); i++) {
      ((FolderListener)this.listeners.get(i)).onItemsChanged();
    }
  }
  
  void onAddToDatabase(ContentValues paramContentValues)
  {
    super.onAddToDatabase(paramContentValues);
    paramContentValues.put("title", this.title.toString());
  }
  
  public void remove(ShortcutInfo paramShortcutInfo)
  {
    this.contents.remove(paramShortcutInfo);
    for (int i = 0; i < this.listeners.size(); i++) {
      ((FolderListener)this.listeners.get(i)).onRemove(paramShortcutInfo);
    }
    itemsChanged();
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    this.title = paramCharSequence;
    for (int i = 0; i < this.listeners.size(); i++) {
      ((FolderListener)this.listeners.get(i)).onTitleChanged(paramCharSequence);
    }
  }
  
  public String toString()
  {
    return "FolderInfo(id=" + this.id + " type=" + this.itemType + " container=" + this.container + " screen=" + this.screenId + " cellX=" + this.cellX + " cellY=" + this.cellY + " spanX=" + this.spanX + " spanY=" + this.spanY + " dropPos=" + this.dropPos + ")";
  }
  
  void unbind()
  {
    super.unbind();
    this.listeners.clear();
  }
  
  static abstract interface FolderListener
  {
    public abstract void onAdd(ShortcutInfo paramShortcutInfo);
    
    public abstract void onItemsChanged();
    
    public abstract void onRemove(ShortcutInfo paramShortcutInfo);
    
    public abstract void onTitleChanged(CharSequence paramCharSequence);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.FolderInfo
 * JD-Core Version:    0.7.0.1
 */