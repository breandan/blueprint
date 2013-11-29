package com.android.launcher3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Cling
  extends FrameLayout
  implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener, Insettable
{
  private static float FIRST_RUN_CIRCLE_BUFFER_DPS;
  private static String FIRST_RUN_LANDSCAPE;
  private static String FIRST_RUN_PORTRAIT = "first_run_portrait";
  private static String FOLDER_LANDSCAPE;
  private static String FOLDER_LARGE;
  private static String FOLDER_PORTRAIT;
  private static float WORKSPACE_CIRCLE_Y_OFFSET_DPS = 30.0F;
  private static String WORKSPACE_CUSTOM;
  private static float WORKSPACE_INNER_CIRCLE_RADIUS_DPS;
  private static String WORKSPACE_LANDSCAPE;
  private static String WORKSPACE_LARGE;
  private static float WORKSPACE_OUTER_CIRCLE_RADIUS_DPS;
  private static String WORKSPACE_PORTRAIT;
  private Drawable mBackground;
  private int mBackgroundColor;
  private Paint mBubblePaint;
  private Paint mDotPaint;
  private String mDrawIdentifier;
  private Paint mErasePaint;
  private Drawable mFocusedHotseatApp;
  private Rect mFocusedHotseatAppBounds;
  private ComponentName mFocusedHotseatAppComponent;
  private final Rect mInsets = new Rect();
  private boolean mIsInitialized;
  private Launcher mLauncher;
  private View mScrimView;
  private int[] mTouchDownPt = new int[2];
  
  static
  {
    FIRST_RUN_LANDSCAPE = "first_run_landscape";
    WORKSPACE_PORTRAIT = "workspace_portrait";
    WORKSPACE_LANDSCAPE = "workspace_landscape";
    WORKSPACE_LARGE = "workspace_large";
    WORKSPACE_CUSTOM = "workspace_custom";
    FOLDER_PORTRAIT = "folder_portrait";
    FOLDER_LANDSCAPE = "folder_landscape";
    FOLDER_LARGE = "folder_large";
    FIRST_RUN_CIRCLE_BUFFER_DPS = 60.0F;
    WORKSPACE_INNER_CIRCLE_RADIUS_DPS = 50.0F;
    WORKSPACE_OUTER_CIRCLE_RADIUS_DPS = 60.0F;
  }
  
  public Cling(Context paramContext)
  {
    this(paramContext, null, 0);
  }
  
  public Cling(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public Cling(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Cling, paramInt, 0);
    this.mDrawIdentifier = localTypedArray.getString(0);
    localTypedArray.recycle();
    setClickable(true);
  }
  
  void bringScrimToFront()
  {
    if (this.mScrimView != null) {
      this.mScrimView.bringToFront();
    }
  }
  
  void cleanup()
  {
    this.mBackground = null;
    this.mIsInitialized = false;
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    Bitmap localBitmap;
    Canvas localCanvas;
    DisplayMetrics localDisplayMetrics;
    float f1;
    if (this.mIsInitialized)
    {
      paramCanvas.save();
      if ((this.mBackground == null) && (this.mDrawIdentifier.equals(WORKSPACE_CUSTOM))) {
        this.mBackground = getResources().getDrawable(2130837517);
      }
      localBitmap = null;
      localCanvas = null;
      if (this.mScrimView == null) {
        break label239;
      }
      this.mScrimView.setBackgroundColor(this.mBackgroundColor);
      localDisplayMetrics = new DisplayMetrics();
      this.mLauncher.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
      f1 = getAlpha();
      View localView1 = getContent();
      if (localView1 != null) {
        f1 *= localView1.getAlpha();
      }
      if ((!this.mDrawIdentifier.equals(FIRST_RUN_PORTRAIT)) && (!this.mDrawIdentifier.equals(FIRST_RUN_LANDSCAPE))) {
        break label371;
      }
      View localView2 = findViewById(2131296592);
      Rect localRect = new Rect();
      localView2.getGlobalVisibleRect(localRect);
      this.mBubblePaint.setAlpha((int)(255.0F * f1));
      float f2 = DynamicGrid.pxFromDp(FIRST_RUN_CIRCLE_BUFFER_DPS, localDisplayMetrics);
      paramCanvas.drawCircle(localDisplayMetrics.widthPixels / 2, localRect.centerY(), (f2 + localView2.getMeasuredWidth()) / 2.0F, this.mBubblePaint);
    }
    for (;;)
    {
      paramCanvas.restore();
      super.dispatchDraw(paramCanvas);
      return;
      label239:
      if (this.mBackground != null)
      {
        this.mBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        this.mBackground.draw(paramCanvas);
        localBitmap = null;
        localCanvas = null;
        break;
      }
      if ((this.mDrawIdentifier.equals(WORKSPACE_PORTRAIT)) || (this.mDrawIdentifier.equals(WORKSPACE_LANDSCAPE)) || (this.mDrawIdentifier.equals(WORKSPACE_LARGE)))
      {
        localBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        localCanvas = new Canvas(localBitmap);
        localCanvas.drawColor(this.mBackgroundColor);
        break;
      }
      paramCanvas.drawColor(this.mBackgroundColor);
      localBitmap = null;
      localCanvas = null;
      break;
      label371:
      if ((this.mDrawIdentifier.equals(WORKSPACE_PORTRAIT)) || (this.mDrawIdentifier.equals(WORKSPACE_LANDSCAPE)) || (this.mDrawIdentifier.equals(WORKSPACE_LARGE)))
      {
        int i = DynamicGrid.pxFromDp(WORKSPACE_CIRCLE_Y_OFFSET_DPS, localDisplayMetrics);
        this.mErasePaint.setAlpha(128);
        localCanvas.drawCircle(localDisplayMetrics.widthPixels / 2, localDisplayMetrics.heightPixels / 2 - i, DynamicGrid.pxFromDp(WORKSPACE_OUTER_CIRCLE_RADIUS_DPS, localDisplayMetrics), this.mErasePaint);
        this.mErasePaint.setAlpha(0);
        localCanvas.drawCircle(localDisplayMetrics.widthPixels / 2, localDisplayMetrics.heightPixels / 2 - i, DynamicGrid.pxFromDp(WORKSPACE_INNER_CIRCLE_RADIUS_DPS, localDisplayMetrics), this.mErasePaint);
        paramCanvas.drawBitmap(localBitmap, 0.0F, 0.0F, null);
        localCanvas.setBitmap(null);
        if ((this.mFocusedHotseatAppBounds != null) && (this.mFocusedHotseatApp != null))
        {
          this.mFocusedHotseatApp.setBounds(this.mFocusedHotseatAppBounds.left, this.mFocusedHotseatAppBounds.top, this.mFocusedHotseatAppBounds.right, this.mFocusedHotseatAppBounds.bottom);
          this.mFocusedHotseatApp.setAlpha((int)(255.0F * f1));
          this.mFocusedHotseatApp.draw(paramCanvas);
        }
      }
    }
  }
  
  public View focusSearch(int paramInt)
  {
    return focusSearch(this, paramInt);
  }
  
  public View focusSearch(View paramView, int paramInt)
  {
    return FocusFinder.getInstance().findNextFocus(this, paramView, paramInt);
  }
  
  View getContent()
  {
    return findViewById(2131296591);
  }
  
  String getDrawIdentifier()
  {
    return this.mDrawIdentifier;
  }
  
  void hide(int paramInt, final Runnable paramRunnable)
  {
    if ((this.mDrawIdentifier.equals(FIRST_RUN_PORTRAIT)) || (this.mDrawIdentifier.equals(FIRST_RUN_LANDSCAPE))) {
      getContent().animate().alpha(0.0F).setDuration(paramInt).setListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          Cling.this.setVisibility(8);
          paramRunnable.run();
        }
      }).start();
    }
    for (;;)
    {
      if (this.mScrimView != null) {
        this.mScrimView.animate().alpha(0.0F).setDuration(paramInt).setListener(new AnimatorListenerAdapter()
        {
          public void onAnimationEnd(Animator paramAnonymousAnimator)
          {
            Cling.this.mScrimView.setVisibility(8);
          }
        }).start();
      }
      return;
      animate().alpha(0.0F).setDuration(paramInt).setListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          Cling.this.setVisibility(8);
          paramRunnable.run();
        }
      }).start();
    }
  }
  
  void init(Launcher paramLauncher, View paramView)
  {
    if (!this.mIsInitialized)
    {
      this.mLauncher = paramLauncher;
      this.mScrimView = paramView;
      this.mBackgroundColor = -587202560;
      setOnLongClickListener(this);
      setOnClickListener(this);
      setOnTouchListener(this);
      this.mErasePaint = new Paint();
      this.mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
      this.mErasePaint.setColor(16777215);
      this.mErasePaint.setAlpha(0);
      this.mErasePaint.setAntiAlias(true);
      int i = getResources().getColor(2131230770);
      this.mBubblePaint = new Paint();
      this.mBubblePaint.setColor(i);
      this.mBubblePaint.setAntiAlias(true);
      this.mDotPaint = new Paint();
      this.mDotPaint.setColor(7519213);
      this.mDotPaint.setAntiAlias(true);
      this.mIsInitialized = true;
    }
  }
  
  public void onClick(View paramView)
  {
    if (((this.mDrawIdentifier.equals(WORKSPACE_PORTRAIT)) || (this.mDrawIdentifier.equals(WORKSPACE_LANDSCAPE)) || (this.mDrawIdentifier.equals(WORKSPACE_LARGE))) && (this.mFocusedHotseatAppBounds != null) && (this.mFocusedHotseatAppBounds.contains(this.mTouchDownPt[0], this.mTouchDownPt[1])))
    {
      Intent localIntent = new Intent("android.intent.action.MAIN");
      localIntent.setComponent(this.mFocusedHotseatAppComponent);
      localIntent.addCategory("android.intent.category.LAUNCHER");
      this.mLauncher.startActivity(localIntent, null);
      this.mLauncher.dismissWorkspaceCling(this);
    }
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    return (this.mDrawIdentifier.equals(WORKSPACE_PORTRAIT)) || (this.mDrawIdentifier.equals(WORKSPACE_LANDSCAPE)) || (this.mDrawIdentifier.equals(WORKSPACE_LARGE)) || (this.mDrawIdentifier.equals(WORKSPACE_CUSTOM));
  }
  
  public boolean onLongClick(View paramView)
  {
    if ((this.mDrawIdentifier.equals(WORKSPACE_PORTRAIT)) || (this.mDrawIdentifier.equals(WORKSPACE_LANDSCAPE)) || (this.mDrawIdentifier.equals(WORKSPACE_LARGE)))
    {
      this.mLauncher.dismissWorkspaceCling(null);
      return true;
    }
    return false;
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0)
    {
      this.mTouchDownPt[0] = ((int)paramMotionEvent.getX());
      this.mTouchDownPt[1] = ((int)paramMotionEvent.getY());
    }
    return false;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mDrawIdentifier.equals(FOLDER_PORTRAIT)) || (this.mDrawIdentifier.equals(FOLDER_LANDSCAPE)) || (this.mDrawIdentifier.equals(FOLDER_LARGE)))
    {
      Folder localFolder = this.mLauncher.getWorkspace().getOpenFolder();
      if (localFolder != null)
      {
        Rect localRect = new Rect();
        localFolder.getHitRect(localRect);
        if (localRect.contains((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY())) {
          return false;
        }
      }
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  void setFocusedHotseatApp(int paramInt1, int paramInt2, ComponentName paramComponentName, String paramString1, String paramString2)
  {
    getResources();
    Hotseat localHotseat = this.mLauncher.getHotseat();
    if ((localHotseat != null) && (paramInt1 > -1) && (paramInt2 > -1) && (!paramString1.isEmpty()) && (!paramString2.isEmpty()))
    {
      Rect localRect = localHotseat.getCellCoordinates(localHotseat.getCellXFromOrder(paramInt2), localHotseat.getCellYFromOrder(paramInt2));
      DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
      this.mFocusedHotseatApp = getResources().getDrawable(paramInt1);
      this.mFocusedHotseatAppComponent = paramComponentName;
      this.mFocusedHotseatAppBounds = new Rect(localRect.left, localRect.top, localRect.left + Utilities.sIconTextureWidth, localRect.top + Utilities.sIconTextureHeight);
      Utilities.scaleRectAboutCenter(this.mFocusedHotseatAppBounds, localDeviceProfile.hotseatIconSizePx / localDeviceProfile.iconSizePx);
      TextView localTextView1 = (TextView)findViewById(2131297265);
      if (localTextView1 != null) {
        localTextView1.setText(paramString1);
      }
      TextView localTextView2 = (TextView)findViewById(2131297266);
      if (localTextView2 != null) {
        localTextView2.setText(paramString2);
      }
      findViewById(2131297264).setVisibility(0);
    }
  }
  
  public void setInsets(Rect paramRect)
  {
    this.mInsets.set(paramRect);
    setPadding(paramRect.left, paramRect.top, paramRect.right, paramRect.bottom);
  }
  
  void show(boolean paramBoolean, int paramInt)
  {
    setVisibility(0);
    setLayerType(2, null);
    if ((this.mDrawIdentifier.equals(WORKSPACE_PORTRAIT)) || (this.mDrawIdentifier.equals(WORKSPACE_LANDSCAPE)) || (this.mDrawIdentifier.equals(WORKSPACE_LARGE)) || (this.mDrawIdentifier.equals(WORKSPACE_CUSTOM)))
    {
      View localView = getContent();
      localView.setAlpha(0.0F);
      localView.animate().alpha(1.0F).setDuration(paramInt).setListener(null).start();
      setAlpha(1.0F);
    }
    for (;;)
    {
      if (this.mScrimView != null)
      {
        this.mScrimView.setVisibility(0);
        this.mScrimView.setAlpha(0.0F);
        this.mScrimView.animate().alpha(1.0F).setDuration(paramInt).setListener(null).start();
      }
      setFocusableInTouchMode(true);
      post(new Runnable()
      {
        public void run()
        {
          Cling.this.setFocusable(true);
          Cling.this.requestFocus();
        }
      });
      return;
      if (paramBoolean)
      {
        buildLayer();
        setAlpha(0.0F);
        animate().alpha(1.0F).setInterpolator(new AccelerateInterpolator()).setDuration(paramInt).setListener(null).start();
      }
      else
      {
        setAlpha(1.0F);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.Cling
 * JD-Core Version:    0.7.0.1
 */