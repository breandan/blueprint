package com.google.android.search.core.discoursecontext;

import java.util.List;

public abstract interface MentionedEntity<T>
{
  public abstract T getEntity();
  
  public abstract List<Mention> getMentions();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.discoursecontext.MentionedEntity
 * JD-Core Version:    0.7.0.1
 */