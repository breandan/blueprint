package com.google.android.velvet.actions;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.SearchError;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.search.shared.api.Query;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.velvet.ActionData;
import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.android.voicesearch.fragments.action.AddEventAction;
import com.google.android.voicesearch.fragments.action.AgendaAction;
import com.google.android.voicesearch.fragments.action.CancelAction;
import com.google.android.voicesearch.fragments.action.CommunicationAction;
import com.google.android.voicesearch.fragments.action.EmailAction;
import com.google.android.voicesearch.fragments.action.HelpAction;
import com.google.android.voicesearch.fragments.action.LocalResultsAction;
import com.google.android.voicesearch.fragments.action.OpenUrlAction;
import com.google.android.voicesearch.fragments.action.PhoneCallAction;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.android.voicesearch.fragments.action.PuntAction;
import com.google.android.voicesearch.fragments.action.ReadNotificationAction;
import com.google.android.voicesearch.fragments.action.SelfNoteAction;
import com.google.android.voicesearch.fragments.action.SetAlarmAction;
import com.google.android.voicesearch.fragments.action.SetReminderAction;
import com.google.android.voicesearch.fragments.action.ShowContactInformationAction;
import com.google.android.voicesearch.fragments.action.SmsAction;
import com.google.android.voicesearch.fragments.action.SocialUpdateAction;
import com.google.android.voicesearch.fragments.action.StopNavigationAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.fragments.action.VoiceActionVisitor;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.majel.proto.CalendarProtos.AgendaItem;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResults;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.Nullable;

public class CardDecisionFactory {
    private final Supplier<DiscourseContext> mDiscourseContext;
    private final boolean mFollowOnEnabled;
    private final GsaConfigFlags mGsaConfigFlags;
    private final NetworkInformation mNetworkInfo;
    private final Resources mResources;
    private final long mVelvetCountDownDurationMs;

    public CardDecisionFactory(Resources paramResources, GsaConfigFlags paramGsaConfigFlags, Supplier<DiscourseContext> paramSupplier, long paramLong, boolean paramBoolean, NetworkInformation paramNetworkInformation) {
        this.mResources = paramResources;
        this.mGsaConfigFlags = paramGsaConfigFlags;
        this.mDiscourseContext = paramSupplier;
        this.mVelvetCountDownDurationMs = paramLong;
        this.mFollowOnEnabled = paramBoolean;
        this.mNetworkInfo = paramNetworkInformation;
    }

    private static int getPromptedFieldUpdate(VoiceAction paramVoiceAction, ActionData paramActionData) {
        boolean bool1 = paramActionData.hasActionV2(0);
        int i = 0;
        if (bool1) {
            boolean bool2 = paramActionData.getActionV2(0).hasInteractionInfo();
            i = 0;
            if (bool2) {
                i = paramActionData.getActionV2(0).getInteractionInfo().getPromptedField();
            }
        }
        if (((paramVoiceAction instanceof CommunicationAction)) && (i == 2)) {
            PersonDisambiguation localPersonDisambiguation = ((CommunicationAction) paramVoiceAction).getRecipient();
            if ((localPersonDisambiguation != null) && (localPersonDisambiguation.isCompleted())) {
                i = 4;
            }
        }
        return i;
    }

    public boolean isFollowOnEnabledForRequest(Query paramQuery) {
        return (this.mFollowOnEnabled) && (this.mNetworkInfo.isConnected()) && (paramQuery != null) && (paramQuery.isVoiceSearch());
    }

    public CardDecision makeDecision(VoiceAction paramVoiceAction, ActionData paramActionData, @Nullable Query paramQuery) {
        int i = paramActionData.getNumberOfAttempts();
        if (i == 0) {
            i = ((DiscourseContext) this.mDiscourseContext.get()).getNumberOfAttempts(paramVoiceAction);
        }
        CardDecision localCardDecision = (CardDecision) paramVoiceAction.accept(new CardDecisionFactoryVisitor(paramActionData.shouldAutoExecute(), i, paramQuery));
        if ((!localCardDecision.shouldOverrideNetworkDecision()) && (paramActionData.isNetworkAction())) {
            localCardDecision = new CardDecision(paramActionData, getPromptedFieldUpdate(paramVoiceAction, paramActionData));
        }
        return localCardDecision;
    }

    private class CardDecisionFactoryVisitor
            implements VoiceActionVisitor<CardDecision> {
        private final boolean mConfirmed;
        private final boolean mFollowOnEnabledForRequest;
        private final int mNumberOfAttempts;
        @Nullable
        private Query mQuery;

        CardDecisionFactoryVisitor(boolean paramBoolean, int paramInt, @Nullable Query paramQuery) {
            this.mConfirmed = paramBoolean;
            this.mNumberOfAttempts = paramInt;
            this.mQuery = paramQuery;
            this.mFollowOnEnabledForRequest = CardDecisionFactory.this.isFollowOnEnabledForRequest(paramQuery);
        }

        private String createForContactDisambig(Person paramPerson, ContactSelectMode paramContactSelectMode, TtsConstants.DisambiguationConstants paramDisambiguationConstants, boolean paramBoolean) {
            List localList = paramPerson.denormalizeContacts(paramContactSelectMode.getContactLookupMode());
            if (localList.size() > 1) {
            }
            LinkedHashSet localLinkedHashSet;
            for (boolean bool = true; ; bool = false) {
                Preconditions.checkState(bool);
                localLinkedHashSet = Sets.newLinkedHashSet();
                Iterator localIterator1 = localList.iterator();
                while (localIterator1.hasNext()) {
                    localLinkedHashSet.add(((Contact) localIterator1.next()).getLabel(CardDecisionFactory.this.mResources));
                }
            }
            int i = localLinkedHashSet.size();
            if (i == 1) {
                Resources localResources2 = CardDecisionFactory.this.mResources;
                if (paramBoolean) {
                }
                for (int k = paramDisambiguationConstants.singleTypeDisambig.getTts(this.mNumberOfAttempts); ; k = paramDisambiguationConstants.singleTypeDisambig.getDisplayText()) {
                    return localResources2.getString(k);
                }
            }
            ArrayList localArrayList = Lists.newArrayListWithCapacity(i - 1);
            Object localObject = null;
            Iterator localIterator2 = localLinkedHashSet.iterator();
            while (localIterator2.hasNext()) {
                CharSequence localCharSequence = (CharSequence) localIterator2.next();
                if (localArrayList.size() == i - 1) {
                    localObject = localCharSequence;
                } else {
                    localArrayList.add(localCharSequence);
                }
            }
            Resources localResources1 = CardDecisionFactory.this.mResources;
            if (paramBoolean) {
            }
            for (int j = paramDisambiguationConstants.typeDisambig.getTts(this.mNumberOfAttempts); ; j = paramDisambiguationConstants.typeDisambig.getDisplayText()) {
                Object[] arrayOfObject = new Object[2];
                arrayOfObject[0] = TextUtils.join(", ", localArrayList);
                arrayOfObject[1] = localObject;
                return localResources1.getString(j, arrayOfObject);
            }
        }

        private CardDecision makeConfirmationDecision(TtsConstants paramTtsConstants) {
            return makeDecision(paramTtsConstants, 1);
        }

        private CardDecision makeDecision(TtsConstants paramTtsConstants, int paramInt) {
            CardDecision.Builder localBuilder = CardDecision.prompt(CardDecisionFactory.this.mResources.getString(paramTtsConstants.getDisplayText()), CardDecisionFactory.this.mResources.getString(paramTtsConstants.getTts(this.mNumberOfAttempts)), paramInt).overrideNetworkDecision();
            if (paramTtsConstants.shouldStartFollowOn(this.mNumberOfAttempts)) {
                localBuilder.startFollowOn();
            }
            return localBuilder.build();
        }

        private CardDecision makeNoMatchDecision(TtsConstants paramTtsConstants) {
            return makeDecision(paramTtsConstants, 2);
        }

        CardDecision makeDecision(Disambiguation<Person> paramDisambiguation, ContactSelectMode paramContactSelectMode, TtsConstants.DisambiguationConstants paramDisambiguationConstants) {
            return makeDecision(paramDisambiguation, paramContactSelectMode, paramDisambiguationConstants, this.mFollowOnEnabledForRequest);
        }

        CardDecision makeDecision(Disambiguation<Person> paramDisambiguation, ContactSelectMode paramContactSelectMode, TtsConstants.DisambiguationConstants paramDisambiguationConstants, boolean paramBoolean) {
            Preconditions.checkArgument(paramDisambiguation.isOngoing());
            Preconditions.checkNotNull(paramContactSelectMode);
            List localList = paramDisambiguation.getCandidates();
            int i = localList.size();
            if (i == 1) {
            }
            Resources localResources1;
            int j;
            Object[] arrayOfObject1;
            for (String str1 = createForContactDisambig((Person) localList.get(0), paramContactSelectMode, paramDisambiguationConstants, false); !paramBoolean; str1 = localResources1.getString(j, arrayOfObject1)) {
                return CardDecision.visualPrompt(str1, 2).overrideNetworkDecision().build();
                localResources1 = CardDecisionFactory.this.mResources;
                j = paramDisambiguationConstants.personDisambig.getDisplayText();
                arrayOfObject1 = new Object[1];
                arrayOfObject1[0] = paramDisambiguation.getFormattedTitle();
            }
            String str2;
            CardDecision.Builder localBuilder;
            if (i == 1) {
                str2 = createForContactDisambig((Person) localList.get(0), paramContactSelectMode, paramDisambiguationConstants, true);
                localBuilder = CardDecision.prompt(str1, str2, 2).overrideNetworkDecision();
                if (i != 1) {
                    break label238;
                }
                if (!paramDisambiguationConstants.typeDisambig.shouldStartFollowOn(this.mNumberOfAttempts)) {
                }
            }
            for (; ; ) {
                localBuilder.startFollowOn();
                label238:
                do {
                    return localBuilder.build();
                    Resources localResources2 = CardDecisionFactory.this.mResources;
                    int k = paramDisambiguationConstants.personDisambig.getTts(this.mNumberOfAttempts);
                    Object[] arrayOfObject2 = new Object[1];
                    arrayOfObject2[0] = paramDisambiguation.getFormattedTitle();
                    str2 = localResources2.getString(k, arrayOfObject2);
                    break;
                }
                while (!paramDisambiguationConstants.personDisambig.shouldStartFollowOn(this.mNumberOfAttempts));
            }
        }

        CardDecision makeVisualPromptOnly(CommunicationAction paramCommunicationAction, ContactSelectMode paramContactSelectMode, TtsConstants.DisambiguationConstants paramDisambiguationConstants) {
            PersonDisambiguation localPersonDisambiguation = paramCommunicationAction.getRecipient();
            if ((localPersonDisambiguation != null) && (localPersonDisambiguation.isOngoing())) {
                return makeDecision(localPersonDisambiguation, paramContactSelectMode, paramDisambiguationConstants, false);
            }
            if ((paramContactSelectMode == ContactSelectMode.CALL_CONTACT) && (localPersonDisambiguation != null) && (localPersonDisambiguation.isCompleted()) && (!localPersonDisambiguation.isSelectedByUser())) {
                return CardDecision.noPrompt().autoExecute(CardDecisionFactory.this.mVelvetCountDownDurationMs).build();
            }
            if ((localPersonDisambiguation != null) && (localPersonDisambiguation.hasAlternativeCandidates())) {
                if ((paramContactSelectMode == ContactSelectMode.CALL_CONTACT) || (paramContactSelectMode == ContactSelectMode.SMS)) {
                    return CardDecision.visualPrompt(CardDecisionFactory.this.mResources.getString(2131363234), 0).overrideNetworkDecision().build();
                }
                if (paramContactSelectMode == ContactSelectMode.EMAIL) {
                    return CardDecision.visualPrompt(CardDecisionFactory.this.mResources.getString(2131363236), 0).overrideNetworkDecision().build();
                }
                return CardDecision.SUPPRESS_NETWORK_DECISION;
            }
            return CardDecision.SUPPRESS_NETWORK_DECISION;
        }

        public CardDecision visit(SearchError paramSearchError) {
            return CardDecision.SUPPRESS_NETWORK_DECISION;
        }

        public CardDecision visit(AddEventAction paramAddEventAction) {
            return CardDecision.NETWORK_DECISION;
        }

        public CardDecision visit(AgendaAction paramAgendaAction) {
            int i;
            String str;
            if (paramAgendaAction.getSortReverse()) {
                i = -1 + paramAgendaAction.getAgendaItems().size();
                switch (paramAgendaAction.getAgendaItems().size()) {
                    default:
                        str = ((CalendarProtos.AgendaItem) paramAgendaAction.getAgendaItems().get(i)).getTtsMultipleItemDescription();
                }
            }
            for (; ; ) {
                if (TextUtils.isEmpty(str)) {
                    Log.e("CardDecisionFactory", "Unexpected empty TTS string from AgendaAction");
                }
                return new CardDecision.Builder().vocalizedPrompt(str, 0).overrideNetworkDecision().build();
                i = 0;
                break;
                str = CardDecisionFactory.this.mResources.getString(2131363344);
                continue;
                str = ((CalendarProtos.AgendaItem) paramAgendaAction.getAgendaItems().get(i)).getTtsSingleItemDescription();
            }
        }

        public CardDecision visit(CancelAction paramCancelAction) {
            return CardDecision.SUPPRESS_NETWORK_DECISION;
        }

        public CardDecision visit(EmailAction paramEmailAction) {
            if (!this.mFollowOnEnabledForRequest) {
                return makeVisualPromptOnly(paramEmailAction, ContactSelectMode.EMAIL, TtsConstants.EMAIL_DISAMBIG);
            }
            PersonDisambiguation localPersonDisambiguation = paramEmailAction.getRecipient();
            if (localPersonDisambiguation == null) {
                return makeNoMatchDecision(TtsConstants.EMAIL_GET_NAME);
            }
            if (localPersonDisambiguation.hasNoResults()) {
                if (localPersonDisambiguation.hasAlternativeCandidates()) {
                    return CardDecision.prompt(CardDecisionFactory.this.mResources.getString(2131363236), CardDecisionFactory.this.mResources.getString(2131363237), 0).overrideNetworkDecision().build();
                }
                return makeNoMatchDecision(TtsConstants.EMAIL_GET_NAME);
            }
            if (!localPersonDisambiguation.isCompleted()) {
                return makeDecision(localPersonDisambiguation, ContactSelectMode.EMAIL, TtsConstants.EMAIL_DISAMBIG);
            }
            Preconditions.checkState(localPersonDisambiguation.isCompleted());
            String str1 = paramEmailAction.getSubject();
            String str2 = paramEmailAction.getBody();
            if ((TextUtils.isEmpty(str1)) && (TextUtils.isEmpty(str2))) {
                return CardDecision.prompt(CardDecisionFactory.this.mResources.getString(2131363261), CardDecisionFactory.this.mResources.getString(2131363262), 4).overrideNetworkDecision().startFollowOn().build();
            }
            if (!this.mConfirmed) {
                return makeConfirmationDecision(TtsConstants.EMAIL_CONFIRMATION);
            }
            return CardDecision.prompt(CardDecisionFactory.this.mResources.getString(2131363265), CardDecisionFactory.this.mResources.getString(2131363266), 1).overrideNetworkDecision().autoExecute(0L).build();
        }

        public CardDecision visit(HelpAction paramHelpAction) {
            return CardDecision.NETWORK_DECISION;
        }

        public CardDecision visit(LocalResultsAction paramLocalResultsAction) {
            int i = 1;
            EcoutezStructuredResponse.EcoutezLocalResults localEcoutezLocalResults = paramLocalResultsAction.getResults();
            if ((localEcoutezLocalResults.getLocalResultCount() == i) && (localEcoutezLocalResults.getActionType() == 4)) {
            }
            while ((i != 0) && (!paramLocalResultsAction.canExecute())) {
                return CardDecision.SUPPRESS_NETWORK_DECISION;
                i = 0;
            }
            return CardDecision.NETWORK_DECISION;
        }

        public CardDecision visit(OpenUrlAction paramOpenUrlAction) {
            return CardDecision.NETWORK_DECISION;
        }

        public CardDecision visit(PhoneCallAction paramPhoneCallAction) {
            if (!this.mFollowOnEnabledForRequest) {
                return makeVisualPromptOnly(paramPhoneCallAction, ContactSelectMode.CALL_CONTACT, TtsConstants.CALL_DISAMBIG);
            }
            PersonDisambiguation localPersonDisambiguation = paramPhoneCallAction.getRecipient();
            if ((localPersonDisambiguation == null) || (localPersonDisambiguation.hasNoResults())) {
                if ((localPersonDisambiguation != null) && (localPersonDisambiguation.hasAlternativeCandidates())) {
                    return CardDecision.prompt(CardDecisionFactory.this.mResources.getString(2131363234), CardDecisionFactory.this.mResources.getString(2131363235), 0).overrideNetworkDecision().build();
                }
                return makeNoMatchDecision(TtsConstants.CALL_GET_NAME);
            }
            ContactSelectMode localContactSelectMode = ContactSelectMode.CALL_NUMBER;
            if (!localPersonDisambiguation.isCompleted()) {
                return makeDecision(localPersonDisambiguation, localContactSelectMode, TtsConstants.CALL_DISAMBIG);
            }
            Preconditions.checkState(localPersonDisambiguation.isCompleted());
            Person localPerson = (Person) localPersonDisambiguation.get();
            String str1;
            CardDecision.Builder localBuilder1;
            label220:
            CardDecision.Builder localBuilder2;
            if (localPerson.hasName()) {
                String str2 = localPerson.getSelectedItem().getLabel(CardDecisionFactory.this.mResources);
                Resources localResources2 = CardDecisionFactory.this.mResources;
                Object[] arrayOfObject2 = new Object[2];
                arrayOfObject2[0] = localPerson.getName();
                arrayOfObject2[1] = str2;
                str1 = localResources2.getString(2131363243, arrayOfObject2);
                if (!localPersonDisambiguation.isSelectedByUser()) {
                    break label295;
                }
                localBuilder1 = CardDecision.visualPrompt(CardDecisionFactory.this.mResources.getString(2131363242), 1);
                localBuilder2 = localBuilder1.overrideNetworkDecision();
                if ((!localPersonDisambiguation.isSelectedByUser()) && (!Feature.CALL_NO_COUNTDOWN.isEnabled())) {
                    break label319;
                }
            }
            label295:
            label319:
            for (long l = 0L; ; l = CardDecisionFactory.this.mVelvetCountDownDurationMs) {
                return localBuilder2.autoExecute(l).build();
                Resources localResources1 = CardDecisionFactory.this.mResources;
                Object[] arrayOfObject1 = new Object[1];
                arrayOfObject1[0] = localPersonDisambiguation.getFormattedTitle();
                str1 = localResources1.getString(2131363244, arrayOfObject1);
                break;
                localBuilder1 = CardDecision.prompt(CardDecisionFactory.this.mResources.getString(2131363242), str1, 1);
                break label220;
            }
        }

        public CardDecision visit(PlayMediaAction paramPlayMediaAction) {
            if ((!paramPlayMediaAction.getActionV2().getIsFromSoundSearch()) && (paramPlayMediaAction.getApps() != null) && (paramPlayMediaAction.getSelectedApp() == paramPlayMediaAction.getPlayStoreLink())) {
                return CardDecision.SUPPRESS_NETWORK_DECISION;
            }
            if (!paramPlayMediaAction.isOpenAppAction()) {
                return CardDecision.NETWORK_DECISION;
            }
            if ((this.mQuery != null) && (this.mQuery.isVoiceSearch())) {
                if (CardDecisionFactory.this.mGsaConfigFlags.isPumpkinOpenAppFastEnabled()) {
                    return new CardDecision.Builder().prompt(CardDecisionFactory.this.mResources.getString(2131363465), CardDecisionFactory.this.mResources.getString(2131363255), 1).autoExecute(0L).build();
                }
                return new CardDecision.Builder().vocalizedPrompt(CardDecisionFactory.this.mResources.getString(2131363255), 1).autoExecute(CardDecisionFactory.this.mVelvetCountDownDurationMs).build();
            }
            return CardDecision.SUPPRESS_NETWORK_DECISION;
        }

        public CardDecision visit(PuntAction paramPuntAction) {
            return CardDecision.NETWORK_DECISION;
        }

        public CardDecision visit(ReadNotificationAction paramReadNotificationAction) {
            return new CardDecision.Builder().vocalizedPrompt(paramReadNotificationAction.getMessage(), 0).overrideNetworkDecision().build();
        }

        public CardDecision visit(SelfNoteAction paramSelfNoteAction) {
            return CardDecision.NETWORK_DECISION;
        }

        public CardDecision visit(SetAlarmAction paramSetAlarmAction) {
            return CardDecision.NETWORK_DECISION;
        }

        public CardDecision visit(SetReminderAction paramSetReminderAction) {
            return CardDecision.NETWORK_DECISION;
        }

        public CardDecision visit(ShowContactInformationAction paramShowContactInformationAction) {
            return makeVisualPromptOnly(paramShowContactInformationAction, ContactSelectMode.SHOW_CONTACT_INFO, TtsConstants.PERSON_DISAMBIG);
        }

        public CardDecision visit(SmsAction paramSmsAction) {
            if (!this.mFollowOnEnabledForRequest) {
                return makeVisualPromptOnly(paramSmsAction, ContactSelectMode.SMS, TtsConstants.SMS_DISAMBIG);
            }
            PersonDisambiguation localPersonDisambiguation = paramSmsAction.getRecipient();
            if (localPersonDisambiguation == null) {
                return makeNoMatchDecision(TtsConstants.SMS_GET_NAME);
            }
            if (localPersonDisambiguation.hasNoResults()) {
                if (localPersonDisambiguation.hasAlternativeCandidates()) {
                    return CardDecision.prompt(CardDecisionFactory.this.mResources.getString(2131363234), CardDecisionFactory.this.mResources.getString(2131363235), 0).overrideNetworkDecision().build();
                }
                return makeNoMatchDecision(TtsConstants.SMS_GET_NAME);
            }
            if (!localPersonDisambiguation.isCompleted()) {
                return makeDecision(localPersonDisambiguation, ContactSelectMode.SMS, TtsConstants.SMS_DISAMBIG);
            }
            Preconditions.checkState(localPersonDisambiguation.isCompleted());
            if (paramSmsAction.hasBody()) {
                if (!this.mConfirmed) {
                }
            }
            for (CardDecision.Builder localBuilder = CardDecision.prompt(CardDecisionFactory.this.mResources.getString(2131363253), CardDecisionFactory.this.mResources.getString(2131363254), 1).autoExecute(0L); ; localBuilder = CardDecision.prompt(CardDecisionFactory.this.mResources.getString(2131363249), CardDecisionFactory.this.mResources.getString(2131363250), 4).startFollowOn()) {
                return localBuilder.overrideNetworkDecision().build();
                return makeConfirmationDecision(TtsConstants.SMS_CONFIRMATION);
            }
        }

        public CardDecision visit(SocialUpdateAction paramSocialUpdateAction) {
            return CardDecision.NETWORK_DECISION;
        }

        public CardDecision visit(StopNavigationAction paramStopNavigationAction) {
            return CardDecision.NETWORK_DECISION;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.actions.CardDecisionFactory

 * JD-Core Version:    0.7.0.1

 */