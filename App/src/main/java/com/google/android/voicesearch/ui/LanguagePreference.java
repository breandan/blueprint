package com.google.android.voicesearch.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;

public class LanguagePreference
        extends ListPreference {
    private String[] mDialects;
    private final Settings mSettings = VelvetServices.get().getVoiceSearchServices().getSettings();

    public LanguagePreference(Context paramContext) {
        super(paramContext);
    }

    public LanguagePreference(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private String[] getLanguages() {
        return SpokenLanguageUtils.getLanguageDisplayNames(this.mSettings.getConfiguration(), "…");
    }

    private boolean processDialectClicked(String paramString) {
        GstaticConfiguration.Dialect localDialect = SpokenLanguageUtils.getDialectByDisplayName(this.mSettings.getConfiguration(), paramString);
        if (localDialect != null) {
            EventLogger.recordClientEvent(66);
            setValue(localDialect.getBcp47Locale());
            return true;
        }
        return false;
    }

    private void processLanguageClicked(String paramString) {
        Preconditions.checkNotNull(paramString);
        this.mDialects = SpokenLanguageUtils.getDialectDisplayName(this.mSettings.getConfiguration(), paramString);
        if ((this.mDialects == null) && (processDialectClicked(paramString))) {
            return;
        }
        showDialog(null);
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder paramBuilder) {
        super.onPrepareDialogBuilder(paramBuilder);
        if (this.mDialects != null) {
            final String[] arrayOfString2 = this.mDialects;
            paramBuilder.setTitle(2131363469);
            paramBuilder.setSingleChoiceItems(new ArrayAdapter(getContext(), 2130968790, arrayOfString2), -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    String str = arrayOfString2[paramAnonymousInt];
                    LanguagePreference.this.processDialectClicked(str);
                    paramAnonymousDialogInterface.dismiss();
                }
            });
            this.mDialects = null;
        }
        for (; ; ) {
            paramBuilder.setPositiveButton(null, null);
            return;
            final String[] arrayOfString1 = getLanguages();
            paramBuilder.setSingleChoiceItems(new ArrayAdapter(getContext(), 2130968790, arrayOfString1), -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    String str = arrayOfString1[paramAnonymousInt];
                    if (str.endsWith("…")) {
                        str = str.substring(0, str.length() - "…".length());
                    }
                    LanguagePreference.this.processLanguageClicked(str);
                    paramAnonymousDialogInterface.dismiss();
                }
            });
        }
    }

    public void setValue(String paramString) {
        if (getOnPreferenceChangeListener() != null) {
            getOnPreferenceChangeListener().onPreferenceChange(this, paramString);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ui.LanguagePreference

 * JD-Core Version:    0.7.0.1

 */