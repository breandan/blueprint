package com.google.android.search.core.summons.icing;

import com.google.android.gms.appdatasearch.RegisterSectionInfo;
import com.google.android.gms.appdatasearch.RegisterSectionInfo.Builder;
import com.google.android.gms.appdatasearch.SectionFeature;
import com.google.android.gms.appdatasearch.util.TableStorageSpec;
import com.google.android.gms.appdatasearch.util.TableStorageSpec.Builder;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.velvet.VelvetServices;

public final class ContactsTableStorageSpec
  extends InternalTableStorageSpec
{
  ContactsTableStorageSpec()
  {
    super("contacts", "contact_id");
  }
  
  protected TableStorageSpec buildTableStorageSpec(TableStorageSpec.Builder paramBuilder)
  {
    GsaConfigFlags localGsaConfigFlags = VelvetServices.get().getGsaConfigFlags();
    paramBuilder.score("score").addSectionForColumn("lookup_key", new RegisterSectionInfo.Builder("lookup_key").noIndex(true).build()).addSectionForColumn("icon_uri", new RegisterSectionInfo.Builder("icon_uri").noIndex(true).build()).addSectionForColumn("display_name", new RegisterSectionInfo.Builder("name").weight(localGsaConfigFlags.getIcingContactsDisplayNameSectionWeight()).indexPrefixes(localGsaConfigFlags.getIcingContactsIndexPrefixesDisplayName()).build());
    RegisterSectionInfo.Builder localBuilder1 = RegisterSectionInfo.builder("givennames").weight(localGsaConfigFlags.getIcingContactsGivenNamesSectionWeight()).indexPrefixes(localGsaConfigFlags.getIcingContactsIndexPrefixesGivenNames());
    if (localGsaConfigFlags.getIcingContactsMatchGlobalNicknames()) {
      localBuilder1.addFeature(SectionFeature.matchGlobalNicknames());
    }
    paramBuilder.addSectionForColumn("given_names", localBuilder1.build());
    RegisterSectionInfo.Builder localBuilder2 = new RegisterSectionInfo.Builder("email").weight(localGsaConfigFlags.getIcingContactsEmailsSectionWeight()).format("rfc822").subsectionSeparator("");
    boolean bool1;
    RegisterSectionInfo.Builder localBuilder3;
    if (!localGsaConfigFlags.getIcingContactsIndexEmail())
    {
      bool1 = true;
      paramBuilder.addSectionForColumn("emails", localBuilder2.noIndex(bool1).indexPrefixes(localGsaConfigFlags.getIcingContactsIndexPrefixesEmail()).build());
      paramBuilder.addSectionForColumn("emails_types", new RegisterSectionInfo.Builder("email_types").subsectionSeparator("").noIndex(true).build());
      paramBuilder.addSectionForColumn("emails_labels", new RegisterSectionInfo.Builder("email_labels").subsectionSeparator("").noIndex(true).build());
      paramBuilder.addSectionForColumn("nickname", new RegisterSectionInfo.Builder("nickname").weight(localGsaConfigFlags.getIcingContactsNicknameSectionWeight()).subsectionSeparator("").indexPrefixes(localGsaConfigFlags.getIcingContactsIndexPrefixesNickname()).build());
      if (localGsaConfigFlags.getIcingContactsIndexNote()) {
        paramBuilder.addSectionForColumn("note", new RegisterSectionInfo.Builder("note").weight(localGsaConfigFlags.getIcingContactsNotesSectionWeight()).subsectionSeparator("").indexPrefixes(localGsaConfigFlags.getIcingContactsIndexPrefixesNote()).build());
      }
      if (localGsaConfigFlags.getIcingContactsIndexOrganization()) {
        paramBuilder.addSectionForColumn("organization", new RegisterSectionInfo.Builder("organization").weight(localGsaConfigFlags.getIcingContactsOrganizationSectionWeight()).subsectionSeparator("").indexPrefixes(localGsaConfigFlags.getIcingContactsIndexPrefixesOrganization()).build());
      }
      localBuilder3 = new RegisterSectionInfo.Builder("number").weight(localGsaConfigFlags.getIcingContactsPhoneNumbersSectionWeight()).subsectionSeparator("");
      if (localGsaConfigFlags.getIcingContactsIndexPhoneNumbers()) {
        break label669;
      }
    }
    label669:
    for (boolean bool2 = true;; bool2 = false)
    {
      paramBuilder.addSectionForColumn("phone_numbers", localBuilder3.noIndex(bool2).indexPrefixes(localGsaConfigFlags.getIcingContactsIndexPrefixesPhoneNumbers()).build());
      RegisterSectionInfo.Builder localBuilder4 = new RegisterSectionInfo.Builder("address").weight(localGsaConfigFlags.getIcingContactsPostalAddressSectionWeight()).subsectionSeparator("");
      boolean bool3 = localGsaConfigFlags.getIcingContactsIndexPostalAddress();
      boolean bool4 = false;
      if (!bool3) {
        bool4 = true;
      }
      paramBuilder.addSectionForColumn("postal_address", localBuilder4.noIndex(bool4).indexPrefixes(localGsaConfigFlags.getIcingContactsIndexPrefixesPostalAddress()).build());
      paramBuilder.addSectionForColumn("postal_address_types", new RegisterSectionInfo.Builder("address_types").subsectionSeparator("").noIndex(true).build());
      paramBuilder.addSectionForColumn("postal_address_labels", new RegisterSectionInfo.Builder("address_labels").subsectionSeparator("").noIndex(true).build());
      paramBuilder.addSectionForColumn("phone_number_types", new RegisterSectionInfo.Builder("number_types").subsectionSeparator("").noIndex(true).build());
      paramBuilder.addSectionForColumn("phone_number_labels", new RegisterSectionInfo.Builder("number_labels").subsectionSeparator("").noIndex(true).build());
      return paramBuilder.addGlobalSearchSectionTemplate("text1", 2131363320).addGlobalSearchSectionTemplate("text2", 2131363321).addGlobalSearchSectionTemplate("intent_action", 2131363322).addGlobalSearchSectionTemplate("intent_data", 2131363323).addGlobalSearchSectionTemplate("icon", 2131363324).untrimmable().version("2").build();
      bool1 = false;
      break;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.ContactsTableStorageSpec
 * JD-Core Version:    0.7.0.1
 */