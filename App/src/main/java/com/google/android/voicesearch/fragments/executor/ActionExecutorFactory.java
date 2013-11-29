package com.google.android.voicesearch.fragments.executor;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.SearchError;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.speech.helper.AccountHelper;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.VelvetServices;
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
import com.google.android.voicesearch.util.AppSelectionHelper;
import com.google.android.voicesearch.util.CalendarHelper;
import com.google.android.voicesearch.util.CalendarTextHelper;
import com.google.android.voicesearch.util.EmailSender;
import com.google.android.voicesearch.util.PreferredApplicationsManager;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nonnull;

public class ActionExecutorFactory
  implements VoiceActionVisitor<ActionExecutor<?>>
{
  private final AccountHelper mAccountHelper;
  private AddEventActionExecutor mAddEventActionExecutor;
  private AppSelectionHelper mAppSelectionHelper;
  private CalendarHelper mCalendarHelper;
  private CalendarTextHelper mCalendarTextHelper;
  private final Context mContext;
  private EmailActionExecutor mEmailActionExecutor;
  private EmailSender mEmailSender;
  private final ExecutorService mExecutorService;
  private final IntentStarter mIntentStarter;
  private LocalResultsActionExecutor mLocalResultsActionExecutor;
  private final ScheduledSingleThreadedExecutor mMainThreadExecutor;
  private NoOpExecutor mNoOpExecutor;
  private OpenAppActionExecutor mOpenAppActionExecutor;
  private OpenBookActionExecutor mOpenBookActionExecutor;
  private OpenUrlActionExecutor mOpenUrlActionExecutor;
  private final PackageManager mPackageManager;
  private PhoneCallActionExecutor mPhoneCallActionExecutor;
  private PlayMovieActionExecutor mPlayMovieActionExecutor;
  private PlayMusicActionExecutor mPlayMusicActionExecutor;
  private PuntActionExecutor mPuntActionExecutor;
  private final Resources mResources;
  private SelfNoteActionExecutor mSelfNoteActionExecutor;
  private SetAlarmActionExecutor mSetAlarmActionExecutor;
  private SetReminderActionExecutor mSetReminderActionExecutor;
  private ShowContactInformationActionExecutor mShowContactInformationActionExecutor;
  private SmsActionExecutor mSmsActionExecutor;
  private SocialUpdateActionExecutor mSocialUpdateActionExecutor;
  private StopNavigationActionExecutor mStopNavigationActionExecutor;
  
  public ActionExecutorFactory(VelvetFactory paramVelvetFactory, Context paramContext, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, ExecutorService paramExecutorService, AccountHelper paramAccountHelper, IntentStarter paramIntentStarter, PackageManager paramPackageManager)
  {
    this.mContext = paramContext;
    this.mMainThreadExecutor = paramScheduledSingleThreadedExecutor;
    this.mExecutorService = paramExecutorService;
    this.mAccountHelper = paramAccountHelper;
    this.mIntentStarter = paramIntentStarter;
    this.mResources = paramContext.getResources();
    this.mPackageManager = paramPackageManager;
  }
  
  private CalendarHelper getCalendarHelper()
  {
    if (this.mCalendarHelper == null) {
      this.mCalendarHelper = new CalendarHelper(this.mAccountHelper, this.mContext.getContentResolver(), this.mMainThreadExecutor, this.mExecutorService);
    }
    return this.mCalendarHelper;
  }
  
  private CalendarTextHelper getCalendarTextHelper()
  {
    if (this.mCalendarTextHelper == null) {
      this.mCalendarTextHelper = new CalendarTextHelper(this.mContext);
    }
    return this.mCalendarTextHelper;
  }
  
  private EmailSender getEmailSender()
  {
    if (this.mEmailSender == null) {
      this.mEmailSender = new EmailSender(this.mAccountHelper, this.mContext.getPackageManager());
    }
    return this.mEmailSender;
  }
  
  private NoOpExecutor getNoOpExecutor()
  {
    if (this.mNoOpExecutor == null) {
      this.mNoOpExecutor = new NoOpExecutor();
    }
    return this.mNoOpExecutor;
  }
  
  @Nonnull
  public <T extends VoiceAction> ActionExecutor<T> getActionExecutor(T paramT)
  {
    if ((ActionExecutor)paramT.accept(this) == null) {
      return getNoOpExecutor();
    }
    return (ActionExecutor)paramT.accept(this);
  }
  
  protected AppSelectionHelper getAppSelectionHelper()
  {
    if (this.mAppSelectionHelper == null) {
      this.mAppSelectionHelper = new AppSelectionHelper(new PreferredApplicationsManager(VelvetServices.get().getPreferenceController().getMainPreferences()), this.mContext, this.mPackageManager, this.mContext.getResources());
    }
    return this.mAppSelectionHelper;
  }
  
  public PuntActionExecutor getPuntActionExecutor()
  {
    if (this.mPuntActionExecutor == null) {
      this.mPuntActionExecutor = new PuntActionExecutor(this.mIntentStarter);
    }
    return this.mPuntActionExecutor;
  }
  
  public ActionExecutor<?> visit(SearchError paramSearchError)
  {
    return getNoOpExecutor();
  }
  
  public ActionExecutor<AddEventAction> visit(AddEventAction paramAddEventAction)
  {
    if (this.mAddEventActionExecutor == null) {
      this.mAddEventActionExecutor = new AddEventActionExecutor(this.mIntentStarter, getCalendarHelper(), getCalendarTextHelper());
    }
    return this.mAddEventActionExecutor;
  }
  
  public ActionExecutor<?> visit(AgendaAction paramAgendaAction)
  {
    return getNoOpExecutor();
  }
  
  public ActionExecutor<?> visit(CancelAction paramCancelAction)
  {
    return getNoOpExecutor();
  }
  
  public ActionExecutor<EmailAction> visit(EmailAction paramEmailAction)
  {
    if (this.mEmailActionExecutor == null) {
      this.mEmailActionExecutor = new EmailActionExecutor(this.mIntentStarter, this.mAccountHelper, getEmailSender());
    }
    return this.mEmailActionExecutor;
  }
  
  public ActionExecutor<?> visit(HelpAction paramHelpAction)
  {
    return getNoOpExecutor();
  }
  
  public ActionExecutor<LocalResultsAction> visit(LocalResultsAction paramLocalResultsAction)
  {
    if (this.mLocalResultsActionExecutor == null) {
      this.mLocalResultsActionExecutor = new LocalResultsActionExecutor(this.mIntentStarter);
    }
    return this.mLocalResultsActionExecutor;
  }
  
  public ActionExecutor<OpenUrlAction> visit(OpenUrlAction paramOpenUrlAction)
  {
    if (this.mOpenUrlActionExecutor == null) {
      this.mOpenUrlActionExecutor = new OpenUrlActionExecutor(this.mIntentStarter);
    }
    return this.mOpenUrlActionExecutor;
  }
  
  public ActionExecutor<PhoneCallAction> visit(PhoneCallAction paramPhoneCallAction)
  {
    if (this.mPhoneCallActionExecutor == null) {
      this.mPhoneCallActionExecutor = new PhoneCallActionExecutor(this.mIntentStarter);
    }
    return this.mPhoneCallActionExecutor;
  }
  
  public ActionExecutor<?> visit(PlayMediaAction paramPlayMediaAction)
  {
    if (paramPlayMediaAction.isOpenAppAction())
    {
      if (this.mOpenAppActionExecutor == null) {
        this.mOpenAppActionExecutor = new OpenAppActionExecutor(this.mIntentStarter, getAppSelectionHelper());
      }
      return this.mOpenAppActionExecutor;
    }
    if (paramPlayMediaAction.isPlayMovieAction())
    {
      if (this.mPlayMovieActionExecutor == null) {
        this.mPlayMovieActionExecutor = new PlayMovieActionExecutor(this.mIntentStarter, getAppSelectionHelper(), this.mAccountHelper);
      }
      return this.mPlayMovieActionExecutor;
    }
    if (paramPlayMediaAction.isPlayMusicAction())
    {
      if (this.mPlayMusicActionExecutor == null) {
        this.mPlayMusicActionExecutor = new PlayMusicActionExecutor(this.mIntentStarter, getAppSelectionHelper());
      }
      return this.mPlayMusicActionExecutor;
    }
    if (paramPlayMediaAction.isOpenBookAction())
    {
      if (this.mOpenBookActionExecutor == null) {
        this.mOpenBookActionExecutor = new OpenBookActionExecutor(this.mIntentStarter, getAppSelectionHelper(), this.mAccountHelper);
      }
      return this.mOpenBookActionExecutor;
    }
    throw new UnsupportedOperationException("Unsupported Play media action.");
  }
  
  public ActionExecutor<PuntAction> visit(PuntAction paramPuntAction)
  {
    return getPuntActionExecutor();
  }
  
  public ActionExecutor<?> visit(ReadNotificationAction paramReadNotificationAction)
  {
    return getNoOpExecutor();
  }
  
  public ActionExecutor<SelfNoteAction> visit(SelfNoteAction paramSelfNoteAction)
  {
    if (this.mSelfNoteActionExecutor == null) {
      this.mSelfNoteActionExecutor = new SelfNoteActionExecutor(this.mIntentStarter, this.mAccountHelper, getEmailSender(), this.mResources.getString(2131363535));
    }
    return this.mSelfNoteActionExecutor;
  }
  
  public ActionExecutor<SetAlarmAction> visit(SetAlarmAction paramSetAlarmAction)
  {
    if (this.mSetAlarmActionExecutor == null) {
      this.mSetAlarmActionExecutor = new SetAlarmActionExecutor(this.mIntentStarter);
    }
    return this.mSetAlarmActionExecutor;
  }
  
  public ActionExecutor<SetReminderAction> visit(SetReminderAction paramSetReminderAction)
  {
    if (this.mSetReminderActionExecutor == null) {
      this.mSetReminderActionExecutor = new SetReminderActionExecutor();
    }
    return this.mSetReminderActionExecutor;
  }
  
  public ActionExecutor<ShowContactInformationAction> visit(ShowContactInformationAction paramShowContactInformationAction)
  {
    if (this.mShowContactInformationActionExecutor == null) {
      this.mShowContactInformationActionExecutor = new ShowContactInformationActionExecutor(this.mIntentStarter, getEmailSender());
    }
    return this.mShowContactInformationActionExecutor;
  }
  
  public ActionExecutor<SmsAction> visit(SmsAction paramSmsAction)
  {
    if (this.mSmsActionExecutor == null) {
      this.mSmsActionExecutor = new SmsActionExecutor(this.mIntentStarter, this.mContext.getPackageName());
    }
    return this.mSmsActionExecutor;
  }
  
  public ActionExecutor<SocialUpdateAction> visit(SocialUpdateAction paramSocialUpdateAction)
  {
    if (this.mSocialUpdateActionExecutor == null) {
      this.mSocialUpdateActionExecutor = new SocialUpdateActionExecutor(this.mIntentStarter);
    }
    return this.mSocialUpdateActionExecutor;
  }
  
  public ActionExecutor<StopNavigationAction> visit(StopNavigationAction paramStopNavigationAction)
  {
    if (this.mStopNavigationActionExecutor == null) {
      this.mStopNavigationActionExecutor = new StopNavigationActionExecutor(this.mIntentStarter);
    }
    return this.mStopNavigationActionExecutor;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.ActionExecutorFactory
 * JD-Core Version:    0.7.0.1
 */