package com.google.android.search.core.discoursecontext;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MentionedEntityBuilder<T>
{
  private final T mEntity;
  private final List<Mention> mMentions;
  
  public MentionedEntityBuilder(T paramT)
  {
    this.mEntity = paramT;
    this.mMentions = Lists.newArrayList();
  }
  
  void addMention(Mention paramMention)
  {
    synchronized (this.mMentions)
    {
      this.mMentions.add(paramMention);
      return;
    }
  }
  
  public MentionedEntity<T> build()
  {
    synchronized (this.mMentions)
    {
      ArrayList localArrayList = Lists.newArrayList(this.mMentions);
      Collections.sort(localArrayList);
      new MentionedEntity()
      {
        public T getEntity()
        {
          return MentionedEntityBuilder.this.mEntity;
        }
        
        public List<Mention> getMentions()
        {
          return this.val$immutableMentions;
        }
      };
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.discoursecontext.MentionedEntityBuilder
 * JD-Core Version:    0.7.0.1
 */