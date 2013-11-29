package com.google.android.search.core.summons.icing;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.appdatasearch.GlobalSearchApplicationInfo;
import com.google.android.gms.appdatasearch.util.AppDataSearchDataManager.TableChangeListener;
import com.google.android.gms.appdatasearch.util.AppDataSearchDbOpenHelper;
import com.google.android.gms.appdatasearch.util.AppDataSearchProvider;
import com.google.android.gms.appdatasearch.util.TableStorageSpec;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.search.core.suggest.AppLaunchLogger;
import com.google.android.search.core.suggest.AppLaunchLogger.AppLaunch;
import com.google.android.search.core.suggest.AppLaunchLogger.ContextFileStreamProvider;
import com.google.android.shared.util.SystemClockImpl;
import com.google.android.shared.util.Util;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public final class InternalIcingCorporaProvider
  extends AppDataSearchProvider
{
  private static final Uri APPLICATIONS_CONTENT_URI;
  private static final Uri APPLICATION_SCORES_CONTENT_URI;
  private static final Uri.Builder BASE_URI;
  public static final Uri CLEAR_APP_LAUNCH_LOG_URI = BASE_URI.path("clear_launch_log").build();
  private static final Uri CONTACTS_CONTENT_URI;
  public static final boolean CONTACTS_DELTA_API_SUPPORTED = ContactsHelper.DELTA_API_SUPPORTED;
  public static final Uri DIAGNOSE_QUERY_URI;
  public static final Uri DUMP_QUERY_URI;
  public static final Uri REGISTER_CORPORA_URI;
  public static final Uri ZERO_PREFIX_APPLICATION_SUGGESTIONS_CONTENT_URI = BASE_URI.path("zero_prefix_application_suggestions").build();
  private static AtomicBoolean sCorporaRegistered = new AtomicBoolean(false);
  private AtomicBoolean mExternalCallPending;
  private UriMatcher mUriMatcher;
  
  static
  {
    BASE_URI = new Uri.Builder().scheme("content").authority("com.google.android.googlequicksearchbox.icing");
    APPLICATIONS_CONTENT_URI = BASE_URI.path("applications").build();
    APPLICATION_SCORES_CONTENT_URI = BASE_URI.path("app_scores").build();
    CONTACTS_CONTENT_URI = BASE_URI.path("contacts").build();
    REGISTER_CORPORA_URI = BASE_URI.path("register_corpora").build();
    DUMP_QUERY_URI = BASE_URI.path("dump").build();
    DIAGNOSE_QUERY_URI = BASE_URI.path("diagnose").build();
  }
  
  private void dump(String paramString, PrintWriter paramPrintWriter, boolean paramBoolean)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramString;
    arrayOfObject[1] = (getClass().getSimpleName() + " state");
    DumpUtils.println(paramPrintWriter, arrayOfObject);
    String str = paramString + "  ";
    getHelper().dump(str, paramPrintWriter, paramBoolean);
    UpdateCorporaService.dump(getContext(), str, paramPrintWriter);
  }
  
  private DatabaseHelper getHelper()
  {
    return (DatabaseHelper)getDataManager();
  }
  
  public static void onApplicationLaunched(Context paramContext, SearchConfig paramSearchConfig, AppLaunchLogger.AppLaunch paramAppLaunch)
  {
    AppLaunchLogger.recordAppLaunch(new AppLaunchLogger.ContextFileStreamProvider(paramContext), paramAppLaunch);
    UpdateCorporaService.scheduleApplicationScoreUpdate(paramContext, paramSearchConfig);
  }
  
  private int registerCorpora(String[] paramArrayOfString)
  {
    if (sCorporaRegistered.get()) {
      return 0;
    }
    ImmutableSet localImmutableSet = InternalCorpus.getEnabledCorpora(paramArrayOfString);
    HashSet localHashSet = Sets.newHashSet();
    Iterator localIterator = localImmutableSet.iterator();
    while (localIterator.hasNext()) {
      localHashSet.add(((InternalCorpus)localIterator.next()).getTableStorageSpec());
    }
    int i = safeRegisterCorpora((TableStorageSpec[])localHashSet.toArray(new TableStorageSpec[0]));
    AtomicBoolean localAtomicBoolean = sCorporaRegistered;
    if (i == 0) {}
    for (boolean bool = true;; bool = false)
    {
      localAtomicBoolean.set(bool);
      return i;
    }
  }
  
  private static boolean safeBinderCalls()
  {
    return true;
  }
  
  private int safeDiagnoseTable(TableStorageSpec paramTableStorageSpec)
  {
    try
    {
      int i = super.diagnoseTable(paramTableStorageSpec);
      return i;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.e("Icing.InternalIcingCorporaProvider", "Exception thrown from diagnoseTable", localRuntimeException);
      if (safeBinderCalls()) {
        return 4;
      }
      throw localRuntimeException;
    }
  }
  
  private int safeRegisterCorpora(TableStorageSpec[] paramArrayOfTableStorageSpec)
  {
    try
    {
      int i = super.registerCorpora(paramArrayOfTableStorageSpec);
      return i;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.e("Icing.InternalIcingCorporaProvider", "Exception thrown from registerCorpora", localRuntimeException);
      if (safeBinderCalls()) {
        return 8;
      }
      throw localRuntimeException;
    }
  }
  
  protected DatabaseHelper createDataManager(AppDataSearchDataManager.TableChangeListener paramTableChangeListener)
  {
    return new DatabaseHelper(getContext(), paramTableChangeListener);
  }
  
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    switch (this.mUriMatcher.match(paramUri))
    {
    default: 
      return 0;
    }
    getHelper().clearApplicationLaunchLog(getHelper().getWritableDatabase());
    update(APPLICATION_SCORES_CONTENT_URI, null, String.valueOf(true), null);
    return 1;
  }
  
  public String doGetType(Uri paramUri)
  {
    throw new UnsupportedOperationException();
  }
  
  public boolean doOnCreate()
  {
    this.mUriMatcher = new UriMatcher(-1);
    this.mExternalCallPending = new AtomicBoolean();
    this.mUriMatcher.addURI("com.google.android.googlequicksearchbox.icing", "applications", 1);
    this.mUriMatcher.addURI("com.google.android.googlequicksearchbox.icing", "zero_prefix_application_suggestions", 7);
    this.mUriMatcher.addURI("com.google.android.googlequicksearchbox.icing", "app_scores", 2);
    this.mUriMatcher.addURI("com.google.android.googlequicksearchbox.icing", "contacts", 3);
    this.mUriMatcher.addURI("com.google.android.googlequicksearchbox.icing", "register_corpora", 4);
    this.mUriMatcher.addURI("com.google.android.googlequicksearchbox.icing", "dump", 5);
    this.mUriMatcher.addURI("com.google.android.googlequicksearchbox.icing", "diagnose", 6);
    this.mUriMatcher.addURI("com.google.android.googlequicksearchbox.icing", "clear_launch_log", 8);
    return true;
  }
  
  public Cursor doQuery(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    switch (this.mUriMatcher.match(paramUri))
    {
    default: 
      throw new IllegalArgumentException("illegal uri: " + paramUri);
    case 6: 
      if (!this.mExternalCallPending.compareAndSet(false, true)) {}
      for (int i = 1; i != 0; i = 0)
      {
        Log.w("Icing.InternalIcingCorporaProvider", "Diagnose returning early - external call pending");
        return null;
      }
      MatrixCursor localMatrixCursor2;
      try
      {
        localMatrixCursor2 = new MatrixCursor(new String[] { "corpus", "diagnostic" });
        Iterator localIterator = InternalCorpus.getEnabledCorpora(paramArrayOfString2).iterator();
        while (localIterator.hasNext())
        {
          InternalCorpus localInternalCorpus = (InternalCorpus)localIterator.next();
          int j = safeDiagnoseTable(localInternalCorpus.getTableStorageSpec());
          Object[] arrayOfObject2 = new Object[2];
          arrayOfObject2[0] = localInternalCorpus.getCorpusName();
          arrayOfObject2[1] = Integer.valueOf(j);
          localMatrixCursor2.addRow(arrayOfObject2);
        }
      }
      finally
      {
        this.mExternalCallPending.set(false);
      }
      return localMatrixCursor2;
    case 5: 
      StringWriter localStringWriter = new StringWriter();
      dump(paramString1, new PrintWriter(localStringWriter), Feature.EXTENSIVE_ICING_LOGGING.isEnabled());
      MatrixCursor localMatrixCursor1 = new MatrixCursor(new String[] { "dump" });
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = localStringWriter.toString();
      localMatrixCursor1.addRow(arrayOfObject1);
      return localMatrixCursor1;
    }
    DatabaseHelper localDatabaseHelper = getHelper();
    return localDatabaseHelper.getZeroPrefixApplicationSuggestions(localDatabaseHelper.getReadableDatabase());
  }
  
  protected String getContentProviderAuthority()
  {
    return "com.google.android.googlequicksearchbox.icing";
  }
  
  protected String[] getCorpusNames()
  {
    return InternalTableStorageSpec.getCorpusNames();
  }
  
  protected GlobalSearchApplicationInfo getGlobalSearchableAppInfo()
  {
    return new GlobalSearchApplicationInfo(2131363325, 2131363326, 2130903043, "android.intent.action.MAIN", null, null);
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    throw new UnsupportedOperationException();
  }
  
  protected boolean shouldRegisterCorporaOnCreate()
  {
    return false;
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    int i = this.mUriMatcher.match(paramUri);
    if (i == -1) {
      throw new IllegalArgumentException("invalid uri: " + paramUri);
    }
    SearchConfig localSearchConfig = VelvetServices.get().getCoreServices().getConfig();
    String[] arrayOfString = localSearchConfig.getDisabledInternalIcingCorpora();
    if (i == 4)
    {
      if (!this.mExternalCallPending.compareAndSet(false, true)) {}
      for (int k = 1; k != 0; k = 0)
      {
        Log.w("Icing.InternalIcingCorporaProvider", "Corpora registration returning early - external call pending");
        return 0;
      }
      try
      {
        if (registerCorpora(arrayOfString) != 0) {
          Log.w("Icing.InternalIcingCorporaProvider", "Corpora registration failed");
        }
        return 0;
      }
      finally
      {
        this.mExternalCallPending.set(false);
      }
    }
    if (registerCorpora(arrayOfString) == 0) {}
    DatabaseHelper localDatabaseHelper;
    for (int j = 1;; j = 0)
    {
      if (j == 0) {
        Log.w("Icing.InternalIcingCorporaProvider", "Corpora registration failed");
      }
      localDatabaseHelper = getHelper();
      switch (i)
      {
      default: 
        throw new IllegalStateException("unhandled match for uri " + paramUri);
      }
    }
    return localDatabaseHelper.updateApplications(localDatabaseHelper.getWritableDatabase(), localSearchConfig, paramString);
    if ((!TextUtils.isEmpty(paramString)) && (Boolean.parseBoolean(paramString))) {}
    for (boolean bool = true;; bool = false) {
      return localDatabaseHelper.updateApplicationScores(localDatabaseHelper.getWritableDatabase(), localSearchConfig, bool);
    }
    return localDatabaseHelper.updateContacts(localDatabaseHelper.getWritableDatabase(), paramString);
  }
  
  public static final class ApplicationLaunchReceiver
    extends BroadcastReceiver
  {
    public static void setEnabledSetting(PackageManager paramPackageManager, String paramString, boolean paramBoolean)
    {
      ComponentName localComponentName = new ComponentName(paramString, ApplicationLaunchReceiver.class.getName());
      if (paramBoolean) {}
      for (int i = 1;; i = 2)
      {
        paramPackageManager.setComponentEnabledSetting(localComponentName, i, 1);
        return;
      }
    }
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      SearchConfig localSearchConfig = VelvetServices.get().getCoreServices().getConfig();
      boolean bool1 = localSearchConfig.isIcingAppLaunchBroadcastHandlingEnabled();
      if (!bool1) {
        setEnabledSetting(paramContext.getPackageManager(), paramContext.getPackageName(), false);
      }
      String str;
      do
      {
        boolean bool2;
        do
        {
          return;
          bool2 = VelvetServices.get().getCoreServices().getSearchSettings().isAppHistoryReportingEnabled();
        } while ((!bool2) || (!bool1) || (!bool2) || (!"com.android.launcher3.action.LAUNCH".equals(paramIntent.getAction())));
        str = paramIntent.getStringExtra("intent");
      } while (TextUtils.isEmpty(str));
      Intent localIntent;
      try
      {
        localIntent = Intent.parseUri(str, 0);
        ComponentName localComponentName = localIntent.getComponent();
        if (localComponentName != null)
        {
          InternalIcingCorporaProvider.onApplicationLaunched(paramContext, localSearchConfig, new AppLaunchLogger.AppLaunch(1, System.currentTimeMillis(), localComponentName));
          return;
        }
      }
      catch (URISyntaxException localURISyntaxException)
      {
        Log.e("Icing.ApplicationLaunchReceiver", "Received invalid intent extra", localURISyntaxException);
        return;
      }
      Log.w("Icing.ApplicationLaunchReceiver", "Got launch broadcast without component: " + localIntent);
    }
  }
  
  public static final class CorporaChangedReceiver
    extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      String str1 = paramIntent.getAction();
      Object localObject = "MAYBE";
      String str2 = "MAYBE";
      if (("android.intent.action.PACKAGE_ADDED".equals(str1)) || ("android.intent.action.PACKAGE_REMOVED".equals(str1)) || ("android.intent.action.PACKAGE_CHANGED".equals(str1)))
      {
        String str3 = paramIntent.getData().getSchemeSpecificPart();
        if ("com.google.android.gms".equals(str3)) {
          InternalIcingCorporaProvider.sCorporaRegistered.set(false);
        }
        boolean bool = paramIntent.getBooleanExtra("android.intent.extra.REPLACING", false);
        int i;
        if (str1.equals("android.intent.action.PACKAGE_REMOVED"))
        {
          i = 0;
          if (bool) {}
        }
        else
        {
          i = 1;
        }
        if (i != 0) {
          localObject = str3;
        }
        if (("android.intent.action.PACKAGE_CHANGED".equals(str1)) && ("com.android.providers.contacts".equals(str3))) {
          str2 = "FORCE_ALL";
        }
      }
      while ((TextUtils.equals((CharSequence)localObject, "MAYBE")) && (TextUtils.equals(str2, "MAYBE")))
      {
        return;
        if ("android.intent.action.LOCALE_CHANGED".equals(str1))
        {
          localObject = "FORCE_ALL";
        }
        else if ("android.intent.action.PACKAGE_DATA_CLEARED".equals(str1))
        {
          String str4 = paramIntent.getData().getSchemeSpecificPart();
          if ("com.android.providers.contacts".equals(str4))
          {
            str2 = "FORCE_ALL";
            if (Util.SDK_INT < 18) {
              InternalIcingCorporaProvider.UpdateCorporaService.maybeScheduleContactsSync(paramContext, VelvetServices.get().getCoreServices().getConfig());
            }
          }
          else if ("com.google.android.gms".equals(str4))
          {
            InternalIcingCorporaProvider.sCorporaRegistered.set(false);
          }
        }
        else
        {
          if ("android.provider.Contacts.DATABASE_CREATED".equals(str1))
          {
            InternalIcingCorporaProvider.UpdateCorporaService.maybeScheduleContactsSync(paramContext, VelvetServices.get().getCoreServices().getConfig());
            return;
          }
          Log.w("Icing.InternalIcingCorporaProvider", "BroadcastReceiver received unrecognized intent");
          return;
        }
      }
      paramContext.startService(InternalIcingCorporaProvider.UpdateCorporaService.access$200(paramContext, (String)localObject, str2));
    }
  }
  
  private static final class DatabaseHelper
    extends AppDataSearchDbOpenHelper
  {
    private final ApplicationsHelper mApplicationsHelper;
    private final ContactsHelper mContactsHelper;
    private final Context mContext;
    
    public DatabaseHelper(Context paramContext, AppDataSearchDataManager.TableChangeListener paramTableChangeListener)
    {
      super("icingcorpora.db", null, 9, InternalTableStorageSpec.getTableStorageSpecs(), paramTableChangeListener);
      this.mContext = paramContext;
      this.mApplicationsHelper = new ApplicationsHelper(paramContext.getPackageManager(), new SystemClockImpl(paramContext), new PackageNamePopularity());
      this.mContactsHelper = new ContactsHelper(paramContext.getContentResolver());
    }
    
    private void maybeNotifyUpdated(int paramInt, TableStorageSpec paramTableStorageSpec)
    {
      if ((paramInt > 0) && (!safeNotifyTableChanged(paramTableStorageSpec))) {
        Log.w("Icing.InternalIcingCorporaProvider", "Table change notification failed for " + paramTableStorageSpec);
      }
    }
    
    private boolean safeNotifyTableChanged(TableStorageSpec paramTableStorageSpec)
    {
      try
      {
        boolean bool = notifyTableChanged(paramTableStorageSpec);
        return bool;
      }
      catch (RuntimeException localRuntimeException)
      {
        Log.e("Icing.InternalIcingCorporaProvider", "Exception thrown from notifyTableChanged", localRuntimeException);
        if (InternalIcingCorporaProvider.access$000())
        {
          Log.w("Icing.InternalIcingCorporaProvider", "notifyTableChanged failed.");
          return false;
        }
        throw localRuntimeException;
      }
    }
    
    private void upgradeDbTo3(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mContactsHelper.upgradeDbTo3(paramSQLiteDatabase);
    }
    
    private void upgradeDbTo4(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mContactsHelper.upgradeDbTo4(paramSQLiteDatabase);
    }
    
    private void upgradeDbTo5(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mContactsHelper.upgradeDbTo5(paramSQLiteDatabase);
    }
    
    private void upgradeDbTo6(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mApplicationsHelper.upgradeDbTo6(paramSQLiteDatabase);
      this.mContactsHelper.upgradeDbTo6(paramSQLiteDatabase);
    }
    
    private void upgradeDbTo7(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mApplicationsHelper.upgradeDbTo7(paramSQLiteDatabase);
    }
    
    private void upgradeDbTo8(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mContactsHelper.upgradeDbTo8(paramSQLiteDatabase);
    }
    
    private void upgradeDbTo9(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mContactsHelper.upgradeDbTo9(paramSQLiteDatabase);
    }
    
    public void clearApplicationLaunchLog(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mApplicationsHelper.clearApplicationLaunchLog(paramSQLiteDatabase);
    }
    
    public void doOnCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mApplicationsHelper.createApplicationsTable(paramSQLiteDatabase);
      this.mContactsHelper.createContactsTable(paramSQLiteDatabase);
    }
    
    public void doOnUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      Log.i("Icing.InternalIcingCorporaProvider", "Upgrading DB from " + paramInt1 + " to " + paramInt2);
      if (paramInt1 < 3)
      {
        upgradeDbTo3(paramSQLiteDatabase);
        paramInt1 = 3;
      }
      if (paramInt1 < 4)
      {
        upgradeDbTo4(paramSQLiteDatabase);
        paramInt1 = 4;
      }
      if (paramInt1 < 5)
      {
        upgradeDbTo5(paramSQLiteDatabase);
        paramInt1 = 5;
      }
      if (paramInt1 < 6)
      {
        upgradeDbTo6(paramSQLiteDatabase);
        paramInt1 = 6;
      }
      if (paramInt1 < 7)
      {
        upgradeDbTo7(paramSQLiteDatabase);
        paramInt1 = 7;
      }
      if (paramInt1 < 8)
      {
        upgradeDbTo8(paramSQLiteDatabase);
        paramInt1 = 8;
      }
      if (paramInt1 < 9) {
        upgradeDbTo9(paramSQLiteDatabase);
      }
    }
    
    public void dump(String paramString, PrintWriter paramPrintWriter, boolean paramBoolean)
    {
      SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
      this.mApplicationsHelper.dump(localSQLiteDatabase, paramString, paramPrintWriter, paramBoolean);
      this.mContactsHelper.dump(localSQLiteDatabase, paramString, paramPrintWriter, paramBoolean);
    }
    
    public Cursor getZeroPrefixApplicationSuggestions(SQLiteDatabase paramSQLiteDatabase)
    {
      return this.mApplicationsHelper.getZeroPrefixApplicationSuggestions(paramSQLiteDatabase);
    }
    
    public int updateApplicationScores(SQLiteDatabase paramSQLiteDatabase, SearchConfig paramSearchConfig, boolean paramBoolean)
    {
      int i = this.mApplicationsHelper.updateApplicationScores(paramSQLiteDatabase, paramSearchConfig, this.mContext, paramBoolean);
      maybeNotifyUpdated(i, InternalTableStorageSpec.APPLICATIONS_SPEC.getTableStorageSpec());
      return i;
    }
    
    public int updateApplications(SQLiteDatabase paramSQLiteDatabase, SearchConfig paramSearchConfig, String paramString)
    {
      int i = this.mApplicationsHelper.updateApplicationsList(paramSQLiteDatabase, paramSearchConfig, paramString);
      maybeNotifyUpdated(i, InternalTableStorageSpec.APPLICATIONS_SPEC.getTableStorageSpec());
      return i;
    }
    
    public int updateContacts(SQLiteDatabase paramSQLiteDatabase, String paramString)
    {
      int i = this.mContactsHelper.updateContacts(paramSQLiteDatabase, "delta".equals(paramString));
      maybeNotifyUpdated(i, InternalTableStorageSpec.CONTACTS_SPEC.getTableStorageSpec());
      return i;
    }
  }
  
  private static final class UpdateApplicationsTask
    implements Callable<Boolean>
  {
    private final SearchConfig mConfig;
    private final ContentResolver mContentResolver;
    private final SharedPreferences mPreferences;
    private final String mUpdateMode;
    
    public UpdateApplicationsTask(SearchConfig paramSearchConfig, ContentResolver paramContentResolver, SharedPreferences paramSharedPreferences, String paramString)
    {
      this.mConfig = paramSearchConfig;
      this.mContentResolver = paramContentResolver;
      this.mPreferences = paramSharedPreferences;
      this.mUpdateMode = paramString;
    }
    
    public Boolean call()
    {
      if (!InternalCorpus.isApplicationsCorpusEnabled(this.mConfig))
      {
        Log.i("Icing.InternalIcingCorporaProvider", "Ignoring applications task because apps corpus disabled.");
        return Boolean.valueOf(false);
      }
      String[] arrayOfString = this.mConfig.getDisabledInternalIcingCorpora();
      int i = this.mConfig.getIcingAppsCorpusUpdateAllIntervalMillis();
      int j = this.mConfig.getIcingAppLaunchFullScoresUpdatePeriodMillis();
      long l = System.currentTimeMillis() - this.mPreferences.getLong("KEY_LAST_APPLICATIONS_UPDATE", 0L);
      if (Math.min(l, System.currentTimeMillis() - this.mPreferences.getLong("applications_last_scores_update_timestamp", 0L)) > j) {}
      for (boolean bool1 = true; "NONE".equals(this.mUpdateMode); bool1 = false) {
        return Boolean.valueOf(false);
      }
      if (("FORCE_ALL".equals(this.mUpdateMode)) || (("MAYBE".equals(this.mUpdateMode)) && (l > i)))
      {
        this.mContentResolver.update(InternalIcingCorporaProvider.APPLICATIONS_CONTENT_URI, null, null, arrayOfString);
        this.mPreferences.edit().putLong("KEY_LAST_APPLICATIONS_UPDATE", System.currentTimeMillis()).apply();
        return Boolean.valueOf(true);
      }
      if (("SCORES".equals(this.mUpdateMode)) || (("MAYBE".equals(this.mUpdateMode)) && (bool1)))
      {
        boolean bool2 = bool1;
        this.mContentResolver.update(InternalIcingCorporaProvider.APPLICATION_SCORES_CONTENT_URI, null, String.valueOf(bool2), arrayOfString);
        if (bool2) {
          this.mPreferences.edit().putLong("applications_last_scores_update_timestamp", System.currentTimeMillis()).commit();
        }
        return Boolean.valueOf(true);
      }
      if (!TextUtils.isEmpty(this.mUpdateMode))
      {
        String str = this.mUpdateMode;
        this.mContentResolver.update(InternalIcingCorporaProvider.APPLICATIONS_CONTENT_URI, null, str, arrayOfString);
        return Boolean.valueOf(true);
      }
      return Boolean.valueOf(false);
    }
  }
  
  private static final class UpdateContactsTask
    implements Callable<Boolean>
  {
    private final SearchConfig mConfig;
    private final ContentResolver mContentResolver;
    private final SharedPreferences mPreferences;
    private final String mUpdateMode;
    
    public UpdateContactsTask(SearchConfig paramSearchConfig, ContentResolver paramContentResolver, SharedPreferences paramSharedPreferences, String paramString)
    {
      this.mConfig = paramSearchConfig;
      this.mContentResolver = paramContentResolver;
      this.mPreferences = paramSharedPreferences;
      this.mUpdateMode = paramString;
    }
    
    public Boolean call()
    {
      if (!InternalCorpus.isContactsCorpusEnabled(this.mConfig))
      {
        Log.i("Icing.InternalIcingCorporaProvider", "Ignoring contacts task because contacts corpus disabled");
        return Boolean.valueOf(false);
      }
      if (ContactsHelper.DELTA_API_SUPPORTED) {}
      long l;
      for (int i = this.mConfig.getIcingContactsCorpusUpdateAllIntervalWithDeltaMillis();; i = this.mConfig.getIcingContactsCorpusUpdateAllIntervalWithoutDeltaMillis())
      {
        l = System.currentTimeMillis() - this.mPreferences.getLong("KEY_LAST_CONTACTS_UPDATE", 0L);
        if (!"NONE".equals(this.mUpdateMode)) {
          break;
        }
        return Boolean.valueOf(false);
      }
      if (("FORCE_ALL".equals(this.mUpdateMode)) || (("MAYBE".equals(this.mUpdateMode)) && (l > i)))
      {
        this.mContentResolver.update(InternalIcingCorporaProvider.CONTACTS_CONTENT_URI, null, null, null);
        this.mPreferences.edit().putLong("KEY_LAST_CONTACTS_UPDATE", System.currentTimeMillis()).apply();
        return Boolean.valueOf(true);
      }
      if ("DELTA".equals(this.mUpdateMode))
      {
        this.mContentResolver.update(InternalIcingCorporaProvider.CONTACTS_CONTENT_URI, null, "delta", null);
        return Boolean.valueOf(true);
      }
      return Boolean.valueOf(false);
    }
  }
  
  public static final class UpdateCorporaService
    extends IntentService
  {
    public UpdateCorporaService()
    {
      super();
      setIntentRedelivery(true);
    }
    
    public static Intent createContactsDeltaUpdateIntent(Context paramContext)
    {
      return createUpdateIntent(paramContext, "NONE", "DELTA");
    }
    
    public static Intent createForcedUpdateAllIntent(Context paramContext)
    {
      return createUpdateIntent(paramContext, "FORCE_ALL", "FORCE_ALL");
    }
    
    public static Intent createForcedUpdateAppsIntent(Context paramContext)
    {
      return createUpdateIntent(paramContext, "FORCE_ALL", "MAYBE");
    }
    
    public static Intent createPeriodicUpdateIntent(Context paramContext)
    {
      return createUpdateIntent(paramContext, "MAYBE", "MAYBE");
    }
    
    private static Intent createUpdateIntent(Context paramContext, String paramString1, String paramString2)
    {
      Intent localIntent = new Intent(paramContext, UpdateCorporaService.class);
      localIntent.setAction("ACTION_UPDATE");
      localIntent.putExtra("KEY_APPLICATIONS_UPDATE_MODE", paramString1);
      localIntent.putExtra("KEY_CONTACTS_UPDATE_MODE", paramString2);
      return localIntent;
    }
    
    public static void dump(Context paramContext, String paramString, PrintWriter paramPrintWriter)
    {
      DumpUtils.println(paramPrintWriter, new Object[] { paramString, "Alarm status: " });
      String str = paramString + "  ";
      Object[] arrayOfObject1 = new Object[3];
      arrayOfObject1[0] = str;
      arrayOfObject1[1] = "Applications score pending intent: ";
      arrayOfObject1[2] = getCurrentPendingIntent(paramContext, "ACTION_UPDATE_APP_SCORES");
      DumpUtils.println(paramPrintWriter, arrayOfObject1);
      Object[] arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = str;
      arrayOfObject2[1] = "Contact pending intent: ";
      arrayOfObject2[2] = getCurrentPendingIntent(paramContext, "ACTION_MAYBE_UPDATE_CONTACTS");
      DumpUtils.println(paramPrintWriter, arrayOfObject2);
    }
    
    private static PendingIntent getCurrentPendingIntent(Context paramContext, String paramString)
    {
      Intent localIntent = new Intent(paramContext, UpdateCorporaService.class);
      localIntent.setAction(paramString);
      return PendingIntent.getService(paramContext, 0, localIntent, 536870912);
    }
    
    private void handleMaybeUpdateContactsIntent(Intent paramIntent)
    {
      Bundle localBundle = paramIntent.getExtras();
      if (localBundle == null) {
        return;
      }
      int i = localBundle.getInt("EXTRA_LAST_RAW_CONTACT_COUNT", -1);
      int j = localBundle.getInt("EXTRA_ATTEMPT", 0);
      Cursor localCursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, null, null, null);
      if (localCursor == null)
      {
        Log.i("Icing.InternalIcingCorporaProvider", "Could not fetch contact count - no contacts provider present?");
        return;
      }
      int k;
      try
      {
        k = localCursor.getCount();
        localCursor.close();
        SearchConfig localSearchConfig = VelvetServices.get().getCoreServices().getConfig();
        int m = localSearchConfig.getIcingContactsProviderResyncMaxRepollAttempts();
        if ((k != i) && (j < m))
        {
          maybeRescheduleContactsSync(this, localSearchConfig, j + 1, k);
          return;
        }
      }
      finally
      {
        localCursor.close();
      }
      if (k != i) {
        Log.w("Icing.InternalIcingCorporaProvider", "Number of contacts did not stabilize after attempt " + j);
      }
      startUpdateCorporaTask("MAYBE", "FORCE_ALL");
    }
    
    private void handleUpdateAppScoresIntent(Intent paramIntent)
    {
      startUpdateCorporaTask("SCORES", "NONE");
    }
    
    private void handleUpdateIntent(Intent paramIntent)
    {
      startUpdateCorporaTask(paramIntent.getStringExtra("KEY_APPLICATIONS_UPDATE_MODE"), paramIntent.getStringExtra("KEY_CONTACTS_UPDATE_MODE"));
    }
    
    private static boolean maybeRescheduleContactsSync(Context paramContext, SearchConfig paramSearchConfig, int paramInt1, int paramInt2)
    {
      int i = paramSearchConfig.getIcingContactsProviderResyncRepollPeriodMillis();
      if (i < 0) {
        return false;
      }
      setContactsSyncServiceAlarm(paramContext, paramInt1, paramInt2, i);
      return true;
    }
    
    public static boolean maybeScheduleContactsSync(Context paramContext, SearchConfig paramSearchConfig)
    {
      if (!InternalCorpus.isContactsCorpusEnabled(paramSearchConfig))
      {
        Log.i("Icing.InternalIcingCorporaProvider", "Ignoring contacts schedule because corpus disabled");
        return false;
      }
      int i = paramSearchConfig.getIcingContactsProviderResyncStartedInitialPollDelayMillis();
      if (i < 0)
      {
        Log.i("Icing.InternalIcingCorporaProvider", "Not scheduling contact sync");
        return false;
      }
      setContactsSyncServiceAlarm(paramContext, 0, -1, i);
      return true;
    }
    
    static void scheduleApplicationScoreUpdate(Context paramContext, SearchConfig paramSearchConfig)
    {
      Intent localIntent = new Intent(paramContext, UpdateCorporaService.class);
      localIntent.setAction("ACTION_UPDATE_APP_SCORES");
      setAlarm(paramContext, paramSearchConfig.getIcingServiceAppLaunchBroadcastDelayMillis(), localIntent);
    }
    
    private static void setAlarm(Context paramContext, long paramLong, Intent paramIntent)
    {
      PendingIntent localPendingIntent = PendingIntent.getService(paramContext, 0, paramIntent, 268435456);
      ((AlarmManager)paramContext.getSystemService("alarm")).set(2, paramLong + SystemClock.elapsedRealtime(), localPendingIntent);
    }
    
    private static void setContactsSyncServiceAlarm(Context paramContext, int paramInt1, int paramInt2, long paramLong)
    {
      Intent localIntent = new Intent(paramContext, UpdateCorporaService.class);
      localIntent.setAction("ACTION_MAYBE_UPDATE_CONTACTS");
      localIntent.putExtra("EXTRA_ATTEMPT", paramInt1);
      localIntent.putExtra("EXTRA_LAST_RAW_CONTACT_COUNT", paramInt2);
      setAlarm(paramContext, paramLong, localIntent);
    }
    
    private void startUpdateCorporaTask(String paramString1, String paramString2)
    {
      VelvetServices localVelvetServices = VelvetServices.get();
      Log.i("Icing.InternalIcingCorporaProvider", "Updating corpora: A: " + paramString1 + ", C: " + paramString2);
      new InternalIcingCorporaProvider.UpdateCorporaTask(localVelvetServices.getCoreServices().getConfig(), localVelvetServices.getPreferenceController().getMainPreferences(), getContentResolver(), paramString1, paramString2).call();
    }
    
    protected void onHandleIntent(Intent paramIntent)
    {
      String str = paramIntent.getAction();
      if ("ACTION_UPDATE".equals(str))
      {
        handleUpdateIntent(paramIntent);
        return;
      }
      if ("ACTION_MAYBE_UPDATE_CONTACTS".equals(str))
      {
        handleMaybeUpdateContactsIntent(paramIntent);
        return;
      }
      if ("ACTION_UPDATE_APP_SCORES".equals(str))
      {
        handleUpdateAppScoresIntent(paramIntent);
        return;
      }
      Log.w("Icing.InternalIcingCorporaProvider", "Received unrecognized action.");
    }
  }
  
  public static final class UpdateCorporaTask
    implements Callable<Void>
  {
    private final InternalIcingCorporaProvider.UpdateApplicationsTask mUpdateApplicationsTask;
    private final InternalIcingCorporaProvider.UpdateContactsTask mUpdateContactsTask;
    
    public UpdateCorporaTask(SearchConfig paramSearchConfig, SharedPreferences paramSharedPreferences, ContentResolver paramContentResolver, String paramString1, String paramString2)
    {
      this.mUpdateApplicationsTask = new InternalIcingCorporaProvider.UpdateApplicationsTask(paramSearchConfig, paramContentResolver, paramSharedPreferences, paramString1);
      this.mUpdateContactsTask = new InternalIcingCorporaProvider.UpdateContactsTask(paramSearchConfig, paramContentResolver, paramSharedPreferences, paramString2);
    }
    
    public static void dump(SharedPreferences paramSharedPreferences, String paramString, PrintWriter paramPrintWriter)
    {
      Object[] arrayOfObject1 = new Object[3];
      arrayOfObject1[0] = paramString;
      arrayOfObject1[1] = "Current time: ";
      arrayOfObject1[2] = DumpUtils.formatTimestampISO8301(System.currentTimeMillis());
      DumpUtils.println(paramPrintWriter, arrayOfObject1);
      Object[] arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = paramString;
      arrayOfObject2[1] = "Last applications update: ";
      arrayOfObject2[2] = DumpUtils.formatTimestampISO8301(paramSharedPreferences.getLong("KEY_LAST_APPLICATIONS_UPDATE", -1L));
      DumpUtils.println(paramPrintWriter, arrayOfObject2);
      Object[] arrayOfObject3 = new Object[2];
      arrayOfObject3[0] = paramString;
      arrayOfObject3[1] = ("Last applications full scores update: " + DumpUtils.formatTimestampISO8301(paramSharedPreferences.getLong("applications_last_scores_update_timestamp", 0L)));
      DumpUtils.println(paramPrintWriter, arrayOfObject3);
      Object[] arrayOfObject4 = new Object[3];
      arrayOfObject4[0] = paramString;
      arrayOfObject4[1] = "Last contacts update: ";
      arrayOfObject4[2] = DumpUtils.formatTimestampISO8301(paramSharedPreferences.getLong("KEY_LAST_CONTACTS_UPDATE", -1L));
      DumpUtils.println(paramPrintWriter, arrayOfObject4);
    }
    
    public Void call()
    {
      SystemClock.elapsedRealtime();
      this.mUpdateApplicationsTask.call().booleanValue();
      SystemClock.elapsedRealtime();
      this.mUpdateContactsTask.call().booleanValue();
      SystemClock.elapsedRealtime();
      return null;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.InternalIcingCorporaProvider
 * JD-Core Version:    0.7.0.1
 */