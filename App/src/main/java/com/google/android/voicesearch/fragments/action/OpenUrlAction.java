package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;
import android.text.TextUtils;

public class OpenUrlAction
        implements VoiceAction {
    public static final Parcelable.Creator<OpenUrlAction> CREATOR = new Parcelable.Creator() {
        public OpenUrlAction createFromParcel(Parcel paramAnonymousParcel) {
            return new OpenUrlAction(paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString());
        }

        public OpenUrlAction[] newArray(int paramAnonymousInt) {
            return new OpenUrlAction[paramAnonymousInt];
        }
    };
    private final String mDisplayLink;
    private final String mLink;
    private final String mRenderedLink;
    private final String mTitle;

    public OpenUrlAction(String paramString1, String paramString2, String paramString3, String paramString4) {
        this.mDisplayLink = paramString1;
        this.mLink = paramString2;
        this.mRenderedLink = paramString3;
        this.mTitle = paramString4;
    }

    public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor) {
        return paramVoiceActionVisitor.visit(this);
    }

    public boolean canExecute() {
        return !TextUtils.isEmpty(this.mLink);
    }

    public int describeContents() {
        return 0;
    }

    public String getDisplayLink() {
        return this.mDisplayLink;
    }

    public String getLink() {
        return this.mLink;
    }

    public String getRenderedLink() {
        return this.mRenderedLink;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeString(this.mDisplayLink);
        paramParcel.writeString(this.mLink);
        paramParcel.writeString(this.mRenderedLink);
        paramParcel.writeString(this.mTitle);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.OpenUrlAction

 * JD-Core Version:    0.7.0.1

 */