package com.google.android.libraries.tvdetect.net;

import java.io.IOException;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class AndroidWifiNetwork
  implements WifiNetwork
{
  private final String bssid;
  private final NetworkInterface networkInterface;
  
  public AndroidWifiNetwork(NetworkInterface paramNetworkInterface, String paramString)
  {
    this.networkInterface = paramNetworkInterface;
    this.bssid = paramString;
  }
  
  public String getBssid()
  {
    return this.bssid;
  }
  
  public MulticastSocket newMulticastSocket()
    throws IOException
  {
    if (this.networkInterface == null) {
      throw new IOException("No network interface");
    }
    MulticastSocket localMulticastSocket = new MulticastSocket();
    localMulticastSocket.setNetworkInterface(this.networkInterface);
    localMulticastSocket.setReceiveBufferSize(262144);
    localMulticastSocket.setBroadcast(true);
    return localMulticastSocket;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.net.AndroidWifiNetwork
 * JD-Core Version:    0.7.0.1
 */