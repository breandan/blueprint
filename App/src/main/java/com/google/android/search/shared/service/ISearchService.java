package com.google.android.search.shared.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchBoxStats;
import com.google.android.search.shared.api.Suggestion;

public abstract interface ISearchService
  extends IInterface
{
  public abstract void cancel()
    throws RemoteException;
  
  public abstract void commit(Query paramQuery)
    throws RemoteException;
  
  public abstract void onQuickContactClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
    throws RemoteException;
  
  public abstract void onSuggestionClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
    throws RemoteException;
  
  public abstract void removeSuggestionFromHistory(Suggestion paramSuggestion)
    throws RemoteException;
  
  public abstract void set(Query paramQuery)
    throws RemoteException;
  
  public abstract void setHotwordDetectionEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSearchServiceUiCallback(ISearchServiceUiCallback paramISearchServiceUiCallback, ClientConfig paramClientConfig)
    throws RemoteException;
  
  public abstract void startQueryEdit()
    throws RemoteException;
  
  public abstract void stopListening()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISearchService
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.search.shared.service.ISearchService");
    }
    
    public static ISearchService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.search.shared.service.ISearchService");
      if ((localIInterface != null) && ((localIInterface instanceof ISearchService))) {
        return (ISearchService)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      switch (paramInt1)
      {
      default: 
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902: 
        paramParcel2.writeString("com.google.android.search.shared.service.ISearchService");
        return true;
      case 1: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchService");
        ISearchServiceUiCallback localISearchServiceUiCallback = ISearchServiceUiCallback.Stub.asInterface(paramParcel1.readStrongBinder());
        if (paramParcel1.readInt() != 0) {}
        for (ClientConfig localClientConfig = (ClientConfig)ClientConfig.CREATOR.createFromParcel(paramParcel1);; localClientConfig = null)
        {
          setSearchServiceUiCallback(localISearchServiceUiCallback, localClientConfig);
          return true;
        }
      case 2: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchService");
        if (paramParcel1.readInt() != 0) {}
        for (Query localQuery2 = (Query)Query.CREATOR.createFromParcel(paramParcel1);; localQuery2 = null)
        {
          set(localQuery2);
          return true;
        }
      case 3: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchService");
        if (paramParcel1.readInt() != 0) {}
        for (Query localQuery1 = (Query)Query.CREATOR.createFromParcel(paramParcel1);; localQuery1 = null)
        {
          commit(localQuery1);
          return true;
        }
      case 4: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchService");
        startQueryEdit();
        return true;
      case 5: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchService");
        cancel();
        return true;
      case 6: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchService");
        stopListening();
        return true;
      case 7: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchService");
        Suggestion localSuggestion3;
        if (paramParcel1.readInt() != 0)
        {
          localSuggestion3 = (Suggestion)Suggestion.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label352;
          }
        }
        for (SearchBoxStats localSearchBoxStats2 = (SearchBoxStats)SearchBoxStats.CREATOR.createFromParcel(paramParcel1);; localSearchBoxStats2 = null)
        {
          onSuggestionClicked(localSuggestion3, localSearchBoxStats2);
          return true;
          localSuggestion3 = null;
          break;
        }
      case 8: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchService");
        Suggestion localSuggestion2;
        if (paramParcel1.readInt() != 0)
        {
          localSuggestion2 = (Suggestion)Suggestion.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label422;
          }
        }
        for (SearchBoxStats localSearchBoxStats1 = (SearchBoxStats)SearchBoxStats.CREATOR.createFromParcel(paramParcel1);; localSearchBoxStats1 = null)
        {
          onQuickContactClicked(localSuggestion2, localSearchBoxStats1);
          return true;
          localSuggestion2 = null;
          break;
        }
      case 9: 
        label352:
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchService");
        label422:
        if (paramParcel1.readInt() != 0) {}
        for (Suggestion localSuggestion1 = (Suggestion)Suggestion.CREATOR.createFromParcel(paramParcel1);; localSuggestion1 = null)
        {
          removeSuggestionFromHistory(localSuggestion1);
          return true;
        }
      }
      paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchService");
      if (paramParcel1.readInt() != 0) {}
      for (boolean bool = true;; bool = false)
      {
        setHotwordDetectionEnabled(bool);
        return true;
      }
    }
    
    private static class Proxy
      implements ISearchService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        this.mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return this.mRemote;
      }
      
      public void cancel()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchService");
          this.mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public void commit(Query paramQuery)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 26	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_2
        //   4: aload_2
        //   5: ldc 28
        //   7: invokevirtual 32	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aload_1
        //   11: ifnull +33 -> 44
        //   14: aload_2
        //   15: iconst_1
        //   16: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   19: aload_1
        //   20: aload_2
        //   21: iconst_0
        //   22: invokevirtual 53	com/google/android/search/shared/api/Query:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/search/shared/service/ISearchService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   29: iconst_3
        //   30: aload_2
        //   31: aconst_null
        //   32: iconst_1
        //   33: invokeinterface 38 5 0
        //   38: pop
        //   39: aload_2
        //   40: invokevirtual 41	android/os/Parcel:recycle	()V
        //   43: return
        //   44: aload_2
        //   45: iconst_0
        //   46: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   49: goto -24 -> 25
        //   52: astore_3
        //   53: aload_2
        //   54: invokevirtual 41	android/os/Parcel:recycle	()V
        //   57: aload_3
        //   58: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	59	0	this	Proxy
        //   0	59	1	paramQuery	Query
        //   3	51	2	localParcel	Parcel
        //   52	6	3	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   4	10	52	finally
        //   14	25	52	finally
        //   25	39	52	finally
        //   44	49	52	finally
      }
      
      public void onQuickContactClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchService");
            if (paramSuggestion != null)
            {
              localParcel.writeInt(1);
              paramSuggestion.writeToParcel(localParcel, 0);
              if (paramSearchBoxStats != null)
              {
                localParcel.writeInt(1);
                paramSearchBoxStats.writeToParcel(localParcel, 0);
                this.mRemote.transact(8, localParcel, null, 1);
              }
            }
            else
            {
              localParcel.writeInt(0);
              continue;
            }
            localParcel.writeInt(0);
          }
          finally
          {
            localParcel.recycle();
          }
        }
      }
      
      public void onSuggestionClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchService");
            if (paramSuggestion != null)
            {
              localParcel.writeInt(1);
              paramSuggestion.writeToParcel(localParcel, 0);
              if (paramSearchBoxStats != null)
              {
                localParcel.writeInt(1);
                paramSearchBoxStats.writeToParcel(localParcel, 0);
                this.mRemote.transact(7, localParcel, null, 1);
              }
            }
            else
            {
              localParcel.writeInt(0);
              continue;
            }
            localParcel.writeInt(0);
          }
          finally
          {
            localParcel.recycle();
          }
        }
      }
      
      /* Error */
      public void removeSuggestionFromHistory(Suggestion paramSuggestion)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 26	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_2
        //   4: aload_2
        //   5: ldc 28
        //   7: invokevirtual 32	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aload_1
        //   11: ifnull +34 -> 45
        //   14: aload_2
        //   15: iconst_1
        //   16: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   19: aload_1
        //   20: aload_2
        //   21: iconst_0
        //   22: invokevirtual 58	com/google/android/search/shared/api/Suggestion:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/search/shared/service/ISearchService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   29: bipush 9
        //   31: aload_2
        //   32: aconst_null
        //   33: iconst_1
        //   34: invokeinterface 38 5 0
        //   39: pop
        //   40: aload_2
        //   41: invokevirtual 41	android/os/Parcel:recycle	()V
        //   44: return
        //   45: aload_2
        //   46: iconst_0
        //   47: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   50: goto -25 -> 25
        //   53: astore_3
        //   54: aload_2
        //   55: invokevirtual 41	android/os/Parcel:recycle	()V
        //   58: aload_3
        //   59: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	60	0	this	Proxy
        //   0	60	1	paramSuggestion	Suggestion
        //   3	52	2	localParcel	Parcel
        //   53	6	3	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   4	10	53	finally
        //   14	25	53	finally
        //   25	40	53	finally
        //   45	50	53	finally
      }
      
      /* Error */
      public void set(Query paramQuery)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 26	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_2
        //   4: aload_2
        //   5: ldc 28
        //   7: invokevirtual 32	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aload_1
        //   11: ifnull +33 -> 44
        //   14: aload_2
        //   15: iconst_1
        //   16: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   19: aload_1
        //   20: aload_2
        //   21: iconst_0
        //   22: invokevirtual 53	com/google/android/search/shared/api/Query:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/search/shared/service/ISearchService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   29: iconst_2
        //   30: aload_2
        //   31: aconst_null
        //   32: iconst_1
        //   33: invokeinterface 38 5 0
        //   38: pop
        //   39: aload_2
        //   40: invokevirtual 41	android/os/Parcel:recycle	()V
        //   43: return
        //   44: aload_2
        //   45: iconst_0
        //   46: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   49: goto -24 -> 25
        //   52: astore_3
        //   53: aload_2
        //   54: invokevirtual 41	android/os/Parcel:recycle	()V
        //   57: aload_3
        //   58: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	59	0	this	Proxy
        //   0	59	1	paramQuery	Query
        //   3	51	2	localParcel	Parcel
        //   52	6	3	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   4	10	52	finally
        //   14	25	52	finally
        //   25	39	52	finally
        //   44	49	52	finally
      }
      
      /* Error */
      public void setHotwordDetectionEnabled(boolean paramBoolean)
        throws RemoteException
      {
        // Byte code:
        //   0: iconst_1
        //   1: istore_2
        //   2: invokestatic 26	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   5: astore_3
        //   6: aload_3
        //   7: ldc 28
        //   9: invokevirtual 32	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: iload_1
        //   13: ifeq +28 -> 41
        //   16: aload_3
        //   17: iload_2
        //   18: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   21: aload_0
        //   22: getfield 15	com/google/android/search/shared/service/ISearchService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   25: bipush 10
        //   27: aload_3
        //   28: aconst_null
        //   29: iconst_1
        //   30: invokeinterface 38 5 0
        //   35: pop
        //   36: aload_3
        //   37: invokevirtual 41	android/os/Parcel:recycle	()V
        //   40: return
        //   41: iconst_0
        //   42: istore_2
        //   43: goto -27 -> 16
        //   46: astore 4
        //   48: aload_3
        //   49: invokevirtual 41	android/os/Parcel:recycle	()V
        //   52: aload 4
        //   54: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	55	0	this	Proxy
        //   0	55	1	paramBoolean	boolean
        //   1	42	2	i	int
        //   5	44	3	localParcel	Parcel
        //   46	7	4	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   6	12	46	finally
        //   16	36	46	finally
      }
      
      /* Error */
      public void setSearchServiceUiCallback(ISearchServiceUiCallback paramISearchServiceUiCallback, ClientConfig paramClientConfig)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 26	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_3
        //   4: aload_3
        //   5: ldc 28
        //   7: invokevirtual 32	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aconst_null
        //   11: astore 5
        //   13: aload_1
        //   14: ifnull +11 -> 25
        //   17: aload_1
        //   18: invokeinterface 73 1 0
        //   23: astore 5
        //   25: aload_3
        //   26: aload 5
        //   28: invokevirtual 76	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   31: aload_2
        //   32: ifnull +33 -> 65
        //   35: aload_3
        //   36: iconst_1
        //   37: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   40: aload_2
        //   41: aload_3
        //   42: iconst_0
        //   43: invokevirtual 79	com/google/android/search/shared/service/ClientConfig:writeToParcel	(Landroid/os/Parcel;I)V
        //   46: aload_0
        //   47: getfield 15	com/google/android/search/shared/service/ISearchService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   50: iconst_1
        //   51: aload_3
        //   52: aconst_null
        //   53: iconst_1
        //   54: invokeinterface 38 5 0
        //   59: pop
        //   60: aload_3
        //   61: invokevirtual 41	android/os/Parcel:recycle	()V
        //   64: return
        //   65: aload_3
        //   66: iconst_0
        //   67: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   70: goto -24 -> 46
        //   73: astore 4
        //   75: aload_3
        //   76: invokevirtual 41	android/os/Parcel:recycle	()V
        //   79: aload 4
        //   81: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	82	0	this	Proxy
        //   0	82	1	paramISearchServiceUiCallback	ISearchServiceUiCallback
        //   0	82	2	paramClientConfig	ClientConfig
        //   3	73	3	localParcel	Parcel
        //   73	7	4	localObject	Object
        //   11	16	5	localIBinder	IBinder
        // Exception table:
        //   from	to	target	type
        //   4	10	73	finally
        //   17	25	73	finally
        //   25	31	73	finally
        //   35	46	73	finally
        //   46	60	73	finally
        //   65	70	73	finally
      }
      
      public void startQueryEdit()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchService");
          this.mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopListening()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchService");
          this.mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.service.ISearchService
 * JD-Core Version:    0.7.0.1
 */