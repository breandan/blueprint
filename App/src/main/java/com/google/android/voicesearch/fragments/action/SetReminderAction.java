package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import com.android.recurrencepicker.EventRecurrence;
import com.android.recurrencepicker.EventRecurrence.InvalidFormatException;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.fragments.reminders.DateTimePickerHelper;
import com.google.android.voicesearch.fragments.reminders.RecurrenceHelper;
import com.google.android.voicesearch.fragments.reminders.SymbolicTime;
import com.google.caribou.tasks.RecurrenceProtos.RecurrenceId;
import com.google.common.base.Preconditions;
import com.google.majel.proto.ActionV2Protos.AbsoluteTimeTrigger;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.AddReminderAction;
import com.google.majel.proto.ActionV2Protos.LocalResultCandidateList;
import com.google.majel.proto.ActionV2Protos.LocationTrigger;
import com.google.majel.proto.AliasProto.Alias;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class SetReminderAction
  implements VoiceAction
{
  public static final Parcelable.Creator<SetReminderAction> CREATOR = new Parcelable.Creator()
  {
    public SetReminderAction createFromParcel(Parcel paramAnonymousParcel)
    {
      SetReminderAction localSetReminderAction = new SetReminderAction();
      localSetReminderAction.setOriginalTaskId(paramAnonymousParcel.readString());
      localSetReminderAction.setDateTimeMs(paramAnonymousParcel.readLong());
      int i = paramAnonymousParcel.readInt();
      if (i != -1) {
        localSetReminderAction.setSymbolicTime(SymbolicTime.fromActionV2Symbol(i));
      }
      localSetReminderAction.setLabel(paramAnonymousParcel.readString());
      localSetReminderAction.setEmbeddedAction((ActionV2Protos.ActionV2)ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, ActionV2Protos.ActionV2.class));
      localSetReminderAction.setConfirmationUrlPath(paramAnonymousParcel.readString());
      localSetReminderAction.setLocationTriggerType(paramAnonymousParcel.readInt());
      localSetReminderAction.setTriggerType(paramAnonymousParcel.readInt());
      localSetReminderAction.setLocation((EcoutezStructuredResponse.EcoutezLocalResult)ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, EcoutezStructuredResponse.EcoutezLocalResult.class));
      localSetReminderAction.setHomeLocation((EcoutezStructuredResponse.EcoutezLocalResult)ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, EcoutezStructuredResponse.EcoutezLocalResult.class));
      localSetReminderAction.setWorkLocation((EcoutezStructuredResponse.EcoutezLocalResult)ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, EcoutezStructuredResponse.EcoutezLocalResult.class));
      String str1 = paramAnonymousParcel.readString();
      if (!TextUtils.isEmpty(str1)) {}
      try
      {
        EventRecurrence localEventRecurrence = new EventRecurrence();
        localEventRecurrence.parse(str1);
        localSetReminderAction.setRecurrence(localEventRecurrence);
        String str2 = paramAnonymousParcel.readString();
        if (!TextUtils.isEmpty(str2)) {
          localSetReminderAction.setRecurrenceId(str2);
        }
        return localSetReminderAction;
      }
      catch (EventRecurrence.InvalidFormatException localInvalidFormatException)
      {
        for (;;)
        {
          Log.e("SetReminderAction", "Failed to parse recurrence", localInvalidFormatException);
        }
      }
    }
    
    public SetReminderAction[] newArray(int paramAnonymousInt)
    {
      return new SetReminderAction[paramAnonymousInt];
    }
  };
  private String mConfirmationUrlPath;
  private long mDateTimeMs;
  private ActionV2Protos.ActionV2 mEmbeddedAction;
  private EcoutezStructuredResponse.EcoutezLocalResult mHomeLocation = createUnsetAliasLocation(0);
  private String mLabel;
  private EcoutezStructuredResponse.EcoutezLocalResult mLocation;
  private int mLocationTriggerType;
  private String mOriginalTaskId;
  @Nullable
  private EventRecurrence mRecurrence;
  @Nullable
  private String mRecurrenceId;
  private long mSetUpTimeMs = System.currentTimeMillis();
  @Nullable
  private SymbolicTime mSymbolicTime;
  private int mTriggerType;
  private EcoutezStructuredResponse.EcoutezLocalResult mWorkLocation = createUnsetAliasLocation(1);
  
  public SetReminderAction()
  {
    setLabel(null);
    setTriggerType(1);
    setDefaultDateTime();
    setDefaultLocationTrigger();
  }
  
  public static EcoutezStructuredResponse.EcoutezLocalResult createUnsetAliasLocation(int paramInt)
  {
    return new EcoutezStructuredResponse.EcoutezLocalResult().setAlias(new AliasProto.Alias().setAliasType(paramInt));
  }
  
  private static ActionV2Protos.AbsoluteTimeTrigger getAbsoluteTimeTrigger(long paramLong, ActionV2Protos.AddReminderAction paramAddReminderAction)
  {
    if (paramAddReminderAction.hasAbsoluteTimeTrigger()) {
      return paramAddReminderAction.getAbsoluteTimeTrigger();
    }
    if (!paramAddReminderAction.hasRecurrence()) {
      return null;
    }
    return RecurrenceHelper.getAbsoluteTimeFromRecurrence(paramLong, paramAddReminderAction.getRecurrence());
  }
  
  private static int getTriggerType(ActionV2Protos.AddReminderAction paramAddReminderAction, GsaConfigFlags paramGsaConfigFlags)
  {
    int i;
    if ((paramAddReminderAction.hasAbsoluteTimeTrigger()) || ((paramGsaConfigFlags.getAddReminderRecurrenceEnabledVersion()) && (paramAddReminderAction.hasRecurrence()))) {
      i = 1;
    }
    int k;
    do
    {
      ActionV2Protos.LocationTrigger localLocationTrigger;
      int j;
      do
      {
        boolean bool;
        do
        {
          return i;
          bool = paramAddReminderAction.hasLocationTrigger();
          i = 0;
        } while (!bool);
        localLocationTrigger = paramAddReminderAction.getLocationTrigger();
        j = localLocationTrigger.getLocalResultCandidateListCount();
        i = 0;
      } while (j <= 0);
      k = localLocationTrigger.getLocalResultCandidateList(0).getCandidateLocalResultCount();
      i = 0;
    } while (k <= 0);
    return 2;
  }
  
  private void setDateTimeMs(long paramLong, @Nullable SymbolicTime paramSymbolicTime)
  {
    this.mDateTimeMs = paramLong;
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    setSymbolicTime(paramSymbolicTime);
    if (paramSymbolicTime == null) {
      setTime(localCalendar.get(11), localCalendar.get(12));
    }
    setDate(localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
  }
  
  private void setHomeOrWork(List<EcoutezStructuredResponse.EcoutezLocalResult> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult = (EcoutezStructuredResponse.EcoutezLocalResult)localIterator.next();
      if (localEcoutezLocalResult.hasAlias()) {
        switch (localEcoutezLocalResult.getAlias().getAliasType())
        {
        default: 
          break;
        case 0: 
          setHomeLocation(localEcoutezLocalResult);
          break;
        case 1: 
          setWorkLocation(localEcoutezLocalResult);
        }
      }
    }
  }
  
  public static SetReminderAction setUpFromAction(ActionV2Protos.AddReminderAction paramAddReminderAction)
  {
    return setUpFromAction(paramAddReminderAction, VelvetServices.get().getGsaConfigFlags());
  }
  
  public static SetReminderAction setUpFromAction(ActionV2Protos.AddReminderAction paramAddReminderAction, GsaConfigFlags paramGsaConfigFlags)
  {
    Preconditions.checkNotNull(paramAddReminderAction);
    SetReminderAction localSetReminderAction = new SetReminderAction();
    localSetReminderAction.setOriginalTaskId(paramAddReminderAction.getTaskId());
    localSetReminderAction.setLabel(paramAddReminderAction.getLabel());
    localSetReminderAction.setConfirmationUrlPath(paramAddReminderAction.getConfirmationUrlPath());
    if (paramAddReminderAction.getEmbeddedAction() != null) {
      localSetReminderAction.setEmbeddedAction(paramAddReminderAction.getEmbeddedAction());
    }
    if (paramAddReminderAction.hasLocationTrigger()) {
      localSetReminderAction.setHomeWork(paramAddReminderAction.getLocationTrigger());
    }
    int i = getTriggerType(paramAddReminderAction, paramGsaConfigFlags);
    switch (i)
    {
    default: 
    case 0: 
      for (;;)
      {
        localSetReminderAction.setTriggerType(i);
        return localSetReminderAction;
        localSetReminderAction.setDefaultDateTime();
        localSetReminderAction.setDefaultLocationTrigger();
        i = 1;
      }
    case 1: 
      boolean bool = paramGsaConfigFlags.getAddReminderRecurrenceEnabledVersion();
      if (bool)
      {
        localSetReminderAction.setRecurrence(RecurrenceHelper.convertCaribouRecurrenceToEventRecurrence(paramAddReminderAction.getRecurrence()));
        if ((paramAddReminderAction.hasRecurrenceId()) && (!TextUtils.isEmpty(paramAddReminderAction.getRecurrenceId().getId()))) {
          localSetReminderAction.setRecurrenceId(paramAddReminderAction.getRecurrenceId().getId());
        }
      }
      ActionV2Protos.AbsoluteTimeTrigger localAbsoluteTimeTrigger;
      label194:
      long l;
      if (bool)
      {
        localAbsoluteTimeTrigger = getAbsoluteTimeTrigger(localSetReminderAction.getSetUpTimeMs(), paramAddReminderAction);
        Preconditions.checkNotNull(localAbsoluteTimeTrigger, "The timeTrigger can't be NULL.");
        l = localAbsoluteTimeTrigger.getTimeMs();
        if (!localAbsoluteTimeTrigger.hasSymbolicTime()) {
          break label252;
        }
      }
      label252:
      for (SymbolicTime localSymbolicTime = SymbolicTime.fromActionV2Symbol(localAbsoluteTimeTrigger.getSymbolicTime());; localSymbolicTime = null)
      {
        localSetReminderAction.setDateTimeMs(l, localSymbolicTime);
        localSetReminderAction.setDefaultLocationTrigger();
        break;
        localAbsoluteTimeTrigger = paramAddReminderAction.getAbsoluteTimeTrigger();
        break label194;
      }
    }
    ActionV2Protos.LocationTrigger localLocationTrigger = paramAddReminderAction.getLocationTrigger();
    if (Feature.REMINDERS_LEAVING_TRIGGER.isEnabled()) {
      localSetReminderAction.setLocationTriggerType(localLocationTrigger.getType());
    }
    for (;;)
    {
      localSetReminderAction.setLocation(localLocationTrigger.getLocalResultCandidateList(0).getCandidateLocalResult(0));
      localSetReminderAction.setDefaultDateTime();
      break;
      localSetReminderAction.setLocationTriggerType(0);
    }
  }
  
  public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor)
  {
    return paramVoiceActionVisitor.visit(this);
  }
  
  public boolean canExecute()
  {
    return true;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getConfirmationUrlPath()
  {
    return this.mConfirmationUrlPath;
  }
  
  public long getDateTimeMs()
  {
    return this.mDateTimeMs;
  }
  
  public ActionV2Protos.ActionV2 getEmbeddedAction()
  {
    return this.mEmbeddedAction;
  }
  
  public EcoutezStructuredResponse.EcoutezLocalResult getHomeLocation()
  {
    return this.mHomeLocation;
  }
  
  public String getLabel()
  {
    return this.mLabel;
  }
  
  public EcoutezStructuredResponse.EcoutezLocalResult getLocation()
  {
    return this.mLocation;
  }
  
  public int getLocationTriggerType()
  {
    return this.mLocationTriggerType;
  }
  
  public String getOriginalTaskId()
  {
    return this.mOriginalTaskId;
  }
  
  public EventRecurrence getRecurrence()
  {
    return this.mRecurrence;
  }
  
  public String getRecurrenceId()
  {
    return this.mRecurrenceId;
  }
  
  public long getSetUpTimeMs()
  {
    return this.mSetUpTimeMs;
  }
  
  public SymbolicTime getSymbolicTime()
  {
    return this.mSymbolicTime;
  }
  
  public int getTriggerType()
  {
    return this.mTriggerType;
  }
  
  public EcoutezStructuredResponse.EcoutezLocalResult getWorkLocation()
  {
    return this.mWorkLocation;
  }
  
  public boolean isSupportedRepeatedReminder()
  {
    return (this.mRecurrence != null) && ((this.mRecurrence.freq == 4) || (this.mRecurrence.freq == 5) || (this.mRecurrence.freq == 6) || (this.mRecurrence.freq == 7));
  }
  
  public void setConfirmationUrlPath(String paramString)
  {
    this.mConfirmationUrlPath = paramString;
  }
  
  public void setDate(int paramInt1, int paramInt2, int paramInt3)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(this.mDateTimeMs);
    localCalendar.set(1, paramInt1);
    localCalendar.set(2, paramInt2);
    localCalendar.set(5, paramInt3);
    this.mDateTimeMs = localCalendar.getTimeInMillis();
  }
  
  public void setDateTimeMs(long paramLong)
  {
    this.mDateTimeMs = paramLong;
  }
  
  public void setDefaultDateTime()
  {
    int i = 4 + Calendar.getInstance().get(11);
    int j = 1;
    if (i < SymbolicTime.MORNING.defaultHour) {
      this.mSymbolicTime = SymbolicTime.MORNING;
    }
    for (;;)
    {
      Calendar localCalendar = Calendar.getInstance();
      if (j == 0) {
        localCalendar.add(6, 1);
      }
      this.mDateTimeMs = localCalendar.getTimeInMillis();
      return;
      if (i < SymbolicTime.AFTERNOON.defaultHour)
      {
        this.mSymbolicTime = SymbolicTime.AFTERNOON;
      }
      else if (i < SymbolicTime.EVENING.defaultHour)
      {
        this.mSymbolicTime = SymbolicTime.EVENING;
      }
      else if (i < SymbolicTime.NIGHT.defaultHour)
      {
        this.mSymbolicTime = SymbolicTime.NIGHT;
      }
      else
      {
        this.mSymbolicTime = SymbolicTime.MORNING;
        j = 0;
      }
    }
  }
  
  public void setDefaultLocationTrigger()
  {
    setLocationTriggerType(0);
    setLocation(getHomeLocation());
  }
  
  public void setEmbeddedAction(ActionV2Protos.ActionV2 paramActionV2)
  {
    this.mEmbeddedAction = paramActionV2;
  }
  
  public void setHomeLocation(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
  {
    this.mHomeLocation = paramEcoutezLocalResult;
  }
  
  public void setHomeWork(ActionV2Protos.LocationTrigger paramLocationTrigger)
  {
    setHomeLocation(null);
    setWorkLocation(null);
    if (paramLocationTrigger.hasDefaultLocations()) {
      setHomeOrWork(paramLocationTrigger.getDefaultLocations().getCandidateLocalResultList());
    }
    Iterator localIterator = paramLocationTrigger.getLocalResultCandidateListList().iterator();
    for (;;)
    {
      ActionV2Protos.LocalResultCandidateList localLocalResultCandidateList;
      if (localIterator.hasNext())
      {
        localLocalResultCandidateList = (ActionV2Protos.LocalResultCandidateList)localIterator.next();
        if ((getHomeLocation() == null) || (getWorkLocation() == null)) {}
      }
      else
      {
        if (getHomeLocation() == null) {
          this.mHomeLocation = createUnsetAliasLocation(0);
        }
        if (getWorkLocation() == null) {
          this.mWorkLocation = createUnsetAliasLocation(1);
        }
        return;
      }
      setHomeOrWork(localLocalResultCandidateList.getCandidateLocalResultList());
    }
  }
  
  public void setLabel(String paramString)
  {
    this.mLabel = paramString;
  }
  
  public void setLocation(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
  {
    this.mLocation = paramEcoutezLocalResult;
  }
  
  public void setLocationTriggerType(int paramInt)
  {
    this.mLocationTriggerType = paramInt;
  }
  
  public void setOriginalTaskId(String paramString)
  {
    this.mOriginalTaskId = paramString;
  }
  
  public void setRecurrence(EventRecurrence paramEventRecurrence)
  {
    this.mRecurrence = paramEventRecurrence;
  }
  
  public void setRecurrenceId(String paramString)
  {
    this.mRecurrenceId = paramString;
  }
  
  public void setSetUpTimeMs(long paramLong)
  {
    this.mSetUpTimeMs = paramLong;
  }
  
  public void setSymbolicTime(SymbolicTime paramSymbolicTime)
  {
    setSymbolicTime(paramSymbolicTime, DateTimePickerHelper.getAvailableTimes(isSupportedRepeatedReminder(), this.mDateTimeMs, this.mSetUpTimeMs));
  }
  
  public void setSymbolicTime(SymbolicTime paramSymbolicTime, List<SymbolicTime> paramList)
  {
    if (paramSymbolicTime == SymbolicTime.WEEKEND)
    {
      boolean bool2 = DateTimePickerHelper.isDateWeekendAvailable(this.mSetUpTimeMs);
      SymbolicTime localSymbolicTime = null;
      if (bool2) {
        localSymbolicTime = SymbolicTime.WEEKEND;
      }
      this.mSymbolicTime = localSymbolicTime;
      if ((this.mSymbolicTime != null) && ((this.mSymbolicTime != SymbolicTime.WEEKEND) || (!DateTimePickerHelper.isDateWeekendAvailable(this.mSetUpTimeMs))) && (!paramList.contains(this.mSymbolicTime))) {
        break label157;
      }
    }
    label157:
    for (boolean bool1 = true;; bool1 = false)
    {
      Preconditions.checkState(bool1);
      if (this.mSymbolicTime != null) {
        setTime(this.mSymbolicTime.defaultHour, 0);
      }
      return;
      if ((paramSymbolicTime == null) || (paramList.isEmpty()))
      {
        this.mSymbolicTime = null;
        break;
      }
      if (paramList.contains(paramSymbolicTime))
      {
        this.mSymbolicTime = paramSymbolicTime;
        break;
      }
      this.mSymbolicTime = ((SymbolicTime)paramList.get(0));
      break;
    }
  }
  
  public void setTime(int paramInt1, int paramInt2)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(this.mDateTimeMs);
    localCalendar.set(11, paramInt1);
    localCalendar.set(12, paramInt2);
    localCalendar.set(13, 0);
    localCalendar.set(14, 0);
    this.mDateTimeMs = localCalendar.getTimeInMillis();
  }
  
  public void setTriggerType(int paramInt)
  {
    this.mTriggerType = paramInt;
  }
  
  public void setWorkLocation(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
  {
    this.mWorkLocation = paramEcoutezLocalResult;
  }
  
  public String toString()
  {
    return "SetReminderAction(mSetUpTimeMs=" + this.mSetUpTimeMs + ", " + "mOriginalTaskId=" + this.mOriginalTaskId + ", " + "mDateTimeMs=" + this.mDateTimeMs + ", " + "mSymbolicTime=" + this.mSymbolicTime + ", " + "mRecurrence=" + this.mRecurrence + ", " + "mRecurrenceId=" + this.mRecurrenceId + ", " + "mLabel=" + this.mLabel + ", " + "mEmbeddedAction=" + this.mEmbeddedAction + ", " + "confirmationUrl=" + this.mConfirmationUrlPath + ", " + "mLocationTriggerType=" + this.mLocationTriggerType + ", " + "mTriggerType=" + this.mTriggerType + ", " + "mLocation=" + this.mLocation + ", " + "mHomeLocation=" + this.mHomeLocation + ", " + "mWorkLocation=" + this.mWorkLocation + ")";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.mOriginalTaskId);
    paramParcel.writeLong(this.mDateTimeMs);
    int i;
    if (this.mSymbolicTime == null)
    {
      i = -1;
      paramParcel.writeInt(i);
      paramParcel.writeString(this.mLabel);
      ProtoParcelable.writeProtoToParcel(this.mEmbeddedAction, paramParcel);
      paramParcel.writeString(this.mConfirmationUrlPath);
      paramParcel.writeInt(this.mLocationTriggerType);
      paramParcel.writeInt(this.mTriggerType);
      ProtoParcelable.writeProtoToParcel(this.mLocation, paramParcel);
      ProtoParcelable.writeProtoToParcel(this.mHomeLocation, paramParcel);
      ProtoParcelable.writeProtoToParcel(this.mWorkLocation, paramParcel);
      if (this.mRecurrence == null) {
        break label132;
      }
      paramParcel.writeString(this.mRecurrence.toString());
    }
    for (;;)
    {
      paramParcel.writeString(this.mRecurrenceId);
      return;
      i = this.mSymbolicTime.actionV2Symbol;
      break;
      label132:
      paramParcel.writeString(null);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.action.SetReminderAction
 * JD-Core Version:    0.7.0.1
 */