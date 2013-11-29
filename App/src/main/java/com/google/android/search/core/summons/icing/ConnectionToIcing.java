package com.google.android.search.core.summons.icing;

import com.google.android.gms.appdatasearch.PhraseAffinityResponse;
import com.google.android.gms.appdatasearch.PhraseAffinitySpecification;
import com.google.android.gms.appdatasearch.QuerySpecification;
import com.google.android.gms.appdatasearch.SearchResults;
import java.io.PrintWriter;

public abstract interface ConnectionToIcing
{
  public abstract PhraseAffinityResponse blockingGetPhraseAffinity(String[] paramArrayOfString, PhraseAffinitySpecification paramPhraseAffinitySpecification);
  
  public abstract SearchResults blockingQuery(String paramString, String[] paramArrayOfString, QuerySpecification paramQuerySpecification);
  
  public abstract void dump(String paramString, PrintWriter paramPrintWriter);
  
  public abstract void startWaitingForQueries();
  
  public abstract void stopWaitingForQueries();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.ConnectionToIcing
 * JD-Core Version:    0.7.0.1
 */