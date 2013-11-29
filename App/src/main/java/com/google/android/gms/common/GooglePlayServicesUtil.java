package com.google.android.gms.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.R.string;
import com.google.android.gms.internal.dm;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public final class GooglePlayServicesUtil
{
  private static final byte[][] mA;
  private static final byte[][] mB;
  static final byte[][] mC;
  public static boolean mD = false;
  public static boolean mE = false;
  static boolean mF = false;
  private static int mG = -1;
  private static final Object mH = new Object();
  static final byte[][] mw;
  static final byte[][] mx;
  static final byte[][] my;
  static final byte[][] mz;
  
  static
  {
    byte[][] arrayOfByte1 = new byte[2][];
    arrayOfByte1[0] = A("");
    arrayOfByte1[1] = A("");
    mw = arrayOfByte1;
    byte[][] arrayOfByte2 = new byte[2][];
    arrayOfByte2[0] = A("");
    arrayOfByte2[1] = A("");
    mx = arrayOfByte2;
    byte[][] arrayOfByte3 = new byte[1][];
    arrayOfByte3[0] = A("");
    my = arrayOfByte3;
    byte[][] arrayOfByte4 = new byte[2][];
    arrayOfByte4[0] = A("");
    arrayOfByte4[1] = A("");
    mz = arrayOfByte4;
    byte[][][] arrayOfByte = new byte[4][][];
    arrayOfByte[0] = mw;
    arrayOfByte[1] = mx;
    arrayOfByte[2] = my;
    arrayOfByte[3] = mz;
    mA = a(arrayOfByte);
    byte[][] arrayOfByte5 = new byte[3][];
    arrayOfByte5[0] = mw[0];
    arrayOfByte5[1] = mx[0];
    arrayOfByte5[2] = mz[0];
    mB = arrayOfByte5;
    byte[][] arrayOfByte6 = new byte[1][];
    arrayOfByte6[0] = A("");
    mC = arrayOfByte6;
  }
  
  private static byte[] A(String paramString)
  {
    try
    {
      byte[] arrayOfByte = paramString.getBytes("ISO-8859-1");
      return arrayOfByte;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError(localUnsupportedEncodingException);
    }
  }
  
  private static byte[] a(PackageInfo paramPackageInfo, byte[]... paramVarArgs)
  {
    CertificateFactory localCertificateFactory;
    try
    {
      localCertificateFactory = CertificateFactory.getInstance("X509");
      if (paramPackageInfo.signatures.length != 1)
      {
        Log.w("GooglePlayServicesUtil", "Package has more than one signature.");
        return null;
      }
    }
    catch (CertificateException localCertificateException1)
    {
      Log.w("GooglePlayServicesUtil", "Could not get certificate instance.");
      return null;
    }
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramPackageInfo.signatures[0].toByteArray());
    byte[] arrayOfByte1;
    label148:
    for (;;)
    {
      try
      {
        X509Certificate localX509Certificate = (X509Certificate)localCertificateFactory.generateCertificate(localByteArrayInputStream);
        int i;
        byte[] arrayOfByte2;
        i++;
      }
      catch (CertificateException localCertificateException2)
      {
        try
        {
          localX509Certificate.checkValidity();
          arrayOfByte1 = paramPackageInfo.signatures[0].toByteArray();
          i = 0;
          if (i >= paramVarArgs.length) {
            break;
          }
          arrayOfByte2 = paramVarArgs[i];
          if (!Arrays.equals(arrayOfByte2, arrayOfByte1)) {
            break label148;
          }
          return arrayOfByte2;
        }
        catch (CertificateExpiredException localCertificateExpiredException)
        {
          Log.w("GooglePlayServicesUtil", "Certificate has expired.");
          return null;
        }
        catch (CertificateNotYetValidException localCertificateNotYetValidException)
        {
          Log.w("GooglePlayServicesUtil", "Certificate is not yet valid.");
          return null;
        }
        localCertificateException2 = localCertificateException2;
        Log.w("GooglePlayServicesUtil", "Could not generate certificate.");
        return null;
      }
    }
    if (Log.isLoggable("GooglePlayServicesUtil", 2)) {
      Log.v("GooglePlayServicesUtil", "Signature not valid.  Found: \n" + Base64.encodeToString(arrayOfByte1, 0));
    }
    return null;
  }
  
  private static byte[][] a(byte[][]... paramVarArgs)
  {
    int i = paramVarArgs.length;
    int j = 0;
    int k = 0;
    while (j < i)
    {
      k += paramVarArgs[j].length;
      j++;
    }
    byte[][] arrayOfByte = new byte[k][];
    int m = paramVarArgs.length;
    int n = 0;
    int i2;
    for (int i1 = 0; n < m; i1 = i2)
    {
      byte[][] arrayOfByte1 = paramVarArgs[n];
      i2 = i1;
      int i3 = 0;
      while (i3 < arrayOfByte1.length)
      {
        int i4 = i2 + 1;
        arrayOfByte[i2] = arrayOfByte1[i3];
        i3++;
        i2 = i4;
      }
      n++;
    }
    return arrayOfByte;
  }
  
  static boolean aA(int paramInt)
  {
    boolean bool = true;
    switch (aB(paramInt))
    {
    default: 
      bool = false;
    case 1: 
    case 0: 
      do
      {
        return bool;
      } while (!aM());
      return false;
    }
    return false;
  }
  
  private static int aB(int paramInt)
  {
    if (paramInt == -1) {
      paramInt = 2;
    }
    return paramInt;
  }
  
  public static boolean aM()
  {
    if (mD) {
      return mE;
    }
    return "user".equals(Build.TYPE);
  }
  
  public static PendingIntent getErrorPendingIntent(int paramInt1, Context paramContext, int paramInt2)
  {
    Intent localIntent = getGooglePlayServicesAvailabilityRecoveryIntent(paramContext, paramInt1, -1);
    if (localIntent == null) {
      return null;
    }
    return PendingIntent.getActivity(paramContext, paramInt2, localIntent, 268435456);
  }
  
  public static String getErrorString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN_ERROR_CODE";
    case 0: 
      return "SUCCESS";
    case 1: 
      return "SERVICE_MISSING";
    case 2: 
      return "SERVICE_VERSION_UPDATE_REQUIRED";
    case 3: 
      return "SERVICE_DISABLED";
    case 4: 
      return "SIGN_IN_REQUIRED";
    case 5: 
      return "INVALID_ACCOUNT";
    case 6: 
      return "RESOLUTION_REQUIRED";
    case 7: 
      return "NETWORK_ERROR";
    case 8: 
      return "INTERNAL_ERROR";
    case 9: 
      return "SERVICE_INVALID";
    case 10: 
      return "DEVELOPER_ERROR";
    case 11: 
      return "LICENSE_CHECK_FAILED";
    }
    return "DATE_INVALID";
  }
  
  public static Intent getGooglePlayServicesAvailabilityRecoveryIntent(Context paramContext, int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      return null;
    case 1: 
    case 2: 
      if (aA(paramInt2))
      {
        if (o(paramContext)) {
          return dm.J("com.google.android.gms");
        }
        return dm.I("com.google.android.apps.bazaar");
      }
      return dm.I("com.google.android.gms");
    case 3: 
      return dm.G("com.google.android.gms");
    }
    return dm.bm();
  }
  
  public static int isGooglePlayServicesAvailable(Context paramContext)
  {
    PackageManager localPackageManager = paramContext.getPackageManager();
    try
    {
      paramContext.getResources().getString(R.string.common_google_play_services_unknown_issue);
      if (System.currentTimeMillis() < 1227312000288L) {
        return 12;
      }
    }
    catch (Throwable localThrowable)
    {
      for (;;)
      {
        Log.e("GooglePlayServicesUtil", "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included.");
      }
      n(paramContext);
      byte[] arrayOfByte;
      try
      {
        PackageInfo localPackageInfo1 = localPackageManager.getPackageInfo("com.android.vending", 64);
        arrayOfByte = a(localPackageInfo1, mw);
        if (arrayOfByte == null)
        {
          Log.w("GooglePlayServicesUtil", "Google Play Store signature invalid.");
          return 9;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException1)
      {
        Log.w("GooglePlayServicesUtil", "Google Play Store is missing.");
        return 9;
      }
      PackageInfo localPackageInfo2;
      try
      {
        localPackageInfo2 = localPackageManager.getPackageInfo("com.google.android.gms", 64);
        if (a(localPackageInfo2, new byte[][] { arrayOfByte }) == null)
        {
          Log.w("GooglePlayServicesUtil", "Google Play services signature invalid.");
          return 9;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException2)
      {
        Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
        return 1;
      }
      if (localPackageInfo2.versionCode < 4023500)
      {
        Log.w("GooglePlayServicesUtil", "Google Play services out of date.  Requires 4023500 but found " + localPackageInfo2.versionCode);
        return 2;
      }
      try
      {
        ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo("com.google.android.gms", 0);
        if (!localApplicationInfo.enabled) {
          return 3;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException3)
      {
        Log.wtf("GooglePlayServicesUtil", "Google Play services missing when getting application info.");
        localNameNotFoundException3.printStackTrace();
        return 1;
      }
    }
    return 0;
  }
  
  public static void m(Context paramContext)
    throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException
  {
    int i = isGooglePlayServicesAvailable(paramContext);
    if (i != 0)
    {
      Intent localIntent = getGooglePlayServicesAvailabilityRecoveryIntent(paramContext, i, -1);
      Log.e("GooglePlayServicesUtil", "GooglePlayServices not available due to error " + i);
      if (localIntent == null) {
        throw new GooglePlayServicesNotAvailableException(i);
      }
      throw new GooglePlayServicesRepairableException(i, "Google Play Services not available", localIntent);
    }
  }
  
  private static void n(Context paramContext)
  {
    try
    {
      ApplicationInfo localApplicationInfo2 = paramContext.getPackageManager().getApplicationInfo(paramContext.getPackageName(), 128);
      localApplicationInfo1 = localApplicationInfo2;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        Bundle localBundle;
        Log.wtf("GooglePlayServicesUtil", "This should never happen.", localNameNotFoundException);
        ApplicationInfo localApplicationInfo1 = null;
      }
    }
    localBundle = localApplicationInfo1.metaData;
    if (localBundle != null)
    {
      int i = localBundle.getInt("com.google.android.gms.version");
      if (i == 4023500) {
        return;
      }
      throw new IllegalStateException("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected 4023500 but found " + i + ".  You must have the" + " following declaration within the <application> element: " + "    <meta-data android:name=\"" + "com.google.android.gms.version" + "\" android:value=\"@integer/google_play_services_version\" />");
    }
    throw new IllegalStateException("A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
  }
  
  private static boolean o(Context paramContext)
  {
    boolean bool;
    if (mD) {
      bool = mF;
    }
    for (;;)
    {
      return bool;
      try
      {
        byte[] arrayOfByte = a(paramContext.getPackageManager().getPackageInfo("com.google.android.apps.bazaar", 64), mC);
        bool = false;
        if (arrayOfByte != null) {
          return true;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.common.GooglePlayServicesUtil
 * JD-Core Version:    0.7.0.1
 */