package com.google.android.velvet.actions;

import android.content.res.Resources;
import android.util.Log;

import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.voicesearch.fragments.action.CommunicationAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

public class DisambiguationUtil {
    static RefinementResult getRefinedDisambiguation(Resources paramResources, PersonDisambiguation paramPersonDisambiguation, CommunicationAction paramCommunicationAction, String paramString1, String paramString2, String paramString3) {
        ContactLookup.Mode localMode = paramCommunicationAction.getSelectMode().getContactLookupMode();
        int i;
        if (paramString1 != null) {
            localPersonDisambiguation = getRefinedDisambiguationByName(paramPersonDisambiguation, localMode, paramString1);
            i = 134;
            if (paramString2 != null) {
                if (localPersonDisambiguation == null) {
                    break label120;
                }
            }
        }
        label120:
        for (PersonDisambiguation localPersonDisambiguation = getRefinedDisambiguationByType(paramResources, localPersonDisambiguation, localMode, paramString2); ; localPersonDisambiguation = getRefinedDisambiguationByType(paramResources, paramPersonDisambiguation, localMode, paramString2)) {
            i = 136;
            if ((localPersonDisambiguation == null) || ((localPersonDisambiguation.hasNoResults()) && (!localPersonDisambiguation.hasAlternativeCandidates()))) {
                break label134;
            }
            return new RefinementResult(localPersonDisambiguation, i, null);
            i = 0;
            localPersonDisambiguation = null;
            if (paramString3 == null) {
                break;
            }
            localPersonDisambiguation = getRefinedDisambiguationByPosition(paramResources, paramPersonDisambiguation, localMode, paramString3);
            i = 135;
            break;
        }
        label134:
        return new RefinementResult(paramPersonDisambiguation, 137, null);
    }

    static PersonDisambiguation getRefinedDisambiguationByName(PersonDisambiguation paramPersonDisambiguation, ContactLookup.Mode paramMode, String paramString) {
        List localList = paramPersonDisambiguation.getCandidates();
        ArrayList localArrayList = Lists.newArrayList();
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext()) {
            Person localPerson = (Person) localIterator.next();
            if ((localPerson.hasName()) && (localPerson.getName().toLowerCase().indexOf(paramString) != -1)) {
                localArrayList.add(localPerson);
            }
        }
        PersonDisambiguation localPersonDisambiguation = new PersonDisambiguation(paramMode);
        localPersonDisambiguation.setTitle(paramPersonDisambiguation.getTitle());
        localPersonDisambiguation.setCandidates(localArrayList, true);
        return localPersonDisambiguation;
    }

    static PersonDisambiguation getRefinedDisambiguationByPosition(Resources paramResources, PersonDisambiguation paramPersonDisambiguation, ContactLookup.Mode paramMode, String paramString) {
        int i;
        try {
            i = Integer.parseInt(paramString);
            if (i < 1) {
                Log.e("DisambiguationUtil", "Selection index is less than 1: " + i);
                return paramPersonDisambiguation;
            }
        } catch (NumberFormatException localNumberFormatException) {
            Log.e("DisambiguationUtil", "Selection string cannot be parsed as an integer: " + paramString);
            return paramPersonDisambiguation;
        }
        List localList1 = paramPersonDisambiguation.getCandidates();
        Person localPerson;
        if (localList1.size() == 1) {
            localPerson = (Person) localList1.get(0);
            List localList2 = localPerson.denormalizeContacts(paramMode);
            if (i > localList2.size()) {
                Log.e("DisambiguationUtil", "Selection index exceeds the number of contacts: " + i + "/" + localList2.size());
                return paramPersonDisambiguation;
            }
            localPerson.setSelectedItem((Contact) localList2.get(i - 1));
        }
        for (; ; ) {
            PersonDisambiguation localPersonDisambiguation = new PersonDisambiguation(paramMode);
            localPersonDisambiguation.setTitle(paramPersonDisambiguation.getTitle());
            localPersonDisambiguation.setCandidates(Lists.newArrayList(new Person[]{localPerson}), true);
            return localPersonDisambiguation;
            if (i > localList1.size()) {
                Log.e("DisambiguationUtil", "Selection index exceeds the number of persons: " + i + "/" + localList1.size());
                return paramPersonDisambiguation;
            }
            localPerson = (Person) localList1.get(i - 1);
        }
    }

    static PersonDisambiguation getRefinedDisambiguationByType(Resources paramResources, PersonDisambiguation paramPersonDisambiguation, ContactLookup.Mode paramMode, String paramString) {
        List localList = paramPersonDisambiguation.getCandidates();
        ArrayList localArrayList1 = Lists.newArrayList();
        ArrayList localArrayList2 = Lists.newArrayList();
        Iterator localIterator1 = localList.iterator();
        while (localIterator1.hasNext()) {
            Person localPerson = (Person) localIterator1.next();
            localArrayList2.clear();
            Iterator localIterator2 = localPerson.denormalizeContacts(paramMode).iterator();
            while (localIterator2.hasNext()) {
                Contact localContact = (Contact) localIterator2.next();
                String str = localContact.getLabel(paramResources);
                if ((str != null) && (paramString.equalsIgnoreCase(str))) {
                    localArrayList2.add(localContact);
                }
            }
            if (!localArrayList2.isEmpty()) {
                localArrayList1.add(Person.normalizeContactsForOnePerson(localArrayList2));
            }
        }
        PersonDisambiguation localPersonDisambiguation = new PersonDisambiguation(paramMode);
        localPersonDisambiguation.setTitle(paramPersonDisambiguation.getTitle());
        localPersonDisambiguation.setCandidates(localArrayList1, true);
        return localPersonDisambiguation;
    }

    @Nullable
    public static CommunicationAction getUpdatedCommunicationAction(CommunicationAction paramCommunicationAction, PersonDisambiguation paramPersonDisambiguation) {
        if ((paramCommunicationAction == null) || (paramPersonDisambiguation == null)) {
            return null;
        }
        return paramCommunicationAction.forNewRecipient(paramPersonDisambiguation);
    }

    @Nullable
    public static VoiceAction maybeDisambiguate(DiscourseContext paramDiscourseContext, Resources paramResources, String paramString1, String paramString2, String paramString3) {
        CommunicationAction localCommunicationAction;
        if (paramDiscourseContext == null) {
            localCommunicationAction = null;
            if (localCommunicationAction != null) {
                break label23;
            }
        }
        label23:
        PersonDisambiguation localPersonDisambiguation;
        do {
            return null;
            localCommunicationAction = paramDiscourseContext.getCurrentCommunicationAction();
            break;
            localPersonDisambiguation = localCommunicationAction.getRecipient();
        } while (localPersonDisambiguation == null);
        if (localPersonDisambiguation.isOngoing()) {
            RefinementResult localRefinementResult = getRefinedDisambiguation(paramResources, localPersonDisambiguation, localCommunicationAction, paramString1, paramString2, paramString3);
            localPersonDisambiguation = localRefinementResult.personDisambiguation;
            EventLogger.recordClientEventWithSource(localRefinementResult.clientEventType, 33554432, Integer.valueOf(localCommunicationAction.getActionTypeLog()));
        }
        return getUpdatedCommunicationAction(localCommunicationAction, localPersonDisambiguation);
    }

    static class RefinementResult {
        final int clientEventType;
        final PersonDisambiguation personDisambiguation;

        private RefinementResult(PersonDisambiguation paramPersonDisambiguation, int paramInt) {
            this.personDisambiguation = paramPersonDisambiguation;
            this.clientEventType = paramInt;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.actions.DisambiguationUtil

 * JD-Core Version:    0.7.0.1

 */