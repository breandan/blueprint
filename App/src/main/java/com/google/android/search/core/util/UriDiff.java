package com.google.android.search.core.util;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nonnull;

public class UriDiff
{
  public static final UriDiff SAME = new UriDiff();
  private final boolean mAuthority;
  @Nonnull
  private final Set<String> mFragmentParams;
  private final boolean mPath;
  @Nonnull
  private final Set<String> mQueryParams;
  private final boolean mScheme;
  
  private UriDiff()
  {
    this.mScheme = false;
    this.mAuthority = false;
    this.mPath = false;
    this.mQueryParams = Collections.emptySet();
    this.mFragmentParams = Collections.emptySet();
  }
  
  private UriDiff(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Set<String> paramSet1, Set<String> paramSet2)
  {
    this.mScheme = paramBoolean1;
    this.mAuthority = paramBoolean2;
    this.mPath = paramBoolean3;
    this.mQueryParams = ImmutableSet.copyOf(paramSet1);
    this.mFragmentParams = ImmutableSet.copyOf(paramSet2);
  }
  
  public static UriDiff diff(Uri paramUri1, Uri paramUri2)
  {
    if (Objects.equal(paramUri1, paramUri2)) {
      return SAME;
    }
    if (paramUri1 == null) {
      return new UriDiff(true, true, true, paramUri2.getQueryParameterNames(), fragmentAsQuery(paramUri2).getQueryParameterNames());
    }
    if (paramUri2 == null) {
      return new UriDiff(true, true, true, paramUri1.getQueryParameterNames(), fragmentAsQuery(paramUri1).getQueryParameterNames());
    }
    boolean bool1;
    if (!TextUtils.equals(paramUri1.getScheme(), paramUri2.getScheme()))
    {
      bool1 = true;
      if (TextUtils.equals(paramUri1.getAuthority(), paramUri2.getAuthority())) {
        break label161;
      }
    }
    label161:
    for (boolean bool2 = true;; bool2 = false)
    {
      boolean bool3 = TextUtils.equals(paramUri1.getPath(), paramUri2.getPath());
      boolean bool4 = false;
      if (!bool3) {
        bool4 = true;
      }
      Set localSet1 = diffQueryParams(paramUri1, paramUri2);
      Set localSet2 = diffQueryParams(fragmentAsQuery(paramUri1), fragmentAsQuery(paramUri2));
      return new UriDiff(bool1, bool2, bool4, localSet1, localSet2);
      bool1 = false;
      break;
    }
  }
  
  private static Set<String> diffQueryParams(Uri paramUri1, Uri paramUri2)
  {
    Set localSet1 = paramUri1.getQueryParameterNames();
    Set localSet2 = paramUri2.getQueryParameterNames();
    HashSet localHashSet = new HashSet(Sets.union(localSet1, localSet2));
    Iterator localIterator = localHashSet.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if ((localSet1.contains(str)) && (localSet2.contains(str)) && (Objects.equal(paramUri1.getQueryParameters(str), paramUri2.getQueryParameters(str)))) {
        localIterator.remove();
      }
    }
    return localHashSet;
  }
  
  private static Uri fragmentAsQuery(Uri paramUri)
  {
    return paramUri.buildUpon().encodedQuery(paramUri.getFragment()).fragment(null).build();
  }
  
  public boolean authorityDifferent()
  {
    return this.mAuthority;
  }
  
  public Set<String> fragmentDiffs()
  {
    return this.mFragmentParams;
  }
  
  public boolean pathDifferent()
  {
    return this.mPath;
  }
  
  public Set<String> queryDiffs()
  {
    return this.mQueryParams;
  }
  
  public boolean schemeDifferent()
  {
    return this.mScheme;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.UriDiff
 * JD-Core Version:    0.7.0.1
 */