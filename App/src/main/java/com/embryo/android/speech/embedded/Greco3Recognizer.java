package com.embryo.android.speech.embedded;

import android.util.Log;

import com.embryo.android.speech.exception.AudioRecognizeException;
import com.embryo.android.speech.exception.RecognizeException;
import com.embryo.speech.recognizer.AbstractRecognizer;
import com.embryo.speech.recognizer.RecognizerCallback;
import com.embryo.speech.recognizer.api.RecognizerProtos;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class Greco3Recognizer
        extends AbstractRecognizer {
    private static boolean sSharedLibraryLoaded = false;
    private final int mBytesPerSample;
    private final RecognizerCallbackWrapper mCallback = new RecognizerCallbackWrapper();
    private long mProgressMs;
    private int mSamplingRate;

    public Greco3Recognizer(int paramInt1, int paramInt2) {
        this.mSamplingRate = paramInt1;
        this.mBytesPerSample = paramInt2;
        addCallback(this.mCallback);
    }

    public static Greco3Recognizer create(com.embryo.android.speech.embedded.Greco3EngineManager.Resources paramResources, int paramInt1, int paramInt2) {
        Greco3Recognizer g3Recognizer = new Greco3Recognizer(paramInt1, paramInt2);
        File configFile = new File(paramResources.configFile);
        byte[] fileBytes = getFileBytes(configFile);
        int status = 0;

        if (Greco3Mode.isAsciiConfiguration(configFile)) {
            status = g3Recognizer.initFromFile(paramResources.configFile,paramResources.resources);
        } else {
            if((fileBytes == null) || (fileBytes.length == 0)) {
                Log.e("Vs.G3Recognizer", "Error reading g3 config file: " + configFile);
                return null;
            }
            status = g3Recognizer.initFromProto(fileBytes, paramResources.resources);
        }

        if(status == 0) {
            return g3Recognizer;
        }

        Log.e("Vs.G3Recognizer", "Failed to bring up g3, Status code: " + status);
        return null;
    }

    private static byte[] getFileBytes(File paramFile) {
        try {
            byte[] arrayOfByte = Files.toByteArray(paramFile);
            return arrayOfByte;
        } catch (IOException localIOException) {
        }
        return null;
    }

    public static synchronized void maybeLoadSharedLibrary() {
        if (sSharedLibraryLoaded) {
            return;
        }
        try {
            System.loadLibrary("google_recognizer_jni_l");
        } catch (UnsatisfiedLinkError ule) {
            System.loadLibrary("google_recognizer_jni");
        }
        nativeInit();
        sSharedLibraryLoaded = true;
    }

    public int cancel() {
        this.mCallback.invalidate();
        return super.cancel();
    }

    protected int read(byte[] paramArrayOfByte)
            throws IOException {
        try {
            int i = super.read(paramArrayOfByte);
            if (i > 0) {
                this.mProgressMs += i * 1000 / (this.mBytesPerSample * this.mSamplingRate);
                if (this.mProgressMs % 200L == 0L) {
                    this.mCallback.updateProgress(this.mProgressMs);
                }
            }
            return i;
        } catch (IOException localIOException) {
            this.mCallback.notifyError(new AudioRecognizeException("Audio error", localIOException));
            throw localIOException;
        }
    }

    public void setCallback(com.embryo.android.speech.embedded.Greco3Callback paramGreco3Callback) {
        mCallback.mDelegate = paramGreco3Callback;
        mProgressMs = 0x0;
    }

    public void setSamplingRate(int paramInt) {
        this.mSamplingRate = paramInt;
    }

    private static class RecognizerCallbackWrapper
            implements RecognizerCallback {
        private static com.embryo.android.speech.embedded.Greco3Callback mDelegate;

        public void handleAudioLevelEvent(RecognizerProtos.AudioLevelEvent paramAudioLevelEvent) {
            if (this.mDelegate != null) {
                this.mDelegate.handleAudioLevelEvent(paramAudioLevelEvent);
            }
        }

        public void handleEndpointerEvent(RecognizerProtos.EndpointerEvent paramEndpointerEvent) {
            if (this.mDelegate != null) {
                this.mDelegate.handleEndpointerEvent(paramEndpointerEvent);
            }
        }

        public void handleRecognitionEvent(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
            if (this.mDelegate != null) {
                this.mDelegate.handleRecognitionEvent(paramRecognitionEvent);
            }
        }

        public void invalidate() {
            this.mDelegate = null;
        }

        public void notifyError(RecognizeException paramRecognizeException) {
            if (this.mDelegate != null) {
                this.mDelegate.handleError(paramRecognizeException);
            }
        }

        public void updateProgress(long paramLong) {
            if (this.mDelegate != null) {
                this.mDelegate.handleProgressUpdate(paramLong);
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3Recognizer

 * JD-Core Version:    0.7.0.1

 */