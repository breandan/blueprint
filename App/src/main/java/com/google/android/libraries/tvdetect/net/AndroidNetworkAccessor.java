package com.google.android.libraries.tvdetect.net;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import com.google.android.libraries.tvdetect.util.L;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class AndroidNetworkAccessor
  implements NetworkAccessor
{
  private static final InetAddress LOOPBACK_ADDRESS = ;
  private final Context androidContext;
  
  private AndroidNetworkAccessor(Context paramContext)
  {
    this.androidContext = paramContext;
  }
  
  public static AndroidNetworkAccessor create(Context paramContext)
  {
    return new AndroidNetworkAccessor(paramContext);
  }
  
  private static NetworkInterface findNetworkWithMac(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    String str1 = paramString.toUpperCase(Locale.US).replaceAll("[^ABCDEF\\d]", "");
    if (str1.length() != 12) {
      return null;
    }
    try
    {
      Enumeration localEnumeration = NetworkInterface.getNetworkInterfaces();
      if (localEnumeration == null) {
        return null;
      }
      ArrayList localArrayList = Collections.list(localEnumeration);
      String str2 = str1.substring(2);
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        NetworkInterface localNetworkInterface = (NetworkInterface)localIterator.next();
        if (isWantedNetwork(localNetworkInterface))
        {
          String str3 = getMacAddress(localNetworkInterface);
          if ((str3 != null) && (str3.substring(2).equals(str2))) {
            return localNetworkInterface;
          }
        }
      }
    }
    catch (SocketException localSocketException)
    {
      L.e("Error retrieving local interfaces");
      return null;
    }
    return null;
  }
  
  private WifiInfo getActiveWifiNetworkInfo()
  {
    WifiInfo localWifiInfo;
    if (!((ConnectivityManager)this.androidContext.getSystemService("connectivity")).getNetworkInfo(1).isConnected()) {
      localWifiInfo = null;
    }
    do
    {
      return localWifiInfo;
      WifiManager localWifiManager = (WifiManager)this.androidContext.getSystemService("wifi");
      if (localWifiManager == null) {
        return null;
      }
      localWifiInfo = localWifiManager.getConnectionInfo();
    } while (localWifiInfo != null);
    return null;
  }
  
  private static InetAddress getIpv4Address(NetworkInterface paramNetworkInterface)
  {
    Enumeration localEnumeration = paramNetworkInterface.getInetAddresses();
    if (localEnumeration == null) {
      return null;
    }
    while (localEnumeration.hasMoreElements())
    {
      InetAddress localInetAddress = (InetAddress)localEnumeration.nextElement();
      if ((localInetAddress instanceof Inet4Address)) {
        return localInetAddress;
      }
    }
    return null;
  }
  
  private static InetAddress getLoopbackAddress()
  {
    try
    {
      InetAddress localInetAddress = InetAddress.getByName("127.0.0.1");
      return localInetAddress;
    }
    catch (UnknownHostException localUnknownHostException) {}
    return null;
  }
  
  private static String getMacAddress(NetworkInterface paramNetworkInterface)
  {
    StringBuilder localStringBuilder;
    do
    {
      byte[] arrayOfByte;
      try
      {
        arrayOfByte = paramNetworkInterface.getHardwareAddress();
        if (arrayOfByte == null) {
          return null;
        }
      }
      catch (SocketException localSocketException)
      {
        return null;
      }
      localStringBuilder = new StringBuilder();
      for (int i = 0; i < arrayOfByte.length; i++)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Byte.valueOf(arrayOfByte[i]);
        localStringBuilder.append(String.format("%02X", arrayOfObject));
      }
    } while (localStringBuilder.length() != 12);
    return localStringBuilder.toString();
  }
  
  private static boolean isWantedNetwork(NetworkInterface paramNetworkInterface)
  {
    if (Build.VERSION.SDK_INT >= 9) {
      try
      {
        boolean bool = isWantedNetworkV9(paramNetworkInterface);
        return bool;
      }
      catch (SocketException localSocketException)
      {
        return false;
      }
    }
    return isWantedNetworkV8(paramNetworkInterface);
  }
  
  private static boolean isWantedNetworkV8(NetworkInterface paramNetworkInterface)
  {
    InetAddress localInetAddress = getIpv4Address(paramNetworkInterface);
    if (localInetAddress == null) {}
    while (localInetAddress.equals(LOOPBACK_ADDRESS)) {
      return false;
    }
    return true;
  }
  
  @TargetApi(9)
  private static boolean isWantedNetworkV9(NetworkInterface paramNetworkInterface)
    throws SocketException
  {
    if (!isWantedNetworkV8(paramNetworkInterface)) {}
    while ((paramNetworkInterface.isLoopback()) || (paramNetworkInterface.isPointToPoint())) {
      return false;
    }
    return true;
  }
  
  public WifiNetwork getActiveWifiNetwork(boolean paramBoolean)
  {
    WifiInfo localWifiInfo = getActiveWifiNetworkInfo();
    if (localWifiInfo == null) {
      return null;
    }
    NetworkInterface localNetworkInterface = null;
    if (paramBoolean) {
      localNetworkInterface = findNetworkWithMac(localWifiInfo.getMacAddress());
    }
    return new AndroidWifiNetwork(localNetworkInterface, localWifiInfo.getBSSID());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.net.AndroidNetworkAccessor
 * JD-Core Version:    0.7.0.1
 */