package com.google.android.speech.embedded;

import com.google.android.search.core.CoreSearchServices;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.RelationshipNameLookup;
import com.google.android.velvet.VelvetServices;
import com.google.speech.grammar.pumpkin.Validator;

public class PumpkinContactsValidator
  extends Validator
{
  private final ContactLookup mContactLookup;
  private final RelationshipNameLookup mRelationshipNameLookup = VelvetServices.get().getRelationshipNameLookup();
  private final CoreSearchServices mServices;
  
  public PumpkinContactsValidator(CoreSearchServices paramCoreSearchServices, ContactLookup paramContactLookup)
  {
    this.mServices = paramCoreSearchServices;
    this.mContactLookup = paramContactLookup;
  }
  
  public float getPosterior(String paramString)
  {
    if (paramString.length() < 3) {}
    while ((!this.mRelationshipNameLookup.isRelationshipName(paramString)) && (!this.mContactLookup.hasMatchingContacts(paramString))) {
      return 0.0F;
    }
    return PumpkinTagger.moreTokensHigherProbability(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.embedded.PumpkinContactsValidator
 * JD-Core Version:    0.7.0.1
 */