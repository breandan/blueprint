package com.google.android.shared.util;

import com.google.common.base.Preconditions;

import java.util.Iterator;

public class SplitIterator
        implements Iterator<String> {
    static final int SPLIT_ON_WHITESPACE = -1;
    private final int mDelimiter;
    private final boolean mOmitEmpty;
    private int mSearchStart;
    private final CharSequence mSequence;
    private int mTokenEnd;
    private int mTokenStart;
    private final boolean mTrim;

    SplitIterator(CharSequence paramCharSequence, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
        this.mSequence = paramCharSequence;
        this.mDelimiter = paramInt;
        this.mTrim = paramBoolean1;
        this.mOmitEmpty = paramBoolean2;
        findNextToken();
    }

    private void findNextToken() {
        if (this.mTokenStart != -1) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            if (this.mDelimiter != -1) {
                break;
            }
            findNextTokenWithWhitespaceDelimiter();
            return;
        }
        findNextTokenWithCharDelimiter((char) this.mDelimiter);
    }

    private void findNextTokenWithCharDelimiter(char paramChar) {
        int i = this.mSequence.length();
        while (this.mSearchStart != -1) {
            int j = this.mSearchStart;
            for (int k = this.mSearchStart; (k != i) && (this.mSequence.charAt(k) != paramChar); k++) {
            }
            if (k == i) {
            }
            for (int m = -1; ; m = k + 1) {
                this.mSearchStart = m;
                if (!this.mTrim) {
                    break label132;
                }
                int n = Whitespace.indexOfNonMatchIn(this.mSequence, j, k);
                if (n == -1) {
                    break;
                }
                this.mTokenStart = n;
                this.mTokenEnd = (1 + Whitespace.lastIndexOfNonMatchIn(this.mSequence, n, k));
                return;
            }
            j = k;
            label132:
            if ((j != k) || (!this.mOmitEmpty)) {
                this.mTokenStart = j;
                this.mTokenEnd = k;
                return;
            }
        }
        this.mTokenStart = -1;
    }

    private void findNextTokenWithWhitespaceDelimiter() {
        if (this.mSearchStart == -1) {
            this.mTokenStart = -1;
        }
        int i;
        do {
            return;
            i = this.mSequence.length();
            if (!this.mOmitEmpty) {
                break;
            }
            this.mTokenStart = Whitespace.indexOfNonMatchIn(this.mSequence, this.mSearchStart, i);
        } while (this.mTokenStart == -1);
        this.mTokenEnd = Whitespace.indexIn(this.mSequence, 1 + this.mTokenStart, i);
        if (this.mTokenEnd != -1) {
            this.mSearchStart = (1 + this.mTokenEnd);
            return;
        }
        this.mTokenEnd = i;
        this.mSearchStart = -1;
        return;
        this.mTokenStart = this.mSearchStart;
        this.mTokenEnd = Whitespace.indexIn(this.mSequence, this.mSearchStart, i);
        if (this.mTokenEnd != -1) {
            this.mSearchStart = (1 + this.mTokenEnd);
            return;
        }
        this.mTokenEnd = i;
        this.mSearchStart = -1;
    }

    public static SplitIterator splitOnCharOmitEmptyStrings(CharSequence paramCharSequence, char paramChar) {
        return new SplitIterator(paramCharSequence, paramChar, false, true);
    }

    public static SplitIterator splitOnCharTrimOmitEmptyStrings(CharSequence paramCharSequence, char paramChar) {
        return new SplitIterator(paramCharSequence, paramChar, true, true);
    }

    public static SplitIterator splitOnWhitespaceOmitEmptyStrings(CharSequence paramCharSequence) {
        return new SplitIterator(paramCharSequence, -1, false, true);
    }

    public boolean hasNext() {
        return this.mTokenStart != -1;
    }

    public String next() {
        String str = this.mSequence.subSequence(this.mTokenStart, this.mTokenEnd).toString();
        findNextToken();
        return str;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.SplitIterator

 * JD-Core Version:    0.7.0.1

 */