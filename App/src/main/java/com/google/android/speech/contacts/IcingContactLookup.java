package com.google.android.speech.contacts;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.appdatasearch.PhraseAffinityCorpusSpec.Builder;
import com.google.android.gms.appdatasearch.PhraseAffinityResponse;
import com.google.android.gms.appdatasearch.PhraseAffinitySpecification;
import com.google.android.gms.appdatasearch.PhraseAffinitySpecification.Builder;
import com.google.android.gms.appdatasearch.QuerySpecification;
import com.google.android.gms.appdatasearch.SearchResults;
import com.google.android.gms.appdatasearch.SearchResults.Result;
import com.google.android.gms.appdatasearch.SearchResults.ResultIterator;
import com.google.android.gms.appdatasearch.Section;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.RelationshipManager;
import com.google.android.search.core.summons.icing.ConnectionToIcing;
import com.google.android.search.core.summons.icing.ContactsHelper;
import com.google.android.search.core.summons.icing.IcingFactory;
import com.google.android.search.core.summons.icing.InternalCorpus;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.majel.proto.ActionV2Protos.ActionContact;
import com.google.majel.proto.ContactProtos.ContactQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IcingContactLookup
  extends ContactLookup
{
  private static final String CONTACTS_CORPUS_NAME = InternalCorpus.CONTACTS.getCorpusName();
  private static final String[] CORPORA_CONTACTS;
  private final QuerySpecification mContactsQuerySpecification;
  private final ConnectionToIcing mIcingConnection;
  private final PhraseAffinitySpecification mPhraseAffinitySpecification;
  private final ProviderContactLookup mProviderContactLookup;
  private final Resources mResources;
  
  static
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = CONTACTS_CORPUS_NAME;
    CORPORA_CONTACTS = arrayOfString;
  }
  
  public IcingContactLookup(Context paramContext, RelationshipNameLookup paramRelationshipNameLookup, RelationshipManager paramRelationshipManager)
  {
    this.mResources = paramContext.getResources();
    this.mIcingConnection = VelvetServices.get().getGlobalSearchServices().getIcingFactory().getConnectionToIcing();
    ArrayList localArrayList = Lists.newArrayList();
    localArrayList.add(new Section("name"));
    localArrayList.add(new Section("givennames"));
    localArrayList.add(new Section("nickname"));
    localArrayList.add(new Section("number"));
    localArrayList.add(new Section("number_types"));
    localArrayList.add(new Section("number_labels"));
    localArrayList.add(new Section("lookup_key"));
    localArrayList.add(new Section("icon_uri"));
    localArrayList.add(new Section("email"));
    localArrayList.add(new Section("email_types"));
    localArrayList.add(new Section("email_labels"));
    localArrayList.add(new Section("address"));
    localArrayList.add(new Section("address_types"));
    localArrayList.add(new Section("address_labels"));
    this.mContactsQuerySpecification = new QuerySpecification(false, true, localArrayList, null);
    this.mPhraseAffinitySpecification = new PhraseAffinitySpecification.Builder().addCorpusSpec(new PhraseAffinityCorpusSpec.Builder().packageName(paramContext.getPackageName()).corpusName(CONTACTS_CORPUS_NAME).addSectionWeight("name", 48).addSectionWeight("givennames", 32).addSectionWeight("nickname", 16)).build();
    this.mProviderContactLookup = new ProviderContactLookup(paramContext.getContentResolver(), paramRelationshipNameLookup, paramRelationshipManager);
  }
  
  @Nonnull
  private List<Contact> filterContactsByType(List<Contact> paramList, @Nullable String paramString)
  {
    int i = safeStringToInt(paramString, -1);
    if (i == -1) {
      return paramList;
    }
    int j = -1;
    for (int k = 0;; k++) {
      if (k < paramList.size())
      {
        if (((Contact)paramList.get(k)).getType() == i) {
          j = k;
        }
      }
      else
      {
        if (j == -1) {
          break;
        }
        Contact[] arrayOfContact = new Contact[1];
        arrayOfContact[0] = ((Contact)paramList.get(j));
        return Lists.newArrayList(arrayOfContact);
      }
    }
  }
  
  private List<Person> findAllByDisplayName(List<String> paramList, @Nullable ContactLookup.Mode paramMode, @Nullable String paramString)
  {
    Object localObject = Lists.newArrayList();
    if (paramList.isEmpty()) {
      return localObject;
    }
    Iterator localIterator = getNamesToQuery(paramList).iterator();
    SearchResults.Result localResult;
    for (;;)
    {
      if (!localIterator.hasNext()) {
        break label331;
      }
      String str1 = (String)localIterator.next();
      SearchResults localSearchResults = this.mIcingConnection.blockingQuery(str1, CORPORA_CONTACTS, this.mContactsQuerySpecification);
      if (localSearchResults != null)
      {
        SearchResults.ResultIterator localResultIterator = localSearchResults.iterator();
        while (localResultIterator.hasNext())
        {
          localResult = localResultIterator.next();
          localResult.getPackageName();
          localResult.getCorpus();
          long l1 = 0L;
          try
          {
            long l2 = Long.parseLong(localResult.getUri());
            l1 = l2;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            for (;;)
            {
              String str2;
              String str3;
              Person localPerson;
              List localList1;
              List localList2;
              List localList3;
              Log.w("IcingContactLookup", "Could parse contact id: " + localResult.getUri());
            }
          }
          str2 = localResult.getSection("name");
          str3 = localResult.getSection("lookup_key");
          localPerson = new Person(l1, str3, str2);
          localList1 = getContactsFromResult(l1, str3, str2, localResult, ContactLookup.Mode.PHONE_NUMBER, "number", "number_types", "number_labels");
          if (localList1 != null) {
            localPerson.setPhoneNumbers(filterContactsByType(localList1, paramString));
          }
          localList2 = getContactsFromResult(l1, str3, str2, localResult, ContactLookup.Mode.EMAIL, "email", "email_types", "email_labels");
          if (localList2 != null) {
            localPerson.setEmailAddresses(filterContactsByType(localList2, paramString));
          }
          localList3 = getContactsFromResult(l1, str3, str2, localResult, ContactLookup.Mode.POSTAL_ADDRESS, "address", "address_types", "address_labels");
          if (localList3 != null) {
            localPerson.setPostalAddresses(filterContactsByType(localList3, paramString));
          }
          ((List)localObject).add(localPerson);
        }
      }
    }
    label331:
    if (paramMode != null) {
      localObject = rerankByMode((List)localObject, paramMode);
    }
    PersonMergeStrategy[] arrayOfPersonMergeStrategy = new PersonMergeStrategy[1];
    arrayOfPersonMergeStrategy[0] = new PersonMergeStrategy.MergeById();
    return Person.mergePersons((List)localObject, arrayOfPersonMergeStrategy);
  }
  
  @Nullable
  private List<Contact> getContactsFromResult(long paramLong, String paramString1, String paramString2, SearchResults.Result paramResult, ContactLookup.Mode paramMode, String paramString3, String paramString4, String paramString5)
  {
    String[] arrayOfString1 = ContactsHelper.sectionToList(paramResult.getSection(paramString3));
    String[] arrayOfString2 = ContactsHelper.sectionToList(paramResult.getSection(paramString4));
    String[] arrayOfString3 = ContactsHelper.sectionToList(paramResult.getSection(paramString5));
    Object localObject;
    if ((arrayOfString1 == null) || (arrayOfString1.length == 0))
    {
      localObject = null;
      return localObject;
    }
    label76:
    label99:
    int i;
    label107:
    String str;
    if (arrayOfString2 == null)
    {
      Log.w("IcingContactLookup", "getContactsFromResult() : Null type section.");
      arrayOfString2 = new String[arrayOfString1.length];
      if (arrayOfString3 != null) {
        break label197;
      }
      Log.w("IcingContactLookup", "getContactsFromResult() : Null label section.");
      arrayOfString3 = new String[arrayOfString1.length];
      localObject = Lists.newArrayList();
      i = 0;
      if (i < arrayOfString1.length)
      {
        str = arrayOfString1[i];
        if (!TextUtils.isEmpty(str)) {
          break label258;
        }
      }
    }
    for (;;)
    {
      i++;
      break label107;
      break;
      if (arrayOfString2.length == arrayOfString1.length) {
        break label76;
      }
      Log.w("IcingContactLookup", "getContactsFromResult() : Wrong number of types: " + arrayOfString2.length + " != " + arrayOfString1.length);
      arrayOfString2 = new String[arrayOfString1.length];
      break label76;
      label197:
      if (arrayOfString3.length == arrayOfString1.length) {
        break label99;
      }
      Log.w("IcingContactLookup", "getContactsFromResult() : Wrong number of labels: " + arrayOfString3.length + " != " + arrayOfString1.length);
      arrayOfString3 = new String[arrayOfString1.length];
      break label99;
      label258:
      ((List)localObject).add(new Contact(paramMode, paramLong, paramString1, paramString2, 0, str, arrayOfString3[i], safeStringToInt(arrayOfString2[i], -1), false));
    }
  }
  
  static final List<String> getNamesToQuery(@Nonnull List<String> paramList)
  {
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      label40:
      int j;
      label59:
      String str2;
      if (localArrayList.contains(str1))
      {
        String[] arrayOfString = NicknameTable.getNamesForNickname(str1);
        if (arrayOfString == null) {
          continue;
        }
        int i = arrayOfString.length;
        j = 0;
        if (j < i)
        {
          str2 = arrayOfString[j];
          if (!localArrayList.contains(str2)) {
            break label101;
          }
        }
      }
      for (;;)
      {
        j++;
        break label59;
        break;
        localArrayList.add(str1);
        break label40;
        label101:
        localArrayList.add(str2);
      }
    }
    return localArrayList;
  }
  
  private ContactLookup.Mode getPreferredLookupMode(ContactProtos.ContactQuery paramContactQuery)
  {
    if (paramContactQuery.getContactMethodCount() == 1) {}
    switch (paramContactQuery.getContactMethod(0))
    {
    default: 
      return null;
    case 1: 
      return ContactLookup.Mode.PHONE_NUMBER;
    case 2: 
      return ContactLookup.Mode.EMAIL;
    }
    return ContactLookup.Mode.POSTAL_ADDRESS;
  }
  
  static List<Person> rerankByMode(List<Person> paramList, ContactLookup.Mode paramMode)
  {
    Preconditions.checkNotNull(paramMode);
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Person localPerson = (Person)localIterator.next();
      if (paramMode == ContactLookup.Mode.PHONE_NUMBER)
      {
        if (localPerson.getPhoneNumbers().size() > 0)
        {
          localIterator.remove();
          localArrayList.add(localPerson);
        }
      }
      else if (paramMode == ContactLookup.Mode.EMAIL)
      {
        if (localPerson.getEmailAddresses().size() > 0)
        {
          localIterator.remove();
          localArrayList.add(localPerson);
        }
      }
      else if ((paramMode == ContactLookup.Mode.POSTAL_ADDRESS) && (localPerson.getPostalAddresses().size() > 0))
      {
        localIterator.remove();
        localArrayList.add(localPerson);
      }
    }
    localArrayList.addAll(paramList);
    return localArrayList;
  }
  
  private int safeStringToInt(String paramString, int paramInt)
  {
    if (TextUtils.isEmpty(paramString)) {
      return paramInt;
    }
    try
    {
      int i = Integer.parseInt(paramString);
      return i;
    }
    catch (NumberFormatException localNumberFormatException) {}
    return paramInt;
  }
  
  public Person fetchContactInfo(long paramLong)
  {
    return this.mProviderContactLookup.fetchContactInfo(paramLong);
  }
  
  public List<Contact> fetchEmailAddresses(long paramLong, String paramString)
  {
    return this.mProviderContactLookup.fetchEmailAddresses(paramLong, paramString);
  }
  
  public List<Contact> fetchPhoneNumbers(long paramLong, String paramString)
  {
    return this.mProviderContactLookup.fetchPhoneNumbers(paramLong, paramString);
  }
  
  public List<Contact> fetchPostalAddresses(long paramLong, String paramString)
  {
    return this.mProviderContactLookup.fetchPostalAddresses(paramLong, paramString);
  }
  
  public List<Person> findAllByDisplayName(@Nonnull ContactLookup.Mode paramMode, List<ActionV2Protos.ActionContact> paramList, @Nullable String paramString)
  {
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      ActionV2Protos.ActionContact localActionContact = (ActionV2Protos.ActionContact)localIterator.next();
      if (localActionContact.hasName()) {
        localArrayList.add(localActionContact.getName());
      }
    }
    return findAllByDisplayName(localArrayList, paramMode, paramString);
  }
  
  public List<Person> findAllByDisplayName(ContactProtos.ContactQuery paramContactQuery)
  {
    Preconditions.checkState(Feature.CONTACT_REFERENCE.isEnabled());
    ContactLookup.Mode localMode = getPreferredLookupMode(paramContactQuery);
    List localList = paramContactQuery.getNameList();
    boolean bool = paramContactQuery.hasContactType();
    String str = null;
    if (bool) {
      str = String.valueOf(PhoneActionUtils.contactTypeToAndroidTypeColumn(localMode, paramContactQuery.getContactType()));
    }
    return findAllByDisplayName(localList, localMode, str);
  }
  
  public List<Person> findAllByPhoneNumber(String paramString)
  {
    return this.mProviderContactLookup.findAllByPhoneNumber(paramString);
  }
  
  public List<String> findFavoriteContactNames(int paramInt1, int paramInt2)
  {
    return this.mProviderContactLookup.findFavoriteContactNames(paramInt1, paramInt2);
  }
  
  public boolean hasMatchingContacts(String paramString)
  {
    PhraseAffinityResponse localPhraseAffinityResponse = this.mIcingConnection.blockingGetPhraseAffinity(new String[] { paramString }, this.mPhraseAffinitySpecification);
    if (localPhraseAffinityResponse == null) {}
    while (!localPhraseAffinityResponse.isPhraseFound(0)) {
      return false;
    }
    localPhraseAffinityResponse.getAffinityScore(0, 0);
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.IcingContactLookup
 * JD-Core Version:    0.7.0.1
 */