package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.voicesearch.ui.ActionEditorView;

public class OpenBookCard
        extends PlayMediaCard<OpenBookController>
        implements OpenBookController.Ui {
    private TextView mAuthorView;
    private TextView mTitleView;

    public OpenBookCard(Context paramContext) {
        super(paramContext);
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        ActionEditorView localActionEditorView = createMediaActionEditor(paramContext, paramLayoutInflater, paramViewGroup, paramBundle, 2130968783, 2131363451);
        this.mTitleView = ((TextView) localActionEditorView.findViewById(2131296870));
        this.mAuthorView = ((TextView) localActionEditorView.findViewById(2131296871));
        return localActionEditorView;
    }

    public void setAuthor(String paramString) {
        showTextIfNonEmpty(this.mAuthorView, paramString);
    }

    public void setTitle(String paramString) {
        this.mTitleView.setText(paramString);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.OpenBookCard

 * JD-Core Version:    0.7.0.1

 */