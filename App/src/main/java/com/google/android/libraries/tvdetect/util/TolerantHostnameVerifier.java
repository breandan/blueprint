package com.google.android.libraries.tvdetect.util;

import javax.net.ssl.SSLException;
import org.apache.http.conn.ssl.AbstractVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

public class TolerantHostnameVerifier
  extends AbstractVerifier
{
  public void verify(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
    throws SSLException
  {
    try
    {
      SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER.verify(paramString, paramArrayOfString1, paramArrayOfString2);
      return;
    }
    catch (SSLException localSSLException)
    {
      while ((paramArrayOfString1.length > 0) && (paramArrayOfString1[0] != null) && (paramArrayOfString1[0].endsWith("google.com")) && (paramString.endsWith("youtube.com"))) {}
      throw localSSLException;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.util.TolerantHostnameVerifier
 * JD-Core Version:    0.7.0.1
 */