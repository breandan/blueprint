package com.embryo.android.voicesearch.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioTrack;
import android.util.Log;
import android.util.SparseArray;

import com.embryo.android.speech.audio.SpeakNowSoundPlayer;
import com.embryo.common.io.ByteStreams;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

public class AudioTrackSoundManager
        implements SpeakNowSoundPlayer {
    private static final byte[] INVALID_SOUND = new byte[0];
    private final SparseArray<byte[]> mAudioData = new SparseArray();
    private final AudioRouter mAudioRouter;
    private final Context mContext;
    private final ExecutorService mPlaybackExecutor;

    public AudioTrackSoundManager(Context paramContext, AudioRouter paramAudioRouter, ExecutorService paramExecutorService) {
        this.mContext = paramContext;
        this.mAudioRouter = paramAudioRouter;
        this.mPlaybackExecutor = paramExecutorService;
    }

    private static long clip(long paramLong1, long paramLong2, long paramLong3) {
        if (paramLong1 > paramLong3) {
            return paramLong3;
        }
        if (paramLong1 < paramLong2) {
            return paramLong2;
        }
        return paramLong1;
    }

    private static void closeSilently(Object paramObject) {
        try {
            if ((paramObject instanceof AssetFileDescriptor)) {
                ((AssetFileDescriptor) paramObject).close();
                return;
            }
            if ((paramObject instanceof Closeable)) {
                ((Closeable) paramObject).close();
                return;
            }
        } catch (IOException localIOException) {
        }
    }

    private byte[] loadSound(int id) {
        AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(id);
        long lengthAsLong = afd.getLength();
        if (lengthAsLong > 0x7fffffff) {
            closeSilently(afd);
            return INVALID_SOUND;
        }

        int length = (int) afd.getLength();
        byte[] data = new byte[length];
        InputStream is = null;
        try {
            is = afd.createInputStream();
            if (ByteStreams.read(is, data, 0x0, length) != length) {
                throw new IOException();
            }
        } catch (IOException e) {
            data = INVALID_SOUND;
        } finally {
            closeSilently(is);
            closeSilently(afd);
        }

        return data;
    }

    private int playSound(int paramInt) {
        byte[] arrayOfByte = this.mAudioData.get(paramInt, null);
        if (arrayOfByte == null) {
            arrayOfByte = loadSound(paramInt);
        }
        if (arrayOfByte == INVALID_SOUND) {
            return -1;
        }
        return playSound(arrayOfByte);
    }

    private void playSoundAsync(final int paramInt) {
        this.mPlaybackExecutor.execute(new Runnable() {
            public void run() {
                AudioTrackSoundManager.this.playSound(paramInt);
            }
        });
    }

    private void writeToAudioTrack(byte[] paramArrayOfByte, AudioTrack paramAudioTrack, int paramInt) {
        int i = 0;
        while (i < paramInt) {
            int j = paramAudioTrack.write(paramArrayOfByte, i, paramInt - i);
            if (j < 0) {
                break;
            }
            i += j;
        }
    }

    protected AudioTrack createAudioTrack(int paramInt) {
        return new AudioTrack(this.mAudioRouter.getOutputStream(), 16000, 4, 2, paramInt, 1);
    }

    public void playDictationDoneSound() {
        playSoundAsync(2131165193);
    }

    public void playErrorSound() {
        playSoundAsync(2131165191);
    }

    public void playHandsFreeShutDownSound() {
        playSoundAsync(2131165193);
    }

    public void playNoInputSound() {
        playSoundAsync(2131165193);
    }

    public void playRecognitionDoneSound() {
        playSoundAsync(2131165195);
    }

    int playSound(byte[] paramArrayOfByte) {
        int i = paramArrayOfByte.length;
        AudioTrack localAudioTrack = createAudioTrack(i);
        if (localAudioTrack.getState() != 1) {
            return -1;
        }
        writeToAudioTrack(paramArrayOfByte, localAudioTrack, i);
        int j = localAudioTrack.getAudioSessionId();
        localAudioTrack.play();
        this.mPlaybackExecutor.execute(new WaitAndReleaseRunnable(localAudioTrack, i));
        return j;
    }

    public int playSpeakNowSound() {
        com.embryo.android.shared.util.ExtraPreconditions.checkNotMainThread();
        return playSound(2131165194);
    }

    static final class WaitAndReleaseRunnable
            implements Runnable {
        final AudioTrack mAudioTrack;
        final int mLengthBytes;

        WaitAndReleaseRunnable(AudioTrack paramAudioTrack, int paramInt) {
            this.mAudioTrack = paramAudioTrack;
            this.mLengthBytes = paramInt;
        }

        public void run() {
            int lengthInFrames = this.mLengthBytes / 2;
            int previousPosition = -1;
            long blockedTimeMs = 0L;
            for (int currentPosition = 0; (currentPosition < lengthInFrames) && (this.mAudioTrack.getPlayState() == 3); currentPosition = this.mAudioTrack.getPlaybackHeadPosition()) {
                long sleepTimeMs = AudioTrackSoundManager.clip(1000 * (lengthInFrames - currentPosition) / 16000, 50L, 500L);
                if (currentPosition == previousPosition) {
                    blockedTimeMs += sleepTimeMs;
                    if (blockedTimeMs > 500L) {
                        Log.w("AudioTrackSoundManager", "Waited unsuccessfully for 500ms for AudioTrack to make progress, Aborting");
                        break;
                    }
                } else {
                    blockedTimeMs = 0;
                }

                previousPosition = currentPosition;

                try {
                    Thread.sleep(sleepTimeMs);
                } catch (InterruptedException localInterruptedException) {
                }
            }
            this.mAudioTrack.release();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     AudioTrackSoundManager

 * JD-Core Version:    0.7.0.1

 */