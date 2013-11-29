package com.google.android.sidekick.shared.remoteapi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import javax.annotation.Nullable;

public class ProtoParcelable
  implements Parcelable
{
  public static final Parcelable.Creator<ProtoParcelable> CREATOR = new Parcelable.Creator()
  {
    public ProtoParcelable createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ProtoParcelable(paramAnonymousParcel.createByteArray(), null);
    }
    
    public ProtoParcelable[] newArray(int paramAnonymousInt)
    {
      return new ProtoParcelable[paramAnonymousInt];
    }
  };
  @Nullable
  byte[] mData;
  @Nullable
  MessageMicro mProto;
  boolean protoSet;
  
  private ProtoParcelable(@Nullable MessageMicro paramMessageMicro)
  {
    this.mProto = paramMessageMicro;
    this.protoSet = true;
  }
  
  private ProtoParcelable(@Nullable byte[] paramArrayOfByte)
  {
    this.mData = paramArrayOfByte;
    this.protoSet = false;
  }
  
  public static ProtoParcelable create(@Nullable MessageMicro paramMessageMicro)
  {
    return new ProtoParcelable(paramMessageMicro);
  }
  
  @Nullable
  private byte[] getData()
  {
    if ((this.mData == null) && (this.protoSet) && (this.mProto != null)) {
      this.mData = this.mProto.toByteArray();
    }
    return this.mData;
  }
  
  @Nullable
  public static <T extends MessageMicro> T parseProto(@Nullable byte[] paramArrayOfByte, Class<T> paramClass)
    throws IllegalArgumentException
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    try
    {
      MessageMicro localMessageMicro = ((MessageMicro)paramClass.newInstance()).mergeFrom(paramArrayOfByte);
      return localMessageMicro;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      throw new IllegalArgumentException(localInvalidProtocolBufferMicroException);
    }
    catch (InstantiationException localInstantiationException)
    {
      throw new IllegalArgumentException(localInstantiationException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new IllegalArgumentException(localIllegalAccessException);
    }
  }
  
  @Nullable
  public static <T extends MessageMicro> T readProtoFromParcel(Parcel paramParcel, Class<T> paramClass)
  {
    return parseProto(paramParcel.createByteArray(), paramClass);
  }
  
  public static void writeProtoToParcel(@Nullable MessageMicro paramMessageMicro, Parcel paramParcel)
  {
    if (paramMessageMicro != null) {}
    for (byte[] arrayOfByte = paramMessageMicro.toByteArray();; arrayOfByte = null)
    {
      paramParcel.writeByteArray(arrayOfByte);
      return;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @Nullable
  public <T extends MessageMicro> T getProto(Class<T> paramClass)
  {
    if (this.mProto == null)
    {
      if (this.protoSet) {
        return null;
      }
      this.mProto = parseProto(this.mData, paramClass);
      this.protoSet = true;
    }
    try
    {
      MessageMicro localMessageMicro = this.mProto;
      return localMessageMicro;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new IllegalArgumentException(localClassCastException);
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(getData());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.remoteapi.ProtoParcelable
 * JD-Core Version:    0.7.0.1
 */