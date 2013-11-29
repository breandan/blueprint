package com.google.android.search.shared.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.text.StaticLayout;
import android.view.View;

public class StaticLayoutTextView
  extends View
{
  public boolean mIncludesPadding;
  public StaticLayout mStaticLayout;
  
  public StaticLayoutTextView(Context paramContext, StaticLayout paramStaticLayout, boolean paramBoolean)
  {
    super(paramContext);
    this.mStaticLayout = paramStaticLayout;
    this.mIncludesPadding = paramBoolean;
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    this.mStaticLayout.draw(paramCanvas);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.StaticLayoutTextView
 * JD-Core Version:    0.7.0.1
 */