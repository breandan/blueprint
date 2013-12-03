package com.google.android.speech.audio;

import android.media.audiofx.AudioEffect;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AudioUtils {
    private static final UUID EFFECT_TYPE_NOISE_SUPRRESSOR = UUID.fromString("58b4b260-8e06-11e0-aa8e-0002a5d5c51b");
    private static Constructor<? extends InputStream> sAmrInputStreamConstructor;

    private static byte[] addWavHeaders(byte[] paramArrayOfByte)
            throws IOException {
        byte[] arrayOfByte = new byte[44];
        ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
        localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        localByteBuffer.put(new byte[]{82, 73, 70, 70});
        localByteBuffer.putInt(-8 + (44 + paramArrayOfByte.length));
        localByteBuffer.put(new byte[]{87, 65, 86, 69});
        localByteBuffer.put(new byte[]{102, 109, 116, 32});
        localByteBuffer.putInt(16);
        localByteBuffer.putShort((short) 1);
        localByteBuffer.putShort((short) 1);
        localByteBuffer.putInt(8000);
        localByteBuffer.putInt(16000);
        localByteBuffer.putShort((short) 2);
        localByteBuffer.putShort((short) 16);
        localByteBuffer.put(new byte[]{100, 97, 116, 97});
        localByteBuffer.putInt(paramArrayOfByte.length);
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(44 + paramArrayOfByte.length);
        localByteArrayOutputStream.write(arrayOfByte);
        localByteArrayOutputStream.write(paramArrayOfByte);
        return localByteArrayOutputStream.toByteArray();
    }

    /* Error */
    private static InputStream createAmrInputStream(InputStream paramInputStream) {
        // Byte code:
        //   0: ldc 2
        //   2: monitorenter
        //   3: getstatic 87	com/google/android/speech/audio/AudioUtils:sAmrInputStreamConstructor	Ljava/lang/reflect/Constructor;
        //   6: ifnonnull +23 -> 29
        //   9: ldc 89
        //   11: invokestatic 95	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
        //   14: iconst_1
        //   15: anewarray 91	java/lang/Class
        //   18: dup
        //   19: iconst_0
        //   20: ldc 97
        //   22: aastore
        //   23: invokevirtual 101	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
        //   26: putstatic 87	com/google/android/speech/audio/AudioUtils:sAmrInputStreamConstructor	Ljava/lang/reflect/Constructor;
        //   29: getstatic 87	com/google/android/speech/audio/AudioUtils:sAmrInputStreamConstructor	Ljava/lang/reflect/Constructor;
        //   32: iconst_1
        //   33: anewarray 4	java/lang/Object
        //   36: dup
        //   37: iconst_0
        //   38: aload_0
        //   39: aastore
        //   40: invokevirtual 107	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
        //   43: checkcast 97	java/io/InputStream
        //   46: astore_3
        //   47: ldc 2
        //   49: monitorexit
        //   50: aload_3
        //   51: areturn
        //   52: astore_2
        //   53: ldc 2
        //   55: monitorexit
        //   56: aload_2
        //   57: athrow
        //   58: astore_1
        //   59: new 109	java/lang/RuntimeException
        //   62: dup
        //   63: ldc 111
        //   65: aload_1
        //   66: invokespecial 114	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   69: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	70	0	paramInputStream	InputStream
        //   58	8	1	localException	java.lang.Exception
        //   52	5	2	localObject	Object
        //   46	5	3	localInputStream	InputStream
        // Exception table:
        //   from	to	target	type
        //   3	29	52	finally
        //   29	50	52	finally
        //   53	56	52	finally
        //   0	3	58	java/lang/Exception
        //   56	58	58	java/lang/Exception
    }

    private static InputStream createAmrWbInputStream(InputStream paramInputStream) {
        return new AudioEncoderInputStream(paramInputStream, "audio/amr-wb", 16000, 2048, 23850, 1);
    }

    public static byte[] encode(Encoding paramEncoding, byte[] paramArrayOfByte)
            throws IOException {
        switch (1.
        $SwitchMap$com$google$android$speech$audio$AudioUtils$Encoding[paramEncoding.ordinal()])
        {
            default:
                throw new IllegalArgumentException("Encoding not supported: " + paramEncoding);
            case 1:
                return addWavHeaders(paramArrayOfByte);
            case 2:
                return encodeAmr(paramArrayOfByte, 3, true);
        }
        return encodeAmr(paramArrayOfByte, 9, false);
    }

    private static byte[] encodeAmr(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
            throws IOException {
        InputStream localInputStream = null;
        try {
            localInputStream = getEncodingInputStream(new ByteArrayInputStream(paramArrayOfByte), paramInt);
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            if (paramBoolean) {
                localByteArrayOutputStream.write("#!AMR\n".getBytes());
            }
            byte[] arrayOfByte1 = new byte[384];
            for (; ; ) {
                int i = ByteStreams.read(localInputStream, arrayOfByte1, 0, arrayOfByte1.length);
                if (i <= 0) {
                    break;
                }
                localByteArrayOutputStream.write(arrayOfByte1, 0, i);
            }
            arrayOfByte2 = localByteArrayOutputStream.toByteArray();
        } finally {
            Closeables.closeQuietly(localInputStream);
        }
        byte[] arrayOfByte2;
        Closeables.closeQuietly(localInputStream);
        return arrayOfByte2;
    }

    public static Encoding getAmrEncodingForRecording(AudioStore.AudioRecording paramAudioRecording) {
        if (paramAudioRecording.getSampleRate() == 16000) {
            return Encoding.AMRWB;
        }
        if (paramAudioRecording.getSampleRate() == 8000) {
            return Encoding.AMR;
        }
        throw new IllegalArgumentException("Unsupported sample rate: " + paramAudioRecording.getSampleRate());
    }

    public static InputStream getEncodingInputStream(InputStream paramInputStream, int paramInt) {
        if (paramInt == 0) {
            return paramInputStream;
        }
        if (paramInt == 3) {
            return createAmrInputStream(paramInputStream);
        }
        if (paramInt == 9) {
            return createAmrWbInputStream(paramInputStream);
        }
        throw new RuntimeException("unsupported encoding:" + paramInt);
    }

    public static List<String> getNoiseSuppressors(GstaticConfiguration.Platform paramPlatform) {
        Object localObject;
        if (paramPlatform == null) {
            localObject = Lists.newArrayList();
        }
        for (; ; ) {
            return localObject;
            localObject = new LinkedList();
            List localList = paramPlatform.getEnabledNoiseSuppressorsList();
            for (AudioEffect.Descriptor localDescriptor : AudioEffect.queryEffects()) {
                if (EFFECT_TYPE_NOISE_SUPRRESSOR.equals(localDescriptor.type)) {
                    String str = localDescriptor.uuid.toString();
                    if (!localList.contains(str)) {
                        break label97;
                    }
                    ((List) localObject).add(str);
                }
            }
        }
        label97:
        ((List) localObject).clear();
        return localObject;
    }

    public static enum Encoding {
        WAV("audio/wav", "wav", 1, 0),
        AMR ("audio/AMR", "amr", 2, 3),
        AMRWB ("audio/amr-wb", "amr", 3, 9);
        private final int mCode;
        private final String mExt;
        private final String mMimeType;
        private final int mRecognizerEncoding;
        private static Encoding[] localEncodings;

        static {
            Encoding[] arrayOfEncoding = new Encoding[3];
            arrayOfEncoding[0] = WAV;
            arrayOfEncoding[1] = AMR;
            arrayOfEncoding[2] = AMRWB;
            localEncodings = arrayOfEncoding;
        }

        private Encoding(String paramString1, String paramString2, int paramInt1, int paramInt2) {
            this.mMimeType = paramString1;
            this.mExt = paramString2;
            this.mCode = paramInt1;
            this.mRecognizerEncoding = paramInt2;
        }

        public static Encoding fromCode(int paramInt) {
            for (Encoding localEncoding : localEncodings) {
                if (localEncoding.getCode() == paramInt) {
                    return localEncoding;
                }
            }
            throw new IllegalArgumentException("invalid code: " + paramInt);
        }

        public int getCode() {
            return this.mCode;
        }

        public String getExt() {
            return this.mExt;
        }

        public String getMimeType() {
            return this.mMimeType;
        }

        public int getRecognizerEncoding() {
            return this.mRecognizerEncoding;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.audio.AudioUtils

 * JD-Core Version:    0.7.0.1

 */