package com.google.android.shared.util;

import android.util.Base64;

import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;

public class ProtoUtils {
    public static <T extends MessageMicro> T copyOf(T paramT) {
        try {
            MessageMicro localMessageMicro = (MessageMicro) paramT.getClass().newInstance();
            return copyOf(paramT, localMessageMicro);
        } catch (Exception localException) {
            throw new RuntimeException(localException);
        }
    }

    public static <T extends MessageMicro> T copyOf(T paramT1, T paramT2) {
        try {
            MessageMicro localMessageMicro = paramT2.mergeFrom(paramT1.toByteArray());
            return localMessageMicro;
        } catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {
        }
        return paramT2;
    }

    public static String messageToUrlSafeBase64(MessageMicro paramMessageMicro) {
        return Base64.encodeToString(paramMessageMicro.toByteArray(), 11);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.ProtoUtils

 * JD-Core Version:    0.7.0.1

 */