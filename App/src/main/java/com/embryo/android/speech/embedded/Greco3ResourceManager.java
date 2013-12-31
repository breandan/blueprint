package com.embryo.android.speech.embedded;

import android.util.Log;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class Greco3ResourceManager
        extends com.embryo.speech.recognizer.ResourceManager {
    public static Greco3ResourceManager create(String configFileName, String[] resourcePaths) {
        Greco3ResourceManager rm = new Greco3ResourceManager();
        File configFile = new File(configFileName);
        int status;
        if (Greco3Mode.isAsciiConfiguration(configFile)) {
            status = rm.initFromFile(configFileName, resourcePaths);
        } else {
            byte[] fileBytes = getFileBytes(configFile);
            if (fileBytes == null) {
                Log.e("VS.G3ResourceManager", "Error reading g3 config file: " + configFileName);
                return null;
            }
            status = rm.initFromProto(fileBytes, resourcePaths);
        }

        if (status == 0) {
            return rm;
        }

        Log.e("VS.G3ResourceManager", "Failed to bring up g3, Status code: " + status);
        return null;
    }

    private static byte[] getFileBytes(File paramFile) {
        try {
            byte[] arrayOfByte = Files.toByteArray(paramFile);
            return arrayOfByte;
        } catch (IOException localIOException) {
        }
        return null;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3ResourceManager

 * JD-Core Version:    0.7.0.1

 */