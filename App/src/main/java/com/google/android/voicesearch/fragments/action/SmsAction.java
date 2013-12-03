package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;
import android.text.TextUtils;

import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.voicesearch.contacts.ContactSelectMode;

public class SmsAction
        extends CommunicationActionImpl {
    public static final Parcelable.Creator<SmsAction> CREATOR = new Parcelable.Creator() {
        public SmsAction createFromParcel(Parcel paramAnonymousParcel) {
            return new SmsAction((PersonDisambiguation) paramAnonymousParcel.readParcelable(getClass().getClassLoader()), paramAnonymousParcel.readString());
        }

        public SmsAction[] newArray(int paramAnonymousInt) {
            return new SmsAction[paramAnonymousInt];
        }
    };
    private final String mBody;

    public SmsAction(PersonDisambiguation paramPersonDisambiguation, String paramString) {
        this.mRecipient = paramPersonDisambiguation;
        this.mBody = paramString;
    }

    public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor) {
        return paramVoiceActionVisitor.visit(this);
    }

    public boolean canExecute() {
        return (PersonDisambiguation.isCompleted(this.mRecipient)) && (hasBody());
    }

    public int describeContents() {
        return 0;
    }

    public CommunicationAction forNewRecipient(PersonDisambiguation paramPersonDisambiguation) {
        return new SmsAction(paramPersonDisambiguation, getBody());
    }

    public int getActionTypeLog() {
        return 1;
    }

    public String getBody() {
        return this.mBody;
    }

    public ContactSelectMode getSelectMode() {
        return ContactSelectMode.SMS;
    }

    public boolean hasBody() {
        return !TextUtils.isEmpty(this.mBody);
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeParcelable(this.mRecipient, paramInt);
        paramParcel.writeString(this.mBody);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.SmsAction

 * JD-Core Version:    0.7.0.1

 */