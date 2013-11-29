package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActionLauncherUtil;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.ReminderData;
import com.google.geo.sidekick.Sidekick.ReminderData.SmartActionData;
import com.google.geo.sidekick.Sidekick.ReminderEntry;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class ReminderEntryAdapter
  extends BaseEntryAdapter
{
  private static final String TAG = Tag.getTag(ReminderEntryAdapter.class);
  private final Clock mClock;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  
  public ReminderEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    this.mClock = paramClock;
  }
  
  @Nullable
  public static CharSequence getSubtitleMessage(Context paramContext, Sidekick.ReminderEntry paramReminderEntry, long paramLong)
  {
    if (paramReminderEntry.hasSubtitle()) {
      return paramReminderEntry.getSubtitle();
    }
    return getTriggerMessage(paramContext, paramReminderEntry, paramLong);
  }
  
  @Nullable
  public static CharSequence getTriggerMessage(Context paramContext, Sidekick.ReminderEntry paramReminderEntry, long paramLong)
  {
    if (paramReminderEntry.hasTriggeringMessage()) {
      return paramReminderEntry.getTriggeringMessage();
    }
    if (paramReminderEntry.hasTriggerTimeSeconds())
    {
      long l = 1000L * paramReminderEntry.getTriggerTimeSeconds();
      int i;
      int i2;
      if (paramReminderEntry.hasResolution())
      {
        i = paramReminderEntry.getResolution();
        if ((i != 2) || (!paramReminderEntry.hasDayPart())) {
          break label332;
        }
        if (!DateUtils.isToday(l)) {
          break label150;
        }
        int i1 = paramReminderEntry.getDayPart();
        i2 = 0;
        switch (i1)
        {
        }
      }
      for (;;)
      {
        if (i2 == 0) {
          break label150;
        }
        return paramContext.getString(i2);
        i = 1;
        break;
        i2 = 2131362761;
        continue;
        i2 = 2131362762;
        continue;
        i2 = 2131362763;
        continue;
        i2 = 2131362764;
      }
      label150:
      if (TimeUtilities.isTomorrow(l, paramLong))
      {
        int m = paramReminderEntry.getDayPart();
        int n = 0;
        switch (m)
        {
        }
        while (n != 0)
        {
          return paramContext.getString(n);
          n = 2131362766;
          continue;
          n = 2131362767;
          continue;
          n = 2131362768;
          continue;
          n = 2131362769;
        }
      }
      if (TimeUtilities.wasYesterday(l, paramLong))
      {
        int j = paramReminderEntry.getDayPart();
        int k = 0;
        switch (j)
        {
        }
        while (k != 0)
        {
          return paramContext.getString(k);
          k = 2131362771;
          continue;
          k = 2131362772;
          continue;
          k = 2131362773;
          continue;
          k = 2131362774;
        }
      }
      label332:
      if (i == 3)
      {
        if (DateUtils.isToday(l)) {
          return paramContext.getString(2131362742);
        }
        if (TimeUtilities.isTomorrow(l, paramLong)) {
          return paramContext.getString(2131362765);
        }
        if (TimeUtilities.wasYesterday(l, paramLong)) {
          return paramContext.getString(2131362770);
        }
        return DateUtils.formatDateTime(paramContext, l, 18);
      }
      if (i == 4) {
        return paramContext.getString(2131362775);
      }
      return TimeUtilities.formatDisplayTime(paramContext, l, 19);
    }
    return null;
  }
  
  private void processSmartAction(View paramView, Sidekick.Entry paramEntry, final PredictiveCardContainer paramPredictiveCardContainer)
  {
    if ((!paramEntry.hasReminderData()) || (paramEntry.getReminderData().getSmartActionDataCount() == 0)) {}
    Integer localInteger1;
    Object localObject;
    Integer localInteger2;
    do
    {
      return;
      Iterator localIterator = paramEntry.getReminderData().getSmartActionDataList().iterator();
      Sidekick.ReminderData.SmartActionData localSmartActionData;
      for (;;)
      {
        boolean bool = localIterator.hasNext();
        localInteger1 = null;
        localObject = null;
        localInteger2 = null;
        if (!bool) {
          break;
        }
        localSmartActionData = (Sidekick.ReminderData.SmartActionData)localIterator.next();
        if ((ProtoUtils.findAction(paramEntry, localSmartActionData.getType(), new int[0]) != null) && (paramPredictiveCardContainer.isReminderSmartActionSupported(localSmartActionData.getType()))) {
          switch (localSmartActionData.getType())
          {
          default: 
            Log.w(TAG, "Unsupported smart action type: " + localSmartActionData.getType());
          }
        }
      }
      localInteger1 = Integer.valueOf(2130837636);
      localInteger2 = Integer.valueOf(2131363607);
      localObject = localSmartActionData;
    } while (localObject == null);
    Button localButton = (Button)paramView.findViewById(2131296943);
    localButton.setVisibility(0);
    if (localInteger1 != null) {
      LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localButton, localInteger1.intValue(), 0, 0, 0);
    }
    if (localInteger2 != null) {
      localButton.setText(localInteger2.intValue());
    }
    localButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramPredictiveCardContainer.startWebSearch(this.val$query, null);
      }
    });
  }
  
  private void updateAddressView(TextView paramTextView, Sidekick.ReminderEntry paramReminderEntry, CharSequence paramCharSequence)
  {
    boolean bool1 = paramReminderEntry.hasLocation();
    String str = null;
    if (bool1)
    {
      Sidekick.Location localLocation = paramReminderEntry.getLocation();
      boolean bool2 = localLocation.hasAddress();
      str = null;
      if (bool2)
      {
        str = localLocation.getAddress();
        if (paramCharSequence.toString().contains(str)) {
          str = null;
        }
      }
    }
    if (!TextUtils.isEmpty(str))
    {
      paramTextView.setText(str);
      paramTextView.setVisibility(0);
      return;
    }
    paramTextView.setVisibility(8);
  }
  
  public View getView(Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968804, paramViewGroup, false);
    if (!SharedPreferencesContext.fromCardContainer(paramPredictiveCardContainer).getBoolean("com.google.android.apps.sidekick.REMINDER_INTRO_DISPLAYED").booleanValue()) {
      localView.findViewById(2131296938).setVisibility(0);
    }
    final Sidekick.Entry localEntry = getEntry();
    Sidekick.ReminderEntry localReminderEntry = localEntry.getReminderEntry();
    TextView localTextView1 = (TextView)localView.findViewById(2131296451);
    if (localReminderEntry.hasReminderMessage()) {
      localTextView1.setText(localReminderEntry.getReminderMessage());
    }
    CharSequence localCharSequence = getSubtitleMessage(paramContext, localReminderEntry, this.mClock.currentTimeMillis());
    TextView localTextView2 = (TextView)localView.findViewById(2131296941);
    if (!TextUtils.isEmpty(localCharSequence)) {
      localTextView2.setText(localCharSequence);
    }
    for (;;)
    {
      if (localEntry.hasReminderData())
      {
        if (!TextUtils.isEmpty(localEntry.getReminderData().getSnoozeMessage()))
        {
          Button localButton = (Button)localView.findViewById(2131296942);
          localButton.setText(localEntry.getReminderData().getSnoozeMessage());
          localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, localEntry, 90)
          {
            public void onEntryClick(View paramAnonymousView)
            {
              paramPredictiveCardContainer.snoozeReminder(localEntry);
            }
          });
          localButton.setVisibility(0);
        }
        processSmartAction(localView, localEntry, paramPredictiveCardContainer);
      }
      if (localReminderEntry.hasImage()) {
        this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localView.findViewById(2131296939), localReminderEntry.getImage(), 2131689774, 2131689775);
      }
      updateAddressView((TextView)localView.findViewById(2131296914), localReminderEntry, localCharSequence);
      return localView;
      localTextView2.setVisibility(8);
    }
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    Sidekick.Entry localEntry = getEntry();
    Sidekick.Action localAction1 = ProtoUtils.findAction(localEntry, 13, new int[0]);
    Sidekick.Action localAction2 = ProtoUtils.findAction(localEntry, 32, new int[0]);
    Intent localIntent = ActionLauncherUtil.createActionLauncherIntent(paramContext);
    localIntent.putExtra("action_type", 147);
    localIntent.putExtra("entry", localEntry.toByteArray());
    ProtoUtils.putProtoExtra(localIntent, "action", localAction1);
    ProtoUtils.putProtoExtra(localIntent, "delete_action", localAction2);
    paramContext.startActivity(localIntent);
  }
  
  public void onDismiss(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    super.onDismiss(paramContext, paramPredictiveCardContainer);
    paramPredictiveCardContainer.deleteNotificationsForEntry(getEntry());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.ReminderEntryAdapter
 * JD-Core Version:    0.7.0.1
 */