package com.google.android.sidekick.shared.cards;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.Util;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.training.BackOfCardAdapter;
import com.google.android.sidekick.shared.training.TrainingBackOfCardAdapter;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.ClickActionHelper;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.MapsLauncher;
import com.google.android.sidekick.shared.util.MapsLauncher.TravelMode;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.ReminderData;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public abstract class BaseEntryAdapter
  implements EntryCardViewAdapter
{
  private static final View.OnTouchListener LAST_TOUCH_LISTENER = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      Point localPoint = (Point)paramAnonymousView.getTag(2131296288);
      if (localPoint == null) {
        localPoint = new Point();
      }
      localPoint.set((int)paramAnonymousMotionEvent.getX(), (int)paramAnonymousMotionEvent.getY());
      paramAnonymousView.setTag(2131296288, localPoint);
      return false;
    }
  };
  private final ActivityHelper mActivityHelper;
  private volatile Sidekick.Entry mEntry;
  private final Sidekick.EntryTreeNode mEntryTreeNode;
  
  public BaseEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    this.mEntryTreeNode = null;
    this.mEntry = ((Sidekick.Entry)Preconditions.checkNotNull(paramEntry));
    this.mActivityHelper = paramActivityHelper;
  }
  
  BaseEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode, ActivityHelper paramActivityHelper)
  {
    this.mEntryTreeNode = ((Sidekick.EntryTreeNode)Preconditions.checkNotNull(paramEntryTreeNode));
    this.mEntry = ((Sidekick.Entry)Preconditions.checkNotNull(paramEntryTreeNode.getGroupEntry()));
    this.mActivityHelper = paramActivityHelper;
  }
  
  @Nullable
  protected static Sidekick.Action findAction(Sidekick.Entry paramEntry, int paramInt)
  {
    Iterator localIterator = paramEntry.getEntryActionList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Action localAction = (Sidekick.Action)localIterator.next();
      if (localAction.getType() == paramInt) {
        return localAction;
      }
    }
    return null;
  }
  
  private String getRouteHtmlString(Sidekick.CommuteSummary paramCommuteSummary, Context paramContext, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramCommuteSummary.hasTravelTimeWithoutDelayInMinutes()) {}
    for (String str = TimeUtilities.getEtaString(paramContext, paramCommuteSummary, true); (str != null) && (paramCommuteSummary.hasRouteSummary()); str = null)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = str;
      arrayOfObject[1] = paramCommuteSummary.getRouteSummary();
      return paramContext.getString(paramInt1, arrayOfObject);
    }
    if (str != null) {
      return paramContext.getString(paramInt2, new Object[] { str });
    }
    return paramContext.getString(paramInt3);
  }
  
  protected void addFeedbackPrompt(ViewGroup paramViewGroup, View paramView)
  {
    paramViewGroup.addView(paramView);
  }
  
  protected void configureRouteButtons(View paramView, final Sidekick.CommuteSummary paramCommuteSummary, Context paramContext, final DirectionsLauncher paramDirectionsLauncher, PredictiveCardContainer paramPredictiveCardContainer, final Sidekick.Location paramLocation, boolean paramBoolean)
  {
    NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
    if (!localNavigationContext.shouldShowNavigation(paramLocation)) {
      return;
    }
    final MapsLauncher.TravelMode localTravelMode = paramDirectionsLauncher.getTravelMode(localNavigationContext, paramCommuteSummary);
    boolean bool = paramDirectionsLauncher.modeSupportsNavigation(localTravelMode);
    Button localButton1;
    Button localButton2;
    label69:
    String str;
    label88:
    Sidekick.Entry localEntry;
    if (bool)
    {
      localButton1 = (Button)paramView.findViewById(2131296334);
      localButton2 = (Button)paramView.findViewById(2131296335);
      if (!bool) {
        break label198;
      }
      str = getRouteHtmlString(paramCommuteSummary, paramContext, 2131362286, 2131362285, 2131362186);
      if (localButton1 != null)
      {
        localButton1.setText(Html.fromHtml(str));
        localEntry = getEntry();
        if (!bool) {
          break label215;
        }
      }
    }
    label198:
    label215:
    for (int i = 11;; i = 120)
    {
      localButton1.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, localEntry, i)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          if (paramCommuteSummary.getPathfinderWaypointCount() > 0) {}
          for (List localList = paramCommuteSummary.getPathfinderWaypointList();; localList = null)
          {
            paramDirectionsLauncher.start(paramLocation, localList, localTravelMode, MapsLauncher.getPersonalizedRouteToken(paramCommuteSummary));
            return;
          }
        }
      });
      if (!paramBoolean) {
        break;
      }
      if (localButton1 != null) {
        localButton1.setVisibility(0);
      }
      if (localButton2 == null) {
        break;
      }
      localButton2.setVisibility(8);
      return;
      localButton1 = (Button)paramView.findViewById(2131296335);
      localButton2 = (Button)paramView.findViewById(2131296334);
      break label69;
      str = getRouteHtmlString(paramCommuteSummary, paramContext, 2131362189, 2131362188, 2131362187);
      break label88;
    }
  }
  
  public BackOfCardAdapter createBackOfCardAdapter(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    return new TrainingBackOfCardAdapter(this, paramScheduledSingleThreadedExecutor);
  }
  
  @Nullable
  public View findViewForChildEntry(View paramView, Sidekick.Entry paramEntry)
  {
    List localList = (List)paramView.getTag(2131296281);
    if (localList != null)
    {
      ProtoKey localProtoKey = new ProtoKey(paramEntry);
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        View localView = (View)localIterator.next();
        Sidekick.Entry localEntry = (Sidekick.Entry)localView.getTag(2131296280);
        if ((localEntry != null) && (localProtoKey.equals(new ProtoKey(localEntry)))) {
          return localView;
        }
      }
    }
    return null;
  }
  
  protected ActivityHelper getActivityHelper()
  {
    return this.mActivityHelper;
  }
  
  @Nullable
  public Sidekick.Entry getDismissEntry()
  {
    return getEntry();
  }
  
  public final Sidekick.Entry getEntry()
  {
    return this.mEntry;
  }
  
  public final Sidekick.EntryTreeNode getGroupEntryTreeNode()
  {
    return this.mEntryTreeNode;
  }
  
  @Nullable
  protected Point getLastTouchPoint(View paramView)
  {
    return (Point)paramView.getTag(2131296288);
  }
  
  public String getLoggingName()
  {
    String str1 = getClass().getName();
    String str2 = Util.removeTrailingSuffix(Util.removeTrailingSuffix(str1.substring(1 + str1.lastIndexOf('.')), "Adapter"), "Entry");
    String str3 = ProtoUtils.getGenericEntryType(getEntry());
    if (str3 != null) {
      str2 = str2 + "(" + str3 + ")";
    }
    return str2;
  }
  
  @Nullable
  public CharSequence getReminderFormattedEventDate()
  {
    if ((this.mEntry.getReminderData() != null) && (this.mEntry.getReminderData().hasFormattedEventDate())) {
      return this.mEntry.getReminderData().getFormattedEventDate();
    }
    return null;
  }
  
  @Nullable
  protected View getViewToFocusForDetails(View paramView)
  {
    return null;
  }
  
  public void handleClickAction(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.ClickAction paramClickAction)
  {
    if (!ClickActionHelper.performClick(paramPredictiveCardContainer, paramClickAction, false)) {
      if (paramClickAction.hasAction()) {
        break label32;
      }
    }
    label32:
    for (int i = 2131363215;; i = 2131363306)
    {
      Toast.makeText(paramContext, i, 0).show();
      return;
    }
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView) {}
  
  public void maybeShowFeedbackPrompt(final PredictiveCardContainer paramPredictiveCardContainer, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater)
  {
    if (!this.mEntry.hasUserPrompt()) {}
    Sidekick.Action localAction1;
    Sidekick.Action localAction2;
    do
    {
      return;
      localAction1 = ProtoUtils.findAction(getEntry(), 25, new int[0]);
      localAction2 = ProtoUtils.findAction(getEntry(), 26, new int[0]);
    } while ((localAction1 == null) || (localAction2 == null));
    final View localView = paramLayoutInflater.inflate(2130968628, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296457)).setText(this.mEntry.getUserPrompt());
    Button localButton1 = (Button)localView.findViewById(2131296459);
    localButton1.setText(localAction1.getDisplayMessage());
    localButton1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramPredictiveCardContainer.recordFeedbackPromptAction(BaseEntryAdapter.this.mEntry, 25);
        localView.setVisibility(8);
      }
    });
    Button localButton2 = (Button)localView.findViewById(2131296458);
    localButton2.setText(localAction2.getDisplayMessage());
    localButton2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramPredictiveCardContainer.recordFeedbackPromptAction(BaseEntryAdapter.this.mEntry, 26);
        localView.setVisibility(8);
      }
    });
    addFeedbackPrompt(paramViewGroup, localView);
  }
  
  public void onDismiss(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    paramPredictiveCardContainer.dismissEntry(getDismissEntry());
    paramPredictiveCardContainer.logAnalyticsAction("DISMISS_CARD", getLoggingName());
  }
  
  public void onViewVisibleOnScreen(PredictiveCardContainer paramPredictiveCardContainer) {}
  
  protected void openUrl(Context paramContext, Uri paramUri)
  {
    openUrlWithMessage(paramContext, paramUri, 2131363215);
  }
  
  public void openUrl(Context paramContext, String paramString)
  {
    openUrl(paramContext, Uri.parse(paramString));
  }
  
  protected void openUrlWithMessage(Context paramContext, Uri paramUri, int paramInt)
  {
    this.mActivityHelper.safeViewUriWithMessage(paramContext, paramUri, false, paramInt);
  }
  
  public void registerActions(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView) {}
  
  public final void registerBackOfCardMenuListener(Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (!TrainingBackOfCardAdapter.isEnabledFor(this.mEntry)) {}
    View localView2;
    do
    {
      View localView1;
      do
      {
        return;
        localView1 = paramView.findViewById(2131296461);
      } while (localView1 == null);
      localView1.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 145)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          if (!paramAnonymousView.isSelected()) {}
          for (boolean bool = true;; bool = false)
          {
            paramAnonymousView.setSelected(bool);
            paramPredictiveCardContainer.toggleBackOfCard(BaseEntryAdapter.this);
            return;
          }
        }
      });
      localView1.setVisibility(0);
      localView2 = paramView.findViewById(2131296451);
    } while (localView2 == null);
    int i = paramContext.getResources().getDimensionPixelSize(2131689725);
    LayoutUtils.setPaddingRelative(localView2, LayoutUtils.getPaddingStart(localView2), localView2.getPaddingTop(), i, localView2.getPaddingBottom());
  }
  
  public void registerDetailsClickListener(final PredictiveCardContainer paramPredictiveCardContainer, final View paramView)
  {
    EntryClickListener local3 = new EntryClickListener(paramPredictiveCardContainer, getEntry(), 3)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        BaseEntryAdapter.this.launchDetails(paramAnonymousView.getContext(), paramPredictiveCardContainer, paramView);
      }
    };
    paramView.setOnClickListener(local3);
    View localView1 = getViewToFocusForDetails(paramView);
    if (localView1 != null)
    {
      localView1.setFocusable(true);
      localView1.setOnClickListener(local3);
      View localView2 = localView1.findViewById(2131296461);
      if (localView2 != null)
      {
        localView2.setNextFocusLeftId(localView1.getId());
        localView1.setNextFocusRightId(localView2.getId());
      }
    }
  }
  
  public void registerTouchListener(View paramView)
  {
    paramView.setOnTouchListener(LAST_TOUCH_LISTENER);
  }
  
  public void replaceEntry(Sidekick.Entry paramEntry)
  {
    this.mEntry = paramEntry;
  }
  
  public View updateView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, View paramView, Sidekick.Entry paramEntry)
  {
    return paramView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.BaseEntryAdapter
 * JD-Core Version:    0.7.0.1
 */