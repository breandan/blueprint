package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.TimeToLeaveAttributes;
import com.google.geo.sidekick.Sidekick.TimeToLeaveDetails;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class TimeToLeaveUtil
{
  private static final Integer[] STARTING_LOCATIONS;
  private static final Integer[] TRAVEL_MODES;
  private static final int[] TRAVEL_MODE_DRAWABLES = { 2130837947, 2130837963, 2130837941 };
  private final Clock mClock;
  private final DirectionsLauncher mDirectionsLauncher;
  private final Sidekick.Entry mEntry;
  private final int mEventDescriptionId;
  private final Sidekick.TimeToLeaveDetails mTimeToLeaveDetails;
  private final TravelReport mTravelReport;
  
  static
  {
    Integer[] arrayOfInteger1 = new Integer[3];
    arrayOfInteger1[0] = Integer.valueOf(1);
    arrayOfInteger1[1] = Integer.valueOf(2);
    arrayOfInteger1[2] = Integer.valueOf(3);
    STARTING_LOCATIONS = arrayOfInteger1;
    Integer[] arrayOfInteger2 = new Integer[3];
    arrayOfInteger2[0] = Integer.valueOf(0);
    arrayOfInteger2[1] = Integer.valueOf(1);
    arrayOfInteger2[2] = Integer.valueOf(3);
    TRAVEL_MODES = arrayOfInteger2;
  }
  
  public TimeToLeaveUtil(Sidekick.Entry paramEntry, Sidekick.CommuteSummary paramCommuteSummary, DirectionsLauncher paramDirectionsLauncher, int paramInt, Clock paramClock)
  {
    this.mEntry = paramEntry;
    this.mTimeToLeaveDetails = ((Sidekick.TimeToLeaveDetails)Preconditions.checkNotNull(paramEntry.getTimeToLeaveDetails()));
    this.mTravelReport = new TravelReport(paramCommuteSummary);
    this.mDirectionsLauncher = paramDirectionsLauncher;
    this.mEventDescriptionId = paramInt;
    this.mClock = paramClock;
  }
  
  private String getArrivalTimeTinkerState(Context paramContext, String paramString, int paramInt)
  {
    if (paramInt == 0) {
      return paramContext.getString(2131362414);
    }
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    arrayOfObject[1] = paramString;
    return paramContext.getString(2131362412, arrayOfObject);
  }
  
  private int getDropDownWidth(ArrayAdapter<Integer> paramArrayAdapter, ViewGroup paramViewGroup)
  {
    int i = 0;
    int j = View.MeasureSpec.makeMeasureSpec(0, 0);
    int k = View.MeasureSpec.makeMeasureSpec(0, 0);
    View localView = null;
    for (int m = 0; m < paramArrayAdapter.getCount(); m++)
    {
      localView = paramArrayAdapter.getView(m, localView, paramViewGroup);
      if (localView != null)
      {
        localView.measure(j, k);
        i = Math.max(i, localView.getMeasuredWidth());
      }
    }
    return i;
  }
  
  private CharSequence getFormatedTimeToLeave(Context paramContext)
  {
    return TimeUtilities.formatDisplayTime(paramContext, this.mTimeToLeaveDetails.getTimeToLeave(), 0);
  }
  
  private String getStartingLocationLabel(Context paramContext)
  {
    return getStartingLocationLabel(paramContext, this.mTimeToLeaveDetails.getTtlAttributes().getStartingLocation());
  }
  
  static String getStartingLocationLabel(Context paramContext, int paramInt)
  {
    int i = 2131362403;
    switch (paramInt)
    {
    }
    for (;;)
    {
      return paramContext.getString(i);
      i = 2131362404;
      continue;
      i = 2131362405;
    }
  }
  
  private String getStartingLocationTinkerState(Context paramContext)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = getStartingLocationLabel(paramContext);
    return paramContext.getString(2131362411, arrayOfObject);
  }
  
  private String getTimeToLeaveMessage(Context paramContext)
  {
    if (isInLoudWindow(getTimeToLeaveMinutes()))
    {
      int i = Ints.checkedCast(getTimeToLeaveMinutes());
      int j = Math.abs(i);
      Resources localResources = paramContext.getResources();
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = Integer.valueOf(j);
      String str = localResources.getQuantityString(2131558411, j, arrayOfObject3);
      if (i < 0) {
        return paramContext.getString(2131362417, new Object[] { str });
      }
      Object[] arrayOfObject4 = new Object[2];
      arrayOfObject4[0] = getStartingLocationLabel(paramContext);
      arrayOfObject4[1] = str;
      return paramContext.getString(2131362415, arrayOfObject4);
    }
    if (this.mTimeToLeaveDetails.getTtlAttributes().getStartingLocation() == 3)
    {
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = getFormatedTimeToLeave(paramContext);
      return paramContext.getString(2131362420, arrayOfObject2);
    }
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = getStartingLocationLabel(paramContext);
    arrayOfObject1[1] = getFormatedTimeToLeave(paramContext);
    return paramContext.getString(2131362418, arrayOfObject1);
  }
  
  static String getTravelModeLabel(Context paramContext, int paramInt)
  {
    int i;
    switch (paramInt)
    {
    case 2: 
    default: 
      i = 2131362406;
    }
    for (;;)
    {
      return paramContext.getString(i);
      i = 2131362406;
      continue;
      i = 2131362407;
      continue;
      i = 2131362408;
    }
  }
  
  private String getTravelTime(Context paramContext)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mTravelReport.getTotalEtaMinutes();
    return paramContext.getString(2131362410, arrayOfObject);
  }
  
  private String getTravleModeTinkerState(Context paramContext)
  {
    return getTravelModeLabel(paramContext, this.mTimeToLeaveDetails.getTtlAttributes().getTravelMode());
  }
  
  private boolean handleArrivalTimePaddingChange(int paramInt)
  {
    return true;
  }
  
  private boolean handleStartLocationClick(int paramInt)
  {
    return true;
  }
  
  private boolean handleTravelMenuClick(int paramInt)
  {
    return true;
  }
  
  private static boolean isInLoudWindow(long paramLong)
  {
    return paramLong <= 15L;
  }
  
  private void setupNavButton(PredictiveCardContainer paramPredictiveCardContainer, View paramView, final Sidekick.Location paramLocation)
  {
    Button localButton = (Button)paramView.findViewById(2131297111);
    final MapsLauncher.TravelMode localTravelMode = MapsLauncher.TravelMode.fromSidekickProtoTravelMode(this.mTimeToLeaveDetails.getTtlAttributes().getTravelMode());
    int i = 2130837942;
    final Sidekick.CommuteSummary localCommuteSummary;
    Sidekick.Entry localEntry;
    if (isInLoudWindow(getTimeToLeaveMinutes()))
    {
      i = 2130837667;
      localButton.setBackgroundResource(i);
      localCommuteSummary = this.mTravelReport.getRoute();
      if (localCommuteSummary != null)
      {
        boolean bool = this.mDirectionsLauncher.modeSupportsNavigation(localTravelMode);
        localEntry = this.mEntry;
        if (!bool) {
          break label148;
        }
      }
    }
    label148:
    for (int j = 11;; j = 120)
    {
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, localEntry, j)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          if (localCommuteSummary.getPathfinderWaypointCount() > 0) {}
          for (List localList = localCommuteSummary.getPathfinderWaypointList();; localList = null)
          {
            TimeToLeaveUtil.this.mDirectionsLauncher.start(paramLocation, localList, localTravelMode, MapsLauncher.getPersonalizedRouteToken(localCommuteSummary));
            return;
          }
        }
      });
      return;
      if (localTravelMode == MapsLauncher.TravelMode.TRANSIT)
      {
        i = 2130837963;
        break;
      }
      if (localTravelMode != MapsLauncher.TravelMode.BIKING) {
        break;
      }
      i = 2130837941;
      break;
    }
  }
  
  private void setupTinkerState(Context paramContext, View paramView)
  {
    ((TextView)paramView.findViewById(2131297115)).setText(getTravelTime(paramContext));
    ((TextView)paramView.findViewById(2131297113)).setText(underline(getTravleModeTinkerState(paramContext)));
    ((TextView)paramView.findViewById(2131297114)).setText(underline(getStartingLocationTinkerState(paramContext)));
    int i = (int)TimeUnit.SECONDS.toMinutes(this.mTimeToLeaveDetails.getTtlAttributes().getArriveEarlyTimeSeconds());
    ((TextView)paramView.findViewById(2131297117)).setText(underline(getArrivalTimeTinkerState(paramContext, paramContext.getString(this.mEventDescriptionId), i)));
    paramView.findViewById(2131297112).setVisibility(0);
  }
  
  private void setupTinkerStateMenus(final View paramView, final Context paramContext)
  {
    final TextView localTextView1 = (TextView)paramView.findViewById(2131297113);
    localTextView1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        TimeToLeaveUtil.this.showTravelModeMenu(paramContext, paramView, localTextView1);
      }
    });
    final TextView localTextView2 = (TextView)paramView.findViewById(2131297114);
    localTextView2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        TimeToLeaveUtil.this.showStartingLocationMenu(paramContext, paramView, localTextView2);
      }
    });
    final TextView localTextView3 = (TextView)paramView.findViewById(2131297117);
    localTextView3.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        TimeToLeaveUtil.this.showArrivalTimeSlider(paramContext, localTextView3);
      }
    });
  }
  
  private void showArrivalTimeSlider(final Context paramContext, final TextView paramTextView)
  {
    View localView = LayoutInflater.from(paramContext).inflate(2130968601, (ViewGroup)paramTextView.getParent(), false);
    SeekBar localSeekBar = (SeekBar)localView.findViewById(2131296349);
    int i = (int)TimeUnit.SECONDS.toMinutes(this.mTimeToLeaveDetails.getTtlAttributes().getArriveEarlyTimeSeconds());
    localSeekBar.setMax(18);
    localSeekBar.setProgress(i / 5);
    localSeekBar.setBackgroundColor(paramContext.getResources().getColor(17170443));
    localSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          int i = paramAnonymousInt * 5;
          paramTextView.setText(TimeToLeaveUtil.this.underline(TimeToLeaveUtil.access$1100(TimeToLeaveUtil.this, paramContext, paramContext.getString(TimeToLeaveUtil.this.mEventDescriptionId), i)));
          TimeToLeaveUtil.this.handleArrivalTimePaddingChange(i);
        }
      }
      
      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}
      
      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {}
    });
    PopupWindow localPopupWindow = new PopupWindow(paramContext);
    localPopupWindow.setBackgroundDrawable(paramContext.getResources().getDrawable(2130838039));
    localPopupWindow.setContentView(localView);
    localPopupWindow.setWidth((int)(1.5D * (((ViewGroup)paramTextView.getParent()).getMeasuredWidth() / 2)));
    localPopupWindow.setHeight(-2);
    localPopupWindow.setFocusable(true);
    localPopupWindow.showAsDropDown(paramTextView);
  }
  
  private void showStartingLocationMenu(final Context paramContext, View paramView, final TextView paramTextView)
  {
    final LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
    ArrayAdapter local7 = new ArrayAdapter(paramContext, 2130968836, STARTING_LOCATIONS)
    {
      public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
      {
        View localView = paramAnonymousView;
        if (localView == null) {
          localView = localLayoutInflater.inflate(2130968836, paramAnonymousViewGroup, false);
        }
        String str1 = TimeToLeaveUtil.getStartingLocationLabel(paramContext, TimeToLeaveUtil.STARTING_LOCATIONS[paramAnonymousInt].intValue());
        String str2 = paramContext.getString(2131362411, new Object[] { str1 });
        ((TextView)localView.findViewById(2131296681)).setText(str2);
        localView.findViewById(2131296310).setVisibility(8);
        return localView;
      }
    };
    final ListPopupWindow localListPopupWindow = new ListPopupWindow(paramContext);
    localListPopupWindow.setAdapter(local7);
    localListPopupWindow.setAnchorView(paramTextView);
    localListPopupWindow.setModal(true);
    localListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        String str1 = TimeToLeaveUtil.getStartingLocationLabel(paramContext, TimeToLeaveUtil.STARTING_LOCATIONS[paramAnonymousInt].intValue());
        String str2 = paramContext.getString(2131362411, new Object[] { str1 });
        paramTextView.setText(TimeToLeaveUtil.this.underline(str2));
        TimeToLeaveUtil.this.handleStartLocationClick(TimeToLeaveUtil.STARTING_LOCATIONS[paramAnonymousInt].intValue());
        localListPopupWindow.dismiss();
      }
    });
    localListPopupWindow.setContentWidth(getDropDownWidth(local7, (ViewGroup)paramView));
    localListPopupWindow.show();
  }
  
  private void showTravelModeMenu(final Context paramContext, View paramView, final TextView paramTextView)
  {
    final LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
    ArrayAdapter local5 = new ArrayAdapter(paramContext, 2130968836, TRAVEL_MODES)
    {
      public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
      {
        View localView = paramAnonymousView;
        if (localView == null) {
          localView = localLayoutInflater.inflate(2130968836, paramAnonymousViewGroup, false);
        }
        ((TextView)localView.findViewById(2131296681)).setText(TimeToLeaveUtil.getTravelModeLabel(paramContext, TimeToLeaveUtil.TRAVEL_MODES[paramAnonymousInt].intValue()));
        ((ImageView)localView.findViewById(2131296310)).setImageResource(TimeToLeaveUtil.TRAVEL_MODE_DRAWABLES[paramAnonymousInt]);
        return localView;
      }
    };
    final ListPopupWindow localListPopupWindow = new ListPopupWindow(paramContext);
    localListPopupWindow.setAdapter(local5);
    localListPopupWindow.setAnchorView(paramTextView);
    localListPopupWindow.setModal(true);
    localListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        paramTextView.setText(TimeToLeaveUtil.this.underline(TimeToLeaveUtil.getTravelModeLabel(paramContext, TimeToLeaveUtil.TRAVEL_MODES[paramAnonymousInt].intValue())));
        TimeToLeaveUtil.this.handleTravelMenuClick(TimeToLeaveUtil.TRAVEL_MODES[paramAnonymousInt].intValue());
        localListPopupWindow.dismiss();
      }
    });
    localListPopupWindow.setContentWidth(getDropDownWidth(local5, (ViewGroup)paramView));
    localListPopupWindow.show();
  }
  
  private SpannableString underline(String paramString)
  {
    SpannableString localSpannableString = new SpannableString(paramString);
    localSpannableString.setSpan(new UnderlineSpan(), 0, localSpannableString.length(), 0);
    return localSpannableString;
  }
  
  long getTimeToLeaveMinutes()
  {
    return TimeUnit.MILLISECONDS.toMinutes(this.mTimeToLeaveDetails.getTimeToLeave() - this.mClock.currentTimeMillis());
  }
  
  public void setupTimeToLeave(Context paramContext, View paramView, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, Sidekick.Location paramLocation, @Nullable TransitPlaceEntryViewUtil paramTransitPlaceEntryViewUtil)
  {
    View localView1 = paramView.findViewById(2131296424);
    TextView localTextView = (TextView)localView1.findViewById(2131297109);
    localTextView.setText(getTimeToLeaveMessage(paramContext));
    setupTinkerState(paramContext, paramView);
    localView1.setOnClickListener(null);
    if ((paramTransitPlaceEntryViewUtil != null) && (paramTransitPlaceEntryViewUtil.shouldShowTransitView()))
    {
      View localView2 = paramTransitPlaceEntryViewUtil.updateTransitRouteList(paramContext, paramPredictiveCardContainer, paramView, paramLayoutInflater, true, true);
      localView2.setPadding(0, localView2.getPaddingTop(), 0, localView2.getPaddingBottom());
    }
    ToggleClickListener localToggleClickListener = new ToggleClickListener(localView1, getTimeToLeaveMinutes(), paramContext.getResources().getColor(2131230870), paramPredictiveCardContainer, this.mEntry, 151);
    localToggleClickListener.initializeCollapsed();
    localTextView.setOnClickListener(localToggleClickListener);
    setupNavButton(paramPredictiveCardContainer, paramView, paramLocation);
    setupTinkerStateMenus(paramView, paramContext);
    localView1.setVisibility(0);
  }
  
  private static class ToggleClickListener
    extends EntryClickListener
  {
    private final int mTextColor;
    private final long mTimeToLeaveMinutes;
    private final View mView;
    
    public ToggleClickListener(View paramView, long paramLong, int paramInt1, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.Entry paramEntry, int paramInt2)
    {
      super(paramEntry, paramInt2);
      this.mView = paramView;
      this.mTimeToLeaveMinutes = paramLong;
      this.mTextColor = paramInt1;
    }
    
    private void collapseImpl(View paramView1, View paramView2, View paramView3)
    {
      if (TimeToLeaveUtil.isInLoudWindow(this.mTimeToLeaveMinutes))
      {
        setBackground(this.mView, 2130838104);
        ((TextView)this.mView.findViewById(2131297109)).setTextColor(-1);
        ((TextView)this.mView.findViewById(2131297115)).setTextColor(-1);
        ((Button)this.mView.findViewById(2131297111)).setBackgroundResource(2130837667);
      }
      paramView1.setVisibility(8);
      paramView2.setVisibility(8);
      if (paramView3 != null) {
        paramView3.setVisibility(8);
      }
    }
    
    private void expandImpl(View paramView1, View paramView2, View paramView3)
    {
      if (TimeToLeaveUtil.isInLoudWindow(this.mTimeToLeaveMinutes))
      {
        setBackground(this.mView, 2130838103);
        ((TextView)this.mView.findViewById(2131297109)).setTextColor(this.mTextColor);
        ((TextView)this.mView.findViewById(2131297115)).setTextColor(this.mTextColor);
        ((Button)this.mView.findViewById(2131297111)).setBackgroundResource(2130837666);
      }
      paramView1.setVisibility(0);
      paramView2.setVisibility(0);
      if (paramView3 != null) {
        paramView3.setVisibility(0);
      }
    }
    
    private boolean isTinkerStateExpanded()
    {
      return this.mView.findViewById(2131297116).getVisibility() == 0;
    }
    
    private void setBackground(View paramView, int paramInt)
    {
      int i = paramView.getPaddingLeft();
      int j = paramView.getPaddingTop();
      int k = paramView.getPaddingRight();
      int m = paramView.getPaddingBottom();
      paramView.setBackgroundResource(paramInt);
      paramView.setPadding(i, j, k, m);
    }
    
    public void initializeCollapsed()
    {
      collapseImpl(this.mView.findViewById(2131297112), this.mView.findViewById(2131297116), this.mView.findViewById(2131296329));
    }
    
    public void onEntryClick(View paramView)
    {
      View localView1 = this.mView.findViewById(2131297112);
      View localView2 = this.mView.findViewById(2131297116);
      View localView3 = this.mView.findViewById(2131296329);
      if (!isTinkerStateExpanded())
      {
        expandImpl(localView1, localView2, localView3);
        return;
      }
      collapseImpl(localView1, localView2, localView3);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.TimeToLeaveUtil
 * JD-Core Version:    0.7.0.1
 */