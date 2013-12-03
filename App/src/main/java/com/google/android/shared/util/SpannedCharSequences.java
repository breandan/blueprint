package com.google.android.shared.util;

import android.os.Parcel;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.SuggestionSpan;

import com.google.android.search.shared.api.CorrectionSpan;
import com.google.android.search.shared.api.VoiceCorrectionSpan;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nullable;

public final class SpannedCharSequences {
    public static boolean charSequencesEqual(@Nullable CharSequence paramCharSequence1, @Nullable CharSequence paramCharSequence2) {
        boolean bool = true;
        if (!TextUtils.equals(paramCharSequence1, paramCharSequence2)) {
            bool = false;
        }
        do {
            do {
                return bool;
                if (!(paramCharSequence1 instanceof Spanned)) {
                    break;
                }
                if ((paramCharSequence2 instanceof Spanned)) {
                    return spansEqual((Spanned) paramCharSequence1, (Spanned) paramCharSequence2);
                }
            } while (!hasSpans((Spanned) paramCharSequence1));
            return false;
        }
        while ((!(paramCharSequence2 instanceof Spanned)) || (!hasSpans((Spanned) paramCharSequence2)));
        return false;
    }

    public static void copySpansFrom(Spanned paramSpanned, int paramInt1, int paramInt2, Class paramClass, Spannable paramSpannable, int paramInt3) {
        int i = paramSpannable.length() - paramInt3;
        if (i < paramInt2 - paramInt1) {
            paramInt2 = paramInt1 + i;
        }
        Object[] arrayOfObject = paramSpanned.getSpans(paramInt1, paramInt2, paramClass);
        int j = 0;
        if (j < arrayOfObject.length) {
            int k = paramSpanned.getSpanStart(arrayOfObject[j]);
            int m = paramSpanned.getSpanEnd(arrayOfObject[j]);
            if ((k < paramInt1) || (m > paramInt2)) {
            }
            for (; ; ) {
                j++;
                break;
                paramSpannable.setSpan(arrayOfObject[j], paramInt3 + k - paramInt1, paramInt3 + m - paramInt1, paramSpanned.getSpanFlags(arrayOfObject[j]));
            }
        }
    }

    private static boolean hasSpans(Spanned paramSpanned) {
        int i = paramSpanned.getSpans(0, paramSpanned.length(), Object.class).length;
        boolean bool = false;
        if (i != 0) {
            bool = true;
        }
        return bool;
    }

    @Nullable
    public static CharSequence immutableValueOf(@Nullable CharSequence paramCharSequence) {
        if ((paramCharSequence == null) || ((paramCharSequence instanceof String)) || ((paramCharSequence instanceof SpannedString))) {
            return paramCharSequence;
        }
        return SpannedString.valueOf(paramCharSequence);
    }

    public static CharSequence readFromParcel(Parcel paramParcel) {
        CharSequence localCharSequence = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
        int i = paramParcel.readInt();
        int j = paramParcel.readInt();
        if ((i == 0) && (j == 0)) {
            return immutableValueOf(localCharSequence);
        }
        SpannableString localSpannableString = SpannableString.valueOf(localCharSequence);
        for (int k = 0; k < i; k++) {
            int i3 = paramParcel.readInt();
            int i4 = paramParcel.readInt();
            int i5 = paramParcel.readInt();
            localSpannableString.setSpan((CorrectionSpan) CorrectionSpan.CREATOR.createFromParcel(paramParcel), i3, i4, i5);
        }
        for (int m = 0; m < j; m++) {
            int n = paramParcel.readInt();
            int i1 = paramParcel.readInt();
            int i2 = paramParcel.readInt();
            localSpannableString.setSpan((VoiceCorrectionSpan) VoiceCorrectionSpan.CREATOR.createFromParcel(paramParcel), n, i1, i2);
        }
        return SpannedString.valueOf(localSpannableString);
    }

    @Nullable
    public static ImmutableList<CharSequence> readListFromParcel(Parcel paramParcel) {
        int i = paramParcel.readInt();
        if (i == -1) {
            return null;
        }
        ImmutableList.Builder localBuilder = ImmutableList.builder();
        for (int j = 0; j < i; j++) {
            CharSequence localCharSequence = readFromParcel(paramParcel);
            if (localCharSequence != null) {
                localBuilder.add(localCharSequence);
            }
        }
        return localBuilder.build();
    }

    public static void removeSystemSpans(Spannable paramSpannable) {
        for (Object localObject : paramSpannable.getSpans(0, paramSpannable.length(), Object.class)) {
            if ((!(localObject instanceof SuggestionSpan)) && (!(localObject instanceof CorrectionSpan)) && (!(localObject instanceof VoiceCorrectionSpan))) {
                paramSpannable.removeSpan(localObject);
            }
        }
    }

    private static boolean spansEqual(Spanned paramSpanned1, Spanned paramSpanned2) {
        Object[] arrayOfObject1 = paramSpanned1.getSpans(0, paramSpanned1.length(), Object.class);
        Object[] arrayOfObject2 = paramSpanned2.getSpans(0, paramSpanned2.length(), Object.class);
        if (arrayOfObject1.length != arrayOfObject2.length) {
            return false;
        }
        for (int i = 0; ; i++) {
            if (i >= arrayOfObject1.length) {
                break label117;
            }
            Object localObject1 = arrayOfObject1[i];
            Object localObject2 = arrayOfObject2[i];
            if ((!Objects.equal(localObject1, localObject2)) || (paramSpanned1.getSpanStart(localObject1) != paramSpanned2.getSpanStart(localObject2)) || (paramSpanned1.getSpanEnd(localObject1) != paramSpanned2.getSpanEnd(localObject2))) {
                break;
            }
        }
        label117:
        return true;
    }

    public static void writeAllToParcel(@Nullable Collection<? extends CharSequence> paramCollection, Parcel paramParcel, int paramInt) {
        if (paramCollection == null) {
            paramParcel.writeInt(-1);
        }
        for (; ; ) {
            return;
            paramParcel.writeInt(paramCollection.size());
            Iterator localIterator = paramCollection.iterator();
            while (localIterator.hasNext()) {
                writeToParcel((CharSequence) localIterator.next(), paramParcel, paramInt);
            }
        }
    }

    private static void writeExtraSpansToParcel(Spanned paramSpanned, Parcel paramParcel, int paramInt) {
        CorrectionSpan[] arrayOfCorrectionSpan = (CorrectionSpan[]) paramSpanned.getSpans(0, paramSpanned.length(), CorrectionSpan.class);
        VoiceCorrectionSpan[] arrayOfVoiceCorrectionSpan = (VoiceCorrectionSpan[]) paramSpanned.getSpans(0, paramSpanned.length(), VoiceCorrectionSpan.class);
        paramParcel.writeInt(arrayOfCorrectionSpan.length);
        paramParcel.writeInt(arrayOfVoiceCorrectionSpan.length);
        int i = arrayOfCorrectionSpan.length;
        for (int j = 0; j < i; j++) {
            CorrectionSpan localCorrectionSpan = arrayOfCorrectionSpan[j];
            paramParcel.writeInt(paramSpanned.getSpanStart(localCorrectionSpan));
            paramParcel.writeInt(paramSpanned.getSpanEnd(localCorrectionSpan));
            paramParcel.writeInt(paramSpanned.getSpanFlags(localCorrectionSpan));
            localCorrectionSpan.writeToParcel(paramParcel, paramInt);
        }
        int k = arrayOfVoiceCorrectionSpan.length;
        for (int m = 0; m < k; m++) {
            VoiceCorrectionSpan localVoiceCorrectionSpan = arrayOfVoiceCorrectionSpan[m];
            paramParcel.writeInt(paramSpanned.getSpanStart(localVoiceCorrectionSpan));
            paramParcel.writeInt(paramSpanned.getSpanEnd(localVoiceCorrectionSpan));
            paramParcel.writeInt(paramSpanned.getSpanFlags(localVoiceCorrectionSpan));
            localVoiceCorrectionSpan.writeToParcel(paramParcel, paramInt);
        }
    }

    public static void writeToParcel(CharSequence paramCharSequence, Parcel paramParcel, int paramInt) {
        TextUtils.writeToParcel(paramCharSequence, paramParcel, paramInt);
        if ((paramCharSequence instanceof Spanned)) {
            writeExtraSpansToParcel((Spanned) paramCharSequence, paramParcel, paramInt);
            return;
        }
        paramParcel.writeInt(0);
        paramParcel.writeInt(0);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.SpannedCharSequences

 * JD-Core Version:    0.7.0.1

 */