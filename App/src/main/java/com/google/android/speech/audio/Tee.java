package com.google.android.speech.audio;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Tee {
    private final byte[] mBuffer;
    private final InputStream mDelegate;
    private final int mKeepSize;
    private final InputStream mLeader;
    private final int[] mReadPositions;
    private final int mReadSize;
    private int mBasePos;
    private int mBufferBegin;
    private int mBufferEnd;
    private boolean mEof;
    private IOException mException;
    private int mStartMark;

    public Tee(InputStream delegate, int readSizeBytes, int minBuffers, int maxBuffers, int maxSiblings) {
        Preconditions.checkArgument((minBuffers < maxBuffers));
        mDelegate = delegate;
        mBuffer = new byte[(maxBuffers * readSizeBytes)];
        mKeepSize = (minBuffers * readSizeBytes);
        mBufferBegin = 0x0;
        mBufferEnd = 0x0;
        mEof = false;
        mReadSize = readSizeBytes;
        mReadPositions = new int[maxSiblings];
        Arrays.fill(mReadPositions, 0x7fffffff);
        mLeader = new Tee.TeeLeaderInputStream(this);
        mReadPositions[0x0] = 0x0;
    }

    private void doRead(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        byte[] arrayOfByte = this.mBuffer;
        int i = arrayOfByte.length;
        if (paramInt1 + paramInt3 <= i) {
            System.arraycopy(arrayOfByte, paramInt1, paramArrayOfByte, paramInt2, paramInt3);
            return;
        }
        if (paramInt1 >= arrayOfByte.length) {
            System.arraycopy(arrayOfByte, paramInt1 - i, paramArrayOfByte, paramInt2, paramInt3);
            return;
        }
        int j = i - paramInt1;
        int k = paramInt3 - j;
        System.arraycopy(arrayOfByte, paramInt1, paramArrayOfByte, paramInt2, j);
        System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt2 + j, k);
    }

    private int findSlowestReaderLocked() {
        int minimum = 0x7fffffff;
        for(int i = 0x0; i < mReadPositions.length; i = i + 0x1) {
            int position = mReadPositions[i];
            if(position < minimum) {
                minimum = position;
            }
        }
        Preconditions.checkState((minimum <= mBufferEnd));
        return minimum;
    }

    private int readFromDelegate(int readPos) throws IOException {
        int bufLength = mBuffer.length;
        int fillPos = readPos < bufLength ? readPos : readPos - bufLength;
        Preconditions.checkArgument(((bufLength - fillPos) >= mReadSize));
        try {
            return ByteStreams.read(mDelegate, mBuffer, fillPos, mReadSize);
        } catch (IOException ioe) {
            synchronized (this) {
                mException = ioe;
                notifyAll();
            }
            throw ioe;
        }
    }

    private void rewindBuffersLocked() throws IOException {
        Preconditions.checkArgument((mReadPositions[0x0] >= mKeepSize));
        int preservePos = mReadPositions[0x0] - mKeepSize;
        int readPosition = Math.min(findSlowestReaderLocked(), preservePos);
        int bufLength = mBuffer.length;
        if (((mBufferEnd + mReadSize) - readPosition) <= bufLength) {
            if (mStartMark < readPosition) {
                mStartMark = 0x7fffffff;
            }
            if (readPosition >= bufLength) {
                mBasePos = (mBasePos + bufLength);
                if (mStartMark != 0x7fffffff) {
                    mStartMark = (mStartMark - bufLength);
                }
                for (int i = 0x0; i != mReadPositions.length; i = i + 0x1) {
                    if (mReadPositions[i] != 0x7fffffff) {
                        mReadPositions[i] = (mReadPositions[i] - bufLength);
                    }
                }
                readPosition -= bufLength;
                mBufferEnd = (mBufferEnd - bufLength);
            }
            mBufferBegin = readPosition;
        }
        mException = new IOException("Buffer overflow, no available space.");
        throw mException;
    }

    void close() {
        try {
            mDelegate.close();
        } catch (IOException ignored) {
            Log.e("Tee", "IOException closing audio track: " + ignored);
        }
        synchronized (this) {
            mEof = true;
            notifyAll();
        }
    }

    public InputStream getLeader() {
        return this.mLeader;
    }

    int readLeader(byte[] bytes, int offset, int count) throws IOException {
        int bufLength = mBuffer.length;
        int totalCount = 0x0;
        int lastCount = 0x0;
        int NO_DELEGATE_READ = -0x1;
        int lastDelegateRead = -0x1;
        synchronized (this) {
            if (mException != null) {
                throw mException;
            }
            int readPos = mReadPositions[0x0];
            if (readPos == 0x7fffffff) {
                if (lastDelegateRead != -0x1) {
                    return (totalCount - lastCount);
                }
                return totalCount;
            }
            int bufferEnd = mBufferEnd;
            if (lastDelegateRead != -0x1) {
                bufferEnd += lastDelegateRead;
                mBufferEnd = bufferEnd;
                notifyAll();
                if (lastDelegateRead < mReadSize) {
                    mEof = true;
                    return totalCount;
                }
                lastDelegateRead = -0x1;
            }
            if (lastCount != 0) {
                readPos += lastCount;
                mReadPositions[0x0] = readPos;
                lastCount = 0x0;
            }
            if (totalCount == count) {
                return count;
            }
            if (bufferEnd == readPos) {
                if (mEof) {
                    return totalCount;
                }
                if (((mReadSize + bufferEnd) - mBufferBegin) > bufLength) {
                    rewindBuffersLocked();
                    readPos = mReadPositions[0x0];
                    bufferEnd = readPos;
                }
            }
            if (bufferEnd == readPos) {
                lastDelegateRead = readFromDelegate(bufferEnd);
                bufferEnd += lastDelegateRead;
            }
            int avail = bufferEnd - readPos;
            int need = count - totalCount;
            lastCount = avail < need ? avail : need;
            doRead(readPos, bytes, (offset + totalCount), lastCount);
            totalCount += lastCount;
        }

        return totalCount;
    }

    int readSecondary(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) throws IOException {
        int i = 0;
        int j = 0;
        try {
            if (this.mException != null) {
                throw this.mException;
            }
        } finally {
        }
        int k = this.mReadPositions[paramInt1];
        if (k == 2147483647) {
            return 0;
        }
        if (j != 0) {
            k += j;
            this.mReadPositions[paramInt1] = k;
            j = 0;
        }
        if (i == paramInt3) {
            return paramInt3;
        }
        int m = this.mBufferEnd;
        int n;
        int i1;
        if (m != k) {
            n = m - k;
            i1 = paramInt3 - i;
            if (n >= i1) {
                break label198;
            }
        }
        label198:
        for (j = n; ; j = i1) {
            for (; ; ) {
                doRead(k, paramArrayOfByte, paramInt2 + i, j);
                i += j;
                break;
                if (this.mEof) {
                    return i;
                }
                try {
                    wait();
                } catch (InterruptedException localInterruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted waiting for buffers: " + paramInt1);
                }
            }
        }
    }

    public synchronized void remove(int paramInt) {
        this.mReadPositions[paramInt] = 2147483647;
    }

    public synchronized void setStartAtDelegatePos(long delegatePos) {
        if ((long) (mBasePos + mBufferBegin) > delegatePos) {
            mStartMark = 0x7fffffff;
            return;
        }
        if ((long) (mBasePos + mBufferEnd) < delegatePos) {
            mStartMark = 0x7fffffff;
            return;
        }
        mStartMark = (int) (delegatePos - (long) mBasePos);
    }

    public synchronized InputStream split() throws IOException {
        if (mStartMark == 0x7fffffff) {
            throw new IOException("No splits possible, buffers rewound.");
        }

        for (int i = 1; i != mReadPositions.length && mReadPositions[i] != 0x7fffffff; i++) {
            if (i == mReadPositions.length) {
                throw new IOException("No splits possible, too many siblings.");
            } else {
                mReadPositions[i] = mStartMark;
                Tee.TeeSecondaryInputStream tis = new Tee.TeeSecondaryInputStream(this, i);
                return tis;
            }
        }

        return null;
    }

    private static class TeeLeaderInputStream
            extends InputStream {
        private final Tee mSharedStream;

        TeeLeaderInputStream(Tee paramTee) {
            this.mSharedStream = paramTee;
        }

        public void close() {
            this.mSharedStream.remove(0);
            this.mSharedStream.close();
        }

        public int read() {
            throw new UnsupportedOperationException("Find some other app to be inefficient in.");
        }

        public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
                throws IOException {
            int i = this.mSharedStream.readLeader(paramArrayOfByte, paramInt1, paramInt2);
            if (i == 0) {
                i = -1;
            }
            return i;
        }
    }

    private static class TeeSecondaryInputStream
            extends InputStream {
        private final int mStreamId;
        private Tee mSharedStream;

        TeeSecondaryInputStream(Tee paramTee, int paramInt) {
            this.mSharedStream = paramTee;
            this.mStreamId = paramInt;
        }

        public synchronized void close() {
            if (this.mSharedStream != null) {
                this.mSharedStream.remove(this.mStreamId);
                this.mSharedStream = null;
            }
        }

        public int read() {
            throw new UnsupportedOperationException("Find some other app to be inefficient in.");
        }

        public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
                throws IOException {
            try {
                if (this.mSharedStream == null) {
                    throw new IOException("Secondary Tee stream closed.");
                }
            } finally {
            }
            int i = this.mSharedStream.readSecondary(this.mStreamId, paramArrayOfByte, paramInt1, paramInt2);
            int j = i;
            if (j == 0) {
                j = -1;
            }
            return j;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.audio.Tee

 * JD-Core Version:    0.7.0.1

 */
