package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import javax.annotation.Nullable;

public class AgendaCardView
        extends LinearLayout {
    private int mBottom;
    private View mHeader;
    private int mTop;

    public AgendaCardView(Context paramContext) {
        this(paramContext, null);
    }

    public AgendaCardView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void addAllItems(AgendaCardView paramAgendaCardView, int paramInt1, int paramInt2) {
        for (int i = paramInt2 - paramInt1; i > 0; i--) {
            AgendaExpandableItemView localAgendaExpandableItemView = (AgendaExpandableItemView) paramAgendaCardView.getChildAt(paramInt1);
            localAgendaExpandableItemView.setPressed(false);
            paramAgendaCardView.removeView(localAgendaExpandableItemView);
            addItem(localAgendaExpandableItemView);
        }
    }

    private boolean canSplitBefore(int paramInt) {
        return (paramInt > 0) && ((paramInt != 1) || (!hasHeader())) && (paramInt < getChildCount());
    }

    private AgendaExpandableItemView getItemAt(int paramInt) {
        return (AgendaExpandableItemView) getChildAt(paramInt + getItemIndexOffset());
    }

    private int getItemIndexOffset() {
        if (hasHeader()) {
            return 1;
        }
        return 0;
    }

    public static AgendaCardView inflate(Context paramContext) {
        return (AgendaCardView) View.inflate(paramContext, 2130968586, null);
    }

    @Nullable
    private AgendaCardView splitBefore(int paramInt) {
        if (!canSplitBefore(paramInt)) {
            return null;
        }
        AgendaCardView localAgendaCardView = inflate(getContext());
        localAgendaCardView.addAllItems(this, paramInt, getChildCount());
        return localAgendaCardView;
    }

    public void addAllItems(AgendaCardView paramAgendaCardView) {
        addAllItems(paramAgendaCardView, 0, paramAgendaCardView.getChildCount());
    }

    public void addItem(AgendaExpandableItemView paramAgendaExpandableItemView) {
        addView(paramAgendaExpandableItemView);
        paramAgendaExpandableItemView.setParentCard(this);
    }

    public void addItem(AgendaExpandableItemView paramAgendaExpandableItemView, int paramInt) {
        addView(paramAgendaExpandableItemView, paramInt + getItemIndexOffset());
        paramAgendaExpandableItemView.setParentCard(this);
    }

    public void collapseItem() {
        getItemAt(0).collapse();
        getItemAt(0).invalidate();
    }

    public int getItemCount() {
        return getChildCount() - getItemIndexOffset();
    }

    public int getPreComputedBottom() {
        return this.mBottom;
    }

    public int getPreComputedTop() {
        return this.mTop;
    }

    public boolean hasHeader() {
        return this.mHeader != null;
    }

    public boolean isExpanded() {
        return getItemAt(0).isExpanded();
    }

    public void setHeader(View paramView) {
        if (this.mHeader != null) {
            removeView(this.mHeader);
        }
        this.mHeader = paramView;
        addView(this.mHeader, 0);
    }

    public void setTopAndBottom(int paramInt1, int paramInt2) {
        this.mTop = paramInt1;
        this.mBottom = paramInt2;
    }

    @Nullable
    public AgendaCardView splitAfter(AgendaExpandableItemView paramAgendaExpandableItemView) {
        return splitBefore(1 + indexOfChild(paramAgendaExpandableItemView));
    }

    @Nullable
    public AgendaCardView splitBefore(AgendaExpandableItemView paramAgendaExpandableItemView) {
        return splitBefore(indexOfChild(paramAgendaExpandableItemView));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.AgendaCardView

 * JD-Core Version:    0.7.0.1

 */