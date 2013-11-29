package com.android.launcher3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class FolderIcon
  extends FrameLayout
  implements FolderInfo.FolderListener
{
  public static Drawable sSharedFolderLeaveBehind = null;
  private static boolean sStaticValuesDirty = true;
  private PreviewItemDrawingParams mAnimParams = new PreviewItemDrawingParams(0.0F, 0.0F, 0.0F, 0);
  boolean mAnimating = false;
  private int mAvailableSpaceInPreview;
  private float mBaselineIconScale;
  private int mBaselineIconSize;
  private Folder mFolder;
  private BubbleTextView mFolderName;
  FolderRingAnimator mFolderRingAnimator = null;
  private ArrayList<ShortcutInfo> mHiddenItems = new ArrayList();
  private FolderInfo mInfo;
  private int mIntrinsicIconSize;
  private Launcher mLauncher;
  private CheckLongPressHelper mLongPressHelper;
  private float mMaxPerspectiveShift;
  private Rect mOldBounds = new Rect();
  private PreviewItemDrawingParams mParams = new PreviewItemDrawingParams(0.0F, 0.0F, 0.0F, 0);
  private ImageView mPreviewBackground;
  private int mPreviewOffsetX;
  private int mPreviewOffsetY;
  private int mTotalWidth = -1;
  
  public FolderIcon(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public FolderIcon(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  private void animateFirstItem(Drawable paramDrawable, int paramInt, final boolean paramBoolean, final Runnable paramRunnable)
  {
    final PreviewItemDrawingParams localPreviewItemDrawingParams = computePreviewItemDrawingParams(0, null);
    final float f1 = (this.mAvailableSpaceInPreview - paramDrawable.getIntrinsicWidth()) / 2;
    final float f2 = (this.mAvailableSpaceInPreview - paramDrawable.getIntrinsicHeight()) / 2 + getPaddingTop();
    this.mAnimParams.drawable = paramDrawable;
    ValueAnimator localValueAnimator = LauncherAnimUtils.ofFloat(this, new float[] { 0.0F, 1.0F });
    localValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        float f = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
        if (paramBoolean)
        {
          f = 1.0F - f;
          FolderIcon.this.mPreviewBackground.setAlpha(f);
        }
        FolderIcon.this.mAnimParams.transX = (f1 + f * (localPreviewItemDrawingParams.transX - f1));
        FolderIcon.this.mAnimParams.transY = (f2 + f * (localPreviewItemDrawingParams.transY - f2));
        FolderIcon.this.mAnimParams.scale = (1.0F + f * (localPreviewItemDrawingParams.scale - 1.0F));
        FolderIcon.this.invalidate();
      }
    });
    localValueAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        FolderIcon.this.mAnimating = false;
        if (paramRunnable != null) {
          paramRunnable.run();
        }
      }
      
      public void onAnimationStart(Animator paramAnonymousAnimator)
      {
        FolderIcon.this.mAnimating = true;
      }
    });
    localValueAnimator.setDuration(paramInt);
    localValueAnimator.start();
  }
  
  private void computePreviewDrawingParams(int paramInt1, int paramInt2)
  {
    if ((this.mIntrinsicIconSize != paramInt1) || (this.mTotalWidth != paramInt2))
    {
      DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
      this.mIntrinsicIconSize = paramInt1;
      this.mTotalWidth = paramInt2;
      int i = this.mPreviewBackground.getLayoutParams().height;
      int j = FolderRingAnimator.sPreviewPadding;
      this.mAvailableSpaceInPreview = (i - j * 2);
      int k = (int)(1.8F * (this.mAvailableSpaceInPreview / 2));
      int m = (int)(1.24F * this.mIntrinsicIconSize);
      this.mBaselineIconScale = (1.0F * k / m);
      this.mBaselineIconSize = ((int)(this.mIntrinsicIconSize * this.mBaselineIconScale));
      this.mMaxPerspectiveShift = (0.24F * this.mBaselineIconSize);
      this.mPreviewOffsetX = ((this.mTotalWidth - this.mAvailableSpaceInPreview) / 2);
      this.mPreviewOffsetY = (j + localDeviceProfile.folderBackgroundOffset);
    }
  }
  
  private void computePreviewDrawingParams(Drawable paramDrawable)
  {
    computePreviewDrawingParams(paramDrawable.getIntrinsicWidth(), getMeasuredWidth());
  }
  
  private PreviewItemDrawingParams computePreviewItemDrawingParams(int paramInt, PreviewItemDrawingParams paramPreviewItemDrawingParams)
  {
    float f1 = 1.0F * (-1 + (3 - paramInt)) / 2.0F;
    float f2 = 1.0F - 0.35F * (1.0F - f1);
    float f3 = (1.0F - f1) * this.mMaxPerspectiveShift;
    float f4 = f2 * this.mBaselineIconSize;
    float f5 = (1.0F - f2) * this.mBaselineIconSize;
    float f6 = this.mAvailableSpaceInPreview - (f5 + (f3 + f4)) + getPaddingTop();
    float f7 = f3 + f5;
    float f8 = f2 * this.mBaselineIconScale;
    int i = (int)(80.0F * (1.0F - f1));
    if (paramPreviewItemDrawingParams == null)
    {
      PreviewItemDrawingParams localPreviewItemDrawingParams = new PreviewItemDrawingParams(f7, f6, f8, i);
      return localPreviewItemDrawingParams;
    }
    paramPreviewItemDrawingParams.transX = f7;
    paramPreviewItemDrawingParams.transY = f6;
    paramPreviewItemDrawingParams.scale = f8;
    paramPreviewItemDrawingParams.overlayAlpha = i;
    return paramPreviewItemDrawingParams;
  }
  
  private void drawPreviewItem(Canvas paramCanvas, PreviewItemDrawingParams paramPreviewItemDrawingParams)
  {
    paramCanvas.save();
    paramCanvas.translate(paramPreviewItemDrawingParams.transX + this.mPreviewOffsetX, paramPreviewItemDrawingParams.transY + this.mPreviewOffsetY);
    paramCanvas.scale(paramPreviewItemDrawingParams.scale, paramPreviewItemDrawingParams.scale);
    Drawable localDrawable = paramPreviewItemDrawingParams.drawable;
    if (localDrawable != null)
    {
      this.mOldBounds.set(localDrawable.getBounds());
      localDrawable.setBounds(0, 0, this.mIntrinsicIconSize, this.mIntrinsicIconSize);
      localDrawable.setColorFilter(Color.argb(paramPreviewItemDrawingParams.overlayAlpha, 255, 255, 255), PorterDuff.Mode.SRC_ATOP);
      localDrawable.draw(paramCanvas);
      localDrawable.clearColorFilter();
      localDrawable.setBounds(this.mOldBounds);
    }
    paramCanvas.restore();
  }
  
  static FolderIcon fromXml(int paramInt, Launcher paramLauncher, ViewGroup paramViewGroup, FolderInfo paramFolderInfo, IconCache paramIconCache)
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    FolderIcon localFolderIcon = (FolderIcon)LayoutInflater.from(paramLauncher).inflate(paramInt, paramViewGroup, false);
    localFolderIcon.setClipToPadding(false);
    localFolderIcon.mFolderName = ((BubbleTextView)localFolderIcon.findViewById(2131296639));
    localFolderIcon.mFolderName.setText(paramFolderInfo.title);
    localFolderIcon.mFolderName.setCompoundDrawablePadding(0);
    ((FrameLayout.LayoutParams)localFolderIcon.mFolderName.getLayoutParams()).topMargin = (localDeviceProfile.iconSizePx + localDeviceProfile.iconDrawablePaddingPx);
    localFolderIcon.mPreviewBackground = ((ImageView)localFolderIcon.findViewById(2131296638));
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)localFolderIcon.mPreviewBackground.getLayoutParams();
    localLayoutParams.topMargin = localDeviceProfile.folderBackgroundOffset;
    localLayoutParams.width = localDeviceProfile.folderIconSizePx;
    localLayoutParams.height = localDeviceProfile.folderIconSizePx;
    localFolderIcon.setTag(paramFolderInfo);
    localFolderIcon.setOnClickListener(paramLauncher);
    localFolderIcon.mInfo = paramFolderInfo;
    localFolderIcon.mLauncher = paramLauncher;
    String str = paramLauncher.getString(2131361951);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramFolderInfo.title;
    localFolderIcon.setContentDescription(String.format(str, arrayOfObject));
    Folder localFolder = Folder.fromXml(paramLauncher);
    localFolder.setDragController(paramLauncher.getDragController());
    localFolder.setFolderIcon(localFolderIcon);
    localFolder.bind(paramFolderInfo);
    localFolderIcon.mFolder = localFolder;
    localFolderIcon.mFolderRingAnimator = new FolderRingAnimator(paramLauncher, localFolderIcon);
    paramFolderInfo.addListener(localFolderIcon);
    return localFolderIcon;
  }
  
  private float getLocalCenterForIndex(int paramInt, int[] paramArrayOfInt)
  {
    this.mParams = computePreviewItemDrawingParams(Math.min(3, paramInt), this.mParams);
    PreviewItemDrawingParams localPreviewItemDrawingParams1 = this.mParams;
    localPreviewItemDrawingParams1.transX += this.mPreviewOffsetX;
    PreviewItemDrawingParams localPreviewItemDrawingParams2 = this.mParams;
    localPreviewItemDrawingParams2.transY += this.mPreviewOffsetY;
    float f1 = this.mParams.transX + this.mParams.scale * this.mIntrinsicIconSize / 2.0F;
    float f2 = this.mParams.transY + this.mParams.scale * this.mIntrinsicIconSize / 2.0F;
    paramArrayOfInt[0] = Math.round(f1);
    paramArrayOfInt[1] = Math.round(f2);
    return this.mParams.scale;
  }
  
  private void init()
  {
    this.mLongPressHelper = new CheckLongPressHelper(this);
  }
  
  private void onDrop(final ShortcutInfo paramShortcutInfo, DragView paramDragView, Rect paramRect, float paramFloat, int paramInt, Runnable paramRunnable, DropTarget.DragObject paramDragObject)
  {
    paramShortcutInfo.cellX = -1;
    paramShortcutInfo.cellY = -1;
    if (paramDragView != null)
    {
      DragLayer localDragLayer = this.mLauncher.getDragLayer();
      Rect localRect1 = new Rect();
      localDragLayer.getViewRectRelativeToSelf(paramDragView, localRect1);
      Rect localRect2 = paramRect;
      if (localRect2 == null)
      {
        localRect2 = new Rect();
        Workspace localWorkspace = this.mLauncher.getWorkspace();
        localWorkspace.setFinalTransitionTransform((CellLayout)getParent().getParent());
        float f1 = getScaleX();
        float f2 = getScaleY();
        setScaleX(1.0F);
        setScaleY(1.0F);
        paramFloat = localDragLayer.getDescendantRectRelativeToSelf(this, localRect2);
        setScaleX(f1);
        setScaleY(f2);
        localWorkspace.resetTransitionTransform((CellLayout)getParent().getParent());
      }
      int[] arrayOfInt = new int[2];
      float f3 = getLocalCenterForIndex(paramInt, arrayOfInt);
      arrayOfInt[0] = Math.round(paramFloat * arrayOfInt[0]);
      arrayOfInt[1] = Math.round(paramFloat * arrayOfInt[1]);
      localRect2.offset(arrayOfInt[0] - paramDragView.getMeasuredWidth() / 2, arrayOfInt[1] - paramDragView.getMeasuredHeight() / 2);
      if (paramInt < 3) {}
      for (float f4 = 0.5F;; f4 = 0.0F)
      {
        float f5 = f3 * paramFloat;
        localDragLayer.animateView(paramDragView, localRect1, localRect2, f4, 1.0F, 1.0F, f5, f5, 400, new DecelerateInterpolator(2.0F), new AccelerateInterpolator(2.0F), paramRunnable, 0, null);
        addItem(paramShortcutInfo);
        this.mHiddenItems.add(paramShortcutInfo);
        this.mFolder.hideItem(paramShortcutInfo);
        postDelayed(new Runnable()
        {
          public void run()
          {
            FolderIcon.this.mHiddenItems.remove(paramShortcutInfo);
            FolderIcon.this.mFolder.showItem(paramShortcutInfo);
            FolderIcon.this.invalidate();
          }
        }, 400L);
        return;
      }
    }
    addItem(paramShortcutInfo);
  }
  
  private boolean willAcceptItem(ItemInfo paramItemInfo)
  {
    int i = paramItemInfo.itemType;
    return ((i == 0) || (i == 1)) && (!this.mFolder.isFull()) && (paramItemInfo != this.mInfo) && (!this.mInfo.opened);
  }
  
  public boolean acceptDrop(Object paramObject)
  {
    ItemInfo localItemInfo = (ItemInfo)paramObject;
    return (!this.mFolder.isDestroyed()) && (willAcceptItem(localItemInfo));
  }
  
  public void addItem(ShortcutInfo paramShortcutInfo)
  {
    this.mInfo.add(paramShortcutInfo);
  }
  
  public void cancelLongPress()
  {
    super.cancelLongPress();
    this.mLongPressHelper.cancelLongPress();
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    if (this.mFolder == null) {}
    while ((this.mFolder.getItemCount() == 0) && (!this.mAnimating)) {
      return;
    }
    ArrayList localArrayList = this.mFolder.getItemsInReadingOrder();
    if (this.mAnimating) {
      computePreviewDrawingParams(this.mAnimParams.drawable);
    }
    for (;;)
    {
      int i = Math.min(localArrayList.size(), 3);
      if (this.mAnimating) {
        break label175;
      }
      for (int j = i - 1; j >= 0; j--)
      {
        TextView localTextView = (TextView)localArrayList.get(j);
        if (!this.mHiddenItems.contains(localTextView.getTag()))
        {
          Drawable localDrawable = localTextView.getCompoundDrawables()[1];
          this.mParams = computePreviewItemDrawingParams(j, this.mParams);
          this.mParams.drawable = localDrawable;
          drawPreviewItem(paramCanvas, this.mParams);
        }
      }
      break;
      computePreviewDrawingParams(((TextView)localArrayList.get(0)).getCompoundDrawables()[1]);
    }
    label175:
    drawPreviewItem(paramCanvas, this.mAnimParams);
  }
  
  Folder getFolder()
  {
    return this.mFolder;
  }
  
  FolderInfo getFolderInfo()
  {
    return this.mInfo;
  }
  
  public boolean getTextVisible()
  {
    return this.mFolderName.getVisibility() == 0;
  }
  
  public void onAdd(ShortcutInfo paramShortcutInfo)
  {
    invalidate();
    requestLayout();
  }
  
  public void onDragEnter(Object paramObject)
  {
    if ((this.mFolder.isDestroyed()) || (!willAcceptItem((ItemInfo)paramObject))) {
      return;
    }
    CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)getLayoutParams();
    CellLayout localCellLayout = (CellLayout)getParent().getParent();
    this.mFolderRingAnimator.setCell(localLayoutParams.cellX, localLayoutParams.cellY);
    this.mFolderRingAnimator.setCellLayout(localCellLayout);
    this.mFolderRingAnimator.animateToAcceptState();
    localCellLayout.showFolderAccept(this.mFolderRingAnimator);
  }
  
  public void onDragExit()
  {
    this.mFolderRingAnimator.animateToNaturalState();
  }
  
  public void onDragExit(Object paramObject)
  {
    onDragExit();
  }
  
  public void onDrop(DropTarget.DragObject paramDragObject)
  {
    if ((paramDragObject.dragInfo instanceof AppInfo)) {}
    for (ShortcutInfo localShortcutInfo = ((AppInfo)paramDragObject.dragInfo).makeShortcut();; localShortcutInfo = (ShortcutInfo)paramDragObject.dragInfo)
    {
      this.mFolder.notifyDrop();
      onDrop(localShortcutInfo, paramDragObject.dragView, null, 1.0F, this.mInfo.contents.size(), paramDragObject.postAnimationRunnable, paramDragObject);
      return;
    }
  }
  
  public void onItemsChanged()
  {
    invalidate();
    requestLayout();
  }
  
  public void onRemove(ShortcutInfo paramShortcutInfo)
  {
    invalidate();
    requestLayout();
  }
  
  protected Parcelable onSaveInstanceState()
  {
    sStaticValuesDirty = true;
    return super.onSaveInstanceState();
  }
  
  public void onTitleChanged(CharSequence paramCharSequence)
  {
    this.mFolderName.setText(paramCharSequence.toString());
    setContentDescription(String.format(getContext().getString(2131361951), new Object[] { paramCharSequence }));
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = super.onTouchEvent(paramMotionEvent);
    switch (paramMotionEvent.getAction())
    {
    case 2: 
    default: 
      return bool;
    case 0: 
      this.mLongPressHelper.postCheckForLongPress();
      return bool;
    }
    this.mLongPressHelper.cancelLongPress();
    return bool;
  }
  
  public void performCreateAnimation(ShortcutInfo paramShortcutInfo1, View paramView, ShortcutInfo paramShortcutInfo2, DragView paramDragView, Rect paramRect, float paramFloat, Runnable paramRunnable)
  {
    Drawable localDrawable = ((TextView)paramView).getCompoundDrawables()[1];
    computePreviewDrawingParams(localDrawable.getIntrinsicWidth(), paramView.getMeasuredWidth());
    animateFirstItem(localDrawable, 350, false, null);
    addItem(paramShortcutInfo1);
    onDrop(paramShortcutInfo2, paramDragView, paramRect, paramFloat, 1, paramRunnable, null);
  }
  
  public void performDestroyAnimation(View paramView, Runnable paramRunnable)
  {
    Drawable localDrawable = ((TextView)paramView).getCompoundDrawables()[1];
    computePreviewDrawingParams(localDrawable.getIntrinsicWidth(), paramView.getMeasuredWidth());
    animateFirstItem(localDrawable, 200, true, paramRunnable);
  }
  
  public void setTextVisible(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mFolderName.setVisibility(0);
      return;
    }
    this.mFolderName.setVisibility(4);
  }
  
  public static class FolderRingAnimator
  {
    public static int sPreviewPadding = -1;
    public static int sPreviewSize;
    public static Drawable sSharedInnerRingDrawable;
    public static Drawable sSharedOuterRingDrawable = null;
    private ValueAnimator mAcceptAnimator;
    private CellLayout mCellLayout;
    public int mCellX;
    public int mCellY;
    public FolderIcon mFolderIcon = null;
    public float mInnerRingSize;
    private ValueAnimator mNeutralAnimator;
    public float mOuterRingSize;
    
    static
    {
      sSharedInnerRingDrawable = null;
      sPreviewSize = -1;
    }
    
    public FolderRingAnimator(Launcher paramLauncher, FolderIcon paramFolderIcon)
    {
      this.mFolderIcon = paramFolderIcon;
      Resources localResources = paramLauncher.getResources();
      if (FolderIcon.sStaticValuesDirty)
      {
        if (Looper.myLooper() != Looper.getMainLooper()) {
          throw new RuntimeException("FolderRingAnimator loading drawables on non-UI thread " + Thread.currentThread());
        }
        sPreviewSize = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile().folderIconSizePx;
        sPreviewPadding = localResources.getDimensionPixelSize(2131689553);
        sSharedOuterRingDrawable = localResources.getDrawable(2130838023);
        sSharedInnerRingDrawable = localResources.getDrawable(2130838022);
        FolderIcon.sSharedFolderLeaveBehind = localResources.getDrawable(2130838024);
        FolderIcon.access$002(false);
      }
    }
    
    public void animateToAcceptState()
    {
      if (this.mNeutralAnimator != null) {
        this.mNeutralAnimator.cancel();
      }
      this.mAcceptAnimator = LauncherAnimUtils.ofFloat(this.mCellLayout, new float[] { 0.0F, 1.0F });
      this.mAcceptAnimator.setDuration(100L);
      final int i = sPreviewSize;
      this.mAcceptAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          float f = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
          FolderIcon.FolderRingAnimator.this.mOuterRingSize = ((1.0F + 0.3F * f) * i);
          FolderIcon.FolderRingAnimator.this.mInnerRingSize = ((1.0F + 0.15F * f) * i);
          if (FolderIcon.FolderRingAnimator.this.mCellLayout != null) {
            FolderIcon.FolderRingAnimator.this.mCellLayout.invalidate();
          }
        }
      });
      this.mAcceptAnimator.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationStart(Animator paramAnonymousAnimator)
        {
          if (FolderIcon.FolderRingAnimator.this.mFolderIcon != null) {
            FolderIcon.FolderRingAnimator.this.mFolderIcon.mPreviewBackground.setVisibility(4);
          }
        }
      });
      this.mAcceptAnimator.start();
    }
    
    public void animateToNaturalState()
    {
      if (this.mAcceptAnimator != null) {
        this.mAcceptAnimator.cancel();
      }
      this.mNeutralAnimator = LauncherAnimUtils.ofFloat(this.mCellLayout, new float[] { 0.0F, 1.0F });
      this.mNeutralAnimator.setDuration(100L);
      final int i = sPreviewSize;
      this.mNeutralAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          float f = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
          FolderIcon.FolderRingAnimator.this.mOuterRingSize = ((1.0F + 0.3F * (1.0F - f)) * i);
          FolderIcon.FolderRingAnimator.this.mInnerRingSize = ((1.0F + 0.15F * (1.0F - f)) * i);
          if (FolderIcon.FolderRingAnimator.this.mCellLayout != null) {
            FolderIcon.FolderRingAnimator.this.mCellLayout.invalidate();
          }
        }
      });
      this.mNeutralAnimator.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          if (FolderIcon.FolderRingAnimator.this.mCellLayout != null) {
            FolderIcon.FolderRingAnimator.this.mCellLayout.hideFolderAccept(FolderIcon.FolderRingAnimator.this);
          }
          if (FolderIcon.FolderRingAnimator.this.mFolderIcon != null) {
            FolderIcon.FolderRingAnimator.this.mFolderIcon.mPreviewBackground.setVisibility(0);
          }
        }
      });
      this.mNeutralAnimator.start();
    }
    
    public float getInnerRingSize()
    {
      return this.mInnerRingSize;
    }
    
    public float getOuterRingSize()
    {
      return this.mOuterRingSize;
    }
    
    public void setCell(int paramInt1, int paramInt2)
    {
      this.mCellX = paramInt1;
      this.mCellY = paramInt2;
    }
    
    public void setCellLayout(CellLayout paramCellLayout)
    {
      this.mCellLayout = paramCellLayout;
    }
  }
  
  class PreviewItemDrawingParams
  {
    Drawable drawable;
    int overlayAlpha;
    float scale;
    float transX;
    float transY;
    
    PreviewItemDrawingParams(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt)
    {
      this.transX = paramFloat1;
      this.transY = paramFloat2;
      this.scale = paramFloat3;
      this.overlayAlpha = paramInt;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.FolderIcon
 * JD-Core Version:    0.7.0.1
 */