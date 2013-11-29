package com.google.android.sidekick.shared.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.ui.UndoDismissToast;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class UndoDismissManager
{
  private static final String TAG = Tag.getTag(UndoDismissManager.class);
  private static final long UNDO_TOAST_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(5L);
  private final Runnable mAutoDismissTask = new Runnable()
  {
    public void run()
    {
      UndoDismissManager.this.dismissPending();
    }
  };
  private int mBottomInset = 0;
  private final Context mContext;
  private PopupWindow mPopup;
  private boolean mReceiversRegistered = false;
  private final Rect mRect = new Rect();
  private final ScheduledSingleThreadedExecutor mUiExecutor;
  private final int[] mXY = new int[2];
  
  public UndoDismissManager(Context paramContext, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    this.mContext = paramContext;
    this.mUiExecutor = paramScheduledSingleThreadedExecutor;
  }
  
  private PopupWindow createAndShowUndoToast(View paramView, final PendingViewDismiss paramPendingViewDismiss, @Nullable String paramString)
  {
    registerReceivers();
    UndoDismissToast localUndoDismissToast = (UndoDismissToast)((LayoutInflater)this.mContext.getSystemService("layout_inflater")).inflate(2130968904, null);
    final PopupWindow localPopupWindow = new PopupWindow(localUndoDismissToast, -1, -2, true);
    if (!Strings.isNullOrEmpty(paramString)) {
      ((TextView)localUndoDismissToast.findViewById(2131296678)).setText(paramString);
    }
    localUndoDismissToast.findViewById(2131297188).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramPendingViewDismiss.restore();
        localPopupWindow.dismiss();
      }
    });
    localPopupWindow.setAnimationStyle(16973828);
    localPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
    {
      public void onDismiss()
      {
        paramPendingViewDismiss.commit();
        UndoDismissManager.access$002(UndoDismissManager.this, null);
      }
    });
    localPopupWindow.showAtLocation(paramView, 81, 0, this.mContext.getResources().getDimensionPixelOffset(2131689885) + this.mBottomInset);
    WindowManager localWindowManager = (WindowManager)this.mContext.getSystemService("window");
    WindowManager.LayoutParams localLayoutParams = (WindowManager.LayoutParams)localUndoDismissToast.getLayoutParams();
    localLayoutParams.flags = (0x20 | localLayoutParams.flags);
    localLayoutParams.flags = (0x8 | localLayoutParams.flags);
    localLayoutParams.width = -2;
    localLayoutParams.height = -2;
    localWindowManager.updateViewLayout(localUndoDismissToast, localLayoutParams);
    return localPopupWindow;
  }
  
  private void registerReceivers()
  {
    if (this.mReceiversRegistered) {
      return;
    }
    this.mContext.registerReceiver(new BroadcastReceiver()new IntentFilter
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        UndoDismissManager.this.dismissPending();
      }
    }, new IntentFilter("com.google.android.apps.now.DEFERRED_ACTIONS_COMMITTED"));
    this.mReceiversRegistered = true;
  }
  
  public void dismissPending()
  {
    this.mUiExecutor.cancelExecute(this.mAutoDismissTask);
    if (this.mPopup != null) {}
    try
    {
      this.mPopup.dismiss();
      label27:
      this.mPopup = null;
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label27;
    }
  }
  
  public void handleTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mPopup == null) || (!this.mPopup.isShowing())) {}
    do
    {
      return;
      View localView = this.mPopup.getContentView();
      localView.getLocationOnScreen(this.mXY);
      this.mRect.set(this.mXY[0], this.mXY[1], this.mXY[0] + localView.getWidth(), this.mXY[1] + localView.getHeight());
    } while (this.mRect.contains((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY()));
    dismissPending();
  }
  
  public void setBottomInset(int paramInt)
  {
    this.mBottomInset = paramInt;
  }
  
  public void showUndoToast(PendingViewDismiss paramPendingViewDismiss, @Nullable Sidekick.Entry paramEntry)
  {
    if ((paramPendingViewDismiss.getDismissedViews().isEmpty()) || (paramPendingViewDismiss.isExpired())) {
      return;
    }
    View localView = ((View)paramPendingViewDismiss.getDismissedViews().get(0)).getRootView();
    if (localView.getWindowToken() == null)
    {
      Log.w(TAG, "View not attached");
      return;
    }
    String str = null;
    if (paramEntry != null)
    {
      Iterator localIterator = paramEntry.getEntryActionList().iterator();
      Sidekick.Action localAction;
      do
      {
        boolean bool1 = localIterator.hasNext();
        str = null;
        if (!bool1) {
          break;
        }
        localAction = (Sidekick.Action)localIterator.next();
      } while (localAction.getType() != 1);
      boolean bool2 = localAction.hasActionTakenMessage();
      str = null;
      if (bool2) {
        str = localAction.getActionTakenMessage();
      }
    }
    dismissPending();
    paramPendingViewDismiss.intercept();
    this.mPopup = createAndShowUndoToast(localView, paramPendingViewDismiss, str);
    this.mUiExecutor.executeDelayed(this.mAutoDismissTask, UNDO_TOAST_TIMEOUT_MS);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.UndoDismissManager
 * JD-Core Version:    0.7.0.1
 */