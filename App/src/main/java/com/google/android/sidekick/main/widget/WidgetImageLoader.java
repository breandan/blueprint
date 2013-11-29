package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.RemoteViews;
import com.google.android.shared.util.CancellableNowOrLater;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.main.inject.WidgetManager;

public class WidgetImageLoader
{
  private final UriLoader<Drawable> mImageLoader;
  private final WidgetManager mWidgetManager;
  
  public WidgetImageLoader(UriLoader<Drawable> paramUriLoader, WidgetManager paramWidgetManager)
  {
    this.mImageLoader = paramUriLoader;
    this.mWidgetManager = paramWidgetManager;
  }
  
  void loadImageUri(Context paramContext, RemoteViews paramRemoteViews, int paramInt, Uri paramUri, Rect paramRect)
  {
    CancellableNowOrLater localCancellableNowOrLater = this.mImageLoader.load(paramUri);
    if (localCancellableNowOrLater.haveNow())
    {
      Drawable localDrawable = (Drawable)localCancellableNowOrLater.getNow();
      Bitmap localBitmap;
      if (localDrawable != null)
      {
        localBitmap = ((BitmapDrawable)localDrawable).getBitmap();
        if (paramRect == null) {
          break label92;
        }
        paramRemoteViews.setImageViewBitmap(paramInt, Bitmap.createBitmap(localBitmap, paramRect.left, paramRect.top, paramRect.width(), paramRect.height()));
      }
      for (;;)
      {
        paramRemoteViews.setViewVisibility(paramInt, 0);
        return;
        label92:
        paramRemoteViews.setImageViewBitmap(paramInt, localBitmap);
      }
    }
    localCancellableNowOrLater.getLater(new Consumer()
    {
      public boolean consume(Drawable paramAnonymousDrawable)
      {
        if (paramAnonymousDrawable != null) {
          WidgetImageLoader.this.mWidgetManager.updateWidget();
        }
        return true;
      }
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.WidgetImageLoader
 * JD-Core Version:    0.7.0.1
 */