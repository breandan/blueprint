package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.renderingcontext.HelpContext;
import com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext;
import com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext.SharedPreferenceContextEditor;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.Histogram;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.LocationUtilities.DistanceUnit;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.LocationHistoryReminderEntry;
import com.google.geo.sidekick.Sidekick.LocationHistoryReminderEntry.ActivityStats;
import com.google.geo.sidekick.Sidekick.LocationHistoryReminderEntry.StatsPerMonth;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.Iterator;
import java.util.List;

public class LocationHistoryReminderEntryAdapter
  extends BaseEntryAdapter
{
  private static final int[] SUMMARY_COLORS = { 2131230846, 2131230847 };
  private final List<CumulativeStats> mAerobicStats;
  private final Sidekick.LocationHistoryReminderEntry mLocationHistory;
  
  LocationHistoryReminderEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mLocationHistory = paramEntry.getLocationHistoryReminderEntry();
    this.mAerobicStats = Lists.newLinkedList();
    Iterator localIterator = this.mLocationHistory.getAerobicStatsList().iterator();
    Sidekick.LocationHistoryReminderEntry.ActivityStats localActivityStats;
    if (localIterator.hasNext())
    {
      localActivityStats = (Sidekick.LocationHistoryReminderEntry.ActivityStats)localIterator.next();
      if (!localActivityStats.hasActivity())
      {
        Log.w("LocationHistoryReminderEntryAdapter", "Activity name was missing, dropping activity data");
        this.mAerobicStats.clear();
      }
    }
    for (;;)
    {
      return;
      int j = 0;
      label83:
      Sidekick.LocationHistoryReminderEntry.StatsPerMonth localStatsPerMonth;
      CumulativeStats localCumulativeStats;
      if (j < localActivityStats.getStatsCount())
      {
        localStatsPerMonth = localActivityStats.getStats(j);
        if (this.mAerobicStats.size() > j) {
          break label212;
        }
        localCumulativeStats = new CumulativeStats(null);
        if (!localStatsPerMonth.hasMonth())
        {
          Log.w("LocationHistoryReminderEntryAdapter", "Month was missing, dropping activity data");
          this.mAerobicStats.clear();
          return;
        }
        localCumulativeStats.month = localStatsPerMonth.getMonth();
        this.mAerobicStats.add(localCumulativeStats);
      }
      label212:
      do
      {
        if (localStatsPerMonth.hasDistanceInMeters()) {
          localCumulativeStats.distance = ((int)(localCumulativeStats.distance + Math.round(LocationUtilities.toLocalDistanceUnits(localStatsPerMonth.getDistanceInMeters()))));
        }
        j++;
        break label83;
        break;
        localCumulativeStats = (CumulativeStats)this.mAerobicStats.get(j);
      } while (localCumulativeStats.month == localStatsPerMonth.getMonth());
      Log.w("LocationHistoryReminderEntryAdapter", "Month didn't match between activities, dropping activity data");
      this.mAerobicStats.clear();
      return;
      for (int i = -1 + this.mAerobicStats.size(); (i >= 0) && (((CumulativeStats)this.mAerobicStats.get(i)).distance == 0); i--) {
        this.mAerobicStats.remove(i);
      }
    }
  }
  
  private void addSummaryRow(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, TableLayout paramTableLayout, LayoutInflater paramLayoutInflater, Sidekick.LocationHistoryReminderEntry.ActivityStats paramActivityStats, float paramFloat, int paramInt)
  {
    TableRow localTableRow = (TableRow)paramLayoutInflater.inflate(2130968743, paramTableLayout, false);
    if ((paramActivityStats.hasActivityIcon()) && (paramActivityStats.getActivityIcon().hasUrl()) && (paramActivityStats.getActivityIcon().getUrlType() == 0))
    {
      WebImageView localWebImageView = (WebImageView)localTableRow.findViewById(2131296783);
      localWebImageView.setImageUrl(paramActivityStats.getActivityIcon().getUrl(), paramPredictiveCardContainer.getImageLoader());
      localWebImageView.setVisibility(0);
    }
    Histogram localHistogram = (Histogram)localTableRow.findViewById(2131296784);
    ViewGroup localViewGroup = (ViewGroup)localTableRow.findViewById(2131296785);
    int i = paramInt - 1;
    if (i >= 0)
    {
      if (i < paramActivityStats.getStatsCount()) {}
      for (double d = LocationUtilities.toLocalDistanceUnits(paramActivityStats.getStats(i).getDistanceInMeters());; d = 0.0D)
      {
        int j = (int)Math.round(d);
        int k = paramContext.getResources().getColor(SUMMARY_COLORS[i]);
        localHistogram.addBar(paramFloat * j, k);
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = formatDistance(paramContext, j);
        arrayOfObject[1] = paramActivityStats.getActivity();
        String str = paramContext.getString(2131362649, arrayOfObject);
        TextView localTextView = (TextView)localViewGroup.getChildAt(-1 + localViewGroup.getChildCount() - i);
        localTextView.setTextColor(k);
        localTextView.setText(Html.fromHtml(str));
        localTextView.setVisibility(0);
        i--;
        break;
      }
    }
    paramTableLayout.addView(localTableRow);
  }
  
  private static String formatDistance(Context paramContext, int paramInt)
  {
    if (LocationUtilities.getLocalDistanceUnits() == LocationUtilities.DistanceUnit.KILOMETERS) {}
    for (int i = 2131558435;; i = 2131558434)
    {
      Resources localResources = paramContext.getResources();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      return localResources.getQuantityString(i, paramInt, arrayOfObject);
    }
  }
  
  private int getAerobicDistanceMax()
  {
    int i = 0;
    Iterator localIterator = this.mAerobicStats.iterator();
    while (localIterator.hasNext()) {
      i = Math.max(i, ((CumulativeStats)localIterator.next()).distance);
    }
    return i;
  }
  
  private String getDiffTitle(Context paramContext, int paramInt1, int paramInt2)
  {
    if (paramInt2 != 0)
    {
      String str1 = formatDistance(paramContext, Math.abs(paramInt2));
      if (paramInt2 >= 0) {}
      for (int i = 2131362655;; i = 2131362656)
      {
        String str2 = paramContext.getString(i);
        String[] arrayOfString = paramContext.getResources().getStringArray(2131492923);
        if ((paramInt1 > arrayOfString.length) || (paramInt1 <= 0)) {
          break;
        }
        return String.format(arrayOfString[(paramInt1 - 1)], new Object[] { str1, str2 });
      }
    }
    return null;
  }
  
  private static String getMonthName(Context paramContext, int paramInt)
  {
    String[] arrayOfString = paramContext.getResources().getStringArray(2131492922);
    if ((paramInt <= arrayOfString.length) && (paramInt > 0)) {
      return arrayOfString[(paramInt - 1)];
    }
    return null;
  }
  
  private String getSummaryText(Context paramContext, int paramInt)
  {
    String str = getMonthName(paramContext, paramInt);
    int i = paramContext.getResources().getColor(2131230846);
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = str;
    arrayOfObject[1] = Integer.valueOf(0xFFFFFF & i);
    return paramContext.getString(2131362648, arrayOfObject);
  }
  
  private String getSummaryText(Context paramContext, int paramInt1, int paramInt2)
  {
    Resources localResources = paramContext.getResources();
    String str1 = getMonthName(paramContext, paramInt1);
    String str2 = getMonthName(paramContext, paramInt2);
    int i = localResources.getColor(2131230846);
    int j = localResources.getColor(2131230847);
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = str2;
    arrayOfObject[1] = Integer.valueOf(j & 0xFFFFFF);
    arrayOfObject[2] = str1;
    arrayOfObject[3] = Integer.valueOf(i & 0xFFFFFF);
    return paramContext.getString(2131362647, arrayOfObject);
  }
  
  private CharSequence getTitle(Context paramContext, int paramInt1, int paramInt2)
  {
    String[] arrayOfString = paramContext.getResources().getStringArray(2131492921);
    String str = formatDistance(paramContext, paramInt1);
    if ((paramInt2 <= arrayOfString.length) && (paramInt2 > 0)) {
      return Html.fromHtml(String.format(arrayOfString[(paramInt2 - 1)], new Object[] { str }));
    }
    return null;
  }
  
  private boolean hasRecordedDistance(Sidekick.LocationHistoryReminderEntry.ActivityStats paramActivityStats, int paramInt)
  {
    for (int i = 0; i < paramInt; i++) {
      if ((paramActivityStats.getStatsCount() > i) && (Math.round(LocationUtilities.toLocalDistanceUnits(paramActivityStats.getStats(i).getDistanceInMeters())) > 0L)) {
        return true;
      }
    }
    return false;
  }
  
  private void hidePromptBubble(View paramView)
  {
    View localView = paramView.findViewById(2131296782);
    if (localView != null) {
      localView.setVisibility(8);
    }
  }
  
  private boolean isPromptBubbleHidden(View paramView)
  {
    View localView = paramView.findViewById(2131296782);
    return (localView == null) || (localView.getVisibility() == 8);
  }
  
  private void showActivitySummary(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, View paramView, Uri paramUri)
  {
    TextView localTextView1 = (TextView)paramView.findViewById(2131296451);
    Resources localResources = paramContext.getResources();
    ((ViewStub)paramView.findViewById(2131296779)).inflate();
    TextView localTextView2 = (TextView)paramView.findViewById(2131296295);
    TextView localTextView3 = (TextView)paramView.findViewById(2131296296);
    TextView localTextView4 = (TextView)paramView.findViewById(2131296298);
    localTextView4.setMovementMethod(new LinkMovementMethod()
    {
      public void onTakeFocus(TextView paramAnonymousTextView, Spannable paramAnonymousSpannable, int paramAnonymousInt)
      {
        super.onTakeFocus(paramAnonymousTextView, paramAnonymousSpannable, paramAnonymousInt);
        down(paramAnonymousTextView, paramAnonymousSpannable);
      }
    });
    localTextView4.setSelectAllOnFocus(false);
    if (this.mAerobicStats.size() == 0)
    {
      localTextView1.setText(paramContext.getText(2131362645));
      localTextView2.setVisibility(8);
      localTextView3.setVisibility(8);
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = paramUri.toString();
      localTextView4.setText(Html.fromHtml(localResources.getString(2131362650, arrayOfObject2)));
      return;
    }
    int i = ((CumulativeStats)this.mAerobicStats.get(0)).month;
    int j = ((CumulativeStats)this.mAerobicStats.get(0)).distance;
    localTextView1.setText(getTitle(paramContext, j, i));
    localTextView1.setTextColor(localResources.getColor(2131230846));
    if (this.mAerobicStats.size() == 1)
    {
      localTextView2.setVisibility(8);
      localTextView3.setText(Html.fromHtml(getSummaryText(paramContext, i)));
      TableLayout localTableLayout = (TableLayout)paramView.findViewById(2131296297);
      float f = 1.0F / getAerobicDistanceMax();
      Iterator localIterator = this.mLocationHistory.getAerobicStatsList().iterator();
      while (localIterator.hasNext())
      {
        Sidekick.LocationHistoryReminderEntry.ActivityStats localActivityStats = (Sidekick.LocationHistoryReminderEntry.ActivityStats)localIterator.next();
        int m = Math.min(this.mAerobicStats.size(), 2);
        if (hasRecordedDistance(localActivityStats, m)) {
          addSummaryRow(paramContext, paramPredictiveCardContainer, localTableLayout, paramLayoutInflater, localActivityStats, f, m);
        }
      }
    }
    int k = ((CumulativeStats)this.mAerobicStats.get(1)).month;
    String str = getDiffTitle(paramContext, k, j - ((CumulativeStats)this.mAerobicStats.get(1)).distance);
    if (str != null) {
      localTextView2.setText(Html.fromHtml(str));
    }
    for (;;)
    {
      localTextView3.setText(Html.fromHtml(getSummaryText(paramContext, i, k)));
      break;
      localTextView2.setVisibility(8);
    }
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = paramUri.toString();
    localTextView4.setText(Html.fromHtml(localResources.getString(2131362651, arrayOfObject1)));
  }
  
  public View getView(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, final LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    final View localView1 = paramLayoutInflater.inflate(2130968742, paramViewGroup, false);
    final SharedPreferencesContext localSharedPreferencesContext = SharedPreferencesContext.fromCardContainer(paramPredictiveCardContainer);
    final String str1 = paramContext.getString(2131362147);
    final String str2 = paramContext.getString(2131362146);
    boolean bool = localSharedPreferencesContext.getBoolean(str1).booleanValue();
    final Uri localUri = HelpContext.fromCardContainer(paramPredictiveCardContainer).getHelpUri("locationhistory");
    if (!bool)
    {
      View localView2 = ((ViewStub)localView1.findViewById(2131296781)).inflate();
      TextView localTextView = (TextView)localView2.findViewById(2131296457);
      localTextView.setMovementMethod(LinkMovementMethod.getInstance());
      localTextView.setText(Html.fromHtml(paramContext.getString(2131362652, new Object[] { localUri })));
      ((TextView)localView1.findViewById(2131296451)).setText(paramContext.getText(2131362645));
      localView1.findViewById(2131296778).setVisibility(0);
      Button localButton1 = (Button)localView2.findViewById(2131296462);
      localButton1.setText(2131363439);
      localButton1.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 68)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          if (LocationHistoryReminderEntryAdapter.this.isPromptBubbleHidden(localView1)) {
            return;
          }
          localSharedPreferencesContext.edit().putBoolean(str1, true).putBoolean(str2, false).apply();
          LocationHistoryReminderEntryAdapter.this.hidePromptBubble(localView1);
        }
      });
      Button localButton2 = (Button)localView2.findViewById(2131296463);
      localButton2.setText(2131363438);
      localButton2.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 69)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          if (LocationHistoryReminderEntryAdapter.this.isPromptBubbleHidden(localView1)) {
            return;
          }
          localSharedPreferencesContext.edit().putBoolean(str1, true).putBoolean(str2, true).apply();
          LocationHistoryReminderEntryAdapter.this.hidePromptBubble(localView1);
          localView1.findViewById(2131296778).setVisibility(8);
          LocationHistoryReminderEntryAdapter.this.showActivitySummary(paramContext, paramPredictiveCardContainer, paramLayoutInflater, localView1, localUri);
        }
      });
      return localView1;
    }
    showActivitySummary(paramContext, paramPredictiveCardContainer, paramLayoutInflater, localView1, localUri);
    return localView1;
  }
  
  private static class CumulativeStats
  {
    int distance;
    int month;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.LocationHistoryReminderEntryAdapter
 * JD-Core Version:    0.7.0.1
 */