package com.android.ex.photo.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import java.util.HashMap;

public abstract class BaseCursorPagerAdapter
  extends BaseFragmentPagerAdapter
{
  protected Context mContext;
  protected Cursor mCursor;
  protected SparseIntArray mItemPosition;
  protected final HashMap<Object, Integer> mObjectRowMap = new HashMap();
  protected int mRowIDColumn;
  
  public BaseCursorPagerAdapter(Context paramContext, FragmentManager paramFragmentManager, Cursor paramCursor)
  {
    super(paramFragmentManager);
    init(paramContext, paramCursor);
  }
  
  private void init(Context paramContext, Cursor paramCursor)
  {
    int i;
    if (paramCursor != null)
    {
      i = 1;
      this.mCursor = paramCursor;
      this.mContext = paramContext;
      if (i == 0) {
        break label45;
      }
    }
    label45:
    for (int j = this.mCursor.getColumnIndex("uri");; j = -1)
    {
      this.mRowIDColumn = j;
      return;
      i = 0;
      break;
    }
  }
  
  private boolean moveCursorTo(int paramInt)
  {
    if ((this.mCursor != null) && (!this.mCursor.isClosed())) {
      return this.mCursor.moveToPosition(paramInt);
    }
    return false;
  }
  
  private void setItemPosition()
  {
    if ((this.mCursor == null) || (this.mCursor.isClosed()))
    {
      this.mItemPosition = null;
      return;
    }
    SparseIntArray localSparseIntArray = new SparseIntArray(this.mCursor.getCount());
    this.mCursor.moveToPosition(-1);
    while (this.mCursor.moveToNext()) {
      localSparseIntArray.append(this.mCursor.getString(this.mRowIDColumn).hashCode(), this.mCursor.getPosition());
    }
    this.mItemPosition = localSparseIntArray;
  }
  
  public void destroyItem(View paramView, int paramInt, Object paramObject)
  {
    this.mObjectRowMap.remove(paramObject);
    super.destroyItem(paramView, paramInt, paramObject);
  }
  
  public int getCount()
  {
    if (this.mCursor != null) {
      return this.mCursor.getCount();
    }
    return 0;
  }
  
  public Cursor getCursor()
  {
    return this.mCursor;
  }
  
  public Fragment getItem(int paramInt)
  {
    if ((this.mCursor != null) && (moveCursorTo(paramInt))) {
      return getItem(this.mContext, this.mCursor, paramInt);
    }
    return null;
  }
  
  public abstract Fragment getItem(Context paramContext, Cursor paramCursor, int paramInt);
  
  public int getItemPosition(Object paramObject)
  {
    Integer localInteger = (Integer)this.mObjectRowMap.get(paramObject);
    if ((localInteger == null) || (this.mItemPosition == null)) {
      return -2;
    }
    return this.mItemPosition.get(localInteger.intValue(), -2);
  }
  
  public Object instantiateItem(View paramView, int paramInt)
  {
    if (this.mCursor == null) {
      throw new IllegalStateException("this should only be called when the cursor is valid");
    }
    if (moveCursorTo(paramInt)) {}
    for (Integer localInteger = Integer.valueOf(this.mCursor.getString(this.mRowIDColumn).hashCode());; localInteger = null)
    {
      Object localObject = super.instantiateItem(paramView, paramInt);
      if (localObject != null) {
        this.mObjectRowMap.put(localObject, localInteger);
      }
      return localObject;
    }
  }
  
  protected String makeFragmentName(int paramInt1, int paramInt2)
  {
    if (moveCursorTo(paramInt2)) {
      return "android:pager:" + paramInt1 + ":" + this.mCursor.getString(this.mRowIDColumn).hashCode();
    }
    return super.makeFragmentName(paramInt1, paramInt2);
  }
  
  public Cursor swapCursor(Cursor paramCursor)
  {
    int i;
    StringBuilder localStringBuilder2;
    if (Log.isLoggable("BaseCursorPagerAdapter", 2))
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("swapCursor old=");
      if (this.mCursor != null) {
        break label78;
      }
      i = -1;
      localStringBuilder2 = localStringBuilder1.append(i).append("; new=");
      if (paramCursor != null) {
        break label92;
      }
    }
    label78:
    label92:
    for (int j = -1;; j = paramCursor.getCount())
    {
      Log.v("BaseCursorPagerAdapter", j);
      if (paramCursor != this.mCursor) {
        break label103;
      }
      return null;
      i = this.mCursor.getCount();
      break;
    }
    label103:
    Cursor localCursor = this.mCursor;
    this.mCursor = paramCursor;
    if (paramCursor != null) {}
    for (this.mRowIDColumn = paramCursor.getColumnIndex("uri");; this.mRowIDColumn = -1)
    {
      setItemPosition();
      notifyDataSetChanged();
      return localCursor;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.adapters.BaseCursorPagerAdapter
 * JD-Core Version:    0.7.0.1
 */