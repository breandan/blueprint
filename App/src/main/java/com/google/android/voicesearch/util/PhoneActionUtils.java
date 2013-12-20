package com.google.android.voicesearch.util;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.search.core.Feature;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.speech.contacts.ActionContactLookupSupplier;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.ContactReferenceLookupSupplier;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.speech.contacts.PersonMergeStrategy;
import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.android.voicesearch.fragments.action.CommunicationAction;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.majel.proto.ActionV2Protos.ActionContact;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.ContactEmail;
import com.google.majel.proto.ActionV2Protos.ContactPhoneNumber;
import com.google.majel.proto.ActionV2Protos.ContactPostalAddress;
import com.google.majel.proto.ActionV2Protos.PhoneAction;
import com.google.majel.proto.ContactProtos.ContactInformation;
import com.google.majel.proto.ContactProtos.ContactInformation.PhoneNumber;
import com.google.majel.proto.ContactProtos.ContactReference;
import com.google.majel.proto.ContactProtos.ContactType;
import com.google.majel.proto.MajelProtos;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.majel.proto.PeanutProtos.Peanut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

public class PhoneActionUtils {
    public static final String MESSAGE_DEFAULT_CONTACT_TYPE = String.valueOf(2);

    @Nullable
    public static ContactProtos.ContactType androidTypeColumnToContactType(ContactLookup.Mode paramMode, int paramInt) {
        if (paramInt == 0) {
        }
        do {
            return null;
            if (paramInt == paramMode.typeHome) {
                return new ContactProtos.ContactType().setType(1);
            }
            if (paramInt == paramMode.typeMain) {
                return new ContactProtos.ContactType().setType(4);
            }
            if (paramInt == paramMode.typeMobile) {
                return new ContactProtos.ContactType().setType(3);
            }
            if (paramInt == paramMode.typeOther) {
                return new ContactProtos.ContactType().setType(5);
            }
        } while (paramInt != paramMode.typeWork);
        return new ContactProtos.ContactType().setType(2);
    }

    public static int contactTypeToAndroidTypeColumn(ContactLookup.Mode paramMode, ContactProtos.ContactType paramContactType) {
        if ((paramContactType != null) && (paramContactType.hasType())) {
        }
        switch (paramContactType.getType()) {
            default:
                Log.w("PhoneActionUtils", "Unknown contact type: " + paramContactType.getType());
                return 0;
            case 3:
                return paramMode.typeMobile;
            case 1:
                return paramMode.typeHome;
            case 4:
                return paramMode.typeMain;
            case 5:
                return paramMode.typeOther;
        }
        return paramMode.typeWork;
    }

    public static boolean containsSameName(List<Person> paramList) {
        if (!paramList.isEmpty()) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkArgument(bool);
            if (paramList.size() != 1) {
                break;
            }
            return true;
        }
        String str = ((Person) paramList.get(0)).getName();
        int i = 1;
        label48:
        Person localPerson;
        if (i < paramList.size()) {
            localPerson = (Person) paramList.get(i);
            if ((str != null) || (localPerson.getName() != null)) {
                break label88;
            }
        }
        label88:
        do {
            i++;
            break label48;
            break;
            if ((str == null) || (localPerson.getName() == null)) {
                return false;
            }
        } while (str.equals(localPerson.getName()));
        return false;
    }

    @Nullable
    public static PersonDisambiguation createPersonDisambiguation(ContactLookup paramContactLookup, ContactSelectMode paramContactSelectMode, boolean paramBoolean, String paramString, List<ActionV2Protos.ActionContact> paramList) {
        if (paramList.isEmpty()) {
            return null;
        }
        if (paramBoolean) {
        }
        for (ContactSelectMode localContactSelectMode = ContactSelectMode.SHOW_CONTACT_INFO; ; localContactSelectMode = paramContactSelectMode) {
            PersonDisambiguation localPersonDisambiguation = new PersonDisambiguation(localContactSelectMode.getContactLookupMode());
            localPersonDisambiguation.setCandidates(new ActionContactLookupSupplier(paramContactLookup, paramContactSelectMode, paramList, paramString).get(), false);
            localPersonDisambiguation.setTitle(getContactName((ActionV2Protos.ActionContact) paramList.get(0)));
            return localPersonDisambiguation;
        }
    }

    @Nullable
    public static PersonDisambiguation createPersonDisambiguationContactReference(ContactLookup paramContactLookup, ContactLookup.Mode paramMode, ContactProtos.ContactReference paramContactReference, Supplier<DiscourseContext> paramSupplier) {
        Preconditions.checkState(Feature.CONTACT_REFERENCE.isEnabled());
        PersonDisambiguation localPersonDisambiguation1 = new PersonDisambiguation(paramMode);
        localPersonDisambiguation1.setIsRelationship(paramContactReference.getIsRelationship());
        localPersonDisambiguation1.setCanonicalRelationship(paramContactReference.getCanonicalRelationshipName());
        if (paramContactReference.getPlaceholderContact()) {
            if (Feature.DISCOURSE_CONTEXT.isEnabled()) {
                CommunicationAction localCommunicationAction = ((DiscourseContext) paramSupplier.get()).getCurrentCommunicationAction();
                if (localCommunicationAction != null) {
                    PersonDisambiguation localPersonDisambiguation2 = localCommunicationAction.getRecipient();
                    if (localPersonDisambiguation2 != null) {
                        return new PersonDisambiguation(localPersonDisambiguation2);
                    }
                }
            }
            return null;
        }
        if (isPhoneNumberContact(paramContactReference)) {
            String str = ((ContactProtos.ContactInformation.PhoneNumber) ((ContactProtos.ContactInformation) paramContactReference.getContactInformationList().get(0)).getPhoneNumberList().get(0)).getValue();
            Contact localContact = Contact.newPhoneNumberOnlyContact(str);
            localPersonDisambiguation1.setTitle(str);
            Person[] arrayOfPerson = new Person[1];
            arrayOfPerson[0] = Person.fromContact(localContact);
            localPersonDisambiguation1.setCandidates(Lists.newArrayList(arrayOfPerson), false);
            return localPersonDisambiguation1;
        }
        ArrayList localArrayList = Lists.newArrayList();
        Iterator localIterator = paramContactReference.getContactInformationList().iterator();
        while (localIterator.hasNext()) {
            Person localPerson = Person.fromContactInformation((ContactProtos.ContactInformation) localIterator.next());
            if (localPerson != null) {
                localArrayList.add(localPerson);
            }
        }
        PersonMergeStrategy[] arrayOfPersonMergeStrategy = new PersonMergeStrategy[4];
        arrayOfPersonMergeStrategy[0] = new PersonMergeStrategy.MergeByNameAndServerImageUri();
        arrayOfPersonMergeStrategy[1] = new PersonMergeStrategy.MergeByNameAndEmailAddress();
        arrayOfPersonMergeStrategy[2] = new PersonMergeStrategy.MergeByNameAndPhoneNumber();
        arrayOfPersonMergeStrategy[3] = new PersonMergeStrategy.MergeByNameAndOrthogonalContactType();
        List localList = Person.mergePersons(localArrayList, arrayOfPersonMergeStrategy);
        localPersonDisambiguation1.setTitle(paramContactReference.getName());
        if ((paramContactReference.hasContactQuery()) && (paramContactReference.getContactQuery().getNameCount() > 0)) {
            localPersonDisambiguation1.setCandidates(new ContactReferenceLookupSupplier(localList, paramContactLookup, paramContactReference.getContactQuery()).get(), false);
            return localPersonDisambiguation1;
        }
        localPersonDisambiguation1.setCandidates(localList, false);
        return localPersonDisambiguation1;
    }

    @Nullable
    public static PersonDisambiguation createPhoneCallPersonDisambiguation(ContactLookup paramContactLookup, ContactSelectMode paramContactSelectMode, String paramString, List<ActionV2Protos.ActionContact> paramList) {
        if ((!paramList.isEmpty()) && (isPhoneNumberAction((ActionV2Protos.ActionContact) paramList.get(0)))) {
            String str = getSpokenNumber((ActionV2Protos.ActionContact) paramList.get(0));
            PersonDisambiguation localPersonDisambiguation = new PersonDisambiguation(paramContactSelectMode.getContactLookupMode());
            localPersonDisambiguation.setTitle(((ActionV2Protos.ActionContact) paramList.get(0)).getName());
            Person[] arrayOfPerson = new Person[1];
            arrayOfPerson[0] = Person.fromContact(Contact.newPhoneNumberOnlyContact(str));
            localPersonDisambiguation.setCandidates(Lists.newArrayList(arrayOfPerson), false);
            return localPersonDisambiguation;
        }
        return createPersonDisambiguation(paramContactLookup, paramContactSelectMode, false, paramString, paramList);
    }

    @Nullable
    public static PersonDisambiguation createPhoneCallPersonDisambiguationActionV2(ActionV2Protos.ActionV2 paramActionV2, ContactLookup paramContactLookup) {
        ActionV2Protos.PhoneAction localPhoneAction = paramActionV2.getPhoneActionExtension();
        if ((paramActionV2.hasActionV2Extension()) && (paramActionV2.getActionV2Extension().hasPhoneActionExtension())) {
            mergePhoneAction(localPhoneAction, paramActionV2.getActionV2Extension().getPhoneActionExtension());
        }
        return createPhoneCallPersonDisambiguation(paramContactLookup, ContactSelectMode.CALL_CONTACT, null, localPhoneAction.getContactList());
    }

    public static String getAndClearContactMethodType(List<ActionV2Protos.ActionContact> paramList) {
        String str = null;
        Iterator localIterator1 = paramList.iterator();
        while (localIterator1.hasNext()) {
            ActionV2Protos.ActionContact localActionContact = (ActionV2Protos.ActionContact) localIterator1.next();
            Iterator localIterator2 = localActionContact.getPhoneList().iterator();
            while (localIterator2.hasNext()) {
                ActionV2Protos.ContactPhoneNumber localContactPhoneNumber = (ActionV2Protos.ContactPhoneNumber) localIterator2.next();
                if (localContactPhoneNumber.hasType()) {
                    if (str == null) {
                        str = localContactPhoneNumber.getType();
                    }
                    localContactPhoneNumber.clearType();
                }
            }
            Iterator localIterator3 = localActionContact.getEmailList().iterator();
            while (localIterator3.hasNext()) {
                ActionV2Protos.ContactEmail localContactEmail = (ActionV2Protos.ContactEmail) localIterator3.next();
                if (localContactEmail.hasType()) {
                    if (str == null) {
                        str = localContactEmail.getType();
                    }
                    localContactEmail.clearType();
                }
            }
            Iterator localIterator4 = localActionContact.getAddressList().iterator();
            while (localIterator4.hasNext()) {
                ActionV2Protos.ContactPostalAddress localContactPostalAddress = (ActionV2Protos.ContactPostalAddress) localIterator4.next();
                if (localContactPostalAddress.hasType()) {
                    if (str == null) {
                        str = localContactPostalAddress.getType();
                    }
                    localContactPostalAddress.clearType();
                }
            }
        }
        return str;
    }

    public static Intent getCallIntent(String paramString) {
        Intent localIntent = new Intent("android.intent.action.CALL");
        localIntent.setData(Uri.fromParts("tel", paramString, null));
        return localIntent;
    }

    public static String getContactName(ActionV2Protos.ActionContact paramActionContact) {
        if (paramActionContact.hasName()) {
            return paramActionContact.getName();
        }
        return paramActionContact.getParsedName();
    }

    public static String getContactName(ActionV2Protos.PhoneAction paramPhoneAction) {
        if (paramPhoneAction.getContactCount() > 0) {
            return getContactName(paramPhoneAction.getContact(0));
        }
        return null;
    }

    public static String getContactType(ContactLookup.Mode paramMode, ActionV2Protos.ActionContact paramActionContact) {
        if (paramActionContact.getPhoneCount() > 0) {
            return typeStringToAndroidTypeColumn(paramMode, paramActionContact.getPhone(0).getType());
        }
        return null;
    }

    public static Intent getEditPersonIntent(Person paramPerson) {
        return new Intent("android.intent.action.EDIT", paramPerson.getUri());
    }

    public static Intent getSendSmsIntent(@Nullable String paramString1, @Nullable String paramString2) {
        Intent localIntent = new Intent();
        localIntent.setAction("android.intent.action.SENDTO");
        String[] arrayOfString;
        if (paramString1 != null) {
            arrayOfString = new String[1];
            arrayOfString[0] = paramString1;
        }
        for (; ; ) {
            localIntent.setData(Uri.fromParts("smsto", TextUtils.join(",", arrayOfString), null));
            if (!TextUtils.isEmpty(paramString2)) {
                localIntent.putExtra("sms_body", paramString2);
            }
            return localIntent;
            arrayOfString = new String[0];
        }
    }

    public static Intent getShowPersonIntent(@Nullable Person paramPerson) {
        if (paramPerson != null) {
            return new Intent("android.intent.action.VIEW", paramPerson.getUri());
        }
        return Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_CONTACTS");
    }

    private static String getSpokenNumber(ActionV2Protos.ActionContact paramActionContact) {
        return paramActionContact.getPhone(0).getNumber();
    }

    public static String getSpokenNumber(ActionV2Protos.PhoneAction paramPhoneAction) {
        return getSpokenNumber(paramPhoneAction.getContact(0));
    }

    public static boolean isPhoneActionFromEmbeddedRecognizer(MajelProtos.MajelResponse paramMajelResponse) {
        int i = paramMajelResponse.getPeanutCount();
        boolean bool1 = false;
        if (i > 0) {
            PeanutProtos.Peanut localPeanut = paramMajelResponse.getPeanut(0);
            int j = localPeanut.getActionV2Count();
            bool1 = false;
            if (j > 0) {
                ActionV2Protos.ActionV2 localActionV2 = localPeanut.getActionV2(0);
                boolean bool2 = localActionV2.hasPhoneActionExtension();
                bool1 = false;
                if (bool2) {
                    ActionV2Protos.PhoneAction localPhoneAction = localActionV2.getPhoneActionExtension();
                    int k = localPhoneAction.getContactCount();
                    bool1 = false;
                    if (k > 0) {
                        bool1 = localPhoneAction.getContact(0).hasEmbeddedActionContactExtension();
                    }
                }
            }
        }
        return bool1;
    }

    public static boolean isPhoneNumberAction(ActionV2Protos.ActionContact paramActionContact) {
        return (!paramActionContact.hasName()) && (!paramActionContact.hasParsedName()) && (paramActionContact.getPhoneCount() == 1) && (paramActionContact.getPhone(0).hasNumber());
    }

    public static boolean isPhoneNumberAction(ActionV2Protos.PhoneAction paramPhoneAction) {
        boolean bool;
        if (paramPhoneAction.hasContactCr()) {
            bool = isPhoneNumberContact(paramPhoneAction.getContactCr());
        }
        int i;
        do {
            return bool;
            i = paramPhoneAction.getContactCount();
            bool = false;
        } while (i <= 0);
        return isPhoneNumberAction(paramPhoneAction.getContact(0));
    }

    public static boolean isPhoneNumberContact(ContactProtos.ContactReference paramContactReference) {
        if ((!paramContactReference.hasContactQuery()) && (paramContactReference.getContactInformationCount() == 1)) {
            ContactProtos.ContactInformation localContactInformation = (ContactProtos.ContactInformation) paramContactReference.getContactInformationList().get(0);
            if ((!localContactInformation.hasDisplayName()) && (!localContactInformation.hasClientEntityId())) {
                List localList = localContactInformation.getPhoneNumberList();
                return (localList.size() == 1) && (((ContactProtos.ContactInformation.PhoneNumber) localList.get(0)).hasValue());
            }
        }
        return false;
    }

    public static void mergePhoneAction(ActionV2Protos.PhoneAction paramPhoneAction1, ActionV2Protos.PhoneAction paramPhoneAction2) {
        HashMap localHashMap = Maps.newHashMap();
        Iterator localIterator1 = paramPhoneAction1.getContactList().iterator();
        while (localIterator1.hasNext()) {
            ActionV2Protos.ActionContact localActionContact3 = (ActionV2Protos.ActionContact) localIterator1.next();
            if (localActionContact3.hasName()) {
                localHashMap.put(localActionContact3.getName(), localActionContact3);
            }
        }
        Iterator localIterator2 = paramPhoneAction2.getContactList().iterator();
        while (localIterator2.hasNext()) {
            ActionV2Protos.ActionContact localActionContact1 = (ActionV2Protos.ActionContact) localIterator2.next();
            if (localActionContact1.hasName()) {
                String str = localActionContact1.getName();
                if (localHashMap.containsKey(str)) {
                    ActionV2Protos.ActionContact localActionContact2 = (ActionV2Protos.ActionContact) localHashMap.get(str);
                    if (localActionContact1.hasEmbeddedActionContactExtension()) {
                        localActionContact2.setEmbeddedActionContactExtension(localActionContact1.getEmbeddedActionContactExtension());
                    }
                    if (localActionContact2.hasEmbeddedActionContactExtension()) {
                        localActionContact2.getEmbeddedActionContactExtension().setMerged(true);
                    }
                } else {
                    paramPhoneAction1.getContactList().add(localActionContact1);
                }
            }
        }
    }

    public static String spaceOutDigits(String paramString) {
        Preconditions.checkNotNull(paramString);
        char[] arrayOfChar = paramString.toCharArray();
        StringBuilder localStringBuilder = new StringBuilder();
        int i = 0;
        int j = paramString.length();
        int k = 0;
        if (k < j) {
            char c = arrayOfChar[k];
            if (Character.isDigit(c)) {
                if (i != 0) {
                    localStringBuilder.append(" ");
                }
                i = 1;
                localStringBuilder.append(c);
            }
            for (; ; ) {
                k++;
                break;
                if (c == ' ') {
                    if (i != 0) {
                        localStringBuilder.append(",");
                    } else {
                        localStringBuilder.append(" ");
                    }
                } else {
                    localStringBuilder.append(c);
                    i = 0;
                }
            }
        }
        return localStringBuilder.toString();
    }

    public static String typeStringToAndroidTypeColumn(ContactLookup.Mode paramMode, String paramString) {
        if ("home".equals(paramString)) {
            return String.valueOf(paramMode.typeHome);
        }
        if ("work".equals(paramString)) {
            return String.valueOf(paramMode.typeWork);
        }
        if (("cell".equals(paramString)) || ("mobile".equals(paramString))) {
            return String.valueOf(paramMode.typeMobile);
        }
        if ("main".equals(paramString)) {
            return String.valueOf(paramMode.typeMain);
        }
        if ("other".equals(paramString)) {
            return String.valueOf(paramMode.typeOther);
        }
        return null;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.util.PhoneActionUtils

 * JD-Core Version:    0.7.0.1

 */