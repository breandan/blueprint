package com.embryo.android.speech.audio;

import com.embryo.common.base.Preconditions;
import com.embryo.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AudioRecorder {
    private AudioStore mAudioStore = null;
    private int mBytesPerMsec;
    private int mEndPos;
    private int mMaxFlattenedBufferSize;
    private ClampedLengthRecordingThread mRecordingThread;
    private String mRequestId;
    private int mSampleRate;
    private int mStartPos;

    private void doStopRecording(boolean paramBoolean) {
        if (mRecordingThread == null) {
            return;
        }
        if (paramBoolean) {
            mRecordingThread.requestStop();
        }

        try {
            mRecordingThread.join();
        } catch (InterruptedException ie) {
            mAudioStore = null;
            mRequestId = null;
            return;
        }

        byte[] lastAudio = null;
        if (mRecordingThread.isGood()) {
            lastAudio = mRecordingThread.getBuffer();
            mEndPos = Math.min(mRecordingThread.getTotalLength(), mEndPos);
        } else if (mRecordingThread.isOverflown() && mEndPos <= mMaxFlattenedBufferSize) {
            lastAudio = mRecordingThread.getBuffer();
        }

        if (mStartPos < mEndPos) {
            mAudioStore.put(mRequestId, new AudioStore.AudioRecording(mSampleRate, getLastAudio(lastAudio)));
            mAudioStore = null;
            mRequestId = null;
            mRecordingThread = null;
        } else {
            lastAudio = null;
        }
    }

    private byte[] getLastAudio(byte[] paramArrayOfByte) {
        if ((paramArrayOfByte == null) || ((mStartPos == 0) && (mEndPos == paramArrayOfByte.length))) {
            return paramArrayOfByte;
        }
        return Arrays.copyOfRange(paramArrayOfByte, 2 * (mStartPos / 2), mEndPos);
    }

    public boolean isRecording() {
        return mRecordingThread != null;
    }

    public void setRecordingStartTime(long paramLong) {
        boolean bool1 = true;
        boolean bool2 = false;

        if (mRecordingThread != null) {
            Preconditions.checkState(bool1);
        } else {
            Preconditions.checkState(bool2);
        }

        if (paramLong < 0L) {
            bool1 = false;
        }

        Preconditions.checkArgument(bool1);
        mStartPos = ((int) (paramLong * mBytesPerMsec / 1000L));
    }

    public void startRecording(InputStream paramInputStream, int paramInt1, int paramInt2, AudioStore paramAudioStore, String paramString) {
        boolean bool = true;
        if (mRecordingThread != null) {
            bool = false;
        }

        Preconditions.checkState(bool);
        mAudioStore = paramAudioStore;
        mSampleRate = paramInt1;
        mBytesPerMsec = com.embryo.android.speech.audio.MicrophoneInputStreamFactory.getBytesPerMsec(paramInt1);
        mRequestId = paramString;
        mStartPos = 0;
        mEndPos = 2147483647;
        int i = 10000 * mBytesPerMsec;
        mMaxFlattenedBufferSize = (60000 * mBytesPerMsec);
        mRecordingThread = new ClampedLengthRecordingThread(i, mMaxFlattenedBufferSize, paramInputStream, paramInt2);
        mRecordingThread.start();
    }

    public void waitForRecording() {
        doStopRecording(false);
    }

    private static class ClampedLengthRecordingThread
            extends Thread {
        private final InputStream mInput;
        private final int mMaxSize;
        private final int mReadSize;
        private byte[] mBuf;
        private int mState;
        private int mTotalLength;

        ClampedLengthRecordingThread(int paramInt1, int paramInt2, InputStream paramInputStream, int paramInt3) {
            mMaxSize = paramInt2;
            mReadSize = paramInt3;
            mInput = paramInputStream;
            mBuf = new byte[paramInt1];
            mState = 1;
        }

        public byte[] getBuffer() {
            return mBuf;
        }

        public int getTotalLength() {
            return mTotalLength;
        }

        public boolean isGood() {
            return mState == 3;
        }

        public boolean isOverflown() {
            return mState == -2;
        }

        public void requestStop() {
            if (mState == 1) {
                mState = 2;
                interrupt();
            }
        }

        public void run() {
            int totalLength = 0;
            int lastBytesRead = 0;
            while (true)
                try {
                    if (lastBytesRead == -1) {
                        mTotalLength = totalLength;
                        mState = 3;
                        Closeables.closeQuietly(mInput);
                        return;
                    }
                    if (mState != 2) {
                        if (totalLength + lastBytesRead > mMaxSize) {
                            mTotalLength = mMaxSize;
                            mState = -2;
                            Closeables.closeQuietly(mInput);
                            return;
                        }
                        totalLength += lastBytesRead;
                    }
                    if (totalLength < mMaxSize) {
                        int expectedTotalLength = Math.min(totalLength + mReadSize, mMaxSize);
                        if (expectedTotalLength > mBuf.length) {
                            byte[] arrayOfByte = new byte[Math.min(expectedTotalLength * 2, mMaxSize)];
                            System.arraycopy(mBuf, 0, arrayOfByte, 0, totalLength);
                            mBuf = arrayOfByte;
                        }
                        lastBytesRead = mInput.read(mBuf, totalLength, expectedTotalLength - totalLength);
                    } else {
                        lastBytesRead = mInput.read(new byte[1]);
                    }
                } catch (IOException ioe) {
                    mTotalLength = totalLength;
                    int state = mState;
                    if (state != 2) {
                        state = -1;
                    }
                    mState = state;
                } finally {
                    Closeables.closeQuietly(mInput);
                }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     AudioRecorder

 * JD-Core Version:    0.7.0.1

 */