package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.widget.RemoteViews;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.shared.cards.CurrencyExchangeEntryAdapter;
import com.google.geo.sidekick.Sidekick.CurrencyExchangeEntry;
import com.google.geo.sidekick.Sidekick.Entry;
import java.text.NumberFormat;

public class CurrencyExchangeRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<CurrencyExchangeEntryAdapter>
{
  public CurrencyExchangeRemoteViewsAdapter(CurrencyExchangeEntryAdapter paramCurrencyExchangeEntryAdapter)
  {
    super(paramCurrencyExchangeEntryAdapter);
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968647);
    localRemoteViews.setTextViewText(2131296498, Html.fromHtml(((CurrencyExchangeEntryAdapter)getEntryCardViewAdapter()).getEntry().getCurrencyExchangeEntry().getTitle()));
    return localRemoteViews;
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    Sidekick.CurrencyExchangeEntry localCurrencyExchangeEntry = ((CurrencyExchangeEntryAdapter)getEntryCardViewAdapter()).getEntry().getCurrencyExchangeEntry();
    localRemoteViews.setTextViewText(2131297261, Html.fromHtml(localCurrencyExchangeEntry.getTitle()));
    float f = 10.0F / localCurrencyExchangeEntry.getLocalToHomeRate();
    NumberFormat localNumberFormat = NumberFormat.getInstance();
    localNumberFormat.setMinimumFractionDigits(2);
    localNumberFormat.setMaximumFractionDigits(2);
    Resources localResources = paramContext.getResources();
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = localNumberFormat.format(10L);
    arrayOfObject[1] = BidiUtils.unicodeWrap(localCurrencyExchangeEntry.getHomeCurrency());
    arrayOfObject[2] = localNumberFormat.format(f);
    arrayOfObject[3] = BidiUtils.unicodeWrap(localCurrencyExchangeEntry.getLocalCurrency());
    localRemoteViews.setTextViewText(2131297260, localResources.getString(2131362706, arrayOfObject));
    localRemoteViews.setTextColor(2131297260, paramContext.getResources().getColor(2131230903));
    return localRemoteViews;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.CurrencyExchangeRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */