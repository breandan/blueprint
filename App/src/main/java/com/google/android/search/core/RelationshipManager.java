package com.google.android.search.core;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;
import com.google.android.speech.contacts.RelationshipNameLookup;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RelationshipManager
  extends AsyncTask<SearchSettings, Void, Void>
{
  private static final boolean DBG = false;
  private static final String TAG = "RelationshipManager";
  private final Context mContext;
  private final RelationshipNameLookup mRelationshipNameLookup;
  private final Map<String, Set<String>> mRelationshipToContactMapping = Maps.newHashMap();
  private final Resources mResources;
  
  public RelationshipManager(SearchSettings paramSearchSettings, Context paramContext, RelationshipNameLookup paramRelationshipNameLookup)
  {
    this.mContext = paramContext;
    this.mRelationshipNameLookup = paramRelationshipNameLookup;
    this.mResources = paramContext.getResources();
    populateRelationshipToContactMapping(paramSearchSettings, this.mRelationshipToContactMapping);
  }
  
  private static void populateRelationshipToContactMapping(SearchSettings paramSearchSettings, Map<String, Set<String>> paramMap)
  {
    Iterator localIterator = paramSearchSettings.getRelationshipContactInfo().getEntryList().iterator();
    while (localIterator.hasNext())
    {
      RelationshipContactInfo.RelationshipContactEntry localRelationshipContactEntry = (RelationshipContactInfo.RelationshipContactEntry)localIterator.next();
      paramMap.put(localRelationshipContactEntry.getCanonicalRelationship(), Sets.newHashSet(localRelationshipContactEntry.getContactLookupKeyList()));
    }
  }
  
  public void addContactLookupKeyForRelationship(String paramString1, String paramString2)
  {
    String str = this.mRelationshipNameLookup.getCanonicalRelationshipName(paramString1);
    if (str == null) {
      return;
    }
    if (this.mRelationshipToContactMapping.containsKey(str)) {}
    for (Object localObject = (Set)this.mRelationshipToContactMapping.get(str);; localObject = Sets.newHashSet())
    {
      ((Set)localObject).add(paramString2);
      return;
    }
  }
  
  public void clearContactLookupKeyForRelationship(String paramString1, String paramString2)
  {
    String str = this.mRelationshipNameLookup.getCanonicalRelationshipName(paramString1);
    if ((str == null) || (!this.mRelationshipToContactMapping.containsKey(str))) {
      return;
    }
    ((Set)this.mRelationshipToContactMapping.get(str)).remove(paramString2);
  }
  
  protected Void doInBackground(SearchSettings... paramVarArgs)
  {
    publishProgress(new Void[0]);
    saveRelationshipToContactMappingToSettings(paramVarArgs[0]);
    return null;
  }
  
  public Set<String> getContactLookupKeyForRelationship(String paramString)
  {
    String str = this.mRelationshipNameLookup.getCanonicalRelationshipName(paramString);
    if ((str == null) || (!this.mRelationshipToContactMapping.containsKey(str))) {
      return null;
    }
    return (Set)this.mRelationshipToContactMapping.get(str);
  }
  
  protected void onPostExecute(Void paramVoid)
  {
    String str = this.mResources.getString(2131363347);
    Toast.makeText(this.mContext, str, 0).show();
  }
  
  protected void onProgressUpdate(Void... paramVarArgs)
  {
    String str = this.mResources.getString(2131363346);
    Toast.makeText(this.mContext, str, 0).show();
  }
  
  public void saveRelationshipToContactMappingToSettings(SearchSettings paramSearchSettings)
  {
    RelationshipContactInfo localRelationshipContactInfo = new RelationshipContactInfo();
    Iterator localIterator1 = this.mRelationshipToContactMapping.entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      RelationshipContactInfo.RelationshipContactEntry localRelationshipContactEntry = new RelationshipContactInfo.RelationshipContactEntry();
      localRelationshipContactEntry.setCanonicalRelationship((String)localEntry.getKey());
      Iterator localIterator2 = ((Set)localEntry.getValue()).iterator();
      while (localIterator2.hasNext()) {
        localRelationshipContactEntry.addContactLookupKey((String)localIterator2.next());
      }
      localRelationshipContactInfo.addEntry(localRelationshipContactEntry);
    }
    paramSearchSettings.setRelationshipContactInfo(localRelationshipContactInfo);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.RelationshipManager
 * JD-Core Version:    0.7.0.1
 */