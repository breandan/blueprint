package com.google.android.search.core.google;

import android.net.Uri;
import android.util.Log;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.util.ByteArrayWithHeadersResponse;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.search.core.util.UriRequest;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.gws.plugins.searchapp.GsaConfigurationProto.GsaExperiments;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class DownloadExperimentConfigTask
  implements Callable<Void>
{
  private final boolean mExitAfterDownloadingConfig;
  private final GsaConfigFlags mGsaConfigFlags;
  private final HttpHelper mHttpHelper;
  private final SearchSettings mSettings;
  private final SearchUrlHelper mUrlHelper;
  
  public DownloadExperimentConfigTask(SearchSettings paramSearchSettings, SearchUrlHelper paramSearchUrlHelper, HttpHelper paramHttpHelper, GsaConfigFlags paramGsaConfigFlags, boolean paramBoolean)
  {
    this.mHttpHelper = paramHttpHelper;
    this.mUrlHelper = paramSearchUrlHelper;
    this.mSettings = paramSearchSettings;
    this.mGsaConfigFlags = paramGsaConfigFlags;
    this.mExitAfterDownloadingConfig = paramBoolean;
  }
  
  private void maybeUpdateChecksum(Map<String, List<String>> paramMap)
  {
    if ((paramMap.containsKey("ETag")) && (!((List)paramMap.get("ETag")).isEmpty())) {
      this.mSettings.setGsaConfigChecksum((String)((List)paramMap.get("ETag")).get(0));
    }
  }
  
  public Void call()
  {
    fetchExperimentConfig();
    return null;
  }
  
  void fetchExperimentConfig()
  {
    if (!this.mExitAfterDownloadingConfig) {
      ExtraPreconditions.checkNotMainThread();
    }
    try
    {
      UriRequest localUriRequest = this.mUrlHelper.getGsaExperimentConfigRequest();
      HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(localUriRequest.getUri().toString(), localUriRequest.getHeaders());
      localGetRequest.setUseCaches(false);
      ByteArrayWithHeadersResponse localByteArrayWithHeadersResponse = this.mHttpHelper.rawGetWithHeaders(localGetRequest, 13);
      GsaConfigurationProto.GsaExperiments localGsaExperiments = GsaConfigurationProto.GsaExperiments.parseFrom(localByteArrayWithHeadersResponse.getResponse());
      this.mSettings.setGsaConfigServer(localGsaExperiments);
      maybeUpdateChecksum(localByteArrayWithHeadersResponse.getResponseHeaders());
      if (!this.mExitAfterDownloadingConfig)
      {
        this.mGsaConfigFlags.updateGsaConfig(localGsaExperiments, this.mSettings.getGsaConfigOverride());
        return;
      }
      Log.e("Search.DownloadExperimentConfigTask", "****RECEIVED A FORCE_RESTART: CRASHING THE APP ****");
      System.exit(0);
      return;
    }
    catch (IOException localIOException)
    {
      if (((localIOException instanceof HttpHelper.HttpException)) && (((HttpHelper.HttpException)localIOException).getStatusCode() == 304))
      {
        Log.i("Search.DownloadExperimentConfigTask", "Experiment config has not changed.");
        return;
      }
      if (this.mExitAfterDownloadingConfig)
      {
        Log.e("Search.DownloadExperimentConfigTask", "Received a force_restart *and* /ajax/searchapp failed. Clear the experiment config so we use baked in default values");
        this.mGsaConfigFlags.clearGsaConfigFlags();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.DownloadExperimentConfigTask
 * JD-Core Version:    0.7.0.1
 */