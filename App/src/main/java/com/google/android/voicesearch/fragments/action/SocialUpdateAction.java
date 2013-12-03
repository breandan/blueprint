package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SocialUpdateAction
        implements VoiceAction {
    public static final Parcelable.Creator<SocialUpdateAction> CREATOR = new Parcelable.Creator() {
        public SocialUpdateAction createFromParcel(Parcel paramAnonymousParcel) {
            return new SocialUpdateAction(paramAnonymousParcel.readString(), SocialUpdateAction.SocialNetwork.values()[paramAnonymousParcel.readInt()]);
        }

        public SocialUpdateAction[] newArray(int paramAnonymousInt) {
            return new SocialUpdateAction[paramAnonymousInt];
        }
    };
    @Nullable
    private final String mMessage;
    @Nonnull
    private final SocialNetwork mSocialNetwork;

    public SocialUpdateAction(@Nullable String paramString, @Nonnull SocialNetwork paramSocialNetwork) {
        this.mSocialNetwork = ((SocialNetwork) Preconditions.checkNotNull(paramSocialNetwork));
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

    public SocialNetwork getSocialNetwork() {
        return this.mSocialNetwork;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeString(this.mMessage);
        paramParcel.writeInt(this.mSocialNetwork.ordinal());
    }

    public static enum SocialNetwork {
        final int nameResId;
        final int notSupportedResId;
        final String pkg;

        static {
            SocialNetwork[] arrayOfSocialNetwork = new SocialNetwork[2];
            arrayOfSocialNetwork[0] = GOOGLE_PLUS;
            arrayOfSocialNetwork[1] = TWITTER;
            $VALUES = arrayOfSocialNetwork;
        }

        private SocialNetwork(String paramString, int paramInt1, int paramInt2) {
            this.pkg = paramString;
            this.nameResId = paramInt1;
            this.notSupportedResId = paramInt2;
        }

        public int getNameResId() {
            return this.nameResId;
        }

        public int getNotSupportedResId() {
            return this.notSupportedResId;
        }

        public String getPkg() {
            return this.pkg;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.SocialUpdateAction

 * JD-Core Version:    0.7.0.1

 */