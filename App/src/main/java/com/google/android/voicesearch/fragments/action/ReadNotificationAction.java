package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;

public class ReadNotificationAction
        implements VoiceAction {
    public static final Parcelable.Creator<ReadNotificationAction> CREATOR = new Parcelable.Creator() {
        public ReadNotificationAction createFromParcel(Parcel paramAnonymousParcel) {
            return new ReadNotificationAction(paramAnonymousParcel.readString());
        }

        public ReadNotificationAction[] newArray(int paramAnonymousInt) {
            return new ReadNotificationAction[paramAnonymousInt];
        }
    };
    private final String mMessage;

    public ReadNotificationAction(String paramString) {
        this.mMessage = paramString;
    }

    public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor) {
        return paramVoiceActionVisitor.visit(this);
    }

    public boolean canExecute() {
        return false;
    }

    public int describeContents() {
        return 0;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeString(this.mMessage);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.ReadNotificationAction

 * JD-Core Version:    0.7.0.1

 */