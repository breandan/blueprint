package com.google.android.velvet.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.shared.util.CancellableNowOrLater;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.UriLoader;
import com.google.android.shared.util.Util;
import com.google.android.velvet.Corpus;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.presenter.FooterPresenter;

import java.io.FileNotFoundException;

import javax.annotation.Nullable;

public class CorpusBar
        extends LinearLayout {
    private final View.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {
            if (paramAnonymousView == CorpusBar.this.mMoreCorporaSelector) {
                CorpusBar.this.moreCorporaClicked();
                return;
            }
            CorpusBar.this.mPresenter.onCorpusClicked((Corpus) paramAnonymousView.getTag());
        }
    };
    private Corpus mCurrentlySelectedCorpus;
    private UriLoader<Drawable> mImageLoader;
    private int mInitialMaxWebSelectors;
    private View mMoreCorporaSelector;
    private View mPendingScrollToView;
    private FooterPresenter mPresenter;
    private boolean mShowAllCorpora;
    private final Rect mTmpRect = new Rect();

    public CorpusBar(Context paramContext) {
        super(paramContext);
    }

    public CorpusBar(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public CorpusBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    private void moreCorporaClicked() {
        this.mShowAllCorpora = true;
        updateSelectorVisibility();
        if (this.mInitialMaxWebSelectors < getChildCount()) {
            getChildAt(this.mInitialMaxWebSelectors).requestFocus();
        }
    }

    private void scrollParentTo(View paramView) {
        if (getParent().isLayoutRequested()) {
            this.mPendingScrollToView = paramView;
            return;
        }
        this.mTmpRect.set(0, 0, paramView.getWidth(), paramView.getHeight());
        offsetDescendantRectToMyCoords(paramView, this.mTmpRect);
        int i = (((ViewGroup) getParent()).getWidth() - paramView.getWidth()) / 2;
        this.mTmpRect.left = Math.max(0, this.mTmpRect.left - i);
        this.mTmpRect.right = Math.min(getWidth(), i + this.mTmpRect.right);
        getParent().requestChildRectangleOnScreen(this, this.mTmpRect, false);
    }

    private void updateSelectorVisibility() {
        int i = 0;
        int j = 0;
        if (j < getChildCount()) {
            View localView = getChildAt(j);
            int k;
            if (localView == this.mMoreCorporaSelector) {
                if (this.mShowAllCorpora) {
                    k = 8;
                    label37:
                    localView.setVisibility(k);
                    i = 1;
                }
            }
            for (; ; ) {
                j++;
                break;
                k = 0;
                break label37;
                if ((!this.mShowAllCorpora) && (j >= this.mInitialMaxWebSelectors) && (i == 0)) {
                    localView.setVisibility(8);
                } else {
                    localView.setVisibility(0);
                }
            }
        }
    }

    public void addCorpusSelector(Corpus paramCorpus) {
        if (paramCorpus.getSelectorLayoutId() == 0) {
            return;
        }
        View localView = this.mPresenter.getFactory().createCorpusSelector(this.mPresenter, this, paramCorpus);
        localView.setTag(paramCorpus);
        final ImageView localImageView = (ImageView) localView.findViewById(2131296489);
        Uri localUri1 = paramCorpus.getIconUri();
        CancellableNowOrLater localCancellableNowOrLater;
        TextView localTextView;
        Uri localUri2;
        if ((localImageView != null) && (localUri1 != null)) {
            localCancellableNowOrLater = this.mImageLoader.load(localUri1);
            if (localCancellableNowOrLater.haveNow()) {
                localImageView.setImageDrawable((Drawable) localCancellableNowOrLater.getNow());
            }
        } else {
            localTextView = (TextView) localView.findViewById(2131296490);
            localUri2 = paramCorpus.getNameUri();
            if ((localTextView == null) || (localUri2 == null)) {
            }
        }
        for (; ; ) {
            try {
                Pair localPair = Util.getResourceId(getContext(), localUri2);
                localTextView.setText(((Resources) localPair.first).getString(((Integer) localPair.second).intValue()));
                localView.setOnClickListener(this.mClickListener);
                if (this.mMoreCorporaSelector != null) {
                    break label263;
                }
                addView(localView);
                updateSelectorVisibility();
                return;
            } catch (FileNotFoundException localFileNotFoundException) {
                Log.w("Velvet.CorpusBar", "Couldn't get name for corpus " + paramCorpus, localFileNotFoundException);
                return;
            } catch (Resources.NotFoundException localNotFoundException) {
                Log.w("Velvet.CorpusBar", "Couldn't get name for corpus " + paramCorpus, localNotFoundException);
                return;
            }
            localCancellableNowOrLater.getLater(new Consumer() {
                public boolean consume(Drawable paramAnonymousDrawable) {
                    localImageView.setImageDrawable(paramAnonymousDrawable);
                    return true;
                }
            });
            break;
            label263:
            addView(localView, indexOfChild(this.mMoreCorporaSelector));
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        VelvetServices localVelvetServices = VelvetServices.get();
        this.mImageLoader = localVelvetServices.getImageLoader();
        this.mInitialMaxWebSelectors = localVelvetServices.getCoreServices().getConfig().getMaxInitialWebCorpusSelectors();
        if (this.mInitialMaxWebSelectors == 0) {
        }
        for (boolean bool = true; ; bool = false) {
            this.mShowAllCorpora = bool;
            return;
        }
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        if (this.mPendingScrollToView != null) {
            final View localView = this.mPendingScrollToView;
            this.mPendingScrollToView = null;
            post(new Runnable() {
                public void run() {
                    if ((localView.getParent() == CorpusBar.this) && (CorpusBar.this.getParent() != null)) {
                        CorpusBar.this.scrollParentTo(localView);
                    }
                }
            });
        }
    }

    public void removeCorpusSelectors(Corpus paramCorpus) {
        int i = -1 + getChildCount();
        if (i >= 0) {
            View localView = getChildAt(i);
            if (localView == this.mMoreCorporaSelector) {
            }
            for (; ; ) {
                i--;
                break;
                Corpus localCorpus = (Corpus) localView.getTag();
                if ((paramCorpus.equals(localCorpus)) || (paramCorpus.equals(localCorpus.getParent()))) {
                    removeView(localView);
                }
            }
        }
    }

    public void resetSelectedCorpus() {
        if (this.mInitialMaxWebSelectors == 0) {
        }
        for (boolean bool = true; ; bool = false) {
            this.mShowAllCorpora = bool;
            updateSelectorVisibility();
            setSelectedCorpus(null);
            return;
        }
    }

    public void setPresenter(FooterPresenter paramFooterPresenter) {
        this.mPresenter = paramFooterPresenter;
        if (this.mInitialMaxWebSelectors > 0) {
            this.mMoreCorporaSelector = this.mPresenter.getFactory().createMoreCorporaSelector(this.mPresenter, this);
            this.mMoreCorporaSelector.setOnClickListener(this.mClickListener);
            addView(this.mMoreCorporaSelector);
        }
    }

    public void setSelectedCorpus(@Nullable Corpus paramCorpus) {
        if ((this.mCurrentlySelectedCorpus == null) || (!this.mCurrentlySelectedCorpus.equals(paramCorpus))) {
            if (this.mCurrentlySelectedCorpus != null) {
                View localView2 = findViewWithTag(this.mCurrentlySelectedCorpus);
                if (localView2 != null) {
                    localView2.setSelected(false);
                }
            }
            this.mCurrentlySelectedCorpus = paramCorpus;
            if (this.mCurrentlySelectedCorpus != null) {
                View localView1 = findViewWithTag(paramCorpus);
                if (localView1 != null) {
                    localView1.setSelected(true);
                    if (localView1.getVisibility() != 0) {
                        this.mShowAllCorpora = true;
                        updateSelectorVisibility();
                    }
                    scrollParentTo(localView1);
                }
            }
            return;
        }
        this.mCurrentlySelectedCorpus = paramCorpus;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.widget.CorpusBar

 * JD-Core Version:    0.7.0.1

 */