package com.google.android.sidekick.main.sync;

import com.google.android.shared.util.ProtoUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.protobuf.micro.MessageMicro;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.Nullable;

public class StateDifference<T extends MessageMicro>
{
  private static final MessageVisitor.Visitor ADD_VISITOR = new MessageVisitor.Visitor()
  {
    public void visitMissingMessage(Field paramAnonymousField1, MessageMicro paramAnonymousMessageMicro1, Field paramAnonymousField2, @Nullable MessageMicro paramAnonymousMessageMicro2) {}
    
    public SortedMap<String, MessageMicro> visitRepeatedMessage(Field paramAnonymousField1, SortedMap<String, MessageMicro> paramAnonymousSortedMap1, MessageMicro paramAnonymousMessageMicro, Field paramAnonymousField2, SortedMap<String, MessageMicro> paramAnonymousSortedMap2)
    {
      Sets.SetView localSetView = Sets.difference(paramAnonymousSortedMap2.keySet(), paramAnonymousSortedMap1.keySet());
      TreeMap localTreeMap = Maps.newTreeMap();
      Iterator localIterator = localSetView.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localTreeMap.put(str, paramAnonymousSortedMap2.get(str));
      }
      return localTreeMap;
    }
    
    @Nullable
    public List<Object> visitRepeatedSimpleType(Field paramAnonymousField1, List<Object> paramAnonymousList1, Field paramAnonymousField2, List<Object> paramAnonymousList2)
    {
      throw new UnsupportedOperationException("Cannot difference repeated messages of simple types");
    }
    
    public void visitScalar(Field paramAnonymousField1, MessageMicro paramAnonymousMessageMicro1, Field paramAnonymousField2, MessageMicro paramAnonymousMessageMicro2)
    {
      MessageMicroUtil.clearFieldValue(paramAnonymousMessageMicro2, paramAnonymousField2);
    }
  };
  private static final MessageVisitor.Visitor DELETE_VISITOR = new MessageVisitor.Visitor()
  {
    public void visitMissingMessage(Field paramAnonymousField1, MessageMicro paramAnonymousMessageMicro1, Field paramAnonymousField2, @Nullable MessageMicro paramAnonymousMessageMicro2) {}
    
    public SortedMap<String, MessageMicro> visitRepeatedMessage(Field paramAnonymousField1, SortedMap<String, MessageMicro> paramAnonymousSortedMap1, MessageMicro paramAnonymousMessageMicro, Field paramAnonymousField2, SortedMap<String, MessageMicro> paramAnonymousSortedMap2)
    {
      Sets.SetView localSetView = Sets.difference(paramAnonymousSortedMap1.keySet(), paramAnonymousSortedMap2.keySet());
      TreeMap localTreeMap = Maps.newTreeMap();
      Iterator localIterator = localSetView.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localTreeMap.put(str, paramAnonymousSortedMap1.get(str));
      }
      return localTreeMap;
    }
    
    @Nullable
    public List<Object> visitRepeatedSimpleType(Field paramAnonymousField1, List<Object> paramAnonymousList1, Field paramAnonymousField2, List<Object> paramAnonymousList2)
    {
      throw new UnsupportedOperationException("Cannot difference repeated messages of simple types");
    }
    
    public void visitScalar(Field paramAnonymousField1, MessageMicro paramAnonymousMessageMicro1, Field paramAnonymousField2, MessageMicro paramAnonymousMessageMicro2)
    {
      MessageMicroUtil.clearFieldValue(paramAnonymousMessageMicro2, paramAnonymousField2);
    }
  };
  private static final MessageVisitor.Visitor UPDATE_VISITOR = new MessageVisitor.Visitor()
  {
    public void visitMissingMessage(Field paramAnonymousField1, MessageMicro paramAnonymousMessageMicro1, Field paramAnonymousField2, @Nullable MessageMicro paramAnonymousMessageMicro2) {}
    
    public SortedMap<String, MessageMicro> visitRepeatedMessage(Field paramAnonymousField1, SortedMap<String, MessageMicro> paramAnonymousSortedMap1, MessageMicro paramAnonymousMessageMicro, Field paramAnonymousField2, SortedMap<String, MessageMicro> paramAnonymousSortedMap2)
    {
      TreeMap localTreeMap = Maps.newTreeMap();
      Iterator localIterator = Sets.intersection(paramAnonymousSortedMap1.keySet(), paramAnonymousSortedMap2.keySet()).iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        MessageMicro localMessageMicro1 = (MessageMicro)paramAnonymousSortedMap1.get(str);
        MessageMicro localMessageMicro2 = (MessageMicro)paramAnonymousSortedMap2.get(str);
        if (!Arrays.equals(localMessageMicro1.toByteArray(), localMessageMicro2.toByteArray())) {
          localTreeMap.put(str, paramAnonymousSortedMap2.get(str));
        }
      }
      return localTreeMap;
    }
    
    @Nullable
    public List<Object> visitRepeatedSimpleType(Field paramAnonymousField1, List<Object> paramAnonymousList1, Field paramAnonymousField2, List<Object> paramAnonymousList2)
    {
      throw new UnsupportedOperationException("Cannot difference repeated messages of simple types");
    }
    
    public void visitScalar(Field paramAnonymousField1, MessageMicro paramAnonymousMessageMicro1, Field paramAnonymousField2, MessageMicro paramAnonymousMessageMicro2)
    {
      if (MessageMicroUtil.getFieldValue(paramAnonymousMessageMicro1, paramAnonymousField1).equals(MessageMicroUtil.getFieldValue(paramAnonymousMessageMicro2, paramAnonymousField2))) {
        MessageMicroUtil.clearFieldValue(paramAnonymousMessageMicro2, paramAnonymousField2);
      }
    }
  };
  private final T mFromMessage;
  private final MessageVisitor mMessageVisitor;
  private final T mToMessage;
  
  private StateDifference(T paramT1, T paramT2, RepeatedMessageInfo paramRepeatedMessageInfo)
  {
    this.mFromMessage = paramT1;
    this.mToMessage = paramT2;
    this.mMessageVisitor = new MessageVisitor(paramRepeatedMessageInfo, true);
  }
  
  private static <T extends MessageMicro> T newMessageMicro(Class<T> paramClass)
  {
    try
    {
      MessageMicro localMessageMicro = (MessageMicro)paramClass.newInstance();
      return localMessageMicro;
    }
    catch (InstantiationException localInstantiationException)
    {
      throw new RuntimeException(localInstantiationException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new RuntimeException(localIllegalAccessException);
    }
  }
  
  public static <T extends MessageMicro> StateDifference<T> newStateDifference(T paramT1, T paramT2, RepeatedMessageInfo paramRepeatedMessageInfo)
  {
    return new StateDifference(paramT1, paramT2, paramRepeatedMessageInfo);
  }
  
  public T compareForAdds()
  {
    MessageMicro localMessageMicro = newMessageMicro(this.mFromMessage.getClass());
    ProtoUtils.copyOf(this.mToMessage, localMessageMicro);
    this.mMessageVisitor.visit(this.mFromMessage, localMessageMicro, ADD_VISITOR);
    return localMessageMicro;
  }
  
  public T compareForDeletes()
  {
    MessageMicro localMessageMicro = newMessageMicro(this.mFromMessage.getClass());
    ProtoUtils.copyOf(this.mToMessage, localMessageMicro);
    this.mMessageVisitor.visit(this.mFromMessage, localMessageMicro, DELETE_VISITOR);
    return localMessageMicro;
  }
  
  public T compareForUpdates()
  {
    MessageMicro localMessageMicro = newMessageMicro(this.mFromMessage.getClass());
    ProtoUtils.copyOf(this.mToMessage, localMessageMicro);
    this.mMessageVisitor.visit(this.mFromMessage, localMessageMicro, UPDATE_VISITOR);
    return localMessageMicro;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.sync.StateDifference
 * JD-Core Version:    0.7.0.1
 */