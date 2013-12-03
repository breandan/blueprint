package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import com.google.android.voicesearch.ui.ActionEditorView;

public class SetAlarmCard
        extends AbstractCardView<SetAlarmController>
        implements SetAlarmController.Ui {
    private boolean isLabelSet;
    private boolean isTimeSet;
    private TextView mLabelView;
    private TextView mTimeView;

    public SetAlarmCard(Context paramContext) {
        super(paramContext);
    }

    private void checkUiReady() {
        if ((this.isLabelSet) && (this.isTimeSet)) {
            ((SetAlarmController) getController()).uiReady();
        }
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        ActionEditorView localActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968824);
        this.mLabelView = ((TextView) localActionEditorView.findViewById(2131296987));
        this.mLabelView.setVisibility(8);
        this.mTimeView = ((TextView) localActionEditorView.findViewById(2131296986));
        setConfirmIcon(2130837618);
        return localActionEditorView;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
        paramAccessibilityNodeInfo.setClassName(SetAlarmCard.class.getCanonicalName());
    }

    public void setLabel(String paramString) {
        this.mLabelView.setText(paramString);
        if (TextUtils.isEmpty(paramString)) {
            this.mLabelView.setVisibility(8);
        }
        for (; ; ) {
            this.isLabelSet = true;
            checkUiReady();
            return;
            this.mLabelView.setVisibility(0);
        }
    }

    public void setTime(String paramString) {
        this.mTimeView.setText(paramString);
        if (TextUtils.isEmpty(paramString)) {
            this.mTimeView.setVisibility(8);
        }
        for (; ; ) {
            this.isTimeSet = true;
            checkUiReady();
            return;
            this.mTimeView.setVisibility(0);
        }
    }

    public void showDisabled() {
        disableActionEditor(2131363675);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.SetAlarmCard

 * JD-Core Version:    0.7.0.1

 */