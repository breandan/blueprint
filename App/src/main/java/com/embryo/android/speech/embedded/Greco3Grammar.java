package com.embryo.android.speech.embedded;

import com.embryo.common.base.Preconditions;
import java.io.File;

public enum Greco3Grammar
{
    CONTACT_DIALING("contacts", true, true),
    HANDS_FREE_COMMANDS("hands_free_commands", true, false);
    private final boolean mAddContacts;
    private final boolean mCompiledOnDevice;
    private final String mDirectoryName;


    private Greco3Grammar(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    {
        this.mDirectoryName = paramString;
        this.mCompiledOnDevice = paramBoolean1;
        this.mAddContacts = paramBoolean2;
    }

    public static Greco3Grammar fromDirectoryName(String paramString)
    {
        if (CONTACT_DIALING.getDirectoryName().equals(paramString)) {
            return CONTACT_DIALING;
        }
        if (HANDS_FREE_COMMANDS.getDirectoryName().equals(paramString)) {
            return HANDS_FREE_COMMANDS;
        }
        return null;
    }

    public static Greco3Grammar valueOf(File paramFile)
    {
        return fromDirectoryName(paramFile.getName());
    }

    public String getApkFullName(String paramString)
    {
        return ((String)Preconditions.checkNotNull(paramString)).replace("-", "_").toLowerCase() + "_" + this.mDirectoryName;
    }

    public String getDirectoryName()
    {
        return this.mDirectoryName;
    }

    public boolean isAddContacts()
    {
        return this.mAddContacts;
    }
}
