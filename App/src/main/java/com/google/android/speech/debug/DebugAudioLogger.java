package com.google.android.speech.debug;

import android.content.Context;
import android.util.Log;

import com.google.android.speech.SpeechSettings;
import com.google.android.speech.test.TestPlatformLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DebugAudioLogger {
    public static InputStream maybeWrapInLogStream(InputStream paramInputStream, Context paramContext, SpeechSettings paramSpeechSettings) {
        String str2;
        if (shouldLog(paramSpeechSettings)) {
            String str1 = "mic-" + System.currentTimeMillis() + ".pcm";
            str2 = new File(paramContext.getDir("debug", 0), str1).getAbsolutePath();
            TestPlatformLog.logAudioPath(str2);
            try {
                CopyInputStream localCopyInputStream = new CopyInputStream(paramInputStream, new BufferedOutputStream(new FileOutputStream(str2), 960000));
                paramInputStream = localCopyInputStream;
                return paramInputStream;
            } catch (FileNotFoundException localFileNotFoundException) {
                Log.e("VS.DebugLogger", "Error opening audio log file.", localFileNotFoundException);
            }
        }
        return paramInputStream;
    }

    private static boolean shouldLog(SpeechSettings paramSpeechSettings) {
        return paramSpeechSettings.isDebugAudioLoggingEnabled();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.debug.DebugAudioLogger

 * JD-Core Version:    0.7.0.1

 */