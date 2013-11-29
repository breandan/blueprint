package com.google.android.sidekick.shared.util;

import com.google.protobuf.micro.MessageMicro;
import java.util.Arrays;

public class ProtoKey<P extends MessageMicro>
{
  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  private volatile byte[] mBytes;
  private volatile Integer mHashCode;
  private final P mProto;
  
  public ProtoKey(P paramP)
  {
    this.mProto = paramP;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {}
    ProtoKey localProtoKey;
    do
    {
      return true;
      if (!(paramObject instanceof ProtoKey)) {
        return false;
      }
      localProtoKey = (ProtoKey)paramObject;
    } while (this.mProto == localProtoKey.mProto);
    if (this.mProto == null) {
      return false;
    }
    if (!this.mProto.getClass().isInstance(localProtoKey.mProto)) {
      return false;
    }
    return Arrays.equals(getBytes(), localProtoKey.getBytes());
  }
  
  public byte[] getBytes()
  {
    if (this.mBytes == null) {
      if (this.mProto != null) {
        break label28;
      }
    }
    label28:
    for (byte[] arrayOfByte = EMPTY_BYTE_ARRAY;; arrayOfByte = this.mProto.toByteArray())
    {
      this.mBytes = arrayOfByte;
      return this.mBytes;
    }
  }
  
  public P getProto()
  {
    return this.mProto;
  }
  
  public int hashCode()
  {
    if (this.mHashCode == null) {
      this.mHashCode = Integer.valueOf(Arrays.hashCode(getBytes()));
    }
    return this.mHashCode.intValue();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.ProtoKey
 * JD-Core Version:    0.7.0.1
 */