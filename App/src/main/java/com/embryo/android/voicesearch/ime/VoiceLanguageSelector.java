package com.embryo.android.voicesearch.ime;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import com.embryo.android.voicesearch.logger.BugLogger;
import com.embryo.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VoiceLanguageSelector {
    private final InputmethodSubtypeAdapter mInputmethodSubtypeAdapter;
    private final Supplier<String> mPhoneLocaleSupplier;
    private final Settings mSettings;

    public VoiceLanguageSelector(Context paramContext, Settings paramSettings) {
        this(paramSettings, createInputmethodSubtypeAdapter(paramContext), new Supplier() {
            public String get() {
                return Locale.getDefault().toString();
            }
        });
    }

    public VoiceLanguageSelector(Settings paramSettings, InputmethodSubtypeAdapter paramInputmethodSubtypeAdapter, Supplier<String> paramSupplier) {
        this.mSettings = paramSettings;
        this.mInputmethodSubtypeAdapter = paramInputmethodSubtypeAdapter;
        this.mPhoneLocaleSupplier = paramSupplier;
    }

    private static InputmethodSubtypeAdapter createInputmethodSubtypeAdapter(Context paramContext) {
        final InputMethodManager imm = (InputMethodManager)paramContext.getSystemService("input_method");
        return new InputmethodSubtypeAdapter() {
            public String getCurrentSubtypeLocale() {
                return imm.getCurrentInputMethodSubtype().getLocale();
            }

            public String[] getEnabledBcp47Locales() {
                List<InputMethodSubtype> subtypes = imm.getEnabledInputMethodSubtypeList(null, false);
                ArrayList<String> supportedLocales = new ArrayList<String>();
                for(int i = 0; i < subtypes.size(); i = i + 1) {
                    String bcp47Locale = subtypes.get(i).getExtraValueOf("bcp47");
                    if(bcp47Locale != null) {
                        supportedLocales.add(bcp47Locale);
                        continue;
                    }
                    BugLogger.record(0x684375);
                }
                return supportedLocales.toArray(new String[supportedLocales.size()]);
            }

            public String getLastInputMethodSubtypeLocale() {
                InputMethodSubtype localInputMethodSubtype = imm.getLastInputMethodSubtype();
                if (localInputMethodSubtype != null) {
                    return localInputMethodSubtype.getLocale();
                }
                return null;
            }
        };
    }

    public com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Dialect[] getEnabledDialects(String paramString) {
        Preconditions.checkNotNull(paramString);
        String[] arrayOfString = this.mInputmethodSubtypeAdapter.getEnabledBcp47Locales();
        com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Dialect[] arrayOfDialect;
        if (arrayOfString.length == 0) {
            com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Dialect localDialect = com.embryo.android.speech.utils.SpokenLanguageUtils.getVoiceImeMainLanguage(this.mSettings.getConfiguration(), paramString);
            arrayOfDialect = new com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Dialect[1];
            arrayOfDialect[0] = localDialect;
        } else {
            arrayOfDialect = new com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Dialect[arrayOfString.length];
            for (int i = 0; i < arrayOfString.length; i++) {
                arrayOfDialect[i] = com.embryo.android.speech.utils.SpokenLanguageUtils.getSpokenLanguageByBcp47Locale(this.mSettings.getConfiguration(), arrayOfString[i]);
            }
        }
        return arrayOfDialect;
    }

    public static abstract interface InputmethodSubtypeAdapter {
        public abstract String getCurrentSubtypeLocale();

        public abstract String[] getEnabledBcp47Locales();

        public abstract String getLastInputMethodSubtypeLocale();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     VoiceLanguageSelector

 * JD-Core Version:    0.7.0.1

 */