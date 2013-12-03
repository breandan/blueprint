package com.google.android.voicesearch.handsfree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.contacts.Contact;
import com.google.common.base.Preconditions;

class PhoneCallContactView
        extends FrameLayout
        implements PhoneCallContactController.Ui {
    private final TextView mContactNameView;
    private final TextView mContactPhoneTypeView;
    private PhoneCallContactController mController;
    private final RecognizerViewHelper mRecognizerViewHelper;

    public PhoneCallContactView(Context paramContext) {
        super(paramContext);
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(2130968707, this);
        findViewById(2131296671).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                PhoneCallContactView.this.mController.cancelByTouch();
            }
        });
        findViewById(2131296496).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                PhoneCallContactView.this.mController.callContactByTouch();
            }
        });
        this.mContactNameView = ((TextView) findViewById(2131296464));
        this.mContactPhoneTypeView = ((TextView) findViewById(2131296466));
        this.mRecognizerViewHelper = new RecognizerViewHelper(this);
    }

    public void setContact(Contact paramContact) {
        Preconditions.checkNotNull(paramContact);
        ExtraPreconditions.checkMainThread();
        this.mContactNameView.setText(paramContact.getName());
        this.mContactPhoneTypeView.setText(paramContact.getLabel(getResources()));
    }

    public void setController(PhoneCallContactController paramPhoneCallContactController) {
        if (this.mController == null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mController = ((PhoneCallContactController) Preconditions.checkNotNull(paramPhoneCallContactController));
            return;
        }
    }

    public void setLanguage(String paramString) {
        this.mRecognizerViewHelper.setLanguage(paramString);
    }

    public void showListening() {
        this.mRecognizerViewHelper.showListening();
    }

    public void showNotListening() {
        this.mRecognizerViewHelper.showNotListening();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.handsfree.PhoneCallContactView

 * JD-Core Version:    0.7.0.1

 */