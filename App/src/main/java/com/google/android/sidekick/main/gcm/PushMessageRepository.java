package com.google.android.sidekick.main.gcm;

import com.google.android.apps.sidekick.gcm.PushMessageRepoProtos.AccumulatedRefreshState;
import com.google.android.apps.sidekick.gcm.PushMessageRepoProtos.TargetDisplayUpdate;
import com.google.android.sidekick.main.file.FileBackedProto;
import com.google.android.sidekick.main.file.FileBackedProto.ReadModifyWrite;
import com.google.android.sidekick.main.file.FileBytesReader;
import com.google.android.sidekick.main.file.FileBytesWriter;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.SidekickPushMessage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PushMessageRepository
{
  private static final Supplier<PushMessageRepoProtos.AccumulatedRefreshState> PROTO_FACTORY = new Supplier()
  {
    public PushMessageRepoProtos.AccumulatedRefreshState get()
    {
      return new PushMessageRepoProtos.AccumulatedRefreshState();
    }
  };
  private static final String TAG = Tag.getTag(PushMessageRepository.class);
  private final FileBackedProto<PushMessageRepoProtos.AccumulatedRefreshState> mStateManager;
  
  public PushMessageRepository(FileBytesReader paramFileBytesReader, FileBytesWriter paramFileBytesWriter)
  {
    this.mStateManager = new FileBackedProto(PROTO_FACTORY, "push_message_repository_store", paramFileBytesReader, paramFileBytesWriter, false);
  }
  
  private static Sidekick.Interest copyWithOnlySupportedFields(Sidekick.Interest paramInterest)
  {
    Sidekick.Interest localInterest = new Sidekick.Interest();
    if (paramInterest.hasTargetDisplay()) {
      localInterest.setTargetDisplay(paramInterest.getTargetDisplay());
    }
    if (paramInterest.getEntryTypeRestrictCount() > 0)
    {
      Iterator localIterator = paramInterest.getEntryTypeRestrictList().iterator();
      while (localIterator.hasNext()) {
        localInterest.addEntryTypeRestrict(((Integer)localIterator.next()).intValue());
      }
    }
    return localInterest;
  }
  
  static PushMessageRepoProtos.TargetDisplayUpdate getNewOrExistingUpdateFrom(PushMessageRepoProtos.AccumulatedRefreshState paramAccumulatedRefreshState, int paramInt)
  {
    Iterator localIterator = paramAccumulatedRefreshState.getTargetDisplayUpdateList().iterator();
    while (localIterator.hasNext())
    {
      PushMessageRepoProtos.TargetDisplayUpdate localTargetDisplayUpdate2 = (PushMessageRepoProtos.TargetDisplayUpdate)localIterator.next();
      if ((localTargetDisplayUpdate2.hasTargetDisplay()) && (localTargetDisplayUpdate2.getTargetDisplay() == paramInt)) {
        return localTargetDisplayUpdate2;
      }
    }
    PushMessageRepoProtos.TargetDisplayUpdate localTargetDisplayUpdate1 = new PushMessageRepoProtos.TargetDisplayUpdate().setTargetDisplay(paramInt);
    paramAccumulatedRefreshState.addTargetDisplayUpdate(localTargetDisplayUpdate1);
    return localTargetDisplayUpdate1;
  }
  
  public void add(Sidekick.SidekickPushMessage paramSidekickPushMessage)
  {
    Sidekick.Interest localInterest = paramSidekickPushMessage.getInterest();
    if (localInterest == null) {}
    while (!localInterest.hasTargetDisplay()) {
      return;
    }
    this.mStateManager.doReadModifyWrite(new UpdateStateToAddPushMessage(paramSidekickPushMessage));
  }
  
  public ImmutableList<PendingPartialUpdate> getPendingPartialUpdates()
  {
    PushMessageRepoProtos.AccumulatedRefreshState localAccumulatedRefreshState = (PushMessageRepoProtos.AccumulatedRefreshState)this.mStateManager.getData();
    if (localAccumulatedRefreshState == null) {
      return ImmutableList.of();
    }
    if (localAccumulatedRefreshState.getTargetDisplayUpdateCount() == 0) {
      return ImmutableList.of();
    }
    ImmutableList.Builder localBuilder = ImmutableList.builder();
    Iterator localIterator = localAccumulatedRefreshState.getTargetDisplayUpdateList().iterator();
    while (localIterator.hasNext()) {
      localBuilder.add(new PendingPartialUpdateImpl((PushMessageRepoProtos.TargetDisplayUpdate)localIterator.next(), null));
    }
    return localBuilder.build();
  }
  
  public void partialUpdateComplete(PendingPartialUpdate paramPendingPartialUpdate)
  {
    this.mStateManager.doReadModifyWrite(new RemoveCompletedEntryTypes(paramPendingPartialUpdate));
  }
  
  public static abstract interface PendingPartialUpdate
  {
    public abstract Sidekick.Interest getInterest();
    
    public abstract int getTargetDisplay();
  }
  
  private static class PendingPartialUpdateImpl
    implements PushMessageRepository.PendingPartialUpdate
  {
    private final PushMessageRepoProtos.TargetDisplayUpdate mTargetDisplayUpdate;
    
    private PendingPartialUpdateImpl(PushMessageRepoProtos.TargetDisplayUpdate paramTargetDisplayUpdate)
    {
      this.mTargetDisplayUpdate = paramTargetDisplayUpdate;
    }
    
    public Sidekick.Interest getInterest()
    {
      return this.mTargetDisplayUpdate.getInterest();
    }
    
    public int getTargetDisplay()
    {
      return this.mTargetDisplayUpdate.getTargetDisplay();
    }
  }
  
  static class RemoveCompletedEntryTypes
    implements FileBackedProto.ReadModifyWrite<PushMessageRepoProtos.AccumulatedRefreshState>
  {
    private final PushMessageRepository.PendingPartialUpdate mCompletedUpdate;
    
    RemoveCompletedEntryTypes(PushMessageRepository.PendingPartialUpdate paramPendingPartialUpdate)
    {
      this.mCompletedUpdate = paramPendingPartialUpdate;
    }
    
    public PushMessageRepoProtos.AccumulatedRefreshState readModifyMaybeWrite(PushMessageRepoProtos.AccumulatedRefreshState paramAccumulatedRefreshState)
    {
      boolean bool = false;
      Iterator localIterator = paramAccumulatedRefreshState.getTargetDisplayUpdateList().iterator();
      int i = this.mCompletedUpdate.getTargetDisplay();
      while (localIterator.hasNext())
      {
        PushMessageRepoProtos.TargetDisplayUpdate localTargetDisplayUpdate = (PushMessageRepoProtos.TargetDisplayUpdate)localIterator.next();
        if (localTargetDisplayUpdate.getTargetDisplay() == i)
        {
          HashSet localHashSet = Sets.newHashSet(localTargetDisplayUpdate.getInterest().getEntryTypeRestrictList());
          bool = localHashSet.removeAll(this.mCompletedUpdate.getInterest().getEntryTypeRestrictList());
          if (localHashSet.isEmpty())
          {
            localIterator.remove();
            bool = true;
          }
        }
      }
      if (bool) {
        return paramAccumulatedRefreshState;
      }
      return null;
    }
  }
  
  static class UpdateStateToAddPushMessage
    implements FileBackedProto.ReadModifyWrite<PushMessageRepoProtos.AccumulatedRefreshState>
  {
    private final Sidekick.SidekickPushMessage mPushMessage;
    
    UpdateStateToAddPushMessage(Sidekick.SidekickPushMessage paramSidekickPushMessage)
    {
      this.mPushMessage = paramSidekickPushMessage;
    }
    
    public PushMessageRepoProtos.AccumulatedRefreshState readModifyMaybeWrite(PushMessageRepoProtos.AccumulatedRefreshState paramAccumulatedRefreshState)
    {
      PushMessageRepoProtos.TargetDisplayUpdate localTargetDisplayUpdate = PushMessageRepository.getNewOrExistingUpdateFrom(paramAccumulatedRefreshState, this.mPushMessage.getInterest().getTargetDisplay());
      if (!localTargetDisplayUpdate.hasInterest()) {
        localTargetDisplayUpdate.setInterest(PushMessageRepository.copyWithOnlySupportedFields(this.mPushMessage.getInterest()));
      }
      for (;;)
      {
        return paramAccumulatedRefreshState;
        Sidekick.Interest localInterest1 = localTargetDisplayUpdate.getInterest();
        Sidekick.Interest localInterest2 = this.mPushMessage.getInterest();
        HashSet localHashSet = new HashSet(localInterest1.getEntryTypeRestrictList());
        localHashSet.addAll(localInterest2.getEntryTypeRestrictList());
        localInterest1.clearEntryTypeRestrict();
        Iterator localIterator = localHashSet.iterator();
        while (localIterator.hasNext()) {
          localInterest1.addEntryTypeRestrict(((Integer)localIterator.next()).intValue());
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.gcm.PushMessageRepository
 * JD-Core Version:    0.7.0.1
 */