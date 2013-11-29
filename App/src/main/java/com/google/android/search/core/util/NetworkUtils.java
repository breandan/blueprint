package com.google.android.search.core.util;

import java.io.IOException;
import java.net.HttpURLConnection;

public class NetworkUtils
{
  public static void connect(HttpURLConnection paramHttpURLConnection)
    throws IOException
  {
    try
    {
      paramHttpURLConnection.connect();
      return;
    }
    catch (SecurityException localSecurityException)
    {
      throw new IOException(localSecurityException);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.NetworkUtils
 * JD-Core Version:    0.7.0.1
 */