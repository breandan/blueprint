package com.google.android.velvet.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;

public class DogfoodIndicator
        extends View {
    private final Paint mBannerPaint;
    private final float mDensity = getResources().getDisplayMetrics().density;
    private final Rect mTextBounds;
    private final TextPaint mTextPaint;

    public DogfoodIndicator(Context paramContext) {
        super(paramContext);
        setMinimumWidth(dpToPx(100));
        setMinimumHeight(dpToPx(64));
        setLayerType(1, null);
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(dpToPx(10));
        this.mTextPaint.setTextAlign(Paint.Align.LEFT);
        this.mTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mTextBounds = new Rect();
        this.mTextPaint.getTextBounds("CONFIDENTIAL", 0, "CONFIDENTIAL".length(), this.mTextBounds);
        this.mBannerPaint = new Paint();
        this.mBannerPaint.setColor(-2133000192);
        this.mBannerPaint.setAntiAlias(true);
    }

    private int dpToPx(int paramInt) {
        return (int) (this.mDensity * paramInt);
    }

    protected void onDraw(Canvas paramCanvas) {
        super.onDraw(paramCanvas);
        paramCanvas.rotate(-30.0F, this.mTextBounds.width(), this.mTextBounds.height());
        paramCanvas.translate(dpToPx(-5), dpToPx(-5));
        int i = dpToPx(20);
        int j = dpToPx(2);
        paramCanvas.drawRect(-i, -j, i + this.mTextBounds.width(), j + this.mTextBounds.height(), this.mBannerPaint);
        paramCanvas.drawText("CONFIDENTIAL", 0.0F, this.mTextBounds.height(), this.mTextPaint);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.DogfoodIndicator

 * JD-Core Version:    0.7.0.1

 */