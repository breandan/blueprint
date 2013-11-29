package com.google.android.speech.contacts;

import com.google.android.search.core.Feature;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RelationshipNameLookup
{
  private static final Map<String, String> sRelations = ImmutableMap.builder().put("mom", "Mother").put("mommy", "Mother").put("mummy", "Mother").put("mum", "Mother").put("mother", "Mother").put("pop", "Father").put("papa", "Father").put("dad", "Father").put("daddy", "Father").put("father", "Father").put("sis", "Sister").put("sister", "Sister").put("bro", "Brother").put("brother", "Brother").put("grandma", "GrandMother").put("grandmother", "GrandMother").put("grandpa", "GrandFather").put("granddad", "GrandFather").put("grandfather", "GrandFather").put("uncle", "Uncle").put("aunt", "Aunt").put("nephew", "Nephew").put("niece", "Niece").put("cousin", "Cousin").put("husband", "Spouse").put("hubby", "Spouse").put("life partner", "Spouse").put("wife", "Spouse").put("spouse", "Spouse").build();
  private String mLocaleBcp47;
  
  public RelationshipNameLookup(@Nonnull String paramString)
  {
    Preconditions.checkNotNull(paramString);
    this.mLocaleBcp47 = paramString;
  }
  
  @Nullable
  public String getCanonicalRelationshipName(@Nullable String paramString)
  {
    if ((!Feature.CONTACT_LOOKUP_BY_RELATIONSHIP_NAME.isEnabled()) || (paramString == null) || (!this.mLocaleBcp47.startsWith("en"))) {
      return null;
    }
    return (String)sRelations.get(paramString.toLowerCase(Locale.US));
  }
  
  public boolean isRelationshipName(@Nullable String paramString)
  {
    if ((!Feature.CONTACT_LOOKUP_BY_RELATIONSHIP_NAME.isEnabled()) || (paramString == null) || (!this.mLocaleBcp47.startsWith("en"))) {
      return false;
    }
    return sRelations.containsKey(paramString.toLowerCase(Locale.US));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.RelationshipNameLookup
 * JD-Core Version:    0.7.0.1
 */