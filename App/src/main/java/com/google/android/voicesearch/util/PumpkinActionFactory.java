package com.google.android.voicesearch.util;

import com.google.android.search.core.Feature;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.speech.embedded.TaggerResult;
import com.google.android.speech.exception.ResponseRecognizeException;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.android.voicesearch.fragments.action.CommunicationAction;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.majel.proto.ActionV2Protos.ActionContact;
import com.google.majel.proto.ActionV2Protos.ContactPhoneNumber;
import com.google.majel.proto.ContactProtos.ContactInformation;
import com.google.majel.proto.ContactProtos.ContactInformation.PhoneNumber;
import com.google.majel.proto.ContactProtos.ContactQuery;
import com.google.majel.proto.ContactProtos.ContactReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PumpkinActionFactory {
    private static void checkActionName(String paramString, TaggerResult paramTaggerResult)
            throws ResponseRecognizeException {
        if (!paramString.equals(paramTaggerResult.getActionName())) {
            throw new ResponseRecognizeException("Expected action " + paramString + " but got " + paramTaggerResult.getActionName());
        }
    }

    public static void checkPhoneAction(TaggerResult paramTaggerResult)
            throws ResponseRecognizeException {
        if ("CallNumber".equals(paramTaggerResult.getActionName())) {
            Preconditions.checkNotNull(paramTaggerResult.getArgument("Number"));
            return;
        }
        checkActionName("CallContact", paramTaggerResult);
    }

    public static void checkSMSAction(TaggerResult paramTaggerResult)
            throws ResponseRecognizeException {
        checkActionName("SendTextToContact", paramTaggerResult);
    }

    static ActionV2Protos.ActionContact createContact(TaggerResult paramTaggerResult) {
        ActionV2Protos.ActionContact localActionContact = new ActionV2Protos.ActionContact();
        String str1 = paramTaggerResult.getArgument("Contact");
        String str2 = paramTaggerResult.getArgument("PhoneType");
        String str3 = paramTaggerResult.getArgument("Number");
        if (str1 != null) {
            localActionContact.setName(str1);
        }
        if (str2 != null) {
            ActionV2Protos.ContactPhoneNumber localContactPhoneNumber1 = new ActionV2Protos.ContactPhoneNumber();
            localContactPhoneNumber1.setType(str2.toLowerCase());
            localActionContact.addPhone(localContactPhoneNumber1);
        }
        if (str3 != null) {
            ActionV2Protos.ContactPhoneNumber localContactPhoneNumber2 = new ActionV2Protos.ContactPhoneNumber();
            localContactPhoneNumber2.setNumber(str3);
            localActionContact.addPhone(localContactPhoneNumber2);
        }
        return localActionContact;
    }

    static ContactProtos.ContactReference createContactReference(TaggerResult paramTaggerResult) {
        String str1 = paramTaggerResult.getArgument("Contact");
        String str2 = paramTaggerResult.getArgument("PhoneType");
        String str3 = paramTaggerResult.getArgument("Number");
        if (str1 != null) {
            ContactProtos.ContactReference localContactReference1 = new ContactProtos.ContactReference();
            localContactReference1.setName(str1);
            localContactReference1.setIsRelationship(VelvetServices.get().getRelationshipNameLookup().isRelationshipName(str1));
            localContactReference1.setCanonicalRelationshipName(VelvetServices.get().getRelationshipNameLookup().getCanonicalRelationshipName(str1));
            ContactProtos.ContactQuery localContactQuery = new ContactProtos.ContactQuery();
            localContactQuery.addName(str1);
            localContactQuery.addContactMethod(1);
            if (str2 != null) {
                String str4 = PhoneActionUtils.typeStringToAndroidTypeColumn(ContactLookup.Mode.PHONE_NUMBER, str2);
                if (str4 != null) {
                    localContactQuery.setContactType(PhoneActionUtils.androidTypeColumnToContactType(ContactLookup.Mode.PHONE_NUMBER, Integer.valueOf(str4).intValue()));
                }
            }
            localContactReference1.setContactQuery(localContactQuery);
            return localContactReference1;
        }
        if (str3 != null) {
            ContactProtos.ContactInformation localContactInformation = new ContactProtos.ContactInformation();
            localContactInformation.addPhoneNumber(new ContactProtos.ContactInformation.PhoneNumber().setValue(str3));
            ContactProtos.ContactReference localContactReference2 = new ContactProtos.ContactReference();
            localContactReference2.addContactInformation(localContactInformation);
            return localContactReference2;
        }
        return null;
    }

    public static PersonDisambiguation createPersonDisambiguation(TaggerResult paramTaggerResult, ContactLookup paramContactLookup, String paramString, Supplier<DiscourseContext> paramSupplier) {
        if (Feature.CONTACT_REFERENCE.isEnabled()) {
            ContactProtos.ContactReference localContactReference = createContactReference(paramTaggerResult);
            if (localContactReference != null) {
                return PhoneActionUtils.createPersonDisambiguationContactReference(paramContactLookup, ContactLookup.Mode.PHONE_NUMBER, localContactReference, paramSupplier);
            }
            return null;
        }
        ContactSelectMode localContactSelectMode = ContactSelectMode.CALL_CONTACT;
        ActionV2Protos.ActionContact[] arrayOfActionContact = new ActionV2Protos.ActionContact[1];
        arrayOfActionContact[0] = createContact(paramTaggerResult);
        return PhoneActionUtils.createPhoneCallPersonDisambiguation(paramContactLookup, localContactSelectMode, paramString, Lists.newArrayList(arrayOfActionContact));
    }

    public static int getActionTypeLog(@Nullable DiscourseContext paramDiscourseContext, @Nonnull TaggerResult paramTaggerResult) {
        String str = paramTaggerResult.getActionName();
        int i;
        if ("CallContact".equals(str)) {
            i = 10;
        }
        CommunicationAction localCommunicationAction;
        do {
            do {
                boolean bool3;
                do {
                    boolean bool2;
                    do {
                        boolean bool1;
                        do {
                            return i;
                            if ("CallNumber".equals(str)) {
                                return 28;
                            }
                            if ("OpenApp".equals(str)) {
                                return 3;
                            }
                            if ("SendTextToContact".equals(str)) {
                                return 1;
                            }
                            bool1 = "Undo".equals(str);
                            i = 0;
                        } while (bool1);
                        bool2 = "Redo".equals(str);
                        i = 0;
                    } while (bool2);
                    bool3 = "Cancel".equals(str);
                    i = 0;
                } while (bool3);
                if ((!"Selection".equals(str)) && (!"SelectRecipient".equals(str))) {
                    break;
                }
                i = 0;
            } while (paramDiscourseContext == null);
            localCommunicationAction = paramDiscourseContext.getCurrentCommunicationAction();
            i = 0;
        } while (localCommunicationAction == null);
        return localCommunicationAction.getActionTypeLog();
        VelvetStrictMode.logW("ActionV2Factory", "Unknown action: " + str);
        return 0;
    }

    public static boolean isFollowOn(@Nonnull TaggerResult paramTaggerResult) {
        String str = paramTaggerResult.getActionName();
        return ("Undo".equals(str)) || ("Redo".equals(str)) || ("Selection".equals(str)) || ("Cancel".equals(str));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.util.PumpkinActionFactory

 * JD-Core Version:    0.7.0.1

 */