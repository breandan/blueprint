package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.voicesearch.contacts.ContactSelectMode;
import javax.annotation.Nullable;

public class EmailAction
  extends CommunicationActionImpl
{
  public static final Parcelable.Creator<EmailAction> CREATOR = new Parcelable.Creator()
  {
    public EmailAction createFromParcel(Parcel paramAnonymousParcel)
    {
      return new EmailAction((PersonDisambiguation)paramAnonymousParcel.readParcelable(getClass().getClassLoader()), paramAnonymousParcel.readString(), paramAnonymousParcel.readString());
    }
    
    public EmailAction[] newArray(int paramAnonymousInt)
    {
      return new EmailAction[paramAnonymousInt];
    }
  };
  @Nullable
  private String mBody;
  @Nullable
  private String mSubject;
  
  public EmailAction(PersonDisambiguation paramPersonDisambiguation, String paramString1, String paramString2)
  {
    this.mRecipient = paramPersonDisambiguation;
    this.mSubject = paramString1;
    this.mBody = paramString2;
  }
  
  public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor)
  {
    return paramVoiceActionVisitor.visit(this);
  }
  
  public boolean canExecute()
  {
    return (this.mRecipient != null) && (this.mRecipient.isCompleted()) && (hasSubjectOrBody());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CommunicationAction forNewRecipient(PersonDisambiguation paramPersonDisambiguation)
  {
    return new EmailAction(paramPersonDisambiguation, getSubject(), getBody());
  }
  
  public int getActionTypeLog()
  {
    return 2;
  }
  
  public String getBody()
  {
    return this.mBody;
  }
  
  public ContactSelectMode getSelectMode()
  {
    return ContactSelectMode.EMAIL;
  }
  
  public String getSubject()
  {
    return this.mSubject;
  }
  
  public boolean hasSubjectOrBody()
  {
    return (!TextUtils.isEmpty(this.mSubject)) || (!TextUtils.isEmpty(this.mBody));
  }
  
  public void setBody(String paramString)
  {
    this.mBody = paramString;
  }
  
  public void setSubject(String paramString)
  {
    this.mSubject = paramString;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(this.mRecipient, paramInt);
    paramParcel.writeString(this.mSubject);
    paramParcel.writeString(this.mBody);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.action.EmailAction
 * JD-Core Version:    0.7.0.1
 */