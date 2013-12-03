package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.voicesearch.ui.ActionEditorView;

public class SocialUpdateCard
        extends AbstractCardView<SocialUpdateController>
        implements SocialUpdateController.Ui {
    private TextView mUnsupportedText;
    private TextView mUpdateText;

    public SocialUpdateCard(Context paramContext) {
        super(paramContext);
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        ActionEditorView localActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968835);
        localActionEditorView.setConfirmIcon(2130837894);
        this.mUpdateText = ((TextView) localActionEditorView.findViewById(2131297013));
        this.mUnsupportedText = ((TextView) localActionEditorView.findViewById(2131297014));
        TextView[] arrayOfTextView = new TextView[2];
        arrayOfTextView[0] = this.mUpdateText;
        arrayOfTextView[1] = this.mUnsupportedText;
        clearTextViews(arrayOfTextView);
        return localActionEditorView;
    }

    public void showDisabled() {
        setConfirmIcon(2130837679);
        setConfirmText(2131363623);
        this.mUnsupportedText.setVisibility(0);
        this.mUpdateText.setVisibility(8);
    }

    public void showEditPost(SocialUpdateAction.SocialNetwork paramSocialNetwork, CharSequence paramCharSequence) {
        setConfirmIcon(2130837642);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = getContext().getString(paramSocialNetwork.getNameResId());
        setConfirmText(2131363550, arrayOfObject);
        TextView localTextView = this.mUpdateText;
        if (TextUtils.isEmpty(paramCharSequence)) {
            paramCharSequence = getContext().getString(2131363300);
        }
        localTextView.setText(paramCharSequence);
        this.mUpdateText.setVisibility(0);
        this.mUnsupportedText.setText(getContext().getString(paramSocialNetwork.getNotSupportedResId()));
        this.mUnsupportedText.setVisibility(8);
        ((SocialUpdateController) getController()).uiReady();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.SocialUpdateCard

 * JD-Core Version:    0.7.0.1

 */