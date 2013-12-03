package com.google.android.voicesearch.fragments.reminders;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.search.core.ui.SuggestionDialog.SuggestionFetcher;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.Chain;
import com.google.geo.sidekick.Sidekick.ChainId;
import com.google.geo.sidekick.Sidekick.GeostoreFeatureId;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.PlaceAutocompleteQuery;
import com.google.geo.sidekick.Sidekick.PlaceDetailsQuery;
import com.google.geo.sidekick.Sidekick.PlaceReference;
import com.google.geo.sidekick.Sidekick.PlaceSuggestion;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

public class PlacesApiFetcher
        implements SuggestionDialog.SuggestionFetcher {
    private static final String TAG = Tag.getTag(PlacesApiFetcher.class);
    private final PlacesListAdapter mAdapter;
    private final Context mContext;
    private volatile boolean mHasNetworkError;
    private final NetworkClient mNetworkClient;

    public PlacesApiFetcher(Context paramContext) {
        this.mNetworkClient = VelvetServices.get().getSidekickInjector().getNetworkClient();
        this.mContext = paramContext;
        this.mAdapter = new PlacesListAdapter(paramContext);
    }

    PlacesApiFetcher(NetworkClient paramNetworkClient, PlacesListAdapter paramPlacesListAdapter, Context paramContext) {
        this.mNetworkClient = paramNetworkClient;
        this.mAdapter = paramPlacesListAdapter;
        this.mContext = paramContext;
    }

    private void updateSuggestions(@Nullable List<PlaceSuggestion> paramList) {
        if (paramList == null) {
        }
        for (boolean bool = true; ; bool = false) {
            this.mHasNetworkError = bool;
            this.mAdapter.clear();
            if (!this.mHasNetworkError) {
                this.mAdapter.addAll(paramList);
            }
            return;
        }
    }

    public PlacesListAdapter getAdapter() {
        return this.mAdapter;
    }

    public boolean hasNetworkError() {
        return this.mHasNetworkError;
    }

    public void startFetchingSuggestions(String paramString) {
        new FetchPlaceSuggestions(paramString).execute(new Void[0]);
    }

    class FetchPlaceSuggestions
            extends AsyncTask<Void, Void, List<PlacesApiFetcher.PlaceSuggestion>> {
        private final String mPlaceString;

        FetchPlaceSuggestions(String paramString) {
            this.mPlaceString = paramString;
        }

        private String getChainHash(Sidekick.ChainId paramChainId) {
            StringBuilder localStringBuilder = new StringBuilder();
            if (paramChainId.hasFeatureId()) {
                Sidekick.GeostoreFeatureId localGeostoreFeatureId = paramChainId.getFeatureId();
                if (localGeostoreFeatureId.hasCellId()) {
                    localStringBuilder.append(localGeostoreFeatureId.getCellId());
                }
                if (localGeostoreFeatureId.hasFprint()) {
                    localStringBuilder.append(localGeostoreFeatureId.getFprint());
                }
            }
            return localStringBuilder.toString();
        }

        protected List<PlacesApiFetcher.PlaceSuggestion> doInBackground(Void... paramVarArgs) {
            Sidekick.ResponsePayload localResponsePayload = PlacesApiFetcher.this.mNetworkClient.sendRequestWithLocation(new Sidekick.RequestPayload().setPlaceAutocompleteQuery(new Sidekick.PlaceAutocompleteQuery().setQuery(this.mPlaceString)));
            if (localResponsePayload == null) {
                Log.e(PlacesApiFetcher.TAG, "Error fetching place suggestions");
                return null;
            }
            return getSuggestionsWithChainData(localResponsePayload.getPlaceAutocompleteResponse().getSuggestionList());
        }

        List<PlacesApiFetcher.PlaceSuggestion> getSuggestionsWithChainData(List<Sidekick.PlaceSuggestion> paramList) {
            HashSet localHashSet = Sets.newHashSet();
            ArrayList localArrayList = Lists.newArrayList();
            Iterator localIterator = paramList.iterator();
            if (localIterator.hasNext()) {
                Sidekick.PlaceSuggestion localPlaceSuggestion = (Sidekick.PlaceSuggestion) localIterator.next();
                String str1;
                if ((localPlaceSuggestion.hasChain()) && (localPlaceSuggestion.getChain().hasChainId())) {
                    str1 = getChainHash(localPlaceSuggestion.getChain().getChainId());
                    if (!localHashSet.contains(str1)) {
                        if (!localPlaceSuggestion.getChain().hasDisplayName()) {
                            break label179;
                        }
                    }
                }
                label179:
                for (String str2 = localPlaceSuggestion.getChain().getDisplayName(); ; str2 = localPlaceSuggestion.getDescription()) {
                    localArrayList.add(new PlacesApiFetcher.PlaceSuggestion(str2, PlacesApiFetcher.this.mContext.getString(2131363135), null, localPlaceSuggestion.getChain()));
                    localHashSet.add(str1);
                    localArrayList.add(new PlacesApiFetcher.PlaceSuggestion(localPlaceSuggestion.getDescription(), localPlaceSuggestion.getSubDescription(), localPlaceSuggestion.getReference(), null));
                    break;
                }
            }
            return localArrayList;
        }

        protected void onPostExecute(List<PlacesApiFetcher.PlaceSuggestion> paramList) {
            PlacesApiFetcher.this.updateSuggestions(paramList);
        }
    }

    public static class PlaceDetails {
        private final String mAddress;
        private final String mDescription;
        private final Pair<Double, Double> mLatLong;
        private final String mName;

        PlaceDetails(String paramString1, String paramString2, String paramString3, @Nullable Pair<Double, Double> paramPair) {
            this.mName = paramString1;
            this.mAddress = paramString2;
            this.mDescription = paramString3;
            this.mLatLong = paramPair;
        }

        String getAddress() {
            return this.mAddress;
        }

        String getName() {
            return this.mName;
        }

        public EcoutezStructuredResponse.EcoutezLocalResult toEcoutezLocalResult() {
            EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult = new EcoutezStructuredResponse.EcoutezLocalResult();
            String str1 = getName();
            String str2 = getAddress();
            if (!str1.equals(str2)) {
                localEcoutezLocalResult.setTitle(str1);
            }
            localEcoutezLocalResult.setAddress(str2);
            if (this.mLatLong != null) {
                localEcoutezLocalResult.setLatDegrees(((Double) this.mLatLong.first).doubleValue());
                localEcoutezLocalResult.setLngDegrees(((Double) this.mLatLong.second).doubleValue());
            }
            return localEcoutezLocalResult;
        }
    }

    public static final class PlaceDetailsTask
            extends AsyncTask<Void, Void, PlacesApiFetcher.PlaceDetails> {
        private final SimpleCallback<EcoutezStructuredResponse.EcoutezLocalResult> mCallback;
        private final NetworkClient mNetworkClient;
        private final PlacesApiFetcher.PlaceSuggestion mPlaceSuggestion;

        public PlaceDetailsTask(SimpleCallback<EcoutezStructuredResponse.EcoutezLocalResult> paramSimpleCallback, PlacesApiFetcher.PlaceSuggestion paramPlaceSuggestion, NetworkClient paramNetworkClient) {
            this.mCallback = paramSimpleCallback;
            this.mPlaceSuggestion = paramPlaceSuggestion;
            this.mNetworkClient = paramNetworkClient;
        }

        protected PlacesApiFetcher.PlaceDetails doInBackground(Void... paramVarArgs) {
            Sidekick.ResponsePayload localResponsePayload = this.mNetworkClient.sendRequestWithLocation(new Sidekick.RequestPayload().setPlaceDetailsQuery(new Sidekick.PlaceDetailsQuery().setReference(this.mPlaceSuggestion.getPlaceReference())));
            if ((localResponsePayload == null) || (!localResponsePayload.hasPlaceDetailsResponse()) || (!localResponsePayload.getPlaceDetailsResponse().hasLocation())) {
                Log.e(PlacesApiFetcher.TAG, "Error fetching place details");
                return null;
            }
            Sidekick.Location localLocation = localResponsePayload.getPlaceDetailsResponse().getLocation();
            return new PlacesApiFetcher.PlaceDetails(localLocation.getName(), localLocation.getAddress(), this.mPlaceSuggestion.getDescription(), Pair.create(Double.valueOf(localLocation.getLat()), Double.valueOf(localLocation.getLng())));
        }

        protected void onPostExecute(PlacesApiFetcher.PlaceDetails paramPlaceDetails) {
            SimpleCallback localSimpleCallback = this.mCallback;
            if (paramPlaceDetails == null) {
            }
            for (Object localObject = null; ; localObject = paramPlaceDetails.toEcoutezLocalResult()) {
                localSimpleCallback.onResult(localObject);
                return;
            }
        }
    }

    public static class PlaceSuggestion {
        private final Sidekick.Chain mChainData;
        private final String mDescription;
        private final Sidekick.PlaceReference mPlaceReference;
        private final String mSubDescription;

        PlaceSuggestion(String paramString1, @Nullable String paramString2, Sidekick.PlaceReference paramPlaceReference, @Nullable Sidekick.Chain paramChain) {
            this.mDescription = paramString1;
            this.mSubDescription = paramString2;
            this.mPlaceReference = paramPlaceReference;
            this.mChainData = paramChain;
        }

        @Nullable
        Sidekick.Chain getChainData() {
            return this.mChainData;
        }

        public String getDescription() {
            return this.mDescription;
        }

        Sidekick.PlaceReference getPlaceReference() {
            return this.mPlaceReference;
        }

        @Nullable
        public String getSubDescription() {
            return this.mSubDescription;
        }

        public String toString() {
            StringBuilder localStringBuilder = new StringBuilder("{ description: ").append(this.mDescription).append(" subDescription: ").append(this.mSubDescription).append(" reference: ").append(this.mPlaceReference);
            if (this.mChainData != null) {
                localStringBuilder.append(" chainData: {").append(" displayName: " + this.mChainData.getDisplayName()).append(" entityId: " + this.mChainData.getChainId().getProminentEntityId()).append(" }");
            }
            localStringBuilder.append(" }");
            return localStringBuilder.toString();
        }
    }

    static final class PlacesListAdapter
            extends ArrayAdapter<PlacesApiFetcher.PlaceSuggestion> {
        PlacesListAdapter(Context paramContext) {
            super(2130968833, 16908310, Lists.newArrayList());
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            View localView = super.getView(paramInt, paramView, paramViewGroup);
            PlacesApiFetcher.PlaceSuggestion localPlaceSuggestion = (PlacesApiFetcher.PlaceSuggestion) getItem(paramInt);
            ((TextView) localView.findViewById(16908310)).setText(localPlaceSuggestion.getDescription());
            String str = localPlaceSuggestion.getSubDescription();
            if (!TextUtils.isEmpty(str)) {
                TextView localTextView = (TextView) localView.findViewById(2131296309);
                localTextView.setText(str);
                localTextView.setVisibility(0);
            }
            return localView;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.reminders.PlacesApiFetcher

 * JD-Core Version:    0.7.0.1

 */