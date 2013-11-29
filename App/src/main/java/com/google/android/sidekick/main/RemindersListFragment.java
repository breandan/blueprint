package com.google.android.sidekick.main;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.preferences.NowConfigurationListFragment;
import com.google.android.search.core.preferences.cards.AbstractMyStuffSettingsFragment;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.actions.DeleteReminderDialogFragment;
import com.google.android.sidekick.main.actions.EditReminderDialogFragment;
import com.google.android.sidekick.main.actions.EditReminderDialogFragment.EditReminderDialogListener;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.cards.ReminderEntryAdapter;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.util.IntentUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.ClientUserData;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryQuery;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.MinimumDataVersion;
import com.google.geo.sidekick.Sidekick.ReminderEntry;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class RemindersListFragment
  extends NowConfigurationListFragment
  implements EditReminderDialogFragment.EditReminderDialogListener
{
  public static final String TAG = Tag.getTag(RemindersListFragment.class);
  private Clock mClock;
  private AsyncTask<Void, Void, List<Sidekick.Entry>> mFetchRemindersTask;
  protected BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ("com.google.android.apps.sidekick.DATA_BACKEND_VERSION_STORE".equals(paramAnonymousIntent.getAction())) {
        RemindersListFragment.this.fetchReminders(null);
      }
    }
  };
  private NetworkClient mNetworkClient;
  private String mOldDataVersion;
  private SidekickInjector mSidekickInjector;
  private UserInteractionLogger mUserInteractionLogger;
  
  private void fetchReminders(@Nullable Bundle paramBundle)
  {
    if ((paramBundle != null) && (paramBundle.containsKey("REMINDERS_KEY")) && (!paramBundle.getBoolean("FORCE_REFRESH_KEY", false))) {
      try
      {
        setReminders(Lists.newArrayList(Sidekick.EntryTreeNode.parseFrom(paramBundle.getByteArray("REMINDERS_KEY")).getEntryList()));
        return;
      }
      catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {}
    }
    this.mFetchRemindersTask = new FetchRemindersTask(null).execute(new Void[0]);
  }
  
  @Nullable
  private String getKansasVersionInfo()
  {
    Iterator localIterator = this.mSidekickInjector.getDataBackendVersionStore().getMinimumDataVersions().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.MinimumDataVersion localMinimumDataVersion = (Sidekick.MinimumDataVersion)localIterator.next();
      if (localMinimumDataVersion.getStorageBackend() == 1) {
        return localMinimumDataVersion.getVersion();
      }
    }
    return null;
  }
  
  private LocalBroadcastManager getLocalBroadcastManager()
  {
    return LocalBroadcastManager.getInstance(getActivity());
  }
  
  private void restoreFromBundle(Bundle paramBundle)
  {
    if ((paramBundle != null) && (paramBundle.containsKey("KANSAS_VERSION_KEY")))
    {
      this.mOldDataVersion = paramBundle.getString("KANSAS_VERSION_KEY");
      if (!this.mOldDataVersion.equals(getKansasVersionInfo()))
      {
        fetchReminders(null);
        this.mOldDataVersion = null;
        return;
      }
    }
    fetchReminders(paramBundle);
  }
  
  private void setReminders(@Nullable List<Sidekick.Entry> paramList)
  {
    Activity localActivity = getActivity();
    if (localActivity == null) {
      return;
    }
    ReminderListAdapter localReminderListAdapter = (ReminderListAdapter)getListAdapter();
    if (localReminderListAdapter == null)
    {
      setListAdapter(new ReminderListAdapter(localActivity, localActivity.getLayoutInflater(), paramList, this.mClock));
      return;
    }
    localReminderListAdapter.setReminders(paramList);
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    restoreFromBundle(paramBundle);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    VelvetServices localVelvetServices = VelvetServices.get();
    CoreSearchServices localCoreSearchServices = localVelvetServices.getCoreServices();
    this.mSidekickInjector = localVelvetServices.getSidekickInjector();
    this.mNetworkClient = this.mSidekickInjector.getNetworkClient();
    this.mClock = localCoreSearchServices.getClock();
    this.mUserInteractionLogger = localCoreSearchServices.getUserInteractionLogger();
    setHasOptionsMenu(true);
  }
  
  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    final Context localContext = getActivity().getApplicationContext();
    paramMenuInflater.inflate(2131886080, paramMenu);
    MenuItem localMenuItem = paramMenu.findItem(2131297280);
    localMenuItem.setTitle(getString(2131362736));
    localMenuItem.setShowAsAction(2);
    localMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
    {
      public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem)
      {
        RemindersListFragment.this.mUserInteractionLogger.logAnalyticsAction("BUTTON_PRESS", "ADD_REMINDER");
        if (RemindersListFragment.this.mFetchRemindersTask != null)
        {
          RemindersListFragment.this.mFetchRemindersTask.cancel(true);
          RemindersListFragment.access$302(RemindersListFragment.this, null);
        }
        RemindersListFragment.access$402(RemindersListFragment.this, RemindersListFragment.this.getKansasVersionInfo());
        Intent localIntent = IntentUtils.createResumeVelvetWithQueryIntent(localContext, Query.EMPTY.withQueryChars(localContext.getString(2131362736)));
        RemindersListFragment.this.startActivity(localIntent);
        return true;
      }
    });
    AbstractMyStuffSettingsFragment.addOrFixHelpMenuItem(localContext, paramMenu, "reminders");
  }
  
  public void onDestroy()
  {
    if (this.mFetchRemindersTask != null)
    {
      this.mFetchRemindersTask.cancel(true);
      this.mFetchRemindersTask = null;
    }
    super.onDestroy();
  }
  
  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    Object localObject = ((ReminderListAdapter)getListAdapter()).getItem(paramInt);
    if (!(localObject instanceof Sidekick.Entry)) {}
    Sidekick.Entry localEntry;
    do
    {
      return;
      localEntry = (Sidekick.Entry)localObject;
      if (ProtoUtils.findAction(localEntry, 13, new int[0]) != null)
      {
        EditReminderDialogFragment.newInstance(this, localEntry).show(getFragmentManager(), "edit_reminder");
        return;
      }
    } while (ProtoUtils.findAction(localEntry, 32, new int[] { 146 }) == null);
    DeleteReminderDialogFragment.newInstance(this, localEntry).show(getFragmentManager(), "delete_reminder");
  }
  
  public void onPause()
  {
    super.onPause();
    getLocalBroadcastManager().unregisterReceiver(this.mMessageReceiver);
  }
  
  public void onReminderDeleted(String paramString)
  {
    ReminderListAdapter localReminderListAdapter = (ReminderListAdapter)getListAdapter();
    for (int i = 0;; i++) {
      if (i < localReminderListAdapter.getCount())
      {
        Object localObject = localReminderListAdapter.getItem(i);
        if (((localObject instanceof Sidekick.Entry)) && (paramString.equals(((Sidekick.Entry)localObject).getReminderEntry().getTaskId()))) {
          localReminderListAdapter.removeItem(i);
        }
      }
      else
      {
        return;
      }
    }
  }
  
  public void onReminderEdited()
  {
    fetchReminders(null);
  }
  
  public void onResume()
  {
    super.onResume();
    getLocalBroadcastManager().registerReceiver(this.mMessageReceiver, new IntentFilter("com.google.android.apps.sidekick.DATA_BACKEND_VERSION_STORE"));
    if ((this.mOldDataVersion == null) || (!this.mOldDataVersion.equals(getKansasVersionInfo())))
    {
      fetchReminders(null);
      this.mOldDataVersion = null;
    }
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mOldDataVersion != null) {
      paramBundle.putString("KANSAS_VERSION_KEY", this.mOldDataVersion);
    }
    ReminderListAdapter localReminderListAdapter = (ReminderListAdapter)getListAdapter();
    if (localReminderListAdapter != null)
    {
      Sidekick.EntryTreeNode localEntryTreeNode = new Sidekick.EntryTreeNode();
      Iterator localIterator = localReminderListAdapter.getAllRemindersList().iterator();
      while (localIterator.hasNext()) {
        localEntryTreeNode.addEntry((Sidekick.Entry)localIterator.next());
      }
      paramBundle.putByteArray("REMINDERS_KEY", localEntryTreeNode.toByteArray());
    }
  }
  
  private class FetchRemindersTask
    extends AsyncTask<Void, Void, List<Sidekick.Entry>>
  {
    private FetchRemindersTask() {}
    
    protected List<Sidekick.Entry> doInBackground(Void... paramVarArgs)
    {
      ArrayList localArrayList = Lists.newArrayList();
      Sidekick.Interest localInterest = new Sidekick.Interest().setTargetDisplay(6).addEntryTypeRestrict(43);
      Collection localCollection = RemindersListFragment.this.mSidekickInjector.getDataBackendVersionStore().getMinimumDataVersions();
      Sidekick.ClientUserData localClientUserData = new Sidekick.ClientUserData();
      Iterator localIterator1 = localCollection.iterator();
      while (localIterator1.hasNext()) {
        localClientUserData.addMinimumDataVersion((Sidekick.MinimumDataVersion)localIterator1.next());
      }
      Sidekick.EntryQuery localEntryQuery = new Sidekick.EntryQuery().addInterest(localInterest).setClientUserData(localClientUserData);
      Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setEntryQuery(localEntryQuery);
      Sidekick.ResponsePayload localResponsePayload = RemindersListFragment.this.mNetworkClient.sendRequestWithoutLocation(localRequestPayload);
      if (localResponsePayload == null) {
        localArrayList = null;
      }
      while (!localResponsePayload.hasEntryResponse()) {
        return localArrayList;
      }
      Iterator localIterator2 = localResponsePayload.getEntryResponse().getEntryTreeList().iterator();
      while (localIterator2.hasNext())
      {
        Iterator localIterator3 = ((Sidekick.EntryTree)localIterator2.next()).getRoot().getEntryList().iterator();
        while (localIterator3.hasNext())
        {
          Sidekick.Entry localEntry = (Sidekick.Entry)localIterator3.next();
          if (localEntry.hasReminderEntry()) {
            localArrayList.add(localEntry);
          }
        }
      }
    }
    
    protected void onPostExecute(List<Sidekick.Entry> paramList)
    {
      RemindersListFragment.access$302(RemindersListFragment.this, null);
      RemindersListFragment.this.setReminders(paramList);
    }
  }
  
  private static class ReminderListAdapter
    extends BaseAdapter
  {
    private static final Predicate<Sidekick.Entry> ARCHIVED_REMINDER_PREDICATE = new Predicate()
    {
      public boolean apply(@Nullable Sidekick.Entry paramAnonymousEntry)
      {
        return (paramAnonymousEntry.getReminderEntry().getState() == 2) && (!RemindersListFragment.ReminderListAdapter.isRecurringReminder(paramAnonymousEntry));
      }
    };
    private static final Predicate<Sidekick.Entry> ONGOING_REMINDER_PREDICATE = new Predicate()
    {
      public boolean apply(@Nullable Sidekick.Entry paramAnonymousEntry)
      {
        return (paramAnonymousEntry.getReminderEntry().getState() == 3) || (RemindersListFragment.ReminderListAdapter.isRecurringReminder(paramAnonymousEntry));
      }
    };
    private static final Predicate<Sidekick.Entry> UPCOMING_REMINDER_PREDICATE = new Predicate()
    {
      public boolean apply(@Nullable Sidekick.Entry paramAnonymousEntry)
      {
        return (paramAnonymousEntry.getReminderEntry().getState() == 1) && (!RemindersListFragment.ReminderListAdapter.isRecurringReminder(paramAnonymousEntry));
      }
    };
    private final Clock mClock;
    private final Context mContext;
    private final List<ListItem> mItems;
    private final LayoutInflater mLayoutInflater;
    
    public ReminderListAdapter(Context paramContext, LayoutInflater paramLayoutInflater, @Nullable List<Sidekick.Entry> paramList, Clock paramClock)
    {
      this.mContext = paramContext;
      this.mLayoutInflater = paramLayoutInflater;
      if (paramList == null) {}
      for (int i = 0;; i = paramList.size())
      {
        this.mItems = Lists.newArrayListWithCapacity(i + 3);
        updateReminderLists(paramList);
        this.mClock = paramClock;
        return;
      }
    }
    
    private View getHeaderView(View paramView, int paramInt)
    {
      if (paramView == null) {
        paramView = this.mLayoutInflater.inflate(2130968808, null);
      }
      ((TextView)paramView.findViewById(16908310)).setText((String)getItem(paramInt));
      return paramView;
    }
    
    private View getMessageView(View paramView, int paramInt)
    {
      if (paramView == null) {
        paramView = this.mLayoutInflater.inflate(2130968807, null);
      }
      TextView localTextView = (TextView)paramView.findViewById(16908310);
      localTextView.setMaxLines(10);
      localTextView.setText((String)getItem(paramInt));
      localTextView.setTextColor(this.mContext.getResources().getColor(2131230753));
      localTextView.setTypeface(localTextView.getTypeface(), 2);
      return paramView;
    }
    
    private View getUpcomingReminderView(View paramView, int paramInt)
    {
      if (paramView == null) {
        paramView = this.mLayoutInflater.inflate(2130968805, null);
      }
      TextView localTextView1 = (TextView)paramView.findViewById(16908310);
      TextView localTextView2 = (TextView)paramView.findViewById(16908304);
      TextView localTextView3 = (TextView)paramView.findViewById(2131296309);
      Sidekick.ReminderEntry localReminderEntry = ((Sidekick.Entry)getItem(paramInt)).getReminderEntry();
      localTextView1.setText(localReminderEntry.getReminderMessage());
      Object localObject = ReminderEntryAdapter.getTriggerMessage(paramView.getContext(), localReminderEntry, this.mClock.currentTimeMillis());
      boolean bool1 = localReminderEntry.hasLocation();
      String str = null;
      if (bool1)
      {
        Sidekick.Location localLocation = localReminderEntry.getLocation();
        boolean bool2 = localLocation.hasAddress();
        str = null;
        if (bool2)
        {
          boolean bool3 = localLocation.getAddress().equals(localLocation.getName());
          str = null;
          if (!bool3) {
            str = localLocation.getAddress();
          }
        }
      }
      if (!TextUtils.isEmpty(str))
      {
        if (TextUtils.isEmpty((CharSequence)localObject)) {
          localObject = str;
        }
      }
      else
      {
        if (TextUtils.isEmpty((CharSequence)localObject)) {
          break label278;
        }
        localTextView2.setText((CharSequence)localObject);
        localTextView2.setVisibility(0);
      }
      for (;;)
      {
        if ((!localReminderEntry.hasSubtitle()) || (TextUtils.isEmpty(localReminderEntry.getSubtitle()))) {
          break label288;
        }
        localTextView3.setText(BidiUtils.unicodeWrap(localReminderEntry.getSubtitle()));
        localTextView3.setVisibility(0);
        return paramView;
        Context localContext = this.mContext;
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = localObject;
        arrayOfObject[1] = BidiUtils.unicodeWrap(str);
        localObject = localContext.getString(2131362737, arrayOfObject);
        break;
        label278:
        localTextView2.setVisibility(8);
      }
      label288:
      localTextView3.setVisibility(8);
      return paramView;
    }
    
    private static boolean isRecurringReminder(Sidekick.Entry paramEntry)
    {
      return paramEntry.getReminderEntry().hasRecurrenceInfo();
    }
    
    private boolean isReminderType(int paramInt)
    {
      return (paramInt != -1) && (paramInt != -2);
    }
    
    private void updateReminderLists(@Nullable List<Sidekick.Entry> paramList)
    {
      this.mItems.clear();
      if (paramList == null) {
        this.mItems.add(new ListItem(-2, this.mContext.getString(2131362173), null));
      }
      for (;;)
      {
        return;
        this.mItems.add(new ListItem(-1, this.mContext.getString(2131362730), null));
        ImmutableList localImmutableList1 = ImmutableList.copyOf(Iterables.filter(paramList, UPCOMING_REMINDER_PREDICATE));
        if (localImmutableList1.isEmpty()) {
          this.mItems.add(new ListItem(-2, this.mContext.getString(2131362733), null));
        }
        for (;;)
        {
          ImmutableList localImmutableList2 = ImmutableList.copyOf(Iterables.filter(paramList, ONGOING_REMINDER_PREDICATE));
          if (localImmutableList2.isEmpty()) {
            break;
          }
          this.mItems.add(new ListItem(-1, this.mContext.getString(2131362731), null));
          Iterator localIterator3 = localImmutableList2.iterator();
          while (localIterator3.hasNext())
          {
            Sidekick.Entry localEntry3 = (Sidekick.Entry)localIterator3.next();
            this.mItems.add(new ListItem(localEntry3.getReminderEntry().getState(), null, localEntry3));
          }
          Iterator localIterator1 = localImmutableList1.iterator();
          while (localIterator1.hasNext())
          {
            Sidekick.Entry localEntry1 = (Sidekick.Entry)localIterator1.next();
            this.mItems.add(new ListItem(localEntry1.getReminderEntry().getState(), null, localEntry1));
          }
        }
        ImmutableList localImmutableList3 = ImmutableList.copyOf(Iterables.filter(paramList, ARCHIVED_REMINDER_PREDICATE));
        if (!localImmutableList3.isEmpty())
        {
          this.mItems.add(new ListItem(-1, this.mContext.getString(2131362732), null));
          Iterator localIterator2 = localImmutableList3.iterator();
          while (localIterator2.hasNext())
          {
            Sidekick.Entry localEntry2 = (Sidekick.Entry)localIterator2.next();
            this.mItems.add(new ListItem(localEntry2.getReminderEntry().getState(), null, localEntry2));
          }
        }
      }
    }
    
    public boolean areAllItemsEnabled()
    {
      return false;
    }
    
    public List<Sidekick.Entry> getAllRemindersList()
    {
      ArrayList localArrayList = Lists.newArrayListWithCapacity(this.mItems.size());
      Iterator localIterator = this.mItems.iterator();
      while (localIterator.hasNext())
      {
        ListItem localListItem = (ListItem)localIterator.next();
        if (isReminderType(localListItem.mType)) {
          localArrayList.add(localListItem.mEntry);
        }
      }
      return localArrayList;
    }
    
    public View getArchivedReminderView(View paramView, int paramInt)
    {
      if (paramView == null) {
        paramView = this.mLayoutInflater.inflate(2130968807, null);
      }
      ((TextView)paramView.findViewById(16908310)).setText(((Sidekick.Entry)getItem(paramInt)).getReminderEntry().getReminderMessage());
      return paramView;
    }
    
    public int getCount()
    {
      return this.mItems.size();
    }
    
    public Object getItem(int paramInt)
    {
      ListItem localListItem = (ListItem)this.mItems.get(paramInt);
      if (!isReminderType(localListItem.mType)) {
        return localListItem.mLabel;
      }
      return localListItem.mEntry;
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public int getItemViewType(int paramInt)
    {
      return ((ListItem)this.mItems.get(paramInt)).mType;
    }
    
    public View getOngoingReminderView(View paramView, int paramInt)
    {
      if (paramView == null) {
        paramView = this.mLayoutInflater.inflate(2130968805, null);
      }
      TextView localTextView1 = (TextView)paramView.findViewById(16908310);
      Sidekick.ReminderEntry localReminderEntry = ((Sidekick.Entry)getItem(paramInt)).getReminderEntry();
      localTextView1.setText(localReminderEntry.getReminderMessage());
      TextView localTextView2 = (TextView)paramView.findViewById(16908304);
      if (!TextUtils.isEmpty(localReminderEntry.getTriggeringMessage()))
      {
        localTextView2.setText(localReminderEntry.getTriggeringMessage());
        localTextView2.setVisibility(0);
        return paramView;
      }
      localTextView2.setVisibility(8);
      return paramView;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      switch (getItemViewType(paramInt))
      {
      case 0: 
      case 1: 
      default: 
        return getUpcomingReminderView(paramView, paramInt);
      case -1: 
        return getHeaderView(paramView, paramInt);
      case -2: 
        return getMessageView(paramView, paramInt);
      case 2: 
        return getArchivedReminderView(paramView, paramInt);
      }
      return getOngoingReminderView(paramView, paramInt);
    }
    
    public int getViewTypeCount()
    {
      return 4;
    }
    
    public boolean hasStableIds()
    {
      return false;
    }
    
    public boolean isEnabled(int paramInt)
    {
      return isReminderType(getItemViewType(paramInt));
    }
    
    public void removeItem(int paramInt)
    {
      if (!isReminderType(((ListItem)this.mItems.get(paramInt)).mType)) {
        return;
      }
      this.mItems.remove(paramInt);
      notifyDataSetChanged();
    }
    
    public void setReminders(@Nullable List<Sidekick.Entry> paramList)
    {
      updateReminderLists(paramList);
      notifyDataSetChanged();
    }
    
    static class ListItem
    {
      final Sidekick.Entry mEntry;
      final String mLabel;
      final int mType;
      
      ListItem(int paramInt, @Nullable String paramString, @Nullable Sidekick.Entry paramEntry)
      {
        this.mType = paramInt;
        this.mLabel = paramString;
        this.mEntry = paramEntry;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.RemindersListFragment
 * JD-Core Version:    0.7.0.1
 */