package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;

import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.android.velvet.VelvetStrictMode;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResults;

public class LocalResultsAction
        implements VoiceAction {
    public static final Parcelable.Creator<LocalResultsAction> CREATOR = new Parcelable.Creator() {
        public LocalResultsAction createFromParcel(Parcel paramAnonymousParcel) {
            int i = 1;
            try {
                localEcoutezLocalResults = (EcoutezStructuredResponse.EcoutezLocalResults) ((ProtoParcelable) paramAnonymousParcel.readParcelable(getClass().getClassLoader())).getProto(EcoutezStructuredResponse.EcoutezLocalResults.class);
                int k = paramAnonymousParcel.readInt();
                if (paramAnonymousParcel.readByte() == i) {
                    LocalResultsAction localLocalResultsAction = new LocalResultsAction(localEcoutezLocalResults, i);
                    LocalResultsAction.access$002(localLocalResultsAction, k);
                    return localLocalResultsAction;
                }
            } catch (IllegalArgumentException localIllegalArgumentException) {
                for (; ; ) {
                    EcoutezStructuredResponse.EcoutezLocalResults localEcoutezLocalResults = new EcoutezStructuredResponse.EcoutezLocalResults();
                    VelvetStrictMode.logW("LocalResultsAction", "Couldn't restore local results");
                    continue;
                    int j = 0;
                }
            }
        }

        public LocalResultsAction[] newArray(int paramAnonymousInt) {
            return null;
        }
    };
    private final boolean mIsTelephoneCapable;
    private final EcoutezStructuredResponse.EcoutezLocalResults mResults;
    private int mTransportationMethod;

    public LocalResultsAction(EcoutezStructuredResponse.EcoutezLocalResults paramEcoutezLocalResults, boolean paramBoolean) {
        this.mResults = paramEcoutezLocalResults;
        this.mIsTelephoneCapable = paramBoolean;
        this.mTransportationMethod = paramEcoutezLocalResults.getTransportationMethod();
    }

    public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor) {
        return paramVoiceActionVisitor.visit(this);
    }

    public boolean canExecute() {
        return (this.mResults.getLocalResultCount() == 1) && ((this.mIsTelephoneCapable) || (this.mResults.getActionType() != 4));
    }

    public int describeContents() {
        return 0;
    }

    public EcoutezStructuredResponse.EcoutezLocalResults getResults() {
        return this.mResults;
    }

    public int getTransportationMethod() {
        return this.mTransportationMethod;
    }

    public void setTransportationMethod(int paramInt) {
        this.mTransportationMethod = paramInt;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeParcelable(ProtoParcelable.create(this.mResults), paramInt);
        paramParcel.writeInt(this.mTransportationMethod);
        if (this.mIsTelephoneCapable) {
        }
        for (int i = 1; ; i = 0) {
            paramParcel.writeByte((byte) i);
            return;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.LocalResultsAction

 * JD-Core Version:    0.7.0.1

 */