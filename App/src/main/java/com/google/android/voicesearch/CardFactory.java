package com.google.android.voicesearch;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.google.android.search.core.SearchError;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.voicesearch.fragments.AbstractCardController;
import com.google.android.voicesearch.fragments.AbstractCardView;
import com.google.android.voicesearch.fragments.AgendaCard;
import com.google.android.voicesearch.fragments.AgendaController;
import com.google.android.voicesearch.fragments.CalendarEventCard;
import com.google.android.voicesearch.fragments.CalendarEventController;
import com.google.android.voicesearch.fragments.CancelButtonCard;
import com.google.android.voicesearch.fragments.CancelConfirmationCard;
import com.google.android.voicesearch.fragments.CancelController;
import com.google.android.voicesearch.fragments.EmailCard;
import com.google.android.voicesearch.fragments.EmailController;
import com.google.android.voicesearch.fragments.ErrorCard;
import com.google.android.voicesearch.fragments.ErrorController;
import com.google.android.voicesearch.fragments.HelpCard;
import com.google.android.voicesearch.fragments.LocalResultsCard;
import com.google.android.voicesearch.fragments.LocalResultsController;
import com.google.android.voicesearch.fragments.MessageEditorCard;
import com.google.android.voicesearch.fragments.MessageEditorController;
import com.google.android.voicesearch.fragments.OpenAppPlayMediaCard;
import com.google.android.voicesearch.fragments.OpenBookCard;
import com.google.android.voicesearch.fragments.OpenUrlCard;
import com.google.android.voicesearch.fragments.OpenUrlController;
import com.google.android.voicesearch.fragments.PhoneCallCard;
import com.google.android.voicesearch.fragments.PhoneCallController;
import com.google.android.voicesearch.fragments.PhoneCallNumberCard;
import com.google.android.voicesearch.fragments.PlayMovieCard;
import com.google.android.voicesearch.fragments.PlayMusicCard;
import com.google.android.voicesearch.fragments.PuntCard;
import com.google.android.voicesearch.fragments.PuntController;
import com.google.android.voicesearch.fragments.ReadNotificationCard;
import com.google.android.voicesearch.fragments.ReadNotificationController;
import com.google.android.voicesearch.fragments.SelfNoteCard;
import com.google.android.voicesearch.fragments.SelfNoteController;
import com.google.android.voicesearch.fragments.SetAlarmCard;
import com.google.android.voicesearch.fragments.SetAlarmController;
import com.google.android.voicesearch.fragments.SetReminderCard;
import com.google.android.voicesearch.fragments.SetReminderController;
import com.google.android.voicesearch.fragments.ShowContactInformationCard;
import com.google.android.voicesearch.fragments.ShowContactInformationController;
import com.google.android.voicesearch.fragments.SocialUpdateCard;
import com.google.android.voicesearch.fragments.SocialUpdateController;
import com.google.android.voicesearch.fragments.StopNavigationCard;
import com.google.android.voicesearch.fragments.StopNavigationController;
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

public class CardFactory
        implements VoiceActionVisitor<AbstractCardView<?>> {
    private final Context mContextWrapper;

    public CardFactory(Context paramContext) {
        this.mContextWrapper = new ContextThemeWrapper(paramContext, 2131624107);
    }

    private static <T extends AbstractCardController<?, ?>> AbstractCardView<T> setController(AbstractCardView<T> paramAbstractCardView, T paramT) {
        paramAbstractCardView.setController(paramT);
        return paramAbstractCardView;
    }

    public <T extends VoiceAction, C extends AbstractCardController<T, ?>> AbstractCardView<C> createCard(C paramC) {
        return setController((AbstractCardView) paramC.getVoiceAction().accept(this), paramC);
    }

    public AbstractCardView<ErrorController> visit(SearchError paramSearchError) {
        return new ErrorCard(this.mContextWrapper);
    }

    public AbstractCardView<CalendarEventController> visit(AddEventAction paramAddEventAction) {
        return new CalendarEventCard(this.mContextWrapper);
    }

    public AbstractCardView<AgendaController> visit(AgendaAction paramAgendaAction) {
        return new AgendaCard(this.mContextWrapper);
    }

    public AbstractCardView<CancelController> visit(CancelAction paramCancelAction) {
        if (paramCancelAction.isCancelButton()) {
            return new CancelButtonCard(this.mContextWrapper);
        }
        return new CancelConfirmationCard(this.mContextWrapper);
    }

    public AbstractCardView<EmailController> visit(EmailAction paramEmailAction) {
        return new EmailCard(this.mContextWrapper);
    }

    public AbstractCardView<?> visit(HelpAction paramHelpAction) {
        return new HelpCard(this.mContextWrapper);
    }

    public AbstractCardView<LocalResultsController> visit(LocalResultsAction paramLocalResultsAction) {
        return new LocalResultsCard(this.mContextWrapper);
    }

    public AbstractCardView<OpenUrlController> visit(OpenUrlAction paramOpenUrlAction) {
        return new OpenUrlCard(this.mContextWrapper);
    }

    public AbstractCardView<PhoneCallController> visit(PhoneCallAction paramPhoneCallAction) {
        PersonDisambiguation localPersonDisambiguation = paramPhoneCallAction.getRecipient();
        if ((localPersonDisambiguation != null) && (localPersonDisambiguation.isCompleted()) && (!((Person) localPersonDisambiguation.get()).hasName())) {
            return new PhoneCallNumberCard(this.mContextWrapper);
        }
        return new PhoneCallCard(this.mContextWrapper);
    }

    public AbstractCardView<?> visit(PlayMediaAction paramPlayMediaAction) {
        if (paramPlayMediaAction.isPlayMovieAction()) {
            return new PlayMovieCard(this.mContextWrapper);
        }
        if (paramPlayMediaAction.isOpenBookAction()) {
            return new OpenBookCard(this.mContextWrapper);
        }
        if (paramPlayMediaAction.isPlayMusicAction()) {
            return new PlayMusicCard(this.mContextWrapper);
        }
        if (paramPlayMediaAction.isOpenAppAction()) {
            return new OpenAppPlayMediaCard(this.mContextWrapper);
        }
        throw new IllegalStateException();
    }

    public AbstractCardView<PuntController> visit(PuntAction paramPuntAction) {
        return new PuntCard(this.mContextWrapper);
    }

    public AbstractCardView<ReadNotificationController> visit(ReadNotificationAction paramReadNotificationAction) {
        return new ReadNotificationCard(this.mContextWrapper);
    }

    public AbstractCardView<SelfNoteController> visit(SelfNoteAction paramSelfNoteAction) {
        return new SelfNoteCard(this.mContextWrapper);
    }

    public AbstractCardView<SetAlarmController> visit(SetAlarmAction paramSetAlarmAction) {
        return new SetAlarmCard(this.mContextWrapper);
    }

    public AbstractCardView<SetReminderController> visit(SetReminderAction paramSetReminderAction) {
        return new SetReminderCard(this.mContextWrapper);
    }

    public AbstractCardView<ShowContactInformationController> visit(ShowContactInformationAction paramShowContactInformationAction) {
        return new ShowContactInformationCard(this.mContextWrapper);
    }

    public AbstractCardView<MessageEditorController> visit(SmsAction paramSmsAction) {
        return new MessageEditorCard(this.mContextWrapper);
    }

    public AbstractCardView<SocialUpdateController> visit(SocialUpdateAction paramSocialUpdateAction) {
        return new SocialUpdateCard(this.mContextWrapper);
    }

    public AbstractCardView<StopNavigationController> visit(StopNavigationAction paramStopNavigationAction) {
        return new StopNavigationCard(this.mContextWrapper);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.CardFactory

 * JD-Core Version:    0.7.0.1

 */