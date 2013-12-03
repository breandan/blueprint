package com.google.android.velvet.presenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.core.state.VelvetEventBus.Observer;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.ui.MainContentView;
import com.google.android.voicesearch.settings.Settings;

public class VoicesearchLanguagePresenter
        extends MainContentPresenter
        implements VelvetEventBus.Observer {
    private TextView mText;

    public VoicesearchLanguagePresenter(MainContentView paramMainContentView) {
        super("voicesearchlang", paramMainContentView);
    }

    private boolean shouldShow() {
        return !getEventBus().getDiscoveryState().shouldShowAll();
    }

    private void updateText() {
        Settings localSettings = VelvetServices.get().getVoiceSearchServices().getSettings();
        if ((shouldShow()) && (!localSettings.isDefaultSpokenLanguage())) {
            this.mText.setText(SpokenLanguageUtils.getDisplayName(localSettings.getConfiguration(), localSettings.getSpokenLocaleBcp47()));
            this.mText.setVisibility(0);
            return;
        }
        this.mText.setVisibility(8);
    }

    protected void onPostAttach(Bundle paramBundle) {
        View localView = getFactory().createVoicesearchLanguageView(this, getCardContainer());
        this.mText = ((TextView) localView.findViewById(2131297206));
        postAddViews(new View[]{localView});
        getEventBus().addObserver(this);
        postSetMainContentFrontScrimVisible(false);
    }

    protected void onPreDetach() {
        postSetMainContentFrontScrimVisible(true);
        postRemoveAllViews();
        getEventBus().removeObserver(this);
    }

    public void onStateChanged(VelvetEventBus.Event paramEvent) {
        if (paramEvent.hasDiscoveryChanged()) {
            updateText();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.VoicesearchLanguagePresenter

 * JD-Core Version:    0.7.0.1

 */