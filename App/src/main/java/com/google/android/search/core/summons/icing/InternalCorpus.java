package com.google.android.search.core.summons.icing;

import com.google.android.gms.appdatasearch.GlobalSearchApplicationInfo;
import com.google.android.gms.appdatasearch.util.TableStorageSpec;
import com.google.android.search.core.SearchConfig;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class InternalCorpus
{
  public static final InternalCorpus APPLICATIONS = new InternalCorpus(InternalTableStorageSpec.APPLICATIONS_SPEC, new GlobalSearchApplicationInfo(2131363327, 2131363328, 2130837737, "android.intent.action.MAIN", null, null));
  public static final InternalCorpus CONTACTS = new InternalCorpus(InternalTableStorageSpec.CONTACTS_SPEC, new GlobalSearchApplicationInfo(2131363330, 2131363331, 2130837738, "android.intent.action.VIEW", null, null));
  private static final ImmutableSet<InternalCorpus> INTERNAL_CORPORA = ImmutableSet.of(APPLICATIONS, CONTACTS);
  private final GlobalSearchApplicationInfo mApplicationInfo;
  private final InternalTableStorageSpec mInternalTableStorageSpec;
  
  InternalCorpus(InternalTableStorageSpec paramInternalTableStorageSpec, GlobalSearchApplicationInfo paramGlobalSearchApplicationInfo)
  {
    this.mInternalTableStorageSpec = paramInternalTableStorageSpec;
    this.mApplicationInfo = paramGlobalSearchApplicationInfo;
  }
  
  public static ImmutableSet<InternalCorpus> getEnabledCorpora(String[] paramArrayOfString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
      return INTERNAL_CORPORA;
    }
    ImmutableSet.Builder localBuilder = ImmutableSet.builder();
    List localList = Arrays.asList(paramArrayOfString);
    Iterator localIterator = INTERNAL_CORPORA.iterator();
    label85:
    while (localIterator.hasNext())
    {
      InternalCorpus localInternalCorpus = (InternalCorpus)localIterator.next();
      if (!localList.contains(localInternalCorpus.getCorpusName())) {}
      for (int i = 1;; i = 0)
      {
        if (i == 0) {
          break label85;
        }
        localBuilder.add(localInternalCorpus);
        break;
      }
    }
    return localBuilder.build();
  }
  
  public static Set<InternalCorpus> getEnabledCorpora(SearchConfig paramSearchConfig)
  {
    if (paramSearchConfig.isInternalIcingCorporaEnabled()) {
      return getEnabledCorpora(paramSearchConfig.getDisabledInternalIcingCorpora());
    }
    return Collections.emptySet();
  }
  
  public static boolean isApplicationsCorpusEnabled(SearchConfig paramSearchConfig)
  {
    return getEnabledCorpora(paramSearchConfig.getDisabledInternalIcingCorpora()).contains(APPLICATIONS);
  }
  
  public static boolean isContactsCorpusEnabled(SearchConfig paramSearchConfig)
  {
    return getEnabledCorpora(paramSearchConfig.getDisabledInternalIcingCorpora()).contains(CONTACTS);
  }
  
  public GlobalSearchApplicationInfo getApplicationInfo()
  {
    return this.mApplicationInfo;
  }
  
  public String getCorpusName()
  {
    return this.mInternalTableStorageSpec.getCorpusName();
  }
  
  TableStorageSpec getTableStorageSpec()
  {
    return this.mInternalTableStorageSpec.getTableStorageSpec();
  }
  
  public String toString()
  {
    return "InternalCorpus[" + getCorpusName() + "]";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.InternalCorpus
 * JD-Core Version:    0.7.0.1
 */