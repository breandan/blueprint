package com.google.android.voicesearch.fragments;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.velvet.presenter.MainContentUi;
import com.google.android.voicesearch.util.AgendaTimeUtil;

public class AgendaCardContainerView
        extends LinearLayout {
    private AbstractCardView<?> mAbstractCardView;
    private int mBatchSize = 10;
    private boolean mCanShowMore = false;
    private final int mCollapsedCardSpacingPx;
    private AgendaCardView mCurrentCard;
    private final int mExpandedCardSpacingPx;
    private boolean mHasExpandedCard = false;
    private boolean mUseReverseOrder = false;

    public AgendaCardContainerView(Context paramContext) {
        this(paramContext, null);
    }

    public AgendaCardContainerView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        setOrientation(1);
        LayoutTransition localLayoutTransition = new LayoutTransition();
        localLayoutTransition.enableTransitionType(4);
        setLayoutTransition(localLayoutTransition);
        this.mCollapsedCardSpacingPx = getResources().getDimensionPixelSize(2131689595);
        this.mExpandedCardSpacingPx = (2 * this.mCollapsedCardSpacingPx);
    }

    private AgendaCardView createAgendaCardView(int paramInt) {
        AgendaCardView localAgendaCardView = AgendaCardView.inflate(getContext());
        localAgendaCardView.setHeader(createDateHeaderView(paramInt));
        localAgendaCardView.setTag(Integer.valueOf(paramInt));
        localAgendaCardView.setVisibility(8);
        return localAgendaCardView;
    }

    private View createDateHeaderView(int paramInt) {
        View localView = View.inflate(getContext(), 2130968588, null);
        ((TextView) localView.findViewById(2131296311)).setText(AgendaTimeUtil.formatDateFromJulianDay(getContext(), paramInt));
        return localView;
    }

    private AgendaCardView getOrCreateCardForDay(int paramInt) {
        AgendaCardView localAgendaCardView2;
        if ((this.mCurrentCard != null) && (paramInt == ((Integer) this.mCurrentCard.getTag()).intValue())) {
            localAgendaCardView2 = this.mCurrentCard;
            return localAgendaCardView2;
        }
        for (int i = 0; ; i++) {
            if (i < getChildCount()) {
                localAgendaCardView2 = (AgendaCardView) getChildAt(i);
                int j = ((Integer) localAgendaCardView2.getTag()).intValue();
                if (paramInt == j) {
                    break;
                }
                if (((this.mUseReverseOrder) || (paramInt >= j)) && ((!this.mUseReverseOrder) || (paramInt <= j))) {
                    continue;
                }
            }
            AgendaCardView localAgendaCardView1 = createAgendaCardView(paramInt);
            addView(localAgendaCardView1, i);
            return localAgendaCardView1;
        }
    }

    public void addItem(AgendaExpandableItemView paramAgendaExpandableItemView, int paramInt) {
        this.mCurrentCard = getOrCreateCardForDay(paramInt);
        if (this.mUseReverseOrder) {
            this.mCurrentCard.addItem(paramAgendaExpandableItemView, 0);
        }
        for (; ; ) {
            this.mCanShowMore = true;
            return;
            this.mCurrentCard.addItem(paramAgendaExpandableItemView);
        }
    }

    public boolean collapseAll() {
        if (!this.mHasExpandedCard) {
            return false;
        }
        this.mAbstractCardView.post(new MainContentPresenter.Transaction() {
            public void commit(MainContentUi paramAnonymousMainContentUi) {
                Object localObject = null;
                int i = 0;
                if (i < AgendaCardContainerView.this.getChildCount()) {
                    AgendaCardView localAgendaCardView = (AgendaCardView) AgendaCardContainerView.this.getChildAt(i);
                    if (localAgendaCardView.isExpanded()) {
                        localAgendaCardView.collapseItem();
                    }
                    if (localAgendaCardView.hasHeader()) {
                        localObject = localAgendaCardView;
                    }
                    for (; ; ) {
                        i++;
                        break;
                        if (localObject != null) {
                            localObject.addAllItems(localAgendaCardView);
                            AgendaCardContainerView.this.removeView(localAgendaCardView);
                            i--;
                        }
                    }
                }
                AgendaCardContainerView.access$002(AgendaCardContainerView.this, false);
                if (paramAnonymousMainContentUi != null) {
                    paramAnonymousMainContentUi.getScrollViewControl().smoothScrollToY(0);
                }
            }
        });
        return true;
    }

    public boolean expand(final AgendaExpandableItemView paramAgendaExpandableItemView) {
        if (!paramAgendaExpandableItemView.canExpand()) {
            return false;
        }
        this.mAbstractCardView.post(new MainContentPresenter.Transaction() {
            public void commit(MainContentUi paramAnonymousMainContentUi) {
                AgendaCardView localAgendaCardView1 = paramAgendaExpandableItemView.getParentCard();
                int i = AgendaCardContainerView.this.indexOfChild(localAgendaCardView1);
                AgendaCardView localAgendaCardView2 = localAgendaCardView1.splitAfter(paramAgendaExpandableItemView);
                if (localAgendaCardView2 != null) {
                    AgendaCardContainerView.this.addView(localAgendaCardView2, i + 1);
                }
                AgendaCardView localAgendaCardView3 = localAgendaCardView1.splitBefore(paramAgendaExpandableItemView);
                if (localAgendaCardView3 != null) {
                    AgendaCardContainerView.this.addView(localAgendaCardView3, i + 1);
                }
                for (; ; ) {
                    paramAgendaExpandableItemView.expand();
                    AgendaCardContainerView.access$002(AgendaCardContainerView.this, true);
                    return;
                }
            }
        });
        return true;
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        for (int i = 0; i < getChildCount(); i++) {
            AgendaCardView localAgendaCardView = (AgendaCardView) getChildAt(i);
            localAgendaCardView.layout(paramInt1, paramInt2 + localAgendaCardView.getPreComputedTop(), paramInt3, paramInt2 + localAgendaCardView.getPreComputedBottom());
        }
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        super.onMeasure(paramInt1, paramInt2);
        int i = 0;
        int j = 0;
        Object localObject = null;
        int k = 0;
        while (k < getChildCount()) {
            AgendaCardView localAgendaCardView = (AgendaCardView) getChildAt(k);
            if (localAgendaCardView.getVisibility() == 8) {
                k++;
            } else {
                if (localObject != null) {
                    if ((!localAgendaCardView.isExpanded()) && (!localObject.isExpanded())) {
                        break label112;
                    }
                }
                label112:
                for (int n = this.mExpandedCardSpacingPx; ; n = this.mCollapsedCardSpacingPx) {
                    i += n;
                    j = i + localAgendaCardView.getMeasuredHeight();
                    localAgendaCardView.setTopAndBottom(i, j);
                    i = j;
                    localObject = localAgendaCardView;
                    break;
                }
            }
        }
        if ((this.mCanShowMore) && (localObject != null)) {
            if (!localObject.isExpanded()) {
                break label165;
            }
        }
        label165:
        for (int m = this.mExpandedCardSpacingPx; ; m = this.mCollapsedCardSpacingPx) {
            j += m;
            setMeasuredDimension(getMeasuredWidth(), j);
            return;
        }
    }

    public void setAbstractCardView(AbstractCardView<?> paramAbstractCardView) {
        this.mAbstractCardView = paramAbstractCardView;
    }

    public void setBatchSize(int paramInt) {
        if (paramInt > 0) {
            this.mBatchSize = paramInt;
        }
    }

    public void setUseReverseOrder(boolean paramBoolean) {
        this.mUseReverseOrder = paramBoolean;
    }

    public boolean showMoreItems() {
        int i = this.mBatchSize;
        int j = getChildCount();
        this.mCanShowMore = false;
        for (int k = 0; ; k++) {
            AgendaCardView localAgendaCardView;
            if (k < j) {
                localAgendaCardView = (AgendaCardView) getChildAt(k);
                if (localAgendaCardView.getVisibility() != 8) {
                    continue;
                }
                localAgendaCardView.setVisibility(0);
                if (k == j - 1) {
                    break label81;
                }
            }
            label81:
            for (boolean bool = true; ; bool = false) {
                this.mCanShowMore = bool;
                i -= localAgendaCardView.getItemCount();
                if (i > 0) {
                    break;
                }
                return this.mCanShowMore;
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.AgendaCardContainerView

 * JD-Core Version:    0.7.0.1

 */