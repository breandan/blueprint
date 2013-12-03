package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.search.core.SearchError;
import com.google.android.search.core.ears.SoundSearchError;
import com.google.android.search.shared.ui.SuggestionGridLayout.LayoutParams;
import com.google.android.search.shared.ui.SuggestionGridLayout.LayoutParams.AnimationType;
import com.google.android.voicesearch.ui.ActionEditorView;

public class ErrorCard
        extends AbstractCardView<ErrorController>
        implements ErrorController.Ui {
    private TextView mExplanation;
    private ImageView mImage;
    private TextView mTitle;

    public ErrorCard(Context paramContext) {
        super(paramContext);
    }

    protected void handleConfirmation(int paramInt) {
        if (paramInt == 100) {
            ((ErrorController) getController()).retry();
            return;
        }
        super.handleConfirmation(paramInt);
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        ActionEditorView localActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968669);
        this.mTitle = ((TextView) localActionEditorView.findViewById(2131296382));
        this.mExplanation = ((TextView) localActionEditorView.findViewById(2131296546));
        this.mImage = ((ImageView) localActionEditorView.findViewById(2131296545));
        setConfirmTag(100);
        localActionEditorView.setNoConfirmIcon();
        localActionEditorView.setContentClickable(false);
        SuggestionGridLayout.LayoutParams localLayoutParams = new SuggestionGridLayout.LayoutParams(-1, -2, 0);
        localLayoutParams.appearAnimationType = SuggestionGridLayout.LayoutParams.AnimationType.SLIDE_DOWN;
        localLayoutParams.canDismiss = false;
        setLayoutParams(localLayoutParams);
        return localActionEditorView;
    }

    public void showError(String paramString, SearchError paramSearchError) {
        if (paramSearchError == null) {
            this.mExplanation.setText("");
            return;
        }
        int i = paramSearchError.getErrorTitleResId();
        int j;
        if (i == 0) {
            i = paramSearchError.getErrorMessageResId();
            j = 0;
            if (i != 0) {
                break label120;
            }
            this.mTitle.setText(paramSearchError.getErrorMessage());
            label46:
            if (j != 0) {
                break label131;
            }
            this.mExplanation.setVisibility(8);
            this.mImage.setVisibility(8);
            label69:
            if ((!(paramSearchError instanceof SoundSearchError)) && ((!paramSearchError.isRetriable()) || (j == 0))) {
                break label169;
            }
            int k = paramSearchError.getButtonTextId();
            if (k == 0) {
                break label160;
            }
            setConfirmText(k);
        }
        for (; ; ) {
            showConfirmBar(true);
            return;
            j = paramSearchError.getErrorExplanationResId();
            break;
            label120:
            this.mTitle.setText(i);
            break label46;
            label131:
            this.mExplanation.setVisibility(0);
            this.mExplanation.setText(j);
            this.mImage.setVisibility(8);
            break label69;
            label160:
            setConfirmText(2131363288);
        }
        label169:
        showConfirmBar(false);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.ErrorCard

 * JD-Core Version:    0.7.0.1

 */