package com.google.android.speech.contacts;

import com.google.android.search.core.Feature;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.majel.proto.ContactProtos.ContactQuery;
import java.util.List;

public class ContactReferenceLookupSupplier
  implements Supplier<List<Person>>
{
  private final ContactLookup mContactLookup;
  private final ContactProtos.ContactQuery mContactQuery;
  private final List<Person> mServerPersons;
  
  public ContactReferenceLookupSupplier(List<Person> paramList, ContactLookup paramContactLookup, ContactProtos.ContactQuery paramContactQuery)
  {
    if (paramContactQuery.getNameCount() > 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      this.mServerPersons = ((List)Preconditions.checkNotNull(paramList));
      this.mContactLookup = ((ContactLookup)Preconditions.checkNotNull(paramContactLookup));
      this.mContactQuery = paramContactQuery;
      return;
    }
  }
  
  public List<Person> get()
  {
    List localList = this.mContactLookup.findAllByDisplayName(this.mContactQuery);
    if ((this.mServerPersons == null) || (this.mServerPersons.isEmpty())) {
      return localList;
    }
    if (Feature.CONTACT_REFERENCE_MERGE_DETAILS.isEnabled())
    {
      PersonMergeStrategy[] arrayOfPersonMergeStrategy = new PersonMergeStrategy[3];
      arrayOfPersonMergeStrategy[0] = new PersonMergeStrategy.MergeByNameAndEmailAddress();
      arrayOfPersonMergeStrategy[1] = new PersonMergeStrategy.MergeByNameAndPhoneNumber();
      arrayOfPersonMergeStrategy[2] = new PersonMergeStrategy.MergeByNameOnly();
      return Person.mergeLists(localList, this.mServerPersons, arrayOfPersonMergeStrategy);
    }
    localList.addAll(this.mServerPersons);
    return localList;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.ContactReferenceLookupSupplier
 * JD-Core Version:    0.7.0.1
 */