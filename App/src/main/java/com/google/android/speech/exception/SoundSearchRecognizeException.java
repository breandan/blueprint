package com.google.android.speech.exception;

public class SoundSearchRecognizeException
        extends RecognizeException {
    private final RecognizeException mOriginalException;

    public SoundSearchRecognizeException(RecognizeException paramRecognizeException) {
        this.mOriginalException = paramRecognizeException;
    }

    public RecognizeException getOriginalException() {
        return this.mOriginalException;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.exception.SoundSearchRecognizeException

 * JD-Core Version:    0.7.0.1

 */