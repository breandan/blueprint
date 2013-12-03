package com.google.speech.grammar.pumpkin;

public class ActionFrame {
    private long nativeActionFrame;

    protected ActionFrame(long paramLong) {
        if (paramLong == 0L) {
            throw new IllegalArgumentException("Can't initialize ActionFrame wrapper with a null ActionFrame");
        }
        this.nativeActionFrame = paramLong;
    }

    private static native void nativeDelete(long paramLong);

    public void delete() {
        try {
            nativeDelete(this.nativeActionFrame);
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    protected void finalize() {
        delete();
    }

    public long getNativeActionFrame() {
        return this.nativeActionFrame;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.grammar.pumpkin.ActionFrame

 * JD-Core Version:    0.7.0.1

 */