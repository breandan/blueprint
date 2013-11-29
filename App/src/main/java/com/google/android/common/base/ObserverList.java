package com.google.android.common.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ObserverList<E>
  implements Iterable<E>
{
  private int mIterationDepth = 0;
  public final List<E> mObservers = new ArrayList();
  
  static
  {
    if (!ObserverList.class.desiredAssertionStatus()) {}
    for (boolean bool = true;; bool = false)
    {
      $assertionsDisabled = bool;
      return;
    }
  }
  
  private void compact()
  {
    assert (this.mIterationDepth == 0);
    Iterator localIterator = this.mObservers.iterator();
    while (localIterator.hasNext()) {
      if (localIterator.next() == null) {
        localIterator.remove();
      }
    }
  }
  
  private void decrementIterationDepthAndCompactIfNeeded()
  {
    this.mIterationDepth = (-1 + this.mIterationDepth);
    assert (this.mIterationDepth >= 0);
    if (this.mIterationDepth == 0) {
      compact();
    }
  }
  
  private E getObserverAt(int paramInt)
  {
    return this.mObservers.get(paramInt);
  }
  
  public void addObserver(E paramE)
  {
    if ((paramE == null) || (this.mObservers.contains(paramE))) {
      return;
    }
    this.mObservers.add(paramE);
  }
  
  public boolean hasObserver(E paramE)
  {
    return this.mObservers.contains(paramE);
  }
  
  public Iterator<E> iterator()
  {
    return new ObserverListIterator(null);
  }
  
  public void removeObserver(E paramE)
  {
    int i = this.mObservers.indexOf(paramE);
    if (i == -1) {
      return;
    }
    if (this.mIterationDepth == 0)
    {
      this.mObservers.remove(paramE);
      return;
    }
    this.mObservers.set(i, null);
  }
  
  private class ObserverListIterator
    implements Iterator<E>
  {
    private int mIndex = 0;
    private boolean mIsExhausted = false;
    private final int mListEndMarker;
    
    private ObserverListIterator()
    {
      ObserverList.access$108(ObserverList.this);
      this.mListEndMarker = ObserverList.this.mObservers.size();
    }
    
    private void compactListIfNeeded()
    {
      if (!this.mIsExhausted)
      {
        this.mIsExhausted = true;
        ObserverList.this.decrementIterationDepthAndCompactIfNeeded();
      }
    }
    
    public boolean hasNext()
    {
      for (int i = this.mIndex; (i < this.mListEndMarker) && (ObserverList.this.getObserverAt(i) == null); i++) {}
      if (i < this.mListEndMarker) {
        return true;
      }
      compactListIfNeeded();
      return false;
    }
    
    public E next()
    {
      while ((this.mIndex < this.mListEndMarker) && (ObserverList.this.getObserverAt(this.mIndex) == null)) {
        this.mIndex = (1 + this.mIndex);
      }
      if (this.mIndex < this.mListEndMarker)
      {
        ObserverList localObserverList = ObserverList.this;
        int i = this.mIndex;
        this.mIndex = (i + 1);
        return localObserverList.getObserverAt(i);
      }
      compactListIfNeeded();
      throw new NoSuchElementException();
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.common.base.ObserverList
 * JD-Core Version:    0.7.0.1
 */