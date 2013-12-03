package com.google.android.speech.audio;

import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

public class AudioSource
        implements AudioInputStreamFactory {
    private CaptureThread mCaptureThread;
    private AudioInputStreamFactory mInputStreamFactory;
    private final int mMaxBuffers;
    private final int mMinBuffers;
    private final int mReadSize;
    private final int mSampleRate;
    @Nullable
    private final SpeechLevelGenerator mSpeechLevelGenerator;
    private Tee mTee;

    public AudioSource(int paramInt1, int paramInt2, int paramInt3, int paramInt4, AudioInputStreamFactory paramAudioInputStreamFactory, @Nullable SpeechLevelSource paramSpeechLevelSource) {
        Preconditions.checkNotNull(paramAudioInputStreamFactory);
        this.mSampleRate = paramInt1;
        this.mReadSize = paramInt2;
        this.mMinBuffers = paramInt3;
        this.mMaxBuffers = paramInt4;
        this.mInputStreamFactory = paramAudioInputStreamFactory;
        if (paramSpeechLevelSource == null) {
        }
        for (SpeechLevelGenerator localSpeechLevelGenerator = null; ; localSpeechLevelGenerator = new SpeechLevelGenerator(paramSpeechLevelSource)) {
            this.mSpeechLevelGenerator = localSpeechLevelGenerator;
            return;
        }
    }

    public AudioSource(AudioSource paramAudioSource) {
        try {
            this.mSampleRate = paramAudioSource.mSampleRate;
            this.mReadSize = paramAudioSource.mReadSize;
            this.mMinBuffers = paramAudioSource.mMinBuffers;
            this.mMaxBuffers = paramAudioSource.mMaxBuffers;
            this.mInputStreamFactory = paramAudioSource.mInputStreamFactory;
            this.mSpeechLevelGenerator = paramAudioSource.mSpeechLevelGenerator;
            this.mTee = paramAudioSource.mTee;
            this.mCaptureThread = paramAudioSource.mCaptureThread;
            paramAudioSource.mInputStreamFactory = null;
            paramAudioSource.mTee = null;
            paramAudioSource.mCaptureThread = null;
            return;
        } finally {
        }
    }

    public InputStream createInputStream()
            throws IOException {
        try {
            if (this.mInputStreamFactory == null) {
                throw new IOException("Stopped");
            }
        } finally {
        }
        if (this.mTee == null) {
            this.mTee = new Tee(this.mInputStreamFactory.createInputStream(), this.mReadSize, this.mMinBuffers, this.mMaxBuffers, 16);
            if (this.mCaptureThread != null) {
                this.mCaptureThread.setInputStream(this.mTee.getLeader());
                this.mCaptureThread.start();
            }
        }
        InputStream localInputStream = this.mTee.split();
        return localInputStream;
    }

    public void setStartTime(long paramLong) {
        Preconditions.checkNotNull(this.mTee);
        Preconditions.checkNotNull(this.mCaptureThread);
        Preconditions.checkNotNull(this.mInputStreamFactory);
        long l = paramLong * MicrophoneInputStreamFactory.getBytesPerMsec(this.mSampleRate) / 1000L;
        this.mTee.setStartAtDelegatePos(l);
    }

    public void shutdown() {
        try {
            stopListening();
            this.mInputStreamFactory = null;
            this.mTee = null;
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    /* Error */
    public void start(RecognitionEventListener paramRecognitionEventListener) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 53	com/google/android/speech/audio/AudioSource:mCaptureThread	Lcom/google/android/speech/audio/AudioSource$CaptureThread;
        //   6: ifnonnull +68 -> 74
        //   9: iconst_1
        //   10: istore_3
        //   11: iload_3
        //   12: invokestatic 106	com/google/common/base/Preconditions:checkState	(Z)V
        //   15: aload_0
        //   16: getfield 41	com/google/android/speech/audio/AudioSource:mInputStreamFactory	Lcom/google/android/speech/audio/AudioInputStreamFactory;
        //   19: invokestatic 31	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
        //   22: pop
        //   23: aload_0
        //   24: new 74	com/google/android/speech/audio/AudioSource$CaptureThread
        //   27: dup
        //   28: aload_0
        //   29: getfield 35	com/google/android/speech/audio/AudioSource:mReadSize	I
        //   32: aload_0
        //   33: getfield 43	com/google/android/speech/audio/AudioSource:mSpeechLevelGenerator	Lcom/google/android/speech/audio/SpeechLevelGenerator;
        //   36: aload_1
        //   37: invokespecial 109	com/google/android/speech/audio/AudioSource$CaptureThread:<init>	(ILcom/google/android/speech/audio/SpeechLevelGenerator;Lcom/google/android/speech/listeners/RecognitionEventListener;)V
        //   40: putfield 53	com/google/android/speech/audio/AudioSource:mCaptureThread	Lcom/google/android/speech/audio/AudioSource$CaptureThread;
        //   43: aload_0
        //   44: getfield 51	com/google/android/speech/audio/AudioSource:mTee	Lcom/google/android/speech/audio/Tee;
        //   47: ifnull +24 -> 71
        //   50: aload_0
        //   51: getfield 53	com/google/android/speech/audio/AudioSource:mCaptureThread	Lcom/google/android/speech/audio/AudioSource$CaptureThread;
        //   54: aload_0
        //   55: getfield 51	com/google/android/speech/audio/AudioSource:mTee	Lcom/google/android/speech/audio/Tee;
        //   58: invokevirtual 72	com/google/android/speech/audio/Tee:getLeader	()Ljava/io/InputStream;
        //   61: invokevirtual 78	com/google/android/speech/audio/AudioSource$CaptureThread:setInputStream	(Ljava/io/InputStream;)V
        //   64: aload_0
        //   65: getfield 53	com/google/android/speech/audio/AudioSource:mCaptureThread	Lcom/google/android/speech/audio/AudioSource$CaptureThread;
        //   68: invokevirtual 81	com/google/android/speech/audio/AudioSource$CaptureThread:start	()V
        //   71: aload_0
        //   72: monitorexit
        //   73: return
        //   74: iconst_0
        //   75: istore_3
        //   76: goto -65 -> 11
        //   79: astore_2
        //   80: aload_0
        //   81: monitorexit
        //   82: aload_2
        //   83: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	84	0	this	AudioSource
        //   0	84	1	paramRecognitionEventListener	RecognitionEventListener
        //   79	4	2	localObject	Object
        //   10	66	3	bool	boolean
        // Exception table:
        //   from	to	target	type
        //   2	9	79	finally
        //   11	71	79	finally
    }

    public void stopListening() {
        try {
            if (this.mCaptureThread != null) {
                this.mCaptureThread.closeLeader();
                if (this.mTee != null) {
                    this.mCaptureThread.interrupt();
                }
                this.mCaptureThread = null;
            }
            if (this.mTee == null) {
                this.mInputStreamFactory = null;
            }
            return;
        } finally {
        }
    }

    private static class CaptureThread
            extends Thread {
        private final RecognitionEventListener mEventListener;
        private InputStream mLeader;
        private final int mReadSize;
        @Nullable
        private final SpeechLevelGenerator mSpeechLevelGenerator;

        public CaptureThread(int paramInt, @Nullable SpeechLevelGenerator paramSpeechLevelGenerator, RecognitionEventListener paramRecognitionEventListener) {
            super();
            this.mReadSize = paramInt;
            this.mSpeechLevelGenerator = paramSpeechLevelGenerator;
            this.mEventListener = paramRecognitionEventListener;
        }

        public void closeLeader() {
            Closeables.closeQuietly(this.mLeader);
        }

        /* Error */
        public void run() {
            // Byte code:
            //   0: aload_0
            //   1: getfield 30	com/google/android/speech/audio/AudioSource$CaptureThread:mLeader	Ljava/io/InputStream;
            //   4: invokestatic 45	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
            //   7: pop
            //   8: aload_0
            //   9: getfield 22	com/google/android/speech/audio/AudioSource$CaptureThread:mReadSize	I
            //   12: newarray byte
            //   14: astore_2
            //   15: iconst_1
            //   16: istore_3
            //   17: aload_0
            //   18: getfield 30	com/google/android/speech/audio/AudioSource$CaptureThread:mLeader	Ljava/io/InputStream;
            //   21: aload_2
            //   22: invokevirtual 51	java/io/InputStream:read	([B)I
            //   25: istore 6
            //   27: iload 6
            //   29: iconst_m1
            //   30: if_icmpeq +58 -> 88
            //   33: invokestatic 55	java/lang/Thread:currentThread	()Ljava/lang/Thread;
            //   36: invokevirtual 59	java/lang/Thread:isInterrupted	()Z
            //   39: ifne +49 -> 88
            //   42: iload_3
            //   43: ifeq +14 -> 57
            //   46: aload_0
            //   47: getfield 26	com/google/android/speech/audio/AudioSource$CaptureThread:mEventListener	Lcom/google/android/speech/listeners/RecognitionEventListener;
            //   50: invokeinterface 64 1 0
            //   55: iconst_0
            //   56: istore_3
            //   57: aload_0
            //   58: getfield 24	com/google/android/speech/audio/AudioSource$CaptureThread:mSpeechLevelGenerator	Lcom/google/android/speech/audio/SpeechLevelGenerator;
            //   61: ifnull -44 -> 17
            //   64: aload_0
            //   65: getfield 24	com/google/android/speech/audio/AudioSource$CaptureThread:mSpeechLevelGenerator	Lcom/google/android/speech/audio/SpeechLevelGenerator;
            //   68: aload_2
            //   69: iconst_0
            //   70: iload 6
            //   72: invokevirtual 70	com/google/android/speech/audio/SpeechLevelGenerator:update	([BII)V
            //   75: goto -58 -> 17
            //   78: astore 5
            //   80: aload_0
            //   81: getfield 30	com/google/android/speech/audio/AudioSource$CaptureThread:mLeader	Ljava/io/InputStream;
            //   84: invokestatic 36	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
            //   87: return
            //   88: aload_0
            //   89: getfield 30	com/google/android/speech/audio/AudioSource$CaptureThread:mLeader	Ljava/io/InputStream;
            //   92: invokestatic 36	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
            //   95: return
            //   96: astore 4
            //   98: aload_0
            //   99: getfield 30	com/google/android/speech/audio/AudioSource$CaptureThread:mLeader	Ljava/io/InputStream;
            //   102: invokestatic 36	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
            //   105: aload 4
            //   107: athrow
            // Local variable table:
            //   start	length	slot	name	signature
            //   0	108	0	this	CaptureThread
            //   14	55	2	arrayOfByte	byte[]
            //   16	41	3	i	int
            //   96	10	4	localObject	Object
            //   78	1	5	localIOException	IOException
            //   25	46	6	j	int
            // Exception table:
            //   from	to	target	type
            //   17	27	78	java/io/IOException
            //   33	42	78	java/io/IOException
            //   46	55	78	java/io/IOException
            //   57	75	78	java/io/IOException
            //   17	27	96	finally
            //   33	42	96	finally
            //   46	55	96	finally
            //   57	75	96	finally
        }

        public void setInputStream(InputStream paramInputStream) {
            this.mLeader = paramInputStream;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.audio.AudioSource

 * JD-Core Version:    0.7.0.1

 */