package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.search.shared.ui.WebImageView;
import com.google.android.search.shared.ui.WebImageView.Listener;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.ui.ActionEditorView;
import com.google.android.voicesearch.ui.TravelModeSpinner;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LocalResultsCard
        extends AbstractCardView<LocalResultsController>
        implements LocalResultsController.Ui {
    private LayoutInflater mInflater;
    private final List<View> mLocalItems = new ArrayList();
    private ActionEditorView mMainContentView;
    private View mMapClickCatcher;
    private WebImageView mMapImage;
    private int mMarkerIndex = 0;
    private TravelModeSpinner mTravelModeSpinner;
    private ViewGroup mViewContainer;

    public LocalResultsCard(Context paramContext) {
        super(paramContext);
    }

    private View getActionTarget(View paramView) {
        return (View) Preconditions.checkNotNull(paramView.findViewById(2131296773));
    }

    private ImageView getIcon(View paramView) {
        return (ImageView) Preconditions.checkNotNull(paramView.findViewById(2131296777));
    }

    private TextView getMarker(View paramView) {
        return (TextView) Preconditions.checkNotNull(paramView.findViewById(2131296772));
    }

    public void addLocalResult(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, boolean paramBoolean, View.OnClickListener paramOnClickListener1, View.OnClickListener paramOnClickListener2) {
        String[] arrayOfString = getResources().getStringArray(2131492932);
        if (this.mMarkerIndex >= arrayOfString.length) {
            return;
        }
        View localView = this.mInflater.inflate(2130968741, this.mViewContainer, false);
        ((TextView) localView.findViewById(2131296774)).setText(paramString1);
        ((TextView) localView.findViewById(2131296775)).setText(paramString2);
        TextView localTextView1 = (TextView) localView.findViewById(2131296776);
        ImageView localImageView;
        if ((paramString3 == null) || (paramString3.isEmpty())) {
            localTextView1.setVisibility(8);
            TextView localTextView2 = getMarker(localView);
            localTextView2.setText(arrayOfString[this.mMarkerIndex]);
            localTextView2.setOnClickListener(paramOnClickListener1);
            localImageView = getIcon(localView);
            if (!paramBoolean) {
                break label224;
            }
        }
        label224:
        for (int i = 0; ; i = 8) {
            localImageView.setVisibility(i);
            if (paramBoolean) {
                localImageView.setImageResource(paramInt1);
                localImageView.setContentDescription(getResources().getString(paramInt2));
            }
            getActionTarget(localView).setOnClickListener(paramOnClickListener2);
            this.mViewContainer.addView(localView);
            this.mLocalItems.add(localView);
            this.mMarkerIndex = (1 + this.mMarkerIndex);
            return;
            localTextView1.setText(paramString3);
            break;
        }
    }

    public void addLocalResultDivider() {
        View localView = this.mInflater.inflate(2130968740, this.mViewContainer, false);
        this.mViewContainer.addView(localView);
        this.mLocalItems.add(localView);
    }

    public void handleDetach() {
        super.handleDetach();
        Iterator localIterator = this.mLocalItems.iterator();
        while (localIterator.hasNext()) {
            View localView = (View) localIterator.next();
            this.mViewContainer.removeView(localView);
        }
        this.mLocalItems.clear();
        this.mMarkerIndex = 0;
    }

    public void hideConfirmation() {
        this.mMainContentView.showCountDownView(false);
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        this.mMainContentView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968739);
        this.mMapImage = ((WebImageView) this.mMainContentView.findViewById(2131296768));
        this.mMapImage.setOnDownloadListener(new WebImageView.Listener() {
            public void onImageDownloaded(Drawable paramAnonymousDrawable) {
                ((LocalResultsController) LocalResultsCard.this.getController()).uiReady();
            }
        });
        this.mMapClickCatcher = this.mMainContentView.findViewById(2131296769);
        this.mViewContainer = ((ViewGroup) this.mMainContentView.findViewById(2131296767));
        this.mTravelModeSpinner = ((TravelModeSpinner) this.mMainContentView.findViewById(2131296770));
        this.mTravelModeSpinner.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                ((LocalResultsController) LocalResultsCard.this.getController()).cancelCountDownByUser();
                return false;
            }
        });
        this.mTravelModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                ((LocalResultsController) LocalResultsCard.this.getController()).setTransportationMethod(LocalResultsCard.this.mTravelModeSpinner.getSelectedTransportationMethod());
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
        this.mInflater = paramLayoutInflater;
        this.mMainContentView.setContentClickable(false);
        return this.mMainContentView;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
        paramAccessibilityNodeInfo.setClassName(LocalResultsCard.class.getCanonicalName());
    }

    public void setActionTypeAndTransportationMethod(int paramInt1, int paramInt2) {
        this.mTravelModeSpinner.setActionType(paramInt1, paramInt2);
    }

    public void setMapImageBitmap(Bitmap paramBitmap, View.OnClickListener paramOnClickListener) {
        this.mMapImage.setImageBitmap(paramBitmap);
        this.mMapClickCatcher.setOnClickListener(paramOnClickListener);
    }

    public void setMapImageUrl(String paramString, View.OnClickListener paramOnClickListener) {
        VelvetServices localVelvetServices = VelvetServices.get();
        Uri localUri = localVelvetServices.getCoreServices().getSearchUrlHelper().maybeMakeAbsoluteUri(paramString);
        this.mMapImage.setImageUri(localUri, localVelvetServices.getImageLoader());
        this.mMapClickCatcher.setOnClickListener(paramOnClickListener);
    }

    public void showConfirmation(int paramInt1, int paramInt2) {
        setConfirmText(paramInt2);
        setConfirmIcon(paramInt1);
        this.mMainContentView.showCountDownView(true);
    }

    public void showDisabled() {
        disableActionEditor(2131363676);
        this.mViewContainer.setAlpha(0.5F);
        this.mMapClickCatcher.setEnabled(false);
        this.mTravelModeSpinner.setEnabled(false);
        Iterator localIterator = this.mLocalItems.iterator();
        while (localIterator.hasNext()) {
            View localView = (View) localIterator.next();
            if (localView.getId() == 2131296771) {
                getActionTarget(localView).setEnabled(false);
                getMarker(localView).setEnabled(false);
                getIcon(localView).setColorFilter(-3355444);
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.LocalResultsCard

 * JD-Core Version:    0.7.0.1

 */