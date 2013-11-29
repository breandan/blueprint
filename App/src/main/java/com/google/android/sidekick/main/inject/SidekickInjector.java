package com.google.android.sidekick.main.inject;

import android.support.v4.content.LocalBroadcastManager;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.sidekick.main.DataBackendVersionStore;
import com.google.android.sidekick.main.NowOptInHelper;
import com.google.android.sidekick.main.SensorSignalsOracle;
import com.google.android.sidekick.main.UserClientIdManager;
import com.google.android.sidekick.main.actions.ReminderSmartActionUtil;
import com.google.android.sidekick.main.calendar.CalendarController;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.contextprovider.RenderingContextPopulator;
import com.google.android.sidekick.main.entry.EntriesRefreshScheduler;
import com.google.android.sidekick.main.entry.EntriesRefreshThrottle;
import com.google.android.sidekick.main.entry.EntryInvalidator;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.entry.EntryTreePruner;
import com.google.android.sidekick.main.entry.LocationDisabledCardHelper;
import com.google.android.sidekick.main.file.AsyncFileStorage;
import com.google.android.sidekick.main.gcm.GcmManager;
import com.google.android.sidekick.main.gcm.PushMessageRepository;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.location.LocationReportingOptInHelper;
import com.google.android.sidekick.main.notifications.EntryNotification;
import com.google.android.sidekick.main.notifications.NotificationStore;
import com.google.android.sidekick.main.notifications.NowNotificationManager;
import com.google.android.sidekick.main.trigger.TriggerConditionEvaluator;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.client.NowRemoteClient;
import com.google.android.sidekick.shared.client.UndoDismissManager;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.velvet.presenter.FirstUseCardHandler;
import com.google.common.base.Supplier;

public abstract interface SidekickInjector
{
  public abstract boolean areRemindersEnabled();
  
  public abstract ActivityHelper getActivityHelper();
  
  public abstract AsyncFileStorage getAsyncFileStorage();
  
  public abstract CalendarController getCalendarController();
  
  public abstract CalendarDataProvider getCalendarDataProvider();
  
  public abstract DataBackendVersionStore getDataBackendVersionStore();
  
  public abstract EntriesRefreshScheduler getEntriesRefreshScheduler();
  
  public abstract EntriesRefreshThrottle getEntriesRefreshThrottle();
  
  public abstract EntryAdapterFactory<EntryCardViewAdapter> getEntryCardViewFactory();
  
  public abstract EntryInvalidator getEntryInvalidator();
  
  public abstract EntryAdapterFactory<EntryNotification> getEntryNotificationFactory();
  
  public abstract EntryProvider getEntryProvider();
  
  public abstract EntryTreePruner getEntryTreePruner();
  
  public abstract ExecutedUserActionStore getExecutedUserActionStore();
  
  public abstract FirstUseCardHandler getFirstUseCardHandler();
  
  public abstract GcmManager getGCMManager();
  
  public abstract SidekickInteractionManager getInteractionManager();
  
  public abstract LocalBroadcastManager getLocalBroadcastManager();
  
  public abstract LocationDisabledCardHelper getLocationDisabledCardHelper();
  
  public abstract LocationOracle getLocationOracle();
  
  public abstract LocationReportingOptInHelper getLocationReportingOptInHelper();
  
  public abstract NetworkClient getNetworkClient();
  
  public abstract NotificationStore getNotificationStore();
  
  public abstract Supplier<NowConfigurationPreferences> getNowConfigurationPreferencesSupplier();
  
  public abstract NowNotificationManager getNowNotificationManager();
  
  public abstract NowOptInHelper getNowOptInHelper();
  
  public abstract NowRemoteClient getNowRemoteClient();
  
  public abstract PushMessageRepository getPushMessageRespository();
  
  public abstract ReminderSmartActionUtil getReminderSmartActionUtil();
  
  public abstract RenderingContextPopulator getRenderingContextPopulator();
  
  public abstract SensorSignalsOracle getSensorSignalsOracle();
  
  public abstract StaticMapCache getStaticMapCache();
  
  public abstract StaticMapLoader getStaticMapLoader();
  
  public abstract TrainingQuestionManager getTrainingQuestionManager();
  
  public abstract TriggerConditionEvaluator getTriggerConditionEvaluator();
  
  public abstract UndoDismissManager getUndoDismissManager();
  
  public abstract UserClientIdManager getUserClientIdManager();
  
  public abstract VelvetImageGalleryHelper getVelvetImageGalleryHelper();
  
  public abstract WidgetManager getWidgetManager();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.SidekickInjector
 * JD-Core Version:    0.7.0.1
 */