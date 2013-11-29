package com.google.android.libraries.tvdetect.net;

import java.io.IOException;
import java.net.MulticastSocket;

public abstract interface WifiNetwork
{
  public abstract String getBssid();
  
  public abstract MulticastSocket newMulticastSocket()
    throws IOException;
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.net.WifiNetwork
 * JD-Core Version:    0.7.0.1
 */