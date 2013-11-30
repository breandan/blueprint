package com.google.android.speech.embedded;

import com.google.common.base.Preconditions;

import java.io.File;

public enum Greco3Grammar {
    CONTACT_DIALING("CONTACT_DIALING", 0x0, "contacts", true, true),
    HANDS_FREE_COMMANDS("HANDS_FREE_COMMANDS", 0x1, "hands_free_commands", true, false);
    private final boolean mAddContacts;
    private final boolean mCompiledOnDevice;
    private final String mDirectoryName;
    Greco3Grammar[] arrayOfGreco3Grammar = new Greco3Grammar[]{CONTACT_DIALING, HANDS_FREE_COMMANDS};

    private Greco3Grammar(String paramString1, int paramInt, String paramString2, boolean paramBoolean1, boolean paramBoolean2) {
        this.mDirectoryName = paramString1;
        this.mCompiledOnDevice = paramBoolean1;
        this.mAddContacts = paramBoolean2;
    }

    public static Greco3Grammar fromDirectoryName(String paramString) {
        if (CONTACT_DIALING.getDirectoryName().equals(paramString)) {
            return CONTACT_DIALING;
        }
        if (HANDS_FREE_COMMANDS.getDirectoryName().equals(paramString)) {
            return HANDS_FREE_COMMANDS;
        }
        return null;
    }

    public static Greco3Grammar valueOf(File paramFile) {
        return fromDirectoryName(paramFile.getName());
    }

    public String getApkFullName(String paramString) {
        return ((String) Preconditions.checkNotNull(paramString)).replace("-", "_").toLowerCase() + "_" + this.mDirectoryName;
    }

    public String getDirectoryName() {
        return this.mDirectoryName;
    }

    public boolean isAddContacts() {
        return this.mAddContacts;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.Greco3Grammar

 * JD-Core Version:    0.7.0.1

 */