package com.embryo.android.speech;

import com.embryo.speech.s3.S3;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecognitionResponse {
    private final int mEngine;
    private final Object mResponse;
    private final int mType;

    public RecognitionResponse(int paramInt, @Nonnull com.embryo.speech.recognizer.api.RecognizerProtos.EndpointerEvent paramEndpointerEvent) {
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
        return new RecognitionResponse(paramInt, paramS3Response);
    }

    @Nullable
    public <T> T get(int paramInt) {
        if (paramInt == this.mType) {
            return (T) this.mResponse;
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
            case 3:
                return "SOUND_SEARCH";
        }
    }

    public int getType() {
        return this.mType;
    }

    public String toString() {
        return super.toString();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionResponse

 * JD-Core Version:    0.7.0.1

 */