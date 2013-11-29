package com.google.android.search.shared.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.google.android.shared.util.CancellableNowOrLater;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.NowOrLater;
import com.google.android.shared.util.UriLoader;
import com.google.android.shared.util.Util;
import com.google.common.base.Objects;

public class AsyncIcon
{
  private Uri mCurrentId;
  private final ImageView mView;
  private Uri mWantedId;
  
  public AsyncIcon(ImageView paramImageView)
  {
    this.mView = paramImageView;
  }
  
  private void handleNewDrawable(Drawable paramDrawable, Uri paramUri, String paramString, UriLoader<Drawable> paramUriLoader)
  {
    if (paramDrawable != null)
    {
      setDrawable(paramDrawable, paramUri);
      return;
    }
    if (paramString != null)
    {
      set(paramString, Util.toResourceUriString(this.mView.getContext().getPackageName(), 2130838047), paramUriLoader);
      return;
    }
    clearDrawable();
  }
  
  private void setDrawable(Drawable paramDrawable, Uri paramUri)
  {
    this.mCurrentId = paramUri;
    this.mView.setImageDrawable(paramDrawable);
    if (paramDrawable == null)
    {
      this.mView.setVisibility(4);
      return;
    }
    this.mView.setVisibility(0);
    paramDrawable.setVisible(false, false);
    paramDrawable.setVisible(true, false);
  }
  
  public void clearDrawable()
  {
    setDrawable(null, null);
  }
  
  public void set(String paramString1, final String paramString2, final UriLoader<Drawable> paramUriLoader)
  {
    if (paramString1 != null)
    {
      final Uri localUri = Uri.parse(paramString1);
      this.mWantedId = localUri;
      CancellableNowOrLater localCancellableNowOrLater;
      if (!Objects.equal(this.mWantedId, this.mCurrentId))
      {
        localCancellableNowOrLater = paramUriLoader.load(localUri);
        if (localCancellableNowOrLater.haveNow()) {
          handleNewDrawable((Drawable)localCancellableNowOrLater.getNow(), localUri, paramString2, paramUriLoader);
        }
      }
      else
      {
        return;
      }
      clearDrawable();
      localCancellableNowOrLater.getLater(new Consumer()
      {
        public boolean consume(Drawable paramAnonymousDrawable)
        {
          if (Objects.equal(localUri, AsyncIcon.this.mWantedId))
          {
            AsyncIcon.this.handleNewDrawable(paramAnonymousDrawable, localUri, paramString2, paramUriLoader);
            return true;
          }
          return false;
        }
      });
      return;
    }
    this.mWantedId = null;
    handleNewDrawable(null, null, paramString2, paramUriLoader);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.AsyncIcon
 * JD-Core Version:    0.7.0.1
 */