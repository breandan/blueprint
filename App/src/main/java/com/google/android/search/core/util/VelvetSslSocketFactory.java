package com.google.android.search.core.util;

import android.content.Context;
import android.net.SSLSessionCache;
import com.google.android.search.core.SearchConfig;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocketFactory;

public class VelvetSslSocketFactory
  extends SSLSocketFactory
{
  private static final byte[][] NPN_PROTOCOLS = { { 115, 112, 100, 121, 47, 51 }, { 104, 116, 116, 112, 47, 49, 46, 49 } };
  private static SSLSessionCache sSslSessionCache;
  private final boolean mAdvertiseSpdy;
  private final Context mContext;
  private SSLSocketFactory mDelegate;
  private final int mHandshakeTimeoutMs;
  private final SearchConfig mSearchConfig;
  private boolean mSessionCacheSizeAdjusted;
  
  public VelvetSslSocketFactory(int paramInt, Context paramContext, SearchConfig paramSearchConfig, boolean paramBoolean)
  {
    this.mHandshakeTimeoutMs = paramInt;
    this.mContext = paramContext;
    this.mSearchConfig = paramSearchConfig;
    this.mAdvertiseSpdy = paramBoolean;
  }
  
  private static SSLSessionCache createOrGetSessionCache(Context paramContext)
  {
    try
    {
      if (sSslSessionCache == null) {
        sSslSessionCache = new SSLSessionCache(paramContext);
      }
      SSLSessionCache localSSLSessionCache = sSslSessionCache;
      return localSSLSessionCache;
    }
    finally {}
  }
  
  /* Error */
  private Socket maybeAdjustSessionCacheSize(Socket paramSocket)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 60	com/google/android/search/core/util/VelvetSslSocketFactory:mSessionCacheSizeAdjusted	Z
    //   6: istore_3
    //   7: iload_3
    //   8: ifeq +7 -> 15
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: areturn
    //   15: aload_1
    //   16: instanceof 62
    //   19: ifeq -8 -> 11
    //   22: aload_1
    //   23: checkcast 62	javax/net/ssl/SSLSocket
    //   26: astore 4
    //   28: aload 4
    //   30: invokevirtual 66	javax/net/ssl/SSLSocket:getSession	()Ljavax/net/ssl/SSLSession;
    //   33: ifnull -22 -> 11
    //   36: aload 4
    //   38: invokevirtual 66	javax/net/ssl/SSLSocket:getSession	()Ljavax/net/ssl/SSLSession;
    //   41: invokeinterface 72 1 0
    //   46: bipush 25
    //   48: invokeinterface 78 2 0
    //   53: aload_0
    //   54: iconst_1
    //   55: putfield 60	com/google/android/search/core/util/VelvetSslSocketFactory:mSessionCacheSizeAdjusted	Z
    //   58: goto -47 -> 11
    //   61: astore_2
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_2
    //   65: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	66	0	this	VelvetSslSocketFactory
    //   0	66	1	paramSocket	Socket
    //   61	4	2	localObject	Object
    //   6	2	3	bool	boolean
    //   26	11	4	localSSLSocket	javax.net.ssl.SSLSocket
    // Exception table:
    //   from	to	target	type
    //   2	7	61	finally
    //   15	58	61	finally
  }
  
  /* Error */
  private void maybeInitDelegate()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 81	com/google/android/search/core/util/VelvetSslSocketFactory:mDelegate	Ljavax/net/ssl/SSLSocketFactory;
    //   6: ifnonnull +73 -> 79
    //   9: aload_0
    //   10: getfield 45	com/google/android/search/core/util/VelvetSslSocketFactory:mSearchConfig	Lcom/google/android/search/core/SearchConfig;
    //   13: invokevirtual 87	com/google/android/search/core/SearchConfig:isSslSessionCacheEnabled	()Z
    //   16: istore_2
    //   17: aload_0
    //   18: getfield 47	com/google/android/search/core/util/VelvetSslSocketFactory:mAdvertiseSpdy	Z
    //   21: ifeq +61 -> 82
    //   24: aload_0
    //   25: getfield 45	com/google/android/search/core/util/VelvetSslSocketFactory:mSearchConfig	Lcom/google/android/search/core/SearchConfig;
    //   28: invokevirtual 90	com/google/android/search/core/SearchConfig:isSpdyEnabled	()Z
    //   31: ifeq +51 -> 82
    //   34: iconst_1
    //   35: istore_3
    //   36: iload_2
    //   37: ifeq +50 -> 87
    //   40: aload_0
    //   41: getfield 43	com/google/android/search/core/util/VelvetSslSocketFactory:mContext	Landroid/content/Context;
    //   44: invokestatic 92	com/google/android/search/core/util/VelvetSslSocketFactory:createOrGetSessionCache	(Landroid/content/Context;)Landroid/net/SSLSessionCache;
    //   47: astore 4
    //   49: aload_0
    //   50: aload_0
    //   51: getfield 41	com/google/android/search/core/util/VelvetSslSocketFactory:mHandshakeTimeoutMs	I
    //   54: aload 4
    //   56: invokestatic 98	android/net/SSLCertificateSocketFactory:getDefault	(ILandroid/net/SSLSessionCache;)Ljavax/net/ssl/SSLSocketFactory;
    //   59: putfield 81	com/google/android/search/core/util/VelvetSslSocketFactory:mDelegate	Ljavax/net/ssl/SSLSocketFactory;
    //   62: iload_3
    //   63: ifeq +16 -> 79
    //   66: aload_0
    //   67: getfield 81	com/google/android/search/core/util/VelvetSslSocketFactory:mDelegate	Ljavax/net/ssl/SSLSocketFactory;
    //   70: checkcast 94	android/net/SSLCertificateSocketFactory
    //   73: getstatic 35	com/google/android/search/core/util/VelvetSslSocketFactory:NPN_PROTOCOLS	[[B
    //   76: invokevirtual 102	android/net/SSLCertificateSocketFactory:setNpnProtocols	([[B)V
    //   79: aload_0
    //   80: monitorexit
    //   81: return
    //   82: iconst_0
    //   83: istore_3
    //   84: goto -48 -> 36
    //   87: aconst_null
    //   88: astore 4
    //   90: goto -41 -> 49
    //   93: astore_1
    //   94: aload_0
    //   95: monitorexit
    //   96: aload_1
    //   97: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	98	0	this	VelvetSslSocketFactory
    //   93	4	1	localObject	Object
    //   16	21	2	bool	boolean
    //   35	49	3	i	int
    //   47	42	4	localSSLSessionCache	SSLSessionCache
    // Exception table:
    //   from	to	target	type
    //   2	34	93	finally
    //   40	49	93	finally
    //   49	62	93	finally
    //   66	79	93	finally
  }
  
  public Socket createSocket(String paramString, int paramInt)
    throws IOException, UnknownHostException
  {
    maybeInitDelegate();
    return maybeAdjustSessionCacheSize(this.mDelegate.createSocket(paramString, paramInt));
  }
  
  public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
    throws IOException, UnknownHostException
  {
    maybeInitDelegate();
    return maybeAdjustSessionCacheSize(this.mDelegate.createSocket(paramString, paramInt1, paramInetAddress, paramInt2));
  }
  
  public Socket createSocket(InetAddress paramInetAddress, int paramInt)
    throws IOException
  {
    maybeInitDelegate();
    return this.mDelegate.createSocket(paramInetAddress, paramInt);
  }
  
  public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
    throws IOException
  {
    maybeInitDelegate();
    return this.mDelegate.createSocket(paramInetAddress1, paramInt1, paramInetAddress2, paramInt2);
  }
  
  public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
    throws IOException
  {
    maybeInitDelegate();
    return maybeAdjustSessionCacheSize(this.mDelegate.createSocket(paramSocket, paramString, paramInt, paramBoolean));
  }
  
  public String[] getDefaultCipherSuites()
  {
    maybeInitDelegate();
    return this.mDelegate.getDefaultCipherSuites();
  }
  
  public String[] getSupportedCipherSuites()
  {
    maybeInitDelegate();
    return this.mDelegate.getSupportedCipherSuites();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.VelvetSslSocketFactory
 * JD-Core Version:    0.7.0.1
 */