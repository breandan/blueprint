package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.StockListEntryAdapter;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.StockQuote;
import com.google.geo.sidekick.Sidekick.StockQuoteListEntry;
import java.text.NumberFormat;

public class StockQuoteRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<StockListEntryAdapter>
{
  public StockQuoteRemoteViewsAdapter(StockListEntryAdapter paramStockListEntryAdapter)
  {
    super(paramStockListEntryAdapter);
  }
  
  private RemoteViews createRemoteViewInternal(Context paramContext, boolean paramBoolean)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968843);
    Sidekick.StockQuote localStockQuote = ((StockListEntryAdapter)getEntryCardViewAdapter()).getEntry().getStockQuoteListEntry().getStockQuoteEntry(0);
    localRemoteViews.setTextViewText(2131297050, ((StockListEntryAdapter)getEntryCardViewAdapter()).getTitle(paramContext, localStockQuote));
    if (localStockQuote.hasLastPrice())
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Float.valueOf(localStockQuote.getLastPrice());
      localRemoteViews.setTextViewText(2131297051, String.format("%.2f", arrayOfObject));
    }
    if (paramBoolean)
    {
      if (localStockQuote.hasPriceVariation())
      {
        localRemoteViews.setTextViewText(2131297052, StockListEntryAdapter.formatNumber(localStockQuote.getPriceVariation(), NumberFormat.getNumberInstance()));
        localRemoteViews.setTextColor(2131297052, StockListEntryAdapter.getColorForPriceVariation(paramContext, localStockQuote.getPriceVariation()));
        localRemoteViews.setViewVisibility(2131297052, 0);
      }
      if (localStockQuote.hasPriceVariationPercent())
      {
        localRemoteViews.setTextViewText(2131297053, StockListEntryAdapter.formatPercent(localStockQuote.getPriceVariationPercent()));
        localRemoteViews.setTextColor(2131297053, StockListEntryAdapter.getColorForPriceVariation(paramContext, localStockQuote.getPriceVariationPercent()));
        localRemoteViews.setViewVisibility(2131297053, 0);
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
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.StockQuoteRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */