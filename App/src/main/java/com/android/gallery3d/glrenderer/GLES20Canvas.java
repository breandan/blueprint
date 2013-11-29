package com.android.gallery3d.glrenderer;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import com.android.gallery3d.util.IntArray;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class GLES20Canvas
  implements GLCanvas
{
  private static final float[] BOUNDS_COORDINATES = { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  private static final float[] BOX_COORDINATES;
  private static final String TAG = GLES20Canvas.class.getSimpleName();
  private static final GLId mGLId = new GLES20IdImpl();
  private float[] mAlphas = new float[8];
  private int mBoxCoordinates;
  private int mCountDrawLine;
  private int mCountDrawMesh;
  private int mCountFillRect;
  private int mCountTextureRect;
  private int mCurrentAlphaIndex = 0;
  private int mCurrentMatrixIndex = 0;
  private final IntArray mDeleteBuffers;
  ShaderParameter[] mDrawParameters;
  private int mDrawProgram;
  private int[] mFrameBuffer;
  private int mHeight;
  private float[] mMatrices = new float[''];
  ShaderParameter[] mMeshParameters;
  private int mMeshProgram;
  ShaderParameter[] mOesTextureParameters;
  private int mOesTextureProgram;
  private float[] mProjectionMatrix = new float[16];
  private IntArray mSaveFlags = new IntArray();
  private int mScreenHeight;
  private int mScreenWidth;
  private ArrayList<RawTexture> mTargetTextures;
  private final float[] mTempColor;
  private final int[] mTempIntArray;
  private final float[] mTempMatrix;
  private final RectF mTempSourceRect;
  private final RectF mTempTargetRect;
  private final float[] mTempTextureMatrix;
  ShaderParameter[] mTextureParameters;
  private int mTextureProgram;
  private final IntArray mUnboundTextures;
  private int mWidth;
  
  static
  {
    BOX_COORDINATES = new float[] { 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F };
  }
  
  public GLES20Canvas()
  {
    ShaderParameter[] arrayOfShaderParameter1 = new ShaderParameter[3];
    arrayOfShaderParameter1[0] = new AttributeShaderParameter("aPosition");
    arrayOfShaderParameter1[1] = new UniformShaderParameter("uMatrix");
    arrayOfShaderParameter1[2] = new UniformShaderParameter("uColor");
    this.mDrawParameters = arrayOfShaderParameter1;
    ShaderParameter[] arrayOfShaderParameter2 = new ShaderParameter[5];
    arrayOfShaderParameter2[0] = new AttributeShaderParameter("aPosition");
    arrayOfShaderParameter2[1] = new UniformShaderParameter("uMatrix");
    arrayOfShaderParameter2[2] = new UniformShaderParameter("uTextureMatrix");
    arrayOfShaderParameter2[3] = new UniformShaderParameter("uTextureSampler");
    arrayOfShaderParameter2[4] = new UniformShaderParameter("uAlpha");
    this.mTextureParameters = arrayOfShaderParameter2;
    ShaderParameter[] arrayOfShaderParameter3 = new ShaderParameter[5];
    arrayOfShaderParameter3[0] = new AttributeShaderParameter("aPosition");
    arrayOfShaderParameter3[1] = new UniformShaderParameter("uMatrix");
    arrayOfShaderParameter3[2] = new UniformShaderParameter("uTextureMatrix");
    arrayOfShaderParameter3[3] = new UniformShaderParameter("uTextureSampler");
    arrayOfShaderParameter3[4] = new UniformShaderParameter("uAlpha");
    this.mOesTextureParameters = arrayOfShaderParameter3;
    ShaderParameter[] arrayOfShaderParameter4 = new ShaderParameter[5];
    arrayOfShaderParameter4[0] = new AttributeShaderParameter("aPosition");
    arrayOfShaderParameter4[1] = new UniformShaderParameter("uMatrix");
    arrayOfShaderParameter4[2] = new AttributeShaderParameter("aTextureCoordinate");
    arrayOfShaderParameter4[3] = new UniformShaderParameter("uTextureSampler");
    arrayOfShaderParameter4[4] = new UniformShaderParameter("uAlpha");
    this.mMeshParameters = arrayOfShaderParameter4;
    this.mUnboundTextures = new IntArray();
    this.mDeleteBuffers = new IntArray();
    this.mCountDrawMesh = 0;
    this.mCountTextureRect = 0;
    this.mCountFillRect = 0;
    this.mCountDrawLine = 0;
    this.mFrameBuffer = new int[1];
    this.mTargetTextures = new ArrayList();
    this.mTempMatrix = new float[32];
    this.mTempColor = new float[4];
    this.mTempSourceRect = new RectF();
    this.mTempTargetRect = new RectF();
    this.mTempTextureMatrix = new float[16];
    this.mTempIntArray = new int[1];
    Matrix.setIdentityM(this.mTempTextureMatrix, 0);
    Matrix.setIdentityM(this.mMatrices, this.mCurrentMatrixIndex);
    this.mAlphas[this.mCurrentAlphaIndex] = 1.0F;
    this.mTargetTextures.add(null);
    this.mBoxCoordinates = uploadBuffer(createBuffer(BOX_COORDINATES));
    int i = loadShader(35633, "uniform mat4 uMatrix;\nattribute vec2 aPosition;\nvoid main() {\n  vec4 pos = vec4(aPosition, 0.0, 1.0);\n  gl_Position = uMatrix * pos;\n}\n");
    int j = loadShader(35633, "uniform mat4 uMatrix;\nuniform mat4 uTextureMatrix;\nattribute vec2 aPosition;\nvarying vec2 vTextureCoord;\nvoid main() {\n  vec4 pos = vec4(aPosition, 0.0, 1.0);\n  gl_Position = uMatrix * pos;\n  vTextureCoord = (uTextureMatrix * pos).xy;\n}\n");
    int k = loadShader(35633, "uniform mat4 uMatrix;\nattribute vec2 aPosition;\nattribute vec2 aTextureCoordinate;\nvarying vec2 vTextureCoord;\nvoid main() {\n  vec4 pos = vec4(aPosition, 0.0, 1.0);\n  gl_Position = uMatrix * pos;\n  vTextureCoord = aTextureCoordinate;\n}\n");
    int m = loadShader(35632, "precision mediump float;\nuniform vec4 uColor;\nvoid main() {\n  gl_FragColor = uColor;\n}\n");
    int n = loadShader(35632, "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform float uAlpha;\nuniform sampler2D uTextureSampler;\nvoid main() {\n  gl_FragColor = texture2D(uTextureSampler, vTextureCoord);\n  gl_FragColor *= uAlpha;\n}\n");
    int i1 = loadShader(35632, "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform float uAlpha;\nuniform samplerExternalOES uTextureSampler;\nvoid main() {\n  gl_FragColor = texture2D(uTextureSampler, vTextureCoord);\n  gl_FragColor *= uAlpha;\n}\n");
    this.mDrawProgram = assembleProgram(i, m, this.mDrawParameters);
    this.mTextureProgram = assembleProgram(j, n, this.mTextureParameters);
    this.mOesTextureProgram = assembleProgram(j, i1, this.mOesTextureParameters);
    this.mMeshProgram = assembleProgram(k, n, this.mMeshParameters);
    GLES20.glBlendFunc(1, 771);
    checkError();
  }
  
  private int assembleProgram(int paramInt1, int paramInt2, ShaderParameter[] paramArrayOfShaderParameter)
  {
    int i = GLES20.glCreateProgram();
    checkError();
    if (i == 0) {
      throw new RuntimeException("Cannot create GL program: " + GLES20.glGetError());
    }
    GLES20.glAttachShader(i, paramInt1);
    checkError();
    GLES20.glAttachShader(i, paramInt2);
    checkError();
    GLES20.glLinkProgram(i);
    checkError();
    int[] arrayOfInt = this.mTempIntArray;
    GLES20.glGetProgramiv(i, 35714, arrayOfInt, 0);
    if (arrayOfInt[0] != 1)
    {
      Log.e(TAG, "Could not link program: ");
      Log.e(TAG, GLES20.glGetProgramInfoLog(i));
      GLES20.glDeleteProgram(i);
      i = 0;
    }
    for (int j = 0; j < paramArrayOfShaderParameter.length; j++) {
      paramArrayOfShaderParameter[j].loadHandle(i);
    }
    return i;
  }
  
  public static void checkError()
  {
    int i = GLES20.glGetError();
    if (i != 0)
    {
      Throwable localThrowable = new Throwable();
      Log.e(TAG, "GL error: " + i, localThrowable);
    }
  }
  
  private static void convertCoordinate(RectF paramRectF1, RectF paramRectF2, BasicTexture paramBasicTexture)
  {
    int i = paramBasicTexture.getWidth();
    int j = paramBasicTexture.getHeight();
    int k = paramBasicTexture.getTextureWidth();
    int m = paramBasicTexture.getTextureHeight();
    paramRectF1.left /= k;
    paramRectF1.right /= k;
    paramRectF1.top /= m;
    paramRectF1.bottom /= m;
    float f1 = i / k;
    if (paramRectF1.right > f1)
    {
      paramRectF2.right = (paramRectF2.left + paramRectF2.width() * (f1 - paramRectF1.left) / paramRectF1.width());
      paramRectF1.right = f1;
    }
    float f2 = j / m;
    if (paramRectF1.bottom > f2)
    {
      paramRectF2.bottom = (paramRectF2.top + paramRectF2.height() * (f2 - paramRectF1.top) / paramRectF1.height());
      paramRectF1.bottom = f2;
    }
  }
  
  private static void copyTextureCoordinates(BasicTexture paramBasicTexture, RectF paramRectF)
  {
    int i = paramBasicTexture.getWidth();
    int j = paramBasicTexture.getHeight();
    boolean bool = paramBasicTexture.hasBorder();
    int k = 0;
    int m = 0;
    if (bool)
    {
      k = 1;
      m = 1;
      i--;
      j--;
    }
    paramRectF.set(k, m, i, j);
  }
  
  private static FloatBuffer createBuffer(float[] paramArrayOfFloat)
  {
    FloatBuffer localFloatBuffer = ByteBuffer.allocateDirect(4 * paramArrayOfFloat.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
    localFloatBuffer.put(paramArrayOfFloat, 0, paramArrayOfFloat.length).position(0);
    return localFloatBuffer;
  }
  
  private void draw(ShaderParameter[] paramArrayOfShaderParameter, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    setMatrix(paramArrayOfShaderParameter, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    int i = paramArrayOfShaderParameter[0].handle;
    GLES20.glEnableVertexAttribArray(i);
    checkError();
    GLES20.glDrawArrays(paramInt1, 0, paramInt2);
    checkError();
    GLES20.glDisableVertexAttribArray(i);
    checkError();
  }
  
  private void drawTextureRect(BasicTexture paramBasicTexture, RectF paramRectF1, RectF paramRectF2)
  {
    setTextureMatrix(paramRectF1);
    drawTextureRect(paramBasicTexture, this.mTempTextureMatrix, paramRectF2);
  }
  
  private void drawTextureRect(BasicTexture paramBasicTexture, float[] paramArrayOfFloat, RectF paramRectF)
  {
    ShaderParameter[] arrayOfShaderParameter = prepareTexture(paramBasicTexture);
    setPosition(arrayOfShaderParameter, 0);
    GLES20.glUniformMatrix4fv(arrayOfShaderParameter[2].handle, 1, false, paramArrayOfFloat, 0);
    checkError();
    if (paramBasicTexture.isFlippedVertically())
    {
      save(2);
      translate(0.0F, paramRectF.centerY());
      scale(1.0F, -1.0F, 1.0F);
      translate(0.0F, -paramRectF.centerY());
    }
    draw(arrayOfShaderParameter, 5, 4, paramRectF.left, paramRectF.top, paramRectF.width(), paramRectF.height());
    if (paramBasicTexture.isFlippedVertically()) {
      restore();
    }
    this.mCountTextureRect = (1 + this.mCountTextureRect);
  }
  
  private void enableBlending(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      GLES20.glEnable(3042);
      checkError();
      return;
    }
    GLES20.glDisable(3042);
    checkError();
  }
  
  private RawTexture getTargetTexture()
  {
    return (RawTexture)this.mTargetTextures.get(-1 + this.mTargetTextures.size());
  }
  
  private static int loadShader(int paramInt, String paramString)
  {
    int i = GLES20.glCreateShader(paramInt);
    GLES20.glShaderSource(i, paramString);
    checkError();
    GLES20.glCompileShader(i);
    checkError();
    return i;
  }
  
  private void prepareTexture(BasicTexture paramBasicTexture, int paramInt, ShaderParameter[] paramArrayOfShaderParameter)
  {
    GLES20.glUseProgram(paramInt);
    checkError();
    if ((!paramBasicTexture.isOpaque()) || (getAlpha() < 0.95F)) {}
    for (boolean bool = true;; bool = false)
    {
      enableBlending(bool);
      GLES20.glActiveTexture(33984);
      checkError();
      paramBasicTexture.onBind(this);
      GLES20.glBindTexture(paramBasicTexture.getTarget(), paramBasicTexture.getId());
      checkError();
      GLES20.glUniform1i(paramArrayOfShaderParameter[3].handle, 0);
      checkError();
      GLES20.glUniform1f(paramArrayOfShaderParameter[4].handle, getAlpha());
      checkError();
      return;
    }
  }
  
  private ShaderParameter[] prepareTexture(BasicTexture paramBasicTexture)
  {
    ShaderParameter[] arrayOfShaderParameter;
    if (paramBasicTexture.getTarget() == 3553) {
      arrayOfShaderParameter = this.mTextureParameters;
    }
    for (int i = this.mTextureProgram;; i = this.mOesTextureProgram)
    {
      prepareTexture(paramBasicTexture, i, arrayOfShaderParameter);
      return arrayOfShaderParameter;
      arrayOfShaderParameter = this.mOesTextureParameters;
    }
  }
  
  private void setMatrix(ShaderParameter[] paramArrayOfShaderParameter, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    Matrix.translateM(this.mTempMatrix, 0, this.mMatrices, this.mCurrentMatrixIndex, paramFloat1, paramFloat2, 0.0F);
    Matrix.scaleM(this.mTempMatrix, 0, paramFloat3, paramFloat4, 1.0F);
    Matrix.multiplyMM(this.mTempMatrix, 16, this.mProjectionMatrix, 0, this.mTempMatrix, 0);
    GLES20.glUniformMatrix4fv(paramArrayOfShaderParameter[1].handle, 1, false, this.mTempMatrix, 16);
    checkError();
  }
  
  private void setPosition(ShaderParameter[] paramArrayOfShaderParameter, int paramInt)
  {
    GLES20.glBindBuffer(34962, this.mBoxCoordinates);
    checkError();
    GLES20.glVertexAttribPointer(paramArrayOfShaderParameter[0].handle, 2, 5126, false, 8, paramInt * 8);
    checkError();
    GLES20.glBindBuffer(34962, 0);
    checkError();
  }
  
  private void setTextureMatrix(RectF paramRectF)
  {
    this.mTempTextureMatrix[0] = paramRectF.width();
    this.mTempTextureMatrix[5] = paramRectF.height();
    this.mTempTextureMatrix[12] = paramRectF.left;
    this.mTempTextureMatrix[13] = paramRectF.top;
  }
  
  private int uploadBuffer(Buffer paramBuffer, int paramInt)
  {
    mGLId.glGenBuffers(1, this.mTempIntArray, 0);
    checkError();
    int i = this.mTempIntArray[0];
    GLES20.glBindBuffer(34962, i);
    checkError();
    GLES20.glBufferData(34962, paramInt * paramBuffer.capacity(), paramBuffer, 35044);
    checkError();
    return i;
  }
  
  public void clearBuffer()
  {
    GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
    checkError();
    GLES20.glClear(16384);
    checkError();
  }
  
  public void drawTexture(BasicTexture paramBasicTexture, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
      return;
    }
    copyTextureCoordinates(paramBasicTexture, this.mTempSourceRect);
    this.mTempTargetRect.set(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
    convertCoordinate(this.mTempSourceRect, this.mTempTargetRect, paramBasicTexture);
    drawTextureRect(paramBasicTexture, this.mTempSourceRect, this.mTempTargetRect);
  }
  
  public void drawTexture(BasicTexture paramBasicTexture, RectF paramRectF1, RectF paramRectF2)
  {
    if ((paramRectF2.width() <= 0.0F) || (paramRectF2.height() <= 0.0F)) {
      return;
    }
    this.mTempSourceRect.set(paramRectF1);
    this.mTempTargetRect.set(paramRectF2);
    convertCoordinate(this.mTempSourceRect, this.mTempTargetRect, paramBasicTexture);
    drawTextureRect(paramBasicTexture, this.mTempSourceRect, this.mTempTargetRect);
  }
  
  public float getAlpha()
  {
    return this.mAlphas[this.mCurrentAlphaIndex];
  }
  
  public GLId getGLId()
  {
    return mGLId;
  }
  
  public void initializeTexture(BasicTexture paramBasicTexture, Bitmap paramBitmap)
  {
    int i = paramBasicTexture.getTarget();
    GLES20.glBindTexture(i, paramBasicTexture.getId());
    checkError();
    GLUtils.texImage2D(i, 0, paramBitmap, 0);
  }
  
  public void initializeTextureSize(BasicTexture paramBasicTexture, int paramInt1, int paramInt2)
  {
    int i = paramBasicTexture.getTarget();
    GLES20.glBindTexture(i, paramBasicTexture.getId());
    checkError();
    GLES20.glTexImage2D(i, 0, paramInt1, paramBasicTexture.getTextureWidth(), paramBasicTexture.getTextureHeight(), 0, paramInt1, paramInt2, null);
  }
  
  public void restore()
  {
    int i = 1;
    int j = this.mSaveFlags.removeLast();
    int k;
    if ((j & 0x1) == i)
    {
      k = i;
      if (k != 0) {
        this.mCurrentAlphaIndex = (-1 + this.mCurrentAlphaIndex);
      }
      if ((j & 0x2) != 2) {
        break label61;
      }
    }
    for (;;)
    {
      if (i != 0) {
        this.mCurrentMatrixIndex = (-16 + this.mCurrentMatrixIndex);
      }
      return;
      k = 0;
      break;
      label61:
      i = 0;
    }
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (paramFloat1 == 0.0F) {
      return;
    }
    float[] arrayOfFloat1 = this.mTempMatrix;
    Matrix.setRotateM(arrayOfFloat1, 0, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    float[] arrayOfFloat2 = this.mMatrices;
    int i = this.mCurrentMatrixIndex;
    Matrix.multiplyMM(arrayOfFloat1, 16, arrayOfFloat2, i, arrayOfFloat1, 0);
    System.arraycopy(arrayOfFloat1, 16, arrayOfFloat2, i, 16);
  }
  
  public void save(int paramInt)
  {
    int i = 1;
    int j;
    if ((paramInt & 0x1) == i)
    {
      j = i;
      if (j != 0)
      {
        float f = getAlpha();
        this.mCurrentAlphaIndex = (1 + this.mCurrentAlphaIndex);
        if (this.mAlphas.length <= this.mCurrentAlphaIndex) {
          this.mAlphas = Arrays.copyOf(this.mAlphas, 2 * this.mAlphas.length);
        }
        this.mAlphas[this.mCurrentAlphaIndex] = f;
      }
      if ((paramInt & 0x2) != 2) {
        break label163;
      }
    }
    for (;;)
    {
      if (i != 0)
      {
        int k = this.mCurrentMatrixIndex;
        this.mCurrentMatrixIndex = (16 + this.mCurrentMatrixIndex);
        if (this.mMatrices.length <= this.mCurrentMatrixIndex) {
          this.mMatrices = Arrays.copyOf(this.mMatrices, 2 * this.mMatrices.length);
        }
        System.arraycopy(this.mMatrices, k, this.mMatrices, this.mCurrentMatrixIndex, 16);
      }
      this.mSaveFlags.add(paramInt);
      return;
      j = 0;
      break;
      label163:
      i = 0;
    }
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    Matrix.scaleM(this.mMatrices, this.mCurrentMatrixIndex, paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void setSize(int paramInt1, int paramInt2)
  {
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
    GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
    checkError();
    Matrix.setIdentityM(this.mMatrices, this.mCurrentMatrixIndex);
    Matrix.orthoM(this.mProjectionMatrix, 0, 0.0F, paramInt1, 0.0F, paramInt2, -1.0F, 1.0F);
    if (getTargetTexture() == null)
    {
      this.mScreenWidth = paramInt1;
      this.mScreenHeight = paramInt2;
      Matrix.translateM(this.mMatrices, this.mCurrentMatrixIndex, 0.0F, paramInt2, 0.0F);
      Matrix.scaleM(this.mMatrices, this.mCurrentMatrixIndex, 1.0F, -1.0F, 1.0F);
    }
  }
  
  public void setTextureParameters(BasicTexture paramBasicTexture)
  {
    int i = paramBasicTexture.getTarget();
    GLES20.glBindTexture(i, paramBasicTexture.getId());
    checkError();
    GLES20.glTexParameteri(i, 10242, 33071);
    GLES20.glTexParameteri(i, 10243, 33071);
    GLES20.glTexParameterf(i, 10241, 9729.0F);
    GLES20.glTexParameterf(i, 10240, 9729.0F);
  }
  
  public void texSubImage2D(BasicTexture paramBasicTexture, int paramInt1, int paramInt2, Bitmap paramBitmap, int paramInt3, int paramInt4)
  {
    int i = paramBasicTexture.getTarget();
    GLES20.glBindTexture(i, paramBasicTexture.getId());
    checkError();
    GLUtils.texSubImage2D(i, 0, paramInt1, paramInt2, paramBitmap, paramInt3, paramInt4);
  }
  
  public void translate(float paramFloat1, float paramFloat2)
  {
    int i = this.mCurrentMatrixIndex;
    float[] arrayOfFloat = this.mMatrices;
    int j = i + 12;
    arrayOfFloat[j] += paramFloat1 * arrayOfFloat[(i + 0)] + paramFloat2 * arrayOfFloat[(i + 4)];
    int k = i + 13;
    arrayOfFloat[k] += paramFloat1 * arrayOfFloat[(i + 1)] + paramFloat2 * arrayOfFloat[(i + 5)];
    int m = i + 14;
    arrayOfFloat[m] += paramFloat1 * arrayOfFloat[(i + 2)] + paramFloat2 * arrayOfFloat[(i + 6)];
    int n = i + 15;
    arrayOfFloat[n] += paramFloat1 * arrayOfFloat[(i + 3)] + paramFloat2 * arrayOfFloat[(i + 7)];
  }
  
  public boolean unloadTexture(BasicTexture paramBasicTexture)
  {
    boolean bool = paramBasicTexture.isLoaded();
    if (bool) {
      synchronized (this.mUnboundTextures)
      {
        this.mUnboundTextures.add(paramBasicTexture.getId());
        return bool;
      }
    }
    return bool;
  }
  
  public int uploadBuffer(FloatBuffer paramFloatBuffer)
  {
    return uploadBuffer(paramFloatBuffer, 4);
  }
  
  private static class AttributeShaderParameter
    extends GLES20Canvas.ShaderParameter
  {
    public AttributeShaderParameter(String paramString)
    {
      super();
    }
    
    public void loadHandle(int paramInt)
    {
      this.handle = GLES20.glGetAttribLocation(paramInt, this.mName);
      GLES20Canvas.checkError();
    }
  }
  
  private static abstract class ShaderParameter
  {
    public int handle;
    protected final String mName;
    
    public ShaderParameter(String paramString)
    {
      this.mName = paramString;
    }
    
    public abstract void loadHandle(int paramInt);
  }
  
  private static class UniformShaderParameter
    extends GLES20Canvas.ShaderParameter
  {
    public UniformShaderParameter(String paramString)
    {
      super();
    }
    
    public void loadHandle(int paramInt)
    {
      this.handle = GLES20.glGetUniformLocation(paramInt, this.mName);
      GLES20Canvas.checkError();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.glrenderer.GLES20Canvas
 * JD-Core Version:    0.7.0.1
 */