package com.embryo.android.speech.debug;

import android.content.Context;
import android.util.Log;

import com.embryo.android.speech.SpeechSettings;
import com.embryo.android.speech.test.TestPlatformLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DebugAudioLogger {
    public static InputStream maybeWrapInLogStream(InputStream in, Context context, SpeechSettings settings) {
        if (shouldLog(settings)) {
            String fileName = "mic-" + System.currentTimeMillis() + ".pcm";
            File outputDir = context.getDir("debug", 0x0);
            String absolutePath = new File(outputDir, fileName).getAbsolutePath();
            TestPlatformLog.logAudioPath(absolutePath);
            try {
                return new CopyInputStream(in, new BufferedOutputStream(new FileOutputStream(absolutePath), 960000));
            } catch (FileNotFoundException ex) {
                Log.e("VS.DebugLogger", "Error opening audio log file.", ex);
            }
        }
        return in;
    }

    private static boolean shouldLog(com.embryo.android.speech.SpeechSettings paramSpeechSettings) {
        return paramSpeechSettings.isDebugAudioLoggingEnabled();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     DebugAudioLogger

 * JD-Core Version:    0.7.0.1

 */