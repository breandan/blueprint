package com.google.android.search.core.summons;

import android.content.ComponentName;
import android.net.Uri;
import com.google.android.gms.appdatasearch.SearchResults.Result;
import com.google.android.search.core.summons.icing.InternalCorpus;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;

public class SourceNameHelper
{
  private static final Map<InternalCorpus, String> INTERNAL_ICING_CORPORA_TO_CANONICAL_SOURCE_NAME = ImmutableMap.builder().put(InternalCorpus.APPLICATIONS, "applications").put(InternalCorpus.CONTACTS, "contacts").build();
  public static final Map<Uri, String> SUGGEST_URI_TO_CANONICAL_SOURCE_NAME = ImmutableMap.builder().put(Uri.parse("content://applications/search_suggest_query"), "applications").put(Uri.parse("content://com.android.contacts/search_suggest_query"), "contacts").build();
  private final Map<InternalCorpus, String> mInternalIcingCorporaToCanonicalName;
  private final String mMyPackageName;
  private final Map<Uri, String> mSuggestUriToCanonicalName;
  
  public SourceNameHelper(String paramString)
  {
    this(paramString, SUGGEST_URI_TO_CANONICAL_SOURCE_NAME, INTERNAL_ICING_CORPORA_TO_CANONICAL_SOURCE_NAME);
  }
  
  SourceNameHelper(String paramString, Map<Uri, String> paramMap, Map<InternalCorpus, String> paramMap1)
  {
    this.mMyPackageName = paramString;
    this.mSuggestUriToCanonicalName = paramMap;
    this.mInternalIcingCorporaToCanonicalName = paramMap1;
  }
  
  private String getSourceNameForInternalIcingCorpus(String paramString)
  {
    return this.mMyPackageName + "/" + paramString;
  }
  
  public String getCanonicalNameForExternalIcingPackage(String paramString)
  {
    return paramString;
  }
  
  public String getCanonicalNameForInternalIcingCorpus(InternalCorpus paramInternalCorpus)
  {
    Preconditions.checkArgument(this.mInternalIcingCorporaToCanonicalName.containsKey(paramInternalCorpus));
    return (String)this.mInternalIcingCorporaToCanonicalName.get(paramInternalCorpus);
  }
  
  public String getCanonicalNameForSearchableSource(Uri paramUri, String paramString)
  {
    if (this.mSuggestUriToCanonicalName.containsKey(paramUri)) {
      return (String)this.mSuggestUriToCanonicalName.get(paramUri);
    }
    return paramString;
  }
  
  public String getSourceNameForExternalIcingPackage(String paramString)
  {
    return paramString;
  }
  
  public String getSourceNameForIcingResult(SearchResults.Result paramResult)
  {
    if (this.mMyPackageName.equals(paramResult.getPackageName())) {
      return getSourceNameForInternalIcingCorpus(paramResult.getCorpus());
    }
    return getSourceNameForExternalIcingPackage(paramResult.getPackageName());
  }
  
  public String getSourceNameForInternalIcingCorpus(InternalCorpus paramInternalCorpus)
  {
    return getSourceNameForInternalIcingCorpus(paramInternalCorpus.getCorpusName());
  }
  
  public String getSourceNameForSearchableSource(ComponentName paramComponentName)
  {
    return paramComponentName.flattenToShortString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.SourceNameHelper
 * JD-Core Version:    0.7.0.1
 */