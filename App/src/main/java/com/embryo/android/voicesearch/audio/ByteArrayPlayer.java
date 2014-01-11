package com.embryo.android.voicesearch.audio;

import android.media.MediaPlayer;
import android.os.ConditionVariable;
import android.util.Base64;
import android.util.Log;

import com.embryo.common.base.Preconditions;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.annotation.Nonnull;

public class ByteArrayPlayer {
    @Nonnull
    private final byte[] mAudioBytes;
    private final AudioRouter mAudioRouter;
    private final ConditionVariable mDone;
    private final Executor mExecutor;
    private final Thread mRunnerThread;
    private final ConditionVariable mStartPlayback;
    private volatile Callback mCallback;
    private MediaPlayer mPlayer;
    private volatile boolean mStopped;

    public ByteArrayPlayer(@Nonnull AudioRouter paramAudioRouter, @Nonnull byte[] paramArrayOfByte, @Nonnull Executor paramExecutor) {
        this.mAudioBytes = Preconditions.checkNotNull(paramArrayOfByte);
        this.mExecutor = Preconditions.checkNotNull(paramExecutor);
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

    private void doRun() {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(mAudioRouter.getOutputStream());
        try {
            mPlayer.setDataSource(buildDataUri());
            mPlayer.prepare();
        } catch (IOException e) {
            Log.w("VS.ByteArrayPlayer", "I/O Exception initializing media player :" + e);
            finish();
            return;
        }
        try {
            mAudioRouter.onStartTtsPlayback();
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mDone.open();
                    return true;
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    mDone.open();
                }
            });
            mStartPlayback.block();
            if (mStopped) {
            }
            mPlayer.start();
            mDone.block();
        } catch (IllegalArgumentException localIllegalArgumentException1) {
        } finally {
            finish();
            mExecutor.execute(new Runnable() {

                public void run() {
                    ByteArrayPlayer.Callback callback = mCallback;
                    if (callback != null) {
                        callback.onComplete();
                    }
                }
            });
        }
    }

    private void finish() {
        try {
            mPlayer.stop();
        } catch (IllegalStateException localIllegalStateException1) {
        }
        mPlayer.release();
        mAudioRouter.onStopTtsPlayback();
    }

    public void start(Callback paramCallback) {
        this.mCallback = Preconditions.checkNotNull(paramCallback);
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