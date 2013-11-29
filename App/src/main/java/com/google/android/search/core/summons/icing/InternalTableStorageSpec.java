package com.google.android.search.core.summons.icing;

import com.google.android.gms.appdatasearch.util.TableStorageSpec;
import com.google.android.gms.appdatasearch.util.TableStorageSpec.Builder;

public abstract class InternalTableStorageSpec
{
  static final ApplicationsTableStorageSpec APPLICATIONS_SPEC = new ApplicationsTableStorageSpec();
  static final ContactsTableStorageSpec CONTACTS_SPEC = new ContactsTableStorageSpec();
  static final InternalTableStorageSpec[] CORPUS_SPECS;
  private final String mTableName;
  private TableStorageSpec mTableSpec;
  private final String mUriColumn;
  
  static
  {
    InternalTableStorageSpec[] arrayOfInternalTableStorageSpec = new InternalTableStorageSpec[2];
    arrayOfInternalTableStorageSpec[0] = APPLICATIONS_SPEC;
    arrayOfInternalTableStorageSpec[1] = CONTACTS_SPEC;
    CORPUS_SPECS = arrayOfInternalTableStorageSpec;
  }
  
  public InternalTableStorageSpec(String paramString1, String paramString2)
  {
    this.mTableName = paramString1;
    this.mUriColumn = paramString2;
  }
  
  static String[] getCorpusNames()
  {
    String[] arrayOfString = new String[CORPUS_SPECS.length];
    for (int i = 0; i < CORPUS_SPECS.length; i++) {
      arrayOfString[i] = CORPUS_SPECS[i].getCorpusName();
    }
    return arrayOfString;
  }
  
  static TableStorageSpec[] getTableStorageSpecs()
  {
    TableStorageSpec[] arrayOfTableStorageSpec = new TableStorageSpec[CORPUS_SPECS.length];
    for (int i = 0; i < CORPUS_SPECS.length; i++) {
      arrayOfTableStorageSpec[i] = CORPUS_SPECS[i].getTableStorageSpec();
    }
    return arrayOfTableStorageSpec;
  }
  
  protected abstract TableStorageSpec buildTableStorageSpec(TableStorageSpec.Builder paramBuilder);
  
  public String getCorpusName()
  {
    return TableStorageSpec.getCorpusName(this.mTableName, this.mUriColumn);
  }
  
  final TableStorageSpec getTableStorageSpec()
  {
    if (this.mTableSpec == null) {
      this.mTableSpec = buildTableStorageSpec(TableStorageSpec.builder(this.mTableName).uriColumn(this.mUriColumn));
    }
    return this.mTableSpec;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.InternalTableStorageSpec
 * JD-Core Version:    0.7.0.1
 */