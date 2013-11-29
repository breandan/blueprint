package com.google.android.search.core.discoursecontext;

import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.CardDecision;
import com.google.android.velvet.actions.Disambiguation;
import com.google.android.voicesearch.fragments.action.CommunicationAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.DeferredAction;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.annotation.Nullable;

public class DiscourseContext
{
  @Nullable
  private CurrentVoiceAction mCurrentVoiceActionExperimental;
  private final Map<Object, MentionedEntityBuilder<?>> mEntities = Maps.newHashMap();
  private final Stack<VoiceAction> mVoiceActionRedoList = new Stack();
  private final Stack<VoiceAction> mVoiceActionUndoList = new Stack();
  
  private boolean noDisambiguationProgress(VoiceAction paramVoiceAction)
  {
    boolean bool2;
    if (this.mCurrentVoiceActionExperimental != null)
    {
      CardDecision localCardDecision = this.mCurrentVoiceActionExperimental.mCardDecision;
      bool2 = false;
      if (localCardDecision != null)
      {
        int i = this.mCurrentVoiceActionExperimental.mCardDecision.getPromptedField();
        bool2 = false;
        if (i == 2) {
          break label45;
        }
      }
    }
    label45:
    PersonDisambiguation localPersonDisambiguation;
    do
    {
      boolean bool1;
      do
      {
        return bool2;
        bool1 = paramVoiceAction instanceof CommunicationAction;
        bool2 = false;
      } while (!bool1);
      localPersonDisambiguation = ((CommunicationAction)paramVoiceAction).getRecipient();
      bool2 = false;
      if (localPersonDisambiguation != null)
      {
        boolean bool3 = localPersonDisambiguation.hasNoResults();
        bool2 = false;
        if (bool3) {
          bool2 = true;
        }
      }
    } while ((this.mCurrentVoiceActionExperimental == null) || (!(this.mCurrentVoiceActionExperimental.mVoiceAction instanceof CommunicationAction)));
    return bool2 | Disambiguation.haveSameState(((CommunicationAction)this.mCurrentVoiceActionExperimental.mVoiceAction).getRecipient(), localPersonDisambiguation);
  }
  
  public void clearCurrentActionAccept()
  {
    this.mCurrentVoiceActionExperimental = null;
    this.mVoiceActionRedoList.clear();
    this.mVoiceActionUndoList.clear();
  }
  
  public void clearCurrentActionCancel()
  {
    this.mCurrentVoiceActionExperimental = null;
    this.mVoiceActionRedoList.clear();
  }
  
  public ActionV2Protos.ActionV2 getCurrentActionV2()
  {
    if (this.mCurrentVoiceActionExperimental == null) {
      return null;
    }
    return this.mCurrentVoiceActionExperimental.mActionV2;
  }
  
  public CardDecision getCurrentCardDecision()
  {
    if (this.mCurrentVoiceActionExperimental == null) {
      return null;
    }
    return this.mCurrentVoiceActionExperimental.mCardDecision;
  }
  
  public CommunicationAction getCurrentCommunicationAction()
  {
    VoiceAction localVoiceAction = getCurrentVoiceAction();
    if ((localVoiceAction instanceof CommunicationAction)) {
      return (CommunicationAction)localVoiceAction;
    }
    return null;
  }
  
  public String getCurrentEventId()
  {
    if (this.mCurrentVoiceActionExperimental == null) {
      return null;
    }
    return this.mCurrentVoiceActionExperimental.mEventId;
  }
  
  public VoiceAction getCurrentVoiceAction()
  {
    if (this.mCurrentVoiceActionExperimental == null) {
      return null;
    }
    return this.mCurrentVoiceActionExperimental.mVoiceAction;
  }
  
  <T> MentionedEntityBuilder<T> getMentionedEntityBuilder(T paramT)
  {
    MentionedEntityBuilder localMentionedEntityBuilder1 = (MentionedEntityBuilder)this.mEntities.get(paramT);
    if (localMentionedEntityBuilder1 != null) {
      return localMentionedEntityBuilder1;
    }
    MentionedEntityBuilder localMentionedEntityBuilder2 = new MentionedEntityBuilder(paramT);
    this.mEntities.put(paramT, localMentionedEntityBuilder2);
    return localMentionedEntityBuilder2;
  }
  
  public int getNumberOfAttempts()
  {
    if (this.mCurrentVoiceActionExperimental == null) {
      return 0;
    }
    return this.mCurrentVoiceActionExperimental.mNumberOfAttempts;
  }
  
  public int getNumberOfAttempts(VoiceAction paramVoiceAction)
  {
    if ((this.mCurrentVoiceActionExperimental != null) && (paramVoiceAction == this.mCurrentVoiceActionExperimental.mVoiceAction)) {
      return this.mCurrentVoiceActionExperimental.mNumberOfAttempts;
    }
    if (noDisambiguationProgress(paramVoiceAction))
    {
      if (this.mCurrentVoiceActionExperimental == null) {
        return 1;
      }
      return 1 + this.mCurrentVoiceActionExperimental.mNumberOfAttempts;
    }
    return 0;
  }
  
  @Nullable
  public VoiceAction getRedoAction()
  {
    if (this.mVoiceActionRedoList.isEmpty()) {
      return null;
    }
    return (VoiceAction)this.mVoiceActionRedoList.peek();
  }
  
  public Set<MentionedEntity<?>> getSnapshot()
  {
    HashSet localHashSet;
    try
    {
      localHashSet = Sets.newHashSet();
      Iterator localIterator = this.mEntities.values().iterator();
      while (localIterator.hasNext()) {
        localHashSet.add(((MentionedEntityBuilder)localIterator.next()).build());
      }
    }
    finally {}
    return localHashSet;
  }
  
  @Nullable
  public VoiceAction getUndoAction()
  {
    if ((!this.mVoiceActionUndoList.isEmpty()) && (this.mCurrentVoiceActionExperimental != null)) {
      this.mVoiceActionRedoList.push(this.mVoiceActionUndoList.pop());
    }
    if (this.mVoiceActionUndoList.isEmpty()) {
      return null;
    }
    return (VoiceAction)this.mVoiceActionUndoList.peek();
  }
  
  public <T> void mention(T paramT, Mention paramMention)
  {
    try
    {
      getMentionedEntityBuilder(paramT).addMention(paramMention);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void mentionVoiceActionExperimental(ActionV2Protos.ActionV2 paramActionV2, VoiceAction paramVoiceAction, CardDecision paramCardDecision, String paramString)
  {
    int i;
    int j;
    if (this.mCurrentVoiceActionExperimental == null)
    {
      i = 1;
      if ((paramActionV2 == null) || (!paramActionV2.hasDeferredActionExtension())) {
        break label114;
      }
      j = paramActionV2.getDeferredActionExtension().getNumberOfAttempts();
      label30:
      this.mCurrentVoiceActionExperimental = new CurrentVoiceAction(paramActionV2, paramVoiceAction, paramCardDecision, j, paramString);
      if ((this.mVoiceActionUndoList.isEmpty()) || (this.mVoiceActionUndoList.peek() != paramVoiceAction))
      {
        if ((this.mVoiceActionRedoList.isEmpty()) || (this.mVoiceActionRedoList.peek() != paramVoiceAction)) {
          break label124;
        }
        this.mVoiceActionRedoList.pop();
      }
    }
    for (;;)
    {
      this.mVoiceActionUndoList.push(paramVoiceAction);
      return;
      i = 0;
      break;
      label114:
      j = getNumberOfAttempts(paramVoiceAction);
      break label30;
      label124:
      this.mVoiceActionRedoList.clear();
      if (i != 0) {
        this.mVoiceActionUndoList.clear();
      }
    }
  }
  
  private static class CurrentVoiceAction
  {
    final ActionV2Protos.ActionV2 mActionV2;
    final CardDecision mCardDecision;
    String mEventId;
    final int mNumberOfAttempts;
    final VoiceAction mVoiceAction;
    
    public CurrentVoiceAction(ActionV2Protos.ActionV2 paramActionV2, VoiceAction paramVoiceAction, CardDecision paramCardDecision, int paramInt, String paramString)
    {
      this.mActionV2 = paramActionV2;
      this.mVoiceAction = paramVoiceAction;
      this.mCardDecision = paramCardDecision;
      this.mNumberOfAttempts = paramInt;
      this.mEventId = paramString;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.discoursecontext.DiscourseContext
 * JD-Core Version:    0.7.0.1
 */