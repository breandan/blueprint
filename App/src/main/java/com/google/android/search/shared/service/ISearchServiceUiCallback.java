package com.google.android.search.shared.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.api.SuggestionLogInfo;
import java.util.ArrayList;
import java.util.List;

public abstract interface ISearchServiceUiCallback
  extends IInterface
{
  public abstract void hideSuggestions()
    throws RemoteException;
  
  public abstract void launchIntent(Intent paramIntent)
    throws RemoteException;
  
  public abstract void onRemoveSuggestionFromHistoryFailed()
    throws RemoteException;
  
  public abstract void setExternalFlags(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setFinalRecognizedText(String paramString)
    throws RemoteException;
  
  public abstract void setQuery(Query paramQuery)
    throws RemoteException;
  
  public abstract void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void showErrorMessage(String paramString)
    throws RemoteException;
  
  public abstract void showRecognitionState(int paramInt)
    throws RemoteException;
  
  public abstract void showSuggestions(Query paramQuery, List<Suggestion> paramList, boolean paramBoolean, SuggestionLogInfo paramSuggestionLogInfo)
    throws RemoteException;
  
  public abstract void updateRecognizedText(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void updateSpeechLevel(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISearchServiceUiCallback
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.search.shared.service.ISearchServiceUiCallback");
    }
    
    public static ISearchServiceUiCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ISearchServiceUiCallback))) {
        return (ISearchServiceUiCallback)localIInterface;
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
        paramParcel2.writeString("com.google.android.search.shared.service.ISearchServiceUiCallback");
        return true;
      case 1: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        int j = paramParcel1.readInt();
        int k = paramParcel1.readInt();
        int m = paramParcel1.readInt();
        boolean bool2 = false;
        if (m != 0) {
          bool2 = true;
        }
        setSearchPlateMode(j, k, bool2);
        return true;
      case 2: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        if (paramParcel1.readInt() != 0) {}
        for (Query localQuery2 = (Query)Query.CREATOR.createFromParcel(paramParcel1);; localQuery2 = null)
        {
          setQuery(localQuery2);
          return true;
        }
      case 3: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        setExternalFlags(paramParcel1.readInt(), paramParcel1.readString());
        return true;
      case 4: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        showErrorMessage(paramParcel1.readString());
        return true;
      case 5: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        showRecognitionState(paramParcel1.readInt());
        return true;
      case 6: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        updateRecognizedText(paramParcel1.readString(), paramParcel1.readString());
        return true;
      case 7: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        setFinalRecognizedText(paramParcel1.readString());
        return true;
      case 8: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        Query localQuery1;
        ArrayList localArrayList;
        boolean bool1;
        if (paramParcel1.readInt() != 0)
        {
          localQuery1 = (Query)Query.CREATOR.createFromParcel(paramParcel1);
          localArrayList = paramParcel1.createTypedArrayList(Suggestion.CREATOR);
          int i = paramParcel1.readInt();
          bool1 = false;
          if (i != 0) {
            bool1 = true;
          }
          if (paramParcel1.readInt() == 0) {
            break label404;
          }
        }
        for (SuggestionLogInfo localSuggestionLogInfo = (SuggestionLogInfo)SuggestionLogInfo.CREATOR.createFromParcel(paramParcel1);; localSuggestionLogInfo = null)
        {
          showSuggestions(localQuery1, localArrayList, bool1, localSuggestionLogInfo);
          return true;
          localQuery1 = null;
          break;
        }
      case 9: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        hideSuggestions();
        return true;
      case 10: 
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        onRemoveSuggestionFromHistoryFailed();
        return true;
      case 11: 
        label404:
        paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
        if (paramParcel1.readInt() != 0) {}
        for (Intent localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);; localIntent = null)
        {
          launchIntent(localIntent);
          return true;
        }
      }
      paramParcel1.enforceInterface("com.google.android.search.shared.service.ISearchServiceUiCallback");
      updateSpeechLevel(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements ISearchServiceUiCallback
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
      
      public void hideSuggestions()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchServiceUiCallback");
          this.mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public void launchIntent(Intent paramIntent)
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
        //   22: invokevirtual 53	android/content/Intent:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/search/shared/service/ISearchServiceUiCallback$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   29: bipush 11
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
        //   0	60	1	paramIntent	Intent
        //   3	52	2	localParcel	Parcel
        //   53	6	3	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   4	10	53	finally
        //   14	25	53	finally
        //   25	40	53	finally
        //   45	50	53	finally
      }
      
      public void onRemoveSuggestionFromHistoryFailed()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchServiceUiCallback");
          this.mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setExternalFlags(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchServiceUiCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          this.mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setFinalRecognizedText(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchServiceUiCallback");
          localParcel.writeString(paramString);
          this.mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public void setQuery(Query paramQuery)
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
        //   22: invokevirtual 65	com/google/android/search/shared/api/Query:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/search/shared/service/ISearchServiceUiCallback$Stub$Proxy:mRemote	Landroid/os/IBinder;
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
      public void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        // Byte code:
        //   0: iconst_1
        //   1: istore 4
        //   3: invokestatic 26	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   6: astore 5
        //   8: aload 5
        //   10: ldc 28
        //   12: invokevirtual 32	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   15: aload 5
        //   17: iload_1
        //   18: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   21: aload 5
        //   23: iload_2
        //   24: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   27: iload_3
        //   28: ifeq +31 -> 59
        //   31: aload 5
        //   33: iload 4
        //   35: invokevirtual 47	android/os/Parcel:writeInt	(I)V
        //   38: aload_0
        //   39: getfield 15	com/google/android/search/shared/service/ISearchServiceUiCallback$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   42: iconst_1
        //   43: aload 5
        //   45: aconst_null
        //   46: iconst_1
        //   47: invokeinterface 38 5 0
        //   52: pop
        //   53: aload 5
        //   55: invokevirtual 41	android/os/Parcel:recycle	()V
        //   58: return
        //   59: iconst_0
        //   60: istore 4
        //   62: goto -31 -> 31
        //   65: astore 6
        //   67: aload 5
        //   69: invokevirtual 41	android/os/Parcel:recycle	()V
        //   72: aload 6
        //   74: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	75	0	this	Proxy
        //   0	75	1	paramInt1	int
        //   0	75	2	paramInt2	int
        //   0	75	3	paramBoolean	boolean
        //   1	60	4	i	int
        //   6	62	5	localParcel	Parcel
        //   65	8	6	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   8	27	65	finally
        //   31	53	65	finally
      }
      
      public void showErrorMessage(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchServiceUiCallback");
          localParcel.writeString(paramString);
          this.mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showRecognitionState(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchServiceUiCallback");
          localParcel.writeInt(paramInt);
          this.mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showSuggestions(Query paramQuery, List<Suggestion> paramList, boolean paramBoolean, SuggestionLogInfo paramSuggestionLogInfo)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchServiceUiCallback");
            if (paramQuery != null)
            {
              localParcel.writeInt(1);
              paramQuery.writeToParcel(localParcel, 0);
              localParcel.writeTypedList(paramList);
              if (paramBoolean)
              {
                localParcel.writeInt(i);
                if (paramSuggestionLogInfo == null) {
                  break label115;
                }
                localParcel.writeInt(1);
                paramSuggestionLogInfo.writeToParcel(localParcel, 0);
                this.mRemote.transact(8, localParcel, null, 1);
              }
            }
            else
            {
              localParcel.writeInt(0);
              continue;
            }
            i = 0;
          }
          finally
          {
            localParcel.recycle();
          }
          continue;
          label115:
          localParcel.writeInt(0);
        }
      }
      
      public void updateRecognizedText(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchServiceUiCallback");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          this.mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void updateSpeechLevel(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.search.shared.service.ISearchServiceUiCallback");
          localParcel.writeInt(paramInt);
          this.mRemote.transact(12, localParcel, null, 1);
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
 * Qualified Name:     com.google.android.search.shared.service.ISearchServiceUiCallback
 * JD-Core Version:    0.7.0.1
 */