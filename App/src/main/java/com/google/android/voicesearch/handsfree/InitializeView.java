package com.google.android.voicesearch.handsfree;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

class InitializeView
        extends FrameLayout
        implements InitializeController.Ui {
    private final TextView mTextView;

    public InitializeView(Context paramContext) {
        super(paramContext);
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(2130968712, this);
        this.mTextView = ((TextView) findViewById(2131296678));
    }

    public void setMessage(int paramInt) {
        this.mTextView.setText(paramInt);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.handsfree.InitializeView

 * JD-Core Version:    0.7.0.1

 */