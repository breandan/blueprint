package com.google.android.search.core.summons.icing;

import com.google.android.gms.appdatasearch.RegisterSectionInfo.Builder;
import com.google.android.gms.appdatasearch.util.TableStorageSpec;
import com.google.android.gms.appdatasearch.util.TableStorageSpec.Builder;

public final class ApplicationsTableStorageSpec
  extends InternalTableStorageSpec
{
  ApplicationsTableStorageSpec()
  {
    super("applications", "_id");
  }
  
  protected TableStorageSpec buildTableStorageSpec(TableStorageSpec.Builder paramBuilder)
  {
    return paramBuilder.addSectionForColumn("display_name", new RegisterSectionInfo.Builder("name").indexPrefixes(true).build()).addSectionForColumn("icon_uri", new RegisterSectionInfo.Builder("icon_uri").noIndex(true).build()).addSectionForColumn("package_name", new RegisterSectionInfo.Builder("package_name").noIndex(true).build()).addSectionForColumn("class_name", new RegisterSectionInfo.Builder("class_name").noIndex(true).build()).score("score").addGlobalSearchSectionTemplate("text1", 2131363316).addGlobalSearchSectionTemplate("intent_action", 2131363317).addGlobalSearchSectionTemplate("intent_data", 2131363318).addGlobalSearchSectionTemplate("icon", 2131363319).untrimmable().version("2").build();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.ApplicationsTableStorageSpec
 * JD-Core Version:    0.7.0.1
 */