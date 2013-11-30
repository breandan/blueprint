package com.google.android.speech.embedded;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.common.collect.Maps;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.speech.grammar.pumpkin.PumpkinTaggerResultsProto;
import com.google.speech.logs.VoicesearchClientLogProto;

import java.util.Iterator;
import java.util.Map;

public class TaggerResult
        implements Parcelable {
    public static final Parcelable.Creator<TaggerResult> CREATOR = new Parcelable.Creator() {
        public TaggerResult createFromParcel(Parcel paramAnonymousParcel) {
            return new TaggerResult(paramAnonymousParcel);
        }

        public TaggerResult[] newArray(int paramAnonymousInt) {
            return new TaggerResult[paramAnonymousInt];
        }
    };
    private final String mActionName;
    private final Map<String, String> mArguments = Maps.newHashMap();
    private final VoicesearchClientLogProto.EmbeddedParserDetails mEmbeddedParserDetails;

    public TaggerResult(Parcel paramParcel) {
        this.mActionName = paramParcel.readString();
        Bundle localBundle = paramParcel.readBundle();
        Iterator localIterator = localBundle.keySet().iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            this.mArguments.put(str, localBundle.getString(str));
        }
        this.mEmbeddedParserDetails = new VoicesearchClientLogProto.EmbeddedParserDetails();
        try {
            this.mEmbeddedParserDetails.mergeFrom(paramParcel.createByteArray());
            return;
        } catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {
            Log.e("TaggerResult", "Cannot read embedded parser details.");
        }
    }

    public TaggerResult(PumpkinTaggerResultsProto.HypothesisResult paramHypothesisResult, VoicesearchClientLogProto.EmbeddedParserDetails paramEmbeddedParserDetails) {
        this.mActionName = paramHypothesisResult.getActionName();
        this.mEmbeddedParserDetails = paramEmbeddedParserDetails;
        Iterator localIterator = paramHypothesisResult.getActionArgumentList().iterator();
        while (localIterator.hasNext()) {
            PumpkinTaggerResultsProto.ActionArgument localActionArgument = (PumpkinTaggerResultsProto.ActionArgument) localIterator.next();
            this.mArguments.put(localActionArgument.getName(), localActionArgument.getValue());
        }
    }

    public int describeContents() {
        return 0;
    }

    public String getActionName() {
        return this.mActionName;
    }

    public String getArgument(String paramString) {
        return (String) this.mArguments.get(paramString);
    }

    public VoicesearchClientLogProto.EmbeddedParserDetails getEmbeddedParserDetails() {
        return this.mEmbeddedParserDetails;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeString(this.mActionName);
        Bundle localBundle = new Bundle();
        Iterator localIterator = this.mArguments.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            localBundle.putString((String) localEntry.getKey(), (String) localEntry.getValue());
        }
        paramParcel.writeBundle(localBundle);
        paramParcel.writeByteArray(this.mEmbeddedParserDetails.toByteArray());
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.TaggerResult

 * JD-Core Version:    0.7.0.1

 */