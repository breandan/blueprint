package com.google.android.velvet.presenter.inappwebpage;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Printer;
import com.google.android.shared.util.PrefixPrinter;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

final class RequestStack
  implements Parcelable, Iterable<Request>
{
  public static final Parcelable.Creator<RequestStack> CREATOR = new Creator(null);
  private final ArrayDeque<Request> mDeque = new ArrayDeque();
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(Printer paramPrinter)
  {
    PrefixPrinter localPrefixPrinter = new PrefixPrinter(paramPrinter);
    localPrefixPrinter.println("RequestStack:");
    localPrefixPrinter.addToPrefix("  ");
    Iterator localIterator = this.mDeque.iterator();
    while (localIterator.hasNext()) {
      localPrefixPrinter.println(((Request)localIterator.next()).toString());
    }
  }
  
  public Request getCurrentRequest()
  {
    return (Request)this.mDeque.getFirst();
  }
  
  public boolean isEmpty()
  {
    return this.mDeque.isEmpty();
  }
  
  public Iterator<Request> iterator()
  {
    return this.mDeque.iterator();
  }
  
  public void popCurrentRequest()
  {
    this.mDeque.removeFirst();
  }
  
  public void pushNewCurrentRequest(Request paramRequest)
  {
    Preconditions.checkNotNull(paramRequest);
    this.mDeque.addFirst(paramRequest);
  }
  
  public String toString()
  {
    return Objects.toStringHelper(this).add("mDeque", this.mDeque).toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(Lists.newArrayList(this.mDeque));
  }
  
  private static class Creator
    implements Parcelable.Creator<RequestStack>
  {
    public RequestStack createFromParcel(Parcel paramParcel)
    {
      RequestStack localRequestStack = new RequestStack();
      ArrayList localArrayList = Lists.newArrayList();
      paramParcel.readTypedList(localArrayList, Request.CREATOR);
      localRequestStack.mDeque.addAll(localArrayList);
      return localRequestStack;
    }
    
    public RequestStack[] newArray(int paramInt)
    {
      return new RequestStack[paramInt];
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.inappwebpage.RequestStack
 * JD-Core Version:    0.7.0.1
 */