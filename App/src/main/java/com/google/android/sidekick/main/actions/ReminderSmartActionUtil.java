package com.google.android.sidekick.main.actions;

import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.sidekick.main.sync.RepeatedMessageInfo;
import com.google.android.sidekick.main.sync.StateMerge;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.ContactLookup.Mode;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.fragments.action.PhoneCallAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.geo.sidekick.Sidekick.ContactData;
import com.google.geo.sidekick.Sidekick.ContactReference;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.PhoneNumber;
import com.google.geo.sidekick.Sidekick.ReminderData;
import com.google.geo.sidekick.Sidekick.ReminderData.SmartActionData;
import com.google.majel.proto.ContactProtos.ContactInformation;
import com.google.majel.proto.ContactProtos.ContactInformation.PhoneNumber;
import com.google.majel.proto.ContactProtos.ContactReference;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class ReminderSmartActionUtil
{
  private final DeviceCapabilityManager mDeviceCapabilityManager;
  
  public ReminderSmartActionUtil(DeviceCapabilityManager paramDeviceCapabilityManager)
  {
    this.mDeviceCapabilityManager = paramDeviceCapabilityManager;
  }
  
  @Nullable
  private PhoneCallAction parsePhoneCallAction(Sidekick.ReminderData.SmartActionData paramSmartActionData, ContactLookup paramContactLookup)
  {
    ContactProtos.ContactReference localContactReference = convertContactReference(paramSmartActionData.getContactReference());
    VelvetServices localVelvetServices = VelvetServices.get();
    return new PhoneCallAction(PhoneActionUtils.createPersonDisambiguationContactReference(paramContactLookup, ContactLookup.Mode.PHONE_NUMBER, localContactReference, localVelvetServices.getCoreServices().getDiscourseContext()));
  }
  
  ContactProtos.ContactReference convertContactReference(Sidekick.ContactReference paramContactReference)
  {
    ContactProtos.ContactReference localContactReference = new ContactProtos.ContactReference();
    RepeatedMessageInfo localRepeatedMessageInfo = new RepeatedMessageInfo();
    StateMerge.newStateMergeFor(localContactReference, localRepeatedMessageInfo, false).applyUpdates(paramContactReference);
    Iterator localIterator1 = paramContactReference.getContactDataList().iterator();
    while (localIterator1.hasNext())
    {
      Sidekick.ContactData localContactData = (Sidekick.ContactData)localIterator1.next();
      ContactProtos.ContactInformation localContactInformation = new ContactProtos.ContactInformation();
      if (localContactData.hasName()) {
        localContactInformation.setDisplayName(localContactData.getName());
      }
      if (localContactData.hasClientEntityId()) {
        localContactInformation.setClientEntityId(localContactData.getClientEntityId());
      }
      Iterator localIterator2 = localContactData.getPhoneNumberList().iterator();
      while (localIterator2.hasNext())
      {
        Sidekick.PhoneNumber localPhoneNumber = (Sidekick.PhoneNumber)localIterator2.next();
        ContactProtos.ContactInformation.PhoneNumber localPhoneNumber1 = new ContactProtos.ContactInformation.PhoneNumber();
        StateMerge.newStateMergeFor(localPhoneNumber1, localRepeatedMessageInfo, false).applyUpdates(localPhoneNumber);
        localContactInformation.addPhoneNumber(localPhoneNumber1);
      }
      localContactReference.addContactInformation(localContactInformation);
    }
    return localContactReference;
  }
  
  @Nullable
  public VoiceAction extractVoiceAction(Sidekick.Entry paramEntry, int paramInt, ContactLookup paramContactLookup)
  {
    Sidekick.ReminderData.SmartActionData localSmartActionData = getSmartActionDataFromEntry(paramEntry, paramInt);
    if (localSmartActionData == null) {
      return null;
    }
    switch (localSmartActionData.getType())
    {
    default: 
      return null;
    }
    return parsePhoneCallAction(localSmartActionData, paramContactLookup);
  }
  
  @Nullable
  Sidekick.ReminderData.SmartActionData getSmartActionDataFromEntry(Sidekick.Entry paramEntry, int paramInt)
  {
    if (!paramEntry.hasReminderData()) {
      return null;
    }
    Iterator localIterator = paramEntry.getReminderData().getSmartActionDataList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.ReminderData.SmartActionData localSmartActionData = (Sidekick.ReminderData.SmartActionData)localIterator.next();
      if (localSmartActionData.getType() == paramInt) {
        return localSmartActionData;
      }
    }
    return null;
  }
  
  public boolean isSmartActionSupportedByDevice(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return true;
    }
    return this.mDeviceCapabilityManager.isTelephoneCapable();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.ReminderSmartActionUtil
 * JD-Core Version:    0.7.0.1
 */