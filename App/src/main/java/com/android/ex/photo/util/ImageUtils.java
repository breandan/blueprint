package com.android.ex.photo.util;

import android.content.ContentResolver;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Base64;
import android.util.Log;
import com.android.ex.photo.PhotoViewActivity;
import com.android.ex.photo.loaders.PhotoBitmapLoaderInterface.BitmapResult;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtils
{
  private static final Pattern BASE64_IMAGE_URI_PATTERN = Pattern.compile("^(?:.*;)?base64,.*");
  public static final ImageSize sUseImageSize = ImageSize.EXTRA_SMALL;
  
  static
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      sUseImageSize = ImageSize.NORMAL;
      return;
    }
    if (PhotoViewActivity.sMemoryClass >= 32L)
    {
      sUseImageSize = ImageSize.NORMAL;
      return;
    }
    if (PhotoViewActivity.sMemoryClass >= 24L)
    {
      sUseImageSize = ImageSize.SMALL;
      return;
    }
  }
  
  private static InputStreamFactory createInputStreamFactory(ContentResolver paramContentResolver, Uri paramUri)
  {
    if ("data".equals(paramUri.getScheme())) {
      return new DataInputStreamFactory(paramContentResolver, paramUri);
    }
    return new BaseInputStreamFactory(paramContentResolver, paramUri);
  }
  
  public static PhotoBitmapLoaderInterface.BitmapResult createLocalBitmap(ContentResolver paramContentResolver, Uri paramUri, int paramInt)
  {
    PhotoBitmapLoaderInterface.BitmapResult localBitmapResult = new PhotoBitmapLoaderInterface.BitmapResult();
    InputStreamFactory localInputStreamFactory = createInputStreamFactory(paramContentResolver, paramUri);
    try
    {
      Point localPoint = getImageBounds(localInputStreamFactory);
      if (localPoint == null)
      {
        localBitmapResult.status = 1;
        return localBitmapResult;
      }
      BitmapFactory.Options localOptions = new BitmapFactory.Options();
      localOptions.inSampleSize = Math.max(localPoint.x / paramInt, localPoint.y / paramInt);
      localBitmapResult.bitmap = decodeStream(localInputStreamFactory, null, localOptions);
      localBitmapResult.status = 0;
      return localBitmapResult;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      return localBitmapResult;
    }
    catch (IOException localIOException)
    {
      localBitmapResult.status = 1;
      return localBitmapResult;
    }
    catch (SecurityException localSecurityException)
    {
      localBitmapResult.status = 1;
      return localBitmapResult;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    return localBitmapResult;
  }
  
  /* Error */
  public static android.graphics.Bitmap decodeStream(InputStreamFactory paramInputStreamFactory, android.graphics.Rect paramRect, BitmapFactory.Options paramOptions)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_0
    //   3: invokeinterface 134 1 0
    //   8: astore_3
    //   9: aload_3
    //   10: ldc2_w 135
    //   13: invokestatic 142	com/android/ex/photo/util/Exif:getOrientation	(Ljava/io/InputStream;J)I
    //   16: istore 13
    //   18: aload_3
    //   19: invokevirtual 147	java/io/InputStream:close	()V
    //   22: aload_0
    //   23: invokeinterface 134 1 0
    //   28: astore_3
    //   29: aload_3
    //   30: aload_1
    //   31: aload_2
    //   32: invokestatic 152	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   35: astore 11
    //   37: aload_3
    //   38: ifnull +59 -> 97
    //   41: aload 11
    //   43: ifnonnull +54 -> 97
    //   46: aload_2
    //   47: getfield 156	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   50: ifne +47 -> 97
    //   53: ldc 158
    //   55: ldc 160
    //   57: invokestatic 166	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   60: pop
    //   61: new 168	java/lang/UnsupportedOperationException
    //   64: dup
    //   65: ldc 170
    //   67: invokespecial 173	java/lang/UnsupportedOperationException:<init>	(Ljava/lang/String;)V
    //   70: athrow
    //   71: astore 9
    //   73: ldc 158
    //   75: ldc 175
    //   77: aload 9
    //   79: invokestatic 179	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   82: pop
    //   83: aload_3
    //   84: ifnull +7 -> 91
    //   87: aload_3
    //   88: invokevirtual 147	java/io/InputStream:close	()V
    //   91: aconst_null
    //   92: astore 11
    //   94: aload 11
    //   96: areturn
    //   97: aload 11
    //   99: ifnull +68 -> 167
    //   102: iload 13
    //   104: ifeq +63 -> 167
    //   107: new 181	android/graphics/Matrix
    //   110: dup
    //   111: invokespecial 182	android/graphics/Matrix:<init>	()V
    //   114: astore 15
    //   116: aload 15
    //   118: iload 13
    //   120: i2f
    //   121: invokevirtual 186	android/graphics/Matrix:postRotate	(F)Z
    //   124: pop
    //   125: aload 11
    //   127: iconst_0
    //   128: iconst_0
    //   129: aload 11
    //   131: invokevirtual 192	android/graphics/Bitmap:getWidth	()I
    //   134: aload 11
    //   136: invokevirtual 195	android/graphics/Bitmap:getHeight	()I
    //   139: aload 15
    //   141: iconst_1
    //   142: invokestatic 199	android/graphics/Bitmap:createBitmap	(Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
    //   145: astore 17
    //   147: aload 17
    //   149: astore 11
    //   151: aload_3
    //   152: ifnull -58 -> 94
    //   155: aload_3
    //   156: invokevirtual 147	java/io/InputStream:close	()V
    //   159: aload 11
    //   161: areturn
    //   162: astore 18
    //   164: aload 11
    //   166: areturn
    //   167: aload_3
    //   168: ifnull -74 -> 94
    //   171: aload_3
    //   172: invokevirtual 147	java/io/InputStream:close	()V
    //   175: aload 11
    //   177: areturn
    //   178: astore 14
    //   180: aload 11
    //   182: areturn
    //   183: astore 6
    //   185: ldc 158
    //   187: ldc 201
    //   189: aload 6
    //   191: invokestatic 179	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   194: pop
    //   195: aload_3
    //   196: ifnull +7 -> 203
    //   199: aload_3
    //   200: invokevirtual 147	java/io/InputStream:close	()V
    //   203: aconst_null
    //   204: areturn
    //   205: astore 4
    //   207: aload_3
    //   208: ifnull +7 -> 215
    //   211: aload_3
    //   212: invokevirtual 147	java/io/InputStream:close	()V
    //   215: aload 4
    //   217: athrow
    //   218: astore 12
    //   220: goto -129 -> 91
    //   223: astore 8
    //   225: goto -22 -> 203
    //   228: astore 5
    //   230: goto -15 -> 215
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	233	0	paramInputStreamFactory	InputStreamFactory
    //   0	233	1	paramRect	android.graphics.Rect
    //   0	233	2	paramOptions	BitmapFactory.Options
    //   1	211	3	localInputStream	InputStream
    //   205	11	4	localObject1	Object
    //   228	1	5	localIOException1	IOException
    //   183	7	6	localIOException2	IOException
    //   223	1	8	localIOException3	IOException
    //   71	7	9	localOutOfMemoryError	java.lang.OutOfMemoryError
    //   35	146	11	localObject2	Object
    //   218	1	12	localIOException4	IOException
    //   16	103	13	i	int
    //   178	1	14	localIOException5	IOException
    //   114	26	15	localMatrix	android.graphics.Matrix
    //   145	3	17	localBitmap	android.graphics.Bitmap
    //   162	1	18	localIOException6	IOException
    // Exception table:
    //   from	to	target	type
    //   2	37	71	java/lang/OutOfMemoryError
    //   46	71	71	java/lang/OutOfMemoryError
    //   107	147	71	java/lang/OutOfMemoryError
    //   155	159	162	java/io/IOException
    //   171	175	178	java/io/IOException
    //   2	37	183	java/io/IOException
    //   46	71	183	java/io/IOException
    //   107	147	183	java/io/IOException
    //   2	37	205	finally
    //   46	71	205	finally
    //   73	83	205	finally
    //   107	147	205	finally
    //   185	195	205	finally
    //   87	91	218	java/io/IOException
    //   199	203	223	java/io/IOException
    //   211	215	228	java/io/IOException
  }
  
  private static Point getImageBounds(InputStreamFactory paramInputStreamFactory)
    throws IOException
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inJustDecodeBounds = true;
    decodeStream(paramInputStreamFactory, null, localOptions);
    return new Point(localOptions.outWidth, localOptions.outHeight);
  }
  
  private static class BaseInputStreamFactory
    implements ImageUtils.InputStreamFactory
  {
    protected final ContentResolver mResolver;
    protected final Uri mUri;
    
    public BaseInputStreamFactory(ContentResolver paramContentResolver, Uri paramUri)
    {
      this.mResolver = paramContentResolver;
      this.mUri = paramUri;
    }
    
    public InputStream createInputStream()
      throws FileNotFoundException
    {
      return this.mResolver.openInputStream(this.mUri);
    }
  }
  
  private static class DataInputStreamFactory
    extends ImageUtils.BaseInputStreamFactory
  {
    private byte[] mData;
    
    public DataInputStreamFactory(ContentResolver paramContentResolver, Uri paramUri)
    {
      super(paramUri);
    }
    
    private byte[] parseDataUri(Uri paramUri)
    {
      String str = paramUri.getSchemeSpecificPart();
      try
      {
        if (str.startsWith("base64,")) {
          return Base64.decode(str.substring("base64,".length()), 8);
        }
        if (ImageUtils.BASE64_IMAGE_URI_PATTERN.matcher(str).matches())
        {
          byte[] arrayOfByte = Base64.decode(str.substring(str.indexOf("base64,") + "base64,".length()), 0);
          return arrayOfByte;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Log.e("ImageUtils", "Mailformed data URI: " + localIllegalArgumentException);
      }
      return null;
    }
    
    public InputStream createInputStream()
      throws FileNotFoundException
    {
      if (this.mData == null)
      {
        this.mData = parseDataUri(this.mUri);
        if (this.mData == null) {
          return super.createInputStream();
        }
      }
      return new ByteArrayInputStream(this.mData);
    }
  }
  
  public static enum ImageSize
  {
    static
    {
      NORMAL = new ImageSize("NORMAL", 2);
      ImageSize[] arrayOfImageSize = new ImageSize[3];
      arrayOfImageSize[0] = EXTRA_SMALL;
      arrayOfImageSize[1] = SMALL;
      arrayOfImageSize[2] = NORMAL;
      $VALUES = arrayOfImageSize;
    }
    
    private ImageSize() {}
  }
  
  public static abstract interface InputStreamFactory
  {
    public abstract InputStream createInputStream()
      throws FileNotFoundException;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.util.ImageUtils
 * JD-Core Version:    0.7.0.1
 */