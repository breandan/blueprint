package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.IntentStarter.ResultCallback;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.training.TrainingBackOfCardAdapter;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.ClickActionHelper;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.ClickAction.Extra;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GenericCardEntry;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.XMLReader;

public class GenericCardEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.GenericCardEntry mGenericCardEntry;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final WifiManager mWifiManager;
  
  public GenericCardEntryAdapter(Sidekick.Entry paramEntry, WifiManager paramWifiManager, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mGenericCardEntry = paramEntry.getGenericCardEntry();
    this.mWifiManager = paramWifiManager;
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
  }
  
  private void createButton(Sidekick.ClickAction paramClickAction, Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, int paramInt)
  {
    if (!paramClickAction.hasLabel()) {}
    while ((!paramClickAction.hasUri()) && (!paramClickAction.hasAction()) && (!paramClickAction.getLatitudeOptInAction())) {
      return;
    }
    int i = -1;
    if (paramClickAction.hasIconType()) {
      i = ClickActionHelper.getIconResId(paramClickAction);
    }
    if (i == -1) {}
    for (int j = 2130968622;; j = 2130968624)
    {
      View localView = paramLayoutInflater.inflate(j, paramViewGroup, false);
      Button localButton = (Button)localView.findViewById(2131296449);
      localButton.setText(paramClickAction.getLabel());
      if (i != -1) {
        LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localButton, i, 0, 0, 0);
      }
      View.OnClickListener localOnClickListener = handleSpecialIntents(paramClickAction, paramContext, paramPredictiveCardContainer);
      if (localOnClickListener == null) {
        localOnClickListener = handleGenericIntent(paramClickAction, paramContext, paramPredictiveCardContainer, paramInt);
      }
      localButton.setOnClickListener(localOnClickListener);
      paramViewGroup.addView(localView);
      return;
    }
  }
  
  private Intent createIntent(Sidekick.ClickAction paramClickAction)
  {
    Intent localIntent;
    Iterator localIterator;
    if (paramClickAction.hasUri())
    {
      localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramClickAction.getUri()));
      localIntent.setFlags(268435456);
      localIterator = paramClickAction.getExtraList().iterator();
    }
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return localIntent;
      }
      Sidekick.ClickAction.Extra localExtra = (Sidekick.ClickAction.Extra)localIterator.next();
      if (localExtra.hasStringValue())
      {
        localIntent.putExtra(localExtra.getKey(), localExtra.getStringValue());
        continue;
        localIntent = new Intent(paramClickAction.getAction());
        break;
      }
      if (localExtra.hasLongValue()) {
        localIntent.putExtra(localExtra.getKey(), localExtra.getLongValue());
      } else if (localExtra.hasBoolValue()) {
        localIntent.putExtra(localExtra.getKey(), localExtra.getBoolValue());
      }
    }
    return localIntent;
  }
  
  private View.OnClickListener getLatitudeOptInOnClick(Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, Sidekick.ClickAction paramClickAction)
  {
    new EntryClickListener(paramPredictiveCardContainer, getEntry(), 129, paramClickAction)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        paramAnonymousView.setEnabled(false);
        paramPredictiveCardContainer.optIntoLocationReportingAsync();
        paramPredictiveCardContainer.dismissEntry(GenericCardEntryAdapter.this.getEntry());
      }
    };
  }
  
  private View.OnClickListener handleGenericIntent(final Sidekick.ClickAction paramClickAction, final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, int paramInt)
  {
    new EntryClickListener(paramPredictiveCardContainer, getEntry(), 129, paramClickAction)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        Intent localIntent = GenericCardEntryAdapter.this.createIntent(paramClickAction);
        GenericCardEntryAdapter.this.getActivityHelper().safeStartActivityWithMessage(paramContext, localIntent, 2131363306);
      }
    };
  }
  
  private boolean hasBackOfCardQuestion()
  {
    return TrainingBackOfCardAdapter.isEnabledFor(getEntry());
  }
  
  private boolean hasWifiChangePermission(Context paramContext)
  {
    return paramContext.getPackageManager().checkPermission("android.permission.CHANGE_WIFI_STATE", paramContext.getPackageName()) == 0;
  }
  
  private void setCardImage(PredictiveCardContainer paramPredictiveCardContainer, WebImageView paramWebImageView, Uri paramUri, int paramInt1, int paramInt2)
  {
    paramWebImageView.setVisibility(0);
    paramWebImageView.setImageUri(paramUri, paramPredictiveCardContainer.getImageLoader());
    if ((paramInt1 > 0) || (paramInt2 > 0))
    {
      if (paramInt1 <= 0) {
        break label52;
      }
      if (paramInt2 <= 0) {
        break label59;
      }
    }
    for (;;)
    {
      paramWebImageView.setLayoutParams(new LinearLayout.LayoutParams(paramInt1, paramInt2));
      return;
      label52:
      paramInt1 = -2;
      break;
      label59:
      paramInt2 = -2;
    }
  }
  
  private void setTemplateGridImages(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, LayoutInflater paramLayoutInflater, int paramInt)
  {
    if (this.mGenericCardEntry.getPhotoCount() > 0)
    {
      LinearLayout localLinearLayout = (LinearLayout)paramView.findViewById(2131296655);
      localLinearLayout.setVisibility(0);
      int i = 0;
      Iterator localIterator = this.mGenericCardEntry.getPhotoList().iterator();
      while (localIterator.hasNext())
      {
        Sidekick.Photo localPhoto = (Sidekick.Photo)localIterator.next();
        if (i < paramInt)
        {
          i++;
          WebImageView localWebImageView = (WebImageView)paramLayoutInflater.inflate(2130968701, localLinearLayout, false);
          localWebImageView.setImageUri(this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, localPhoto, 0, 0), paramPredictiveCardContainer.getImageLoader());
          localLinearLayout.addView(localWebImageView);
        }
      }
    }
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968700, paramViewGroup, false);
    ViewGroup localViewGroup1 = (ViewGroup)localView.findViewById(2131296450);
    TextView localTextView1;
    label138:
    int k;
    label220:
    WebImageView localWebImageView;
    if ((this.mGenericCardEntry.hasTitle()) || (getEntry().hasReason()) || (hasBackOfCardQuestion()))
    {
      localViewGroup1.setVisibility(0);
      if ((!getEntry().hasReason()) && (!hasBackOfCardQuestion())) {
        localViewGroup1.removeView(localView.findViewById(2131296461));
      }
      localTextView1 = (TextView)localView.findViewById(2131296451);
      if (!this.mGenericCardEntry.hasTitle()) {
        break label382;
      }
      localTextView1.setText(Html.fromHtml(this.mGenericCardEntry.getTitle()));
      localTextView1.setMaxLines(4);
      localTextView1.setVisibility(0);
      if (this.mGenericCardEntry.hasText())
      {
        TextView localTextView2 = (TextView)localView.findViewById(2131296653);
        localTextView2.setText(Html.fromHtml(this.mGenericCardEntry.getText(), null, new CustomTagHandler(null)));
        localTextView2.setVisibility(0);
      }
      if (this.mGenericCardEntry.getPhotoCount() <= 0) {
        break label443;
      }
      if (!this.mGenericCardEntry.hasTemplate()) {
        break label392;
      }
      k = this.mGenericCardEntry.getTemplate();
      if ((k != 1) && (k != 2)) {
        break label414;
      }
      if (k != 1) {
        break label398;
      }
      localWebImageView = (WebImageView)localView.findViewById(2131296652);
      label251:
      Sidekick.Photo localPhoto = this.mGenericCardEntry.getPhoto(0);
      setCardImage(paramPredictiveCardContainer, localWebImageView, this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, localPhoto, 0, 0), localPhoto.getWidth(), localPhoto.getHeight());
    }
    for (;;)
    {
      if (this.mGenericCardEntry.getViewActionCount() <= 0) {
        break label496;
      }
      ViewGroup localViewGroup2 = (ViewGroup)localView;
      int i = 0;
      Iterator localIterator = this.mGenericCardEntry.getViewActionList().iterator();
      while (localIterator.hasNext())
      {
        Sidekick.ClickAction localClickAction = (Sidekick.ClickAction)localIterator.next();
        int j = i + 1;
        createButton(localClickAction, paramContext, paramPredictiveCardContainer, paramLayoutInflater, localViewGroup2, i);
        i = j;
      }
      localViewGroup1.setVisibility(8);
      break;
      label382:
      localTextView1.setVisibility(8);
      break label138;
      label392:
      k = 2;
      break label220;
      label398:
      localWebImageView = (WebImageView)localView.findViewById(2131296654);
      break label251;
      label414:
      if (k == 3) {}
      for (int m = 1;; m = 4)
      {
        setTemplateGridImages(paramContext, paramPredictiveCardContainer, localView, paramLayoutInflater, m);
        break;
      }
      label443:
      if (this.mGenericCardEntry.hasImageUrl()) {
        setCardImage(paramPredictiveCardContainer, (WebImageView)localView.findViewById(2131296654), Uri.parse(this.mGenericCardEntry.getImageUrl()), this.mGenericCardEntry.getImageWidth(), this.mGenericCardEntry.getImageHeight());
      }
    }
    label496:
    return localView;
  }
  
  View.OnClickListener handleSpecialIntents(Sidekick.ClickAction paramClickAction, final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer)
  {
    String str1;
    String str2;
    label26:
    View.OnClickListener localOnClickListener;
    if (paramClickAction.hasAction())
    {
      str1 = paramClickAction.getAction();
      if (!paramClickAction.hasUri()) {
        break label62;
      }
      str2 = paramClickAction.getUri();
      if ((!paramClickAction.getLatitudeOptInAction()) && (!"com.google.android.apps.maps.LOCATION_SETTINGS".equals(str1))) {
        break label68;
      }
      localOnClickListener = getLatitudeOptInOnClick(paramContext, paramPredictiveCardContainer, paramClickAction);
    }
    label62:
    label68:
    boolean bool;
    do
    {
      return localOnClickListener;
      str1 = null;
      break;
      str2 = null;
      break label26;
      if (("android.settings.WIFI_SETTINGS".equals(str1)) && (hasWifiChangePermission(paramContext))) {
        new EntryClickListener(paramPredictiveCardContainer, getEntry(), 129, paramClickAction)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            GenericCardEntryAdapter.this.mWifiManager.setWifiEnabled(true);
          }
        };
      }
      if ((Build.VERSION.SDK_INT >= 18) && ("android.net.wifi.action.REQUEST_SCAN_ALWAYS_AVAILABLE".equals(str1))) {
        new EntryClickListener(paramPredictiveCardContainer, getEntry(), 129, paramClickAction)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            Intent localIntent = new Intent("android.net.wifi.action.REQUEST_SCAN_ALWAYS_AVAILABLE");
            paramPredictiveCardContainer.getIntentStarter().startActivityForResult(localIntent, new IntentStarter.ResultCallback()
            {
              public void onResult(int paramAnonymous2Int, Intent paramAnonymous2Intent, Context paramAnonymous2Context)
              {
                if (paramAnonymous2Int == -1) {
                  GenericCardEntryAdapter.2.this.val$cardContainer.dismissEntry(GenericCardEntryAdapter.this.getEntry(), false);
                }
              }
            });
          }
        };
      }
      bool = "https://www.google.com/history/settings".equals(str2);
      localOnClickListener = null;
    } while (!bool);
    new EntryClickListener(paramPredictiveCardContainer, getEntry(), 129, paramClickAction)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        paramPredictiveCardContainer.enableSearchHistoryForActiveAccount(new Consumer()
        {
          public boolean consume(Boolean paramAnonymous2Boolean)
          {
            if (paramAnonymous2Boolean.booleanValue()) {
              Toast.makeText(GenericCardEntryAdapter.3.this.val$context, 2131363157, 0).show();
            }
            for (;;)
            {
              return true;
              Toast.makeText(GenericCardEntryAdapter.3.this.val$context, 2131363158, 0).show();
            }
          }
        });
        paramPredictiveCardContainer.dismissEntry(GenericCardEntryAdapter.this.getEntry(), false);
      }
    };
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mGenericCardEntry.hasDetailsClickAction()) && (this.mGenericCardEntry.getDetailsClickAction().hasUri())) {
      getActivityHelper().safeViewUriWithMessage(paramContext, Uri.parse(this.mGenericCardEntry.getDetailsClickAction().getUri()), false, 2131363215);
    }
  }
  
  private static class CustomTagHandler
    implements Html.TagHandler
  {
    private static void end(Editable paramEditable, Class paramClass, Object paramObject)
    {
      int i = paramEditable.length();
      Object localObject = getLast(paramEditable, paramClass);
      int j = paramEditable.getSpanStart(localObject);
      paramEditable.removeSpan(localObject);
      if (j != i) {
        paramEditable.setSpan(paramObject, j, i, 33);
      }
    }
    
    private static Object getLast(Spanned paramSpanned, Class paramClass)
    {
      Object[] arrayOfObject = paramSpanned.getSpans(0, paramSpanned.length(), paramClass);
      if (arrayOfObject.length == 0) {
        return null;
      }
      return arrayOfObject[(-1 + arrayOfObject.length)];
    }
    
    private static void handleRelativeSizeTag(boolean paramBoolean, Editable paramEditable, Object paramObject, float paramFloat)
    {
      if (paramBoolean)
      {
        start(paramEditable, paramObject);
        return;
      }
      end(paramEditable, paramObject.getClass(), new RelativeSizeSpan(paramFloat));
    }
    
    private static void start(Editable paramEditable, Object paramObject)
    {
      int i = paramEditable.length();
      paramEditable.setSpan(paramObject, i, i, 17);
    }
    
    public void handleTag(boolean paramBoolean, String paramString, Editable paramEditable, XMLReader paramXMLReader)
    {
      if ("large".equalsIgnoreCase(paramString)) {
        handleRelativeSizeTag(paramBoolean, paramEditable, new Large(null), 2.0F);
      }
      do
      {
        return;
        if ("xlarge".equalsIgnoreCase(paramString))
        {
          handleRelativeSizeTag(paramBoolean, paramEditable, new XLarge(null), 3.0F);
          return;
        }
      } while (!"xxlarge".equalsIgnoreCase(paramString));
      handleRelativeSizeTag(paramBoolean, paramEditable, new XXLarge(null), 4.0F);
    }
    
    private static class Large {}
    
    private static class XLarge {}
    
    private static class XXLarge {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.GenericCardEntryAdapter
 * JD-Core Version:    0.7.0.1
 */