package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.google.android.googlequicksearchbox.R.styleable;
import javax.annotation.Nullable;

public class CardTableLayout
  extends TableLayout
{
  @Nullable
  private AlignedDrawable mAlignedDivider;
  private int mDividerBackground = 0;
  private int mDividerColumn = -1;
  private int mDividerPadding;
  @Nullable
  private Drawable mOriginalDivider;
  private final Rect mTmpRect;
  
  public CardTableLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CardTableLayout);
    this.mDividerColumn = localTypedArray.getInt(0, -1);
    setDividerBackgroundColor(localTypedArray.getColor(1, 0));
    localTypedArray.recycle();
    this.mTmpRect = new Rect();
    if (super.getDividerPadding() != 0)
    {
      setDividerPadding(super.getDividerPadding());
      super.setDividerPadding(0);
    }
    if ((this.mOriginalDivider == null) && (getDividerDrawable() != null)) {
      setDividerDrawable(super.getDividerDrawable());
    }
  }
  
  private boolean alignDivider(AlignedDrawable paramAlignedDrawable)
  {
    int i = this.mDividerPadding;
    int j = this.mDividerPadding;
    if (this.mDividerColumn != -1)
    {
      View localView = getFirstCellInColumn(this.mDividerColumn);
      if (localView != null)
      {
        this.mTmpRect.set(0, 0, localView.getWidth(), 0);
        offsetDescendantRectToMyCoords(localView, this.mTmpRect);
        i += this.mTmpRect.left;
        j += getWidth() - this.mTmpRect.right;
      }
    }
    boolean bool1;
    if (!paramAlignedDrawable.setLeftPadding(i))
    {
      boolean bool2 = paramAlignedDrawable.setRightPadding(j);
      bool1 = false;
      if (!bool2) {}
    }
    else
    {
      bool1 = true;
    }
    return bool1;
  }
  
  private Drawable createAlignedDivider(Drawable paramDrawable, int paramInt)
  {
    this.mAlignedDivider = new AlignedDrawable(paramDrawable);
    alignDivider(this.mAlignedDivider);
    if (paramInt == 0) {
      return this.mAlignedDivider;
    }
    Drawable[] arrayOfDrawable = new Drawable[2];
    arrayOfDrawable[0] = new ColorDrawable(paramInt);
    arrayOfDrawable[1] = this.mAlignedDivider;
    return new LayerDrawable(arrayOfDrawable);
  }
  
  @Nullable
  private View getFirstCellInColumn(int paramInt)
  {
    for (int i = 0; i < getChildCount(); i++)
    {
      View localView = getChildAt(i);
      if ((localView instanceof TableRow))
      {
        TableRow localTableRow = (TableRow)localView;
        if (paramInt < localTableRow.getChildCount()) {
          return localTableRow.getChildAt(paramInt);
        }
      }
    }
    return null;
  }
  
  private void setDividerBackgroundColor(int paramInt)
  {
    if (this.mDividerBackground == paramInt) {
      return;
    }
    if (this.mOriginalDivider != null) {
      super.setDividerDrawable(createAlignedDivider(this.mOriginalDivider, paramInt));
    }
    this.mDividerBackground = paramInt;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if ((this.mAlignedDivider != null) && (alignDivider(this.mAlignedDivider))) {
      invalidate();
    }
  }
  
  public void setDividerColumn(int paramInt)
  {
    if (this.mDividerColumn == paramInt) {
      return;
    }
    this.mDividerColumn = paramInt;
    invalidate();
  }
  
  public void setDividerDrawable(Drawable paramDrawable)
  {
    if (paramDrawable == this.mOriginalDivider) {
      return;
    }
    this.mOriginalDivider = paramDrawable;
    if (paramDrawable != null) {}
    for (Drawable localDrawable = createAlignedDivider(paramDrawable, this.mDividerBackground);; localDrawable = null)
    {
      super.setDividerDrawable(localDrawable);
      return;
    }
  }
  
  public void setDividerPadding(int paramInt)
  {
    if (this.mDividerPadding == paramInt) {}
    do
    {
      return;
      this.mDividerPadding = paramInt;
    } while ((this.mAlignedDivider == null) || (!alignDivider(this.mAlignedDivider)));
    invalidate();
  }
  
  private static class AlignedDrawable
    extends InsetDrawable
  {
    private int mLeftPadding;
    private int mRightPadding;
    private Rect mTmpRect = new Rect();
    
    public AlignedDrawable(Drawable paramDrawable)
    {
      super(0);
    }
    
    private boolean setLeftPadding(int paramInt)
    {
      if (this.mLeftPadding == paramInt) {
        return false;
      }
      this.mLeftPadding = paramInt;
      return true;
    }
    
    private boolean setRightPadding(int paramInt)
    {
      if (this.mRightPadding == paramInt) {
        return false;
      }
      this.mRightPadding = paramInt;
      return true;
    }
    
    protected void onBoundsChange(Rect paramRect)
    {
      this.mTmpRect.set(paramRect);
      Rect localRect1 = this.mTmpRect;
      localRect1.left += this.mLeftPadding;
      Rect localRect2 = this.mTmpRect;
      localRect2.right -= this.mRightPadding;
      super.onBoundsChange(this.mTmpRect);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.CardTableLayout
 * JD-Core Version:    0.7.0.1
 */