/**
 * Generated by smali2java 1.0.0.558
 * Copyright (C) 2013 Hensence.com
 */

package com.embryo.android.speech.message;

import android.util.Log;


import com.embryo.android.speech.listeners.RecognitionEventListener;
import com.embryo.speech.s3.S3;
import com.embryo.speech.speech.s3.Recognizer;

import java.io.ByteArrayOutputStream;

public class GsaS3ResponseProcessor implements S3ResponseProcessor {
    private final ByteArrayOutputStream mAudioBytes;

    public GsaS3ResponseProcessor() {
        mAudioBytes = new ByteArrayOutputStream();
    }

    public void process(S3.S3Response response, RecognitionEventListener callback) {
        switch (response.getStatus()) {
            case 0: {
                if (response.hasRecognizerEventExtension())
                    processRecognizerEvent(callback, response.getRecognizerEventExtension());
                return;
            }
            case 1: {
                processDone(callback, response);
                return;
            }
            case 2: {
                throw new IllegalStateException("Error S3Response received via onResult");
            }
            case 3: {
                Log.w("GsaS3ResponseProcessor", "NOT_STARTED received");
                break;
            }
        }
    }

    private void processRecognizerEvent(RecognitionEventListener callback, Recognizer.RecognizerEvent recognizerEvent) {
        callback.onRecognitionResult(recognizerEvent.getRecognitionEvent());
    }

    private void processDone(RecognitionEventListener callback, S3.S3Response response) {
        for (String debugLine : response.getDebugLineList()) {
            Log.d("GsaS3ResponseProcessor", "DBG: " + debugLine);
        }
        callback.onDone();
    }
}