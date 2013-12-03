package com.google.android.voicesearch.intentapi;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.search.shared.ui.SoundLevels;
import com.google.android.shared.util.SpeechLevelSource;

public class IntentApiViewHelper {
    private Callback mCallback;
    private View.OnClickListener mCancelRecognitionClick;
    private TextView mLanguage;
    private final ImageView mMicImage;
    private final ProgressBar mProgressBar;
    private final TextView mPrompt;
    private final SoundLevels mSoundLevels;
    private View.OnClickListener mStopRecognitionClick;
    private final TextView mTitle;

    public IntentApiViewHelper(View paramView) {
        this.mSoundLevels = ((SoundLevels) paramView.findViewById(2131296715));
        this.mMicImage = ((ImageView) paramView.findViewById(2131296528));
        this.mPrompt = ((TextView) paramView.findViewById(2131296732));
        this.mTitle = ((TextView) paramView.findViewById(2131296731));
        this.mLanguage = ((TextView) paramView.findViewById(2131296730));
        this.mProgressBar = ((ProgressBar) paramView.findViewById(2131296716));
        this.mStopRecognitionClick = new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (IntentApiViewHelper.this.mCallback != null) {
                    IntentApiViewHelper.this.mCallback.onStopRecordingClicked();
                }
            }
        };
        this.mCancelRecognitionClick = new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (IntentApiViewHelper.this.mCallback != null) {
                    IntentApiViewHelper.this.mCallback.onCancelRecordingClicked();
                }
            }
        };
    }

    public void setCallback(Callback paramCallback) {
        this.mCallback = paramCallback;
    }

    public void setLanguage(CharSequence paramCharSequence) {
        this.mLanguage.setText(paramCharSequence);
    }

    public void setSpeechLevelSource(SpeechLevelSource paramSpeechLevelSource) {
        this.mSoundLevels.setLevelSource(paramSpeechLevelSource);
    }

    public void setText(int paramInt) {
        this.mPrompt.setText(paramInt);
    }

    public void setText(CharSequence paramCharSequence) {
        this.mPrompt.setText(paramCharSequence);
    }

    public void showInitializing() {
        this.mSoundLevels.setEnabled(false);
        this.mSoundLevels.setVisibility(4);
        this.mLanguage.setVisibility(4);
        this.mMicImage.setBackgroundResource(2130837777);
        this.mMicImage.setOnClickListener(this.mCancelRecognitionClick);
        this.mPrompt.setVisibility(4);
        this.mProgressBar.setVisibility(4);
    }

    public void showListening() {
        this.mSoundLevels.setEnabled(false);
        this.mSoundLevels.setVisibility(4);
        this.mLanguage.setVisibility(0);
        this.mMicImage.setBackgroundResource(2130837990);
        this.mMicImage.setOnClickListener(this.mCancelRecognitionClick);
        this.mPrompt.setVisibility(0);
        this.mProgressBar.setVisibility(4);
    }

    public void showRecognizing() {
        this.mSoundLevels.setEnabled(false);
        this.mSoundLevels.setVisibility(4);
        this.mLanguage.setVisibility(4);
        this.mMicImage.setBackgroundResource(2130837987);
        this.mMicImage.setOnClickListener(this.mCancelRecognitionClick);
        this.mPrompt.setVisibility(0);
        this.mProgressBar.setVisibility(0);
    }

    public void showRecording() {
        this.mSoundLevels.setEnabled(true);
        this.mSoundLevels.setVisibility(0);
        this.mLanguage.setVisibility(0);
        this.mMicImage.setBackgroundResource(2130837993);
        this.mMicImage.setOnClickListener(this.mStopRecognitionClick);
        this.mPrompt.setVisibility(0);
        this.mProgressBar.setVisibility(4);
    }

    public static abstract interface Callback {
        public abstract void onCancelRecordingClicked();

        public abstract void onStopRecordingClicked();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.intentapi.IntentApiViewHelper

 * JD-Core Version:    0.7.0.1

 */