package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.OkAuthenticator;
import com.squareup.okhttp.OkAuthenticator.Challenge;
import com.squareup.okhttp.OkAuthenticator.Credential;
import java.io.IOException;
import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class HttpAuthenticator
{
  public static final OkAuthenticator SYSTEM_DEFAULT = new OkAuthenticator()
  {
    private InetAddress getConnectToInetAddress(Proxy paramAnonymousProxy, URL paramAnonymousURL)
      throws IOException
    {
      if ((paramAnonymousProxy != null) && (paramAnonymousProxy.type() != Proxy.Type.DIRECT)) {
        return ((InetSocketAddress)paramAnonymousProxy.address()).getAddress();
      }
      return InetAddress.getByName(paramAnonymousURL.getHost());
    }
    
    public OkAuthenticator.Credential authenticate(Proxy paramAnonymousProxy, URL paramAnonymousURL, List<OkAuthenticator.Challenge> paramAnonymousList)
      throws IOException
    {
      Iterator localIterator = paramAnonymousList.iterator();
      while (localIterator.hasNext())
      {
        OkAuthenticator.Challenge localChallenge = (OkAuthenticator.Challenge)localIterator.next();
        PasswordAuthentication localPasswordAuthentication = Authenticator.requestPasswordAuthentication(paramAnonymousURL.getHost(), getConnectToInetAddress(paramAnonymousProxy, paramAnonymousURL), paramAnonymousURL.getPort(), paramAnonymousURL.getProtocol(), localChallenge.getRealm(), localChallenge.getScheme(), paramAnonymousURL, Authenticator.RequestorType.SERVER);
        if (localPasswordAuthentication != null) {
          return OkAuthenticator.Credential.basic(localPasswordAuthentication.getUserName(), new String(localPasswordAuthentication.getPassword()));
        }
      }
      return null;
    }
    
    public OkAuthenticator.Credential authenticateProxy(Proxy paramAnonymousProxy, URL paramAnonymousURL, List<OkAuthenticator.Challenge> paramAnonymousList)
      throws IOException
    {
      Iterator localIterator = paramAnonymousList.iterator();
      while (localIterator.hasNext())
      {
        OkAuthenticator.Challenge localChallenge = (OkAuthenticator.Challenge)localIterator.next();
        InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramAnonymousProxy.address();
        PasswordAuthentication localPasswordAuthentication = Authenticator.requestPasswordAuthentication(localInetSocketAddress.getHostName(), getConnectToInetAddress(paramAnonymousProxy, paramAnonymousURL), localInetSocketAddress.getPort(), paramAnonymousURL.getProtocol(), localChallenge.getRealm(), localChallenge.getScheme(), paramAnonymousURL, Authenticator.RequestorType.PROXY);
        if (localPasswordAuthentication != null) {
          return OkAuthenticator.Credential.basic(localPasswordAuthentication.getUserName(), new String(localPasswordAuthentication.getPassword()));
        }
      }
      return null;
    }
  };
  
  private static List<OkAuthenticator.Challenge> parseChallenges(RawHeaders paramRawHeaders, String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if (i < paramRawHeaders.length())
    {
      if (!paramString.equalsIgnoreCase(paramRawHeaders.getFieldName(i))) {}
      label183:
      for (;;)
      {
        i++;
        break;
        String str1 = paramRawHeaders.getValue(i);
        int j = 0;
        for (;;)
        {
          if (j >= str1.length()) {
            break label183;
          }
          int k = j;
          int m = HeaderParser.skipUntil(str1, j, " ");
          String str2 = str1.substring(k, m).trim();
          int n = HeaderParser.skipWhitespace(str1, m);
          if (!str1.regionMatches(n, "realm=\"", 0, "realm=\"".length())) {
            break;
          }
          int i1 = n + "realm=\"".length();
          int i2 = HeaderParser.skipUntil(str1, i1, "\"");
          String str3 = str1.substring(i1, i2);
          j = HeaderParser.skipWhitespace(str1, 1 + HeaderParser.skipUntil(str1, i2 + 1, ","));
          localArrayList.add(new OkAuthenticator.Challenge(str2, str3));
        }
      }
    }
    return localArrayList;
  }
  
  public static boolean processAuthHeader(OkAuthenticator paramOkAuthenticator, int paramInt, RawHeaders paramRawHeaders1, RawHeaders paramRawHeaders2, Proxy paramProxy, URL paramURL)
    throws IOException
  {
    String str1;
    String str2;
    List localList;
    if (paramInt == 401)
    {
      str1 = "WWW-Authenticate";
      str2 = "Authorization";
      localList = parseChallenges(paramRawHeaders1, str1);
      if (!localList.isEmpty()) {
        break label61;
      }
    }
    for (;;)
    {
      return false;
      if (paramInt == 407)
      {
        str1 = "Proxy-Authenticate";
        str2 = "Proxy-Authorization";
        break;
      }
      throw new IllegalArgumentException();
      label61:
      if (paramRawHeaders1.getResponseCode() == 407) {}
      for (OkAuthenticator.Credential localCredential = paramOkAuthenticator.authenticateProxy(paramProxy, paramURL, localList); localCredential != null; localCredential = paramOkAuthenticator.authenticate(paramProxy, paramURL, localList))
      {
        paramRawHeaders2.set(str2, localCredential.getHeaderValue());
        return true;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.internal.http.HttpAuthenticator
 * JD-Core Version:    0.7.0.1
 */