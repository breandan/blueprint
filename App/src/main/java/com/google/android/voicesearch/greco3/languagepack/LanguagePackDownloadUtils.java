package com.google.android.voicesearch.greco3.languagepack;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class LanguagePackDownloadUtils {
    public static boolean deleteExistingFilesOrCreateDirectory(File paramFile) {
        boolean bool;
        if (paramFile.exists()) {
            bool = true;
            File[] arrayOfFile = paramFile.listFiles();
            if (arrayOfFile != null) {
                for (int i = 0; i < arrayOfFile.length; i++) {
                    if (!arrayOfFile[i].delete()) {
                        Log.e("LanguagePackDownloadUtils", "Unable to delete file: " + arrayOfFile[i].getAbsolutePath());
                        bool = false;
                    }
                }
            }
        } else {
            if (paramFile.mkdir()) {
                break label116;
            }
            Log.e("LanguagePackDownloadUtils", "Unable to create directory: " + paramFile.getAbsolutePath());
            bool = false;
        }
        return bool;
        label116:
        return true;
    }

    public static File getDestinationDir(Context paramContext, String paramString) {
        return new File(paramContext.getDir("g3_models", 0), paramString);
    }

    public static File getStagingDir(Context paramContext, String paramString) {
        return new File(paramContext.getDir("g3_models", 0), paramString + "_staging");
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.LanguagePackDownloadUtils

 * JD-Core Version:    0.7.0.1

 */