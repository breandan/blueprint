package com.android.launcher3;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FocusHelper
{
  private static View findIndexOfIcon(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    int i = paramArrayList.size();
    int j = paramInt1 + paramInt2;
    while ((j >= 0) && (j < i))
    {
      View localView = (View)paramArrayList.get(j);
      if (((localView instanceof BubbleTextView)) || ((localView instanceof FolderIcon))) {
        return localView;
      }
      j += paramInt2;
    }
    return null;
  }
  
  private static TabHost findTabHostParent(View paramView)
  {
    for (ViewParent localViewParent = paramView.getParent(); (localViewParent != null) && (!(localViewParent instanceof TabHost)); localViewParent = localViewParent.getParent()) {}
    return (TabHost)localViewParent;
  }
  
  private static ViewGroup getAppsCustomizePage(ViewGroup paramViewGroup, int paramInt)
  {
    Object localObject = (ViewGroup)((PagedView)paramViewGroup).getPageAt(paramInt);
    if ((localObject instanceof CellLayout)) {
      localObject = ((CellLayout)localObject).getShortcutsAndWidgets();
    }
    return localObject;
  }
  
  private static ShortcutAndWidgetContainer getCellLayoutChildrenForIndex(ViewGroup paramViewGroup, int paramInt)
  {
    return (ShortcutAndWidgetContainer)((ViewGroup)paramViewGroup.getChildAt(paramInt)).getChildAt(0);
  }
  
  private static ArrayList<View> getCellLayoutChildrenSortedSpatially(CellLayout paramCellLayout, ViewGroup paramViewGroup)
  {
    int i = paramCellLayout.getCountX();
    int j = paramViewGroup.getChildCount();
    ArrayList localArrayList = new ArrayList();
    for (int k = 0; k < j; k++) {
      localArrayList.add(paramViewGroup.getChildAt(k));
    }
    Collections.sort(localArrayList, new Comparator()
    {
      public int compare(View paramAnonymousView1, View paramAnonymousView2)
      {
        CellLayout.LayoutParams localLayoutParams1 = (CellLayout.LayoutParams)paramAnonymousView1.getLayoutParams();
        CellLayout.LayoutParams localLayoutParams2 = (CellLayout.LayoutParams)paramAnonymousView2.getLayoutParams();
        return localLayoutParams1.cellY * this.val$cellCountX + localLayoutParams1.cellX - (localLayoutParams2.cellY * this.val$cellCountX + localLayoutParams2.cellX);
      }
    });
    return localArrayList;
  }
  
  private static View getClosestIconOnLine(CellLayout paramCellLayout, ViewGroup paramViewGroup, View paramView, int paramInt)
  {
    ArrayList localArrayList = getCellLayoutChildrenSortedSpatially(paramCellLayout, paramViewGroup);
    CellLayout.LayoutParams localLayoutParams1 = (CellLayout.LayoutParams)paramView.getLayoutParams();
    int i = paramCellLayout.getCountY();
    int j = localLayoutParams1.cellY;
    int k = j + paramInt;
    if ((k >= 0) && (k < i))
    {
      float f1 = 3.4028235E+38F;
      int m = -1;
      int n = localArrayList.indexOf(paramView);
      int i1;
      if (paramInt < 0) {
        i1 = -1;
      }
      while (n != i1)
      {
        View localView = (View)localArrayList.get(n);
        CellLayout.LayoutParams localLayoutParams2 = (CellLayout.LayoutParams)localView.getLayoutParams();
        int i2;
        if (paramInt < 0) {
          if (localLayoutParams2.cellY < j) {
            i2 = 1;
          }
        }
        for (;;)
        {
          if ((i2 != 0) && (((localView instanceof BubbleTextView)) || ((localView instanceof FolderIcon))))
          {
            float f2 = (float)Math.sqrt(Math.pow(localLayoutParams2.cellX - localLayoutParams1.cellX, 2.0D) + Math.pow(localLayoutParams2.cellY - localLayoutParams1.cellY, 2.0D));
            if (f2 < f1)
            {
              m = n;
              f1 = f2;
            }
          }
          if (n > i1) {
            break label246;
          }
          n++;
          break;
          i1 = localArrayList.size();
          break;
          i2 = 0;
          continue;
          if (localLayoutParams2.cellY > j) {
            i2 = 1;
          } else {
            i2 = 0;
          }
        }
        label246:
        n--;
      }
      if (m > -1) {
        return (View)localArrayList.get(m);
      }
    }
    return null;
  }
  
  private static View getIconInDirection(CellLayout paramCellLayout, ViewGroup paramViewGroup, int paramInt1, int paramInt2)
  {
    return findIndexOfIcon(getCellLayoutChildrenSortedSpatially(paramCellLayout, paramViewGroup), paramInt1, paramInt2);
  }
  
  private static View getIconInDirection(CellLayout paramCellLayout, ViewGroup paramViewGroup, View paramView, int paramInt)
  {
    ArrayList localArrayList = getCellLayoutChildrenSortedSpatially(paramCellLayout, paramViewGroup);
    return findIndexOfIcon(localArrayList, localArrayList.indexOf(paramView), paramInt);
  }
  
  static boolean handleAppsCustomizeKeyEvent(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    Object localObject;
    ViewGroup localViewGroup1;
    int i;
    int j;
    label46:
    PagedView localPagedView;
    TabWidget localTabWidget;
    int k;
    int m;
    int n;
    int i1;
    int i2;
    int i3;
    if ((paramView.getParent() instanceof ShortcutAndWidgetContainer))
    {
      localObject = (ViewGroup)paramView.getParent();
      localViewGroup1 = (ViewGroup)((ViewGroup)localObject).getParent();
      i = ((CellLayout)localViewGroup1).getCountX();
      j = ((CellLayout)localViewGroup1).getCountY();
      localPagedView = (PagedView)localViewGroup1.getParent();
      localTabWidget = findTabHostParent(localPagedView).getTabWidget();
      k = ((ViewGroup)localObject).indexOfChild(paramView);
      m = ((ViewGroup)localObject).getChildCount();
      n = localPagedView.indexToPage(localPagedView.indexOfChild(localViewGroup1));
      i1 = localPagedView.getChildCount();
      i2 = k % i;
      i3 = k / i;
      if (paramKeyEvent.getAction() == 1) {
        break label250;
      }
    }
    label250:
    for (int i4 = 1;; i4 = 0) {
      switch (paramInt)
      {
      default: 
        return false;
        localViewGroup1 = (ViewGroup)paramView.getParent();
        localObject = localViewGroup1;
        i = ((PagedViewGridLayout)localViewGroup1).getCellCountX();
        j = ((PagedViewGridLayout)localViewGroup1).getCellCountY();
        break label46;
      }
    }
    if (i4 != 0)
    {
      if (k <= 0) {
        break label281;
      }
      ((ViewGroup)localObject).getChildAt(k - 1).requestFocus();
    }
    for (;;)
    {
      return true;
      label281:
      if (n > 0)
      {
        ViewGroup localViewGroup5 = getAppsCustomizePage(localPagedView, n - 1);
        if (localViewGroup5 != null)
        {
          localPagedView.snapToPage(n - 1);
          View localView4 = localViewGroup5.getChildAt(-1 + localViewGroup5.getChildCount());
          if (localView4 != null) {
            localView4.requestFocus();
          }
        }
      }
    }
    if (i4 != 0)
    {
      if (k >= m - 1) {
        break label368;
      }
      ((ViewGroup)localObject).getChildAt(k + 1).requestFocus();
    }
    for (;;)
    {
      return true;
      label368:
      if (n < i1 - 1)
      {
        ViewGroup localViewGroup4 = getAppsCustomizePage(localPagedView, n + 1);
        if (localViewGroup4 != null)
        {
          localPagedView.snapToPage(n + 1);
          View localView3 = localViewGroup4.getChildAt(0);
          if (localView3 != null) {
            localView3.requestFocus();
          }
        }
      }
    }
    if (i4 != 0)
    {
      if (i3 <= 0) {
        break label455;
      }
      ((ViewGroup)localObject).getChildAt(i2 + i * (i3 - 1)).requestFocus();
    }
    for (;;)
    {
      return true;
      label455:
      localTabWidget.requestFocus();
    }
    if ((i4 != 0) && (i3 < j - 1)) {
      ((ViewGroup)localObject).getChildAt(Math.min(m - 1, i2 + i * (i3 + 1))).requestFocus();
    }
    return true;
    if (i4 != 0) {
      ((View.OnClickListener)localPagedView).onClick(paramView);
    }
    return true;
    if (i4 != 0)
    {
      if (n <= 0) {
        break label580;
      }
      ViewGroup localViewGroup3 = getAppsCustomizePage(localPagedView, n - 1);
      if (localViewGroup3 != null)
      {
        localPagedView.snapToPage(n - 1);
        View localView2 = localViewGroup3.getChildAt(0);
        if (localView2 != null) {
          localView2.requestFocus();
        }
      }
    }
    for (;;)
    {
      return true;
      label580:
      ((ViewGroup)localObject).getChildAt(0).requestFocus();
    }
    if (i4 != 0)
    {
      if (n >= i1 - 1) {
        break label653;
      }
      ViewGroup localViewGroup2 = getAppsCustomizePage(localPagedView, n + 1);
      if (localViewGroup2 != null)
      {
        localPagedView.snapToPage(n + 1);
        View localView1 = localViewGroup2.getChildAt(0);
        if (localView1 != null) {
          localView1.requestFocus();
        }
      }
    }
    for (;;)
    {
      return true;
      label653:
      ((ViewGroup)localObject).getChildAt(m - 1).requestFocus();
    }
    if (i4 != 0) {
      ((ViewGroup)localObject).getChildAt(0).requestFocus();
    }
    return true;
    if (i4 != 0) {
      ((ViewGroup)localObject).getChildAt(m - 1).requestFocus();
    }
    return true;
  }
  
  static boolean handleAppsCustomizeTabKeyEvent(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    int i = 1;
    TabHost localTabHost = findTabHostParent(paramView);
    FrameLayout localFrameLayout = localTabHost.getTabContentView();
    View localView = localTabHost.findViewById(2131296341);
    if (paramKeyEvent.getAction() != i) {
      switch (paramInt)
      {
      }
    }
    do
    {
      return false;
      i = 0;
      break;
      if ((i != 0) && (paramView != localView)) {
        localView.requestFocus();
      }
      return true;
    } while ((i == 0) || (paramView != localView));
    localFrameLayout.requestFocus();
    return true;
  }
  
  static boolean handleFolderKeyEvent(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    ShortcutAndWidgetContainer localShortcutAndWidgetContainer = (ShortcutAndWidgetContainer)paramView.getParent();
    CellLayout localCellLayout = (CellLayout)localShortcutAndWidgetContainer.getParent();
    FolderEditText localFolderEditText = ((Folder)((ScrollView)localCellLayout.getParent()).getParent()).mFolderName;
    if (paramKeyEvent.getAction() != 1) {}
    for (int i = 1;; i = 0) {
      switch (paramInt)
      {
      default: 
        return false;
      }
    }
    if (i != 0)
    {
      View localView6 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer, paramView, -1);
      if (localView6 != null) {
        localView6.requestFocus();
      }
    }
    return true;
    if (i != 0)
    {
      View localView5 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer, paramView, 1);
      if (localView5 == null) {
        break label172;
      }
      localView5.requestFocus();
    }
    for (;;)
    {
      return true;
      label172:
      localFolderEditText.requestFocus();
    }
    if (i != 0)
    {
      View localView4 = getClosestIconOnLine(localCellLayout, localShortcutAndWidgetContainer, paramView, -1);
      if (localView4 != null) {
        localView4.requestFocus();
      }
    }
    return true;
    if (i != 0)
    {
      View localView3 = getClosestIconOnLine(localCellLayout, localShortcutAndWidgetContainer, paramView, 1);
      if (localView3 == null) {
        break label237;
      }
      localView3.requestFocus();
    }
    for (;;)
    {
      return true;
      label237:
      localFolderEditText.requestFocus();
    }
    if (i != 0)
    {
      View localView2 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer, -1, 1);
      if (localView2 != null) {
        localView2.requestFocus();
      }
    }
    return true;
    if (i != 0)
    {
      View localView1 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer, localShortcutAndWidgetContainer.getChildCount(), -1);
      if (localView1 != null) {
        localView1.requestFocus();
      }
    }
    return true;
  }
  
  static boolean handleHotseatButtonKeyEvent(View paramView, int paramInt1, KeyEvent paramKeyEvent, int paramInt2)
  {
    ViewGroup localViewGroup = (ViewGroup)paramView.getParent();
    Workspace localWorkspace = (Workspace)((ViewGroup)localViewGroup.getParent()).findViewById(2131296746);
    int i = localViewGroup.indexOfChild(paramView);
    int j = localViewGroup.getChildCount();
    int k = localWorkspace.getCurrentPage();
    if (paramKeyEvent.getAction() != 1) {}
    for (int m = 1;; m = 0) {
      switch (paramInt1)
      {
      default: 
        return false;
      }
    }
    if (m != 0)
    {
      if (i <= 0) {
        break label125;
      }
      localViewGroup.getChildAt(i - 1).requestFocus();
    }
    for (;;)
    {
      return true;
      label125:
      localWorkspace.snapToPage(k - 1);
    }
    if (m != 0)
    {
      if (i >= j - 1) {
        break label166;
      }
      localViewGroup.getChildAt(i + 1).requestFocus();
    }
    for (;;)
    {
      return true;
      label166:
      localWorkspace.snapToPage(k + 1);
    }
    if (m != 0)
    {
      CellLayout localCellLayout = (CellLayout)localWorkspace.getChildAt(k);
      View localView = getIconInDirection(localCellLayout, localCellLayout.getShortcutsAndWidgets(), -1, 1);
      if (localView == null) {
        break label222;
      }
      localView.requestFocus();
    }
    for (;;)
    {
      return true;
      label222:
      localWorkspace.requestFocus();
    }
    return true;
  }
  
  static boolean handleIconKeyEvent(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    ShortcutAndWidgetContainer localShortcutAndWidgetContainer1 = (ShortcutAndWidgetContainer)paramView.getParent();
    CellLayout localCellLayout = (CellLayout)localShortcutAndWidgetContainer1.getParent();
    Workspace localWorkspace = (Workspace)localCellLayout.getParent();
    ViewGroup localViewGroup1 = (ViewGroup)localWorkspace.getParent();
    ViewGroup localViewGroup2 = (ViewGroup)localViewGroup1.findViewById(2131296748);
    ViewGroup localViewGroup3 = (ViewGroup)localViewGroup1.findViewById(2131296747);
    int i = localWorkspace.indexOfChild(localCellLayout);
    int j = localWorkspace.getChildCount();
    int k;
    if (paramKeyEvent.getAction() != 1) {
      k = 1;
    }
    switch (paramInt)
    {
    default: 
    case 21: 
    case 22: 
    case 19: 
    case 20: 
      do
      {
        do
        {
          do
          {
            return false;
            k = 0;
            break;
            if (k != 0)
            {
              View localView11 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer1, paramView, -1);
              if (localView11 == null) {
                break label200;
              }
              localView11.requestFocus();
            }
            for (;;)
            {
              return true;
              if (i > 0)
              {
                ShortcutAndWidgetContainer localShortcutAndWidgetContainer2 = getCellLayoutChildrenForIndex(localWorkspace, i - 1);
                View localView12 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer2, localShortcutAndWidgetContainer2.getChildCount(), -1);
                if (localView12 != null) {
                  localView12.requestFocus();
                } else {
                  localWorkspace.snapToPage(i - 1);
                }
              }
            }
            if (k != 0)
            {
              View localView9 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer1, paramView, 1);
              if (localView9 == null) {
                break label285;
              }
              localView9.requestFocus();
            }
            for (;;)
            {
              return true;
              if (i < j - 1)
              {
                View localView10 = getIconInDirection(localCellLayout, getCellLayoutChildrenForIndex(localWorkspace, i + 1), -1, 1);
                if (localView10 != null) {
                  localView10.requestFocus();
                } else {
                  localWorkspace.snapToPage(i + 1);
                }
              }
            }
          } while (k == 0);
          View localView8 = getClosestIconOnLine(localCellLayout, localShortcutAndWidgetContainer1, paramView, -1);
          if (localView8 != null)
          {
            localView8.requestFocus();
            return true;
          }
          localViewGroup2.requestFocus();
          return false;
        } while (k == 0);
        View localView7 = getClosestIconOnLine(localCellLayout, localShortcutAndWidgetContainer1, paramView, 1);
        if (localView7 != null)
        {
          localView7.requestFocus();
          return true;
        }
      } while (localViewGroup3 == null);
      localViewGroup3.requestFocus();
      return false;
    case 92: 
      if (k != 0)
      {
        if (i <= 0) {
          break label468;
        }
        View localView6 = getIconInDirection(localCellLayout, getCellLayoutChildrenForIndex(localWorkspace, i - 1), -1, 1);
        if (localView6 == null) {
          break label456;
        }
        localView6.requestFocus();
      }
      for (;;)
      {
        return true;
        localWorkspace.snapToPage(i - 1);
        continue;
        View localView5 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer1, -1, 1);
        if (localView5 != null) {
          localView5.requestFocus();
        }
      }
    case 93: 
      if (k != 0)
      {
        if (i >= j - 1) {
          break label549;
        }
        View localView4 = getIconInDirection(localCellLayout, getCellLayoutChildrenForIndex(localWorkspace, i + 1), -1, 1);
        if (localView4 == null) {
          break label537;
        }
        localView4.requestFocus();
      }
      for (;;)
      {
        return true;
        label537:
        localWorkspace.snapToPage(i + 1);
        continue;
        label549:
        View localView3 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer1, localShortcutAndWidgetContainer1.getChildCount(), -1);
        if (localView3 != null) {
          localView3.requestFocus();
        }
      }
    case 122: 
      label200:
      label468:
      if (k != 0)
      {
        View localView2 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer1, -1, 1);
        if (localView2 != null) {
          localView2.requestFocus();
        }
      }
      label285:
      label456:
      return true;
    }
    if (k != 0)
    {
      View localView1 = getIconInDirection(localCellLayout, localShortcutAndWidgetContainer1, localShortcutAndWidgetContainer1.getChildCount(), -1);
      if (localView1 != null) {
        localView1.requestFocus();
      }
    }
    return true;
  }
  
  static boolean handleTabKeyEvent(AccessibleTabView paramAccessibleTabView, int paramInt, KeyEvent paramKeyEvent)
  {
    int i = 1;
    if (!LauncherAppState.getInstance().isScreenLarge()) {
      return false;
    }
    FocusOnlyTabWidget localFocusOnlyTabWidget = (FocusOnlyTabWidget)paramAccessibleTabView.getParent();
    TabHost localTabHost = findTabHostParent(localFocusOnlyTabWidget);
    FrameLayout localFrameLayout = localTabHost.getTabContentView();
    int j = localFocusOnlyTabWidget.getTabCount();
    int k = localFocusOnlyTabWidget.getChildTabIndex(paramAccessibleTabView);
    if (paramKeyEvent.getAction() != i) {}
    for (;;)
    {
      switch (paramInt)
      {
      default: 
        return false;
      case 19: 
        return true;
        i = 0;
      }
    }
    if ((i != 0) && (k > 0)) {
      localFocusOnlyTabWidget.getChildTabViewAt(k - 1).requestFocus();
    }
    return true;
    if (i != 0)
    {
      if (k >= j - 1) {
        break label153;
      }
      localFocusOnlyTabWidget.getChildTabViewAt(k + 1).requestFocus();
    }
    for (;;)
    {
      return true;
      label153:
      if (paramAccessibleTabView.getNextFocusRightId() != -1) {
        localTabHost.findViewById(paramAccessibleTabView.getNextFocusRightId()).requestFocus();
      }
    }
    if (i != 0) {
      localFrameLayout.requestFocus();
    }
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.FocusHelper
 * JD-Core Version:    0.7.0.1
 */