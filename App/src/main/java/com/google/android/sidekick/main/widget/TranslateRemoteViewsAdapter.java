package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.content.res.Resources;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.TranslateEntryAdapter;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.TranslateEntry;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;

public class TranslateRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<TranslateEntryAdapter>
{
  public TranslateRemoteViewsAdapter(TranslateEntryAdapter paramTranslateEntryAdapter)
  {
    super(paramTranslateEntryAdapter);
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    Sidekick.TranslateEntry localTranslateEntry1 = ((TranslateEntryAdapter)getEntryCardViewAdapter()).getEntry().getTranslateEntry();
    Sidekick.TranslateEntry localTranslateEntry2;
    if (!localTranslateEntry1.hasTargetText()) {
      localTranslateEntry2 = new Sidekick.TranslateEntry();
    }
    try
    {
      localTranslateEntry2.mergeFrom(localTranslateEntry1.toByteArray());
      localTranslateEntry2.setTargetText("Bienvenido");
      localTranslateEntry2.setSourceText("Welcome");
      label70:
      localRemoteViews.setTextViewText(2131297260, localTranslateEntry1.getTargetText());
      localRemoteViews.setTextColor(2131297260, paramContext.getResources().getColor(2131230903));
      localRemoteViews.setTextViewText(2131297261, localTranslateEntry1.getSourceText());
      return localRemoteViews;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      break label70;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.TranslateRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */