package com.google.android.gms.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.google.android.gms.location.reporting.ReportingState;
import com.google.android.gms.location.reporting.UploadRequest;
import com.google.android.gms.location.reporting.UploadRequestResult;
import com.google.android.gms.location.reporting.d;
import com.google.android.gms.location.reporting.e;
import com.google.android.gms.location.reporting.f;

public abstract interface ft
  extends IInterface
{
  public abstract ReportingState getReportingState(Account paramAccount)
    throws RemoteException;
  
  public abstract int i(long paramLong)
    throws RemoteException;
  
  public abstract UploadRequestResult requestUpload(UploadRequest paramUploadRequest)
    throws RemoteException;
  
  public abstract int tryOptIn(Account paramAccount)
    throws RemoteException;
  
  public static abstract class a
    extends Binder
    implements ft
  {
    public static ft T(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gms.location.reporting.internal.IReportingService");
      if ((localIInterface != null) && ((localIInterface instanceof ft))) {
        return (ft)localIInterface;
      }
      return new a(paramIBinder);
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      switch (paramInt1)
      {
      default: 
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902: 
        paramParcel2.writeString("com.google.android.gms.location.reporting.internal.IReportingService");
        return true;
      case 1: 
        paramParcel1.enforceInterface("com.google.android.gms.location.reporting.internal.IReportingService");
        int n = paramParcel1.readInt();
        Account localAccount2 = null;
        if (n != 0) {
          localAccount2 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
        }
        ReportingState localReportingState = getReportingState(localAccount2);
        paramParcel2.writeNoException();
        if (localReportingState != null)
        {
          paramParcel2.writeInt(1);
          localReportingState.writeToParcel(paramParcel2, 1);
        }
        for (;;)
        {
          return true;
          paramParcel2.writeInt(0);
        }
      case 2: 
        paramParcel1.enforceInterface("com.google.android.gms.location.reporting.internal.IReportingService");
        int k = paramParcel1.readInt();
        Account localAccount1 = null;
        if (k != 0) {
          localAccount1 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
        }
        int m = tryOptIn(localAccount1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(m);
        return true;
      case 3: 
        paramParcel1.enforceInterface("com.google.android.gms.location.reporting.internal.IReportingService");
        int j = paramParcel1.readInt();
        UploadRequest localUploadRequest = null;
        if (j != 0) {
          localUploadRequest = UploadRequest.CREATOR.aS(paramParcel1);
        }
        UploadRequestResult localUploadRequestResult = requestUpload(localUploadRequest);
        paramParcel2.writeNoException();
        if (localUploadRequestResult != null)
        {
          paramParcel2.writeInt(1);
          localUploadRequestResult.writeToParcel(paramParcel2, 1);
        }
        for (;;)
        {
          return true;
          paramParcel2.writeInt(0);
        }
      }
      paramParcel1.enforceInterface("com.google.android.gms.location.reporting.internal.IReportingService");
      int i = i(paramParcel1.readLong());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private static class a
      implements ft
    {
      private IBinder dG;
      
      a(IBinder paramIBinder)
      {
        this.dG = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return this.dG;
      }
      
      public ReportingState getReportingState(Account paramAccount)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.location.reporting.internal.IReportingService");
            if (paramAccount != null)
            {
              localParcel1.writeInt(1);
              paramAccount.writeToParcel(localParcel1, 0);
              this.dG.transact(1, localParcel1, localParcel2, 0);
              localParcel2.readException();
              if (localParcel2.readInt() != 0)
              {
                ReportingState localReportingState2 = ReportingState.CREATOR.aR(localParcel2);
                localReportingState1 = localReportingState2;
                return localReportingState1;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            ReportingState localReportingState1 = null;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      public int i(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.location.reporting.internal.IReportingService");
          localParcel1.writeLong(paramLong);
          this.dG.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public UploadRequestResult requestUpload(UploadRequest paramUploadRequest)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.location.reporting.internal.IReportingService");
            if (paramUploadRequest != null)
            {
              localParcel1.writeInt(1);
              paramUploadRequest.writeToParcel(localParcel1, 0);
              this.dG.transact(3, localParcel1, localParcel2, 0);
              localParcel2.readException();
              if (localParcel2.readInt() != 0)
              {
                UploadRequestResult localUploadRequestResult2 = UploadRequestResult.CREATOR.aT(localParcel2);
                localUploadRequestResult1 = localUploadRequestResult2;
                return localUploadRequestResult1;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            UploadRequestResult localUploadRequestResult1 = null;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      /* Error */
      public int tryOptIn(Account paramAccount)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_2
        //   4: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   7: astore_3
        //   8: aload_2
        //   9: ldc 29
        //   11: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   14: aload_1
        //   15: ifnull +49 -> 64
        //   18: aload_2
        //   19: iconst_1
        //   20: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   23: aload_1
        //   24: aload_2
        //   25: iconst_0
        //   26: invokevirtual 43	android/accounts/Account:writeToParcel	(Landroid/os/Parcel;I)V
        //   29: aload_0
        //   30: getfield 15	com/google/android/gms/internal/ft$a$a:dG	Landroid/os/IBinder;
        //   33: iconst_2
        //   34: aload_2
        //   35: aload_3
        //   36: iconst_0
        //   37: invokeinterface 49 5 0
        //   42: pop
        //   43: aload_3
        //   44: invokevirtual 52	android/os/Parcel:readException	()V
        //   47: aload_3
        //   48: invokevirtual 56	android/os/Parcel:readInt	()I
        //   51: istore 6
        //   53: aload_3
        //   54: invokevirtual 71	android/os/Parcel:recycle	()V
        //   57: aload_2
        //   58: invokevirtual 71	android/os/Parcel:recycle	()V
        //   61: iload 6
        //   63: ireturn
        //   64: aload_2
        //   65: iconst_0
        //   66: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   69: goto -40 -> 29
        //   72: astore 4
        //   74: aload_3
        //   75: invokevirtual 71	android/os/Parcel:recycle	()V
        //   78: aload_2
        //   79: invokevirtual 71	android/os/Parcel:recycle	()V
        //   82: aload 4
        //   84: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	85	0	this	a
        //   0	85	1	paramAccount	Account
        //   3	76	2	localParcel1	Parcel
        //   7	68	3	localParcel2	Parcel
        //   72	11	4	localObject	Object
        //   51	11	6	i	int
        // Exception table:
        //   from	to	target	type
        //   8	14	72	finally
        //   18	29	72	finally
        //   29	53	72	finally
        //   64	69	72	finally
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.ft
 * JD-Core Version:    0.7.0.1
 */