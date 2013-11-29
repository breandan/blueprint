package com.android.launcher3;

import java.lang.ref.SoftReference;

abstract class SoftReferenceThreadLocal<T>
{
  private ThreadLocal<SoftReference<T>> mThreadLocal = new ThreadLocal();
  
  public T get()
  {
    SoftReference localSoftReference = (SoftReference)this.mThreadLocal.get();
    if (localSoftReference == null)
    {
      Object localObject2 = initialValue();
      this.mThreadLocal.set(new SoftReference(localObject2));
      return localObject2;
    }
    Object localObject1 = localSoftReference.get();
    if (localObject1 == null)
    {
      localObject1 = initialValue();
      this.mThreadLocal.set(new SoftReference(localObject1));
    }
    return localObject1;
  }
  
  abstract T initialValue();
  
  public void set(T paramT)
  {
    this.mThreadLocal.set(new SoftReference(paramT));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.SoftReferenceThreadLocal
 * JD-Core Version:    0.7.0.1
 */