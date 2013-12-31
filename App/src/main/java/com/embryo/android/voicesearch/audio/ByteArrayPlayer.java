package com.embryo.android.voicesearch.audio;

import android.media.MediaPlayer;
import android.os.ConditionVariable;
import android.util.Base64;

import com.google.common.base.Preconditions;

import java.util.concurrent.Executor;

import javax.annotation.Nonnull;

public class ByteArrayPlayer {
    @Nonnull
    private final byte[] mAudioBytes;
    private final AudioRouter mAudioRouter;
    private volatile Callback mCallback;
    private final ConditionVariable mDone;
    private final Executor mExecutor;
    private MediaPlayer mPlayer;
    private final Thread mRunnerThread;
    private final ConditionVariable mStartPlayback;
    private volatile boolean mStopped;

    public ByteArrayPlayer(@Nonnull AudioRouter paramAudioRouter, @Nonnull byte[] paramArrayOfByte, @Nonnull Executor paramExecutor) {
        this.mAudioBytes = ((byte[]) Preconditions.checkNotNull(paramArrayOfByte));
        this.mExecutor = ((Executor) Preconditions.checkNotNull(paramExecutor));
        this.mAudioRouter = paramAudioRouter;
        this.mStartPlayback = new ConditionVariable();
        this.mDone = new ConditionVariable();
        this.mRunnerThread = new Thread(new Runnable() {
            public void run() {
                ByteArrayPlayer.this.doRun();
            }
        });
        this.mRunnerThread.start();
    }

    private String buildDataUri() {
        return "data:;base64," + Base64.encodeToString(this.mAudioBytes, 0);
    }

    /* Error */
    private void doRun() {
        // Byte code:
        //   0: aload_0
        //   1: new 100	android/media/MediaPlayer
        //   4: dup
        //   5: invokespecial 101	android/media/MediaPlayer:<init>	()V
        //   8: putfield 103	com/google/android/voicesearch/audio/ByteArrayPlayer:mPlayer	Landroid/media/MediaPlayer;
        //   11: aload_0
        //   12: getfield 103	com/google/android/voicesearch/audio/ByteArrayPlayer:mPlayer	Landroid/media/MediaPlayer;
        //   15: aload_0
        //   16: getfield 42	com/google/android/voicesearch/audio/ByteArrayPlayer:mAudioRouter	Lcom/google/android/voicesearch/audio/AudioRouter;
        //   19: invokeinterface 109 1 0
        //   24: invokevirtual 113	android/media/MediaPlayer:setAudioStreamType	(I)V
        //   27: aload_0
        //   28: getfield 103	com/google/android/voicesearch/audio/ByteArrayPlayer:mPlayer	Landroid/media/MediaPlayer;
        //   31: aload_0
        //   32: invokespecial 115	com/google/android/voicesearch/audio/ByteArrayPlayer:buildDataUri	()Ljava/lang/String;
        //   35: invokevirtual 119	android/media/MediaPlayer:setDataSource	(Ljava/lang/String;)V
        //   38: aload_0
        //   39: getfield 103	com/google/android/voicesearch/audio/ByteArrayPlayer:mPlayer	Landroid/media/MediaPlayer;
        //   42: invokevirtual 122	android/media/MediaPlayer:prepare	()V
        //   45: aload_0
        //   46: getfield 42	com/google/android/voicesearch/audio/ByteArrayPlayer:mAudioRouter	Lcom/google/android/voicesearch/audio/AudioRouter;
        //   49: invokeinterface 125 1 0
        //   54: aload_0
        //   55: getfield 103	com/google/android/voicesearch/audio/ByteArrayPlayer:mPlayer	Landroid/media/MediaPlayer;
        //   58: new 127	com/google/android/voicesearch/audio/ByteArrayPlayer$2
        //   61: dup
        //   62: aload_0
        //   63: invokespecial 128	com/google/android/voicesearch/audio/ByteArrayPlayer$2:<init>	(Lcom/google/android/voicesearch/audio/ByteArrayPlayer;)V
        //   66: invokevirtual 132	android/media/MediaPlayer:setOnErrorListener	(Landroid/media/MediaPlayer$OnErrorListener;)V
        //   69: aload_0
        //   70: getfield 103	com/google/android/voicesearch/audio/ByteArrayPlayer:mPlayer	Landroid/media/MediaPlayer;
        //   73: new 134	com/google/android/voicesearch/audio/ByteArrayPlayer$3
        //   76: dup
        //   77: aload_0
        //   78: invokespecial 135	com/google/android/voicesearch/audio/ByteArrayPlayer$3:<init>	(Lcom/google/android/voicesearch/audio/ByteArrayPlayer;)V
        //   81: invokevirtual 139	android/media/MediaPlayer:setOnCompletionListener	(Landroid/media/MediaPlayer$OnCompletionListener;)V
        //   84: aload_0
        //   85: getfield 47	com/google/android/voicesearch/audio/ByteArrayPlayer:mStartPlayback	Landroid/os/ConditionVariable;
        //   88: invokevirtual 142	android/os/ConditionVariable:block	()V
        //   91: aload_0
        //   92: getfield 144	com/google/android/voicesearch/audio/ByteArrayPlayer:mStopped	Z
        //   95: istore 5
        //   97: iload 5
        //   99: ifeq +56 -> 155
        //   102: aload_0
        //   103: invokespecial 147	com/google/android/voicesearch/audio/ByteArrayPlayer:finish	()V
        //   106: aload_0
        //   107: getfield 40	com/google/android/voicesearch/audio/ByteArrayPlayer:mExecutor	Ljava/util/concurrent/Executor;
        //   110: new 149	com/google/android/voicesearch/audio/ByteArrayPlayer$4
        //   113: dup
        //   114: aload_0
        //   115: invokespecial 150	com/google/android/voicesearch/audio/ByteArrayPlayer$4:<init>	(Lcom/google/android/voicesearch/audio/ByteArrayPlayer;)V
        //   118: invokeinterface 153 2 0
        //   123: return
        //   124: astore_1
        //   125: ldc 155
        //   127: new 78	java/lang/StringBuilder
        //   130: dup
        //   131: invokespecial 79	java/lang/StringBuilder:<init>	()V
        //   134: ldc 157
        //   136: invokevirtual 85	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   139: aload_1
        //   140: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   143: invokevirtual 94	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   146: invokestatic 166	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
        //   149: pop
        //   150: aload_0
        //   151: invokespecial 147	com/google/android/voicesearch/audio/ByteArrayPlayer:finish	()V
        //   154: return
        //   155: aload_0
        //   156: getfield 103	com/google/android/voicesearch/audio/ByteArrayPlayer:mPlayer	Landroid/media/MediaPlayer;
        //   159: invokevirtual 167	android/media/MediaPlayer:start	()V
        //   162: aload_0
        //   163: getfield 49	com/google/android/voicesearch/audio/ByteArrayPlayer:mDone	Landroid/os/ConditionVariable;
        //   166: invokevirtual 142	android/os/ConditionVariable:block	()V
        //   169: aload_0
        //   170: invokespecial 147	com/google/android/voicesearch/audio/ByteArrayPlayer:finish	()V
        //   173: aload_0
        //   174: getfield 40	com/google/android/voicesearch/audio/ByteArrayPlayer:mExecutor	Ljava/util/concurrent/Executor;
        //   177: new 149	com/google/android/voicesearch/audio/ByteArrayPlayer$4
        //   180: dup
        //   181: aload_0
        //   182: invokespecial 150	com/google/android/voicesearch/audio/ByteArrayPlayer$4:<init>	(Lcom/google/android/voicesearch/audio/ByteArrayPlayer;)V
        //   185: invokeinterface 153 2 0
        //   190: return
        //   191: astore 4
        //   193: aload_0
        //   194: invokespecial 147	com/google/android/voicesearch/audio/ByteArrayPlayer:finish	()V
        //   197: aload_0
        //   198: getfield 40	com/google/android/voicesearch/audio/ByteArrayPlayer:mExecutor	Ljava/util/concurrent/Executor;
        //   201: new 149	com/google/android/voicesearch/audio/ByteArrayPlayer$4
        //   204: dup
        //   205: aload_0
        //   206: invokespecial 150	com/google/android/voicesearch/audio/ByteArrayPlayer$4:<init>	(Lcom/google/android/voicesearch/audio/ByteArrayPlayer;)V
        //   209: invokeinterface 153 2 0
        //   214: return
        //   215: astore_3
        //   216: aload_0
        //   217: invokespecial 147	com/google/android/voicesearch/audio/ByteArrayPlayer:finish	()V
        //   220: aload_0
        //   221: getfield 40	com/google/android/voicesearch/audio/ByteArrayPlayer:mExecutor	Ljava/util/concurrent/Executor;
        //   224: new 149	com/google/android/voicesearch/audio/ByteArrayPlayer$4
        //   227: dup
        //   228: aload_0
        //   229: invokespecial 150	com/google/android/voicesearch/audio/ByteArrayPlayer$4:<init>	(Lcom/google/android/voicesearch/audio/ByteArrayPlayer;)V
        //   232: invokeinterface 153 2 0
        //   237: aload_3
        //   238: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	239	0	this	ByteArrayPlayer
        //   124	16	1	localIOException	java.io.IOException
        //   215	23	3	localObject	Object
        //   191	1	4	localIllegalArgumentException	java.lang.IllegalArgumentException
        //   95	3	5	bool	boolean
        // Exception table:
        //   from	to	target	type
        //   27	45	124	java/io/IOException
        //   45	97	191	java/lang/IllegalArgumentException
        //   155	169	191	java/lang/IllegalArgumentException
        //   45	97	215	finally
        //   155	169	215	finally
    }

    private void finish() {
        try {
            this.mPlayer.stop();
            label7:
            this.mPlayer.release();
            this.mAudioRouter.onStopTtsPlayback();
            return;
        } catch (IllegalStateException localIllegalStateException) {
            break label7;
        }
    }

    public void start(Callback paramCallback) {
        this.mCallback = ((Callback) Preconditions.checkNotNull(paramCallback));
        this.mStartPlayback.open();
    }

    public void stop() {
        this.mStopped = true;
        this.mDone.open();
        this.mStartPlayback.open();
    }

    public static abstract interface Callback {
        public abstract void onComplete();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ByteArrayPlayer

 * JD-Core Version:    0.7.0.1

 */