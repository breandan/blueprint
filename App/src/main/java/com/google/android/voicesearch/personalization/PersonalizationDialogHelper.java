package com.google.android.voicesearch.personalization;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.shared.util.TextUtil;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.ui.URLObservableSpan;

public class PersonalizationDialogHelper
        implements URLObservableSpan.URLSpanListener {
    private final Context mContext;
    private final Settings mSettings;

    public PersonalizationDialogHelper(Context paramContext, Settings paramSettings) {
        this.mContext = paramContext;
        this.mSettings = paramSettings;
    }

    private void setEnabled(boolean paramBoolean, int paramInt) {
        Settings localSettings = this.mSettings;
        int i;
        if (paramBoolean) {
            i = 4;
            localSettings.setPersonalizationValue(i);
            if (paramInt == 2) {
                if (!paramBoolean) {
                    break label43;
                }
            }
        }
        label43:
        for (int j = 48; ; j = 49) {
            EventLogger.recordClientEvent(j);
            return;
            i = 3;
            break;
        }
    }

    public Dialog createDialog(int paramInt, Callbacks paramCallbacks) {
        ContextThemeWrapper localContextThemeWrapper = new ContextThemeWrapper(this.mContext, 16973935);
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(localContextThemeWrapper);
        localBuilder.setTitle(2131363430);
        LayoutInflater localLayoutInflater = (LayoutInflater) localContextThemeWrapper.getSystemService("layout_inflater");
        String str;
        int i;
        int j;
        View localView;
        TextView localTextView2;
        switch (paramInt) {
            default:
                Log.e("PersonalizationOptInActivity", "unknown dialog " + paramInt);
                return null;
            case 2:
                str = TextUtil.replaceLink(localContextThemeWrapper.getString(2131363435), this.mSettings.getConfiguration().getHelp().getPrivacyUrl());
                i = 1;
                j = 1;
                localBuilder.setPositiveButton(2131363438, new EnableButtonListener(localContextThemeWrapper, paramInt));
                localBuilder.setNegativeButton(2131363439, new DisableButtonListener(paramInt));
                localView = localLayoutInflater.inflate(2130968773, null);
                if (i == 0) {
                    localView.findViewById(2131296843).setVisibility(8);
                }
                TextView localTextView1 = (TextView) localView.findViewById(2131296678);
                localTextView1.setText(URLObservableSpan.replace(Html.fromHtml(str), this));
                localTextView1.setMovementMethod(LinkMovementMethod.getInstance());
                localTextView2 = (TextView) localView.findViewById(2131296844);
                if (j != 0) {
                    localTextView2.setText(URLObservableSpan.replace(Html.fromHtml(TextUtil.createLinkTag(localContextThemeWrapper.getString(2131363436), this.mSettings.getConfiguration().getPersonalization().getMoreInfoUrl())), this));
                    localTextView2.setMovementMethod(LinkMovementMethod.getInstance());
                }
                break;
        }
        for (; ; ) {
            localBuilder.setView(localView);
            AlertDialog localAlertDialog = localBuilder.create();
            localAlertDialog.setOnCancelListener(paramCallbacks);
            localAlertDialog.setOnDismissListener(paramCallbacks);
            return localAlertDialog;
            str = TextUtil.replaceLink(localContextThemeWrapper.getString(2131363435), this.mSettings.getConfiguration().getHelp().getPrivacyUrl());
            j = 1;
            localBuilder.setPositiveButton(17039370, new EnableButtonListener(localContextThemeWrapper, paramInt));
            localBuilder.setNegativeButton(17039360, new DisableButtonListener(paramInt));
            i = 0;
            break;
            str = TextUtil.replaceLink(localContextThemeWrapper.getString(2131363437), this.mSettings.getConfiguration().getPersonalization().getDashboardUrl());
            localBuilder.setPositiveButton(17039370, new DisableButtonListener(paramInt));
            localBuilder.setNegativeButton(17039360, new EnableButtonListener(localContextThemeWrapper, paramInt));
            i = 0;
            j = 0;
            break;
            localTextView2.setVisibility(8);
        }
    }

    public void onClick(String paramString) {
        if (paramString.equals(this.mSettings.getConfiguration().getPersonalization().getMoreInfoUrl())) {
            EventLogger.recordClientEvent(46);
        }
        while (!paramString.equals(this.mSettings.getConfiguration().getPersonalization().getDashboardUrl())) {
            return;
        }
        EventLogger.recordClientEvent(47);
    }

    public static abstract interface Callbacks
            extends DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
    }

    private class DisableButtonListener
            implements DialogInterface.OnClickListener {
        private int mSource;

        public DisableButtonListener(int paramInt) {
            if (paramInt == i) {
                i = 1;
            }
            this.mSource = i;
        }

        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            PersonalizationDialogHelper.this.setEnabled(false, this.mSource);
        }
    }

    private class EnableButtonListener
            implements DialogInterface.OnClickListener {
        private final Activity mActivity;
        private final int mSource;

        public EnableButtonListener(Context paramContext, int paramInt) {
            if ((paramContext instanceof Activity)) {
            }
            for (this.mActivity = ((Activity) paramContext); ; this.mActivity = null) {
                if (paramInt == i) {
                    i = 1;
                }
                this.mSource = i;
                return;
            }
        }

        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            AccountManager localAccountManager = AccountManager.get(PersonalizationDialogHelper.this.mContext);
            String str = PersonalizationDialogHelper.this.mSettings.getVoiceSearchTokenType();
            Account[] arrayOfAccount = localAccountManager.getAccounts();
            int i = arrayOfAccount.length;
            for (int j = 0; j < i; j++) {
                localAccountManager.getAuthToken(arrayOfAccount[j], str, null, this.mActivity, null, null);
            }
            PersonalizationDialogHelper.this.setEnabled(true, this.mSource);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.personalization.PersonalizationDialogHelper

 * JD-Core Version:    0.7.0.1

 */