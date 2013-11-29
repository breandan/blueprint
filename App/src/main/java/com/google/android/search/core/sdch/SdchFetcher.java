package com.google.android.search.core.sdch;

import android.util.Log;
import android.util.Pair;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.shared.util.NamingDelayedTaskExecutor;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.voicesearch.logger.EventLogger;
import java.io.IOException;

public class SdchFetcher
{
  private final NamingDelayedTaskExecutor mExecutor;
  private final HttpHelper mHttpHelper;
  
  public SdchFetcher(HttpHelper paramHttpHelper, NamingDelayedTaskExecutor paramNamingDelayedTaskExecutor)
  {
    this.mHttpHelper = paramHttpHelper;
    this.mExecutor = paramNamingDelayedTaskExecutor;
  }
  
  void fetch(final String paramString, final SimpleCallback<Pair<String, byte[]>> paramSimpleCallback)
  {
    this.mExecutor.executeDelayed(new Runnable()
    {
      public void run()
      {
        EventLogger.recordClientEvent(127);
        HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(paramString);
        localGetRequest.setUseCaches(false);
        byte[] arrayOfByte;
        try
        {
          arrayOfByte = SdchFetcher.this.mHttpHelper.rawGet(localGetRequest, 11);
          if (arrayOfByte == null)
          {
            Log.e("Velvet.SdchFetcher", "Null dictionary");
            return;
          }
        }
        catch (IOException localIOException)
        {
          Log.e("Velvet.SdchFetcher", "Error fetching dictionary : " + localIOException.getMessage());
          return;
        }
        Log.i("Velvet.SdchFetcher", "Fetched dictionary, size: " + arrayOfByte.length);
        paramSimpleCallback.onResult(Pair.create(paramString, arrayOfByte));
      }
    }, 3000L);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.sdch.SdchFetcher
 * JD-Core Version:    0.7.0.1
 */