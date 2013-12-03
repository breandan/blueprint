package com.google.android.voicesearch.ime;

import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodSubtype;

import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.android.voicesearch.settings.Settings;
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
        new InputmethodSubtypeAdapter() {
            public String getCurrentSubtypeLocale() {
                return this.val$imm.getCurrentInputMethodSubtype().getLocale();
            }

            public String[] getEnabledBcp47Locales() {
                List localList = this.val$imm.getEnabledInputMethodSubtypeList(null, false);
                ArrayList localArrayList = new ArrayList();
                int i = 0;
                if (i < localList.size()) {
                    String str = ((InputMethodSubtype) localList.get(i)).getExtraValueOf("bcp47");
                    if (str != null) {
                        localArrayList.add(str);
                    }
                    for (; ; ) {
                        i++;
                        break;
                        BugLogger.record(6833013);
                    }
                }
                return (String[]) localArrayList.toArray(new String[localArrayList.size()]);
            }

            public String getLastInputMethodSubtypeLocale() {
                InputMethodSubtype localInputMethodSubtype = this.val$imm.getLastInputMethodSubtype();
                if (localInputMethodSubtype != null) {
                    return localInputMethodSubtype.getLocale();
                }
                return null;
            }
        };
    }

    public String getDictationBcp47Locale() {
        String str1;
        if (this.mInputmethodSubtypeAdapter.getEnabledBcp47Locales().length == 0) {
            String str3 = this.mInputmethodSubtypeAdapter.getLastInputMethodSubtypeLocale();
            GstaticConfiguration.Configuration localConfiguration = this.mSettings.getConfiguration();
            String[] arrayOfString = new String[2];
            arrayOfString[0] = str3;
            arrayOfString[1] = ((String) this.mPhoneLocaleSupplier.get());
            str1 = SpokenLanguageUtils.getSpokenBcp47Locale(localConfiguration, arrayOfString);
            if (str1 == null) {
                str1 = this.mSettings.getSpokenLocaleBcp47();
            }
        }
        do {
            return str1;
            str1 = SpokenLanguageUtils.getSpokenBcp47Locale(this.mSettings.getConfiguration(), this.mInputmethodSubtypeAdapter.getCurrentSubtypeLocale());
        } while (str1 != null);
        Log.e("VoiceLanguageSelector", "The subtype of the IME are not aligned with the supported locale");
        String str2 = this.mSettings.getSpokenLocaleBcp47();
        BugLogger.record(6271090);
        return str2;
    }

    public GstaticConfiguration.Dialect[] getEnabledDialects(String paramString) {
        Preconditions.checkNotNull(paramString);
        String[] arrayOfString = this.mInputmethodSubtypeAdapter.getEnabledBcp47Locales();
        GstaticConfiguration.Dialect[] arrayOfDialect;
        if (arrayOfString.length == 0) {
            GstaticConfiguration.Dialect localDialect = SpokenLanguageUtils.getVoiceImeMainLanguage(this.mSettings.getConfiguration(), paramString);
            arrayOfDialect = new GstaticConfiguration.Dialect[1];
            arrayOfDialect[0] = localDialect;
        }
        for (; ; ) {
            return arrayOfDialect;
            arrayOfDialect = new GstaticConfiguration.Dialect[arrayOfString.length];
            for (int i = 0; i < arrayOfString.length; i++) {
                arrayOfDialect[i] = SpokenLanguageUtils.getSpokenLanguageByBcp47Locale(this.mSettings.getConfiguration(), arrayOfString[i]);
            }
        }
    }

    public static abstract interface InputmethodSubtypeAdapter {
        public abstract String getCurrentSubtypeLocale();

        public abstract String[] getEnabledBcp47Locales();

        public abstract String getLastInputMethodSubtypeLocale();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ime.VoiceLanguageSelector

 * JD-Core Version:    0.7.0.1

 */