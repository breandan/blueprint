package com.embryo.android.voicesearch.ime;

import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import com.embryo.android.speech.utils.SpokenLanguageUtils;
import com.embryo.android.voicesearch.settings.Settings;
import com.embryo.wireless.voicesearch.proto.GstaticConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executor;

public class VoiceImeSubtypeUpdater
        implements Settings.ConfigurationChangeListener {
    private final Context mContext;
    private final Executor mExecutor;
    private final String mPackageName;
    private boolean mScheduledUpdate;

    public VoiceImeSubtypeUpdater(Context paramContext, Executor paramExecutor) {
        this.mPackageName = paramContext.getPackageName();
        this.mContext = paramContext;
        this.mExecutor = paramExecutor;
    }

    private InputMethodSubtype createInputMethodSubtype(GstaticConfiguration.Dialect paramDialect, boolean paramBoolean) {
        StringBuilder localStringBuilder = new StringBuilder(120);
        localStringBuilder.append("UntranslatableReplacementStringInSubtypeName=");
        localStringBuilder.append(paramDialect.getDisplayName());
        if (!paramBoolean) {
            localStringBuilder.append(",requireNetworkConnectivity");
        }
        localStringBuilder.append(",bcp47=").append(paramDialect.getBcp47Locale());
        return new InputMethodSubtype(2131363599, 0, paramDialect.getMainJavaLocale(), "voice", localStringBuilder.toString(), true, false);
    }

    private void maybeScheduleUpdate(final GstaticConfiguration.Configuration paramConfiguration, boolean paramBoolean) {
        try {
            if ((this.mScheduledUpdate) && (!paramBoolean)) {
                return;
            }
            this.mScheduledUpdate = true;
            Runnable local1 = new Runnable() {
                public void run() {
                    VoiceImeSubtypeUpdater.this.updateVoiceImeSubtypes(paramConfiguration);
                }
            };
            this.mExecutor.execute(local1);
            return;
        } finally {
        }
    }

    private void updateVoiceImeSubtypes(GstaticConfiguration.Configuration paramConfiguration) {
        LinkedList localLinkedList;
        InputMethodManager localInputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
        InputMethodInfo localInputMethodInfo;
        do {
            try {
                ArrayList localArrayList1 = SpokenLanguageUtils.getVoiceImeDialects(paramConfiguration);
                localLinkedList = new LinkedList();
                ArrayList localArrayList2 = SpokenLanguageUtils.getEmbeddedBcp47(paramConfiguration);
                Iterator localIterator1 = localArrayList1.iterator();
                while (localIterator1.hasNext()) {
                    GstaticConfiguration.Dialect localDialect = (GstaticConfiguration.Dialect) localIterator1.next();
                    localLinkedList.add(createInputMethodSubtype(localDialect, localArrayList2.contains(localDialect.getBcp47Locale())));
                }
            } catch (Throwable localThrowable) {
                Log.e("VoiceImeSubtypeUpdater", "Error updating IME subtype: ", localThrowable);
                return;
            }
            Iterator localIterator2 = localInputMethodManager.getInputMethodList().iterator();

            if (!localIterator2.hasNext())
                return;

            localInputMethodInfo = (InputMethodInfo) localIterator2.next();
        } while (!localInputMethodInfo.getPackageName().equals(this.mPackageName));
        localInputMethodManager.setAdditionalInputMethodSubtypes(localInputMethodInfo.getId(), (InputMethodSubtype[]) localLinkedList.toArray(new InputMethodSubtype[localLinkedList.size()]));
    }

    public void maybeScheduleUpdate(GstaticConfiguration.Configuration paramConfiguration) {
        maybeScheduleUpdate(paramConfiguration, false);
    }

    public void onChange(GstaticConfiguration.Configuration paramConfiguration) {
        maybeScheduleUpdate(paramConfiguration, true);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     VoiceImeSubtypeUpdater

 * JD-Core Version:    0.7.0.1

 */