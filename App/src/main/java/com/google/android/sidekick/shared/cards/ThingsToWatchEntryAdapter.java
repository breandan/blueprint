package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.SecondScreenUtil;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ThingsToWatchEntry;
import com.google.geo.sidekick.Sidekick.ThingsToWatchEntry.ClickActionWithFallback;
import java.util.Iterator;
import java.util.List;

public class ThingsToWatchEntryAdapter
  extends BaseEntryAdapter
{
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final Sidekick.ThingsToWatchEntry mThingsToWatchEntry;
  
  public ThingsToWatchEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    this.mThingsToWatchEntry = paramEntry.getThingsToWatchEntry();
  }
  
  private EntryClickListener createAppClickListener(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, final Sidekick.ClickAction paramClickAction1, final Sidekick.ClickAction paramClickAction2)
  {
    new EntryClickListener(paramPredictiveCardContainer, getEntry(), 153, paramClickAction1)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        PackageManager localPackageManager = paramContext.getPackageManager();
        String str1 = paramClickAction1.getPreferredApp();
        String str2 = paramClickAction1.getUri();
        if (TextUtils.isEmpty(str2)) {
          str2 = paramClickAction2.getUri();
        }
        if (!TextUtils.isEmpty(str2))
        {
          Intent localIntent1 = new Intent("android.intent.action.VIEW");
          localIntent1.setData(Uri.parse(str2));
          List localList = localPackageManager.queryIntentActivities(localIntent1, 65536);
          if (!localList.isEmpty())
          {
            Iterator localIterator = localList.iterator();
            while (localIterator.hasNext())
            {
              ResolveInfo localResolveInfo = (ResolveInfo)localIterator.next();
              if (str1.equals(localResolveInfo.activityInfo.applicationInfo.packageName))
              {
                localIntent1.setClassName(str1, localResolveInfo.activityInfo.name);
                paramContext.startActivity(localIntent1);
                return;
              }
            }
          }
        }
        Intent localIntent2 = localPackageManager.getLaunchIntentForPackage(paramClickAction1.getPreferredApp());
        paramContext.startActivity(localIntent2);
      }
    };
  }
  
  private EntryClickListener createUriClickListener(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, final Sidekick.ClickAction paramClickAction)
  {
    new EntryClickListener(paramPredictiveCardContainer, getEntry(), 153, paramClickAction)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        ThingsToWatchEntryAdapter.this.openUrl(paramContext, paramClickAction.getUri());
      }
    };
  }
  
  private boolean isPackageInstalled(String paramString, PackageManager paramPackageManager)
  {
    try
    {
      paramPackageManager.getPackageInfo(paramString, 128);
      return true;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    return false;
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    ViewGroup localViewGroup = (ViewGroup)paramLayoutInflater.inflate(2130968856, paramViewGroup, false);
    TextView localTextView = (TextView)localViewGroup.findViewById(2131296451);
    localTextView.setText(this.mThingsToWatchEntry.getTitle());
    localTextView.setVisibility(0);
    if (this.mThingsToWatchEntry.hasEpisodeTitle()) {
      CardTextUtil.setTextView(localViewGroup, 2131297090, this.mThingsToWatchEntry.getEpisodeTitle());
    }
    if (this.mThingsToWatchEntry.hasSubtitle()) {
      CardTextUtil.setTextView(localViewGroup, 2131297092, this.mThingsToWatchEntry.getSubtitle());
    }
    int i;
    label202:
    int j;
    label298:
    Sidekick.ThingsToWatchEntry.ClickActionWithFallback localClickActionWithFallback;
    int k;
    int m;
    label335:
    Button localButton1;
    if (this.mThingsToWatchEntry.getImageCount() > 0)
    {
      this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localViewGroup.findViewById(2131297088), this.mThingsToWatchEntry.getImage(0), 2131689753, 2131689752);
      if (this.mThingsToWatchEntry.hasFormattedHtmlTimeString()) {
        CardTextUtil.setTextView(localViewGroup, 2131297091, Html.fromHtml(this.mThingsToWatchEntry.getFormattedHtmlTimeString()));
      }
      if (this.mThingsToWatchEntry.hasJustification()) {
        CardTextUtil.setTextView(localViewGroup, 2131296353, this.mThingsToWatchEntry.getJustification());
      }
      if (this.mThingsToWatchEntry.getClickActionCount() <= 0) {
        break label450;
      }
      i = 1;
      final Sidekick.Action localAction = ProtoUtils.findAction(getEntry(), 152, new int[0]);
      if ((localAction != null) && (localAction.hasInterest()))
      {
        Button localButton2 = (Button)localViewGroup.findViewById(2131297093);
        localButton2.setVisibility(0);
        localButton2.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 152)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            paramContext.startActivity(SecondScreenUtil.createTvIntent(paramContext, localAction.getInterest()));
          }
        });
        i = 1;
      }
      PackageManager localPackageManager = paramContext.getPackageManager();
      j = 0;
      Iterator localIterator = this.mThingsToWatchEntry.getClickActionList().iterator();
      if (!localIterator.hasNext()) {
        break label495;
      }
      localClickActionWithFallback = (Sidekick.ThingsToWatchEntry.ClickActionWithFallback)localIterator.next();
      k = j + 1;
      if (j != 0) {
        break label456;
      }
      m = 2130968623;
      localButton1 = (Button)paramLayoutInflater.inflate(m, paramViewGroup, false);
      if ((!localClickActionWithFallback.hasPreferredClickAction()) || (!localClickActionWithFallback.getPreferredClickAction().hasPreferredApp()) || (!isPackageInstalled(localClickActionWithFallback.getPreferredClickAction().getPreferredApp(), localPackageManager))) {
        break label463;
      }
      localButton1.setText(localClickActionWithFallback.getPreferredClickAction().getLabel());
      localButton1.setOnClickListener(createAppClickListener(paramContext, paramPredictiveCardContainer, localClickActionWithFallback.getPreferredClickAction(), localClickActionWithFallback.getFallbackClickAction()));
    }
    for (;;)
    {
      localViewGroup.addView(localButton1);
      j = k;
      break label298;
      ((LinearLayout)localViewGroup.findViewById(2131297089)).setMinimumHeight(0);
      break;
      label450:
      i = 0;
      break label202;
      label456:
      m = 2130968622;
      break label335;
      label463:
      localButton1.setText(localClickActionWithFallback.getFallbackClickAction().getLabel());
      localButton1.setOnClickListener(createUriClickListener(paramContext, paramPredictiveCardContainer, localClickActionWithFallback.getFallbackClickAction()));
    }
    label495:
    if (i != 0) {
      localViewGroup.findViewById(2131296386).setVisibility(0);
    }
    return localViewGroup;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    String str;
    if (this.mThingsToWatchEntry.hasTitle())
    {
      str = this.mThingsToWatchEntry.getTitle();
      if (this.mThingsToWatchEntry.hasProgramType()) {
        switch (this.mThingsToWatchEntry.getProgramType())
        {
        }
      }
    }
    for (;;)
    {
      paramPredictiveCardContainer.startWebSearch(str, null);
      return;
      str = str + " " + paramContext.getString(2131362784);
      continue;
      str = str + " " + paramContext.getString(2131362782);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.ThingsToWatchEntryAdapter
 * JD-Core Version:    0.7.0.1
 */