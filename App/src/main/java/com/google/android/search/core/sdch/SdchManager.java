package com.google.android.search.core.sdch;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.search.core.SearchConfig;
import com.google.android.shared.util.Clock;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SdchManager
{
  private final SimpleCallback<List<SdchDictionary>> mCacheCompletionCallback = new SimpleCallback()
  {
    public void onResult(List<SdchDictionary> paramAnonymousList)
    {
      SdchManager.this.registerCachedDictionaries(paramAnonymousList);
    }
  };
  private boolean mCacheLoaded;
  private final Clock mClock;
  private final Map<String, SdchDictionary> mDictionaries;
  private final SdchDictionaryCache mDictionaryCache;
  private final SdchFetcher mFetcher;
  private final SimpleCallback<Pair<String, byte[]>> mFetcherCallback = new SimpleCallback()
  {
    public void onResult(Pair<String, byte[]> paramAnonymousPair)
    {
      SdchManager.this.processFetchedDictionary(paramAnonymousPair);
    }
  };
  private final Object mLock = new Object();
  private Map<String, Boolean> mPendingFetches;
  private final SearchConfig mSearchConfig;
  
  public SdchManager(SdchFetcher paramSdchFetcher, SdchDictionaryCache paramSdchDictionaryCache, Clock paramClock, SearchConfig paramSearchConfig)
  {
    this.mFetcher = paramSdchFetcher;
    this.mDictionaryCache = paramSdchDictionaryCache;
    this.mClock = paramClock;
    this.mSearchConfig = paramSearchConfig;
    this.mDictionaries = Maps.newHashMap();
    this.mPendingFetches = Maps.newHashMap();
  }
  
  @Nullable
  private SdchDictionary getMatchingDictionary(String paramString1, String paramString2)
  {
    ArrayList localArrayList = Lists.newArrayListWithExpectedSize(1);
    synchronized (this.mLock)
    {
      Iterator localIterator = this.mDictionaries.values().iterator();
      while (localIterator.hasNext())
      {
        SdchDictionary localSdchDictionary = (SdchDictionary)localIterator.next();
        DictionaryMetadata localDictionaryMetadata = localSdchDictionary.getMetadata();
        if ((paramString1.endsWith(localDictionaryMetadata.getDomain())) && (paramString2.equals(localDictionaryMetadata.getPath()))) {
          localArrayList.add(localSdchDictionary);
        }
      }
    }
    if (localArrayList.isEmpty()) {
      return null;
    }
    if (localArrayList.size() == 1) {
      return (SdchDictionary)localArrayList.get(0);
    }
    return (SdchDictionary)localArrayList.get(0);
  }
  
  @Nullable
  static String normalizeDictionaryUrl(String paramString, URL paramURL)
  {
    Uri localUri = Uri.parse(paramString.trim());
    Uri.Builder localBuilder = localUri.buildUpon();
    if (TextUtils.isEmpty(localUri.getScheme())) {
      localBuilder.scheme(paramURL.getProtocol());
    }
    if (TextUtils.isEmpty(localUri.getAuthority())) {
      localBuilder.authority(paramURL.getAuthority());
    }
    return localBuilder.build().toString();
  }
  
  private void processFetchedDictionary(@Nonnull Pair<String, byte[]> paramPair)
  {
    SdchDictionary localSdchDictionary = SdchDictionary.parseAndValidateFrom((String)paramPair.first, (byte[])paramPair.second, this.mClock);
    if (localSdchDictionary != null) {}
    synchronized (this.mLock)
    {
      this.mDictionaries.put(localSdchDictionary.getMetadata().getServerHash(), localSdchDictionary);
      this.mPendingFetches.remove(paramPair.first);
      this.mDictionaryCache.writeDictionaryToCache(localSdchDictionary);
      return;
    }
  }
  
  private InputStream wrapWithSdchStream(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[9];
    ByteStreams.readFully(paramInputStream, arrayOfByte);
    String str = new String(arrayOfByte, 0, 8, "UTF-8");
    if ((arrayOfByte[8] != 0) || (str.length() != 8)) {
      throw new IOException("Invalid dictionary identifier: " + str);
    }
    SdchDictionary localSdchDictionary;
    synchronized (this.mLock)
    {
      localSdchDictionary = (SdchDictionary)this.mDictionaries.get(str);
      if (localSdchDictionary == null) {
        throw new IOException("Advertised invalid dictionary : " + str);
      }
    }
    EventLogger.recordClientEvent(129);
    return new VcDiffDecoderStream(paramInputStream, localSdchDictionary.getBytes(), localSdchDictionary.getOffset(), localSdchDictionary.getLength(), 4096);
  }
  
  void addDictionaryForTesting(String paramString, SdchDictionary paramSdchDictionary)
  {
    synchronized (this.mLock)
    {
      this.mDictionaries.put(paramString, paramSdchDictionary);
      return;
    }
  }
  
  public void advertiseSdch(HttpURLConnection paramHttpURLConnection)
  {
    if (!this.mSearchConfig.isSdchEnabledForSerp()) {}
    SdchDictionary localSdchDictionary;
    do
    {
      return;
      synchronized (this.mLock)
      {
        if (!this.mCacheLoaded) {
          return;
        }
      }
      EventLogger.recordClientEvent(126);
      paramHttpURLConnection.addRequestProperty("Accept-Encoding", "gzip, sdch");
      localSdchDictionary = getMatchingDictionary(paramHttpURLConnection.getURL().getHost(), paramHttpURLConnection.getURL().getPath());
    } while (localSdchDictionary == null);
    String str = localSdchDictionary.getMetadata().getClientHash();
    EventLogger.recordClientEvent(128);
    paramHttpURLConnection.addRequestProperty("Avail-Dictionary", str);
  }
  
  public void initCache()
  {
    this.mDictionaryCache.loadAll(this.mCacheCompletionCallback);
  }
  
  public InputStream maybeDecompressResponse(HttpURLConnection paramHttpURLConnection)
    throws IOException
  {
    Object localObject1;
    if (!this.mSearchConfig.isSdchEnabledForSerp()) {
      localObject1 = paramHttpURLConnection.getInputStream();
    }
    for (;;)
    {
      return localObject1;
      Map localMap = paramHttpURLConnection.getHeaderFields();
      List localList1 = (List)localMap.get("Get-Dictionary");
      List localList2 = (List)localMap.get("Content-Encoding");
      String str2;
      if ((localList1 != null) && (!localList1.isEmpty()) && (!TextUtils.isEmpty((CharSequence)localList1.get(0)))) {
        str2 = normalizeDictionaryUrl((String)localList1.get(0), paramHttpURLConnection.getURL());
      }
      int i;
      int j;
      for (;;)
      {
        String str1;
        synchronized (this.mLock)
        {
          if (!this.mPendingFetches.containsKey(str2))
          {
            this.mPendingFetches.put(str2, Boolean.TRUE);
            this.mFetcher.fetch(str2, this.mFetcherCallback);
          }
          i = 0;
          j = 0;
          if (localList2 == null) {
            break;
          }
          Iterator localIterator = localList2.iterator();
          if (!localIterator.hasNext()) {
            break;
          }
          str1 = (String)localIterator.next();
          if ("sdch".equalsIgnoreCase(str1)) {
            j = 1;
          }
        }
        if ("gzip".equalsIgnoreCase(str1)) {
          i = 1;
        } else {
          Log.e("Velvet.SdchManager", "Unknown content encoding: " + str1);
        }
      }
      if (i != 0) {}
      for (localObject1 = new GZIPInputStream(paramHttpURLConnection.getInputStream()); j != 0; localObject1 = paramHttpURLConnection.getInputStream()) {
        return wrapWithSdchStream((InputStream)localObject1);
      }
    }
  }
  
  void registerCachedDictionaries(List<SdchDictionary> paramList)
  {
    synchronized (this.mLock)
    {
      Iterator localIterator = paramList.iterator();
      if (localIterator.hasNext())
      {
        SdchDictionary localSdchDictionary = (SdchDictionary)localIterator.next();
        this.mDictionaries.put(localSdchDictionary.getMetadata().getServerHash(), localSdchDictionary);
      }
    }
    this.mCacheLoaded = true;
    Log.i("Velvet.SdchManager", "Sdch cache load complete.");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.sdch.SdchManager
 * JD-Core Version:    0.7.0.1
 */