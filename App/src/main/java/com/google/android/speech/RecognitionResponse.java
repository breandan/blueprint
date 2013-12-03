package com.google.android.speech;

import android.util.Log;

import com.google.android.speech.utils.ProtoBufUtils;
import com.google.common.base.Preconditions;
import com.google.speech.s3.S3.S3Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecognitionResponse {
    private final int mEngine;
    private final Object mResponse;
    private final int mType;

    public RecognitionResponse(int paramInt, @Nonnull RecognizerProtos.EndpointerEvent paramEndpointerEvent) {
        this.mEngine = paramInt;
        this.mType = 2;
        this.mResponse = paramEndpointerEvent;
    }

    public RecognitionResponse(int paramInt, @Nonnull S3.S3Response paramS3Response) {
        this.mEngine = paramInt;
        this.mType = 1;
        this.mResponse = Preconditions.checkNotNull(paramS3Response);
    }

    public RecognitionResponse(@Nonnull Boolean paramBoolean) {
        this.mEngine = 3;
        this.mType = 3;
        this.mResponse = paramBoolean;
    }

    public static RecognitionResponse fromS3Response(int paramInt, @Nonnull S3.S3Response paramS3Response) {
        if (!paramS3Response.hasRecognizerEventExtension()) {
            return new RecognitionResponse(paramInt, paramS3Response);
        }
        Recognizer.RecognizerEvent localRecognizerEvent = paramS3Response.getRecognizerEventExtension();
        if (localRecognizerEvent.hasRecognitionEvent() == localRecognizerEvent.hasEndpointerEvent()) {
            Log.w("RecognitionResponse", "Invalid response. Expecting exactly one recognition or endpointer event:" + ProtoBufUtils.toString(paramS3Response));
        }
        if (paramS3Response.getRecognizerEventExtension().hasEndpointerEvent()) {
            return new RecognitionResponse(paramInt, paramS3Response.getRecognizerEventExtension().getEndpointerEvent());
        }
        return new RecognitionResponse(paramInt, paramS3Response);
    }

    @Nullable
    public <T> T get(int paramInt) {
        if (paramInt == this.mType) {
            return this.mResponse;
        }
        throw new IllegalStateException("Requested type: " + paramInt + ", but was: " + this.mType);
    }

    public int getEngine() {
        return this.mEngine;
    }

    public String getEngineName() {
        switch (this.mEngine) {
            default:
                return "UNKNOWN";
            case 1:
                return "EMBEDDED";
            case 2:
                return "NETWORK";
        }
        return "SOUND_SEARCH";
    }

    public int getType() {
        return this.mType;
    }

    public String toString() {
        return super.toString();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.RecognitionResponse

 * JD-Core Version:    0.7.0.1

 */