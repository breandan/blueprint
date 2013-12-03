package com.google.android.voicesearch.util;

import com.google.android.search.core.SearchError;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.voicesearch.fragments.action.AddEventAction;
import com.google.android.voicesearch.fragments.action.AgendaAction;
import com.google.android.voicesearch.fragments.action.CancelAction;
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
import com.google.common.base.Function;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.EmailAction;
import com.google.majel.proto.ActionV2Protos.PhoneAction;
import com.google.majel.proto.ActionV2Protos.SMSAction;

import javax.annotation.Nullable;

public class VoiceActionToActionV2Converter
        implements VoiceActionVisitor<ActionV2Protos.ActionV2>, Function<VoiceAction, ActionV2Protos.ActionV2> {
    @Nullable
    public ActionV2Protos.ActionV2 apply(VoiceAction paramVoiceAction) {
        return (ActionV2Protos.ActionV2) paramVoiceAction.accept(this);
    }

    public ActionV2Protos.ActionV2 visit(SearchError paramSearchError) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(AddEventAction paramAddEventAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(AgendaAction paramAgendaAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(CancelAction paramCancelAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(EmailAction paramEmailAction) {
        ActionV2Protos.EmailAction localEmailAction = new ActionV2Protos.EmailAction();
        PersonDisambiguation localPersonDisambiguation = paramEmailAction.getRecipient();
        if (localPersonDisambiguation != null) {
            localEmailAction.addToCr(localPersonDisambiguation.toContactReference());
        }
        localEmailAction.setSubject(paramEmailAction.getSubject());
        localEmailAction.setBody(paramEmailAction.getBody());
        return new ActionV2Protos.ActionV2().setEmailActionExtension(localEmailAction);
    }

    public ActionV2Protos.ActionV2 visit(HelpAction paramHelpAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(LocalResultsAction paramLocalResultsAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(OpenUrlAction paramOpenUrlAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(PhoneCallAction paramPhoneCallAction) {
        ActionV2Protos.PhoneAction localPhoneAction = new ActionV2Protos.PhoneAction();
        PersonDisambiguation localPersonDisambiguation = paramPhoneCallAction.getRecipient();
        if (localPersonDisambiguation != null) {
            localPhoneAction.setContactCr(localPersonDisambiguation.toContactReference());
        }
        return new ActionV2Protos.ActionV2().setPhoneActionExtension(localPhoneAction);
    }

    public ActionV2Protos.ActionV2 visit(PlayMediaAction paramPlayMediaAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(PuntAction paramPuntAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(ReadNotificationAction paramReadNotificationAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(SelfNoteAction paramSelfNoteAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(SetAlarmAction paramSetAlarmAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(SetReminderAction paramSetReminderAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(ShowContactInformationAction paramShowContactInformationAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(SmsAction paramSmsAction) {
        ActionV2Protos.SMSAction localSMSAction = new ActionV2Protos.SMSAction();
        PersonDisambiguation localPersonDisambiguation = paramSmsAction.getRecipient();
        if (localPersonDisambiguation != null) {
            localSMSAction.addRecipientCr(localPersonDisambiguation.toContactReference());
        }
        if (paramSmsAction.hasBody()) {
            localSMSAction.setMessageBody(paramSmsAction.getBody());
        }
        return new ActionV2Protos.ActionV2().setSMSActionExtension(localSMSAction);
    }

    public ActionV2Protos.ActionV2 visit(SocialUpdateAction paramSocialUpdateAction) {
        return null;
    }

    public ActionV2Protos.ActionV2 visit(StopNavigationAction paramStopNavigationAction) {
        return null;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.util.VoiceActionToActionV2Converter

 * JD-Core Version:    0.7.0.1

 */