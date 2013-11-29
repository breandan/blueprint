package com.google.android.sidekick.main.tv;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.google.android.libraries.tvdetect.Device;
import com.google.android.libraries.tvdetect.DeviceFinder;
import com.google.android.libraries.tvdetect.DeviceFinder.Callback;
import com.google.android.libraries.tvdetect.DeviceFinderOptions;
import com.google.android.libraries.tvdetect.DeviceFinderOptions.Builder;
import com.google.android.libraries.tvdetect.ProductTypes;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.velvet.VelvetServices;

public class TvDetectionTestActivity
  extends Activity
  implements DeviceFinder.Callback
{
  private Button mButton;
  private Clock mClock;
  private long mDetectStartTimeMillis;
  private boolean mDeviceFound;
  private ArrayAdapter<String> mListAdapter;
  private ProgressBar mProgressBar;
  private ScheduledSingleThreadedExecutor mUiThreadExecutor;
  
  public void detectTv(View paramView)
  {
    this.mDeviceFound = false;
    this.mDetectStartTimeMillis = this.mClock.currentTimeMillis();
    this.mButton.setEnabled(false);
    this.mProgressBar.setVisibility(0);
    new TvDetectorImpl.DefaultDeviceFinderFactory(getApplicationContext()).newDeviceFinder().search(this, DeviceFinderOptions.newBuilder().setWantedProductTypes(ProductTypes.setOfTvOnly()).build());
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    VelvetServices localVelvetServices = VelvetServices.get();
    this.mClock = localVelvetServices.getCoreServices().getClock();
    this.mUiThreadExecutor = localVelvetServices.getAsyncServices().getUiThreadExecutor();
    setContentView(2130968895);
    this.mListAdapter = new ArrayAdapter(this, 17367043);
    ((ListView)findViewById(2131297164)).setAdapter(this.mListAdapter);
    this.mButton = ((Button)findViewById(2131296449));
    this.mProgressBar = ((ProgressBar)findViewById(2131296791));
  }
  
  public void onDeviceFound(final Device paramDevice)
  {
    this.mDeviceFound = true;
    this.mUiThreadExecutor.execute(new Runnable()
    {
      public void run()
      {
        ArrayAdapter localArrayAdapter = TvDetectionTestActivity.this.mListAdapter;
        String str = TvDetectionTestActivity.this.getResources().getString(2131362895);
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Long.valueOf(TvDetectionTestActivity.this.mClock.currentTimeMillis() - TvDetectionTestActivity.this.mDetectStartTimeMillis);
        arrayOfObject[1] = paramDevice.toString();
        localArrayAdapter.add(String.format(str, arrayOfObject));
        TvDetectionTestActivity.this.mListAdapter.notifyDataSetChanged();
      }
    });
  }
  
  public void onProgressChanged(int paramInt1, int paramInt2)
  {
    if (paramInt1 == paramInt2) {
      this.mUiThreadExecutor.execute(new Runnable()
      {
        public void run()
        {
          if (!TvDetectionTestActivity.this.mDeviceFound)
          {
            ArrayAdapter localArrayAdapter = TvDetectionTestActivity.this.mListAdapter;
            String str = TvDetectionTestActivity.this.getResources().getString(2131362896);
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Long.valueOf(TvDetectionTestActivity.this.mClock.currentTimeMillis() - TvDetectionTestActivity.this.mDetectStartTimeMillis);
            localArrayAdapter.add(String.format(str, arrayOfObject));
            TvDetectionTestActivity.this.mListAdapter.notifyDataSetChanged();
          }
          TvDetectionTestActivity.this.mButton.setEnabled(true);
          TvDetectionTestActivity.this.mProgressBar.setVisibility(8);
        }
      });
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.tv.TvDetectionTestActivity
 * JD-Core Version:    0.7.0.1
 */