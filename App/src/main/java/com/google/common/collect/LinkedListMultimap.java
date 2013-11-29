package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

public class LinkedListMultimap<K, V>
  implements ListMultimap<K, V>, Serializable
{
  private static final long serialVersionUID;
  private transient List<Map.Entry<K, V>> entries;
  private transient Node<K, V> head;
  private transient Multiset<K> keyCount = LinkedHashMultiset.create();
  private transient Set<K> keySet;
  private transient Map<K, Node<K, V>> keyToKeyHead = Maps.newHashMap();
  private transient Map<K, Node<K, V>> keyToKeyTail = Maps.newHashMap();
  private transient Map<K, Collection<V>> map;
  private transient Node<K, V> tail;
  private transient List<V> valuesList;
  
  private Node<K, V> addNode(@Nullable K paramK, @Nullable V paramV, @Nullable Node<K, V> paramNode)
  {
    Node localNode1 = new Node(paramK, paramV);
    if (this.head == null)
    {
      this.tail = localNode1;
      this.head = localNode1;
      this.keyToKeyHead.put(paramK, localNode1);
      this.keyToKeyTail.put(paramK, localNode1);
      this.keyCount.add(paramK);
      return localNode1;
    }
    if (paramNode == null)
    {
      this.tail.next = localNode1;
      localNode1.previous = this.tail;
      Node localNode2 = (Node)this.keyToKeyTail.get(paramK);
      if (localNode2 == null) {
        this.keyToKeyHead.put(paramK, localNode1);
      }
      for (;;)
      {
        this.keyToKeyTail.put(paramK, localNode1);
        this.tail = localNode1;
        break;
        localNode2.nextSibling = localNode1;
        localNode1.previousSibling = localNode2;
      }
    }
    localNode1.previous = paramNode.previous;
    localNode1.previousSibling = paramNode.previousSibling;
    localNode1.next = paramNode;
    localNode1.nextSibling = paramNode;
    if (paramNode.previousSibling == null)
    {
      this.keyToKeyHead.put(paramK, localNode1);
      label214:
      if (paramNode.previous != null) {
        break label254;
      }
      this.head = localNode1;
    }
    for (;;)
    {
      paramNode.previous = localNode1;
      paramNode.previousSibling = localNode1;
      break;
      paramNode.previousSibling.nextSibling = localNode1;
      break label214;
      label254:
      paramNode.previous.next = localNode1;
    }
  }
  
  private static void checkElement(@Nullable Object paramObject)
  {
    if (paramObject == null) {
      throw new NoSuchElementException();
    }
  }
  
  public static <K, V> LinkedListMultimap<K, V> create()
  {
    return new LinkedListMultimap();
  }
  
  private static <K, V> Map.Entry<K, V> createEntry(Node<K, V> paramNode)
  {
    new AbstractMapEntry()
    {
      public K getKey()
      {
        return this.val$node.key;
      }
      
      public V getValue()
      {
        return this.val$node.value;
      }
      
      public V setValue(V paramAnonymousV)
      {
        Object localObject = this.val$node.value;
        this.val$node.value = paramAnonymousV;
        return localObject;
      }
    };
  }
  
  private List<V> getCopy(@Nullable Object paramObject)
  {
    return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(paramObject)));
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    this.keyCount = LinkedHashMultiset.create();
    this.keyToKeyHead = Maps.newHashMap();
    this.keyToKeyTail = Maps.newHashMap();
    int i = paramObjectInputStream.readInt();
    for (int j = 0; j < i; j++) {
      put(paramObjectInputStream.readObject(), paramObjectInputStream.readObject());
    }
  }
  
  private void removeAllNodes(@Nullable Object paramObject)
  {
    ValueForKeyIterator localValueForKeyIterator = new ValueForKeyIterator(paramObject);
    while (localValueForKeyIterator.hasNext())
    {
      localValueForKeyIterator.next();
      localValueForKeyIterator.remove();
    }
  }
  
  private void removeNode(Node<K, V> paramNode)
  {
    if (paramNode.previous != null)
    {
      paramNode.previous.next = paramNode.next;
      if (paramNode.next == null) {
        break label98;
      }
      paramNode.next.previous = paramNode.previous;
      label36:
      if (paramNode.previousSibling == null) {
        break label109;
      }
      paramNode.previousSibling.nextSibling = paramNode.nextSibling;
      label54:
      if (paramNode.nextSibling == null) {
        break label154;
      }
      paramNode.nextSibling.previousSibling = paramNode.previousSibling;
    }
    for (;;)
    {
      this.keyCount.remove(paramNode.key);
      return;
      this.head = paramNode.next;
      break;
      label98:
      this.tail = paramNode.previous;
      break label36;
      label109:
      if (paramNode.nextSibling != null)
      {
        this.keyToKeyHead.put(paramNode.key, paramNode.nextSibling);
        break label54;
      }
      this.keyToKeyHead.remove(paramNode.key);
      break label54;
      label154:
      if (paramNode.previousSibling != null) {
        this.keyToKeyTail.put(paramNode.key, paramNode.previousSibling);
      } else {
        this.keyToKeyTail.remove(paramNode.key);
      }
    }
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    paramObjectOutputStream.writeInt(size());
    Iterator localIterator = entries().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramObjectOutputStream.writeObject(localEntry.getKey());
      paramObjectOutputStream.writeObject(localEntry.getValue());
    }
  }
  
  public Map<K, Collection<V>> asMap()
  {
    Object localObject = this.map;
    if (localObject == null)
    {
      localObject = new AbstractMap()
      {
        Set<Map.Entry<K, Collection<V>>> entrySet;
        
        public boolean containsKey(@Nullable Object paramAnonymousObject)
        {
          return LinkedListMultimap.this.containsKey(paramAnonymousObject);
        }
        
        public Set<Map.Entry<K, Collection<V>>> entrySet()
        {
          Object localObject = this.entrySet;
          if (localObject == null)
          {
            localObject = new LinkedListMultimap.AsMapEntries(LinkedListMultimap.this, null);
            this.entrySet = ((Set)localObject);
          }
          return localObject;
        }
        
        public Collection<V> get(@Nullable Object paramAnonymousObject)
        {
          List localList = LinkedListMultimap.this.get(paramAnonymousObject);
          if (localList.isEmpty()) {
            localList = null;
          }
          return localList;
        }
        
        public Collection<V> remove(@Nullable Object paramAnonymousObject)
        {
          List localList = LinkedListMultimap.this.removeAll(paramAnonymousObject);
          if (localList.isEmpty()) {
            localList = null;
          }
          return localList;
        }
      };
      this.map = ((Map)localObject);
    }
    return localObject;
  }
  
  public void clear()
  {
    this.head = null;
    this.tail = null;
    this.keyCount.clear();
    this.keyToKeyHead.clear();
    this.keyToKeyTail.clear();
  }
  
  public boolean containsEntry(@Nullable Object paramObject1, @Nullable Object paramObject2)
  {
    ValueForKeyIterator localValueForKeyIterator = new ValueForKeyIterator(paramObject1);
    while (localValueForKeyIterator.hasNext()) {
      if (Objects.equal(localValueForKeyIterator.next(), paramObject2)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean containsKey(@Nullable Object paramObject)
  {
    return this.keyToKeyHead.containsKey(paramObject);
  }
  
  public boolean containsValue(@Nullable Object paramObject)
  {
    NodeIterator localNodeIterator = new NodeIterator();
    while (localNodeIterator.hasNext()) {
      if (Objects.equal(((Node)localNodeIterator.next()).value, paramObject)) {
        return true;
      }
    }
    return false;
  }
  
  public List<Map.Entry<K, V>> entries()
  {
    Object localObject = this.entries;
    if (localObject == null)
    {
      localObject = new AbstractSequentialList()
      {
        public ListIterator<Map.Entry<K, V>> listIterator(int paramAnonymousInt)
        {
          new ListIterator()
          {
            public void add(Map.Entry<K, V> paramAnonymous2Entry)
            {
              throw new UnsupportedOperationException();
            }
            
            public boolean hasNext()
            {
              return this.val$nodes.hasNext();
            }
            
            public boolean hasPrevious()
            {
              return this.val$nodes.hasPrevious();
            }
            
            public Map.Entry<K, V> next()
            {
              return LinkedListMultimap.createEntry((LinkedListMultimap.Node)this.val$nodes.next());
            }
            
            public int nextIndex()
            {
              return this.val$nodes.nextIndex();
            }
            
            public Map.Entry<K, V> previous()
            {
              return LinkedListMultimap.createEntry((LinkedListMultimap.Node)this.val$nodes.previous());
            }
            
            public int previousIndex()
            {
              return this.val$nodes.previousIndex();
            }
            
            public void remove()
            {
              this.val$nodes.remove();
            }
            
            public void set(Map.Entry<K, V> paramAnonymous2Entry)
            {
              throw new UnsupportedOperationException();
            }
          };
        }
        
        public int size()
        {
          return LinkedListMultimap.this.keyCount.size();
        }
      };
      this.entries = ((List)localObject);
    }
    return localObject;
  }
  
  public boolean equals(@Nullable Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if ((paramObject instanceof Multimap))
    {
      Multimap localMultimap = (Multimap)paramObject;
      return asMap().equals(localMultimap.asMap());
    }
    return false;
  }
  
  public List<V> get(@Nullable final K paramK)
  {
    new AbstractSequentialList()
    {
      public ListIterator<V> listIterator(int paramAnonymousInt)
      {
        return new LinkedListMultimap.ValueForKeyIterator(LinkedListMultimap.this, paramK, paramAnonymousInt);
      }
      
      public boolean removeAll(Collection<?> paramAnonymousCollection)
      {
        return Iterators.removeAll(iterator(), paramAnonymousCollection);
      }
      
      public boolean retainAll(Collection<?> paramAnonymousCollection)
      {
        return Iterators.retainAll(iterator(), paramAnonymousCollection);
      }
      
      public int size()
      {
        return LinkedListMultimap.this.keyCount.count(paramK);
      }
    };
  }
  
  public int hashCode()
  {
    return asMap().hashCode();
  }
  
  public Set<K> keySet()
  {
    Object localObject = this.keySet;
    if (localObject == null)
    {
      localObject = new AbstractSet()
      {
        public boolean contains(Object paramAnonymousObject)
        {
          return LinkedListMultimap.this.keyCount.contains(paramAnonymousObject);
        }
        
        public Iterator<K> iterator()
        {
          return new LinkedListMultimap.DistinctKeyIterator(LinkedListMultimap.this, null);
        }
        
        public boolean removeAll(Collection<?> paramAnonymousCollection)
        {
          Preconditions.checkNotNull(paramAnonymousCollection);
          return super.removeAll(paramAnonymousCollection);
        }
        
        public int size()
        {
          return LinkedListMultimap.this.keyCount.elementSet().size();
        }
      };
      this.keySet = ((Set)localObject);
    }
    return localObject;
  }
  
  public boolean put(@Nullable K paramK, @Nullable V paramV)
  {
    addNode(paramK, paramV, null);
    return true;
  }
  
  public boolean remove(@Nullable Object paramObject1, @Nullable Object paramObject2)
  {
    ValueForKeyIterator localValueForKeyIterator = new ValueForKeyIterator(paramObject1);
    while (localValueForKeyIterator.hasNext()) {
      if (Objects.equal(localValueForKeyIterator.next(), paramObject2))
      {
        localValueForKeyIterator.remove();
        return true;
      }
    }
    return false;
  }
  
  public List<V> removeAll(@Nullable Object paramObject)
  {
    List localList = getCopy(paramObject);
    removeAllNodes(paramObject);
    return localList;
  }
  
  public int size()
  {
    return this.keyCount.size();
  }
  
  public String toString()
  {
    return asMap().toString();
  }
  
  public List<V> values()
  {
    Object localObject = this.valuesList;
    if (localObject == null)
    {
      localObject = new AbstractSequentialList()
      {
        public ListIterator<V> listIterator(int paramAnonymousInt)
        {
          new ListIterator()
          {
            public void add(V paramAnonymous2V)
            {
              throw new UnsupportedOperationException();
            }
            
            public boolean hasNext()
            {
              return this.val$nodes.hasNext();
            }
            
            public boolean hasPrevious()
            {
              return this.val$nodes.hasPrevious();
            }
            
            public V next()
            {
              return this.val$nodes.next().value;
            }
            
            public int nextIndex()
            {
              return this.val$nodes.nextIndex();
            }
            
            public V previous()
            {
              return this.val$nodes.previous().value;
            }
            
            public int previousIndex()
            {
              return this.val$nodes.previousIndex();
            }
            
            public void remove()
            {
              this.val$nodes.remove();
            }
            
            public void set(V paramAnonymous2V)
            {
              this.val$nodes.setValue(paramAnonymous2V);
            }
          };
        }
        
        public int size()
        {
          return LinkedListMultimap.this.keyCount.size();
        }
      };
      this.valuesList = ((List)localObject);
    }
    return localObject;
  }
  
  private class AsMapEntries
    extends AbstractSet<Map.Entry<K, Collection<V>>>
  {
    private AsMapEntries() {}
    
    public Iterator<Map.Entry<K, Collection<V>>> iterator()
    {
      new Iterator()
      {
        public boolean hasNext()
        {
          return this.val$keyIterator.hasNext();
        }
        
        public Map.Entry<K, Collection<V>> next()
        {
          new AbstractMapEntry()
          {
            public K getKey()
            {
              return this.val$key;
            }
            
            public Collection<V> getValue()
            {
              return LinkedListMultimap.this.get(this.val$key);
            }
          };
        }
        
        public void remove()
        {
          this.val$keyIterator.remove();
        }
      };
    }
    
    public int size()
    {
      return LinkedListMultimap.this.keyCount.elementSet().size();
    }
  }
  
  private class DistinctKeyIterator
    implements Iterator<K>
  {
    LinkedListMultimap.Node<K, V> current;
    LinkedListMultimap.Node<K, V> next = LinkedListMultimap.this.head;
    final Set<K> seenKeys = Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size());
    
    private DistinctKeyIterator() {}
    
    public boolean hasNext()
    {
      return this.next != null;
    }
    
    public K next()
    {
      LinkedListMultimap.checkElement(this.next);
      this.current = this.next;
      this.seenKeys.add(this.current.key);
      do
      {
        this.next = this.next.next;
      } while ((this.next != null) && (!this.seenKeys.add(this.next.key)));
      return this.current.key;
    }
    
    public void remove()
    {
      if (this.current != null) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkState(bool);
        LinkedListMultimap.this.removeAllNodes(this.current.key);
        this.current = null;
        return;
      }
    }
  }
  
  private static final class Node<K, V>
  {
    final K key;
    Node<K, V> next;
    Node<K, V> nextSibling;
    Node<K, V> previous;
    Node<K, V> previousSibling;
    V value;
    
    Node(@Nullable K paramK, @Nullable V paramV)
    {
      this.key = paramK;
      this.value = paramV;
    }
    
    public String toString()
    {
      return this.key + "=" + this.value;
    }
  }
  
  private class NodeIterator
    implements ListIterator<LinkedListMultimap.Node<K, V>>
  {
    LinkedListMultimap.Node<K, V> current;
    LinkedListMultimap.Node<K, V> next;
    int nextIndex;
    LinkedListMultimap.Node<K, V> previous;
    
    NodeIterator()
    {
      this.next = LinkedListMultimap.this.head;
    }
    
    NodeIterator(int paramInt)
    {
      int i = LinkedListMultimap.this.size();
      Preconditions.checkPositionIndex(paramInt, i);
      if (paramInt >= i / 2)
      {
        this.previous = LinkedListMultimap.this.tail;
        this.nextIndex = i;
        int n;
        for (int m = paramInt;; m = n)
        {
          n = m + 1;
          if (m >= i) {
            break;
          }
          previous();
        }
      }
      this.next = LinkedListMultimap.this.head;
      int k;
      for (int j = paramInt;; j = k)
      {
        k = j - 1;
        if (j <= 0) {
          break;
        }
        next();
      }
      this.current = null;
    }
    
    public void add(LinkedListMultimap.Node<K, V> paramNode)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean hasNext()
    {
      return this.next != null;
    }
    
    public boolean hasPrevious()
    {
      return this.previous != null;
    }
    
    public LinkedListMultimap.Node<K, V> next()
    {
      LinkedListMultimap.checkElement(this.next);
      LinkedListMultimap.Node localNode = this.next;
      this.current = localNode;
      this.previous = localNode;
      this.next = this.next.next;
      this.nextIndex = (1 + this.nextIndex);
      return this.current;
    }
    
    public int nextIndex()
    {
      return this.nextIndex;
    }
    
    public LinkedListMultimap.Node<K, V> previous()
    {
      LinkedListMultimap.checkElement(this.previous);
      LinkedListMultimap.Node localNode = this.previous;
      this.current = localNode;
      this.next = localNode;
      this.previous = this.previous.previous;
      this.nextIndex = (-1 + this.nextIndex);
      return this.current;
    }
    
    public int previousIndex()
    {
      return -1 + this.nextIndex;
    }
    
    public void remove()
    {
      boolean bool;
      if (this.current != null)
      {
        bool = true;
        Preconditions.checkState(bool);
        if (this.current == this.next) {
          break label67;
        }
        this.previous = this.current.previous;
        this.nextIndex = (-1 + this.nextIndex);
      }
      for (;;)
      {
        LinkedListMultimap.this.removeNode(this.current);
        this.current = null;
        return;
        bool = false;
        break;
        label67:
        this.next = this.current.next;
      }
    }
    
    public void set(LinkedListMultimap.Node<K, V> paramNode)
    {
      throw new UnsupportedOperationException();
    }
    
    void setValue(V paramV)
    {
      if (this.current != null) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkState(bool);
        this.current.value = paramV;
        return;
      }
    }
  }
  
  private class ValueForKeyIterator
    implements ListIterator<V>
  {
    LinkedListMultimap.Node<K, V> current;
    final Object key;
    LinkedListMultimap.Node<K, V> next;
    int nextIndex;
    LinkedListMultimap.Node<K, V> previous;
    
    ValueForKeyIterator(@Nullable Object paramObject)
    {
      this.key = paramObject;
      this.next = ((LinkedListMultimap.Node)LinkedListMultimap.this.keyToKeyHead.get(paramObject));
    }
    
    public ValueForKeyIterator(@Nullable Object paramObject, int paramInt)
    {
      int i = LinkedListMultimap.this.keyCount.count(paramObject);
      Preconditions.checkPositionIndex(paramInt, i);
      if (paramInt >= i / 2)
      {
        this.previous = ((LinkedListMultimap.Node)LinkedListMultimap.this.keyToKeyTail.get(paramObject));
        this.nextIndex = i;
        int n;
        for (int m = paramInt;; m = n)
        {
          n = m + 1;
          if (m >= i) {
            break;
          }
          previous();
        }
      }
      this.next = ((LinkedListMultimap.Node)LinkedListMultimap.this.keyToKeyHead.get(paramObject));
      int k;
      for (int j = paramInt;; j = k)
      {
        k = j - 1;
        if (j <= 0) {
          break;
        }
        next();
      }
      this.key = paramObject;
      this.current = null;
    }
    
    public void add(V paramV)
    {
      this.previous = LinkedListMultimap.this.addNode(this.key, paramV, this.next);
      this.nextIndex = (1 + this.nextIndex);
      this.current = null;
    }
    
    public boolean hasNext()
    {
      return this.next != null;
    }
    
    public boolean hasPrevious()
    {
      return this.previous != null;
    }
    
    public V next()
    {
      LinkedListMultimap.checkElement(this.next);
      LinkedListMultimap.Node localNode = this.next;
      this.current = localNode;
      this.previous = localNode;
      this.next = this.next.nextSibling;
      this.nextIndex = (1 + this.nextIndex);
      return this.current.value;
    }
    
    public int nextIndex()
    {
      return this.nextIndex;
    }
    
    public V previous()
    {
      LinkedListMultimap.checkElement(this.previous);
      LinkedListMultimap.Node localNode = this.previous;
      this.current = localNode;
      this.next = localNode;
      this.previous = this.previous.previousSibling;
      this.nextIndex = (-1 + this.nextIndex);
      return this.current.value;
    }
    
    public int previousIndex()
    {
      return -1 + this.nextIndex;
    }
    
    public void remove()
    {
      boolean bool;
      if (this.current != null)
      {
        bool = true;
        Preconditions.checkState(bool);
        if (this.current == this.next) {
          break label67;
        }
        this.previous = this.current.previousSibling;
        this.nextIndex = (-1 + this.nextIndex);
      }
      for (;;)
      {
        LinkedListMultimap.this.removeNode(this.current);
        this.current = null;
        return;
        bool = false;
        break;
        label67:
        this.next = this.current.nextSibling;
      }
    }
    
    public void set(V paramV)
    {
      if (this.current != null) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkState(bool);
        this.current.value = paramV;
        return;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.LinkedListMultimap
 * JD-Core Version:    0.7.0.1
 */