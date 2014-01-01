package com.embryo.android.speech.audio;

import android.media.AudioRecord;
import android.util.Log;

import com.embryo.android.voicesearch.audio.AudioRouter;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MicrophoneInputStream
        extends InputStream {
    @Nullable
    protected final SpeakNowSoundPlayer mBeepPlayer;
    protected final Object mLock = new Object();
    protected final boolean mNoiseSuppressionEnabled;
    protected final com.embryo.android.speech.logger.SpeechLibLogger mSpeechLibLogger;
    @Nullable
    private final AudioRouter mAudioRouter;
    private final int mBufferSize;
    private final boolean mPreemptible;
    private final int mSampleRate;
    @Nullable
    protected AudioRecord mAudioRecord;
    private boolean mAudioRecordCreated = false;
    private boolean mClosed;
    private boolean mStarted = false;

    public MicrophoneInputStream(int paramInt1, int paramInt2, boolean paramBoolean1, @Nullable SpeakNowSoundPlayer paramSpeakNowSoundPlayer, @Nullable AudioRouter paramAudioRouter, com.embryo.android.speech.logger.SpeechLibLogger paramSpeechLibLogger, boolean paramBoolean2) {
        this.mSampleRate = paramInt1;
        this.mBufferSize = Math.max(AudioRecord.getMinBufferSize(paramInt1, 16, 2), paramInt2);
        this.mNoiseSuppressionEnabled = paramBoolean1;
        this.mBeepPlayer = paramSpeakNowSoundPlayer;
        this.mAudioRouter = paramAudioRouter;
        this.mSpeechLibLogger = paramSpeechLibLogger;
        this.mPreemptible = paramBoolean2;
    }

    @Nonnull
    private AudioRecord ensureStartedLocked()
            throws IOException {
        com.embryo.android.shared.util.ExtraPreconditions.checkHoldsLock(this.mLock);
        if ((this.mAudioRecordCreated) && (this.mAudioRecord == null)) {
            throw new IOException("AudioRecord failed to initialize.");
        }
        if (this.mStarted) {
            return this.mAudioRecord;
        }
        this.mSpeechLibLogger.recordOpenMicrophoneLatencyStart();
        if (this.mAudioRouter != null) {
            this.mAudioRouter.awaitRouting();
        }
        this.mSpeechLibLogger.recordBreakdownEvent(49);
        if (!this.mAudioRecordCreated) {
            this.mAudioRecord = createAudioRecord();
            this.mAudioRecordCreated = true;
        }
        this.mSpeechLibLogger.recordBreakdownEvent(50);
        if (this.mAudioRecord == null) {
            throw new IOException("AudioRecord failed to initialize.");
        }
        createNoiseSuppressor();
        startRecording();
        int i = this.mAudioRecord.getRecordingState();
        if (i != 3) {
            throw new IOException("couldn't start recording, state is:" + i);
        }
        this.mStarted = true;
        this.mSpeechLibLogger.recordBreakdownEvent(54);
        return this.mAudioRecord;
    }

    public void close() {
        if (this.mAudioRouter != null) {
            this.mAudioRouter.cancelPendingAwaitRouting();
        }
        synchronized (this.mLock) {
            if ((this.mAudioRecord != null) && (!this.mClosed)) {
                Log.i("MicrophoneInputStream", "mic_close");
                this.mAudioRecord.stop();
                releaseNoiseSuppressor();
                this.mAudioRecord.release();
                this.mClosed = true;
            }
            return;
        }
    }

    @Nullable
    protected AudioRecord createAudioRecord() {
        int i = 6;
        if (this.mPreemptible) {
            i = 1999;
        }
        AudioRecord localAudioRecord = new AudioRecord(i, this.mSampleRate, 16, 2, this.mBufferSize);
        if (localAudioRecord.getState() != 1) {
            localAudioRecord.release();
            localAudioRecord = null;
        }
        return localAudioRecord;
    }

    protected void createNoiseSuppressor() {
    }

    public int read() {
        throw new UnsupportedOperationException("Single-byte read not supported");
    }

    public int read(byte[] paramArrayOfByte)
            throws IOException {
        return read(paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    public int read(byte[] b, int offset, int length) throws IOException {
        synchronized (mLock) {
            if (mClosed) {
                return -0x1;
            }
            AudioRecord record = ensureStartedLocked();
            int rtn = record.read(b, offset, length);
            synchronized (mLock) {
                if (mClosed) {
                    return -0x1;
                }
            }
            if (rtn < -0x1) {
                if (rtn == -0x3) {
                    throw new IOException("not open");
                }
                if (rtn == -0x2) {
                    throw new IOException("Bad offset/length arguments for buffer");
                }
                throw new IOException("Unexpected error code: " + rtn);
            }
            return rtn;
        }
    }

    protected void releaseNoiseSuppressor() {
    }

    protected void startRecording() {
        this.mAudioRecord.startRecording();
        this.mSpeechLibLogger.recordBreakdownEvent(53);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     MicrophoneInputStream

 * JD-Core Version:    0.7.0.1

 */