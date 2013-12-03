package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;

public class SetAlarmAction
        implements VoiceAction {
    public static final Parcelable.Creator<SetAlarmAction> CREATOR = new Parcelable.Creator() {
        public SetAlarmAction createFromParcel(Parcel paramAnonymousParcel) {
            int i = 1;
            String str = paramAnonymousParcel.readString();
            if (paramAnonymousParcel.readByte() == i) {
            }
            while (i != 0) {
                return new SetAlarmAction(str, paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
                i = 0;
            }
            return new SetAlarmAction(str);
        }

        public SetAlarmAction[] newArray(int paramAnonymousInt) {
            return new SetAlarmAction[paramAnonymousInt];
        }
    };
    private final boolean mHasTime;
    private final int mHour;
    private final String mLabel;
    private final int mMinute;

    public SetAlarmAction(String paramString) {
        this.mLabel = paramString;
        this.mHasTime = false;
        this.mHour = 0;
        this.mMinute = 0;
    }

    public SetAlarmAction(String paramString, int paramInt1, int paramInt2) {
        this.mLabel = paramString;
        this.mHasTime = true;
        this.mHour = paramInt1;
        this.mMinute = paramInt2;
    }

    public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor) {
        return paramVoiceActionVisitor.visit(this);
    }

    public boolean canExecute() {
        return this.mHasTime;
    }

    public int describeContents() {
        return 0;
    }

    public int getHour() {
        return this.mHour;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public int getMinute() {
        return this.mMinute;
    }

    public boolean hasTime() {
        return this.mHasTime;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeString(this.mLabel);
        if (this.mHasTime) {
        }
        for (int i = 1; ; i = 0) {
            paramParcel.writeByte((byte) i);
            if (this.mHasTime) {
                paramParcel.writeInt(this.mHour);
                paramParcel.writeInt(this.mMinute);
            }
            return;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.SetAlarmAction

 * JD-Core Version:    0.7.0.1

 */