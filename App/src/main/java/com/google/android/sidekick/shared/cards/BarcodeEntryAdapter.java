package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import com.google.android.search.shared.ui.CrossfadingWebImageView;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.GoogleServiceWebviewClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.GoogleServiceWebviewUtil;
import com.google.android.sidekick.shared.util.MoonshineUtilities;
import com.google.geo.sidekick.Sidekick.BarcodeEntry;
import com.google.geo.sidekick.Sidekick.BarcodeEntry.FlightBoardingPass;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GmailReference;
import com.google.geo.sidekick.Sidekick.Photo;

public class BarcodeEntryAdapter
  extends BaseEntryAdapter
{
  private static final Spanned EM_DASH = Html.fromHtml("&#8212;");
  private final Sidekick.BarcodeEntry mBarcodeEntry;
  
  public BarcodeEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mBarcodeEntry = paramEntry.getBarcodeEntry();
  }
  
  private void addBoardingPassToCard(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, View paramView)
  {
    if (!this.mBarcodeEntry.hasFlightBoardingPass()) {}
    Sidekick.BarcodeEntry.FlightBoardingPass localFlightBoardingPass;
    do
    {
      return;
      localFlightBoardingPass = this.mBarcodeEntry.getFlightBoardingPass();
      TextView localTextView1 = (TextView)paramView.findViewById(2131296451);
      localTextView1.setText(getTitle(paramContext, localFlightBoardingPass));
      if (localFlightBoardingPass.hasOperatingAirlineName())
      {
        TextView localTextView5 = (TextView)paramView.findViewById(2131296369);
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = BidiUtils.unicodeWrap(localFlightBoardingPass.getOperatingAirlineName());
        localTextView5.setText(paramContext.getString(2131362328, arrayOfObject2));
        localTextView5.setVisibility(0);
      }
      TextView localTextView2 = (TextView)paramView.findViewById(2131296371);
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = BidiUtils.unicodeWrap(localFlightBoardingPass.getPassengerName());
      localTextView2.setText(Html.fromHtml(paramContext.getString(2131362322, arrayOfObject1)));
      setBoardingPassData(paramContext, localFlightBoardingPass, (TableLayout)paramView.findViewById(2131296354));
      if (localFlightBoardingPass.hasTicketNumber())
      {
        TextView localTextView4 = (TextView)paramView.findViewById(2131296373);
        localTextView4.setText(localFlightBoardingPass.getTicketNumber());
        localTextView4.setVisibility(0);
      }
      if ((localFlightBoardingPass.hasAdditionalTicketText()) && (!localFlightBoardingPass.getAdditionalTicketText().isEmpty()))
      {
        TextView localTextView3 = (TextView)paramView.findViewById(2131296374);
        localTextView3.setText(localFlightBoardingPass.getAdditionalTicketText());
        localTextView3.setVisibility(0);
      }
      Button localButton1 = (Button)paramView.findViewById(2131296376);
      Sidekick.GmailReference localGmailReference = MoonshineUtilities.getEffectiveGmailReferenceAndSetText(paramContext, localButton1, localFlightBoardingPass.getGmailReferenceList());
      if (localGmailReference != null)
      {
        localButton1.setVisibility(0);
        localButton1.setOnClickListener(new GoogleServiceWebviewClickListener(paramContext, localGmailReference.getEmailUrl(), localTextView1.getText().toString(), false, this, 41, "mail", GoogleServiceWebviewUtil.GMAIL_URL_PREFIXES, null, paramPredictiveCardContainer));
      }
    } while (!localFlightBoardingPass.hasManageFlightUrl());
    final String str = localFlightBoardingPass.getManageFlightUrl();
    Button localButton2 = (Button)paramView.findViewById(2131296375);
    localButton2.setVisibility(0);
    localButton2.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 42)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        BarcodeEntryAdapter.this.openUrl(paramContext, str);
      }
    });
  }
  
  private View basicBarcodeView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968605, paramViewGroup, false);
    if ((this.mBarcodeEntry.hasBarcode()) && (this.mBarcodeEntry.getBarcode().hasUrl()))
    {
      CrossfadingWebImageView localCrossfadingWebImageView = (CrossfadingWebImageView)localView.findViewById(2131296372);
      localCrossfadingWebImageView.setImageUrl(this.mBarcodeEntry.getBarcode().getUrl(), paramPredictiveCardContainer.getImageLoader());
      localCrossfadingWebImageView.setVisibility(0);
    }
    return localView;
  }
  
  private CharSequence getTitle(Context paramContext, Sidekick.BarcodeEntry.FlightBoardingPass paramFlightBoardingPass)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = BidiUtils.unicodeWrap(paramFlightBoardingPass.getAirlineName());
    arrayOfObject[1] = BidiUtils.unicodeWrap(paramFlightBoardingPass.getFlightNumber());
    return Html.fromHtml(paramContext.getString(2131362327, arrayOfObject));
  }
  
  private void setBoardingPassData(Context paramContext, Sidekick.BarcodeEntry.FlightBoardingPass paramFlightBoardingPass, TableLayout paramTableLayout)
  {
    TextView localTextView1 = (TextView)paramTableLayout.findViewById(2131296366);
    Object localObject1;
    Object localObject2;
    label55:
    Object localObject3;
    label86:
    Object localObject4;
    label117:
    TextView localTextView5;
    TextView localTextView6;
    TextView localTextView7;
    label257:
    TextView localTextView8;
    if (paramFlightBoardingPass.hasTerminal())
    {
      localObject1 = paramFlightBoardingPass.getTerminal();
      localTextView1.setText((CharSequence)localObject1);
      TextView localTextView2 = (TextView)paramTableLayout.findViewById(2131296364);
      if (!paramFlightBoardingPass.hasGate()) {
        break label298;
      }
      localObject2 = paramFlightBoardingPass.getGate();
      localTextView2.setText((CharSequence)localObject2);
      TextView localTextView3 = (TextView)paramTableLayout.findViewById(2131296362);
      if (!paramFlightBoardingPass.hasSeat()) {
        break label306;
      }
      localObject3 = paramFlightBoardingPass.getSeat();
      localTextView3.setText((CharSequence)localObject3);
      TextView localTextView4 = (TextView)paramTableLayout.findViewById(2131296360);
      if (!paramFlightBoardingPass.hasGroup()) {
        break label314;
      }
      localObject4 = paramFlightBoardingPass.getGroup();
      localTextView4.setText((CharSequence)localObject4);
      localTextView5 = (TextView)paramTableLayout.findViewById(2131296356);
      localTextView6 = (TextView)paramTableLayout.findViewById(2131296367);
      localTextView7 = (TextView)paramTableLayout.findViewById(2131296368);
      if (!paramFlightBoardingPass.hasAirportCode()) {
        break label341;
      }
      localTextView5.setText(paramFlightBoardingPass.getAirportCode());
      if (!paramFlightBoardingPass.hasDepartureTimeInMs()) {
        break label322;
      }
      Long localLong = Long.valueOf(paramFlightBoardingPass.getDepartureTimeInMs());
      String str = DateUtils.formatDateTime(paramContext, localLong.longValue(), 1);
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = BidiUtils.unicodeWrap(paramFlightBoardingPass.getAirportCode());
      arrayOfObject[1] = str;
      localTextView6.setText(paramContext.getString(2131362330, arrayOfObject));
      localTextView7.setText(DateUtils.formatDateTime(paramContext, localLong.longValue(), 524310));
      localTextView8 = (TextView)paramTableLayout.findViewById(2131296358);
      if (!paramFlightBoardingPass.hasArrivalAirportCode()) {
        break label368;
      }
    }
    label298:
    label306:
    label314:
    label322:
    label341:
    label368:
    for (Object localObject5 = paramFlightBoardingPass.getArrivalAirportCode();; localObject5 = EM_DASH)
    {
      localTextView8.setText((CharSequence)localObject5);
      return;
      localObject1 = EM_DASH;
      break;
      localObject2 = EM_DASH;
      break label55;
      localObject3 = EM_DASH;
      break label86;
      localObject4 = EM_DASH;
      break label117;
      localTextView6.setText(EM_DASH);
      localTextView7.setText(EM_DASH);
      break label257;
      localTextView5.setText(EM_DASH);
      localTextView6.setText(EM_DASH);
      localTextView7.setText(EM_DASH);
      break label257;
    }
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = basicBarcodeView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
    if (this.mBarcodeEntry.hasFlightBoardingPass()) {
      addBoardingPassToCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, localView);
    }
    return localView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.BarcodeEntryAdapter
 * JD-Core Version:    0.7.0.1
 */