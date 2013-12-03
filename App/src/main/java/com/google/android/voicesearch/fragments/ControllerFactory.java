package com.google.android.voicesearch.fragments;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;

import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.search.core.SearchError;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.velvet.VelvetFactory;
import com.google.android.voicesearch.CardController;
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
import com.google.android.voicesearch.fragments.executor.ActionExecutor;
import com.google.android.voicesearch.fragments.executor.ActionExecutorFactory;
import com.google.android.voicesearch.fragments.reminders.EditReminderPresenter;
import com.google.android.voicesearch.fragments.reminders.MyPlacesSaver;
import com.google.android.voicesearch.fragments.reminders.ReminderSaver;
import com.google.android.voicesearch.util.CalendarTextHelper;

import java.util.concurrent.ExecutorService;

public class ControllerFactory {
    private CalendarTextHelper mCalendarTextHelper;
    private final Clock mClock;
    private final ContactLookup mContactLookup;
    private final Context mContext;
    private final DeviceCapabilityManager mDeviceCapabilityManager;
    private final EntryProvider mEntryProvider;
    private final ActionExecutorFactory mExecutorFactory;
    private final ExecutorService mExecutorService;
    private final NetworkClient mNetworkClient;
    private ReminderSaver mReminderSaver;
    private final Resources mResources;
    private final VelvetFactory mVelvetFactory;

    public ControllerFactory(VelvetFactory paramVelvetFactory, ContactLookup paramContactLookup, Context paramContext, ExecutorService paramExecutorService, DeviceCapabilityManager paramDeviceCapabilityManager, NetworkClient paramNetworkClient, EntryProvider paramEntryProvider, Clock paramClock, ActionExecutorFactory paramActionExecutorFactory) {
        this.mVelvetFactory = paramVelvetFactory;
        this.mContactLookup = paramContactLookup;
        this.mContext = paramContext;
        this.mExecutorService = paramExecutorService;
        this.mDeviceCapabilityManager = paramDeviceCapabilityManager;
        this.mNetworkClient = paramNetworkClient;
        this.mEntryProvider = paramEntryProvider;
        this.mClock = paramClock;
        this.mExecutorFactory = paramActionExecutorFactory;
        this.mResources = paramContext.getResources();
    }

    private EditReminderPresenter createEditReminderPresenter(SetReminderAction paramSetReminderAction) {
        return new EditReminderPresenter(getReminderSaver(), this.mNetworkClient, new MyPlacesSaver(this.mContext, this.mNetworkClient, this.mEntryProvider, this.mClock), paramSetReminderAction);
    }

    private CalendarTextHelper getCalendarTextHelper() {
        if (this.mCalendarTextHelper == null) {
            this.mCalendarTextHelper = new CalendarTextHelper(this.mContext);
        }
        return this.mCalendarTextHelper;
    }

    private ReminderSaver getReminderSaver() {
        if (this.mReminderSaver == null) {
            this.mReminderSaver = this.mVelvetFactory.createReminderSaver();
        }
        return this.mReminderSaver;
    }

    public <T extends VoiceAction> AbstractCardController<T, ?> createController(T paramT, CardController paramCardController) {
        AbstractCardController localAbstractCardController = (AbstractCardController) paramT.accept(new ControllerFactoryVisitor(paramCardController, null));
        localAbstractCardController.setActionExecutor((ActionExecutor) paramT.accept(this.mExecutorFactory));
        return localAbstractCardController;
    }

    private class ControllerFactoryVisitor
            implements VoiceActionVisitor<AbstractCardController<?, ?>> {
        private final CardController mCardController;

        private ControllerFactoryVisitor(CardController paramCardController) {
            this.mCardController = paramCardController;
        }

        public AbstractCardController<SearchError, ?> visit(SearchError paramSearchError) {
            return new ErrorController(this.mCardController);
        }

        public AbstractCardController<AddEventAction, ?> visit(AddEventAction paramAddEventAction) {
            return new CalendarEventController(this.mCardController, ControllerFactory.this.getCalendarTextHelper());
        }

        public AbstractCardController<AgendaAction, ?> visit(AgendaAction paramAgendaAction) {
            return new AgendaController(this.mCardController);
        }

        public AbstractCardController<CancelAction, ?> visit(CancelAction paramCancelAction) {
            return new CancelController(this.mCardController);
        }

        public AbstractCardController<EmailAction, ?> visit(EmailAction paramEmailAction) {
            return new EmailController(this.mCardController);
        }

        public AbstractCardController<HelpAction, ?> visit(HelpAction paramHelpAction) {
            return new HelpController(this.mCardController, DateFormat.is24HourFormat(ControllerFactory.this.mContext));
        }

        public AbstractCardController<LocalResultsAction, ?> visit(LocalResultsAction paramLocalResultsAction) {
            return new LocalResultsController(this.mCardController);
        }

        public AbstractCardController<OpenUrlAction, ?> visit(OpenUrlAction paramOpenUrlAction) {
            return new OpenUrlController(this.mCardController);
        }

        public AbstractCardController<PhoneCallAction, ?> visit(PhoneCallAction paramPhoneCallAction) {
            return new PhoneCallController(this.mCardController);
        }

        public AbstractCardController<PlayMediaAction, ?> visit(PlayMediaAction paramPlayMediaAction) {
            if (paramPlayMediaAction.isPlayMovieAction()) {
                return new PlayMovieController(this.mCardController);
            }
            if (paramPlayMediaAction.isOpenBookAction()) {
                return new OpenBookController(this.mCardController);
            }
            if (paramPlayMediaAction.isPlayMusicAction()) {
                return new PlayMusicController(this.mCardController);
            }
            if (paramPlayMediaAction.isOpenAppAction()) {
                return new OpenAppPlayMediaController(this.mCardController);
            }
            throw new IllegalStateException();
        }

        public AbstractCardController<PuntAction, ?> visit(PuntAction paramPuntAction) {
            return new PuntController(this.mCardController);
        }

        public AbstractCardController<ReadNotificationAction, ?> visit(ReadNotificationAction paramReadNotificationAction) {
            return new ReadNotificationController(this.mCardController);
        }

        public AbstractCardController<SelfNoteAction, ?> visit(SelfNoteAction paramSelfNoteAction) {
            return new SelfNoteController(this.mCardController);
        }

        public AbstractCardController<SetAlarmAction, ?> visit(SetAlarmAction paramSetAlarmAction) {
            return new SetAlarmController(this.mCardController, DateFormat.getTimeFormat(ControllerFactory.this.mContext));
        }

        public AbstractCardController<SetReminderAction, ?> visit(SetReminderAction paramSetReminderAction) {
            return new SetReminderController(this.mCardController, ControllerFactory.this.createEditReminderPresenter(paramSetReminderAction));
        }

        public AbstractCardController<ShowContactInformationAction, ?> visit(ShowContactInformationAction paramShowContactInformationAction) {
            return new ShowContactInformationController(this.mCardController, ControllerFactory.this.mContactLookup, ControllerFactory.this.mExecutorService, ControllerFactory.this.mResources.getString(2131363621), (ClipboardManager) ControllerFactory.this.mContext.getSystemService("clipboard"), ControllerFactory.this.mDeviceCapabilityManager);
        }

        public AbstractCardController<SmsAction, ?> visit(SmsAction paramSmsAction) {
            return new MessageEditorController(this.mCardController);
        }

        public AbstractCardController<SocialUpdateAction, ?> visit(SocialUpdateAction paramSocialUpdateAction) {
            return new SocialUpdateController(this.mCardController);
        }

        public AbstractCardController<StopNavigationAction, ?> visit(StopNavigationAction paramStopNavigationAction) {
            return new StopNavigationController(this.mCardController);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.ControllerFactory

 * JD-Core Version:    0.7.0.1

 */