package com.google.android.sidekick.main.sync;

import com.google.common.collect.Maps;
import com.google.protobuf.micro.MessageMicro;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.Nullable;

public class StateMerge<T extends MessageMicro>
{
  private final MessageMicro mBuilder;
  private final RepeatedMessageInfo mRepeatedMessageInfo;
  private final boolean mUpdateOnly;
  private final MessageVisitor.Visitor mUpdateVistor = new MessageVisitor.Visitor()
  {
    public void visitMissingMessage(Field paramAnonymousField1, MessageMicro paramAnonymousMessageMicro1, Field paramAnonymousField2, @Nullable MessageMicro paramAnonymousMessageMicro2)
    {
      if (paramAnonymousMessageMicro2 != null) {
        visitScalar(paramAnonymousField1, paramAnonymousMessageMicro1, paramAnonymousField2, paramAnonymousMessageMicro2);
      }
    }
    
    public SortedMap<String, MessageMicro> visitRepeatedMessage(Field paramAnonymousField1, SortedMap<String, MessageMicro> paramAnonymousSortedMap1, MessageMicro paramAnonymousMessageMicro, Field paramAnonymousField2, SortedMap<String, MessageMicro> paramAnonymousSortedMap2)
    {
      TreeMap localTreeMap = Maps.newTreeMap(paramAnonymousSortedMap2);
      int i = 0;
      if (MessageMicroUtil.isMessageField(paramAnonymousField1))
      {
        Iterator localIterator = paramAnonymousSortedMap1.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          String str = (String)localEntry.getKey();
          MessageMicro localMessageMicro1 = (MessageMicro)localEntry.getValue();
          MessageMicro localMessageMicro2 = (MessageMicro)paramAnonymousSortedMap2.get(str);
          if ((!StateMerge.this.mUpdateOnly) && (localMessageMicro2 == null) && (!paramAnonymousField1.getDeclaringClass().equals(paramAnonymousField2.getDeclaringClass())))
          {
            localMessageMicro2 = (MessageMicro)MessageMicroUtil.getFieldBuilder(paramAnonymousMessageMicro, paramAnonymousField2);
            MessageMicroUtil.addRepeatedField(paramAnonymousMessageMicro, paramAnonymousField2, localMessageMicro2);
            localTreeMap.put(str, localMessageMicro2);
            i = 1;
          }
          if (localMessageMicro2 != null)
          {
            StateMerge.newStateMergeFor(localMessageMicro2, StateMerge.this.mRepeatedMessageInfo, StateMerge.this.mUpdateOnly).applyUpdates(localMessageMicro1);
          }
          else if (!StateMerge.this.mUpdateOnly)
          {
            localTreeMap.put(str, localMessageMicro1);
            i = 1;
          }
        }
      }
      localTreeMap.putAll(paramAnonymousSortedMap1);
      if ((paramAnonymousSortedMap2.size() == paramAnonymousSortedMap1.size()) && (i == 0)) {}
      for (int j = 1;; j = 0)
      {
        if ((StateMerge.this.mUpdateOnly) || (j != 0)) {
          localTreeMap = null;
        }
        return localTreeMap;
      }
    }
    
    @Nullable
    public List<Object> visitRepeatedSimpleType(Field paramAnonymousField1, List<Object> paramAnonymousList1, Field paramAnonymousField2, List<Object> paramAnonymousList2)
    {
      return paramAnonymousList1;
    }
    
    public void visitScalar(Field paramAnonymousField1, MessageMicro paramAnonymousMessageMicro1, Field paramAnonymousField2, MessageMicro paramAnonymousMessageMicro2)
    {
      Object localObject = MessageMicroUtil.getFieldValue(paramAnonymousMessageMicro1, paramAnonymousField1);
      if ((MessageMicroUtil.isMessageField(paramAnonymousField1)) && (!paramAnonymousField1.equals(paramAnonymousField2)))
      {
        StateMerge.newStateMergeFor((MessageMicro)MessageMicroUtil.getFieldBuilder(paramAnonymousMessageMicro2, paramAnonymousField2), StateMerge.this.mRepeatedMessageInfo, StateMerge.this.mUpdateOnly).applyUpdates((MessageMicro)localObject);
        return;
      }
      MessageMicroUtil.setFieldValue(paramAnonymousMessageMicro2, paramAnonymousField2, localObject);
    }
  };
  
  private StateMerge(MessageMicro paramMessageMicro, RepeatedMessageInfo paramRepeatedMessageInfo, boolean paramBoolean)
  {
    this.mBuilder = paramMessageMicro;
    if (paramRepeatedMessageInfo != null) {}
    for (;;)
    {
      this.mRepeatedMessageInfo = paramRepeatedMessageInfo;
      this.mUpdateOnly = paramBoolean;
      return;
      paramRepeatedMessageInfo = new RepeatedMessageInfo();
    }
  }
  
  public static <T extends MessageMicro> StateMerge<T> newStateMergeFor(MessageMicro paramMessageMicro, @Nullable RepeatedMessageInfo paramRepeatedMessageInfo, boolean paramBoolean)
  {
    return new StateMerge(paramMessageMicro, paramRepeatedMessageInfo, paramBoolean);
  }
  
  public StateMerge<T> applyUpdates(T paramT)
  {
    new MessageVisitor(this.mRepeatedMessageInfo).visit(paramT, this.mBuilder, this.mUpdateVistor);
    return this;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.sync.StateMerge
 * JD-Core Version:    0.7.0.1
 */