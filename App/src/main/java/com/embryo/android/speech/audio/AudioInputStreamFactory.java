package com.embryo.android.speech.audio;

import java.io.IOException;
import java.io.InputStream;

public abstract interface AudioInputStreamFactory {
    public abstract InputStream createInputStream()
            throws IOException;
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     AudioInputStreamFactory

 * JD-Core Version:    0.7.0.1

 */