package com.android.launcher3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.widget.AutoScrollHelper;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Folder
  extends LinearLayout
  implements View.OnClickListener, View.OnFocusChangeListener, View.OnLongClickListener, TextView.OnEditorActionListener, DragSource, DropTarget, FolderInfo.FolderListener
{
  private static String sDefaultFolderName;
  private static String sHintText;
  private int DRAG_MODE_NONE = 0;
  private int DRAG_MODE_REORDER = 1;
  private ActionMode.Callback mActionModeCallback = new ActionMode.Callback()
  {
    public boolean onActionItemClicked(ActionMode paramAnonymousActionMode, MenuItem paramAnonymousMenuItem)
    {
      return false;
    }
    
    public boolean onCreateActionMode(ActionMode paramAnonymousActionMode, Menu paramAnonymousMenu)
    {
      return false;
    }
    
    public void onDestroyActionMode(ActionMode paramAnonymousActionMode) {}
    
    public boolean onPrepareActionMode(ActionMode paramAnonymousActionMode, Menu paramAnonymousMenu)
    {
      return false;
    }
  };
  private AutoScrollHelper mAutoScrollHelper;
  protected CellLayout mContent;
  private ShortcutInfo mCurrentDragInfo;
  private View mCurrentDragView;
  private boolean mDeferDropAfterUninstall;
  private Runnable mDeferredAction;
  private boolean mDeleteFolderOnDropCompleted = false;
  private boolean mDestroyed;
  protected DragController mDragController;
  private boolean mDragInProgress = false;
  private int mDragMode = this.DRAG_MODE_NONE;
  private int[] mEmptyCell = new int[2];
  private int mExpandDuration;
  private FolderIcon mFolderIcon;
  private float mFolderIconPivotX;
  private float mFolderIconPivotY;
  FolderEditText mFolderName;
  private int mFolderNameHeight;
  private final IconCache mIconCache;
  private Drawable mIconDrawable;
  private final LayoutInflater mInflater;
  protected FolderInfo mInfo;
  private InputMethodManager mInputMethodManager;
  private boolean mIsEditingName = false;
  private boolean mItemAddedBackToSelfViaIcon = false;
  private ArrayList<View> mItemsInReadingOrder = new ArrayList();
  boolean mItemsInvalidated = false;
  protected Launcher mLauncher;
  private int mMaxCountX;
  private int mMaxCountY;
  private int mMaxNumItems;
  private Alarm mOnExitAlarm = new Alarm();
  OnAlarmListener mOnExitAlarmListener = new OnAlarmListener()
  {
    public void onAlarm(Alarm paramAnonymousAlarm)
    {
      Folder.this.completeDragExit();
    }
  };
  private int[] mPreviousTargetCell = new int[2];
  private boolean mRearrangeOnClose = false;
  private Alarm mReorderAlarm = new Alarm();
  OnAlarmListener mReorderAlarmListener = new OnAlarmListener()
  {
    public void onAlarm(Alarm paramAnonymousAlarm)
    {
      Folder.this.realTimeReorder(Folder.this.mEmptyCell, Folder.this.mTargetCell);
    }
  };
  private ScrollView mScrollView;
  private int mState = -1;
  private boolean mSuppressFolderDeletion = false;
  boolean mSuppressOnAdd = false;
  private int[] mTargetCell = new int[2];
  private Rect mTempRect = new Rect();
  private boolean mUninstallSuccessful;
  
  public Folder(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LauncherAppState localLauncherAppState = LauncherAppState.getInstance();
    DeviceProfile localDeviceProfile = localLauncherAppState.getDynamicGrid().getDeviceProfile();
    setAlwaysDrawnWithCacheEnabled(false);
    this.mInflater = LayoutInflater.from(paramContext);
    this.mIconCache = localLauncherAppState.getIconCache();
    Resources localResources = getResources();
    this.mMaxCountX = ((int)localDeviceProfile.numColumns);
    this.mMaxCountY = ((int)localDeviceProfile.numRows);
    this.mMaxNumItems = (this.mMaxCountX * this.mMaxCountY);
    this.mInputMethodManager = ((InputMethodManager)getContext().getSystemService("input_method"));
    this.mExpandDuration = localResources.getInteger(2131427355);
    if (sDefaultFolderName == null) {
      sDefaultFolderName = localResources.getString(2131361862);
    }
    if (sHintText == null) {
      sHintText = localResources.getString(2131361929);
    }
    this.mLauncher = ((Launcher)paramContext);
    setFocusableInTouchMode(true);
  }
  
  private void arrangeChildren(ArrayList<View> paramArrayList)
  {
    int[] arrayOfInt = new int[2];
    if (paramArrayList == null) {
      paramArrayList = getItemsInReadingOrder();
    }
    this.mContent.removeAllViews();
    int i = 0;
    if (i < paramArrayList.size())
    {
      View localView = (View)paramArrayList.get(i);
      this.mContent.getVacantCell(arrayOfInt, 1, 1);
      CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)localView.getLayoutParams();
      localLayoutParams.cellX = arrayOfInt[0];
      localLayoutParams.cellY = arrayOfInt[1];
      ItemInfo localItemInfo = (ItemInfo)localView.getTag();
      if ((localItemInfo.cellX != arrayOfInt[0]) || (localItemInfo.cellY != arrayOfInt[1]))
      {
        localItemInfo.cellX = arrayOfInt[0];
        localItemInfo.cellY = arrayOfInt[1];
        LauncherModel.addOrMoveItemInDatabase(this.mLauncher, localItemInfo, this.mInfo.id, 0L, localItemInfo.cellX, localItemInfo.cellY);
      }
      CellLayout localCellLayout = this.mContent;
      if (0 != 0) {}
      for (int j = 0;; j = -1)
      {
        localCellLayout.addViewToCellLayout(localView, j, (int)localItemInfo.id, localLayoutParams, true);
        i++;
        break;
      }
    }
    this.mItemsInvalidated = true;
  }
  
  private void centerAboutIcon()
  {
    DragLayer.LayoutParams localLayoutParams = (DragLayer.LayoutParams)getLayoutParams();
    DragLayer localDragLayer = (DragLayer)this.mLauncher.findViewById(2131296745);
    int i = getPaddingLeft() + getPaddingRight() + this.mContent.getDesiredWidth();
    int j = getFolderHeight();
    float f = localDragLayer.getDescendantRectRelativeToSelf(this.mFolderIcon, this.mTempRect);
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    int k = (int)(this.mTempRect.left + f * this.mTempRect.width() / 2.0F);
    int m = (int)(this.mTempRect.top + f * this.mTempRect.height() / 2.0F);
    int n = k - i / 2;
    int i1 = m - j / 2;
    int i2 = this.mLauncher.getWorkspace().getNextPage();
    this.mLauncher.getWorkspace().setFinalScrollForPageChange(i2);
    ShortcutAndWidgetContainer localShortcutAndWidgetContainer = ((CellLayout)this.mLauncher.getWorkspace().getChildAt(i2)).getShortcutsAndWidgets();
    Rect localRect = new Rect();
    localDragLayer.getDescendantRectRelativeToSelf(localShortcutAndWidgetContainer, localRect);
    this.mLauncher.getWorkspace().resetFinalScrollForPageChange(i2);
    int i3 = Math.min(Math.max(localRect.left, n), localRect.left + localRect.width() - i);
    int i4 = Math.min(Math.max(localRect.top, i1), localRect.top + localRect.height() - j);
    if ((localDeviceProfile.isPhone()) && (localDeviceProfile.availableWidthPx - i < localDeviceProfile.iconSizePx)) {
      i3 = (localDeviceProfile.availableWidthPx - i) / 2;
    }
    for (;;)
    {
      if (j >= localRect.height()) {
        i4 = localRect.top + (localRect.height() - j) / 2;
      }
      int i5 = i / 2 + (n - i3);
      int i6 = j / 2 + (i1 - i4);
      setPivotX(i5);
      setPivotY(i6);
      this.mFolderIconPivotX = ((int)(this.mFolderIcon.getMeasuredWidth() * (1.0F * i5 / i)));
      this.mFolderIconPivotY = ((int)(this.mFolderIcon.getMeasuredHeight() * (1.0F * i6 / j)));
      localLayoutParams.width = i;
      localLayoutParams.height = j;
      localLayoutParams.x = i3;
      localLayoutParams.y = i4;
      return;
      if (i >= localRect.width()) {
        i3 = localRect.left + (localRect.width() - i) / 2;
      }
    }
  }
  
  static Folder fromXml(Context paramContext)
  {
    return (Folder)LayoutInflater.from(paramContext).inflate(2130968906, null);
  }
  
  private int getContentAreaHeight()
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    if (localDeviceProfile.isLandscape) {}
    for (int i = 0;; i = 1)
    {
      Rect localRect = localDeviceProfile.getWorkspacePadding(i);
      return Math.min(localDeviceProfile.availableHeightPx - localRect.top - localRect.bottom - this.mFolderNameHeight, this.mContent.getDesiredHeight());
    }
  }
  
  private float[] getDragViewVisualCenter(int paramInt1, int paramInt2, int paramInt3, int paramInt4, DragView paramDragView, float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat == null) {}
    for (float[] arrayOfFloat = new float[2];; arrayOfFloat = paramArrayOfFloat)
    {
      int i = paramInt1 - paramInt3;
      int j = paramInt2 - paramInt4;
      arrayOfFloat[0] = (i + paramDragView.getDragRegion().width() / 2);
      arrayOfFloat[1] = (j + paramDragView.getDragRegion().height() / 2);
      return arrayOfFloat;
    }
  }
  
  private int getFolderHeight()
  {
    return getPaddingTop() + getPaddingBottom() + getContentAreaHeight() + this.mFolderNameHeight;
  }
  
  private View getViewForInfo(ShortcutInfo paramShortcutInfo)
  {
    for (int i = 0; i < this.mContent.getCountY(); i++) {
      for (int j = 0; j < this.mContent.getCountX(); j++)
      {
        View localView = this.mContent.getChildAt(j, i);
        if (localView.getTag() == paramShortcutInfo) {
          return localView;
        }
      }
    }
    return null;
  }
  
  private void onCloseComplete()
  {
    DragLayer localDragLayer = (DragLayer)getParent();
    if (localDragLayer != null) {
      localDragLayer.removeView(this);
    }
    this.mDragController.removeDropTarget(this);
    clearFocus();
    this.mFolderIcon.requestFocus();
    if (this.mRearrangeOnClose)
    {
      setupContentForNumItems(getItemCount());
      this.mRearrangeOnClose = false;
    }
    if (getItemCount() <= 1)
    {
      if ((this.mDragInProgress) || (this.mSuppressFolderDeletion)) {
        break label89;
      }
      replaceFolderWithFinalItem();
    }
    for (;;)
    {
      this.mSuppressFolderDeletion = false;
      return;
      label89:
      if (this.mDragInProgress) {
        this.mDeleteFolderOnDropCompleted = true;
      }
    }
  }
  
  private void placeInReadingOrder(ArrayList<ShortcutInfo> paramArrayList)
  {
    int i = 0;
    int j = paramArrayList.size();
    for (int k = 0; k < j; k++)
    {
      ShortcutInfo localShortcutInfo2 = (ShortcutInfo)paramArrayList.get(k);
      if (localShortcutInfo2.cellX > i) {
        i = localShortcutInfo2.cellX;
      }
    }
    Collections.sort(paramArrayList, new GridComparator(i + 1));
    int m = this.mContent.getCountX();
    for (int n = 0; n < j; n++)
    {
      int i1 = n % m;
      int i2 = n / m;
      ShortcutInfo localShortcutInfo1 = (ShortcutInfo)paramArrayList.get(n);
      localShortcutInfo1.cellX = i1;
      localShortcutInfo1.cellY = i2;
    }
  }
  
  private void positionAndSizeAsIcon()
  {
    if (!(getParent() instanceof DragLayer)) {
      return;
    }
    setScaleX(0.8F);
    setScaleY(0.8F);
    setAlpha(0.0F);
    this.mState = 0;
  }
  
  private void realTimeReorder(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = 0;
    float f = 30.0F;
    if (readingOrderGreaterThan(paramArrayOfInt2, paramArrayOfInt1))
    {
      int i6;
      int i7;
      if (paramArrayOfInt1[0] >= -1 + this.mContent.getCountX())
      {
        i6 = 1;
        if (i6 == 0) {
          break label191;
        }
        i7 = 1 + paramArrayOfInt1[1];
      }
      label46:
      for (int i8 = i7;; i8++)
      {
        int i9 = paramArrayOfInt2[1];
        if (i8 > i9) {
          return;
        }
        int i10 = paramArrayOfInt1[1];
        int i11;
        if (i8 == i10)
        {
          i11 = 1 + paramArrayOfInt1[0];
          label81:
          int i12 = paramArrayOfInt2[1];
          if (i8 >= i12) {
            break label205;
          }
        }
        label191:
        label205:
        for (int i13 = -1 + this.mContent.getCountX();; i13 = paramArrayOfInt2[0])
        {
          for (int i14 = i11; i14 <= i13; i14++)
          {
            View localView2 = this.mContent.getChildAt(i14, i8);
            if (this.mContent.animateChildToPosition(localView2, paramArrayOfInt1[0], paramArrayOfInt1[1], 230, i, true, true))
            {
              paramArrayOfInt1[0] = i14;
              paramArrayOfInt1[1] = i8;
              i = (int)(f + i);
              f = (float)(0.9D * f);
            }
          }
          i6 = 0;
          break;
          i7 = paramArrayOfInt1[1];
          break label46;
          i11 = 0;
          break label81;
        }
      }
    }
    int j;
    int k;
    if (paramArrayOfInt1[0] == 0)
    {
      j = 1;
      if (j == 0) {
        break label377;
      }
      k = -1 + paramArrayOfInt1[1];
    }
    label240:
    for (int m = k;; m--)
    {
      int n = paramArrayOfInt2[1];
      if (m < n) {
        return;
      }
      int i1 = paramArrayOfInt1[1];
      int i2;
      if (m == i1)
      {
        i2 = -1 + paramArrayOfInt1[0];
        int i3 = paramArrayOfInt2[1];
        if (m <= i3) {
          break label399;
        }
      }
      for (int i4 = 0;; i4 = paramArrayOfInt2[0])
      {
        for (int i5 = i2; i5 >= i4; i5--)
        {
          View localView1 = this.mContent.getChildAt(i5, m);
          if (this.mContent.animateChildToPosition(localView1, paramArrayOfInt1[0], paramArrayOfInt1[1], 230, i, true, true))
          {
            paramArrayOfInt1[0] = i5;
            paramArrayOfInt1[1] = m;
            i = (int)(f + i);
            f = (float)(0.9D * f);
          }
        }
        j = 0;
        break;
        label377:
        k = paramArrayOfInt1[1];
        break label240;
        i2 = -1 + this.mContent.getCountX();
        break label275;
      }
    }
    label275:
  }
  
  private void replaceFolderWithFinalItem()
  {
    Runnable local7 = new Runnable()
    {
      public void run()
      {
        CellLayout localCellLayout = Folder.this.mLauncher.getCellLayout(Folder.this.mInfo.container, Folder.this.mInfo.screenId);
        int i = Folder.this.getItemCount();
        Object localObject = null;
        if (i == 1)
        {
          ShortcutInfo localShortcutInfo = (ShortcutInfo)Folder.this.mInfo.contents.get(0);
          View localView = Folder.this.mLauncher.createShortcut(2130968595, localCellLayout, localShortcutInfo);
          LauncherModel.addOrMoveItemInDatabase(Folder.this.mLauncher, localShortcutInfo, Folder.this.mInfo.container, Folder.this.mInfo.screenId, Folder.this.mInfo.cellX, Folder.this.mInfo.cellY);
          localObject = localView;
        }
        if (Folder.this.getItemCount() <= 1)
        {
          LauncherModel.deleteItemFromDatabase(Folder.this.mLauncher, Folder.this.mInfo);
          localCellLayout.removeView(Folder.this.mFolderIcon);
          if ((Folder.this.mFolderIcon instanceof DropTarget)) {
            Folder.this.mDragController.removeDropTarget((DropTarget)Folder.this.mFolderIcon);
          }
          Folder.this.mLauncher.removeFolder(Folder.this.mInfo);
        }
        if (localObject != null) {
          Folder.this.mLauncher.getWorkspace().addInScreenFromBind(localObject, Folder.this.mInfo.container, Folder.this.mInfo.screenId, Folder.this.mInfo.cellX, Folder.this.mInfo.cellY, Folder.this.mInfo.spanX, Folder.this.mInfo.spanY);
        }
      }
    };
    View localView = getItemAt(0);
    if (localView != null) {
      this.mFolderIcon.performDestroyAnimation(localView, local7);
    }
    this.mDestroyed = true;
  }
  
  private void sendCustomAccessibilityEvent(int paramInt, String paramString)
  {
    AccessibilityManager localAccessibilityManager = (AccessibilityManager)getContext().getSystemService("accessibility");
    if (localAccessibilityManager.isEnabled())
    {
      AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(paramInt);
      onInitializeAccessibilityEvent(localAccessibilityEvent);
      localAccessibilityEvent.getText().add(paramString);
      localAccessibilityManager.sendAccessibilityEvent(localAccessibilityEvent);
    }
  }
  
  private void setFocusOnFirstChild()
  {
    View localView = this.mContent.getChildAt(0, 0);
    if (localView != null) {
      localView.requestFocus();
    }
  }
  
  private void setupContentDimensions(int paramInt)
  {
    ArrayList localArrayList = getItemsInReadingOrder();
    int i = this.mContent.getCountX();
    int j = this.mContent.getCountY();
    int k = 0;
    if (k == 0)
    {
      int m = i;
      int n = j;
      if (i * j < paramInt) {
        if (((i <= j) || (j == this.mMaxCountY)) && (i < this.mMaxCountX)) {
          i++;
        }
      }
      label167:
      for (;;)
      {
        label71:
        if (j == 0) {
          j++;
        }
        label79:
        if ((i == m) && (j == n)) {}
        for (k = 1;; k = 0)
        {
          break;
          if (j >= this.mMaxCountY) {
            break label167;
          }
          j++;
          break label71;
          if ((i * (j - 1) >= paramInt) && (j >= i))
          {
            j = Math.max(0, j - 1);
            break label79;
          }
          if (j * (i - 1) < paramInt) {
            break label79;
          }
          i = Math.max(0, i - 1);
          break label79;
        }
      }
    }
    this.mContent.setGridSize(i, j);
    arrangeChildren(localArrayList);
  }
  
  private void setupContentForNumItems(int paramInt)
  {
    setupContentDimensions(paramInt);
    if ((DragLayer.LayoutParams)getLayoutParams() == null)
    {
      DragLayer.LayoutParams localLayoutParams = new DragLayer.LayoutParams(0, 0);
      localLayoutParams.customPosition = true;
      setLayoutParams(localLayoutParams);
    }
    centerAboutIcon();
  }
  
  private void updateItemLocationsInDatabase()
  {
    ArrayList localArrayList = getItemsInReadingOrder();
    for (int i = 0; i < localArrayList.size(); i++)
    {
      ItemInfo localItemInfo = (ItemInfo)((View)localArrayList.get(i)).getTag();
      LauncherModel.moveItemInDatabase(this.mLauncher, localItemInfo, this.mInfo.id, 0L, localItemInfo.cellX, localItemInfo.cellY);
    }
  }
  
  private void updateItemLocationsInDatabaseBatch()
  {
    ArrayList localArrayList1 = getItemsInReadingOrder();
    ArrayList localArrayList2 = new ArrayList();
    for (int i = 0; i < localArrayList1.size(); i++) {
      localArrayList2.add((ItemInfo)((View)localArrayList1.get(i)).getTag());
    }
    LauncherModel.moveItemsInDatabase(this.mLauncher, localArrayList2, this.mInfo.id, 0);
  }
  
  private void updateTextViewFocus()
  {
    View localView = getItemAt(-1 + getItemCount());
    getItemAt(-1 + getItemCount());
    if (localView != null)
    {
      this.mFolderName.setNextFocusDownId(localView.getId());
      this.mFolderName.setNextFocusRightId(localView.getId());
      this.mFolderName.setNextFocusLeftId(localView.getId());
      this.mFolderName.setNextFocusUpId(localView.getId());
    }
  }
  
  public boolean acceptDrop(DropTarget.DragObject paramDragObject)
  {
    int i = ((ItemInfo)paramDragObject.dragInfo).itemType;
    return ((i == 0) || (i == 1)) && (!isFull());
  }
  
  public void animateClosed()
  {
    if (!(getParent() instanceof DragLayer)) {
      return;
    }
    ObjectAnimator localObjectAnimator = LauncherAnimUtils.ofPropertyValuesHolder(this, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { 0.0F }), PropertyValuesHolder.ofFloat("scaleX", new float[] { 0.9F }), PropertyValuesHolder.ofFloat("scaleY", new float[] { 0.9F }) });
    localObjectAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        Folder.this.onCloseComplete();
        Folder.this.setLayerType(0, null);
        Folder.access$102(Folder.this, 0);
      }
      
      public void onAnimationStart(Animator paramAnonymousAnimator)
      {
        Folder.this.sendCustomAccessibilityEvent(32, Folder.this.getContext().getString(2131361949));
        Folder.access$102(Folder.this, 1);
      }
    });
    localObjectAnimator.setDuration(this.mExpandDuration);
    setLayerType(2, null);
    localObjectAnimator.start();
  }
  
  public void animateOpen()
  {
    positionAndSizeAsIcon();
    if (!(getParent() instanceof DragLayer)) {
      return;
    }
    centerAboutIcon();
    ObjectAnimator localObjectAnimator = LauncherAnimUtils.ofPropertyValuesHolder(this, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { 1.0F }), PropertyValuesHolder.ofFloat("scaleX", new float[] { 1.0F }), PropertyValuesHolder.ofFloat("scaleY", new float[] { 1.0F }) });
    localObjectAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        Folder.access$102(Folder.this, 2);
        Folder.this.setLayerType(0, null);
        Cling localCling = Folder.this.mLauncher.showFirstRunFoldersCling();
        if (localCling != null)
        {
          localCling.bringScrimToFront();
          Folder.this.bringToFront();
          localCling.bringToFront();
        }
        Folder.this.setFocusOnFirstChild();
      }
      
      public void onAnimationStart(Animator paramAnonymousAnimator)
      {
        Folder localFolder = Folder.this;
        String str = Folder.this.getContext().getString(2131361946);
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(Folder.this.mContent.getCountX());
        arrayOfObject[1] = Integer.valueOf(Folder.this.mContent.getCountY());
        localFolder.sendCustomAccessibilityEvent(32, String.format(str, arrayOfObject));
        Folder.access$102(Folder.this, 1);
      }
    });
    localObjectAnimator.setDuration(this.mExpandDuration);
    setLayerType(2, null);
    localObjectAnimator.start();
  }
  
  void bind(FolderInfo paramFolderInfo)
  {
    this.mInfo = paramFolderInfo;
    ArrayList localArrayList1 = paramFolderInfo.contents;
    ArrayList localArrayList2 = new ArrayList();
    setupContentForNumItems(localArrayList1.size());
    placeInReadingOrder(localArrayList1);
    int i = 0;
    int j = 0;
    if (j < localArrayList1.size())
    {
      ShortcutInfo localShortcutInfo2 = (ShortcutInfo)localArrayList1.get(j);
      if (!createAndAddShortcut(localShortcutInfo2)) {
        localArrayList2.add(localShortcutInfo2);
      }
      for (;;)
      {
        j++;
        break;
        i++;
      }
    }
    setupContentForNumItems(i);
    Iterator localIterator = localArrayList2.iterator();
    while (localIterator.hasNext())
    {
      ShortcutInfo localShortcutInfo1 = (ShortcutInfo)localIterator.next();
      this.mInfo.remove(localShortcutInfo1);
      LauncherModel.deleteItemFromDatabase(this.mLauncher, localShortcutInfo1);
    }
    this.mItemsInvalidated = true;
    updateTextViewFocus();
    this.mInfo.addListener(this);
    if (!sDefaultFolderName.contentEquals(this.mInfo.title)) {
      this.mFolderName.setText(this.mInfo.title);
    }
    for (;;)
    {
      updateItemLocationsInDatabase();
      return;
      this.mFolderName.setText("");
    }
  }
  
  public void completeDragExit()
  {
    this.mLauncher.closeFolder();
    this.mCurrentDragInfo = null;
    this.mCurrentDragView = null;
    this.mSuppressOnAdd = false;
    this.mRearrangeOnClose = true;
  }
  
  protected boolean createAndAddShortcut(ShortcutInfo paramShortcutInfo)
  {
    BubbleTextView localBubbleTextView = (BubbleTextView)this.mInflater.inflate(2130968595, this, false);
    localBubbleTextView.setCompoundDrawables(null, Utilities.createIconDrawable(paramShortcutInfo.getIcon(this.mIconCache)), null, null);
    localBubbleTextView.setText(paramShortcutInfo.title);
    localBubbleTextView.setTag(paramShortcutInfo);
    localBubbleTextView.setTextColor(getResources().getColor(2131230767));
    localBubbleTextView.setShadowsEnabled(false);
    localBubbleTextView.setGlowColor(getResources().getColor(2131230768));
    localBubbleTextView.setOnClickListener(this);
    localBubbleTextView.setOnLongClickListener(this);
    if ((this.mContent.getChildAt(paramShortcutInfo.cellX, paramShortcutInfo.cellY) != null) || (paramShortcutInfo.cellX < 0) || (paramShortcutInfo.cellY < 0) || (paramShortcutInfo.cellX >= this.mContent.getCountX()) || (paramShortcutInfo.cellY >= this.mContent.getCountY()))
    {
      Log.e("Launcher.Folder", "Folder order not properly persisted during bind");
      if (!findAndSetEmptyCells(paramShortcutInfo)) {
        return false;
      }
    }
    CellLayout.LayoutParams localLayoutParams = new CellLayout.LayoutParams(paramShortcutInfo.cellX, paramShortcutInfo.cellY, paramShortcutInfo.spanX, paramShortcutInfo.spanY);
    localBubbleTextView.setOnKeyListener(new FolderKeyEventListener());
    CellLayout localCellLayout = this.mContent;
    int i = 0;
    if (0 != 0) {}
    for (;;)
    {
      localCellLayout.addViewToCellLayout(localBubbleTextView, i, (int)paramShortcutInfo.id, localLayoutParams, true);
      return true;
      i = -1;
    }
  }
  
  public void deferCompleteDropAfterUninstallActivity()
  {
    this.mDeferDropAfterUninstall = true;
  }
  
  public void dismissEditingName()
  {
    this.mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
    doneEditingFolderName(true);
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    return true;
  }
  
  public void doneEditingFolderName(boolean paramBoolean)
  {
    this.mFolderName.setHint(sHintText);
    String str = this.mFolderName.getText().toString();
    this.mInfo.setTitle(str);
    LauncherModel.updateItemInDatabase(this.mLauncher, this.mInfo);
    if (paramBoolean) {
      sendCustomAccessibilityEvent(32, String.format(getContext().getString(2131361950), new Object[] { str }));
    }
    requestFocus();
    Selection.setSelection(this.mFolderName.getText(), 0, 0);
    this.mIsEditingName = false;
  }
  
  protected boolean findAndSetEmptyCells(ShortcutInfo paramShortcutInfo)
  {
    int[] arrayOfInt = new int[2];
    if (this.mContent.findCellForSpan(arrayOfInt, paramShortcutInfo.spanX, paramShortcutInfo.spanY))
    {
      paramShortcutInfo.cellX = arrayOfInt[0];
      paramShortcutInfo.cellY = arrayOfInt[1];
      return true;
    }
    return false;
  }
  
  public View getEditTextRegion()
  {
    return this.mFolderName;
  }
  
  public void getHitRectRelativeToDragLayer(Rect paramRect)
  {
    getHitRect(paramRect);
  }
  
  FolderInfo getInfo()
  {
    return this.mInfo;
  }
  
  public View getItemAt(int paramInt)
  {
    return this.mContent.getShortcutsAndWidgets().getChildAt(paramInt);
  }
  
  public int getItemCount()
  {
    return this.mContent.getShortcutsAndWidgets().getChildCount();
  }
  
  public ArrayList<View> getItemsInReadingOrder()
  {
    if (this.mItemsInvalidated)
    {
      this.mItemsInReadingOrder.clear();
      for (int i = 0; i < this.mContent.getCountY(); i++) {
        for (int j = 0; j < this.mContent.getCountX(); j++)
        {
          View localView = this.mContent.getChildAt(j, i);
          if (localView != null) {
            this.mItemsInReadingOrder.add(localView);
          }
        }
      }
      this.mItemsInvalidated = false;
    }
    return this.mItemsInReadingOrder;
  }
  
  float getPivotXForIconAnimation()
  {
    return this.mFolderIconPivotX;
  }
  
  float getPivotYForIconAnimation()
  {
    return this.mFolderIconPivotY;
  }
  
  public void hideItem(ShortcutInfo paramShortcutInfo)
  {
    getViewForInfo(paramShortcutInfo).setVisibility(4);
  }
  
  boolean isDestroyed()
  {
    return this.mDestroyed;
  }
  
  public boolean isDropEnabled()
  {
    return true;
  }
  
  public boolean isEditingName()
  {
    return this.mIsEditingName;
  }
  
  public boolean isFull()
  {
    return getItemCount() >= this.mMaxNumItems;
  }
  
  public boolean isLayoutRtl()
  {
    return getLayoutDirection() == 1;
  }
  
  public void notifyDrop()
  {
    if (this.mDragInProgress) {
      this.mItemAddedBackToSelfViaIcon = true;
    }
  }
  
  public void onAdd(ShortcutInfo paramShortcutInfo)
  {
    this.mItemsInvalidated = true;
    if (this.mSuppressOnAdd) {
      return;
    }
    if (!findAndSetEmptyCells(paramShortcutInfo))
    {
      setupContentForNumItems(1 + getItemCount());
      findAndSetEmptyCells(paramShortcutInfo);
    }
    createAndAddShortcut(paramShortcutInfo);
    LauncherModel.addOrMoveItemInDatabase(this.mLauncher, paramShortcutInfo, this.mInfo.id, 0L, paramShortcutInfo.cellX, paramShortcutInfo.cellY);
  }
  
  public void onClick(View paramView)
  {
    if ((paramView.getTag() instanceof ShortcutInfo)) {
      this.mLauncher.onClick(paramView);
    }
  }
  
  public void onDragEnter(DropTarget.DragObject paramDragObject)
  {
    this.mPreviousTargetCell[0] = -1;
    this.mPreviousTargetCell[1] = -1;
    this.mOnExitAlarm.cancelAlarm();
  }
  
  public void onDragExit(DropTarget.DragObject paramDragObject)
  {
    this.mAutoScrollHelper.setEnabled(false);
    if (!paramDragObject.dragComplete)
    {
      this.mOnExitAlarm.setOnAlarmListener(this.mOnExitAlarmListener);
      this.mOnExitAlarm.setAlarm(800L);
    }
    this.mReorderAlarm.cancelAlarm();
    this.mDragMode = this.DRAG_MODE_NONE;
  }
  
  public void onDragOver(DropTarget.DragObject paramDragObject)
  {
    DragView localDragView = paramDragObject.dragView;
    int i = this.mScrollView.getScrollY();
    float[] arrayOfFloat = getDragViewVisualCenter(paramDragObject.x, paramDragObject.y, paramDragObject.xOffset, paramDragObject.yOffset, localDragView, null);
    arrayOfFloat[0] -= getPaddingLeft();
    arrayOfFloat[1] -= getPaddingTop();
    long l = SystemClock.uptimeMillis();
    MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 2, paramDragObject.x, paramDragObject.y, 0);
    if (!this.mAutoScrollHelper.isEnabled()) {
      this.mAutoScrollHelper.setEnabled(true);
    }
    boolean bool = this.mAutoScrollHelper.onTouch(this, localMotionEvent);
    localMotionEvent.recycle();
    if (bool)
    {
      this.mReorderAlarm.cancelAlarm();
      return;
    }
    this.mTargetCell = this.mContent.findNearestArea((int)arrayOfFloat[0], i + (int)arrayOfFloat[1], 1, 1, this.mTargetCell);
    if (isLayoutRtl()) {
      this.mTargetCell[0] = (-1 + (this.mContent.getCountX() - this.mTargetCell[0]));
    }
    if ((this.mTargetCell[0] != this.mPreviousTargetCell[0]) || (this.mTargetCell[1] != this.mPreviousTargetCell[1]))
    {
      this.mReorderAlarm.cancelAlarm();
      this.mReorderAlarm.setOnAlarmListener(this.mReorderAlarmListener);
      this.mReorderAlarm.setAlarm(250L);
      this.mPreviousTargetCell[0] = this.mTargetCell[0];
      this.mPreviousTargetCell[1] = this.mTargetCell[1];
      this.mDragMode = this.DRAG_MODE_REORDER;
      return;
    }
    this.mDragMode = this.DRAG_MODE_NONE;
  }
  
  public void onDrop(DropTarget.DragObject paramDragObject)
  {
    ShortcutInfo localShortcutInfo1;
    if ((paramDragObject.dragInfo instanceof AppInfo))
    {
      localShortcutInfo1 = ((AppInfo)paramDragObject.dragInfo).makeShortcut();
      localShortcutInfo1.spanX = 1;
      localShortcutInfo1.spanY = 1;
      if (localShortcutInfo1 == this.mCurrentDragInfo)
      {
        ShortcutInfo localShortcutInfo2 = (ShortcutInfo)this.mCurrentDragView.getTag();
        CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)this.mCurrentDragView.getLayoutParams();
        int i = this.mEmptyCell[0];
        localLayoutParams.cellX = i;
        localShortcutInfo2.cellX = i;
        int j = this.mEmptyCell[1];
        localLayoutParams.cellY = j;
        localShortcutInfo2.cellX = j;
        this.mContent.addViewToCellLayout(this.mCurrentDragView, -1, (int)localShortcutInfo1.id, localLayoutParams, true);
        if (!paramDragObject.dragView.hasDrawn()) {
          break label191;
        }
        this.mLauncher.getDragLayer().animateViewIntoPosition(paramDragObject.dragView, this.mCurrentDragView);
      }
    }
    for (;;)
    {
      this.mItemsInvalidated = true;
      setupContentDimensions(getItemCount());
      this.mSuppressOnAdd = true;
      this.mInfo.add(localShortcutInfo1);
      return;
      localShortcutInfo1 = (ShortcutInfo)paramDragObject.dragInfo;
      break;
      label191:
      paramDragObject.deferDragViewCleanupPostAnimation = false;
      this.mCurrentDragView.setVisibility(0);
    }
  }
  
  public void onDropCompleted(final View paramView, final DropTarget.DragObject paramDragObject, final boolean paramBoolean1, final boolean paramBoolean2)
  {
    if (this.mDeferDropAfterUninstall)
    {
      Log.d("Launcher.Folder", "Deferred handling drop because waiting for uninstall.");
      this.mDeferredAction = new Runnable()
      {
        public void run()
        {
          Folder.this.onDropCompleted(paramView, paramDragObject, paramBoolean1, paramBoolean2);
          Folder.access$702(Folder.this, null);
        }
      };
      return;
    }
    int i;
    int j;
    if (this.mDeferredAction != null)
    {
      i = 1;
      if ((!paramBoolean2) || ((i != 0) && (!this.mUninstallSuccessful))) {
        break label165;
      }
      j = 1;
      label65:
      if (j == 0) {
        break label171;
      }
      if ((this.mDeleteFolderOnDropCompleted) && (!this.mItemAddedBackToSelfViaIcon)) {
        replaceFolderWithFinalItem();
      }
    }
    for (;;)
    {
      if ((paramView != this) && (this.mOnExitAlarm.alarmPending()))
      {
        this.mOnExitAlarm.cancelAlarm();
        if (j == 0) {
          this.mSuppressFolderDeletion = true;
        }
        completeDragExit();
      }
      this.mDeleteFolderOnDropCompleted = false;
      this.mDragInProgress = false;
      this.mItemAddedBackToSelfViaIcon = false;
      this.mCurrentDragInfo = null;
      this.mCurrentDragView = null;
      this.mSuppressOnAdd = false;
      updateItemLocationsInDatabaseBatch();
      return;
      i = 0;
      break;
      label165:
      j = 0;
      break label65;
      label171:
      setupContentForNumItems(getItemCount());
      this.mFolderIcon.onDrop(paramDragObject);
    }
  }
  
  public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 6)
    {
      dismissEditingName();
      return true;
    }
    return false;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mScrollView = ((ScrollView)findViewById(2131296980));
    this.mContent = ((CellLayout)findViewById(2131297191));
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    this.mContent.setCellDimensions(localDeviceProfile.folderCellWidthPx, localDeviceProfile.folderCellHeightPx);
    this.mContent.setGridSize(0, 0);
    this.mContent.getShortcutsAndWidgets().setMotionEventSplittingEnabled(false);
    this.mContent.setInvertIfRtl(true);
    this.mFolderName = ((FolderEditText)findViewById(2131296948));
    this.mFolderName.setFolder(this);
    this.mFolderName.setOnFocusChangeListener(this);
    this.mFolderName.measure(0, 0);
    this.mFolderNameHeight = this.mFolderName.getMeasuredHeight();
    this.mFolderName.setCustomSelectionActionModeCallback(this.mActionModeCallback);
    this.mFolderName.setOnEditorActionListener(this);
    this.mFolderName.setSelectAllOnFocus(true);
    this.mFolderName.setInputType(0x2000 | 0x80000 | this.mFolderName.getInputType());
    this.mAutoScrollHelper = new FolderAutoScrollHelper(this.mScrollView);
  }
  
  public void onFlingToDelete(DropTarget.DragObject paramDragObject, int paramInt1, int paramInt2, PointF paramPointF) {}
  
  public void onFlingToDeleteCompleted() {}
  
  public void onFocusChange(View paramView, boolean paramBoolean)
  {
    if ((paramView == this.mFolderName) && (paramBoolean)) {
      startEditingFolderName();
    }
  }
  
  public void onItemsChanged()
  {
    updateTextViewFocus();
  }
  
  public boolean onLongClick(View paramView)
  {
    if (!this.mLauncher.isDraggingEnabled()) {
      return true;
    }
    Object localObject = paramView.getTag();
    if ((localObject instanceof ShortcutInfo))
    {
      ShortcutInfo localShortcutInfo = (ShortcutInfo)localObject;
      if (!paramView.isInTouchMode()) {
        return false;
      }
      this.mLauncher.dismissFolderCling(null);
      this.mLauncher.getWorkspace().onDragStartedWithItem(paramView);
      this.mLauncher.getWorkspace().beginDragShared(paramView, this);
      this.mIconDrawable = ((TextView)paramView).getCompoundDrawables()[1];
      this.mCurrentDragInfo = localShortcutInfo;
      this.mEmptyCell[0] = localShortcutInfo.cellX;
      this.mEmptyCell[1] = localShortcutInfo.cellY;
      this.mCurrentDragView = paramView;
      this.mContent.removeView(this.mCurrentDragView);
      this.mInfo.remove(this.mCurrentDragInfo);
      this.mDragInProgress = true;
      this.mItemAddedBackToSelfViaIcon = false;
    }
    return true;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getPaddingLeft() + getPaddingRight() + this.mContent.getDesiredWidth();
    int j = getFolderHeight();
    int k = View.MeasureSpec.makeMeasureSpec(this.mContent.getDesiredWidth(), 1073741824);
    int m = View.MeasureSpec.makeMeasureSpec(getContentAreaHeight(), 1073741824);
    this.mContent.setFixedSize(this.mContent.getDesiredWidth(), this.mContent.getDesiredHeight());
    this.mScrollView.measure(k, m);
    this.mFolderName.measure(k, View.MeasureSpec.makeMeasureSpec(this.mFolderNameHeight, 1073741824));
    setMeasuredDimension(i, j);
  }
  
  public void onRemove(ShortcutInfo paramShortcutInfo)
  {
    this.mItemsInvalidated = true;
    if (paramShortcutInfo == this.mCurrentDragInfo) {}
    for (;;)
    {
      return;
      View localView = getViewForInfo(paramShortcutInfo);
      this.mContent.removeView(localView);
      if (this.mState == 1) {
        this.mRearrangeOnClose = true;
      }
      while (getItemCount() <= 1)
      {
        replaceFolderWithFinalItem();
        return;
        setupContentForNumItems(getItemCount());
      }
    }
  }
  
  public void onTitleChanged(CharSequence paramCharSequence) {}
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }
  
  public void onUninstallActivityReturned(boolean paramBoolean)
  {
    this.mDeferDropAfterUninstall = false;
    this.mUninstallSuccessful = paramBoolean;
    if (this.mDeferredAction != null) {
      this.mDeferredAction.run();
    }
  }
  
  boolean readingOrderGreaterThan(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    boolean bool;
    if (paramArrayOfInt1[1] <= paramArrayOfInt2[1])
    {
      int i = paramArrayOfInt1[1];
      int j = paramArrayOfInt2[1];
      bool = false;
      if (i == j)
      {
        int k = paramArrayOfInt1[0];
        int m = paramArrayOfInt2[0];
        bool = false;
        if (k <= m) {}
      }
    }
    else
    {
      bool = true;
    }
    return bool;
  }
  
  public void setDragController(DragController paramDragController)
  {
    this.mDragController = paramDragController;
  }
  
  void setFolderIcon(FolderIcon paramFolderIcon)
  {
    this.mFolderIcon = paramFolderIcon;
  }
  
  public void showItem(ShortcutInfo paramShortcutInfo)
  {
    getViewForInfo(paramShortcutInfo).setVisibility(0);
  }
  
  public void startEditingFolderName()
  {
    this.mFolderName.setHint("");
    this.mIsEditingName = true;
  }
  
  public boolean supportsFlingToDelete()
  {
    return true;
  }
  
  private class GridComparator
    implements Comparator<ShortcutInfo>
  {
    int mNumCols;
    
    public GridComparator(int paramInt)
    {
      this.mNumCols = paramInt;
    }
    
    public int compare(ShortcutInfo paramShortcutInfo1, ShortcutInfo paramShortcutInfo2)
    {
      return paramShortcutInfo1.cellY * this.mNumCols + paramShortcutInfo1.cellX - (paramShortcutInfo2.cellY * this.mNumCols + paramShortcutInfo2.cellX);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.Folder
 * JD-Core Version:    0.7.0.1
 */