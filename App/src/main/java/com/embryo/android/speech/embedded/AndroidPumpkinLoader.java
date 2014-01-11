package com.embryo.android.speech.embedded;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import com.embryo.speech.grammar.pumpkin.PumpkinConfigProto;

import java.io.FileInputStream;
import java.io.IOException;

public class AndroidPumpkinLoader
        extends com.embryo.speech.grammar.pumpkin.PumpkinLoader {
    private final AssetManager mAssets;
    private final String mAssetsFolder;

    public AndroidPumpkinLoader(Context paramContext, String paramString) {
        super(null, null, null);
        this.mAssetsFolder = getAssetsFolderName(paramString);
        this.mAssets = paramContext.getAssets();
    }

    public static String getAssetsFolderName(String paramString) {
        if (paramString.startsWith("en-")) {
            paramString = "en-US";
        }
        return paramString;
    }

    private static void loadLibrary() {
    }

    private byte[] readFile(String paramString)
            throws IOException {
        AssetFileDescriptor localAssetFileDescriptor = this.mAssets.openFd(paramString);
        byte[] arrayOfByte = new byte[(int) localAssetFileDescriptor.getLength()];
        FileInputStream localFileInputStream = localAssetFileDescriptor.createInputStream();
        try {
            localFileInputStream.read(arrayOfByte);
            return arrayOfByte;
        } finally {
            localFileInputStream.close();
        }
    }

    public com.embryo.speech.grammar.pumpkin.ActionFrame createActionFrame(byte[] paramArrayOfByte) {
        return actionFrameManager.createActionFrame(paramArrayOfByte);
    }

    public void init()
            throws IOException {
        loadLibrary();
        super.init();
    }

    public byte[] loadActionDisambigSetConfigBytes()
            throws IOException {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.mAssetsFolder;
        return readFile(String.format("%s/action_disambig.pumpkin", arrayOfObject));
    }

    public byte[] loadActionSelectRecipientSetConfigBytes()
            throws IOException {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.mAssetsFolder;
        return readFile(String.format("%s/action_select_recipient.pumpkin", arrayOfObject));
    }

    public byte[] loadActionSetConfigBytes()
            throws IOException {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.mAssetsFolder;
        return readFile(String.format("%s/action.pumpkin", arrayOfObject));
    }

    protected PumpkinConfigProto.PumpkinConfig loadPumpkinConfig()
            throws IOException {
        return null;
    }

    public void loadPumpkinConfigBytes()
            throws IOException {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.mAssetsFolder;
        this.pumpkinConfigBytes = readFile(String.format("%s/config.pumpkin", arrayOfObject));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     AndroidPumpkinLoader

 * JD-Core Version:    0.7.0.1

 */