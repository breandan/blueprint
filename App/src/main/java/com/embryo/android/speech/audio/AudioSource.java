package com.embryo.android.speech.audio;

import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

public class AudioSource implements AudioInputStreamFactory {
    private final int mMaxBuffers;
    private final int mMinBuffers;
    private final int mReadSize;
    private final int mSampleRate;
    @Nullable
    private final SpeechLevelGenerator mSpeechLevelGenerator;
    private CaptureThread mCaptureThread;
    private AudioInputStreamFactory mInputStreamFactory;
    private Tee mTee;

    public AudioSource(int sampleRate, int readSize, int minBuffers, int maxBuffers, AudioInputStreamFactory inputStreamFactory, com.embryo.android.shared.util.SpeechLevelSource speechLevelSource) {
        Preconditions.checkNotNull(inputStreamFactory);
        mSampleRate = sampleRate;
        mReadSize = readSize;
        mMinBuffers = minBuffers;
        mMaxBuffers = maxBuffers;
        mInputStreamFactory = inputStreamFactory;
        mSpeechLevelGenerator = new SpeechLevelGenerator(speechLevelSource);
    }

    public AudioSource(AudioSource oldAudioSource) {
        synchronized (oldAudioSource) {
            mSampleRate = oldAudioSource.mSampleRate;
            mReadSize = oldAudioSource.mReadSize;
            mMinBuffers = oldAudioSource.mMinBuffers;
            mMaxBuffers = oldAudioSource.mMaxBuffers;
            mInputStreamFactory = oldAudioSource.mInputStreamFactory;
            mSpeechLevelGenerator = oldAudioSource.mSpeechLevelGenerator;
            mTee = oldAudioSource.mTee;
            mCaptureThread = oldAudioSource.mCaptureThread;
            oldAudioSource.mInputStreamFactory = null;
            oldAudioSource.mTee = null;
            oldAudioSource.mCaptureThread = null;
        }
    }

    public synchronized InputStream createInputStream() throws IOException {
        if (mInputStreamFactory == null) {
            throw new IOException("Stopped");
        }
        if (mTee == null) {
            mTee = new Tee(mInputStreamFactory.createInputStream(), mReadSize, mMinBuffers, mMaxBuffers, 0x10);
            if (mCaptureThread != null) {
                mCaptureThread.setInputStream(mTee.getLeader());
                mCaptureThread.start();
            }
        }
        return mTee.split();
    }

    public void setStartTime(long paramLong) {
        Preconditions.checkNotNull(this.mTee);
        Preconditions.checkNotNull(this.mCaptureThread);
        Preconditions.checkNotNull(this.mInputStreamFactory);
        long l = paramLong * MicrophoneInputStreamFactory.getBytesPerMsec(this.mSampleRate) / 1000L;
        this.mTee.setStartAtDelegatePos(l);
    }

    public synchronized void shutdown() {
        stopListening();
        this.mInputStreamFactory = null;
        this.mTee = null;
    }

    public synchronized void start(com.embryo.android.speech.listeners.RecognitionEventListener listener) {
        Preconditions.checkState((mCaptureThread == null));
        Preconditions.checkNotNull(mInputStreamFactory);
        mCaptureThread = new AudioSource.CaptureThread(mReadSize, mSpeechLevelGenerator, listener);
        if (mTee != null) {
            mCaptureThread.setInputStream(mTee.getLeader());
            mCaptureThread.start();
        }
    }

    public synchronized void stopListening() {
        if (mCaptureThread != null) {
            mCaptureThread.closeLeader();
            if (mTee != null) {
                mCaptureThread.interrupt();
            }
            mCaptureThread = null;
        }
        if (mTee == null) {
            mInputStreamFactory = null;
        }
    }

    private static class CaptureThread
            extends Thread {
        private final com.embryo.android.speech.listeners.RecognitionEventListener mEventListener;
        private final int mReadSize;
        @Nullable
        private final SpeechLevelGenerator mSpeechLevelGenerator;
        private InputStream mLeader;

        public CaptureThread(int paramInt, @Nullable SpeechLevelGenerator paramSpeechLevelGenerator, com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener) {
            super();
            this.mReadSize = paramInt;
            this.mSpeechLevelGenerator = paramSpeechLevelGenerator;
            this.mEventListener = paramRecognitionEventListener;
        }

        public void closeLeader() {
            Closeables.closeQuietly(this.mLeader);
        }

        public void run() {
            Preconditions.checkNotNull(mLeader);
            byte[] buffer = new byte[mReadSize];
            int read; //v2
            int first = 1; //v1

            while (true) {
                try {
                    read = mLeader.read(buffer);
                    if (read != -1 && !Thread.currentThread().isInterrupted()) {
                        if (first != 0) {
                            mEventListener.onReadyForSpeech();
                            first = 0;
                        } else {
                            if (mSpeechLevelGenerator == null)
                                continue;
                            mSpeechLevelGenerator.update(buffer, 0, read);
                        }
                    } else {
                        Closeables.closeQuietly(mLeader);
                        return;
                    }
                } catch (IOException e) {
                    Closeables.closeQuietly(mLeader);
                    return;
                }
            }
        }

        public void setInputStream(InputStream paramInputStream) {
            this.mLeader = paramInputStream;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     AudioSource

 * JD-Core Version:    0.7.0.1

 */
