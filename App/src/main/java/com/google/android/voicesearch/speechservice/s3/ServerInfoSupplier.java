package com.google.android.voicesearch.speechservice.s3;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.shared.util.ProtoUtils;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Supplier;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Debug;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.DebugServer;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.HttpServerInfo;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.PairHttpServerInfo;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class ServerInfoSupplier
  implements Supplier<GstaticConfiguration.PairHttpServerInfo>
{
  private GstaticConfiguration.PairHttpServerInfo mCachedInfo;
  private final DebugFeatures mDebugFeatures;
  private final SearchConfig mSearchConfig;
  private String mSearchDomain;
  private final SearchUrlHelper mSearchUrlHelper;
  private final Settings mSettings;
  
  public ServerInfoSupplier(Settings paramSettings, SearchConfig paramSearchConfig, SearchUrlHelper paramSearchUrlHelper, DebugFeatures paramDebugFeatures)
  {
    this.mSettings = paramSettings;
    this.mSearchConfig = paramSearchConfig;
    this.mSearchUrlHelper = paramSearchUrlHelper;
    this.mDebugFeatures = paramDebugFeatures;
  }
  
  @Nullable
  private static GstaticConfiguration.DebugServer getDebugServer(String paramString, Settings paramSettings)
  {
    if (!paramSettings.getConfiguration().hasDebug())
    {
      Log.w("VS.ServerInfoSupplier", "Debug info section not found");
      return null;
    }
    Iterator localIterator = paramSettings.getConfiguration().getDebug().getDebugServerList().iterator();
    while (localIterator.hasNext())
    {
      GstaticConfiguration.DebugServer localDebugServer = (GstaticConfiguration.DebugServer)localIterator.next();
      if ((localDebugServer.hasLabel()) && (localDebugServer.getLabel().equals(paramString))) {
        return localDebugServer;
      }
    }
    Log.e("VS.ServerInfoSupplier", "Invalid or missing override: " + paramString);
    return null;
  }
  
  @Nullable
  private static GstaticConfiguration.PairHttpServerInfo getOverriddenServer(Settings paramSettings, SearchConfig paramSearchConfig)
  {
    String str1 = paramSettings.getDebugS3SandboxOverride();
    if (!TextUtils.isEmpty(str1))
    {
      GstaticConfiguration.DebugServer localDebugServer2 = getDebugServer("Dev SSL/HTTPS", paramSettings);
      if (localDebugServer2 != null)
      {
        Log.i("VS.ServerInfoSupplier", "Using manual S3 server override: " + str1);
        return mergeSandboxInfo(str1, localDebugServer2);
      }
    }
    String str2 = paramSettings.getS3ServerOverride();
    if (TextUtils.isEmpty(str2)) {
      str2 = paramSearchConfig.getS3ServerOverride();
    }
    if (!TextUtils.isEmpty(str2))
    {
      GstaticConfiguration.DebugServer localDebugServer1 = getDebugServer(str2, paramSettings);
      if (localDebugServer1 != null)
      {
        Log.i("VS.ServerInfoSupplier", "Using s3 override: " + str2);
        return localDebugServer1.getPairHttpServerInfo();
      }
    }
    return null;
  }
  
  private static GstaticConfiguration.PairHttpServerInfo maybeReplaceDomain(String paramString, GstaticConfiguration.PairHttpServerInfo paramPairHttpServerInfo)
  {
    if ((paramString.indexOf("sandbox.google") > 0) || (!paramPairHttpServerInfo.getUp().getReplaceHostWithTld())) {
      return paramPairHttpServerInfo;
    }
    GstaticConfiguration.PairHttpServerInfo localPairHttpServerInfo = new GstaticConfiguration.PairHttpServerInfo();
    ProtoUtils.copyOf(paramPairHttpServerInfo, localPairHttpServerInfo);
    Uri.Builder localBuilder1 = Uri.parse(localPairHttpServerInfo.getDown().getUrl()).buildUpon();
    Uri.Builder localBuilder2 = Uri.parse(localPairHttpServerInfo.getUp().getUrl()).buildUpon();
    localPairHttpServerInfo.getDown().setUrl(localBuilder1.authority(paramString).build().toString());
    localPairHttpServerInfo.getUp().setUrl(localBuilder2.authority(paramString).build().toString());
    return localPairHttpServerInfo;
  }
  
  static GstaticConfiguration.HttpServerInfo mergeSandboxInfo(GstaticConfiguration.HttpServerInfo paramHttpServerInfo, String paramString)
  {
    GstaticConfiguration.HttpServerInfo localHttpServerInfo = new GstaticConfiguration.HttpServerInfo();
    ProtoUtils.copyOf(paramHttpServerInfo, localHttpServerInfo);
    Uri localUri = Uri.parse(paramHttpServerInfo.getUrl());
    Uri.Builder localBuilder = localUri.buildUpon();
    localBuilder.path("/" + paramString + localUri.getPath());
    localHttpServerInfo.setUrl(localBuilder.build().toString());
    return localHttpServerInfo;
  }
  
  private static GstaticConfiguration.PairHttpServerInfo mergeSandboxInfo(String paramString, GstaticConfiguration.DebugServer paramDebugServer)
  {
    GstaticConfiguration.PairHttpServerInfo localPairHttpServerInfo = new GstaticConfiguration.PairHttpServerInfo();
    localPairHttpServerInfo.setDown(mergeSandboxInfo(paramDebugServer.getPairHttpServerInfo().getDown(), paramString));
    localPairHttpServerInfo.setUp(mergeSandboxInfo(paramDebugServer.getPairHttpServerInfo().getUp(), paramString));
    return localPairHttpServerInfo;
  }
  
  public GstaticConfiguration.PairHttpServerInfo get()
  {
    if (this.mSettings.getConfiguration().hasDebug())
    {
      GstaticConfiguration.PairHttpServerInfo localPairHttpServerInfo3 = getOverriddenServer(this.mSettings, this.mSearchConfig);
      if (localPairHttpServerInfo3 != null) {
        return localPairHttpServerInfo3;
      }
    }
    String str = this.mSearchUrlHelper.getSearchDomain();
    try
    {
      if (str.equals(this.mSearchDomain))
      {
        GstaticConfiguration.PairHttpServerInfo localPairHttpServerInfo2 = this.mCachedInfo;
        return localPairHttpServerInfo2;
      }
    }
    finally {}
    this.mSearchDomain = str;
    this.mCachedInfo = maybeReplaceDomain(str, this.mSettings.getConfiguration().getPairHttpServerInfo());
    GstaticConfiguration.PairHttpServerInfo localPairHttpServerInfo1 = this.mCachedInfo;
    return localPairHttpServerInfo1;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.speechservice.s3.ServerInfoSupplier
 * JD-Core Version:    0.7.0.1
 */