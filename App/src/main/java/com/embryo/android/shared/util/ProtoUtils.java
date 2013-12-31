package com.embryo.android.shared.util;

import com.embryo.protobuf.micro.InvalidProtocolBufferMicroException;
import com.embryo.protobuf.micro.MessageMicro;

public class ProtoUtils {
    public static <T extends MessageMicro> T copyOf(T paramT) {
        try {
            MessageMicro localMessageMicro = paramT.getClass().newInstance();
            return (T) copyOf(paramT, localMessageMicro);
        } catch (Exception localException) {
            throw new RuntimeException(localException);
        }
    }

    public static <T extends MessageMicro> T copyOf(T paramT1, T paramT2) {
        try {
            MessageMicro localMessageMicro = paramT2.mergeFrom(paramT1.toByteArray());
            return (T) localMessageMicro;
        } catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {
        }
        return paramT2;
    }

}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ProtoUtils

 * JD-Core Version:    0.7.0.1

 */