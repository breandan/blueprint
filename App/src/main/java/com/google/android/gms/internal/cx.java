package com.google.android.gms.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.google.android.gms.appdatasearch.CorpusStatus;
import com.google.android.gms.appdatasearch.DocumentResults;
import com.google.android.gms.appdatasearch.GlobalSearchApplicationInfo;
import com.google.android.gms.appdatasearch.GlobalSearchQuerySpecification;
import com.google.android.gms.appdatasearch.PIMEUpdateResponse;
import com.google.android.gms.appdatasearch.PhraseAffinityResponse;
import com.google.android.gms.appdatasearch.PhraseAffinitySpecification;
import com.google.android.gms.appdatasearch.QuerySpecification;
import com.google.android.gms.appdatasearch.RegisterCorpusInfo;
import com.google.android.gms.appdatasearch.RegisteredPackageInfo;
import com.google.android.gms.appdatasearch.RequestIndexingSpecification;
import com.google.android.gms.appdatasearch.SearchResults;
import com.google.android.gms.appdatasearch.SuggestSpecification;
import com.google.android.gms.appdatasearch.SuggestionResults;
import com.google.android.gms.appdatasearch.d;
import com.google.android.gms.appdatasearch.e;
import com.google.android.gms.appdatasearch.g;
import com.google.android.gms.appdatasearch.k;
import com.google.android.gms.appdatasearch.l;
import com.google.android.gms.appdatasearch.m;
import com.google.android.gms.appdatasearch.n;
import com.google.android.gms.appdatasearch.q;
import com.google.android.gms.appdatasearch.r;
import com.google.android.gms.appdatasearch.s;
import com.google.android.gms.appdatasearch.u;
import com.google.android.gms.appdatasearch.x;
import com.google.android.gms.appdatasearch.y;

public abstract interface cx
  extends IInterface
{
  public abstract DocumentResults a(String[] paramArrayOfString, String paramString1, String paramString2, QuerySpecification paramQuerySpecification)
    throws RemoteException;
  
  public abstract PIMEUpdateResponse a(String paramString, int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract SearchResults a(String paramString, int paramInt1, int paramInt2, GlobalSearchQuerySpecification paramGlobalSearchQuerySpecification)
    throws RemoteException;
  
  public abstract SearchResults a(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt1, int paramInt2, QuerySpecification paramQuerySpecification)
    throws RemoteException;
  
  public abstract SuggestionResults a(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt, SuggestSpecification paramSuggestSpecification)
    throws RemoteException;
  
  public abstract void a(GlobalSearchApplicationInfo paramGlobalSearchApplicationInfo)
    throws RemoteException;
  
  public abstract void a(String paramString, RegisterCorpusInfo paramRegisterCorpusInfo)
    throws RemoteException;
  
  public abstract boolean a(r paramr)
    throws RemoteException;
  
  public abstract boolean a(String paramString1, String paramString2, long paramLong, RequestIndexingSpecification paramRequestIndexingSpecification)
    throws RemoteException;
  
  public abstract GlobalSearchApplicationInfo[] aG()
    throws RemoteException;
  
  public abstract boolean b(String paramString, RegisterCorpusInfo paramRegisterCorpusInfo)
    throws RemoteException;
  
  public abstract void blockPackages(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract CorpusStatus d(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract Bundle diagnostic(Bundle paramBundle)
    throws RemoteException;
  
  public abstract Bundle e(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract RegisterCorpusInfo f(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract PhraseAffinityResponse getPhraseAffinity(String[] paramArrayOfString, PhraseAffinitySpecification paramPhraseAffinitySpecification)
    throws RemoteException;
  
  public abstract RegisteredPackageInfo[] getRegisteredPackageInfo()
    throws RemoteException;
  
  public abstract String[] s(String paramString)
    throws RemoteException;
  
  public abstract void t(String paramString)
    throws RemoteException;
  
  public abstract void triggerCompaction()
    throws RemoteException;
  
  public abstract boolean u(String paramString)
    throws RemoteException;
  
  public abstract void unblockPackages(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void v(String paramString)
    throws RemoteException;
  
  public abstract String[] w(String paramString)
    throws RemoteException;
  
  public abstract String[] x(String paramString)
    throws RemoteException;
  
  public static abstract class a
    extends Binder
    implements cx
  {
    public static cx t(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
      if ((localIInterface != null) && ((localIInterface instanceof cx))) {
        return (cx)localIInterface;
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
        paramParcel2.writeString("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        return true;
      case 1: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        String str10 = paramParcel1.readString();
        String str11 = paramParcel1.readString();
        String[] arrayOfString7 = paramParcel1.createStringArray();
        int i10 = paramParcel1.readInt();
        int i11 = paramParcel1.readInt();
        int i12 = paramParcel1.readInt();
        QuerySpecification localQuerySpecification2 = null;
        if (i12 != 0) {
          localQuerySpecification2 = QuerySpecification.CREATOR.t(paramParcel1);
        }
        SearchResults localSearchResults2 = a(str10, str11, arrayOfString7, i10, i11, localQuerySpecification2);
        paramParcel2.writeNoException();
        if (localSearchResults2 != null)
        {
          paramParcel2.writeInt(1);
          localSearchResults2.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 2: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        String str8 = paramParcel1.readString();
        String str9 = paramParcel1.readString();
        String[] arrayOfString6 = paramParcel1.createStringArray();
        int i9 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {}
        for (SuggestSpecification localSuggestSpecification = SuggestSpecification.CREATOR.D(paramParcel1);; localSuggestSpecification = null)
        {
          SuggestionResults localSuggestionResults = a(str8, str9, arrayOfString6, i9, localSuggestSpecification);
          paramParcel2.writeNoException();
          if (localSuggestionResults == null) {
            break;
          }
          paramParcel2.writeInt(1);
          localSuggestionResults.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 3: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        String[] arrayOfString5 = paramParcel1.createStringArray();
        String str6 = paramParcel1.readString();
        String str7 = paramParcel1.readString();
        int i8 = paramParcel1.readInt();
        QuerySpecification localQuerySpecification1 = null;
        if (i8 != 0) {
          localQuerySpecification1 = QuerySpecification.CREATOR.t(paramParcel1);
        }
        DocumentResults localDocumentResults = a(arrayOfString5, str6, str7, localQuerySpecification1);
        paramParcel2.writeNoException();
        if (localDocumentResults != null)
        {
          paramParcel2.writeInt(1);
          localDocumentResults.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 4: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        String str4 = paramParcel1.readString();
        String str5 = paramParcel1.readString();
        long l = paramParcel1.readLong();
        RequestIndexingSpecification localRequestIndexingSpecification;
        if (paramParcel1.readInt() != 0)
        {
          localRequestIndexingSpecification = RequestIndexingSpecification.CREATOR.x(paramParcel1);
          boolean bool4 = a(str4, str5, l, localRequestIndexingSpecification);
          paramParcel2.writeNoException();
          if (!bool4) {
            break label622;
          }
        }
        for (int i7 = 1;; i7 = 0)
        {
          paramParcel2.writeInt(i7);
          return true;
          localRequestIndexingSpecification = null;
          break;
        }
      case 5: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        CorpusStatus localCorpusStatus = d(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        if (localCorpusStatus != null)
        {
          paramParcel2.writeInt(1);
          localCorpusStatus.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 6: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        String[] arrayOfString4 = s(paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeStringArray(arrayOfString4);
        return true;
      case 7: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        String str3 = paramParcel1.readString();
        int i6 = paramParcel1.readInt();
        RegisterCorpusInfo localRegisterCorpusInfo3 = null;
        if (i6 != 0) {
          localRegisterCorpusInfo3 = RegisterCorpusInfo.CREATOR.u(paramParcel1);
        }
        a(str3, localRegisterCorpusInfo3);
        paramParcel2.writeNoException();
        return true;
      case 8: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        Bundle localBundle3 = e(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        if (localBundle3 != null)
        {
          paramParcel2.writeInt(1);
          localBundle3.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 9: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        if (paramParcel1.readInt() != 0) {}
        for (Bundle localBundle1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; localBundle1 = null)
        {
          Bundle localBundle2 = diagnostic(localBundle1);
          paramParcel2.writeNoException();
          if (localBundle2 == null) {
            break;
          }
          paramParcel2.writeInt(1);
          localBundle2.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 10: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        String str2 = paramParcel1.readString();
        int i3 = paramParcel1.readInt();
        int i4 = paramParcel1.readInt();
        int i5 = paramParcel1.readInt();
        GlobalSearchQuerySpecification localGlobalSearchQuerySpecification = null;
        if (i5 != 0) {
          localGlobalSearchQuerySpecification = GlobalSearchQuerySpecification.CREATOR.n(paramParcel1);
        }
        SearchResults localSearchResults1 = a(str2, i3, i4, localGlobalSearchQuerySpecification);
        paramParcel2.writeNoException();
        if (localSearchResults1 != null)
        {
          paramParcel2.writeInt(1);
          localSearchResults1.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 11: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        int i2 = paramParcel1.readInt();
        GlobalSearchApplicationInfo localGlobalSearchApplicationInfo = null;
        if (i2 != 0) {
          localGlobalSearchApplicationInfo = GlobalSearchApplicationInfo.CREATOR.l(paramParcel1);
        }
        a(localGlobalSearchApplicationInfo);
        paramParcel2.writeNoException();
        return true;
      case 12: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        t(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 13: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        GlobalSearchApplicationInfo[] arrayOfGlobalSearchApplicationInfo = aG();
        paramParcel2.writeNoException();
        paramParcel2.writeTypedArray(arrayOfGlobalSearchApplicationInfo, 1);
        return true;
      case 14: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        String str1 = paramParcel1.readString();
        int n = paramParcel1.readInt();
        RegisterCorpusInfo localRegisterCorpusInfo2 = null;
        if (n != 0) {
          localRegisterCorpusInfo2 = RegisterCorpusInfo.CREATOR.u(paramParcel1);
        }
        boolean bool3 = b(str1, localRegisterCorpusInfo2);
        paramParcel2.writeNoException();
        int i1 = 0;
        if (bool3) {
          i1 = 1;
        }
        paramParcel2.writeInt(i1);
        return true;
      case 15: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        RegisteredPackageInfo[] arrayOfRegisteredPackageInfo = getRegisteredPackageInfo();
        paramParcel2.writeNoException();
        paramParcel2.writeTypedArray(arrayOfRegisteredPackageInfo, 1);
        return true;
      case 16: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        blockPackages(paramParcel1.createStringArray());
        paramParcel2.writeNoException();
        return true;
      case 17: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        unblockPackages(paramParcel1.createStringArray());
        paramParcel2.writeNoException();
        return true;
      case 18: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        triggerCompaction();
        paramParcel2.writeNoException();
        return true;
      case 19: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        int k = paramParcel1.readInt();
        r localr = null;
        if (k != 0) {
          localr = r.CREATOR.y(paramParcel1);
        }
        boolean bool2 = a(localr);
        paramParcel2.writeNoException();
        int m = 0;
        if (bool2) {
          m = 1;
        }
        paramParcel2.writeInt(m);
        return true;
      case 20: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        boolean bool1 = u(paramParcel1.readString());
        paramParcel2.writeNoException();
        int j = 0;
        if (bool1) {
          j = 1;
        }
        paramParcel2.writeInt(j);
        return true;
      case 21: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        v(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 22: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        String[] arrayOfString3 = w(paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeStringArray(arrayOfString3);
        return true;
      case 23: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        PIMEUpdateResponse localPIMEUpdateResponse = a(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.createByteArray());
        paramParcel2.writeNoException();
        if (localPIMEUpdateResponse != null)
        {
          paramParcel2.writeInt(1);
          localPIMEUpdateResponse.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 24: 
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        String[] arrayOfString2 = paramParcel1.createStringArray();
        int i = paramParcel1.readInt();
        PhraseAffinitySpecification localPhraseAffinitySpecification = null;
        if (i != 0) {
          localPhraseAffinitySpecification = PhraseAffinitySpecification.CREATOR.s(paramParcel1);
        }
        PhraseAffinityResponse localPhraseAffinityResponse = getPhraseAffinity(arrayOfString2, localPhraseAffinitySpecification);
        paramParcel2.writeNoException();
        if (localPhraseAffinityResponse != null)
        {
          paramParcel2.writeInt(1);
          localPhraseAffinityResponse.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 25: 
        label622:
        paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
        RegisterCorpusInfo localRegisterCorpusInfo1 = f(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        if (localRegisterCorpusInfo1 != null)
        {
          paramParcel2.writeInt(1);
          localRegisterCorpusInfo1.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      }
      paramParcel1.enforceInterface("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
      String[] arrayOfString1 = x(paramParcel1.readString());
      paramParcel2.writeNoException();
      paramParcel2.writeStringArray(arrayOfString1);
      return true;
    }
    
    private static class a
      implements cx
    {
      private IBinder dG;
      
      a(IBinder paramIBinder)
      {
        this.dG = paramIBinder;
      }
      
      public DocumentResults a(String[] paramArrayOfString, String paramString1, String paramString2, QuerySpecification paramQuerySpecification)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
            localParcel1.writeStringArray(paramArrayOfString);
            localParcel1.writeString(paramString1);
            localParcel1.writeString(paramString2);
            if (paramQuerySpecification != null)
            {
              localParcel1.writeInt(1);
              paramQuerySpecification.writeToParcel(localParcel1, 0);
              this.dG.transact(3, localParcel1, localParcel2, 0);
              localParcel2.readException();
              if (localParcel2.readInt() != 0)
              {
                DocumentResults localDocumentResults2 = DocumentResults.CREATOR.k(localParcel2);
                localDocumentResults1 = localDocumentResults2;
                return localDocumentResults1;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            DocumentResults localDocumentResults1 = null;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      /* Error */
      public PIMEUpdateResponse a(String paramString, int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 4
        //   5: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 5
        //   10: aload 4
        //   12: ldc 27
        //   14: invokevirtual 31	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 4
        //   19: aload_1
        //   20: invokevirtual 38	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   23: aload 4
        //   25: iload_2
        //   26: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   29: aload 4
        //   31: aload_3
        //   32: invokevirtual 81	android/os/Parcel:writeByteArray	([B)V
        //   35: aload_0
        //   36: getfield 15	com/google/android/gms/internal/cx$a$a:dG	Landroid/os/IBinder;
        //   39: bipush 23
        //   41: aload 4
        //   43: aload 5
        //   45: iconst_0
        //   46: invokeinterface 54 5 0
        //   51: pop
        //   52: aload 5
        //   54: invokevirtual 57	android/os/Parcel:readException	()V
        //   57: aload 5
        //   59: invokevirtual 61	android/os/Parcel:readInt	()I
        //   62: ifeq +30 -> 92
        //   65: getstatic 86	com/google/android/gms/appdatasearch/PIMEUpdateResponse:CREATOR	Lcom/google/android/gms/appdatasearch/i;
        //   68: aload 5
        //   70: invokevirtual 92	com/google/android/gms/appdatasearch/i:p	(Landroid/os/Parcel;)Lcom/google/android/gms/appdatasearch/PIMEUpdateResponse;
        //   73: astore 9
        //   75: aload 9
        //   77: astore 8
        //   79: aload 5
        //   81: invokevirtual 76	android/os/Parcel:recycle	()V
        //   84: aload 4
        //   86: invokevirtual 76	android/os/Parcel:recycle	()V
        //   89: aload 8
        //   91: areturn
        //   92: aconst_null
        //   93: astore 8
        //   95: goto -16 -> 79
        //   98: astore 6
        //   100: aload 5
        //   102: invokevirtual 76	android/os/Parcel:recycle	()V
        //   105: aload 4
        //   107: invokevirtual 76	android/os/Parcel:recycle	()V
        //   110: aload 6
        //   112: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	113	0	this	a
        //   0	113	1	paramString	String
        //   0	113	2	paramInt	int
        //   0	113	3	paramArrayOfByte	byte[]
        //   3	103	4	localParcel1	Parcel
        //   8	93	5	localParcel2	Parcel
        //   98	13	6	localObject	Object
        //   77	17	8	localPIMEUpdateResponse1	PIMEUpdateResponse
        //   73	3	9	localPIMEUpdateResponse2	PIMEUpdateResponse
        // Exception table:
        //   from	to	target	type
        //   10	75	98	finally
      }
      
      public SearchResults a(String paramString, int paramInt1, int paramInt2, GlobalSearchQuerySpecification paramGlobalSearchQuerySpecification)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
            localParcel1.writeString(paramString);
            localParcel1.writeInt(paramInt1);
            localParcel1.writeInt(paramInt2);
            if (paramGlobalSearchQuerySpecification != null)
            {
              localParcel1.writeInt(1);
              paramGlobalSearchQuerySpecification.writeToParcel(localParcel1, 0);
              this.dG.transact(10, localParcel1, localParcel2, 0);
              localParcel2.readException();
              if (localParcel2.readInt() != 0)
              {
                SearchResults localSearchResults2 = SearchResults.CREATOR.A(localParcel2);
                localSearchResults1 = localSearchResults2;
                return localSearchResults1;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            SearchResults localSearchResults1 = null;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      public SearchResults a(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt1, int paramInt2, QuerySpecification paramQuerySpecification)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
            localParcel1.writeString(paramString1);
            localParcel1.writeString(paramString2);
            localParcel1.writeStringArray(paramArrayOfString);
            localParcel1.writeInt(paramInt1);
            localParcel1.writeInt(paramInt2);
            if (paramQuerySpecification != null)
            {
              localParcel1.writeInt(1);
              paramQuerySpecification.writeToParcel(localParcel1, 0);
              this.dG.transact(1, localParcel1, localParcel2, 0);
              localParcel2.readException();
              if (localParcel2.readInt() != 0)
              {
                SearchResults localSearchResults2 = SearchResults.CREATOR.A(localParcel2);
                localSearchResults1 = localSearchResults2;
                return localSearchResults1;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            SearchResults localSearchResults1 = null;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      public SuggestionResults a(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt, SuggestSpecification paramSuggestSpecification)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
            localParcel1.writeString(paramString1);
            localParcel1.writeString(paramString2);
            localParcel1.writeStringArray(paramArrayOfString);
            localParcel1.writeInt(paramInt);
            if (paramSuggestSpecification != null)
            {
              localParcel1.writeInt(1);
              paramSuggestSpecification.writeToParcel(localParcel1, 0);
              this.dG.transact(2, localParcel1, localParcel2, 0);
              localParcel2.readException();
              if (localParcel2.readInt() != 0)
              {
                SuggestionResults localSuggestionResults2 = SuggestionResults.CREATOR.E(localParcel2);
                localSuggestionResults1 = localSuggestionResults2;
                return localSuggestionResults1;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            SuggestionResults localSuggestionResults1 = null;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      /* Error */
      public void a(GlobalSearchApplicationInfo paramGlobalSearchApplicationInfo)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_2
        //   4: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   7: astore_3
        //   8: aload_2
        //   9: ldc 27
        //   11: invokevirtual 31	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   14: aload_1
        //   15: ifnull +42 -> 57
        //   18: aload_2
        //   19: iconst_1
        //   20: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   23: aload_1
        //   24: aload_2
        //   25: iconst_0
        //   26: invokevirtual 127	com/google/android/gms/appdatasearch/GlobalSearchApplicationInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   29: aload_0
        //   30: getfield 15	com/google/android/gms/internal/cx$a$a:dG	Landroid/os/IBinder;
        //   33: bipush 11
        //   35: aload_2
        //   36: aload_3
        //   37: iconst_0
        //   38: invokeinterface 54 5 0
        //   43: pop
        //   44: aload_3
        //   45: invokevirtual 57	android/os/Parcel:readException	()V
        //   48: aload_3
        //   49: invokevirtual 76	android/os/Parcel:recycle	()V
        //   52: aload_2
        //   53: invokevirtual 76	android/os/Parcel:recycle	()V
        //   56: return
        //   57: aload_2
        //   58: iconst_0
        //   59: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   62: goto -33 -> 29
        //   65: astore 4
        //   67: aload_3
        //   68: invokevirtual 76	android/os/Parcel:recycle	()V
        //   71: aload_2
        //   72: invokevirtual 76	android/os/Parcel:recycle	()V
        //   75: aload 4
        //   77: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	78	0	this	a
        //   0	78	1	paramGlobalSearchApplicationInfo	GlobalSearchApplicationInfo
        //   3	69	2	localParcel1	Parcel
        //   7	61	3	localParcel2	Parcel
        //   65	11	4	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   8	14	65	finally
        //   18	29	65	finally
        //   29	48	65	finally
        //   57	62	65	finally
      }
      
      /* Error */
      public void a(String paramString, RegisterCorpusInfo paramRegisterCorpusInfo)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_3
        //   4: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   7: astore 4
        //   9: aload_3
        //   10: ldc 27
        //   12: invokevirtual 31	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   15: aload_3
        //   16: aload_1
        //   17: invokevirtual 38	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   20: aload_2
        //   21: ifnull +45 -> 66
        //   24: aload_3
        //   25: iconst_1
        //   26: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   29: aload_2
        //   30: aload_3
        //   31: iconst_0
        //   32: invokevirtual 131	com/google/android/gms/appdatasearch/RegisterCorpusInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   35: aload_0
        //   36: getfield 15	com/google/android/gms/internal/cx$a$a:dG	Landroid/os/IBinder;
        //   39: bipush 7
        //   41: aload_3
        //   42: aload 4
        //   44: iconst_0
        //   45: invokeinterface 54 5 0
        //   50: pop
        //   51: aload 4
        //   53: invokevirtual 57	android/os/Parcel:readException	()V
        //   56: aload 4
        //   58: invokevirtual 76	android/os/Parcel:recycle	()V
        //   61: aload_3
        //   62: invokevirtual 76	android/os/Parcel:recycle	()V
        //   65: return
        //   66: aload_3
        //   67: iconst_0
        //   68: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   71: goto -36 -> 35
        //   74: astore 5
        //   76: aload 4
        //   78: invokevirtual 76	android/os/Parcel:recycle	()V
        //   81: aload_3
        //   82: invokevirtual 76	android/os/Parcel:recycle	()V
        //   85: aload 5
        //   87: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	88	0	this	a
        //   0	88	1	paramString	String
        //   0	88	2	paramRegisterCorpusInfo	RegisterCorpusInfo
        //   3	79	3	localParcel1	Parcel
        //   7	70	4	localParcel2	Parcel
        //   74	12	5	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   9	20	74	finally
        //   24	35	74	finally
        //   35	56	74	finally
        //   66	71	74	finally
      }
      
      public boolean a(r paramr)
        throws RemoteException
      {
        boolean bool = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
            if (paramr != null)
            {
              localParcel1.writeInt(1);
              paramr.writeToParcel(localParcel1, 0);
              this.dG.transact(19, localParcel1, localParcel2, 0);
              localParcel2.readException();
              int i = localParcel2.readInt();
              if (i != 0) {
                return bool;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            bool = false;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      public boolean a(String paramString1, String paramString2, long paramLong, RequestIndexingSpecification paramRequestIndexingSpecification)
        throws RemoteException
      {
        boolean bool = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
            localParcel1.writeString(paramString1);
            localParcel1.writeString(paramString2);
            localParcel1.writeLong(paramLong);
            if (paramRequestIndexingSpecification != null)
            {
              localParcel1.writeInt(1);
              paramRequestIndexingSpecification.writeToParcel(localParcel1, 0);
              this.dG.transact(4, localParcel1, localParcel2, 0);
              localParcel2.readException();
              int i = localParcel2.readInt();
              if (i != 0) {
                return bool;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            bool = false;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      public GlobalSearchApplicationInfo[] aG()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          this.dG.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          GlobalSearchApplicationInfo[] arrayOfGlobalSearchApplicationInfo = (GlobalSearchApplicationInfo[])localParcel2.createTypedArray(GlobalSearchApplicationInfo.CREATOR);
          return arrayOfGlobalSearchApplicationInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return this.dG;
      }
      
      public boolean b(String paramString, RegisterCorpusInfo paramRegisterCorpusInfo)
        throws RemoteException
      {
        boolean bool = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
            localParcel1.writeString(paramString);
            if (paramRegisterCorpusInfo != null)
            {
              localParcel1.writeInt(1);
              paramRegisterCorpusInfo.writeToParcel(localParcel1, 0);
              this.dG.transact(14, localParcel1, localParcel2, 0);
              localParcel2.readException();
              int i = localParcel2.readInt();
              if (i != 0) {
                return bool;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            bool = false;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      public void blockPackages(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          localParcel1.writeStringArray(paramArrayOfString);
          this.dG.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public CorpusStatus d(String paramString1, String paramString2)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_3
        //   4: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   7: astore 4
        //   9: aload_3
        //   10: ldc 27
        //   12: invokevirtual 31	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   15: aload_3
        //   16: aload_1
        //   17: invokevirtual 38	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   20: aload_3
        //   21: aload_2
        //   22: invokevirtual 38	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/gms/internal/cx$a$a:dG	Landroid/os/IBinder;
        //   29: iconst_5
        //   30: aload_3
        //   31: aload 4
        //   33: iconst_0
        //   34: invokeinterface 54 5 0
        //   39: pop
        //   40: aload 4
        //   42: invokevirtual 57	android/os/Parcel:readException	()V
        //   45: aload 4
        //   47: invokevirtual 61	android/os/Parcel:readInt	()I
        //   50: ifeq +29 -> 79
        //   53: getstatic 166	com/google/android/gms/appdatasearch/CorpusStatus:CREATOR	Lcom/google/android/gms/appdatasearch/c;
        //   56: aload 4
        //   58: invokevirtual 172	com/google/android/gms/appdatasearch/c:j	(Landroid/os/Parcel;)Lcom/google/android/gms/appdatasearch/CorpusStatus;
        //   61: astore 8
        //   63: aload 8
        //   65: astore 7
        //   67: aload 4
        //   69: invokevirtual 76	android/os/Parcel:recycle	()V
        //   72: aload_3
        //   73: invokevirtual 76	android/os/Parcel:recycle	()V
        //   76: aload 7
        //   78: areturn
        //   79: aconst_null
        //   80: astore 7
        //   82: goto -15 -> 67
        //   85: astore 5
        //   87: aload 4
        //   89: invokevirtual 76	android/os/Parcel:recycle	()V
        //   92: aload_3
        //   93: invokevirtual 76	android/os/Parcel:recycle	()V
        //   96: aload 5
        //   98: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	99	0	this	a
        //   0	99	1	paramString1	String
        //   0	99	2	paramString2	String
        //   3	90	3	localParcel1	Parcel
        //   7	81	4	localParcel2	Parcel
        //   85	12	5	localObject	Object
        //   65	16	7	localCorpusStatus1	CorpusStatus
        //   61	3	8	localCorpusStatus2	CorpusStatus
        // Exception table:
        //   from	to	target	type
        //   9	63	85	finally
      }
      
      public Bundle diagnostic(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
            if (paramBundle != null)
            {
              localParcel1.writeInt(1);
              paramBundle.writeToParcel(localParcel1, 0);
              this.dG.transact(9, localParcel1, localParcel2, 0);
              localParcel2.readException();
              if (localParcel2.readInt() != 0)
              {
                localBundle = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
                return localBundle;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            Bundle localBundle = null;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      /* Error */
      public Bundle e(String paramString1, String paramString2)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_3
        //   4: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   7: astore 4
        //   9: aload_3
        //   10: ldc 27
        //   12: invokevirtual 31	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   15: aload_3
        //   16: aload_1
        //   17: invokevirtual 38	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   20: aload_3
        //   21: aload_2
        //   22: invokevirtual 38	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/gms/internal/cx$a$a:dG	Landroid/os/IBinder;
        //   29: bipush 8
        //   31: aload_3
        //   32: aload 4
        //   34: iconst_0
        //   35: invokeinterface 54 5 0
        //   40: pop
        //   41: aload 4
        //   43: invokevirtual 57	android/os/Parcel:readException	()V
        //   46: aload 4
        //   48: invokevirtual 61	android/os/Parcel:readInt	()I
        //   51: ifeq +30 -> 81
        //   54: getstatic 180	android/os/Bundle:CREATOR	Landroid/os/Parcelable$Creator;
        //   57: aload 4
        //   59: invokeinterface 186 2 0
        //   64: checkcast 176	android/os/Bundle
        //   67: astore 7
        //   69: aload 4
        //   71: invokevirtual 76	android/os/Parcel:recycle	()V
        //   74: aload_3
        //   75: invokevirtual 76	android/os/Parcel:recycle	()V
        //   78: aload 7
        //   80: areturn
        //   81: aconst_null
        //   82: astore 7
        //   84: goto -15 -> 69
        //   87: astore 5
        //   89: aload 4
        //   91: invokevirtual 76	android/os/Parcel:recycle	()V
        //   94: aload_3
        //   95: invokevirtual 76	android/os/Parcel:recycle	()V
        //   98: aload 5
        //   100: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	101	0	this	a
        //   0	101	1	paramString1	String
        //   0	101	2	paramString2	String
        //   3	92	3	localParcel1	Parcel
        //   7	83	4	localParcel2	Parcel
        //   87	12	5	localObject	Object
        //   67	16	7	localBundle	Bundle
        // Exception table:
        //   from	to	target	type
        //   9	69	87	finally
      }
      
      /* Error */
      public RegisterCorpusInfo f(String paramString1, String paramString2)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_3
        //   4: invokestatic 25	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   7: astore 4
        //   9: aload_3
        //   10: ldc 27
        //   12: invokevirtual 31	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   15: aload_3
        //   16: aload_1
        //   17: invokevirtual 38	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   20: aload_3
        //   21: aload_2
        //   22: invokevirtual 38	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/gms/internal/cx$a$a:dG	Landroid/os/IBinder;
        //   29: bipush 25
        //   31: aload_3
        //   32: aload 4
        //   34: iconst_0
        //   35: invokeinterface 54 5 0
        //   40: pop
        //   41: aload 4
        //   43: invokevirtual 57	android/os/Parcel:readException	()V
        //   46: aload 4
        //   48: invokevirtual 61	android/os/Parcel:readInt	()I
        //   51: ifeq +29 -> 80
        //   54: getstatic 193	com/google/android/gms/appdatasearch/RegisterCorpusInfo:CREATOR	Lcom/google/android/gms/appdatasearch/n;
        //   57: aload 4
        //   59: invokevirtual 199	com/google/android/gms/appdatasearch/n:u	(Landroid/os/Parcel;)Lcom/google/android/gms/appdatasearch/RegisterCorpusInfo;
        //   62: astore 8
        //   64: aload 8
        //   66: astore 7
        //   68: aload 4
        //   70: invokevirtual 76	android/os/Parcel:recycle	()V
        //   73: aload_3
        //   74: invokevirtual 76	android/os/Parcel:recycle	()V
        //   77: aload 7
        //   79: areturn
        //   80: aconst_null
        //   81: astore 7
        //   83: goto -15 -> 68
        //   86: astore 5
        //   88: aload 4
        //   90: invokevirtual 76	android/os/Parcel:recycle	()V
        //   93: aload_3
        //   94: invokevirtual 76	android/os/Parcel:recycle	()V
        //   97: aload 5
        //   99: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	100	0	this	a
        //   0	100	1	paramString1	String
        //   0	100	2	paramString2	String
        //   3	91	3	localParcel1	Parcel
        //   7	82	4	localParcel2	Parcel
        //   86	12	5	localObject	Object
        //   66	16	7	localRegisterCorpusInfo1	RegisterCorpusInfo
        //   62	3	8	localRegisterCorpusInfo2	RegisterCorpusInfo
        // Exception table:
        //   from	to	target	type
        //   9	64	86	finally
      }
      
      public PhraseAffinityResponse getPhraseAffinity(String[] paramArrayOfString, PhraseAffinitySpecification paramPhraseAffinitySpecification)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
            localParcel1.writeStringArray(paramArrayOfString);
            if (paramPhraseAffinitySpecification != null)
            {
              localParcel1.writeInt(1);
              paramPhraseAffinitySpecification.writeToParcel(localParcel1, 0);
              this.dG.transact(24, localParcel1, localParcel2, 0);
              localParcel2.readException();
              if (localParcel2.readInt() != 0)
              {
                PhraseAffinityResponse localPhraseAffinityResponse2 = PhraseAffinityResponse.CREATOR.r(localParcel2);
                localPhraseAffinityResponse1 = localPhraseAffinityResponse2;
                return localPhraseAffinityResponse1;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            PhraseAffinityResponse localPhraseAffinityResponse1 = null;
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
        }
      }
      
      public RegisteredPackageInfo[] getRegisteredPackageInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          this.dG.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          RegisteredPackageInfo[] arrayOfRegisteredPackageInfo = (RegisteredPackageInfo[])localParcel2.createTypedArray(RegisteredPackageInfo.CREATOR);
          return arrayOfRegisteredPackageInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] s(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          localParcel1.writeString(paramString);
          this.dG.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void t(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          localParcel1.writeString(paramString);
          this.dG.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void triggerCompaction()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          this.dG.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean u(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          localParcel1.writeString(paramString);
          this.dG.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          boolean bool = false;
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unblockPackages(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          localParcel1.writeStringArray(paramArrayOfString);
          this.dG.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void v(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          localParcel1.writeString(paramString);
          this.dG.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] w(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          localParcel1.writeString(paramString);
          this.dG.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] x(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.IAppDataSearch");
          localParcel1.writeString(paramString);
          this.dG.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.cx
 * JD-Core Version:    0.7.0.1
 */