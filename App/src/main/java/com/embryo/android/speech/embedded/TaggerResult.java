package com.embryo.android.speech.embedded;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.embryo.protobuf.micro.InvalidProtocolBufferMicroException;
import com.embryo.speech.grammar.pumpkin.PumpkinTaggerResultsProto;
import com.embryo.speech.logs.VoicesearchClientLogProto;
import com.google.common.collect.Maps;

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
        return this.mArguments.get(paramString);
    }

    public VoicesearchClientLogProto.EmbeddedParserDetails getEmbeddedParserDetails() {
        return this.mEmbeddedParserDetails;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mActionName);
        Bundle argumentsBundle = new Bundle();
        for(Map.Entry<String, String> entry : mArguments.entrySet()) {
            argumentsBundle.putString(entry.getKey(), entry.getValue());
        }
        out.writeBundle(argumentsBundle);
        out.writeByteArray(mEmbeddedParserDetails.toByteArray());
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     TaggerResult

 * JD-Core Version:    0.7.0.1

 */