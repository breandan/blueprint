package com.google.android.voicesearch.ime;

import android.util.Log;

public class EditDistanceCalculator {
    private static boolean DEBUG = false;
    private short mCorLength;
    private short mMaxNewChars;
    private final byte mMaxNewCharsPerc;
    private final byte mMaxNewContiguousChars;
    private short mRecLength;

    public EditDistanceCalculator(byte paramByte1, byte paramByte2) {
        this.mMaxNewContiguousChars = paramByte1;
        this.mMaxNewCharsPerc = paramByte2;
    }

    public short getDistance(CharSequence paramCharSequence1, CharSequence paramCharSequence2) {
        this.mCorLength = ((short) paramCharSequence2.length());
        this.mRecLength = ((short) paramCharSequence1.length());
        this.mMaxNewChars = ((short) (this.mMaxNewCharsPerc * this.mCorLength / 100));
        DeltaTable localDeltaTable = new DeltaTable(1 + this.mRecLength, 1 + this.mCorLength, null);
        if (DEBUG) {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("     ");
            for (int i = 0; i < this.mRecLength; i++) {
                localStringBuilder.append(paramCharSequence1.charAt(i));
                localStringBuilder.append("   ");
            }
            Log.d("EditDistance", "  " + localStringBuilder.toString());
            Log.d("EditDistance", "  " + localDeltaTable.toCurrRowDebug());
        }
        for (int j = 1; j < 1 + this.mCorLength; j++) {
            boolean bool = true;
            int k = 1;
            if (k < 1 + this.mRecLength) {
                if (k - 1 > j + (this.mCorLength - this.mMaxNewChars)) {
                    localDeltaTable.mCurrRow[k].mDeadEnd = true;
                }
                for (; ; ) {
                    if (!localDeltaTable.mCurrRow[k].mDeadEnd) {
                        localDeltaTable.mCurrRow[k].updateDeadEnd(j, k);
                    }
                    bool &= localDeltaTable.mCurrRow[k].mDeadEnd;
                    k++;
                    break;
                    if (paramCharSequence1.charAt(k - 1) == paramCharSequence2.charAt(j - 1)) {
                        localDeltaTable.mCurrRow[k].copy(localDeltaTable.mPrevRow[(k - 1)]);
                        localDeltaTable.mCurrRow[k].mContiguousChars = 0;
                    } else {
                        Delta localDelta1 = localDeltaTable.mCurrRow[(k - 1)];
                        localDeltaTable.mCurrRow[k].copy(localDelta1);
                        Delta localDelta2 = localDeltaTable.mPrevRow[k];
                        localDeltaTable.mCurrRow[k].updateIfBetter(localDelta2);
                        Delta localDelta3 = localDeltaTable.mPrevRow[(k - 1)];
                        localDeltaTable.mCurrRow[k].updateIfBetter(localDelta3);
                    }
                }
            }
            if (DEBUG) {
                Log.d("EditDistance", paramCharSequence2.charAt(j - 1) + " " + localDeltaTable.toCurrRowDebug());
            }
            if (bool) {
                return this.mMaxNewChars;
            }
            localDeltaTable.nextRow();
        }
        if (localDeltaTable.mPrevRow[this.mRecLength].mDeadEnd) {
            return this.mMaxNewChars;
        }
        return localDeltaTable.mPrevRow[this.mRecLength].mTotalChars;
    }

    private class Delta {
        byte mContiguousChars;
        boolean mDeadEnd;
        short mTotalChars;

        private Delta() {
        }

        private boolean canAddContiguousChar() {
            return (this.mTotalChars < -1 + EditDistanceCalculator.this.mMaxNewChars) && (this.mContiguousChars < -1 + EditDistanceCalculator.this.mMaxNewContiguousChars);
        }

        private void copy(Delta paramDelta) {
            this.mContiguousChars = paramDelta.mContiguousChars;
            this.mTotalChars = paramDelta.mTotalChars;
            this.mDeadEnd = paramDelta.mDeadEnd;
        }

        private void updateDeadEnd(int paramInt1, int paramInt2) {
            if (paramInt2 - 1 > paramInt1 + (EditDistanceCalculator.this.mCorLength - EditDistanceCalculator.this.mMaxNewChars) + this.mTotalChars) {
                this.mDeadEnd = true;
            }
        }

        private void updateIfBetter(Delta paramDelta) {
            if (paramDelta.mDeadEnd) {
            }
            while ((!paramDelta.canAddContiguousChar()) || ((!this.mDeadEnd) && (this.mTotalChars <= 1 + paramDelta.mTotalChars))) {
                return;
            }
            this.mTotalChars = ((short) (1 + paramDelta.mTotalChars));
            this.mContiguousChars = ((byte) (1 + paramDelta.mContiguousChars));
            this.mDeadEnd = false;
        }
    }

    private class DeltaTable {
        private EditDistanceCalculator.Delta[] mCurrRow;
        private EditDistanceCalculator.Delta[] mPrevRow;

        private DeltaTable(int paramInt1, int paramInt2) {
            this.mPrevRow = new EditDistanceCalculator.Delta[paramInt1];
            this.mCurrRow = new EditDistanceCalculator.Delta[paramInt1];
            int i = 0;
            if (i < paramInt1) {
                if (i < paramInt2 - EditDistanceCalculator.this.mMaxNewChars) {
                    this.mPrevRow[i] = new EditDistanceCalculator.Delta(EditDistanceCalculator.this, null);
                    this.mPrevRow[i].mContiguousChars = 0;
                    this.mPrevRow[i].mTotalChars = 0;
                }
                for (; ; ) {
                    this.mCurrRow[i] = new EditDistanceCalculator.Delta(EditDistanceCalculator.this, null);
                    i = (short) (i + 1);
                    break;
                    this.mPrevRow[i] = new EditDistanceCalculator.Delta(EditDistanceCalculator.this, null);
                    this.mPrevRow[i].mDeadEnd = true;
                }
            }
        }

        private void nextRow() {
            EditDistanceCalculator.Delta[] arrayOfDelta = this.mCurrRow;
            this.mCurrRow = this.mPrevRow;
            this.mPrevRow = arrayOfDelta;
            if ((this.mPrevRow[0].mDeadEnd) || (!this.mPrevRow[0].canAddContiguousChar())) {
                this.mCurrRow[0].mDeadEnd = true;
                return;
            }
            this.mCurrRow[0] = this.mPrevRow[0];
            EditDistanceCalculator.Delta localDelta = this.mCurrRow[0];
            localDelta.mTotalChars = ((short) (1 + localDelta.mTotalChars));
        }

        private String toCurrRowDebug() {
            StringBuilder localStringBuilder = new StringBuilder();
            int i = 0;
            if (i < this.mCurrRow.length) {
                if (this.mCurrRow[i].mDeadEnd) {
                    localStringBuilder.append(" D");
                }
                for (; ; ) {
                    localStringBuilder.append("  ");
                    i = (short) (i + 1);
                    break;
                    String str = Integer.toString(this.mCurrRow[i].mTotalChars);
                    if (str.length() == 1) {
                        localStringBuilder.append(" ");
                    }
                    localStringBuilder.append(str);
                }
            }
            return localStringBuilder.toString();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ime.EditDistanceCalculator

 * JD-Core Version:    0.7.0.1

 */