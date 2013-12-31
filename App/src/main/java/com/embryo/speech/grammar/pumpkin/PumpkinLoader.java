package com.embryo.speech.grammar.pumpkin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Nullable;

public abstract class PumpkinLoader {
    protected static final Logger logger = Logger.getLogger(PumpkinLoader.class.getName());
    protected static ActionFrameManager actionFrameManager;
    protected final String actionPath;
    protected final String grmPath;
    @Nullable
    protected File pumpkinConfig;
    @Nullable
    protected byte[] pumpkinConfigBytes;
    protected Tagger tagger;
    protected UserValidators userValidators;

    public PumpkinLoader(String paramString1, String paramString2, byte[] paramArrayOfByte) {
        this.actionPath = paramString1;
        this.grmPath = paramString2;
        this.pumpkinConfig = null;
        this.pumpkinConfigBytes = paramArrayOfByte;
    }

    public Tagger getTagger() {
        return this.tagger;
    }

    public UserValidators getUserValidators() {
        return this.userValidators;
    }

    public synchronized void init() throws IOException {
        if (actionFrameManager == null) {
            actionFrameManager = new ActionFrameManager();
        }
        if (pumpkinConfig != null) {
            com.embryo.speech.grammar.pumpkin.PumpkinConfigProto.PumpkinConfig config = loadPumpkinConfig();
            if (tagger == null) {
                tagger = new Tagger(config);
            }
            if (userValidators == null) {
                userValidators = new UserValidators(config);
            }
            return;
        }
        if (tagger == null) {
            tagger = new Tagger(pumpkinConfigBytes);
        }
        if (userValidators == null) {
            userValidators = new UserValidators(pumpkinConfigBytes);
        }
    }

    protected abstract com.embryo.speech.grammar.pumpkin.PumpkinConfigProto.PumpkinConfig loadPumpkinConfig()
            throws IOException;
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     PumpkinLoader

 * JD-Core Version:    0.7.0.1

 */