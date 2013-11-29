package com.google.android.sidekick.main.sync;

import android.util.Base64;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageMicroUtil
{
  public static <T> void addRepeatedField(MessageMicro paramMessageMicro, Field paramField, T paramT)
  {
    try
    {
      Object localObject = getRepeatedFieldAsList(paramMessageMicro, paramField);
      if (((List)localObject).isEmpty())
      {
        localObject = Lists.newArrayList();
        paramField.set(paramMessageMicro, localObject);
      }
      ((List)localObject).add(paramT);
      return;
    }
    catch (Exception localException)
    {
      Throwables.propagate(localException);
    }
  }
  
  public static void clearFieldValue(MessageMicro paramMessageMicro, Field paramField)
  {
    try
    {
      String str = derivedFieldName("has", paramField);
      Field localField = paramMessageMicro.getClass().getDeclaredField(str);
      localField.setAccessible(true);
      localField.set(paramMessageMicro, Boolean.FALSE);
      if (MessageMicro.class.isAssignableFrom(paramField.getType())) {
        paramField.set(paramMessageMicro, null);
      }
      return;
    }
    catch (Exception localException)
    {
      Throwables.propagate(localException);
    }
  }
  
  static void clearRepeatedField(MessageMicro paramMessageMicro, Field paramField)
  {
    try
    {
      paramField.set(paramMessageMicro, Collections.emptyList());
      return;
    }
    catch (Exception localException)
    {
      Throwables.propagate(localException);
    }
  }
  
  public static <T extends MessageMicro> T decodeFromByteArray(T paramT, byte[] paramArrayOfByte)
    throws InvalidProtocolBufferMicroException
  {
    return paramT.mergeFrom(paramArrayOfByte);
  }
  
  public static <T extends MessageMicro> T decodeFromString(T paramT, String paramString)
    throws InvalidProtocolBufferMicroException
  {
    return paramT.mergeFrom(Base64.decode(paramString, 3));
  }
  
  static String derivedFieldName(String paramString, Field paramField)
  {
    String str = paramField.getName();
    return paramString + Character.toUpperCase(str.charAt(0)) + str.substring(1, -1 + str.length());
  }
  
  public static String encodeAsString(MessageMicro paramMessageMicro)
  {
    return Base64.encodeToString(paramMessageMicro.toByteArray(), 3);
  }
  
  public static byte[] encodeToByteArray(MessageMicro paramMessageMicro)
  {
    return paramMessageMicro.toByteArray();
  }
  
  public static Object getFieldBuilder(MessageMicro paramMessageMicro, Field paramField)
  {
    try
    {
      if (!isRepeatedField(paramField))
      {
        if (hasField(paramMessageMicro, paramField)) {
          return getFieldValue(paramMessageMicro, paramField);
        }
        Object localObject = paramField.getType().newInstance();
        setFieldValue(paramMessageMicro, paramField, localObject);
        return localObject;
      }
    }
    catch (Exception localException)
    {
      return Throwables.propagate(localException);
    }
    String str = derivedFieldName("add", paramField);
    Method[] arrayOfMethod = paramField.getDeclaringClass().getMethods();
    int i = arrayOfMethod.length;
    for (int j = 0;; j++) {
      if (j < i)
      {
        Method localMethod = arrayOfMethod[j];
        if (localMethod.getName().equals(str))
        {
          Class[] arrayOfClass = localMethod.getParameterTypes();
          if (arrayOfClass.length == 1) {
            return arrayOfClass[0].newInstance();
          }
        }
      }
      else
      {
        throw new RuntimeException("Unable to find add method for repeated field: " + paramField.getName());
      }
    }
  }
  
  public static Object getFieldValue(MessageMicro paramMessageMicro, Field paramField)
  {
    try
    {
      Object localObject = paramField.get(paramMessageMicro);
      return localObject;
    }
    catch (Exception localException)
    {
      return Throwables.propagate(localException);
    }
  }
  
  public static Field getProtoField(MessageMicro paramMessageMicro, String paramString)
    throws NoSuchFieldException
  {
    paramMessageMicro.getClass().getDeclaredField(paramString + '_').setAccessible(true);
    return getProtoField(paramMessageMicro.getClass(), paramString);
  }
  
  public static Field getProtoField(Class<? extends MessageMicro> paramClass, String paramString)
    throws NoSuchFieldException
  {
    Field localField = paramClass.getDeclaredField(paramString + '_');
    localField.setAccessible(true);
    return localField;
  }
  
  static List<Field> getProtoFields(MessageMicro paramMessageMicro)
  {
    ArrayList localArrayList = Lists.newArrayList();
    for (Field localField : paramMessageMicro.getClass().getDeclaredFields()) {
      if (localField.getName().endsWith("_"))
      {
        localField.setAccessible(true);
        localArrayList.add(localField);
      }
    }
    return localArrayList;
  }
  
  public static <T> List<T> getRepeatedFieldAsList(MessageMicro paramMessageMicro, Field paramField)
  {
    return (List)getFieldValue(paramMessageMicro, paramField);
  }
  
  public static int getRepeatedFieldCount(MessageMicro paramMessageMicro, Field paramField)
  {
    List localList = getRepeatedFieldAsList(paramMessageMicro, paramField);
    if (localList != null) {
      return localList.size();
    }
    return 0;
  }
  
  public static boolean hasField(MessageMicro paramMessageMicro, Field paramField)
  {
    try
    {
      String str = derivedFieldName("has", paramField);
      Field localField = paramMessageMicro.getClass().getDeclaredField(str);
      localField.setAccessible(true);
      boolean bool = ((Boolean)localField.get(paramMessageMicro)).booleanValue();
      return bool;
    }
    catch (Exception localException)
    {
      Throwables.propagate(localException);
    }
    return false;
  }
  
  public static boolean isEmpty(MessageMicro paramMessageMicro)
  {
    for (Field localField : paramMessageMicro.getClass().getDeclaredFields()) {
      if (localField.getName().endsWith("_"))
      {
        localField.setAccessible(true);
        if ((isRepeatedField(localField)) && (getRepeatedFieldCount(paramMessageMicro, localField) > 0)) {
          return false;
        }
      }
      else if (localField.getName().startsWith("has"))
      {
        localField.setAccessible(true);
        try
        {
          boolean bool = ((Boolean)localField.get(paramMessageMicro)).booleanValue();
          if (bool) {
            return false;
          }
        }
        catch (Exception localException)
        {
          Throwables.propagate(localException);
        }
      }
    }
    return true;
  }
  
  static boolean isMessageField(Field paramField)
  {
    return (MessageMicro.class.isAssignableFrom(paramField.getType())) || (List.class.isAssignableFrom(paramField.getType()));
  }
  
  public static boolean isRepeatedField(Field paramField)
  {
    return List.class.isAssignableFrom(paramField.getType());
  }
  
  public static <T> void setFieldValue(MessageMicro paramMessageMicro, Field paramField, T paramT)
  {
    try
    {
      paramField.set(paramMessageMicro, paramT);
      String str = derivedFieldName("has", paramField);
      Field localField = paramMessageMicro.getClass().getDeclaredField(str);
      localField.setAccessible(true);
      localField.set(paramMessageMicro, Boolean.TRUE);
      return;
    }
    catch (Exception localException)
    {
      Throwables.propagate(localException);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.sync.MessageMicroUtil
 * JD-Core Version:    0.7.0.1
 */