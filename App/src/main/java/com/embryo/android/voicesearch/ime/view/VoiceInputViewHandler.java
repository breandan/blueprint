package com.embryo.android.voicesearch.ime.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.voicesearch.ui.DrawSoundLevelsView;
import com.google.common.collect.Sets;

import java.util.HashSet;

public class VoiceInputViewHandler {
    private final Context mContext;
    private final com.embryo.android.shared.util.SpeechLevelSource mSpeechLevelSource;
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

    public VoiceInputViewHandler(Context paramContext, com.embryo.android.shared.util.SpeechLevelSource paramSpeechLevelSource) {
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
        if (visibleViews.contains(view)) {
            view.setVisibility(0x0);
            if (view == mSoundLevels) {
                mSoundLevels.setEnabled(true);
            }
            return;
        }
        view.setVisibility(0x4);
        if (view == mSoundLevels) {
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

    public void hideWaitingForResults() {
        this.mWaitingForResultBar.setVisibility(4);
    }

    private static enum State {
        AUDIO_NOT_INITIALIZED,
        ERROR,
        LISTENING,
        PAUSED,
        RECORDING,
        WORKING;
    }

}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     VoiceInputViewHandler

 * JD-Core Version:    0.7.0.1

 */