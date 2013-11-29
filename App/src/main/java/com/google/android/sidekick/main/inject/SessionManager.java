package com.google.android.sidekick.main.inject;

import java.util.UUID;

public abstract interface SessionManager
{
  public abstract SessionKey getSessionKey();
  
  public static final class SessionKey
  {
    public final long expirationSeconds;
    public final long jitteredExpirationSeconds;
    public final UUID key;
    
    public SessionKey(UUID paramUUID, long paramLong1, long paramLong2)
    {
      paramUUID.getMostSignificantBits();
      paramUUID.getLeastSignificantBits();
      this.key = paramUUID;
      this.expirationSeconds = paramLong1;
      this.jitteredExpirationSeconds = paramLong2;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.SessionManager
 * JD-Core Version:    0.7.0.1
 */