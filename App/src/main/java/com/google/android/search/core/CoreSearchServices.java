package com.google.android.search.core;

import android.database.DataSetObservable;
import com.google.android.e100.MessageBuffer;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.RlzHelper;
import com.google.android.search.core.google.SearchBoxLogging;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.UriRewriter;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.search.core.sdch.SdchManager;
import com.google.android.search.core.util.AlarmHelper;
import com.google.android.search.core.util.ForceableLock;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.NamingDelayedTaskExecutor;
import com.google.android.sidekick.main.GmsLocationReportingHelper;
import com.google.android.sidekick.main.inject.PendingIntentFactory;
import com.google.android.speech.network.ConnectionFactory;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.velvet.ActionDiscoveryData;
import com.google.android.velvet.Cookies;
import com.google.android.velvet.Corpora;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.velvet.gallery.ImageMetadataController;
import com.google.android.voicesearch.speechservice.s3.PinholeParamsBuilderImpl;
import com.google.common.base.Supplier;

public abstract interface CoreSearchServices
{
  public abstract ActionDiscoveryData getActionDiscoveryData();
  
  public abstract AlarmHelper getAlarmHelper();
  
  public abstract VelvetBackgroundTasks getBackgroundTasks();
  
  public abstract Clock getClock();
  
  public abstract SearchConfig getConfig();
  
  public abstract Cookies getCookies();
  
  public abstract ForceableLock getCookiesLock();
  
  public abstract Corpora getCorpora();
  
  public abstract DeviceCapabilityManager getDeviceCapabilityManager();
  
  public abstract Supplier<DiscourseContext> getDiscourseContext();
  
  public abstract GmsLocationReportingHelper getGmsLocationReportingHelper();
  
  public abstract GooglePlayServicesHelper getGooglePlayServicesHelper();
  
  public abstract GsaConfigFlags getGsaConfigFlags();
  
  public abstract NamingDelayedTaskExecutor getHttpExecutor();
  
  public abstract HttpHelper getHttpHelper();
  
  public abstract ImageMetadataController getImageMetadataController();
  
  public abstract LocationSettings getLocationSettings();
  
  public abstract LoginHelper getLoginHelper();
  
  public abstract MessageBuffer getMessageBuffer();
  
  public abstract NetworkInformation getNetworkInfo();
  
  public abstract NowOptInSettings getNowOptInSettings();
  
  public abstract PendingIntentFactory getPendingIntentFactory();
  
  public abstract PinholeParamsBuilderImpl getPinholeParamsBuilder();
  
  public abstract PredictiveCardsPreferences getPredictiveCardsPreferences();
  
  public abstract RlzHelper getRlzHelper();
  
  public abstract SdchManager getSdchManager();
  
  public abstract SearchBoxLogging getSearchBoxLogging();
  
  public abstract SearchControllerCache getSearchControllerCache();
  
  public abstract DataSetObservable getSearchHistoryChangedObservable();
  
  public abstract SearchSettings getSearchSettings();
  
  public abstract SearchUrlHelper getSearchUrlHelper();
  
  public abstract ConnectionFactory getSpdyConnectionFactory();
  
  public abstract UriRewriter getUriRewriter();
  
  public abstract UserAgentHelper getUserAgentHelper();
  
  public abstract UserInteractionLogger getUserInteractionLogger();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.CoreSearchServices
 * JD-Core Version:    0.7.0.1
 */