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

    private void findNextTokenWithCharDelimiter(char delimiter) {
        int len = mSequence.length();
        if(mSearchStart != -0x1) {
            int start = mSearchStart;
            int end = mSearchStart;
            while(end != len && mSequence.charAt(end) != delimiter) {
                end = end + 0x1;
            }
            mSearchStart = end == len ? -0x1 : (end + 0x1);
            if(mTrim) {
                start = Whitespace.indexOfNonMatchIn(mSequence, start, end);
                if(start != -0x1) {
                    mTokenStart = start;
                    mTokenEnd = (Whitespace.lastIndexOfNonMatchIn(mSequence, start, end) + 0x1);
                    return;
                }
                start = end;
            }
            if((start != end) || (!mOmitEmpty)) {
                mTokenStart = start;
                mTokenEnd = end;
            }
            return;
        }
        mTokenStart = -0x1;
    }

    private void findNextTokenWithWhitespaceDelimiter() {
        if(mSearchStart == -0x1) {
            mTokenStart = -0x1;
            return;
        }
        int len = mSequence.length();
        if(mOmitEmpty) {
            mTokenStart = Whitespace.indexOfNonMatchIn(mSequence, mSearchStart, len);
            if(mTokenStart != -0x1) {
                mTokenEnd = Whitespace.indexIn(mSequence, (mTokenStart + 0x1), len);
                if(mTokenEnd != -0x1) {
                    mSearchStart = (mTokenEnd + 0x1);
                    return;
                }
                mTokenEnd = len;
                mSearchStart = -0x1;
            }
            return;
        }
        mTokenStart = mSearchStart;
        mTokenEnd = Whitespace.indexIn(mSequence, mSearchStart, len);
        if(mTokenEnd != -0x1) {
            mSearchStart = (mTokenEnd + 0x1);
            return;
        }
        mTokenEnd = len;
        mSearchStart = -0x1;
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