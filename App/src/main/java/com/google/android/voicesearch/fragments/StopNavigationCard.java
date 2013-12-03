package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.voicesearch.ui.ActionEditorView;

public class StopNavigationCard
        extends AbstractCardView<StopNavigationController>
        implements StopNavigationController.Ui {
    private TextView mContentView;

    public StopNavigationCard(Context paramContext) {
        super(paramContext);
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        ActionEditorView localActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968659);
        this.mContentView = ((TextView) localActionEditorView.findViewById(2131296519));
        localActionEditorView.setContentClickable(false);
        localActionEditorView.showCountDownView(true);
        localActionEditorView.setNoConfirmIcon();
        return localActionEditorView;
    }

    public void setText(String paramString) {
        this.mContentView.setVisibility(0);
        this.mContentView.setText(paramString);
        ((StopNavigationController) getController()).uiReady();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.StopNavigationCard

 * JD-Core Version:    0.7.0.1

 */