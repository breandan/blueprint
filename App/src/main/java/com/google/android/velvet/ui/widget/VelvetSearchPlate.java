package com.google.android.velvet.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.ui.SearchPlate;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.presenter.SearchPlatePresenter;
import com.google.android.velvet.presenter.VelvetSearchPlateUi;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class VelvetSearchPlate
        extends FrameLayout
        implements VelvetSearchPlateUi {
    private int mMode = 0;
    private int mRecognitionState;
    private SearchPlate mSearchPlate;
    private View mSoundSearchPromotedQuery;

    public VelvetSearchPlate(Context paramContext) {
        this(paramContext, null);
    }

    public VelvetSearchPlate(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public VelvetSearchPlate(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    private void showView(View paramView) {
        if ((paramView != null) && (paramView.getVisibility() != 0)) {
            paramView.setVisibility(0);
        }
    }

    public void dispatchDraw(Canvas paramCanvas) {
        EventLogger.recordOneOffBreakdownEvent(20);
        super.dispatchDraw(paramCanvas);
    }

    public void focusQueryAndShowKeyboard() {
        this.mSearchPlate.focusQueryAndShowKeyboard(false);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSearchPlate = ((SearchPlate) findViewById(2131296961));
        this.mSoundSearchPromotedQuery = this.mSearchPlate.findViewById(2131296973);
        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                return true;
            }
        });
        this.mSearchPlate.setSpeechLevelSource(VelvetServices.get().getVoiceSearchServices().getSpeechLevelSource());
    }

    public void restoreInstanceState(Bundle paramBundle) {
        Preconditions.checkNotNull(paramBundle);
        if (paramBundle.getBoolean("SearchPlateFragment.musicActionVisible")) {
            showView(this.mSoundSearchPromotedQuery);
        }
        if (paramBundle.containsKey("SearchPlateFragment.mode")) {
            setSearchPlateMode(paramBundle.getInt("SearchPlateFragment.mode"), 0, true);
        }
        if (paramBundle.containsKey("SearchPlateFragment.recognitionState")) {
            showRecognitionState(paramBundle.getInt("SearchPlateFragment.recognitionState"));
        }
    }

    public void saveInstanceState(Bundle paramBundle) {
        if (this.mMode != 0) {
            paramBundle.putInt("SearchPlateFragment.mode", this.mMode);
        }
        if (this.mRecognitionState != 0) {
            paramBundle.putInt("SearchPlateFragment.recognitionState", this.mRecognitionState);
        }
        if ((this.mSoundSearchPromotedQuery != null) && (this.mSoundSearchPromotedQuery.getVisibility() == 0)) {
        }
        for (boolean bool = true; ; bool = false) {
            paramBundle.putBoolean("SearchPlateFragment.musicActionVisible", bool);
            return;
        }
    }

    public void setExternalFlags(int paramInt, String paramString, boolean paramBoolean) {
        this.mSearchPlate.setExternalFlags(paramInt, paramString, paramBoolean);
    }

    public void setFinalRecognizedText(@Nonnull CharSequence paramCharSequence) {
        this.mSearchPlate.setFinalRecognizedText(paramCharSequence);
    }

    public void setPresenter(SearchPlatePresenter paramSearchPlatePresenter) {
        this.mSearchPlate.setCallback(paramSearchPlatePresenter.getSearchPlateCallback());
    }

    public void setQuery(Query paramQuery) {
        this.mSearchPlate.setQuery(paramQuery, false);
    }

    public void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean) {
        this.mMode = paramInt1;
        this.mSearchPlate.setMode(paramInt1, paramInt2, paramBoolean);
    }

    public void setTextQueryCorrections(Spanned paramSpanned) {
        this.mSearchPlate.setTextQueryCorrections(paramSpanned, false);
    }

    public void showErrorMessage(String paramString) {
        this.mSearchPlate.showErrorMessage(paramString);
    }

    public void showProgress(boolean paramBoolean) {
        this.mSearchPlate.showProgress(paramBoolean);
    }

    public void showRecognitionState(int paramInt) {
        this.mRecognitionState = paramInt;
        this.mSearchPlate.showRecognitionState(paramInt, false);
    }

    public void unfocusQueryAndHideKeyboard() {
        this.mSearchPlate.unfocusQueryAndHideKeyboard(false);
    }

    public void updateRecognizedText(String paramString1, String paramString2) {
        this.mSearchPlate.updateRecognizedText(paramString1, paramString2);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.widget.VelvetSearchPlate

 * JD-Core Version:    0.7.0.1

 */