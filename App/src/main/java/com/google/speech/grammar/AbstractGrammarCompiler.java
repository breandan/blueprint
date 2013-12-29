package com.google.speech.grammar;

import java.nio.charset.Charset;
import java.util.logging.Logger;

public abstract class AbstractGrammarCompiler {
    private static final Logger logger = Logger.getLogger(AbstractGrammarCompiler.class.getName());
    private long nativeObj = nativeConstruct();

    private void assertValidState() {
        if (this.nativeObj == 0L) {
            throw new IllegalStateException("Recognizer not initialized");
        }
    }

    private static native boolean nativeCompile(long paramLong, byte[] paramArrayOfByte);

    private static native long nativeConstruct();

    private static native boolean nativeDelete(long paramLong);

    private static native boolean nativeInitFromFile(long paramLong, String paramString, String[] paramArrayOfString);

    private static native boolean nativeInitFromProto(long paramLong, byte[] paramArrayOfByte, String[] paramArrayOfString);

    private static native boolean nativeReadCache(long paramLong, String paramString);

    private static native boolean nativeWriteCache(long paramLong, String paramString, boolean paramBoolean);

    private static native boolean nativeWriteClgFst(long paramLong, String paramString1, String paramString2);

    private static native boolean nativeWriteSemanticFst(long paramLong, String paramString1, String paramString2);

    public synchronized boolean compileAbnf(String paramString) {
	assertValidState();
	return nativeCompile(this.nativeObj, paramString.getBytes(Charset.forName("UTF-8")));
    }

    public synchronized void delete() {
            if (this.nativeObj != 0L) {
                nativeDelete(this.nativeObj);
                this.nativeObj = 0L;
            }
    }

    protected void finalize() {
        delete();
    }

    public synchronized boolean initFromFile(String paramString, String[] paramArrayOfString) {
            assertValidState();
            return nativeInitFromFile(this.nativeObj, paramString, paramArrayOfString);
    }

    public synchronized boolean initFromProto(byte[] paramArrayOfByte, String[] paramArrayOfString) {
            assertValidState();
            return nativeInitFromProto(this.nativeObj, paramArrayOfByte, paramArrayOfString);
    }

    public synchronized boolean readCache(String paramString) {
            assertValidState();
            return nativeReadCache(this.nativeObj, paramString);
    }

    public synchronized boolean writeCache(String paramString, boolean paramBoolean) {
            assertValidState();
            return nativeWriteCache(this.nativeObj, paramString, paramBoolean);
    }

    public synchronized boolean writeClgFst(String paramString1, String paramString2) {
            assertValidState();
            return nativeWriteClgFst(this.nativeObj, paramString1, paramString2);
    }

    public synchronized boolean writeSemanticFst(String paramString1, String paramString2) {
            assertValidState();
            return nativeWriteSemanticFst(this.nativeObj, paramString1, paramString2);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.grammar.AbstractGrammarCompiler

 * JD-Core Version:    0.7.0.1

 */
