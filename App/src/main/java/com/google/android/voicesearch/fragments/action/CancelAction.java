package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;

import javax.annotation.Nullable;

public class CancelAction
        implements VoiceAction {
    public static final CancelAction CANCEL_BUTTON = new CancelAction(2131361885);
    public static final Parcelable.Creator<CancelAction> CREATOR = new Parcelable.Creator() {
        public CancelAction createFromParcel(Parcel paramAnonymousParcel) {
            int i = paramAnonymousParcel.readInt();
            if (i == 0) {
                return CancelAction.CANCEL_BUTTON;
            }
            return new CancelAction(i, null);
        }

        public CancelAction[] newArray(int paramAnonymousInt) {
            return new CancelAction[paramAnonymousInt];
        }
    };
    private final int mMessageId;

    private CancelAction(int paramInt) {
        this.mMessageId = paramInt;
    }

    public static CancelAction fromVoiceAction(@Nullable VoiceAction paramVoiceAction) {
        if ((paramVoiceAction instanceof PhoneCallAction)) {
            return new CancelAction(2131363557);
        }
        if ((paramVoiceAction instanceof EmailAction)) {
            return new CancelAction(2131363558);
        }
        if ((paramVoiceAction instanceof SmsAction)) {
            return new CancelAction(2131363559);
        }
        return new CancelAction(2131363560);
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

    public int getMessageId() {
        return this.mMessageId;
    }

    public boolean isCancelButton() {
        return this == CANCEL_BUTTON;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeInt(this.mMessageId);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.CancelAction

 * JD-Core Version:    0.7.0.1

 */