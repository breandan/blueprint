package com.google.android.voicesearch.ime.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.ui.DrawSoundLevelsView;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.util.HashSet;

public class VoiceInputViewHandler {
    private final Context mContext;
    private final SpeechLevelSource mSpeechLevelSource;
    private final ViewBuilder mViewBuilder;
    private RelativeLayout mClickableMicLevels;
    private View[] mClickables;
    private ImageView mImageIndicator;
    private TextView mImeStateView;
    private View mInputView;
    private LanguageSpinner mLanguageView;
    private ImageView mPrevImeView;
    private DrawSoundLevelsView mSoundLevels;
    private View.OnClickListener mStartRecognitionListener;
    private State mState;
    private Button mStopButton;
    private View.OnClickListener mStopRecognitionListener;
    private TextView mTitleView;
    private View[] mViews;
    private View mWaitingForResultBar;

    public VoiceInputViewHandler(Context paramContext, SpeechLevelSource paramSpeechLevelSource) {
        this.mContext = paramContext;
        this.mSpeechLevelSource = paramSpeechLevelSource;
        this.mViewBuilder = ViewBuilder.create(paramContext);
    }

    private void setOnClickListener(View.OnClickListener paramOnClickListener) {
        for (View localView : this.mClickables) {
            if (localView != null) {
                localView.setOnClickListener(paramOnClickListener);
                if (paramOnClickListener == null) {
                    localView.setContentDescription(null);
                }
            }
        }
    }

    private void showView(View view, HashSet<View> visibleViews) {
        if(visibleViews.contains(view)) {
            view.setVisibility(0x0);
            if(view == mSoundLevels) {
                mSoundLevels.setEnabled(true);
            }
            return;
        }
        view.setVisibility(0x4);
        if(view == mSoundLevels) {
            mSoundLevels.setEnabled(false);
        }
    }

    private void showViews(View... paramVarArgs) {
        HashSet localHashSet = Sets.newHashSet(paramVarArgs);
        View[] arrayOfView = this.mViews;
        int i = arrayOfView.length;
        for (int j = 0; j < i; j++) {
            showView(arrayOfView[j], localHashSet);
        }
    }

    public void displayAudioNotInitialized() {
        this.mState = State.AUDIO_NOT_INITIALIZED;
        this.mInputView.setKeepScreenOn(true);
        View[] arrayOfView = new View[1];
        arrayOfView[0] = this.mImageIndicator;
        showViews(arrayOfView);
        this.mImageIndicator.setBackgroundResource(2130837777);
        setOnClickListener(null);
    }

    public void displayError(int paramInt) {
        this.mState = State.ERROR;
        this.mInputView.setKeepScreenOn(false);
        View[] arrayOfView = new View[3];
        arrayOfView[0] = this.mImageIndicator;
        arrayOfView[1] = this.mPrevImeView;
        arrayOfView[2] = this.mImeStateView;
        showViews(arrayOfView);
        this.mImageIndicator.setBackgroundResource(2130837995);
        setOnClickListener(null);
        this.mImeStateView.setText(paramInt);
    }

    public void displayListening() {
        this.mState = State.LISTENING;
        this.mInputView.setKeepScreenOn(true);
        View[] arrayOfView = new View[4];
        arrayOfView[0] = this.mLanguageView;
        arrayOfView[1] = this.mSoundLevels;
        arrayOfView[2] = this.mImageIndicator;
        arrayOfView[3] = this.mImeStateView;
        showViews(arrayOfView);
        this.mImageIndicator.setBackgroundResource(2130837990);
        setOnClickListener(this.mStopRecognitionListener);
        this.mImeStateView.setText(2131363461);
        this.mClickableMicLevels.setContentDescription(this.mContext.getString(2131363566));
    }

    public void displayPause(boolean paramBoolean) {
        this.mState = State.PAUSED;
        this.mInputView.setKeepScreenOn(false);
        View[] arrayOfView = new View[4];
        arrayOfView[0] = this.mLanguageView;
        arrayOfView[1] = this.mImageIndicator;
        arrayOfView[2] = this.mPrevImeView;
        arrayOfView[3] = this.mImeStateView;
        showViews(arrayOfView);
        if (paramBoolean) {
            this.mWaitingForResultBar.setVisibility(0);
        }
        this.mImageIndicator.setBackgroundResource(2130837987);
        setOnClickListener(this.mStartRecognitionListener);
        this.mImeStateView.setText(2131363565);
        this.mClickableMicLevels.setContentDescription(this.mContext.getString(2131363565));
    }

    public void displayRecording() {
        this.mState = State.RECORDING;
        this.mInputView.setKeepScreenOn(true);
        View[] arrayOfView = new View[4];
        arrayOfView[0] = this.mLanguageView;
        arrayOfView[1] = this.mSoundLevels;
        arrayOfView[2] = this.mImageIndicator;
        arrayOfView[3] = this.mImeStateView;
        showViews(arrayOfView);
        this.mImageIndicator.setBackgroundResource(2130837993);
        setOnClickListener(this.mStopRecognitionListener);
        this.mImeStateView.setText(2131363566);
        this.mClickableMicLevels.setContentDescription(this.mContext.getString(2131363566));
    }

    public void displayWorking() {
        this.mState = State.WORKING;
        this.mInputView.setKeepScreenOn(true);
        View[] arrayOfView = new View[3];
        arrayOfView[0] = this.mTitleView;
        arrayOfView[1] = this.mWaitingForResultBar;
        arrayOfView[2] = this.mStopButton;
        showViews(arrayOfView);
        setOnClickListener(null);
        this.mTitleView.setText(2131363568);
    }

    public View getView(final Callback paramCallback) {
        this.mInputView = this.mViewBuilder.createView(this.mContext);
        this.mStartRecognitionListener = new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                paramCallback.startRecognition();
            }
        };
        this.mStopRecognitionListener = new
                View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        paramCallback.stopRecognition();
                    }
                };
        this.mTitleView = ((TextView) Preconditions.checkNotNull((TextView) this.mInputView.findViewById(2131296382)));
        this.mSoundLevels = ((DrawSoundLevelsView) Preconditions.checkNotNull((DrawSoundLevelsView) this.mInputView.findViewById(2131296715)));
        this.mSoundLevels.setLevelSource(this.mSpeechLevelSource);
        this.mImageIndicator = ((ImageView) Preconditions.checkNotNull((ImageView) this.mInputView.findViewById(2131296717)));
        this.mLanguageView = ((LanguageSpinner) Preconditions.checkNotNull((LanguageSpinner) this.mInputView.findViewById(2131296707)));
        this.mLanguageView.setCallback(paramCallback);
        this.mPrevImeView = ((ImageView) Preconditions.checkNotNull((ImageView) this.mInputView.findViewById(2131296709)));
        this.mPrevImeView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                EventLogger.recordClientEvent(37);
                paramCallback.close();
            }
        });
        this.mImeStateView = ((TextView) Preconditions.checkNotNull((TextView) this.mInputView.findViewById(2131296712)));
        this.mWaitingForResultBar = ((View) Preconditions.checkNotNull(this.mInputView.findViewById(2131296716)));
        this.mStopButton = ((Button) Preconditions.checkNotNull((Button) this.mInputView.findViewById(2131296713)));
        this.mStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                paramCallback.forceClose();
            }
        });
        this.mClickableMicLevels = ((RelativeLayout) Preconditions.checkNotNull(this.mInputView.findViewById(2131296708)));
        View[] arrayOfView1 = new View[4];
        arrayOfView1[0] = this.mInputView.findViewById(2131296711);
        arrayOfView1[1] = this.mInputView.findViewById(2131296710);
        arrayOfView1[2] = this.mInputView.findViewById(2131296712);
        arrayOfView1[3] = this.mClickableMicLevels;
        this.mClickables = arrayOfView1;
        View[] arrayOfView2 = new View[8];
        arrayOfView2[0] = this.mTitleView;
        arrayOfView2[1] = this.mSoundLevels;
        arrayOfView2[2] = this.mImageIndicator;
        arrayOfView2[3] = this.mLanguageView;
        arrayOfView2[4] = this.mPrevImeView;
        arrayOfView2[5] = this.mImeStateView;
        arrayOfView2[6] = this.mStopButton;
        arrayOfView2[7] = this.mWaitingForResultBar;
        this.mViews = arrayOfView2;
        return this.mInputView;
    }

    public void hideWaitingForResults() {
        this.mWaitingForResultBar.setVisibility(4);
    }

    public boolean isListening() {
        return this.mState == State.LISTENING;
    }

    public boolean isPaused() {
        return this.mState == State.PAUSED;
    }

    public boolean isRecording() {
        return this.mState == State.RECORDING;
    }

    public void restoreState() {
        if (this.mState == State.RECORDING) {
            displayRecording();
            return;
        }
        if (this.mState == State.LISTENING) {
            displayListening();
            return;
        }
        if (this.mState == State.WORKING) {
            displayWorking();
            return;
        }
        if (this.mState == State.AUDIO_NOT_INITIALIZED) {
            displayAudioNotInitialized();
            return;
        }
        Log.e("VoiceInputViewHelper", "Restored into unexpected state: " + this.mState);
        displayPause(false);
    }

    public void setLanguages(String paramString, GstaticConfiguration.Dialect[] paramArrayOfDialect) {
        this.mLanguageView.setLanguages(paramString, paramArrayOfDialect);
    }

    private static enum State {
        AUDIO_NOT_INITIALIZED,
        ERROR,
        LISTENING,
        PAUSED,
        RECORDING,
        WORKING;
    }

    public static abstract interface Callback
            extends LanguageSpinner.Callback {
        public abstract void close();

        public abstract void forceClose();

        public abstract void startRecognition();

        public abstract void stopRecognition();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ime.view.VoiceInputViewHandler

 * JD-Core Version:    0.7.0.1

 */