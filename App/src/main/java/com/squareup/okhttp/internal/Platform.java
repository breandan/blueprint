package com.squareup.okhttp.internal;

import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import javax.net.ssl.SSLSocket;

public class Platform
{
  private static final Platform PLATFORM = ;
  private Constructor<DeflaterOutputStream> deflaterConstructor;
  
  /* Error */
  private static Platform findPlatform()
  {
    // Byte code:
    //   0: ldc 26
    //   2: ldc 28
    //   4: iconst_0
    //   5: anewarray 30	java/lang/Class
    //   8: invokevirtual 34	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   11: astore_1
    //   12: ldc 36
    //   14: invokestatic 40	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   17: astore 19
    //   19: aload 19
    //   21: astore 12
    //   23: iconst_1
    //   24: anewarray 30	java/lang/Class
    //   27: astore 13
    //   29: aload 13
    //   31: iconst_0
    //   32: getstatic 46	java/lang/Boolean:TYPE	Ljava/lang/Class;
    //   35: aastore
    //   36: aload 12
    //   38: ldc 48
    //   40: aload 13
    //   42: invokevirtual 34	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   45: astore 14
    //   47: aload 12
    //   49: ldc 50
    //   51: iconst_1
    //   52: anewarray 30	java/lang/Class
    //   55: dup
    //   56: iconst_0
    //   57: ldc 52
    //   59: aastore
    //   60: invokevirtual 34	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   63: astore 15
    //   65: new 54	com/squareup/okhttp/internal/Platform$Android41
    //   68: dup
    //   69: aload_1
    //   70: aload 12
    //   72: aload 14
    //   74: aload 15
    //   76: aload 12
    //   78: ldc 56
    //   80: iconst_1
    //   81: anewarray 30	java/lang/Class
    //   84: dup
    //   85: iconst_0
    //   86: ldc 58
    //   88: aastore
    //   89: invokevirtual 34	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   92: aload 12
    //   94: ldc 60
    //   96: iconst_0
    //   97: anewarray 30	java/lang/Class
    //   100: invokevirtual 34	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   103: aconst_null
    //   104: invokespecial 63	com/squareup/okhttp/internal/Platform$Android41:<init>	(Ljava/lang/reflect/Method;Ljava/lang/Class;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Lcom/squareup/okhttp/internal/Platform$1;)V
    //   107: astore 16
    //   109: aload 16
    //   111: areturn
    //   112: astore_0
    //   113: new 2	com/squareup/okhttp/internal/Platform
    //   116: dup
    //   117: invokespecial 64	com/squareup/okhttp/internal/Platform:<init>	()V
    //   120: areturn
    //   121: astore 10
    //   123: ldc 66
    //   125: invokestatic 40	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   128: astore 12
    //   130: goto -107 -> 23
    //   133: astore 17
    //   135: new 68	com/squareup/okhttp/internal/Platform$Android23
    //   138: dup
    //   139: aload_1
    //   140: aload 12
    //   142: aload 14
    //   144: aload 15
    //   146: aconst_null
    //   147: invokespecial 71	com/squareup/okhttp/internal/Platform$Android23:<init>	(Ljava/lang/reflect/Method;Ljava/lang/Class;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Lcom/squareup/okhttp/internal/Platform$1;)V
    //   150: astore 18
    //   152: aload 18
    //   154: areturn
    //   155: astore 11
    //   157: ldc 73
    //   159: invokestatic 40	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   162: astore 5
    //   164: new 75	java/lang/StringBuilder
    //   167: dup
    //   168: invokespecial 76	java/lang/StringBuilder:<init>	()V
    //   171: ldc 73
    //   173: invokevirtual 80	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: ldc 82
    //   178: invokevirtual 80	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: invokevirtual 86	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   184: invokestatic 40	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   187: astore 6
    //   189: new 75	java/lang/StringBuilder
    //   192: dup
    //   193: invokespecial 76	java/lang/StringBuilder:<init>	()V
    //   196: ldc 73
    //   198: invokevirtual 80	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: ldc 88
    //   203: invokevirtual 80	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   206: invokevirtual 86	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   209: invokestatic 40	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   212: astore 7
    //   214: new 75	java/lang/StringBuilder
    //   217: dup
    //   218: invokespecial 76	java/lang/StringBuilder:<init>	()V
    //   221: ldc 73
    //   223: invokevirtual 80	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   226: ldc 90
    //   228: invokevirtual 80	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: invokevirtual 86	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   234: invokestatic 40	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   237: astore 8
    //   239: new 92	com/squareup/okhttp/internal/Platform$JdkWithJettyNpnPlatform
    //   242: dup
    //   243: aload_1
    //   244: aload 5
    //   246: ldc 94
    //   248: iconst_2
    //   249: anewarray 30	java/lang/Class
    //   252: dup
    //   253: iconst_0
    //   254: ldc 96
    //   256: aastore
    //   257: dup
    //   258: iconst_1
    //   259: aload 6
    //   261: aastore
    //   262: invokevirtual 34	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   265: aload 5
    //   267: ldc 98
    //   269: iconst_1
    //   270: anewarray 30	java/lang/Class
    //   273: dup
    //   274: iconst_0
    //   275: ldc 96
    //   277: aastore
    //   278: invokevirtual 34	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   281: aload 7
    //   283: aload 8
    //   285: invokespecial 101	com/squareup/okhttp/internal/Platform$JdkWithJettyNpnPlatform:<init>	(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/Class;Ljava/lang/Class;)V
    //   288: astore 9
    //   290: aload 9
    //   292: areturn
    //   293: astore 4
    //   295: new 103	com/squareup/okhttp/internal/Platform$Java6
    //   298: dup
    //   299: aload_1
    //   300: aconst_null
    //   301: invokespecial 106	com/squareup/okhttp/internal/Platform$Java6:<init>	(Ljava/lang/reflect/Method;Lcom/squareup/okhttp/internal/Platform$1;)V
    //   304: areturn
    //   305: astore_3
    //   306: goto -11 -> 295
    //   309: astore_2
    //   310: goto -153 -> 157
    // Local variable table:
    //   start	length	slot	name	signature
    //   112	1	0	localNoSuchMethodException1	NoSuchMethodException
    //   11	289	1	localMethod1	Method
    //   309	1	2	localNoSuchMethodException2	NoSuchMethodException
    //   305	1	3	localNoSuchMethodException3	NoSuchMethodException
    //   293	1	4	localClassNotFoundException1	java.lang.ClassNotFoundException
    //   162	104	5	localClass1	Class
    //   187	73	6	localClass2	Class
    //   212	70	7	localClass3	Class
    //   237	47	8	localClass4	Class
    //   288	3	9	localJdkWithJettyNpnPlatform	JdkWithJettyNpnPlatform
    //   121	1	10	localClassNotFoundException2	java.lang.ClassNotFoundException
    //   155	1	11	localClassNotFoundException3	java.lang.ClassNotFoundException
    //   21	120	12	localClass5	Class
    //   27	14	13	arrayOfClass	Class[]
    //   45	98	14	localMethod2	Method
    //   63	82	15	localMethod3	Method
    //   107	3	16	localAndroid41	Android41
    //   133	1	17	localNoSuchMethodException4	NoSuchMethodException
    //   150	3	18	localAndroid23	Android23
    //   17	3	19	localClass6	Class
    // Exception table:
    //   from	to	target	type
    //   0	12	112	java/lang/NoSuchMethodException
    //   12	19	121	java/lang/ClassNotFoundException
    //   65	109	133	java/lang/NoSuchMethodException
    //   23	65	155	java/lang/ClassNotFoundException
    //   65	109	155	java/lang/ClassNotFoundException
    //   123	130	155	java/lang/ClassNotFoundException
    //   135	152	155	java/lang/ClassNotFoundException
    //   157	290	293	java/lang/ClassNotFoundException
    //   157	290	305	java/lang/NoSuchMethodException
    //   12	19	309	java/lang/NoSuchMethodException
    //   23	65	309	java/lang/NoSuchMethodException
    //   123	130	309	java/lang/NoSuchMethodException
    //   135	152	309	java/lang/NoSuchMethodException
  }
  
  public static Platform get()
  {
    return PLATFORM;
  }
  
  public void connectSocket(Socket paramSocket, InetSocketAddress paramInetSocketAddress, int paramInt)
    throws IOException
  {
    paramSocket.connect(paramInetSocketAddress, paramInt);
  }
  
  public void enableTlsExtensions(SSLSocket paramSSLSocket, String paramString) {}
  
  public int getMtu(Socket paramSocket)
    throws IOException
  {
    return 1400;
  }
  
  public byte[] getNpnSelectedProtocol(SSLSocket paramSSLSocket)
  {
    return null;
  }
  
  public String getPrefix()
  {
    return "OkHttp";
  }
  
  public void logW(String paramString)
  {
    System.out.println(paramString);
  }
  
  public OutputStream newDeflaterOutputStream(OutputStream paramOutputStream, Deflater paramDeflater, boolean paramBoolean)
  {
    try
    {
      Constructor localConstructor = this.deflaterConstructor;
      if (localConstructor == null)
      {
        Class[] arrayOfClass = new Class[3];
        arrayOfClass[0] = OutputStream.class;
        arrayOfClass[1] = Deflater.class;
        arrayOfClass[2] = Boolean.TYPE;
        localConstructor = DeflaterOutputStream.class.getConstructor(arrayOfClass);
        this.deflaterConstructor = localConstructor;
      }
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = paramOutputStream;
      arrayOfObject[1] = paramDeflater;
      arrayOfObject[2] = Boolean.valueOf(paramBoolean);
      OutputStream localOutputStream = (OutputStream)localConstructor.newInstance(arrayOfObject);
      return localOutputStream;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      throw new UnsupportedOperationException("Cannot SPDY; no SYNC_FLUSH available");
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      if ((localInvocationTargetException.getCause() instanceof RuntimeException)) {}
      for (RuntimeException localRuntimeException = (RuntimeException)localInvocationTargetException.getCause();; localRuntimeException = new RuntimeException(localInvocationTargetException.getCause())) {
        throw localRuntimeException;
      }
    }
    catch (InstantiationException localInstantiationException)
    {
      throw new RuntimeException(localInstantiationException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new AssertionError();
    }
  }
  
  public void setNpnProtocols(SSLSocket paramSSLSocket, byte[] paramArrayOfByte) {}
  
  public void supportTlsIntolerantServer(SSLSocket paramSSLSocket)
  {
    paramSSLSocket.setEnabledProtocols(new String[] { "SSLv3" });
  }
  
  public void tagSocket(Socket paramSocket)
    throws SocketException
  {}
  
  public URI toUriLenient(URL paramURL)
    throws URISyntaxException
  {
    return paramURL.toURI();
  }
  
  public void untagSocket(Socket paramSocket)
    throws SocketException
  {}
  
  private static class Android23
    extends Platform.Java6
  {
    protected final Class<?> openSslSocketClass;
    private final Method setHostname;
    private final Method setUseSessionTickets;
    
    private Android23(Method paramMethod1, Class<?> paramClass, Method paramMethod2, Method paramMethod3)
    {
      super(null);
      this.openSslSocketClass = paramClass;
      this.setUseSessionTickets = paramMethod2;
      this.setHostname = paramMethod3;
    }
    
    public void connectSocket(Socket paramSocket, InetSocketAddress paramInetSocketAddress, int paramInt)
      throws IOException
    {
      try
      {
        paramSocket.connect(paramInetSocketAddress, paramInt);
        return;
      }
      catch (SecurityException localSecurityException)
      {
        IOException localIOException = new IOException("Exception in connect");
        localIOException.initCause(localSecurityException);
        throw localIOException;
      }
    }
    
    public void enableTlsExtensions(SSLSocket paramSSLSocket, String paramString)
    {
      super.enableTlsExtensions(paramSSLSocket, paramString);
      if (this.openSslSocketClass.isInstance(paramSSLSocket)) {}
      try
      {
        Method localMethod = this.setUseSessionTickets;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Boolean.valueOf(true);
        localMethod.invoke(paramSSLSocket, arrayOfObject);
        this.setHostname.invoke(paramSSLSocket, new Object[] { paramString });
        return;
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        throw new RuntimeException(localInvocationTargetException);
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        throw new AssertionError(localIllegalAccessException);
      }
    }
  }
  
  private static class Android41
    extends Platform.Android23
  {
    private final Method getNpnSelectedProtocol;
    private final Method setNpnProtocols;
    
    private Android41(Method paramMethod1, Class<?> paramClass, Method paramMethod2, Method paramMethod3, Method paramMethod4, Method paramMethod5)
    {
      super(paramClass, paramMethod2, paramMethod3, null);
      this.setNpnProtocols = paramMethod4;
      this.getNpnSelectedProtocol = paramMethod5;
    }
    
    public byte[] getNpnSelectedProtocol(SSLSocket paramSSLSocket)
    {
      if (!this.openSslSocketClass.isInstance(paramSSLSocket)) {
        return null;
      }
      try
      {
        byte[] arrayOfByte = (byte[])this.getNpnSelectedProtocol.invoke(paramSSLSocket, new Object[0]);
        return arrayOfByte;
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        throw new RuntimeException(localInvocationTargetException);
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        throw new AssertionError(localIllegalAccessException);
      }
    }
    
    public void setNpnProtocols(SSLSocket paramSSLSocket, byte[] paramArrayOfByte)
    {
      if (!this.openSslSocketClass.isInstance(paramSSLSocket)) {
        return;
      }
      try
      {
        this.setNpnProtocols.invoke(paramSSLSocket, new Object[] { paramArrayOfByte });
        return;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        throw new AssertionError(localIllegalAccessException);
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        throw new RuntimeException(localInvocationTargetException);
      }
    }
  }
  
  private static class Java6
    extends Platform
  {
    private final Method getMtu;
    
    private Java6(Method paramMethod)
    {
      this.getMtu = paramMethod;
    }
    
    public int getMtu(Socket paramSocket)
      throws IOException
    {
      try
      {
        NetworkInterface localNetworkInterface = NetworkInterface.getByInetAddress(paramSocket.getLocalAddress());
        if (localNetworkInterface == null) {
          return super.getMtu(paramSocket);
        }
        int i = ((Integer)this.getMtu.invoke(localNetworkInterface, new Object[0])).intValue();
        return i;
      }
      catch (NullPointerException localNullPointerException)
      {
        return super.getMtu(paramSocket);
      }
      catch (SocketException localSocketException)
      {
        return super.getMtu(paramSocket);
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        throw new AssertionError(localIllegalAccessException);
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        if ((localInvocationTargetException.getCause() instanceof IOException)) {
          throw ((IOException)localInvocationTargetException.getCause());
        }
        throw new RuntimeException(localInvocationTargetException.getCause());
      }
    }
  }
  
  private static class JdkWithJettyNpnPlatform
    extends Platform.Java6
  {
    private final Class<?> clientProviderClass;
    private final Method getMethod;
    private final Method putMethod;
    private final Class<?> serverProviderClass;
    
    public JdkWithJettyNpnPlatform(Method paramMethod1, Method paramMethod2, Method paramMethod3, Class<?> paramClass1, Class<?> paramClass2)
    {
      super(null);
      this.putMethod = paramMethod2;
      this.getMethod = paramMethod3;
      this.clientProviderClass = paramClass1;
      this.serverProviderClass = paramClass2;
    }
    
    public byte[] getNpnSelectedProtocol(SSLSocket paramSSLSocket)
    {
      try
      {
        Platform.JettyNpnProvider localJettyNpnProvider = (Platform.JettyNpnProvider)Proxy.getInvocationHandler(this.getMethod.invoke(null, new Object[] { paramSSLSocket }));
        if ((!Platform.JettyNpnProvider.access$300(localJettyNpnProvider)) && (Platform.JettyNpnProvider.access$400(localJettyNpnProvider) == null))
        {
          Logger.getLogger(OkHttpClient.class.getName()).log(Level.INFO, "NPN callback dropped so SPDY is disabled. Is npn-boot on the boot class path?");
          return null;
        }
        if (!Platform.JettyNpnProvider.access$300(localJettyNpnProvider))
        {
          byte[] arrayOfByte = Platform.JettyNpnProvider.access$400(localJettyNpnProvider).getBytes("US-ASCII");
          return arrayOfByte;
        }
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        throw new AssertionError();
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        throw new AssertionError();
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        throw new AssertionError();
      }
      return null;
    }
    
    public void setNpnProtocols(SSLSocket paramSSLSocket, byte[] paramArrayOfByte)
    {
      try
      {
        ArrayList localArrayList = new ArrayList();
        int j;
        int k;
        for (int i = 0; i < paramArrayOfByte.length; i = j + k)
        {
          j = i + 1;
          k = paramArrayOfByte[i];
          localArrayList.add(new String(paramArrayOfByte, j, k, "US-ASCII"));
        }
        ClassLoader localClassLoader = Platform.class.getClassLoader();
        Class[] arrayOfClass = new Class[2];
        arrayOfClass[0] = this.clientProviderClass;
        arrayOfClass[1] = this.serverProviderClass;
        Object localObject = Proxy.newProxyInstance(localClassLoader, arrayOfClass, new Platform.JettyNpnProvider(localArrayList));
        this.putMethod.invoke(null, new Object[] { paramSSLSocket, localObject });
        return;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        throw new AssertionError(localUnsupportedEncodingException);
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        throw new AssertionError(localInvocationTargetException);
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        throw new AssertionError(localIllegalAccessException);
      }
    }
  }
  
  private static class JettyNpnProvider
    implements InvocationHandler
  {
    private final List<String> protocols;
    private String selected;
    private boolean unsupported;
    
    public JettyNpnProvider(List<String> paramList)
    {
      this.protocols = paramList;
    }
    
    public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
      throws Throwable
    {
      String str = paramMethod.getName();
      Class localClass = paramMethod.getReturnType();
      if (paramArrayOfObject == null) {
        paramArrayOfObject = Util.EMPTY_STRING_ARRAY;
      }
      if ((str.equals("supports")) && (Boolean.TYPE == localClass)) {
        return Boolean.valueOf(true);
      }
      if ((str.equals("unsupported")) && (Void.TYPE == localClass))
      {
        this.unsupported = true;
        return null;
      }
      if ((str.equals("protocols")) && (paramArrayOfObject.length == 0)) {
        return this.protocols;
      }
      if ((str.equals("selectProtocol")) && (String.class == localClass) && (paramArrayOfObject.length == 1) && ((paramArrayOfObject[0] == null) || ((paramArrayOfObject[0] instanceof List))))
      {
        ((List)paramArrayOfObject[0]);
        this.selected = ((String)this.protocols.get(0));
        return this.selected;
      }
      if ((str.equals("protocolSelected")) && (paramArrayOfObject.length == 1))
      {
        this.selected = ((String)paramArrayOfObject[0]);
        return null;
      }
      return paramMethod.invoke(this, paramArrayOfObject);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.internal.Platform
 * JD-Core Version:    0.7.0.1
 */