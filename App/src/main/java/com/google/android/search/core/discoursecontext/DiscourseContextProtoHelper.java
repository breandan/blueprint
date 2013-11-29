package com.google.android.search.core.discoursecontext;

import android.content.res.Resources;
import com.google.android.e100.MessageNotificationReference;
import com.google.android.search.core.Feature;
import com.google.android.shared.util.Clock;
import com.google.android.speech.contacts.Person;
import com.google.android.velvet.actions.CardDecision;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.util.VoiceActionToActionV2Converter;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.knowledge.context.ClientDiscourseContextProto.ClientDiscourseContext;
import com.google.knowledge.context.ClientDiscourseContextProto.ClientEntity;
import com.google.knowledge.context.ClientDiscourseContextProto.ClientMention;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.ActionV2Metadata;
import com.google.majel.proto.ActionV2Protos.DeferredAction;
import com.google.majel.proto.ActionV2Protos.InteractionInfo;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public class DiscourseContextProtoHelper
{
  private final Function<VoiceAction, ActionV2Protos.ActionV2> mActionConverter;
  private final Clock mClock;
  private final Supplier<DiscourseContext> mDiscourseContextSupplier;
  private long mLastContextCaptureTimeMs = 0L;
  private final Resources mResources;
  
  public DiscourseContextProtoHelper(Supplier<DiscourseContext> paramSupplier, Clock paramClock, Resources paramResources)
  {
    this.mDiscourseContextSupplier = paramSupplier;
    this.mClock = paramClock;
    this.mResources = paramResources;
    this.mActionConverter = new VoiceActionToActionV2Converter();
  }
  
  @Nullable
  static ActionV2Protos.ActionV2 createActionV2(DiscourseContext paramDiscourseContext, Function<VoiceAction, ActionV2Protos.ActionV2> paramFunction)
  {
    ActionV2Protos.ActionV2 localActionV21 = paramDiscourseContext.getCurrentActionV2();
    VoiceAction localVoiceAction = paramDiscourseContext.getCurrentVoiceAction();
    if (localVoiceAction != null)
    {
      ActionV2Protos.ActionV2 localActionV22 = (ActionV2Protos.ActionV2)paramFunction.apply(localVoiceAction);
      ActionV2Protos.ActionV2Metadata localActionV2Metadata2;
      if ((localActionV22 != null) && (localActionV21 != null))
      {
        if (localActionV21.hasMetadata())
        {
          ActionV2Protos.ActionV2Metadata localActionV2Metadata1 = localActionV21.getMetadata();
          if (!localActionV22.hasMetadata()) {
            break label276;
          }
          localActionV2Metadata2 = localActionV22.getMetadata();
          if (localActionV2Metadata1.hasServerState()) {
            localActionV2Metadata2.setServerState(localActionV2Metadata1.getServerState());
          }
          if (localActionV2Metadata1.hasActionType()) {
            localActionV2Metadata2.setActionType(localActionV2Metadata1.getActionType());
          }
          if (localActionV2Metadata1.hasParsedActionType()) {
            localActionV2Metadata2.setParsedActionType(localActionV2Metadata1.getParsedActionType());
          }
        }
        if (localActionV21.hasDeferredActionExtension()) {
          localActionV22.setDeferredActionExtension(localActionV21.getDeferredActionExtension());
        }
        if (localActionV21.hasInteractionInfo()) {
          localActionV22.setInteractionInfo(localActionV21.getInteractionInfo());
        }
      }
      CardDecision localCardDecision = paramDiscourseContext.getCurrentCardDecision();
      ActionV2Protos.InteractionInfo localInteractionInfo;
      if ((localActionV22 != null) && (localCardDecision != null))
      {
        if (!localActionV22.hasInteractionInfo()) {
          break label296;
        }
        localInteractionInfo = localActionV22.getInteractionInfo();
        label185:
        int j = localCardDecision.getPromptedField();
        if (j == 0) {
          break label316;
        }
        localInteractionInfo.setPromptedField(j);
        label205:
        localInteractionInfo.setIncomplete(localCardDecision.shouldStartFollowOnVoiceSearch());
        if (!localCardDecision.shouldAutoExecute()) {
          break label325;
        }
        localActionV22.setExecute(true);
      }
      for (;;)
      {
        if ((localActionV22 != null) && (!localActionV22.hasDeferredActionExtension()))
        {
          int i = paramDiscourseContext.getNumberOfAttempts();
          if (i != 0) {
            localActionV22.setDeferredActionExtension(new ActionV2Protos.DeferredAction().setNumberOfAttempts(i));
          }
        }
        return localActionV22;
        label276:
        localActionV2Metadata2 = new ActionV2Protos.ActionV2Metadata();
        localActionV22.setMetadata(localActionV2Metadata2);
        break;
        label296:
        localInteractionInfo = new ActionV2Protos.InteractionInfo();
        localActionV22.setInteractionInfo(localInteractionInfo);
        break label185;
        label316:
        localInteractionInfo.clearPromptedField();
        break label205;
        label325:
        localActionV22.clearExecute();
      }
    }
    return null;
  }
  
  @Nullable
  private ClientDiscourseContextProto.ClientDiscourseContext createDiscourseContextMessage(DiscourseContext paramDiscourseContext, long paramLong1, long paramLong2, Function<VoiceAction, ActionV2Protos.ActionV2> paramFunction)
  {
    ClientDiscourseContextProto.ClientDiscourseContext localClientDiscourseContext = null;
    Iterator localIterator = paramDiscourseContext.getSnapshot().iterator();
    while (localIterator.hasNext())
    {
      ClientDiscourseContextProto.ClientEntity localClientEntity = createEntityMessage((MentionedEntity)localIterator.next(), paramLong1, paramLong2);
      if (localClientEntity != null)
      {
        if (localClientDiscourseContext == null) {
          localClientDiscourseContext = new ClientDiscourseContextProto.ClientDiscourseContext();
        }
        localClientDiscourseContext.addEntity(localClientEntity);
      }
    }
    ActionV2Protos.ActionV2 localActionV2 = createActionV2(paramDiscourseContext, paramFunction);
    if (localActionV2 != null)
    {
      if (localClientDiscourseContext == null) {
        localClientDiscourseContext = new ClientDiscourseContextProto.ClientDiscourseContext();
      }
      localClientDiscourseContext.addActions(localActionV2);
    }
    return localClientDiscourseContext;
  }
  
  static ClientDiscourseContextProto.ClientMention createMentionMessage(Mention paramMention)
  {
    return new ClientDiscourseContextProto.ClientMention().setUnixTimeMs(paramMention.getTimeMs());
  }
  
  @Nullable
  ClientDiscourseContextProto.ClientEntity createEntityMessage(MentionedEntity<?> paramMentionedEntity, long paramLong1, long paramLong2)
  {
    ClientDiscourseContextProto.ClientEntity localClientEntity = null;
    Iterator localIterator = paramMentionedEntity.getMentions().iterator();
    while (localIterator.hasNext())
    {
      Mention localMention = (Mention)localIterator.next();
      long l = localMention.getTimeMs();
      if ((l >= paramLong1) && ((l < paramLong2) || (paramLong2 == 0L)))
      {
        if (localClientEntity == null) {
          localClientEntity = createEntityMessageFromObject(paramMentionedEntity.getEntity());
        }
        localClientEntity.addMention(createMentionMessage(localMention));
      }
    }
    return localClientEntity;
  }
  
  ClientDiscourseContextProto.ClientEntity createEntityMessageFromObject(Object paramObject)
  {
    Preconditions.checkNotNull(paramObject);
    if ((paramObject instanceof Person))
    {
      Person localPerson = (Person)paramObject;
      ClientDiscourseContextProto.ClientEntity localClientEntity2 = new ClientDiscourseContextProto.ClientEntity().addType(1);
      if (Feature.DISCOURSE_CONTEXT_CONTACTS.isEnabled())
      {
        localClientEntity2.setClientEntityId(Long.toString(localPerson.getId()));
        String str = localPerson.getName();
        if (str != null) {
          localClientEntity2.setCanonicalText(str);
        }
        return localClientEntity2;
      }
      localClientEntity2.setCanonicalText(this.mResources.getString(2131363555));
      return localClientEntity2;
    }
    if ((paramObject instanceof MessageNotificationReference))
    {
      ClientDiscourseContextProto.ClientEntity localClientEntity1 = new ClientDiscourseContextProto.ClientEntity();
      localClientEntity1.addType(5);
      localClientEntity1.setCanonicalText(this.mResources.getString(2131363556));
      return localClientEntity1;
    }
    throw new IllegalArgumentException("Can't convert " + paramObject.getClass().getName());
  }
  
  public String getCurrentEventId()
  {
    return ((DiscourseContext)this.mDiscourseContextSupplier.get()).getCurrentEventId();
  }
  
  @Nullable
  public ClientDiscourseContextProto.ClientDiscourseContext takeRecentDiscourseContext()
  {
    long l1 = this.mLastContextCaptureTimeMs;
    long l2 = this.mClock.currentTimeMillis();
    ClientDiscourseContextProto.ClientDiscourseContext localClientDiscourseContext = createDiscourseContextMessage((DiscourseContext)this.mDiscourseContextSupplier.get(), l1, l2, this.mActionConverter);
    this.mLastContextCaptureTimeMs = l2;
    return localClientDiscourseContext;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.discoursecontext.DiscourseContextProtoHelper
 * JD-Core Version:    0.7.0.1
 */