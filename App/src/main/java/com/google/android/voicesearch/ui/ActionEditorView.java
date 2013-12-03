package com.google.android.voicesearch.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.googlequicksearchbox.R.styleable;

public class ActionEditorView
        extends LinearLayout {
    private ActionEditorListener mActionEditorListener;
    private View mCancelButton;
    private final Drawable mCardBackground;
    private View mClickCatcher;
    private CountDownView mCountDownView;
    private TextView mFollowOnPromptView;
    private ViewGroup mMainContent;

    public ActionEditorView(Context paramContext) {
        this(paramContext, null);
    }

    public ActionEditorView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 2131624093);
    }

    public ActionEditorView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        TypedArray localTypedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ResultCard);
        this.mCardBackground = localTypedArray.getDrawable(0);
        localTypedArray.recycle();
    }

    private void handleContentClicked() {
        if (this.mActionEditorListener != null) {
            this.mActionEditorListener.onBailOut();
        }
        this.mCountDownView.cancelCountDownAnimation();
    }

    public void cancelCountDownAnimation() {
        this.mCountDownView.cancelCountDownAnimation();
    }

    public void disable(int paramInt) {
        setConfirmText(paramInt);
        setClickable(false);
        setConfirmationEnabled(false);
        setContentClickable(false);
        setBailOutEnabled(false);
        this.mCountDownView.grayOutConfirmIcon();
        this.mMainContent.setAlpha(0.5F);
    }

    protected void onFinishInflate() {
        findViewById(2131296289).setBackground(this.mCardBackground);
        this.mMainContent = ((ViewGroup) findViewById(2131296290));
        this.mCountDownView = ((CountDownView) findViewById(2131296491));
        this.mFollowOnPromptView = ((TextView) findViewById(2131296293));
        this.mClickCatcher = findViewById(2131296291);
        this.mClickCatcher.setClickable(true);
        this.mClickCatcher.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ActionEditorView.this.handleContentClicked();
            }
        });
        this.mCancelButton = findViewById(2131296671);
        if (this.mCancelButton != null) {
            this.mCancelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    if (ActionEditorView.this.mActionEditorListener != null) {
                        ActionEditorView.this.mActionEditorListener.onCancel();
                    }
                }
            });
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
        paramAccessibilityNodeInfo.setClassName(ActionEditorView.class.getCanonicalName());
    }

    public void setActionEditorListener(ActionEditorListener paramActionEditorListener) {
        this.mActionEditorListener = paramActionEditorListener;
        this.mCountDownView.setActionEditorListener(paramActionEditorListener);
    }

    public void setBailOutEnabled(boolean paramBoolean) {
        this.mMainContent.setClickable(paramBoolean);
        this.mMainContent.setEnabled(paramBoolean);
    }

    public void setConfirmIcon(int paramInt) {
        this.mCountDownView.setConfirmIcon(paramInt);
    }

    public void setConfirmIcon(View paramView) {
        this.mCountDownView.setConfirmIcon(paramView);
    }

    public void setConfirmTag(int paramInt) {
        this.mCountDownView.setTag(Integer.valueOf(paramInt));
    }

    public void setConfirmText(int paramInt) {
        this.mCountDownView.setConfirmText(paramInt);
    }

    public void setConfirmText(String paramString) {
        this.mCountDownView.setConfirmText(paramString);
    }

    public void setConfirmationEnabled(boolean paramBoolean) {
        this.mCountDownView.setConfirmationEnabled(paramBoolean);
    }

    public void setContentClickable(boolean paramBoolean) {
        this.mClickCatcher.setClickable(paramBoolean);
        this.mClickCatcher.setEnabled(paramBoolean);
    }

    public void setFollowOnPrompt(CharSequence paramCharSequence) {
        TextView localTextView;
        if (this.mFollowOnPromptView != null) {
            localTextView = this.mFollowOnPromptView;
            if (!TextUtils.isEmpty(paramCharSequence)) {
                break label36;
            }
        }
        label36:
        for (int i = 8; ; i = 0) {
            localTextView.setVisibility(i);
            this.mFollowOnPromptView.setText(paramCharSequence);
            return;
        }
    }

    public void setFollowOnPromptState(boolean paramBoolean) {
        if (this.mFollowOnPromptView != null) {
            this.mFollowOnPromptView.setActivated(paramBoolean);
        }
    }

    public void setMainContent(int paramInt) {
        this.mMainContent.removeAllViews();
        inflate(getContext(), paramInt, this.mMainContent);
    }

    public void setNoConfirmIcon() {
        this.mCountDownView.setNoConfirmIcon();
    }

    public void showCountDownView(boolean paramBoolean) {
        CountDownView localCountDownView = this.mCountDownView;
        if (paramBoolean) {
        }
        for (int i = 0; ; i = 8) {
            localCountDownView.setVisibility(i);
            return;
        }
    }

    public void showVoiceOfGoogleText(int paramInt, Object... paramVarArgs) {
        TextView localTextView = (TextView) findViewById(2131296292);
        localTextView.setText(getResources().getString(paramInt, paramVarArgs));
        localTextView.setVisibility(0);
    }

    public void startCountDownAnimation(long paramLong) {
        this.mCountDownView.startCountDownAnimation(paramLong);
    }

    public static abstract interface ActionEditorListener {
        public abstract void onBailOut();

        public abstract void onCancel();

        public abstract void onCancelCountdown();

        public abstract void onExecute(int paramInt);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ui.ActionEditorView

 * JD-Core Version:    0.7.0.1

 */