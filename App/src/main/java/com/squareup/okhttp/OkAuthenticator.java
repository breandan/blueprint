package com.squareup.okhttp;

import com.squareup.okhttp.internal.Base64;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

public abstract interface OkAuthenticator
{
  public abstract Credential authenticate(Proxy paramProxy, URL paramURL, List<Challenge> paramList)
    throws IOException;
  
  public abstract Credential authenticateProxy(Proxy paramProxy, URL paramURL, List<Challenge> paramList)
    throws IOException;
  
  public static final class Challenge
  {
    private final String realm;
    private final String scheme;
    
    public Challenge(String paramString1, String paramString2)
    {
      this.scheme = paramString1;
      this.realm = paramString2;
    }
    
    public boolean equals(Object paramObject)
    {
      return ((paramObject instanceof Challenge)) && (((Challenge)paramObject).scheme.equals(this.scheme)) && (((Challenge)paramObject).realm.equals(this.realm));
    }
    
    public String getRealm()
    {
      return this.realm;
    }
    
    public String getScheme()
    {
      return this.scheme;
    }
    
    public int hashCode()
    {
      return this.scheme.hashCode() + 31 * this.realm.hashCode();
    }
    
    public String toString()
    {
      return this.scheme + " realm=\"" + this.realm + "\"";
    }
  }
  
  public static final class Credential
  {
    private final String headerValue;
    
    private Credential(String paramString)
    {
      this.headerValue = paramString;
    }
    
    public static Credential basic(String paramString1, String paramString2)
    {
      try
      {
        String str = Base64.encode((paramString1 + ":" + paramString2).getBytes("ISO-8859-1"));
        Credential localCredential = new Credential("Basic " + str);
        return localCredential;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        throw new AssertionError();
      }
    }
    
    public boolean equals(Object paramObject)
    {
      return ((paramObject instanceof Credential)) && (((Credential)paramObject).headerValue.equals(this.headerValue));
    }
    
    public String getHeaderValue()
    {
      return this.headerValue;
    }
    
    public int hashCode()
    {
      return this.headerValue.hashCode();
    }
    
    public String toString()
    {
      return this.headerValue;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.OkAuthenticator
 * JD-Core Version:    0.7.0.1
 */