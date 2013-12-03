package com.google.speech.grammar.pumpkin;

public class ActionFrameManager {
    private long nativeActionFrameManager = nativeCreate();

    public ActionFrameManager() {
        if (this.nativeActionFrameManager == 0L) {
            throw new IllegalArgumentException("Couldn't create action_frame_manager from the provided config");
        }
    }

    private void delete() {
        try {
            nativeDelete(this.nativeActionFrameManager);
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    private static native long nativeCreate();

    private static native void nativeDelete(long paramLong);

    private static native long nativeLoadActionFrame(long paramLong, byte[] paramArrayOfByte);

    public ActionFrame createActionFrame(byte[] paramArrayOfByte) {
        long l = nativeLoadActionFrame(this.nativeActionFrameManager, paramArrayOfByte);
        if (l == 0L) {
            throw new IllegalArgumentException("Couldn't create action_frame from the provided ActionSetConfig");
        }
        return new ActionFrame(l);
    }

    protected void finalize() {
        delete();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.grammar.pumpkin.ActionFrameManager

 * JD-Core Version:    0.7.0.1

 */