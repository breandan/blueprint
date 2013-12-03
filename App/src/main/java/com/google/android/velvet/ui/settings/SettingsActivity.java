package com.google.android.velvet.ui.settings;

import android.accounts.Account;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.HasOptOutSwitchHandler;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.OptOutSwitchHandler;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.search.core.util.LoggingIntentStarter;
import com.google.android.shared.util.ActivityIntentStarter;
import com.google.android.shared.util.IntentStarter;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.tg.FirstRunActivity;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Configuration;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SettingsActivity
        extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener, HasOptOutSwitchHandler {
    private Sidekick.SidekickConfiguration mBackingConfiguration;
    private HeaderAdapter mHeaderAdapter;
    private PreferenceActivity.Header mInitialHeader;
    private ActivityIntentStarter mIntentStarter;
    private NowOptInSettings mNowOptInSettings;
    private OptOutSwitchHandler mOptOutHandler;
    private GsaPreferenceController mPreferencesController;

    private Intent createOptInIntent(NowOptInSettings paramNowOptInSettings) {
        Intent localIntent = new Intent(getApplicationContext(), FirstRunActivity.class);
        if (paramNowOptInSettings.userHasSeenFirstRunScreens()) {
            localIntent.putExtra("single_page", true);
        }
        return localIntent;
    }

    private GsaPreferenceController getPreferencesController() {
        if (this.mPreferencesController == null) {
            this.mPreferencesController = VelvetServices.get().getPreferenceController();
        }
        return this.mPreferencesController;
    }

    private void registerSettingsChangeListener() {
        getPreferencesController().getStartupPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private boolean shouldRemoveNowHeader(PreferenceActivity.Header paramHeader, Account paramAccount, int paramInt, NowOptInSettings paramNowOptInSettings, PredictiveCardsPreferences paramPredictiveCardsPreferences) {
        int i;
        if (paramHeader.id == 2131297269L) {
            i = 1;
            if (paramAccount != null) {
                break label26;
            }
        }
        label26:
        label206:
        do {
            boolean bool;
            do {
                Sidekick.Configuration localConfiguration;
                do {
                    return true;
                    i = 0;
                    break;
                    if (paramInt == 0) {
                        if (paramHeader.id != 2131297275L) {
                            if (paramHeader.extras == null) {
                                paramHeader.extras = new Bundle();
                            }
                            paramHeader.extras.putBoolean("LOADING_KEY", true);
                            paramHeader.summary = getString(2131363187);
                        }
                        return false;
                    }
                    if (paramHeader.extras != null) {
                        paramHeader.extras.remove("LOADING_KEY");
                    }
                    if (paramInt == 1) {
                        break label206;
                    }
                    localConfiguration = paramNowOptInSettings.getSavedConfiguration(paramAccount);
                }
                while ((localConfiguration == null) || (paramNowOptInSettings.localeIsBlockedFromNow(localConfiguration)));
                bool = paramNowOptInSettings.domainIsBlockedFromNow(localConfiguration, paramAccount);
            } while ((i == 0) || (!bool));
            paramHeader.summary = Html.fromHtml(getString(2131362694, new Object[]{"http://support.google.com/websearch/answer/2938260"}));
            paramHeader.intent = null;
            paramHeader.fragment = null;
            paramHeader.extras = new Bundle();
            paramHeader.extras.putBoolean("notSelectableAndContainsLink", true);
            return false;
            if ((paramNowOptInSettings.isAccountOptedIn(paramAccount)) && (this.mBackingConfiguration != null)) {
                break label246;
            }
        } while (i == 0);
        paramHeader.intent = createOptInIntent(paramNowOptInSettings);
        paramHeader.fragment = null;
        for (; ; ) {
            return false;
            label246:
            if (!paramPredictiveCardsPreferences.isSavedConfigurationVersionCurrent()) {
                paramHeader.fragment = null;
            }
        }
    }

    public IntentStarter getIntentStarter() {
        return this.mIntentStarter;
    }

    public OptOutSwitchHandler getOptOutSwitchHandler() {
        return this.mOptOutHandler;
    }

    public SharedPreferences getSharedPreferences(String paramString, int paramInt) {
        VelvetServices localVelvetServices = VelvetServices.get();
        if ("sidekick".equals(paramString)) {
            return localVelvetServices.getCoreServices().getPredictiveCardsPreferences().getWorkingPreferences();
        }
        if (getPreferencesController().isMainPreferencesName(paramString, paramInt)) {
            return getPreferencesController().getMainPreferences();
        }
        return super.getSharedPreferences(paramString, paramInt);
    }

    protected boolean isValidFragment(String paramString) {
        return true;
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        this.mIntentStarter.onActivityResultDelegate(paramInt1, paramInt2, paramIntent);
    }

    public void onBuildHeaders(List<PreferenceActivity.Header> paramList) {
        CoreSearchServices localCoreSearchServices = VelvetServices.get().getCoreServices();
        localCoreSearchServices.getUserInteractionLogger().logView("SETTINGS");
        loadHeadersFromResource(2131099671, paramList);
        LoginHelper localLoginHelper = localCoreSearchServices.getLoginHelper();
        NowOptInSettings localNowOptInSettings = localCoreSearchServices.getNowOptInSettings();
        PredictiveCardsPreferences localPredictiveCardsPreferences = localCoreSearchServices.getPredictiveCardsPreferences();
        boolean bool = localCoreSearchServices.getConfig().isContentProviderGlobalSearchEnabled();
        if (!bool) {
            bool = localCoreSearchServices.getGooglePlayServicesHelper().isGooglePlayServicesAvailable();
        }
        this.mBackingConfiguration = ((NowConfigurationPreferences) getSharedPreferences("sidekick", 0)).getBackingConfiguration();
        Iterator localIterator = paramList.iterator();
        Account localAccount = localLoginHelper.getAccount();
        int i = localNowOptInSettings.canAccountRunNow(localAccount);
        if (i == 0) {
            registerSettingsChangeListener();
        }
        while (localIterator.hasNext()) {
            PreferenceActivity.Header localHeader = (PreferenceActivity.Header) localIterator.next();
            if (((localHeader.id == 2131297269L) || (localHeader.id == 2131297273L) || (localHeader.id == 2131297276L) || (localHeader.id == 2131297275L)) && (shouldRemoveNowHeader(localHeader, localAccount, i, localNowOptInSettings, localPredictiveCardsPreferences))) {
                localIterator.remove();
            } else if ((localHeader.id == 2131297278L) || (localHeader.id == 2131297279L) || (localHeader.id == 2131297277L)) {
                localIterator.remove();
            } else if (((localHeader.id == 2131297276L) || (localHeader.id == 2131297275L)) && (!DebugFeatures.getInstance().dogfoodDebugEnabled())) {
                localIterator.remove();
            } else if ((localHeader.id == 2131297271L) && (!bool)) {
                localIterator.remove();
            } else if ((localHeader.fragment != null) && (this.mInitialHeader == null) && (paramList.contains(localHeader))) {
                this.mInitialHeader = localHeader;
            }
        }
        if (this.mHeaderAdapter != null) {
            this.mHeaderAdapter.clear();
            this.mHeaderAdapter.addAll(paramList);
        }
    }

    public void onCreate(Bundle paramBundle) {
        CoreSearchServices localCoreSearchServices = VelvetServices.get().getCoreServices();
        DebugFeatures.setDebugLevel();
        this.mNowOptInSettings = localCoreSearchServices.getNowOptInSettings();
        this.mOptOutHandler = new OptOutSwitchHandler(this.mNowOptInSettings, localCoreSearchServices.getLoginHelper(), createOptInIntent(this.mNowOptInSettings), this, localCoreSearchServices.getUserInteractionLogger());
        this.mOptOutHandler.restoreInstanceState(paramBundle);
        super.onCreate(paramBundle);
        ActionBar localActionBar = getActionBar();
        if (localActionBar != null) {
            localActionBar.setDisplayOptions(4, 4);
        }
        this.mIntentStarter = new LoggingIntentStarter(this, 100);
        Greco3DataManager localGreco3DataManager = VelvetServices.get().getVoiceSearchServices().getGreco3Container().getGreco3DataManager();
        if (!localGreco3DataManager.isInitialized()) {
            localGreco3DataManager.initialize();
        }
    }

    public PreferenceActivity.Header onGetInitialHeader() {
        return this.mInitialHeader;
    }

    public boolean onIsMultiPane() {
        return getResources().getBoolean(2131755036);
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        if (paramMenuItem.getItemId() == 16908332) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(paramMenuItem);
    }

    public void onPause() {
        super.onPause();
        getPreferencesController().getStartupPreferences().unregisterOnSharedPreferenceChangeListener(this);
        VelvetServices.get().getSidekickInjector().getEntryProvider().refreshNowIfDelayedRefreshInFlight();
    }

    public void onResume() {
        super.onResume();
        if (this.mHeaderAdapter != null) {
            this.mHeaderAdapter.onResume();
        }
    }

    public void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        this.mOptOutHandler.saveInstanceState(paramBundle);
    }

    public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString) {
        if (this.mNowOptInSettings.isPreferenceKeyForAnOptInChange(paramString)) {
            invalidateHeaders();
            paramSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    public void setListAdapter(ListAdapter paramListAdapter) {
        if (paramListAdapter == null) {
            super.setListAdapter(null);
            return;
        }
        int i = paramListAdapter.getCount();
        ArrayList localArrayList = Lists.newArrayList();
        for (int j = 0; j < i; j++) {
            localArrayList.add((PreferenceActivity.Header) paramListAdapter.getItem(j));
        }
        Sidekick.SidekickConfiguration localSidekickConfiguration = this.mBackingConfiguration;
        Boolean localBoolean = null;
        if (localSidekickConfiguration == null) {
            localBoolean = Boolean.valueOf(false);
        }
        this.mHeaderAdapter = new HeaderAdapter(this, this.mOptOutHandler, localArrayList, localBoolean);
        super.setListAdapter(this.mHeaderAdapter);
    }

    private static class HeaderAdapter
            extends ArrayAdapter<PreferenceActivity.Header> {
        private final LayoutInflater mInflater;
        private final Boolean mOptInSwitchStateOverride;
        private final OptOutSwitchHandler mOptOutSwitchHandler;

        public HeaderAdapter(Context paramContext, OptOutSwitchHandler paramOptOutSwitchHandler, List<PreferenceActivity.Header> paramList, Boolean paramBoolean) {
            super(0, paramList);
            this.mInflater = ((LayoutInflater) paramContext.getSystemService("layout_inflater"));
            this.mOptOutSwitchHandler = paramOptOutSwitchHandler;
            this.mOptInSwitchStateOverride = paramBoolean;
        }

        private static int getHeaderType(PreferenceActivity.Header paramHeader) {
            if ((paramHeader.extras != null) && (paramHeader.extras.containsKey("LOADING_KEY"))) {
                return 3;
            }
            if (paramHeader.id == 2131297269L) {
                return 2;
            }
            if ((paramHeader.fragment == null) && (paramHeader.intent == null)) {
                return 0;
            }
            return 1;
        }

        private CharSequence getTitle(PreferenceActivity.Header paramHeader) {
            return Html.fromHtml(paramHeader.getTitle(getContext().getResources()).toString());
        }

        private void updateIcon(HeaderViewHolder paramHeaderViewHolder, PreferenceActivity.Header paramHeader) {
            int i = paramHeader.iconRes;
            int j = 0;
            if (i == 0) {
                Drawable localDrawable = paramHeaderViewHolder.icon.getDrawable();
                j = 0;
                if (localDrawable != null) {
                    j = 1;
                }
            }
            paramHeaderViewHolder.icon.setImageResource(paramHeader.iconRes);
            if (j != 0) {
                paramHeaderViewHolder.icon.requestLayout();
            }
        }

        public boolean areAllItemsEnabled() {
            return false;
        }

        public int getItemViewType(int paramInt) {
            return getHeaderType((PreferenceActivity.Header) getItem(paramInt));
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            PreferenceActivity.Header localHeader = (PreferenceActivity.Header) getItem(paramInt);
            int i = getHeaderType(localHeader);
            HeaderViewHolder localHeaderViewHolder;
            Object localObject;
            if (paramView == null) {
                localHeaderViewHolder = new HeaderViewHolder(null);
                localObject = null;
                switch (i) {
                    default:
                        label68:
                        ((View) localObject).setTag(localHeaderViewHolder);
                }
            }
            for (; ; ) {
                switch (i) {
                    default:
                        return localObject;
                    TextView localTextView = new TextView(getContext(), null, 16843272);
                    int j = localTextView.getPaddingLeft();
                    localTextView.setPadding(j, j * 2, j, j / 2);
                    localObject = localTextView;
                    localHeaderViewHolder.title = ((TextView) localObject);
                    break label68;
                    localObject = this.mInflater.inflate(2130968793, paramViewGroup, false);
                    localHeaderViewHolder.icon = ((ImageView) ((View) localObject).findViewById(2131296310));
                    localHeaderViewHolder.title = ((TextView) ((View) localObject).findViewById(16908310));
                    localHeaderViewHolder.summary = ((TextView) ((View) localObject).findViewById(16908304));
                    localHeaderViewHolder.switchWidget = ((Switch) ((View) localObject).findViewById(2131296896));
                    break label68;
                    localObject = this.mInflater.inflate(2130968791, paramViewGroup, false);
                    localHeaderViewHolder.icon = ((ImageView) ((View) localObject).findViewById(2131296310));
                    localHeaderViewHolder.title = ((TextView) ((View) localObject).findViewById(16908310));
                    localHeaderViewHolder.summary = ((TextView) ((View) localObject).findViewById(16908304));
                    break label68;
                    localObject = this.mInflater.inflate(2130968793, paramViewGroup, false);
                    localHeaderViewHolder.icon = ((ImageView) ((View) localObject).findViewById(2131296310));
                    localHeaderViewHolder.title = ((TextView) ((View) localObject).findViewById(16908310));
                    localHeaderViewHolder.summary = ((TextView) ((View) localObject).findViewById(16908304));
                    localHeaderViewHolder.switchWidget = ((Switch) ((View) localObject).findViewById(2131296896));
                    localHeaderViewHolder.loadingBar = ((ProgressBar) ((View) localObject).findViewById(2131296897));
                    break label68;
                    localObject = paramView;
                    localHeaderViewHolder = (HeaderViewHolder) ((View) localObject).getTag();
                }
            }
            localHeaderViewHolder.title.setText(getTitle(localHeader));
            return localObject;
            if ((localHeader.id == 2131297269L) && (!this.mOptOutSwitchHandler.hasSwitch())) {
                this.mOptOutSwitchHandler.setSwitch(localHeaderViewHolder.switchWidget, this.mOptInSwitchStateOverride);
            }
            localHeaderViewHolder.title.setText(getTitle(localHeader));
            updateIcon(localHeaderViewHolder, localHeader);
            CharSequence localCharSequence = localHeader.getSummary(getContext().getResources());
            if (!TextUtils.isEmpty(localCharSequence)) {
                localHeaderViewHolder.summary.setVisibility(0);
                localHeaderViewHolder.summary.setText(localCharSequence);
                if ((localHeader.extras != null) && (localHeader.extras.getBoolean("notSelectableAndContainsLink"))) {
                    localHeaderViewHolder.summary.setMovementMethod(LinkMovementMethod.getInstance());
                    localHeaderViewHolder.summary.setFocusable(true);
                }
            }
            while (localHeaderViewHolder.loadingBar != null) {
                localHeaderViewHolder.loadingBar.setVisibility(8);
                localHeaderViewHolder.loadingBar = null;
                return localObject;
                localHeaderViewHolder.summary.setMovementMethod(null);
                localHeaderViewHolder.summary.setFocusable(false);
                continue;
                localHeaderViewHolder.summary.setVisibility(8);
            }
            localHeaderViewHolder.title.setText(getTitle(localHeader));
            updateIcon(localHeaderViewHolder, localHeader);
            localHeaderViewHolder.summary.setVisibility(0);
            localHeaderViewHolder.summary.setText(localHeader.getSummary(getContext().getResources()));
            localHeaderViewHolder.summary.setMovementMethod(null);
            localHeaderViewHolder.summary.setFocusable(false);
            localHeaderViewHolder.switchWidget.setVisibility(8);
            localHeaderViewHolder.loadingBar.setVisibility(0);
            return localObject;
        }

        public int getViewTypeCount() {
            return 4;
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isEnabled(int paramInt) {
            int i = getHeaderType((PreferenceActivity.Header) getItem(paramInt));
            return (i != 0) && (i != 3) && (((PreferenceActivity.Header) getItem(paramInt)).id != 2131297269L);
        }

        public void onResume() {
            this.mOptOutSwitchHandler.onResume();
        }

        private static class HeaderViewHolder {
            ImageView icon;
            ProgressBar loadingBar;
            TextView summary;
            Switch switchWidget;
            TextView title;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.settings.SettingsActivity

 * JD-Core Version:    0.7.0.1

 */