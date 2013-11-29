package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import javax.annotation.Nullable;

public abstract class AbstractThreeLineRemoteViewsAdapter<T extends EntryCardViewAdapter>
  extends BaseEntryRemoteViewsAdapter<T>
{
  private final WidgetImageLoader mImageLoader;
  
  protected AbstractThreeLineRemoteViewsAdapter(T paramT, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramT);
    this.mImageLoader = paramWidgetImageLoader;
  }
  
  private RemoteViews createRemoteViewInternal(Context paramContext, boolean paramBoolean)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    setLineOne(localRemoteViews, paramContext);
    setLineTwo(localRemoteViews, paramContext);
    setLineThree(localRemoteViews, paramContext);
    if (paramBoolean)
    {
      String str = getImageUrl(paramContext);
      if (!TextUtils.isEmpty(str))
      {
        Uri localUri = Uri.parse(str);
        this.mImageLoader.loadImageUri(paramContext, localRemoteViews, 2131297263, localUri, null);
        localRemoteViews.setViewVisibility(2131297263, 0);
      }
    }
    return localRemoteViews;
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext, false);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext, true);
  }
  
  protected abstract CharSequence getFirstLine(Context paramContext);
  
  @Nullable
  protected abstract String getImageUrl(Context paramContext);
  
  @Nullable
  protected abstract CharSequence getSecondLine(Context paramContext);
  
  @Nullable
  protected abstract CharSequence getThirdLine(Context paramContext);
  
  protected void setLineOne(RemoteViews paramRemoteViews, Context paramContext)
  {
    paramRemoteViews.setTextViewText(2131297260, getFirstLine(paramContext));
  }
  
  protected void setLineThree(RemoteViews paramRemoteViews, Context paramContext)
  {
    CharSequence localCharSequence = getThirdLine(paramContext);
    if (!TextUtils.isEmpty(localCharSequence))
    {
      paramRemoteViews.setTextViewText(2131297262, localCharSequence);
      paramRemoteViews.setViewVisibility(2131297262, 0);
    }
  }
  
  protected void setLineTwo(RemoteViews paramRemoteViews, Context paramContext)
  {
    CharSequence localCharSequence = getSecondLine(paramContext);
    if (!TextUtils.isEmpty(localCharSequence))
    {
      paramRemoteViews.setTextViewText(2131297261, localCharSequence);
      return;
    }
    paramRemoteViews.setViewVisibility(2131297261, 4);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.AbstractThreeLineRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */