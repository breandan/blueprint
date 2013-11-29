package com.google.android.gms.auth;

import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.R.string;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.io.IOException;
import java.net.URISyntaxException;

public final class GoogleAuthUtil
{
  public static final String KEY_ANDROID_PACKAGE_NAME;
  public static final String KEY_CALLER_UID;
  private static final ComponentName ko;
  private static final ComponentName kp;
  private static final Intent kq;
  private static final Intent kr;
  
  static
  {
    String str1;
    if (Build.VERSION.SDK_INT >= 11)
    {
      str1 = "callerUid";
      KEY_CALLER_UID = str1;
      if (Build.VERSION.SDK_INT < 14) {
        break label107;
      }
    }
    label107:
    for (String str2 = "androidPackageName";; str2 = "androidPackageName")
    {
      KEY_ANDROID_PACKAGE_NAME = str2;
      ko = new ComponentName("com.google.android.gms", "com.google.android.gms.auth.GetToken");
      kp = new ComponentName("com.google.android.gms", "com.google.android.gms.recovery.RecoveryService");
      kq = new Intent().setPackage("com.google.android.gms").setComponent(ko);
      kr = new Intent().setPackage("com.google.android.gms").setComponent(kp);
      return;
      str1 = "callerUid";
      break;
    }
  }
  
  private static String a(Context paramContext, String paramString1, String paramString2, Bundle paramBundle)
    throws IOException, UserRecoverableNotifiedException, GoogleAuthException
  {
    if (paramBundle == null) {
      paramBundle = new Bundle();
    }
    try
    {
      String str3 = getToken(paramContext, paramString1, paramString2, paramBundle);
      return str3;
    }
    catch (GooglePlayServicesAvailabilityException localGooglePlayServicesAvailabilityException)
    {
      PendingIntent localPendingIntent = GooglePlayServicesUtil.getErrorPendingIntent(localGooglePlayServicesAvailabilityException.getConnectionStatusCode(), paramContext, 0);
      Resources localResources = paramContext.getResources();
      Notification localNotification = new Notification(17301642, localResources.getString(R.string.auth_client_play_services_err_notification_msg), System.currentTimeMillis());
      localNotification.flags = (0x10 | localNotification.flags);
      String str1 = paramContext.getApplicationInfo().name;
      if (TextUtils.isEmpty(str1)) {
        str1 = paramContext.getPackageName();
      }
      String str2 = localResources.getString(R.string.auth_client_requested_by_msg, new Object[] { str1 });
      int i;
      switch (localGooglePlayServicesAvailabilityException.getConnectionStatusCode())
      {
      default: 
        i = R.string.auth_client_using_bad_version_title;
      }
      for (;;)
      {
        localNotification.setLatestEventInfo(paramContext, localResources.getString(i), str2, localPendingIntent);
        ((NotificationManager)paramContext.getSystemService("notification")).notify(39789, localNotification);
        throw new UserRecoverableNotifiedException("User intervention required. Notification has been pushed.");
        i = R.string.auth_client_needs_installation_title;
        continue;
        i = R.string.auth_client_needs_update_title;
        continue;
        i = R.string.auth_client_needs_enabling_title;
      }
    }
    catch (UserRecoverableAuthException localUserRecoverableAuthException)
    {
      throw new UserRecoverableNotifiedException("User intervention required. Notification has been pushed.");
    }
  }
  
  private static void b(Intent paramIntent)
  {
    if (paramIntent == null) {
      throw new IllegalArgumentException("Callack cannot be null.");
    }
    String str = paramIntent.toUri(1);
    try
    {
      Intent.parseUri(str, 1);
      return;
    }
    catch (URISyntaxException localURISyntaxException)
    {
      throw new IllegalArgumentException("Parameter callback contains invalid data. It must be serializable using toUri() and parseUri().");
    }
  }
  
  /* Error */
  public static String getToken(Context paramContext, String paramString1, String paramString2, Bundle paramBundle)
    throws IOException, UserRecoverableAuthException, GoogleAuthException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 206	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   4: astore 4
    //   6: ldc 208
    //   8: invokestatic 213	com/google/android/gms/internal/ds:E	(Ljava/lang/String;)V
    //   11: aload 4
    //   13: invokestatic 217	com/google/android/gms/auth/GoogleAuthUtil:m	(Landroid/content/Context;)V
    //   16: aload_3
    //   17: ifnonnull +124 -> 141
    //   20: new 75	android/os/Bundle
    //   23: dup
    //   24: invokespecial 76	android/os/Bundle:<init>	()V
    //   27: astore 5
    //   29: aload_0
    //   30: invokevirtual 125	android/content/Context:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   33: getfield 220	android/content/pm/ApplicationInfo:packageName	Ljava/lang/String;
    //   36: astore 6
    //   38: aload 5
    //   40: ldc 222
    //   42: aload 6
    //   44: invokevirtual 225	android/os/Bundle:putString	(Ljava/lang/String;Ljava/lang/String;)V
    //   47: aload 5
    //   49: getstatic 29	com/google/android/gms/auth/GoogleAuthUtil:KEY_ANDROID_PACKAGE_NAME	Ljava/lang/String;
    //   52: invokevirtual 229	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
    //   55: ifne +13 -> 68
    //   58: aload 5
    //   60: getstatic 29	com/google/android/gms/auth/GoogleAuthUtil:KEY_ANDROID_PACKAGE_NAME	Ljava/lang/String;
    //   63: aload 6
    //   65: invokevirtual 225	android/os/Bundle:putString	(Ljava/lang/String;Ljava/lang/String;)V
    //   68: new 231	com/google/android/gms/common/a
    //   71: dup
    //   72: invokespecial 232	com/google/android/gms/common/a:<init>	()V
    //   75: astore 7
    //   77: aload 4
    //   79: getstatic 59	com/google/android/gms/auth/GoogleAuthUtil:kq	Landroid/content/Intent;
    //   82: aload 7
    //   84: iconst_1
    //   85: invokevirtual 236	android/content/Context:bindService	(Landroid/content/Intent;Landroid/content/ServiceConnection;I)Z
    //   88: ifeq +187 -> 275
    //   91: aload 7
    //   93: invokevirtual 240	com/google/android/gms/common/a:aK	()Landroid/os/IBinder;
    //   96: invokestatic 245	com/google/android/gms/internal/o$a:a	(Landroid/os/IBinder;)Lcom/google/android/gms/internal/o;
    //   99: aload_1
    //   100: aload_2
    //   101: aload 5
    //   103: invokeinterface 250 4 0
    //   108: astore 12
    //   110: aload 12
    //   112: ldc 252
    //   114: invokevirtual 255	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   117: astore 13
    //   119: aload 13
    //   121: invokestatic 136	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   124: istore 14
    //   126: iload 14
    //   128: ifne +26 -> 154
    //   131: aload 4
    //   133: aload 7
    //   135: invokevirtual 259	android/content/Context:unbindService	(Landroid/content/ServiceConnection;)V
    //   138: aload 13
    //   140: areturn
    //   141: new 75	android/os/Bundle
    //   144: dup
    //   145: aload_3
    //   146: invokespecial 262	android/os/Bundle:<init>	(Landroid/os/Bundle;)V
    //   149: astore 5
    //   151: goto -122 -> 29
    //   154: aload 12
    //   156: ldc_w 264
    //   159: invokevirtual 255	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   162: astore 15
    //   164: aload 12
    //   166: ldc_w 266
    //   169: invokevirtual 270	android/os/Bundle:getParcelable	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   172: checkcast 47	android/content/Intent
    //   175: astore 16
    //   177: aload 15
    //   179: invokestatic 273	com/google/android/gms/auth/GoogleAuthUtil:z	(Ljava/lang/String;)Z
    //   182: ifeq +52 -> 234
    //   185: new 73	com/google/android/gms/auth/UserRecoverableAuthException
    //   188: dup
    //   189: aload 15
    //   191: aload 16
    //   193: invokespecial 276	com/google/android/gms/auth/UserRecoverableAuthException:<init>	(Ljava/lang/String;Landroid/content/Intent;)V
    //   196: athrow
    //   197: astore 10
    //   199: ldc_w 278
    //   202: ldc_w 280
    //   205: aload 10
    //   207: invokestatic 286	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   210: pop
    //   211: new 65	java/io/IOException
    //   214: dup
    //   215: ldc_w 288
    //   218: invokespecial 289	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   221: athrow
    //   222: astore 9
    //   224: aload 4
    //   226: aload 7
    //   228: invokevirtual 259	android/content/Context:unbindService	(Landroid/content/ServiceConnection;)V
    //   231: aload 9
    //   233: athrow
    //   234: aload 15
    //   236: invokestatic 292	com/google/android/gms/auth/GoogleAuthUtil:y	(Ljava/lang/String;)Z
    //   239: ifeq +26 -> 265
    //   242: new 65	java/io/IOException
    //   245: dup
    //   246: aload 15
    //   248: invokespecial 289	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   251: athrow
    //   252: astore 8
    //   254: new 69	com/google/android/gms/auth/GoogleAuthException
    //   257: dup
    //   258: ldc_w 294
    //   261: invokespecial 295	com/google/android/gms/auth/GoogleAuthException:<init>	(Ljava/lang/String;)V
    //   264: athrow
    //   265: new 69	com/google/android/gms/auth/GoogleAuthException
    //   268: dup
    //   269: aload 15
    //   271: invokespecial 295	com/google/android/gms/auth/GoogleAuthException:<init>	(Ljava/lang/String;)V
    //   274: athrow
    //   275: new 65	java/io/IOException
    //   278: dup
    //   279: ldc_w 297
    //   282: invokespecial 289	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   285: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	286	0	paramContext	Context
    //   0	286	1	paramString1	String
    //   0	286	2	paramString2	String
    //   0	286	3	paramBundle	Bundle
    //   4	221	4	localContext	Context
    //   27	123	5	localBundle1	Bundle
    //   36	28	6	str1	String
    //   75	152	7	locala	com.google.android.gms.common.a
    //   252	1	8	localInterruptedException	java.lang.InterruptedException
    //   222	10	9	localObject	Object
    //   197	9	10	localRemoteException	android.os.RemoteException
    //   108	57	12	localBundle2	Bundle
    //   117	22	13	str2	String
    //   124	3	14	bool	boolean
    //   162	108	15	str3	String
    //   175	17	16	localIntent	Intent
    // Exception table:
    //   from	to	target	type
    //   91	126	197	android/os/RemoteException
    //   154	197	197	android/os/RemoteException
    //   234	252	197	android/os/RemoteException
    //   265	275	197	android/os/RemoteException
    //   91	126	222	finally
    //   154	197	222	finally
    //   199	222	222	finally
    //   234	252	222	finally
    //   254	265	222	finally
    //   265	275	222	finally
    //   91	126	252	java/lang/InterruptedException
    //   154	197	252	java/lang/InterruptedException
    //   234	252	252	java/lang/InterruptedException
    //   265	275	252	java/lang/InterruptedException
  }
  
  public static String getTokenWithNotification(Context paramContext, String paramString1, String paramString2, Bundle paramBundle, Intent paramIntent)
    throws IOException, UserRecoverableNotifiedException, GoogleAuthException
  {
    b(paramIntent);
    if (paramBundle == null) {
      paramBundle = new Bundle();
    }
    paramBundle.putParcelable("callback_intent", paramIntent);
    paramBundle.putBoolean("handle_notification", true);
    return a(paramContext, paramString1, paramString2, paramBundle);
  }
  
  public static void invalidateToken(Context paramContext, String paramString)
  {
    AccountManager.get(paramContext).invalidateAuthToken("com.google", paramString);
  }
  
  private static void m(Context paramContext)
    throws GooglePlayServicesAvailabilityException, GoogleAuthException
  {
    try
    {
      GooglePlayServicesUtil.m(paramContext);
      return;
    }
    catch (GooglePlayServicesRepairableException localGooglePlayServicesRepairableException)
    {
      throw new GooglePlayServicesAvailabilityException(localGooglePlayServicesRepairableException.getConnectionStatusCode(), localGooglePlayServicesRepairableException.getMessage(), localGooglePlayServicesRepairableException.getIntent());
    }
    catch (GooglePlayServicesNotAvailableException localGooglePlayServicesNotAvailableException)
    {
      throw new GoogleAuthException(localGooglePlayServicesNotAvailableException.getMessage());
    }
  }
  
  private static boolean y(String paramString)
  {
    return ("NetworkError".equals(paramString)) || ("ServiceUnavailable".equals(paramString)) || ("Timeout".equals(paramString));
  }
  
  private static boolean z(String paramString)
  {
    return ("BadAuthentication".equals(paramString)) || ("CaptchaRequired".equals(paramString)) || ("DeviceManagementRequiredOrSyncDisabled".equals(paramString)) || ("NeedPermission".equals(paramString)) || ("NeedsBrowser".equals(paramString)) || ("UserCancel".equals(paramString)) || ("AppDownloadRequired".equals(paramString));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.auth.GoogleAuthUtil
 * JD-Core Version:    0.7.0.1
 */