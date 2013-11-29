package com.google.android.speech.contacts;

import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.majel.proto.ActionV2Protos.ActionContact;
import java.util.List;
import javax.annotation.Nullable;

public class ActionContactLookupSupplier
  implements Supplier<List<Person>>
{
  private final ContactLookup mContactLookup;
  private final ContactSelectMode mMode;
  @Nullable
  private final String mPreferredContactType;
  private final List<ActionV2Protos.ActionContact> mRecognizedContacts;
  
  public ActionContactLookupSupplier(ContactLookup paramContactLookup, ContactSelectMode paramContactSelectMode, List<ActionV2Protos.ActionContact> paramList, @Nullable String paramString)
  {
    if (paramList.size() > 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      this.mContactLookup = ((ContactLookup)Preconditions.checkNotNull(paramContactLookup));
      this.mMode = ((ContactSelectMode)Preconditions.checkNotNull(paramContactSelectMode));
      this.mRecognizedContacts = paramList;
      this.mPreferredContactType = paramString;
      return;
    }
  }
  
  public List<Person> get()
  {
    return this.mContactLookup.findAllByDisplayName(this.mMode.getContactLookupMode(), this.mRecognizedContacts, this.mPreferredContactType);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.ActionContactLookupSupplier
 * JD-Core Version:    0.7.0.1
 */