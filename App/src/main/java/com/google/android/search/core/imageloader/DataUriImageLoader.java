package com.google.android.search.core.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import com.google.android.shared.util.SynchronousLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUriImageLoader
  extends SynchronousLoader<Drawable>
{
  private static final Pattern BASE64_IMAGE_URI_PATTERN = Pattern.compile("^(?:.*;)?base64,.*");
  private final Resources mResources;
  
  public DataUriImageLoader(Resources paramResources)
  {
    this.mResources = paramResources;
  }
  
  public static byte[] parseDataUri(Uri paramUri)
  {
    if (!"data".equals(paramUri.getScheme())) {}
    for (;;)
    {
      return null;
      String str = paramUri.getSchemeSpecificPart();
      try
      {
        if (str.startsWith("base64,")) {
          return Base64.decode(str.substring(7), 8);
        }
        if (BASE64_IMAGE_URI_PATTERN.matcher(str).matches())
        {
          byte[] arrayOfByte = Base64.decode(str.substring(str.indexOf("base64,") + "base64,".length()), 0);
          return arrayOfByte;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Log.e("Search.DataUriImageLoader", "Mailformed data URI: " + localIllegalArgumentException);
      }
    }
    return null;
  }
  
  public void clearCache() {}
  
  public Drawable loadNow(Uri paramUri)
  {
    byte[] arrayOfByte = parseDataUri(paramUri);
    if ((arrayOfByte == null) || (arrayOfByte.length == 0)) {
      return null;
    }
    Bitmap localBitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    return new BitmapDrawable(this.mResources, localBitmap);
  }
  
  public boolean supportsUri(Uri paramUri)
  {
    return "data".equals(paramUri.getScheme());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.imageloader.DataUriImageLoader
 * JD-Core Version:    0.7.0.1
 */