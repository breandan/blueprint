package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.search.shared.ui.WebImageView;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.ui.ActionEditorView;
import com.google.android.voicesearch.ui.AppSelectorView;

import java.util.List;

public abstract class PlayMediaCard<T extends PlayMediaController<?>>
        extends AbstractCardView<T>
        implements PlayMediaController.Ui, AppSelectorView.OnAppSelectedListener {
    private ImageView mAppIconView;
    private LinearLayout mBuyInfoView;
    private ActionEditorView mContent;
    protected WebImageView mImageView;
    private LinearLayout mOwnedInfoView;
    private RatingBar mPlayRatingBarView;
    private TextView mPreviewView;
    private TextView mPriceView;
    private int mVoiceOfGoogleResId;

    public PlayMediaCard(Context paramContext) {
        super(paramContext);
    }

    public ActionEditorView createMediaActionEditor(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle, int paramInt1, int paramInt2) {
        this.mContent = createActionEditor(paramLayoutInflater, paramViewGroup, paramInt1);
        this.mContent.setBailOutEnabled(false);
        this.mContent.setContentClickable(false);
        this.mBuyInfoView = ((LinearLayout) this.mContent.findViewById(2131296865));
        this.mOwnedInfoView = ((LinearLayout) this.mContent.findViewById(2131296874));
        this.mImageView = ((WebImageView) this.mContent.findViewById(2131296869));
        this.mPriceView = ((TextView) this.mContent.findViewById(2131296867));
        this.mPlayRatingBarView = ((RatingBar) this.mContent.findViewById(2131296868));
        this.mPreviewView = ((TextView) this.mContent.findViewById(2131296872));
        if (this.mPreviewView != null) {
            this.mPreviewView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    ((PlayMediaController) PlayMediaCard.this.getController()).bailOut();
                }
            });
        }
        this.mVoiceOfGoogleResId = paramInt2;
        this.mAppIconView = ((ImageView) paramLayoutInflater.inflate(2130968782, this, false));
        return this.mContent;
    }

    public ViewGroup.LayoutParams getImageDimensions() {
        if (this.mImageView != null) {
            return this.mImageView.getLayoutParams();
        }
        return null;
    }

    public void onAppSelected(AppSelectionHelper.App paramApp) {
        ((PlayMediaController) getController()).appSelected(paramApp);
    }

    public void setAppLabel(int paramInt) {
        this.mContent.setConfirmText(paramInt);
    }

    public void setAppLabel(String paramString) {
        this.mContent.setConfirmText(paramString);
    }

    public void setPrice(String paramString1, String paramString2) {
        if (this.mPriceView != null) {
            if (!TextUtils.isEmpty(paramString1)) {
                break label31;
            }
            this.mPriceView.setText(paramString2);
        }
        for (; ; ) {
            this.mPriceView.setVisibility(0);
            return;
            label31:
            this.mPriceView.setText(getContext().getString(2131363454, new Object[]{paramString1, paramString2}));
        }
    }

    public void setSelectorApps(List<AppSelectionHelper.App> paramList, int paramInt, boolean paramBoolean) {
        if (paramList.size() == 1) {
            if (paramBoolean) {
                this.mAppIconView.setImageDrawable(getResources().getDrawable(2130837679));
                this.mContent.setConfirmIcon(this.mAppIconView);
            }
        }
        while (paramList.size() <= 1) {
            for (; ; ) {
                return;
                this.mAppIconView.setImageDrawable(getResources().getDrawable(2130837669));
            }
        }
        AppSelectorView localAppSelectorView = (AppSelectorView) ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(2130968786, this.mContent, false);
        this.mContent.setConfirmIcon(localAppSelectorView);
        localAppSelectorView.setOnAppSelectedListener(this);
        localAppSelectorView.setAppSelectorOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                ((PlayMediaController) PlayMediaCard.this.getController()).cancelCountDownByUser();
                return false;
            }
        });
        localAppSelectorView.setSelectorApps(paramList, paramInt);
    }

    public void showImageBitmap(Bitmap paramBitmap) {
        if (this.mImageView != null) {
            this.mImageView.setImageBitmap(paramBitmap);
            this.mImageView.setVisibility(0);
        }
    }

    public void showImageDrawable(Drawable paramDrawable) {
        if (this.mImageView != null) {
            this.mImageView.setImageDrawable(paramDrawable);
            this.mImageView.setVisibility(0);
        }
    }

    public void showImageUri(Uri paramUri) {
        if (this.mImageView != null) {
            this.mImageView.setImageUri(paramUri, VelvetServices.get().getImageLoader());
            this.mImageView.setVisibility(0);
        }
    }

    public void showOwnedMode(boolean paramBoolean) {
        int i = 8;
        int j;
        LinearLayout localLinearLayout1;
        if (this.mOwnedInfoView != null) {
            LinearLayout localLinearLayout2 = this.mOwnedInfoView;
            if (paramBoolean) {
                j = 0;
                localLinearLayout2.setVisibility(j);
            }
        } else if (this.mBuyInfoView != null) {
            localLinearLayout1 = this.mBuyInfoView;
            if (!paramBoolean) {
                break label58;
            }
        }
        for (; ; ) {
            localLinearLayout1.setVisibility(i);
            return;
            j = i;
            break;
            label58:
            i = 0;
        }
    }

    public void showPlayStoreRating(double paramDouble) {
        RatingBar localRatingBar;
        if (this.mPlayRatingBarView != null) {
            this.mPlayRatingBarView.setRating((float) paramDouble);
            localRatingBar = this.mPlayRatingBarView;
            if (paramDouble >= 0.0D) {
                break label38;
            }
        }
        label38:
        for (int i = 8; ; i = 0) {
            localRatingBar.setVisibility(i);
            return;
        }
    }

    public void showPreview(boolean paramBoolean) {
        TextView localTextView;
        if (this.mPreviewView != null) {
            localTextView = this.mPreviewView;
            if (!paramBoolean) {
                break label24;
            }
        }
        label24:
        for (int i = 0; ; i = 8) {
            localTextView.setVisibility(i);
            return;
        }
    }

    public void showVoiceOfGoogle() {
        if (this.mVoiceOfGoogleResId != 0) {
            this.mContent.showVoiceOfGoogleText(this.mVoiceOfGoogleResId, new Object[0]);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.PlayMediaCard

 * JD-Core Version:    0.7.0.1

 */