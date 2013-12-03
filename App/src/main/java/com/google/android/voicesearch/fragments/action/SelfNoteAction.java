package com.google.android.voicesearch.fragments.action;

import android.net.Uri;
import android.os.Parcel;
import android.text.TextUtils;

import java.util.concurrent.Future;

import javax.annotation.Nullable;

public class SelfNoteAction
        implements VoiceAction {
    public static final Parcelable.Creator<SelfNoteAction> CREATOR = new Parcelable.Creator() {
        public SelfNoteAction createFromParcel(Parcel paramAnonymousParcel) {
            return new SelfNoteAction(paramAnonymousParcel.readString(), null);
        }

        public SelfNoteAction[] newArray(int paramAnonymousInt) {
            return new SelfNoteAction[paramAnonymousInt];
        }
    };
    @Nullable
    private final Future<Uri> mAudioUri;
    @Nullable
    private final String mNote;

    public SelfNoteAction(@Nullable String paramString, @Nullable Future<Uri> paramFuture) {
        this.mNote = paramString;
        this.mAudioUri = paramFuture;
    }

    public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor) {
        return paramVoiceActionVisitor.visit(this);
    }

    public boolean canExecute() {
        return !TextUtils.isEmpty(this.mNote);
    }

    public int describeContents() {
        return 0;
    }

    public Future<Uri> getAudioUri() {
        return this.mAudioUri;
    }

    public String getNote() {
        return this.mNote;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeString(this.mNote);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.SelfNoteAction

 * JD-Core Version:    0.7.0.1

 */