package com.google.android.e100;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public class MessageBuffer
{
  private final Map<String, MessageContents> mBuffer = new HashMap();
  private boolean mIsBetweenAnnouncementAndAudioAvailable;
  private String mLatestSenderAnnounced;
  
  public void clear()
  {
    try
    {
      this.mBuffer.clear();
      this.mLatestSenderAnnounced = null;
      this.mIsBetweenAnnouncementAndAudioAvailable = false;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Nullable
  public String onAudioAvailable()
  {
    try
    {
      this.mIsBetweenAnnouncementAndAudioAvailable = false;
      Iterator localIterator = this.mBuffer.keySet().iterator();
      String str;
      do
      {
        boolean bool = localIterator.hasNext();
        localObject2 = null;
        if (!bool) {
          break;
        }
        str = (String)localIterator.next();
      } while (!((MessageContents)this.mBuffer.get(str)).needsAnnouncement());
      Object localObject2 = str;
      ((MessageContents)this.mBuffer.get(str)).onAnnouncementCreated();
      return localObject2;
    }
    finally {}
  }
  
  /* Error */
  public boolean onMessageReceived(String paramString1, String paramString2, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 20	com/google/android/e100/MessageBuffer:mBuffer	Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface 72 2 0
    //   12: ifne +21 -> 33
    //   15: aload_0
    //   16: getfield 20	com/google/android/e100/MessageBuffer:mBuffer	Ljava/util/Map;
    //   19: aload_1
    //   20: new 60	com/google/android/e100/MessageContents
    //   23: dup
    //   24: invokespecial 73	com/google/android/e100/MessageContents:<init>	()V
    //   27: invokeinterface 77 3 0
    //   32: pop
    //   33: aload_0
    //   34: getfield 20	com/google/android/e100/MessageBuffer:mBuffer	Ljava/util/Map;
    //   37: aload_1
    //   38: invokeinterface 58 2 0
    //   43: checkcast 60	com/google/android/e100/MessageContents
    //   46: aload_2
    //   47: iload_3
    //   48: invokevirtual 80	com/google/android/e100/MessageContents:onMessageReceived	(Ljava/lang/String;Z)Z
    //   51: ifeq +23 -> 74
    //   54: aload_0
    //   55: getfield 29	com/google/android/e100/MessageBuffer:mIsBetweenAnnouncementAndAudioAvailable	Z
    //   58: ifne +16 -> 74
    //   61: aload_0
    //   62: iconst_1
    //   63: putfield 29	com/google/android/e100/MessageBuffer:mIsBetweenAnnouncementAndAudioAvailable	Z
    //   66: iconst_1
    //   67: istore 5
    //   69: aload_0
    //   70: monitorexit
    //   71: iload 5
    //   73: ireturn
    //   74: iconst_0
    //   75: istore 5
    //   77: goto -8 -> 69
    //   80: astore 4
    //   82: aload_0
    //   83: monitorexit
    //   84: aload 4
    //   86: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	87	0	this	MessageBuffer
    //   0	87	1	paramString1	String
    //   0	87	2	paramString2	String
    //   0	87	3	paramBoolean	boolean
    //   80	5	4	localObject	Object
    //   67	9	5	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	33	80	finally
    //   33	66	80	finally
  }
  
  public void onSenderAnnounced(String paramString)
  {
    try
    {
      if (!this.mBuffer.containsKey(paramString)) {
        throw new IllegalStateException("Announced unknown sender " + paramString);
      }
    }
    finally {}
    this.mLatestSenderAnnounced = paramString;
  }
  
  public String takeNextMessage()
  {
    MessageContents localMessageContents;
    try
    {
      localMessageContents = (MessageContents)this.mBuffer.get(this.mLatestSenderAnnounced);
      if (localMessageContents == null) {
        throw new IllegalArgumentException("Requested notification from unknown sender: " + this.mLatestSenderAnnounced);
      }
    }
    finally {}
    String str = localMessageContents.takeNextMessage();
    return str;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.e100.MessageBuffer
 * JD-Core Version:    0.7.0.1
 */