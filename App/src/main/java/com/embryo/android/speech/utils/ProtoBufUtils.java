package com.embryo.android.speech.utils;

import com.google.speech.s3.S3;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProtoBufUtils {
    private static final ArrayList<String> OBFUSCATE_FILEDS = new ArrayList();

    static {
        OBFUSCATE_FILEDS.add(getString(S3.AuthToken.class, "token"));
    }

    private static void fieldToString(StringBuffer paramStringBuffer, String paramString, com.embryo.protobuf.micro.MessageMicro paramMessageMicro, Field paramField, Object paramObject) {
        if ((paramObject instanceof com.embryo.protobuf.micro.MessageMicro)) {
            if (hasFieldValue(paramMessageMicro, paramField)) {
                protoToString(paramStringBuffer, paramString, getFieldName(paramField), (com.embryo.protobuf.micro.MessageMicro) paramObject);
            }
        }
        if (!hasFieldValue(paramMessageMicro, paramField)) {
            if ((paramObject instanceof List)) {
                fieldsToString(paramStringBuffer, paramString, paramMessageMicro, paramField, (List) paramObject);
                return;
            }
        }
        paramStringBuffer.append(paramString);
        paramStringBuffer.append(getFieldName(paramField));
        paramStringBuffer.append(":");
        valueToString(paramStringBuffer, paramMessageMicro, getFieldName(paramField), paramObject);
        paramStringBuffer.append("\n");
    }

    private static void fieldsToString(StringBuffer paramStringBuffer, String paramString, com.embryo.protobuf.micro.MessageMicro paramMessageMicro, Field paramField, List<?> paramList) {
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            if ((localObject instanceof com.embryo.protobuf.micro.MessageMicro)) {
                protoToString(paramStringBuffer, paramString, getFieldName(paramField), (com.embryo.protobuf.micro.MessageMicro) localObject);
            } else {
                paramStringBuffer.append(paramString);
                paramStringBuffer.append(getFieldName(paramField));
                paramStringBuffer.append(":");
                valueToString(paramStringBuffer, paramMessageMicro, getFieldName(paramField), localObject);
                paramStringBuffer.append("\n");
            }
        }
    }

    private static String getFieldName(Field paramField) {
        String str = paramField.getName();
        return str.substring(0, -1 + str.length());
    }

    private static String getIntFieldValue(com.embryo.protobuf.micro.MessageMicro paramMessageMicro, String paramString, Object paramObject) {
        if (((paramMessageMicro instanceof com.embryo.speech.logs.VoicesearchClientLogProto.ClientEvent)) && (paramString.equals("eventType"))) {
            return DebugEnumUtils.getClientEventTypeLabel(((Integer) paramObject).intValue());
        }
        if (((paramMessageMicro instanceof com.embryo.speech.logs.VoicesearchClientLogProto.LatencyBreakdownEvent)) && (paramString.equals("event"))) {
            return DebugEnumUtils.getLatencyBreakDownLabel(((Integer) paramObject).intValue());
        }
        return paramObject.toString();
    }

    private static ArrayList<Field> getProtoFields(com.embryo.protobuf.micro.MessageMicro paramMessageMicro) {
        ArrayList localArrayList = new ArrayList();
        for (Field localField : paramMessageMicro.getClass().getDeclaredFields()) {
            if (localField.getName().endsWith("_")) {
                localField.setAccessible(true);
                localArrayList.add(localField);
            }
        }
        return localArrayList;
    }

    private static String getString(Class<? extends com.embryo.protobuf.micro.MessageMicro> paramClass, String paramString) {
        return paramClass.getName() + "#" + paramString;
    }

    private static boolean hasFieldValue(com.embryo.protobuf.micro.MessageMicro paramMessageMicro, Field paramField) {
        try {
            String str1 = paramField.getName();
            String str2 = "has" + Character.toUpperCase(str1.charAt(0)) + str1.substring(1, -1 + str1.length());
            Field localField = paramMessageMicro.getClass().getDeclaredField(str2);
            localField.setAccessible(true);
            boolean bool = ((Boolean) localField.get(paramMessageMicro)).booleanValue();
            return bool;
        } catch (NoSuchFieldException localNoSuchFieldException) {
            return false;
        } catch (IllegalArgumentException localIllegalArgumentException) {
            return false;
        } catch (IllegalAccessException localIllegalAccessException) {
        }
        return false;
    }

    private static void protoToString(StringBuffer paramStringBuffer, String paramString1, String paramString2, com.embryo.protobuf.micro.MessageMicro paramMessageMicro) {
        try {
            paramStringBuffer.append(paramString1);
            paramStringBuffer.append(paramString2);
            paramStringBuffer.append(" {");
            paramStringBuffer.append("\n");
            Iterator localIterator = getProtoFields(paramMessageMicro).iterator();
            while (localIterator.hasNext()) {
                Field localField = (Field) localIterator.next();
                Object localObject = localField.get(paramMessageMicro);
                fieldToString(paramStringBuffer, paramString1 + " ", paramMessageMicro, localField, localObject);
            }
        } catch (IllegalArgumentException localIllegalArgumentException) {
            paramStringBuffer.append("Unable to print proto buffer " + localIllegalArgumentException);
            localIllegalArgumentException.printStackTrace();
            paramStringBuffer.append(paramString1);
            paramStringBuffer.append("}\n");
        } catch (IllegalAccessException localIllegalAccessException) {
            paramStringBuffer.append("Unable to print proto buffer " + localIllegalAccessException);
            localIllegalAccessException.printStackTrace();
        }
    }

    public static String toString(com.embryo.protobuf.micro.MessageMicro paramMessageMicro) {
        if (paramMessageMicro == null) {
            return "null";
        }
        StringBuffer localStringBuffer = new StringBuffer();
        protoToString(localStringBuffer, "", paramMessageMicro.getClass().getSimpleName(), paramMessageMicro);
        return localStringBuffer.toString();
    }

    private static void valueToString(StringBuffer paramStringBuffer, com.embryo.protobuf.micro.MessageMicro paramMessageMicro, String paramString, Object paramObject) {
        String str;
        if (paramObject == null) {
            str = "null";
        }
        while (OBFUSCATE_FILEDS.contains(getString(paramMessageMicro.getClass(), paramString))) {
            if ((paramObject instanceof com.embryo.protobuf.micro.ByteStringMicro)) {
                str = "bytes[" + ((com.embryo.protobuf.micro.ByteStringMicro) paramObject).size() + "]";
            } else if ((paramObject instanceof Integer)) {
                str = getIntFieldValue(paramMessageMicro, paramString, paramObject);
            } else {
                str = paramObject.toString();
            }

            paramStringBuffer.append(str.substring(0, Math.min(4, str.length())));
            paramStringBuffer.append("XXXXXXXX");
            paramStringBuffer.append(str);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ProtoBufUtils

 * JD-Core Version:    0.7.0.1

 */