package com.google.android.search.core.summons.icing;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.appdatasearch.GlobalSearchApplicationInfo;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.summons.SourceNameHelper;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

class IcingSourcesFactory
{
  private final String TAG = "IcingSourcesFactory";
  private final SearchConfig mConfig;
  private final Context mContext;
  private final Set<InternalCorpus> mInternalCorpora;
  private final SourceNameHelper mSourceNameHelper;
  
  IcingSourcesFactory(Context paramContext, SearchConfig paramSearchConfig, SourceNameHelper paramSourceNameHelper)
  {
    this(paramContext, paramSearchConfig, paramSourceNameHelper, InternalCorpus.getEnabledCorpora(paramSearchConfig));
  }
  
  IcingSourcesFactory(Context paramContext, SearchConfig paramSearchConfig, SourceNameHelper paramSourceNameHelper, Set<InternalCorpus> paramSet)
  {
    this.mContext = paramContext;
    this.mConfig = paramSearchConfig;
    this.mSourceNameHelper = paramSourceNameHelper;
    this.mInternalCorpora = paramSet;
  }
  
  private IcingSource createSingleSource(GlobalSearchApplicationInfo paramGlobalSearchApplicationInfo, String paramString1, ApplicationInfo paramApplicationInfo, String paramString2, String paramString3)
  {
    boolean bool = this.mConfig.isFullSizeIconIcingPackage(paramApplicationInfo.packageName);
    return new IcingSource(paramString1, paramApplicationInfo, paramApplicationInfo.packageName, paramGlobalSearchApplicationInfo.labelId, paramGlobalSearchApplicationInfo.settingsDescriptionId, paramGlobalSearchApplicationInfo.iconId, paramGlobalSearchApplicationInfo.defaultIntentAction, paramGlobalSearchApplicationInfo.defaultIntentData, paramGlobalSearchApplicationInfo.defaultIntentActivity, bool, paramString2, paramString3);
  }
  
  @Nullable
  private ApplicationInfo getApplicationInfo(String paramString)
  {
    try
    {
      ApplicationInfo localApplicationInfo = this.mContext.getPackageManager().getApplicationInfo(paramString, 0);
      return localApplicationInfo;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      Log.w("IcingSourcesFactory", "Could not get application info for package " + paramString, localNameNotFoundException);
    }
    return null;
  }
  
  Collection<IcingSource> createSources(GlobalSearchApplicationInfo... paramVarArgs)
  {
    if ((paramVarArgs == null) || (paramVarArgs.length == 0))
    {
      localObject = Collections.emptyList();
      return localObject;
    }
    Object localObject = Lists.newArrayListWithCapacity(-1 + (paramVarArgs.length + this.mInternalCorpora.size()));
    int i = paramVarArgs.length;
    int j = 0;
    label39:
    GlobalSearchApplicationInfo localGlobalSearchApplicationInfo;
    if (j < i)
    {
      localGlobalSearchApplicationInfo = paramVarArgs[j];
      if (localGlobalSearchApplicationInfo != null) {
        break label70;
      }
      Log.w("IcingSourcesFactory", "GlobalSearchApplicationInfo array contained null value");
    }
    for (;;)
    {
      j++;
      break label39;
      break;
      label70:
      String str1 = localGlobalSearchApplicationInfo.getPackageName();
      if (TextUtils.isEmpty(str1))
      {
        Log.w("IcingSourcesFactory", "no packagename set in global search app info: " + localGlobalSearchApplicationInfo);
      }
      else if (this.mConfig.isIcingSourcePackageIgnored(str1))
      {
        Log.i("IcingSourcesFactory", "ignoring icing source " + str1);
      }
      else
      {
        ApplicationInfo localApplicationInfo = getApplicationInfo(str1);
        if (localApplicationInfo == null)
        {
          Log.w("IcingSourcesFactory", "could not find application info for package " + str1);
        }
        else if (this.mContext.getPackageName().equals(str1))
        {
          Iterator localIterator = this.mInternalCorpora.iterator();
          while (localIterator.hasNext())
          {
            InternalCorpus localInternalCorpus = (InternalCorpus)localIterator.next();
            String str2 = this.mSourceNameHelper.getSourceNameForInternalIcingCorpus(localInternalCorpus);
            String str3 = this.mSourceNameHelper.getCanonicalNameForInternalIcingCorpus(localInternalCorpus);
            ((List)localObject).add(createSingleSource(localInternalCorpus.getApplicationInfo(), str2, localApplicationInfo, str3, localInternalCorpus.getCorpusName()));
          }
        }
        else
        {
          ((List)localObject).add(createSingleSource(localGlobalSearchApplicationInfo, this.mSourceNameHelper.getSourceNameForExternalIcingPackage(str1), localApplicationInfo, this.mSourceNameHelper.getCanonicalNameForExternalIcingPackage(str1), null));
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.IcingSourcesFactory
 * JD-Core Version:    0.7.0.1
 */