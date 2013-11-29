package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.StockQuote;
import com.google.geo.sidekick.Sidekick.StockQuoteListEntry;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StockListEntryAdapter
  extends BaseEntryAdapter
{
  static final int INITIAL_LIST_SIZE = 5;
  private final Clock mClock;
  private final Sidekick.StockQuoteListEntry mStocksListEntry;
  
  StockListEntryAdapter(Sidekick.Entry paramEntry, Clock paramClock, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mStocksListEntry = paramEntry.getStockQuoteListEntry();
    this.mClock = paramClock;
  }
  
  private void addBottomDivider(View paramView)
  {
    ((TableLayout)paramView.findViewById(2131297060)).setShowDividers(7);
  }
  
  private void addStocks(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, View paramView, List<StockQuoteIndex> paramList, int paramInt1, int paramInt2)
  {
    TableLayout localTableLayout = (TableLayout)paramView.findViewById(2131297060);
    NumberFormat localNumberFormat = NumberFormat.getNumberInstance();
    for (int i = paramInt1; i < paramInt2; i++)
    {
      StockQuoteIndex localStockQuoteIndex = (StockQuoteIndex)paramList.get(i);
      View localView = paramLayoutInflater.inflate(2130968844, localTableLayout, false);
      ((TextView)localView.findViewById(2131297054)).setText(stockShortNameOrSymbol(localStockQuoteIndex.mStockQuote));
      setStockValues(paramContext, paramPredictiveCardContainer, localStockQuoteIndex.mStockQuote, localView, localNumberFormat);
      localTableLayout.addView(localView);
    }
  }
  
  public static String formatNumber(float paramFloat, NumberFormat paramNumberFormat)
  {
    if (Math.abs(paramFloat) >= 10000.0F) {
      paramNumberFormat.setMaximumFractionDigits(0);
    }
    for (;;)
    {
      return BidiUtils.formatAndUnicodeWrapNumber(paramNumberFormat, paramFloat);
      paramNumberFormat.setMinimumFractionDigits(2);
      paramNumberFormat.setMaximumFractionDigits(2);
    }
  }
  
  public static String formatPercent(double paramDouble)
  {
    return BidiUtils.formatAndUnicodeWrapPercent(paramDouble, 2, 2);
  }
  
  public static int getColorForPriceVariation(Context paramContext, double paramDouble)
  {
    if (paramDouble >= 0.0D) {}
    for (int i = 2131230844;; i = 2131230843) {
      return paramContext.getResources().getColor(i);
    }
  }
  
  private String getStockChartUrl(Context paramContext, Sidekick.StockQuote paramStockQuote)
  {
    int i = (int)paramContext.getResources().getDimension(2131689738);
    int j = (int)paramContext.getResources().getDimension(2131689737);
    int k = 1;
    int m = paramContext.getResources().getDisplayMetrics().densityDpi;
    if ((m == 240) || (m == 320) || (m == 480))
    {
      k = 2;
      i /= 2;
      j /= 2;
    }
    Locale localLocale = Locale.US;
    String str = paramStockQuote.getChartUrl();
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = Integer.valueOf(i);
    arrayOfObject[1] = Integer.valueOf(j);
    arrayOfObject[2] = Integer.valueOf(k);
    return String.format(localLocale, str, arrayOfObject);
  }
  
  private String getSubtitle(Context paramContext, Sidekick.StockQuote paramStockQuote)
  {
    long l = TimeUnit.SECONDS.toMillis(paramStockQuote.getLastUpdateSeconds());
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramStockQuote.getExchange();
    arrayOfObject[1] = DateUtils.formatDateTime(paramContext, l, 17);
    return paramContext.getString(2131362603, arrayOfObject);
  }
  
  private void launchDetails(Sidekick.StockQuote paramStockQuote, Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    paramPredictiveCardContainer.startWebSearch(paramStockQuote.getSymbol() + " " + paramContext.getString(2131362604), null);
  }
  
  private View multiStockView(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, final LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    final View localView = paramLayoutInflater.inflate(2130968846, paramViewGroup, false);
    long l = getLastUpdateTimeMillis(this.mStocksListEntry);
    boolean bool = TimeUtilities.isSameDay(l, this.mClock.currentTimeMillis());
    TextView localTextView1 = (TextView)localView.findViewById(2131296451);
    if (bool) {}
    final ArrayList localArrayList;
    for (int i = 2131362600;; i = 2131362605)
    {
      localTextView1.setText(i);
      TextView localTextView2 = (TextView)localView.findViewById(2131297057);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = DateUtils.formatDateTime(paramContext, l, 17);
      localTextView2.setText(paramContext.getString(2131362602, arrayOfObject));
      localArrayList = Lists.newArrayListWithExpectedSize(this.mStocksListEntry.getStockQuoteEntryCount());
      for (int j = 0;; j++)
      {
        int k = this.mStocksListEntry.getStockQuoteEntryCount();
        if (j >= k) {
          break;
        }
        localArrayList.add(new StockQuoteIndex(this.mStocksListEntry.getStockQuoteEntry(j), j, null));
      }
    }
    addStocks(paramContext, paramPredictiveCardContainer, paramLayoutInflater, localView, localArrayList, 0, Math.min(localArrayList.size(), 5));
    if (localArrayList.size() > 5)
    {
      final Button localButton = (Button)localView.findViewById(2131296316);
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 102)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          localButton.setVisibility(8);
          StockListEntryAdapter.this.addStocks(paramContext, paramPredictiveCardContainer, paramLayoutInflater, localView, localArrayList, 5, localArrayList.size());
        }
      });
      localButton.setVisibility(0);
      addBottomDivider(localView);
    }
    return localView;
  }
  
  private void setStockValues(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, final Sidekick.StockQuote paramStockQuote, View paramView, NumberFormat paramNumberFormat)
  {
    if (paramStockQuote.hasLastPrice()) {
      ((TextView)paramView.findViewById(2131297051)).setText(formatNumber(paramStockQuote.getLastPrice(), paramNumberFormat));
    }
    if (paramStockQuote.hasPriceVariation())
    {
      TextView localTextView2 = (TextView)paramView.findViewById(2131297055);
      localTextView2.setText(formatNumber(paramStockQuote.getPriceVariation(), paramNumberFormat));
      localTextView2.setTextColor(getColorForPriceVariation(paramContext, paramStockQuote.getPriceVariation()));
    }
    if (paramStockQuote.hasPriceVariationPercent())
    {
      TextView localTextView1 = (TextView)paramView.findViewById(2131297056);
      localTextView1.setText(formatPercent(paramStockQuote.getPriceVariationPercent()));
      localTextView1.setTextColor(getColorForPriceVariation(paramContext, paramStockQuote.getPriceVariationPercent()));
    }
    paramView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 101)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        StockListEntryAdapter.this.launchDetails(paramStockQuote, paramContext, paramPredictiveCardContainer);
      }
    });
  }
  
  private View singleStockView(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, final Sidekick.StockQuote paramStockQuote)
  {
    View localView = paramLayoutInflater.inflate(2130968845, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(getTitle(paramContext, paramStockQuote));
    ((TextView)localView.findViewById(2131297057)).setText(getSubtitle(paramContext, paramStockQuote));
    NumberFormat localNumberFormat = NumberFormat.getNumberInstance();
    setStockValues(paramContext, paramPredictiveCardContainer, paramStockQuote, (ViewGroup)localView.findViewById(2131297058), localNumberFormat);
    if (paramStockQuote.hasChartUrl())
    {
      WebImageView localWebImageView = (WebImageView)localView.findViewById(2131297059);
      localWebImageView.setImageUri(Uri.parse(getStockChartUrl(paramContext, paramStockQuote)), paramPredictiveCardContainer.getNonCachingImageLoader());
      localWebImageView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 103)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          StockListEntryAdapter.this.launchDetails(paramStockQuote, paramContext, paramPredictiveCardContainer);
        }
      });
      localWebImageView.setVisibility(0);
    }
    return localView;
  }
  
  private static String stockShortNameOrSymbol(Sidekick.StockQuote paramStockQuote)
  {
    if (paramStockQuote.hasShortName()) {}
    for (String str = paramStockQuote.getShortName();; str = paramStockQuote.getSymbol()) {
      return BidiUtils.unicodeWrap(str);
    }
  }
  
  public long getLastUpdateTimeMillis(Sidekick.StockQuoteListEntry paramStockQuoteListEntry)
  {
    long l = 0L;
    Iterator localIterator = paramStockQuoteListEntry.getStockQuoteEntryList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.StockQuote localStockQuote = (Sidekick.StockQuote)localIterator.next();
      if (localStockQuote.getLastUpdateSeconds() > l) {
        l = localStockQuote.getLastUpdateSeconds();
      }
    }
    return TimeUnit.SECONDS.toMillis(l);
  }
  
  public String getTitle(Context paramContext, Sidekick.StockQuote paramStockQuote)
  {
    if (TimeUtilities.isSameDay(TimeUnit.SECONDS.toMillis(paramStockQuote.getLastUpdateSeconds()), this.mClock.currentTimeMillis()))
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = stockShortNameOrSymbol(paramStockQuote);
      return paramContext.getString(2131362601, arrayOfObject);
    }
    return stockShortNameOrSymbol(paramStockQuote);
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (this.mStocksListEntry.getStockQuoteEntryCount() == 1) {}
    for (View localView = singleStockView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, this.mStocksListEntry.getStockQuoteEntry(0));; localView = multiStockView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup))
    {
      if (this.mStocksListEntry.hasDisclaimerUrl())
      {
        Button localButton = (Button)localView.findViewById(2131296504);
        localButton.setVisibility(0);
        localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 100)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            StockListEntryAdapter.this.openUrl(paramContext, StockListEntryAdapter.this.mStocksListEntry.getDisclaimerUrl());
          }
        });
      }
      return localView;
    }
  }
  
  protected View getViewToFocusForDetails(View paramView)
  {
    if (this.mStocksListEntry.getStockQuoteEntryCount() == 1) {
      return paramView.findViewById(2131296450);
    }
    return null;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mStocksListEntry.getStockQuoteEntryCount() == 1) {
      launchDetails(this.mStocksListEntry.getStockQuoteEntry(0), paramContext, paramPredictiveCardContainer);
    }
  }
  
  static class StockQuoteIndex
  {
    final int mIndex;
    final Sidekick.StockQuote mStockQuote;
    
    private StockQuoteIndex(Sidekick.StockQuote paramStockQuote, int paramInt)
    {
      this.mStockQuote = paramStockQuote;
      this.mIndex = paramInt;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.StockListEntryAdapter
 * JD-Core Version:    0.7.0.1
 */