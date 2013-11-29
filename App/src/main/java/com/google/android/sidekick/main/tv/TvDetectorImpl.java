package com.google.android.sidekick.main.tv;

import android.content.Context;
import com.google.android.libraries.tvdetect.Device;
import com.google.android.libraries.tvdetect.DeviceFinder;
import com.google.android.libraries.tvdetect.DeviceFinder.Callback;
import com.google.android.libraries.tvdetect.DeviceFinderOptions;
import com.google.android.libraries.tvdetect.DeviceFinderOptions.Builder;
import com.google.android.libraries.tvdetect.DeviceFinders;
import com.google.android.libraries.tvdetect.DeviceModel;
import com.google.android.libraries.tvdetect.ProductInfo;
import com.google.android.libraries.tvdetect.ProductInfo.Builder;
import com.google.android.libraries.tvdetect.ProductInfoService;
import com.google.android.libraries.tvdetect.ProductInfoServiceException;
import com.google.android.libraries.tvdetect.ProductType;
import com.google.android.libraries.tvdetect.ProductTypes;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.DetectedDevice;
import com.google.geo.sidekick.Sidekick.DeviceModel;
import com.google.geo.sidekick.Sidekick.NetworkDeviceInfoQuery;
import com.google.geo.sidekick.Sidekick.NetworkDeviceInfoResponse;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public final class TvDetectorImpl
  implements DeviceFinder.Callback, TvDetector
{
  private final Collection<Device> mDetectedDevices;
  private DeviceFinder mDeviceFinder;
  private final DeviceFinderFactory mDeviceFinderFactory;
  private final CountDownLatch mFastResultsLatch;
  private final CountDownLatch mFullResultsLatch;
  private final Object mLock;
  private TvDetector.Observer mObserver;
  
  public TvDetectorImpl(DeviceFinderFactory paramDeviceFinderFactory)
  {
    this.mDeviceFinderFactory = paramDeviceFinderFactory;
    this.mDetectedDevices = Collections.synchronizedSet(Sets.newHashSet());
    this.mFullResultsLatch = new CountDownLatch(1);
    this.mFastResultsLatch = new CountDownLatch(1);
    this.mLock = new Object();
  }
  
  public Collection<Device> getDetectedDevices(long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean)
  {
    
    for (;;)
    {
      boolean bool;
      synchronized (this.mLock)
      {
        if (this.mDeviceFinder != null)
        {
          bool = true;
          Preconditions.checkState(bool, "Must call startDetection() first");
          if (!paramBoolean) {
            break label69;
          }
        }
      }
      try
      {
        this.mFastResultsLatch.await(paramLong, paramTimeUnit);
        for (;;)
        {
          label47:
          return ImmutableSet.copyOf(this.mDetectedDevices);
          bool = false;
          break;
          localObject2 = finally;
          throw localObject2;
          label69:
          this.mFullResultsLatch.await(paramLong, paramTimeUnit);
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        break label47;
      }
    }
  }
  
  public void onDeviceFound(Device paramDevice)
  {
    this.mDetectedDevices.add(paramDevice);
  }
  
  public void onProgressChanged(int paramInt1, int paramInt2)
  {
    this.mFastResultsLatch.countDown();
    if (paramInt1 == paramInt2) {
      this.mFullResultsLatch.countDown();
    }
    if (paramInt1 == paramInt2) {}
    synchronized (this.mLock)
    {
      TvDetector.Observer localObserver = this.mObserver;
      if (localObserver != null) {
        localObserver.onTvDetectionFinished();
      }
      return;
    }
  }
  
  public void startDetection(@Nullable TvDetector.Observer paramObserver)
  {
    synchronized (this.mLock)
    {
      if (this.mDeviceFinder == null)
      {
        bool = true;
        Preconditions.checkState(bool, "TvDetectorImpl.startDetection is not re-entrant");
        this.mDeviceFinder = this.mDeviceFinderFactory.newDeviceFinder();
        this.mObserver = paramObserver;
        DeviceFinderOptions localDeviceFinderOptions = DeviceFinderOptions.newBuilder().setWantedProductTypes(ProductTypes.setOfTvOnly()).build();
        this.mDeviceFinder.search(this, localDeviceFinderOptions);
        return;
      }
      boolean bool = false;
    }
  }
  
  public void stopDetection()
  {
    synchronized (this.mLock)
    {
      DeviceFinder localDeviceFinder = this.mDeviceFinder;
      if (localDeviceFinder != null)
      {
        localDeviceFinder.stopSearch();
        this.mFullResultsLatch.countDown();
        this.mFastResultsLatch.countDown();
      }
      return;
    }
  }
  
  public static final class DefaultDeviceFinderFactory
    implements TvDetectorImpl.DeviceFinderFactory
  {
    private final Context mContext;
    
    public DefaultDeviceFinderFactory(Context paramContext)
    {
      this.mContext = paramContext;
    }
    
    public DeviceFinder newDeviceFinder()
    {
      NetworkClient localNetworkClient = VelvetServices.get().getSidekickInjector().getNetworkClient();
      return DeviceFinders.newDeviceFinder(this.mContext, new TvDetectorImpl.SidekickProductInfoService(localNetworkClient));
    }
  }
  
  static abstract interface DeviceFinderFactory
  {
    public abstract DeviceFinder newDeviceFinder();
  }
  
  public static final class Factory
    implements TvDetector.Factory
  {
    private final Context mContext;
    
    public Factory(Context paramContext)
    {
      this.mContext = paramContext;
    }
    
    public TvDetector newTvDetector()
    {
      return new TvDetectorImpl(new TvDetectorImpl.DefaultDeviceFinderFactory(this.mContext));
    }
  }
  
  static class SidekickProductInfoService
    implements ProductInfoService
  {
    private final NetworkClient mNetworkClient;
    
    SidekickProductInfoService(NetworkClient paramNetworkClient)
    {
      this.mNetworkClient = paramNetworkClient;
    }
    
    private static Sidekick.DeviceModel convertToDeviceModelProto(DeviceModel paramDeviceModel)
    {
      Sidekick.DeviceModel localDeviceModel = new Sidekick.DeviceModel();
      if (paramDeviceModel.category != null) {
        localDeviceModel.setCategory(paramDeviceModel.category);
      }
      if (paramDeviceModel.manufacturer != null) {
        localDeviceModel.setManufacturer(paramDeviceModel.manufacturer);
      }
      if (paramDeviceModel.manufacturerUrl != null) {
        localDeviceModel.setManufacturerUrl(paramDeviceModel.manufacturerUrl);
      }
      if (paramDeviceModel.modelDescription != null) {
        localDeviceModel.setModelDescription(paramDeviceModel.modelDescription);
      }
      if (paramDeviceModel.modelId != null) {
        localDeviceModel.setModelId(paramDeviceModel.modelId);
      }
      if (paramDeviceModel.modelName != null) {
        localDeviceModel.setModelName(paramDeviceModel.modelName);
      }
      if (paramDeviceModel.modelNumber != null) {
        localDeviceModel.setModelNumber(paramDeviceModel.modelNumber);
      }
      if (paramDeviceModel.modelUrl != null) {
        localDeviceModel.setModelUrl(paramDeviceModel.modelUrl);
      }
      return localDeviceModel;
    }
    
    private ProductType convertToProductType(int paramInt)
    {
      if (paramInt == 1) {
        return ProductType.TV;
      }
      return ProductType.UNKNOWN;
    }
    
    public ProductInfo getProductInfo(DeviceModel paramDeviceModel)
      throws ProductInfoServiceException
    {
      Sidekick.NetworkDeviceInfoQuery localNetworkDeviceInfoQuery = new Sidekick.NetworkDeviceInfoQuery().setDeviceModel(convertToDeviceModelProto(paramDeviceModel));
      Sidekick.ResponsePayload localResponsePayload = this.mNetworkClient.sendRequestWithoutLocation(new Sidekick.RequestPayload().setNetworkDeviceInfoQuery(localNetworkDeviceInfoQuery));
      if (localResponsePayload == null) {
        throw new ProductInfoServiceException("No response");
      }
      if ((!localResponsePayload.hasNetworkDeviceInfoResponse()) || (!localResponsePayload.getNetworkDeviceInfoResponse().hasDetectedDevice())) {
        throw new ProductInfoServiceException("ProductInfo missing from response");
      }
      Sidekick.DetectedDevice localDetectedDevice = localResponsePayload.getNetworkDeviceInfoResponse().getDetectedDevice();
      return ProductInfo.newBuilder().setType(convertToProductType(localDetectedDevice.getDeviceType())).setUserFriendlyName(localDetectedDevice.getModelName()).build();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.tv.TvDetectorImpl
 * JD-Core Version:    0.7.0.1
 */