package com.google.android.sidekick.main.actions;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.search.core.preferences.cards.MyPlacesSettingsFragment;
import com.google.android.shared.util.Util;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.fragments.reminders.PlacesApiFetcher;
import com.google.android.voicesearch.fragments.reminders.PlacesApiFetcher.PlaceDetailsTask;
import com.google.android.voicesearch.fragments.reminders.PlacesApiFetcher.PlaceSuggestion;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;

public class EditHomeWorkDialogFragment
  extends BaseEditDialogFragment
  implements TextWatcher, View.OnFocusChangeListener, SimpleCallback<PlacesApiFetcher.PlaceSuggestion>
{
  private Sidekick.Action mAction;
  private boolean mButtonsVisible;
  private FragmentLaunchingAlertDialog mDialog;
  private int mEmptyResultsMessageId;
  private Sidekick.Entry mEntry;
  private EditText mFilterEditText;
  private Sidekick.Location mLocation;
  private TextView mNoResultsMessageView;
  private EditText mPlaceNameField;
  private boolean mSelectAllOnFocus;
  private PlacesApiFetcher mSuggestionFetcher;
  private ListView mSuggestionListView;
  private View mView;
  
  private void addButtons()
  {
    this.mDialog.setPositiveButton(17039370, new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        EditHomeWorkDialogFragment.this.hideSoftKeyboard(EditHomeWorkDialogFragment.this.mFilterEditText);
        String str1 = EditHomeWorkDialogFragment.this.mFilterEditText.getText().toString();
        Sidekick.Location localLocation = new Sidekick.Location().setAddress(str1);
        if (!EditHomeWorkDialogFragment.this.mPlaceNameField.getText().toString().isEmpty()) {
          localLocation.setName(EditHomeWorkDialogFragment.this.mPlaceNameField.getText().toString());
        }
        EditHomeWorkDialogFragment.this.startServerAction(localLocation);
        int i = EditHomeWorkDialogFragment.this.mPlaceNameField.getVisibility();
        String str2 = null;
        if (i == 0) {
          str2 = EditHomeWorkDialogFragment.this.mPlaceNameField.getText().toString();
        }
        if ((EditHomeWorkDialogFragment.this.getTargetFragment() instanceof MyPlacesSettingsFragment)) {
          ((MyPlacesSettingsFragment)EditHomeWorkDialogFragment.this.getTargetFragment()).saveEntry(EditHomeWorkDialogFragment.this.mEntry, EditHomeWorkDialogFragment.this.mAction, str1, str2);
        }
      }
    });
    this.mDialog.setNegativeButton(17039360, new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        EditHomeWorkDialogFragment.this.hideSoftKeyboard(EditHomeWorkDialogFragment.this.mFilterEditText);
        EditHomeWorkDialogFragment.this.mDialog.cancel();
      }
    });
  }
  
  private static Sidekick.Location getFrequentPlaceLocation(Sidekick.Entry paramEntry)
  {
    if ((paramEntry.hasFrequentPlaceEntry()) && (paramEntry.getFrequentPlaceEntry().hasFrequentPlace()) && (paramEntry.getFrequentPlaceEntry().getFrequentPlace().hasLocation())) {
      return paramEntry.getFrequentPlaceEntry().getFrequentPlace().getLocation();
    }
    return null;
  }
  
  private void hideButtons()
  {
    this.mDialog.getButton(-1).setVisibility(8);
    this.mDialog.getButton(-2).setVisibility(8);
    this.mButtonsVisible = false;
  }
  
  public static EditHomeWorkDialogFragment newInstance(Context paramContext, Intent paramIntent)
  {
    Bundle localBundle = paramIntent.getExtras();
    Sidekick.Entry localEntry = (Sidekick.Entry)ProtoUtils.getProtoExtra(localBundle, "entry", Sidekick.Entry.class);
    Sidekick.Action localAction = (Sidekick.Action)ProtoUtils.getProtoExtra(localBundle, "action", Sidekick.Action.class);
    if ((localEntry == null) || (localAction == null)) {}
    do
    {
      return null;
      if (localAction.getType() == 17) {
        return newInstance(localEntry, localAction, 2131362528, null);
      }
    } while (localAction.getType() != 18);
    return newInstance(localEntry, localAction, 2131362530, PlaceUtils.getPlaceName(paramContext, localEntry.getFrequentPlaceEntry().getFrequentPlace()));
  }
  
  public static EditHomeWorkDialogFragment newInstance(Sidekick.Entry paramEntry, Sidekick.Action paramAction, int paramInt, String paramString)
  {
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("entry_key", paramEntry.toByteArray());
    localBundle.putByteArray("action_key", paramAction.toByteArray());
    localBundle.putInt("title_key", paramInt);
    if (paramString != null) {
      localBundle.putString("place_name_key", paramString);
    }
    EditHomeWorkDialogFragment localEditHomeWorkDialogFragment = new EditHomeWorkDialogFragment();
    localEditHomeWorkDialogFragment.setArguments(localBundle);
    return localEditHomeWorkDialogFragment;
  }
  
  private void showButtons()
  {
    this.mSuggestionListView.setVisibility(8);
    this.mNoResultsMessageView.setVisibility(8);
    this.mDialog.getButton(-1).setVisibility(0);
    this.mDialog.getButton(-2).setVisibility(0);
    this.mButtonsVisible = true;
  }
  
  private void showNoResultsMessageView(boolean paramBoolean)
  {
    int i;
    int j;
    if (this.mSuggestionListView.getEmptyView() != null)
    {
      i = 1;
      if (!this.mSuggestionFetcher.hasNetworkError()) {
        break label58;
      }
      j = 2131363518;
      label25:
      this.mNoResultsMessageView.setText(j);
      if ((!paramBoolean) || (i != 0)) {
        break label66;
      }
      this.mSuggestionListView.setEmptyView(this.mNoResultsMessageView);
    }
    label58:
    label66:
    while ((paramBoolean) || (i == 0))
    {
      return;
      i = 0;
      break;
      j = this.mEmptyResultsMessageId;
      break label25;
    }
    this.mNoResultsMessageView.setVisibility(8);
    this.mSuggestionListView.setEmptyView(null);
  }
  
  private void startServerAction(Sidekick.Location paramLocation)
  {
    FragmentManager localFragmentManager = getFragmentManager();
    if (localFragmentManager == null) {}
    while (localFragmentManager.findFragmentByTag("editPlaceWorkerFragment") != null) {
      return;
    }
    EditHomeWorkWorkerFragment localEditHomeWorkWorkerFragment = EditHomeWorkWorkerFragment.newInstance(this.mEntry, this.mAction, paramLocation, this.mLocation.getName(), this.mLocation.getAddress());
    localFragmentManager.beginTransaction().addToBackStack("editPlaceWorkerFragment").add(localEditHomeWorkWorkerFragment, "editPlaceWorkerFragment").commit();
  }
  
  public void afterTextChanged(Editable paramEditable)
  {
    String str = paramEditable.toString();
    if (TextUtils.isEmpty(str)) {
      showNoResultsMessageView(false);
    }
    if (this.mButtonsVisible) {
      hideButtons();
    }
    this.mSuggestionFetcher.startFetchingSuggestions(str);
  }
  
  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    Bundle localBundle = getArguments();
    this.mEntry = ProtoUtils.getEntryFromByteArray(localBundle.getByteArray("entry_key"));
    this.mAction = ProtoUtils.getActionFromByteArray(localBundle.getByteArray("action_key"));
    this.mLocation = getFrequentPlaceLocation(this.mEntry);
    int i = localBundle.getInt("title_key", 0);
    this.mSuggestionFetcher = new PlacesApiFetcher(getActivity());
    String str1 = localBundle.getString("place_name_key", null);
    this.mView = ((LayoutInflater)getActivity().getSystemService("layout_inflater")).inflate(2130968583, null);
    this.mDialog = new FragmentLaunchingAlertDialog(getActivity(), getFragmentManager(), i);
    this.mDialog.setView(this.mView);
    this.mFilterEditText = ((EditText)this.mView.findViewById(2131296300));
    this.mSuggestionListView = ((ListView)this.mView.findViewById(2131296301));
    this.mNoResultsMessageView = ((TextView)this.mView.findViewById(2131296302));
    this.mEmptyResultsMessageId = 2131363510;
    this.mFilterEditText.addTextChangedListener(this);
    String str2 = PlaceUtils.getAddress(this.mEntry);
    if (!TextUtils.isEmpty(str2)) {
      setFilter(str2);
    }
    this.mPlaceNameField = ((EditText)this.mView.findViewById(2131296299));
    if (str1 != null)
    {
      this.mPlaceNameField.setVisibility(0);
      this.mPlaceNameField.setText(str1);
    }
    for (;;)
    {
      addButtons();
      setAdapter(this.mSuggestionFetcher.getAdapter());
      this.mDialog.setOnShowListener(new DialogInterface.OnShowListener()
      {
        public void onShow(DialogInterface paramAnonymousDialogInterface)
        {
          EditHomeWorkDialogFragment.this.mFilterEditText.requestFocus();
          EditHomeWorkDialogFragment.this.mFilterEditText.setSelection(0, EditHomeWorkDialogFragment.this.mFilterEditText.getText().length());
          EditHomeWorkDialogFragment.this.hideButtons();
        }
      });
      this.mDialog.setWindowSoftInputMode(5);
      return this.mDialog;
      this.mPlaceNameField.setVisibility(8);
    }
  }
  
  public void onFocusChange(View paramView, boolean paramBoolean)
  {
    if (paramView.equals(this.mFilterEditText))
    {
      if (!paramBoolean) {
        break label53;
      }
      if (this.mSelectAllOnFocus)
      {
        this.mFilterEditText.selectAll();
        this.mSelectAllOnFocus = false;
      }
    }
    else
    {
      return;
    }
    this.mSuggestionFetcher.startFetchingSuggestions(this.mFilterEditText.getText().toString());
    return;
    label53:
    showButtons();
  }
  
  public void onResult(PlacesApiFetcher.PlaceSuggestion paramPlaceSuggestion)
  {
    new PlacesApiFetcher.PlaceDetailsTask(new SimpleCallback()
    {
      public void onResult(EcoutezStructuredResponse.EcoutezLocalResult paramAnonymousEcoutezLocalResult)
      {
        if (paramAnonymousEcoutezLocalResult == null)
        {
          Toast.makeText(EditHomeWorkDialogFragment.this.getActivity(), 2131363424, 0).show();
          return;
        }
        EditHomeWorkDialogFragment.this.mFilterEditText.removeTextChangedListener(EditHomeWorkDialogFragment.this);
        EditHomeWorkDialogFragment.this.mFilterEditText.setText(paramAnonymousEcoutezLocalResult.getAddress());
        EditHomeWorkDialogFragment.this.mFilterEditText.addTextChangedListener(EditHomeWorkDialogFragment.this);
        EditHomeWorkDialogFragment.this.showButtons();
      }
    }, paramPlaceSuggestion, VelvetServices.get().getSidekickInjector().getNetworkClient()).execute(new Void[0]);
  }
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void setAdapter(BaseAdapter paramBaseAdapter)
  {
    paramBaseAdapter.registerDataSetObserver(new DataSetObserver()
    {
      public void onChanged()
      {
        EditHomeWorkDialogFragment localEditHomeWorkDialogFragment = EditHomeWorkDialogFragment.this;
        if (!TextUtils.isEmpty(EditHomeWorkDialogFragment.this.mFilterEditText.getText().toString())) {}
        for (boolean bool = true;; bool = false)
        {
          localEditHomeWorkDialogFragment.showNoResultsMessageView(bool);
          return;
        }
      }
    });
    this.mSuggestionListView.setAdapter(paramBaseAdapter);
    this.mSuggestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        EditHomeWorkDialogFragment.this.onResult((PlacesApiFetcher.PlaceSuggestion)paramAnonymousAdapterView.getAdapter().getItem(paramAnonymousInt));
        Util.hideSoftKeyboard(EditHomeWorkDialogFragment.this.getActivity(), EditHomeWorkDialogFragment.this.mFilterEditText);
      }
    });
    String str = this.mFilterEditText.getText().toString();
    this.mSuggestionFetcher.startFetchingSuggestions(str);
  }
  
  public void setFilter(String paramString)
  {
    this.mFilterEditText.setText(paramString);
    this.mFilterEditText.setSelectAllOnFocus(true);
    this.mFilterEditText.setOnFocusChangeListener(this);
    this.mSelectAllOnFocus = true;
    if (!TextUtils.isEmpty(paramString))
    {
      this.mFilterEditText.setSelection(paramString.length());
      this.mFilterEditText.selectAll();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.EditHomeWorkDialogFragment
 * JD-Core Version:    0.7.0.1
 */