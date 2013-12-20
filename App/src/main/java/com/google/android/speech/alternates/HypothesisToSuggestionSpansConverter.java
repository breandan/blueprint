package com.google.android.speech.alternates;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.style.EasyEditSpan;
import android.text.style.SuggestionSpan;

import com.google.android.speech.logger.SuggestionLogger;
import com.google.common.base.Preconditions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.annotation.Nullable;

public class HypothesisToSuggestionSpansConverter {
    private final Context mApplicationContext;
    @Nullable
    private final Constructor<EasyEditSpan> mEasyEditSpanConstructor;
    private int mSuggestionCounter = 0;
    private final SuggestionLogger mSuggestionLogger;

    public HypothesisToSuggestionSpansConverter(Context paramContext, SuggestionLogger paramSuggestionLogger) {
        this.mApplicationContext = Preconditions.checkNotNull(paramContext);
        this.mSuggestionLogger = Preconditions.checkNotNull(paramSuggestionLogger);
        this.mEasyEditSpanConstructor = getEasyEditSpanConstructor();
    }

    private EasyEditSpan createEasyEditSpan(Context paramContext, String paramString, int paramInt) {
        for (; ; ) {
            try {
                Constructor localConstructor = this.mEasyEditSpanConstructor;
                EasyEditSpan localEasyEditSpan1;
                if (localConstructor != null) {
                    localEasyEditSpan1 = null;
                }
                try {
                    Intent localIntent = SuggestionSpanBroadcastReceiver.createIntentForEasyEditSpan(this.mApplicationContext, paramString, paramInt);
                    int i = this.mSuggestionCounter;
                    this.mSuggestionCounter = (i + 1);
                    PendingIntent localPendingIntent = PendingIntent.getBroadcast(paramContext, i, localIntent, 0);
                    EasyEditSpan localEasyEditSpan3 = this.mEasyEditSpanConstructor.newInstance(new Object[]{localPendingIntent});
                    localEasyEditSpan1 = localEasyEditSpan3;
                } catch (IllegalArgumentException localIllegalArgumentException) {
                    try {
                        EasyEditSpan localEasyEditSpan2 = new EasyEditSpan();
                        return localEasyEditSpan2;
                    } finally {
                    }
                    continue;
                } catch (InstantiationException localInstantiationException) {
                    localEasyEditSpan1 = null;
                    continue;
                } catch (IllegalAccessException localIllegalAccessException) {
                    localEasyEditSpan1 = null;
                    continue;
                } catch (InvocationTargetException localInvocationTargetException) {
                    localEasyEditSpan1 = null;
                    continue;
                }
                if (localEasyEditSpan1 == null) {
                }
                EasyEditSpan localEasyEditSpan2 = localEasyEditSpan1;
            } finally {
            }
        }
    }

    private static Constructor<EasyEditSpan> getEasyEditSpanConstructor() {
        try {
            Constructor localConstructor = EasyEditSpan.class.getConstructor(new Class[]{PendingIntent.class});
            return localConstructor;
        } catch (NoSuchMethodException localNoSuchMethodException) {
        }
        return null;
    }

    private SpannableString getSuggestionSpan(String paramString, int paramInt, Hypothesis paramHypothesis) {
        SpannableString localSpannableString = new SpannableString(paramHypothesis.getText());
        if (paramString != null) {
            Iterator localIterator = paramHypothesis.getSpans().iterator();
            while (localIterator.hasNext()) {
                Hypothesis.Span localSpan = (Hypothesis.Span) localIterator.next();
                String[] arrayOfString = new String[localSpan.mAlternates.size()];
                localSpan.mAlternates.toArray(arrayOfString);
                SuggestionSpan localSuggestionSpan = new SuggestionSpan(this.mApplicationContext, null, arrayOfString, 1, SuggestionSpanBroadcastReceiver.class);
                localSpannableString.setSpan(localSuggestionSpan, localSpan.mUtf16Start, localSpan.mUtf16End, 33);
                this.mSuggestionLogger.addSuggestion(localSuggestionSpan.hashCode(), paramString, paramInt, localSpan.mUtf8Start, localSpan.mUtf8Length);
            }
        }
        return localSpannableString;
    }

    public SpannableString getSuggestionSpannableString(String paramString, int paramInt, Hypothesis paramHypothesis) {
        SpannableString localSpannableString = getSuggestionSpan(paramString, paramInt, paramHypothesis);
        localSpannableString.setSpan(createEasyEditSpan(this.mApplicationContext, paramString, paramInt), 0, localSpannableString.length(), 33);
        return localSpannableString;
    }

    public SpannedString getSuggestionSpannedStringForQuery(String paramString, Hypothesis paramHypothesis) {
        return SpannedString.valueOf(getSuggestionSpan(paramString, 0, paramHypothesis));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.alternates.HypothesisToSuggestionSpansConverter

 * JD-Core Version:    0.7.0.1

 */