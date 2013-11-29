package com.google.android.sidekick.main.inject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.apps.sidekick.inject.ExecutedUserActions;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.entry.EntryUtil;
import com.google.android.sidekick.main.file.FileBytesReader;
import com.google.android.sidekick.main.file.FileBytesWriter;
import com.google.android.sidekick.shared.util.ExecutedUserActionBuilder;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.ActivityLifecycleNotifier;
import com.google.android.velvet.ActivityLifecycleObserver;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.Iterator;
import java.util.List;

public class ExecutedUserActionStoreImpl
  implements ExecutedUserActionStore, ActivityLifecycleObserver
{
  static final String ACTION_DELETE = "delete";
  static final String ACTION_WRITE = "write";
  private static final String TAG = Tag.getTag(ExecutedUserActionStoreImpl.class);
  private final ActivityLifecycleNotifier mActivityLifecycleNotifier;
  private final Clock mClock;
  private final Context mContext;
  private final List<Sidekick.ExecutedUserAction> mDeferredUserActions = Lists.newArrayList();
  private final ExecutedUserActions mExecutedUserActions = new ExecutedUserActions();
  private final Object mFileLock = new Object();
  private final FileBytesReader mFileReader;
  private final FileBytesWriter mFileWriter;
  
  public ExecutedUserActionStoreImpl(Context paramContext, FileBytesReader paramFileBytesReader, FileBytesWriter paramFileBytesWriter, Clock paramClock, ActivityLifecycleNotifier paramActivityLifecycleNotifier)
  {
    this.mContext = paramContext;
    this.mFileReader = paramFileBytesReader;
    this.mFileWriter = paramFileBytesWriter;
    this.mClock = paramClock;
    this.mActivityLifecycleNotifier = paramActivityLifecycleNotifier;
    this.mActivityLifecycleNotifier.addActivityLifecycleObserver(this);
  }
  
  private void addExecutedUserAction(ExecutedUserActionBuilder paramExecutedUserActionBuilder)
  {
    synchronized (this.mExecutedUserActions)
    {
      this.mExecutedUserActions.addExecutedUserAction(paramExecutedUserActionBuilder.build());
      return;
    }
  }
  
  private boolean containsLockedOn(Iterable<Sidekick.ExecutedUserAction> paramIterable, ProtoKey<Sidekick.Entry> paramProtoKey, Sidekick.Action paramAction)
  {
    try
    {
      Iterator localIterator = paramIterable.iterator();
      while (localIterator.hasNext()) {
        if (isSameAction(paramProtoKey, paramAction, (Sidekick.ExecutedUserAction)localIterator.next())) {
          return true;
        }
      }
      return false;
    }
    finally {}
  }
  
  private boolean isSameAction(ProtoKey<Sidekick.Entry> paramProtoKey, Sidekick.Action paramAction, Sidekick.ExecutedUserAction paramExecutedUserAction)
  {
    return (paramAction.getType() == paramExecutedUserAction.getAction().getType()) && (EntryUtil.isSameEntry(paramProtoKey, new ProtoKey(paramExecutedUserAction.getEntry())));
  }
  
  public void addDeferredAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction)
  {
    Sidekick.ExecutedUserAction localExecutedUserAction = new ExecutedUserActionBuilder(paramEntry, paramAction, this.mClock.currentTimeMillis()).build();
    synchronized (this.mDeferredUserActions)
    {
      ProtoKey localProtoKey = new ProtoKey(localExecutedUserAction.getEntry());
      if (!containsLockedOn(this.mDeferredUserActions, localProtoKey, paramAction)) {
        this.mDeferredUserActions.add(localExecutedUserAction);
      }
      return;
    }
  }
  
  public void commitDeferredActions()
  {
    synchronized (this.mDeferredUserActions)
    {
      if (this.mDeferredUserActions.isEmpty()) {
        return;
      }
      synchronized (this.mExecutedUserActions)
      {
        Iterator localIterator = this.mDeferredUserActions.iterator();
        if (localIterator.hasNext())
        {
          Sidekick.ExecutedUserAction localExecutedUserAction = (Sidekick.ExecutedUserAction)localIterator.next();
          this.mExecutedUserActions.addExecutedUserAction(localExecutedUserAction);
        }
      }
    }
    this.mDeferredUserActions.clear();
    Intent localIntent = new Intent("com.google.android.apps.now.DEFERRED_ACTIONS_COMMITTED");
    localIntent.setPackage(this.mContext.getPackageName());
    this.mContext.sendBroadcast(localIntent);
  }
  
  void deleteStore()
  {
    synchronized (this.mFileLock)
    {
      this.mFileWriter.deleteFile("executed_user_action_log");
      return;
    }
  }
  
  /* Error */
  public List<Sidekick.ExecutedUserAction> flush()
  {
    // Byte code:
    //   0: invokestatic 58	com/google/common/collect/Lists:newArrayList	()Ljava/util/ArrayList;
    //   3: astore_1
    //   4: aload_0
    //   5: getfield 62	com/google/android/sidekick/main/inject/ExecutedUserActionStoreImpl:mFileLock	Ljava/lang/Object;
    //   8: astore_2
    //   9: aload_2
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield 66	com/google/android/sidekick/main/inject/ExecutedUserActionStoreImpl:mFileReader	Lcom/google/android/sidekick/main/file/FileBytesReader;
    //   15: ldc 190
    //   17: ldc 200
    //   19: invokevirtual 206	com/google/android/sidekick/main/file/FileBytesReader:readEncryptedFileBytes	(Ljava/lang/String;I)[B
    //   22: astore 4
    //   24: aload 4
    //   26: ifnull +32 -> 58
    //   29: new 49	com/google/android/apps/sidekick/inject/ExecutedUserActions
    //   32: dup
    //   33: invokespecial 50	com/google/android/apps/sidekick/inject/ExecutedUserActions:<init>	()V
    //   36: astore 5
    //   38: aload 5
    //   40: aload 4
    //   42: invokevirtual 210	com/google/android/apps/sidekick/inject/ExecutedUserActions:mergeFrom	([B)Lcom/google/protobuf/micro/MessageMicro;
    //   45: pop
    //   46: aload_1
    //   47: aload 5
    //   49: invokevirtual 213	com/google/android/apps/sidekick/inject/ExecutedUserActions:getExecutedUserActionList	()Ljava/util/List;
    //   52: invokeinterface 217 2 0
    //   57: pop
    //   58: aload_0
    //   59: getfield 68	com/google/android/sidekick/main/inject/ExecutedUserActionStoreImpl:mFileWriter	Lcom/google/android/sidekick/main/file/FileBytesWriter;
    //   62: ldc 190
    //   64: invokevirtual 195	com/google/android/sidekick/main/file/FileBytesWriter:deleteFile	(Ljava/lang/String;)V
    //   67: aload_2
    //   68: monitorexit
    //   69: aload_0
    //   70: getfield 52	com/google/android/sidekick/main/inject/ExecutedUserActionStoreImpl:mExecutedUserActions	Lcom/google/android/apps/sidekick/inject/ExecutedUserActions;
    //   73: astore 8
    //   75: aload 8
    //   77: monitorenter
    //   78: aload_1
    //   79: aload_0
    //   80: getfield 52	com/google/android/sidekick/main/inject/ExecutedUserActionStoreImpl:mExecutedUserActions	Lcom/google/android/apps/sidekick/inject/ExecutedUserActions;
    //   83: invokevirtual 213	com/google/android/apps/sidekick/inject/ExecutedUserActions:getExecutedUserActionList	()Ljava/util/List;
    //   86: invokeinterface 217 2 0
    //   91: pop
    //   92: aload_0
    //   93: getfield 52	com/google/android/sidekick/main/inject/ExecutedUserActionStoreImpl:mExecutedUserActions	Lcom/google/android/apps/sidekick/inject/ExecutedUserActions;
    //   96: invokevirtual 220	com/google/android/apps/sidekick/inject/ExecutedUserActions:clear	()Lcom/google/android/apps/sidekick/inject/ExecutedUserActions;
    //   99: pop
    //   100: aload 8
    //   102: monitorexit
    //   103: aload_1
    //   104: areturn
    //   105: astore 6
    //   107: getstatic 43	com/google/android/sidekick/main/inject/ExecutedUserActionStoreImpl:TAG	Ljava/lang/String;
    //   110: ldc 222
    //   112: invokestatic 228	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   115: pop
    //   116: goto -58 -> 58
    //   119: astore_3
    //   120: aload_2
    //   121: monitorexit
    //   122: aload_3
    //   123: athrow
    //   124: astore 9
    //   126: aload 8
    //   128: monitorexit
    //   129: aload 9
    //   131: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	132	0	this	ExecutedUserActionStoreImpl
    //   3	101	1	localArrayList	java.util.ArrayList
    //   8	113	2	localObject1	Object
    //   119	4	3	localObject2	Object
    //   22	19	4	arrayOfByte	byte[]
    //   36	12	5	localExecutedUserActions1	ExecutedUserActions
    //   105	1	6	localInvalidProtocolBufferMicroException	InvalidProtocolBufferMicroException
    //   124	6	9	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   38	58	105	com/google/protobuf/micro/InvalidProtocolBufferMicroException
    //   11	24	119	finally
    //   29	38	119	finally
    //   38	58	119	finally
    //   58	69	119	finally
    //   107	116	119	finally
    //   120	122	119	finally
    //   78	103	124	finally
    //   126	129	124	finally
  }
  
  void handleWriteIntent(Intent paramIntent)
  {
    byte[] arrayOfByte1 = paramIntent.getByteArrayExtra("actions");
    Preconditions.checkNotNull(arrayOfByte1);
    if (arrayOfByte1.length > 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      synchronized (this.mFileLock)
      {
        byte[] arrayOfByte2 = this.mFileReader.readEncryptedFileBytes("executed_user_action_log", 524288);
        ExecutedUserActions localExecutedUserActions = new ExecutedUserActions();
        if (arrayOfByte2 != null) {}
        try
        {
          if (arrayOfByte2.length != 0) {
            localExecutedUserActions.mergeFrom(arrayOfByte2);
          }
          localObject2 = finally;
        }
        catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException2)
        {
          try
          {
            localExecutedUserActions.mergeFrom(arrayOfByte1);
            if (!this.mFileWriter.writeEncryptedFileBytes("executed_user_action_log", localExecutedUserActions.toByteArray(), 524288)) {
              Log.e(TAG, "Failed to write actions");
            }
            return;
          }
          catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException1)
          {
            Log.e(TAG, "Received intent with invalid action data");
            return;
          }
          localInvalidProtocolBufferMicroException2 = localInvalidProtocolBufferMicroException2;
          Log.e(TAG, "File storage contained invalid data");
          return;
        }
      }
    }
  }
  
  public boolean hasDeferredAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction)
  {
    ProtoKey localProtoKey = new ProtoKey(new ExecutedUserActionBuilder(paramEntry, paramAction, 0L).build().getEntry());
    return containsLockedOn(this.mDeferredUserActions, localProtoKey, paramAction);
  }
  
  public boolean hasExecutedUserAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction)
  {
    ProtoKey localProtoKey = new ProtoKey(new ExecutedUserActionBuilder(paramEntry, paramAction, 0L).build().getEntry());
    return containsLockedOn(this.mExecutedUserActions.getExecutedUserActionList(), localProtoKey, paramAction);
  }
  
  public void onActivityStart() {}
  
  public void onActivityStop()
  {
    persist();
  }
  
  public void persist()
  {
    commitDeferredActions();
    synchronized (this.mExecutedUserActions)
    {
      int i = this.mExecutedUserActions.getExecutedUserActionCount();
      byte[] arrayOfByte = null;
      if (i > 0)
      {
        arrayOfByte = this.mExecutedUserActions.toByteArray();
        this.mExecutedUserActions.clear();
      }
      if (arrayOfByte != null)
      {
        Intent localIntent = new Intent("write", null, this.mContext, WriteService.class);
        localIntent.putExtra("actions", arrayOfByte);
        startService(localIntent);
      }
      return;
    }
  }
  
  public void postDeleteStore()
  {
    startService(new Intent("delete", null, this.mContext, WriteService.class));
    synchronized (this.mDeferredUserActions)
    {
      this.mDeferredUserActions.clear();
    }
    synchronized (this.mExecutedUserActions)
    {
      this.mExecutedUserActions.clear();
      return;
      localObject1 = finally;
      throw localObject1;
    }
  }
  
  public boolean removeDeferredAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction)
  {
    synchronized (this.mDeferredUserActions)
    {
      if (this.mDeferredUserActions.isEmpty()) {
        return false;
      }
      ProtoKey localProtoKey = new ProtoKey(new ExecutedUserActionBuilder(paramEntry, paramAction, 0L).build().getEntry());
      Iterator localIterator = this.mDeferredUserActions.iterator();
      while (localIterator.hasNext()) {
        if (isSameAction(localProtoKey, paramAction, (Sidekick.ExecutedUserAction)localIterator.next()))
        {
          localIterator.remove();
          return true;
        }
      }
    }
    return false;
  }
  
  public void saveAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction)
  {
    removeDeferredAction(paramEntry, paramAction);
    addExecutedUserAction(new ExecutedUserActionBuilder(paramEntry, paramAction, this.mClock.currentTimeMillis()));
  }
  
  public void saveClickAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction, Sidekick.ClickAction paramClickAction)
  {
    addExecutedUserAction(new ExecutedUserActionBuilder(paramEntry, paramAction, this.mClock.currentTimeMillis()).withClickAction(paramClickAction));
  }
  
  public void saveExecutedUserActions(List<Sidekick.ExecutedUserAction> paramList)
  {
    synchronized (this.mExecutedUserActions)
    {
      Iterator localIterator = paramList.iterator();
      if (localIterator.hasNext())
      {
        Sidekick.ExecutedUserAction localExecutedUserAction = (Sidekick.ExecutedUserAction)localIterator.next();
        this.mExecutedUserActions.addExecutedUserAction(localExecutedUserAction);
      }
    }
  }
  
  public void saveViewAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction, long paramLong, int paramInt, boolean paramBoolean)
  {
    addExecutedUserAction(new ExecutedUserActionBuilder(paramEntry, paramAction, this.mClock.currentTimeMillis()).withCardHeight(paramInt).withPortrait(paramBoolean).withExecutionTimeMs(paramLong));
  }
  
  void startService(Intent paramIntent)
  {
    this.mContext.startService(paramIntent);
  }
  
  public static class WriteService
    extends IntentService
  {
    public WriteService()
    {
      super();
      setIntentRedelivery(true);
    }
    
    protected void onHandleIntent(Intent paramIntent)
    {
      if (paramIntent == null) {}
      ExecutedUserActionStoreImpl localExecutedUserActionStoreImpl;
      do
      {
        return;
        ExecutedUserActionStore localExecutedUserActionStore = VelvetServices.get().getSidekickInjector().getExecutedUserActionStore();
        if (!(localExecutedUserActionStore instanceof ExecutedUserActionStoreImpl))
        {
          Log.e(ExecutedUserActionStoreImpl.TAG, "Unexpected ExecutedUserActionStore implementation, dropping intent:" + paramIntent.getAction());
          return;
        }
        localExecutedUserActionStoreImpl = (ExecutedUserActionStoreImpl)localExecutedUserActionStore;
        if ("write".equals(paramIntent.getAction()))
        {
          localExecutedUserActionStoreImpl.handleWriteIntent(paramIntent);
          return;
        }
      } while (!"delete".equals(paramIntent.getAction()));
      localExecutedUserActionStoreImpl.deleteStore();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.ExecutedUserActionStoreImpl
 * JD-Core Version:    0.7.0.1
 */