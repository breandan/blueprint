package com.google.audio.ears;

public class MusicDetector
{
  public static native void close();
  
  public static native boolean init(int paramInt);
  
  public static native float process(byte[] paramArrayOfByte, int paramInt);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.audio.ears.MusicDetector
 * JD-Core Version:    0.7.0.1
 */