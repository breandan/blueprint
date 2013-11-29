package com.google.android.search.core.preferences.cards;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.Sports.SportTeamPlayer;
import com.google.geo.sidekick.Sidekick.SportsTeams;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public abstract class AddTeamDialogFragment
  extends DialogFragment
  implements TextWatcher
{
  private static final String TAG = Tag.getTag(AddTeamDialogFragment.class);
  private SportTeamListAdapter mAdapter;
  private EditText mTeamsFilter;
  private ListView mTeamsListView;
  
  private List<SportTeamPlayerWithName> createSortedSportsList(Sidekick.SportsTeams paramSportsTeams)
  {
    ArrayList localArrayList = new ArrayList(paramSportsTeams.getSportTeamPlayerCount());
    Iterator localIterator = paramSportsTeams.getSportTeamPlayerList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.SidekickConfiguration.Sports.SportTeamPlayer localSportTeamPlayer = (Sidekick.SidekickConfiguration.Sports.SportTeamPlayer)localIterator.next();
      localArrayList.add(new SportTeamPlayerWithName(localSportTeamPlayer, sportTeamToLabel(localSportTeamPlayer)));
    }
    Collections.sort(localArrayList);
    return localArrayList;
  }
  
  public static String sportTeamToLabel(Sidekick.SidekickConfiguration.Sports.SportTeamPlayer paramSportTeamPlayer)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramSportTeamPlayer.hasTeam())
    {
      localStringBuilder.append(paramSportTeamPlayer.getTeam());
      return localStringBuilder.toString();
    }
    return null;
  }
  
  public void afterTextChanged(Editable paramEditable)
  {
    String str = paramEditable.toString();
    if (TextUtils.isEmpty(str))
    {
      this.mTeamsListView.setVisibility(8);
      return;
    }
    this.mAdapter.filterBy(str);
    this.mTeamsListView.setVisibility(0);
  }
  
  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public abstract AdapterView.OnItemClickListener getOnItemClickListener();
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    try
    {
      Sidekick.SportsTeams localSportsTeams = Sidekick.SportsTeams.parseFrom(getArguments().getByteArray("sports_entries_extra"));
      List localList = createSortedSportsList(localSportsTeams);
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
      View localView = getActivity().getLayoutInflater().inflate(2130968585, null);
      this.mTeamsFilter = ((EditText)localView.findViewById(2131296303));
      this.mTeamsListView = ((ListView)localView.findViewById(2131296304));
      this.mTeamsListView.setEmptyView(localView.findViewById(16908292));
      this.mAdapter = new SportTeamListAdapter(getActivity(), localList);
      this.mTeamsListView.setAdapter(this.mAdapter);
      this.mTeamsListView.setOnItemClickListener(getOnItemClickListener());
      this.mTeamsListView.setVisibility(8);
      localBuilder.setView(localView);
      AlertDialog localAlertDialog = localBuilder.create();
      localAlertDialog.getWindow().setSoftInputMode(5);
      this.mTeamsFilter.addTextChangedListener(this);
      localAlertDialog.setOnShowListener(new DialogInterface.OnShowListener()
      {
        public void onShow(DialogInterface paramAnonymousDialogInterface)
        {
          View localView = ((AlertDialog)paramAnonymousDialogInterface).findViewById(16908290);
          if (localView != null) {
            localView.setOnClickListener(new View.OnClickListener()
            {
              public void onClick(View paramAnonymous2View)
              {
                AddTeamDialogFragment.this.dismiss();
              }
            });
          }
        }
      });
      return localAlertDialog;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      Log.e(TAG, "Failed to parse sports team list: ", localInvalidProtocolBufferMicroException);
    }
    return null;
  }
  
  public void onStart()
  {
    super.onStart();
    Dialog localDialog = getDialog();
    WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
    localLayoutParams.copyFrom(localDialog.getWindow().getAttributes());
    localLayoutParams.width = -1;
    localLayoutParams.height = -1;
    localDialog.getWindow().setAttributes(localLayoutParams);
  }
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void updateSportsEntities(Sidekick.SportsTeams paramSportsTeams)
  {
    if ((this.mAdapter != null) && (this.mTeamsListView != null))
    {
      this.mAdapter = new SportTeamListAdapter(getActivity(), createSortedSportsList(paramSportsTeams));
      this.mTeamsListView.setAdapter(this.mAdapter);
      afterTextChanged(this.mTeamsFilter.getEditableText());
    }
  }
  
  public static final class SportTeamListAdapter
    extends BaseAdapter
  {
    private static final Map<Integer, Integer> SPORT_ENUM_TO_ICON_ID = ImmutableMap.builder().put(Integer.valueOf(2), Integer.valueOf(2130837909)).put(Integer.valueOf(0), Integer.valueOf(2130837912)).put(Integer.valueOf(1), Integer.valueOf(2130837915)).put(Integer.valueOf(3), Integer.valueOf(2130837918)).put(Integer.valueOf(4), Integer.valueOf(2130837925)).put(Integer.valueOf(5), Integer.valueOf(2130837921)).build();
    private final Activity mActivity;
    private SportsFilter mFilter;
    private String mFilterConstraint;
    private List<AddTeamDialogFragment.SportTeamPlayerWithName> mFilteredItems;
    private final List<AddTeamDialogFragment.SportTeamPlayerWithName> mUnfilteredItems;
    
    public SportTeamListAdapter(Activity paramActivity, List<AddTeamDialogFragment.SportTeamPlayerWithName> paramList)
    {
      this.mUnfilteredItems = paramList;
      this.mFilteredItems = Collections.emptyList();
      this.mActivity = paramActivity;
    }
    
    void filterBy(String paramString)
    {
      this.mFilterConstraint = paramString.toLowerCase();
      getFilter().filter(this.mFilterConstraint);
    }
    
    public int getCount()
    {
      return this.mFilteredItems.size();
    }
    
    public Filter getFilter()
    {
      if (this.mFilter == null) {
        this.mFilter = new SportsFilter();
      }
      return this.mFilter;
    }
    
    public AddTeamDialogFragment.SportTeamPlayerWithName getItem(int paramInt)
    {
      return (AddTeamDialogFragment.SportTeamPlayerWithName)this.mFilteredItems.get(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null) {
        paramView = this.mActivity.getLayoutInflater().inflate(2130968834, null);
      }
      AddTeamDialogFragment.SportTeamPlayerWithName localSportTeamPlayerWithName = getItem(paramInt);
      Integer localInteger1 = Integer.valueOf(AddTeamDialogFragment.SportTeamPlayerWithName.access$000(localSportTeamPlayerWithName).getSport());
      ImageView localImageView;
      if (localInteger1 != null)
      {
        Integer localInteger2 = (Integer)SPORT_ENUM_TO_ICON_ID.get(localInteger1);
        localImageView = (ImageView)paramView.findViewById(16908294);
        if (localInteger2 == null) {
          break label124;
        }
        localImageView.setImageResource(localInteger2.intValue());
        localImageView.setVisibility(0);
      }
      for (;;)
      {
        if (this.mFilterConstraint != null) {
          ((TextView)paramView.findViewById(16908310)).setText(highlightQuery(localSportTeamPlayerWithName.toString(), this.mFilterConstraint));
        }
        return paramView;
        label124:
        localImageView.setVisibility(4);
      }
    }
    
    public CharSequence highlightQuery(CharSequence paramCharSequence, String paramString)
    {
      ArrayList localArrayList = Lists.newArrayList();
      for (String str : paramString.toString().split("\\s"))
      {
        Matcher localMatcher = Pattern.compile("\\b" + str, 2).matcher(paramCharSequence);
        if (localMatcher.find()) {
          localArrayList.add(new Pair(Integer.valueOf(localMatcher.start()), Integer.valueOf(localMatcher.end())));
        }
      }
      Collections.sort(localArrayList, new Comparator()
      {
        public int compare(Pair<Integer, Integer> paramAnonymousPair1, Pair<Integer, Integer> paramAnonymousPair2)
        {
          return ((Integer)paramAnonymousPair1.first).intValue() - ((Integer)paramAnonymousPair2.first).intValue();
        }
      });
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramCharSequence);
      int k = 0;
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if (k != ((Integer)localPair.first).intValue()) {
          localSpannableStringBuilder.setSpan(new TextAppearanceSpan(this.mActivity, 2131624094), k, ((Integer)localPair.first).intValue(), 0);
        }
        localSpannableStringBuilder.setSpan(new TextAppearanceSpan(this.mActivity, 2131624095), ((Integer)localPair.first).intValue(), ((Integer)localPair.second).intValue(), 0);
        k = ((Integer)localPair.second).intValue();
      }
      if (k < paramCharSequence.length()) {
        localSpannableStringBuilder.setSpan(new TextAppearanceSpan(this.mActivity, 2131624094), k, paramCharSequence.length(), 0);
      }
      return localSpannableStringBuilder;
    }
    
    public class SportsFilter
      extends Filter
    {
      TeamMatcher mTeamMatcher = new TeamMatcher(null);
      
      public SportsFilter() {}
      
      public List<AddTeamDialogFragment.SportTeamPlayerWithName> filterResults(CharSequence paramCharSequence)
      {
        Object localObject;
        if (paramCharSequence.toString().isEmpty()) {
          localObject = Collections.emptyList();
        }
        for (;;)
        {
          return localObject;
          localObject = Lists.newArrayList();
          Iterator localIterator = AddTeamDialogFragment.SportTeamListAdapter.this.mUnfilteredItems.iterator();
          while (localIterator.hasNext())
          {
            AddTeamDialogFragment.SportTeamPlayerWithName localSportTeamPlayerWithName = (AddTeamDialogFragment.SportTeamPlayerWithName)localIterator.next();
            if (this.mTeamMatcher.setMatcher(paramCharSequence.toString()).apply(localSportTeamPlayerWithName)) {
              ((List)localObject).add(localSportTeamPlayerWithName);
            }
          }
        }
      }
      
      protected Filter.FilterResults performFiltering(CharSequence paramCharSequence)
      {
        List localList = filterResults(paramCharSequence);
        Filter.FilterResults localFilterResults = new Filter.FilterResults();
        localFilterResults.values = localList;
        localFilterResults.count = localList.size();
        return localFilterResults;
      }
      
      protected void publishResults(CharSequence paramCharSequence, Filter.FilterResults paramFilterResults)
      {
        AddTeamDialogFragment.SportTeamListAdapter.access$402(AddTeamDialogFragment.SportTeamListAdapter.this, (List)paramFilterResults.values);
        if (paramFilterResults.count > 0)
        {
          AddTeamDialogFragment.SportTeamListAdapter.this.notifyDataSetChanged();
          return;
        }
        AddTeamDialogFragment.SportTeamListAdapter.this.notifyDataSetInvalidated();
      }
      
      private class TeamMatcher
        implements Predicate<AddTeamDialogFragment.SportTeamPlayerWithName>
      {
        private List<Pattern> mPatterns;
        
        private TeamMatcher() {}
        
        public boolean apply(@Nullable AddTeamDialogFragment.SportTeamPlayerWithName paramSportTeamPlayerWithName)
        {
          if (paramSportTeamPlayerWithName == null) {
            return false;
          }
          Iterator localIterator = this.mPatterns.iterator();
          while (localIterator.hasNext()) {
            if (!((Pattern)localIterator.next()).matcher(AddTeamDialogFragment.SportTeamPlayerWithName.access$100(paramSportTeamPlayerWithName)).find()) {
              return false;
            }
          }
          return true;
        }
        
        public TeamMatcher setMatcher(String paramString)
        {
          this.mPatterns = Lists.newArrayList();
          for (String str : paramString.split("\\s")) {
            this.mPatterns.add(Pattern.compile("\\b" + str, 2));
          }
          return this;
        }
      }
    }
  }
  
  public static class SportTeamPlayerWithName
    implements Comparable<SportTeamPlayerWithName>
  {
    private final String mName;
    private final Sidekick.SidekickConfiguration.Sports.SportTeamPlayer mSportTeamPlayer;
    
    public SportTeamPlayerWithName(Sidekick.SidekickConfiguration.Sports.SportTeamPlayer paramSportTeamPlayer, String paramString)
    {
      this.mSportTeamPlayer = paramSportTeamPlayer;
      this.mName = paramString;
    }
    
    public int compareTo(SportTeamPlayerWithName paramSportTeamPlayerWithName)
    {
      return this.mName.compareTo(paramSportTeamPlayerWithName.mName);
    }
    
    public Sidekick.SidekickConfiguration.Sports.SportTeamPlayer getSportTeamPlayer()
    {
      return this.mSportTeamPlayer;
    }
    
    public String toString()
    {
      return this.mName;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.AddTeamDialogFragment
 * JD-Core Version:    0.7.0.1
 */