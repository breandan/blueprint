package com.google.android.sidekick.main.actions;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.PlaceData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class RenamePlaceDialogFragment
  extends BaseEditDialogFragment
{
  private String mCurrentName;
  private Sidekick.Action mDeleteAction;
  private Sidekick.Entry mEntry;
  private Sidekick.FrequentPlace mFrequentPlace;
  private Sidekick.Action mRenameAction;
  
  public static RenamePlaceDialogFragment newInstance(Intent paramIntent)
  {
    Bundle localBundle = paramIntent.getExtras();
    Sidekick.Entry localEntry = (Sidekick.Entry)ProtoUtils.getProtoExtra(localBundle, "entry", Sidekick.Entry.class);
    Sidekick.Action localAction = (Sidekick.Action)ProtoUtils.getProtoExtra(localBundle, "action", Sidekick.Action.class);
    if ((localEntry == null) || (localAction == null)) {
      return null;
    }
    return newInstance(localEntry, localAction, (Sidekick.Action)ProtoUtils.getProtoExtra(localBundle, "delete_action", Sidekick.Action.class));
  }
  
  public static RenamePlaceDialogFragment newInstance(Sidekick.Entry paramEntry, Sidekick.Action paramAction1, @Nullable Sidekick.Action paramAction2)
  {
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("entry_key", paramEntry.toByteArray());
    localBundle.putByteArray("rename_action_key", paramAction1.toByteArray());
    if (paramAction2 != null) {
      localBundle.putByteArray("delete_action_key", paramAction2.toByteArray());
    }
    RenamePlaceDialogFragment localRenamePlaceDialogFragment = new RenamePlaceDialogFragment();
    localRenamePlaceDialogFragment.setArguments(localBundle);
    return localRenamePlaceDialogFragment;
  }
  
  private void startEditPlaceTask(Sidekick.PlaceData paramPlaceData)
  {
    FragmentManager localFragmentManager = getFragmentManager();
    if (localFragmentManager == null) {}
    while (localFragmentManager.findFragmentByTag("editPlaceWorkerFragment") != null) {
      return;
    }
    RenamePlaceWorkerFragment localRenamePlaceWorkerFragment = RenamePlaceWorkerFragment.newInstance(this.mEntry, this.mRenameAction, paramPlaceData);
    localFragmentManager.beginTransaction().addToBackStack("editPlaceWorkerFragment").add(localRenamePlaceWorkerFragment, "editPlaceWorkerFragment").commit();
  }
  
  List<ListAdapterUnion> getAlternatePlaces()
  {
    Object localObject;
    if (!this.mEntry.hasFrequentPlaceEntry()) {
      localObject = Collections.emptyList();
    }
    for (;;)
    {
      return localObject;
      localObject = new ArrayList();
      if (this.mFrequentPlace.getAlternatePlaceDataCount() > 0)
      {
        Iterator localIterator = this.mFrequentPlace.getAlternatePlaceDataList().iterator();
        while (localIterator.hasNext())
        {
          Sidekick.PlaceData localPlaceData = (Sidekick.PlaceData)localIterator.next();
          if (!localPlaceData.getDisplayName().equals(this.mCurrentName)) {
            ((List)localObject).add(new ListAdapterUnion(localPlaceData, null));
          }
        }
      }
    }
  }
  
  void initFrequentPlace()
  {
    if ((this.mEntry.hasFrequentPlaceEntry()) && (this.mEntry.getFrequentPlaceEntry().hasFrequentPlace()))
    {
      this.mFrequentPlace = this.mEntry.getFrequentPlaceEntry().getFrequentPlace();
      if (this.mFrequentPlace.hasPlaceData())
      {
        this.mCurrentName = this.mFrequentPlace.getPlaceData().getDisplayName();
        return;
      }
      if (this.mFrequentPlace.hasLocation())
      {
        this.mCurrentName = this.mFrequentPlace.getLocation().getName();
        return;
      }
      this.mCurrentName = "";
      return;
    }
    this.mFrequentPlace = new Sidekick.FrequentPlace();
    this.mCurrentName = "";
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    Bundle localBundle = getArguments();
    this.mEntry = ProtoUtils.getEntryFromByteArray(localBundle.getByteArray("entry_key"));
    this.mRenameAction = ProtoUtils.getActionFromByteArray(localBundle.getByteArray("rename_action_key"));
    if (localBundle.containsKey("delete_action_key")) {
      this.mDeleteAction = ProtoUtils.getActionFromByteArray(localBundle.getByteArray("delete_action_key"));
    }
    initFrequentPlace();
    final List localList = getAlternatePlaces();
    RenameArrayAdapter localRenameArrayAdapter = new RenameArrayAdapter(getActivity(), localList);
    View localView = LayoutInflater.from(getActivity()).inflate(2130968812, null, false);
    ListView localListView = (ListView)localView.findViewById(2131296951);
    localListView.setAdapter(localRenameArrayAdapter);
    final EditText localEditText = (EditText)localView.findViewById(2131296299);
    localEditText.setText(PlaceUtils.getPlaceName(getActivity(), this.mFrequentPlace));
    localEditText.setInputType(8192);
    final FragmentLaunchingAlertDialog localFragmentLaunchingAlertDialog = new FragmentLaunchingAlertDialog(getActivity(), getFragmentManager(), 2131362570);
    localFragmentLaunchingAlertDialog.setView(localView);
    localFragmentLaunchingAlertDialog.setNeutralButton(17039360, new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RenamePlaceDialogFragment.this.hideSoftKeyboard(localEditText);
        localFragmentLaunchingAlertDialog.cancel();
      }
    });
    localFragmentLaunchingAlertDialog.setPositiveButton(17039370, new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RenamePlaceDialogFragment.this.hideSoftKeyboard(localEditText);
        Sidekick.PlaceData localPlaceData = new Sidekick.PlaceData().setDisplayName(localEditText.getText().toString());
        RenamePlaceDialogFragment.this.startEditPlaceTask(localPlaceData);
        localFragmentLaunchingAlertDialog.hide();
      }
    });
    if (this.mDeleteAction != null) {
      localFragmentLaunchingAlertDialog.setNegativeButton(2131362572, new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          RenamePlaceDialogFragment.this.hideSoftKeyboard(localEditText);
          DeletePlaceDialogFragment.newInstance(RenamePlaceDialogFragment.this.mEntry, RenamePlaceDialogFragment.this.mDeleteAction).show(RenamePlaceDialogFragment.this.getFragmentManager().beginTransaction().addToBackStack("delete_place_dialog"), "delete_place_dialog");
          localFragmentLaunchingAlertDialog.hide();
        }
      });
    }
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        RenamePlaceDialogFragment.ListAdapterUnion localListAdapterUnion = (RenamePlaceDialogFragment.ListAdapterUnion)localList.get(paramAnonymousInt);
        localEditText.setText(RenamePlaceDialogFragment.ListAdapterUnion.access$300(localListAdapterUnion).getDisplayName());
        RenamePlaceDialogFragment.this.startEditPlaceTask(RenamePlaceDialogFragment.ListAdapterUnion.access$300(localListAdapterUnion));
        RenamePlaceDialogFragment.this.hideSoftKeyboard(localEditText);
        localFragmentLaunchingAlertDialog.hide();
      }
    });
    localFragmentLaunchingAlertDialog.setOnShowListener(new DialogInterface.OnShowListener()
    {
      public void onShow(DialogInterface paramAnonymousDialogInterface)
      {
        localEditText.requestFocus();
        localEditText.setSelection(0, localEditText.getText().length());
      }
    });
    localFragmentLaunchingAlertDialog.setWindowSoftInputMode(5);
    return localFragmentLaunchingAlertDialog;
  }
  
  static class ListAdapterUnion
  {
    private final String label;
    private final Sidekick.PlaceData placeData;
    
    private ListAdapterUnion(Sidekick.PlaceData paramPlaceData)
    {
      this.placeData = paramPlaceData;
      this.label = null;
    }
    
    public String toString()
    {
      if (this.placeData != null) {
        return this.placeData.getDisplayName();
      }
      return this.label;
    }
  }
  
  class RenameArrayAdapter
    extends ArrayAdapter<RenamePlaceDialogFragment.ListAdapterUnion>
  {
    public RenameArrayAdapter(List<RenamePlaceDialogFragment.ListAdapterUnion> paramList)
    {
      super(17367049, localList);
    }
    
    public int getItemViewType(int paramInt)
    {
      return RenamePlaceDialogFragment.ViewFactory.access$500(((RenamePlaceDialogFragment.ListAdapterUnion)getItem(paramInt)).placeData);
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      return RenamePlaceDialogFragment.ViewFactory.access$600(getItemViewType(paramInt), getItem(paramInt), paramView, paramViewGroup, LayoutInflater.from(RenamePlaceDialogFragment.this.getActivity()));
    }
    
    public int getViewTypeCount()
    {
      return 3;
    }
  }
  
  private static class ViewFactory
  {
    private static final int[][] sResourceMap = { { 2130968809, 2131296947 }, { 2130968811, 2131296950 }, { 17367049, 0 } };
    
    private static View createViewFromResource(int paramInt, Object paramObject, View paramView, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater)
    {
      int i = sResourceMap[paramInt][0];
      int j = sResourceMap[paramInt][1];
      if (paramView == null) {}
      for (View localView = paramLayoutInflater.inflate(i, paramViewGroup, false);; localView = paramView)
      {
        if (paramObject != null)
        {
          if (j == 0) {}
          for (;;)
          {
            try
            {
              localTextView = (TextView)localView;
              localTextView.setText(paramObject.toString());
              return localView;
            }
            catch (ClassCastException localClassCastException)
            {
              TextView localTextView;
              throw new IllegalStateException("Expected TextView", localClassCastException);
            }
            localTextView = (TextView)localView.findViewById(j);
          }
        }
        return localView;
      }
    }
    
    private static int getItemViewType(Sidekick.PlaceData paramPlaceData)
    {
      if (paramPlaceData != null)
      {
        if (paramPlaceData.hasContactData()) {
          return 0;
        }
        if (paramPlaceData.hasBusinessData()) {
          return 1;
        }
      }
      return 2;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.RenamePlaceDialogFragment
 * JD-Core Version:    0.7.0.1
 */