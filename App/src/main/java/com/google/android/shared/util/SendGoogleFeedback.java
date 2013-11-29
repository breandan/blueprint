package com.google.android.shared.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import com.google.android.shared.ui.CoScrollContainer;
import com.google.common.math.DoubleMath;
import java.math.RoundingMode;

public class SendGoogleFeedback
  extends Activity
{
  private static Bitmap getCurrentScreenshot(Context paramContext, View paramView)
  {
    Bitmap localBitmap;
    if ((Util.isLowRamDevice(paramContext)) || (!(paramView instanceof CoScrollContainer)))
    {
      paramView.buildDrawingCache();
      localBitmap = paramView.getDrawingCache();
    }
    for (int i = 1200;; i = 2800)
    {
      return scaleBitmap(localBitmap, i);
      CoScrollContainer localCoScrollContainer = (CoScrollContainer)paramView;
      localCoScrollContainer.setDrawingCacheEnabled(true);
      int j = localCoScrollContainer.getWidth();
      int k = localCoScrollContainer.getChildAt(0).getHeight();
      localCoScrollContainer.layout(0, 0, j, k);
      localBitmap = Bitmap.createBitmap(j, k, Bitmap.Config.ARGB_8888);
      localCoScrollContainer.draw(new Canvas(localBitmap));
    }
  }
  
  public static void launchGoogleFeedback(Context paramContext, final View paramView)
  {
    paramContext.bindService(new Intent("android.intent.action.BUG_REPORT"), new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        try
        {
          Parcel localParcel = Parcel.obtain();
          Bitmap localBitmap = SendGoogleFeedback.getCurrentScreenshot(this.val$context, paramView);
          if (localBitmap != null) {
            localBitmap.writeToParcel(localParcel, 0);
          }
          paramAnonymousIBinder.transact(1, localParcel, null, 0);
          this.val$context.unbindService(this);
          return;
        }
        catch (RemoteException localRemoteException)
        {
          Log.d("SendGoogleFeedback", "Failed to connect to bugreport service", localRemoteException);
          return;
        }
        finally
        {
          paramView.destroyDrawingCache();
        }
      }
      
      public void onServiceDisconnected(ComponentName paramAnonymousComponentName) {}
    }, 1);
  }
  
  private static Bitmap scaleBitmap(Bitmap paramBitmap, int paramInt)
  {
    if (paramBitmap == null) {
      return null;
    }
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    if ((i <= paramInt) && (j <= paramInt))
    {
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = Integer.valueOf(i);
      arrayOfObject2[1] = Integer.valueOf(j);
      Log.d("SendGoogleFeedback", String.format("Using screenshot of size %dx%d", arrayOfObject2));
      return paramBitmap;
    }
    double d;
    int k;
    if (i > j)
    {
      d = paramInt / i;
      k = paramInt;
    }
    for (int m = DoubleMath.roundToInt(d * j, RoundingMode.HALF_UP);; m = paramInt)
    {
      Object[] arrayOfObject1 = new Object[4];
      arrayOfObject1[0] = Integer.valueOf(i);
      arrayOfObject1[1] = Integer.valueOf(j);
      arrayOfObject1[2] = Integer.valueOf(k);
      arrayOfObject1[3] = Integer.valueOf(m);
      Log.w("SendGoogleFeedback", String.format("Reducing screenshot size from %dx%d to %dx%d", arrayOfObject1));
      return Bitmap.createScaledBitmap(paramBitmap, k, m, true);
      k = DoubleMath.roundToInt(paramInt / j * i, RoundingMode.HALF_UP);
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    launchGoogleFeedback(getApplicationContext(), getWindow().getDecorView().getRootView());
    finish();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.SendGoogleFeedback
 * JD-Core Version:    0.7.0.1
 */