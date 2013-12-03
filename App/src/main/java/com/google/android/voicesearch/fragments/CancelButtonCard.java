package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.search.shared.ui.SuggestionGridLayout.LayoutParams;
import com.google.android.search.shared.ui.SuggestionGridLayout.LayoutParams.AnimationType;

public class CancelButtonCard
        extends AbstractCardView<CancelController>
        implements CancelController.Ui {
    private TextView mTitle;

    public CancelButtonCard(Context paramContext) {
        super(paramContext);
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(2130968618, paramViewGroup, false);
        localView.findViewById(2131296423).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ((CancelController) CancelButtonCard.this.getController()).cancelAction();
            }
        });
        this.mTitle = ((TextView) localView.findViewById(2131296423));
        SuggestionGridLayout.LayoutParams localLayoutParams = new SuggestionGridLayout.LayoutParams(-1, -2, 0);
        localLayoutParams.appearAnimationType = SuggestionGridLayout.LayoutParams.AnimationType.SLIDE_UP;
        localLayoutParams.canDismiss = false;
        setLayoutParams(localLayoutParams);
        return localView;
    }

    public void showMessage(int paramInt) {
        this.mTitle.setText(paramInt);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.CancelButtonCard

 * JD-Core Version:    0.7.0.1

 */