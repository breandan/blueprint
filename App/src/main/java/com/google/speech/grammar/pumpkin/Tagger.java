package com.google.speech.grammar.pumpkin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tagger {
    private static final Logger logger = Logger.getLogger(Tagger.class.getName());
    private long nativeTagger;

    protected Tagger(PumpkinConfigProto.PumpkinConfig paramPumpkinConfig) {
        this(paramPumpkinConfig.toByteArray());
    }

    protected Tagger(byte[] paramArrayOfByte) {
        this.nativeTagger = nativeConstruct(paramArrayOfByte);
    }

    private static native long nativeConstruct(byte[] paramArrayOfByte);

    private static native void nativeDelete(long paramLong);

    private static native byte[] nativeTag(long paramLong1, long paramLong2, long paramLong3, String paramString);

    public synchronized void delete() {
        if (this.nativeTagger != 0L) {
            nativeDelete(this.nativeTagger);
            this.nativeTagger = 0L;
        }
    }

    protected void finalize() {
        delete();
    }

    public PumpkinTaggerResultsProto.PumpkinTaggerResults tag(String paramString, ActionFrame paramActionFrame, UserValidators paramUserValidators) {
        if (paramActionFrame == null) {
            throw new NullPointerException("Passed null ActionFrame to the Pumpkin Tagger");
        }
        if (paramUserValidators == null) {
            throw new NullPointerException("Passed null UserValidators to the Pumpkin Tagger");
        }
        String str = paramString.toLowerCase();
        byte[] arrayOfByte = nativeTag(this.nativeTagger, paramActionFrame.getNativeActionFrame(), paramUserValidators.getNativeUserValidators(), str);
        try {
            PumpkinTaggerResultsProto.PumpkinTaggerResults localPumpkinTaggerResults = PumpkinTaggerResultsProto.PumpkinTaggerResults.parseFrom(arrayOfByte);
            return localPumpkinTaggerResults;
        } catch (IOException localIOException) {
            logger.log(Level.SEVERE, "Couldn't parse PumpkinTaggerResults proto from JNI");
        }
        return null;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.grammar.pumpkin.Tagger

 * JD-Core Version:    0.7.0.1

 */