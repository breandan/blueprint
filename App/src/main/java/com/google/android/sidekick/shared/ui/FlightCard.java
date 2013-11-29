package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.cards.FlightStatusEntryAdapter;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.FlightStatusFormatter;
import com.google.android.sidekick.shared.util.GoogleServiceWebviewUtil;
import com.google.android.sidekick.shared.util.MoonshineUtilities;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.GmailReference;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

public class FlightCard
  extends LinearLayout
{
  private static final String TAG = FlightCard.class.getSimpleName();
  private FlightStatusFormatter mFormatter;
  private Button mGetDirectionsButton;
  private TextView mLabel;
  private Button mNavigateButton;
  private TableLayout mSegmentsTable;
  private TextView mSenderEmail;
  private Queue<ViewGroup> mUnusedStops;
  
  public FlightCard(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public FlightCard(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public FlightCard(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private void addSegment(int paramInt, Calendar paramCalendar, String paramString1, String paramString2, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramCalendar);
    int i = this.mSegmentsTable.getChildCount();
    LayoutInflater.from(getContext()).inflate(2130968693, this.mSegmentsTable, true);
    if (i == 0)
    {
      ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mSegmentsTable.getChildAt(0).getLayoutParams();
      localMarginLayoutParams.setMargins(localMarginLayoutParams.leftMargin, 0, localMarginLayoutParams.rightMargin, localMarginLayoutParams.bottomMargin);
    }
    TextView localTextView1 = (TextView)this.mSegmentsTable.getChildAt(i + 1);
    if (paramBoolean) {}
    for (CharSequence localCharSequence = this.mFormatter.getFormattedDepartureTime(paramCalendar);; localCharSequence = this.mFormatter.getFlightStatus(paramInt, paramCalendar))
    {
      localTextView1.setText(localCharSequence);
      TextView localTextView2 = (TextView)this.mSegmentsTable.getChildAt(i + 2);
      if (paramString1 != null)
      {
        Context localContext2 = getContext();
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = BidiUtils.unicodeWrap(paramString1);
        localTextView2.setText(localContext2.getString(2131362310, arrayOfObject2));
      }
      ViewGroup localViewGroup1 = (ViewGroup)this.mSegmentsTable.getChildAt(i + 3);
      this.mUnusedStops.add(localViewGroup1);
      TextView localTextView3 = (TextView)this.mSegmentsTable.getChildAt(i + 5);
      if (paramString2 != null)
      {
        Context localContext1 = getContext();
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = BidiUtils.unicodeWrap(paramString2);
        localTextView3.setText(localContext1.getString(2131362311, arrayOfObject1));
      }
      ViewGroup localViewGroup2 = (ViewGroup)this.mSegmentsTable.getChildAt(i + 6);
      this.mUnusedStops.add(localViewGroup2);
      return;
    }
  }
  
  public static String fixTimeZone(String paramString)
  {
    if ((paramString != null) && ((paramString.startsWith("GMT+")) || (paramString.startsWith("GMT-"))) && (paramString.indexOf('.') > 0)) {}
    try
    {
      char c = paramString.charAt(3);
      float f = Math.abs(Float.parseFloat(paramString.substring(4))) % 24.0F;
      int i = (int)f;
      int j = (int)(f * 60.0F % 60.0F);
      Locale localLocale = Locale.US;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = Character.valueOf(c);
      arrayOfObject[1] = Integer.valueOf(i);
      arrayOfObject[2] = Integer.valueOf(j);
      String str = String.format(localLocale, "GMT%c%d:%d", arrayOfObject);
      paramString = str;
      return paramString;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      Log.w(TAG, "Invalid time zone: " + paramString);
    }
    return paramString;
  }
  
  private String getSenderEmail(Sidekick.GmailReference paramGmailReference)
  {
    if (paramGmailReference.hasSenderDisplayName()) {
      return paramGmailReference.getSenderDisplayName();
    }
    if (paramGmailReference.hasSenderEmailAddress()) {
      return paramGmailReference.getSenderEmailAddress();
    }
    return null;
  }
  
  private void init()
  {
    setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    setBackgroundResource(2130837532);
    setOrientation(1);
    LayoutInflater.from(getContext()).inflate(2130968691, this);
    this.mFormatter = new FlightStatusFormatter(getContext());
    this.mLabel = ((TextView)findViewById(2131296613));
    this.mSenderEmail = ((TextView)findViewById(2131296614));
    this.mSegmentsTable = ((TableLayout)findViewById(2131296615));
    this.mNavigateButton = ((Button)findViewById(2131296616));
    this.mGetDirectionsButton = ((Button)findViewById(2131296617));
    this.mUnusedStops = new LinkedList();
  }
  
  private static boolean isDifferentDate(Calendar paramCalendar1, Calendar paramCalendar2)
  {
    Preconditions.checkNotNull(paramCalendar1);
    return (paramCalendar2 == null) || (paramCalendar1.get(5) != paramCalendar2.get(5)) || (paramCalendar1.get(2) != paramCalendar2.get(2)) || (paramCalendar1.get(1) != paramCalendar2.get(1));
  }
  
  private void populateStop(String paramString1, String paramString2, String paramString3, String paramString4, Calendar paramCalendar1, Calendar paramCalendar2, Calendar paramCalendar3)
  {
    boolean bool;
    Context localContext;
    TextView localTextView2;
    label117:
    String str;
    label173:
    label201:
    TextView localTextView4;
    int i;
    label360:
    int j;
    if (!this.mUnusedStops.isEmpty())
    {
      bool = true;
      Preconditions.checkState(bool);
      localContext = getContext();
      ViewGroup localViewGroup = (ViewGroup)this.mUnusedStops.poll();
      TextView localTextView1 = (TextView)localViewGroup.findViewById(2131296630);
      localTextView1.setText(paramString2);
      localTextView2 = (TextView)localViewGroup.findViewById(2131296632);
      TextView localTextView3 = (TextView)localViewGroup.findViewById(2131296633);
      if (paramCalendar2 == null) {
        break label457;
      }
      if (!isDifferentDate(paramCalendar2, paramCalendar3)) {
        break label426;
      }
      localTextView2.setText(this.mFormatter.formatDateTime(paramCalendar2, 524315));
      if ((paramCalendar1 != null) && (Math.abs(paramCalendar2.getTime().getTime() - paramCalendar1.getTime().getTime()) > 60000L))
      {
        if (!isDifferentDate(paramCalendar2, paramCalendar1)) {
          break label443;
        }
        str = this.mFormatter.formatDateTime(paramCalendar1, 524315);
        localTextView3.setText(localContext.getString(2131362318, new Object[] { str }));
        localTextView3.setVisibility(0);
      }
      if (localTextView3.getVisibility() == 0)
      {
        Paint localPaint = new Paint();
        localPaint.setTextSize(localTextView1.getTextSize());
        float f1 = 0.0F + localPaint.measureText(localTextView1.getText().toString());
        localPaint.setTextSize(localTextView2.getTextSize());
        float f2 = f1 + localPaint.measureText(localTextView2.getText().toString());
        localPaint.setTextSize(localTextView3.getTextSize());
        if (f2 + localPaint.measureText(localTextView3.getText().toString()) + localContext.getResources().getDimensionPixelSize(2131689750) > LayoutUtils.getCardWidth(localContext)) {
          ((LinearLayout)localViewGroup.findViewById(2131296631)).setOrientation(1);
        }
      }
      localTextView4 = (TextView)localViewGroup.findViewById(2131296634);
      if (TextUtils.isEmpty(paramString3)) {
        break label509;
      }
      i = 1;
      if (TextUtils.isEmpty(paramString4)) {
        break label515;
      }
      j = 1;
      label371:
      if ((i == 0) || (j == 0)) {
        break label521;
      }
      Object[] arrayOfObject3 = new Object[2];
      arrayOfObject3[0] = BidiUtils.unicodeWrap(paramString3);
      arrayOfObject3[1] = BidiUtils.unicodeWrap(paramString4);
      localTextView4.setText(localContext.getString(2131362317, arrayOfObject3));
    }
    label426:
    label443:
    label457:
    label509:
    do
    {
      return;
      bool = false;
      break;
      localTextView2.setText(this.mFormatter.formatTime(paramCalendar2));
      break label117;
      str = this.mFormatter.formatTime(paramCalendar1);
      break label173;
      if (paramCalendar1 == null) {
        break label201;
      }
      if (isDifferentDate(paramCalendar1, paramCalendar3))
      {
        localTextView2.setText(this.mFormatter.formatDateTime(paramCalendar1, 524315));
        break label201;
      }
      localTextView2.setText(this.mFormatter.formatTime(paramCalendar1));
      break label201;
      i = 0;
      break label360;
      j = 0;
      break label371;
      if (i != 0)
      {
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = BidiUtils.unicodeWrap(paramString3);
        localTextView4.setText(localContext.getString(2131362315, arrayOfObject2));
        return;
      }
    } while (j == 0);
    label515:
    label521:
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = BidiUtils.unicodeWrap(paramString4);
    localTextView4.setText(localContext.getString(2131362316, arrayOfObject1));
  }
  
  private void removeSegments()
  {
    this.mSegmentsTable.removeAllViews();
  }
  
  private void setFlightInfo(FlightStatusEntryAdapter paramFlightStatusEntryAdapter, String paramString1, String paramString2, String paramString3, boolean paramBoolean, final String paramString4, List<Sidekick.GmailReference> paramList, PredictiveCardContainer paramPredictiveCardContainer)
  {
    TextView localTextView1 = this.mLabel;
    Context localContext1 = getContext();
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = BidiUtils.unicodeWrap(paramString1);
    arrayOfObject1[1] = BidiUtils.unicodeWrap(paramString2);
    localTextView1.setText(Html.fromHtml(localContext1.getString(2131362303, arrayOfObject1)));
    Button localButton2;
    Button localButton3;
    label101:
    Button localButton1;
    Sidekick.GmailReference localGmailReference;
    if (paramString3 != null) {
      if (paramBoolean)
      {
        localButton2 = this.mNavigateButton;
        localButton3 = this.mGetDirectionsButton;
        Spanned localSpanned = Html.fromHtml(paramString3);
        localButton2.setText(localSpanned);
        localButton2.setVisibility(0);
        localButton3.setVisibility(8);
        if (paramString4 != null) {
          ((Button)findViewById(2131296618)).setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramAnonymousView)
            {
              Intent localIntent = new Intent("android.intent.action.VIEW");
              localIntent.setData(Uri.parse(paramString4));
              paramAnonymousView.getContext().startActivity(localIntent);
            }
          });
        }
        if ((paramList == null) || (paramList.isEmpty())) {
          break label369;
        }
        localButton1 = (Button)findViewById(2131296376);
        localGmailReference = MoonshineUtilities.getEffectiveGmailReferenceAndSetText(getContext(), localButton1, paramList);
        if (localGmailReference != null)
        {
          String str1 = getSenderEmail(localGmailReference);
          if (str1 == null) {
            break label339;
          }
          TextView localTextView2 = this.mSenderEmail;
          Context localContext2 = getContext();
          Object[] arrayOfObject2 = new Object[1];
          arrayOfObject2[0] = BidiUtils.unicodeWrapLtr(str1);
          localTextView2.setText(Html.fromHtml(localContext2.getString(2131362304, arrayOfObject2)));
          this.mSenderEmail.setVisibility(0);
        }
      }
    }
    final String str2;
    final String str3;
    for (;;)
    {
      localButton1.setVisibility(0);
      str2 = this.mLabel.getText().toString();
      str3 = localGmailReference.getEmailUrl();
      if (paramFlightStatusEntryAdapter == null) {
        break label351;
      }
      localButton1.setOnClickListener(new GoogleServiceWebviewClickListener(getContext(), str3, str2, false, paramFlightStatusEntryAdapter, 20, "mail", GoogleServiceWebviewUtil.GMAIL_URL_PREFIXES, null, paramPredictiveCardContainer));
      return;
      localButton2 = this.mGetDirectionsButton;
      localButton3 = this.mNavigateButton;
      break;
      this.mNavigateButton.setVisibility(8);
      this.mGetDirectionsButton.setVisibility(8);
      break label101;
      label339:
      this.mSenderEmail.setVisibility(8);
    }
    label351:
    localButton1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = GoogleServiceWebviewUtil.createIntent(Uri.parse(str3));
        localIntent.putExtra("webview_service", "mail").putExtra("webview_title", str2).putExtra("enable_javascript", false).putExtra("webview_url_prefixes", GoogleServiceWebviewUtil.GMAIL_URL_PREFIXES);
        paramAnonymousView.getContext().startActivity(localIntent);
      }
    });
    return;
    label369:
    this.mSenderEmail.setVisibility(8);
  }
  
  public void setOnNavigateListener(View.OnClickListener paramOnClickListener)
  {
    this.mNavigateButton.setOnClickListener(paramOnClickListener);
    this.mGetDirectionsButton.setOnClickListener(paramOnClickListener);
  }
  
  public static class Builder
  {
    private final String mAirlineName;
    private String mButtonLabel;
    private final PredictiveCardContainer mCardContainer;
    private String mDetailsUrl;
    private FlightStatusEntryAdapter mFlightAdapter;
    private final String mFlightNumber;
    private List<Sidekick.GmailReference> mGmailReferenceList;
    private final List<FlightCard.SegmentBuilder> mSegments;
    private boolean mUseNavigation;
    
    public Builder(PredictiveCardContainer paramPredictiveCardContainer, String paramString1, String paramString2)
    {
      this.mCardContainer = paramPredictiveCardContainer;
      this.mAirlineName = paramString1;
      this.mFlightNumber = paramString2;
      this.mSegments = new LinkedList();
    }
    
    public FlightCard.SegmentBuilder addSegment()
    {
      FlightCard.SegmentBuilder localSegmentBuilder = new FlightCard.SegmentBuilder();
      this.mSegments.add(localSegmentBuilder);
      return localSegmentBuilder;
    }
    
    public Builder setDetailsUrl(String paramString)
    {
      this.mDetailsUrl = ((String)Preconditions.checkNotNull(paramString));
      return this;
    }
    
    public Builder setFlightStatusEntryAdapter(FlightStatusEntryAdapter paramFlightStatusEntryAdapter)
    {
      this.mFlightAdapter = paramFlightStatusEntryAdapter;
      return this;
    }
    
    public Builder setGetDirectionsAction(String paramString)
    {
      this.mButtonLabel = ((String)Preconditions.checkNotNull(paramString));
      this.mUseNavigation = false;
      return this;
    }
    
    public Builder setGmailReferenceList(List<Sidekick.GmailReference> paramList)
    {
      this.mGmailReferenceList = paramList;
      return this;
    }
    
    public Builder setNavigateAction(String paramString)
    {
      this.mButtonLabel = ((String)Preconditions.checkNotNull(paramString));
      this.mUseNavigation = true;
      return this;
    }
    
    public void update(FlightCard paramFlightCard)
    {
      paramFlightCard.setFlightInfo(this.mFlightAdapter, this.mAirlineName, this.mFlightNumber, this.mButtonLabel, this.mUseNavigation, this.mDetailsUrl, this.mGmailReferenceList, this.mCardContainer);
      paramFlightCard.removeSegments();
      Iterator localIterator = this.mSegments.iterator();
      while (localIterator.hasNext()) {
        FlightCard.SegmentBuilder.access$200((FlightCard.SegmentBuilder)localIterator.next(), paramFlightCard);
      }
    }
  }
  
  public static class SegmentBuilder
  {
    private final FlightCard.StopBuilder mArrival = new FlightCard.StopBuilder();
    private final FlightCard.StopBuilder mDeparture = new FlightCard.StopBuilder();
    private int mStatusCode = 0;
    private boolean mUseDepartureForStatus;
    
    private void addTo(FlightCard paramFlightCard)
    {
      if (FlightCard.StopBuilder.access$300(this.mDeparture) != null) {}
      for (Calendar localCalendar = FlightCard.StopBuilder.access$300(this.mDeparture);; localCalendar = FlightCard.StopBuilder.access$400(this.mDeparture))
      {
        paramFlightCard.addSegment(this.mStatusCode, localCalendar, FlightCard.StopBuilder.access$500(this.mDeparture), FlightCard.StopBuilder.access$500(this.mArrival), this.mUseDepartureForStatus);
        FlightCard.StopBuilder.access$700(this.mDeparture, paramFlightCard, localCalendar);
        FlightCard.StopBuilder.access$700(this.mArrival, paramFlightCard, localCalendar);
        return;
      }
    }
    
    public FlightCard.StopBuilder arrival()
    {
      return this.mArrival;
    }
    
    public FlightCard.StopBuilder departure()
    {
      return this.mDeparture;
    }
    
    public SegmentBuilder setStatus(int paramInt)
    {
      this.mStatusCode = paramInt;
      return this;
    }
    
    public SegmentBuilder setUseDepartureForStatus(boolean paramBoolean)
    {
      this.mUseDepartureForStatus = paramBoolean;
      return this;
    }
  }
  
  public static class StopBuilder
  {
    private Calendar mActual;
    private String mAirportCode;
    private String mAirportName;
    private String mGate;
    private Calendar mScheduled;
    private String mTerminal;
    
    private void addTo(FlightCard paramFlightCard, Calendar paramCalendar)
    {
      paramFlightCard.populateStop(this.mAirportName, this.mAirportCode, this.mTerminal, this.mGate, this.mScheduled, this.mActual, paramCalendar);
    }
    
    public StopBuilder setActual(Calendar paramCalendar)
    {
      this.mActual = paramCalendar;
      return this;
    }
    
    public StopBuilder setAirportCode(String paramString)
    {
      this.mAirportCode = paramString;
      return this;
    }
    
    public StopBuilder setAirportName(String paramString)
    {
      this.mAirportName = paramString;
      return this;
    }
    
    public StopBuilder setGate(String paramString)
    {
      this.mGate = paramString;
      return this;
    }
    
    public StopBuilder setScheduled(Calendar paramCalendar)
    {
      this.mScheduled = paramCalendar;
      return this;
    }
    
    public StopBuilder setTerminal(String paramString)
    {
      this.mTerminal = paramString;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.FlightCard
 * JD-Core Version:    0.7.0.1
 */