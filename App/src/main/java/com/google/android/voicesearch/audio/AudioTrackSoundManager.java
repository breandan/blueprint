package com.google.android.voicesearch.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioTrack;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.audio.SpeakNowSoundPlayer;
import com.google.common.io.ByteStreams;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
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

    private byte[] loadSound(int paramInt) {
        AssetFileDescriptor localAssetFileDescriptor = this.mContext.getResources().openRawResourceFd(paramInt);
        if (localAssetFileDescriptor.getLength() > 2147483647L) {
            closeSilently(localAssetFileDescriptor);
            return INVALID_SOUND;
        }
        int i = (int) localAssetFileDescriptor.getLength();
        byte[] arrayOfByte1 = new byte[i];
        FileInputStream localFileInputStream = null;
        try {
            localFileInputStream = localAssetFileDescriptor.createInputStream();
            if (ByteStreams.read(localFileInputStream, arrayOfByte1, 0, i) != i) {
                throw new IOException();
            }
        } catch (IOException localIOException) {
            byte[] arrayOfByte2 = INVALID_SOUND;
            return arrayOfByte2;
            return arrayOfByte1;
        } finally {
            closeSilently(localFileInputStream);
            closeSilently(localAssetFileDescriptor);
        }
    }

    private int playSound(int paramInt) {
        byte[] arrayOfByte = (byte[]) this.mAudioData.get(paramInt, null);
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
        ExtraPreconditions.checkNotMainThread();
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
            int i = this.mLengthBytes / 2;
            int j = -1;
            long l1 = 0L;
            int k = this.mAudioTrack.getPlaybackHeadPosition();
            long l2;
            if ((k < i) && (this.mAudioTrack.getPlayState() == 3)) {
                l2 = AudioTrackSoundManager.clip(1000 * (i - k) / 16000, 50L, 500L);
                if (k != j) {
                    break label96;
                }
                l1 += l2;
                if (l1 <= 500L) {
                    break label98;
                }
                Log.w("AudioTrackSoundManager", "Waited unsuccessfully for 500ms for AudioTrack to make progress, Aborting");
            }
            for (; ; ) {
                for (; ; ) {
                    this.mAudioTrack.release();
                    return;
                    label96:
                    l1 = 0L;
                    label98:
                    j = k;
                    try {
                        Thread.sleep(l2);
                    } catch (InterruptedException localInterruptedException) {
                    }
                }
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.audio.AudioTrackSoundManager

 * JD-Core Version:    0.7.0.1

 */