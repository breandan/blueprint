package com.google.android.sidekick.main.inject;

import android.accounts.Account;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.protobuf.micro.ByteStringMicro;
import javax.annotation.Nullable;

public abstract interface NetworkClient
{
  public abstract boolean isNetworkAvailable();
  
  public abstract Sidekick.ResponsePayload sendRequestWithLocation(Sidekick.RequestPayload paramRequestPayload);
  
  public abstract ResponseAndEventId sendRequestWithLocationCaptureEventId(Sidekick.RequestPayload paramRequestPayload);
  
  public abstract Sidekick.ResponsePayload sendRequestWithoutLocation(Sidekick.RequestPayload paramRequestPayload);
  
  public abstract Sidekick.ResponsePayload sendRequestWithoutLocationWithAccount(Sidekick.RequestPayload paramRequestPayload, Account paramAccount);
  
  public static class ResponseAndEventId
  {
    @Nullable
    public final ByteStringMicro mEventId;
    public final Sidekick.ResponsePayload mPayload;
    
    public ResponseAndEventId(Sidekick.ResponsePayload paramResponsePayload, @Nullable ByteStringMicro paramByteStringMicro)
    {
      this.mPayload = paramResponsePayload;
      this.mEventId = paramByteStringMicro;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.NetworkClient
 * JD-Core Version:    0.7.0.1
 */