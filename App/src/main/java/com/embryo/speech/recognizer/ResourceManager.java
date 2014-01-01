package com.embryo.speech.recognizer;

public abstract class ResourceManager {
    private long nativeObj = nativeConstruct();

    private native long nativeConstruct();

    private native void nativeDelete(long paramLong);

    private native int nativeInitFromFile(long paramLong, String paramString, String[] paramArrayOfString);

    private native int nativeInitFromProto(long paramLong, byte[] paramArrayOfByte, String[] paramArrayOfString);

    private void validate() {
        if (this.nativeObj == 0L) {
            throw new IllegalStateException("recognizer is not initialized");
        }
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

    long getNativeObject() {
        return this.nativeObj;
    }

    public int initFromFile(String paramString, String[] paramArrayOfString) {
        validate();
        return nativeInitFromFile(this.nativeObj, paramString, paramArrayOfString);
    }

    public int initFromProto(byte[] paramArrayOfByte, String[] paramArrayOfString) {
        validate();
        return nativeInitFromProto(this.nativeObj, paramArrayOfByte, paramArrayOfString);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ResourceManager

 * JD-Core Version:    0.7.0.1

 */