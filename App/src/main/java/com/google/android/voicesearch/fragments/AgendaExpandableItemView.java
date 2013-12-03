package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import javax.annotation.Nullable;

public class AgendaExpandableItemView
        extends FrameLayout {
    private AgendaCardView mCardView;
    private View mCollapsedView;
    private boolean mExpanded = false;
    private View mExpandedView;

    public AgendaExpandableItemView(Context paramContext) {
        this(paramContext, null);
    }

    public AgendaExpandableItemView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
    }

    public boolean canExpand() {
        return (this.mExpandedView != null) && (!isExpanded());
    }

    public void collapse() {
        this.mExpanded = false;
        this.mCollapsedView.setVisibility(0);
        this.mExpandedView.setVisibility(8);
    }

    public void expand() {
        this.mExpanded = true;
        this.mCollapsedView.setVisibility(8);
        this.mExpandedView.setVisibility(0);
    }

    public AgendaCardView getParentCard() {
        return this.mCardView;
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    public void setCollapsedView(View paramView) {
        if (this.mCollapsedView != null) {
            removeView(this.mCollapsedView);
        }
        this.mCollapsedView = paramView;
        addView(this.mCollapsedView);
    }

    public void setExpandedView(@Nullable View paramView) {
        if (this.mExpandedView != null) {
            removeView(this.mExpandedView);
        }
        this.mExpandedView = paramView;
        if (this.mExpandedView != null) {
            this.mExpandedView.setBackground(null);
            this.mExpandedView.setVisibility(8);
            addView(this.mExpandedView);
        }
    }

    public void setParentCard(AgendaCardView paramAgendaCardView) {
        this.mCardView = paramAgendaCardView;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.AgendaExpandableItemView

 * JD-Core Version:    0.7.0.1

 */