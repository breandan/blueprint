package com.google.android.speech.contacts;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.text.util.Rfc822Token;
import com.google.android.search.core.Feature;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.majel.proto.ContactProtos.ContactInformation;
import com.google.majel.proto.ContactProtos.ContactInformation.EmailAddress;
import com.google.majel.proto.ContactProtos.ContactInformation.PersonalLocation;
import com.google.majel.proto.ContactProtos.ContactInformation.PhoneNumber;
import com.google.majel.proto.ContactProtos.ContactType;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class Contact
  implements Parcelable, PersonIdentity
{
  public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator()
  {
    public Contact createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = 1;
      ContactLookup.Mode localMode = ContactLookup.Mode.valueOf(paramAnonymousParcel.readString());
      long l = paramAnonymousParcel.readLong();
      String str1 = paramAnonymousParcel.readString();
      String str2 = paramAnonymousParcel.readString();
      String str3 = paramAnonymousParcel.readString();
      String str4 = paramAnonymousParcel.readString();
      int k = paramAnonymousParcel.readInt();
      int m = paramAnonymousParcel.readInt();
      if (paramAnonymousParcel.readByte() == i) {}
      for (;;)
      {
        return new Contact(localMode, l, str1, str2, m, str3, str4, k, i);
        int j = 0;
      }
    }
    
    public Contact[] newArray(int paramAnonymousInt)
    {
      return new Contact[paramAnonymousInt];
    }
  };
  private final long mId;
  private final String mLookupKey;
  private final ContactLookup.Mode mMode;
  private final String mName;
  private final boolean mPrimary;
  private final int mTimesContacted;
  private final int mType;
  private final String mTypeString;
  private final String mValue;
  
  public Contact(ContactLookup.Mode paramMode, long paramLong, String paramString1, String paramString2, int paramInt1, String paramString3, String paramString4, int paramInt2, boolean paramBoolean)
  {
    this.mMode = paramMode;
    this.mId = paramLong;
    this.mLookupKey = paramString1;
    this.mName = paramString2;
    this.mValue = paramString3;
    this.mTypeString = paramString4;
    this.mType = paramInt2;
    this.mTimesContacted = paramInt1;
    this.mPrimary = paramBoolean;
  }
  
  private ContactProtos.ContactInformation.EmailAddress createEmailAddressMessage()
  {
    ContactProtos.ContactInformation.EmailAddress localEmailAddress = new ContactProtos.ContactInformation.EmailAddress().setValue(getValue());
    ContactProtos.ContactType localContactType = PhoneActionUtils.androidTypeColumnToContactType(this.mMode, getType());
    if (localContactType != null) {
      localEmailAddress.setContactType(localContactType);
    }
    return localEmailAddress;
  }
  
  private ContactProtos.ContactInformation.PhoneNumber createPhoneNumberMessage()
  {
    ContactProtos.ContactInformation.PhoneNumber localPhoneNumber = new ContactProtos.ContactInformation.PhoneNumber().setValue(getValue());
    ContactProtos.ContactType localContactType = PhoneActionUtils.androidTypeColumnToContactType(this.mMode, getType());
    if (localContactType != null) {
      localPhoneNumber.setContactType(localContactType);
    }
    return localPhoneNumber;
  }
  
  private ContactProtos.ContactInformation.PersonalLocation createPostalAddressMessage()
  {
    ContactProtos.ContactInformation.PersonalLocation localPersonalLocation = new ContactProtos.ContactInformation.PersonalLocation().setValue(new EcoutezStructuredResponse.EcoutezLocalResult().setAddress(getValue()));
    ContactProtos.ContactType localContactType = PhoneActionUtils.androidTypeColumnToContactType(this.mMode, getType());
    if (localContactType != null) {
      localPersonalLocation.setContactType(localContactType);
    }
    return localPersonalLocation;
  }
  
  public static List<Contact> filterUnique(List<Contact> paramList)
  {
    ArrayList localArrayList = Lists.newArrayList();
    for (int i = 0; i < paramList.size(); i++) {
      maybeMerge(localArrayList, (Contact)paramList.get(i));
    }
    return localArrayList;
  }
  
  public static void maybeMerge(List<Contact> paramList, Contact paramContact)
  {
    for (int i = 0; i < paramList.size(); i++)
    {
      Contact localContact = merge((Contact)paramList.get(i), paramContact);
      if (localContact != null)
      {
        paramList.set(i, localContact);
        return;
      }
    }
    paramList.add(paramContact);
  }
  
  public static Contact merge(Contact paramContact1, Contact paramContact2)
  {
    if ((paramContact1 == null) || (paramContact2 == null) || (paramContact1.mMode != paramContact2.mMode)) {
      paramContact1 = null;
    }
    label100:
    label103:
    for (;;)
    {
      return paramContact1;
      if (paramContact1.mMode == ContactLookup.Mode.PERSON)
      {
        if (!paramContact1.hasSameName(paramContact2)) {
          return null;
        }
      }
      else if (!paramContact1.hasSameValue(paramContact2)) {
        return null;
      }
      int i;
      if ((paramContact1.mPrimary) && (!paramContact2.isPrimary()))
      {
        i = 1;
        if ((!paramContact2.mPrimary) || (paramContact1.isPrimary())) {
          break label100;
        }
      }
      for (int j = 1;; j = 0)
      {
        if (i != 0) {
          break label103;
        }
        if (j == 0) {
          break label105;
        }
        return paramContact2;
        i = 0;
        break;
      }
    }
    label105:
    ContactLookup.Mode localMode = paramContact1.mMode;
    long l;
    String str1;
    label139:
    String str2;
    label152:
    int k;
    String str3;
    label178:
    String str4;
    label201:
    int m;
    if (paramContact1.mId > 0L)
    {
      l = paramContact1.mId;
      if (paramContact1.mLookupKey == null) {
        break label266;
      }
      str1 = paramContact1.mLookupKey;
      if (!paramContact1.hasName()) {
        break label275;
      }
      str2 = paramContact1.mName;
      k = Math.max(paramContact1.mTimesContacted, paramContact2.mTimesContacted);
      if (!paramContact1.hasValue()) {
        break label284;
      }
      str3 = paramContact1.mValue;
      if ((paramContact1.mTypeString == null) || (paramContact1.mTypeString.isEmpty())) {
        break label293;
      }
      str4 = paramContact1.mTypeString;
      if (paramContact1.mType <= 0) {
        break label302;
      }
      m = paramContact1.mType;
      label214:
      if ((!paramContact1.mPrimary) && (!paramContact2.mPrimary)) {
        break label311;
      }
    }
    label266:
    label275:
    label284:
    label293:
    label302:
    label311:
    for (boolean bool = true;; bool = false)
    {
      return new Contact(localMode, l, str1, str2, k, str3, str4, m, bool);
      l = paramContact2.mId;
      break;
      str1 = paramContact2.mLookupKey;
      break label139;
      str2 = paramContact2.mName;
      break label152;
      str3 = paramContact2.mValue;
      break label178;
      str4 = paramContact2.mTypeString;
      break label201;
      m = paramContact2.mType;
      break label214;
    }
  }
  
  public static Contact newPhoneNumberOnlyContact(String paramString)
  {
    return new Contact(ContactLookup.Mode.PHONE_NUMBER, 0L, null, null, 0, paramString, null, -1, false);
  }
  
  public void addToContactInformation(ContactProtos.ContactInformation paramContactInformation)
  {
    Preconditions.checkState(Feature.DISCOURSE_CONTEXT_CONTACTS.isEnabled());
    switch (2.$SwitchMap$com$google$android$speech$contacts$ContactLookup$Mode[this.mMode.ordinal()])
    {
    default: 
      return;
    case 1: 
      paramContactInformation.addPhoneNumber(createPhoneNumberMessage());
      return;
    case 2: 
      paramContactInformation.addEmailAddress(createEmailAddressMessage());
      return;
    }
    paramContactInformation.addPostalAddress(createPostalAddressMessage());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Contact)) {}
    Contact localContact;
    do
    {
      return false;
      localContact = (Contact)paramObject;
    } while ((this.mId != localContact.mId) || (this.mType != localContact.mType) || (!TextUtils.equals(this.mName, localContact.mName)) || (!TextUtils.equals(this.mValue, localContact.mValue)));
    return true;
  }
  
  public String getFormattedValue()
  {
    if ((this.mMode == ContactLookup.Mode.PHONE_NUMBER) && (hasValue())) {
      return PhoneNumberUtils.formatNumber(this.mValue);
    }
    return this.mValue;
  }
  
  public long getId()
  {
    return this.mId;
  }
  
  public String getLabel(Resources paramResources)
  {
    if (this.mMode != null)
    {
      switch (2.$SwitchMap$com$google$android$speech$contacts$ContactLookup$Mode[this.mMode.ordinal()])
      {
      default: 
        return null;
      case 1: 
        return ContactsContract.CommonDataKinds.Phone.getTypeLabel(paramResources, this.mType, this.mTypeString).toString();
      case 2: 
        return ContactsContract.CommonDataKinds.Email.getTypeLabel(paramResources, this.mType, this.mTypeString).toString();
      }
      return ContactsContract.CommonDataKinds.StructuredPostal.getTypeLabel(paramResources, this.mType, this.mTypeString).toString();
    }
    return null;
  }
  
  public String getLookupKey()
  {
    return this.mLookupKey;
  }
  
  public ContactLookup.Mode getMode()
  {
    return this.mMode;
  }
  
  @Nullable
  public String getName()
  {
    return this.mName;
  }
  
  public int getType()
  {
    return this.mType;
  }
  
  @Nullable
  public String getValue()
  {
    return this.mValue;
  }
  
  public boolean hasName()
  {
    return !TextUtils.isEmpty(this.mName);
  }
  
  public boolean hasSameName(PersonIdentity paramPersonIdentity)
  {
    return (paramPersonIdentity != null) && (paramPersonIdentity.hasName()) && (hasName()) && (getName().equals(paramPersonIdentity.getName()));
  }
  
  public boolean hasSameValue(Contact paramContact)
  {
    if ((paramContact == null) || (this.mMode != paramContact.mMode) || (!paramContact.hasValue()) || (!hasValue())) {
      return false;
    }
    if (this.mMode == ContactLookup.Mode.PHONE_NUMBER) {
      return PhoneNumberUtils.compare(this.mValue, paramContact.mValue);
    }
    return this.mValue.equals(paramContact.mValue);
  }
  
  public boolean hasValue()
  {
    return !TextUtils.isEmpty(this.mValue);
  }
  
  public int hashCode()
  {
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = Long.valueOf(this.mId);
    arrayOfObject[1] = Integer.valueOf(this.mType);
    arrayOfObject[2] = this.mName;
    arrayOfObject[3] = this.mValue;
    return Objects.hashCode(arrayOfObject);
  }
  
  public boolean isPrimary()
  {
    return this.mPrimary;
  }
  
  public String toRfc822Token()
  {
    return new Rfc822Token(this.mName, this.mValue, null).toString();
  }
  
  public String toString()
  {
    return "Contact [mId=" + this.mId + ", mLookupKey=" + this.mLookupKey + ", mName=" + this.mName + ", mValue=" + this.mValue + ", mTypeString=" + this.mTypeString + ", mType=" + this.mType + ", mTimesContacted=" + this.mTimesContacted + ", mIsPrimary=" + this.mPrimary + "]";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.mMode.toString());
    paramParcel.writeLong(this.mId);
    paramParcel.writeString(this.mLookupKey);
    paramParcel.writeString(this.mName);
    paramParcel.writeString(this.mValue);
    paramParcel.writeString(this.mTypeString);
    paramParcel.writeInt(this.mType);
    paramParcel.writeInt(this.mTimesContacted);
    if (this.mPrimary) {}
    for (byte b = 1;; b = 0)
    {
      paramParcel.writeByte(b);
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.Contact
 * JD-Core Version:    0.7.0.1
 */