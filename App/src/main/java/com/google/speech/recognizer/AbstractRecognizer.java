package com.google.speech.recognizer;

import android.util.Log;

import com.embryo.speech.recognizer.RecognizerCallback;
import com.embryo.speech.recognizer.api.RecognizerSessionParamsProto;
import com.google.speech.recognizer.api.NativeRecognizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractRecognizer {
    private static final Logger logger = Logger.getLogger(AbstractRecognizer.class.getName());
    private List<RecognizerCallback> callbacks = new ArrayList(1);
    private long nativeObj = nativeConstruct();
    private InputStream reader;
    private ResourceManager rm;

    static {
        System.loadLibrary("google_recognizer_jni_l");
    }

    protected static native void nativeInit();

    private native int nativeCancel(long paramLong);

    private native long nativeConstruct();

    private native void nativeDelete(long paramLong);

    private native int nativeInitFromFile(long paramLong1, long paramLong2, String paramString);

    private native int nativeInitFromProto(long paramLong1, long paramLong2, byte[] paramArrayOfByte);

    private native byte[] nativeRun(long paramLong, byte[] paramArrayOfByte);

    private void validate() {
        if (this.nativeObj == 0L) {
            throw new IllegalStateException("recognizer is not initialized");
        }
    }

    public int addCallback(RecognizerCallback paramRecognizerCallback) {
        this.callbacks.add(paramRecognizerCallback);
        return 0;
    }

    public int cancel() {
        validate();
        return nativeCancel(nativeObj);
    }

    public void delete() {
        if (nativeObj != 0) {
            nativeDelete(nativeObj);
            nativeObj = 0;
        }
    }

    protected void finalize() {
        delete();
    }

    public NativeRecognizer.NativeRecognitionResult run(final RecognizerSessionParamsProto.RecognizerSessionParams sessionParams) {
        validate();

        try {
            byte[] resultBytes = nativeRun(nativeObj, sessionParams.toByteArray());
            return NativeRecognizer.NativeRecognitionResult.parseFrom(resultBytes);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "bad protocol buffer from recognizer jni");
            Log.e("jni_error", "exception", ex);
            return new NativeRecognizer.NativeRecognitionResult().setStatus(0x2);
        }
    }

    protected int read(byte[] buffer) throws IOException {
        if (buffer.length == 0) {
            throw new IOException("illegal zero length buffer");
        }
        int result = reader.read(buffer);
        if (result == -0x1) {
            return result;
        }
        return result;
    }

    protected void handleRecognitionEvent(byte[] bytes) throws com.embryo.protobuf.micro.InvalidProtocolBufferMicroException {
        com.embryo.speech.recognizer.api.RecognizerProtos.RecognitionEvent event = new com.embryo.speech.recognizer.api.RecognizerProtos.RecognitionEvent();
        event.mergeFrom(bytes);
        for (RecognizerCallback cb : callbacks) {
            cb.handleRecognitionEvent(event);
        }
    }

    protected void handleEndpointerEvent(byte[] bytes) throws com.embryo.protobuf.micro.InvalidProtocolBufferMicroException {
        com.embryo.speech.recognizer.api.RecognizerProtos.EndpointerEvent event = new com.embryo.speech.recognizer.api.RecognizerProtos.EndpointerEvent();
        event.mergeFrom(bytes);
        for (RecognizerCallback cb : callbacks) {
            cb.handleEndpointerEvent(event);
        }
    }

    protected void handleAudioLevelEvent(byte[] bytes) throws com.embryo.protobuf.micro.InvalidProtocolBufferMicroException {
        com.embryo.speech.recognizer.api.RecognizerProtos.AudioLevelEvent event = new com.embryo.speech.recognizer.api.RecognizerProtos.AudioLevelEvent();
        event.mergeFrom(bytes);
        for (RecognizerCallback cb : callbacks) {
            cb.handleAudioLevelEvent(event);
        }
    }

    public int initFromFile(String paramString, ResourceManager paramResourceManager) {
        validate();
        this.rm = paramResourceManager;
        return nativeInitFromFile(this.nativeObj, paramResourceManager.getNativeObject(), paramString);
    }

    public int initFromProto(byte[] paramArrayOfByte, ResourceManager paramResourceManager) {
        validate();
        this.rm = paramResourceManager;
        return nativeInitFromProto(this.nativeObj, paramResourceManager.getNativeObject(), paramArrayOfByte);
    }

    public int setAudioReader(InputStream paramInputStream) {
        this.reader = paramInputStream;
        return 0;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     AbstractRecognizer

 * JD-Core Version:    0.7.0.1

 */