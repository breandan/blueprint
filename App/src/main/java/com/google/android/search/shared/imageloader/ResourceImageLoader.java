package com.google.android.search.shared.imageloader;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import com.google.android.shared.util.SynchronousLoader;
import com.google.android.shared.util.Util;
import java.io.FileNotFoundException;

public class ResourceImageLoader
  extends SynchronousLoader<Drawable>
{
  private final Context mContext;
  
  public ResourceImageLoader(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  public void clearCache() {}
  
  public Drawable loadNow(Uri paramUri)
  {
    if (paramUri == null) {}
    for (;;)
    {
      return null;
      try
      {
        int i = Integer.parseInt(paramUri.toString());
        if (i != 0)
        {
          Drawable localDrawable2 = this.mContext.getResources().getDrawable(i);
          return localDrawable2;
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        try
        {
          Drawable localDrawable1 = Util.loadDrawableResource(this.mContext, paramUri);
          return localDrawable1;
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          Log.e("Search.ResourceImageLoader", "Failed to load icon " + paramUri, localFileNotFoundException);
          return null;
        }
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        Log.e("Search.ResourceImageLoader", "Failed to load icon resource: " + paramUri);
      }
    }
    return null;
  }
  
  public boolean supportsUri(Uri paramUri)
  {
    try
    {
      Integer.parseInt(paramUri.toString());
      return true;
    }
    catch (NumberFormatException localNumberFormatException) {}
    return "android.resource".equals(paramUri.getScheme());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.imageloader.ResourceImageLoader
 * JD-Core Version:    0.7.0.1
 */