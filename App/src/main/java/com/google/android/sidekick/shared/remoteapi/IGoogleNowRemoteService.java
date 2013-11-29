package com.google.android.sidekick.shared.remoteapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IGoogleNowRemoteService
  extends IInterface
{
  public abstract Bitmap blockingGetImage(Uri paramUri, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean canUserOptIn(String paramString)
    throws RemoteException;
  
  public abstract boolean createReminder(ProtoParcelable paramProtoParcelable)
    throws RemoteException;
  
  public abstract void deleteNotificationsForEntry(ProtoParcelable paramProtoParcelable)
    throws RemoteException;
  
  public abstract void dismissEntry(ProtoParcelable paramProtoParcelable, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean enableSearchHistoryForActiveAccount()
    throws RemoteException;
  
  public abstract CardsResponse getCards()
    throws RemoteException;
  
  public abstract Bundle getConfiguration()
    throws RemoteException;
  
  public abstract Intent getHelpIntent(String paramString)
    throws RemoteException;
  
  public abstract Bitmap getSampleMap()
    throws RemoteException;
  
  public abstract Bitmap getStaticMap(Location paramLocation, ProtoParcelable paramProtoParcelable, boolean paramBoolean)
    throws RemoteException;
  
  public abstract String getVersion()
    throws RemoteException;
  
  public abstract void invalidateEntries()
    throws RemoteException;
  
  public abstract boolean isReminderSmartActionSupported(int paramInt)
    throws RemoteException;
  
  public abstract boolean isUserOptedIn()
    throws RemoteException;
  
  public abstract void logAction(LoggingRequest paramLoggingRequest)
    throws RemoteException;
  
  public abstract void markCalendarEntryDismissed(long paramLong)
    throws RemoteException;
  
  public abstract boolean optIn(String paramString)
    throws RemoteException;
  
  public abstract void optIntoLocationReporting()
    throws RemoteException;
  
  public abstract boolean placeholder1(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void prefetchImage(Uri paramUri)
    throws RemoteException;
  
  public abstract Intent preparePhotoGalleryIntent(List<ProtoParcelable> paramList, int paramInt)
    throws RemoteException;
  
  public abstract void queueDismissEntryAction(ProtoParcelable paramProtoParcelable, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void recordBackOfCardShown()
    throws RemoteException;
  
  public abstract void recordCardSwipedForDismiss()
    throws RemoteException;
  
  public abstract void recordExecutedUserActions(List<ProtoParcelable> paramList)
    throws RemoteException;
  
  public abstract void recordFeedbackPromptAction(ProtoParcelable paramProtoParcelable, int paramInt)
    throws RemoteException;
  
  public abstract void recordFirstUseCardDismiss(int paramInt)
    throws RemoteException;
  
  public abstract void recordFirstUseCardView(int paramInt)
    throws RemoteException;
  
  public abstract void recordGoogleNowPromoDismissed()
    throws RemoteException;
  
  public abstract void recordPredictiveInteraction()
    throws RemoteException;
  
  public abstract void recordUserAction(ProtoParcelable paramProtoParcelable, int paramInt)
    throws RemoteException;
  
  public abstract void recordViewAction(ProtoParcelable paramProtoParcelable, long paramLong, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void refreshEntries()
    throws RemoteException;
  
  public abstract void removeGroupChildEntry(ProtoParcelable paramProtoParcelable1, ProtoParcelable paramProtoParcelable2)
    throws RemoteException;
  
  public abstract List<TrainingQuestionNode> resolveTrainingQuestions(List<ProtoParcelable> paramList)
    throws RemoteException;
  
  public abstract void savePreferences(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void sendPendingTrainingAnswers()
    throws RemoteException;
  
  public abstract void sendTrainingAction(ProtoParcelable paramProtoParcelable1, ProtoParcelable paramProtoParcelable2, ProtoParcelable paramProtoParcelable3)
    throws RemoteException;
  
  public abstract void setTrafficSharerHiddenState(long paramLong, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setTrainingAnswer(ProtoParcelable paramProtoParcelable1, ProtoParcelable paramProtoParcelable2, ProtoParcelable paramProtoParcelable3)
    throws RemoteException;
  
  public abstract void snoozeReminder(ProtoParcelable paramProtoParcelable)
    throws RemoteException;
  
  public abstract String translateInPlace(String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGoogleNowRemoteService
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
    }
    
    public static IGoogleNowRemoteService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
      if ((localIInterface != null) && ((localIInterface instanceof IGoogleNowRemoteService))) {
        return (IGoogleNowRemoteService)localIInterface;
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
        paramParcel2.writeString("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        return true;
      case 1: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        String str2 = getVersion();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str2);
        return true;
      case 2: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        boolean bool14 = canUserOptIn(paramParcel1.readString());
        paramParcel2.writeNoException();
        if (bool14) {}
        for (int i3 = 1;; i3 = 0)
        {
          paramParcel2.writeInt(i3);
          return true;
        }
      case 3: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        boolean bool13 = optIn(paramParcel1.readString());
        paramParcel2.writeNoException();
        if (bool13) {}
        for (int i2 = 1;; i2 = 0)
        {
          paramParcel2.writeInt(i2);
          return true;
        }
      case 4: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        boolean bool12 = isUserOptedIn();
        paramParcel2.writeNoException();
        if (bool12) {}
        for (int i1 = 1;; i1 = 0)
        {
          paramParcel2.writeInt(i1);
          return true;
        }
      case 5: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        recordGoogleNowPromoDismissed();
        paramParcel2.writeNoException();
        return true;
      case 6: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        CardsResponse localCardsResponse = getCards();
        paramParcel2.writeNoException();
        if (localCardsResponse != null)
        {
          paramParcel2.writeInt(1);
          localCardsResponse.writeToParcel(paramParcel2, 1);
        }
        for (;;)
        {
          return true;
          paramParcel2.writeInt(0);
        }
      case 7: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        ProtoParcelable localProtoParcelable17;
        long l2;
        int n;
        if (paramParcel1.readInt() != 0)
        {
          localProtoParcelable17 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);
          l2 = paramParcel1.readLong();
          n = paramParcel1.readInt();
          if (paramParcel1.readInt() == 0) {
            break label656;
          }
        }
        for (boolean bool11 = true;; bool11 = false)
        {
          recordViewAction(localProtoParcelable17, l2, n, bool11);
          return true;
          localProtoParcelable17 = null;
          break;
        }
      case 8: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        if (paramParcel1.readInt() != 0) {}
        for (ProtoParcelable localProtoParcelable16 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);; localProtoParcelable16 = null)
        {
          recordUserAction(localProtoParcelable16, paramParcel1.readInt());
          return true;
        }
      case 9: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        if (paramParcel1.readInt() != 0) {}
        for (LoggingRequest localLoggingRequest = (LoggingRequest)LoggingRequest.CREATOR.createFromParcel(paramParcel1);; localLoggingRequest = null)
        {
          logAction(localLoggingRequest);
          return true;
        }
      case 10: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        markCalendarEntryDismissed(paramParcel1.readLong());
        return true;
      case 11: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        ProtoParcelable localProtoParcelable15;
        if (paramParcel1.readInt() != 0)
        {
          localProtoParcelable15 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label817;
          }
        }
        for (boolean bool10 = true;; bool10 = false)
        {
          dismissEntry(localProtoParcelable15, bool10);
          return true;
          localProtoParcelable15 = null;
          break;
        }
      case 12: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        ProtoParcelable localProtoParcelable13;
        if (paramParcel1.readInt() != 0)
        {
          localProtoParcelable13 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label887;
          }
        }
        for (ProtoParcelable localProtoParcelable14 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);; localProtoParcelable14 = null)
        {
          removeGroupChildEntry(localProtoParcelable13, localProtoParcelable14);
          return true;
          localProtoParcelable13 = null;
          break;
        }
      case 13: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        Location localLocation;
        ProtoParcelable localProtoParcelable12;
        boolean bool9;
        if (paramParcel1.readInt() != 0)
        {
          localLocation = (Location)Location.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label992;
          }
          localProtoParcelable12 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label998;
          }
          bool9 = true;
          Bitmap localBitmap3 = getStaticMap(localLocation, localProtoParcelable12, bool9);
          paramParcel2.writeNoException();
          if (localBitmap3 == null) {
            break label1004;
          }
          paramParcel2.writeInt(1);
          localBitmap3.writeToParcel(paramParcel2, 1);
        }
        for (;;)
        {
          return true;
          localLocation = null;
          break;
          localProtoParcelable12 = null;
          break label941;
          bool9 = false;
          break label951;
          paramParcel2.writeInt(0);
        }
      case 14: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        Bitmap localBitmap2 = getSampleMap();
        paramParcel2.writeNoException();
        if (localBitmap2 != null)
        {
          paramParcel2.writeInt(1);
          localBitmap2.writeToParcel(paramParcel2, 1);
        }
        for (;;)
        {
          return true;
          paramParcel2.writeInt(0);
        }
      case 15: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        if (paramParcel1.readInt() != 0) {}
        for (Bundle localBundle2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; localBundle2 = null)
        {
          savePreferences(localBundle2);
          return true;
        }
      case 16: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        long l1 = paramParcel1.readLong();
        if (paramParcel1.readInt() != 0) {}
        for (boolean bool8 = true;; bool8 = false)
        {
          setTrafficSharerHiddenState(l1, bool8);
          return true;
        }
      case 17: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        List localList = resolveTrainingQuestions(paramParcel1.createTypedArrayList(ProtoParcelable.CREATOR));
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(localList);
        return true;
      case 18: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        ProtoParcelable localProtoParcelable9;
        ProtoParcelable localProtoParcelable10;
        if (paramParcel1.readInt() != 0)
        {
          localProtoParcelable9 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label1252;
          }
          localProtoParcelable10 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label1258;
          }
        }
        for (ProtoParcelable localProtoParcelable11 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);; localProtoParcelable11 = null)
        {
          setTrainingAnswer(localProtoParcelable9, localProtoParcelable10, localProtoParcelable11);
          return true;
          localProtoParcelable9 = null;
          break;
          localProtoParcelable10 = null;
          break label1213;
        }
      case 19: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        sendPendingTrainingAnswers();
        return true;
      case 20: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        ProtoParcelable localProtoParcelable6;
        ProtoParcelable localProtoParcelable7;
        if (paramParcel1.readInt() != 0)
        {
          localProtoParcelable6 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label1363;
          }
          localProtoParcelable7 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label1369;
          }
        }
        for (ProtoParcelable localProtoParcelable8 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);; localProtoParcelable8 = null)
        {
          sendTrainingAction(localProtoParcelable6, localProtoParcelable7, localProtoParcelable8);
          return true;
          localProtoParcelable6 = null;
          break;
          localProtoParcelable7 = null;
          break label1324;
        }
      case 21: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        optIntoLocationReporting();
        return true;
      case 22: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        boolean bool6;
        if (paramParcel1.readInt() != 0)
        {
          bool6 = true;
          boolean bool7 = placeholder1(bool6);
          paramParcel2.writeNoException();
          if (!bool7) {
            break label1437;
          }
        }
        for (int m = 1;; m = 0)
        {
          paramParcel2.writeInt(m);
          return true;
          bool6 = false;
          break;
        }
      case 23: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        if (paramParcel1.readInt() != 0) {}
        for (Uri localUri2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);; localUri2 = null)
        {
          prefetchImage(localUri2);
          return true;
        }
      case 24: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        Uri localUri1;
        boolean bool5;
        if (paramParcel1.readInt() != 0)
        {
          localUri1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          if (paramParcel1.readInt() == 0) {
            break label1560;
          }
          bool5 = true;
          Bitmap localBitmap1 = blockingGetImage(localUri1, bool5);
          paramParcel2.writeNoException();
          if (localBitmap1 == null) {
            break label1566;
          }
          paramParcel2.writeInt(1);
          localBitmap1.writeToParcel(paramParcel2, 1);
        }
        for (;;)
        {
          return true;
          localUri1 = null;
          break;
          bool5 = false;
          break label1521;
          paramParcel2.writeInt(0);
        }
      case 25: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        Intent localIntent2 = preparePhotoGalleryIntent(paramParcel1.createTypedArrayList(ProtoParcelable.CREATOR), paramParcel1.readInt());
        paramParcel2.writeNoException();
        if (localIntent2 != null)
        {
          paramParcel2.writeInt(1);
          localIntent2.writeToParcel(paramParcel2, 1);
        }
        for (;;)
        {
          return true;
          paramParcel2.writeInt(0);
        }
      case 26: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        ProtoParcelable localProtoParcelable5;
        if (paramParcel1.readInt() != 0)
        {
          localProtoParcelable5 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);
          boolean bool4 = createReminder(localProtoParcelable5);
          paramParcel2.writeNoException();
          if (!bool4) {
            break label1689;
          }
        }
        for (int k = 1;; k = 0)
        {
          paramParcel2.writeInt(k);
          return true;
          localProtoParcelable5 = null;
          break;
        }
      case 27: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        if (paramParcel1.readInt() != 0) {}
        for (ProtoParcelable localProtoParcelable4 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);; localProtoParcelable4 = null)
        {
          snoozeReminder(localProtoParcelable4);
          return true;
        }
      case 28: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        boolean bool3 = isReminderSmartActionSupported(paramParcel1.readInt());
        paramParcel2.writeNoException();
        if (bool3) {}
        for (int j = 1;; j = 0)
        {
          paramParcel2.writeInt(j);
          return true;
        }
      case 29: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        invalidateEntries();
        return true;
      case 30: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        String str1 = translateInPlace(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeString(str1);
        return true;
      case 31: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        recordFirstUseCardView(paramParcel1.readInt());
        return true;
      case 32: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        recordFirstUseCardDismiss(paramParcel1.readInt());
        return true;
      case 33: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        recordBackOfCardShown();
        return true;
      case 34: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        recordCardSwipedForDismiss();
        return true;
      case 35: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        boolean bool2 = enableSearchHistoryForActiveAccount();
        paramParcel2.writeNoException();
        if (bool2) {}
        for (int i = 1;; i = 0)
        {
          paramParcel2.writeInt(i);
          return true;
        }
      case 36: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        Bundle localBundle1 = getConfiguration();
        paramParcel2.writeNoException();
        if (localBundle1 != null)
        {
          paramParcel2.writeInt(1);
          localBundle1.writeToParcel(paramParcel2, 1);
        }
        for (;;)
        {
          return true;
          paramParcel2.writeInt(0);
        }
      case 37: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        if (paramParcel1.readInt() != 0) {}
        for (ProtoParcelable localProtoParcelable3 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);; localProtoParcelable3 = null)
        {
          deleteNotificationsForEntry(localProtoParcelable3);
          return true;
        }
      case 38: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        recordPredictiveInteraction();
        return true;
      case 39: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        recordExecutedUserActions(paramParcel1.createTypedArrayList(ProtoParcelable.CREATOR));
        return true;
      case 40: 
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        refreshEntries();
        return true;
      case 41: 
        label1324:
        label1363:
        label1369:
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        label1437:
        label1566:
        Intent localIntent1 = getHelpIntent(paramParcel1.readString());
        label1560:
        label1689:
        paramParcel2.writeNoException();
        if (localIntent1 != null)
        {
          paramParcel2.writeInt(1);
          localIntent1.writeToParcel(paramParcel2, 1);
        }
        for (;;)
        {
          return true;
          paramParcel2.writeInt(0);
        }
      case 42: 
        label656:
        label817:
        label887:
        paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
        label941:
        label951:
        label992:
        label998:
        label1004:
        label1521:
        if (paramParcel1.readInt() != 0) {}
        label1213:
        label1252:
        label1258:
        for (ProtoParcelable localProtoParcelable2 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);; localProtoParcelable2 = null)
        {
          recordFeedbackPromptAction(localProtoParcelable2, paramParcel1.readInt());
          return true;
        }
      }
      paramParcel1.enforceInterface("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
      ProtoParcelable localProtoParcelable1;
      if (paramParcel1.readInt() != 0)
      {
        localProtoParcelable1 = (ProtoParcelable)ProtoParcelable.CREATOR.createFromParcel(paramParcel1);
        if (paramParcel1.readInt() == 0) {
          break label2192;
        }
      }
      label2192:
      for (boolean bool1 = true;; bool1 = false)
      {
        queueDismissEntryAction(localProtoParcelable1, bool1);
        return true;
        localProtoParcelable1 = null;
        break;
      }
    }
    
    private static class Proxy
      implements IGoogleNowRemoteService
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
      
      public Bitmap blockingGetImage(Uri paramUri, boolean paramBoolean)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          Bitmap localBitmap;
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
            if (paramUri != null)
            {
              localParcel1.writeInt(1);
              paramUri.writeToParcel(localParcel1, 0);
              break label138;
              localParcel1.writeInt(i);
              this.mRemote.transact(24, localParcel1, localParcel2, 0);
              localParcel2.readException();
              if (localParcel2.readInt() != 0)
              {
                localBitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(localParcel2);
                label90:
                return localBitmap;
              }
            }
            else
            {
              localParcel1.writeInt(0);
            }
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          label138:
          do
          {
            i = 0;
            break;
            localBitmap = null;
            break label90;
          } while (!paramBoolean);
        }
      }
      
      public boolean canUserOptIn(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          localParcel1.writeString(paramString);
          this.mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public boolean createReminder(ProtoParcelable paramProtoParcelable)
        throws RemoteException
      {
        boolean bool = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
            if (paramProtoParcelable != null)
            {
              localParcel1.writeInt(1);
              paramProtoParcelable.writeToParcel(localParcel1, 0);
              this.mRemote.transact(26, localParcel1, localParcel2, 0);
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
      
      /* Error */
      public void deleteNotificationsForEntry(ProtoParcelable paramProtoParcelable)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_2
        //   4: aload_2
        //   5: ldc 29
        //   7: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aload_1
        //   11: ifnull +34 -> 45
        //   14: aload_2
        //   15: iconst_1
        //   16: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   19: aload_1
        //   20: aload_2
        //   21: iconst_0
        //   22: invokevirtual 81	com/google/android/sidekick/shared/remoteapi/ProtoParcelable:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   29: bipush 37
        //   31: aload_2
        //   32: aconst_null
        //   33: iconst_1
        //   34: invokeinterface 49 5 0
        //   39: pop
        //   40: aload_2
        //   41: invokevirtual 71	android/os/Parcel:recycle	()V
        //   44: return
        //   45: aload_2
        //   46: iconst_0
        //   47: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   50: goto -25 -> 25
        //   53: astore_3
        //   54: aload_2
        //   55: invokevirtual 71	android/os/Parcel:recycle	()V
        //   58: aload_3
        //   59: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	60	0	this	Proxy
        //   0	60	1	paramProtoParcelable	ProtoParcelable
        //   3	52	2	localParcel	Parcel
        //   53	6	3	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   4	10	53	finally
        //   14	25	53	finally
        //   25	40	53	finally
        //   45	50	53	finally
      }
      
      public void dismissEntry(ProtoParcelable paramProtoParcelable, boolean paramBoolean)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
            if (paramProtoParcelable != null)
            {
              localParcel.writeInt(1);
              paramProtoParcelable.writeToParcel(localParcel, 0);
              break label86;
              localParcel.writeInt(i);
              this.mRemote.transact(11, localParcel, null, 1);
            }
            else
            {
              localParcel.writeInt(0);
            }
          }
          finally
          {
            localParcel.recycle();
          }
          label86:
          while (!paramBoolean)
          {
            i = 0;
            break;
          }
        }
      }
      
      public boolean enableSearchHistoryForActiveAccount()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(35, localParcel1, localParcel2, 0);
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
      
      /* Error */
      public CardsResponse getCards()
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_1
        //   4: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   7: astore_2
        //   8: aload_1
        //   9: ldc 29
        //   11: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   14: aload_0
        //   15: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   18: bipush 6
        //   20: aload_1
        //   21: aload_2
        //   22: iconst_0
        //   23: invokeinterface 49 5 0
        //   28: pop
        //   29: aload_2
        //   30: invokevirtual 52	android/os/Parcel:readException	()V
        //   33: aload_2
        //   34: invokevirtual 56	android/os/Parcel:readInt	()I
        //   37: ifeq +28 -> 65
        //   40: getstatic 92	com/google/android/sidekick/shared/remoteapi/CardsResponse:CREATOR	Landroid/os/Parcelable$Creator;
        //   43: aload_2
        //   44: invokeinterface 68 2 0
        //   49: checkcast 91	com/google/android/sidekick/shared/remoteapi/CardsResponse
        //   52: astore 5
        //   54: aload_2
        //   55: invokevirtual 71	android/os/Parcel:recycle	()V
        //   58: aload_1
        //   59: invokevirtual 71	android/os/Parcel:recycle	()V
        //   62: aload 5
        //   64: areturn
        //   65: aconst_null
        //   66: astore 5
        //   68: goto -14 -> 54
        //   71: astore_3
        //   72: aload_2
        //   73: invokevirtual 71	android/os/Parcel:recycle	()V
        //   76: aload_1
        //   77: invokevirtual 71	android/os/Parcel:recycle	()V
        //   80: aload_3
        //   81: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	82	0	this	Proxy
        //   3	74	1	localParcel1	Parcel
        //   7	66	2	localParcel2	Parcel
        //   71	10	3	localObject	Object
        //   52	15	5	localCardsResponse	CardsResponse
        // Exception table:
        //   from	to	target	type
        //   8	54	71	finally
      }
      
      /* Error */
      public Bundle getConfiguration()
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_1
        //   4: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   7: astore_2
        //   8: aload_1
        //   9: ldc 29
        //   11: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   14: aload_0
        //   15: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   18: bipush 36
        //   20: aload_1
        //   21: aload_2
        //   22: iconst_0
        //   23: invokeinterface 49 5 0
        //   28: pop
        //   29: aload_2
        //   30: invokevirtual 52	android/os/Parcel:readException	()V
        //   33: aload_2
        //   34: invokevirtual 56	android/os/Parcel:readInt	()I
        //   37: ifeq +28 -> 65
        //   40: getstatic 97	android/os/Bundle:CREATOR	Landroid/os/Parcelable$Creator;
        //   43: aload_2
        //   44: invokeinterface 68 2 0
        //   49: checkcast 96	android/os/Bundle
        //   52: astore 5
        //   54: aload_2
        //   55: invokevirtual 71	android/os/Parcel:recycle	()V
        //   58: aload_1
        //   59: invokevirtual 71	android/os/Parcel:recycle	()V
        //   62: aload 5
        //   64: areturn
        //   65: aconst_null
        //   66: astore 5
        //   68: goto -14 -> 54
        //   71: astore_3
        //   72: aload_2
        //   73: invokevirtual 71	android/os/Parcel:recycle	()V
        //   76: aload_1
        //   77: invokevirtual 71	android/os/Parcel:recycle	()V
        //   80: aload_3
        //   81: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	82	0	this	Proxy
        //   3	74	1	localParcel1	Parcel
        //   7	66	2	localParcel2	Parcel
        //   71	10	3	localObject	Object
        //   52	15	5	localBundle	Bundle
        // Exception table:
        //   from	to	target	type
        //   8	54	71	finally
      }
      
      /* Error */
      public Intent getHelpIntent(String paramString)
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
        //   14: aload_2
        //   15: aload_1
        //   16: invokevirtual 76	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   19: aload_0
        //   20: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   23: bipush 41
        //   25: aload_2
        //   26: aload_3
        //   27: iconst_0
        //   28: invokeinterface 49 5 0
        //   33: pop
        //   34: aload_3
        //   35: invokevirtual 52	android/os/Parcel:readException	()V
        //   38: aload_3
        //   39: invokevirtual 56	android/os/Parcel:readInt	()I
        //   42: ifeq +28 -> 70
        //   45: getstatic 102	android/content/Intent:CREATOR	Landroid/os/Parcelable$Creator;
        //   48: aload_3
        //   49: invokeinterface 68 2 0
        //   54: checkcast 101	android/content/Intent
        //   57: astore 6
        //   59: aload_3
        //   60: invokevirtual 71	android/os/Parcel:recycle	()V
        //   63: aload_2
        //   64: invokevirtual 71	android/os/Parcel:recycle	()V
        //   67: aload 6
        //   69: areturn
        //   70: aconst_null
        //   71: astore 6
        //   73: goto -14 -> 59
        //   76: astore 4
        //   78: aload_3
        //   79: invokevirtual 71	android/os/Parcel:recycle	()V
        //   82: aload_2
        //   83: invokevirtual 71	android/os/Parcel:recycle	()V
        //   86: aload 4
        //   88: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	89	0	this	Proxy
        //   0	89	1	paramString	String
        //   3	80	2	localParcel1	Parcel
        //   7	72	3	localParcel2	Parcel
        //   76	11	4	localObject	Object
        //   57	15	6	localIntent	Intent
        // Exception table:
        //   from	to	target	type
        //   8	59	76	finally
      }
      
      /* Error */
      public Bitmap getSampleMap()
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_1
        //   4: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   7: astore_2
        //   8: aload_1
        //   9: ldc 29
        //   11: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   14: aload_0
        //   15: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   18: bipush 14
        //   20: aload_1
        //   21: aload_2
        //   22: iconst_0
        //   23: invokeinterface 49 5 0
        //   28: pop
        //   29: aload_2
        //   30: invokevirtual 52	android/os/Parcel:readException	()V
        //   33: aload_2
        //   34: invokevirtual 56	android/os/Parcel:readInt	()I
        //   37: ifeq +28 -> 65
        //   40: getstatic 62	android/graphics/Bitmap:CREATOR	Landroid/os/Parcelable$Creator;
        //   43: aload_2
        //   44: invokeinterface 68 2 0
        //   49: checkcast 58	android/graphics/Bitmap
        //   52: astore 5
        //   54: aload_2
        //   55: invokevirtual 71	android/os/Parcel:recycle	()V
        //   58: aload_1
        //   59: invokevirtual 71	android/os/Parcel:recycle	()V
        //   62: aload 5
        //   64: areturn
        //   65: aconst_null
        //   66: astore 5
        //   68: goto -14 -> 54
        //   71: astore_3
        //   72: aload_2
        //   73: invokevirtual 71	android/os/Parcel:recycle	()V
        //   76: aload_1
        //   77: invokevirtual 71	android/os/Parcel:recycle	()V
        //   80: aload_3
        //   81: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	82	0	this	Proxy
        //   3	74	1	localParcel1	Parcel
        //   7	66	2	localParcel2	Parcel
        //   71	10	3	localObject	Object
        //   52	15	5	localBitmap	Bitmap
        // Exception table:
        //   from	to	target	type
        //   8	54	71	finally
      }
      
      public Bitmap getStaticMap(Location paramLocation, ProtoParcelable paramProtoParcelable, boolean paramBoolean)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        for (;;)
        {
          Bitmap localBitmap;
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
            if (paramLocation != null)
            {
              localParcel1.writeInt(1);
              paramLocation.writeToParcel(localParcel1, 0);
              if (paramProtoParcelable != null)
              {
                localParcel1.writeInt(1);
                paramProtoParcelable.writeToParcel(localParcel1, 0);
                break label167;
                localParcel1.writeInt(i);
                this.mRemote.transact(13, localParcel1, localParcel2, 0);
                localParcel2.readException();
                if (localParcel2.readInt() == 0) {
                  break label161;
                }
                localBitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(localParcel2);
                label109:
                return localBitmap;
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
            localParcel1.writeInt(0);
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          label161:
          label167:
          while (!paramBoolean)
          {
            i = 0;
            break;
            localBitmap = null;
            break label109;
          }
        }
      }
      
      public String getVersion()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void invalidateEntries()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(29, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean isReminderSmartActionSupported(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          localParcel1.writeInt(paramInt);
          this.mRemote.transact(28, localParcel1, localParcel2, 0);
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
      
      public boolean isUserOptedIn()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      /* Error */
      public void logAction(LoggingRequest paramLoggingRequest)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_2
        //   4: aload_2
        //   5: ldc 29
        //   7: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aload_1
        //   11: ifnull +34 -> 45
        //   14: aload_2
        //   15: iconst_1
        //   16: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   19: aload_1
        //   20: aload_2
        //   21: iconst_0
        //   22: invokevirtual 123	com/google/android/sidekick/shared/remoteapi/LoggingRequest:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   29: bipush 9
        //   31: aload_2
        //   32: aconst_null
        //   33: iconst_1
        //   34: invokeinterface 49 5 0
        //   39: pop
        //   40: aload_2
        //   41: invokevirtual 71	android/os/Parcel:recycle	()V
        //   44: return
        //   45: aload_2
        //   46: iconst_0
        //   47: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   50: goto -25 -> 25
        //   53: astore_3
        //   54: aload_2
        //   55: invokevirtual 71	android/os/Parcel:recycle	()V
        //   58: aload_3
        //   59: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	60	0	this	Proxy
        //   0	60	1	paramLoggingRequest	LoggingRequest
        //   3	52	2	localParcel	Parcel
        //   53	6	3	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   4	10	53	finally
        //   14	25	53	finally
        //   25	40	53	finally
        //   45	50	53	finally
      }
      
      public void markCalendarEntryDismissed(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          localParcel.writeLong(paramLong);
          this.mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean optIn(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          localParcel1.writeString(paramString);
          this.mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public void optIntoLocationReporting()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public boolean placeholder1(boolean paramBoolean)
        throws RemoteException
      {
        // Byte code:
        //   0: iconst_1
        //   1: istore_2
        //   2: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   5: astore_3
        //   6: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   9: astore 4
        //   11: aload_3
        //   12: ldc 29
        //   14: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: iload_1
        //   18: ifeq +56 -> 74
        //   21: iload_2
        //   22: istore 6
        //   24: aload_3
        //   25: iload 6
        //   27: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   30: aload_0
        //   31: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   34: bipush 22
        //   36: aload_3
        //   37: aload 4
        //   39: iconst_0
        //   40: invokeinterface 49 5 0
        //   45: pop
        //   46: aload 4
        //   48: invokevirtual 52	android/os/Parcel:readException	()V
        //   51: aload 4
        //   53: invokevirtual 56	android/os/Parcel:readInt	()I
        //   56: istore 8
        //   58: iload 8
        //   60: ifeq +20 -> 80
        //   63: aload 4
        //   65: invokevirtual 71	android/os/Parcel:recycle	()V
        //   68: aload_3
        //   69: invokevirtual 71	android/os/Parcel:recycle	()V
        //   72: iload_2
        //   73: ireturn
        //   74: iconst_0
        //   75: istore 6
        //   77: goto -53 -> 24
        //   80: iconst_0
        //   81: istore_2
        //   82: goto -19 -> 63
        //   85: astore 5
        //   87: aload 4
        //   89: invokevirtual 71	android/os/Parcel:recycle	()V
        //   92: aload_3
        //   93: invokevirtual 71	android/os/Parcel:recycle	()V
        //   96: aload 5
        //   98: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	99	0	this	Proxy
        //   0	99	1	paramBoolean	boolean
        //   1	81	2	i	int
        //   5	88	3	localParcel1	Parcel
        //   9	79	4	localParcel2	Parcel
        //   85	12	5	localObject	Object
        //   22	4	6	j	int
        //   75	1	6	k	int
        //   56	3	8	m	int
        // Exception table:
        //   from	to	target	type
        //   11	17	85	finally
        //   24	58	85	finally
      }
      
      /* Error */
      public void prefetchImage(Uri paramUri)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_2
        //   4: aload_2
        //   5: ldc 29
        //   7: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aload_1
        //   11: ifnull +34 -> 45
        //   14: aload_2
        //   15: iconst_1
        //   16: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   19: aload_1
        //   20: aload_2
        //   21: iconst_0
        //   22: invokevirtual 43	android/net/Uri:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   29: bipush 23
        //   31: aload_2
        //   32: aconst_null
        //   33: iconst_1
        //   34: invokeinterface 49 5 0
        //   39: pop
        //   40: aload_2
        //   41: invokevirtual 71	android/os/Parcel:recycle	()V
        //   44: return
        //   45: aload_2
        //   46: iconst_0
        //   47: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   50: goto -25 -> 25
        //   53: astore_3
        //   54: aload_2
        //   55: invokevirtual 71	android/os/Parcel:recycle	()V
        //   58: aload_3
        //   59: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	60	0	this	Proxy
        //   0	60	1	paramUri	Uri
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
      public Intent preparePhotoGalleryIntent(List<ProtoParcelable> paramList, int paramInt)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_3
        //   4: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   7: astore 4
        //   9: aload_3
        //   10: ldc 29
        //   12: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   15: aload_3
        //   16: aload_1
        //   17: invokevirtual 140	android/os/Parcel:writeTypedList	(Ljava/util/List;)V
        //   20: aload_3
        //   21: iload_2
        //   22: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   29: bipush 25
        //   31: aload_3
        //   32: aload 4
        //   34: iconst_0
        //   35: invokeinterface 49 5 0
        //   40: pop
        //   41: aload 4
        //   43: invokevirtual 52	android/os/Parcel:readException	()V
        //   46: aload 4
        //   48: invokevirtual 56	android/os/Parcel:readInt	()I
        //   51: ifeq +30 -> 81
        //   54: getstatic 102	android/content/Intent:CREATOR	Landroid/os/Parcelable$Creator;
        //   57: aload 4
        //   59: invokeinterface 68 2 0
        //   64: checkcast 101	android/content/Intent
        //   67: astore 7
        //   69: aload 4
        //   71: invokevirtual 71	android/os/Parcel:recycle	()V
        //   74: aload_3
        //   75: invokevirtual 71	android/os/Parcel:recycle	()V
        //   78: aload 7
        //   80: areturn
        //   81: aconst_null
        //   82: astore 7
        //   84: goto -15 -> 69
        //   87: astore 5
        //   89: aload 4
        //   91: invokevirtual 71	android/os/Parcel:recycle	()V
        //   94: aload_3
        //   95: invokevirtual 71	android/os/Parcel:recycle	()V
        //   98: aload 5
        //   100: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	101	0	this	Proxy
        //   0	101	1	paramList	List<ProtoParcelable>
        //   0	101	2	paramInt	int
        //   3	92	3	localParcel1	Parcel
        //   7	83	4	localParcel2	Parcel
        //   87	12	5	localObject	Object
        //   67	16	7	localIntent	Intent
        // Exception table:
        //   from	to	target	type
        //   9	69	87	finally
      }
      
      public void queueDismissEntryAction(ProtoParcelable paramProtoParcelable, boolean paramBoolean)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
            if (paramProtoParcelable != null)
            {
              localParcel.writeInt(1);
              paramProtoParcelable.writeToParcel(localParcel, 0);
              break label86;
              localParcel.writeInt(i);
              this.mRemote.transact(43, localParcel, null, 1);
            }
            else
            {
              localParcel.writeInt(0);
            }
          }
          finally
          {
            localParcel.recycle();
          }
          label86:
          while (!paramBoolean)
          {
            i = 0;
            break;
          }
        }
      }
      
      public void recordBackOfCardShown()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(33, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void recordCardSwipedForDismiss()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(34, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void recordExecutedUserActions(List<ProtoParcelable> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          localParcel.writeTypedList(paramList);
          this.mRemote.transact(39, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public void recordFeedbackPromptAction(ProtoParcelable paramProtoParcelable, int paramInt)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_3
        //   4: aload_3
        //   5: ldc 29
        //   7: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aload_1
        //   11: ifnull +39 -> 50
        //   14: aload_3
        //   15: iconst_1
        //   16: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   19: aload_1
        //   20: aload_3
        //   21: iconst_0
        //   22: invokevirtual 81	com/google/android/sidekick/shared/remoteapi/ProtoParcelable:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_3
        //   26: iload_2
        //   27: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   30: aload_0
        //   31: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   34: bipush 42
        //   36: aload_3
        //   37: aconst_null
        //   38: iconst_1
        //   39: invokeinterface 49 5 0
        //   44: pop
        //   45: aload_3
        //   46: invokevirtual 71	android/os/Parcel:recycle	()V
        //   49: return
        //   50: aload_3
        //   51: iconst_0
        //   52: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   55: goto -30 -> 25
        //   58: astore 4
        //   60: aload_3
        //   61: invokevirtual 71	android/os/Parcel:recycle	()V
        //   64: aload 4
        //   66: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	67	0	this	Proxy
        //   0	67	1	paramProtoParcelable	ProtoParcelable
        //   0	67	2	paramInt	int
        //   3	58	3	localParcel	Parcel
        //   58	7	4	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   4	10	58	finally
        //   14	25	58	finally
        //   25	45	58	finally
        //   50	55	58	finally
      }
      
      public void recordFirstUseCardDismiss(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          localParcel.writeInt(paramInt);
          this.mRemote.transact(32, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void recordFirstUseCardView(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          localParcel.writeInt(paramInt);
          this.mRemote.transact(31, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void recordGoogleNowPromoDismissed()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void recordPredictiveInteraction()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(38, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public void recordUserAction(ProtoParcelable paramProtoParcelable, int paramInt)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_3
        //   4: aload_3
        //   5: ldc 29
        //   7: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aload_1
        //   11: ifnull +39 -> 50
        //   14: aload_3
        //   15: iconst_1
        //   16: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   19: aload_1
        //   20: aload_3
        //   21: iconst_0
        //   22: invokevirtual 81	com/google/android/sidekick/shared/remoteapi/ProtoParcelable:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_3
        //   26: iload_2
        //   27: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   30: aload_0
        //   31: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   34: bipush 8
        //   36: aload_3
        //   37: aconst_null
        //   38: iconst_1
        //   39: invokeinterface 49 5 0
        //   44: pop
        //   45: aload_3
        //   46: invokevirtual 71	android/os/Parcel:recycle	()V
        //   49: return
        //   50: aload_3
        //   51: iconst_0
        //   52: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   55: goto -30 -> 25
        //   58: astore 4
        //   60: aload_3
        //   61: invokevirtual 71	android/os/Parcel:recycle	()V
        //   64: aload 4
        //   66: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	67	0	this	Proxy
        //   0	67	1	paramProtoParcelable	ProtoParcelable
        //   0	67	2	paramInt	int
        //   3	58	3	localParcel	Parcel
        //   58	7	4	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   4	10	58	finally
        //   14	25	58	finally
        //   25	45	58	finally
        //   50	55	58	finally
      }
      
      public void recordViewAction(ProtoParcelable paramProtoParcelable, long paramLong, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
            if (paramProtoParcelable != null)
            {
              localParcel.writeInt(1);
              paramProtoParcelable.writeToParcel(localParcel, 0);
              localParcel.writeLong(paramLong);
              localParcel.writeInt(paramInt);
              if (paramBoolean)
              {
                localParcel.writeInt(i);
                this.mRemote.transact(7, localParcel, null, 1);
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
        }
      }
      
      public void refreshEntries()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(40, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeGroupChildEntry(ProtoParcelable paramProtoParcelable1, ProtoParcelable paramProtoParcelable2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
            if (paramProtoParcelable1 != null)
            {
              localParcel.writeInt(1);
              paramProtoParcelable1.writeToParcel(localParcel, 0);
              if (paramProtoParcelable2 != null)
              {
                localParcel.writeInt(1);
                paramProtoParcelable2.writeToParcel(localParcel, 0);
                this.mRemote.transact(12, localParcel, null, 1);
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
      
      public List<TrainingQuestionNode> resolveTrainingQuestions(List<ProtoParcelable> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          localParcel1.writeTypedList(paramList);
          this.mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(TrainingQuestionNode.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public void savePreferences(Bundle paramBundle)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_2
        //   4: aload_2
        //   5: ldc 29
        //   7: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aload_1
        //   11: ifnull +34 -> 45
        //   14: aload_2
        //   15: iconst_1
        //   16: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   19: aload_1
        //   20: aload_2
        //   21: iconst_0
        //   22: invokevirtual 168	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   29: bipush 15
        //   31: aload_2
        //   32: aconst_null
        //   33: iconst_1
        //   34: invokeinterface 49 5 0
        //   39: pop
        //   40: aload_2
        //   41: invokevirtual 71	android/os/Parcel:recycle	()V
        //   44: return
        //   45: aload_2
        //   46: iconst_0
        //   47: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   50: goto -25 -> 25
        //   53: astore_3
        //   54: aload_2
        //   55: invokevirtual 71	android/os/Parcel:recycle	()V
        //   58: aload_3
        //   59: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	60	0	this	Proxy
        //   0	60	1	paramBundle	Bundle
        //   3	52	2	localParcel	Parcel
        //   53	6	3	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   4	10	53	finally
        //   14	25	53	finally
        //   25	40	53	finally
        //   45	50	53	finally
      }
      
      public void sendPendingTrainingAnswers()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          this.mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendTrainingAction(ProtoParcelable paramProtoParcelable1, ProtoParcelable paramProtoParcelable2, ProtoParcelable paramProtoParcelable3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
            if (paramProtoParcelable1 != null)
            {
              localParcel.writeInt(1);
              paramProtoParcelable1.writeToParcel(localParcel, 0);
              if (paramProtoParcelable2 != null)
              {
                localParcel.writeInt(1);
                paramProtoParcelable2.writeToParcel(localParcel, 0);
                if (paramProtoParcelable3 == null) {
                  break label113;
                }
                localParcel.writeInt(1);
                paramProtoParcelable3.writeToParcel(localParcel, 0);
                this.mRemote.transact(20, localParcel, null, 1);
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
          continue;
          label113:
          localParcel.writeInt(0);
        }
      }
      
      /* Error */
      public void setTrafficSharerHiddenState(long paramLong, boolean paramBoolean)
        throws RemoteException
      {
        // Byte code:
        //   0: iconst_1
        //   1: istore 4
        //   3: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   6: astore 5
        //   8: aload 5
        //   10: ldc 29
        //   12: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   15: aload 5
        //   17: lload_1
        //   18: invokevirtual 128	android/os/Parcel:writeLong	(J)V
        //   21: iload_3
        //   22: ifeq +32 -> 54
        //   25: aload 5
        //   27: iload 4
        //   29: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   32: aload_0
        //   33: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   36: bipush 16
        //   38: aload 5
        //   40: aconst_null
        //   41: iconst_1
        //   42: invokeinterface 49 5 0
        //   47: pop
        //   48: aload 5
        //   50: invokevirtual 71	android/os/Parcel:recycle	()V
        //   53: return
        //   54: iconst_0
        //   55: istore 4
        //   57: goto -32 -> 25
        //   60: astore 6
        //   62: aload 5
        //   64: invokevirtual 71	android/os/Parcel:recycle	()V
        //   67: aload 6
        //   69: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	70	0	this	Proxy
        //   0	70	1	paramLong	long
        //   0	70	3	paramBoolean	boolean
        //   1	55	4	i	int
        //   6	57	5	localParcel	Parcel
        //   60	8	6	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   8	21	60	finally
        //   25	48	60	finally
      }
      
      public void setTrainingAnswer(ProtoParcelable paramProtoParcelable1, ProtoParcelable paramProtoParcelable2, ProtoParcelable paramProtoParcelable3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        for (;;)
        {
          try
          {
            localParcel.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
            if (paramProtoParcelable1 != null)
            {
              localParcel.writeInt(1);
              paramProtoParcelable1.writeToParcel(localParcel, 0);
              if (paramProtoParcelable2 != null)
              {
                localParcel.writeInt(1);
                paramProtoParcelable2.writeToParcel(localParcel, 0);
                if (paramProtoParcelable3 == null) {
                  break label113;
                }
                localParcel.writeInt(1);
                paramProtoParcelable3.writeToParcel(localParcel, 0);
                this.mRemote.transact(18, localParcel, null, 1);
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
          continue;
          label113:
          localParcel.writeInt(0);
        }
      }
      
      /* Error */
      public void snoozeReminder(ProtoParcelable paramProtoParcelable)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 27	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore_2
        //   4: aload_2
        //   5: ldc 29
        //   7: invokevirtual 33	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   10: aload_1
        //   11: ifnull +34 -> 45
        //   14: aload_2
        //   15: iconst_1
        //   16: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   19: aload_1
        //   20: aload_2
        //   21: iconst_0
        //   22: invokevirtual 81	com/google/android/sidekick/shared/remoteapi/ProtoParcelable:writeToParcel	(Landroid/os/Parcel;I)V
        //   25: aload_0
        //   26: getfield 15	com/google/android/sidekick/shared/remoteapi/IGoogleNowRemoteService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   29: bipush 27
        //   31: aload_2
        //   32: aconst_null
        //   33: iconst_1
        //   34: invokeinterface 49 5 0
        //   39: pop
        //   40: aload_2
        //   41: invokevirtual 71	android/os/Parcel:recycle	()V
        //   44: return
        //   45: aload_2
        //   46: iconst_0
        //   47: invokevirtual 37	android/os/Parcel:writeInt	(I)V
        //   50: goto -25 -> 25
        //   53: astore_3
        //   54: aload_2
        //   55: invokevirtual 71	android/os/Parcel:recycle	()V
        //   58: aload_3
        //   59: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	60	0	this	Proxy
        //   0	60	1	paramProtoParcelable	ProtoParcelable
        //   3	52	2	localParcel	Parcel
        //   53	6	3	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   4	10	53	finally
        //   14	25	53	finally
        //   25	40	53	finally
        //   45	50	53	finally
      }
      
      public String translateInPlace(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          this.mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
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
 * Qualified Name:     com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService
 * JD-Core Version:    0.7.0.1
 */