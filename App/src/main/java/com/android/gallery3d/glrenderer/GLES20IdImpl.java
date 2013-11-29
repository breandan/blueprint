package com.android.gallery3d.glrenderer;

import android.opengl.GLES20;

public class GLES20IdImpl
  implements GLId
{
  private final int[] mTempIntArray = new int[1];
  
  public int generateTexture()
  {
    GLES20.glGenTextures(1, this.mTempIntArray, 0);
    GLES20Canvas.checkError();
    return this.mTempIntArray[0];
  }
  
  public void glGenBuffers(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    GLES20.glGenBuffers(paramInt1, paramArrayOfInt, paramInt2);
    GLES20Canvas.checkError();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.glrenderer.GLES20IdImpl
 * JD-Core Version:    0.7.0.1
 */