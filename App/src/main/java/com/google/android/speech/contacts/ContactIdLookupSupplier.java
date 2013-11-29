package com.google.android.speech.contacts;

import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.common.base.Supplier;
import java.util.Collections;
import java.util.List;

public class ContactIdLookupSupplier
  implements Supplier<List<Person>>
{
  private final ContactLookup mContactLookup;
  private final long mId;
  private final ContactSelectMode mMode;
  
  public ContactIdLookupSupplier(ContactLookup paramContactLookup, ContactSelectMode paramContactSelectMode, long paramLong)
  {
    this.mContactLookup = paramContactLookup;
    this.mMode = paramContactSelectMode;
    this.mId = paramLong;
  }
  
  public List<Person> get()
  {
    switch (1.$SwitchMap$com$google$android$voicesearch$contacts$ContactSelectMode[this.mMode.ordinal()])
    {
    default: 
      return Person.normalizeContacts(this.mContactLookup.fetchPhoneNumbers(this.mId, null));
    case 3: 
      return Person.normalizeContacts(this.mContactLookup.fetchEmailAddresses(this.mId, null));
    }
    return Collections.singletonList(this.mContactLookup.fetchContactInfo(this.mId));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.ContactIdLookupSupplier
 * JD-Core Version:    0.7.0.1
 */