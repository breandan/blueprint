package com.google.android.voicesearch.util;

import com.google.android.speech.exception.AudioRecognizeException;
import com.google.android.speech.exception.NetworkRecognizeException;
import com.google.android.speech.exception.NoMatchRecognizeException;
import com.google.android.speech.exception.RecognizeException;

public class ErrorUtils {
    public static boolean canResendSameAudio(RecognizeException paramRecognizeException) {
        if ((paramRecognizeException instanceof NetworkRecognizeException)) {
        }
        do {
            do {
                return true;
            }
            while ((paramRecognizeException instanceof Greco3RecognitionEngine.NoMatchesFromEmbeddedRecognizerException));
            if ((paramRecognizeException instanceof NoMatchRecognizeException)) {
                return false;
            }
            if ((paramRecognizeException instanceof AudioRecognizeException)) {
                return false;
            }
        }
        while (((paramRecognizeException instanceof Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException)) || (!(paramRecognizeException instanceof OfflineActionsManager.GrammarCompilationException)));
        return false;
    }

    public static int getErrorExplanation(RecognizeException paramRecognizeException) {
        if ((paramRecognizeException instanceof Greco3RecognitionEngine.NoMatchesFromEmbeddedRecognizerException)) {
            return 2131363655;
        }
        return 0;
    }

    public static int getErrorImage(RecognizeException paramRecognizeException) {
        if (((paramRecognizeException instanceof Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException)) || ((paramRecognizeException instanceof Greco3RecognitionEngine.NoMatchesFromEmbeddedRecognizerException))) {
        }
        while (!(paramRecognizeException instanceof NoMatchRecognizeException)) {
            return 0;
        }
        return 2130837906;
    }

    public static int getErrorMessage(RecognizeException paramRecognizeException) {
        int i = 2131363424;
        if ((paramRecognizeException instanceof Greco3RecognitionEngine.NoMatchesFromEmbeddedRecognizerException)) {
            i = 2131363653;
        }
        do {
            do {
                return i;
            } while ((paramRecognizeException instanceof NetworkRecognizeException));
            if ((paramRecognizeException instanceof NoMatchRecognizeException)) {
                return 2131363423;
            }
            if ((paramRecognizeException instanceof AudioRecognizeException)) {
                return 2131363426;
            }
        }
        while ((paramRecognizeException instanceof Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException));
        if ((paramRecognizeException instanceof OfflineActionsManager.GrammarCompilationException)) {
            return 2131363427;
        }
        return 2131363425;
    }

    public static int getErrorTitle(RecognizeException paramRecognizeException) {
        if ((paramRecognizeException instanceof Greco3RecognitionEngine.NoMatchesFromEmbeddedRecognizerException)) {
            return 2131363654;
        }
        return 0;
    }

    public static int getErrorTypeForLogs(RecognizeException paramRecognizeException) {
        if ((paramRecognizeException instanceof AudioRecognizeException)) {
            return 2;
        }
        if ((paramRecognizeException instanceof NoMatchRecognizeException)) {
            return 3;
        }
        return 1;
    }

    public static int getRecognizerIntentError(RecognizeException paramRecognizeException) {
        if ((paramRecognizeException instanceof AudioRecognizeException)) {
            return 5;
        }
        if ((paramRecognizeException instanceof NetworkRecognizeException)) {
            return 4;
        }
        if ((paramRecognizeException instanceof NoMatchRecognizeException)) {
            return 1;
        }
        return 3;
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

 * Qualified Name:     com.google.android.voicesearch.util.ErrorUtils

 * JD-Core Version:    0.7.0.1

 */