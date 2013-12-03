package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;

import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.voicesearch.contacts.ContactSelectMode;

public class PhoneCallAction
        extends CommunicationActionImpl {
    public static final Parcelable.Creator<PhoneCallAction> CREATOR = new Parcelable.Creator() {
        public PhoneCallAction createFromParcel(Parcel paramAnonymousParcel) {
            return new PhoneCallAction((PersonDisambiguation) paramAnonymousParcel.readParcelable(getClass().getClassLoader()));
        }

        public PhoneCallAction[] newArray(int paramAnonymousInt) {
            return new PhoneCallAction[paramAnonymousInt];
        }
    };

    public PhoneCallAction(PersonDisambiguation paramPersonDisambiguation) {
        this.mRecipient = paramPersonDisambiguation;
    }

    public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor) {
        return paramVoiceActionVisitor.visit(this);
    }

    public boolean canExecute() {
        return (this.mRecipient != null) && (this.mRecipient.isCompleted());
    }

    public int describeContents() {
        return 0;
    }

    public CommunicationAction forNewRecipient(PersonDisambiguation paramPersonDisambiguation) {
        return new PhoneCallAction(paramPersonDisambiguation);
    }

    public int getActionTypeLog() {
        return 10;
    }

    public ContactSelectMode getSelectMode() {
        return ContactSelectMode.CALL_CONTACT;
    }

    public String toString() {
        return "PhoneCallAction[recipient=" + getRecipient() + "]";
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeParcelable(this.mRecipient, 0);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.PhoneCallAction

 * JD-Core Version:    0.7.0.1

 */