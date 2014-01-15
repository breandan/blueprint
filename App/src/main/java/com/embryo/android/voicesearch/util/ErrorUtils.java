package com.embryo.android.voicesearch.util;

import com.google.speech.embedded.Greco3RecognitionEngine;
import com.embryo.android.speech.exception.AudioRecognizeException;
import com.embryo.android.speech.exception.NetworkRecognizeException;
import com.embryo.android.speech.exception.NoMatchRecognizeException;
import com.embryo.android.speech.exception.RecognizeException;

public class ErrorUtils {

    public static int getErrorMessage(RecognizeException paramRecognizeException) {
        int i = 2131363424;
        if ((paramRecognizeException instanceof Greco3RecognitionEngine.NoMatchesFromEmbeddedRecognizerException)) {
            i = 2131363653;
        }
        do {
            if ((paramRecognizeException instanceof NetworkRecognizeException)) {
                return i;
            }
            if ((paramRecognizeException instanceof NoMatchRecognizeException)) {
                return 2131363423;
            }
            if ((paramRecognizeException instanceof AudioRecognizeException)) {
                return 2131363426;
            }
        }
        while ((paramRecognizeException instanceof Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException));
//        if ((paramRecognizeException instanceof OfflineActionsManager.GrammarCompilationException)) {
//            return 2131363427;
//        }
        return 2131363425;
    }

    public static int getSpeechRecognizerError(RecognizeException paramRecognizeException) {
        if ((paramRecognizeException instanceof AudioRecognizeException)) {
            return 3;
        }
        if ((paramRecognizeException instanceof NetworkRecognizeException)) {
            return 2;
        }
        if ((paramRecognizeException instanceof NoMatchRecognizeException)) {
            return 7;
        }
        return 4;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ErrorUtils

 * JD-Core Version:    0.7.0.1

 */