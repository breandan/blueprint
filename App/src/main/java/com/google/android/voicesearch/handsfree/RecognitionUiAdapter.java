package com.google.android.voicesearch.handsfree;

import com.google.android.search.shared.api.RecognitionUi;

import javax.annotation.Nonnull;

class RecognitionUiAdapter
        implements RecognitionUi {
    private final HandsFreeRecognitionUi mHandsFreeUi;

    public RecognitionUiAdapter(HandsFreeRecognitionUi paramHandsFreeRecognitionUi) {
        this.mHandsFreeUi = paramHandsFreeRecognitionUi;
    }

    public void setFinalRecognizedText(@Nonnull CharSequence paramCharSequence) {
    }

    public void showRecognitionState(int paramInt) {
        if (paramInt == 4) {
            this.mHandsFreeUi.showListening();
        }
        while (paramInt != 2) {
            return;
        }
        this.mHandsFreeUi.showNotListening();
    }

    public void updateRecognizedText(String paramString1, String paramString2) {
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.handsfree.RecognitionUiAdapter

 * JD-Core Version:    0.7.0.1

 */