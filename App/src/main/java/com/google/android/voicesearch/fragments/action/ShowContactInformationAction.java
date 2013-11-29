package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.common.base.Preconditions;
import java.util.List;

public class ShowContactInformationAction
  extends CommunicationActionImpl
{
  public static final Parcelable.Creator<ShowContactInformationAction> CREATOR = new Parcelable.Creator()
  {
    public ShowContactInformationAction createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = 1;
      ClassLoader localClassLoader = getClass().getClassLoader();
      PersonDisambiguation localPersonDisambiguation = (PersonDisambiguation)paramAnonymousParcel.readParcelable(localClassLoader);
      int k = paramAnonymousParcel.readInt();
      String str = paramAnonymousParcel.readString();
      if (paramAnonymousParcel.readByte() == i) {}
      for (;;)
      {
        return new ShowContactInformationAction(localPersonDisambiguation, k, str, i, paramAnonymousParcel.readArrayList(localClassLoader), paramAnonymousParcel.readArrayList(localClassLoader), paramAnonymousParcel.readArrayList(localClassLoader));
        int j = 0;
      }
    }
    
    public ShowContactInformationAction[] newArray(int paramAnonymousInt)
    {
      return new ShowContactInformationAction[paramAnonymousInt];
    }
  };
  private final String mContactDetailType;
  private boolean mContactDetailsFound;
  private final int mContactMethod;
  private List<Contact> mEmailAddresses;
  private List<Contact> mPhoneNumbers;
  private List<Contact> mPostalAddresses;
  
  public ShowContactInformationAction(PersonDisambiguation paramPersonDisambiguation, int paramInt, String paramString, boolean paramBoolean, List<Contact> paramList1, List<Contact> paramList2, List<Contact> paramList3)
  {
    if ((paramInt == i) || (paramInt == 2) || (paramInt == 3) || (paramInt == 0)) {}
    for (;;)
    {
      Preconditions.checkArgument(i);
      this.mRecipient = ((PersonDisambiguation)Preconditions.checkNotNull(paramPersonDisambiguation));
      this.mContactMethod = paramInt;
      this.mContactDetailType = paramString;
      this.mContactDetailsFound = paramBoolean;
      this.mPhoneNumbers = paramList1;
      this.mEmailAddresses = paramList2;
      this.mPostalAddresses = paramList3;
      return;
      i = 0;
    }
  }
  
  public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor)
  {
    return paramVoiceActionVisitor.visit(this);
  }
  
  public boolean canExecute()
  {
    return false;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CommunicationAction forNewRecipient(PersonDisambiguation paramPersonDisambiguation)
  {
    return new ShowContactInformationAction(paramPersonDisambiguation, getContactMethod(), getContactDetailType(), isContactDetailsFound(), getPhoneNumbers(), getEmailAddresses(), getPostalAddresses());
  }
  
  public int getActionTypeLog()
  {
    return 33;
  }
  
  public String getContactDetailType()
  {
    return this.mContactDetailType;
  }
  
  public int getContactMethod()
  {
    return this.mContactMethod;
  }
  
  public List<Contact> getEmailAddresses()
  {
    return this.mEmailAddresses;
  }
  
  public List<Contact> getPhoneNumbers()
  {
    return this.mPhoneNumbers;
  }
  
  public List<Contact> getPostalAddresses()
  {
    return this.mPostalAddresses;
  }
  
  public ContactSelectMode getSelectMode()
  {
    return ContactSelectMode.SHOW_CONTACT_INFO;
  }
  
  public boolean isContactDetailsFound()
  {
    return this.mContactDetailsFound;
  }
  
  public void setContactDetailsFound(boolean paramBoolean)
  {
    this.mContactDetailsFound = paramBoolean;
  }
  
  public void setEmailAddresses(List<Contact> paramList)
  {
    this.mEmailAddresses = paramList;
  }
  
  public void setPhoneNumbers(List<Contact> paramList)
  {
    this.mPhoneNumbers = paramList;
  }
  
  public void setPostalAddresses(List<Contact> paramList)
  {
    this.mPostalAddresses = paramList;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(this.mRecipient, paramInt);
    paramParcel.writeInt(this.mContactMethod);
    paramParcel.writeString(this.mContactDetailType);
    if (this.mContactDetailsFound) {}
    for (int i = 1;; i = 0)
    {
      paramParcel.writeByte((byte)i);
      paramParcel.writeList(this.mPhoneNumbers);
      paramParcel.writeList(this.mEmailAddresses);
      paramParcel.writeList(this.mPostalAddresses);
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.action.ShowContactInformationAction
 * JD-Core Version:    0.7.0.1
 */