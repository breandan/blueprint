package com.google.android.sidekick.main.sync;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.protobuf.micro.MessageMicro;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.Nullable;

public class MessageVisitor
{
  private final RepeatedMessageInfo mMessageInfo;
  private final boolean mPruneEmptyMessages;
  
  public MessageVisitor(RepeatedMessageInfo paramRepeatedMessageInfo)
  {
    this(paramRepeatedMessageInfo, false);
  }
  
  public MessageVisitor(RepeatedMessageInfo paramRepeatedMessageInfo, boolean paramBoolean)
  {
    this.mMessageInfo = paramRepeatedMessageInfo;
    this.mPruneEmptyMessages = paramBoolean;
  }
  
  private List<Object> listFromRepeatedField(Field paramField, MessageMicro paramMessageMicro)
  {
    if (paramMessageMicro != null) {
      return MessageMicroUtil.getRepeatedFieldAsList(paramMessageMicro, paramField);
    }
    return ImmutableList.of();
  }
  
  private void listToRepeatedField(List<Object> paramList, Field paramField, MessageMicro paramMessageMicro)
  {
    MessageMicroUtil.clearRepeatedField(paramMessageMicro, paramField);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext()) {
      MessageMicroUtil.addRepeatedField(paramMessageMicro, paramField, localIterator.next());
    }
  }
  
  private SortedMap<String, MessageMicro> mapFromRepeatedField(Field paramField, MessageMicro paramMessageMicro)
  {
    TreeMap localTreeMap = Maps.newTreeMap();
    if (paramMessageMicro != null)
    {
      Iterator localIterator = MessageMicroUtil.getRepeatedFieldAsList(paramMessageMicro, paramField).iterator();
      while (localIterator.hasNext())
      {
        MessageMicro localMessageMicro = (MessageMicro)localIterator.next();
        localTreeMap.put(this.mMessageInfo.primaryKeyFor(localMessageMicro), localMessageMicro);
      }
    }
    return localTreeMap;
  }
  
  private void mapToRepeatedField(Map<String, MessageMicro> paramMap, Field paramField, MessageMicro paramMessageMicro)
  {
    MessageMicroUtil.clearRepeatedField(paramMessageMicro, paramField);
    Iterator localIterator = paramMap.values().iterator();
    while (localIterator.hasNext()) {
      MessageMicroUtil.addRepeatedField(paramMessageMicro, paramField, (MessageMicro)localIterator.next());
    }
  }
  
  public void visit(MessageMicro paramMessageMicro1, @Nullable MessageMicro paramMessageMicro2, Visitor paramVisitor)
  {
    int i;
    Iterator localIterator;
    if ((paramMessageMicro2 != null) && (!paramMessageMicro1.getClass().equals(paramMessageMicro2.getClass())))
    {
      i = 1;
      localIterator = MessageMicroUtil.getProtoFields(paramMessageMicro1).iterator();
    }
    for (;;)
    {
      label32:
      if (!localIterator.hasNext()) {
        return;
      }
      Field localField1 = (Field)localIterator.next();
      Field localField2 = localField1;
      if (i != 0) {}
      label216:
      label222:
      do
      {
        for (;;)
        {
          try
          {
            localField2 = paramMessageMicro2.getClass().getDeclaredField(localField1.getName());
            localField2.setAccessible(true);
            if (!MessageMicroUtil.isRepeatedField(localField1)) {
              break label327;
            }
            j = 1;
            if (MessageMicroUtil.getRepeatedFieldCount(paramMessageMicro1, localField1) <= 0) {
              break label222;
            }
            Object localObject2 = MessageMicroUtil.getRepeatedFieldAsList(paramMessageMicro1, localField1).get(0);
            if ((!(localObject2 instanceof MessageMicro)) || (!this.mMessageInfo.hasPrimaryKeyGenerator((MessageMicro)localObject2))) {
              break label216;
            }
            j = 1;
            if (j == 0) {
              break label280;
            }
            if (MessageMicroUtil.getRepeatedFieldCount(paramMessageMicro1, localField1) <= 0) {
              break label32;
            }
            SortedMap localSortedMap = paramVisitor.visitRepeatedMessage(localField1, mapFromRepeatedField(localField1, paramMessageMicro1), paramMessageMicro2, localField2, mapFromRepeatedField(localField2, paramMessageMicro2));
            if ((paramMessageMicro2 == null) || (localSortedMap == null)) {
              break label32;
            }
            mapToRepeatedField(localSortedMap, localField2, paramMessageMicro2);
          }
          catch (NoSuchFieldException localNoSuchFieldException) {}
          i = 0;
          break;
          break label32;
          j = 0;
        }
      } while (MessageMicroUtil.getRepeatedFieldCount(paramMessageMicro2, localField2) <= 0);
      Object localObject1 = MessageMicroUtil.getRepeatedFieldAsList(paramMessageMicro2, localField2).get(0);
      if (((localObject1 instanceof MessageMicro)) && (this.mMessageInfo.hasPrimaryKeyGenerator((MessageMicro)localObject1))) {}
      for (int j = 1;; j = 0) {
        break;
      }
      label280:
      List localList = paramVisitor.visitRepeatedSimpleType(localField1, listFromRepeatedField(localField1, paramMessageMicro1), localField2, listFromRepeatedField(localField2, paramMessageMicro2));
      if ((paramMessageMicro2 != null) && (localList != null))
      {
        listToRepeatedField(localList, localField2, paramMessageMicro2);
        continue;
        label327:
        if (MessageMicroUtil.hasField(paramMessageMicro1, localField1)) {
          if (MessageMicroUtil.isMessageField(localField1))
          {
            if (MessageMicroUtil.hasField(paramMessageMicro2, localField2))
            {
              MessageMicro localMessageMicro1 = (MessageMicro)MessageMicroUtil.getFieldValue(paramMessageMicro1, localField1);
              MessageMicro localMessageMicro2 = null;
              if (paramMessageMicro2 != null) {
                localMessageMicro2 = (MessageMicro)MessageMicroUtil.getFieldBuilder(paramMessageMicro2, localField2);
              }
              visit(localMessageMicro1, localMessageMicro2, paramVisitor);
              if ((this.mPruneEmptyMessages) && (paramMessageMicro2 != null) && (MessageMicroUtil.isEmpty(localMessageMicro2))) {
                MessageMicroUtil.clearFieldValue(paramMessageMicro2, localField2);
              }
            }
            else
            {
              paramVisitor.visitMissingMessage(localField1, paramMessageMicro1, localField2, paramMessageMicro2);
            }
          }
          else {
            paramVisitor.visitScalar(localField1, paramMessageMicro1, localField2, paramMessageMicro2);
          }
        }
      }
    }
  }
  
  public static abstract interface Visitor
  {
    public abstract void visitMissingMessage(Field paramField1, MessageMicro paramMessageMicro1, Field paramField2, @Nullable MessageMicro paramMessageMicro2);
    
    @Nullable
    public abstract SortedMap<String, MessageMicro> visitRepeatedMessage(Field paramField1, SortedMap<String, MessageMicro> paramSortedMap1, MessageMicro paramMessageMicro, Field paramField2, SortedMap<String, MessageMicro> paramSortedMap2);
    
    @Nullable
    public abstract List<Object> visitRepeatedSimpleType(Field paramField1, List<Object> paramList1, Field paramField2, List<Object> paramList2);
    
    public abstract void visitScalar(Field paramField1, MessageMicro paramMessageMicro1, Field paramField2, @Nullable MessageMicro paramMessageMicro2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.sync.MessageVisitor
 * JD-Core Version:    0.7.0.1
 */