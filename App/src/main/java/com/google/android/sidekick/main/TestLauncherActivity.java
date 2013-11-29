package com.google.android.sidekick.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.android.recurrencepicker.RecurrencePickerDialog;
import com.google.android.apps.sidekick.EntryProviderData;
import com.google.android.googlequicksearchbox.SearchActivity;
import com.google.android.search.core.GmmPrecacher;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.sidekick.main.file.AsyncFileStorage;
import com.google.android.sidekick.main.gcm.GcmManager;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.location.LocationOracleImpl;
import com.google.android.sidekick.main.notifications.NotificationRefreshService;
import com.google.android.sidekick.main.notifications.NotificationStore;
import com.google.android.sidekick.main.tv.TvDetectionTestActivity;
import com.google.android.sidekick.shared.util.SecondScreenUtil;
import com.google.android.sidekick.shared.util.SecondScreenUtil.Options;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.ContactLookup.Mode;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.speech.utils.ProtoBufUtils;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.fragments.VoiceActionDialogFragment;
import com.google.android.voicesearch.fragments.action.PhoneCallAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.AnsweredQuestions;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTree.CallbackWithInterest;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.QuestionTemplates;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.StringDictionary;
import com.google.geo.sidekick.Sidekick.TrainingModeDataResponse;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestLauncherActivity
  extends Activity
  implements View.OnClickListener
{
  private static final Contact CONTACT1;
  private static final Contact CONTACT2 = new Contact(ContactLookup.Mode.PHONE_NUMBER, 2L, "222", "222", 10, "0123456789", "mobile", 0, true);
  private static final Person PERSON1;
  private static final Person PERSON2 = Person.fromContact(CONTACT2);
  private static final Object REFRESH_NOTIFICATIONS_ACTION;
  private static final Object REFRESH_TRAFFIC_ACTION;
  private static final Object SCHEDULE_NOTIFICATIONS_ACTION;
  private static final String TAG = Tag.getTag(TestLauncherActivity.class);
  private LocationOracle mLocationOracle;
  private NetworkClient mNetworkClient;
  private NotificationStore mNotificationStore;
  private SensorSignalsOracle mSensorSignalsOracle;
  private TrainingQuestionManager mTrainingQuestionManager;
  private VelvetServices mVelvetServices;
  
  static
  {
    REFRESH_TRAFFIC_ACTION = "com.google.android.sidekick.main.REFRESH_TRAFFIC_ACTION";
    REFRESH_NOTIFICATIONS_ACTION = "com.google.android.sidekick.main.REFRESH_NOTIFICATIONS_ACTION";
    SCHEDULE_NOTIFICATIONS_ACTION = "com.google.android.sidekick.main.SCHEDULE_NOTIFICATIONS_ACTION";
    CONTACT1 = new Contact(ContactLookup.Mode.PHONE_NUMBER, 1L, "111", "111", 0, "1111", "home", 0, false);
    PERSON1 = Person.fromContact(CONTACT1);
  }
  
  private void addLocationFromString(String paramString, List<Location> paramList)
  {
    Location localLocation = parseLocationLine(paramString);
    if (localLocation != null) {
      paramList.add(localLocation);
    }
  }
  
  private void deleteApplicationFile(File paramFile)
  {
    if (paramFile.exists())
    {
      paramFile.delete();
      Toast.makeText(this, "File deleted", 0).show();
      return;
    }
    Toast.makeText(this, "File does not exist", 0).show();
  }
  
  private File getEntriesFile()
  {
    return new File(getApplicationContext().getFilesDir(), "entry_provider");
  }
  
  private File getNotificationsFile()
  {
    return new File(getApplicationContext().getFilesDir(), "notifications_store");
  }
  
  private void loadEncodedData(String paramString)
  {
    byte[] arrayOfByte = Base64.decode(paramString, 3);
    try
    {
      Sidekick.ResponsePayload localResponsePayload = new Sidekick.ResponsePayload();
      localResponsePayload.mergeFrom(arrayOfByte);
      ((VelvetNetworkClient)this.mNetworkClient).setDebugResponse(localResponsePayload);
      return;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      Log.w(TAG, "IO Exception: ", localInvalidProtocolBufferMicroException);
      Toast.makeText(this, "Invalid protocol buffer.  Check the logs.", 0).show();
    }
  }
  
  private void loadPushTestCards()
  {
    Toast.makeText(this, "Test cards loaded on next refresh", 0).show();
    ((VelvetNetworkClient)this.mNetworkClient).setLoadPushTestCards();
  }
  
  private Location parseLocationLine(String paramString)
  {
    String[] arrayOfString = paramString.split(",");
    if (arrayOfString.length < 2)
    {
      Log.w(TAG, "Expected parts (lat, long[, time]) in " + paramString);
      return null;
    }
    Location localLocation = new Location("fake");
    localLocation.setLatitude(Double.parseDouble(arrayOfString[0]));
    localLocation.setLongitude(Double.parseDouble(arrayOfString[1]));
    if ((arrayOfString.length > 2) && (!TextUtils.isEmpty(arrayOfString[2])))
    {
      localLocation.setTime(Long.parseLong(arrayOfString[2]));
      return localLocation;
    }
    localLocation.setTime(System.currentTimeMillis());
    return localLocation;
  }
  
  /* Error */
  private List<Location> parseLocationsFromFile(File paramFile)
  {
    // Byte code:
    //   0: new 282	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 283	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 285	java/io/BufferedReader
    //   11: dup
    //   12: new 287	java/io/FileReader
    //   15: dup
    //   16: aload_1
    //   17: invokespecial 289	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   20: invokespecial 292	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   23: astore_3
    //   24: aload_3
    //   25: invokevirtual 295	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   28: astore 9
    //   30: aload 9
    //   32: ifnull +49 -> 81
    //   35: aload_0
    //   36: aload 9
    //   38: aload_2
    //   39: invokespecial 297	com/google/android/sidekick/main/TestLauncherActivity:addLocationFromString	(Ljava/lang/String;Ljava/util/List;)V
    //   42: goto -18 -> 24
    //   45: astore 4
    //   47: aload_3
    //   48: invokevirtual 300	java/io/BufferedReader:close	()V
    //   51: aload 4
    //   53: athrow
    //   54: astore 7
    //   56: getstatic 40	com/google/android/sidekick/main/TestLauncherActivity:TAG	Ljava/lang/String;
    //   59: ldc_w 302
    //   62: aload 7
    //   64: invokestatic 202	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   67: pop
    //   68: aload_0
    //   69: ldc_w 304
    //   72: iconst_0
    //   73: invokestatic 145	android/widget/Toast:makeText	(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
    //   76: invokevirtual 148	android/widget/Toast:show	()V
    //   79: aload_2
    //   80: areturn
    //   81: aload_3
    //   82: invokevirtual 300	java/io/BufferedReader:close	()V
    //   85: aload_2
    //   86: areturn
    //   87: astore 5
    //   89: getstatic 40	com/google/android/sidekick/main/TestLauncherActivity:TAG	Ljava/lang/String;
    //   92: ldc 196
    //   94: aload 5
    //   96: invokestatic 202	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   99: pop
    //   100: aload_0
    //   101: ldc_w 306
    //   104: iconst_0
    //   105: invokestatic 145	android/widget/Toast:makeText	(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
    //   108: invokevirtual 148	android/widget/Toast:show	()V
    //   111: aload_2
    //   112: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	113	0	this	TestLauncherActivity
    //   0	113	1	paramFile	File
    //   7	105	2	localArrayList	ArrayList
    //   23	59	3	localBufferedReader	java.io.BufferedReader
    //   45	7	4	localObject	Object
    //   87	8	5	localIOException	IOException
    //   54	9	7	localFileNotFoundException	java.io.FileNotFoundException
    //   28	9	9	str	String
    // Exception table:
    //   from	to	target	type
    //   24	30	45	finally
    //   35	42	45	finally
    //   8	24	54	java/io/FileNotFoundException
    //   47	54	54	java/io/FileNotFoundException
    //   81	85	54	java/io/FileNotFoundException
    //   8	24	87	java/io/IOException
    //   47	54	87	java/io/IOException
    //   81	85	87	java/io/IOException
  }
  
  private void precacheTest()
  {
    Toast.makeText(this, "Precache test", 0).show();
    FilePickerDialog.newInstance(2).show(getFragmentManager(), "precache_test");
  }
  
  private void precacheTest(File paramFile)
  {
    List localList = parseLocationsFromFile(paramFile);
    new GmmPrecacher().precache(this, localList);
  }
  
  private void processData(File paramFile, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return;
    case 0: 
      queueData(paramFile);
      return;
    case 1: 
      queueLocations(paramFile);
      return;
    }
    precacheTest(paramFile);
  }
  
  private void pushTestLocations(List<Location> paramList)
  {
    if (!paramList.isEmpty()) {
      ((LocationOracleImpl)this.mLocationOracle).pushTestLocations(paramList);
    }
  }
  
  /* Error */
  private void queueData(File paramFile)
  {
    // Byte code:
    //   0: new 181	com/google/geo/sidekick/Sidekick$ResponsePayload
    //   3: dup
    //   4: invokespecial 182	com/google/geo/sidekick/Sidekick$ResponsePayload:<init>	()V
    //   7: astore_2
    //   8: aconst_null
    //   9: astore_3
    //   10: new 353	java/io/FileInputStream
    //   13: dup
    //   14: aload_1
    //   15: invokespecial 354	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   18: astore 4
    //   20: aload_2
    //   21: aload 4
    //   23: invokestatic 359	com/google/protobuf/micro/CodedInputStreamMicro:newInstance	(Ljava/io/InputStream;)Lcom/google/protobuf/micro/CodedInputStreamMicro;
    //   26: invokevirtual 362	com/google/geo/sidekick/Sidekick$ResponsePayload:mergeFrom	(Lcom/google/protobuf/micro/CodedInputStreamMicro;)Lcom/google/geo/sidekick/Sidekick$ResponsePayload;
    //   29: pop
    //   30: aload_0
    //   31: getfield 188	com/google/android/sidekick/main/TestLauncherActivity:mNetworkClient	Lcom/google/android/sidekick/main/inject/NetworkClient;
    //   34: checkcast 190	com/google/android/sidekick/main/VelvetNetworkClient
    //   37: aload_2
    //   38: invokevirtual 194	com/google/android/sidekick/main/VelvetNetworkClient:setDebugResponse	(Lcom/google/geo/sidekick/Sidekick$ResponsePayload;)V
    //   41: aload 4
    //   43: invokestatic 368	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   46: return
    //   47: astore 5
    //   49: getstatic 40	com/google/android/sidekick/main/TestLauncherActivity:TAG	Ljava/lang/String;
    //   52: ldc_w 302
    //   55: aload 5
    //   57: invokestatic 202	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   60: pop
    //   61: aload_0
    //   62: ldc_w 304
    //   65: iconst_0
    //   66: invokestatic 145	android/widget/Toast:makeText	(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
    //   69: invokevirtual 148	android/widget/Toast:show	()V
    //   72: aload_3
    //   73: invokestatic 368	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   76: return
    //   77: astore 8
    //   79: getstatic 40	com/google/android/sidekick/main/TestLauncherActivity:TAG	Ljava/lang/String;
    //   82: ldc 196
    //   84: aload 8
    //   86: invokestatic 202	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: pop
    //   90: aload_0
    //   91: ldc 204
    //   93: iconst_0
    //   94: invokestatic 145	android/widget/Toast:makeText	(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
    //   97: invokevirtual 148	android/widget/Toast:show	()V
    //   100: aload_3
    //   101: invokestatic 368	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   104: return
    //   105: astore 10
    //   107: getstatic 40	com/google/android/sidekick/main/TestLauncherActivity:TAG	Ljava/lang/String;
    //   110: ldc 196
    //   112: aload 10
    //   114: invokestatic 202	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   117: pop
    //   118: aload_0
    //   119: ldc_w 306
    //   122: iconst_0
    //   123: invokestatic 145	android/widget/Toast:makeText	(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
    //   126: invokevirtual 148	android/widget/Toast:show	()V
    //   129: aload_3
    //   130: invokestatic 368	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   133: return
    //   134: astore 6
    //   136: aload_3
    //   137: invokestatic 368	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   140: aload 6
    //   142: athrow
    //   143: astore 6
    //   145: aload 4
    //   147: astore_3
    //   148: goto -12 -> 136
    //   151: astore 10
    //   153: aload 4
    //   155: astore_3
    //   156: goto -49 -> 107
    //   159: astore 8
    //   161: aload 4
    //   163: astore_3
    //   164: goto -85 -> 79
    //   167: astore 5
    //   169: aload 4
    //   171: astore_3
    //   172: goto -123 -> 49
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	175	0	this	TestLauncherActivity
    //   0	175	1	paramFile	File
    //   7	31	2	localResponsePayload	Sidekick.ResponsePayload
    //   9	163	3	localObject1	Object
    //   18	152	4	localFileInputStream	java.io.FileInputStream
    //   47	9	5	localFileNotFoundException1	java.io.FileNotFoundException
    //   167	1	5	localFileNotFoundException2	java.io.FileNotFoundException
    //   134	7	6	localObject2	Object
    //   143	1	6	localObject3	Object
    //   77	8	8	localInvalidProtocolBufferMicroException1	InvalidProtocolBufferMicroException
    //   159	1	8	localInvalidProtocolBufferMicroException2	InvalidProtocolBufferMicroException
    //   105	8	10	localIOException1	IOException
    //   151	1	10	localIOException2	IOException
    // Exception table:
    //   from	to	target	type
    //   10	20	47	java/io/FileNotFoundException
    //   10	20	77	com/google/protobuf/micro/InvalidProtocolBufferMicroException
    //   10	20	105	java/io/IOException
    //   10	20	134	finally
    //   49	72	134	finally
    //   79	100	134	finally
    //   107	129	134	finally
    //   20	41	143	finally
    //   20	41	151	java/io/IOException
    //   20	41	159	com/google/protobuf/micro/InvalidProtocolBufferMicroException
    //   20	41	167	java/io/FileNotFoundException
  }
  
  private void queueLocation(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    addLocationFromString(paramString, localArrayList);
    pushTestLocations(localArrayList);
    if (!localArrayList.isEmpty())
    {
      long l = ((Location)localArrayList.get(0)).getTime();
      this.mSensorSignalsOracle.setUserTimeMillis(l);
    }
  }
  
  private void queueLocations(File paramFile)
  {
    pushTestLocations(parseLocationsFromFile(paramFile));
  }
  
  private void registerWithGCM()
  {
    new AsyncTask()
    {
      protected Void doInBackground(Void... paramAnonymousVarArgs)
      {
        try
        {
          TestLauncherActivity.this.mVelvetServices.getSidekickInjector().getGCMManager().register();
          return null;
        }
        catch (IOException localIOException)
        {
          for (;;)
          {
            Log.w(TestLauncherActivity.TAG, "IO Exception: ", localIOException);
            Toast.makeText(TestLauncherActivity.this, "IO Exception.  Check the logs.", 0).show();
          }
        }
      }
    }.execute(new Void[0]);
  }
  
  public void browseModeResults(View paramView)
  {
    Sidekick.Interest localInterest = new Sidekick.Interest().setTargetDisplay(9);
    Sidekick.Photo localPhoto = new Sidekick.Photo().setUrl("http://lh3.ggpht.com/uEKm-nqyZsYhw7AyQLhI1kpHYY3dn5n0-6DL7I49DOe1bdjhrSQSVWptTZr_").setUrlType(1);
    SecondScreenUtil.Options localOptions = new SecondScreenUtil.Options().setInterest(localInterest).setTitle("Test results").setContextHeaderImage(localPhoto);
    startActivity(SecondScreenUtil.createIntent(getApplication(), localOptions));
  }
  
  public void callDialog(View paramView)
  {
    PersonDisambiguation localPersonDisambiguation = new PersonDisambiguation(ContactLookup.Mode.PHONE_NUMBER);
    ArrayList localArrayList = Lists.newArrayList();
    localArrayList.add(PERSON1);
    localArrayList.add(PERSON2);
    localPersonDisambiguation.setCandidates(localArrayList, true);
    displayActionDialog(new PhoneCallAction(localPersonDisambiguation));
  }
  
  public void clearData()
  {
    Toast.makeText(this, "Clearing response data", 0).show();
    ((VelvetNetworkClient)this.mNetworkClient).setDebugResponse(null);
  }
  
  public void clearNotificationStore()
  {
    Toast.makeText(this, "Clearing notification store", 0).show();
    new AsyncTask()
    {
      protected Void doInBackground(Void... paramAnonymousVarArgs)
      {
        TestLauncherActivity.this.mNotificationStore.clearAll();
        return null;
      }
    }.execute(new Void[0]);
  }
  
  public void clearSavedLocation()
  {
    Toast.makeText(this, "Cleared saved location", 0).show();
    SharedPreferences.Editor localEditor = this.mVelvetServices.getPreferenceController().getMainPreferences().edit();
    localEditor.remove("lastloc");
    localEditor.apply();
  }
  
  public void clearSportsTeamCache(View paramView)
  {
    this.mVelvetServices.getSidekickInjector().getAsyncFileStorage().deleteFile("static_entities");
  }
  
  public void clearStickyLocation()
  {
    Toast.makeText(this, "Cleared sticky location", 0).show();
    ((LocationOracleImpl)this.mLocationOracle).pushTestLocations(null);
    this.mSensorSignalsOracle.clearUserTimeMillis();
  }
  
  public void clearTrainingModeData()
  {
    Toast.makeText(this, "Clearing training mode data", 0).show();
    new AsyncTask()
    {
      protected Void doInBackground(Void... paramAnonymousVarArgs)
      {
        Iterable localIterable = TestLauncherActivity.this.mTrainingQuestionManager.getPendingAnsweredQuestionsWithEntries();
        TestLauncherActivity.this.mTrainingQuestionManager.updateFromServerResponse(new Sidekick.TrainingModeDataResponse().setQuestionTemplates(new Sidekick.QuestionTemplates()).setStringDictionary(new Sidekick.StringDictionary()).setAnsweredQuestions(new Sidekick.AnsweredQuestions().setUpdateMethod(2)), localIterable);
        return null;
      }
    }.execute(new Void[0]);
  }
  
  public void detectTv(View paramView)
  {
    startActivity(new Intent(this, TvDetectionTestActivity.class));
  }
  
  public void displayActionDialog(VoiceAction paramVoiceAction)
  {
    FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
    VoiceActionDialogFragment.newInstance(paramVoiceAction).show(localFragmentTransaction, "action_dialog");
  }
  
  public void emailResponse(View paramView)
  {
    this.mVelvetServices.getSidekickInjector().getAsyncFileStorage().readFromEncryptedFile("entry_provider", new Function()
    {
      public Void apply(byte[] paramAnonymousArrayOfByte)
      {
        if (paramAnonymousArrayOfByte != null) {}
        try
        {
          EntryProviderData localEntryProviderData = new EntryProviderData();
          localEntryProviderData.mergeFrom(paramAnonymousArrayOfByte);
          String str = ProtoBufUtils.toString(localEntryProviderData.getEntryResponse());
          Intent localIntent = new Intent("android.intent.action.SEND");
          localIntent.setType("text/html");
          localIntent.putExtra("android.intent.extra.TEXT", str);
          TestLauncherActivity.this.startActivity(localIntent);
          label65:
          return null;
        }
        catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
        {
          break label65;
        }
      }
    });
  }
  
  void initializeHandlers()
  {
    findViewById(2131297068).setOnClickListener(this);
    findViewById(2131297067).setOnClickListener(this);
    findViewById(2131297069).setOnClickListener(this);
    findViewById(2131297070).setOnClickListener(this);
    findViewById(2131297071).setOnClickListener(this);
    findViewById(2131297072).setOnClickListener(this);
    findViewById(2131297073).setOnClickListener(this);
    findViewById(2131297074).setOnClickListener(this);
    findViewById(2131297075).setOnClickListener(this);
    findViewById(2131297076).setOnClickListener(this);
    findViewById(2131297077).setOnClickListener(this);
    findViewById(2131297078).setOnClickListener(this);
    findViewById(2131297079).setOnClickListener(this);
    findViewById(2131297085).setOnClickListener(this);
  }
  
  public void launchRecurrenceDialog()
  {
    new RecurrencePickerDialog().show(getFragmentManager(), "recurrence");
  }
  
  public void loadData()
  {
    Toast.makeText(this, "Loading response data", 0).show();
    FilePickerDialog.newInstance(0).show(getFragmentManager(), "data_response");
  }
  
  public void loadLocations()
  {
    Toast.makeText(this, "Loading location data", 0).show();
    FilePickerDialog.newInstance(1).show(getFragmentManager(), "locations");
  }
  
  public void myRemindersWebActivity(View paramView)
  {
    startActivity(new Intent(this, RemindersListWebView.class));
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    case 2131297080: 
    case 2131297081: 
    case 2131297082: 
    case 2131297083: 
    case 2131297084: 
    default: 
      return;
    case 2131297068: 
      loadPushTestCards();
      return;
    case 2131297067: 
      registerWithGCM();
      return;
    case 2131297069: 
      loadData();
      return;
    case 2131297070: 
      loadLocations();
      return;
    case 2131297071: 
      clearStickyLocation();
      return;
    case 2131297072: 
      sendIntent();
      return;
    case 2131297073: 
      clearSavedLocation();
      return;
    case 2131297074: 
      clearData();
      return;
    case 2131297075: 
      clearNotificationStore();
      return;
    case 2131297076: 
      clearTrainingModeData();
      return;
    case 2131297077: 
      precacheTest();
      return;
    case 2131297078: 
      deleteApplicationFile(getEntriesFile());
      return;
    case 2131297079: 
      deleteApplicationFile(getNotificationsFile());
      return;
    }
    launchRecurrenceDialog();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(8);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    this.mVelvetServices = VelvetServices.get();
    if (!DebugFeatures.isSet()) {
      DebugFeatures.setDebugLevel();
    }
    Intent localIntent1;
    String str1;
    if (DebugFeatures.getInstance().teamDebugEnabled())
    {
      setContentView(2130968855);
      this.mNetworkClient = this.mVelvetServices.getSidekickInjector().getNetworkClient();
      this.mLocationOracle = this.mVelvetServices.getSidekickInjector().getLocationOracle();
      this.mSensorSignalsOracle = this.mVelvetServices.getSidekickInjector().getSensorSignalsOracle();
      this.mTrainingQuestionManager = this.mVelvetServices.getSidekickInjector().getTrainingQuestionManager();
      this.mNotificationStore = this.mVelvetServices.getSidekickInjector().getNotificationStore();
      initializeHandlers();
      localIntent1 = getIntent();
      str1 = localIntent1.getAction();
      if ((!"com.google.android.sidekick.main.LOAD_LOCATION".equals(str1)) || (!localIntent1.hasExtra("location"))) {
        break label257;
      }
      String str2 = localIntent1.getStringExtra("location");
      Toast.makeText(this, "Location " + str2, 0).show();
      queueLocation(str2);
      finish();
    }
    for (;;)
    {
      if (!getEntriesFile().exists()) {
        ((Button)findViewById(2131297078)).setEnabled(false);
      }
      return;
      Toast.makeText(this, "Not enabled for a non-debug build", 0).show();
      finish();
      return;
      label257:
      if ("com.google.android.sidekick.main.CLEAR_LOCATIONS".equals(str1))
      {
        clearStickyLocation();
        finish();
      }
      else if ("com.google.android.sidekick.main.CLEAR_NOTIFICATION_STORE".equals(str1))
      {
        clearNotificationStore();
        finish();
      }
      else if (REFRESH_TRAFFIC_ACTION.equals(str1))
      {
        Intent localIntent2 = new Intent(this, TrafficIntentService.class);
        localIntent2.putExtra("force_refresh", true);
        startService(localIntent2);
        finish();
      }
      else if (REFRESH_NOTIFICATIONS_ACTION.equals(str1))
      {
        Intent localIntent3 = new Intent(this, NotificationRefreshService.class);
        localIntent3.setAction("com.google.android.apps.sidekick.notifications.REFRESH");
        startService(localIntent3);
        finish();
      }
      else if (SCHEDULE_NOTIFICATIONS_ACTION.equals(str1))
      {
        Intent localIntent4 = new Intent(this, NotificationRefreshService.class);
        localIntent4.setAction("com.google.android.apps.sidekick.notifications.SCHEDULE_REFRESH");
        Sidekick.Interest localInterest = new Sidekick.Interest().addEntryTypeRestrict(1).setTargetDisplay(3);
        Sidekick.EntryTree.CallbackWithInterest localCallbackWithInterest = new Sidekick.EntryTree.CallbackWithInterest().setInterest(localInterest).setCallbackTimeSeconds((5000L + System.currentTimeMillis()) / 1000L);
        localIntent4.putExtra("com.google.android.apps.sidekick.notifications.NEXT_REFRESH", new Sidekick.EntryTree().addCallbackWithInterest(localCallbackWithInterest).toByteArray());
        startService(localIntent4);
        finish();
      }
      else if ("com.google.android.sidekick.main.LOAD_RESPONSE".equals(str1))
      {
        loadEncodedData(localIntent1.getStringExtra("response"));
        finish();
      }
      else if ("com.google.android.sidekick.main.BAD_CONNECTION".equals(str1))
      {
        setBadConnectionEnabled(Boolean.parseBoolean(localIntent1.getStringExtra("enabled")));
        finish();
      }
    }
  }
  
  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default: 
      return super.onOptionsItemSelected(paramMenuItem);
    }
    Intent localIntent = new Intent(this, SearchActivity.class);
    localIntent.setAction("android.intent.action.ASSIST");
    localIntent.addFlags(67108864);
    startActivity(localIntent);
    return true;
  }
  
  public void sendIntent()
  {
    Toast.makeText(this, "Sending Intent", 0).show();
    new IntentPickerDialog().show(getFragmentManager(), "intent_picker");
  }
  
  public void setBadConnectionEnabled(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "Simulating bad connections";; str = "Back to real connections")
    {
      Toast.makeText(this, str, 0).show();
      ((VelvetNetworkClient)this.mNetworkClient).setDebugBadConnection(paramBoolean);
      return;
    }
  }
  
  public void tvResults(View paramView)
  {
    startActivity(SecondScreenUtil.createTvIntent(getApplication(), "test"));
  }
  
  public static class FilePickerDialog
    extends DialogFragment
  {
    static FilePickerDialog newInstance(int paramInt)
    {
      FilePickerDialog localFilePickerDialog = new FilePickerDialog();
      Bundle localBundle = new Bundle();
      localBundle.putInt("data_type_key", paramInt);
      localFilePickerDialog.setArguments(localBundle);
      return localFilePickerDialog;
    }
    
    File[] getTestFiles()
    {
      String str = Environment.getExternalStorageState();
      int i;
      File[] arrayOfFile;
      if ("mounted".equals(str))
      {
        i = 1;
        if (i != 0) {
          break label57;
        }
        Toast.makeText(getActivity(), "Cannot get to external storage.", 0).show();
        arrayOfFile = null;
      }
      label57:
      File localFile;
      do
      {
        return arrayOfFile;
        if ("mounted_ro".equals(str))
        {
          i = 1;
          break;
        }
        i = 0;
        break;
        localFile = new File(getActivity().getExternalFilesDir(null), "TestData");
        if (!localFile.exists())
        {
          Toast.makeText(getActivity(), "No such directory " + localFile.getAbsolutePath(), 1).show();
          return null;
        }
        arrayOfFile = localFile.listFiles();
        if (arrayOfFile == null)
        {
          Toast.makeText(getActivity(), "Bad directory: " + localFile.getAbsolutePath(), 1).show();
          return null;
        }
      } while (arrayOfFile.length != 0);
      Toast.makeText(getActivity(), "No test files found in " + localFile.getAbsolutePath(), 1).show();
      return null;
    }
    
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      final int i = getArguments().getInt("data_type_key");
      final File[] arrayOfFile = getTestFiles();
      if (arrayOfFile == null)
      {
        AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(getActivity());
        localBuilder1.setTitle("No files to load");
        localBuilder1.setMessage("Please add some files");
        return localBuilder1.create();
      }
      final String[] arrayOfString = new String[arrayOfFile.length];
      for (int j = 0; j < arrayOfString.length; j++) {
        arrayOfString[j] = arrayOfFile[j].getName();
      }
      AlertDialog.Builder localBuilder2 = new AlertDialog.Builder(getActivity());
      localBuilder2.setTitle("Pick a test file to queue");
      localBuilder2.setItems(arrayOfString, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          Toast.makeText(TestLauncherActivity.FilePickerDialog.this.getActivity(), arrayOfString[paramAnonymousInt], 0).show();
          ((TestLauncherActivity)TestLauncherActivity.FilePickerDialog.this.getActivity()).processData(arrayOfFile[paramAnonymousInt], i);
        }
      });
      return localBuilder2.create();
    }
  }
  
  public static class IntentPickerDialog
    extends DialogFragment
  {
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      final String[] arrayOfString = { "Traffic Intent Service" };
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
      localBuilder.setTitle("Pick a service to exercise");
      localBuilder.setItems(arrayOfString, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          Toast.makeText(TestLauncherActivity.IntentPickerDialog.this.getActivity(), arrayOfString[paramAnonymousInt], 0).show();
          if (paramAnonymousInt == 0) {
            TestLauncherActivity.IntentPickerDialog.this.getActivity().startService(new Intent(TestLauncherActivity.IntentPickerDialog.this.getActivity(), TrafficIntentService.class));
          }
        }
      });
      return localBuilder.create();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.TestLauncherActivity
 * JD-Core Version:    0.7.0.1
 */