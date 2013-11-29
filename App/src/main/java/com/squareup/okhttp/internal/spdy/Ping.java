package com.squareup.okhttp.internal.spdy;

import java.util.concurrent.CountDownLatch;

public final class Ping
{
  private final CountDownLatch latch = new CountDownLatch(1);
  private long received = -1L;
  private long sent = -1L;
  
  void cancel()
  {
    if ((this.received != -1L) || (this.sent == -1L)) {
      throw new IllegalStateException();
    }
    this.received = (this.sent - 1L);
    this.latch.countDown();
  }
  
  void receive()
  {
    if ((this.received != -1L) || (this.sent == -1L)) {
      throw new IllegalStateException();
    }
    this.received = System.nanoTime();
    this.latch.countDown();
  }
  
  void send()
  {
    if (this.sent != -1L) {
      throw new IllegalStateException();
    }
    this.sent = System.nanoTime();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.internal.spdy.Ping
 * JD-Core Version:    0.7.0.1
 */