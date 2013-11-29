package com.google.android.voicesearch.speechservice;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.e100.MessageBuffer;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.search.core.Feature;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.util.HelpActionUtils;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.ContactLookup.Mode;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.actions.AgendaTtsUtil;
import com.google.android.velvet.tg.FirstRunActivity;
import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.android.voicesearch.fragments.action.AddEventAction;
import com.google.android.voicesearch.fragments.action.AgendaAction;
import com.google.android.voicesearch.fragments.action.CancelAction;
import com.google.android.voicesearch.fragments.action.EmailAction;
import com.google.android.voicesearch.fragments.action.PhoneCallAction;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.android.voicesearch.fragments.action.PuntAction;
import com.google.android.voicesearch.fragments.action.ReadNotificationAction;
import com.google.android.voicesearch.fragments.action.SelfNoteAction;
import com.google.android.voicesearch.fragments.action.SetAlarmAction;
import com.google.android.voicesearch.fragments.action.SetReminderAction;
import com.google.android.voicesearch.fragments.action.ShowContactInformationAction;
import com.google.android.voicesearch.fragments.action.SmsAction;
import com.google.android.voicesearch.fragments.action.SocialUpdateAction;
import com.google.android.voicesearch.fragments.action.SocialUpdateAction.SocialNetwork;
import com.google.android.voicesearch.fragments.action.StopNavigationAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.util.CalendarHelper;
import com.google.android.voicesearch.util.CalendarHelper.Reminder.Builder;
import com.google.android.voicesearch.util.ExampleContactHelper;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.android.voicesearch.util.QueryCalendarUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.majel.proto.ActionDateTimeProtos.ActionTime;
import com.google.majel.proto.ActionV2Protos.ActionContactGroup;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.AddCalendarEventAction;
import com.google.majel.proto.ActionV2Protos.AddReminderAction;
import com.google.majel.proto.ActionV2Protos.AgendaAction;
import com.google.majel.proto.ActionV2Protos.EmailAction;
import com.google.majel.proto.ActionV2Protos.HelpAction;
import com.google.majel.proto.ActionV2Protos.InteractionInfo;
import com.google.majel.proto.ActionV2Protos.PhoneAction;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;
import com.google.majel.proto.ActionV2Protos.SMSAction;
import com.google.majel.proto.ActionV2Protos.SelfNoteAction;
import com.google.majel.proto.ActionV2Protos.SetAlarmAction;
import com.google.majel.proto.ActionV2Protos.ShowContactInformationAction;
import com.google.majel.proto.ActionV2Protos.UnsupportedAction;
import com.google.majel.proto.ActionV2Protos.UpdateSocialNetworkAction;
import com.google.majel.proto.CalendarProtos.CalendarDateTime;
import com.google.majel.proto.CalendarProtos.CalendarEvent;
import com.google.majel.proto.CalendarProtos.CalendarEvent.Attendee;
import com.google.majel.proto.CalendarProtos.CalendarEvent.Reminder;
import com.google.majel.proto.CalendarProtos.CalendarQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

public class ActionV2Processor
{
  private final int mAppVersion;
  private final ContactLookup mContactLookup;
  private final ContentResolver mContentResolver;
  private final Context mContext;
  private final DeviceCapabilityManager mDeviceCapabilityManager;
  private final Supplier<DiscourseContext> mDiscourseContextSupplier;
  private final ExampleContactHelper mExampleContactHelper;
  private final boolean mIsActionDiscoveryEnabled;
  private final Supplier<Future<Uri>> mLastAudioUriSupplier;
  private final LoginHelper mLoginHelper;
  private final MessageBuffer mMessageBuffer;
  
  public ActionV2Processor(Context paramContext, Supplier<Future<Uri>> paramSupplier, ContactLookup paramContactLookup, ContentResolver paramContentResolver, DeviceCapabilityManager paramDeviceCapabilityManager, boolean paramBoolean, Supplier<DiscourseContext> paramSupplier1, LoginHelper paramLoginHelper, MessageBuffer paramMessageBuffer, ExampleContactHelper paramExampleContactHelper, int paramInt)
  {
    this.mLastAudioUriSupplier = ((Supplier)Preconditions.checkNotNull(paramSupplier));
    this.mContactLookup = ((ContactLookup)Preconditions.checkNotNull(paramContactLookup));
    this.mContentResolver = ((ContentResolver)Preconditions.checkNotNull(paramContentResolver));
    this.mContext = paramContext;
    this.mDeviceCapabilityManager = ((DeviceCapabilityManager)Preconditions.checkNotNull(paramDeviceCapabilityManager));
    this.mIsActionDiscoveryEnabled = paramBoolean;
    this.mDiscourseContextSupplier = ((Supplier)Preconditions.checkNotNull(paramSupplier1));
    this.mLoginHelper = ((LoginHelper)Preconditions.checkNotNull(paramLoginHelper));
    this.mMessageBuffer = paramMessageBuffer;
    this.mExampleContactHelper = paramExampleContactHelper;
    this.mAppVersion = paramInt;
  }
  
  private static SocialUpdateAction.SocialNetwork getSocialNetworkFromActionV2Proto(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 1: 
      return SocialUpdateAction.SocialNetwork.GOOGLE_PLUS;
    }
    return SocialUpdateAction.SocialNetwork.TWITTER;
  }
  
  private boolean processActionV2Async(final ActionV2Protos.ActionV2 paramActionV2, final SimpleCallback<List<VoiceAction>> paramSimpleCallback)
  {
    if ((paramActionV2.hasPhoneActionExtension()) || (paramActionV2.hasSMSActionExtension()) || (paramActionV2.hasEmailActionExtension()) || (paramActionV2.hasShowContactInformationActionExtension()) || (paramActionV2.hasAgendaActionExtension()))
    {
      new AsyncTask()
      {
        protected VoiceAction doInBackground(Void... paramAnonymousVarArgs)
        {
          if (paramActionV2.hasPhoneActionExtension()) {
            return ActionV2Processor.this.processPhoneAction(paramActionV2);
          }
          if (paramActionV2.hasSMSActionExtension()) {
            return ActionV2Processor.this.processSmsAction(paramActionV2.getSMSActionExtension());
          }
          if (paramActionV2.hasEmailActionExtension()) {
            return ActionV2Processor.this.processEmailAction(paramActionV2.getEmailActionExtension());
          }
          if (paramActionV2.hasAgendaActionExtension()) {
            return ActionV2Processor.this.processAgendaAction(paramActionV2.getAgendaActionExtension(), paramActionV2.getEligibleForNoResultsUi());
          }
          if (paramActionV2.hasShowContactInformationActionExtension()) {
            return ActionV2Processor.this.processShowContactInformation(paramActionV2.getShowContactInformationActionExtension());
          }
          throw new AssertionError();
        }
        
        protected void onPostExecute(VoiceAction paramAnonymousVoiceAction)
        {
          if (paramAnonymousVoiceAction != null) {}
          for (ArrayList localArrayList = Lists.newArrayList(new VoiceAction[] { paramAnonymousVoiceAction });; localArrayList = Lists.newArrayList())
          {
            paramSimpleCallback.onResult(localArrayList);
            return;
          }
        }
      }.execute(new Void[0]);
      return true;
    }
    if (paramActionV2.hasHelpActionExtension())
    {
      new AsyncTask()
      {
        protected List<VoiceAction> doInBackground(Void... paramAnonymousVarArgs)
        {
          if (paramActionV2.hasHelpActionExtension()) {
            return ActionV2Processor.this.processHelpAction(paramActionV2.getHelpActionExtension());
          }
          throw new AssertionError();
        }
        
        protected void onPostExecute(List<VoiceAction> paramAnonymousList)
        {
          paramSimpleCallback.onResult(Preconditions.checkNotNull(paramAnonymousList));
        }
      }.execute(new Void[0]);
      return true;
    }
    return false;
  }
  
  private boolean processActionV2Sync(ActionV2Protos.ActionV2 paramActionV2, boolean paramBoolean, SimpleCallback<List<VoiceAction>> paramSimpleCallback)
  {
    Object localObject;
    if ((paramActionV2.hasInteractionInfo()) && (paramActionV2.getInteractionInfo().getCancel())) {
      if (Feature.DISCOURSE_CONTEXT.isEnabled())
      {
        DiscourseContext localDiscourseContext = (DiscourseContext)this.mDiscourseContextSupplier.get();
        VoiceAction localVoiceAction = localDiscourseContext.getCurrentVoiceAction();
        if (localVoiceAction != null)
        {
          localObject = CancelAction.fromVoiceAction(localVoiceAction);
          localDiscourseContext.clearCurrentActionCancel();
        }
      }
    }
    while (localObject == null)
    {
      do
      {
        return false;
        localObject = null;
        break;
        localObject = null;
        break;
        if (paramActionV2.hasSetAlarmActionExtension())
        {
          localObject = processSetAlarmAction(paramActionV2.getSetAlarmActionExtension());
          break;
        }
        if (paramActionV2.hasPlayMediaActionExtension())
        {
          localObject = processPlayMediaAction(paramActionV2.getPlayMediaActionExtension());
          break;
        }
        if (paramActionV2.hasSelfNoteActionExtension())
        {
          localObject = processSelfNoteAction(paramActionV2.getSelfNoteActionExtension(), paramBoolean);
          break;
        }
        if (paramActionV2.hasAddCalendarEventActionExtension())
        {
          localObject = processAddCalendarEventAction(paramActionV2.getAddCalendarEventActionExtension());
          break;
        }
        if (paramActionV2.hasUpdateSocialNetworkActionExtension())
        {
          localObject = processUpdateSocialNetwork(paramActionV2.getUpdateSocialNetworkActionExtension());
          break;
        }
        if (paramActionV2.hasAddReminderActionExtension())
        {
          localObject = processSetReminderAction(paramActionV2.getAddReminderActionExtension());
          break;
        }
        if (paramActionV2.hasUnsupportedActionExtension())
        {
          localObject = processUnsupportedAction(paramActionV2.getUnsupportedActionExtension());
          break;
        }
        if (paramActionV2.hasStopNavigationActionExtension())
        {
          localObject = processStopNavigationAction();
          break;
        }
      } while (!paramActionV2.hasReadNotificationActionExtension());
      localObject = processReadNotificationAction();
    }
    paramSimpleCallback.onResult(Lists.newArrayList(new VoiceAction[] { localObject }));
    return true;
  }
  
  private AddEventAction processAddCalendarEventAction(ActionV2Protos.AddCalendarEventAction paramAddCalendarEventAction)
  {
    if (paramAddCalendarEventAction.hasCalendarEvent())
    {
      CalendarProtos.CalendarEvent localCalendarEvent = paramAddCalendarEventAction.getCalendarEvent();
      boolean bool = localCalendarEvent.hasStartTime();
      long l1;
      if (bool)
      {
        l1 = localCalendarEvent.getStartTime().getTimeMs();
        if (!localCalendarEvent.hasEndTime()) {
          break label119;
        }
      }
      ArrayList localArrayList1;
      label119:
      for (long l2 = localCalendarEvent.getEndTime().getTimeMs();; l2 = CalendarHelper.getDefaultEndTimeMs(l1))
      {
        localArrayList1 = Lists.newArrayList();
        Iterator localIterator1 = localCalendarEvent.getAttendeeList().iterator();
        while (localIterator1.hasNext())
        {
          CalendarProtos.CalendarEvent.Attendee localAttendee = (CalendarProtos.CalendarEvent.Attendee)localIterator1.next();
          if (localAttendee.hasDisplayName()) {
            localArrayList1.add(localAttendee.getDisplayName());
          }
        }
        l1 = CalendarHelper.getDefaultStartTimeMs(System.currentTimeMillis());
        break;
      }
      ArrayList localArrayList2 = Lists.newArrayList();
      if (localCalendarEvent.getReminderCount() == 0) {
        localArrayList2.add(CalendarHelper.createDefaultReminder());
      }
      for (;;)
      {
        return new AddEventAction(localCalendarEvent.getSummary(), localCalendarEvent.getLocation(), localArrayList1, localArrayList2, l1, bool, l2);
        Iterator localIterator2 = localCalendarEvent.getReminderList().iterator();
        while (localIterator2.hasNext())
        {
          CalendarProtos.CalendarEvent.Reminder localReminder = (CalendarProtos.CalendarEvent.Reminder)localIterator2.next();
          CalendarHelper.Reminder.Builder localBuilder = new CalendarHelper.Reminder.Builder();
          if (localReminder.hasMinutes()) {
            localBuilder.setMinutesInAdvance(localReminder.getMinutes());
          }
          if (localReminder.hasMethod()) {
            localBuilder.setMethod(localReminder.getMethod());
          }
          localArrayList2.add(localBuilder.build());
        }
      }
    }
    return null;
  }
  
  private AgendaAction processAgendaAction(ActionV2Protos.AgendaAction paramAgendaAction, boolean paramBoolean)
  {
    CalendarProtos.CalendarQuery localCalendarQuery = paramAgendaAction.getQuery();
    List localList1 = QueryCalendarUtil.queryCalendar(localCalendarQuery.getContent(), localCalendarQuery.getTitleOnly(), localCalendarQuery.getEarliestStartTimeMs(), localCalendarQuery.getLatestStartTimeMs(), paramAgendaAction.getSortReverseChronological(), this.mContentResolver, localCalendarQuery.getMaxResults(), this.mContext.getResources());
    AgendaTtsUtil.populateTtsValues(localList1, paramAgendaAction.getQuery().getSearchType(), this.mContext, VelvetServices.get().getCoreServices().getClock());
    List localList2 = QueryCalendarUtil.mergeResults(localList1, paramAgendaAction.getAgendaList());
    if ((localList2.isEmpty()) && (!paramBoolean)) {
      return null;
    }
    return new AgendaAction(localList2, paramAgendaAction.getAutoExpandFirstResult(), paramAgendaAction.getItemBatchSize(), paramAgendaAction.getQuery().getEarliestStartTimeMs(), paramAgendaAction.getQuery().getLatestStartTimeMs(), paramAgendaAction.getSortReverseChronological());
  }
  
  private EmailAction processEmailAction(ActionV2Protos.EmailAction paramEmailAction)
  {
    PersonDisambiguation localPersonDisambiguation;
    if (Feature.CONTACT_REFERENCE.isEnabled()) {
      if (paramEmailAction.getToCrCount() > 0) {
        localPersonDisambiguation = PhoneActionUtils.createPersonDisambiguationContactReference(this.mContactLookup, ContactLookup.Mode.EMAIL, paramEmailAction.getToCr(0), this.mDiscourseContextSupplier);
      }
    }
    for (;;)
    {
      return new EmailAction(localPersonDisambiguation, paramEmailAction.getSubject(), paramEmailAction.getBody());
      localPersonDisambiguation = null;
      continue;
      int i = paramEmailAction.getToCount();
      localPersonDisambiguation = null;
      if (i > 0) {
        localPersonDisambiguation = PhoneActionUtils.createPersonDisambiguation(this.mContactLookup, ContactSelectMode.EMAIL, false, null, paramEmailAction.getTo(0).getContactList());
      }
    }
  }
  
  private List<VoiceAction> processHelpAction(ActionV2Protos.HelpAction paramHelpAction)
  {
    ArrayList localArrayList = Lists.newArrayList();
    if (this.mIsActionDiscoveryEnabled) {
      HelpActionUtils.process(paramHelpAction, this.mExampleContactHelper, this.mDeviceCapabilityManager, this.mAppVersion, localArrayList);
    }
    return localArrayList;
  }
  
  private VoiceAction processPhoneAction(ActionV2Protos.ActionV2 paramActionV2)
  {
    if (this.mDeviceCapabilityManager.isTelephoneCapable())
    {
      ActionV2Protos.PhoneAction localPhoneAction = paramActionV2.getPhoneActionExtension();
      if ((!Feature.CONTACT_REFERENCE.isEnabled()) && (localPhoneAction.getContactCount() == 0))
      {
        Log.e("ActionV2Processor", "Phone action without any contacts.");
        return null;
      }
      PersonDisambiguation localPersonDisambiguation;
      if (Feature.CONTACT_REFERENCE.isEnabled()) {
        if (localPhoneAction.hasContactCr()) {
          localPersonDisambiguation = PhoneActionUtils.createPersonDisambiguationContactReference(this.mContactLookup, ContactLookup.Mode.PHONE_NUMBER, localPhoneAction.getContactCr(), this.mDiscourseContextSupplier);
        }
      }
      for (;;)
      {
        return new PhoneCallAction(localPersonDisambiguation);
        localPersonDisambiguation = null;
        continue;
        localPersonDisambiguation = PhoneActionUtils.createPhoneCallPersonDisambiguationActionV2(paramActionV2, this.mContactLookup);
      }
    }
    return processUnsupportedAction();
  }
  
  private PlayMediaAction processPlayMediaAction(ActionV2Protos.PlayMediaAction paramPlayMediaAction)
  {
    PlayMediaAction localPlayMediaAction = new PlayMediaAction(paramPlayMediaAction);
    if ((localPlayMediaAction.isOpenAppAction()) || (localPlayMediaAction.isOpenBookAction()) || (localPlayMediaAction.isPlayMovieAction()) || (localPlayMediaAction.isPlayMusicAction())) {
      return localPlayMediaAction;
    }
    return null;
  }
  
  private ReadNotificationAction processReadNotificationAction()
  {
    return new ReadNotificationAction(this.mMessageBuffer.takeNextMessage());
  }
  
  private SelfNoteAction processSelfNoteAction(ActionV2Protos.SelfNoteAction paramSelfNoteAction, boolean paramBoolean)
  {
    String str = paramSelfNoteAction.getBody();
    if (paramBoolean) {}
    for (Future localFuture = (Future)this.mLastAudioUriSupplier.get();; localFuture = null) {
      return new SelfNoteAction(str, localFuture);
    }
  }
  
  private SetAlarmAction processSetAlarmAction(ActionV2Protos.SetAlarmAction paramSetAlarmAction)
  {
    String str = paramSetAlarmAction.getAlarmLabel();
    if ((paramSetAlarmAction.hasTime()) && (paramSetAlarmAction.getTime().hasHour())) {
      return new SetAlarmAction(str, paramSetAlarmAction.getTime().getHour(), paramSetAlarmAction.getTime().getMinute());
    }
    return new SetAlarmAction(str);
  }
  
  private VoiceAction processSetReminderAction(ActionV2Protos.AddReminderAction paramAddReminderAction)
  {
    if (this.mLoginHelper.isUserOptedIntoGoogleNow()) {
      return SetReminderAction.setUpFromAction(paramAddReminderAction);
    }
    return new PuntAction(2131363496, 2131363497, 0, new Intent(this.mContext, FirstRunActivity.class));
  }
  
  private ShowContactInformationAction processShowContactInformation(ActionV2Protos.ShowContactInformationAction paramShowContactInformationAction)
  {
    int i;
    ContactSelectMode localContactSelectMode;
    List localList;
    String str;
    if (paramShowContactInformationAction.getShowPhone())
    {
      i = 1;
      localContactSelectMode = ContactSelectMode.CALL_CONTACT;
      localList = paramShowContactInformationAction.getContactList();
      str = PhoneActionUtils.getAndClearContactMethodType(localList);
      if ((!Feature.CONTACT_REFERENCE.isEnabled()) || (!paramShowContactInformationAction.hasContactCr())) {
        break label118;
      }
    }
    label118:
    for (PersonDisambiguation localPersonDisambiguation = PhoneActionUtils.createPersonDisambiguationContactReference(this.mContactLookup, ContactLookup.Mode.PERSON, paramShowContactInformationAction.getContactCr(), this.mDiscourseContextSupplier);; localPersonDisambiguation = PhoneActionUtils.createPersonDisambiguation(this.mContactLookup, localContactSelectMode, true, null, localList))
    {
      return new ShowContactInformationAction(localPersonDisambiguation, i, str, false, null, null, null);
      if (paramShowContactInformationAction.getShowEmail())
      {
        i = 2;
        localContactSelectMode = ContactSelectMode.EMAIL;
        break;
      }
      if (paramShowContactInformationAction.getShowPostalAddress())
      {
        i = 3;
        localContactSelectMode = null;
        break;
      }
      localContactSelectMode = ContactSelectMode.SHOW_CONTACT_INFO;
      i = 0;
      break;
    }
  }
  
  private VoiceAction processSmsAction(ActionV2Protos.SMSAction paramSMSAction)
  {
    if (this.mDeviceCapabilityManager.isTelephoneCapable())
    {
      PersonDisambiguation localPersonDisambiguation;
      if (Feature.CONTACT_REFERENCE.isEnabled())
      {
        int j = paramSMSAction.getRecipientCrCount();
        localPersonDisambiguation = null;
        if (j > 0) {
          localPersonDisambiguation = PhoneActionUtils.createPersonDisambiguationContactReference(this.mContactLookup, ContactLookup.Mode.PHONE_NUMBER, paramSMSAction.getRecipientCr(0), this.mDiscourseContextSupplier);
        }
      }
      for (;;)
      {
        return new SmsAction(localPersonDisambiguation, paramSMSAction.getMessageBody());
        int i = paramSMSAction.getRecipientCount();
        localPersonDisambiguation = null;
        if (i > 0) {
          localPersonDisambiguation = PhoneActionUtils.createPhoneCallPersonDisambiguation(this.mContactLookup, ContactSelectMode.SMS, PhoneActionUtils.MESSAGE_DEFAULT_CONTACT_TYPE, paramSMSAction.getRecipient(0).getContactList());
        }
      }
    }
    return processUnsupportedAction();
  }
  
  private StopNavigationAction processStopNavigationAction()
  {
    return new StopNavigationAction();
  }
  
  private PuntAction processUnsupportedAction()
  {
    return new PuntAction(2131363293);
  }
  
  private PuntAction processUnsupportedAction(ActionV2Protos.UnsupportedAction paramUnsupportedAction)
  {
    if (!paramUnsupportedAction.hasExplanation())
    {
      Log.e("ActionV2Processor", "Unsupported action without explanation");
      return null;
    }
    return new PuntAction(paramUnsupportedAction.getExplanation());
  }
  
  private SocialUpdateAction processUpdateSocialNetwork(ActionV2Protos.UpdateSocialNetworkAction paramUpdateSocialNetworkAction)
  {
    if (paramUpdateSocialNetworkAction.hasSocialNetwork()) {}
    for (SocialUpdateAction.SocialNetwork localSocialNetwork = getSocialNetworkFromActionV2Proto(paramUpdateSocialNetworkAction.getSocialNetwork()); localSocialNetwork != null; localSocialNetwork = SocialUpdateAction.SocialNetwork.GOOGLE_PLUS) {
      return new SocialUpdateAction(paramUpdateSocialNetworkAction.getStatusMessage(), localSocialNetwork);
    }
    return null;
  }
  
  public boolean processActionV2(ActionV2Protos.ActionV2 paramActionV2, boolean paramBoolean, SimpleCallback<List<VoiceAction>> paramSimpleCallback)
  {
    return (processActionV2Sync(paramActionV2, paramBoolean, paramSimpleCallback)) || (processActionV2Async(paramActionV2, paramSimpleCallback));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.speechservice.ActionV2Processor
 * JD-Core Version:    0.7.0.1
 */