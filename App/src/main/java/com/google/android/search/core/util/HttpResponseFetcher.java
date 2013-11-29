package com.google.android.search.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import javax.annotation.Nonnull;

public abstract class HttpResponseFetcher<T>
{
  private static boolean isRedirect(int paramInt)
  {
    return (paramInt == 302) || (paramInt == 301);
  }
  
  private static boolean isSuccess(int paramInt)
  {
    return (paramInt == 200) || (paramInt == 204);
  }
  
  public abstract T fetchResponse(@Nonnull HttpURLConnection paramHttpURLConnection, byte[] paramArrayOfByte, int paramInt)
    throws IOException;
  
  protected void sendRequest(HttpURLConnection paramHttpURLConnection, byte[] paramArrayOfByte)
    throws HttpHelper.HttpException, IOException
  {
    int i;
    try
    {
      NetworkUtils.connect(paramHttpURLConnection);
      if (paramArrayOfByte != null) {
        paramHttpURLConnection.getOutputStream().write(paramArrayOfByte);
      }
      i = paramHttpURLConnection.getResponseCode();
      if (isSuccess(i)) {
        return;
      }
      if (isRedirect(i))
      {
        String str = paramHttpURLConnection.getHeaderField("Location");
        throw new HttpHelper.HttpRedirectException(i, paramHttpURLConnection.getResponseMessage(), str);
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new IOException("Bad status line in HTTP response", localNumberFormatException);
    }
    throw new HttpHelper.HttpException(i, paramHttpURLConnection.getResponseMessage());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.HttpResponseFetcher
 * JD-Core Version:    0.7.0.1
 */