package com.google.android.voicesearch.fragments.action;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.common.base.Preconditions;

public class PuntAction
  implements VoiceAction
{
  public static final Parcelable.Creator<PuntAction> CREATOR = new Parcelable.Creator()
  {
    public PuntAction createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PuntAction((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramAnonymousParcel), paramAnonymousParcel.readInt(), paramAnonymousParcel.readString(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), (Intent)paramAnonymousParcel.readParcelable(getClass().getClassLoader()), null);
    }
    
    public PuntAction[] newArray(int paramAnonymousInt)
    {
      return new PuntAction[paramAnonymousInt];
    }
  };
  private final int mActionIcon;
  private final Intent mActionIntent;
  private final int mActionLabel;
  private final CharSequence mMessage;
  private final int mMessageId;
  private final String mQuery;
  
  public PuntAction(int paramInt)
  {
    this(paramInt, 0, 0, null);
  }
  
  public PuntAction(int paramInt1, int paramInt2, int paramInt3, Intent paramIntent)
  {
    this(null, paramInt1, null, paramInt2, paramInt3, paramIntent);
  }
  
  public PuntAction(CharSequence paramCharSequence)
  {
    this(paramCharSequence, null);
  }
  
  private PuntAction(CharSequence paramCharSequence, int paramInt1, String paramString, int paramInt2, int paramInt3, Intent paramIntent)
  {
    if ((paramCharSequence != null) || (paramInt1 != 0)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mMessage = paramCharSequence;
      this.mMessageId = paramInt1;
      this.mQuery = paramString;
      this.mActionLabel = paramInt2;
      this.mActionIcon = paramInt3;
      this.mActionIntent = paramIntent;
      return;
    }
  }
  
  public PuntAction(CharSequence paramCharSequence, String paramString)
  {
    this(paramCharSequence, 0, paramString, 0, 0, null);
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
  
  public int getActionIcon()
  {
    return this.mActionIcon;
  }
  
  public int getActionLabel()
  {
    return this.mActionLabel;
  }
  
  public Intent getIntent()
  {
    return this.mActionIntent;
  }
  
  public CharSequence getMessage()
  {
    return this.mMessage;
  }
  
  public int getMessageId()
  {
    return this.mMessageId;
  }
  
  public String getQuery()
  {
    return this.mQuery;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    TextUtils.writeToParcel(this.mMessage, paramParcel, paramInt);
    paramParcel.writeInt(this.mMessageId);
    paramParcel.writeString(this.mQuery);
    paramParcel.writeInt(this.mActionLabel);
    paramParcel.writeInt(this.mActionIcon);
    paramParcel.writeParcelable(this.mActionIntent, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.action.PuntAction
 * JD-Core Version:    0.7.0.1
 */