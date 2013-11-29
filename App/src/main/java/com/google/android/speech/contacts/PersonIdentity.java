package com.google.android.speech.contacts;

import javax.annotation.Nullable;

public abstract interface PersonIdentity
{
  public abstract long getId();
  
  public abstract String getLookupKey();
  
  @Nullable
  public abstract String getName();
  
  public abstract boolean hasName();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.PersonIdentity
 * JD-Core Version:    0.7.0.1
 */