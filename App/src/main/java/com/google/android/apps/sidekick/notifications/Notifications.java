package com.google.android.apps.sidekick.notifications;

import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Notifications
{
  public static final class ClientData
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<Notifications.PendingNotification> pendingNotification_ = Collections.emptyList();
    private List<Notifications.PendingRefresh> pendingRefresh_ = Collections.emptyList();
    
    public ClientData addPendingNotification(Notifications.PendingNotification paramPendingNotification)
    {
      if (paramPendingNotification == null) {
        throw new NullPointerException();
      }
      if (this.pendingNotification_.isEmpty()) {
        this.pendingNotification_ = new ArrayList();
      }
      this.pendingNotification_.add(paramPendingNotification);
      return this;
    }
    
    public ClientData addPendingRefresh(Notifications.PendingRefresh paramPendingRefresh)
    {
      if (paramPendingRefresh == null) {
        throw new NullPointerException();
      }
      if (this.pendingRefresh_.isEmpty()) {
        this.pendingRefresh_ = new ArrayList();
      }
      this.pendingRefresh_.add(paramPendingRefresh);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public Notifications.PendingNotification getPendingNotification(int paramInt)
    {
      return (Notifications.PendingNotification)this.pendingNotification_.get(paramInt);
    }
    
    public int getPendingNotificationCount()
    {
      return this.pendingNotification_.size();
    }
    
    public List<Notifications.PendingNotification> getPendingNotificationList()
    {
      return this.pendingNotification_;
    }
    
    public Notifications.PendingRefresh getPendingRefresh(int paramInt)
    {
      return (Notifications.PendingRefresh)this.pendingRefresh_.get(paramInt);
    }
    
    public int getPendingRefreshCount()
    {
      return this.pendingRefresh_.size();
    }
    
    public List<Notifications.PendingRefresh> getPendingRefreshList()
    {
      return this.pendingRefresh_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getPendingNotificationList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (Notifications.PendingNotification)localIterator1.next());
      }
      Iterator localIterator2 = getPendingRefreshList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, (Notifications.PendingRefresh)localIterator2.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public ClientData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          Notifications.PendingNotification localPendingNotification = new Notifications.PendingNotification();
          paramCodedInputStreamMicro.readMessage(localPendingNotification);
          addPendingNotification(localPendingNotification);
          break;
        }
        Notifications.PendingRefresh localPendingRefresh = new Notifications.PendingRefresh();
        paramCodedInputStreamMicro.readMessage(localPendingRefresh);
        addPendingRefresh(localPendingRefresh);
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getPendingNotificationList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (Notifications.PendingNotification)localIterator1.next());
      }
      Iterator localIterator2 = getPendingRefreshList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(2, (Notifications.PendingRefresh)localIterator2.next());
      }
    }
  }
  
  public static final class PendingNotification
    extends MessageMicro
  {
    private int cachedSize = -1;
    private Sidekick.Entry entry_ = null;
    private long firstInsertTimeSeconds_ = 0L;
    private boolean hasEntry;
    private boolean hasFirstInsertTimeSeconds;
    private boolean hasInterest;
    private boolean hasLastTriggerTimeSeconds;
    private boolean hasNotificationDismissed;
    private boolean hasNotified;
    private boolean hasSnoozeTimeSeconds;
    private Sidekick.Interest interest_ = null;
    private long lastTriggerTimeSeconds_ = 0L;
    private boolean notificationDismissed_ = false;
    private boolean notified_ = false;
    private long snoozeTimeSeconds_ = 0L;
    
    public PendingNotification clearLastTriggerTimeSeconds()
    {
      this.hasLastTriggerTimeSeconds = false;
      this.lastTriggerTimeSeconds_ = 0L;
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public Sidekick.Entry getEntry()
    {
      return this.entry_;
    }
    
    public long getFirstInsertTimeSeconds()
    {
      return this.firstInsertTimeSeconds_;
    }
    
    public Sidekick.Interest getInterest()
    {
      return this.interest_;
    }
    
    public long getLastTriggerTimeSeconds()
    {
      return this.lastTriggerTimeSeconds_;
    }
    
    public boolean getNotificationDismissed()
    {
      return this.notificationDismissed_;
    }
    
    public boolean getNotified()
    {
      return this.notified_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasEntry();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getEntry());
      }
      if (hasInterest()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getInterest());
      }
      if (hasNotified()) {
        i += CodedOutputStreamMicro.computeBoolSize(3, getNotified());
      }
      if (hasNotificationDismissed()) {
        i += CodedOutputStreamMicro.computeBoolSize(4, getNotificationDismissed());
      }
      if (hasLastTriggerTimeSeconds()) {
        i += CodedOutputStreamMicro.computeInt64Size(5, getLastTriggerTimeSeconds());
      }
      if (hasSnoozeTimeSeconds()) {
        i += CodedOutputStreamMicro.computeInt64Size(6, getSnoozeTimeSeconds());
      }
      if (hasFirstInsertTimeSeconds()) {
        i += CodedOutputStreamMicro.computeInt64Size(7, getFirstInsertTimeSeconds());
      }
      this.cachedSize = i;
      return i;
    }
    
    public long getSnoozeTimeSeconds()
    {
      return this.snoozeTimeSeconds_;
    }
    
    public boolean hasEntry()
    {
      return this.hasEntry;
    }
    
    public boolean hasFirstInsertTimeSeconds()
    {
      return this.hasFirstInsertTimeSeconds;
    }
    
    public boolean hasInterest()
    {
      return this.hasInterest;
    }
    
    public boolean hasLastTriggerTimeSeconds()
    {
      return this.hasLastTriggerTimeSeconds;
    }
    
    public boolean hasNotificationDismissed()
    {
      return this.hasNotificationDismissed;
    }
    
    public boolean hasNotified()
    {
      return this.hasNotified;
    }
    
    public boolean hasSnoozeTimeSeconds()
    {
      return this.hasSnoozeTimeSeconds;
    }
    
    public PendingNotification mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          Sidekick.Entry localEntry = new Sidekick.Entry();
          paramCodedInputStreamMicro.readMessage(localEntry);
          setEntry(localEntry);
          break;
        case 18: 
          Sidekick.Interest localInterest = new Sidekick.Interest();
          paramCodedInputStreamMicro.readMessage(localInterest);
          setInterest(localInterest);
          break;
        case 24: 
          setNotified(paramCodedInputStreamMicro.readBool());
          break;
        case 32: 
          setNotificationDismissed(paramCodedInputStreamMicro.readBool());
          break;
        case 40: 
          setLastTriggerTimeSeconds(paramCodedInputStreamMicro.readInt64());
          break;
        case 48: 
          setSnoozeTimeSeconds(paramCodedInputStreamMicro.readInt64());
          break;
        }
        setFirstInsertTimeSeconds(paramCodedInputStreamMicro.readInt64());
      }
    }
    
    public PendingNotification setEntry(Sidekick.Entry paramEntry)
    {
      if (paramEntry == null) {
        throw new NullPointerException();
      }
      this.hasEntry = true;
      this.entry_ = paramEntry;
      return this;
    }
    
    public PendingNotification setFirstInsertTimeSeconds(long paramLong)
    {
      this.hasFirstInsertTimeSeconds = true;
      this.firstInsertTimeSeconds_ = paramLong;
      return this;
    }
    
    public PendingNotification setInterest(Sidekick.Interest paramInterest)
    {
      if (paramInterest == null) {
        throw new NullPointerException();
      }
      this.hasInterest = true;
      this.interest_ = paramInterest;
      return this;
    }
    
    public PendingNotification setLastTriggerTimeSeconds(long paramLong)
    {
      this.hasLastTriggerTimeSeconds = true;
      this.lastTriggerTimeSeconds_ = paramLong;
      return this;
    }
    
    public PendingNotification setNotificationDismissed(boolean paramBoolean)
    {
      this.hasNotificationDismissed = true;
      this.notificationDismissed_ = paramBoolean;
      return this;
    }
    
    public PendingNotification setNotified(boolean paramBoolean)
    {
      this.hasNotified = true;
      this.notified_ = paramBoolean;
      return this;
    }
    
    public PendingNotification setSnoozeTimeSeconds(long paramLong)
    {
      this.hasSnoozeTimeSeconds = true;
      this.snoozeTimeSeconds_ = paramLong;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasEntry()) {
        paramCodedOutputStreamMicro.writeMessage(1, getEntry());
      }
      if (hasInterest()) {
        paramCodedOutputStreamMicro.writeMessage(2, getInterest());
      }
      if (hasNotified()) {
        paramCodedOutputStreamMicro.writeBool(3, getNotified());
      }
      if (hasNotificationDismissed()) {
        paramCodedOutputStreamMicro.writeBool(4, getNotificationDismissed());
      }
      if (hasLastTriggerTimeSeconds()) {
        paramCodedOutputStreamMicro.writeInt64(5, getLastTriggerTimeSeconds());
      }
      if (hasSnoozeTimeSeconds()) {
        paramCodedOutputStreamMicro.writeInt64(6, getSnoozeTimeSeconds());
      }
      if (hasFirstInsertTimeSeconds()) {
        paramCodedOutputStreamMicro.writeInt64(7, getFirstInsertTimeSeconds());
      }
    }
  }
  
  public static final class PendingRefresh
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasInterest;
    private boolean hasRefreshTimeSeconds;
    private Sidekick.Interest interest_ = null;
    private long refreshTimeSeconds_ = 0L;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public Sidekick.Interest getInterest()
    {
      return this.interest_;
    }
    
    public long getRefreshTimeSeconds()
    {
      return this.refreshTimeSeconds_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasInterest();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getInterest());
      }
      if (hasRefreshTimeSeconds()) {
        i += CodedOutputStreamMicro.computeInt64Size(2, getRefreshTimeSeconds());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasInterest()
    {
      return this.hasInterest;
    }
    
    public boolean hasRefreshTimeSeconds()
    {
      return this.hasRefreshTimeSeconds;
    }
    
    public PendingRefresh mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          Sidekick.Interest localInterest = new Sidekick.Interest();
          paramCodedInputStreamMicro.readMessage(localInterest);
          setInterest(localInterest);
          break;
        }
        setRefreshTimeSeconds(paramCodedInputStreamMicro.readInt64());
      }
    }
    
    public PendingRefresh setInterest(Sidekick.Interest paramInterest)
    {
      if (paramInterest == null) {
        throw new NullPointerException();
      }
      this.hasInterest = true;
      this.interest_ = paramInterest;
      return this;
    }
    
    public PendingRefresh setRefreshTimeSeconds(long paramLong)
    {
      this.hasRefreshTimeSeconds = true;
      this.refreshTimeSeconds_ = paramLong;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasInterest()) {
        paramCodedOutputStreamMicro.writeMessage(1, getInterest());
      }
      if (hasRefreshTimeSeconds()) {
        paramCodedOutputStreamMicro.writeInt64(2, getRefreshTimeSeconds());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.apps.sidekick.notifications.Notifications
 * JD-Core Version:    0.7.0.1
 */