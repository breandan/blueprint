package com.google.android.speech.contacts;

import android.content.ContentUris;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.Feature;
import com.google.android.velvet.actions.Disambiguation.Candidate;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.majel.proto.ContactProtos.ContactInformation;
import com.google.majel.proto.ContactProtos.ContactInformation.EmailAddress;
import com.google.majel.proto.ContactProtos.ContactInformation.PersonalLocation;
import com.google.majel.proto.ContactProtos.ContactInformation.PhoneNumber;
import com.google.majel.proto.ContactProtos.ContactType;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Person
  implements Parcelable, PersonIdentity, Disambiguation.Candidate<Contact>
{
  public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator()
  {
    public Person createFromParcel(Parcel paramAnonymousParcel)
    {
      long l = paramAnonymousParcel.readLong();
      String str1 = paramAnonymousParcel.readString();
      String str2 = paramAnonymousParcel.readString();
      ArrayList localArrayList1 = Lists.newArrayList();
      paramAnonymousParcel.readList(localArrayList1, getClass().getClassLoader());
      ArrayList localArrayList2 = Lists.newArrayList();
      paramAnonymousParcel.readList(localArrayList2, getClass().getClassLoader());
      ArrayList localArrayList3 = Lists.newArrayList();
      paramAnonymousParcel.readList(localArrayList3, getClass().getClassLoader());
      Contact localContact = (Contact)paramAnonymousParcel.readValue(getClass().getClassLoader());
      String str3 = paramAnonymousParcel.readString();
      Person localPerson = new Person(l, str1, str2);
      localPerson.setPhoneNumbers(localArrayList1);
      localPerson.setEmailAddresses(localArrayList2);
      localPerson.setPostalAddresses(localArrayList3);
      localPerson.setSelectedItem(localContact);
      localPerson.setServerImageUri(str3);
      return localPerson;
    }
    
    public Person[] newArray(int paramAnonymousInt)
    {
      return new Person[paramAnonymousInt];
    }
  };
  private final List<Contact> mEmailAddresses;
  private final long mId;
  private final String mLookupKey;
  private final String mName;
  private final List<Contact> mPhoneNumbers;
  private final List<Contact> mPostalAddresses;
  private Contact mSelectedContact;
  private String mServerImageUri;
  
  public Person(long paramLong, String paramString)
  {
    this(paramLong, null, paramString);
  }
  
  public Person(long paramLong, String paramString1, String paramString2)
  {
    this.mId = paramLong;
    this.mLookupKey = paramString1;
    this.mName = paramString2;
    this.mPhoneNumbers = Lists.newArrayList();
    this.mEmailAddresses = Lists.newArrayList();
    this.mPostalAddresses = Lists.newArrayList();
  }
  
  public Person(@Nonnull PersonIdentity paramPersonIdentity)
  {
    this(paramPersonIdentity.getId(), paramPersonIdentity.getLookupKey(), paramPersonIdentity.getName());
  }
  
  private void addContactDetails(@Nonnull Contact paramContact)
  {
    switch (2.$SwitchMap$com$google$android$speech$contacts$ContactLookup$Mode[paramContact.getMode().ordinal()])
    {
    default: 
      throw new IllegalArgumentException("Unsupported mode: " + paramContact.getMode());
    case 1: 
      Contact.maybeMerge(this.mPhoneNumbers, paramContact);
    case 4: 
      return;
    case 2: 
      Contact.maybeMerge(this.mEmailAddresses, paramContact);
      return;
    }
    Contact.maybeMerge(this.mPostalAddresses, paramContact);
  }
  
  private static Contact createContact(ContactLookup.Mode paramMode, long paramLong, String paramString1, String paramString2, String paramString3, ContactProtos.ContactType paramContactType)
  {
    return new Contact(paramMode, paramLong, paramString1, paramString2, 0, paramString3, null, PhoneActionUtils.contactTypeToAndroidTypeColumn(paramMode, paramContactType), false);
  }
  
  public static List<Contact> denormalizeContacts(@Nonnull ContactLookup.Mode paramMode, List<Person> paramList)
  {
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext()) {
      localArrayList.addAll(((Person)localIterator.next()).denormalizeContacts(paramMode));
    }
    return localArrayList;
  }
  
  public static Person fromContact(@Nonnull Contact paramContact)
  {
    return normalizeContactsForOnePerson(Lists.newArrayList(new Contact[] { paramContact }));
  }
  
  public static Person fromContactInformation(@Nonnull ContactProtos.ContactInformation paramContactInformation)
  {
    String str1 = paramContactInformation.getClientEntityId();
    long l1 = 0L;
    if (!TextUtils.isEmpty(str1)) {}
    String str2;
    String str3;
    ArrayList localArrayList1;
    ArrayList localArrayList2;
    ArrayList localArrayList3;
    try
    {
      long l2 = ContentUris.parseId(Uri.parse(str1));
      l1 = l2;
      str2 = paramContactInformation.getDisplayName();
      str3 = paramContactInformation.getImageUri();
      localArrayList1 = Lists.newArrayList();
      localArrayList2 = Lists.newArrayList();
      localArrayList3 = Lists.newArrayList();
      Iterator localIterator1 = paramContactInformation.getPhoneNumberList().iterator();
      while (localIterator1.hasNext())
      {
        ContactProtos.ContactInformation.PhoneNumber localPhoneNumber = (ContactProtos.ContactInformation.PhoneNumber)localIterator1.next();
        localArrayList1.add(createContact(ContactLookup.Mode.PHONE_NUMBER, l1, null, str2, localPhoneNumber.getValue(), localPhoneNumber.getContactType()));
      }
      localIterator2 = paramContactInformation.getEmailAddressList().iterator();
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
      Log.e("Person", "ContactInformation has invalid ClientEntityId: " + str1, localUnsupportedOperationException);
      return null;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      Log.e("Person", "ContactInformation has invalid ClientEntityId: " + str1, localNumberFormatException);
      return null;
    }
    Iterator localIterator2;
    while (localIterator2.hasNext())
    {
      ContactProtos.ContactInformation.EmailAddress localEmailAddress = (ContactProtos.ContactInformation.EmailAddress)localIterator2.next();
      localArrayList2.add(createContact(ContactLookup.Mode.EMAIL, l1, null, str2, localEmailAddress.getValue(), localEmailAddress.getContactType()));
    }
    Iterator localIterator3 = paramContactInformation.getPostalAddressList().iterator();
    while (localIterator3.hasNext())
    {
      ContactProtos.ContactInformation.PersonalLocation localPersonalLocation = (ContactProtos.ContactInformation.PersonalLocation)localIterator3.next();
      EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult = localPersonalLocation.getValue();
      if ((localEcoutezLocalResult != null) && (localEcoutezLocalResult.hasAddress())) {
        localArrayList3.add(createContact(ContactLookup.Mode.POSTAL_ADDRESS, l1, null, str2, localEcoutezLocalResult.getAddress(), localPersonalLocation.getContactType()));
      }
    }
    return new Person(l1, null, str2).setPhoneNumbers(localArrayList1).setEmailAddresses(localArrayList2).setPostalAddresses(localArrayList3).setServerImageUri(str3);
  }
  
  private static Person maybeMerge(Person paramPerson1, Person paramPerson2, PersonMergeStrategy[] paramArrayOfPersonMergeStrategy)
  {
    int i = paramArrayOfPersonMergeStrategy.length;
    for (int j = 0; j < i; j++)
    {
      Person localPerson = paramArrayOfPersonMergeStrategy[j].maybeMerge(paramPerson1, paramPerson2);
      if (localPerson != null) {
        return localPerson;
      }
    }
    return null;
  }
  
  public static List<Person> mergeLists(@Nonnull List<Person> paramList1, @Nonnull List<Person> paramList2, @Nonnull PersonMergeStrategy... paramVarArgs)
  {
    Preconditions.checkNotNull(paramList1);
    Preconditions.checkNotNull(paramList2);
    ArrayList localArrayList = Lists.newArrayList(paramList1);
    Iterator localIterator = paramList2.iterator();
    if (localIterator.hasNext())
    {
      Person localPerson1 = (Person)localIterator.next();
      for (int i = 0;; i++)
      {
        int j = localArrayList.size();
        int k = 0;
        if (i < j)
        {
          Person localPerson2 = maybeMerge((Person)localArrayList.get(i), localPerson1, paramVarArgs);
          if (localPerson2 != null)
          {
            localArrayList.set(i, localPerson2);
            k = 1;
          }
        }
        else
        {
          if (k != 0) {
            break;
          }
          localArrayList.add(localPerson1);
          break;
        }
      }
    }
    return localArrayList;
  }
  
  public static List<Person> mergePersons(@Nonnull List<Person> paramList, @Nonnull PersonMergeStrategy... paramVarArgs)
  {
    Preconditions.checkNotNull(paramList);
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = paramList.iterator();
    if (localIterator.hasNext())
    {
      Person localPerson1 = (Person)localIterator.next();
      for (int i = 0;; i++)
      {
        int j = localArrayList.size();
        int k = 0;
        if (i < j)
        {
          Person localPerson2 = maybeMerge((Person)localArrayList.get(i), localPerson1, paramVarArgs);
          if (localPerson2 != null)
          {
            localArrayList.set(i, localPerson2);
            k = 1;
          }
        }
        else
        {
          if (k != 0) {
            break;
          }
          localArrayList.add(localPerson1);
          break;
        }
      }
    }
    return localArrayList;
  }
  
  public static List<Person> normalizeContacts(@Nonnull List<Contact> paramList)
  {
    LinkedHashMap localLinkedHashMap = Maps.newLinkedHashMap();
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = paramList.iterator();
    if (localIterator.hasNext())
    {
      Contact localContact = (Contact)localIterator.next();
      long l = localContact.getId();
      Person localPerson;
      if (l > 0L) {
        if (localLinkedHashMap.containsKey(Long.valueOf(l))) {
          localPerson = (Person)localLinkedHashMap.get(Long.valueOf(l));
        }
      }
      for (;;)
      {
        localPerson.addContactDetails(localContact);
        break;
        localPerson = new Person(localContact);
        localLinkedHashMap.put(Long.valueOf(l), localPerson);
        localArrayList.add(localPerson);
        continue;
        localPerson = new Person(localContact);
        localArrayList.add(localPerson);
      }
    }
    return localArrayList;
  }
  
  public static Person normalizeContactsForOnePerson(@Nonnull List<Contact> paramList)
  {
    Person localPerson = new Person((PersonIdentity)paramList.get(0));
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext()) {
      localPerson.addContactDetails((Contact)localIterator.next());
    }
    return localPerson;
  }
  
  public boolean autoSelectItem(ContactLookup.Mode paramMode)
  {
    if (this.mSelectedContact != null) {
      return true;
    }
    int i = 2.$SwitchMap$com$google$android$speech$contacts$ContactLookup$Mode[paramMode.ordinal()];
    Contact localContact = null;
    switch (i)
    {
    }
    for (;;)
    {
      setSelectedItem(localContact);
      if (localContact != null) {
        break;
      }
      return false;
      int m = this.mPhoneNumbers.size();
      localContact = null;
      if (m == 1)
      {
        localContact = (Contact)this.mPhoneNumbers.get(0);
        continue;
        int k = this.mEmailAddresses.size();
        localContact = null;
        if (k == 1)
        {
          localContact = (Contact)this.mEmailAddresses.get(0);
          continue;
          int j = this.mPostalAddresses.size();
          localContact = null;
          if (j == 1) {
            localContact = (Contact)this.mPostalAddresses.get(0);
          }
        }
      }
    }
  }
  
  public List<Contact> denormalizeContacts(@Nonnull ContactLookup.Mode paramMode)
  {
    if (paramMode != ContactLookup.Mode.PERSON) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool, "Cannot denormalize a Person lookup");
      switch (2.$SwitchMap$com$google$android$speech$contacts$ContactLookup$Mode[paramMode.ordinal()])
      {
      default: 
        throw new IllegalArgumentException("Unsupported mode: " + paramMode);
      }
    }
    return getPhoneNumbers();
    return getEmailAddresses();
    return getPostalAddresses();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    return ((paramObject instanceof Person)) && (((Person)paramObject).mId == this.mId);
  }
  
  public List<Contact> getEmailAddresses()
  {
    return ImmutableList.copyOf(this.mEmailAddresses);
  }
  
  public long getId()
  {
    return this.mId;
  }
  
  public String getLookupKey()
  {
    return this.mLookupKey;
  }
  
  @Nullable
  public String getName()
  {
    return this.mName;
  }
  
  public int getNumSelectableItems(ContactLookup.Mode paramMode)
  {
    if (this.mSelectedContact != null) {
      return 1;
    }
    switch (2.$SwitchMap$com$google$android$speech$contacts$ContactLookup$Mode[paramMode.ordinal()])
    {
    default: 
      return 1;
    case 1: 
      return this.mPhoneNumbers.size();
    case 2: 
      return this.mEmailAddresses.size();
    }
    return this.mPostalAddresses.size();
  }
  
  public List<Contact> getPhoneNumbers()
  {
    return ImmutableList.copyOf(this.mPhoneNumbers);
  }
  
  public List<Contact> getPostalAddresses()
  {
    return ImmutableList.copyOf(this.mPostalAddresses);
  }
  
  public Contact getSelectedItem()
  {
    return this.mSelectedContact;
  }
  
  public String getServerImageUri()
  {
    return this.mServerImageUri;
  }
  
  public Uri getUri()
  {
    if ((this.mPhoneNumbers.size() == 1) && (!((Contact)this.mPhoneNumbers.get(0)).hasName())) {
      return new Uri.Builder().scheme("tel").opaquePart(((Contact)this.mPhoneNumbers.get(0)).getValue()).build();
    }
    return ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, this.mId);
  }
  
  public boolean hasCommonEmailAddress(Person paramPerson)
  {
    Contact localContact;
    Iterator localIterator2;
    do
    {
      Iterator localIterator1 = this.mEmailAddresses.iterator();
      while (!localIterator2.hasNext())
      {
        do
        {
          if (!localIterator1.hasNext()) {
            break;
          }
          localContact = (Contact)localIterator1.next();
        } while (!localContact.hasValue());
        localIterator2 = paramPerson.getEmailAddresses().iterator();
      }
    } while (!localContact.hasSameValue((Contact)localIterator2.next()));
    return true;
    return false;
  }
  
  public boolean hasCommonPhoneNumber(Person paramPerson)
  {
    Contact localContact;
    Iterator localIterator2;
    do
    {
      Iterator localIterator1 = getPhoneNumbers().iterator();
      while (!localIterator2.hasNext())
      {
        do
        {
          if (!localIterator1.hasNext()) {
            break;
          }
          localContact = (Contact)localIterator1.next();
        } while (!localContact.hasValue());
        localIterator2 = paramPerson.getPhoneNumbers().iterator();
      }
    } while (!localContact.hasSameValue((Contact)localIterator2.next()));
    return true;
    return false;
  }
  
  public boolean hasName()
  {
    return !TextUtils.isEmpty(this.mName);
  }
  
  public boolean hasSameName(@Nullable PersonIdentity paramPersonIdentity)
  {
    return (paramPersonIdentity != null) && (paramPersonIdentity.hasName()) && (hasName()) && (getName().equals(paramPersonIdentity.getName()));
  }
  
  public boolean hasSameServerImageUri(@Nullable Person paramPerson)
  {
    return (paramPerson != null) && (paramPerson.hasServerImageUri()) && (hasServerImageUri()) && (getServerImageUri().equals(paramPerson.getServerImageUri()));
  }
  
  public boolean hasServerImageUri()
  {
    return !TextUtils.isEmpty(this.mServerImageUri);
  }
  
  public int hashCode()
  {
    return (int)this.mId;
  }
  
  public Person mergePerson(Person paramPerson)
  {
    Preconditions.checkNotNull(paramPerson);
    ArrayList localArrayList1 = Lists.newArrayList(getPhoneNumbers());
    localArrayList1.addAll(paramPerson.getPhoneNumbers());
    setPhoneNumbers(Contact.filterUnique(localArrayList1));
    ArrayList localArrayList2 = Lists.newArrayList(getEmailAddresses());
    localArrayList2.addAll(paramPerson.getEmailAddresses());
    setEmailAddresses(Contact.filterUnique(localArrayList2));
    ArrayList localArrayList3 = Lists.newArrayList(getPostalAddresses());
    localArrayList3.addAll(paramPerson.getPostalAddresses());
    setPostalAddresses(Contact.filterUnique(localArrayList3));
    return this;
  }
  
  public Person setEmailAddresses(@Nonnull List<Contact> paramList)
  {
    this.mEmailAddresses.clear();
    this.mEmailAddresses.addAll(Contact.filterUnique(paramList));
    return this;
  }
  
  public Person setPhoneNumbers(@Nonnull List<Contact> paramList)
  {
    this.mPhoneNumbers.clear();
    this.mPhoneNumbers.addAll(Contact.filterUnique(paramList));
    return this;
  }
  
  public Person setPostalAddresses(@Nonnull List<Contact> paramList)
  {
    this.mPostalAddresses.clear();
    this.mPostalAddresses.addAll(Contact.filterUnique(paramList));
    return this;
  }
  
  public void setSelectedItem(Contact paramContact)
  {
    this.mSelectedContact = paramContact;
  }
  
  public Person setServerImageUri(String paramString)
  {
    this.mServerImageUri = paramString;
    return this;
  }
  
  public ContactProtos.ContactInformation toContactInformation()
  {
    Preconditions.checkState(Feature.DISCOURSE_CONTEXT_CONTACTS.isEnabled());
    ContactProtos.ContactInformation localContactInformation = new ContactProtos.ContactInformation();
    if (hasName())
    {
      localContactInformation.setDisplayName(getName());
      localContactInformation.setClientEntityId(getUri().toString());
    }
    Contact localContact = getSelectedItem();
    if (localContact != null) {
      localContact.addToContactInformation(localContactInformation);
    }
    for (;;)
    {
      return localContactInformation;
      Iterator localIterator1 = this.mPhoneNumbers.iterator();
      while (localIterator1.hasNext()) {
        ((Contact)localIterator1.next()).addToContactInformation(localContactInformation);
      }
      Iterator localIterator2 = this.mEmailAddresses.iterator();
      while (localIterator2.hasNext()) {
        ((Contact)localIterator2.next()).addToContactInformation(localContactInformation);
      }
      Iterator localIterator3 = this.mPostalAddresses.iterator();
      while (localIterator3.hasNext()) {
        ((Contact)localIterator3.next()).addToContactInformation(localContactInformation);
      }
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(this.mId);
    paramParcel.writeString(this.mLookupKey);
    paramParcel.writeString(this.mName);
    paramParcel.writeList(this.mPhoneNumbers);
    paramParcel.writeList(this.mEmailAddresses);
    paramParcel.writeList(this.mPostalAddresses);
    paramParcel.writeValue(this.mSelectedContact);
    paramParcel.writeString(this.mServerImageUri);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.Person
 * JD-Core Version:    0.7.0.1
 */