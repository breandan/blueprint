package com.google.android.velvet.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.tg.FirstRunActivity;

public class GetGoogleNowView
        extends LinearLayout {
    public GetGoogleNowView(Context paramContext) {
        super(paramContext);
    }

    public GetGoogleNowView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public GetGoogleNowView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    private void dismissGetGoogleNowButton(NowOptInSettings paramNowOptInSettings) {
        paramNowOptInSettings.setGetGoogleNowButtonDismissed();
        setVisibility(8);
    }

    private void getGoogleNow(NowOptInSettings paramNowOptInSettings) {
        Context localContext = getContext();
        Intent localIntent = new Intent(localContext, FirstRunActivity.class);
        if (paramNowOptInSettings.userHasSeenFirstRunScreens()) {
            localIntent.putExtra("skip_to_end", true);
        }
        localContext.startActivity(localIntent);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        CoreSearchServices localCoreSearchServices = VelvetServices.get().getCoreServices();
        final NowOptInSettings localNowOptInSettings = localCoreSearchServices.getNowOptInSettings();
        final UserInteractionLogger localUserInteractionLogger = localCoreSearchServices.getUserInteractionLogger();
        localUserInteractionLogger.logView("GET_GOOGLE_NOW_BUTTON");
        if (localNowOptInSettings.userHasDismissedGetGoogleNowButton()) {
            setVisibility(8);
            return;
        }
        ((Button) findViewById(2131296668)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localUserInteractionLogger.logAnalyticsAction("BUTTON_PRESS", "GET_GOOGLE_NOW_BUTTON_ACCEPT");
                GetGoogleNowView.this.getGoogleNow(localNowOptInSettings);
            }
        });
        ((ImageButton) findViewById(2131296669)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localUserInteractionLogger.logAnalyticsAction("BUTTON_PRESS", "GET_GOOGLE_NOW_BUTTON_DISMISS");
                GetGoogleNowView.this.dismissGetGoogleNowButton(localNowOptInSettings);
            }
        });
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.GetGoogleNowView

 * JD-Core Version:    0.7.0.1

 */