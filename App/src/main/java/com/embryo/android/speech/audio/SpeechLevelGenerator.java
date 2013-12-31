package com.embryo.android.speech.audio;

import com.embryo.android.shared.util.SpeechLevelSource;

public class SpeechLevelGenerator {
    private final SpeechLevelSource mSpeechLevelSource;
    private float mNoiseLevel;

    public SpeechLevelGenerator(SpeechLevelSource paramSpeechLevelSource) {
        this.mSpeechLevelSource = paramSpeechLevelSource;
        this.mNoiseLevel = 75.0F;
    }

    private static float calculateRms(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        long l1 = 0L;
        long l2 = 0L;
        int i = paramInt2 / 2;
        for (int j = paramInt1 + paramInt2; j >= paramInt1 + 2; j -= 2) {
            int k = (short) ((paramArrayOfByte[(j - 1)] << 8) + (0xFF & paramArrayOfByte[(j - 2)]));
            l1 += k;
            l2 += k * k;
        }
        return (float) Math.sqrt((l2 * i - l1 * l1) / (i * i));
    }

    public static int convertRmsDbToVolume(float paramFloat) {
        int i = (int) (100.0F * (Math.min(Math.max(paramFloat, -2.0F), 10.0F) + 2.0F) / 12.0F);
        if (i < 30) {
            return 0;
        }
        return 10 * (i / 10);
    }

    public static float convertVolumeToRmsDb(int paramInt) {
        return -2.0F + 12.0F * (paramInt / 100.0F);
    }

    public void update(byte[] bytes, int offset, int count) {
        float rms = calculateRms(bytes, offset, count);
        if (mNoiseLevel < rms) {
            mNoiseLevel = ((0x3f7fbe77 * mNoiseLevel) + (0x3a83126f * rms));
        } else {
            mNoiseLevel = ((0x3f733333 * mNoiseLevel) + (0x3d4ccccd * rms));
        }
        float snr = -120.0f;
        if (((double) mNoiseLevel > 0.0) && ((double) (rms / mNoiseLevel) > 0.0)) {
            snr = 10.0f * (float) Math.log10((double) (rms / mNoiseLevel));
        }
        mSpeechLevelSource.setSpeechLevel(convertRmsDbToVolume(snr));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     SpeechLevelGenerator

 * JD-Core Version:    0.7.0.1

 */