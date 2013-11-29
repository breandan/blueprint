package com.google.android.search.core.preferences.cards;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.search.core.preferences.NowTrainingQuestionsFetcherFragment;
import com.google.android.search.core.util.FetchMyPlacesTask;
import com.google.android.sidekick.main.actions.EditHomeWorkDialogFragment;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.training.TrainingClosetActivity;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Objects;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import javax.annotation.Nullable;

public class MyPlacesSettingsFragment
  extends AbstractMyStuffSettingsFragment
  implements Preference.OnPreferenceClickListener, SimpleCallback<Pair<Sidekick.Entry, Sidekick.Entry>>
{
  private FetchMyPlacesTask mFetchLocationsTask;
  private Sidekick.Entry mHomeEntry;
  private Preference mHomePreference;
  private ProgressBar mProgressBar;
  private Sidekick.Entry mWorkEntry;
  private Preference mWorkPreference;
  
  private String getEntryName(Sidekick.Entry paramEntry, int paramInt)
  {
    String str = PlaceUtils.getLocationName(paramEntry);
    if (TextUtils.isEmpty(str)) {
      str = getString(paramInt);
    }
    return str;
  }
  
  @Nullable
  private final Sidekick.Action getRenameOrEditAction(Sidekick.Entry paramEntry)
  {
    return ProtoUtils.findAction(paramEntry, 17, new int[] { 18 });
  }
  
  private void maybeUpdateClosetPlaceCount()
  {
    NowTrainingQuestionsFetcherFragment localNowTrainingQuestionsFetcherFragment = (NowTrainingQuestionsFetcherFragment)getFragmentManager().findFragmentByTag("TRAINING_CLOSET_FETCHER");
    if (localNowTrainingQuestionsFetcherFragment != null)
    {
      Sidekick.Entry localEntry = this.mHomeEntry;
      int i = 0;
      if (localEntry != null)
      {
        boolean bool = TextUtils.isEmpty(PlaceUtils.getAddress(this.mHomeEntry));
        i = 0;
        if (!bool) {
          i = 0 + 1;
        }
      }
      if ((this.mWorkEntry != null) && (!TextUtils.isEmpty(PlaceUtils.getAddress(this.mWorkEntry)))) {
        i++;
      }
      localNowTrainingQuestionsFetcherFragment.setNumPlaces(i);
    }
  }
  
  private void showEditDialog(int paramInt, Sidekick.Entry paramEntry, String paramString)
  {
    EditHomeWorkDialogFragment localEditHomeWorkDialogFragment = EditHomeWorkDialogFragment.newInstance(paramEntry, getRenameOrEditAction(paramEntry), paramInt, paramString);
    localEditHomeWorkDialogFragment.setTargetFragment(this, 0);
    localEditHomeWorkDialogFragment.show(getActivity().getFragmentManager(), "address_picker_tag");
  }
  
  private void updatePreference(Preference paramPreference, String paramString, Sidekick.Entry paramEntry)
  {
    paramPreference.setTitle(paramString);
    String str = PlaceUtils.getAddress(paramEntry);
    if (!TextUtils.isEmpty(str)) {
      paramPreference.setSummary(str);
    }
    for (;;)
    {
      if (!TextUtils.isEmpty(paramString)) {
        paramPreference.setTitle(paramString);
      }
      return;
      paramPreference.setSummary(getString(2131362532));
    }
  }
  
  protected int getPreferenceResourceId()
  {
    return 2131099664;
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if ((getView() instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)getView();
      this.mProgressBar = new ProgressBar(getActivity());
      this.mProgressBar.setIndeterminate(true);
      localViewGroup.addView(this.mProgressBar, 0);
    }
    View localView = getView().findViewById(16908298);
    if (localView != null)
    {
      int i = (int)getResources().getDimension(2131689839);
      localView.setPadding(i, 0, i, 0);
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mFetchLocationsTask = new FetchMyPlacesTask(VelvetServices.get().getSidekickInjector().getNetworkClient(), this);
    this.mFetchLocationsTask.execute(new Void[0]);
  }
  
  public void onDestroy()
  {
    if (this.mFetchLocationsTask != null) {
      this.mFetchLocationsTask.cancel(false);
    }
    super.onDestroy();
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    if (paramPreference == this.mHomePreference)
    {
      showEditDialog(2131362528, this.mHomeEntry, null);
      return true;
    }
    if (paramPreference == this.mWorkPreference)
    {
      showEditDialog(2131362530, this.mWorkEntry, getEntryName(this.mWorkEntry, 2131362214));
      return true;
    }
    return false;
  }
  
  public void onResult(Pair<Sidekick.Entry, Sidekick.Entry> paramPair)
  {
    this.mFetchLocationsTask = null;
    if (this.mProgressBar != null)
    {
      ((ViewGroup)getView()).removeView(this.mProgressBar);
      this.mProgressBar = null;
    }
    if (paramPair == null) {
      Toast.makeText(getActivity(), "Error getting locations", 0).show();
    }
    do
    {
      return;
      this.mHomeEntry = ((Sidekick.Entry)paramPair.first);
      this.mWorkEntry = ((Sidekick.Entry)paramPair.second);
      if (this.mHomeEntry != null)
      {
        this.mHomePreference = new Preference(getActivity());
        updatePreference(this.mHomePreference, getEntryName(this.mHomeEntry, 2131361860), this.mHomeEntry);
        getPreferenceScreen().addPreference(this.mHomePreference);
        this.mHomePreference.setOnPreferenceClickListener(this);
      }
    } while (this.mWorkEntry == null);
    this.mWorkPreference = new Preference(getActivity());
    updatePreference(this.mWorkPreference, getEntryName(this.mWorkEntry, 2131362214), this.mWorkEntry);
    getPreferenceScreen().addPreference(this.mWorkPreference);
    this.mWorkPreference.setOnPreferenceClickListener(this);
  }
  
  public void onResume()
  {
    super.onResume();
    if ((getActivity() instanceof TrainingClosetActivity))
    {
      String str = getString(2131362829);
      ((TrainingClosetActivity)getActivity()).showHeader(str, null);
    }
  }
  
  public void saveEntry(Sidekick.Entry paramEntry, Sidekick.Action paramAction, String paramString1, String paramString2)
  {
    String str1 = PlaceUtils.getAddress(paramEntry);
    String str2 = PlaceUtils.getPlaceName(getActivity(), paramEntry.getFrequentPlaceEntry().getFrequentPlace());
    if (((!TextUtils.isEmpty(paramString1)) && (!Objects.equal(str1, paramString1))) || ((!TextUtils.isEmpty(paramString2)) && (!Objects.equal(str2, paramString2))))
    {
      new Sidekick.Location().setAddress(paramString1);
      if ((paramAction.getType() != 17) || (this.mHomePreference == null)) {
        break label148;
      }
      this.mHomeEntry.getFrequentPlaceEntry().getFrequentPlace().getLocation().setAddress(str1);
      this.mHomeEntry.getFrequentPlaceEntry().getFrequentPlace().getLocation().setAddress(paramString1);
      updatePreference(this.mHomePreference, getEntryName(this.mHomeEntry, 2131361860), this.mHomeEntry);
    }
    for (;;)
    {
      maybeUpdateClosetPlaceCount();
      return;
      label148:
      if ((paramAction.getType() == 18) && (this.mWorkPreference != null))
      {
        this.mWorkEntry.getFrequentPlaceEntry().getFrequentPlace().getLocation().setAddress(paramString1).setName(paramString2);
        updatePreference(this.mWorkPreference, paramString2, this.mWorkEntry);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.MyPlacesSettingsFragment
 * JD-Core Version:    0.7.0.1
 */