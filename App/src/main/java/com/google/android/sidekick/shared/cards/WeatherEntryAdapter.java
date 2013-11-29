package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.search.shared.imageloader.ResourceImageLoader;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.WeatherEntry;
import com.google.geo.sidekick.Sidekick.WeatherEntry.WeatherPoint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class WeatherEntryAdapter
  extends BaseEntryAdapter
{
  private Sidekick.WeatherEntry mWeatherEntry;
  
  public WeatherEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mWeatherEntry = paramEntry.getWeatherEntry();
  }
  
  private static void addCurrentConditions(Context paramContext, UriLoader<Drawable> paramUriLoader, Sidekick.WeatherEntry.WeatherPoint paramWeatherPoint, View paramView, StringBuilder paramStringBuilder)
  {
    paramView.findViewById(2131297231).setVisibility(0);
    TextView localTextView1 = (TextView)paramView.findViewById(2131297233);
    localTextView1.setVisibility(0);
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Integer.valueOf(paramWeatherPoint.getHighTemperature());
    localTextView1.setText(paramContext.getString(2131362334, arrayOfObject1));
    if (paramWeatherPoint.hasImageUrl())
    {
      String str2 = getImageUrl(paramWeatherPoint.getImageUrl(), paramContext);
      WebImageView localWebImageView = (WebImageView)paramView.findViewById(2131297232);
      localWebImageView.setImageUrl(str2, paramUriLoader);
      localWebImageView.setVisibility(0);
    }
    if (paramWeatherPoint.hasDescription())
    {
      TextView localTextView4 = (TextView)paramView.findViewById(2131297234);
      localTextView4.setVisibility(0);
      localTextView4.setText(Html.fromHtml(paramWeatherPoint.getDescription()));
    }
    paramStringBuilder.append(paramWeatherPoint.getDescription()).append(" ");
    Object[] arrayOfObject2 = new Object[1];
    arrayOfObject2[0] = Integer.valueOf(paramWeatherPoint.getHighTemperature());
    paramStringBuilder.append(paramContext.getString(2131362341, arrayOfObject2)).append(" ");
    if (paramWeatherPoint.hasChanceOfPrecipitation())
    {
      TextView localTextView3 = (TextView)paramView.findViewById(2131297236);
      localTextView3.setVisibility(0);
      Object[] arrayOfObject4 = new Object[1];
      arrayOfObject4[0] = Integer.valueOf(paramWeatherPoint.getChanceOfPrecipitation());
      localTextView3.setText(paramContext.getString(2131362335, arrayOfObject4));
    }
    String str1;
    if (paramWeatherPoint.hasWindSpeed())
    {
      str1 = "";
      switch (paramWeatherPoint.getWindUnit())
      {
      }
    }
    for (;;)
    {
      TextView localTextView2 = (TextView)paramView.findViewById(2131297235);
      localTextView2.setVisibility(0);
      Object[] arrayOfObject3 = new Object[2];
      arrayOfObject3[0] = Integer.valueOf(paramWeatherPoint.getWindSpeed());
      arrayOfObject3[1] = str1;
      localTextView2.setText(Html.fromHtml(paramContext.getString(2131362336, arrayOfObject3)));
      return;
      str1 = paramContext.getString(2131362339);
      continue;
      str1 = paramContext.getString(2131362338);
      continue;
      str1 = paramContext.getString(2131362340);
    }
  }
  
  private static void addForecast(Context paramContext, UriLoader<Drawable> paramUriLoader, View paramView, List<Sidekick.WeatherEntry.WeatherPoint> paramList, LayoutInflater paramLayoutInflater, StringBuilder paramStringBuilder)
  {
    ViewGroup localViewGroup1 = (ViewGroup)paramView.findViewById(2131297237);
    localViewGroup1.setVisibility(0);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.WeatherEntry.WeatherPoint localWeatherPoint = (Sidekick.WeatherEntry.WeatherPoint)localIterator.next();
      ViewGroup localViewGroup2 = (ViewGroup)paramLayoutInflater.inflate(2130968925, localViewGroup1, false);
      ((TextView)localViewGroup2.findViewById(2131296532)).setText(localWeatherPoint.getLabel());
      if (localWeatherPoint.hasImageUrl())
      {
        Uri localUri = Uri.parse(getImageUrl(localWeatherPoint.getImageUrl(), paramContext));
        ((WebImageView)localViewGroup2.findViewById(2131296310)).setImageUri(localUri, paramUriLoader);
      }
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Integer.valueOf(localWeatherPoint.getHighTemperature());
      String str1 = paramContext.getString(2131362334, arrayOfObject1);
      ((TextView)localViewGroup2.findViewById(2131297247)).setText(str1);
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Integer.valueOf(localWeatherPoint.getLowTemperature());
      String str2 = paramContext.getString(2131362334, arrayOfObject2);
      ((TextView)localViewGroup2.findViewById(2131297248)).setText(str2);
      localViewGroup1.addView(localViewGroup2);
      paramStringBuilder.append(localWeatherPoint.getLabel()).append(" ");
      paramStringBuilder.append(localWeatherPoint.getDescription()).append(" ");
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = Integer.valueOf(localWeatherPoint.getHighTemperature());
      paramStringBuilder.append(paramContext.getString(2131362342, arrayOfObject3)).append(" ");
      Object[] arrayOfObject4 = new Object[1];
      arrayOfObject4[0] = Integer.valueOf(localWeatherPoint.getLowTemperature());
      paramStringBuilder.append(paramContext.getString(2131362343, arrayOfObject4)).append(" ");
    }
  }
  
  private static String getDensityDpiString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "xhdpi";
    case 240: 
      return "hdpi";
    case 120: 
      return "mdpi";
    case 160: 
      return "mdpi";
    case 213: 
      return "hdpi";
    }
    return "xhdpi";
  }
  
  public static String getImageUrl(String paramString, Context paramContext)
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = getDensityDpiString(paramContext.getResources().getDisplayMetrics().densityDpi);
    return String.format(localLocale, paramString, arrayOfObject);
  }
  
  private static String nextDayLabel(Calendar paramCalendar, DateFormat paramDateFormat)
  {
    paramCalendar.add(7, 1);
    return paramDateFormat.format(paramCalendar.getTime());
  }
  
  public static void populateSampleCard(View paramView)
  {
    Context localContext = paramView.getContext();
    ((TextView)paramView.findViewById(2131296451)).setText(localContext.getString(2131362511));
    StringBuilder localStringBuilder = new StringBuilder();
    ResourceImageLoader localResourceImageLoader = new ResourceImageLoader(localContext);
    String str = String.valueOf(2130838034);
    addCurrentConditions(localContext, localResourceImageLoader, new Sidekick.WeatherEntry.WeatherPoint().setHighTemperature(85).setDescription(localContext.getString(2131362512)).setChanceOfPrecipitation(10).setImageUrl(str), paramView, localStringBuilder);
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("EEE", Locale.getDefault());
    Calendar localCalendar = Calendar.getInstance();
    addForecast(localContext, localResourceImageLoader, paramView, ImmutableList.of(new Sidekick.WeatherEntry.WeatherPoint().setLabel(nextDayLabel(localCalendar, localSimpleDateFormat)).setImageUrl(str).setHighTemperature(91).setLowTemperature(67), new Sidekick.WeatherEntry.WeatherPoint().setLabel(nextDayLabel(localCalendar, localSimpleDateFormat)).setImageUrl(str).setHighTemperature(88).setLowTemperature(68), new Sidekick.WeatherEntry.WeatherPoint().setLabel(nextDayLabel(localCalendar, localSimpleDateFormat)).setImageUrl(str).setHighTemperature(87).setLowTemperature(68), new Sidekick.WeatherEntry.WeatherPoint().setLabel(nextDayLabel(localCalendar, localSimpleDateFormat)).setImageUrl(str).setHighTemperature(92).setLowTemperature(71)), LayoutInflater.from(localContext), localStringBuilder);
    paramView.invalidate();
  }
  
  public String getLoggingName()
  {
    String str = super.getLoggingName();
    if (this.mWeatherEntry.hasLocationType()) {}
    switch (this.mWeatherEntry.getLocationType())
    {
    case 3: 
    default: 
      return str;
    case 1: 
      return str + "Home";
    case 2: 
      return str + "Work";
    }
    return str + "TripDestination";
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968923, paramViewGroup, false);
    StringBuilder localStringBuilder = new StringBuilder();
    String str = "";
    if ((this.mWeatherEntry.hasLocation()) && (this.mWeatherEntry.getLocation().hasName()))
    {
      str = this.mWeatherEntry.getLocation().getName();
      int i = str.indexOf(',');
      if (i > 0) {
        str = str.substring(0, i);
      }
      localStringBuilder.append(str).append(" ");
    }
    ((TextView)localView.findViewById(2131296451)).setText(str);
    if (this.mWeatherEntry.hasCurrentConditions()) {
      addCurrentConditions(paramContext, paramPredictiveCardContainer.getImageLoader(), this.mWeatherEntry.getCurrentConditions(), localView, localStringBuilder);
    }
    if (this.mWeatherEntry.getWeatherPointCount() > 0) {
      addForecast(paramContext, paramPredictiveCardContainer.getImageLoader(), localView, this.mWeatherEntry.getWeatherPointList(), paramLayoutInflater, localStringBuilder);
    }
    localView.setContentDescription(localStringBuilder);
    return localView;
  }
  
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296450);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mWeatherEntry == null) || (!this.mWeatherEntry.hasLocation())) {
      return;
    }
    Location localLocation = new Location("weather");
    localLocation.setLatitude(this.mWeatherEntry.getLocation().getLat());
    localLocation.setLongitude(this.mWeatherEntry.getLocation().getLng());
    paramPredictiveCardContainer.startWebSearch(paramContext.getString(2131362544), localLocation);
  }
  
  public void replaceEntry(Sidekick.Entry paramEntry)
  {
    super.replaceEntry(paramEntry);
    this.mWeatherEntry = getEntry().getWeatherEntry();
  }
  
  public View updateView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, View paramView, Sidekick.Entry paramEntry)
  {
    return getView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.WeatherEntryAdapter
 * JD-Core Version:    0.7.0.1
 */