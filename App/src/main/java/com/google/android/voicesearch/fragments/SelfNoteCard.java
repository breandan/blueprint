package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.voicesearch.ui.ActionEditorView;

public class SelfNoteCard
        extends AbstractCardView<SelfNoteController>
        implements SelfNoteController.Ui {
    private TextView mNoteText;

    public SelfNoteCard(Context paramContext) {
        super(paramContext);
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        ActionEditorView localActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968823);
        this.mNoteText = ((TextView) localActionEditorView.findViewById(2131296985));
        return localActionEditorView;
    }

    public void setNoteText(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            this.mNoteText.setText(getContext().getString(2131363300));
        }
        for (; ; ) {
            ((SelfNoteController) getController()).uiReady();
            return;
            this.mNoteText.setText(paramString);
        }
    }

    public void showDisabled() {
        disableActionEditor(2131363673);
    }

    public void showNewNote() {
        setConfirmIcon(2130837642);
        setConfirmText(2131363604);
    }

    public void showSaveNote() {
        setConfirmIcon(2130837692);
        setConfirmText(2131363605);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.SelfNoteCard

 * JD-Core Version:    0.7.0.1

 */