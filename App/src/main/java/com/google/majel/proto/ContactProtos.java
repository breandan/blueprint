package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ContactProtos
{
  public static final class ContactInformation
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String clientEntityId_ = "";
    private String displayName_ = "";
    private List<EmailAddress> emailAddress_ = Collections.emptyList();
    private boolean hasClientEntityId;
    private boolean hasDisplayName;
    private boolean hasImageUri;
    private boolean hasIsInferredMatch;
    private String imageUri_ = "";
    private boolean isInferredMatch_ = false;
    private List<PhoneNumber> phoneNumber_ = Collections.emptyList();
    private List<PersonalLocation> postalAddress_ = Collections.emptyList();
    
    public ContactInformation addEmailAddress(EmailAddress paramEmailAddress)
    {
      if (paramEmailAddress == null) {
        throw new NullPointerException();
      }
      if (this.emailAddress_.isEmpty()) {
        this.emailAddress_ = new ArrayList();
      }
      this.emailAddress_.add(paramEmailAddress);
      return this;
    }
    
    public ContactInformation addPhoneNumber(PhoneNumber paramPhoneNumber)
    {
      if (paramPhoneNumber == null) {
        throw new NullPointerException();
      }
      if (this.phoneNumber_.isEmpty()) {
        this.phoneNumber_ = new ArrayList();
      }
      this.phoneNumber_.add(paramPhoneNumber);
      return this;
    }
    
    public ContactInformation addPostalAddress(PersonalLocation paramPersonalLocation)
    {
      if (paramPersonalLocation == null) {
        throw new NullPointerException();
      }
      if (this.postalAddress_.isEmpty()) {
        this.postalAddress_ = new ArrayList();
      }
      this.postalAddress_.add(paramPersonalLocation);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getClientEntityId()
    {
      return this.clientEntityId_;
    }
    
    public String getDisplayName()
    {
      return this.displayName_;
    }
    
    public List<EmailAddress> getEmailAddressList()
    {
      return this.emailAddress_;
    }
    
    public String getImageUri()
    {
      return this.imageUri_;
    }
    
    public boolean getIsInferredMatch()
    {
      return this.isInferredMatch_;
    }
    
    public List<PhoneNumber> getPhoneNumberList()
    {
      return this.phoneNumber_;
    }
    
    public List<PersonalLocation> getPostalAddressList()
    {
      return this.postalAddress_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasDisplayName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getDisplayName());
      }
      if (hasImageUri()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getImageUri());
      }
      if (hasClientEntityId()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getClientEntityId());
      }
      Iterator localIterator1 = getPhoneNumberList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, (PhoneNumber)localIterator1.next());
      }
      Iterator localIterator2 = getEmailAddressList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, (EmailAddress)localIterator2.next());
      }
      Iterator localIterator3 = getPostalAddressList().iterator();
      while (localIterator3.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, (PersonalLocation)localIterator3.next());
      }
      if (hasIsInferredMatch()) {
        i += CodedOutputStreamMicro.computeBoolSize(7, getIsInferredMatch());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasClientEntityId()
    {
      return this.hasClientEntityId;
    }
    
    public boolean hasDisplayName()
    {
      return this.hasDisplayName;
    }
    
    public boolean hasImageUri()
    {
      return this.hasImageUri;
    }
    
    public boolean hasIsInferredMatch()
    {
      return this.hasIsInferredMatch;
    }
    
    public ContactInformation mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          setDisplayName(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setImageUri(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setClientEntityId(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          PhoneNumber localPhoneNumber = new PhoneNumber();
          paramCodedInputStreamMicro.readMessage(localPhoneNumber);
          addPhoneNumber(localPhoneNumber);
          break;
        case 42: 
          EmailAddress localEmailAddress = new EmailAddress();
          paramCodedInputStreamMicro.readMessage(localEmailAddress);
          addEmailAddress(localEmailAddress);
          break;
        case 50: 
          PersonalLocation localPersonalLocation = new PersonalLocation();
          paramCodedInputStreamMicro.readMessage(localPersonalLocation);
          addPostalAddress(localPersonalLocation);
          break;
        }
        setIsInferredMatch(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public ContactInformation setClientEntityId(String paramString)
    {
      this.hasClientEntityId = true;
      this.clientEntityId_ = paramString;
      return this;
    }
    
    public ContactInformation setDisplayName(String paramString)
    {
      this.hasDisplayName = true;
      this.displayName_ = paramString;
      return this;
    }
    
    public ContactInformation setImageUri(String paramString)
    {
      this.hasImageUri = true;
      this.imageUri_ = paramString;
      return this;
    }
    
    public ContactInformation setIsInferredMatch(boolean paramBoolean)
    {
      this.hasIsInferredMatch = true;
      this.isInferredMatch_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasDisplayName()) {
        paramCodedOutputStreamMicro.writeString(1, getDisplayName());
      }
      if (hasImageUri()) {
        paramCodedOutputStreamMicro.writeString(2, getImageUri());
      }
      if (hasClientEntityId()) {
        paramCodedOutputStreamMicro.writeString(3, getClientEntityId());
      }
      Iterator localIterator1 = getPhoneNumberList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(4, (PhoneNumber)localIterator1.next());
      }
      Iterator localIterator2 = getEmailAddressList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(5, (EmailAddress)localIterator2.next());
      }
      Iterator localIterator3 = getPostalAddressList().iterator();
      while (localIterator3.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(6, (PersonalLocation)localIterator3.next());
      }
      if (hasIsInferredMatch()) {
        paramCodedOutputStreamMicro.writeBool(7, getIsInferredMatch());
      }
    }
    
    public static final class EmailAddress
      extends MessageMicro
    {
      private int cachedSize = -1;
      private ContactProtos.ContactType contactType_ = null;
      private boolean hasContactType;
      private boolean hasInferenceDetails;
      private boolean hasValue;
      private ContactProtos.InferenceDetails inferenceDetails_ = null;
      private String value_ = "";
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public ContactProtos.ContactType getContactType()
      {
        return this.contactType_;
      }
      
      public ContactProtos.InferenceDetails getInferenceDetails()
      {
        return this.inferenceDetails_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasValue();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeStringSize(1, getValue());
        }
        if (hasContactType()) {
          i += CodedOutputStreamMicro.computeMessageSize(2, getContactType());
        }
        if (hasInferenceDetails()) {
          i += CodedOutputStreamMicro.computeMessageSize(3, getInferenceDetails());
        }
        this.cachedSize = i;
        return i;
      }
      
      public String getValue()
      {
        return this.value_;
      }
      
      public boolean hasContactType()
      {
        return this.hasContactType;
      }
      
      public boolean hasInferenceDetails()
      {
        return this.hasInferenceDetails;
      }
      
      public boolean hasValue()
      {
        return this.hasValue;
      }
      
      public EmailAddress mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
        throws IOException
      {
        for (;;)
        {
          int i = paramCodedInputStreamMicro.readTag();
          switch (i)
          {
          default: 
            if (parseUnknownField(paramCodedInputStreamMicro, i)) {
              continue;
            }
          case 0: 
            return this;
          case 10: 
            setValue(paramCodedInputStreamMicro.readString());
            break;
          case 18: 
            ContactProtos.ContactType localContactType = new ContactProtos.ContactType();
            paramCodedInputStreamMicro.readMessage(localContactType);
            setContactType(localContactType);
            break;
          }
          ContactProtos.InferenceDetails localInferenceDetails = new ContactProtos.InferenceDetails();
          paramCodedInputStreamMicro.readMessage(localInferenceDetails);
          setInferenceDetails(localInferenceDetails);
        }
      }
      
      public EmailAddress setContactType(ContactProtos.ContactType paramContactType)
      {
        if (paramContactType == null) {
          throw new NullPointerException();
        }
        this.hasContactType = true;
        this.contactType_ = paramContactType;
        return this;
      }
      
      public EmailAddress setInferenceDetails(ContactProtos.InferenceDetails paramInferenceDetails)
      {
        if (paramInferenceDetails == null) {
          throw new NullPointerException();
        }
        this.hasInferenceDetails = true;
        this.inferenceDetails_ = paramInferenceDetails;
        return this;
      }
      
      public EmailAddress setValue(String paramString)
      {
        this.hasValue = true;
        this.value_ = paramString;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasValue()) {
          paramCodedOutputStreamMicro.writeString(1, getValue());
        }
        if (hasContactType()) {
          paramCodedOutputStreamMicro.writeMessage(2, getContactType());
        }
        if (hasInferenceDetails()) {
          paramCodedOutputStreamMicro.writeMessage(3, getInferenceDetails());
        }
      }
    }
    
    public static final class PersonalLocation
      extends MessageMicro
    {
      private int cachedSize = -1;
      private ContactProtos.ContactType contactType_ = null;
      private boolean hasContactType;
      private boolean hasInferenceDetails;
      private boolean hasValue;
      private ContactProtos.InferenceDetails inferenceDetails_ = null;
      private EcoutezStructuredResponse.EcoutezLocalResult value_ = null;
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public ContactProtos.ContactType getContactType()
      {
        return this.contactType_;
      }
      
      public ContactProtos.InferenceDetails getInferenceDetails()
      {
        return this.inferenceDetails_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasValue();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getValue());
        }
        if (hasContactType()) {
          i += CodedOutputStreamMicro.computeMessageSize(2, getContactType());
        }
        if (hasInferenceDetails()) {
          i += CodedOutputStreamMicro.computeMessageSize(3, getInferenceDetails());
        }
        this.cachedSize = i;
        return i;
      }
      
      public EcoutezStructuredResponse.EcoutezLocalResult getValue()
      {
        return this.value_;
      }
      
      public boolean hasContactType()
      {
        return this.hasContactType;
      }
      
      public boolean hasInferenceDetails()
      {
        return this.hasInferenceDetails;
      }
      
      public boolean hasValue()
      {
        return this.hasValue;
      }
      
      public PersonalLocation mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
        throws IOException
      {
        for (;;)
        {
          int i = paramCodedInputStreamMicro.readTag();
          switch (i)
          {
          default: 
            if (parseUnknownField(paramCodedInputStreamMicro, i)) {
              continue;
            }
          case 0: 
            return this;
          case 10: 
            EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult = new EcoutezStructuredResponse.EcoutezLocalResult();
            paramCodedInputStreamMicro.readMessage(localEcoutezLocalResult);
            setValue(localEcoutezLocalResult);
            break;
          case 18: 
            ContactProtos.ContactType localContactType = new ContactProtos.ContactType();
            paramCodedInputStreamMicro.readMessage(localContactType);
            setContactType(localContactType);
            break;
          }
          ContactProtos.InferenceDetails localInferenceDetails = new ContactProtos.InferenceDetails();
          paramCodedInputStreamMicro.readMessage(localInferenceDetails);
          setInferenceDetails(localInferenceDetails);
        }
      }
      
      public PersonalLocation setContactType(ContactProtos.ContactType paramContactType)
      {
        if (paramContactType == null) {
          throw new NullPointerException();
        }
        this.hasContactType = true;
        this.contactType_ = paramContactType;
        return this;
      }
      
      public PersonalLocation setInferenceDetails(ContactProtos.InferenceDetails paramInferenceDetails)
      {
        if (paramInferenceDetails == null) {
          throw new NullPointerException();
        }
        this.hasInferenceDetails = true;
        this.inferenceDetails_ = paramInferenceDetails;
        return this;
      }
      
      public PersonalLocation setValue(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
      {
        if (paramEcoutezLocalResult == null) {
          throw new NullPointerException();
        }
        this.hasValue = true;
        this.value_ = paramEcoutezLocalResult;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasValue()) {
          paramCodedOutputStreamMicro.writeMessage(1, getValue());
        }
        if (hasContactType()) {
          paramCodedOutputStreamMicro.writeMessage(2, getContactType());
        }
        if (hasInferenceDetails()) {
          paramCodedOutputStreamMicro.writeMessage(3, getInferenceDetails());
        }
      }
    }
    
    public static final class PhoneNumber
      extends MessageMicro
    {
      private int cachedSize = -1;
      private ContactProtos.ContactType contactType_ = null;
      private boolean hasContactType;
      private boolean hasInferenceDetails;
      private boolean hasValue;
      private ContactProtos.InferenceDetails inferenceDetails_ = null;
      private String value_ = "";
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public ContactProtos.ContactType getContactType()
      {
        return this.contactType_;
      }
      
      public ContactProtos.InferenceDetails getInferenceDetails()
      {
        return this.inferenceDetails_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasValue();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeStringSize(1, getValue());
        }
        if (hasContactType()) {
          i += CodedOutputStreamMicro.computeMessageSize(2, getContactType());
        }
        if (hasInferenceDetails()) {
          i += CodedOutputStreamMicro.computeMessageSize(3, getInferenceDetails());
        }
        this.cachedSize = i;
        return i;
      }
      
      public String getValue()
      {
        return this.value_;
      }
      
      public boolean hasContactType()
      {
        return this.hasContactType;
      }
      
      public boolean hasInferenceDetails()
      {
        return this.hasInferenceDetails;
      }
      
      public boolean hasValue()
      {
        return this.hasValue;
      }
      
      public PhoneNumber mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
        throws IOException
      {
        for (;;)
        {
          int i = paramCodedInputStreamMicro.readTag();
          switch (i)
          {
          default: 
            if (parseUnknownField(paramCodedInputStreamMicro, i)) {
              continue;
            }
          case 0: 
            return this;
          case 10: 
            setValue(paramCodedInputStreamMicro.readString());
            break;
          case 18: 
            ContactProtos.ContactType localContactType = new ContactProtos.ContactType();
            paramCodedInputStreamMicro.readMessage(localContactType);
            setContactType(localContactType);
            break;
          }
          ContactProtos.InferenceDetails localInferenceDetails = new ContactProtos.InferenceDetails();
          paramCodedInputStreamMicro.readMessage(localInferenceDetails);
          setInferenceDetails(localInferenceDetails);
        }
      }
      
      public PhoneNumber setContactType(ContactProtos.ContactType paramContactType)
      {
        if (paramContactType == null) {
          throw new NullPointerException();
        }
        this.hasContactType = true;
        this.contactType_ = paramContactType;
        return this;
      }
      
      public PhoneNumber setInferenceDetails(ContactProtos.InferenceDetails paramInferenceDetails)
      {
        if (paramInferenceDetails == null) {
          throw new NullPointerException();
        }
        this.hasInferenceDetails = true;
        this.inferenceDetails_ = paramInferenceDetails;
        return this;
      }
      
      public PhoneNumber setValue(String paramString)
      {
        this.hasValue = true;
        this.value_ = paramString;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasValue()) {
          paramCodedOutputStreamMicro.writeString(1, getValue());
        }
        if (hasContactType()) {
          paramCodedOutputStreamMicro.writeMessage(2, getContactType());
        }
        if (hasInferenceDetails()) {
          paramCodedOutputStreamMicro.writeMessage(3, getInferenceDetails());
        }
      }
    }
  }
  
  public static final class ContactQuery
    extends MessageMicro
  {
    public static final int CLIENT_ENTITY_ID_FIELD_NUMBER = 2;
    public static final int CONTACT_METHOD_FIELD_NUMBER = 3;
    public static final int CONTACT_TYPE_FIELD_NUMBER = 4;
    public static final int NAME_FIELD_NUMBER = 1;
    public static final int VERBOSE_NAME_FIELD_NUMBER = 5;
    private int cachedSize = -1;
    private String clientEntityId_ = "";
    private List<Integer> contactMethod_ = Collections.emptyList();
    private ContactProtos.ContactType contactType_ = null;
    private boolean hasClientEntityId;
    private boolean hasContactType;
    private List<String> name_ = Collections.emptyList();
    private List<ContactProtos.RecognizedName> verboseName_ = Collections.emptyList();
    
    public static ContactQuery parseFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      return new ContactQuery().mergeFrom(paramCodedInputStreamMicro);
    }
    
    public static ContactQuery parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferMicroException
    {
      return (ContactQuery)new ContactQuery().mergeFrom(paramArrayOfByte);
    }
    
    public ContactQuery addContactMethod(int paramInt)
    {
      if (this.contactMethod_.isEmpty()) {
        this.contactMethod_ = new ArrayList();
      }
      this.contactMethod_.add(Integer.valueOf(paramInt));
      return this;
    }
    
    public ContactQuery addName(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.name_.isEmpty()) {
        this.name_ = new ArrayList();
      }
      this.name_.add(paramString);
      return this;
    }
    
    public ContactQuery addVerboseName(ContactProtos.RecognizedName paramRecognizedName)
    {
      if (paramRecognizedName == null) {
        throw new NullPointerException();
      }
      if (this.verboseName_.isEmpty()) {
        this.verboseName_ = new ArrayList();
      }
      this.verboseName_.add(paramRecognizedName);
      return this;
    }
    
    public final ContactQuery clear()
    {
      clearName();
      clearVerboseName();
      clearClientEntityId();
      clearContactMethod();
      clearContactType();
      this.cachedSize = -1;
      return this;
    }
    
    public ContactQuery clearClientEntityId()
    {
      this.hasClientEntityId = false;
      this.clientEntityId_ = "";
      return this;
    }
    
    public ContactQuery clearContactMethod()
    {
      this.contactMethod_ = Collections.emptyList();
      return this;
    }
    
    public ContactQuery clearContactType()
    {
      this.hasContactType = false;
      this.contactType_ = null;
      return this;
    }
    
    public ContactQuery clearName()
    {
      this.name_ = Collections.emptyList();
      return this;
    }
    
    public ContactQuery clearVerboseName()
    {
      this.verboseName_ = Collections.emptyList();
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getClientEntityId()
    {
      return this.clientEntityId_;
    }
    
    public int getContactMethod(int paramInt)
    {
      return ((Integer)this.contactMethod_.get(paramInt)).intValue();
    }
    
    public int getContactMethodCount()
    {
      return this.contactMethod_.size();
    }
    
    public List<Integer> getContactMethodList()
    {
      return this.contactMethod_;
    }
    
    public ContactProtos.ContactType getContactType()
    {
      return this.contactType_;
    }
    
    public String getName(int paramInt)
    {
      return (String)this.name_.get(paramInt);
    }
    
    public int getNameCount()
    {
      return this.name_.size();
    }
    
    public List<String> getNameList()
    {
      return this.name_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getNameList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator1.next());
      }
      int j = 0 + i + 1 * getNameList().size();
      if (hasClientEntityId()) {
        j += CodedOutputStreamMicro.computeStringSize(2, getClientEntityId());
      }
      int k = 0;
      Iterator localIterator2 = getContactMethodList().iterator();
      while (localIterator2.hasNext()) {
        k += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator2.next()).intValue());
      }
      int m = j + k + 1 * getContactMethodList().size();
      if (hasContactType()) {
        m += CodedOutputStreamMicro.computeMessageSize(4, getContactType());
      }
      Iterator localIterator3 = getVerboseNameList().iterator();
      while (localIterator3.hasNext()) {
        m += CodedOutputStreamMicro.computeMessageSize(5, (ContactProtos.RecognizedName)localIterator3.next());
      }
      this.cachedSize = m;
      return m;
    }
    
    public ContactProtos.RecognizedName getVerboseName(int paramInt)
    {
      return (ContactProtos.RecognizedName)this.verboseName_.get(paramInt);
    }
    
    public int getVerboseNameCount()
    {
      return this.verboseName_.size();
    }
    
    public List<ContactProtos.RecognizedName> getVerboseNameList()
    {
      return this.verboseName_;
    }
    
    public boolean hasClientEntityId()
    {
      return this.hasClientEntityId;
    }
    
    public boolean hasContactType()
    {
      return this.hasContactType;
    }
    
    public final boolean isInitialized()
    {
      return true;
    }
    
    public ContactQuery mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          addName(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setClientEntityId(paramCodedInputStreamMicro.readString());
          break;
        case 24: 
          addContactMethod(paramCodedInputStreamMicro.readInt32());
          break;
        case 34: 
          ContactProtos.ContactType localContactType = new ContactProtos.ContactType();
          paramCodedInputStreamMicro.readMessage(localContactType);
          setContactType(localContactType);
          break;
        }
        ContactProtos.RecognizedName localRecognizedName = new ContactProtos.RecognizedName();
        paramCodedInputStreamMicro.readMessage(localRecognizedName);
        addVerboseName(localRecognizedName);
      }
    }
    
    public ContactQuery setClientEntityId(String paramString)
    {
      this.hasClientEntityId = true;
      this.clientEntityId_ = paramString;
      return this;
    }
    
    public ContactQuery setContactMethod(int paramInt1, int paramInt2)
    {
      this.contactMethod_.set(paramInt1, Integer.valueOf(paramInt2));
      return this;
    }
    
    public ContactQuery setContactType(ContactProtos.ContactType paramContactType)
    {
      if (paramContactType == null) {
        throw new NullPointerException();
      }
      this.hasContactType = true;
      this.contactType_ = paramContactType;
      return this;
    }
    
    public ContactQuery setName(int paramInt, String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      this.name_.set(paramInt, paramString);
      return this;
    }
    
    public ContactQuery setVerboseName(int paramInt, ContactProtos.RecognizedName paramRecognizedName)
    {
      if (paramRecognizedName == null) {
        throw new NullPointerException();
      }
      this.verboseName_.set(paramInt, paramRecognizedName);
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getNameList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeString(1, (String)localIterator1.next());
      }
      if (hasClientEntityId()) {
        paramCodedOutputStreamMicro.writeString(2, getClientEntityId());
      }
      Iterator localIterator2 = getContactMethodList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeInt32(3, ((Integer)localIterator2.next()).intValue());
      }
      if (hasContactType()) {
        paramCodedOutputStreamMicro.writeMessage(4, getContactType());
      }
      Iterator localIterator3 = getVerboseNameList().iterator();
      while (localIterator3.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(5, (ContactProtos.RecognizedName)localIterator3.next());
      }
    }
  }
  
  public static final class ContactReference
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String canonicalRelationshipName_ = "";
    private List<ContactProtos.ContactInformation> contactInformation_ = Collections.emptyList();
    private ContactProtos.ContactQuery contactQuery_ = null;
    private boolean hasCanonicalRelationshipName;
    private boolean hasContactQuery;
    private boolean hasIsRelationship;
    private boolean hasName;
    private boolean hasPlaceholderContact;
    private boolean isRelationship_ = false;
    private String name_ = "";
    private boolean placeholderContact_ = false;
    
    public ContactReference addContactInformation(ContactProtos.ContactInformation paramContactInformation)
    {
      if (paramContactInformation == null) {
        throw new NullPointerException();
      }
      if (this.contactInformation_.isEmpty()) {
        this.contactInformation_ = new ArrayList();
      }
      this.contactInformation_.add(paramContactInformation);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getCanonicalRelationshipName()
    {
      return this.canonicalRelationshipName_;
    }
    
    public int getContactInformationCount()
    {
      return this.contactInformation_.size();
    }
    
    public List<ContactProtos.ContactInformation> getContactInformationList()
    {
      return this.contactInformation_;
    }
    
    public ContactProtos.ContactQuery getContactQuery()
    {
      return this.contactQuery_;
    }
    
    public boolean getIsRelationship()
    {
      return this.isRelationship_;
    }
    
    public String getName()
    {
      return this.name_;
    }
    
    public boolean getPlaceholderContact()
    {
      return this.placeholderContact_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getName());
      }
      Iterator localIterator = getContactInformationList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, (ContactProtos.ContactInformation)localIterator.next());
      }
      if (hasContactQuery()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getContactQuery());
      }
      if (hasPlaceholderContact()) {
        i += CodedOutputStreamMicro.computeBoolSize(4, getPlaceholderContact());
      }
      if (hasIsRelationship()) {
        i += CodedOutputStreamMicro.computeBoolSize(5, getIsRelationship());
      }
      if (hasCanonicalRelationshipName()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getCanonicalRelationshipName());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasCanonicalRelationshipName()
    {
      return this.hasCanonicalRelationshipName;
    }
    
    public boolean hasContactQuery()
    {
      return this.hasContactQuery;
    }
    
    public boolean hasIsRelationship()
    {
      return this.hasIsRelationship;
    }
    
    public boolean hasName()
    {
      return this.hasName;
    }
    
    public boolean hasPlaceholderContact()
    {
      return this.hasPlaceholderContact;
    }
    
    public ContactReference mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          setName(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          ContactProtos.ContactInformation localContactInformation = new ContactProtos.ContactInformation();
          paramCodedInputStreamMicro.readMessage(localContactInformation);
          addContactInformation(localContactInformation);
          break;
        case 26: 
          ContactProtos.ContactQuery localContactQuery = new ContactProtos.ContactQuery();
          paramCodedInputStreamMicro.readMessage(localContactQuery);
          setContactQuery(localContactQuery);
          break;
        case 32: 
          setPlaceholderContact(paramCodedInputStreamMicro.readBool());
          break;
        case 40: 
          setIsRelationship(paramCodedInputStreamMicro.readBool());
          break;
        }
        setCanonicalRelationshipName(paramCodedInputStreamMicro.readString());
      }
    }
    
    public ContactReference setCanonicalRelationshipName(String paramString)
    {
      this.hasCanonicalRelationshipName = true;
      this.canonicalRelationshipName_ = paramString;
      return this;
    }
    
    public ContactReference setContactQuery(ContactProtos.ContactQuery paramContactQuery)
    {
      if (paramContactQuery == null) {
        throw new NullPointerException();
      }
      this.hasContactQuery = true;
      this.contactQuery_ = paramContactQuery;
      return this;
    }
    
    public ContactReference setIsRelationship(boolean paramBoolean)
    {
      this.hasIsRelationship = true;
      this.isRelationship_ = paramBoolean;
      return this;
    }
    
    public ContactReference setName(String paramString)
    {
      this.hasName = true;
      this.name_ = paramString;
      return this;
    }
    
    public ContactReference setPlaceholderContact(boolean paramBoolean)
    {
      this.hasPlaceholderContact = true;
      this.placeholderContact_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasName()) {
        paramCodedOutputStreamMicro.writeString(1, getName());
      }
      Iterator localIterator = getContactInformationList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(2, (ContactProtos.ContactInformation)localIterator.next());
      }
      if (hasContactQuery()) {
        paramCodedOutputStreamMicro.writeMessage(3, getContactQuery());
      }
      if (hasPlaceholderContact()) {
        paramCodedOutputStreamMicro.writeBool(4, getPlaceholderContact());
      }
      if (hasIsRelationship()) {
        paramCodedOutputStreamMicro.writeBool(5, getIsRelationship());
      }
      if (hasCanonicalRelationshipName()) {
        paramCodedOutputStreamMicro.writeString(6, getCanonicalRelationshipName());
      }
    }
  }
  
  public static final class ContactType
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String customType_ = "";
    private boolean hasCustomType;
    private boolean hasType;
    private int type_ = 1;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getCustomType()
    {
      return this.customType_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getType());
      }
      if (hasCustomType()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getCustomType());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getType()
    {
      return this.type_;
    }
    
    public boolean hasCustomType()
    {
      return this.hasCustomType;
    }
    
    public boolean hasType()
    {
      return this.hasType;
    }
    
    public ContactType mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          setType(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setCustomType(paramCodedInputStreamMicro.readString());
      }
    }
    
    public ContactType setCustomType(String paramString)
    {
      this.hasCustomType = true;
      this.customType_ = paramString;
      return this;
    }
    
    public ContactType setType(int paramInt)
    {
      this.hasType = true;
      this.type_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasType()) {
        paramCodedOutputStreamMicro.writeInt32(1, getType());
      }
      if (hasCustomType()) {
        paramCodedOutputStreamMicro.writeString(2, getCustomType());
      }
    }
  }
  
  public static final class InferenceDetails
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasIsInferred;
    private boolean isInferred_ = false;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public boolean getIsInferred()
    {
      return this.isInferred_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasIsInferred();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeBoolSize(1, getIsInferred());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasIsInferred()
    {
      return this.hasIsInferred;
    }
    
    public InferenceDetails mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        }
        setIsInferred(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public InferenceDetails setIsInferred(boolean paramBoolean)
    {
      this.hasIsInferred = true;
      this.isInferred_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasIsInferred()) {
        paramCodedOutputStreamMicro.writeBool(1, getIsInferred());
      }
    }
  }
  
  public static final class RecognizedName
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasMatchConfidence;
    private boolean hasValue;
    private float matchConfidence_ = 0.0F;
    private String value_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public float getMatchConfidence()
    {
      return this.matchConfidence_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasValue();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getValue());
      }
      if (hasMatchConfidence()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getMatchConfidence());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getValue()
    {
      return this.value_;
    }
    
    public boolean hasMatchConfidence()
    {
      return this.hasMatchConfidence;
    }
    
    public boolean hasValue()
    {
      return this.hasValue;
    }
    
    public RecognizedName mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          setValue(paramCodedInputStreamMicro.readString());
          break;
        }
        setMatchConfidence(paramCodedInputStreamMicro.readFloat());
      }
    }
    
    public RecognizedName setMatchConfidence(float paramFloat)
    {
      this.hasMatchConfidence = true;
      this.matchConfidence_ = paramFloat;
      return this;
    }
    
    public RecognizedName setValue(String paramString)
    {
      this.hasValue = true;
      this.value_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasValue()) {
        paramCodedOutputStreamMicro.writeString(1, getValue());
      }
      if (hasMatchConfidence()) {
        paramCodedOutputStreamMicro.writeFloat(2, getMatchConfidence());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.ContactProtos
 * JD-Core Version:    0.7.0.1
 */