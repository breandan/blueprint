package com.google.android.speech.audio;

import android.media.audiofx.AudioEffect;

import com.google.common.collect.Lists;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.UUID;

public class AudioUtils {
    private static final UUID EFFECT_TYPE_NOISE_SUPRRESSOR = UUID.fromString("58b4b260-8e06-11e0-aa8e-0002a5d5c51b");

    public static List<String> getNoiseSuppressors(GstaticConfiguration.Platform paramPlatform) {
        List<String> noiseSupressors = Lists.newLinkedList();
        List localList = paramPlatform.getEnabledNoiseSuppressorsList();
        for (AudioEffect.Descriptor localDescriptor : AudioEffect.queryEffects()) {
            if (EFFECT_TYPE_NOISE_SUPRRESSOR.equals(localDescriptor.type)) {
                String str = localDescriptor.uuid.toString();
                if (!localList.contains(str)) {
                    noiseSupressors.clear();
                }
                noiseSupressors.add(str);
            }
        }
        return noiseSupressors;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.audio.AudioUtils

 * JD-Core Version:    0.7.0.1

 */